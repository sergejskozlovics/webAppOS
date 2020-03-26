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
import java.util.ArrayList;
import lv.lumii.layoutengine.LayoutConstraints.ConstraintLine;
import lv.lumii.layoutengine.Line.LineType;
import lv.lumii.layoutengine.OrthogonalSegment.LabelSegment.BoxOutsideLabelSegment;
import lv.lumii.layoutengine.OrthogonalSegment.LabelSegment.LineLabelSegment;
import lv.lumii.layoutengine.OrthogonalSegment.RectangleSegment.SegmentType;
import lv.lumii.layoutengine.OutsideLabel.BoxOutsideLabel;
import lv.lumii.layoutengine.OutsideLabel.LineLabel;
import lv.lumii.layoutengine.OutsideLabel.LineLabel.Orientation;
import lv.lumii.layoutengine.obstacleGraph.Segment;

/**
 * The {@code OrthogonalSegment} class defines a vertical segment for use in the obstacle graph
 * algorithm. Abstract since it has no links to the original diagram element this segment was
 * created from.
 *
 * @author Evgeny
 */
abstract class OrthogonalSegment extends Segment {

    //<editor-fold defaultstate="collapsed" desc="attributes">
    /**
     * The sort priority of left and top box side segments.
     */
    private final static int BOX_LEFT_SIDE_PRIORITY = Integer.MAX_VALUE - 1;
    /**
     * The sort priority of right and bottom box side segments.
     */
    private final static int BOX_RIGHT_SIDE_PRIORITY = Integer.MIN_VALUE + 1;
    /**
     * The sort priority of left and top label side segments.
     */
    private final static int LABEL_LEFT_SIDE_PRIORITY = Integer.MAX_VALUE;
    /**
     * The sort priority of right and bottom label side segments.
     */
    private final static int LABEL_RIGHT_SIDE_PRIORITY = Integer.MIN_VALUE;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="constructors">
    /**
     * Creates a new orthogonal segment.
     *
     * @param pos the position of the new Segment
     * @param start the start of the new Segment
     * @param end the end of the new Segment
     */
    public OrthogonalSegment(double pos, double start, double end) {
        super(pos, start, end);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="accessors">
    /**
     * Returns the width of the empty space before the {@code OrthogonalSegment}.
     *
     * @return the width of the empty space before the {@code OrthogonalSegment}.
     */
    abstract public double getBeforeSpacing();

    /**
     * Returns the width of the empty space after the {@code OrthogonalSegment}.
     *
     * @return the width of the empty space after the {@code OrthogonalSegment}.
     */
    abstract public double getAfterSpacing();

    /**
     * Gets the priority of this {@code OrthogonalSegment} in relation to other segments with the
     * same abscissa and origin element.
     *
     * @return the sort priority of this segment.
     */
    protected abstract int getSortPriority();
    //</editor-fold>

    /**
     * Finds the minimum distance between this and the given segment. Assumes that this segment
     * should be before the given segment and the segment projections intersect. Returns the sum of
     * this segment's after spacing and the given segment's before spacing.
     *
     * @param segment the segment to find the distance to
     * @return the minimum distance between the two segments.
     */
    double findMinimumDistance(OrthogonalSegment segment) {
        return getAfterSpacing() + segment.getBeforeSpacing();
    }

    /**
     * Moves the segment and its origin to the given position depending on its orientation
     * (horizontal or vertical).
     *
     * @param position the position to move the segment and its origin to
     */
    abstract public void move(double position);

    /**
     * Updates the start and end of this segment.
     */
    abstract void updateLength();

    /**
     * Compares the positions of the two OrthogonalSegment objects. If they are equal compares the
     * segments according to various criteria to separate them consistently.
     *
     * @param o the OrthogonalSegment to compare to
     * @return the result of a comparison of the two OrthogonalSegments.
     */
    @Override
    public int compareTo(Segment o) {
        if (!(o instanceof OrthogonalSegment)) {
            return super.compareTo(o);
        }
        OrthogonalSegment s = (OrthogonalSegment) o;

        if (pos < s.pos) {
            return -1;
        } else if (pos > s.pos) {
            return 1;
        } else {
            return compareCollinear(s);
        }
    }

    /**
     * Compares two collinear segments to determine their sort order. By default compares according
     * to {@link #getSortPriority() }.
     *
     * @param s the collinear segment to compare this segment with
     * @return the result of a comparison of two collinear segments
     */
    int compareCollinear(OrthogonalSegment s) {
        return Integer.compare(getSortPriority(), s.getSortPriority());
    }

    /**
     * Returns whether this OrthogonalSegment corresponds to a horizontal diagram element.
     *
     * @return whether this OrthogonalSegment corresponds to a horizontal diagram element.
     */
    abstract boolean isHorizontal();

    /**
     * A class for storing segments corresponding to the sides of rectangles - boxes and labels.
     */
    abstract static class RectangleSegment extends OrthogonalSegment {

        /**
         * Used for storing the logical type of the segment.
         */
        public static enum SegmentType {

            /**
             * The left side of the rectangle.
             */
            LEFT,
            /**
             * The right side of the rectangle.
             */
            RIGHT,
            /**
             * The top side of the rectangle.
             */
            TOP,
            /**
             * The bottom side of the rectangle.
             */
            BOTTOM
        }
        /**
         * The type of this segment.
         */
        final SegmentType segmentType;
        /**
         * The grid lane this segment's element corresponds to, or 0 if none.
         */
        int lane;

        /**
         * Creates a new segment corresponding to the side of a rectangle
         *
         * @param pos the position of this segment
         * @param start the start coordinate of this segment
         * @param end the end coordinate of this segment
         * @param segmentType the rectangle side this segment corresponds to
         */
        public RectangleSegment(double pos, double start, double end, SegmentType segmentType) {
            super(pos, start, end);
            this.segmentType = segmentType;
        }

        /**
         * Creates a new rectangle segment from the given parameters. Creates an instance of the
         * appropriate subclass depending on the type of the given rectangle.
         *
         * @param pos the position of this segment
         * @param start the start coordinate of this segment
         * @param end the end coordinate of this segment
         * @param segmentType the rectangle side this segment corresponds to
         * @param container the rectangular element this segment corresponds to.
         * @return a new rectangle segment
         */
        static RectangleSegment createRectangleSegment(double pos, double start, double end, SegmentType segmentType, AbstractContainer container) {
            if (container instanceof Box) {
                return new BoxSegment(pos, start, end, segmentType, (Box) container);
            } else if (container instanceof InsideLabel) {
                return new LabelSegment(pos, start, end, segmentType, (InsideLabel) container);
            } else {
                throw new IllegalArgumentException("Container should be Box or InsideLabel.");
            }
        }

        /**
         * Returns the container element this segment corresponds to.
         *
         * @return the container element this segment corresponds to.
         */
        abstract AbstractContainer getContainer();

        /**
         * Returns the minimum width of the empty space before the segment.
         *
         * @return the spacing of the element if it corresponds to the top or left side if the
         * rectangle, 0 otherwise.
         */
        @Override
        public double getBeforeSpacing() {
            switch (segmentType) {
                case LEFT:
                case TOP:
                    return getContainer().spacing;
                default:
                    return 0;
            }
        }

        /**
         * Returns the minimum width of the empty space after the segment.
         *
         * @return the spacing of the element if it corresponds to the right or bottom side if the
         * rectangle, 0 otherwise.
         */
        @Override
        public double getAfterSpacing() {
            switch (segmentType) {
                case LEFT:
                case TOP:
                    return 0;
                default:
                    return getContainer().spacing;
            }
        }

        /**
         * Return the segment type of a {@code RectangleSegment}.
         *
         * @return the segment type.
         */
        public SegmentType getSegmentType() {
            return segmentType;
        }

        /**
         * Moves the segment and its origin element to the given position depending on its
         * orientation (horizontal or vertical). This method moves the side of a rectangle the
         * segment corresponds to. Note that this can temporarily create an invalid inverted
         * rectangle. If this method is called from the normalization algorithms, all rectangle
         * should be valid once the algorithm is complete.
         *
         * @param position the position to move the segment and its origin element to
         */
        @Override
        public void move(double position) {
            /*
             * Moves the OrthogonalSegment.
             */
            setPos(position);
            AbstractContainer container = getContainer();
            switch (segmentType) {
                case LEFT:
                    container.left = position;
                    break;
                case TOP:
                    container.top = position;
                    break;
                case RIGHT:
                    container.right = position;
                    break;
                case BOTTOM:
                    container.bottom = position;
                    break;
            }
        }

        /**
         * Updates the segment's {@link #start} and {@link #end} if the segments endpoints have
         * changed, lest they get desynchronized.
         */
        @Override
        void updateLength() {
            updateLength(1);
        }

        /**
         * Updates the segment's {@link #start} and {@link #end} if the segments endpoints have
         * changed, lest they get desynchronized.
         *
         * @param ratio the part of the rectangle spacing value by which to extend segment bounds
         */
        void updateLength(double ratio) {
            AbstractContainer container = getContainer();
            double epsilon = container.getDiagram().getEpsilon();
            double spacing = container.spacing * ratio;
            if (isHorizontal()) {
                setStart(container.left - spacing + epsilon);
                setEnd(container.right + spacing - epsilon);
            } else {
                setStart(container.top - spacing + epsilon);
                setEnd(container.bottom + spacing - epsilon);
            }
        }

        @Override
        boolean isHorizontal() {
            return segmentType == SegmentType.TOP || segmentType == SegmentType.BOTTOM;
        }

        @Override
        double findMinimumDistance(OrthogonalSegment segment) {
            if (segment instanceof ConstraintSegment && (segmentType == SegmentType.LEFT || segmentType == SegmentType.TOP)) {
                ConstraintSegment cSegment = (ConstraintSegment) segment;
                return cSegment.constraint.spacing + cSegment.prev.constraint.distance;
            } else {
                return super.findMinimumDistance(segment);
            }
        }

        /**
         * Compares this rectangle segment with a collinear segment. Two segments belonging to the
         * same rectangle are ordered so that the left/top side comes first. Two segments belonging
         * to different rectangles are compared first by their grid lanes, then by their desired
         * centers, then by their ID. Otherwise, {@link OrthogonalSegment#compareCollinear(lv.lumii.layoutengine.OrthogonalSegment)
         * } is called.
         *
         * @param s the collinear segment to compare this segment to
         * @return the result of a comparison of two collinear segments
         */
        @Override
        int compareCollinear(OrthogonalSegment s) {
            if (s instanceof RectangleSegment) {
                RectangleSegment b = (RectangleSegment) s;

                AbstractContainer first = getContainer(), second = b.getContainer();
                if (first == second) {
                    return getSegmentType() == SegmentType.LEFT
                            || getSegmentType() == SegmentType.TOP
                            ? -1 : 1;
                } else {
                    if (lane != 0 && b.lane != 0 && lane != b.lane) {
                        return Integer.compare(lane, b.lane);
                    } else {
                        Point2D.Double firstCenter = first.getDesiredCenter();
                        Point2D.Double secondCenter = second.getDesiredCenter();
                        boolean horizontalSegments = isHorizontal();
                        double firstPos = horizontalSegments ? firstCenter.y : firstCenter.x;
                        double secondPos = horizontalSegments ? secondCenter.y : secondCenter.x;
                        return (firstPos < secondPos || (firstPos == secondPos && first.id < second.id)) ? -1 : 1;
                    }
                }
            }
            return super.compareCollinear(s);
        }
    }

    /**
     * The {@code OrthogonalSegment.BoxSegment} class defines a segment for use in the obstacle
     * graph algorithm created from a box.
     */
    static class BoxSegment extends RectangleSegment {

        //<editor-fold defaultstate="collapsed" desc="attributes">
        /**
         * The box this segment was created from.
         */
        private final Box box;
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="constructors">
        /**
         * Creates a new {@code OrthogonalSegment} linked to a box.
         *
         * @param pos the position of the new segment
         * @param start the start of the new segment
         * @param end the end of the new segment
         * @param box the {@code Box} this was created from
         * @param segmentType the logical type of the new segment.
         */
        BoxSegment(double pos, double start, double end, SegmentType segmentType, Box box) {
            super(pos, start, end, segmentType);
            this.box = box;
        }
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="accessors">
        @Override
        protected int getSortPriority() {
            switch (segmentType) {
                case LEFT:
                case TOP:
                    return BOX_LEFT_SIDE_PRIORITY;
                default:
                    return BOX_RIGHT_SIDE_PRIORITY;
            }
        }

        /**
         * Returns the box this segment was created from.
         *
         * @return the box this segment was created from.
         */
        public Box getBox() {
            return box;
        }

        @Override
        AbstractContainer getContainer() {
            return box;
        }
        //</editor-fold>

        @Override
        double findMinimumDistance(OrthogonalSegment segment) {
            if (segment instanceof BoxOutsideLabelSegment && ((BoxOutsideLabelSegment) segment).getContainer().getOwner() == box) {
                return 0;
            } else {
                return super.findMinimumDistance(segment);
            }
        }

        /**
         * Compares this box segment with a collinear segment. Lines are absorbed into the box, as
         * the only collinear lines should be those connected to the box. Outside labels are placed
         * outside the box. Otherwise, {@link RectangleSegment#compareCollinear(lv.lumii.layoutengine.OrthogonalSegment)
         * } is called.
         *
         * @param s the collinear segment to compare this segment to
         * @return the result of a comparison of two collinear segments
         */
        @Override
        int compareCollinear(OrthogonalSegment s) {
            if (s instanceof OrthogonalLineSegment) {
                return (segmentType == SegmentType.LEFT || segmentType == SegmentType.TOP) ? -1 : 1;
            } else if (s instanceof BoxOutsideLabelSegment) {
                return Integer.compare(getSortPriority(), s.getSortPriority());
            }
            return super.compareCollinear(s);
        }
    }

    /**
     * The {@code OrthogonalSegment.LabelSegment} class defines a segment for use in the obstacle
     * graph algorithm created from a label.
     */
    static class LabelSegment extends RectangleSegment {

        //<editor-fold defaultstate="collapsed" desc="attributes">
        /**
         * The label this segment was created from.
         */
        final Label label;
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="constructors">
        /**
         * Creates a new {@code OrthogonalSegment} linked to a label.
         *
         * @param pos the position of the new segment
         * @param start the start of the new segment
         * @param end the end of the new segment
         * @param segmentType the logical type of the new segment.
         * @param label the label this was created from
         */
        LabelSegment(double pos, double start, double end, SegmentType segmentType, Label label) {
            super(pos, start, end, segmentType);
            this.label = label;
        }
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="accessors">
        @Override
        protected int getSortPriority() {
            switch (segmentType) {
                case LEFT:
                case TOP:
                    return LABEL_LEFT_SIDE_PRIORITY;
                default:
                    return LABEL_RIGHT_SIDE_PRIORITY;
            }
        }

        @Override
        Label getContainer() {
            return label;
        }
        //</editor-fold>

        /**
         * A class defining segments corresponding to the sides of box outside labels.
         */
        static class BoxOutsideLabelSegment extends LabelSegment {

            /**
             * Creates a new box outside label segment.
             *
             * @param pos the position of this segment
             * @param start the start coordinate of this segment
             * @param end the end coordinate of this segment
             * @param segmentType the rectangle side this segment corresponds to
             * @param label the label this label segment corresponds to
             */
            public BoxOutsideLabelSegment(double pos, double start, double end, SegmentType segmentType, BoxOutsideLabel label) {
                super(pos, start, end, segmentType, label);
            }

            @Override
            BoxOutsideLabel getContainer() {
                return (BoxOutsideLabel) super.getContainer();
            }

            @Override
            double findMinimumDistance(OrthogonalSegment segment) {
                if (segment instanceof BoxSegment && ((BoxSegment) segment).box == label.getOwner()) {
                    return 0;
                } else {
                    return super.findMinimumDistance(segment);
                }
            }

            @Override
            void updateLength() {
                double spacing = label.spacing;
                double epsilon = label.getDiagram().getEpsilon();
                BoxOutsideLabel boxLabel = getContainer();
                if (isHorizontal()) {
                    throw new UnsupportedOperationException("Only vertical box outside label segments on horizontal sides can be updated.");
                }
                switch (boxLabel.side) {
                    case TOP:
                        setStart(boxLabel.top - spacing + epsilon);
                        setEnd(boxLabel.bottom - 5 * epsilon);
                        break;
                    case BOTTOM:
                        setStart(boxLabel.top + 5 * epsilon);
                        setEnd(boxLabel.bottom + spacing - epsilon);
                        break;
                    default:
                        throw new UnsupportedOperationException("Only vertical box outside label segments on horizontal sides can be updated.");
                }
            }

            /**
             * Compares this box outside label segment with a collinear segment. It is placed
             * outside the box. Otherwise, {@link RectangleSegment#compareCollinear(lv.lumii.layoutengine.OrthogonalSegment)
             * } is called.
             *
             * @param s the collinear segment to compare this segment to
             * @return the result of a comparison of two collinear segments
             */
            @Override
            int compareCollinear(OrthogonalSegment s) {
                if (s instanceof BoxSegment) {
                    return Integer.compare(getSortPriority(), s.getSortPriority());
                }
                return super.compareCollinear(s);
            }
        }

        /**
         * A segment corresponding to a side of a line outside label.
         */
        static class LineLabelSegment extends LabelSegment {

            /**
             * The OrthogonalLineSegment this label is connected to.
             */
            OrthogonalLineSegment lineSegment;

            /**
             * Creates a new line label segment.
             *
             * @param pos the position of this segment
             * @param start the start coordinate of this segment
             * @param end the end coordinate of this segment
             * @param segmentType the rectangle side this segment corresponds to
             * @param label the label this segment corresponds to
             * @param lineSegment the line segment to which this label is attached
             */
            public LineLabelSegment(double pos, double start, double end, SegmentType segmentType, Label label, OrthogonalLineSegment lineSegment) {
                super(pos, start, end, segmentType, label);
                this.lineSegment = lineSegment;
            }

            @Override
            LineLabel getContainer() {
                return (LineLabel) super.getContainer();
            }

            @Override
            double findMinimumDistance(OrthogonalSegment segment) {
                if (segment == lineSegment) {
                    LineLabel lineLabel = getContainer();
                    if (getContainer().orientation == Orientation.CENTER) {
                        if (isHorizontal()) {
                            return lineLabel.getCurrentMinHeight() / 2;
                        } else {
                            return lineLabel.getCurrentMinWidth() / 2;
                        }
                    } else {
                        return 0;
                    }
                } else {
                    return super.findMinimumDistance(segment);
                }
            }

            @Override
            void updateLength() {
                double spacing = label.spacing;
                double epsilon = label.getDiagram().getEpsilon();
                LineLabel lineLabel = getContainer();
                if (isHorizontal()) {
                    throw new UnsupportedOperationException("Only vertical line outside label segments on horizontal line segments can be updated.");
                }

                ArrayList<Point2D.Double> points = lineLabel.getOwner().lineGeometry.getPoints();
                int segmentIndex = lineLabel.segmentIndex;
                if (points.get(segmentIndex).y == points.get(segmentIndex + 1).y) {
                    if (lineLabel.orientation != Orientation.CENTER) {
                        if (lineLabel.isLeftTop()) {
                            setStart(lineLabel.top - spacing + epsilon);
                            setEnd(lineLabel.bottom - epsilon);
                        } else {
                            setStart(lineLabel.top + epsilon);
                            setEnd(lineLabel.bottom + spacing - epsilon);
                        }
                    } else {
                        setStart(lineLabel.top - spacing + epsilon);
                        setEnd(lineLabel.bottom + spacing - epsilon);
                    }
                } else {
                    throw new UnsupportedOperationException("Only vertical line outside label segments on horizontal line segments can be updated.");
                }
            }
        }
    }

    /**
     * The {@code OrthogonalSegment.OrthogonalLineSegment} class defines a segment for use in the
     * obstacle graph algorithm created from an line.
     */
    static class OrthogonalLineSegment extends OrthogonalSegment {

        //<editor-fold defaultstate="collapsed" desc="attributes">
        /**
         * The line this segment was created from.
         */
        private final Line line;

        /**
         * The possible directions of the segment.
         */
        public static enum SegmentDirection {

            /**
             * The segment goes to the left.
             */
            LEFT,
            /**
             * The segment goes to the right.
             */
            RIGHT,
            /**
             * The segment goes up.
             */
            UP,
            /**
             * The segment goes down.
             */
            DOWN
        }
        /**
         * The direction of this segment.
         */
        final SegmentDirection direction;
        /**
         * The start point of the segment.
         */
        final Point2D.Double startPoint;
        /**
         * The end point of the segment.
         */
        final Point2D.Double endPoint;
        /**
         * The priority that defines the relation between two line segments during the sorting, in
         * case these segments have equal coordinate.
         */
        private int priority;
        /**
         * The previous segment in the sequence of this segment's line's segments.
         */
        OrthogonalLineSegment prev;
        /**
         * The next segment in the sequence of this segment's line's segments.
         */
        OrthogonalLineSegment next;
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="constructors">
        /**
         * Creates a new {@code OrthogonalSegment} linked to an line.
         *
         * @param pos the position of the new segment
         * @param start the start of the new segment
         * @param end the end of the new segment
         * @param line the {@code Line} this was created from
         * @param startPoint the first endpoint of the segment
         * @param endPoint the second endpoint of the segment
         */
        OrthogonalLineSegment(double pos, double start, double end,
                Line line, Point2D.Double startPoint, Point2D.Double endPoint) {
            super(pos, start, end);
            if (line.getType() == LineType.ORTHOGONAL) {
                this.line = line;
            } else {
                throw new IllegalArgumentException(
                        "Line type must be orthogonal for OrthogonalLineSegment creation.");
            }

            if (startPoint.equals(endPoint)) {
                throw new IllegalArgumentException("Start point and end point cannot be equal.");
            } else if (startPoint.getX() != endPoint.getX()
                    && startPoint.getY() != endPoint.getY()) {
                throw new IllegalArgumentException("Points must lie on either horizontal or vertical line: startPoint="+startPoint+", endPoint="+endPoint);
            } else {
                if (startPoint.getY() == endPoint.getY()) {
                    direction = startPoint.getX() < endPoint.getX() ? SegmentDirection.RIGHT : SegmentDirection.LEFT;
                } else {
                    direction = startPoint.getY() < endPoint.getY() ? SegmentDirection.DOWN : SegmentDirection.UP;
                }
            }

            this.startPoint = startPoint;
            this.endPoint = endPoint;

            priority = 0;
        }
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="accessors">
        /**
         * Returns the line this segment was created from.
         *
         * @return the line this segment was created from.
         */
        public Line getLine() {
            return line;
        }

        /**
         * Returns the width of the empty space before the {@code OrthogonalLineSegment}.
         *
         * @return the spacing of the line.
         */
        @Override
        public double getBeforeSpacing() {
            return line.getSpacing();
        }

        /**
         * Returns the width of the empty space after the {@code OrthogonalLineSegment}.
         *
         * @return the spacing of the line.
         */
        @Override
        public double getAfterSpacing() {
            return line.getSpacing();
        }

        @Override
        protected int getSortPriority() {
            return priority;
        }

        /**
         * Returns the previous segment in the sequence of this segment's line's segments.
         *
         * @return the previous segment in the sequence of this segment's line's segments.
         */
        OrthogonalSegment.OrthogonalLineSegment getPrev() {
            return prev;
        }

        /**
         * Sets the previous segment in the sequence of this segment's line's segments. Possibly
         * breaks normalization if the passed segment is not actually the previous one along the
         * line.
         *
         * @param prev the previous segment in the sequence of this segment's line's segments.
         */
        void setPrev(OrthogonalSegment.OrthogonalLineSegment prev) {
            this.prev = prev;
        }

        /**
         * Returns the next segment in the sequence of this segment's line's segments.
         *
         * @return the next segment in the sequence of this segment's line's segments.
         */
        OrthogonalSegment.OrthogonalLineSegment getNext() {
            return next;
        }

        /**
         * Sets the next segment in the sequence of this segment's line's segments. Possibly breaks
         * normalization if the passed segment is not actually the next one along the line.
         *
         * @param next the next segment in the sequence of this segment's line's segments.
         */
        void setNext(OrthogonalSegment.OrthogonalLineSegment next) {
            this.next = next;
        }
        //</editor-fold>

        /**
         * Moves the {@code OrthogonalLineSegment} and its origin line segment to the given position
         * depending on its orientation (horizontal or vertical).
         *
         * @param position the position to move the segment and its origin line segment to
         */
        @Override
        public void move(double position) {
            /*
             * Moves the OrthogonalSegment.
             */
            setPos(position);
            /*
             * Moves the origin of the segment.
             */
            if (isHorizontal()) {
                startPoint.setLocation(startPoint.getX(), position);
                endPoint.setLocation(endPoint.getX(), position);
            } else {
                startPoint.setLocation(position, startPoint.getY());
                endPoint.setLocation(position, endPoint.getY());
            }
        }

        /**
         * Updates the segment's {@link #start} and {@link #end} if the segments endpoints have
         * changed, lest they get desynchronized.
         */
        @Override
        void updateLength() {
            Point2D.Double upperPoint = isHorizontal()
                    ? (startPoint.getX() < endPoint.getX() ? startPoint : endPoint)
                    : (startPoint.getY() < endPoint.getY() ? startPoint : endPoint),
                    lowerPoint = upperPoint == startPoint ? endPoint : startPoint;

            /*
             * Here it is important to identify upper and lower points of the segment: it is needed
             * to check whether segment connects to some box at these points. In case it is true,
             * the line segment is not extended "into the box" -- if it is not performed, program
             * can derive inconsistent constraints for quadratic optimization which results in
             * error.
             */
            double epsilon = line.getDiagram().getEpsilon();
            double spacing = line.getSpacing();

            double startBonus = spacing;
            if (line.getStartPoint() == upperPoint || line.getEndPoint() == upperPoint) {
                Box box = (Box) (line.getStartPoint() == upperPoint ? line.getStart() : line.getEnd());
                if (isHorizontal() ? box.left == upperPoint.x : box.top == upperPoint.y) {
                    startBonus = 4 * epsilon;
                } else {
                    startBonus = 0;
                }
            }

            double endBonus = spacing;
            if (line.getStartPoint() == lowerPoint || line.getEndPoint() == lowerPoint) {
                Box box = (Box) (line.getStartPoint() == lowerPoint ? line.getStart() : line.getEnd());
                if (isHorizontal() ? box.right == lowerPoint.x : box.bottom == lowerPoint.y) {
                    endBonus = 4 * epsilon;
                } else {
                    endBonus = 0;
                }
            }

            setStart((isHorizontal() ? upperPoint.x : upperPoint.y) - startBonus + epsilon);
            setEnd((isHorizontal() ? lowerPoint.x : lowerPoint.y) + endBonus - epsilon);
        }

        /**
         * Sets the priority value of this segment used for sorting.
         *
         * @param priority the new priority value
         */
        void setPriority(int priority) {
            this.priority = priority;
        }

//        @Override
//        public boolean equals(Object o) {
//            if (!(o instanceof OrthogonalLineSegment)) {
//                return false;
//            }
//            /*
//             * Checks whether two segments have equal length and positioning, belong to the same
//             * line and have equal priority values.
//             */
//            OrthogonalLineSegment s = (OrthogonalLineSegment) o;
//            return getPos() == s.getPos() && getStart() == s.getStart() && getEnd() == s.getEnd()
//                    && line == s.line && priority == s.priority;
//        }
        /**
         * Checks whether this segment is horizontal.
         *
         * @return whether this segment is horizontal.
         */
        @Override
        public boolean isHorizontal() {
            return direction == SegmentDirection.LEFT || direction == SegmentDirection.RIGHT;
        }

        /**
         * Checks whether this segment is some end segment of its line.
         *
         * @return whether this segment is some end segment of its line.
         */
        public boolean isEndSegment() {
            return prev == null || next == null;
        }

        @Override
        double findMinimumDistance(OrthogonalSegment segment) {
            if (segment instanceof LineLabelSegment && ((LineLabelSegment) segment).lineSegment == this) {
                LineLabel lineLabel = ((LineLabelSegment) segment).getContainer();
                if (lineLabel.orientation == Orientation.CENTER) {
                    if (isHorizontal()) {
                        return lineLabel.getCurrentMinHeight() / 2;
                    } else {
                        return lineLabel.getCurrentMinWidth() / 2;
                    }
                } else {
                    return 0;
                }
            } else {
                return super.findMinimumDistance(segment);
            }
        }

        /**
         * Compares this line segment with a collinear segment. If {@code s} is a box segment,
         * places this line segment inside the box (if a line and box segment are collinear, the
         * line should be connected to that box). Otherwise, {@link OrthogonalSegment#compareCollinear(lv.lumii.layoutengine.OrthogonalSegment)
         * } is called.
         *
         * @param s the collinear segment to compare this segment to
         * @return the result of a comparison of two collinear segments
         */
        @Override
        int compareCollinear(OrthogonalSegment s) {
            if (s instanceof BoxSegment) {
                BoxSegment boxSegment = (BoxSegment) s;
                return (boxSegment.segmentType == SegmentType.LEFT || boxSegment.segmentType == SegmentType.TOP) ? 1 : -1;
            }
            return super.compareCollinear(s);
        }
    }

    /**
     * A class for storing segments corresponding to constraint lines for use in normalization.
     */
    static class ConstraintSegment extends OrthogonalSegment {

        /**
         * The constraint this segments corresponds to.
         */
        ConstraintLine constraint;
        /**
         * The previous constraint segment in this segment's grid.
         */
        ConstraintSegment prev;
        /**
         * The time of DFS entering the container of this constraint segment grid.
         */
        int inTime;

        /**
         * Creates a new constraint segment from the given constraint line. As the constraint is a
         * line, the segment's length is infinite.
         *
         * @param constraint the constraint from which to create this segment
         * @param inTime the time of DFS entering the container of this constraint segment grid
         */
        ConstraintSegment(ConstraintLine constraint, int inTime) {
            super(constraint.pos, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
            this.constraint = constraint;
            this.inTime = inTime;
        }

        /**
         * Constraints have no spacing and always return 0.
         *
         * @return 0
         */
        @Override
        public double getBeforeSpacing() {
            return constraint.spacing;
        }

        /**
         * Constraints have no spacing and always return 0.
         *
         * @return 0
         */
        @Override
        public double getAfterSpacing() {
            return constraint.spacing;
        }

        /**
         * Throws an {@code UnsupportedOperationException}, as constraint segments should never be
         * sorted.
         *
         * @return nothing
         */
        @Override
        protected int getSortPriority() {
            throw new UnsupportedOperationException("Constraint segments should never be sorted by priority.");
        }

        /**
         * Moves the corresponding constraint line to the given position.
         *
         * @param position the new position of the corresponding constraint line
         */
        @Override
        public void move(double position) {
            setPos(position);
            constraint.pos = position;
        }

        /**
         * Assumes this segment is to the left of the passed segment. If the passed segment is also
         * a {@code ConstraintSegment}, returns this segment's constraint line's minimum distance to
         * its next line. Otherwise behaves as
         * {@link OrthogonalSegment#findMinimumDistance(lv.lumii.layoutengine.OrthogonalSegment)}
         *
         * @param segment the segment to find the distance to
         * @return the minimum distance between the two segments
         */
        @Override
        double findMinimumDistance(OrthogonalSegment segment) {
            if (segment instanceof ConstraintSegment) {
                ConstraintSegment secondSegment = (ConstraintSegment) segment;
                if (inTime >= secondSegment.inTime) {
                    return constraint.spacing + constraint.distance + secondSegment.constraint.spacing;
                } else {
                    return secondSegment.constraint.spacing + secondSegment.prev.constraint.distance + constraint.spacing;
                }
            } else {
                RectangleSegment rSegment = (RectangleSegment) segment;
                if (rSegment.segmentType == SegmentType.RIGHT || rSegment.segmentType == SegmentType.BOTTOM) {
                    return constraint.distance + constraint.spacing;
                } else {
                    return super.findMinimumDistance(segment);
                }
            }
        }

        /**
         * Throws an {@code UnsupportedOperationException}, as constraint segments do not have
         * length.
         */
        @Override
        void updateLength() {
            throw new UnsupportedOperationException("Constraint segments don't have a length.");
        }

        /**
         * Currently throws an {@code UnsupportedOperationException}.
         * @return nothing
         */
        @Override
        boolean isHorizontal() {
            throw new UnsupportedOperationException("Not planned to be implemented.");
        }
    }
}
