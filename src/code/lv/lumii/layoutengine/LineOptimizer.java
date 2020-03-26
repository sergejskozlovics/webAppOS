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

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.*;
import lv.lumii.layoutengine.Box.BoxSide;
import lv.lumii.layoutengine.GeometryHelper.PointComparator;
import lv.lumii.layoutengine.Line.LineType;
import lv.lumii.layoutengine.OrthogonalSegment.OrthogonalLineSegment;
import lv.lumii.layoutengine.OrthogonalSegment.RectangleSegment;
import lv.lumii.layoutengine.OrthogonalSegment.RectangleSegment.SegmentType;
import lv.lumii.layoutengine.OutsideLabel.LineLabel;
import lv.lumii.layoutengine.obstacleGraph.ObstacleGraph;
import lv.lumii.layoutengine.obstacleGraph.Segment;
import lv.lumii.layoutengine.obstacleGraph.Segment.VerticalPart;
import lv.lumii.layoutengine.util.Pair;

/**
 * This class contains methods that correct and improve the geometry of lines.
 *
 * @author Evgeny Vihrov
 */
abstract class LineOptimizer {

    /**
     * The number of diagram epsilons by which a line segment could be moved to consider it equal to
     * its position before any actions that change geometry.
     */
    static double SEGMENT_MATCHING_OFFSET = 10;

    /**
     * Finds the set of rectangles that would serve as obstacles for a line connecting the two given
     * boxes.
     *
     * @param a the first box to connect
     * @param b the second box to connect
     * @param includeLCA whether to include the LCA in the set
     * @return the set of rectangles that would serve as obstacles for a line connecting the two
     * given boxes.
     */
    static LinkedHashSet<AbstractContainer> getObstacleRectangles(Box a, Box b, boolean includeLCA) {
        Box lca;
        if (a == b) {
            lca = a.getPrevBox();
        } else {
            lca = a.findLCABox(b);
        }

        return getObstacleRectangles(a.getPrevBox(), b.getPrevBox(), lca, includeLCA);
    }

    /**
     * Finds the set of rectangles that would serve as obstacles for a line connecting two boxes,
     * possibly including the LCA of these boxes.
     *
     * @param line the line for which to find obstacle rectangles
     * @param includeLCA whether to include the LCA in the set
     * @return the set of rectangles that would serve as obstacles for the line.
     */
    static LinkedHashSet<AbstractContainer> getObstacleRectangles(Line line, boolean includeLCA) {
        return getObstacleRectangles((Box) line.getStart(), (Box) line.getEnd(), includeLCA);
    }

    /**
     * Find the set of rectangles that can obstruct a line between boxes with the given parents and
     * LCA.
     *
     * @param firstParent the parent of one end box of the lines for which this obstacle set is
     * found
     * @param secondParent the parent of the other end box of the lines for which this obstacle set
     * is found
     * @param lca the least common ancestor box of the boxes to connect
     * @param includeLCA whether to include the LCA in the set
     * @return the set of rectangles that can obstruct a line with the given parameters
     */
    static LinkedHashSet<AbstractContainer> getObstacleRectangles(Box firstParent, Box secondParent, Box lca, boolean includeLCA) {
        assert lca != null;

        LinkedHashSet<AbstractContainer> obstacles = new LinkedHashSet<>();
        Box lcaParent = lca.getPrevBox();

        /*
         * Adds all the next boxes of each predeccesor of both boxes. At this step including the
         * predeccesors themselves.
         */
        if (lcaParent != firstParent) {
            for (Box parent = firstParent; parent != lca; parent = parent.getPrevBox()) {
                obstacles.addAll(parent.getNextRectangles(false));
            }
        }
        if (lcaParent != secondParent) {
            for (Box parent = secondParent; parent != lca; parent = parent.getPrevBox()) {
                obstacles.addAll(parent.getNextRectangles(false));
            }
        }
        obstacles.addAll(lca.getNextRectangles(false));
        /*
         * Removes all the predeccesors of both boxes.
         */
        if (lcaParent != firstParent) {
            for (Box parent = firstParent; parent != lca; parent = parent.getPrevBox()) {
                obstacles.remove(parent);
            }
        }
        if (lcaParent != secondParent) {
            for (Box parent = secondParent; parent != lca; parent = parent.getPrevBox()) {
                obstacles.remove(parent);
            }
        }

        if (includeLCA) {
            obstacles.add(lca);
        }

        return obstacles;
    }

    /**
     * Generates a set of {@link OrthogonalSegment.RectangleSegment}s from the sides of the given
     * rectangles. Note that the direction given is the direction of iteration over these segments,
     * so {@code horizontal==true} will produce segments corresponding to the left and right sides
     * of the rectangles. The segments are prolonged by the given value.
     *
     * @param rectangles the rectangles for whose sides to find segments
     * @param horizontal whether the rectangles' sides will be iterated over horizontally
     * @param value the value by which to prolong the segments
     * @return the set of {@link OrthogonalSegment.RectangleSegment}s corresponding to the rectangle
     * sides.
     */
    static ArrayList<RectangleSegment> findProlongedSegments(LinkedHashSet<AbstractContainer> rectangles, boolean horizontal, double value) {
        RectangleSegment.SegmentType firstSegmentType, secondSegmentType;
        if (horizontal) {
            firstSegmentType = RectangleSegment.SegmentType.LEFT;
            secondSegmentType = RectangleSegment.SegmentType.RIGHT;
        } else {
            firstSegmentType = RectangleSegment.SegmentType.TOP;
            secondSegmentType = RectangleSegment.SegmentType.BOTTOM;
        }

        ArrayList<RectangleSegment> segments = new ArrayList<>(rectangles.size() * 2);
        for (AbstractContainer rect : rectangles) {
            /*
             * The segments are extended by spacing/4 so that lines aren't drawn so close to them
             * that normalization switches them around.
             */
            double firstBound = (horizontal ? rect.getTop() : rect.getLeft()) - value;
            double secondBound = (horizontal ? rect.getBottom() : rect.getRight()) + value;

            RectangleSegment leftSegment = RectangleSegment.createRectangleSegment(
                    (horizontal ? rect.getLeft() : rect.getTop()),
                    firstBound, secondBound, firstSegmentType, rect);
            RectangleSegment rightSegment = RectangleSegment.createRectangleSegment(
                    (horizontal ? rect.getRight() : rect.getBottom()),
                    firstBound, secondBound, secondSegmentType, rect);
            segments.add(leftSegment);
            segments.add(rightSegment);
        }

        return segments;
    }

    /**
     * Generates a set of {@link OrthogonalSegment.RectangleSegment}s from the sides of the given
     * rectangles. Note that the direction given is the direction of iteration over these segments,
     * so {@code horizontal==true} will produce segments corresponding to the left and right sides
     * of the rectangles. The segments are possibly extended by the rectangles' spacing so that
     * lines aren't drawn too close to them.
     *
     * @param rectangles the rectangles for whose sides to find segments
     * @param horizontal whether the rectangles' sides will be iterated over horizontally
     * @param spacingRatio the resulting segments will be extended at both ends by
     * {@code spacingRatio * spacing}.
     * @return the set of {@link OrthogonalSegment.RectangleSegment}s corresponding to the rectangle
     * sides.
     */
    static ArrayList<RectangleSegment> findSpacedSegments(Collection<? extends AbstractContainer> rectangles, boolean horizontal, double spacingRatio) {
        RectangleSegment.SegmentType firstSegmentType, secondSegmentType;
        if (horizontal) {
            firstSegmentType = RectangleSegment.SegmentType.LEFT;
            secondSegmentType = RectangleSegment.SegmentType.RIGHT;
        } else {
            firstSegmentType = RectangleSegment.SegmentType.TOP;
            secondSegmentType = RectangleSegment.SegmentType.BOTTOM;
        }

        ArrayList<RectangleSegment> segments = new ArrayList<>(rectangles.size() * 2);
        for (AbstractContainer rect : rectangles) {
            double spacing = rect.getSpacing();
            /*
             * The segments are extended by spacing/4 so that lines aren't drawn so close to them
             * that normalization switches them around.
             */
            double firstBound = (horizontal ? rect.getTop() : rect.getLeft()) - spacing * spacingRatio;
            double secondBound = (horizontal ? rect.getBottom() : rect.getRight()) + spacing * spacingRatio;

            OrthogonalSegment.RectangleSegment leftSegment = RectangleSegment.createRectangleSegment(
                    (horizontal ? rect.getLeft() : rect.getTop()),
                    firstBound, secondBound, firstSegmentType, rect);
            OrthogonalSegment.RectangleSegment rightSegment = RectangleSegment.createRectangleSegment(
                    (horizontal ? rect.getRight() : rect.getBottom()),
                    firstBound, secondBound, secondSegmentType, rect);
            segments.add(leftSegment);
            segments.add(rightSegment);
        }

        return segments;
    }

    /**
     * Reconnects the endpoints of the given polyline to its end boxes.
     *
     * @param line the polyline to reconnect
     */
    static void reconnectPolyline(Line line) {
        if (line.lineGeometry.points.size() == 2) {
            correctStraightLine(line);
        } else {
            Point2D.Double startPoint = line.getStartPoint();
            Point2D.Double prevPoint = line.lineGeometry.points.get(1);
            Box startBox = (Box) line.getStart();
            /*
             * First, tries to reconnect the start point and keeping the first line segment on the
             * same line. If that didn't succeed, tries to connect start box center with the second
             * line point of the line. If that also failed, connects the second line point with the
             * closest start box midpoint.
             */
            Point2D.Double newStart = findBestPoint(startBox, prevPoint, startPoint, line.getUsedStartSides());
            if (newStart == null) {
                startPoint = startBox.getCenter();
                newStart = findBestPoint(startBox, prevPoint, startPoint, line.getUsedStartSides());
            }
            if (newStart == null) {
                newStart = GeometryHelper.findClosestMidPoint(startBox, prevPoint, line.getUsedStartSides());
            }
            line.lineGeometry.points.set(0, newStart);
            /**
             * Performs a similar procedure for end line point.
             */
            Point2D.Double endPoint = line.getEndPoint();
            prevPoint = line.lineGeometry.points.get(line.lineGeometry.points.size() - 2);
            Box endBox = (Box) line.getEnd();
            Point2D.Double newEnd = findBestPoint(endBox, prevPoint, endPoint, line.getUsedEndSides());
            if (newEnd == null) {
                endPoint = endBox.getCenter();
                newEnd = findBestPoint(endBox, prevPoint, endPoint, line.getUsedEndSides());
            }
            if (newEnd == null) {
                newEnd = GeometryHelper.findClosestMidPoint(endBox, prevPoint, line.getUsedEndSides());
            }
            line.lineGeometry.points.set(line.lineGeometry.points.size() - 1, newEnd);
            line.lineGeometry.cullPoints();
        }
    }

    /**
     * Corrects the given straight line. If the line defined by the endpoints of the line intersects
     * both of the line start and end boxes, then the line is cut down to the shortest segment that
     * connects the boxes. Otherwise sets the line to the shortest segment of the line that connects
     * the centers of the two boxes.
     *
     * @param line the straight line to correct
     */
    static void correctStraightLine(Line line) {
        Box a = (Box) line.getStart(), b = (Box) line.getEnd();
        ArrayList<Point2D.Double> newPoints = new ArrayList<>();
        if (a == b) {
            line.lineGeometry = new LineGeometry.Polyline(a, b, 0b1111, 0b1111, line.spacing);
            return;
        }

        Point2D.Double start = line.getStartPoint(), end = line.getEndPoint();

        /*
         * The start and end points of the line cannot be equal for the algorithm.
         */
        if (start.equals(end)) {
            start = a.getCenter();
            end = b.getCenter();
        }

        /*
         * Finds the endpoints of the new segment that connect to the boxes.
         */
        Point2D.Double startPoint = findBestPoint(a, end, start, line.getUsedStartSides());
        Point2D.Double endPoint = findBestPoint(b, start, end, line.getUsedEndSides());

        /*
         * If the current line does not intersect one of the boxes, set the line to the one
         * connecting box centers.
         */
        if (startPoint == null || endPoint == null) {
            startPoint = findBestPoint(a, b.getCenter(), a.getCenter(), line.getUsedStartSides());
            endPoint = findBestPoint(b, a.getCenter(), b.getCenter(), line.getUsedEndSides());
        }

        if (startPoint != null && endPoint != null) {
            /*
             * If one of the boxes is inside the other, then the segment found runs over the box
             * that is inside. Since the segment direction has been changed in this situation, we
             * find the best point pair again.
             */
            startPoint = findBestPoint(a, endPoint, startPoint, line.getUsedStartSides());
            endPoint = findBestPoint(b, startPoint, endPoint, line.getUsedEndSides());
        } else {
            double[] cx = new double[4], cy = new double[4];

            ArrayList<Point2D.Double> startPoints = new ArrayList<>();
            if (startPoint == null) {
                cx[0] = cx[3] = a.getLeft();
                cx[1] = cx[2] = a.getRight();
                cy[0] = cy[1] = a.getTop();
                cy[2] = cy[3] = a.getBottom();
                for (int i = 0; i < 4; i++) {
                    if ((line.getUsedStartSides() & (1 << i)) != 0) {
                        int j = (i + 1) % 4;
                        startPoints.add(new Point2D.Double((cx[i] + cx[j]) / 2, (cy[i] + cy[j]) / 2));
                    }
                }
            } else {
                startPoints.add(startPoint);
            }

            ArrayList<Point2D.Double> endPoints = new ArrayList<>();
            if (endPoint == null) {
                cx[0] = cx[3] = b.getLeft();
                cx[1] = cx[2] = b.getRight();
                cy[0] = cy[1] = b.getTop();
                cy[2] = cy[3] = b.getBottom();
                for (int i = 0; i < 4; i++) {
                    if ((line.getUsedEndSides() & (1 << i)) != 0) {
                        int j = (i + 1) % 4;
                        endPoints.add(new Point2D.Double((cx[i] + cx[j]) / 2, (cy[i] + cy[j]) / 2));
                    }
                }
            } else {
                endPoints.add(endPoint);
            }

            double distance = Double.POSITIVE_INFINITY;
            for (Point2D.Double A : startPoints) {
                for (Point2D.Double B : endPoints) {
                    double currDistance = A.distance(B);
                    if (currDistance < distance) {
                        startPoint = A;
                        endPoint = B;
                        distance = currDistance;
                    }
                }
            }
        }

        assert startPoint != null && endPoint != null;
        newPoints.add(startPoint);
        newPoints.add(endPoint);
        line.lineGeometry.points = newPoints;
    }

    /**
     * Finds the intersection point of the given segment line and box. If there are multiple
     * choices, returns the closest one to the start point of the segment.
     *
     * @param box the box with which to find the intersection
     * @param start the start point of the segment
     * @param end the end point of the segment
     * @param sides the set of allowed sides on which the point can lie
     * @return the intersection point closest to the start point. If the line formed by the segment
     * does not intersect the box, returns {@code null}.
     */
    static Point2D.Double findBestPoint(Box box, Point2D.Double start, Point2D.Double end, int sides) {
        double[] cx = new double[4], cy = new double[4];
        cx[0] = cx[3] = box.getLeft();
        cx[1] = cx[2] = box.getRight();
        cy[0] = cy[1] = box.getTop();
        cy[2] = cy[3] = box.getBottom();

        /*
         * For each line formed by some box side, finds an intersection with the given segment, and
         * from all of them which lie inside the corresponding sides, finds the one closest to the
         * given segment start point.
         */
        double resX = 0, resY = 0, resT = Double.MAX_VALUE;
        for (int i = 0; i < 4; i++) {
            if (((1 << i) & sides) == 0) {
                continue;
            }

            int j = (i + 1) % 4;
            double x, y, t;
            if (cx[i] == cx[j]) {
                if (start.x != end.x) {
                    x = cx[i];
                    t = (x - start.x) / (end.x - start.x);
                    y = start.y + t * (end.y - start.y);
                    t = Math.abs(t);
                    if (t < resT && Math.min(cy[i], cy[j]) <= y && y <= Math.max(cy[i], cy[j])) {
                        resX = x;
                        resY = y;
                        resT = t;
                    }
                }
            } else {
                if (start.y != end.y) {
                    y = cy[i];
                    t = (y - start.y) / (end.y - start.y);
                    x = start.x + t * (end.x - start.x);
                    t = Math.abs(t);
                    if (t < resT && Math.min(cx[i], cx[j]) <= x && x <= Math.max(cx[i], cx[j])) {
                        resX = x;
                        resY = y;
                        resT = t;
                    }
                }
            }
        }

        return (resT < Double.MAX_VALUE ? new Point2D.Double(resX, resY) : null);
    }

    /**
     * Traces around the given box given two points on the perimeter of the box. Traces so that
     * first and last point of the traced line are the given spacing away from the given points, and
     * the line goes the minimum length around the box while maintaining this distance from it.
     *
     * @param box the box to trace around
     * @param start the point on the box's perimeter to start the tracing from
     * @param startSide the side of box on which the line starts, needed when the line connects to a
     * corner
     * @param end the point on the box's perimeter to end the tracing
     * @param endSide the side of box on which the line ends, needed when the line connects to a
     * corner
     * @param spacing the distance from the box's side to trace around, tracing inside the box if
     * the given spacing is negative.
     * @return a list of points, all the given away from the box's side, going around the box so
     * that the start and end points are in front of {@code start} and {@code end}, respectively.
     */
    static ArrayList<Point2D.Double> traceAroundBox(Box box,
            Point2D.Double start, BoxSide startSide, Point2D.Double end, BoxSide endSide, double spacing) {
        ArrayList<Point2D.Double> result = new ArrayList<>();
        if (!GeometryHelper.isPointOnPerimeter(box, end) || !GeometryHelper.isPointOnPerimeter(box, start)) {
            throw new IllegalArgumentException("To trace around a box the passed points must lie on the perimeter of the box.");
        }
        if (start.equals(end)) {
            throw new IllegalArgumentException("To trace around a box the passed points must be different.");
        }

        double left, right, top, bottom;
        left = box.getLeft();
        right = box.getRight();
        top = box.getTop();
        bottom = box.getBottom();

        /**
         * The sides of the box on which the endpoints reside, starting from 0 for top and going
         * clockwise.
         */
        /*
         * Creates the points in front of the given points.
         */
        switch (startSide) {
            case TOP:
                result.add(new Point2D.Double(start.x, start.getY() - spacing));
                break;
            case RIGHT:
                result.add(new Point2D.Double(start.x + spacing, start.getY()));
                break;
            case BOTTOM:
                result.add(new Point2D.Double(start.x, start.y + spacing));
                break;
            case LEFT:
                result.add(new Point2D.Double(start.x - spacing, start.y));
                break;
        }

        Point2D.Double lastPoint = null;
        switch (endSide) {
            case TOP:
                lastPoint = new Point2D.Double(end.x, end.y - spacing);
                break;
            case RIGHT:
                lastPoint = new Point2D.Double(end.x + spacing, end.y);
                break;
            case BOTTOM:
                lastPoint = new Point2D.Double(end.x, end.y + spacing);
                break;
            case LEFT:
                lastPoint = new Point2D.Double(end.x - spacing, end.y);
                break;
        }
        assert lastPoint != null;

        /*
         * If both points are on opposite sides of the box, find the side to trace around that gives
         * the minimal total line length. If both points are on the same side of the box, no
         * additional points are neccesary.
         */
        if ((startSide.ordinal() - endSide.ordinal()) % 2 == 0) {
            if (startSide != endSide) {
                if (startSide.ordinal() % 2 == 0) {
                    double newX = ((start.x + end.x - 2 * left) < (2 * right - start.x - end.x))
                            ? left - spacing : right + spacing;
                    result.add(new Point2D.Double(newX, result.get(0).y));
                    result.add(new Point2D.Double(newX, lastPoint.y));

                } else {
                    double newY = ((start.y + end.y - 2 * top) < (2 * bottom - start.y - end.y))
                            ? top - spacing : bottom + spacing;
                    result.add(new Point2D.Double(result.get(0).x, newY));
                    result.add(new Point2D.Double(lastPoint.x, newY));

                }
            }
        } else {
            /*
             * If the points are on adjacent sides of the box, adds a point on the corner to connect
             * them.
             */
            double newX, newY;
            if (startSide.ordinal() % 2 == 0) {
                newX = lastPoint.x;
                newY = result.get(0).y;
            } else {
                newX = result.get(0).x;
                newY = lastPoint.y;
            }
            result.add(new Point2D.Double(newX, newY));
        }
        result.add(lastPoint);
        return result;
    }

    /**
     * Cuts a line connecting a box with itself so that the line consists of a single part going
     * from on point on the box's perimeter to another, going outside the box.
     *
     * @param line the line to cut
     */
    static void cutOneBoxLine(Line line) {
        assert line.getStart() == line.getEnd() && line.getStart() instanceof Box;
        Box box = (Box) line.getStart();

        int firstPoint = -1, lastPoint = -1;
        ArrayList<Point2D.Double> points = line.lineGeometry.points;
        assert points.size() >= 2;
        for (int i = 0; i < points.size(); i++) {
            if (!box.contains(points.get(i), 0)) {
                firstPoint = i;
                while (i + 1 < points.size() && GeometryHelper.findIntersection(box, points.get(i), points.get(i + 1)) == null) {
                    i++;
                }
                lastPoint = i;
                break;
            }
        }

        ArrayList<Point2D.Double> newPoints = new ArrayList<>();
        if (firstPoint != -1) {
            /*
             * Some part of the line is outside the box.
             */
            if (firstPoint > 0) {
                newPoints.add(GeometryHelper.findIntersection(box,
                        points.get(firstPoint), points.get(firstPoint - 1)));
            }
            newPoints.addAll(points.subList(firstPoint, lastPoint + 1));
            if (lastPoint + 1 < points.size()) {
                newPoints.add(GeometryHelper.findIntersection(box,
                        points.get(lastPoint), points.get(lastPoint + 1)));
            }
        } else {
            /*
             * The whole line is inside the box, retrace the line.
             */
            Tracer tracer = new Tracer(box, box);
            newPoints = tracer.trace(box, box, line.getUsedStartSides(), line.getUsedEndSides());
        }

        line.lineGeometry.points = newPoints;
    }

    /**
     * Cuts a line so that it never enters it's end-boxes, keeping a single part of the line
     * connecting them.
     *
     * @param line the line to cut.
     */
    static void cutLine(Line line) {
        Box startBox = (Box) line.getStart(), endBox = (Box) line.getEnd();
        if (startBox == endBox) {
            cutOneBoxLine(line);
            return;
        }

        Box lca = startBox.findLCABox(endBox);

        /*
         * Goes in each direction along the line, cutting it the first time it crosses the box that
         * should be at the end going in that direction.
         */
        for (int k = 0; k < 2; k++) {
            boolean first = k == 0;
            Box box = (first ? endBox : startBox);
            if (box == lca) {
                continue;
            }

            ArrayList<Point2D.Double> linePoints = line.lineGeometry.points;
            if (!first) {
                Collections.reverse(linePoints);
            }

            for (int i = 0, j = 1; j < linePoints.size(); i++, j++) {
                Point2D.Double firstPoint = linePoints.get(i), secondPoint = linePoints.get(j);

                Point2D.Double newPoint = GeometryHelper.findIntersection(box, firstPoint, secondPoint);
                if (newPoint != null) {
                    linePoints = new ArrayList<>(linePoints.subList(0, j));
                    linePoints.add(newPoint);
                    break;
                }
            }

            if (!first) {
                Collections.reverse(linePoints);
            }
            line.lineGeometry.points = linePoints;
            line.lineGeometry.cullPoints();
        }
//
//        Box lcaBox = startBox.findLCABox(endBox);
//        boolean retrace = false;
//        for (int k = 0; k < 2 && !retrace; k++) {
//            Box box = k == 0 ? startBox : endBox;
//            boolean outside = box != lcaBox;
//            retrace = true;
//            for (Point2D.Double point : line.lineGeometry.points) {
//                if (outside ? !box.contains(point, 0) : box.strictContains(point)) {
//                    retrace = false;
//                    break;
//                }
//            }
//        }
//        if (retrace) {
//            Tracer tracer = new Tracer(startBox, endBox);
//            line.lineGeometry.points = tracer.trace(startBox, endBox, line.getUsedStartSides(), line.getUsedEndSides());
//        }
    }

    /**
     * Joins any segments of the given lines that can be joined without the lines crossing any
     * rectangles, in order to minimize line corner count. Note that the lines must have the same
     * set of obstacle rectangles, that is, rectangles that the line cannot cross.
     *
     * @param lines the lines whose segments to join
     */
    static void cleanupLines(ArrayList<Line> lines) {
        cleanupLines(lines, null, null, true);
    }

    /**
     * Joins segments of the given lines that can be joined without the lines crossing any
     * rectangles, in order to minimize line corner count. Note that the lines must have the same
     * set of obstacle rectangles, that is, rectangles that the line cannot cross. For the i-th
     * line, only the first {@code startCount.get(i)} and the last {@code endCount.get(i)} segments
     * are changed.
     *
     * @param lines the lines whose segments to join
     * @param startCounts the number of segments from the start to cleanup for each line
     * @param endCounts the number of segments from the end to cleanup for each line
     * @param fixLabels whether to renumber line label segment indices
     */
    static void cleanupLines(ArrayList<Line> lines, ArrayList<Integer> startCounts, ArrayList<Integer> endCounts, boolean fixLabels) {
        if (lines.isEmpty()) {
            return;
        }

        /*
         * The set of obstacle rectangles is uniquely defined by the parents of the boxes a line connects
         * and their lca. We do not want the diagram's sides to count as obstacles for newly
         * expanded lines.
         */
        LinkedHashSet<AbstractContainer> obstacles = getObstacleRectangles(lines.get(0), true);
        obstacles.remove(lines.get(0).getDiagram());

        /**
         * Call multiple iterations of segments joining, as the the algorithm can produce new
         * joinable segments. Lines that require an arbitrarily large number of iteration can be
         * produced, but in practice 2 iterations are enough for most lines.
         */
        for (int i = 0; i < 3; i++) {
            joinSegments(lines, obstacles, true, startCounts, endCounts, fixLabels);
            joinSegments(lines, obstacles, false, startCounts, endCounts, fixLabels);
        }
    }

    /**
     * A line segment for segment count minimization, storing the left and right bounds within which
     * this segments can move. As with all {@link OrthogonalSegment}s, variable names assume the
     * segments are vertical.
     */
    private static class LineSegment extends OrthogonalSegment.OrthogonalLineSegment {

        /**
         * The left bound if this line segments, how much to left can it be moved without crossing
         * any rectangles.
         */
        double leftBound;
        /**
         * The right bound if this line segments, how much to right can it be moved without crossing
         * any rectangles.
         */
        double rightBound;
        /**
         * A flag used to mark segments inside rectangles for removal.
         */
        boolean toFix;

        /**
         * Creates a new {@code LineSegment}.
         *
         * @param x the abscissa of the segment.
         * @param top the smaller endpoint ordinate of the segment.
         * @param bottom the larger endpoint ordinate of the segment.
         * @param line the line this segment belongs to.
         * @param startPoint one of the endpoints of this segment. This point should belong to the
         * actual line, and changing it should change the line.
         * @param endPoint one of the endpoints of this segment. This point should belong to the
         * actual line, and changing it should change the line.
         */
        public LineSegment(double x, double top, double bottom, Line line, Point2D.Double startPoint, Point2D.Double endPoint) {
            super(x, top, bottom, line, startPoint, endPoint);
        }
    }

    /**
     * Joins the segments of the given lines in a single direction. {@code horizontal==true} means
     * that the sweepline is horizontal, thus vertical segments are joined. For the i-th line, only
     * the first {@code startCount.get(i)} and the last {@code endCount.get(i)} segments are
     * changed.
     *
     * @param lines the lines whose segments to join
     * @param obstacles the set of obstacle rectangles for these lines
     * @param horizontal the sweepline direction
     * @param startCounts the number of segments from the start to cleanup for each line
     * @param endCounts the number of segments from the end to cleanup for each line
     * @param fixLabels whether to renumber line label segment indices
     */
    static void joinSegments(ArrayList<Line> lines, LinkedHashSet<AbstractContainer> obstacles, boolean horizontal, ArrayList<Integer> startCounts, ArrayList<Integer> endCounts, boolean fixLabels) {
        if (lines.isEmpty()) {
            return;
        }

        double epsilon = lines.get(0).getDiagram().getEpsilon();

        ArrayList<OrthogonalSegment> segments = new ArrayList<>();
        /*
         * The set of each lines first segment in the current direction.
         */
        ArrayList<LineSegment> first = new ArrayList<>();

        /*
         * Construct the line segments by going along each line.
         */
        for (int l = 0; l < lines.size(); l++) {
            Line line = lines.get(l);
            int startCount = startCounts == null ? line.lineGeometry.points.size() : startCounts.get(l);
            int endCount = endCounts == null ? line.lineGeometry.points.size() : endCounts.get(l);

            ArrayList<Point2D.Double> points = line.lineGeometry.points;
            ArrayList<LineSegment> lineSegments = new ArrayList<>();
            for (int i = 0; i + 1 < points.size(); i++) {
                if (horizontal ? points.get(i).x == points.get(i + 1).x : points.get(i).y == points.get(i + 1).y) {
                    double pos = horizontal ? points.get(i).x : points.get(i).y;
                    double firstBound = horizontal ? points.get(i).y : points.get(i).x;
                    double secondBound = horizontal ? points.get(i + 1).y : points.get(i + 1).x;
                    double top = Math.min(firstBound, secondBound);
                    double bottom = Math.max(firstBound, secondBound);
                    LineSegment segment = new LineSegment(pos, top, bottom, line, points.get(i), points.get(i + 1));
                    if (i < startCount || points.size() - i - 1 <= endCount) {
                        segment.toFix = true;
                    }
                    lineSegments.add(segment);
                }
            }

            if (!lineSegments.isEmpty()) {
                first.add(lineSegments.get(0));
                for (int i = 0; i + 1 < lineSegments.size(); i++) {
                    lineSegments.get(i).next = lineSegments.get(i + 1);
                }

                segments.addAll(lineSegments);
            } else {
                first.add(null);
            }
        }

        segments.addAll(findSpacedSegments(obstacles, horizontal, 1. / 8));

        Collections.sort(segments);

        /*
         * Find the left and right bounds of each segment.
         */
        findLineBounds(segments, true, epsilon);
        findLineBounds(segments, false, epsilon);

        /*
         * For each line, greedily construct bundles of subsequent segements which fit into each
         * other's bound windows. Once the end of the line or a segments that doesn't fit into the
         * current winodw is found, merge the current bundle into a single segment.
         */
        for (int i = 0; i < lines.size(); i++) {
            Line line = lines.get(i);
            LineSegment curr = first.get(i);
            boolean loop = line.getStart() == line.getEnd();
            double leftBound = Double.NEGATIVE_INFINITY, rightBound = Double.POSITIVE_INFINITY;
            LinkedList<LineSegment> queue = new LinkedList<>();
            while (true) {
                /*
                 * If the current bundle is finished, merge it. If the line is a loop, it is not
                 * allowed to merge the very first segment with the very last segment, so the bundle
                 * is also finished.
                 */
                if (!queue.isEmpty()
                        && (curr == null || curr.leftBound > rightBound || curr.rightBound < leftBound
                        || (loop && queue.peekFirst().startPoint == line.getStartPoint() && curr.endPoint == line.getEndPoint()))) {
                    /*
                     * The position of the new segment is the weighed average of the merged
                     * segments.
                     */
                    double pos = 0, len = 0;
                    for (LineSegment segment : queue) {
                        double d = segment.getEnd() - segment.getStart();
                        pos += segment.getPos() * d;
                        len += d;
                    }
                    pos /= len;
                    if (pos < leftBound) {
                        pos = leftBound;
                    }
                    if (pos > rightBound) {
                        pos = rightBound;
                    }
                    for (LineSegment segment : queue) {
                        if (Math.abs(segment.pos - pos) < epsilon) {
                            pos = segment.pos;
                            break;
                        }
                    }

                    while (!queue.isEmpty()) {
                        queue.poll().move(pos);
                    }

                    if (curr == null) {
                        break;
                    }

                    leftBound = Double.NEGATIVE_INFINITY;
                    rightBound = Double.POSITIVE_INFINITY;
                }

                if (curr == null) {
                    break;
                }

                if (curr.leftBound > leftBound) {
                    leftBound = curr.leftBound;
                }
                if (curr.rightBound < rightBound) {
                    rightBound = curr.rightBound;
                }

                queue.add(curr);

                curr = (LineSegment) curr.next;
            }

            /*
             * After segment merging the line has 0-length segments, cull those; also reassign label
             * segment indices if necessary.
             */
            if (fixLabels) {
                line.lineGeometry.cullPoints(line.getLabels());
            } else {
                line.lineGeometry.cullPoints();
            }
        }
    }

    /**
     * A segment part as an element of a sweepline, storing the part's coordinates.
     */
    static private class Part implements Comparable<Part> {

        /**
         * The abscissa of the segment.
         */
        double x;
        /**
         * The smaller ordinate of the segment part.
         */
        double top;
        /**
         * The larger ordinate of the segment part.
         */
        double bottom;

        /**
         * Creates a new segment part.
         *
         * @param x the abscissa of the segment.
         * @param top the smaller ordinate of the segment part.
         * @param bottom the larger ordinate of the segment part.
         */
        Part(double x, double top, double bottom) {
            this.x = x;
            this.top = top;
            this.bottom = bottom;
        }

        /**
         * Creates a new segment part from the whole of the given segment.
         *
         * @param segment the segment from which to create the segment part.
         */
        Part(OrthogonalSegment segment) {
            this.x = segment.getPos();
            this.top = segment.getStart();
            this.bottom = segment.getEnd();
        }

        @Override
        public int compareTo(Part p) {
            int cmp = Double.compare(top, p.top);
            if (cmp != 0) {
                return cmp;
            }
            return Double.compare(bottom, p.bottom);
        }
    }

    /**
     * A treap node for storing a segment part as a treap element.
     */
    static private class Node implements Comparable<Node> {

        /**
         * The left child of this node.
         */
        Node left;
        /**
         * The right child of this node.
         */
        Node right;
        /**
         * The segment part this node corresponds to.
         */
        Part key;
        /**
         * The randomly generated heap priority for this node.
         */
        int priority;
        /**
         * The maximum abscissa of all parts in the treap this node is the root of.
         */
        double max;

        /**
         * Creates a new treap node from the given segment part.
         *
         * @param key the segment part to use as this node's key.
         */
        private Node(Part key) {
            this.key = key;
            Random rand = new Random();
            priority = rand.nextInt();
        }

        /**
         * Splits this treap into two such that the first contains all elements with key.top smaller
         * than {@code x}, and the second contains all elements with key.top equal to or larger than
         * {@code x}.
         *
         * @param x the key value by which to split the treap
         * @return a pair of treaps, with left containing elements smaller than {@code x} and right
         * elements larger than {@code x}.
         */
        Pair<Node, Node> split(double x) {
            Pair<Node, Node> res;
            if (x <= key.top) {
                Pair<Node, Node> subSplit = (left != null ? left.split(x) : new Pair<Node, Node>(null, null));
                left = subSplit.getSecond();
                res = new Pair<>(subSplit.getFirst(), this);
            } else {
                Pair<Node, Node> subSplit = (right != null ? right.split(x) : new Pair<Node, Node>(null, null));
                right = subSplit.getFirst();
                res = new Pair<>(this, subSplit.getSecond());
            }
            if (res.getFirst() != null) {
                res.getFirst().update();
            }
            if (res.getSecond() != null) {
                res.getSecond().update();
            }
            return res;
        }

        /**
         * Inserts the given {@code node} into {@code treap}. Return the resulting treap, as
         * {@code treap} may be null.
         *
         * @param treap the treap into which to insert the new node. May be {@code null}.
         * @param node the new node to insert.
         * @return the resulting treap.
         */
        static Node insert(Node treap, Node node) {
            Node res;
            if (treap == null) {
                res = node;
            } else if (node.priority > treap.priority) {
                Pair<Node, Node> subSplit = treap.split(node.key.top);
                node.left = subSplit.getFirst();
                node.right = subSplit.getSecond();
                res = node;
            } else {
                if (node.key.compareTo(treap.key) < 0) {
                    treap.left = insert(treap.left, node);
                } else {
                    treap.right = insert(treap.right, node);
                }
                res = treap;
            }
            res.update();
            return res;
        }

        /**
         * Merges the two given treaps. It is required that all keys in the left treap are smaller
         * than any key in the right.
         *
         * @param left one of the treaps to merge, with smaller keys than {@code right}
         * @param right one of the treaps to merge, with larger keys than {@code left}
         * @return the merged treap
         */
        static Node merge(Node left, Node right) {
            Node res;
            if (left == null) {
                res = right;
            } else if (right == null) {
                res = left;
            } else if (left.priority > right.priority) {
                left.right = merge(left.right, right);
                res = left;
            } else {
                right.left = merge(left, right.left);
                res = right;
            }
            if (res != null) {
                res.update();
            }
            return res;
        }

        /**
         * Erases the given node from this treap. Returns the resulting treap, as the node to erase
         * may be the root. This treap should contain the given node.
         *
         * @param node the node to erase
         * @return the resulting treap
         */
        Node erase(Node node) {
            if (this == node) {
                return merge(node.left, node.right);
            } else {
                if (node.key.compareTo(key) < 0) {
                    left = left.erase(node);
                } else {
                    right = right.erase(node);
                }
                update();
                return this;
            }
        }

        /**
         * fins the largest node in this treap whose {@code key.top} is smaller than {@code x}.
         *
         * @param x the key value to find
         * @return the largest node in this treap whose {@code key.top} is smaller than {@code x}
         */
        Node find(double x) {
            if (key.top <= x) {
                if (right == null) {
                    return this;
                }
                Node y = right.find(x);
                return y == null ? this : y;
            }
            return left == null ? null : left.find(x);
        }

        /**
         * Updates this node's {@link #max}.
         */
        void update() {
            max = key.x;
            if (left != null) {
                max = Math.max(max, left.max);
            }
            if (right != null) {
                max = Math.max(max, right.max);
            }
        }

        @Override
        public int compareTo(Node t) {
            return key.compareTo(t.key);
        }
    }

    /**
     * A treap for storing the segment sweepline used in segment joining.
     */
    static private class SweepTreap {

        /**
         * The root node of this treap.
         */
        Node root;

        /**
         * Creates a new sweepline with an infinitely long dummy segment with an abscissa of
         * negative infinity.
         */
        public SweepTreap() {
            root = Node.insert(root, new Node(new Part(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY)));
        }

        /**
         * Adds the given part to the sweepline.
         *
         * @param part the part to add
         */
        void add(Part part) {
            assert part.top < part.bottom;
            /*
             * the two segments partially covered by the new part.
             */
            Node first = root.find(part.top);
            Node second = root.find(part.bottom);

            if (first != second) {
                /*
                 * We split the treap so that all segments fully covered by the new one are in a
                 * single part. When marging back together, we do not merge this part.
                 */
                Pair<Node, Node> leftSplit = root.split(first.key.bottom);
                Pair<Node, Node> rightSplit = leftSplit.getSecond().split(second.key.top);

                Node left = leftSplit.getFirst();
                Node right = rightSplit.getSecond();

                /*
                 * If the partially covered segments are now 0-length remove them.
                 */
                first.key.bottom = part.top;
                second.key.top = part.bottom;
                if (first.key.bottom == first.key.top) {
                    left = left.erase(first);
                }
                if (second.key.bottom == second.key.top) {
                    right = right.erase(second);
                }
                root = Node.merge(left, right);
            } else {
                /*
                 * if the new part fits within an existing part, that part is split into two.
                 */
                second = new Node(new Part(first.key.x, part.bottom, first.key.bottom));
                first.key.bottom = part.top;
                /*
                 * If either of the two parts is 0-length it is not used.
                 */
                if (first.key.bottom == first.key.top) {
                    root = root.erase(first);
                }
                if (second.key.bottom != second.key.top) {
                    root = Node.insert(root, second);
                }
            }
            root = Node.insert(root, new Node(part));
        }

        /**
         * Find the maximum key value among the sweepline parts between the given coordinates.
         *
         * @param l the smallest coordinate to check
         * @param r the largest coordinate to check
         * @return the maximum key value among the sweepline parts between the given coordinates.
         */
        double max(double l, double r) {
            Node first = root.find(l);

            Pair<Node, Node> leftSplit = root.split(first.key.top);
            Pair<Node, Node> rightSplit = leftSplit.getSecond().split(r);

            double res = rightSplit.getFirst().max;

            root = Node.merge(leftSplit.getFirst(), Node.merge(rightSplit.getFirst(), rightSplit.getSecond()));

            return res;
        }
    }

    /**
     * Finds a single bound for all the line segments among the given segments. If
     * {@code right==true} the sweepline goes from left to right, thus the left bound is found.
     *
     * @param segments the segments to check, both line and rectangle segments, ordered from left to
     * right.
     * @param right whether to move sweepline right
     * @param epsilon the bounds are narrowed by this epsilon value
     */
    private static void findLineBounds(ArrayList<OrthogonalSegment> segments, boolean right, double epsilon) {
        SweepTreap sweep = new SweepTreap();

        int n = segments.size();
        for (int i = right ? 0 : (n - 1); right ? i < n : i >= 0; i += right ? +1 : -1) {
            OrthogonalSegment segment = segments.get(i);
            if (segment instanceof LineSegment) {
                LineSegment lineSegment = (LineSegment) segment;
                if (lineSegment.toFix) {
                    double max = sweep.max(lineSegment.getStart(), lineSegment.getEnd());
                    if (right) {
                        lineSegment.leftBound = max + epsilon;
                    } else {
                        lineSegment.rightBound = -max - epsilon;
                    }
                } else {
                    lineSegment.leftBound = lineSegment.rightBound = lineSegment.getPos();
                }
            } else {
                sweep.add(new Part((right ? 1 : -1) * segment.getPos(), segment.getStart(), segment.getEnd()));
            }
        }
    }

    /**
     * A line segment for the line correcting algorithm. It stores the possible splittings to
     * multiple segments on the left side and on the right side of the original segment. Each of
     * theses segments has the property that we can move the corresponding part (projection) of the
     * original segment in place of the segment in the splitting. As with all
     * {@link OrthogonalSegment}s, variable names assume the segments are vertical.
     */
    private static class SplitSegment extends OrthogonalSegment.OrthogonalLineSegment {

        /**
         * The values for the segments in the left splitting. If the segment is vertical, then they
         * correspond to the abscissas, otherwise to the ordinates of the segments. The segments are
         * stored in direction from the start point to the end point of the segment.
         */
        ArrayList<Double> leftVal;
        /**
         * The positions of the segment endpoints in the left splitting. If the segment is vertical,
         * then they correspond to the ordinates, otherwise to the abscissas of the segment end
         * points. The segments are stored in direction from the start point to the end point of the
         * segment.
         */
        ArrayList<Double> leftPos;
        /**
         * The values for the segments in the right splitting. If the segment is vertical, then they
         * correspond to the abscissas, otherwise to the ordinates of the segments. The segments are
         * stored in direction from the start point to the end point of the segment.
         */
        ArrayList<Double> rightVal;
        /**
         * The positions of the segment endpoints in the right splitting. If the segment is
         * vertical, then they correspond to the ordinates, otherwise to the abscissas of the
         * segment end points. The segments are stored in direction from the start point to the end
         * point of the segment.
         */
        ArrayList<Double> rightPos;
        /**
         * These points store the final result of correcting this particular line segment.
         */
        ArrayList<Point2D.Double> points;

        /**
         * Creates a new {@link SplitSegment} instance.
         *
         * @param x the abscissa of the segment
         * @param top the top of the segment
         * @param bottom the bottom of the segment
         * @param line the line that has the segment
         * @param startPoint the start point of the segment
         * @param endPoint the end point of the segment
         */
        SplitSegment(double x, double top, double bottom, Line line, Point2D.Double startPoint, Point2D.Double endPoint) {
            super(x, top, bottom, line, startPoint, endPoint);
            leftVal = new ArrayList<>();
            leftPos = new ArrayList<>();
            rightVal = new ArrayList<>();
            rightPos = new ArrayList<>();
            points = new ArrayList<>();
        }

        @Override
        void updateLength() {
            if (isHorizontal()) {
                setStart(Math.min(startPoint.x, endPoint.x));
                setEnd(Math.max(startPoint.x, endPoint.x));
            } else {
                setStart(Math.min(startPoint.y, endPoint.y));
                setEnd(Math.max(startPoint.y, endPoint.y));
            }
        }
    }

    /**
     * Corrects a set of orthogonal lines so that they do not intersect rectangles which they should
     * not.
     *
     * @param lines the lines to correct
     */
    static void correctOrthogonalLines(ArrayList<Line> lines) {
        if (lines.isEmpty()) {
            return;
        }

        /*
         * The set of obstacle rectangles is uniquely defined by the parents of the boxes a line connects
         * and their lca. We do not want the diagram's sides to count as obstacles for newly
         * expanded lines.
         */
        LinkedHashSet<AbstractContainer> rectangles = getObstacleRectangles(lines.get(0), false);

        /*
         * Joins the line segments inside the rectangles. This is done to cut out all the line twists
         * inside the rectangles because they should not be considered during the correcting.
         */
        cleanupIntersections(lines, rectangles);

        /*
         * Creates all line segments for the sweepline. For each line segment, stores a pointer to
         * the consecutive segment of the line. Also stores the first segment for each line.
         */
        ArrayList<SplitSegment> verticalLineSegments = new ArrayList<>();
        ArrayList<SplitSegment> horizontalLineSegments = new ArrayList<>();
        SplitSegment[] first = new SplitSegment[lines.size()];
        for (int i = 0; i < lines.size(); i++) {
            Line line = lines.get(i);
            ArrayList<Point2D.Double> points = line.lineGeometry.points;
            SplitSegment prev = null;
            for (int j = 0; j + 1 < points.size(); j++) {
                boolean vertical = points.get(j).x == points.get(j + 1).x;
                double pos, firstBound, secondBound;
                if (vertical) {
                    pos = points.get(j).x;
                    firstBound = points.get(j).y;
                    secondBound = points.get(j + 1).y;
                } else {
                    pos = points.get(j).y;
                    firstBound = points.get(j).x;
                    secondBound = points.get(j + 1).x;
                }
                double top = Math.min(firstBound, secondBound);
                double bottom = Math.max(firstBound, secondBound);
                SplitSegment segment = new SplitSegment(pos, top, bottom, line, points.get(j), points.get(j + 1));
                if (vertical) {
                    verticalLineSegments.add(segment);
                } else {
                    horizontalLineSegments.add(segment);
                }

                if (prev != null) {
                    prev.next = segment;
                } else {
                    first[i] = segment;
                }
                prev = segment;
            }
        }

        double epsilon = lines.get(0).getDiagram().getEpsilon();

        /*
         * Finds and adds rectangle vertical segments. Then calculates the left and right splitting for
         * each vertical line segment. After that, for each vertical line segment, merges the
         * splittings' segments to multiple segments so that they do not intersect any rectangles.
         */
        ArrayList<OrthogonalSegment> verticalSegments = new ArrayList<>();
        verticalSegments.addAll(verticalLineSegments);
        verticalSegments.addAll(findProlongedSegments(rectangles, true, epsilon));
        Collections.sort(verticalSegments);
        splitSegments(verticalSegments, true, true);
        splitSegments(verticalSegments, true, false);
        mergeSegments(verticalLineSegments, false);

        /*
         * Since horizontal segments endpoints may have changed, update them.
         */
        for (OrthogonalSegment segment : horizontalLineSegments) {
            segment.updateLength();
        }

        /*
         * Finds and adds rectangle horizontal segments. Then calculates the top and bottom splitting for
         * each horizontal line segment. After that, for each horizontal line segment, merges the
         * splittings' segments to multiple segments so that they do not intersect any rectangles.
         */
        ArrayList<OrthogonalSegment> horizontalSegments = new ArrayList<>();
        horizontalSegments.addAll(horizontalLineSegments);
        horizontalSegments.addAll(findProlongedSegments(rectangles, false, epsilon));
        Collections.sort(horizontalSegments);
        splitSegments(horizontalSegments, false, true);
        splitSegments(horizontalSegments, false, false);
        mergeSegments(horizontalLineSegments, true);

        /*
         * Now that each line segment has been changed to multiple segments that do not cross rectangles,
         * each line is constructed from the consecutive composition of all the new segments.
         */
        for (int i = 0; i < lines.size(); i++) {
            Line line = lines.get(i);

            ArrayList<LineLabel> lineLabels = line.getLabels();

            /*
             * Collects labels for each of the old segments.
             */
            ArrayList<ArrayList<LineLabel>> segmentLabels = new ArrayList<>();
            for (int j = 0; j + 1 < line.lineGeometry.points.size(); j++) {
                segmentLabels.add(new ArrayList<LineLabel>());
            }
            for (LineLabel label : lineLabels) {
                segmentLabels.get(label.segmentIndex).add(label);
            }

            /*
             * Builds new point list.
             */
            ArrayList<Point2D.Double> newPoints = new ArrayList<>();
            int oldIndex = 0, newIndex = 0;
            for (SplitSegment segment = first[i]; segment != null; segment = (SplitSegment) segment.next) {
                ArrayList<Point2D.Double> points = segment.points;
                newPoints.addAll(points);

                /**
                 * Renumbers label segment indices to the new segments to which the current segment
                 * was split. The new index for a label is an index of a segment inside whose
                 * projection this label center projection resides.
                 */
                ArrayList<LineLabel> labels = segmentLabels.get(oldIndex);
                if (!labels.isEmpty()) {
                    boolean segmentHorizontal = segment.isHorizontal();
                    boolean positiveDirection = segment.start < segment.end;
                    Collections.sort(labels, new LineLabelComparator(segmentHorizontal, positiveDirection));
                    double minPos = positiveDirection ? segment.start : segment.end;
                    double maxPos = positiveDirection ? segment.end : segment.start;
                    /**
                     * If the projection of the center of a label is outside the projection of the
                     * old segment, moves the segment inside it.
                     */
                    for (LineLabel label : labels) {
                        Point2D.Double center = label.getCenter();
                        double pos = segmentHorizontal ? center.x : center.y;
                        if (pos < minPos) {
                            if (segmentHorizontal) {
                                center.x = minPos;
                            } else {
                                center.y = minPos;
                            }
                            label.setCenter(center);
                        } else if (pos > maxPos) {
                            if (segmentHorizontal) {
                                center.x = maxPos;
                            } else {
                                center.y = maxPos;
                            }
                            label.setCenter(center);
                        }
                    }

                    /**
                     * Assigns new segment indices for the labels. Iterates two pointers, one for
                     * the sorted label array, other for the segment index.
                     */
                    int labelIndex = 0;
                    double labelPos = segmentHorizontal ? labels.get(0).getCenterX() : labels.get(0).getCenterY();
                    for (int pointIndex = 0; pointIndex < points.size() && labelIndex < labels.size(); pointIndex += 2) {
                        double firstPos = segmentHorizontal ? points.get(pointIndex).x : points.get(pointIndex).y;
                        double secondPos = segmentHorizontal ? points.get(pointIndex + 1).x : points.get(pointIndex + 1).y;
                        while (labelIndex < labels.size() && Math.min(firstPos, secondPos) <= labelPos && labelPos <= Math.max(firstPos, secondPos)) {
                            labels.get(labelIndex).segmentIndex = newIndex + pointIndex;
                            labelIndex++;
                            if (labelIndex < labels.size()) {
                                labelPos = segmentHorizontal ? labels.get(labelIndex).getCenterX() : labels.get(labelIndex).getCenterY();
                            }
                        }
                    }
                }

                oldIndex++;
                newIndex += segment.points.size();
            }
            line.lineGeometry.points = newPoints;
            line.lineGeometry.cullPoints(lineLabels);
        }
    }

    /**
     * A comparator for sorting the line labels of a single segment so that they follow the
     * segment's direction.
     */
    static class LineLabelComparator implements Comparator<LineLabel> {

        /**
         * Whether the labels should be compared by their abscissas.
         */
        boolean compareHorizontal;
        /**
         * Whether the lowest-coordinate labels should come first.
         */
        boolean positiveDirection;

        /**
         * Creates a new line label comparator comparing labels in the given direction.
         *
         * @param compareHorizontal whether the labels should be compared by their abscissas or
         * ordinates.
         * @param positiveDirection whether the lowest-coordinate labels should come first.
         */
        public LineLabelComparator(boolean compareHorizontal, boolean positiveDirection) {
            this.compareHorizontal = compareHorizontal;
            this.positiveDirection = positiveDirection;
        }

        @Override
        public int compare(LineLabel label1, LineLabel label2) {
            Point2D.Double center1 = label1.getCenter();
            Point2D.Double center2 = label2.getCenter();
            int res = compareHorizontal ? Double.compare(center1.x, center2.x) : Double.compare(center1.y, center2.y);
            return positiveDirection ? res : -res;
        }
    }

    /**
     * Removes occurrences of the whole line segments strictly inside the given rectangles. Does
     * this by merging the segments inside the rectangle with previous segment that is not strictly
     * inside the rectangle.<br/>
     *
     * Note: there is also one case where it is not possible to remove such segment:
     * <pre>
     *   |
     * +-|------+
     * | |      |
     * | +----+ |
     * |      | |
     * +------|-+
     *        |
     * </pre>
     *
     * @param lines the lines to clean up
     * @param obstacleRectangles the rectangles inside which to clean up lines
     */
    static void cleanupIntersections(ArrayList<Line> lines, LinkedHashSet<AbstractContainer> obstacleRectangles) {
        /*
         * Collects all the vertical and horizontal line segments. For each line segment, also
         * stores the next consecutive segment.
         */
        ArrayList<LineSegment> verticalLineSegments = new ArrayList<>();
        ArrayList<LineSegment> horizontalLineSegments = new ArrayList<>();
        LineSegment[] first = new LineSegment[lines.size()];
        for (int i = 0; i < lines.size(); i++) {
            Line line = lines.get(i);
            ArrayList<Point2D.Double> points = line.lineGeometry.points;
            LineSegment prev = null;
            for (int j = 0; j + 1 < points.size(); j++) {
                boolean vertical = points.get(j).x == points.get(j + 1).x;
                double pos, firstBound, secondBound;
                if (vertical) {
                    pos = points.get(j).x;
                    firstBound = points.get(j).y;
                    secondBound = points.get(j + 1).y;
                } else {
                    pos = points.get(j).y;
                    firstBound = points.get(j).x;
                    secondBound = points.get(j + 1).x;
                }
                double top = Math.min(firstBound, secondBound);
                double bottom = Math.max(firstBound, secondBound);
                LineSegment segment = new LineSegment(pos, top, bottom, line, points.get(j), points.get(j + 1));
                if (vertical) {
                    verticalLineSegments.add(segment);
                } else {
                    horizontalLineSegments.add(segment);
                }

                if (prev != null) {
                    prev.next = segment;
                } else {
                    first[i] = segment;
                }
                prev = segment;
            }
        }

        /*
         * Finds all the line segments that are inside the rectangles.
         */
        findInsideSegments(verticalLineSegments, obstacleRectangles, true);
        findInsideSegments(horizontalLineSegments, obstacleRectangles, false);

        /*
         * For each line, traverses all of its segments and for each segment inside some rectangle, merges
         * it with previous one with the same direction, if the segment between them is also inside
         * the rectangle.
         */
        for (int i = 0; i < lines.size(); i++) {
            for (LineSegment curr = first[i]; curr != null; curr = (LineSegment) curr.next) {
                if (curr.toFix) {
                    LineSegment next = (LineSegment) curr.next;
                    if (next != null && next.toFix) {
                        if (next.startPoint.x == next.endPoint.x) {
                            next.startPoint.x = next.endPoint.x = curr.startPoint.x;
                        } else {
                            next.startPoint.y = next.endPoint.y = curr.startPoint.y;
                        }
                        curr = next;
                    }
                }
            }
            lines.get(i).lineGeometry.cullPoints(lines.get(i).labels);
        }
    }

    /**
     * Finds all the line segments that are inside some of the given rectangles. Does this by
     * setting the corresponding flag of the segment.
     *
     * @param lineSegments the line segments to check
     * @param obstacles the rectangles within which to search
     * @param segmentsVertical whether the segment direction is vertical or horizontal
     */
    static void findInsideSegments(ArrayList<LineSegment> lineSegments, LinkedHashSet<AbstractContainer> obstacles, boolean segmentsVertical) {
        /*
         * Adds the rectangle segments and line segments together and sorts them. Then runs a sweepline
         * over the segments. Maintains the sorted set of the rectangles that are intersected by a
         * current position in sweepline. With the line segment event, searches the first open rectangle
         * in the set that intersects the segment. If such is found, then the segment is inside the
         * rectangle, otherwise it is outside any rectangle.
         */
        ArrayList<OrthogonalSegment> segments = new ArrayList<>();
        segments.addAll(lineSegments);
        segments.addAll(findProlongedSegments(obstacles, segmentsVertical, 0));
        Collections.sort(segments);

        TreeSet<Part> openRectangles = new TreeSet<>();
        SegmentType openType = segmentsVertical ? SegmentType.LEFT : SegmentType.TOP;

        for (int i = 0; i < segments.size(); i++) {
            if (segments.get(i) instanceof RectangleSegment) {
                RectangleSegment segment = (RectangleSegment) segments.get(i);
                Part part = new Part(segment);
                if (segment.getSegmentType() == openType) {
                    openRectangles.add(part);
                } else {
                    openRectangles.remove(part);
                }
            } else {
                LineSegment segment = (LineSegment) segments.get(i);
                Part part = openRectangles.lower(new Part(0, segment.getStart(), Double.POSITIVE_INFINITY));
                if (part != null && part.bottom >= segment.getEnd()) {
                    segment.toFix = true;
                }
            }
        }
    }

    /**
     * Splits the line segments among the given segments into paths along their side which do not
     * intersect any rectangles.
     *
     * @param segments a sorted set of both line segments and rectangle segments of rectangles the
     * given lines should not cross
     * @param verticalSegments whether the given segments are vertical
     * @param right whether to split the segments into paths along their right (lower) side, or
     * their left (upper) side
     */
    private static void splitSegments(ArrayList<OrthogonalSegment> segments, boolean verticalSegments, boolean right) {
        TreeSet<Part> openRectangles = new TreeSet<>();
        SweepTreap sweep = new SweepTreap();

        SegmentType openType = verticalSegments
                ? right ? SegmentType.LEFT : SegmentType.RIGHT
                : right ? SegmentType.TOP : SegmentType.BOTTOM;

        /*
         * We keep a sorted set of currently open rectangles and a treap of closed rectangles, storing a
         * sweepline of still visible closed rectangles. When we reach a line segment, we find which
         * rectangles the line segment crosses using the set of open rectangles, and find how far from segment
         * can the new paths be drawn by seeing which closed rectangles are visible from the segment.
         */
        int n = segments.size();
        for (int i = (right ? 0 : n - 1); (right ? i < n : i >= 0); i += (right ? 1 : -1)) {
            if (segments.get(i) instanceof RectangleSegment) {
                RectangleSegment segment = (RectangleSegment) segments.get(i);
                Part part = new Part(segment);
                if (segment.getSegmentType() == openType) {
                    openRectangles.add(part);
                } else {
                    openRectangles.remove(part);
                    sweep.add(new Part((right ? part.x : -part.x), part.top, part.bottom));
                }
            } else {
                SplitSegment segment = (SplitSegment) segments.get(i);
                Line line = segment.getLine();
                double epsilon = line.getDiagram().getEpsilon() * 2;

                ArrayList<Double> tpos = new ArrayList<>(),
                        lval = new ArrayList<>(), rval = new ArrayList<>();
                double x = segment.getPos(), top = segment.getStart(), bottom = segment.getEnd();

                Part firstPart = openRectangles.lower(new Part(0, top, Double.POSITIVE_INFINITY));
                if (firstPart == null) {
                    firstPart = new Part(0, top, Double.POSITIVE_INFINITY);
                }
                /*
                 * The set of rectangles this line segment crosses. This includes the line start and end
                 * rectangles for the segments connected to them.
                 */
                NavigableSet<Part> portion = openRectangles.subSet(firstPart, top <= firstPart.bottom,
                        new Part(0, bottom, Double.POSITIVE_INFINITY), true);

                boolean down = verticalSegments
                        ? segment.startPoint.y < segment.endPoint.y
                        : segment.startPoint.x < segment.endPoint.x;

                /*
                 * We build a window for each part of the line segment, where parts are separated by
                 * rectangle sides the segment crosses, with the rectangles extended by epsilon. For all parts
                 * the furthest it can go from the original segment is the nearest closed rectangle, found
                 * using the treap. The nearest edge of the window is the segment itself for parts
                 * which do not cross a rectangle, and the side of the rectangle for parts that do.
                 */
                double prevPos = top;
                for (Part part : portion) {
                    /*
                     * If we're not currently inside a rectangle, create a part until the next rectangle.
                     */
                    if (prevPos < part.top) {
                        tpos.add(down ? part.top : prevPos);
                        double val = sweep.max(prevPos, part.top) + epsilon;
                        lval.add(right ? val : x);
                        rval.add(right ? x : -val);
                        prevPos = part.top;
                    }

                    /*
                     * If we are currently inside a rectangle, create a part until the end of the rectangle or
                     * the segment, whichever comes first.
                     */
                    double endPos = Math.min(part.bottom, bottom);
                    tpos.add(down ? endPos : prevPos);
                    double val = sweep.max(prevPos, endPos);
                    lval.add((right ? val : part.x) + epsilon);
                    rval.add((right ? part.x : -val) - epsilon);
                    prevPos = endPos;
                }
                /*
                 * If the segment continues beyond the last rectangle, create a part for this
                 * continuation.
                 */
                if (prevPos < bottom) {
                    tpos.add(down ? bottom : prevPos);
                    double val = sweep.max(prevPos, bottom) + epsilon;
                    lval.add(right ? val : x);
                    rval.add(right ? x : -val);
                }

                if (!down) {
                    Collections.reverse(tpos);
                    Collections.reverse(lval);
                    Collections.reverse(rval);
                }

                /*
                 * For the first and last segment of a line, the end parts need to be bounded by the
                 * boxes the line connects.
                 *
                 */
                if (segment.startPoint == line.getStartPoint()) {
                    Box box = (Box) line.getStart();
                    double leftSide = verticalSegments ? box.getLeft() : box.getTop();
                    double rightSide = verticalSegments ? box.getRight() : box.getBottom();
                    lval.set(0, right ? leftSide + epsilon : x);
                    rval.set(0, right ? x : rightSide - epsilon);
                }
                if (segment.endPoint == line.getEndPoint()) {
                    Box box = (Box) line.getEnd();
                    double leftSide = verticalSegments ? box.getLeft() : box.getTop();
                    double rightSide = verticalSegments ? box.getRight() : box.getBottom();
                    int last = lval.size() - 1;
                    lval.set(last, right ? leftSide + epsilon : x);
                    rval.set(last, right ? x : rightSide - epsilon);
                }

                /*
                 * The segment parts and then greedily merged as long as they have a common window.
                 */
                ArrayList<Double> val = new ArrayList<>(), pos = new ArrayList<>();

                double leftBound = Double.NEGATIVE_INFINITY, rightBound = Double.POSITIVE_INFINITY;
                for (int j = 0; j < tpos.size(); j++) {
                    double leftVal = lval.get(j), rightVal = rval.get(j);
                    if (rightBound < leftVal || rightVal < leftBound) {
                        val.add(x < leftBound ? leftBound : x > rightBound ? rightBound : x);
                        pos.add(tpos.get(j - 1));
                        leftBound = Double.NEGATIVE_INFINITY;
                        rightBound = Double.POSITIVE_INFINITY;
                    }
                    if (leftBound < leftVal) {
                        leftBound = leftVal;
                    }
                    if (rightVal < rightBound) {
                        rightBound = rightVal;
                    }
                }
                if (!tpos.isEmpty()) {
                    val.add(x < leftBound ? leftBound : x > rightBound ? rightBound : x);
                    pos.add(tpos.get(tpos.size() - 1));
                }

                if (right) {
                    segment.leftPos = pos;
                    segment.leftVal = val;
                } else {
                    segment.rightPos = pos;
                    segment.rightVal = val;
                }
            }
        }
    }

    /**
     * Takes a set of SplitSegments with pre-calculated subdivisions and chooses a path along them
     * to create the segment's replacement set of points. We move the endpoints of the segments to
     * match those of the new points sets. This is needed as those points will affect the splitting
     * of neighboring segments.
     *
     * @param segments the segments whose subdivisions to merge
     * @param segmentsHorizontal whether the given set of segments are horizontal
     */
    static void mergeSegments(ArrayList<SplitSegment> segments, boolean segmentsHorizontal) {
        for (SplitSegment segment : segments) {
            ArrayList<Double> leftPos = segment.leftPos;
            ArrayList<Double> leftVal = segment.leftVal;
            ArrayList<Double> rightPos = segment.rightPos;
            ArrayList<Double> rightVal = segment.rightVal;
            ArrayList<Point2D.Double> points = segment.points;

            /*
             * We start at the start of the segment and take parts along it until we reach the end.
             */
            double currPos, endPos;
            if (segmentsHorizontal) {
                currPos = segment.startPoint.x;
                endPos = segment.endPoint.x;
            } else {
                currPos = segment.startPoint.y;
                endPos = segment.endPoint.y;
            }

            boolean right = currPos < endPos;

            int leftPtr = 0, rightPtr = 0;

            double segVal = segment.getPos();
            while (currPos != endPos) {
                /*
                 * Go along each side until we get the next potential part to choose.
                 */
                while (right ? leftPos.get(leftPtr) <= currPos : leftPos.get(leftPtr) >= currPos) {
                    leftPtr++;
                }
                while (right ? rightPos.get(rightPtr) <= currPos : rightPos.get(rightPtr) >= currPos) {
                    rightPtr++;
                }

                double leftNextPos = leftPos.get(leftPtr);
                double rightNextPos = rightPos.get(rightPtr);
                double leftNextVal = leftVal.get(leftPtr);
                double rightNextVal = rightVal.get(rightPtr);

                /**
                 * We choose the part which will get us closer to the end of the segment. Is both
                 * are equal in this regard, choose the part closer to the original segment.
                 */
                boolean goLeftPath;
                if (right) {
                    if (leftNextPos > rightNextPos) {
                        goLeftPath = true;
                    } else if (leftNextPos < rightNextPos) {
                        goLeftPath = false;
                    } else {
                        goLeftPath = Math.abs(segVal - leftNextVal) < Math.abs(segVal - rightNextVal);
                    }
                } else {
                    if (leftNextPos < rightNextPos) {
                        goLeftPath = true;
                    } else if (leftNextPos > rightNextPos) {
                        goLeftPath = false;
                    } else {
                        goLeftPath = Math.abs(segVal - leftNextVal) < Math.abs(segVal - rightNextVal);
                    }
                }

                double pos, val;
                if (goLeftPath) {
                    pos = leftNextPos;
                    val = leftNextVal;
                } else {
                    pos = rightNextPos;
                    val = rightNextVal;
                }

                /*
                 * We add both endpoints of the chosen part. The same points should never be added
                 * twice this way, as in that case the corresponding parts would have been merged
                 * beforehand.
                 */
                if (segmentsHorizontal) {
                    points.add(new Point2D.Double(currPos, val));
                    points.add(new Point2D.Double(pos, val));
                } else {
                    points.add(new Point2D.Double(val, currPos));
                    points.add(new Point2D.Double(val, pos));
                }

                currPos = pos;
            }

            /**
             * Move the endpoints of the original segment to match those of the new points, if the
             * points have changed.
             */
            if (!points.isEmpty()) {
                if (segmentsHorizontal) {
                    segment.startPoint.y = points.get(0).y;
                    segment.endPoint.y = points.get(points.size() - 1).y;
                } else {
                    segment.startPoint.x = points.get(0).x;
                    segment.endPoint.x = points.get(points.size() - 1).x;
                }
            }
        }
    }

    /**
     * Arranges orthogonal line ends along the box borders.
     *
     * @param diagram the diagram whose lines to arrange
     */
    static void arrangeLineEnds(Diagram diagram) {
        ArrayList<ArrayList<OrthogonalLineSegment>> lineSegments = Normalizer.generateLineSegments(diagram, null);
        ArrayList<ArrayList<RectangleSegment>> boxSegments = Normalizer.generateRectangleSegments(diagram, false);

        ArrayList<ArrayList<OrthogonalLineSegment>> endSegments = new ArrayList<>();
        for (ArrayList<OrthogonalLineSegment> singleDimensionSegments : lineSegments) {
            ArrayList<OrthogonalLineSegment> singleDimensionEndSegments = new ArrayList<>();
            for (OrthogonalLineSegment lineSegment : singleDimensionSegments) {
                if (lineSegment.isEndSegment()) {
                    singleDimensionEndSegments.add(lineSegment);
                }
            }
            endSegments.add(singleDimensionEndSegments);
        }
        for (int i = 0; i < 2; i++) {
            ArrayList<OrthogonalSegment> allSegments = new ArrayList<>();
            allSegments.addAll(boxSegments.get(i));
            allSegments.addAll(endSegments.get(i));
            ArrayList<Pair<OrthogonalSegment, OrthogonalSegment>> obstacleGraph = ObstacleGraph.findObstacleGraph(allSegments);
            arrangeOrthogonalLineEnds(endSegments.get(i), obstacleGraph, i == 1);
            Normalizer.updateSegments(endSegments.get(1 - i));
        }

        ArrayList<Line> straightLines = new ArrayList<>();
        for (Line line : diagram.getDescendantLines()) {
            if (line.lineGeometry.points.size() == 2 && (line.getType() == LineType.STRAIGHT || line.getType() == LineType.POLYLINE)) {
                straightLines.add(line);
            }
        }
        arrangeStraightLineEnds(straightLines);
    }

    /**
     * If the point is on some corner of the box, moves it along one of the allowed incident sides
     * by epsilon. If the given point is start point of the line, tries to move it in
     * counter-clockwise order along the box perimeter. If the given point is end point of the line,
     * tries to move it in clockwise order along the box perimeter.
     *
     * @param point the point to move from the corner of the box.
     * @param box the box to which this point is connected
     * @param allowedSides the allowed line end sides for this box
     * @param start whether the given point is start or end point of the straight line
     */
    static private void shiftCorner(Point2D.Double point, Box box, int allowedSides, boolean start) {
        double[] cx = {box.left, box.right, box.right, box.left};
        double[] cy = {box.top, box.top, box.bottom, box.bottom};
        double epsilon = box.getDiagram().getEpsilon();
        for (int i = 0; i < 4; i++) {
            if (Math.abs(point.x - cx[i]) < epsilon && Math.abs(point.y - cy[i]) < epsilon) {
                int j = (i + 3) % 4;
                boolean cw = (allowedSides & (1 << i)) > 0;
                boolean ccw = (allowedSides & (1 << j)) > 0;
                boolean moveCw = true;
                if (start && ccw) {
                    moveCw = false;
                }
                if (!start && !cw) {
                    moveCw = false;
                }
                double dx, dy;
                if (moveCw) {
                    int k = (i + 1) % 4;
                    dx = Math.signum(cx[k] - cx[i]) * epsilon;
                    dy = Math.signum(cy[k] - cy[i]) * epsilon;
                } else {
                    dx = Math.signum(cx[j] - cx[i]) * epsilon;
                    dy = Math.signum(cy[j] - cy[i]) * epsilon;
                }
                point.x = cx[i] + dx;
                point.y = cy[i] + dy;
                break;
            }
        }
    }

    /**
     * Arranges the orthogonal line end segments of the given direction along the box borders.
     *
     * @param lineSegments the end segments of the given direction
     * @param obstacleGraph the obstacle graph for the given end segments and diagram boxes
     * @param segmentsHorizontal the direction of the given end segments
     */
    static void arrangeOrthogonalLineEnds(
            ArrayList<OrthogonalLineSegment> lineSegments,
            ArrayList<Pair<OrthogonalSegment, OrthogonalSegment>> obstacleGraph,
            boolean segmentsHorizontal) {
        /**
         * For each end segment, stores the upper and lower visible segments in a corresponding map.
         */
        HashMap<OrthogonalLineSegment, ArrayList<OrthogonalSegment>> upperSegments = new HashMap<>(), lowerSegments = new HashMap<>();
        for (OrthogonalLineSegment lineSegment : lineSegments) {
            upperSegments.put(lineSegment, new ArrayList<OrthogonalSegment>());
            lowerSegments.put(lineSegment, new ArrayList<OrthogonalSegment>());
        }
        for (Pair<OrthogonalSegment, OrthogonalSegment> obstacle : obstacleGraph) {
            OrthogonalSegment first = obstacle.getFirst(), second = obstacle.getSecond();
            if (first instanceof OrthogonalLineSegment) {
                OrthogonalLineSegment upperSegment = (OrthogonalLineSegment) first;
                lowerSegments.get(upperSegment).add(second);
            }
            if (second instanceof OrthogonalLineSegment) {
                OrthogonalLineSegment lowerSegment = (OrthogonalLineSegment) second;
                upperSegments.get(lowerSegment).add(first);
            }
        }

        /**
         * Sort the segments by their position for the next step.
         */
        Collections.sort(lineSegments);

        /**
         * Moves each segment to the maximum position possible, and store this value as the maximum
         * possible position of this segment. To achieve this, move the segments in the descending
         * order of their position.
         */
        HashMap<OrthogonalLineSegment, Double> maxPos = new HashMap<>();
        for (int i = lineSegments.size() - 1; i >= 0; i--) {
            OrthogonalLineSegment lineSegment = lineSegments.get(i);
            double pos = Double.POSITIVE_INFINITY;
            for (OrthogonalSegment lowerSegment : lowerSegments.get(lineSegment)) {
                pos = Math.min(pos, lowerSegment.pos - lineSegment.findMinimumDistance(lowerSegment));
            }
            assert pos != Double.POSITIVE_INFINITY;
            lineSegment.move(pos);
            maxPos.put(lineSegment, pos);
        }

        /**
         * Moves all segments to the minimum possible position. To achieve this, move the segments
         * in the ascending order of their position.
         */
        for (OrthogonalLineSegment lineSegment : lineSegments) {
            double pos = Double.NEGATIVE_INFINITY;
            for (OrthogonalSegment upperSegment : upperSegments.get(lineSegment)) {
                pos = Math.max(pos, upperSegment.pos + upperSegment.findMinimumDistance(lineSegment));
            }
            lineSegment.move(pos);
        }

        /**
         * Finds all such end segment pairs that it is an obstacle of the obstacle graph, they are
         * connected to a common box side and the spacings of their original lines are equal.
         */
        ArrayList<Pair<OrthogonalLineSegment, OrthogonalLineSegment>> similar = new ArrayList<>();
        for (Pair<OrthogonalSegment, OrthogonalSegment> obstacle : obstacleGraph) {
            OrthogonalSegment first = obstacle.getFirst(), second = obstacle.getSecond();
            if (first instanceof OrthogonalLineSegment && second instanceof OrthogonalLineSegment) {
                OrthogonalLineSegment upperSegment = (OrthogonalLineSegment) first;
                OrthogonalLineSegment lowerSegment = (OrthogonalLineSegment) second;
                Line upperLine = upperSegment.getLine();
                Line lowerLine = lowerSegment.getLine();
                if (upperLine.getSpacing() == lowerLine.getSpacing()) {
                    Box upperStart = (Box) upperLine.getStart();
                    Box upperEnd = (Box) upperLine.getEnd();
                    Box lowerStart = (Box) lowerLine.getStart();
                    Box lowerEnd = (Box) lowerLine.getEnd();
                    boolean good = false;
                    if (upperSegment.startPoint == upperLine.getStartPoint()) {
                        if (lowerStart == upperStart && lowerSegment.startPoint == lowerLine.getStartPoint()) {
                            good = true;
                        } else if (lowerEnd == upperStart && lowerSegment.endPoint == lowerLine.getEndPoint()) {
                            good = true;
                        }
                    }
                    if (upperSegment.endPoint == upperLine.getEndPoint()) {
                        if (lowerStart == upperEnd && lowerSegment.startPoint == lowerLine.getStartPoint()) {
                            good = true;
                        } else if (lowerEnd == upperEnd && lowerSegment.endPoint == lowerLine.getEndPoint()) {
                            good = true;
                        }
                    }
                    if (good) {
                        similar.add(new Pair<>(upperSegment, lowerSegment));
                    }
                }
            }
        }

        /**
         * Builds an undirected graph from the found end segment pairs.
         */
        HashMap<OrthogonalLineSegment, ArrayList<OrthogonalLineSegment>> bundleGraph = new HashMap<>();
        for (OrthogonalLineSegment lineSegment : lineSegments) {
            bundleGraph.put(lineSegment, new ArrayList<OrthogonalLineSegment>());
        }
        for (Pair<OrthogonalLineSegment, OrthogonalLineSegment> obstacle : similar) {
            OrthogonalLineSegment first = obstacle.getFirst(), second = obstacle.getSecond();
            bundleGraph.get(first).add(second);
            bundleGraph.get(second).add(first);
        }

        /**
         * Finds the bundles of the end segments from the bundle graph. In each bundle the distance
         * between any consecutive segments is constant for this bundle. Searches the bundles by
         * traversing the graph.
         */
        int bundleCount = 0;
        HashMap<OrthogonalLineSegment, Integer> bundleNumber = new HashMap<>();
        ArrayList<ArrayList<OrthogonalLineSegment>> bundles = new ArrayList<>();
        ArrayList<Double> maxMove = new ArrayList<>();
        for (OrthogonalLineSegment lineSegment : lineSegments) {
            if (!bundleNumber.containsKey(lineSegment)) {
                LinkedList<OrthogonalLineSegment> queue = new LinkedList<>();
                double spacing = lineSegment.getLine().getSpacing();
                double epsilon = lineSegment.getLine().getDiagram().getEpsilon();
                queue.add(lineSegment);
                bundleNumber.put(lineSegment, bundleCount);
                double moveDist = maxPos.get(lineSegment) - lineSegment.pos;
                ArrayList<OrthogonalLineSegment> bundle = new ArrayList<>();
                bundle.add(lineSegment);
                while (!queue.isEmpty()) {
                    OrthogonalLineSegment currSegment = queue.poll();
                    for (OrthogonalLineSegment nextSegment : bundleGraph.get(currSegment)) {
                        if (!bundleNumber.containsKey(nextSegment) && Math.abs(Math.abs(nextSegment.pos - currSegment.pos) - 2 * spacing) < epsilon) {
                            queue.add(nextSegment);
                            bundleNumber.put(nextSegment, bundleCount);
                            moveDist = Math.min(moveDist, maxPos.get(nextSegment) - nextSegment.pos);
                            bundle.add(nextSegment);
                        }
                    }
                }
                maxMove.add(moveDist);
                bundles.add(bundle);
                bundleCount++;
            }
        }

        /**
         * Merges bundles, if necessary. This can happen since all segments are at their minimum
         * possible positions, so some bundle could be moved down and be merged with a lower bundle.
         */
        for (int i = 0; i < bundles.size(); i++) {
            ArrayList<OrthogonalLineSegment> bundle = bundles.get(i);
            int nextBundle = -1;
            double minLen = maxMove.get(i);
            double spacing = bundle.get(0).getLine().getSpacing();
            for (OrthogonalLineSegment lineSegment : bundle) {
                for (OrthogonalLineSegment nextSegment : bundleGraph.get(lineSegment)) {
                    if (lineSegment.pos < nextSegment.pos) {
                        int j = bundleNumber.get(nextSegment);
                        if (i != j) {
                            double len = nextSegment.pos - lineSegment.pos - 2 * spacing;
                            if (len < minLen) {
                                minLen = len;
                                nextBundle = j;
                            }
                        }
                    }
                }
            }

            if (nextBundle != -1) {
                for (OrthogonalLineSegment lineSegment : bundle) {
                    lineSegment.move(lineSegment.pos + minLen);
                }

                if (nextBundle > i) {
                    for (OrthogonalLineSegment lineSegment : bundle) {
                        bundleNumber.put(lineSegment, nextBundle);
                    }
                    maxMove.set(nextBundle, Math.min(maxMove.get(nextBundle), maxMove.get(i) - minLen));
                    bundles.get(nextBundle).addAll(bundle);
                } else {
                    for (OrthogonalLineSegment lineSegment : bundles.get(nextBundle)) {
                        bundleNumber.put(lineSegment, i);
                    }
                    maxMove.set(i, Math.min(maxMove.get(nextBundle), maxMove.get(i) - minLen));
                    bundle.addAll(bundles.get(nextBundle));
                    i--;
                }
            } else {
                for (OrthogonalLineSegment lineSegment : bundle) {
                    lineSegment.move(lineSegment.pos + minLen / 2);
                }
            }
        }
    }

    /**
     * A comparator that sorts the lines by their smallest point in ascending order, and by their
     * largest point in ascending order in case of tiebreaker. Uses {@link PointComparator} to
     * compare points.
     */
    static class StraightLineComparator implements Comparator<Line> {

        @Override
        public int compare(Line l1, Line l2) {
            Point2D.Double l1Start = l1.getStartPoint();
            Point2D.Double l1End = l1.getEndPoint();
            Point2D.Double l2Start = l2.getStartPoint();
            Point2D.Double l2End = l2.getEndPoint();

            PointComparator cmp = new PointComparator();
            if (cmp.compare(l1Start, l1End) == 1) {
                Point2D.Double tmp = l1Start;
                l1Start = l1End;
                l1End = tmp;
            }
            if (cmp.compare(l2Start, l2End) == 1) {
                Point2D.Double tmp = l2Start;
                l2Start = l2End;
                l2End = tmp;
            }

            int res = cmp.compare(l1Start, l2Start);
            return res == 0 ? cmp.compare(l1End, l2End) : res;
        }
    }

    /**
     * Finds the vector by which two consecutive one bundle line endpoints that lie on the same box
     * and the same side differ.
     *
     * @param a the upper side endpoint with respect to the line
     * @param b the line endpoint that lies on the other box
     * @param c the line endpoint that lies on the actual side
     * @param dist the distance between the two consecutive one bundle lines
     * @return the vector by which two consecutive one bundle line endpoints that lie on the same
     * box and the same side differ.
     */
    static Point2D.Double findShift(Point2D.Double a, Point2D.Double b, Point2D.Double c, double dist) {
        /**
         * The angle between the side and the line is equal to arcCos(dotProduct(CA,CB) /
         * (|CA|*|CB|)). Then the result is the vector |CA| scaled to distance (distance /
         * sin(angle)).
         */
        double dp = (a.x - c.x) * (b.x - c.x) + (a.y - c.y) * (b.y - c.y);
        double alpha = Math.acos(dp / (a.distance(c) * b.distance(c)));
        double q = dist / Math.sin(alpha);
        double len = a.distance(c);
        return new Point2D.Double((a.x - c.x) / len * q, (a.y - c.y) / len * q);
    }

    /**
     * Arranges the straight lines along the box borders. Separates the same lines by the calculated
     * distance. Keeps the changed lines parallel to their old positions.
     *
     * @param lines the straight lines to arrange
     */
    static void arrangeStraightLineEnds(ArrayList<Line> lines) {
        /**
         * Moves all the line points that are on the corners of the boxes.
         */
        for (Line line : lines) {
            shiftCorner(line.getStartPoint(), (Box) line.getStart(), line.getUsedStartSides(), true);
            shiftCorner(line.getEndPoint(), (Box) line.getEnd(), line.getUsedEndSides(), false);
        }

        /**
         * Sorts the lines so that all the lines with the same geometry will be consecutive.
         */
        StraightLineComparator slc = new StraightLineComparator();
        Collections.sort(lines, slc);

        /**
         * Identifies and arranges each bundle separately. In each bundle, there can be two types of
         * lines, with opposite directions, but with the same geometry (ignoring the direction of
         * the line).
         */
        for (int i = 0, j = 0; i < lines.size(); i = j) {
            while (j < lines.size() && slc.compare(lines.get(i), lines.get(j)) == 0) {
                j++;
            }

            /**
             * For each end point, finds the side endpoints, to which it is connected to.
             */
            Line line = lines.get(i);
            Box start = (Box) line.getStart();
            Box end = (Box) line.getEnd();
            Point2D.Double first = line.getStartPoint();
            Point2D.Double second = line.getEndPoint();
            double[] sx = {start.left, start.right, start.right, start.left};
            double[] sy = {start.top, start.top, start.bottom, start.bottom};
            int it = 0;
            for (int p = 0, q = 1; p < 4; p++, q = (q + 1) % 4) {
                if ((sx[p] == sx[q] && sx[q] == first.x) || (sy[p] == sy[q] && sy[q] == first.y)) {
                    it = p;
                }
            }
            int jt = (it + 1) % 4;
            Point2D.Double[] firstSideEndPoints = {new Point2D.Double(sx[it], sy[it]), new Point2D.Double(sx[jt], sy[jt])};
            double[] ex = {end.left, end.right, end.right, end.left};
            double[] ey = {end.top, end.top, end.bottom, end.bottom};
            it = 0;
            for (int p = 0, q = 1; p < 4; p++, q = (q + 1) % 4) {
                if ((ex[p] == ex[q] && ex[q] == second.x) || (ey[p] == ey[q] && ey[q] == second.y)) {
                    it = p;
                }
            }
            jt = (it + 1) % 4;
            Point2D.Double[] secondSideEndPoints = {new Point2D.Double(ex[it], ey[it]), new Point2D.Double(ex[jt], ey[jt])};

            /**
             * Swaps the points and corresponding side end point array, if the second point is less
             * than the first point. The first point should be the smaller one.
             */
            PointComparator pc = new PointComparator();
            if (pc.compare(first, second) == 1) {
                Point2D.Double tmp = first;
                first = second;
                second = tmp;
                Point2D.Double[] ptmp = firstSideEndPoints;
                firstSideEndPoints = secondSideEndPoints;
                secondSideEndPoints = ptmp;
                Box btmp = start;
                start = end;
                end = btmp;
            }

            /**
             * For each pair of end points, if the 0th endpoint is not on the upper (to the left of
             * the first->second vector) side of the line, swaps it with the 1st one.
             */
            Line2D.Double l = new Line2D.Double(first, second);
            if (l.relativeCCW(firstSideEndPoints[0]) == -1) {
                Point2D.Double tmp = firstSideEndPoints[0];
                firstSideEndPoints[0] = firstSideEndPoints[1];
                firstSideEndPoints[1] = tmp;
            }
            if (l.relativeCCW(secondSideEndPoints[0]) == -1) {
                Point2D.Double tmp = secondSideEndPoints[0];
                secondSideEndPoints[0] = secondSideEndPoints[1];
                secondSideEndPoints[1] = tmp;
            }

            /**
             * Calculates the distance from the current line by which it can be transposed, keeping
             * it parallel, up and down in direction perpendicular to it, so that it is still
             * connected to the current sides.
             */
            double upperDist = Math.min(l.ptLineDist(firstSideEndPoints[0]), l.ptLineDist(secondSideEndPoints[0]));
            double lowerDist = Math.min(l.ptLineDist(firstSideEndPoints[1]), l.ptLineDist(secondSideEndPoints[1]));

            /**
             * The distance between the two consecutive lines is the minimum of the maximum possible
             * and the maximum spacing of the lines.
             */
            double spacing = 0;
            for (int k = i; k < j; k++) {
                spacing = Math.max(spacing, lines.get(k).getSpacing());
            }
            int count = j - i;
            double dist = Math.min(2 * spacing, (upperDist + lowerDist) / (count + 1));

            /**
             * Calculates the vectors by which to shift the endpoints of the line on each of the two
             * line end sides, to obtain the geometry of the next line.
             */
            Point2D.Double firstShift = findShift(firstSideEndPoints[0], second, first, dist);
            Point2D.Double secondShift = findShift(secondSideEndPoints[0], first, second, dist);

            /**
             * The current line should be moved by the shift distance to obtain the starting
             * position of the zeroth (fictive) line. The next line (shifted by firstShift and
             * secondShift) will be the actual first line.
             */
            double half = ((count + 1) * dist) / 2;
            double shift = -half;
            if (half > lowerDist) {
                shift += half - lowerDist;
            } else if (half > upperDist) {
                shift -= half - upperDist;
            }

            double q = shift / dist;
            Point2D.Double firstStart = new Point2D.Double(first.x + q * firstShift.x, first.y + q * firstShift.y);
            Point2D.Double secondStart = new Point2D.Double(second.x + q * secondShift.x, second.y + q * secondShift.y);

            /**
             * Puts all the counter-clockwise lines to the beginning, and all the clockwise lines to
             * the end of the bundle.
             */
            int pi = i, pj = j - 1;
            while (pi < pj) {
                while (pi < j && lines.get(pi).getStartPoint().equals(second)) {
                    pi++;
                }
                while (pj >= i && lines.get(pj).getStartPoint().equals(first)) {
                    pj--;
                }
                if (pi < pj) {
                    Line tmp = lines.get(pi);
                    lines.set(pi, lines.get(pj));
                    lines.set(pj, tmp);
                    pi++;
                    pj--;
                }
            }

            /**
             * Sets the new geometry to the lines of the bundle by adding firstShift and secondShift
             * to the endpoints of the previous line.
             */
            for (int k = i; k < j; k++) {
                firstStart.x += firstShift.x;
                firstStart.y += firstShift.y;
                secondStart.x += secondShift.x;
                secondStart.y += secondShift.y;
                Point2D.Double startPoint = lines.get(k).getStartPoint();
                Point2D.Double endPoint = lines.get(k).getEndPoint();
                if (lines.get(k).getStart() == start) {
                    startPoint.setLocation(firstStart);
                    endPoint.setLocation(secondStart);
                } else {
                    startPoint.setLocation(secondStart);
                    endPoint.setLocation(firstStart);
                }
            }
        }
    }

    /**
     * Adjusts the label positions and segments of the given lines after changing line points.
     * Places the labels in the places along the new line the most similar to their old position.
     *
     * @param lines the lines with changed points
     * @param oldPointsList the previous point arrays of these lines, in the same order as
     * {@code lines}.
     */
    static void adjustLabelPlacement(ArrayList<Line> lines, ArrayList<ArrayList<Point2D.Double>> oldPointsList) {
        for (int t = 0; t < lines.size(); t++) {
            Line line = lines.get(t);
            double epsilon = line.getDiagram().getEpsilon();

            ArrayList<Point2D.Double> newPoints = line.lineGeometry.points, oldPoints = oldPointsList.get(t);
            if (oldPoints == null) {
                continue;
            }

            if (line.getType() == LineType.ORTHOGONAL) {
                /**
                 * Creates strings of line segment directions for both the old and the new line.
                 */
                int[] oldString = new int[oldPoints.size() - 1];
                for (int i = 0; i + 1 < oldPoints.size(); i++) {
                    Point2D.Double prevPoint = oldPoints.get(i);
                    Point2D.Double currPoint = oldPoints.get(i + 1);
                    oldString[i] = prevPoint.x < currPoint.x ? 1 : prevPoint.x > currPoint.x ? 3 : prevPoint.y < currPoint.y ? 2 : 0;
                }
                int[] newString = new int[newPoints.size() - 1];
                for (int i = 0; i + 1 < newPoints.size(); i++) {
                    Point2D.Double prevPoint = newPoints.get(i);
                    Point2D.Double currPoint = newPoints.get(i + 1);
                    newString[i] = prevPoint.x < currPoint.x ? 1 : prevPoint.x > currPoint.x ? 3 : prevPoint.y < currPoint.y ? 2 : 0;
                }

                /**
                 * Measures the similarity of each pair of segments from the two lines. If the two
                 * segments are perpendicular, their similarity is 0. Otherwise, looks at +-10
                 * segments, including the ones compared. Counts the number of equally directed
                 * pairs with equal offsets and sets the similarity to 2^(this count)-1.
                 */
                long[][] similarity = new long[oldString.length][newString.length];
                for (int i = 0; i < oldString.length; i++) {
                    for (int j = 0; j < newString.length; j++) {
                        if (oldString[i] % 2 == newString[j] % 2) {
                            int leftDif = Math.min(10, Math.min(i, j));
                            int rightDif = Math.min(10, Math.min(oldString.length - i - 1, newString.length - j - 1));

                            long value = 1;
                            for (int k = -leftDif; k <= rightDif; k++) {
                                if (oldString[i + k] == newString[j + k]) {
                                    similarity[i][j] += value;
                                    value *= 2;
                                }
                            }
                        }
                    }
                }

                /**
                 * Finds the path through this similarity matrix with the largest total sum. Each
                 * segment of the old line must be matched to a single segment from the new one, and
                 * more than one segment can be matched to a single new segment.
                 */
                int[][] parent = new int[oldString.length][newString.length];
                for (int i = 1; i < oldString.length; i++) {
                    int parentCol = 0;
                    for (int j = 0; j < newString.length; j++) {
                        if (similarity[i - 1][j] > similarity[i - 1][parentCol]) {
                            parentCol = j;
                        }
                        parent[i][j] = parentCol;
                        similarity[i][j] += similarity[i - 1][parentCol];
                    }
                }

                /**
                 * Reconstructs the optimal path, creating the matching used.
                 */
                int[] match = new int[oldString.length];
                long best = 0;
                for (int j = 0; j < newString.length; j++) {
                    if (similarity[oldString.length - 1][j] > best) {
                        best = similarity[oldString.length - 1][j];
                        match[oldString.length - 1] = j;
                    }
                }
                for (int i = oldString.length - 2; i >= 0; i--) {
                    match[i] = parent[i + 1][match[i + 1]];
                }

                /**
                 * Finds the labels attached to each old segment.
                 */
                ArrayList<ArrayList<LineLabel>> segmentLabels = new ArrayList<>();
                for (int i = 0; i < oldString.length; i++) {
                    segmentLabels.add(new ArrayList<LineLabel>());
                }
                for (LineLabel label : line.getLabels()) {
                    segmentLabels.get(label.segmentIndex).add(label);
                }
                /**
                 * Attaches the labels to their new segments. If a single old segment was matched to
                 * a particular new segment, places the labels proportionally to the segment
                 * lengths. Otherwise, tries to place the labels at their projections on the new
                 * segment. If this is not possible, places them proportionally to the total length
                 * of the segments matched to the particular new segment.
                 */
                for (int i = 0; i < oldString.length;) {
                    int j = i;
                    int p = match[i];
                    while (j < oldString.length && p == match[j]) {
                        for (LineLabel label : segmentLabels.get(j)) {
                            label.segmentIndex = p;
                        }
                        j++;
                    }

                    Point2D.Double first = newPoints.get(p);
                    Point2D.Double second = newPoints.get(p + 1);
                    boolean segmentVertical = first.x == second.x;
                    double segmentLenX = second.x - first.x;
                    double segmentLenY = second.y - first.y;

                    if (i + 1 == j && !((segmentVertical == (oldPoints.get(i).x == oldPoints.get(i + 1).x))
                            && Math.abs(segmentVertical ? (oldPoints.get(i).x - first.x) : (oldPoints.get(i).y - first.y)) < SEGMENT_MATCHING_OFFSET * epsilon)) {
                        double xLen = oldPoints.get(i + 1).x - oldPoints.get(i).x;
                        double yLen = oldPoints.get(i + 1).y - oldPoints.get(i).y;
                        for (LineLabel label : segmentLabels.get(i)) {
                            double ratio = xLen == 0 ? (label.getCenterY() - oldPoints.get(i).y) / yLen : (label.getCenterX() - oldPoints.get(i).x) / xLen;
                            label.setCenter(new Point2D.Double(first.x + ratio * segmentLenX, first.y + ratio * segmentLenY));
                        }
                    } else {
                        double totalLen = 0;
                        for (int k = i; k < j; k++) {
                            totalLen += GeometryHelper.findDistance(oldPoints.get(k), oldPoints.get(k + 1));
                        }
                        double partLen = 0;
                        for (int k = i; k < j; k++) {
                            for (LineLabel label : segmentLabels.get(k)) {
                                Point2D.Double center = label.getCenter();
                                if ((segmentVertical && (center.y < Math.min(first.y, second.y) || Math.max(first.y, second.y) < center.y))
                                        || (!segmentVertical && (center.x < Math.min(first.x, second.x) || Math.max(first.x, second.x) < center.x))) {
                                    Point2D.Double curr = oldPoints.get(k);
                                    Point2D.Double next = oldPoints.get(k + 1);
                                    double ratio = (partLen + Math.abs(curr.x == next.x ? center.y - curr.y : center.x - curr.x)) / totalLen;
                                    label.setCenter(new Point2D.Double(first.x + ratio * segmentLenX, first.y + ratio * segmentLenY));
                                }
                            }
                            partLen += GeometryHelper.findDistance(oldPoints.get(k), oldPoints.get(k + 1));
                        }
                    }

                    i = j;
                }
            } else {
                adjustPolylineLabelPlacement(line);
            }
        }
    }

    /**
     * Adjusts the label positions and segments of the given polyline after changing its points.
     * Places the labels in the places along the new line the most similar to their old position.
     *
     * @param line the line with changed points
     */
    static void adjustPolylineLabelPlacement(Line line) {
        assert line.getType() != LineType.ORTHOGONAL;

        ArrayList<Point2D.Double> points = line.lineGeometry.points;
        double[] partLengths = new double[points.size()];
        partLengths[0] = 0;
        Point2D.Double prev = points.get(0);
        Point2D.Double next;
        for (int i = 1; i < points.size(); i++) {
            next = points.get(i);
            partLengths[i] = prev.distance(next) + partLengths[i - 1];
            prev = next;
        }
        double lineLength = partLengths[points.size() - 1];

        for (LineLabel label : line.getLabels()) {
            Point2D.Double center = label.getCenter();
            if (label.isRotated()) {
                label.segmentIndex = GeometryHelper.findNearestSegment(center, line);
            } else {
                Point2D.Double[] corners = GeometryHelper.getChainPoints(label);
                double minDistance = Double.POSITIVE_INFINITY;
                for (int i = 0; i < 4; i++) {
                    Pair<Integer, Double> segDist = GeometryHelper.findNearestSegmentAndDistance(corners[i], line);
                    int currSegment = segDist.getFirst();
                    double currDistance = segDist.getSecond();
                    if (currDistance < minDistance) {
                        minDistance = currDistance;
                        label.segmentIndex = currSegment;
                    }
                }
            }

            GeometryHelper.movePointToLine(center, line, label.segmentIndex);

            label.currentPosition = (partLengths[label.segmentIndex] + center.distance(points.get(label.segmentIndex))) / lineLength;

            GeometryHelper.placeLabelOnSegmentPoint(label,
                    points.get(label.segmentIndex), points.get(label.segmentIndex + 1), center);
        }
    }

    /**
     * Snaps the segments of the new lines to the closest segments of the old lines, if the distance
     * between them is not greater than the sum of the two line spacings.
     *
     * @param newLines the new lines to snap
     */
    static void snapNewLines(ArrayList<Line> newLines) {
        Diagram diagram = newLines.get(0).getDiagram();
        ArrayList<Line> lines = diagram.getDescendantLines();

        ArrayList<ArrayList<OrthogonalLineSegment>> lineSegments = Normalizer.generateLineSegments(diagram, null);
        ArrayList<ArrayList<RectangleSegment>> boxSegments = Normalizer.generateRectangleSegments(diagram, false);

        HashSet<Line> newLineSet = new HashSet<>();
        for (Line line : newLines) {
            if (line.getType() == LineType.ORTHOGONAL) {
                newLineSet.add(line);
            }
        }
        snapNewLines(newLineSet, lines, lineSegments.get(0), boxSegments.get(0), true);
        Normalizer.updateSegments(lineSegments.get(1));
        snapNewLines(newLineSet, lines, lineSegments.get(1), boxSegments.get(1), false);

        for (Line line : newLines) {
            if (line.getType() == LineType.ORTHOGONAL) {
                line.lineGeometry.cullPoints();
            }
        }
    }

    /**
     * Snaps the segments of the new lines to the closest segments of the old lines, if the distance
     * between them is not greater than the sum of the two line spacings. Does this for the given
     * orthogonal direction segments. Utilizes the same obstacle graph algorithm to find closest
     * segments. Runs the sweep algorithm two times: from the start and from the end of the given
     * direction to find the closest segments from the both sides of each new line segment.
     *
     * @param newLines the new lines to snap
     * @param oldLines the old lines to which to snap new line segments
     * @param lineSegments all line segments
     * @param boxSegments box segments
     * @param segmentsVertical whether the segments are vertical or horizontal
     */
    static void snapNewLines(
            HashSet<Line> newLines,
            ArrayList<Line> oldLines,
            ArrayList<OrthogonalLineSegment> lineSegments,
            ArrayList<RectangleSegment> boxSegments,
            boolean segmentsVertical) {
        ArrayList<OrthogonalSegment> segments = new ArrayList<>();
        segments.addAll(lineSegments);
        segments.addAll(boxSegments);
        Collections.sort(segments);

        LinkedHashMap<OrthogonalLineSegment, Double> closestPos = new LinkedHashMap<>();
        snapSweep(segments, newLines, closestPos, true);
        for (OrthogonalLineSegment lineSegment : lineSegments) {
            if (newLines.contains(lineSegment.getLine())) {
                lineSegment.pos = segmentsVertical ? lineSegment.startPoint.x : lineSegment.startPoint.y;
            }
        }
        snapSweep(segments, newLines, closestPos, false);
        for (OrthogonalLineSegment lineSegment : lineSegments) {
            if (newLines.contains(lineSegment.getLine())) {
                Double pos = closestPos.get(lineSegment);
                if (pos != null) {
                    lineSegment.move(pos);
                }
            }
        }
    }

    /**
     * Runs the obstacle graph sweepline to determine the closest segments to the new line segments.
     *
     * @param segments the segments of the lines and boxes in the diagram
     * @param newLines the new lines
     * @param closestPos maps each new line segment to its currently closest old line segment
     * coordinate
     * @param fromStart whether to run the sweepline from the start or the end of the direction
     */
    static void snapSweep(
            ArrayList<OrthogonalSegment> segments,
            HashSet<Line> newLines,
            LinkedHashMap<OrthogonalLineSegment, Double> closestPos,
            boolean fromStart) {
        TreeSet<VerticalPart<OrthogonalSegment>> sweepline = new TreeSet<>();
        /*
         * Adds dummy buffer parts to both ends of the sweepline, prevents analysing special cases.
         */
        sweepline.add(new VerticalPart<OrthogonalSegment>(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, null));
        sweepline.add(new VerticalPart<OrthogonalSegment>(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, null));

        /*
         * The main cycle through the segments in order from left to right.
         */
        for (int i = (fromStart ? 0 : segments.size() - 1); i != (fromStart ? segments.size() : -1); i += (fromStart ? +1 : -1)) {
            OrthogonalSegment segment = segments.get(i);
            /*
             * The parts to start and end checking overlap with the current segment from.
             */
            VerticalPart<OrthogonalSegment> startPart = sweepline.lower(new Segment.VerticalPart<>(segment.getStart(), segment.getStart(), segment));
            /*
             * A TreeSet containing the parts to check for overlap with the current segment.
             * Includes both startPart and endPart.
             */
            NavigableSet<VerticalPart<OrthogonalSegment>> parts = sweepline.tailSet(startPart, startPart.intersects(segment));
            startPart = parts.first();
            /*
             * The parts to iterate through, with mid being the part to currently check.
             */
            VerticalPart<OrthogonalSegment> upper, mid, lower;
            Iterator<VerticalPart<OrthogonalSegment>> it = parts.iterator();
            upper = sweepline.first();
            mid = it.next();
            /*
             * The cycle through the parts, stops when the current part is below the current
             * segment.
             */
            boolean isNewLineSegment = segment instanceof OrthogonalLineSegment && newLines.contains(((OrthogonalLineSegment) segment).getLine());
            double bestPosDif = Double.POSITIVE_INFINITY;
            OrthogonalSegment bestSegment = null;
            while (mid.intersects(segment)) {
                it.remove();
                lower = it.hasNext() ? it.next() : null;
                /*
                 * If the current part intersects the current segment and its original segment is
                 * not blocked by the parts neighbours, adds an edge to the obstacle graph.
                 */
                if (isNewLineSegment) {
                    OrthogonalSegment originalSegment = mid.getSegment();
                    if (!(upper.intersects(segment) && (fromStart ? upper.getSegment().getPos() > originalSegment.getPos() : upper.getSegment().getPos() < originalSegment.getPos())
                            && upper.getBottom() > mid.getSegment().getStart())) {
                        if (!(lower.intersects(segment) && (fromStart ? lower.getSegment().getPos() > originalSegment.getPos() : lower.getSegment().getPos() < originalSegment.getPos())
                                && mid.getSegment().getEnd() > lower.getTop())) {
                            double newPosDif = Math.abs(segment.getPos() - originalSegment.getPos());
                            if (newPosDif < bestPosDif) {
                                bestPosDif = newPosDif;
                                bestSegment = originalSegment;
                            }
                        }
                    }
                }
                /*
                 * Advances the part cycle.
                 */
                upper = mid;
                mid = lower;
            }

            if (isNewLineSegment
                    && bestSegment instanceof OrthogonalLineSegment
                    && bestSegment.getAfterSpacing() + segment.getBeforeSpacing() + 10 > bestPosDif
                    && ((OrthogonalLineSegment) segment).getLine() != ((OrthogonalLineSegment) bestSegment).getLine()) {
                Double currClosestPos = closestPos.get((OrthogonalLineSegment) segment);
                if (currClosestPos == null || bestPosDif < Math.abs(segment.getPos() - currClosestPos)) {
                    closestPos.put((OrthogonalLineSegment) segment, bestSegment.pos);
                }
                segment.setPos(bestSegment.pos);
            }

            /*
             * The current segment always gets added to the sweepline whole.
             */
            VerticalPart<OrthogonalSegment> segmentPart = new Segment.VerticalPart<>(segment);
            sweepline.add(segmentPart);
            if (startPart.intersects(segment)
                    && startPart.getTop() < segmentPart.getTop()
                    && startPart.getBottom() > segmentPart.getTop()) {
                sweepline.add(new Segment.VerticalPart<>(startPart.getTop(), segmentPart.getTop(), startPart.getSegment()));
            }
            if (upper.intersects(segment)
                    && upper.getBottom() > segmentPart.getBottom()
                    && upper.getTop() < segmentPart.getBottom()) {
                sweepline.add(new Segment.VerticalPart<>(segmentPart.getBottom(), upper.getBottom(), upper.getSegment()));
            }
        }
    }
}
