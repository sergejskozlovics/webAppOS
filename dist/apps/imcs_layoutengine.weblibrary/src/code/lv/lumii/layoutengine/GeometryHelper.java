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
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import lv.lumii.layoutengine.Box.BoxSide;
import lv.lumii.layoutengine.OutsideLabel.LineLabel;
import lv.lumii.layoutengine.OutsideLabel.LineLabel.Orientation;
import lv.lumii.layoutengine.util.Pair;

/**
 * A class of helpful geometry functions used in various tracer and normalizer functions.
 *
 * @author JK
 */
abstract class GeometryHelper {

    /**
     * Calculates the Manhattan distance from the given point to the closest point of the box's
     * perimeter.
     *
     * @param x the abscissa of the point
     * @param y the ordinate of the point
     * @param box the box to find the distance to
     * @return the Manhattan distance between the given point and the closest point of the box's
     * perimeter
     */
    static double findManhattanDistance(double x, double y, Box box) {
        double left = box.left, right = box.right,
                top = box.top, bottom = box.bottom;
        if ((x == left || x == right) && (y == top || y == bottom)) {
            return Double.MIN_VALUE;
        }
        /*
         * If the point lies inside the box finds the distance to the closest edge.
         */
        if (left <= x && x <= right && top <= y && y <= bottom) {
            return Math.min(Math.min(x - left, right - x),
                    Math.min(y - top, bottom - y));
        } else {
            return (left <= x && x <= right
                    ? 0 : (x < left ? left - x : x - right))
                    + (top <= y && y <= bottom
                    ? 0 : (y < top ? top - y : y - bottom));
        }
    }

    /**
     * Calculates the Manhattan distance from the given box's perimeter to the closest point of the
     * box's perimeter.
     *
     * @param a the first box
     * @param b the second box
     * @return the Manhattan distance from the given box's perimeter to the closest point of the
     * box's perimeter.
     */
    static double findManhattanDistance(Box a, Box b) {
        return Math.min(Math.min(Math.min(
                findManhattanDistance(a.getLeft(), a.getTop(), b),
                findManhattanDistance(a.getLeft(), a.getBottom(), b)),
                Math.min(
                        findManhattanDistance(a.getRight(), a.getTop(), b),
                        findManhattanDistance(a.getRight(), a.getBottom(), b))),
                Math.min(Math.min(
                                findManhattanDistance(b.getLeft(), b.getTop(), a),
                                findManhattanDistance(b.getLeft(), b.getBottom(), a)),
                        Math.min(
                                findManhattanDistance(b.getRight(), b.getTop(), a),
                                findManhattanDistance(b.getRight(), b.getBottom(), a))));
    }

    /**
     * Checks whether the given rectangle, extended by {@code epsilon}, contains the given point.
     *
     * @param left the left side of the rectangle
     * @param top the top side of the rectangle
     * @param right the right side of the rectangle
     * @param bottom the bottom side of the rectangle
     * @param point the point
     * @param epsilon how much to extend the rectangle by
     * @return whether the rectangle contains the point
     */
    static boolean contains(double left, double top, double right, double bottom, Point2D.Double point, double epsilon) {
        return left - epsilon <= point.getX() && point.getX() <= right + epsilon
                && top - epsilon <= point.getY() && point.getY() <= bottom + epsilon;
    }

    /**
     * Creates a rectangle with the given sides
     *
     * @param left the left side of the new rectangle
     * @param top the top of the new rectangle
     * @param right the right side of the new rectangle
     * @param bottom the bottom of the new rectangle
     * @return a new rectangle with the given sides
     */
    static Rectangle2D.Double rectangle(double left, double top, double right, double bottom) {
        return new Rectangle2D.Double(left, top, right - left, bottom - top);
    }

    /**
     * Moves the given point inside the given rectangle. The resulting point is at least
     * {@code epsilon} away from any side of the rectangle.
     *
     * @param point the point to move
     * @param left the left side of the rectangle
     * @param top the top side of the rectangle
     * @param right the right side of the rectangle
     * @param bottom the bottom side of the rectangle
     * @param epsilon the minimum value of free space between the moved point and any side of the
     * rectangle
     */
    static void movePointInside(Point2D.Double point, double left, double top, double right, double bottom, double epsilon) {
        if (!contains(left, top, right, bottom, point, -epsilon)) {
            if (point.getX() <= left) {
                point.setLocation(left + epsilon, point.getY());
            } else if (point.getX() >= right) {
                point.setLocation(right - epsilon, point.getY());
            }
            if (point.getY() <= top) {
                point.setLocation(point.getX(), top + epsilon);
            } else if (point.getY() >= bottom) {
                point.setLocation(point.getX(), bottom - epsilon);
            }
        }
    }

    /**
     * Moves the given point outside any of the given rectangles. The resulting point is at least
     * {@code epsilon} away from any of the rectangles.
     *
     * @param point the point to move
     * @param rectangles the rectangles from which to move the point
     * @param epsilon the minimum value of free space between the moved point and any of the
     * rectangles
     */
    static void movePointOutside(Point2D.Double point, ArrayList<AbstractContainer> rectangles, double epsilon) {
        for (AbstractContainer rect : rectangles) {
            if (rect.contains(point, epsilon)) {
                double toLeft = point.getX() - rect.getLeft(),
                        toRight = rect.getRight() - point.getX(),
                        toTop = point.getY() - rect.getTop(),
                        toBottom = rect.getBottom() - point.getY(),
                        minDist = Math.min(Math.min(toLeft, toRight), Math.min(toTop, toBottom));
                
                if (minDist == toLeft) {
                    point.setLocation(rect.getLeft() - epsilon, point.getY());
                } else if (minDist == toRight) {
                    point.setLocation(rect.getRight() + epsilon, point.getY());
                } else if (minDist == toTop) {
                    point.setLocation(point.getX(), rect.getTop() - epsilon);
                } else {
                    point.setLocation(point.getX(), rect.getBottom() + epsilon);
                }
                break;
            }
        }
    }

    /**
     * Moves the given point to the perimeter of the given box by the minimum possible distance.
     *
     * @param point the point to move
     * @param box the box to whose perimeter to move the point
     */
    static void movePointToPerimeter(Point2D.Double point, Box box) {
        if (isPointOnPerimeter(box, point)) {
            return;
        }
        double toLeft = Math.abs(point.x - box.left),
                toRight = Math.abs(point.x - box.right),
                toTop = Math.abs(point.y - box.top),
                toBottom = Math.abs(point.y - box.bottom);
        if (box.contains(point, 0)) {
            double minDist = Math.min(Math.min(toLeft, toRight), Math.min(toTop, toBottom));
            if (minDist == toLeft) {
                point.x = box.left;
            } else if (minDist == toRight) {
                point.x = box.right;
            } else if (minDist == toTop) {
                point.y = box.top;
            } else {
                point.y = box.bottom;
            }
        } else {
            if (point.x < box.left || box.right < point.x) {
                point.x = toLeft < toRight ? box.left : box.right;
            }
            if (point.y < box.top || box.bottom < point.y) {
                point.y = toTop < toBottom ? box.top : box.bottom;
            }
        }
    }

    /**
     * Moves the given point to the given side of the box by the minimum possible distance.
     *
     * @param point the point to move
     * @param box the box to whose perimeter to move the point
     * @param side the side to which to move the point
     */
    static void movePointToSide(Point2D.Double point, Box box, BoxSide side) {
        if (side.isHorizontal()) {
            point.x = Math.max(box.left, Math.min(box.right, point.x));
            point.y = side == BoxSide.TOP ? box.top : box.bottom;
        } else {
            point.x = side == BoxSide.LEFT ? box.left : box.right;
            point.y = Math.max(box.top, Math.min(box.bottom, point.y));
        }
    }

    /**
     * Reconnects the endpoints of the given line to the given start and end boxes, if they are
     * disconnected for whatever reason. If this causes the line to collapse to a single point,
     * creates a tiny u-turn line instead, as point lines are not supported. Returns a pair
     * containing the number of new segments created at the start and end of the line.
     *
     * @param line the line whose endpoints to reconnect
     * @param startBox the start box the line should connect to
     * @param endBox the end box the line should connect to
     * @return the number of new segments created at the start and end of the line
     */
    static Pair<Integer, Integer> reconnectEndPoints(Line line, Box startBox, Box endBox) {
        int startCount = reconnectEndPoint(line, startBox, true);
        int endCount = reconnectEndPoint(line, endBox, false);
        
        line.getLineGeometry().cullPoints();

        /*
         * Handle special case when there is only one point now by creating a tiny (based on box
         * spacing) u-turn line.
         */
        ArrayList<Point2D.Double> points = line.lineGeometry.points;
        assert points.size() >= 2;
        if (points.get(0).equals(points.get(1))) {
            assert startBox == endBox;
            points.remove(1);
            Point2D.Double point = points.get(0);
            
            double spacing = startBox.getSpacing() * 0.25,
                    eps = startBox.getDiagram().getEpsilon();
            if (point.x == startBox.getLeft() || point.x == startBox.getRight()) {
                double shiftX = point.x + (point.x == startBox.getLeft() ? -spacing : spacing);
                points.add(new Point2D.Double(shiftX, point.y));
                points.add(new Point2D.Double(shiftX, point.y + eps));
                points.add(new Point2D.Double(point.x, point.y + eps));
            } else {
                double shiftY = point.y + (point.y == startBox.getTop() ? -spacing : spacing);
                points.add(new Point2D.Double(point.x, shiftY));
                points.add(new Point2D.Double(point.x + eps, shiftY));
                points.add(new Point2D.Double(point.x + eps, point.y));
            }
            line.lineGeometry.points = points;
            startCount = endCount = 0;
        }/*
         * Handle case when start point and end point are equal.
         */ else if (line.getStartPoint().equals(line.getEndPoint())) {
            assert points.size() >= 4;
            assert startBox == endBox;
            Point2D.Double point = points.get(0);
            
            int n = points.size();
            double eps = startBox.getDiagram().getEpsilon();
            if (point.x == startBox.getLeft() || point.x == startBox.getRight()) {
                points.get(n - 2).y += eps;
                points.get(n - 1).y += eps;
            } else {
                points.get(n - 2).x += eps;
                points.get(n - 1).x += eps;
            }
            line.lineGeometry.points = points;
        }
        
        return new Pair<>(startCount, endCount);
    }

    /**
     * Reconnects a single orthogonal line ending to the given box, used by
     * {@link #reconnectEndPoints(Line, Box, Box)}.
     *
     * @param line the line to reconnect
     * @param box the box to connect the ending to
     * @param isStartPoint whether to connect the start or the end of the line
     * @return the number of new segments created to reconnect the line
     */
    private static int reconnectEndPoint(Line line, Box box, boolean isStartPoint) {
        double eps = box.getDiagram().getEpsilon();
        ArrayList<Point2D.Double> points = line.lineGeometry.points;
        Point2D.Double endPoint = isStartPoint ? points.get(0) : points.get(points.size() - 1),
                prevPoint = isStartPoint ? points.get(1) : points.get(points.size() - 2);

//        /*
//         * Lines ending inside the box are continued until the closest box edge (so possibly going
//         * backwards).
//         */
//        if (box.strictContains(endPoint)) {
//            if (endPoint.y == prevPoint.y) {
//                endPoint.x = (endPoint.x < prevPoint.x ? box.getLeft() : box.getRight());
//            } else {
//                endPoint.y = (endPoint.y < prevPoint.y ? box.getTop() : box.getBottom());
//            }
//        }
        if (isPointOnPerimeter(box, endPoint)) {
            int sides = isStartPoint ? line.getUsedStartSides() : line.getUsedEndSides();
            BoxSide currSide = BoxSide.findPointSide(endPoint, prevPoint, box);
            if (((1 << currSide.ordinal()) & sides) != 0) {
                return 0;
            }
        }
        
        double margin = box.getSpacing() / 2;
        if (Math.abs(endPoint.y - box.top) <= eps) {
            endPoint.y += prevPoint.y < box.top ? -margin : +margin;
        }
        if (Math.abs(endPoint.y - box.bottom) <= eps) {
            endPoint.y += prevPoint.y > box.bottom ? +margin : -margin;
        }
        if (Math.abs(endPoint.x - box.left) <= eps) {
            endPoint.x += prevPoint.x < box.left ? -margin : +margin;
        }
        if (Math.abs(endPoint.x - box.right) <= eps) {
            endPoint.x += prevPoint.x > box.right ? +margin : -margin;
        }
        
        Box pointBox = new Box(endPoint.x - eps, endPoint.x + eps, endPoint.y - eps, endPoint.y + eps, box.getDiagram(), box.getSpacing());
        Tracer tracer = new Tracer(box, pointBox);
        
        int pointDir;
        boolean lineIsSegment = points.size() == 2;
        if (endPoint.x == prevPoint.x) {
            pointDir = lineIsSegment ? endPoint.y < prevPoint.y ? 0b0001 : 0b0100 : 0b0101;
        } else {
            pointDir = lineIsSegment ? endPoint.x < prevPoint.x ? 0b1000 : 0b0010 : 0b1010;
        }
        
        ArrayList<Point2D.Double> pointsToAdd = tracer.trace(pointBox, box, pointDir, isStartPoint ? line.getUsedStartSides() : line.getUsedEndSides());
        Point2D.Double first = pointsToAdd.get(0);
        Point2D.Double second = pointsToAdd.get(1);
        
        int size = points.size();
        if (first.x == second.x) {
            double x = isStartPoint ? points.get(0).x : points.get(size - 1).x;
            for (int i = isStartPoint ? 0 : (size - 1); (isStartPoint ? i < size : i >= 0) && points.get(i).x == x; i += isStartPoint ? +1 : -1) {
                points.get(i).x = first.x;
            }
        } else {
            double y = isStartPoint ? points.get(0).y : points.get(size - 1).y;
            for (int i = isStartPoint ? 0 : (size - 1); (isStartPoint ? i < size : i >= 0) && points.get(i).y == y; i += isStartPoint ? +1 : -1) {
                points.get(i).y = first.y;
            }
        }
        
        if (isStartPoint) {
            Collections.reverse(pointsToAdd);
            points.addAll(0, pointsToAdd);
        } else {
            points.addAll(pointsToAdd);
        }
        
        int count = 1;
        for (int i = 0; i + 2 < pointsToAdd.size(); i++) {
            if ((pointsToAdd.get(i).x == pointsToAdd.get(i + 1).x && pointsToAdd.get(i + 1).x != pointsToAdd.get(i + 2).x)
                    || (pointsToAdd.get(i).y == pointsToAdd.get(i + 1).y && pointsToAdd.get(i + 1).y != pointsToAdd.get(i + 2).y)) {
                count++;
            }
        }
        return count;

//        int indexForInsert = isStartPoint ? 0 : points.size();
//            /*
//             * Lines going along the box's edge are moved inside the box by epsilon. Lines with
//             * endpoints not being connectable to the box by a single orthogonal segment get a new
//             * corner inserted.
//             */
//            boolean horizontal = endPoint.y == prevPoint.y, vertical = !horizontal;
//            if (box.getLeft() <= endPoint.x && endPoint.x <= box.getRight()) {
//                if (box.getLeft() == endPoint.x) {
//                    endPoint.x += eps;
//                    if (vertical) {
//                        prevPoint.x += eps;
//                    }
//                } else if (endPoint.x == box.getRight()) {
//                    endPoint.x -= eps;
//                    if (vertical) {
//                        prevPoint.x -= eps;
//                    }
//                }
//                double y = Math.abs(box.getTop() - endPoint.y)
//                        < Math.abs(box.getBottom() - endPoint.y)
//                        ? box.getTop() : box.getBottom();
//                if (vertical) {
//                    endPoint.y = y;
//                } else {
//                    points.add(indexForInsert, new Point2D.Double(endPoint.x, y));
//                }
//            } else if (box.getTop() <= endPoint.y && endPoint.y <= box.getBottom()) {
//                if (box.getTop() == endPoint.y) {
//                    endPoint.y += eps;
//                    if (horizontal) {
//                        prevPoint.y += eps;
//                    }
//                } else if (endPoint.y == box.getBottom()) {
//                    endPoint.y -= eps;
//                    if (horizontal) {
//                        prevPoint.y -= eps;
//                    }
//                }
//                double x = Math.abs(box.getLeft() - endPoint.x)
//                        < Math.abs(box.getRight() - endPoint.x)
//                        ? box.getLeft() : box.getRight();
//                if (horizontal) {
//                    endPoint.x = x;
//                } else {
//                    points.add(indexForInsert, new Point2D.Double(x, endPoint.y));
//                }
//            } else {
//                if (endPoint.x == prevPoint.x) {
//                    endPoint.y = box.getCenter().y;
//                    points.add(indexForInsert,
//                            new Point2D.Double(
//                            (Math.abs(box.getLeft() - endPoint.x)
//                            < Math.abs(box.getRight() - endPoint.x))
//                            ? box.getLeft() : box.getRight(),
//                            endPoint.y));
//                } else {
//                    endPoint.x = box.getCenter().x;
//                    points.add(indexForInsert,
//                            new Point2D.Double(
//                            endPoint.x,
//                            (Math.abs(box.getTop() - endPoint.y)
//                            < Math.abs(box.getBottom() - endPoint.y))
//                            ? box.getTop() : box.getBottom()));
//                }
//            }
    }

    /**
     * Returns whether the gives points lies on the perimeter of the given box.
     *
     * @param box the box whose perimeter to check
     * @param point the point to check
     * @return whether the gives points lies on the perimeter of the given box.
     */
    static boolean isPointOnPerimeter(Box box, Point2D.Double point) {
        return box.contains(point, 0)
                && (point.getY() == box.getTop()
                || point.getY() == box.getBottom()
                || point.getX() == box.getLeft()
                || point.getX() == box.getRight());
    }

    /**
     * Finds the intersection point between the segment defined by its two endpoints and the
     * perimeter of the given box.
     *
     * @param box the box to find the intersection with
     * @param firstPoint the first endpoint of the segment
     * @param secondPoint the second endpoint of the segment
     * @return the intersection point between the segment and the box, or {@code null} if there is
     * none.
     */
    static Point2D.Double findIntersection(Box box, Point2D.Double firstPoint, Point2D.Double secondPoint) {
        if (firstPoint.x == secondPoint.x) {
            double x = firstPoint.x;
            if (box.left <= x && x <= box.right) {
                if (firstPoint.y <= box.top && box.top <= secondPoint.y) {
                    return new Point2D.Double(x, box.top);
                } else if (firstPoint.y >= box.bottom && box.bottom >= secondPoint.y) {
                    return new Point2D.Double(x, box.bottom);
                } else if (firstPoint.y >= box.top && firstPoint.y <= box.bottom) {
                    if (secondPoint.y <= box.top) {
                        return new Point2D.Double(x, box.top);
                    } else if (secondPoint.y >= box.bottom) {
                        return new Point2D.Double(x, box.bottom);
                    }
                }
            }
            return null;
        } else if (firstPoint.y == secondPoint.y) {
            double y = firstPoint.y;
            if (box.top <= y && y <= box.bottom) {
                if (firstPoint.x <= box.left && box.left <= secondPoint.x) {
                    return new Point2D.Double(box.left, y);
                } else if (firstPoint.x >= box.right && box.right >= secondPoint.x) {
                    return new Point2D.Double(box.right, y);
                } else if (firstPoint.x >= box.left && firstPoint.x <= box.right) {
                    if (secondPoint.x <= box.left) {
                        return new Point2D.Double(box.left, y);
                    } else if (secondPoint.x >= box.right) {
                        return new Point2D.Double(box.right, y);
                    }
                }
            }
            return null;
        }
        
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
        double minX = Math.min(firstPoint.x, secondPoint.x);
        double maxX = Math.max(firstPoint.x, secondPoint.x);
        double minY = Math.min(firstPoint.y, secondPoint.y);
        double maxY = Math.max(firstPoint.y, secondPoint.y);
        for (int i = 0; i < 4; i++) {
//            if (((1 << i) & sides) == 0) {
//                continue;
//            }

            int j = (i + 1) % 4;
            double x, y, t;
            if (cx[i] == cx[j]) {
                if (firstPoint.x != secondPoint.x) {
                    x = cx[i];
                    if (minX <= x && x <= maxX) {
                        t = (x - firstPoint.x) / (secondPoint.x - firstPoint.x);
                        y = firstPoint.y + t * (secondPoint.y - firstPoint.y);
                        t = Math.abs(t);
                        if (t < resT && Math.min(cy[i], cy[j]) <= y && y <= Math.max(cy[i], cy[j])
                                && minY <= y && y <= maxY) {
                            resX = x;
                            resY = y;
                            resT = t;
                        }
                    }
                }
            } else {
                if (firstPoint.y != secondPoint.y) {
                    y = cy[i];
                    if (minY <= y && y <= maxY) {
                        t = (y - firstPoint.y) / (secondPoint.y - firstPoint.y);
                        x = firstPoint.x + t * (secondPoint.x - firstPoint.x);
                        t = Math.abs(t);
                        if (t < resT && Math.min(cx[i], cx[j]) <= x && x <= Math.max(cx[i], cx[j])
                                && minX <= x && x <= maxX) {
                            resX = x;
                            resY = y;
                            resT = t;
                        }
                    }
                }
            }
        }
        
        return (resT < Double.MAX_VALUE ? new Point2D.Double(resX, resY) : null);
//        double left = box.getLeft(), right = box.getRight(), top = box.getTop(), bottom = box.getBottom();
//        if (firstPoint.y == secondPoint.y && top <= firstPoint.y && firstPoint.y <= bottom) {
//            if ((firstPoint.x <= left && right <= secondPoint.x)
//                    || (secondPoint.x <= left && right <= firstPoint.x)) {
//                return new Point2D.Double(firstPoint.x < secondPoint.x ? left : right, firstPoint.y);
//            } else if ((firstPoint.x <= left && left <= secondPoint.x)
//                    || (secondPoint.x <= left && left <= firstPoint.x)) {
//                return new Point2D.Double(left, firstPoint.y);
//            } else if ((firstPoint.x <= right && right <= secondPoint.x)
//                    || (secondPoint.x <= right && right <= firstPoint.x)) {
//                return new Point2D.Double(right, firstPoint.y);
//            }
//        } else if (firstPoint.x == secondPoint.x && left <= firstPoint.x && firstPoint.x <= right) {
//            if ((firstPoint.y <= top && bottom <= secondPoint.y)
//                    || (secondPoint.y <= top && bottom <= firstPoint.y)) {
//                return new Point2D.Double(firstPoint.x, firstPoint.y < secondPoint.y ? top : bottom);
//            } else if ((firstPoint.y <= top && top <= secondPoint.y)
//                    || (secondPoint.y <= top && top <= firstPoint.y)) {
//                return new Point2D.Double(firstPoint.x, top);
//            } else if ((firstPoint.y <= bottom && bottom <= secondPoint.y)
//                    || (secondPoint.y <= bottom && bottom <= firstPoint.y)) {
//                return new Point2D.Double(firstPoint.x, bottom);
//            }
//        }
//        return null;
    }

    /**
     * Returns whether the given box intersects the given line at any point.
     *
     * @param box the box whose intersection to check
     * @param line the line whose intersection to check
     * @param onPerimeter whether the line points on perimeter are considered to intersect the box
     * @return whether the given line and box intersect
     */
    static boolean boxIntersectsLine(Box box, Line line, boolean onPerimeter) {
        ArrayList<Point2D.Double> points = line.lineGeometry.points;
        for (int i = 0; i + 1 < points.size(); i++) {
            Point2D.Double first = points.get(i);
            Point2D.Double second = points.get(i + 1);
            if (line.getType() == Line.LineType.ORTHOGONAL) {
                if (first.x == second.x
                        && box.left <= first.x && first.x <= box.right
                        && !(onPerimeter
                        ? (first.y < box.top && second.y < box.top) || (first.y > box.bottom && second.y > box.bottom)
                        : (first.y <= box.top && second.y <= box.top) || (first.y >= box.bottom && second.y >= box.bottom))) {
                    return true;
                } else if (first.y == second.y
                        && box.top <= first.y && first.y <= box.bottom
                        && !(onPerimeter
                        ? (first.x < box.left && second.x < box.left) || (first.x > box.right && second.x > box.right)
                        : (first.x <= box.left && second.x <= box.left) || (first.x >= box.right && second.x >= box.right))) {
                    return true;
                }
            } else if (findIntersection(box, first, second) != null) {
                return true;
            }
        }
        return false;
    }

    /**
     * Finds the closest box side midpoint to the given point.
     *
     * @param box the box whose midpoints to consider
     * @param point the point for which to find the closest midpoint
     * @param sides the allowed sides of the box
     * @return the closest box side midpoint to the given point.
     */
    static Point2D.Double findClosestMidPoint(Box box, Point2D.Double point, int sides) {
        double[] cx = new double[4], cy = new double[4];
        
        ArrayList<Point2D.Double> midPoints = new ArrayList<>();
        cx[0] = cx[3] = box.getLeft();
        cx[1] = cx[2] = box.getRight();
        cy[0] = cy[1] = box.getTop();
        cy[2] = cy[3] = box.getBottom();
        for (int i = 0; i < 4; i++) {
            if ((sides & (1 << i)) != 0) {
                int j = (i + 1) % 4;
                midPoints.add(new Point2D.Double((cx[i] + cx[j]) / 2, (cy[i] + cy[j]) / 2));
            }
        }
        
        double distance = Double.POSITIVE_INFINITY;
        Point2D.Double result = null;
        for (Point2D.Double midPoint : midPoints) {
            double currDistance = midPoint.distance(point);
            if (currDistance < distance) {
                result = midPoint;
                distance = currDistance;
            }
        }
        return result;
    }

    /**
     * Returns the midpoint of the given box's given side.
     *
     * @param box the box whose side's midpoint to find
     * @param side the side whose midpoint to find
     * @return the midpoint of the given box's given side.
     */
    static Point2D.Double findSideMidPoint(Box box, BoxSide side) {
        switch (side) {
            case LEFT:
                return new Point2D.Double(box.left, box.getCenterY());
            case RIGHT:
                return new Point2D.Double(box.right, box.getCenterY());
            case BOTTOM:
                return new Point2D.Double(box.getCenterX(), box.bottom);
            case TOP:
                return new Point2D.Double(box.getCenterX(), box.top);
            default:
                return null;
        }
    }

    /**
     * Moves the given point to the given segment of the given line. The point is moved to its
     * projection on the segment's line. If this lies beyond an end of the segment, moves the point
     * to that end of the segment.
     *
     * @param point the point to move
     * @param line the line whose segment to move the point to
     * @param segmentIndex the index of the segment to move the point to
     */
    static void movePointToLine(Point2D.Double point, Line line, int segmentIndex) {
        ArrayList<Point2D.Double> points = line.lineGeometry.points;
        Point2D.Double first = points.get(segmentIndex), second = points.get(segmentIndex + 1);
        if (line.getType() == Line.LineType.ORTHOGONAL) {
            if (first.x == second.x) {
                point.x = first.x;
                double y1 = first.y, y2 = second.y;
                point.y = Math.min(Math.max(y1, y2), Math.max(Math.min(y1, y2), point.y));
            } else {
                point.y = first.y;
                double x1 = first.x, x2 = second.x;
                point.x = Math.min(Math.max(x1, x2), Math.max(Math.min(x1, x2), point.x));
            }
        } else {
            double dotProduct = (second.x - first.x) * (point.x - first.x) + (second.y - first.y) * (point.y - first.y);
            double lengthSq = first.distanceSq(second);
            double ratio = dotProduct / lengthSq;
            ratio = Math.max(0, Math.min(1, ratio));
            point.x = first.x + ratio * (second.x - first.x);
            point.y = first.y + ratio * (second.y - first.y);
        }
    }

    /**
     * Finds the index of the line segment nearest to the given point. Uses Euclidean distance.
     *
     * @param point the point whose nearest segment to find
     * @param line the line from which to find the nearest segment
     * @return the index of the line segment nearest to the given point
     */
    static int findNearestSegment(Point2D.Double point, Line line) {
        double minDist = Double.POSITIVE_INFINITY;
        int index = -1;
        ArrayList<Point2D.Double> points = line.lineGeometry.points;
        for (int i = 0; i + 1 < points.size(); i++) {
            Line2D.Double segment = new Line2D.Double(points.get(i), points.get(i + 1));
            double currDist = segment.ptSegDistSq(point);
            if (currDist < minDist) {
                minDist = currDist;
                index = i;
            }
        }
        return index;
    }

    /**
     * Finds the index of the line segment nearest to the given point and the distance to this
     * segment. Uses Euclidean distance.
     *
     * @param point the point whose nearest segment to find
     * @param line the line from which to find the nearest segment
     * @return the index of the line segment nearest to the given point
     */
    static Pair<Integer, Double> findNearestSegmentAndDistance(Point2D.Double point, Line line) {
        double minDist = Double.POSITIVE_INFINITY;
        int index = -1;
        ArrayList<Point2D.Double> points = line.lineGeometry.points;
        for (int i = 0; i + 1 < points.size(); i++) {
            Line2D.Double segment = new Line2D.Double(points.get(i), points.get(i + 1));
            double currDist = segment.ptSegDistSq(point);
            if (currDist < minDist) {
                minDist = currDist;
                index = i;
            }
        }
        return new Pair<>(index, minDist);
    }

    /**
     * Finds the line index segment and a point corresponding to the given proportion of the given
     * line. The point is returned as a ratio between 0 and 1, inclusive - the ratio of the part of
     * this segment that comes before the found point and its whole length
     *
     * @param position the proportional position along the line for which to find the corresponding
     * segment and point. Should be between 0 and 1, inclusive.
     * @param line the line on which to find this position
     * @return a pair where the first element is the index of the corresponding segment and the
     * second is the ratio of the part of this segment that comes before the found point and its
     * whole length
     */
    static Pair<Integer, Double> findSegmentAndPoint(double position, Line line) {
        double posLength = line.getLength() * position;
        double partLength = 0;
        int index;
        ArrayList<Point2D.Double> points = line.lineGeometry.points;
        boolean orthogonal = line.getType() == Line.LineType.ORTHOGONAL;
        double segmentLength = 0;
        for (index = 0; index < points.size() - 1; index++) {
            Point2D.Double first = points.get(index), second = points.get(index + 1);
            segmentLength = orthogonal ? Math.abs(first.x - second.x) + Math.abs(first.y - second.y) : first.distance(second);
            if (partLength + segmentLength > posLength) {
                break;
            }
            partLength += segmentLength;
        }
        double ratio;
        if (index == points.size() - 1) {
            index = points.size() - 2;
            ratio = 1;
        } else {
            ratio = (posLength - partLength) / segmentLength;
        }
        return new Pair<>(index, ratio);
    }

    /**
     * Finds the distance between the given points. Optimized for points with the same ordinate or
     * abscissa.
     *
     * @param p the first point
     * @param q the second point
     * @return the distance between the given points
     */
    static double findDistance(Point2D.Double p, Point2D.Double q) {
        if (p.x == q.x) {
            return Math.abs(p.y - q.y);
        } else if (p.y == q.y) {
            return Math.abs(p.x - q.x);
        } else {
            return p.distance(q);
        }
    }

    /**
     * Places the given line label on the given segment at the given point. Depending on the label
     * orientation, moves the label away from the segment along the segment's normal vector. In the
     * resulting position the label correctly touches the line with the projection of its center on
     * the segment being the given point.
     *
     * @param label the line label to place
     * @param first the first point of the segment
     * @param second the second point of the segment
     * @param point the point on the segment at which to place the label
     */
    static void placeLabelOnSegmentPoint(LineLabel label, Point2D.Double first, Point2D.Double second, Point2D.Double point) {
        label.setCenter(point);
        Line line = label.getOwner();
        if (label.orientation != Orientation.CENTER) {
            if (line.getType() != Line.LineType.ORTHOGONAL) {
                /**
                 * This normal vector is counter-clockwise.
                 */
                double nx = second.y - first.y;
                double ny = first.x - second.x;
                
                double nLength = Math.sqrt(nx * nx + ny * ny);
                double distance;
                if (label.isRotated()) {
                    distance = label.getHeight() / 2;
                } else {
                    double dx;
                    if ((second.x - first.x) * (second.y - first.y) > 0) {
                        dx = label.getCenterX() - label.left;
                    } else {
                        dx = label.getCenterX() - label.right;
                    }
                    double dy = label.getCenterY() - label.bottom;
                    double crossProduct = nx * dx + ny * dy;
                    distance = Math.abs(crossProduct / nLength);
                }
                double epsilon = label.getDiagram().getEpsilon();
                distance += 2 * epsilon;
                double ratio = distance / nLength;
                nx *= ratio;
                ny *= ratio;
                boolean clockwise = label.isClockwise();
                Point2D.Double center = label.getCenter();
                if (clockwise) {
                    center.x -= nx;
                    center.y -= ny;
                } else {
                    center.x += nx;
                    center.y += ny;
                }
                
                label.setCenter(center);
            } else {
                double mult = label.isClockwise() ? 1 : -1;
                if (first.x == second.x) {
                    if (first.y > second.y) {
                        point.x += mult * label.getWidth() / 2;
                    } else {
                        point.x -= mult * label.getWidth() / 2;
                    }
                } else {
                    if (first.x > second.x) {
                        point.y -= mult * label.getHeight() / 2;
                    } else {
                        point.y += mult * label.getHeight() / 2;
                    }
                }
                label.setCenter(point);
            }
        }
    }

    /**
     * Rotates the given point around the given center by the given angle.
     *
     * @param point the point to rotate
     * @param center the point to rotate the given point around
     * @param angle the angle by which to rotate the point
     */
    static void rotate(Point2D.Double point, Point2D.Double center, double angle) {
        double sin = Math.sin(angle);
        double cos = Math.cos(angle);
        double x = point.x - center.x;
        double y = point.y - center.y;
        point.x = (x * cos - y * sin) + center.x;
        point.y = (x * sin + y * cos) + center.y;
    }

    /**
     * A class for storing a chain of points corresponding to a diagram element. For closed elements
     * the first and last point are the same, so that every segment is included in the chain.
     */
    static class Chain {

        /**
         * The points of the chain.
         */
        Point2D.Double[] points;
        /**
         * A unique id for this chain used to identify it so repeated comparisons aren't made.
         */
        int id;

        /**
         * Creates a new chain of points corresponding to a diagram element.
         *
         * @param points the points of the chain
         * @param id a unique id for this chain to identify it so repeated comparisons aren't made
         */
        public Chain(Point2D.Double[] points, int id) {
            this.points = points;
            this.id = id;
        }

        /**
         * Creates a chain of points corresponding to the given rectangle. The first and last points
         * match, so that adjacent point pairs contain every segment of the element.
         *
         * @param rectangle the element for which to construct a chain of points
         * @param id the id of the chain
         */
        public Chain(AbstractContainer rectangle, int id) {
            points = getChainPoints(rectangle);
            this.id = id;
        }

        /**
         * Returns whether the given label chain intersects this chain or either of the
         * corresponding elements includes the other.
         *
         * @param labelPoints a chain corresponding to a line outside label
         * @return whether the given label intersects this chain or either of the corresponding
         * elements includes the other
         */
        boolean intersects(Point2D.Double[] labelPoints) {
            for (int i = 0; i + 1 < labelPoints.length; i++) {
                for (int j = 0; j + 1 < points.length; j++) {
                    if (Line2D.linesIntersect(labelPoints[i].x, labelPoints[i].y, labelPoints[i + 1].x, labelPoints[i + 1].y,
                            points[j].x, points[j].y, points[j + 1].x, points[j + 1].y)) {
                        return true;
                    }
                }
            }
            if (labelPoints.length > 2 && points.length > 2) {
                Point2D.Double point = labelPoints[0];
                boolean inside = true;
                for (int i = 0; i + 1 < points.length; i++) {
                    if (Line2D.relativeCCW(points[i].x, points[i].y, points[i + 1].x, points[i + 1].y, point.x, point.y) >= 0) {
                        inside = false;
                        break;
                    }
                }
                if (inside) {
                    return true;
                }
                point = points[0];
                for (int i = 0; i + 1 < labelPoints.length; i++) {
                    if (Line2D.relativeCCW(labelPoints[i].x, labelPoints[i].y, labelPoints[i + 1].x, labelPoints[i + 1].y, point.x, point.y) >= 0) {
                        return false;
                    }
                }
                return true;
            }
            return false;
        }
    }

    /**
     * Returns a chain of points corresponding to the given rectangle. The first and last points
     * match, so that adjacent point pairs contain every segment of the element.
     *
     * @param rectangle the rectangle for which to construct a chain of points
     * @return a chain of points corresponding to the given element.
     */
    static Point2D.Double[] getChainPoints(AbstractContainer rectangle) {
        Point2D.Double[] points = new Point2D.Double[5];
        double spacing = rectangle instanceof LineLabel ? 0 : rectangle.spacing;
        double left = rectangle.left - spacing;
        double top = rectangle.top - spacing;
        double right = rectangle.right + spacing;
        double bottom = rectangle.bottom + spacing;
        points[0] = new Point2D.Double(left, top);
        points[1] = new Point2D.Double(right, top);
        points[2] = new Point2D.Double(right, bottom);
        points[3] = new Point2D.Double(left, bottom);
        points[4] = points[0];
        if (rectangle instanceof LineLabel) {
            LineLabel label = (LineLabel) rectangle;
            if (label.isRotated()) {
                double angle = label.getRotationAngle();
                Point2D.Double center = label.getCenter();
                for (int i = 0; i < 4; i++) {
                    rotate(points[i], center, angle);
                }
            }
        }
        return points;
    }

    /**
     * A comparator that sorts points by their x value in ascending order, and by their y value in
     * ascending order in case of tiebreaker.
     */
    static class PointComparator implements Comparator<Point2D.Double> {
        
        @Override
        public int compare(Point2D.Double p1, Point2D.Double p2) {
            return p1.x < p2.x ? -1 : p1.x > p2.x ? 1 : p1.y < p2.y ? -1 : p1.y > p2.y ? 1 : 0;
        }
    }
}
