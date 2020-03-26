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
import java.util.HashMap;
import lv.lumii.layoutengine.Box.BoxSide;
import lv.lumii.layoutengine.GeometryHelper.Chain;
import lv.lumii.layoutengine.LayoutConstraints.GridLayoutConstraints;
import lv.lumii.layoutengine.Line.LineType;
import lv.lumii.layoutengine.LineGeometry.Polyline;
import lv.lumii.layoutengine.OutsideLabel.BoxOutsideLabel;
import lv.lumii.layoutengine.OutsideLabel.LineLabel;
import lv.lumii.layoutengine.OutsideLabel.LineLabel.Orientation;
import lv.lumii.layoutengine.util.Pair;

/**
 * This class adjusts the diagram layout to perform any edits using {@link Normalizer}.
 *
 * @author k
 */
abstract class Adjuster {

    /**
     * This times the diagram epsilon will be the minimum distance from an edge that new elements
     * can be inserted at.
     */
    final static double INSERTION_OFFSET_EPSILON_MULTIPLIER = 4;

    /**
     * Adjust the given diagram to satisfy its layout constraints and description better. The
     * compact layout manager tries to minimize the size of each element, down to their minimum
     * size, while maintaining their positions relative to their owners.
     *
     * @param diagram the diagram to adjust
     */
    static void adjust(Diagram diagram) {
        Normalizer.adjust(diagram);
    }

    /**
     * Places new rectangles with a common parent. The rectangles should be already in the correct
     * place in the element hierarchy. The compact layout manager does so by stretching the owner so
     * that all the new rectangles fit inside it, then inserts the rectangles. The rectangles are
     * grown from {@code growPoints} unless they are still obstructed, in which case they are moved
     * slightly.
     *
     * @param rectangles the rectangles to place
     * @param newCenters the points where the centers of the new rectangles should be
     * @param growPoints the points at which to insert the rectangles, sometimes determines the
     * relative positions of elements if the rectangle overlaps other elements
     * @param prevRect the common previous rectangles in the hierarchy for all the given rectangles
     */
    static void placeRectangles(
            ArrayList<AbstractContainer> rectangles,
            ArrayList<Point2D.Double> newCenters,
            ArrayList<Point2D.Double> growPoints,
            AbstractContainer prevRect) {
        Diagram diagram = prevRect.getDiagram();

        ArrayList<Point2D.Double> oldCenters = new ArrayList<>();
        for (int i = 0; i < rectangles.size(); i++) {
            Point2D.Double center = rectangles.get(i).getCenter();
            rectangles.get(i).transpose(new Point2D.Double(newCenters.get(i).x - center.x, newCenters.get(i).y - center.y));
            oldCenters.add(center);
        }

        Rectangle2D.Double enclosingRectangle;
        if (prevRect != diagram) {
            enclosingRectangle = prevRect.getBounds();
            double minLeft = prevRect.getLeft(), maxRight = prevRect.getRight(),
                    minTop = prevRect.getTop(), maxBottom = prevRect.getBottom();
            for (AbstractContainer rect : rectangles) {
                Rectangle2D.Double bounds = rect.getBounds();
                double spacing = rect.getSpacing();
                minLeft = Math.min(minLeft, bounds.getMinX() - spacing);
                maxRight = Math.max(maxRight, bounds.getMaxX() + spacing);
                minTop = Math.min(minTop, bounds.getMinY() - spacing);
                maxBottom = Math.max(maxBottom, bounds.getMaxY() + spacing);
            }
            enclosingRectangle.setRect(minLeft, minTop, maxRight - minLeft, maxBottom - minTop);

            /**
             * Temporarily removes the given rectangles from the diagram so that during the first
             * adjusting the owner won't push the rectangles anywhere. Then adjusts the owner so
             * that the placed rectangles fit inside it.
             */
            ArrayList<Integer> rowIndices = new ArrayList<>();
            ArrayList<Integer> colIndices = new ArrayList<>();
            for (AbstractContainer rectangle : rectangles) {
                Integer row = null;
                Integer col = null;
                AbstractContainer owner = (AbstractContainer) rectangle.getOwner();
                if (owner.getLayoutConstraints() instanceof GridLayoutConstraints) {
                    GridLayoutConstraints constraints = (GridLayoutConstraints) owner.getLayoutConstraints();
                    row = constraints.getRow(rectangle);
                    col = constraints.getColumn(rectangle);
                }
                rowIndices.add(row);
                colIndices.add(col);
                rectangle.getOwner().removeChild(rectangle);
            }

            prevRect.setCurrentMinSize(enclosingRectangle.getWidth(), enclosingRectangle.getHeight());

            ArrayList<AbstractContainer> tRectangles = new ArrayList<>();
            tRectangles.add(prevRect);
            ArrayList<Point2D.Double> tNewCenters = new ArrayList<>();
            tNewCenters.add(new Point2D.Double(enclosingRectangle.getCenterX(), enclosingRectangle.getCenterY()));
            Normalizer.adjustOldRectangles(diagram, tRectangles, tNewCenters);

            for (int i = 0; i < rectangles.size(); i++) {
                AbstractContainer rectangle = rectangles.get(i);
                AbstractContainer owner = (AbstractContainer) rectangle.getOwner();
                if (owner.getLayoutConstraints() instanceof GridLayoutConstraints) {
                    GridLayoutConstraints constraints = (GridLayoutConstraints) owner.getLayoutConstraints();
                    Integer row = rowIndices.get(i);
                    Integer col = colIndices.get(i);
                    if (row != null || col != null) {
                        constraints._setCell(rectangle, row, col);
                    }
                }
                if (rectangle instanceof Box) {
                    ((Container) rectangle.getOwner()).addBox((Box) rectangle);
                } else if (rectangle instanceof Container) {
                    ((Container) rectangle.getOwner()).addContainer((Container) rectangle);
                } else if (rectangle instanceof InsideLabel) {
                    ((AbstractContainer) rectangle.getOwner()).addInsideLabel((InsideLabel) rectangle);
                }
            }
        } else if (prevRect == diagram) {
            diagram.updateBounds();
        }

        for (AbstractContainer rect : rectangles) {
            rect.setCurrentMinSize(rect.getWidth(), rect.getHeight());
            rect.hideChildren();
        }

        /**
         * If a center of one of the given rectangles lies inside one of the owner's children, the
         * given rectangle is transposed so that its center lies outside any other rectangles. If it
         * lies outside the owner, moves it inside the owner. Moves the grow point next to the
         * nearest side of the conflicting rectangle an epsilon value away.
         */
        double epsilon = diagram.getEpsilon();
        ArrayList<AbstractContainer> nextRects = prevRect.getNextRectangles(false);
        nextRects.removeAll(rectangles);
        for (Point2D.Double growPoint : growPoints) {
            GeometryHelper.movePointOutside(growPoint, nextRects, INSERTION_OFFSET_EPSILON_MULTIPLIER * epsilon);
            GeometryHelper.movePointInside(growPoint, prevRect.left, prevRect.top, prevRect.right, prevRect.bottom, INSERTION_OFFSET_EPSILON_MULTIPLIER * epsilon);
        }

        /*
         * Adjusts the rectangles so they fit into the diagram.
         */
        newCenters = new ArrayList<>();
        for (int i = 0; i < rectangles.size(); i++) {
            AbstractContainer rect = rectangles.get(i);
            newCenters.add(rect.getCenter());
            rect.setDesiredCenter(rect.getCenter());
            rect.collapse(growPoints.get(i));
        }

        Normalizer.adjustRectangles(diagram, rectangles, newCenters);

        for (int i = 0; i < rectangles.size(); i++) {
            AbstractContainer rect = rectangles.get(i);
            rect.showChildren();
            /*
             * Moves the descendant rectangles of the given rectangles to their new positions.
             */
            Point2D.Double newCenter = rect.getCenter();
            Point2D.Double oldCenter = oldCenters.get(i);
            Point2D.Double moveVector = new Point2D.Double(newCenter.x - oldCenter.x,
                    newCenter.y - oldCenter.y);
            rect.getLayoutConstraints().transpose(moveVector);
            for (AbstractContainer descendantRect : rect.getDescendantAbstractContainers(true)) {
                descendantRect.transpose(moveVector);
                descendantRect.getLayoutConstraints().transpose(moveVector);
            }
        }
    }

    /**
     * Places the outside labels in the diagram by growing them.
     *
     * @param diagram the diagram in which to grow labels
     */
    static void growOutsideLabels(Diagram diagram) {
        double epsilon = diagram.getEpsilon();
        double len = epsilon / Normalizer.EPSILON_RATIO;
        for (OutsideLabel label : diagram.getDescendantOutsideLabels()) {
            if (label instanceof BoxOutsideLabel) {
                BoxOutsideLabel boxLabel = (BoxOutsideLabel) label;
                BoxSide side = boxLabel.side;
                Box box = boxLabel.getOwner();

                /**
                 * Calculates the insertion point of the label. Moves the insertion point to the
                 * line on the needed box side.
                 */
                double pos = side.isHorizontal() ? boxLabel.getCenterX() : boxLabel.getCenterY();

                if (side.isHorizontal() && (pos < box.left || box.right < pos)) {
                    pos = box.left + boxLabel.position * box.getWidth();
                }
                if (!side.isHorizontal() && (pos < box.top || box.bottom < pos)) {
                    pos = box.top + boxLabel.position * box.getHeight();
                }

                /**
                 * Moves the insertion point epsilon distance away from the foreign sides.
                 */
                if (side.isHorizontal()) {
                    pos = Math.min(box.right - epsilon, Math.max(box.left + epsilon, pos));
                } else {
                    pos = Math.min(box.bottom - epsilon, Math.max(box.top + epsilon, pos));
                }

                /**
                 * Collapses label to a small rectangle with the starting grow size for the labels.
                 */
                if (side.isHorizontal()) {
                    boxLabel.left = boxLabel.right = pos;
                    if (side == BoxSide.TOP) {
                        boxLabel.top = box.top - len;
                        boxLabel.bottom = box.top;
                    } else {
                        boxLabel.top = box.bottom;
                        boxLabel.bottom = box.bottom + len;
                    }
                } else {
                    boxLabel.top = boxLabel.bottom = pos;
                    if (side == BoxSide.LEFT) {
                        boxLabel.left = box.left - len;
                        boxLabel.right = box.left;
                    } else {
                        boxLabel.left = box.right;
                        boxLabel.right = box.right + len;
                    }
                }
                boxLabel.setDesiredCenter(boxLabel.getCenter());
            } else {
                /**
                 * At this point it is assumed that line label segment indices are correct.
                 */
                LineLabel lineLabel = (LineLabel) label;
                if (lineLabel.position == 0) {
                    lineLabel.segmentIndex = 0;
                } else if (lineLabel.position == 1) {
                    lineLabel.segmentIndex = lineLabel.getOwner().lineGeometry.getPoints().size() - 2;
                }

                if (lineLabel.getOwner().getType() == LineType.ORTHOGONAL) {
                    int segmentIndex = lineLabel.segmentIndex;
                    ArrayList<Point2D.Double> points = lineLabel.getOwner().lineGeometry.getPoints();

                    Point2D.Double first = points.get(segmentIndex), second = points.get(segmentIndex + 1);
                    boolean segmentHorizontal = first.y == second.y;

                    if (segmentHorizontal != lineLabel.segmentHorizontal) {
                        if (lineLabel.autoFlip) {
                            lineLabel._flip();
                        }
                        lineLabel.segmentHorizontal = segmentHorizontal;
                    }

                    double pos = segmentHorizontal ? lineLabel.getCenterX() : lineLabel.getCenterY();

                    if (label.position == 0) {
                        pos = segmentHorizontal ? first.x : first.y;
                    } else if (label.position == 1) {
                        pos = segmentHorizontal ? second.x : second.y;
                    }

                    if (segmentHorizontal) {
                        pos = Math.min(Math.max(first.x, second.x) - epsilon, Math.max(Math.min(first.x, second.x) + epsilon, pos));
                    } else {
                        pos = Math.min(Math.max(first.y, second.y) - epsilon, Math.max(Math.min(first.y, second.y) + epsilon, pos));
                    }

                    boolean leftTop = lineLabel.isLeftTop();
                    Orientation orientation = lineLabel.orientation;
                    if (segmentHorizontal) {
                        lineLabel.left = lineLabel.right = pos;
                        if (orientation == Orientation.CENTER) {
                            lineLabel.top = first.y - len;
                            lineLabel.bottom = first.y + len;
                        } else {
                            if (leftTop) {
                                lineLabel.top = first.y - len;
                                lineLabel.bottom = first.y;
                            } else {
                                lineLabel.top = first.y;
                                lineLabel.bottom = first.y + len;
                            }
                        }
                    } else {
                        lineLabel.top = lineLabel.bottom = pos;
                        if (orientation == Orientation.CENTER) {
                            lineLabel.left = first.x - len;
                            lineLabel.right = first.x + len;
                        } else {
                            if (leftTop) {
                                lineLabel.left = first.x - len;
                                lineLabel.right = first.x;
                            } else {
                                lineLabel.left = first.x;
                                lineLabel.right = first.x + len;
                            }
                        }
                    }
                    lineLabel.setDesiredCenter(lineLabel.getCenter());
                } else {
                    if (lineLabel.position == 0 || lineLabel.position == 1) {
                        LineGeometry lineGeometry = lineLabel.getOwner().lineGeometry;
                        ArrayList<Point2D.Double> points = lineGeometry.points;
                        
                        boolean toChange = true;
                        if (lineGeometry.getType() == LineType.POLYLINE) {
                            Polyline polyline = (Polyline) lineGeometry;
                            if ((lineLabel.position == 0
                                    && polyline.oldStartPoint.equals(points.get(0))
                                    && polyline.oldSecondPoint.equals(points.get(1)))
                                    || (lineLabel.position == 1
                                    && polyline.oldPenultimatePoint.equals(points.get(points.size() - 2))
                                    && polyline.oldEndPoint.equals(points.get(points.size() - 1)))) {
                                toChange = false;
                            }
                        }

                        if (toChange) {
                            GeometryHelper.placeLabelOnSegmentPoint(lineLabel,
                                    points.get(lineLabel.segmentIndex),
                                    points.get(lineLabel.segmentIndex + 1),
                                    lineLabel.position == 0 ? points.get(0) : points.get(points.size() - 1));
                        }
                        lineLabel.currentPosition = lineLabel.position;
                    }
                }
            }
        }

        for (Line line : diagram.getDescendantLines()) {
            if (line.getType() == LineType.POLYLINE) {
                Polyline polyline = (Polyline) line.lineGeometry;
                ArrayList<Point2D.Double> points = polyline.points;
                polyline.oldStartPoint = points.get(0);
                polyline.oldSecondPoint = points.get(1);
                polyline.oldPenultimatePoint = points.get(points.size() - 2);
                polyline.oldEndPoint = points.get(points.size() - 1);
            }
        }

        Normalizer.adjustOutsideLabels(diagram);

        for (OutsideLabel label : diagram.getDescendantOutsideLabels()) {
            Point2D.Double newCenter = label.getCenter();
            Point2D.Double oldCenter = label.oldCenter;
            if (!newCenter.equals(oldCenter)) {
                Point2D.Double moveVector = new Point2D.Double(newCenter.x - oldCenter.x, newCenter.y - oldCenter.y);
                for (AbstractContainer insideLabel : label.getDescendantRectangles(false, false)) {
                    insideLabel.transpose(moveVector);
                }
            }
            label.oldCenter = newCenter;
        }

        /**
         * Line label arranging. First creates a grid for storing element perimeter chains.
         */
        double startX = diagram.left;
        double startY = diagram.top;
        double width = diagram.getWidth();
        double height = diagram.getHeight();
        /*
         * In next to lines changed from 1000 to 500.
         */
        int gridWidth = Math.min(600, (int) Math.sqrt(width));
        int gridHeight = Math.min(600, (int) Math.sqrt(height));
        double cellWidth = width / gridWidth;
        double cellHeight = height / gridHeight;
        Grid grid = new Grid(gridWidth, gridHeight, startX, startY, cellWidth, cellHeight);

        /**
         * Inserts element chains in grid.
         */
        HashMap<Box, Pair<Integer, Integer>> boxInOutTime = new HashMap<>();
        insertBoxesInGrid(diagram, grid, boxInOutTime, 0);
        for (Element element : diagram.getDescendants()) {
            if (!(element instanceof Container) && !(element instanceof LineLabel && ((Line) element.getOwner()).getType() != LineType.ORTHOGONAL)) {
                grid.insert(element);
            }
        }

        /**
         * Decides the positions of the polygonal and straight line labels.
         */
        LabelCurrentPositionComparator comparator = new LabelCurrentPositionComparator();
        double minSpacing = diagram.getMinSpacing();
        for (Line line : diagram.getDescendantLines()) {
            if (line.getType() != LineType.ORTHOGONAL) {
                ArrayList<LineLabel> labels = line.getLabels();
                Pair<Integer, Integer> startInOutTime = boxInOutTime.get((Box) line.getStart());
                Pair<Integer, Integer> endInOutTime = boxInOutTime.get((Box) line.getEnd());

                if (!labels.isEmpty()) {
                    /**
                     * Moves the labels so their centers are on the line.
                     */
                    for (LineLabel label : labels) {
                        Point2D.Double center = label.getCenter();
                        GeometryHelper.movePointToLine(center, line, label.segmentIndex);
                        label.setCenter(center);
                    }

                    /**
                     * Sorts labels by their current position.
                     */
                    Collections.sort(labels, comparator);

                    /**
                     * Creates a number pins on the line where the labels will be tried to place.
                     */
                    double lineLength = line.getLength();
                    int pinCount = Math.max(5, Math.min(100, (int) (lineLength / minSpacing)));

                    ArrayList<Point2D.Double> points = line.getPoints();
                    double interval = lineLength / (pinCount + 1);
                    double partLength = 0;
                    double segmentLength = 0;
                    double pinLength = interval;
                    Point2D.Double[] pins = new Point2D.Double[pinCount];
                    int[] pinSegments = new int[pinCount];
                    double[] pinPositions = new double[pinCount];
                    double intervalRatio = 1. / (pinCount + 1);
                    double pinPosition = intervalRatio;
                    int segmentPtr = -1;
                    Point2D.Double first = points.get(0);
                    Point2D.Double second = points.get(0);
                    for (int i = 0; i < pinCount; i++, pinLength += interval) {
                        while (partLength + segmentLength < pinLength) {
                            segmentPtr++;
                            first = second;
                            second = points.get(segmentPtr + 1);
                            partLength += segmentLength;
                            segmentLength = first.distance(second);
                        }

                        double ratio = (pinLength - partLength) / segmentLength;
                        double x = first.x + ratio * (second.x - first.x);
                        double y = first.y + ratio * (second.y - first.y);
                        pins[i] = new Point2D.Double(x, y);
                        pinPositions[i] = pinPosition;
                        pinPosition += intervalRatio;
                        pinSegments[i] = segmentPtr;
                    }

                    /**
                     * First, places the labels with current position 1 and 0, and then all the
                     * other labels.
                     */
                    int rightIndex = labels.size() - 1;
                    while (rightIndex >= 0 && labels.get(rightIndex).currentPosition == 1) {
                        placeLabel(labels.get(rightIndex), pins, pinSegments, pinPositions, pinCount - 1, grid, startInOutTime, endInOutTime);
                        rightIndex--;
                    }
                    for (int leftIndex = 0; leftIndex <= rightIndex; leftIndex++) {
                        LineLabel label = labels.get(leftIndex);
                        placeLabel(label, pins, pinSegments, pinPositions, Math.max(0, Math.min(pinCount - 1, (int) Math.round((lineLength * label.currentPosition) / interval) - 1)), grid, startInOutTime, endInOutTime);
                    }
                }
            }
        }

        diagram.updateBounds();
    }

    /**
     * A grid for storing element perimeter chains. Each grid cell holds all the chains that
     * intersect, contain or are being contained in it.
     */
    static private class Grid {

        /**
         * The cells of the grid.
         */
        Cell[][] cells;
        /**
         * The number of columns.
         */
        int width;
        /**
         * The number of rows.
         */
        int height;
        /**
         * The abscissa of the leftmost column border.
         */
        double startX;
        /**
         * The ordinate of the topmost row border.
         */
        double startY;
        /**
         * The width of each cell.
         */
        double cellWidth;
        /**
         * The height of each row.
         */
        double cellHeight;
        /**
         * A unique identifier pointer for element chains in the grid.
         */
        int nextId;

        /**
         * Creates a new empty grid with the given parameters.
         *
         * @param width the number of columns
         * @param height the number of rows
         * @param startX the abscissa of the leftmost column border
         * @param startY the ordinate of the topmost row border
         * @param cellWidth the width of each cells
         * @param cellHeight the height of each cells
         */
        public Grid(int width, int height, double startX, double startY, double cellWidth, double cellHeight) {
            cells = new Cell[width][height];
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    cells[i][j] = new Cell();
                }
            }
            this.width = width;
            this.height = height;
            this.startX = startX;
            this.startY = startY;
            this.cellWidth = cellWidth;
            this.cellHeight = cellHeight;
            nextId = 0;
        }

        /**
         * Inserts a box chain in the grid.
         *
         * @param box the box to insert
         * @param inTime the time of dfs entering this box
         * @param outTime the time of dfs leaving this box
         */
        void insert(Box box, int inTime, int outTime) {
            Chain chain = new Chain(box, nextId++);
            Pair<Integer, Integer> columnBounds = findColumnBounds(chain.points);
            Pair<Integer, Integer> rowBounds = findRowBounds(chain.points);
            int startColumn = columnBounds.getFirst();
            int endColumn = columnBounds.getSecond();
            int startRow = rowBounds.getFirst();
            int endRow = rowBounds.getSecond();
            for (int column = startColumn; column <= endColumn; column++) {
                for (int row = startRow; row <= endRow; row++) {
                    Cell cell = cells[column][row];
                    cell.boxChains.add(chain);
                    cell.boxInTime.add(inTime);
                    cell.boxOutTime.add(outTime);
                }
            }
        }

        /**
         * Inserts an element chain into the grid. To insert boxes, use the {@link #insert(lv.lumii.layoutengine.Box, int, int)
         * } method.
         *
         * @param element the element to insert
         */
        void insert(Element element) {
            ArrayList<Chain> chains = new ArrayList<>();
            if (element instanceof Line) {
                /**
                 * Creates a chain for each line segment.
                 */
                ArrayList<Point2D.Double> points = ((Line) element).lineGeometry.points;
                for (int i = 0; i + 1 < points.size(); i++) {
                    Point2D.Double[] segment = new Point2D.Double[2];
                    segment[0] = points.get(i);
                    segment[1] = points.get(i + 1);
                    chains.add(new Chain(segment, nextId++));
                }
            } else {
                chains.add(new Chain((AbstractContainer) element, nextId++));
            }
            for (Chain chain : chains) {
                Pair<Integer, Integer> columnBounds = findColumnBounds(chain.points);
                Pair<Integer, Integer> rowBounds = findRowBounds(chain.points);
                int startColumn = columnBounds.getFirst();
                int endColumn = columnBounds.getSecond();
                int startRow = rowBounds.getFirst();
                int endRow = rowBounds.getSecond();
                for (int column = startColumn; column <= endColumn; column++) {
                    for (int row = startRow; row <= endRow; row++) {
                        Cell cell = cells[column][row];
                        if (element instanceof InsideLabel) {
                            cell.insideLabelChains.add(chain);
                        } else if (element instanceof OutsideLabel) {
                            cell.outsideLabelChains.add(chain);
                        } else if (element instanceof Line) {
                            cell.lineChains.add(chain);
                        }
                    }
                }
            }
        }

        /**
         * Finds the minimal column bounds of the grid cell rectangle that contains the bounding
         * rectangle of the given chain.
         *
         * @param chain the chain for which to find the bounds
         * @return the grid column bounds for this chain
         */
        Pair<Integer, Integer> findColumnBounds(Point2D.Double[] chain) {
            double minX = Double.POSITIVE_INFINITY;
            double maxX = Double.NEGATIVE_INFINITY;
            for (Point2D.Double point : chain) {
                if (minX > point.x) {
                    minX = point.x;
                }
                if (maxX < point.x) {
                    maxX = point.x;
                }
            }
            int startColumn = Math.max(0, Math.min(width - 1, (int) Math.floor((minX - startX) / cellWidth)));
            int endColumn = Math.max(0, Math.min(width - 1, (int) Math.floor((maxX - startX) / cellWidth)));
            return new Pair<>(startColumn, endColumn);
        }

        /**
         * Finds the minimal row bounds of the grid cell rectangle that contains the bounding
         * rectangle of the given chain.
         *
         * @param chain the chain for which to find the bounds
         * @return the grid row bounds for this chain
         */
        Pair<Integer, Integer> findRowBounds(Point2D.Double[] chain) {
            double minY = Double.POSITIVE_INFINITY;
            double maxY = Double.NEGATIVE_INFINITY;
            for (Point2D.Double point : chain) {
                if (minY > point.y) {
                    minY = point.y;
                }
                if (maxY < point.y) {
                    maxY = point.y;
                }
            }
            int startRow = Math.max(0, Math.min(height - 1, (int) Math.floor((minY - startY) / cellHeight)));
            int endRow = Math.max(0, Math.min(height - 1, (int) Math.floor((maxY - startY) / cellHeight)));
            return new Pair<>(startRow, endRow);
        }
    }

    /**
     * A rectangle of the grid that contains element perimeter chains.
     */
    static private class Cell {

        /**
         * The chains of the boxes in the cell.
         */
        ArrayList<Chain> boxChains;
        /**
         * The dfs entering time for boxes.
         */
        ArrayList<Integer> boxInTime;
        /**
         * The dfs leaving time for boxes.
         */
        ArrayList<Integer> boxOutTime;
        /**
         * The chains of the inside labels in the cell.
         */
        ArrayList<Chain> insideLabelChains;
        /**
         * The chains of the outside labels in the cell.
         */
        ArrayList<Chain> outsideLabelChains;
        /**
         * The chains of the line segments in the cell.
         */
        ArrayList<Chain> lineChains;

        /**
         * Creates a new empty cell.
         */
        Cell() {
            boxChains = new ArrayList<>();
            boxInTime = new ArrayList<>();
            boxOutTime = new ArrayList<>();
            insideLabelChains = new ArrayList<>();
            outsideLabelChains = new ArrayList<>();
            lineChains = new ArrayList<>();
        }

        /**
         * Returns the number of chains in this cell.
         *
         * @return the number of chains in this cell
         */
        int getChainCount() {
            return boxChains.size() + insideLabelChains.size() + outsideLabelChains.size() + lineChains.size();
        }
    }

    /**
     * This method inserts all diagram boxes into the given grid recursively in DFS manner. It also
     * calculates the time of DFS entering and leaving each box, which later is used to quickly
     * check whether a box is a predecessor of some other box.
     *
     * @param box the box to insert in the grid
     * @param grid the grid where to insert the boxes
     * @param boxInOutTime stores the DFS entering and leaving time for each visited box
     * @param timer the time of DFS entering this box
     * @return the time of DFS leaving this box
     */
    static int insertBoxesInGrid(Box box, Grid grid, HashMap<Box, Pair<Integer, Integer>> boxInOutTime, int timer) {
        int inTime = timer;
        for (Box nextBox : box.getNextBoxes()) {
            timer = insertBoxesInGrid(nextBox, grid, boxInOutTime, timer);
        }
        int outTime = timer++;
        boxInOutTime.put(box, new Pair<>(inTime, outTime));
        if (!(box instanceof Diagram)) {
            grid.insert(box, inTime, outTime);
        }
        return timer;
    }

    /**
     * Compares line labels by their current position.
     */
    static class LabelCurrentPositionComparator implements Comparator<LineLabel> {

        @Override
        public int compare(LineLabel label1, LineLabel label2) {
            return Double.compare(label1.currentPosition, label2.currentPosition);
        }
    }

    /**
     * Places the label in an optimal place, tries to minimize the intersection number of the label
     * with other diagram elements. Chooses the best position of some pin on the line of this label.
     *
     * @param label the label to place
     * @param pins the pins on the line
     * @param pinSegments the line segment indices of the pins
     * @param pinPositions the positions of the pins relative to the line length
     * @param closestPin the closest pin to the given label
     * @param grid the grid whose chains to check for intersection
     * @param startInOutTime the DFS entering/leaving times for the start box of the label line
     * @param endInOutTime the DFS entering/leaving times for the end box of the label line
     */
    static void placeLabel(LineLabel label, Point2D.Double[] pins, int[] pinSegments, double[] pinPositions, int closestPin, Grid grid, Pair<Integer, Integer> startInOutTime, Pair<Integer, Integer> endInOutTime) {
        ArrayList<Point2D.Double> points = label.getOwner().lineGeometry.points;
        Point2D.Double bestPoint = label.getCenter();
        /**
         * If the current label place is good, keeps it there.
         */
        int bestIntersectionMask = calculateMaskOnSegmentPoint(label, points.get(label.segmentIndex), points.get(label.segmentIndex + 1), bestPoint, grid, startInOutTime, endInOutTime);
        boolean bestClockwise = label.clockwiseWithBoth;
        int bestSegmentIndex = label.segmentIndex;
        double bestPosition = label.currentPosition;
        if (bestIntersectionMask != 0) {
            int leftPin = closestPin, rightPin = closestPin + 1;
            int leftLimit = 0;//Math.max(0, closestPin - 10);
            int rightLimit = pins.length - 1;//Math.min(pins.length - 1, closestPin + 10);
            /**
             * Checks the closest pins to the left and the right for the better place.
             */
            for (int i = 0; i < pins.length; i++) {
                if (leftPin >= leftLimit) {
                    int pinSegment = pinSegments[leftPin];
                    label.segmentIndex = pinSegment;
                    int intersectionMask = calculateMaskOnSegmentPoint(label, points.get(pinSegment), points.get(pinSegment + 1), pins[leftPin], grid, startInOutTime, endInOutTime);
                    if (intersectionMask < bestIntersectionMask) {
                        bestIntersectionMask = intersectionMask;
                        bestClockwise = label.clockwiseWithBoth;
                        bestSegmentIndex = label.segmentIndex;
                        bestPoint = pins[leftPin];
                        bestPosition = pinPositions[leftPin];
                        if (bestIntersectionMask == 0) {
                            break;
                        }
                    }
                }

                if (rightPin <= rightLimit) {
                    int pinSegment = pinSegments[rightPin];
                    label.segmentIndex = pinSegment;
                    int intersectionMask = calculateMaskOnSegmentPoint(label, points.get(pinSegment), points.get(pinSegment + 1), pins[rightPin], grid, startInOutTime, endInOutTime);
                    if (intersectionMask < bestIntersectionMask) {
                        bestIntersectionMask = intersectionMask;
                        bestClockwise = label.clockwiseWithBoth;
                        bestSegmentIndex = label.segmentIndex;
                        bestPoint = pins[rightPin];
                        bestPosition = pinPositions[rightPin];
                        if (bestIntersectionMask == 0) {
                            break;
                        }
                    }
                }

                leftPin--;
                rightPin++;
            }
        }

        label.clockwiseWithBoth = bestClockwise;
        label.segmentIndex = bestSegmentIndex;
        label.currentPosition = bestPosition;
        GeometryHelper.placeLabelOnSegmentPoint(label, points.get(label.segmentIndex), points.get(label.segmentIndex + 1), bestPoint);

        grid.insert(label);
    }

    /**
     * Calculates the intersection mask for the given label if it were at the given position. The
     * label is placed at an appropriate orientation so that the projection of its center lies at
     * the given point, then the intersection mask is calculated. If the label's orientation is
     * {@link Orientation#BOTH}, tries both sides of the segment and returns the best mask.
     *
     * @param label the label whose intersection mask to calculate
     * @param first the first point of the line segment on which to place the label
     * @param second the second point of the line segment on which to place the label
     * @param point the point on the segment at which to place the projection of the label center
     * @param grid the grid containing all potential obstacle chains sorted into cells
     * @param startInOutTime the DFS in/out time for the line's start box
     * @param endInOutTime the DFS in/out time for the line's end box
     * @return the intersection mask for this label at the given position
     */
    static int calculateMaskOnSegmentPoint(LineLabel label, Point2D.Double first, Point2D.Double second, Point2D.Double point, Grid grid, Pair<Integer, Integer> startInOutTime, Pair<Integer, Integer> endInOutTime) {
        GeometryHelper.placeLabelOnSegmentPoint(label, first, second, point);
        int intersectionMask = calculateIntersections(label, grid, startInOutTime, endInOutTime, first, second);
        if (label.orientation == Orientation.BOTH && intersectionMask != 0) {
            label.clockwiseWithBoth = !label.clockwiseWithBoth;
            GeometryHelper.placeLabelOnSegmentPoint(label, first, second, point);
            int otherSideIntersectionMask = calculateIntersections(label, grid, startInOutTime, endInOutTime, first, second);
            if (otherSideIntersectionMask < intersectionMask) {
                intersectionMask = otherSideIntersectionMask;
            } else {
                label.clockwiseWithBoth = !label.clockwiseWithBoth;
            }
        }
        return intersectionMask;
    }

    /**
     * Calculates the intersection mask for the given label at its current position. mask. The most
     * significant bit denotes intersection with the line end boxes. The next most significant bit
     * denotes intersection with other boxes (not including the ancestors of the end boxes) and
     * inside labels. The next bit denotes intersection with lines, not counting the segment the
     * label lies on. The last bit denotes intersection with other outside labels, not including
     * line outside labels not yet placed.
     *
     * @param label the label whose intersection mask to calculate
     * @param grid the grid containing all potential obstacle chains sorted into cells
     * @param startInOutTime the DFS in/out time for the line's start box
     * @param endInOutTime the DFS in/out time for the line's end box
     * @param first the first point of the segment on which this label is placed
     * @param second the second point of the segment on which this label is placed
     * @return the intersection mask for this label at its current position
     */
    static int calculateIntersections(LineLabel label, Grid grid, Pair<Integer, Integer> startInOutTime, Pair<Integer, Integer> endInOutTime, Point2D.Double first, Point2D.Double second) {
        Point2D.Double[] labelChain = GeometryHelper.getChainPoints(label);
        Pair<Integer, Integer> columnBounds = grid.findColumnBounds(labelChain);
        Pair<Integer, Integer> rowBounds = grid.findRowBounds(labelChain);
        int startColumn = columnBounds.getFirst();
        int endColumn = columnBounds.getSecond();
        int startRow = rowBounds.getFirst();
        int endRow = rowBounds.getSecond();

        int intersectionMask = 0;
        boolean[] comparedChain = new boolean[grid.nextId];

        double epsilon = label.getDiagram().getEpsilon();
        boolean cornerOnLine = false;
        for (int i = 0; i < 4; i++) {
            Point2D.Double corner = labelChain[i];
            if (Line2D.ptSegDistSq(first.x, first.y, second.x, second.y, corner.x, corner.y) < epsilon) {
                cornerOnLine = true;
                break;
            }
        }
        if (!cornerOnLine) {
            intersectionMask |= 1 << 7;
        }

        Line labelLine = label.getOwner();
        Box start = (Box) labelLine.getStart();
        Box end = (Box) labelLine.getEnd();
        if ((new Chain(start, 0)).intersects(labelChain) || (new Chain(end, 0)).intersects(labelChain)) {
            intersectionMask |= 1 << 6;
        }

        boolean intersects;

        if (label.position != 0 && label.position != 1) {
            intersects = false;
            for (int column = startColumn; column <= endColumn && !intersects; column++) {
                for (int row = startRow; row <= endRow && !intersects; row++) {
                    Cell cell = grid.cells[column][row];
                    for (int i = 0; i < cell.boxChains.size(); i++) {
                        int inTime = cell.boxInTime.get(i);
                        int outTime = cell.boxOutTime.get(i);
                        if (Math.min(startInOutTime.getSecond(), outTime) < Math.max(startInOutTime.getFirst(), inTime)
                                && Math.min(endInOutTime.getSecond(), outTime) < Math.max(endInOutTime.getFirst(), inTime)) {
                            Chain chain = cell.boxChains.get(i);
                            if (!comparedChain[chain.id]) {
                                comparedChain[chain.id] = true;
                                if (chain.intersects(labelChain)) {
                                    intersects = true;
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            for (int column = startColumn; column <= endColumn && !intersects; column++) {
                for (int row = startRow; row <= endRow && !intersects; row++) {
                    Cell cell = grid.cells[column][row];
                    for (Chain chain : cell.insideLabelChains) {
                        if (!comparedChain[chain.id]) {
                            comparedChain[chain.id] = true;
                            if (chain.intersects(labelChain)) {
                                intersects = true;
                                break;
                            }
                        }
                    }
                }
            }
            if (intersects) {
                intersectionMask |= 1 << 5;
            }

            boolean countFirstIntersection = label.orientation != Orientation.CENTER;
            intersects = false;
            for (int column = startColumn; column <= endColumn && !intersects; column++) {
                for (int row = startRow; row <= endRow && !intersects; row++) {
                    Cell cell = grid.cells[column][row];
                    for (Chain chain : cell.lineChains) {
                        if (!comparedChain[chain.id]) {
                            comparedChain[chain.id] = true;
                            if (chain.intersects(labelChain)) {
                                if (countFirstIntersection) {
                                    intersects = true;
                                    break;
                                } else {
                                    countFirstIntersection = true;
                                }
                            }
                        }
                    }
                }
            }
            if (intersects) {
                intersectionMask |= 1 << 4;
            }
        }

        intersects = false;
        for (int column = startColumn; column <= endColumn && !intersects; column++) {
            for (int row = startRow; row <= endRow && !intersects; row++) {
                Cell cell = grid.cells[column][row];
                for (Chain chain : cell.outsideLabelChains) {
                    if (!comparedChain[chain.id]) {
                        comparedChain[chain.id] = true;
                        if (chain.intersects(labelChain)) {
                            intersects = true;
                            break;
                        }
                    }
                }
            }
        }
        if (intersects) {
            intersectionMask |= 1 << 3;
        }

        return intersectionMask;
    }

    /**
     * Resizes the given rectangles to the given bounds, moving aside other elements as needed. The
     * compact layout manager does this by pushing aside other elements as the moved rectangles are
     * grown to their new position. The children of the moved rectangles try to maintain their
     * positions, unless it would lie outside the new position of their parents.
     *
     * @param rectangles the rectangles to resize
     * @param bounds the new desired positions of the rectangles
     */
    static void resizeRectangles(ArrayList<AbstractContainer> rectangles, ArrayList<Rectangle2D.Double> bounds) {
        if (rectangles.isEmpty()) {
            return;
        }

        ArrayList<Point2D.Double> newCenters = new ArrayList<>();
        for (int i = 0; i < rectangles.size(); i++) {
            Rectangle2D.Double rectangle = bounds.get(i);
            rectangles.get(i).setCurrentMinSize(rectangle.getWidth(), rectangle.getHeight());
            newCenters.add(new Point2D.Double(rectangle.getCenterX(), rectangle.getCenterY()));
        }
        Normalizer.adjustOldRectangles(rectangles.get(0).getDiagram(), rectangles, newCenters);
    }

    /**
     * Changes the spacing of the given elements to the given values, moving aside other elements as
     * needed.
     *
     * @param rectangles the rectangles whose spacings to change
     * @param rectangleSpacings the new spacings of the rectangles
     * @param lines the lines whose spacings to change
     * @param lineSpacings the new spacings of the lines
     */
    static void changeSpacings(ArrayList<AbstractContainer> rectangles, ArrayList<Double> rectangleSpacings, ArrayList<Line> lines, ArrayList<Double> lineSpacings) {
        if (rectangles.isEmpty() && lines.isEmpty()) {
            return;
        }

        Diagram diagram = rectangles.isEmpty() ? lines.get(0).getDiagram() : rectangles.get(0).getDiagram();
        Normalizer.adjustOldSpacings(diagram, rectangles, rectangleSpacings, lines, lineSpacings);
    }

    /**
     * Sets the points of the given lines. Corrects line segment intersections and inconsistencies.
     * Corrects line label positions and segment indices.
     *
     * @param lines the lines whose points to change
     * @param lineType the new type of the lines, may be the same as their old type
     * @param points the new points of the lines
     */
    static void setLinePoints(ArrayList<Line> lines, Line.LineType lineType, ArrayList<ArrayList<Point2D.Double>> points) {
        ArrayList<ArrayList<Point2D.Double>> oldPoints = new ArrayList<>();
        for (int i = 0; i < lines.size(); i++) {
            Line line = lines.get(i);
            oldPoints.add(line.lineGeometry == null ? null : line.lineGeometry.points);
            if (!(line.getStart() instanceof Box) || !(line.getEnd() instanceof Box)) {
                throw new UnsupportedOperationException("Lines connected to lines are not supported yet.");
            }
        }

        if (lineType == Line.LineType.ORTHOGONAL) {
            ArrayList<Integer> startCount = new ArrayList<>(), endCount = new ArrayList<>();
            for (int i = 0; i < lines.size(); i++) {
                Line line = lines.get(i);

                line.lineGeometry = new LineGeometry.OrthogonalLine((Connectible) line.getStart(), (Connectible) line.getEnd(), points.get(i));
                assert line.isOrthogonal();

                /**
                 * Moves all points inside the containing box.
                 */
                ArrayList<Point2D.Double> linePoints = line.lineGeometry.points;
                Box box = line.getOwner().getPrevBox(true);
                double epsilon = line.getDiagram().getEpsilon();
                if (box != box.getDiagram()) {
                    for (int j = 0; j < linePoints.size(); j++) {
                        GeometryHelper.movePointInside(linePoints.get(j), box.left, box.top, box.right, box.bottom, epsilon);
                    }
                }
                assert line.isOrthogonal() : line.lineGeometry.getPoints();

                /**
                 * Removes unnecessary points.
                 */
                line.lineGeometry.cullPoints();
                assert line.isOrthogonal() : line.lineGeometry.getPoints();

                /*
                 * Cuts the line to the point first touching the end box; after that performs the
                 * same for start box.
                 */
                LineOptimizer.cutLine(line);
                assert line.isOrthogonal() : line.lineGeometry.getPoints();

                /*
                 * Reconnects line end points to boxes.
                 */
                Box startBox = (Box) line.getStart(), endBox = (Box) line.getEnd();
                Pair<Integer, Integer> addedCount = GeometryHelper.reconnectEndPoints(line, startBox, endBox);
                assert line.isOrthogonal() : line.lineGeometry.getPoints();

                /*
                 * If the line still intersects a box it should not, retraces it.
                 */
                Box lcaBox = startBox.findLCABox(endBox);
                if ((startBox != lcaBox && GeometryHelper.boxIntersectsLine(startBox, line, false))
                        || (endBox != lcaBox && GeometryHelper.boxIntersectsLine(endBox, line, false))) {
                    Tracer tracer = new Tracer(startBox, endBox);
                    line.lineGeometry.points = tracer.trace(startBox, endBox, line.getUsedStartSides(), line.getUsedEndSides());

                    line._resetLabels();
                    ArrayList<Point2D.Double> tmpPoints = new ArrayList<>();
                    for (Point2D.Double point : line.getPoints()) {
                        tmpPoints.add(new Point2D.Double(point.x, point.y));
                    }
                    oldPoints.set(i, tmpPoints);

                    startCount.add(0);
                    endCount.add(0);
                } else {
                    startCount.add(addedCount.getFirst() - 1);
                    endCount.add(addedCount.getSecond() - 1);
                }
            }

            /*
             * Cleans up newly created segments near line endpoints.
             */
            LineOptimizer.cleanupLines(lines, startCount, endCount, false);

            /**
             * Places the labels at the new segments in the most similar places.
             */
            LineOptimizer.adjustLabelPlacement(lines, oldPoints);

            /*
             * Corrects all places where line crosses some rectangle unnecessarily.
             */
            LineOptimizer.correctOrthogonalLines(lines);

            /*
             * Updates endPoint sides.
             */
            for (Line line : lines) {
                LineGeometry.OrthogonalLine lineGeometry = (LineGeometry.OrthogonalLine) line.lineGeometry;
                lineGeometry.findEndSides((Connectible) line.getStart(), (Connectible) line.getEnd());
            }
        } else if (lineType == Line.LineType.STRAIGHT) {
            for (int i = 0; i < lines.size(); i++) {
                Line line = lines.get(i);
                ArrayList<Point2D.Double> linePoints = points.get(i);

                if (linePoints.size() > 2) {
                    Point2D.Double x = linePoints.get(0);
                    Point2D.Double y = linePoints.get(linePoints.size() - 1);
                    linePoints = new ArrayList<>();
                    linePoints.add(x);
                    linePoints.add(y);
                }

                line.lineGeometry = new LineGeometry.StraightLine((Connectible) line.getStart(), (Connectible) line.getEnd(), linePoints);

                if (line.getStart() instanceof Box && line.getEnd() instanceof Box) {
                    LineOptimizer.correctStraightLine(line);
                } else {
                    throw new UnsupportedOperationException("Lines connected to lines are not supported yet.");
                }
            }

            /**
             * Places the labels at the new segments in the most similar places.
             */
            LineOptimizer.adjustLabelPlacement(lines, oldPoints);
        } else if (lineType == Line.LineType.POLYLINE) {
            for (int i = 0; i < lines.size(); i++) {
                Line line = lines.get(i);
                ArrayList<Point2D.Double> linePoints = points.get(i);

                line.lineGeometry = new LineGeometry.Polyline((Connectible) line.getStart(), (Connectible) line.getEnd(), linePoints);
                line.lineGeometry.cullPoints();

                if (line.getStart() instanceof Box && line.getEnd() instanceof Box) {
                    if (line.lineGeometry.points.size() <= 2) {
                        if (line.getStart() != line.getEnd()) {
                            LineOptimizer.correctStraightLine(line);
                        } else {
                            line.trace();
                        }
                    } else {
                        LineOptimizer.reconnectPolyline(line);
                        LineOptimizer.cutLine(line);
                    }
                } else {
                    throw new UnsupportedOperationException("Lines connected to lines are not supported yet.");
                }
            }

            /**
             * Places the labels at the new segments in the most similar places.
             */
            LineOptimizer.adjustLabelPlacement(lines, oldPoints);
        }
    }

    /**
     * A class storing methods for diagram adjustment during manual mode.
     */
    abstract static class ManualAdjuster {

        /**
         * Updates the size and lines of the parents of the given rectangle when it is changed while
         * in manual mode.
         *
         * @param rectangle the rectangle whose parent size and lines to update
         */
        static void updateParentRectangles(AbstractContainer rectangle) {
            AbstractContainer currRect = rectangle;
            AbstractContainer prevRect = rectangle.getPrevRect();
            while (prevRect != currRect.getDiagram()) {
                double spacing = currRect.getSpacing();
                prevRect.left = Math.min(prevRect.left, currRect.left - spacing);
                prevRect.right = Math.max(prevRect.right, currRect.right + spacing);
                prevRect.top = Math.min(prevRect.top, currRect.top - spacing);
                prevRect.bottom = Math.max(prevRect.bottom, currRect.bottom + spacing);

                if (prevRect instanceof Box) {
                    for (Line line : ((Box) prevRect).incidentLines) {
                        reconnectEndPoints(line);
                    }
                }

                currRect = prevRect;
                prevRect = prevRect.getPrevRect();
            }
        }

        /**
         * Reconnects the endpoints of the given line in manual mode. Reconnects the endpoints while
         * ignoring any overlaps.
         *
         * @param line the line to reconnect
         */
        static void reconnectEndPoints(Line line) {
            switch (line.getType()) {
                case ORTHOGONAL:
                    GeometryHelper.reconnectEndPoints(line, (Box) line.getStart(), (Box) line.getEnd());
                    break;
                case STRAIGHT:
                    LineOptimizer.correctStraightLine(line);
                    break;
                case POLYLINE:
                    LineOptimizer.reconnectPolyline(line);
                    break;
            }

            Diagram diagram = line.getDiagram();
            LineOptimizer.cutLine(line);
            for (LineLabel label : line.getLabels()) {
                diagram.layoutAdjustOutsideLabel(label);
            }
        }

        /**
         * Sets the given line to the given points, only ensuring adherence to type and connecting
         * line endpoints.
         *
         * @param line the line whose points to set
         * @param lineType the new type of the line
         * @param points the new points of the line
         */
        static void setPoints(Line line, Line.LineType lineType, ArrayList<Point2D.Double> points) {
            switch (lineType) {
                case ORTHOGONAL:
                    line.lineGeometry = new LineGeometry.OrthogonalLine((Connectible) line.getStart(), (Connectible) line.getEnd(), points);
                    break;
                case STRAIGHT:
                    line.lineGeometry = new LineGeometry.StraightLine((Connectible) line.getStart(), (Connectible) line.getEnd(), points);
                    break;
                case POLYLINE:
                    line.lineGeometry = new LineGeometry.Polyline((Connectible) line.getStart(), (Connectible) line.getEnd(), points);
                    break;
            }

            reconnectEndPoints(line);
        }
    }

    /**
     * Updates the current min size for the boxes in the given diagram.
     *
     * @param diagram the diagram whose box current min size to update
     */
    static void updateBoxCurrentMinSize(Diagram diagram) {
        for (Box box : diagram.getDescendantBoxes()) {
            box.setCurrentMinSize(box.getWidth(), box.getHeight());
        }
    }
}
