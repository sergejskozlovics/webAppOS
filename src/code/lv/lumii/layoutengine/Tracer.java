/*
*
* Copyright (c) 2013-2015 Institute of Mathematics and Computer Science, University of Latvia (IMCS UL). 
*
* This file is part of layoutengine
*
* You can redistribute it and/or modify it under the terms of the GNU General Public License 
* as published by the Free Software Foundation, either version 2 of the License,
* or (at your option) any later version.
*
* This file is also subject to the "Classpath" exception as mentioned in
* the COPYING file that accompanied this code.
*
* You should have received a copy of the GNU General Public License along with layoutengine. If not, see http://www.gnu.org/licenses/.
*
*/

package lv.lumii.layoutengine;

import java.awt.geom.Point2D;
//import java.io.FileNotFoundException;
//import java.io.PrintWriter;
import java.util.*;
import lv.lumii.layoutengine.Box.BoxSide;
import lv.lumii.layoutengine.OrthogonalSegment.BoxSegment;
import lv.lumii.layoutengine.OrthogonalSegment.RectangleSegment;
import lv.lumii.layoutengine.obstacleGraph.Segment.VerticalPart;
import lv.lumii.layoutengine.util.Pair;

/**
 * Contains the methods for line tracing.
 *
 * @author Evgeny
 */
class Tracer {


    /**
     * The value to multiply the corner count term in PathNode comparator.
     */
    double CORNER_WEIGHT = 0.621;
    /**
     * The value by which to multiply the A* heuristic, used for bounded relaxation.
     */
    double RELAXATION_MULTIPLIER = 2.3;
    /**
     * A map from point-nodes to a memoized set of its neighbors in the current trace graph.
     */
    HashMap<PointNode, ArrayList<PointNode>> memPointNodes;
    /**
     * The set of horizontal TraceLines in the current graph.
     */
    Traceline horizontalTraceLines[];
    /**
     * The set of vertical TraceLines in the current graph.
     */
    Traceline verticalTraceLines[];
    /**
     * Horizontal rectangle segments used in trace graph construction.
     */
    ArrayList<OrthogonalSegment.RectangleSegment> horizontalSegments;
    /**
     * Vertical rectangle segments used in trace graph construction.
     */
    ArrayList<OrthogonalSegment.RectangleSegment> verticalSegments;
    /**
     * The epsilon of the diagram this tracer traces in. Used to denote minimum traceline window
     * size.
     */
    double epsilon;

    /**
     * Creates a new tracer with a fixed set of rectangles counted as obstacles based on the
     * parameters given.
     *
     * @param firstParent the first common parent of the boxes connected by the lines to be drawn
     * with this tracer
     * @param secondParent the second common parent of the boxes connected by the lines to be drawn
     * with this tracer
     * @param lca the LCA of the boxes connected by the lines to be drawn with this tracer
     */
    public Tracer(Box firstParent, Box secondParent, Box lca) {
        epsilon = lca.getDiagram().getEpsilon();

        LinkedHashSet<AbstractContainer> obstacles = LineOptimizer.getObstacleRectangles(firstParent, secondParent, lca, true);
        horizontalSegments = LineOptimizer.findSpacedSegments(obstacles, true, 1. / 4);
        verticalSegments = LineOptimizer.findSpacedSegments(obstacles, false, 1. / 4);
        generateGraph();
    }

    /**
     * Creates a new tracer with only the two given boxes counted as obstacles.
     *
     * @param start the first of the two boxes to in include in this tracer
     * @param end the second of the two boxes to in include in this tracer
     */
    public Tracer(Box start, Box end) {
        RELAXATION_MULTIPLIER = 1;
        CORNER_WEIGHT = 10;
        epsilon = start.getDiagram().getEpsilon();
        double spacing = Math.max(start.getSpacing(), end.getSpacing());
        Box boundingBox = new Box(
                Math.min(start.left, end.left) - spacing,
                Math.max(start.right, end.right) + spacing,
                Math.min(start.top, end.top) - spacing,
                Math.max(start.bottom, end.bottom) + spacing,
                start.getDiagram(), spacing);
        ArrayList<Box> boxes = new ArrayList<>();
        boxes.add(start);
        boxes.add(end);
        boxes.add(boundingBox);
        horizontalSegments = LineOptimizer.findSpacedSegments(boxes, true, 1e-9);
        verticalSegments = LineOptimizer.findSpacedSegments(boxes, false, 1e-9);
        generateGraph();
    }

    /**
     * Generates this Tracer's TraceLines and (re-)initializes graph storage.
     */
    private void generateGraph() {
        ArrayList<Traceline> traceLines = findTraceLines(horizontalSegments);
        horizontalTraceLines = traceLines.toArray(new Traceline[traceLines.size()]);
        Arrays.sort(horizontalTraceLines);
        for (int i = 0; i < horizontalTraceLines.length; i++) {
            horizontalTraceLines[i].index = i;
        }

        traceLines = findTraceLines(verticalSegments);
        verticalTraceLines = traceLines.toArray(new Traceline[traceLines.size()]);
        Arrays.sort(verticalTraceLines);
        for (int i = 0; i < verticalTraceLines.length; i++) {
            verticalTraceLines[i].index = i;
        }

        memPointNodes = new HashMap<>();
    }

    /**
     * A line along which diagram lines can be traced. Variable names are based on the assumption
     * that the line is horizontal.
     */
    static class Traceline implements Comparable<Traceline> {

        /**
         * The RectangleSegment bounding the line from the left/above.
         */
        final RectangleSegment leftSegment;
        /**
         * The RectangleSegment bounding the line from the right/below.
         */
        final RectangleSegment rightSegment;
        /**
         * The ordinate of the line, or the abscissa if the line is vertical.
         */
        final double y;
        /**
         * This TraceLine's index in the ordered TraceLine array.
         */
        int index;

        /**
         * Creates a new TraceLine object.
         *
         * @param left the RectangleSegment bounding the line from the left/above
         * @param right the RectangleSegment bounding the line from the right/below
         * @param y the ordinate of the line, or the abscissa if the line is vertical
         */
        Traceline(RectangleSegment left, RectangleSegment right, double y) {
            this.leftSegment = left;
            this.rightSegment = right;
            this.y = y;
        }

        /**
         * Compares this TraceLine to another.
         *
         * @param t the TraceLine to compare to
         * @return {@code Double.compare(y, t.y)} if they are different, the result of comparison
         * between the coordinates of the left/upper-most point otherwise. (-1 if this TraceLine is
         * smaller, 1 if larger and 0 if they are equal.)
         */
        @Override
        public int compareTo(Traceline t) {
            if (y == t.y) {
                return Double.compare(leftSegment.getPos(), t.leftSegment.getPos());
            } else {
                return Double.compare(y, t.y);
            }
        }

        /**
         * Returns whether two TraceLines are equal. They are considered equal if both their
         * RectangleSegments are equal and so is their ordinate/abscissa, for horizontal/vertical
         * TraceLines respectively.
         *
         * @param o the TraceLine to compare with
         * @return true if equal, false otherwise.
         */
        @Override
        public boolean equals(Object o) {
            if (o instanceof Traceline) {
                Traceline t = (Traceline) o;
                return y == t.y && leftSegment == t.leftSegment && rightSegment == t.rightSegment;
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 47 * hash + Objects.hashCode(leftSegment);
            hash = 47 * hash + Objects.hashCode(rightSegment);
            hash = 47 * hash + (int) (Double.doubleToLongBits(y) ^ (Double.doubleToLongBits(y) >>> 32));
            return hash;
        }

        /**
         * Returns the left/upper-most coordinate of the line.
         *
         * @return the left/upper-most coordinate of the line.
         */
        double getLeft() {
            return leftSegment.getPos();
        }

        /**
         * Returns the right/lower-most coordinate of the line.
         *
         * @return the right/lower-most coordinate of the line.
         */
        double getRight() {
            return rightSegment.getPos();
        }
    }

    /**
     * A class for storing the potential points of a traced line.
     */
    static class PointNode {

        /**
         * The abscissa of the point.
         */
        final double x;
        /**
         * The ordinate of the point.
         */
        final double y;
        /**
         * Stores whether this point was entered horizontally.
         */
        final boolean horizontalDirection;
        /**
         * The horizontal TraceLine along which the point resides, can be {@code null}.
         */
        final Traceline horizontalLine;
        /**
         * The vertical TraceLine along which the point resides, can be {@code null}.
         */
        final Traceline verticalLine;

        /**
         * creates a new PointNode object.
         *
         * @param x the abscissa of the point
         * @param y the ordinate of the point
         * @param horizontalDirection whether this PointNode was entered horizontally
         * @param horizontalLine the horizontal TraceLine along which the point resides, can be
         * {@code null}
         * @param verticalLine the vertical TraceLine along which the point resides, can be
         * {@code null}
         */
        PointNode(double x, double y, boolean horizontalDirection, Traceline horizontalLine, Traceline verticalLine) {
            this.x = x;
            this.y = y;
            this.horizontalDirection = horizontalDirection;
            this.horizontalLine = horizontalLine;
            this.verticalLine = verticalLine;
        }

        /**
         * Returns whether two PointNodes are considered equal. They are considered equal if just
         * their coordinates are equal.
         *
         * @param o the PointNode to compare with
         * @return true if equal, false otherwise.
         */
        @Override
        public boolean equals(Object o) {
            if (o instanceof PointNode) {
                PointNode p = (PointNode) o;
                return x == p.x && y == p.y && horizontalDirection == p.horizontalDirection;
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 59 * hash + (int) (Double.doubleToLongBits(this.x) ^ (Double.doubleToLongBits(this.x) >>> 32));
            hash = 59 * hash + (int) (Double.doubleToLongBits(this.y) ^ (Double.doubleToLongBits(this.y) >>> 32));
            hash = 59 * hash + (this.horizontalDirection ? 1 : 0);
            return hash;
        }

        @Override
        public String toString() {
            return "(" + x + "," + y + ")";
        }
    }

    /**
     * A class for storing a node in a potential path along which to trace aline.
     */
    static class PathNode implements Comparable<PathNode> {

        /**
         * The point corresponding to this node, along with its heuristic and the {@link Traceline}s
         * it resides on.
         */
        final PointNode pointNode;
        /**
         * The length of the path up to this node.
         */
        final double distance;
        /**
         * The Manhattan distance between the start and end boxes.
         */
        final double boxDistance;
        /**
         * The number of corners in the path up to this node.
         */
        final int cornerCount;
        /**
         * The previous node in the path, {@code null} for the start node.
         */
        final PathNode prev;
        /**
         * A heuristic of the nodes distance to the destination box.
         */
        double h;

        /**
         * Creates a new PathNode.
         *
         * @param pointNode the PointNode corresponding to this node,
         * @param distance the length of the path up to this node
         * @param prev the previous node in the path, {@code null} for the start node
         * @param cornerCount the number of corners in the path up to this node
         * @param boxDistance the Manhattan distance between the start and end boxes
         * @param heuristic a heuristic of the nodes distance to the destination box
         */
        PathNode(PointNode pointNode, double distance, PathNode prev,
                int cornerCount, double boxDistance, double heuristic) {
            this.pointNode = pointNode;
            this.distance = distance;
            this.prev = prev;
            this.cornerCount = cornerCount;
            this.boxDistance = boxDistance;
            this.h = heuristic;
        }

        /**
         * Compares this PathNode to another. Compares using a heuristic, taking into account
         * distance traveled to reach it, distance from it to the destination and the number of
         * corners in the path to it.
         *
         * @param p the PathNode to compare to
         * @return -1 if this PathNode is smaller, 1 if larger and 0 if both are equal.
         */
        @Override
        public int compareTo(PathNode p) {
            return Double.compare(distance + boxDistance * cornerCount + h,
                    p.distance + p.boxDistance * p.cornerCount + p.h);
        }
    }



    /**
     * Traces an orthogonal line between two boxes and returns it as a array of points. The line
     * will not overlap rectangles it shouldn't, but will not maintain spacing until normalization.
     *
     * @param a the start box
     * @param b the end box
     * @param startSides the mask denoting the allowed start box sides for this line
     * @param endSides the mask denoting the allowed end box sides for this line
     * @return the list of points defining the line
     */
    ArrayList<Point2D.Double> trace(Box a, Box b, int startSides, int endSides) {
        if (a == b) {
            return connectOneBox(a, startSides, endSides, a.getSpacing() / 4);
        }

        double distance = GeometryHelper.findManhattanDistance(a, b);
        double size = Math.max(Math.max(a.getWidth(), b.getHeight()), Math.max(b.getWidth(), b.getHeight()));
        double relaxationMultiplier = distance < size ? 1 : RELAXATION_MULTIPLIER;

        boolean swap = false;
        if ((a.getCenter().getX() > b.getCenter().getX()
                || (a.getCenter().getX() == b.getCenter().getX() && a.getCenter().getY() > b.getCenter().getY()))) {
            Box t = a;
            a = b;
            b = t;
            swap = true;

            int tmp = startSides;
            startSides = endSides;
            endSides = tmp;
        }

        double boxDistance = CORNER_WEIGHT * (Math.abs(a.getCenterX() - b.getCenterX()) + Math.abs(a.getCenterY() - b.getCenterY()));

        /*
         * The priority queue for the pathfinding algorithm.
         */
        PriorityQueue<PathNode> queue = new PriorityQueue<>();
        for (Traceline traceline : horizontalTraceLines) {
            if (traceline.leftSegment.getContainer() == a || traceline.rightSegment.getContainer() == a) {
                boolean left = traceline.leftSegment.getContainer() == a;
                if (left) {
                    if ((startSides & 0b0010) == 0) {
                        continue;
                    }
                } else {
                    if ((startSides & 0b1000) == 0) {
                        continue;
                    }
                }
                double x = left ? traceline.getLeft() : traceline.getRight(),
                        y = traceline.y;
                /*
                 * The segments used are extended somewhat to not draw lines too close to the rectangles.
                 * However, a point is only a valid start point if it actually touches the start
                 * box.
                 */
                if (GeometryHelper.findManhattanDistance(x, y, a) == 0) {
                    queue.add(new PathNode(
                            new PointNode(x, y, true, traceline, null),
                            0, null, 0, boxDistance, GeometryHelper.findManhattanDistance(x, y, b) * relaxationMultiplier));
                }
            }
        }
        for (Traceline traceline : verticalTraceLines) {
            if (traceline.leftSegment.getContainer() == a || traceline.rightSegment.getContainer() == a) {
                boolean top = traceline.leftSegment.getContainer() == a;
                if (top) {
                    if ((startSides & 0b0100) == 0) {
                        continue;
                    }
                } else {
                    if ((startSides & 0b0001) == 0) {
                        continue;
                    }
                }
                double x = traceline.y,
                        y = top ? traceline.getLeft() : traceline.getRight();
                /*
                 * The segments used are extended somewhat to not draw lines too close to the rectangles.
                 * However, a point is only a valid start point if it actually touches the start
                 * box.
                 */
                if (GeometryHelper.findManhattanDistance(x, y, a) == 0) {
                    queue.add(new PathNode(
                            new PointNode(x, y, false, null, traceline),
                            0, null, 0, boxDistance, GeometryHelper.findManhattanDistance(x, y, b) * relaxationMultiplier));
                }
            }
        }

        /*
         * Maps points (Since PointNode.equals compares only the coordinates.) to the shortest path
         * to them currently found.
         */
        HashMap<PointNode, PathNode> visited = new HashMap<>();

        PathNode current = queue.remove();

        /*
         * The path is found with a version of the A* search algorithm, with the manhattan distance
         * to the end box used as the heuristic and ties broken by the number of corners along the
         * path.
         *
         * When the heuristic of the current best PointNode is 0, it lies on the end box and the
         * path is finished.
         */
        while (current.h > 0) {
            /*
             * Check further paths only if the current path is the current best path leading to this
             * point.
             */
            if (!visited.containsKey(current.pointNode)
                    || current.compareTo(visited.get(current.pointNode)) < 0) {
                visited.put(current.pointNode, current);
                for (PointNode newPointNode : findAdjacentPointNodes(current.pointNode)) {

                    /*
                     * For each of the adjacent PointNodes, calculates its distance from the
                     * current.pointNode and the number of corners along the path to it, as well as
                     * its heuristic.
                     */
                    double newDistance = Math.abs(newPointNode.x - current.pointNode.x)
                            + Math.abs(newPointNode.y - current.pointNode.y) + current.distance;
                    int newCornerCount = current.cornerCount
                            + ((current.prev != null
                            && newPointNode.x != current.prev.pointNode.x
                            && newPointNode.y != current.prev.pointNode.y) ? 1 : 0);
                    double newHeuristic = GeometryHelper.findManhattanDistance(newPointNode.x, newPointNode.y, b);
                    /*
                     * The new PathNode is added to the queue only if it's better than the current
                     * best path to that point.
                     */
                    PathNode newPathNode = new PathNode(newPointNode,
                            newDistance, current, newCornerCount, boxDistance, newHeuristic * relaxationMultiplier);
                    if (!visited.containsKey(newPointNode)
                            || newPathNode.compareTo(visited.get(newPointNode)) < 0) {
                        queue.add(newPathNode);
                    }
                }
            }

            current = queue.remove();

            while (current.h == 0) {
                BoxSide side = BoxSide.findPointSide(new Point2D.Double(current.pointNode.x, current.pointNode.y),
                        new Point2D.Double(current.prev.pointNode.x, current.prev.pointNode.y), b);
                if ((side.mask & endSides) == 0) {

                    current = queue.remove();

                } else {
                    break;
                }
            }
        }

        /*
         * Reconstructs the path by going backwards along the found PathNodes.
         */
        LinkedList<Point2D.Double> trace = new LinkedList<>();

        if (swap) {
            while (current != null) {
                trace.addLast(new Point2D.Double(current.pointNode.x, current.pointNode.y));
                current = current.prev;
            }
        } else {
            while (current != null) {
                trace.addFirst(new Point2D.Double(current.pointNode.x, current.pointNode.y));
                current = current.prev;
            }
        }

        ArrayList<Point2D.Double> points = new ArrayList<>(trace);
        if (swap) {
            Box tmp = a;
            a = b;
            b = tmp;
        }
        assert GeometryHelper.isPointOnPerimeter(a, points.get(0)) && GeometryHelper.isPointOnPerimeter(b, points.get(points.size() - 1));
        return points;
    }

    //<editor-fold defaultstate="collapsed" desc="debug">
    /**
     * Traces an orthogonal line between two boxes and returns it as a array of points. The line
     * will not overlap boxes it shouldn't, but will not maintain spacing until normalization. Adds
     * debug output to file..
     *
     * @param a the start box
     * @param b the end box
     * @param startSides the mask denoting the allowed start box sides for this line
     * @param endSides the mask denoting the allowed end box sides for this line
     * @return the list of points defining the line
     */
    ArrayList<Point2D.Double> debugTrace(Box a, Box b, int startSides, int endSides) {
        double distance = GeometryHelper.findManhattanDistance(a, b);
        double size = Math.max(Math.max(a.getWidth(), b.getHeight()), Math.max(b.getWidth(), b.getHeight()));
        double relaxationMultiplier = distance < size ? 1 : RELAXATION_MULTIPLIER;

        boolean swap = false;
        if ((a.getCenter().getX() > b.getCenter().getX()
                || (a.getCenter().getX() == b.getCenter().getX() && a.getCenter().getY() > b.getCenter().getY()))) {
            Box t = a;
            a = b;
            b = t;
            swap = true;

            int tmp = startSides;
            startSides = endSides;
            endSides = tmp;
        }

/*        PrintWriter pw = null;
        try {
            pw = new PrintWriter("traceData.txt");
            Diagram diagram = a.getDiagram();
            pw.println((int) diagram.left + " " + (int) diagram.top + " " + (int) diagram.bounds.width + " " + (int) diagram.bounds.height);
            pw.println(verticalSegments.size() + horizontalSegments.size());
            for (RectangleSegment s : horizontalSegments) {
                pw.println((int) s.getPos() + " " + (int) s.getStart() + " " + (int) s.getPos() + " " + (int) s.getEnd());
            }
            for (RectangleSegment s : verticalSegments) {
                pw.println((int) s.getStart() + " " + (int) s.getPos() + " " + (int) s.getEnd() + " " + (int) s.getPos());
            }
            pw.println(verticalTraceLines.length + horizontalTraceLines.length);
            for (Traceline s : verticalTraceLines) {
                pw.println((int) s.y + " " + (int) s.getLeft() + " " + (int) s.y + " " + (int) s.getRight());
            }
            for (Traceline s : horizontalTraceLines) {
                pw.println((int) s.getLeft() + " " + (int) s.y + " " + (int) s.getRight() + " " + (int) s.y);
            }
            pw.println((int) a.left + " " + (int) a.top + " " + (int) (a.right - a.left) + " " + (int) (a.bottom - a.top));
            pw.println((int) b.left + " " + (int) b.top + " " + (int) (b.right - b.left) + " " + (int) (b.bottom - b.top));
        } catch (FileNotFoundException ex) {
        }*/

        if (a == b) {
            return connectOneBox(a, startSides, endSides, a.getSpacing() / 4);
        }

        double boxDistance = CORNER_WEIGHT * (Math.abs(a.getCenterX() - b.getCenterX()) + Math.abs(a.getCenterY() - b.getCenterY()));

        /*
         * The priority queue for the pathfinding algorithm.
         */
        PriorityQueue<PathNode> queue = new PriorityQueue<>();
        for (Traceline traceline : horizontalTraceLines) {
            if (traceline.leftSegment.getContainer() == a || traceline.rightSegment.getContainer() == a) {
                boolean left = traceline.leftSegment.getContainer() == a;
                if (left) {
                    if ((startSides & 0b0010) == 0) {
                        continue;
                    }
                } else {
                    if ((startSides & 0b1000) == 0) {
                        continue;
                    }
                }
                double x = left ? traceline.getLeft() : traceline.getRight(),
                        y = traceline.y;
                /*
                 * The segments used are extended somewhat to not draw lines too close to the boxes.
                 * However, a point is only a valid start point if it actually touches the start
                 * box.
                 */
                if (GeometryHelper.findManhattanDistance(x, y, a) == 0) {
                    queue.add(new PathNode(
                            new PointNode(x, y, true, traceline, null),
                            0, null, 0, boxDistance, GeometryHelper.findManhattanDistance(x, y, b) * relaxationMultiplier));
                }
            }
        }
        for (Traceline traceline : verticalTraceLines) {
            if (traceline.leftSegment.getContainer() == a || traceline.rightSegment.getContainer() == a) {
                boolean top = traceline.leftSegment.getContainer() == a;
                if (top) {
                    if ((startSides & 0b0100) == 0) {
                        continue;
                    }
                } else {
                    if ((startSides & 0b0001) == 0) {
                        continue;
                    }
                }
                double x = traceline.y,
                        y = top ? traceline.getLeft() : traceline.getRight();
                /*
                 * The segments used are extended somewhat to not draw lines too close to the boxes.
                 * However, a point is only a valid start point if it actually touches the start
                 * box.
                 */
                if (GeometryHelper.findManhattanDistance(x, y, a) == 0) {
                    queue.add(new PathNode(
                            new PointNode(x, y, false, null, traceline),
                            0, null, 0, boxDistance, GeometryHelper.findManhattanDistance(x, y, b) * relaxationMultiplier));
                }
            }
        }

        /*
         * Maps points (Since PointNode.equals compares only the coordinates.) to the shortest path
         * to them currently found.
         */
        HashMap<PointNode, PathNode> visited = new HashMap<>();
        ArrayList<Point2D.Double> printPoints = new ArrayList<>();
/*        if (queue.isEmpty()) {
            pw.println(printPoints.size());
            for (Point2D.Double p : printPoints) {
                pw.println((int) p.x + " " + (int) p.y);
            }
            pw.close();
        }*/
        PathNode current = queue.remove();
        printPoints.add(new Point2D.Double(current.pointNode.x, current.pointNode.y));

        /*
         * The path is found with a version of the A* search algorithm, with the manhattan distance
         * to the end box used as the heuristic and ties broken by the number of corners along the
         * path.
         *
         * When the heuristic of the current best PointNode is 0, it lies on the end box and the
         * path is finished.
         */
        //        System.out.println("...");
        //        System.out.flush();
        while (current.h > 0) {
            /*
             * Check further paths only if the current path is the current best path leading to this
             * point.
             */
            if (!visited.containsKey(current.pointNode)
                    || current.compareTo(visited.get(current.pointNode)) < 0) {
                visited.put(current.pointNode, current);
                for (PointNode newPointNode : findAdjacentPointNodes(current.pointNode)) {

                    /*
                     * For each of the adjacent PointNodes, calculates its distance from the
                     * current.pointNode and the number of corners along the path to it, as well as
                     * its heuristic.
                     */
                    double newDistance = Math.abs(newPointNode.x - current.pointNode.x)
                            + Math.abs(newPointNode.y - current.pointNode.y) + current.distance;
                    int newCornerCount = current.cornerCount
                            + ((current.prev != null
                            && newPointNode.x != current.prev.pointNode.x
                            && newPointNode.y != current.prev.pointNode.y) ? 1 : 0);
                    double newHeuristic = GeometryHelper.findManhattanDistance(newPointNode.x, newPointNode.y, b);
                    /*
                     * The new PathNode is added to the queue only if it's better than the current
                     * best path to that point.
                     */
                    PathNode newPathNode = new PathNode(newPointNode,
                            newDistance, current, newCornerCount, boxDistance, newHeuristic * relaxationMultiplier);
                    if (!visited.containsKey(newPointNode)
                            || newPathNode.compareTo(visited.get(newPointNode)) < 0) {
                        queue.add(newPathNode);
                        //                    System.out.println(newPointNode + " " + "(" + b.left + " " + b.right + " " + b.top + " " + b.bottom+ ")");
                        //                        if (newHeuristic == 0) {
                        //                            System.out.println(endSides + " " + newPointNode + " " + "(" + b.left + " " + b.right + " " + b.top + " " + b.bottom + ")");
                        //                        }
                    }
                }
            }
/*            if (queue.isEmpty()) {
                pw.println(printPoints.size());
                for (Point2D.Double p : printPoints) {
                    pw.println((int) p.x + " " + (int) p.y);
                }
                pw.close();
            }*/

            current = queue.remove();
            printPoints.add(new Point2D.Double(current.pointNode.x, current.pointNode.y));
            while (current.h == 0) {
                BoxSide side = BoxSide.findPointSide(new Point2D.Double(current.pointNode.x, current.pointNode.y),
                        new Point2D.Double(current.prev.pointNode.x, current.prev.pointNode.y), b);
                if ((side.mask & endSides) == 0) {
                    current = queue.remove();
                    printPoints.add(new Point2D.Double(current.pointNode.x, current.pointNode.y));
                } else {
                    //                        System.out.println(current.pointNode.x + " " + current.pointNode.y);
                    //                        System.out.flush();
                    break;
                }
            }
        }

/*        if (!pw.checkError()) {
            pw.println(printPoints.size());
            for (Point2D.Double p : printPoints) {
                pw.println((int) p.x + " " + (int) p.y);
            }
            pw.close();
        }*/

        /*
         * Reconstructs the path by going backwards along the found PathNodes.
         */
        LinkedList<Point2D.Double> trace = new LinkedList<>();

        if (swap) {
            while (current != null) {
                trace.addLast(new Point2D.Double(current.pointNode.x, current.pointNode.y));
                current = current.prev;
            }
        } else {
            while (current != null) {
                trace.addFirst(new Point2D.Double(current.pointNode.x, current.pointNode.y));
                current = current.prev;
            }
        }
        return new ArrayList<>(trace);
    }
    //</editor-fold>

    /**
     * Finds the closest PointNode to the given PointNode lying on the TraceLine in the given
     * direction that lies on the intersection of two TraceLines or next to the endBox.
     *
     * @param pointNode the given PointNode
     * @param startIndex the index in the array of TraceLines that corresponds to the first
     * TraceLine to check
     * @param left whether to check lines above/to the left of the given PointNode
     * @param isHorizontal whether the next point is searched for along a horizontal TraceLine
     * @param tracelines an ordered array of the TraceLines perpendicular to the one to search along
     * @param pathLine the TraceLine along which to check
     * @return the closest PointNode to the given PointNode lying on the TraceLine in the given
     * direction that lies on the intersection of two TraceLines or next to the endBox.
     */
    private PointNode findNextPointNode(
            PointNode pointNode,
            int startIndex,
            boolean left,
            boolean isHorizontal,
            Traceline[] tracelines,
            Traceline pathLine) {
        /*
         * Goes along tracelines in the given direction until it reaches the end of the array or
         * goes beyong pathLine, looking for a traceline that intersects pathLine and returns the
         * intersection if one is found.
         */
        while ((left ? startIndex >= 0 : startIndex < tracelines.length)
                && (left ? tracelines[startIndex].y > pathLine.getLeft()
                : tracelines[startIndex].y < pathLine.getRight())) {

            if (tracelines[startIndex].getLeft() < (isHorizontal ? pointNode.y : pointNode.x)
                    && (isHorizontal ? pointNode.y : pointNode.x) < tracelines[startIndex].getRight()) {

                double x = isHorizontal ? tracelines[startIndex].y : pointNode.x,
                        y = isHorizontal ? pointNode.y : tracelines[startIndex].y;
                return new PointNode(x, y, isHorizontal,
                        isHorizontal ? pathLine : tracelines[startIndex],
                        isHorizontal ? tracelines[startIndex] : pathLine);
            }
            startIndex += left ? -1 : 1;
        }

        /*
         * If no intersection was found, then returns the endpoint of the traceline that is
         * connected to a box.
         */
        double x = isHorizontal ? (left ? pathLine.getLeft() : pathLine.getRight()) : pointNode.x,
                y = isHorizontal ? pointNode.y : (left ? pathLine.getLeft() : pathLine.getRight());
        return new PointNode(x, y, isHorizontal, isHorizontal ? pathLine : null, isHorizontal ? null : pathLine);
    }

    /**
     * Finds the set of PointNodes closest to the given one in all directions.
     *
     * @param pointNode the given pointNode
     * @return a set of pointNodes closest to the given one in all directions
     */
    private ArrayList<PointNode> findAdjacentPointNodes(PointNode pointNode) {
        if (memPointNodes.containsKey(pointNode)) {
            return memPointNodes.get(pointNode);
        }

        ArrayList<PointNode> pointNodes = new ArrayList<>();
        Traceline horizontalLine = pointNode.horizontalLine,
                verticalLine = pointNode.verticalLine;

        PointNode newNode;

        /*
         * This should be false only when the given pointNode is the start of a path.
         */
        if (horizontalLine != null && verticalLine != null) {
            int horizontalLineIndex = horizontalLine.index,
                    verticalLineIndex = verticalLine.index;

            /*
             * Searches up.
             */
            newNode = findNextPointNode(pointNode, horizontalLineIndex - 1, true, false, horizontalTraceLines, verticalLine);
            if (newNode != null) {
                pointNodes.add(newNode);
            }

            /*
             * Searches down.
             */
            newNode = findNextPointNode(pointNode, horizontalLineIndex + 1, false, false, horizontalTraceLines, verticalLine);
            if (newNode != null) {
                pointNodes.add(newNode);
            }

            /*
             * Searches left.
             */
            newNode = findNextPointNode(pointNode, verticalLineIndex - 1, true, true, verticalTraceLines, horizontalLine);
            if (newNode != null) {
                pointNodes.add(newNode);
            }

            /*
             * Searches right.
             */
            newNode = findNextPointNode(pointNode, verticalLineIndex + 1, false, true, verticalTraceLines, horizontalLine);
            if (newNode != null) {
                pointNodes.add(newNode);
            }
        } else {
            /*
             * The point is at the end of a vertical TraceLine connected to the start box.
             */
            if (horizontalLine == null) {
                assert verticalLine != null;
                /*
                 * Since the point doesn't actually lie on a horizontal line, the TraceLine index
                 * used in finding the next pointNode is found using binary search.
                 */
                int i = -(1 + Arrays.binarySearch(horizontalTraceLines, new Traceline(
                        new BoxSegment(Double.POSITIVE_INFINITY, 0, 0, null, null), null, pointNode.y)));

                boolean up = verticalLine.rightSegment.getPos() == pointNode.y;
                if (up) {
                    newNode = findNextPointNode(pointNode, i - 1, true, false, horizontalTraceLines, verticalLine);
                    if (newNode != null) {
                        pointNodes.add(newNode);
                    }
                } else {
                    newNode = findNextPointNode(pointNode, i, false, false, horizontalTraceLines, verticalLine);
                    if (newNode != null) {
                        pointNodes.add(newNode);
                    }
                }
            } else {
                /*
                 * Since the point doesn't actually lie on a vertical line, the TraceLine index used
                 * in finding the next pointNode is found using binary search.
                 */
                int i = -(1 + Arrays.binarySearch(verticalTraceLines, new Traceline(
                        new BoxSegment(Double.POSITIVE_INFINITY, 0, 0, null, null), null, pointNode.x)));

                boolean left = horizontalLine.rightSegment.getPos() == pointNode.x;
                if (left) {
                    newNode = findNextPointNode(pointNode, i - 1, true, true, verticalTraceLines, horizontalLine);
                    if (newNode != null) {
                        pointNodes.add(newNode);
                    }
                } else {
                    newNode = findNextPointNode(pointNode, i, false, true, verticalTraceLines, horizontalLine);
                    if (newNode != null) {
                        pointNodes.add(newNode);
                    }
                }
            }
        }

        memPointNodes.put(pointNode, pointNodes);
        return pointNodes;
    }

    /**
     * Finds the TraceLines running perpendicular to the given RectangleSegments
     *
     * @param segments the RectangleSegments perpendicular to which TraceLines should be constructed
     * @return the set of TraceLines running perpendicular to the given RectangleSegments
     */
    private ArrayList<Traceline> findTraceLines(ArrayList<RectangleSegment> segments) {
        ArrayList<Traceline> graph = new ArrayList<>();

        if (segments.isEmpty()) {
            return graph;
        }

        Collections.sort(segments);

        TreeSet<VerticalPart<RectangleSegment>> sweepline = new TreeSet<>();//,
//                spacingLineSweepline = new TreeSet<>();
        /*
         * Adds dummy buffer parts to both ends of the sweepline, prevents analysing special cases.
         */
        sweepline.add(new VerticalPart<RectangleSegment>(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, null));
        sweepline.add(new VerticalPart<RectangleSegment>(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, null));

        /*
         * The main cycle through the segments in order from leftSegment to rightSegment.
         */
        for (RectangleSegment segment : segments) {
//            Set<VerticalPart> spacingLineParts = spacingLineSweepline.tailSet(
//            new VerticalPart(segment.getStart(), segment.getStart(), null), true);
//            //new VerticalPart(segment.getEnd(), segment.getEnd(), null), true);
//            
//            Iterator<VerticalPart> it = spacingLineParts.iterator();
//            VerticalPart current = it.hasNext()? it.next() : null;
//            while (current!=null && current.getStart()<=segment.getEnd()) {
//            graph.add(new TraceLine(((BoxSegment) current.getSegment()),
//            segment, current.getStart()));
//            it.remove();
//            current=it.hasNext()? it.next() : null;
//            }
//            
//            if (segment.getSegmentType() == BoxSegment.SegmentType.LEFT
//            || segment.getSegmentType() == BoxSegment.SegmentType.TOP) {
//            double spacing = segment.getBox().getSpacing() / 2;
//            spacingLineSweepline.add(new VerticalPart(segment.getStart() - spacing,
//            segment.getStart() - spacing,
//            sweepline.floor(new VerticalPart(segment.getStart() - spacing, segment.getStart() - spacing, null)).getSegment()));
//            spacingLineSweepline.add(new VerticalPart(segment.getEnd() + spacing,
//            segment.getEnd() + spacing,
//            sweepline.floor(new VerticalPart(segment.getEnd() + spacing, segment.getEnd() + spacing, null)).getSegment()));
//            }

            /*
             * The parts to start and end checking overlap with the current segment from.
             */
            VerticalPart<RectangleSegment> startPart = sweepline.lower(new VerticalPart<>(segment.getStart(), segment.getStart(), segment));
            /*
             * A TreeSet containing the parts to check for overlap with the current segment.
             * Includes both startPart and endPart.
             */
            NavigableSet<VerticalPart<RectangleSegment>> parts = sweepline.tailSet(startPart, startPart.intersects(segment));
            startPart = parts.first();

            VerticalPart<RectangleSegment> endPart = startPart;

            /*
             * The parts to iterate through, with mid being the part to currently check.
             */
            VerticalPart<RectangleSegment> mid;
            Iterator<VerticalPart<RectangleSegment>> it = parts.iterator();
            mid = it.next();
            /*
             * The cycle through the parts, stops when the current part is below the current
             * segment.
             */
            while (mid.intersects(segment)) {
                it.remove();
                endPart = mid;

                if (mid.getSegment().getContainer() != segment.getContainer()
                        || mid.getBottom() - mid.getTop() != segment.getEnd() - segment.getStart()) {
                    if (Math.min(mid.getBottom(), segment.getEnd()) - Math.max(mid.getTop(), segment.getStart()) > epsilon) {
                        graph.add(new Traceline(mid.getSegment(), segment,
                                (Math.max(mid.getTop(), segment.getStart())
                                + Math.min(mid.getBottom(), segment.getEnd())) / 2));
                    }
                }

                /*
                 * Advances the part cycle.
                 */
                if (it.hasNext()) {
                    mid = it.next();
                } else {
                    break;
                }
            }

            /*
             * The current segment always gets added to the sweepline whole.
             */
            VerticalPart<RectangleSegment> segmentPart = new VerticalPart<>(segment);
            sweepline.add(segmentPart);
            /*
             * If the first or the last part weren't fully covered by the new segment, reinsert
             * them.
             */
            if (startPart.intersects(segment)) {
                if (startPart.getTop() < segmentPart.getTop() && Math.abs(startPart.getTop() - segmentPart.getTop()) > epsilon) {
                    sweepline.add(new VerticalPart<>(startPart.getTop(), segmentPart.getTop(), startPart.getSegment()));
                }
                if (endPart.getBottom() > segmentPart.getBottom() && Math.abs(segmentPart.getBottom() - endPart.getBottom()) > epsilon) {
                    sweepline.add(new VerticalPart<>(segmentPart.getBottom(), endPart.getBottom(), endPart.getSegment()));
                }
            }
        }

        //Collections.sort(graph);
        return graph;
    }

    /**
     * A class for storing a pair of side indices (starting from 0 for top and going clockwise)
     * along with the heuristic cost of connected those sides with a line, used in determining the
     * best sides to create a looping line from.
     */
    private static class SidePair implements Comparable<SidePair> {

        /**
         * The heuristic cost of the pair.
         */
        Pair<Integer, Double> cost;
        /**
         * The first side of the pair.
         */
        int firstSide;
        /**
         * The second side of the pair, may be the same of the first.
         */
        int secondSide;

        /**
         * Creates a new {@code SidePair} with the given parameters.
         *
         * @param cost the cost of the new pair
         * @param firstSide the first side of the new pair
         * @param secondSide the second side of the new pair
         */
        public SidePair(Pair<Integer, Double> cost, int firstSide, int secondSide) {
            this.cost = cost;
            this.firstSide = firstSide;
            this.secondSide = secondSide;
        }

        @Override
        public int compareTo(SidePair o) {
            return cost.compareTo(o.cost);
        }
    }

    /**
     * Traces a new line connecting a box to itself.
     *
     * @param box the box to connect to itself
     * @param startSides the mask denoting the allowed box sides for the start new line
     * @param endSides the mask denoting the allowed box side for the end of the new line
     * @param spacing the spacing of the line
     * @return the points of the new line
     */
    static ArrayList<Point2D.Double> connectOneBox(Box box, int startSides, int endSides, double spacing) {
        ArrayList<ArrayList<Double>> connectedLines = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            connectedLines.add(new ArrayList<Double>());
        }

        /*
         * For this algorithm, 0 to 3 denotes top to left in clockwise order. We find a heuristic
         * value for each side and adjacent pair of sides to determine where to place the new line.
         */
        double left = box.getLeft(), right = box.getRight(), top = box.getTop(), bottom = box.getBottom();
        for (Line line : box.incidentLines) {
            /*
             * The currently traced line won't have any geometry yet.
             */
            if (line.getStart() != line.getEnd()) {
                for (int i = 0; i < 2; i++) {
                    Point2D.Double endPoint;
                    if (i == 0 && line.getStart() == box) {
                        endPoint = line.getStartPoint();
                    } else if (i == 1 && line.getEnd() == box) {
                        endPoint = line.getEndPoint();
                    } else {
                        continue;
                    }

                    if (GeometryHelper.isPointOnPerimeter(box, endPoint)) {
                        if (endPoint.y == top) {
                            connectedLines.get(0).add(endPoint.x);
                        } else if (endPoint.x == right) {
                            connectedLines.get(1).add(endPoint.y);
                        } else if (endPoint.y == bottom) {
                            connectedLines.get(2).add(endPoint.x);
                        } else if (endPoint.x == left) {
                            connectedLines.get(3).add(endPoint.y);
                        }
                    }
                }
            }
        }
        for (int i = 0; i < 4; i++) {
            if (i % 2 == 0) {
                connectedLines.get(i).add(left);
                connectedLines.get(i).add(right);
            } else {
                connectedLines.get(i).add(top);
                connectedLines.get(i).add(bottom);
            }
            Collections.sort(connectedLines.get(i));
        }

        ArrayList<SidePair> sidePairs = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            if ((startSides & (1 << i)) != 0) {
                for (int j = 0; j < 4; j++) {
                    if ((endSides & (1 << j)) != 0) {
                        if (i == j) {
                            double w = (connectedLines.get(i).size() - 2);
                            sidePairs.add(new SidePair(new Pair<>(w == 0 ? 1 : 3, Double.MAX_VALUE), i, i));
                        } else if ((i + j) % 2 == 1) {
                            double wi = (connectedLines.get(i).size() - 2),
                                    wj = (connectedLines.get(j).size() - 2);
                            boolean cw = (i + 1) % 4 == j;
                            boolean iDir = (i < 2) ^ !cw;
                            boolean jDir = (j < 2) ^ cw;
                            int iSize = connectedLines.get(i).size();
                            int jSize = connectedLines.get(j).size();
                            double iLen = iDir ? connectedLines.get(i).get(iSize - 1) - connectedLines.get(i).get(iSize - 2)
                                    : connectedLines.get(i).get(1) - connectedLines.get(i).get(0);
                            double jLen = jDir ? connectedLines.get(j).get(jSize - 1) - connectedLines.get(j).get(jSize - 2)
                                    : connectedLines.get(j).get(1) - connectedLines.get(j).get(0);
                            sidePairs.add(new SidePair(new Pair<>(wi + wj == 0 ? (cw ? 0 : 1) : (cw ? 2 : 3), Math.min(iLen, jLen)), i, j));
                        } else {
                            sidePairs.add(new SidePair(new Pair<>(4, 0.), i, j));
                        }
                    }
                }
            }
        }
        Collections.sort(sidePairs);

        int firstSide = sidePairs.get(0).firstSide,
                secondSide = sidePairs.get(0).secondSide;
        Point2D.Double start;
        Point2D.Double end;

        double epsilon = Math.min(spacing / 2, Math.min(box.getWidth(), box.getHeight()) / 3);

        boolean sideHorizontal = firstSide % 2 == 0;
        if (firstSide == secondSide) {
            int dir = firstSide < 2 ? 1 : -1;

            if (sideHorizontal) {
                double y = firstSide == 0 ? top : bottom;
                double x = box.getCenterX();
                start = new Point2D.Double(x - epsilon * dir, y);
                end = new Point2D.Double(x + epsilon * dir, y);
            } else {
                double x = firstSide == 3 ? left : right;
                double y = box.getCenterY();
                start = new Point2D.Double(x, y - epsilon * dir);
                end = new Point2D.Double(x, y + epsilon * dir);
            }
        } else if ((firstSide + secondSide) % 2 == 1) {
            boolean cw = (firstSide + 1) % 4 == secondSide;
            boolean firstDir = (firstSide < 2) ^ !cw;
            boolean secondDir = (secondSide < 2) ^ cw;
            if (sideHorizontal) {
                start = new Point2D.Double(firstDir ? right - epsilon : left + epsilon, firstSide == 0 ? top : bottom);
                end = new Point2D.Double(secondSide == 1 ? right : left, secondDir ? bottom - epsilon : top + epsilon);
            } else {
                start = new Point2D.Double(firstSide == 1 ? right : left, firstDir ? bottom - epsilon : top + epsilon);
                end = new Point2D.Double(secondDir ? right - epsilon : left + epsilon, secondSide == 0 ? top : bottom);
            }
        } else {
            for (Line line : box.incidentLines) {
                if (line.lineGeometry != null && line.getStart() == line.getEnd()) {
                    for (int i = 0; i < 2; i++) {
                        Point2D.Double endPoint = i == 0 ? line.getStartPoint() : line.getEndPoint();

                        if (endPoint.y == top) {
                            connectedLines.get(0).add(endPoint.x);
                        } else if (endPoint.x == right) {
                            connectedLines.get(1).add(endPoint.y);
                        } else if (endPoint.y == bottom) {
                            connectedLines.get(2).add(endPoint.x);
                        } else if (endPoint.x == left) {
                            connectedLines.get(3).add(endPoint.y);
                        }
                    }
                }
            }
            if (sideHorizontal) {
                double x = connectedLines.get(1).size() < connectedLines.get(3).size() ? right - 2 * epsilon : left + 2 * epsilon;
                start = new Point2D.Double(x, firstSide == 0 ? top : bottom);
                end = new Point2D.Double(x, secondSide == 0 ? top : bottom);
            } else {
                double y = connectedLines.get(0).size() < connectedLines.get(2).size() ? top + 2 * epsilon : bottom - 2 * epsilon;
                start = new Point2D.Double(firstSide == 1 ? right : left, y);
                end = new Point2D.Double(secondSide == 1 ? right : left, y);
            }
        }

        ArrayList<Point2D.Double> points = new ArrayList<>();
        points.add(start);
        points.addAll(LineOptimizer.traceAroundBox(box,
                start, BoxSide.values()[firstSide],
                end, BoxSide.values()[secondSide], spacing / 2 + box.spacing));
        points.add(end);
        return points;
    }
}
