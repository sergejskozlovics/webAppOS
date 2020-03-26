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
import java.awt.geom.Rectangle2D;
import java.util.*;
import lv.lumii.layoutengine.Box.BoxSide;
import lv.lumii.layoutengine.LayoutConstraints.ConstraintLine;
import lv.lumii.layoutengine.LayoutConstraints.ConstraintType;
import lv.lumii.layoutengine.LayoutConstraints.GridLayoutConstraints;
import lv.lumii.layoutengine.LayoutConstraints.GridLayoutConstraints.LaneElement;
import lv.lumii.layoutengine.Line.LineType;
import lv.lumii.layoutengine.LineGeometry.OrthogonalLine;
import lv.lumii.layoutengine.OrthogonalSegment.*;
import lv.lumii.layoutengine.OrthogonalSegment.LabelSegment.BoxOutsideLabelSegment;
import lv.lumii.layoutengine.OrthogonalSegment.LabelSegment.LineLabelSegment;
import lv.lumii.layoutengine.OrthogonalSegment.RectangleSegment.SegmentType;
import lv.lumii.layoutengine.OutsideLabel.BoxOutsideLabel;
import lv.lumii.layoutengine.OutsideLabel.LineLabel;
import lv.lumii.layoutengine.OutsideLabel.LineLabel.Orientation;
import lv.lumii.layoutengine.funcmin.ExtendedQuadraticOptimizer;
import lv.lumii.layoutengine.obstacleGraph.ObstacleGraph;
import lv.lumii.layoutengine.util.Pair;

/**
 * Contains the methods for diagram normalization.
 *
 * @author Evgeny
 */
abstract class Normalizer {

    //Currently box growpoints can be placed closer to parent edge than new box startsize!
    //  Current epsilon usage:
    //  epsilon - min spacing * STARTING_SIZE_RATIO * EPSILON_RATIO, as the first two terms form the
    //  smallest possible object.
    //  New boxes in effect start out at size Spacing*STARTING_SIZE_RATIO(/2? Might be double counting here.)
    //  CompactLayoutManager.INSERTION_OFFSET_EPSILON_MULTIPLIER=4 * epsilon - the minimum distance
    //  from any edges where a child can be inserted. 
    //  CycleReducer and Funcmin - epsilon is the allowed deviation from the given contraints
    //  BoxSegments are shortened by epsilon.
    //  LineSegments are shortened by epsilon.
    //  The traceline finder doesn't use epsilons, instead extending boxes by just 1/4 of their spacing.
    /**
     * The smallest ratio of current size to minimum size to start growing rectangles from.
     */
    static final double STARTING_SIZE_RATIO = 2e-2;
    /**
     * The ratio by which to multiply the smallest spacing in the diagram multiplied by
     * {@link #STARTING_SIZE_RATIO} to obtain the epsilon of the diagram, as it must be much smaller
     * than the smallest object in the diagram.
     */
    static final double EPSILON_RATIO = 0.000009;
    /**
     * The weight of funcmin terms minimizing rectangle size.
     */
    static final double SIZE_MINIMIZATION_WEIGHT = 10;
    /**
     * The weight of funcmin terms minimizing rectangle drift.
     */
    static final double RECTANGLE_DRIFT_MINIMIZATION_WEIGHT = 1;
    /**
     * The weight of drift minimization funcmin terms for the rectangle currently being moved.
     */
    static final double CURRENT_RECTANGLE_DRIFT_MINIMIZATION_WEIGHT = 10;
    /**
     * The weight of drift minimization used for all segments. Possibly not used at all.
     */
    static final double GENERAL_DRIFT_MINIMIZATION_WEIGHT = 0.001;
    /**
     * The weight of grid cell size minimization.
     */
    static final double CONSTRAINT_SIZE_MINIMIZATION_WEIGHT = 0.001;
    /**
     * The weight with which to stick the outside labels to their owners.
     */
    static final double OUTSIDE_LABEL_STICK_WEIGHT = 10000;
    /**
     * The ratio by which to multiply the smallest spacing in the diagram multiplied by
     * {@link #STARTING_SIZE_RATIO} to obtain the epsilon for the quadratic optimizer.
     */
    static final double OPTIMIZER_EPSILON_RATIO = 0.005;

    /**
     * Normalizes the rectangles of the diagram so that they don't overlap or ignore spacing or
     * other constraints. Does this by recursively growing rectangles.
     *
     * @param diagram the diagram to arrange
     */
    static void arrange(Diagram diagram) {
        growDescendants(null, diagram);
        correctPositions(diagram);
        diagram.updateBoundsFromRectangles();
    }

    /**
     * Normalizes the rectangles of the diagram part so that they don't overlap or ignore spacing or
     * other constraints. Does this by recursively growing rectangles.
     *
     * @param diagramPart the diagram part to arrange
     */
    static void arrange(DiagramPart diagramPart) {
        Container container = diagramPart.enclosingContainer;
        growDescendants(diagramPart.containers, container.getPrevBox(true));
        correctPositions(container);
    }

    /**
     * Arranges the inside labels of the given outside label.
     *
     * @param label the outside label whose inside labels to arrange
     */
    static void arrange(OutsideLabel label) {
        LinkedHashSet<AbstractContainer> part = new LinkedHashSet<>(label.getDescendantRectangles(true, true));
        growDescendants(part, label);
        correctPositions(label);
    }

    /**
     * Transposes all the rectangles inside their previous rectangles. This method should be used
     * only after arranging each level of the diagram.
     *
     * @param container the container whose descendants to transpose (to correct a whole diagram,
     * this container should be diagram)
     */
    static void correctPositions(AbstractContainer container) {
        ArrayList<AbstractContainer> containers = container.getAbstractContainers(true);
        if (container.getPrevRect(true) instanceof Diagram) {
            for (AbstractContainer c : containers) {
                correctPositions(c);
            }
        } else {
            Rectangle2D.Double bounds = container.getPrevRect(true).getBounds();
            Rectangle2D.Double ger = container.findGridEnclosingRectangle();
            /**
             * Moves all the next rectangles inside the current rectangle so their enclosing
             * rectangle is moved by the minimum possible distance.
             */
            Point2D.Double newCorner = new Point2D.Double(
                    Math.min(Math.max(bounds.getMinX(), ger.getMinX()),
                            (bounds.getWidth() > ger.getWidth() ? bounds.getMaxX() - ger.getWidth() : bounds.getCenterX() - ger.getWidth() / 2)),
                    Math.min(Math.max(bounds.getMinY(), ger.getMinY()),
                            (bounds.getHeight() > ger.getHeight() ? bounds.getMaxY() - ger.getHeight() : bounds.getCenterY() - ger.getHeight() / 2)));
            Point2D.Double moveVector = new Point2D.Double(
                    newCorner.x - ger.getMinX(),
                    newCorner.y - ger.getMinY());
            container.getLayoutConstraints().transpose(moveVector);
            for (AbstractContainer c : containers) {
                c.transpose(moveVector);
                correctPositions(c);
            }
        }
    }

    /**
     * Grows all the lower level rectangles of the given rectangle.
     *
     * @param part the diagram part in which to remove rectangle overlaps
     * @param currentContainer the container which descendant rectangles to grow
     */
    private static void growDescendants(LinkedHashSet<? extends AbstractContainer> part, AbstractContainer currentContainer) {
        if (part != null && !part.contains(currentContainer)) {
            return;
        }

        /*
         * First, recursively grows descendants of the inside containers of currentContainer. Also collapses the
         * next rectangles for later normalization.
         */
        for (AbstractContainer container : currentContainer.getAbstractContainers(false)) {
            growDescendants(part, container);
        }
        if (!(currentContainer instanceof Rectangular)) {
            return;
        }

        fixGrid(currentContainer);

        ArrayList<AbstractContainer> rectangles = new ArrayList<>(currentContainer.getNextRectangles(false));
        for (AbstractContainer rectangle : rectangles) {
            rectangle.collapse(rectangle.getCenter());
        }

        /*
         * Gets the segments of next rectangles for the normalization. Also collects the current
         * dimensions to grow rectangles.
         */
        ArrayList<ArrayList<RectangleSegment>> rectangleSegments = generateRectangleSegments(currentContainer, true);
        ArrayList<LinkedHashMap<AbstractContainer, Pair<RectangleSegment, RectangleSegment>>> rectangleSides = generateRectangleSides(rectangleSegments);
        ArrayList<Double> minWidths = new ArrayList<>(), minHeights = new ArrayList<>();
        for (AbstractContainer rectangle : rectangles) {
            minWidths.add(rectangle.getCurrentMinWidth());
            minHeights.add(rectangle.getCurrentMinHeight());
        }

        /**
         * The new rectangles are grown gradually from points. First, grows rectangles to a half of
         * the width; second, grows rectangles to a half of the height; third, grows rectangles to
         * the full width; finally, grows rectangles to the full height.
         */
        for (int i = 0; i < rectangles.size(); i++) {
            AbstractContainer rectangle = rectangles.get(i);

            rectangle.setCurrentMinSize(minWidths.get(i) * 0.5, 0);
            rectangle._setSpacing(rectangle.getSpacing() * 0.5);
            updateSingleRectangle(rectangleSides.get(0).get(rectangle), STARTING_SIZE_RATIO / 0.5);
        }
        arrangeStep(currentContainer, true, rectangleSegments, rectangleSides);
        updateSegments(rectangleSegments.get(1));

        for (int i = 0; i < rectangles.size(); i++) {
            AbstractContainer rectangle = rectangles.get(i);
            rectangle.setCurrentMinSize(minWidths.get(i) * 0.5, minHeights.get(i) * 0.5);
            updateSingleRectangle(rectangleSides.get(1).get(rectangle), 0.5 / 0.5);
        }
        arrangeStep(currentContainer, false, rectangleSegments, rectangleSides);
        updateSegments(rectangleSegments.get(0));

        for (int i = 0; i < rectangles.size(); i++) {
            AbstractContainer rectangle = rectangles.get(i);
            rectangle.setCurrentMinSize(minWidths.get(i), minHeights.get(i) * 0.5);
            rectangle._setSpacing(rectangle.getSpacing() * 2);
            updateSingleRectangle(rectangleSides.get(0).get(rectangle), 0.5 / 1);
        }
        arrangeStep(currentContainer, true, rectangleSegments, rectangleSides);
        updateSegments(rectangleSegments.get(1));

        for (int i = 0; i < rectangles.size(); i++) {
            AbstractContainer rectangle = rectangles.get(i);
            rectangle.setCurrentMinSize(minWidths.get(i), minHeights.get(i));
            updateSingleRectangle(rectangleSides.get(1).get(rectangle), 1 / 1);
        }
        arrangeStep(currentContainer, false, rectangleSegments, rectangleSides);

        Rectangle2D.Double ger = currentContainer.findGridEnclosingRectangle();
        if (ger != null) {
            currentContainer.setCurrentMinSize(
                    Math.max(currentContainer.getCurrentMinWidth(), ger.width),
                    Math.max(currentContainer.getCurrentMinHeight(), ger.height));
        }
    }

    /**
     * Normalizes the next rectangles of the given rectangle in one of the given directions
     * (horizontal or vertical). The segments (horizontal segments in vertical direction and vice
     * versa) of the elements will be pushed to the given direction so the elements are normalized
     * in it.
     *
     * @param rectangle the rectangle which next rectangles to normalize
     * @param horizontalNormalize the direction to normalize to
     * @param rectangleSegments {@link OrthogonalSegment}s corresponding to the sides of the
     * rectangles of the next rectangles, with a separate list for segments running in each
     * direction
     * @param rectangleSides maps from rectangles to the segments corresponding to their sides, with
     * a separate map for segments running in each direction
     */
    static void arrangeStep(AbstractContainer rectangle,
            boolean horizontalNormalize,
            ArrayList<ArrayList<RectangleSegment>> rectangleSegments,
            ArrayList<LinkedHashMap<AbstractContainer, Pair<RectangleSegment, RectangleSegment>>> rectangleSides) {
        ArrayList<OrthogonalSegment> segments = new ArrayList<>();
        int currentDirection = horizontalNormalize ? 0 : 1;
        segments.addAll(rectangleSegments.get(currentDirection));

        ArrayList<ConstraintSegment> constraintSegments = new ArrayList<>();
        ArrayList<Pair<OrthogonalSegment, OrthogonalSegment>> constraintObstacles = new ArrayList<>();
        addConstraints(rectangle, constraintSegments, constraintObstacles, rectangleSides.get(currentDirection), null, null, horizontalNormalize, false, false, 0);

        ArrayList<Pair<OrthogonalSegment, OrthogonalSegment>> obstacleGraph = ObstacleGraph.findObstacleGraph(segments);

        segments.addAll(constraintSegments);
        obstacleGraph.addAll(constraintObstacles);

        normalizeRectangle(segments, obstacleGraph, rectangleSides.get(currentDirection), rectangle, horizontalNormalize);
    }

    /**
     *
     * Arranges the grid row and column borders if the container holds grid constraints. Does this
     * in such a way that minimizes the number of moved rectangles.
     *
     * @param container the container whose grid constraints to fix
     */
    static void fixGrid(AbstractContainer container) {
        if (container.getLayoutConstraints() instanceof GridLayoutConstraints) {
            GridLayoutConstraints constraints = (GridLayoutConstraints) container.getLayoutConstraints();
            ArrayList<LaneElement> laneElements;
            ArrayList<Double> positions;
            double startBound, endBound;

            /*
             * Arranges column borders.
             */
            startBound = Double.MAX_VALUE;
            endBound = -Double.MAX_VALUE;
            laneElements = new ArrayList<>();
            for (AbstractContainer rectangle : container.getRectangles()) {
                Integer col = constraints.getColumn(rectangle);
                if (col != null) {
                    laneElements.add(new LaneElement(rectangle.getCenterX(), col, false, 0, 0));
                    startBound = Math.min(startBound, rectangle.left);
                    endBound = Math.max(endBound, rectangle.right);
                }
            }
            for (Container c : container.getPureContainers()) {
                Integer col = constraints.getColumn(c);
                if (col != null) {
                    for (AbstractContainer rectangle : c.getNextRectangles(false)) {
                        laneElements.add(new LaneElement(rectangle.getCenterX(), col, false, 0, 0));
                        startBound = Math.min(startBound, rectangle.left);
                        endBound = Math.max(endBound, rectangle.right);
                    }
                }
            }
            if (startBound > endBound) {
                Rectangle2D.Double cer = container.findNextRectangleEnclosingRectangle();
                if (cer != null) {
                    startBound = endBound = cer.getCenterX();
                } else {
                    startBound = endBound = 0;
                }
            }
            positions = constraints.arrangeGridBorders(laneElements, true, startBound, endBound);
            for (int i = 1; i < positions.size(); i++) {
                constraints._setColumnRight(i, positions.get(i));
            }

            /*
             * Arranges row borders.
             */
            startBound = Double.MAX_VALUE;
            endBound = -Double.MAX_VALUE;
            laneElements = new ArrayList<>();
            for (AbstractContainer rectangle : container.getRectangles()) {
                Integer row = constraints.getRow(rectangle);
                if (row != null) {
                    laneElements.add(new LaneElement(rectangle.getCenterY(), row, false, 0, 0));
                    startBound = Math.min(startBound, rectangle.top);
                    endBound = Math.max(endBound, rectangle.bottom);
                }
            }
            for (Container c : container.getPureContainers()) {
                Integer row = constraints.getRow(c);
                if (row != null) {
                    for (AbstractContainer rectangle : c.getNextRectangles(false)) {
                        laneElements.add(new LaneElement(rectangle.getCenterY(), row, false, 0, 0));
                        startBound = Math.min(startBound, rectangle.top);
                        endBound = Math.max(endBound, rectangle.bottom);
                    }
                }
            }
            if (startBound > endBound) {
                Rectangle2D.Double cer = container.findNextRectangleEnclosingRectangle();
                if (cer != null) {
                    startBound = endBound = cer.getCenterY();
                } else {
                    startBound = endBound = 0;
                }
            }
            positions = constraints.arrangeGridBorders(laneElements, false, startBound, endBound);
            for (int i = 1; i < positions.size(); i++) {
                constraints._setRowBottom(i, positions.get(i));
            }

            /*
             * Moves the elements to its cell if they aren't inside it.
             */
            for (AbstractContainer c : container.getAbstractContainers(false)) {
                Integer row = constraints.getRow(c), col = constraints.getColumn(c);

                double l, t, r, b;
                if (col != null /*&& fixCols*/) {
                    l = col == 1 ? Double.NEGATIVE_INFINITY : constraints.getColumnLeft(col);
                    r = col == constraints.getColumnCount()
                            ? Double.POSITIVE_INFINITY : constraints.getColumnRight(col);
                } else {
                    l = Double.NEGATIVE_INFINITY;
                    r = Double.POSITIVE_INFINITY;
                }
                if (row != null /*&& fixRows*/) {
                    t = row == 1 ? Double.NEGATIVE_INFINITY : constraints.getRowTop(row);
                    b = row == constraints.getRowCount()
                            ? Double.POSITIVE_INFINITY : constraints.getRowBottom(row);
                } else {
                    t = Double.NEGATIVE_INFINITY;
                    b = Double.POSITIVE_INFINITY;
                }

                if (c instanceof Rectangular) {
                    Point2D.Double center = c.getCenter();
                    if (!GeometryHelper.contains(l, t, r, b, center, 0)) {
                        GeometryHelper.movePointInside(center, l, t, r, b, 0);
                        c.setCenter(center);
                    }
                } else if (c instanceof Container) {
                    for (AbstractContainer rectangle : c.getNextRectangles(false)) {
                        Point2D.Double center = rectangle.getCenter();
                        if (!GeometryHelper.contains(l, t, r, b, center, 0)) {
                            GeometryHelper.movePointInside(center, l, t, r, b, 0);
                            rectangle.setCenter(center);
                        }
                    }
                }
            }
        }

        for (Container childContainer : container.getPureContainers()) {
            fixGrid(childContainer);
        }
    }

    /**
     * Normalizes the next rectangles of the given rectangle in one of the given orthogonal
     * directions.
     *
     * @param segments {@link OrthogonalSegment}s corresponding to the sides of the rectangles of
     * the next rectangles, with a separate list for segments running in each direction
     * @param obstacleGraph the obstacle graph obtained from the rectangle segments
     * @param rectangleSides maps from rectangles to the segments corresponding to their sides, with
     * a separate map for segments running in each direction
     * @param currentRect the rectangle which next rectangles to normalize
     * @param horizontalNormalize the direction to normalize to
     */
    static void normalizeRectangle(ArrayList<OrthogonalSegment> segments,
            ArrayList<Pair<OrthogonalSegment, OrthogonalSegment>> obstacleGraph,
            LinkedHashMap<AbstractContainer, Pair<RectangleSegment, RectangleSegment>> rectangleSides,
            AbstractContainer currentRect, boolean horizontalNormalize) {

        /*
         * The segments are numbered, as quadratic optimization methods work with variable ID's.
         */
        LinkedHashMap<OrthogonalSegment, Integer> segmentIndices = new LinkedHashMap<>();

        ExtendedQuadraticOptimizer optimizer = new ExtendedQuadraticOptimizer(segments.size());

        /*
         * Sets the starting values of the variables to their current value.
         */
        for (int i = 0; i < segments.size(); i++) {
            OrthogonalSegment segment = segments.get(i);
            segmentIndices.put(segment, i);
            optimizer.setVariable(i, segment.getPos());
            optimizer.addQuadraticConstantDifference(i, segment.getPos(), GENERAL_DRIFT_MINIMIZATION_WEIGHT);
        }

        /*
         * Adds the edges of the obstacle graph as constraints.
         */
        for (Pair<OrthogonalSegment, OrthogonalSegment> obstacle : obstacleGraph) {
            OrthogonalSegment leftSegment = obstacle.getFirst();
            OrthogonalSegment rightSegment = obstacle.getSecond();
            optimizer.addInequality(segmentIndices.get(leftSegment), segmentIndices.get(rightSegment),
                    leftSegment.findMinimumDistance(rightSegment));
        }

        /*
         * For each rectangle, adds the rectangle minimum size constraint and the size minimization term.
         */
        for (Pair<RectangleSegment, RectangleSegment> sides : rectangleSides.values()) {
            RectangleSegment leftSegment = sides.getFirst();
            RectangleSegment rightSegment = sides.getSecond();
            AbstractContainer rectangle = leftSegment.getContainer();
            optimizer.addInequality(segmentIndices.get(leftSegment), segmentIndices.get(rightSegment),
                    horizontalNormalize ? rectangle.getCurrentMinWidth() : rectangle.getCurrentMinHeight());
            optimizer.addQuadraticDifference(segmentIndices.get(leftSegment), segmentIndices.get(rightSegment), SIZE_MINIMIZATION_WEIGHT);
        }

        /*
         * Sets the rectangle drift terms for the children of currentRect.
         */
        for (AbstractContainer rectangle : currentRect.getNextRectangles(false)) {
            assert rectangleSides.containsKey(rectangle);
            RectangleSegment leftSegment = rectangleSides.get(rectangle).getFirst();
            RectangleSegment rightSegment = rectangleSides.get(rectangle).getSecond();
            optimizer.addMeanDifference(segmentIndices.get(rightSegment),
                    segmentIndices.get(leftSegment), (rightSegment.getPos() + leftSegment.getPos()) / 2,
                    RECTANGLE_DRIFT_MINIMIZATION_WEIGHT);
        }

        double newPositions[] = optimizer.performOptimization();
        double epsilon = currentRect.getDiagram().getEpsilon();
        for (int i = 0; i < segments.size(); i++) {
            if (Math.abs(newPositions[i] - segments.get(i).getPos()) > epsilon) {
                segments.get(i).move(newPositions[i]);
            }
        }
    }

    /**
     * Adjusts the layout of the diagram.
     *
     * @param diagram the diagram whose layout to adjust
     */
    static void adjust(Diagram diagram) {
        ArrayList<ArrayList<OrthogonalLineSegment>> lineSegments = generateLineSegments(diagram, null);
        ArrayList<ArrayList<RectangleSegment>> rectangleSegments = generateRectangleSegments(diagram, false);
        ArrayList<LinkedHashMap<AbstractContainer, Pair<RectangleSegment, RectangleSegment>>> rectangleSides
                = generateRectangleSides(rectangleSegments);

        adjustStep(diagram, true, lineSegments, rectangleSegments, rectangleSides, false, new ArrayList<AbstractContainer>(), new ArrayList<Point2D.Double>());
        updateSegments(rectangleSegments.get(1));
        adjustStep(diagram, false, lineSegments, rectangleSegments, rectangleSides, false, new ArrayList<AbstractContainer>(), new ArrayList<Point2D.Double>());

        //normalizeLines(diagram, lineSegments, boxSegments, boxSides);
        //assert testDiagram(diagram);
    }

    /**
     * Adjusts the layout of the diagram along with the given existing rectangles. The rectangles
     * are moved to {@code newCenters} as their new centers in the diagram.
     *
     * @param diagram the diagram whose layout to adjust
     * @param rectangles the changed rectangles
     * @param newCenters the new centers of the rectangles
     */
    static void adjustOldRectangles(Diagram diagram, ArrayList<AbstractContainer> rectangles, ArrayList<Point2D.Double> newCenters) {
        ArrayList<ArrayList<OrthogonalLineSegment>> lineSegments = generateLineSegments(diagram, null);
        ArrayList<ArrayList<RectangleSegment>> rectangleSegments = generateRectangleSegments(diagram, false);
        ArrayList<LinkedHashMap<AbstractContainer, Pair<RectangleSegment, RectangleSegment>>> rectangleSides
                = generateRectangleSides(rectangleSegments);

        adjustStep(diagram, true, lineSegments, rectangleSegments, rectangleSides, false, rectangles, newCenters);
        updateSegments(rectangleSegments.get(1));
        adjustStep(diagram, false, lineSegments, rectangleSegments, rectangleSides, false, rectangles, newCenters);
    }

    /**
     * Adjusts the layout of the diagram and grows the outside labels in the diagram.
     *
     * @param diagram the diagram to adjust
     */
    static void adjustOutsideLabels(Diagram diagram) {
        HashMap<Line, ArrayList<OrthogonalLineSegment>> lineSegmentMap = new HashMap<>();
        ArrayList<ArrayList<OrthogonalLineSegment>> lineSegments = generateLineSegments(diagram, lineSegmentMap);
        ArrayList<ArrayList<RectangleSegment>> rectangleSegments = generateRectangleSegments(diagram, false);
        ArrayList<LinkedHashMap<AbstractContainer, Pair<RectangleSegment, RectangleSegment>>> rectangleSides
                = generateRectangleSides(rectangleSegments);

        /**
         * The adjusting proceeds in three steps. First, normalizes the vertical outside label
         * segments by the horizontal box sides. Second, normalizes also all the horizontal outside
         * label segments. Last, normalizes all of the outside label segments. Such sequence is
         * needed so that outside labels would not cross each other.
         */
        rectangleSegments.get(0).addAll(generateOutsideLabelSegments(diagram, true, false, rectangleSides.get(0), lineSegmentMap));
        adjustStep(diagram, true, lineSegments, rectangleSegments, rectangleSides, true, new ArrayList<AbstractContainer>(), new ArrayList<Point2D.Double>());
        /**
         * Replace labels on horizontal sides so their positioning after previous adjust step is
         * correct, that is, they are pinned to their sides/segments.
         */
        for (OutsideLabel label : diagram.getDescendantOutsideLabels()) {
            if (label instanceof BoxOutsideLabel) {
                BoxOutsideLabel boxLabel = (BoxOutsideLabel) label;
                Box box = boxLabel.getOwner();
                BoxSide side = boxLabel.side;
                if (side == BoxSide.LEFT) {
                    boxLabel.left = box.left - boxLabel.getWidth();
                    boxLabel.right = box.left;
                } else if (side == BoxSide.RIGHT) {
                    boxLabel.right = box.right + boxLabel.getWidth();
                    boxLabel.left = box.right;
                }
            } else if (((Line) label.getOwner()).getType() == LineType.ORTHOGONAL) {
                LineLabel lineLabel = (LineLabel) label;
                ArrayList<Point2D.Double> points = lineLabel.getOwner().getPoints();
                boolean leftTop = lineLabel.isLeftTop();
                int segmentIndex = lineLabel.segmentIndex;
                Point2D.Double first = points.get(segmentIndex), second = points.get(segmentIndex + 1);
                if (first.x == second.x) {
                    if (lineLabel.orientation == Orientation.CENTER) {
                        lineLabel.left = first.x - lineLabel.getWidth();
                        lineLabel.right = 2 * first.x - lineLabel.left;
                    } else if (leftTop) {
                        lineLabel.left = first.x - lineLabel.getWidth();
                        lineLabel.right = first.x;
                    } else {
                        lineLabel.right = first.x + lineLabel.getWidth();
                        lineLabel.left = first.x;
                    }
                }
            }
        }
        updateSegments(rectangleSegments.get(1));
        rectangleSegments.get(1).addAll(generateOutsideLabelSegments(diagram, false, false, rectangleSides.get(1), lineSegmentMap));
        rectangleSegments.get(1).addAll(generateOutsideLabelSegments(diagram, false, true, rectangleSides.get(1), lineSegmentMap));
        adjustStep(diagram, false, lineSegments, rectangleSegments, rectangleSides, true, new ArrayList<AbstractContainer>(), new ArrayList<Point2D.Double>());
        updateSegments(rectangleSegments.get(0));
        rectangleSegments.get(0).addAll(generateOutsideLabelSegments(diagram, true, true, rectangleSides.get(0), lineSegmentMap));
        adjustStep(diagram, true, lineSegments, rectangleSegments, rectangleSides, true, new ArrayList<AbstractContainer>(), new ArrayList<Point2D.Double>());
    }

    /**
     * Adjusts the layout of the diagram along with changing the spacings of the given elements.
     *
     * @param diagram the diagram whose layout to adjust
     * @param rectangles the changed rectangles
     * @param rectangleSpacings the new spacings of the rectangles
     * @param lines the changed lines
     * @param lineSpacings the new spacings of the lines
     */
    static void adjustOldSpacings(Diagram diagram, ArrayList<AbstractContainer> rectangles, ArrayList<Double> rectangleSpacings, ArrayList<Line> lines, ArrayList<Double> lineSpacings) {
        ArrayList<ArrayList<OrthogonalLineSegment>> lineSegments = generateLineSegments(diagram, null);
        ArrayList<ArrayList<RectangleSegment>> rectangleSegments = generateRectangleSegments(diagram, false);
        ArrayList<LinkedHashMap<AbstractContainer, Pair<RectangleSegment, RectangleSegment>>> rectangleSides
                = generateRectangleSides(rectangleSegments);
        /**
         * The new spacing is set after segment generation to prevent segment overlap.
         */
        for (int i = 0; i < rectangles.size(); i++) {
            rectangles.get(i)._setSpacing(rectangleSpacings.get(i));
        }
        for (int i = 0; i < lines.size(); i++) {
            lines.get(i)._setSpacing(lineSpacings.get(i));
        }
        diagram.updateEpsilon();
        adjustStep(diagram, true, lineSegments, rectangleSegments, rectangleSides, false, new ArrayList<AbstractContainer>(), new ArrayList<Point2D.Double>());
        updateSegments(rectangleSegments.get(1));
        adjustStep(diagram, false, lineSegments, rectangleSegments, rectangleSides, false, new ArrayList<AbstractContainer>(), new ArrayList<Point2D.Double>());

        //normalizeLines(diagram, lineSegments, boxSegments, boxSides);
        //assert testDiagram(diagram);
    }

    /**
     * Adjusts the layout of the diagram along with newly inserted rectangles. The rectangles are
     * inserted with {@code newCenters} as their desired centers in the diagram.
     *
     * @param diagram the diagram whose layout to adjust
     * @param rectangles the newly inserted rectangles
     * @param newCenters the new centers of the rectangles
     */
    static void adjustRectangles(Diagram diagram, ArrayList<AbstractContainer> rectangles, ArrayList<Point2D.Double> newCenters) {
        ArrayList<ArrayList<OrthogonalLineSegment>> lineSegments = generateLineSegments(diagram, null);
        ArrayList<ArrayList<RectangleSegment>> rectangleSegments = generateRectangleSegments(diagram, false);

        ArrayList<LinkedHashMap<AbstractContainer, Pair<RectangleSegment, RectangleSegment>>> rectangleSides
                = generateRectangleSides(rectangleSegments);
        ArrayList<Double> minWidths = new ArrayList<>(), minHeights = new ArrayList<>();
        for (AbstractContainer rectangle : rectangles) {
            minWidths.add(rectangle.getCurrentMinWidth());
            minHeights.add(rectangle.getCurrentMinHeight());
        }

        /**
         * The new rectangles are grown gradually from points.
         */
        for (int i = 0; i < rectangles.size(); i++) {
            AbstractContainer rectangle = rectangles.get(i);

            rectangle.setCurrentMinSize(minWidths.get(i) * 0.5, 0);
            double qqq = rectangle.getSpacing() * 0.5;
            if (qqq < 3)
              qqq = 3;
            rectangle._setSpacing(qqq);
            updateSingleRectangle(rectangleSides.get(0).get(rectangle), STARTING_SIZE_RATIO / 0.5);
        }
        adjustStep(diagram, true, lineSegments, rectangleSegments, rectangleSides, false, rectangles, newCenters);
        updateSegments(rectangleSegments.get(1));

        for (int i = 0; i < rectangles.size(); i++) {
            AbstractContainer rectangle = rectangles.get(i);
            rectangle.setCurrentMinSize(minWidths.get(i) * 0.5, minHeights.get(i) * 0.5);
            updateSingleRectangle(rectangleSides.get(1).get(rectangle), 0.5 / 0.5);
        }
        adjustStep(diagram, false, lineSegments, rectangleSegments, rectangleSides, false, rectangles, newCenters);
        updateSegments(rectangleSegments.get(0));

        for (int i = 0; i < rectangles.size(); i++) {
            AbstractContainer rectangle = rectangles.get(i);
            rectangle.setCurrentMinSize(minWidths.get(i), minHeights.get(i) * 0.5);
            rectangle._setSpacing(rectangle.getSpacing() * 2);
            updateSingleRectangle(rectangleSides.get(0).get(rectangle), 0.5 / 1);
        }
        adjustStep(diagram, true, lineSegments, rectangleSegments, rectangleSides, false, rectangles, newCenters);
        updateSegments(rectangleSegments.get(1));

        for (int i = 0; i < rectangles.size(); i++) {
            AbstractContainer rectangle = rectangles.get(i);
            rectangle.setCurrentMinSize(minWidths.get(i), minHeights.get(i));
            updateSingleRectangle(rectangleSides.get(1).get(rectangle), 1 / 1);
        }
        adjustStep(diagram, false, lineSegments, rectangleSegments, rectangleSides, false, rectangles, newCenters);

        //normalizeLines(diagram, lineSegments, boxSegments, boxSides);
        //assert testDiagram(diagram);
    }

    /**
     * Generates the OrthogonalSegment segments corresponding to the line segments in both of the
     * orthogonal directions. Returns an ArrayList containing two lists with vertical and horizontal
     * line segments respectively.
     *
     * @param diagram the diagram for which to prepare line segments
     * @param lineSegmentMap maps each line to an {@link OrthogonalSegment} list which corresponds
     * to line segments
     * @return the OrthogonalSegment segments corresponding to the line segments.
     */
    static ArrayList<ArrayList<OrthogonalLineSegment>> generateLineSegments(Diagram diagram, HashMap<Line, ArrayList<OrthogonalLineSegment>> lineSegmentMap) {
        ArrayList<ArrayList<OrthogonalLineSegment>> segments = new ArrayList<>();
        segments.add(new ArrayList<OrthogonalLineSegment>());
        segments.add(new ArrayList<OrthogonalLineSegment>());

        /*
         * Prepares the line segments for the obstacle graph calculation. Each segment corresponds
         * to a segment of some line extended at both ends by the line's spacing. Each
         * OrthogonalSegment formed is stored in one of the two lists of 'segments' (either
         * horizontal or vertical).
         */
        ArrayList<Line> lines = diagram.getDescendantLines();
        for (Line line : lines) {
            if (line.hasGeometry() && (line.getType() == LineType.ORTHOGONAL)) { // first check by SK
                ArrayList<OrthogonalLineSegment> singleLineSegments = null;
                if (lineSegmentMap != null) {
                    singleLineSegments = new ArrayList<>();
                    lineSegmentMap.put(line, singleLineSegments);
                }
                Point2D.Double prev = line.getStartPoint(), curr;
                /*
                 * Denotes the previous line segment in respect to the current line segment.
                 */
                OrthogonalLineSegment prevSegment = null;
                for (ListIterator<Point2D.Double> it = line.lineGeometry.points.listIterator(1); it.hasNext();) {
                    curr = it.next();

                    boolean segmentVertical = curr.getX() == prev.getX();
                    OrthogonalLineSegment currSegment
                            = new OrthogonalLineSegment(
                                    segmentVertical ? curr.getX() : curr.getY(),
                                    0,
                                    0,
                                    line, prev, curr);
                    currSegment.updateLength();
                    segments.get(segmentVertical ? 0 : 1).add(currSegment);
                    /*
                     * Sets the references to the previous and next line segments.
                     */
                    currSegment.setPrev(prevSegment);
                    if (prevSegment != null) {
                        prevSegment.setNext(currSegment);
                    }
                    prevSegment = currSegment;
                    if (singleLineSegments != null) {
                        singleLineSegments.add(currSegment);
                    }
                    prev = curr;
                }
            }
        }

        orderSegments(segments);

        return segments;
    }

    /**
     * Generates the OrthogonalSegment segments corresponding to the box and inside label segments
     * in both of the orthogonal directions. Returns an ArrayList containing two lists with vertical
     * and horizontal rectangle segments respectively. The lists are ordered such that for every
     * {@code k} the {@code 2k-th} and the {@code (2k+1)-th} elements of both lists are the opposite
     * sides of a single rectangle.
     *
     * @param container the container for whose children to prepare rectangle segments
     * @param singleLevel whether to generate segments from the container's next rectangles only
     * @return the OrthogonalSegment segments corresponding to the line segments.
     */
    static ArrayList<ArrayList<RectangleSegment>> generateRectangleSegments(AbstractContainer container, boolean singleLevel) {
        /*
         * Prepares the rectangle segments for the obstacle graph calculation. Each segment corresponds to
         * a side of some rectangle extended at both ends by the rectangle's spacing.
         */
        ArrayList<AbstractContainer> rectangles = singleLevel ? container.getNextRectangles(false) : container.getDescendantRectangles(false, false);
        ArrayList<ArrayList<RectangleSegment>> segments = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            segments.add(new ArrayList<RectangleSegment>());

            boolean segmentsVertical = i == 0;

            SegmentType firstSegmentType, secondSegmentType;
            if (segmentsVertical) {
                firstSegmentType = SegmentType.LEFT;
                secondSegmentType = SegmentType.RIGHT;
            } else {
                firstSegmentType = SegmentType.TOP;
                secondSegmentType = SegmentType.BOTTOM;
            }
            double epsilon = container.getDiagram().getEpsilon();
            for (AbstractContainer rectangle : rectangles) {
                double firstBound = segmentsVertical ? rectangle.top : rectangle.left;
                double secondBound = segmentsVertical ? rectangle.bottom : rectangle.right;
                /*
                 * The segments are widened by the rectangle's spacing to eliminate spacing overlap and
                 * shortened by the diagram's epsilon to allow point-sized overlaps.
                 */
                double spacing = rectangle.getSpacing();
                RectangleSegment leftSegment = RectangleSegment.createRectangleSegment(
                        segmentsVertical ? rectangle.left : rectangle.top,
                        firstBound - spacing + epsilon,
                        secondBound + spacing - epsilon,
                        firstSegmentType, rectangle);
                RectangleSegment rightSegment = RectangleSegment.createRectangleSegment(
                        segmentsVertical ? rectangle.right : rectangle.bottom,
                        firstBound - spacing + epsilon,
                        secondBound + spacing - epsilon,
                        secondSegmentType, rectangle);
                segments.get(i).add(leftSegment);
                segments.get(i).add(rightSegment);
            }
        }

        return segments;
    }

    /**
     * Generates the OrthogonalSegment segments corresponding to the outside label segments in one
     * of the orthogonal directions. Returns an ArrayList with the outside label segments. The list
     * is ordered such that for every {@code k} the {@code 2k-th} and the {@code (2k+1)-th} elements
     * of both lists are the opposite sides of a single outside label.
     *
     * @param container the container for whose children to prepare the segments
     * @param segmentsVertical whether to generate vertical or horizontal segments
     * @param sidesVertical whether to consider labels on the vertical or horizontal box sides
     * @param rectangleSides maps labels to the segments corresponding to their sides
     * @param lineSegmentMap maps each line to an {@link OrthogonalSegment} list which corresponds
     * to line segments
     * @return the OrthogonalSegment segments corresponding to the outside label segments.
     */
    static ArrayList<RectangleSegment> generateOutsideLabelSegments(
            Container container,
            boolean segmentsVertical,
            boolean sidesVertical,
            LinkedHashMap<AbstractContainer, Pair<RectangleSegment, RectangleSegment>> rectangleSides,
            HashMap<Line, ArrayList<OrthogonalLineSegment>> lineSegmentMap) {
        ArrayList<RectangleSegment> segments = new ArrayList<>();

        SegmentType firstSegmentType, secondSegmentType;
        if (segmentsVertical) {
            firstSegmentType = SegmentType.LEFT;
            secondSegmentType = SegmentType.RIGHT;
        } else {
            firstSegmentType = SegmentType.TOP;
            secondSegmentType = SegmentType.BOTTOM;
        }
        double epsilon = container.getDiagram().getEpsilon();

        ArrayList<OutsideLabel> labels = container.getDescendantOutsideLabels();
        for (OutsideLabel label : labels) {
            double spacing = label.spacing;
            if (label instanceof BoxOutsideLabel) {
                BoxOutsideLabel boxLabel = (BoxOutsideLabel) label;
                BoxSide side = boxLabel.side;

                if (sidesVertical ^ side.isHorizontal()) {
                    double firstBound = segmentsVertical ? boxLabel.top : boxLabel.left;
                    double secondBound = segmentsVertical ? boxLabel.bottom : boxLabel.right;

                    if (sidesVertical ^ segmentsVertical) {
                        if (side == BoxSide.LEFT || side == BoxSide.TOP) {
                            secondBound -= 5 * epsilon;
                        } else {
                            firstBound += 5 * epsilon;
                        }
                    } else {
                        firstBound += epsilon - spacing;
                        secondBound += spacing - epsilon;
                    }
                    BoxOutsideLabelSegment leftSegment = new BoxOutsideLabelSegment(
                            segmentsVertical ? boxLabel.left : boxLabel.top,
                            firstBound,
                            secondBound,
                            firstSegmentType,
                            boxLabel);
                    BoxOutsideLabelSegment rightSegment = new BoxOutsideLabelSegment(
                            segmentsVertical ? boxLabel.right : boxLabel.bottom,
                            firstBound,
                            secondBound,
                            secondSegmentType,
                            boxLabel);
                    segments.add(leftSegment);
                    segments.add(rightSegment);
                    rectangleSides.put(boxLabel, new Pair<RectangleSegment, RectangleSegment>(leftSegment, rightSegment));
                }
            } else {
                LineLabel lineLabel = (LineLabel) label;
                Line line = lineLabel.getOwner();
                if (line.getType() != LineType.ORTHOGONAL) {
                    continue;
                }
                ArrayList<Point2D.Double> points = line.lineGeometry.points;
                int segmentIndex = lineLabel.segmentIndex;
                boolean sideHorizontal = points.get(segmentIndex).y == points.get(segmentIndex + 1).y;
                boolean leftTop = lineLabel.isLeftTop();

                if (sidesVertical ^ sideHorizontal) {
                    double firstBound = segmentsVertical ? lineLabel.top : lineLabel.left;
                    double secondBound = segmentsVertical ? lineLabel.bottom : lineLabel.right;

                    if (sidesVertical ^ segmentsVertical) {
                        if (lineLabel.orientation != LineLabel.Orientation.CENTER) {
                            if (leftTop) {
                                secondBound -= epsilon;
                            } else {
                                firstBound += epsilon;
                            }
                        }
                    } else {
                        firstBound += epsilon - spacing;
                        secondBound += spacing - epsilon;
                    }
                    LineLabelSegment leftSegment = new LineLabelSegment(
                            segmentsVertical ? lineLabel.left : lineLabel.top,
                            firstBound,
                            secondBound,
                            firstSegmentType,
                            lineLabel,
                            lineSegmentMap.get(line).get(segmentIndex));
                    LineLabelSegment rightSegment = new LineLabelSegment(
                            segmentsVertical ? lineLabel.right : lineLabel.bottom,
                            firstBound,
                            secondBound,
                            secondSegmentType,
                            lineLabel,
                            lineSegmentMap.get(line).get(segmentIndex));
                    segments.add(leftSegment);
                    segments.add(rightSegment);
                    rectangleSides.put(lineLabel, new Pair<RectangleSegment, RectangleSegment>(leftSegment, rightSegment));
                }
            }
        }

        return segments;
    }

    /**
     * Generates maps from rectangles to their corresponding segments, as pairs of opposing
     * segments. Returns an ArrayList containing two maps with horizontal and vertical rectangle
     * segment pairs respectively.
     *
     * @param segments the segments corresponding to the sides of the diagram's rectangles, ordered
     * as they are returned by {@link #generateRectangleSegments}.
     * @return an ArrayList containing two maps with vertical and horizontal rectangle segment pairs
     * respectively.
     */
    static ArrayList<LinkedHashMap<AbstractContainer, Pair<RectangleSegment, RectangleSegment>>> generateRectangleSides(
            ArrayList<ArrayList<RectangleSegment>> segments) {
        ArrayList<LinkedHashMap<AbstractContainer, Pair<RectangleSegment, RectangleSegment>>> rectangleSides = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            ArrayList<RectangleSegment> s = segments.get(i);
            LinkedHashMap<AbstractContainer, Pair<RectangleSegment, RectangleSegment>> sides = new LinkedHashMap<>();
            for (int j = 0; j < s.size(); j += 2) {
                sides.put(s.get(j).getContainer(), new Pair<>(s.get(j), s.get(j + 1)));
            }
            rectangleSides.add(sides);
        }

        return rectangleSides;
    }

    /**
     * The comparator used when sorting overlapping lines to minimize line intersections.
     */
    static class LineComparator implements Comparator<OrthogonalLineSegment> {

        @Override
        public int compare(OrthogonalLineSegment a, OrthogonalLineSegment b) {
            /*
             * If the segments have already been sorted use the previous results.
             */
            if (a.getSortPriority() != b.getSortPriority()) {
                return a.getSortPriority() < b.getSortPriority() ? -1 : 1;
            }
            /*
             * The adjacent segments of the two lines on the left/top side and the right/bottom side
             * of the current segments, {@code null} if none.
             */
            OrthogonalLineSegment aLeftNext = (a.direction == OrthogonalLineSegment.SegmentDirection.RIGHT
                    || a.direction == OrthogonalLineSegment.SegmentDirection.DOWN) ? a.prev : a.next,
                    aRightNext = aLeftNext == a.prev ? a.next : a.prev,
                    bLeftNext = (b.direction == OrthogonalLineSegment.SegmentDirection.RIGHT
                    || b.direction == OrthogonalLineSegment.SegmentDirection.DOWN) ? b.prev : b.next,
                    bRightNext = bLeftNext == b.prev ? b.next : b.prev;
            /*
             * Finds the priority class of the current segment based on the existance/direction of
             * adjacent segments.
             */
            int aPriority = Normalizer.getLinePriorityClass(aLeftNext, a, aRightNext),
                    bPriority = Normalizer.getLinePriorityClass(bLeftNext, b, bRightNext);

            /*
             * Since for vertical segments the coordinate system is mirrored, not just rotated,
             * invert the results for vertical segments.
             */
            int dir = a.isHorizontal() ? 1 : -1;
            /*
             * If the segments have different priorities, sort by those. This leaves some
             * unnecessary intersections, but they are unavoidable with a simple priority system.
             */
            if (aPriority != bPriority) {
                return aPriority < bPriority ? -dir : dir;
            }

            /*
             * 0, 1 and 2 denote whether the segment (left or right) goes up, is null, or goes down
             * from the segment (a or b).
             */
            int aLeftDir = (aLeftNext == null ? 1
                    : ((aLeftNext.direction == OrthogonalLineSegment.SegmentDirection.LEFT
                    || aLeftNext.direction == OrthogonalLineSegment.SegmentDirection.DOWN) ^ aLeftNext == a.next ? 0 : 2)),
                    aRightDir = (aRightNext == null ? 1
                    : ((aRightNext.direction == OrthogonalLineSegment.SegmentDirection.LEFT
                    || aRightNext.direction == OrthogonalLineSegment.SegmentDirection.DOWN) ^ aRightNext == a.next ? 0 : 2));

            /*
             * Tries to sort segments by looking at their left or right next segments positioning.
             * If this comparator has gotten this far, the segments have the same priority type.
             */
            if (a.getStart() != b.getStart() && aLeftDir != 1) {
                if (aLeftDir == 0) {
                    return a.getStart() < b.getStart() ? dir : -dir;
                } else {
                    return a.getStart() < b.getStart() ? -dir : dir;
                }
            }
            if (a.getEnd() != b.getEnd() && aRightDir != 1) {
                if (aRightDir == 0) {
                    return a.getEnd() < b.getEnd() ? -dir : dir;
                } else {
                    return a.getEnd() < b.getEnd() ? dir : -dir;
                }
            }
            /*
             * At this point a and b have equal positioning and dimensions, as well as equal
             * previous and next segment directions. Tries to sort by previous or next segment order
             * so as to not form an intersection.
             */
            if (aLeftNext != null && aLeftNext.getSortPriority() != bLeftNext.getSortPriority()) {
                return (aLeftDir == 0 ? -dir : dir)
                        * Double.compare(aLeftNext.getSortPriority(), bLeftNext.getSortPriority());
            }
            if (aRightNext != null && aRightNext.getSortPriority() != bRightNext.getSortPriority()) {
                return (aRightDir == 0 ? dir : -dir)
                        * Double.compare(aRightNext.getSortPriority(), bRightNext.getSortPriority());
            }

            /*
             * The comparator wasn't able to differentiate the two segments.
             */
            return 0;
        }
    }

    /**
     * Get the priority class of the given line segment. The priority classes are as follows: <br/>
     * <br/> 0: |___| <br/> <br/>
     *
     * 1: |___ <br/>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;|<br/>
     * <br/>
     *
     * 2: |____ <br/> <br/>
     *
     * 3: ____| <br/> <br/>
     *
     * 4: _____ <br/> <br/>
     *
     * 5: ____ <br/>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;|<br/>
     * <br/>
     *
     * 6: &nbsp;____ <br/> &nbsp;&nbsp;&nbsp;&nbsp;|<br/> <br/>
     *
     * 7: &nbsp;___| <br/> &nbsp;&nbsp;&nbsp;&nbsp;|<br/> <br/>
     *
     * 8: &nbsp;___ <br/>
     * &nbsp;&nbsp;&nbsp;&nbsp;|&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;|<br/> <br/>
     *
     * @param leftNext the next segment along the line at the top/left of the examined segment.
     * @param segment the segment whose priority to find.
     * @param rightNext the next segment along the line at the bottom/right of the examined segment.
     * @return the priority class of the given line segment.
     */
    private static int getLinePriorityClass(OrthogonalLineSegment leftNext,
            OrthogonalLineSegment segment, OrthogonalLineSegment rightNext) {
        int leftDir = (leftNext == null ? 1
                : ((leftNext.direction == OrthogonalLineSegment.SegmentDirection.LEFT
                || leftNext.direction == OrthogonalLineSegment.SegmentDirection.DOWN) ^ leftNext == segment.next ? 0 : 2)),
                rightDir = (rightNext == null ? 1
                : ((rightNext.direction == OrthogonalLineSegment.SegmentDirection.LEFT
                || rightNext.direction == OrthogonalLineSegment.SegmentDirection.DOWN) ^ rightNext == segment.next ? 0 : 2));

        return 3 * leftDir + (rightDir == 0 ? leftDir / 2 : rightDir == 1 ? 2 - leftDir : (leftDir + 3) / 2);
    }

    /**
     * Assigns priorities to line segments to differentiate overlapping segments during
     * normalization.
     *
     * @param segments the segments of the lines
     */
    private static void orderSegments(ArrayList<ArrayList<OrthogonalLineSegment>> segments) {
        /*
         * Assigns the segments to buckets, each bucket contains all segments parallel to the same
         * axis that share the other coordinate.
         */
        LinkedList<ArrayList<OrthogonalLineSegment>> buckets = new LinkedList<>();
        /*
         * Obtaining bucket of the segments for each coordinate.
         */
        for (int i = 0; i < 2; i++) {
            Collections.sort(segments.get(i));

            ArrayList<OrthogonalLineSegment> tSegments = segments.get(i);
            for (int j = 0; j < tSegments.size();) {
                double coord = tSegments.get(j).getPos();

                ArrayList<OrthogonalLineSegment> bucket = new ArrayList<>();
                for (; j < tSegments.size() && tSegments.get(j).getPos() == coord; j++) {
                    bucket.add(tSegments.get(j));
                }

                buckets.add(bucket);
            }
        }

        /*
         * Sorts the segments by their direction (from start point to end point). Since the sorting
         * used further on is stable, this ensures that clumps of otherwise equal lines are
         * separated by their direction and directions are not mixed unnecessarily.
         */
        for (ArrayList<OrthogonalLineSegment> bucket : buckets) {
            Collections.sort(bucket, new Comparator<OrthogonalLineSegment>() {
                @Override
                public int compare(OrthogonalLineSegment a, OrthogonalLineSegment b) {
                    if (a.direction != b.direction) {
                        if (a.direction == OrthogonalLineSegment.SegmentDirection.LEFT
                                || a.direction == OrthogonalLineSegment.SegmentDirection.UP) {
                            return -1;
                        } else {
                            return 1;
                        }
                    }
                    return 0;
                }
            });
        }

        Comparator<OrthogonalLineSegment> lineComparator = new LineComparator();
        /*
         * In each iteration over all the buckets orders segments in each bucket, assigns different
         * priorites to segments that can be differentiated. If a bucket is fully sorted afterwards
         * removes it from the list of buckets to be ordered. If, after an iteration, no bucket has
         * changed, the segments are not differentiable and a single clump of undifferentiated
         * segments in the first bucket are assigned an arbitrary ordering.
         */
        while (!buckets.isEmpty()) {
            ListIterator<ArrayList<OrthogonalLineSegment>> it = buckets.listIterator();
            boolean hasChanged = false;

            /*
             * Iterates over all buckets.
             */
            while (it.hasNext()) {
                ArrayList<OrthogonalLineSegment> bucket = it.next();
                boolean isOrdered = true;

                Collections.sort(bucket, lineComparator);

                /*
                 * Assigns increasing priorities to the buckets' segments. Uses lineComparator as
                 * equal segments get equal priorities.
                 */
                int priorityInc = 0;
                OrthogonalLineSegment current;
                for (int i = 0; i < bucket.size() - 1; i++) {
                    int prevPriorityInc = priorityInc;
                    current = bucket.get(i);
                    if (lineComparator.compare(current, bucket.get(i + 1)) < 0) {
                        priorityInc++;
                    } else {
                        isOrdered = false;
                    }
                    if (current.getSortPriority() != prevPriorityInc) {
                        hasChanged = true;
                    }
                    current.setPriority(prevPriorityInc);
                }
                if (bucket.get(bucket.size() - 1).getSortPriority() != priorityInc) {
                    hasChanged = true;
                }
                bucket.get(bucket.size() - 1).setPriority(priorityInc);

                if (isOrdered) {
                    hasChanged = true;
                    it.remove();
                }
            }

            /*
             * If no bucket has changed arbitrarily separates a single clump. The priorities are
             * assigned according to the segment position in the bucket. As only stable sorts are
             * used, this cleanly separates directions.
             */
            if (!hasChanged) {
                ArrayList<OrthogonalLineSegment> bucket = buckets.getFirst();
                OrthogonalLineSegment current;
                for (int i = 1; i < bucket.size(); i++) {
                    if (bucket.get(i - 1).getSortPriority() == bucket.get(i).getSortPriority()) {
                        int separatedPriority = bucket.get(i).getSortPriority();
                        int priorityInc = 0, currPriority;
                        for (; i < bucket.size(); i++) {
                            current = bucket.get(i);
                            currPriority = current.getSortPriority();
                            if (currPriority == separatedPriority) {
                                priorityInc++;
                            }
                            current.setPriority(currPriority + priorityInc);
                        }
                    }
                }
            }
        }
    }

    /**
     * Updates the state of a set of OrthogonalSegments. Sets the proper values of each segment's
     * top and bottom. Should be used for segments in a single direction after the normalization in
     * the opposite direction has occurred, since the attributes of the given segments have not been
     * changed to proper values.
     *
     * @param segments the set of segments whose state to update
     */
    static void updateSegments(ArrayList<? extends OrthogonalSegment> segments) {
        for (OrthogonalSegment segment : segments) {
            segment.updateLength();
        }
    }

    /**
     * Updates the state of a single rectangle's segments in one direction. Sets the proper values
     * of each segment's top and bottom, multiplying spacing by {@code spacingRatio}, which is
     * needed for normalization to grow a rectangle without it overlapping other rectangles during
     * the growing process.
     *
     * @param segments the pair of opposite segments of the rectangle to update
     * @param spacingRatio the ratio by which to multiply the rectangle's current spacing when
     * updating the segments
     */
    private static void updateSingleRectangle(Pair<RectangleSegment, RectangleSegment> segments, double spacingRatio) {
        segments.getFirst().updateLength(spacingRatio);
        segments.getSecond().updateLength(spacingRatio);
    }

    /**
     * Normalizes the diagram elements in one of the given directions (horizontal or vertical). The
     * segments (horizontal segments in vertical direction and vice versa) of the elements will be
     * pushed to the given direction so the elements are normalized in it.
     *
     * @param diagram the diagram to normalize
     * @param horizontalNormalize the direction to normalize to
     * @param lineSegments {@link OrthogonalSegment}s corresponding to the lines of the diagram,
     * with a separate list for segments running in each direction
     * @param rectangleSegments {@link OrthogonalSegment}s corresponding to the sides of the
     * rectangles of the diagram, with a separate list for segments running in each direction
     * @param rectangleSides maps rectangles to the segments corresponding to their sides, with a
     * separate map for segments running in each direction
     * @param normalizeOutsideLabels whether to normalize outside labels
     * @param changedRectangles rectangles whose center should change during the normalization
     * @param newCenters the points that should be the new centers of the given rectangles
     */
    private static void adjustStep(Diagram diagram,
            boolean horizontalNormalize,
            ArrayList<ArrayList<OrthogonalLineSegment>> lineSegments,
            ArrayList<ArrayList<RectangleSegment>> rectangleSegments,
            ArrayList<LinkedHashMap<AbstractContainer, Pair<RectangleSegment, RectangleSegment>>> rectangleSides,
            boolean normalizeOutsideLabels,
            ArrayList<AbstractContainer> changedRectangles,
            ArrayList<Point2D.Double> newCenters) {
        ArrayList<Pair<OrthogonalSegment, OrthogonalSegment>> obstacleGraph;
        ArrayList<OrthogonalSegment> segments = new ArrayList<>();
        segments.addAll(rectangleSegments.get(horizontalNormalize ? 0 : 1));

        /*
         * Updates the state of the line segments' formed OrthogonalSegments and adds the segments
         * corresponding to the current direction to the lineSegments list.
         */
        updateSegments(lineSegments.get(horizontalNormalize ? 0 : 1));
        segments.addAll(lineSegments.get(horizontalNormalize ? 0 : 1));

        /*
         * Calculates the obstacle graph
         */
        ArrayList<ConstraintSegment> constraintSegments = new ArrayList<>();
        ArrayList<Pair<OrthogonalSegment, OrthogonalSegment>> constraintObstacles = new ArrayList<>();
        addConstraints(diagram, constraintSegments, constraintObstacles, rectangleSides.get(horizontalNormalize ? 0 : 1), null, null, horizontalNormalize, true, normalizeOutsideLabels, 0);

        obstacleGraph = ObstacleGraph.findObstacleGraph(segments);

//        int size = segments.size();
//        for (int i = 0; i < size; ) {
//            OrthogonalSegment left = segments.get(i);
//            for (int j = i + 1; j < size; j++) {
//                OrthogonalSegment right = segments.get(j);
//                if (left.pos == right.pos && right instanceof BoxSegment && ((BoxSegment) right).segmentType == SegmentType.RIGHT && left instanceof BoxOutsideLabelSegment && ((BoxOutsideLabelSegment) left).segmentType == SegmentType.LEFT) {
//                    for (int k = i; k <= j; k++) {
//                        System.out.println(segments.get(k));
//                        System.out.println(segments.get(k).getSortPriority());
//                    }
//                    for (int k = i; k < j; k++) {
//                        for (int l = k + 1; l <= j; l++) {
//                            segments.get(k).compareTo(segments.get(l));
//                        }
//                        System.out.println();
//                    }
//                    System.out.println();
//                }
//            }
//            int j = i + 1;
//            while(j < segments.size() && left.pos == segments.get(j).pos) {
//                j++;
//            }
//            if(j-i > 1) {
//                    for(int k = i; k < j; k++) {
//                        System.out.println(((RectangleSegment)segments.get(k)).getContainer() + " " + ((RectangleSegment)segments.get(k)).segmentType);
//                    }
//                    System.out.println();
//            }
//            i = j;
//        }
//        System.out.println("------------------------------------------------------------");
        segments.addAll(constraintSegments);
        obstacleGraph.addAll(constraintObstacles);

        normalizeDiagram(segments, obstacleGraph, rectangleSides.get(horizontalNormalize ? 0 : 1), diagram,
                horizontalNormalize, normalizeOutsideLabels, changedRectangles, newCenters);

        reconnectEndPoints(diagram, horizontalNormalize);
    }

    /**
     * Adds the constraints of the given container and its descendants to the given set of segments
     * and obstacle graph.
     *
     * @param container the container whose constraints to add
     * @param segments a set of segments for normalization, any constraint segments created are
     * added to this set
     * @param obstacleGraph a set of existing constraints for normalization, any found constraints
     * are added to this
     * @param rectangleSides a map from rectangles to their corresponding two segments
     * @param firstBound the left bound segment for the grid constraints
     * @param secondBound the right bound segment for the grid constraints
     * @param segmentsVertical whether the function deals with vertical or horizontal segments
     * @param recursive whether to add constraints recursively for the children of the container
     * @param includeOutsideLabels whether to apply the constraints to the outside labels
     * @param timer the time of DFS entering this container
     */
    private static void addConstraints(AbstractContainer container,
            ArrayList<ConstraintSegment> segments,
            ArrayList<Pair<OrthogonalSegment, OrthogonalSegment>> obstacleGraph,
            LinkedHashMap<AbstractContainer, Pair<RectangleSegment, RectangleSegment>> rectangleSides,
            OrthogonalSegment firstBound, OrthogonalSegment secondBound,
            boolean segmentsVertical, boolean recursive,
            boolean includeOutsideLabels, int timer) {
        if (container.getLayoutConstraints().getType() == ConstraintType.GRID) {
            GridLayoutConstraints constraints = (GridLayoutConstraints) container.getLayoutConstraints();
            ArrayList<ConstraintLine> constraintLines = segmentsVertical ? constraints.verticalConstraints : constraints.horizontalConstraints;
            if (constraintLines.size() > 1) {
                /*
                 * Construct the constraint segments.
                 */
                ArrayList<ConstraintSegment> constraintSegments = new ArrayList<>();
                ConstraintSegment prev = new ConstraintSegment(constraintLines.get(0), timer);
                for (int i = 1; i < constraintLines.size(); i++) {
                    ConstraintSegment newSegment = new ConstraintSegment(constraintLines.get(i), timer);
                    constraintSegments.add(newSegment);
                    newSegment.prev = prev;
                    prev = newSegment;
                }
                if (firstBound != null) {
                    obstacleGraph.add(new Pair<OrthogonalSegment, OrthogonalSegment>(
                            firstBound, constraintSegments.get(0)));
                }
                if (secondBound != null) {
                    obstacleGraph.add(new Pair<OrthogonalSegment, OrthogonalSegment>(
                            constraintSegments.get(constraintSegments.size() - 1), secondBound));
                }
                /*
                 * The constraints for maintaining minimum grid cell size.
                 */
                for (int i = 0; i + 1 < constraintSegments.size(); i++) {
                    obstacleGraph.add(new Pair<OrthogonalSegment, OrthogonalSegment>(constraintSegments.get(i), constraintSegments.get(i + 1)));
                }
                /*
                 * For each child rectangle, add constraints keeping it between the sides of its cell.
                 */
                for (AbstractContainer rectangle : container.getRectangles()) {
                    Integer lane = segmentsVertical ? constraints.getColumn(rectangle) : constraints.getRow(rectangle);
                    if (lane != null) {
                        setRectangleLane(obstacleGraph, constraintSegments, lane, rectangleSides.get(rectangle), firstBound, secondBound);
                        if (includeOutsideLabels && rectangle instanceof Box) {
                            for (BoxOutsideLabel boxLabel : ((Box) rectangle).getOutsideLabels()) {
                                Pair<RectangleSegment, RectangleSegment> sides = rectangleSides.get(boxLabel);
                                if (sides != null) {
                                    setRectangleLane(obstacleGraph, constraintSegments, lane, sides, firstBound, secondBound);
                                }
                            }
                        }
                    }

                    Pair<RectangleSegment, RectangleSegment> sides = rectangleSides.get(rectangle);
                    OrthogonalSegment newFirstBound = sides.getFirst();
                    OrthogonalSegment newSecondBound = sides.getSecond();

                    if (recursive) {
                        addConstraints(rectangle, segments, obstacleGraph, rectangleSides, newFirstBound, newSecondBound, segmentsVertical, recursive, includeOutsideLabels, ++timer);
                    }
                }
                /*
                 * For child containers, their descendant rectangles must lie in the container's cell.
                 */
                for (Container c : container.getPureContainers()) {
                    Integer lane = segmentsVertical ? constraints.getColumn(c) : constraints.getRow(c);
                    OrthogonalSegment newFirstBound = firstBound;
                    OrthogonalSegment newSecondBound = secondBound;
                    if (lane != null) {
                        for (AbstractContainer rectangle : c.getNextRectangles(false)) {
                            setRectangleLane(obstacleGraph, constraintSegments, lane, rectangleSides.get(rectangle), firstBound, secondBound);
                            if (includeOutsideLabels && rectangle instanceof Box) {
                                for (BoxOutsideLabel boxLabel : ((Box) rectangle).getOutsideLabels()) {
                                    Pair<RectangleSegment, RectangleSegment> sides = rectangleSides.get(boxLabel);
                                    if (sides != null) {
                                        setRectangleLane(obstacleGraph, constraintSegments, lane, sides, firstBound, secondBound);
                                    }
                                }
                            }
                        }

                        if (lane > 1) {
                            newFirstBound = constraintSegments.get(lane - 2);
                        }
                        if (lane <= constraintSegments.size()) {
                            newSecondBound = constraintSegments.get(lane - 1);
                        }
                    }

                    addConstraints(c, segments, obstacleGraph, rectangleSides, newFirstBound, newSecondBound, segmentsVertical, recursive, includeOutsideLabels, ++timer);
                }
                segments.addAll(constraintSegments);
                return;
            }
        }

        /*
         * Recursively add the constraints of the container's descendants.
         */
        for (AbstractContainer c : container.getAbstractContainers(false)) {
            if (recursive || !(c instanceof Rectangular)) {
                OrthogonalSegment newFirstBound = firstBound;
                OrthogonalSegment newSecondBound = secondBound;
                if (c instanceof Rectangular) {
                    Pair<RectangleSegment, RectangleSegment> sides = rectangleSides.get(c);
                    newFirstBound = sides.getFirst();
                    newSecondBound = sides.getSecond();
                }
                addConstraints(c, segments, obstacleGraph, rectangleSides, newFirstBound, newSecondBound, segmentsVertical, recursive, includeOutsideLabels, ++timer);
            }
        }
    }

    /**
     * Sets the lane of the given rectangle (as defined by the given pair of side segments) in its
     * segments and places the appropriate constraints in the obstacle graph.
     *
     * @param obstacleGraph the obstacle graph in which to place the new constraints
     * @param constraintSegments the list of constraint segments corresponding to the grid
     * @param lane the grid lane in which to place the rectangle
     * @param rectangleSides the two side segments of the rectangle used in the current
     * normalization
     * @param firstBound the left bound segment for the grid constraints
     * @param secondBound the right bound segment for the grid constraints
     */
    private static void setRectangleLane(ArrayList<Pair<OrthogonalSegment, OrthogonalSegment>> obstacleGraph,
            ArrayList<ConstraintSegment> constraintSegments,
            Integer lane,
            Pair<RectangleSegment, RectangleSegment> rectangleSides,
            OrthogonalSegment firstBound, OrthogonalSegment secondBound) {
        rectangleSides.getFirst().lane = lane;
        rectangleSides.getSecond().lane = lane;
        if (lane > 1) {
            obstacleGraph.add(new Pair<OrthogonalSegment, OrthogonalSegment>(
                    constraintSegments.get(lane - 2), rectangleSides.getFirst()));
        } else if (firstBound != null) {
            obstacleGraph.add(new Pair<OrthogonalSegment, OrthogonalSegment>(
                    firstBound, rectangleSides.getFirst()));
        }
        if (lane <= constraintSegments.size()) {
            obstacleGraph.add(new Pair<OrthogonalSegment, OrthogonalSegment>(
                    rectangleSides.getSecond(), constraintSegments.get(lane - 1)));
        } else if (secondBound != null) {
            obstacleGraph.add(new Pair<OrthogonalSegment, OrthogonalSegment>(
                    rectangleSides.getSecond(), secondBound));
        }
    }

    /**
     * Reconnects line endpoints to boxes after normalization distorts them.
     *
     * @param diagram the diagram in which to reconnect endpoints
     * @param segmentsHorizontal which segments to reconnect (horizontal or vertical)
     */
    private static void reconnectEndPoints(Diagram diagram, boolean segmentsHorizontal) {
        ArrayList<Line> lines = diagram.getDescendantLines();

        for (Line line : lines) {        	
            if (line.getType() == LineType.ORTHOGONAL) {
                if (line.getStart() instanceof Box) {
                    reconnectEndPoint(line, (Box) line.getStart(), segmentsHorizontal, true);
                } else {
                    throw new UnsupportedOperationException("Lines connected to lines are not supported yet.");
                }
                if (line.getEnd() instanceof Box) {
                    reconnectEndPoint(line, (Box) line.getEnd(), segmentsHorizontal, false);
                } else {
                    throw new UnsupportedOperationException("Lines connected to lines are not supported yet.");
                }
            } else if (line.getType() == LineType.STRAIGHT) {
                LineOptimizer.correctStraightLine(line);
            } else if (line.getType() == LineType.POLYLINE) {
                LineOptimizer.reconnectPolyline(line);
            } else {
                throw new UnsupportedOperationException("The " + line.getType() + " line type is not supported yet.");
            }
        }
    }

    /**
     * Reconnects endpoints for the given line and connected box
     *
     * @param line the line which points to reconnect
     * @param box the box the given line is connected to
     * @param segmentsHorizontal which segments to reconnect (horizontal or vertical)
     * @param start whether the given box is the start box of the given line
     */
    private static void reconnectEndPoint(Line line, Box box, boolean segmentsHorizontal, boolean start) {
        /*
         * The endpoint connected to the box and the point of the line preceding it.
         */
        Point2D.Double endPoint = start ? line.getStartPoint() : line.getEndPoint(),
                prevPoint = line.lineGeometry.points.get(start ? 1 : line.lineGeometry.points.size() - 2);
        LineGeometry.OrthogonalLine lineGeometry = (LineGeometry.OrthogonalLine) line.lineGeometry;

        /*
         * Reconnects the given point to the given box.
         */
        if (segmentsHorizontal && prevPoint.y == endPoint.y) {
            endPoint.x = BoxSide.getCoordinate(box, start ? lineGeometry.startSide : lineGeometry.endSide);
        } else if (!segmentsHorizontal && prevPoint.x == endPoint.x) {
            endPoint.y = BoxSide.getCoordinate(box, start ? lineGeometry.startSide : lineGeometry.endSide);
        }
    }
//    static boolean[] marked;
//    static boolean[] visited;
//    static int[] parent;
//    static ArrayList<ArrayList<Integer>> graph;
//
//    static int debugFindCycle(int u, int p) {
//        parent[u] = p;
//        if (marked[u]) {
//            return u;
//        }
//        marked[u] = true;
//        for (int v : graph.get(u)) {
//            if (!visited[v]) {
//                int c = debugFindCycle(v, u);
//                if (c != -1) {
//                    marked[u] = false;
//                    visited[u] = true;
//                    return c;
//                }
//            }
//        }
//        marked[u] = false;
//        visited[u] = true;
//        return -1;
//    }

    /**
     * Changes the layout of the diagram segments according to the compact layout in one of the
     * given directions. Does this by calculating the new positions of the segments using the
     * quadratic optimization algorithm.
     *
     * @param segments the segments of the diagram elements
     * @param obstacleGraph the segments' obstacle graph
     * @param rectangleSides a map of rectangles to segment pairs such that for each pair, its
     * members are the sides of the rectangle, used to create node minimum size constraints and
     * rectangle drift terms
     * @param diagram the diagram whose elements will be normalized
     * @param horizontalNormalize the direction to normalize in
     * @param normalizeOutsideLabels whether to normalize outside labels
     * @param changedRectangles rectangles whose center should change during the normalization
     * @param newCenters the points that should be the new centers of the given rectangles
     */
    private static void normalizeDiagram(ArrayList<OrthogonalSegment> segments,
            ArrayList<Pair<OrthogonalSegment, OrthogonalSegment>> obstacleGraph,
            LinkedHashMap<AbstractContainer, Pair<RectangleSegment, RectangleSegment>> rectangleSides,
            Diagram diagram, boolean horizontalNormalize, boolean normalizeOutsideLabels,
            ArrayList<AbstractContainer> changedRectangles, ArrayList<Point2D.Double> newCenters) {
        /*
         * The segments are numbered, as quadratic optimization methods work with variable ID's.
         */
        LinkedHashMap<OrthogonalSegment, Integer> segmentIndices = new LinkedHashMap<>();
        HashSet<AbstractContainer> changedRectSet = new HashSet<>(changedRectangles);

        ExtendedQuadraticOptimizer optimizer = new ExtendedQuadraticOptimizer(segments.size());
        double magicEpsilon = diagram.getEpsilon() / EPSILON_RATIO * OPTIMIZER_EPSILON_RATIO;
        optimizer.setEpsilon(magicEpsilon);
        /*
         * Sets the starting values of the variables to their current value.
         */
        for (int i = 0; i < segments.size(); i++) {
            OrthogonalSegment segment = segments.get(i);
            segmentIndices.put(segment, i);
            optimizer.setVariable(i, segment.getPos());
            if (!(segment instanceof BoxOutsideLabelSegment)
                    && (!(segment instanceof LineLabelSegment) || (horizontalNormalize == ((LineLabelSegment) segment).lineSegment.isHorizontal()))) {
                optimizer.addQuadraticConstantDifference(i, segment.getPos(),
                        (normalizeOutsideLabels && segment instanceof OrthogonalLineSegment ? 1000 : 1) * GENERAL_DRIFT_MINIMIZATION_WEIGHT);
            }
        }

//        graph = new ArrayList<>();
//        for (int i = 0; i < segments.size(); i++) {
//            graph.add(new ArrayList<Integer>());
//        }

        /*
         * Adds the edges of the obstacle graph as constraints.
         */
        for (Pair<OrthogonalSegment, OrthogonalSegment> obstacle : obstacleGraph) {
            OrthogonalSegment leftSegment = obstacle.getFirst();
            OrthogonalSegment rightSegment = obstacle.getSecond();
            if (leftSegment == null || rightSegment == null) {
                int x = 0;
            }
            optimizer.addInequality(segmentIndices.get(leftSegment), segmentIndices.get(rightSegment),
                    leftSegment.findMinimumDistance(rightSegment));
//            graph.get(segmentIndices.get(leftSegment)).add(segmentIndices.get(rightSegment));
        }

        /*
         * For each rectangle, adds the minimum size constraint and the size minimization term.
         */
        for (Pair<RectangleSegment, RectangleSegment> sides : rectangleSides.values()) {
            RectangleSegment leftSegment = sides.getFirst();
            RectangleSegment rightSegment = sides.getSecond();
            AbstractContainer rectangle = leftSegment.getContainer();
            optimizer.addInequality(segmentIndices.get(leftSegment), segmentIndices.get(rightSegment),
                    horizontalNormalize ? rectangle.getCurrentMinWidth() : rectangle.getCurrentMinHeight());
//            graph.get(segmentIndices.get(leftSegment)).add(segmentIndices.get(rightSegment));
            optimizer.addQuadraticDifference(segmentIndices.get(leftSegment), segmentIndices.get(rightSegment), SIZE_MINIMIZATION_WEIGHT);
        }

//        marked = new boolean[segments.size()];
//        visited = new boolean[segments.size()];
//        parent = new int[segments.size()];
//        for (int i = 0; i < segments.size(); i++) {
//            if (!visited[i]) {
//                int v = dfs(i, -1);
//                if (v != -1) {
//                    int u = v;
//                    do {
//                        RectangleSegment s = (RectangleSegment) segments.get(u);
//                        System.out.println(s.getContainer() + " " + s.segmentType + " " + s.pos);
//                        u = parent[u];
//                    } while (u != v);
//                }
//            }
//        }
        ArrayList<AbstractContainer> rectList = new ArrayList<>();
        /*
         * Sets the rectangle drift terms for top level rectangles in the diagram.
         */
        for (int i = 0; i < changedRectangles.size(); i++) {
            AbstractContainer rectangle = changedRectangles.get(i);
            if (rectangle.getPrevRect() == diagram) {
                RectangleSegment leftSegment = rectangleSides.get(rectangle).getFirst();
                RectangleSegment rightSegment = rectangleSides.get(rectangle).getSecond();
                optimizer.addMeanDifference(segmentIndices.get(rightSegment), segmentIndices.get(leftSegment),
                        horizontalNormalize ? newCenters.get(i).getX() : newCenters.get(i).getY(), CURRENT_RECTANGLE_DRIFT_MINIMIZATION_WEIGHT);
                rectList.addAll(rectangle.getDescendantRectangles(false, false));
            }
        }
        for (AbstractContainer rectangle : diagram.getNextRectangles(false)) {
            /*
             * If the current rectangle should remain stationary.
             */
            if (!changedRectSet.contains(rectangle)) {
                RectangleSegment leftSegment = rectangleSides.get(rectangle).getFirst();
                RectangleSegment rightSegment = rectangleSides.get(rectangle).getSecond();
                optimizer.addMeanDifference(segmentIndices.get(rightSegment),
                        segmentIndices.get(leftSegment), (rightSegment.getPos() + leftSegment.getPos()) / 2,
                        RECTANGLE_DRIFT_MINIMIZATION_WEIGHT);
                rectList.addAll(rectangle.getDescendantRectangles(false, false));
            }
        }

        int index = 0;
        HashMap<AbstractContainer, Integer> changedRectIndices = new HashMap<>();
        for (AbstractContainer rectangle : changedRectangles) {
            changedRectIndices.put(rectangle, index++);
        }

        /*
         * The distance the moved rectangle centers should move in the direction in which we are
         * currently minimizing.
         */
        ArrayList<Double> changedRectangleMoveDistances = new ArrayList<>();
        for (int i = 0; i < changedRectangles.size(); i++) {
            changedRectangleMoveDistances.add(horizontalNormalize
                    ? newCenters.get(i).getX() - changedRectangles.get(i).getCenter().getX()
                    : newCenters.get(i).getY() - changedRectangles.get(i).getCenter().getY());
        }

        /*
         * For lower level rectangles, minimize drift relative to parent rectangle.
         */
        for (AbstractContainer rectangle : rectList) {
            if (rectangleSides.containsKey(rectangle)) {
                AbstractContainer prevRect = rectangle.getPrevRect(false);

                RectangleSegment leftSegment = rectangleSides.get(rectangle).getFirst();
                RectangleSegment rightSegment = rectangleSides.get(rectangle).getSecond();
                RectangleSegment parentLeftSegment = rectangleSides.get(prevRect).getFirst();
                RectangleSegment parentRightSegment = rectangleSides.get(prevRect).getSecond();

                /*
                 * The distance between the rectangle's center and its parent's center to strive for.
                 */
                double optimalDistance;

                /*
                 * If the current rectangle is being moved, consider the new center to be its center for
                 * the purposes of finding the optimal distance.
                 */
                boolean rectChanged = changedRectSet.contains(rectangle);
                if (rectChanged) {
                    index = changedRectIndices.get(rectangle);
                    optimalDistance = (horizontalNormalize ? newCenters.get(index).getX() : newCenters.get(index).getY());
                } else {
                    optimalDistance = (leftSegment.getPos() + rightSegment.getPos()) / 2;
                }

                optimalDistance -= (parentLeftSegment.getPos() + parentRightSegment.getPos()) / 2;

                /*
                 * If the rectangle's parent is being moved, add the amount the parent should move, in
                 * effect considering the parents new center for the purposes of finding the optimal
                 * distance.
                 */
                if (changedRectSet.contains(prevRect)) {
                    index = changedRectIndices.get(prevRect);
                    optimalDistance -= changedRectangleMoveDistances.get(index);
                }

                optimizer.addDoubleMeanConstantDifference(segmentIndices.get(leftSegment), segmentIndices.get(rightSegment),
                        segmentIndices.get(parentLeftSegment), segmentIndices.get(parentRightSegment), optimalDistance,
                        (rectChanged ? CURRENT_RECTANGLE_DRIFT_MINIMIZATION_WEIGHT : RECTANGLE_DRIFT_MINIMIZATION_WEIGHT));
            }
        }

        if (normalizeOutsideLabels) {
            for (OutsideLabel label : diagram.getDescendantOutsideLabels()) {
                if (label instanceof BoxOutsideLabel) {
                    BoxOutsideLabel boxLabel = (BoxOutsideLabel) label;
                    Pair<RectangleSegment, RectangleSegment> labelSides = rectangleSides.get(boxLabel);
                    if (labelSides != null) {
                        Pair<RectangleSegment, RectangleSegment> boxSides = rectangleSides.get(boxLabel.getOwner());
                        if (boxLabel.side.isHorizontal() ^ horizontalNormalize) {
                            RectangleSegment boxSegment;
                            if (boxLabel.side == BoxSide.LEFT || boxLabel.side == BoxSide.TOP) {
                                boxSegment = boxSides.getFirst();
                            } else {
                                boxSegment = boxSides.getSecond();
                            }
                            optimizer.addQuadraticDifference(segmentIndices.get(labelSides.getFirst()), segmentIndices.get(boxSegment), OUTSIDE_LABEL_STICK_WEIGHT);
                            optimizer.addQuadraticDifference(segmentIndices.get(labelSides.getSecond()), segmentIndices.get(boxSegment), OUTSIDE_LABEL_STICK_WEIGHT);
                        } else {
                            optimizer.addDoubleMeanConstantDifference(
                                    segmentIndices.get(labelSides.getFirst()),
                                    segmentIndices.get(labelSides.getSecond()),
                                    segmentIndices.get(boxSides.getFirst()),
                                    segmentIndices.get(boxSides.getSecond()),
                                    (labelSides.getFirst().getPos() + labelSides.getSecond().getPos()) / 2 - (boxSides.getFirst().getPos() + boxSides.getSecond().getPos()) / 2,
                                    RECTANGLE_DRIFT_MINIMIZATION_WEIGHT);
                        }
                    }
                } else {
                    assert label instanceof LineLabel;
                    LineLabel lineLabel = (LineLabel) label;
                    Pair<RectangleSegment, RectangleSegment> labelSides = rectangleSides.get(lineLabel);
                    if (labelSides != null) {
                        OrthogonalLineSegment lineSegment = ((LineLabelSegment) labelSides.getFirst()).lineSegment;

                        if (lineSegment.isHorizontal() ^ horizontalNormalize) {
                            if (lineLabel.orientation != Orientation.CENTER) {
                                optimizer.addQuadraticDifference(segmentIndices.get(labelSides.getFirst()), segmentIndices.get(lineSegment), OUTSIDE_LABEL_STICK_WEIGHT);
                                optimizer.addQuadraticDifference(segmentIndices.get(labelSides.getSecond()), segmentIndices.get(lineSegment), OUTSIDE_LABEL_STICK_WEIGHT);
                            }
                        } else if (lineLabel.position == 0 || lineLabel.position == 1) {
                            Line line = lineLabel.getOwner();
                            Box box;
                            BoxSide side;
                            if (lineLabel.position == 0) {
                                box = (Box) line.getStart();
                                side = ((OrthogonalLine) line.lineGeometry).startSide;
                            } else {
                                box = (Box) line.getEnd();
                                side = ((OrthogonalLine) line.lineGeometry).endSide;
                            }
                            Pair<RectangleSegment, RectangleSegment> boxSides = rectangleSides.get(box);
                            RectangleSegment boxSegment;
                            RectangleSegment labelSegment;
                            if (side == BoxSide.LEFT || side == BoxSide.TOP) {
                                labelSegment = labelSides.getSecond();
                                boxSegment = boxSides.getFirst();
                            } else {
                                labelSegment = labelSides.getFirst();
                                boxSegment = labelSides.getSecond();
                            }
                            assert boxSegment.segmentType.name().equals(side.name());
                            optimizer.addQuadraticDifference(segmentIndices.get(labelSegment), segmentIndices.get(boxSegment), OUTSIDE_LABEL_STICK_WEIGHT);
                        }
                    }

                }
            }
        }

        ArrayList<Box> descendantBoxes = diagram.getDescendantBoxes();
        ArrayList<Box> boxesWithOutsideLabels = new ArrayList<>();
        ArrayList<Point2D.Double> oldCenters1 = new ArrayList<>();
        if (!normalizeOutsideLabels) {
            for (Box box : descendantBoxes) {
                if (!box.outsideLabels.isEmpty() && !changedRectangles.contains(box)) {
                    boxesWithOutsideLabels.add(box);
                    oldCenters1.add(box.getCenter());
                }
            }
        }
        ArrayList<Box> boxesWithStraightLines = new ArrayList<>();
        ArrayList<Point2D.Double> oldCenters2 = new ArrayList<>();
        for (Box box : descendantBoxes) {
            if (!changedRectangles.contains(box)) {
                for (Line line : box.incidentLines) {
                    if (line.hasGeometry() && (line.getType() != LineType.ORTHOGONAL)) { // first check by SK
                        boxesWithStraightLines.add(box);
                        oldCenters2.add(box.getCenter());
                        break;
                    }
                }
            }
        }

        double newPositions[] = optimizer.performOptimization();
        double epsilon = diagram.getEpsilon();
        for (int i = 0; i < segments.size(); i++) {
            if (Math.abs(newPositions[i] - segments.get(i).getPos()) > epsilon) {
                segments.get(i).move(newPositions[i]);
            }
        }

        for (int i = 0; i < boxesWithOutsideLabels.size(); i++) {
            Box box = boxesWithOutsideLabels.get(i);
            Point2D.Double oldCenter = oldCenters1.get(i);
            Point2D.Double newCenter = box.getCenter();
            double moveX = newCenter.x - oldCenter.x;
            double moveY = newCenter.y - oldCenter.y;
            if (Math.abs(moveX) > epsilon || Math.abs(moveY) > epsilon) {
                Point2D.Double moveVector = new Point2D.Double(moveX, moveY);
                for (BoxOutsideLabel label : box.outsideLabels) {
                    label.transpose(moveVector);
                }
            }
        }
        for (int i = 0; i < boxesWithStraightLines.size(); i++) {
            Box box = boxesWithStraightLines.get(i);
            Point2D.Double oldCenter = oldCenters2.get(i);
            Point2D.Double newCenter = box.getCenter();
            double moveX = newCenter.x - oldCenter.x;
            double moveY = newCenter.y - oldCenter.y;
            if (Math.abs(moveX) > epsilon || Math.abs(moveY) > epsilon) {
                for (Line line : box.incidentLines) {
                    if (line.getType() != LineType.ORTHOGONAL) {
                        Point2D.Double endPoint;
                        if (box == line.getStart()) {
                            endPoint = line.getStartPoint();
                        } else {
                            endPoint = line.getEndPoint();
                        }
                        endPoint.x += moveX;
                        endPoint.y += moveY;
                    }
                }
            }
        }
    }
}
