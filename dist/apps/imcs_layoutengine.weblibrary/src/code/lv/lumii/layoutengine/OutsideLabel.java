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
import java.util.LinkedHashMap;
import lv.lumii.layoutengine.Box.BoxSide;
import lv.lumii.layoutengine.LayoutConstraints.*;
import lv.lumii.layoutengine.Line.LineType;
import lv.lumii.layoutengine.util.Pair;

/**
 * This class describes labels that are on the outer perimeter of some element.
 *
 * @author JK
 */
public abstract class OutsideLabel extends Label {

    /**
     * The position of this label along its line or box side, as a proportion of its whole length.
     * Has to be value between 0 and 1, inclusive.
     */
    double position;
    /**
     * Whether this label is flipped sideways (its width and height are swapped).
     */
    boolean flipped;
    /**
     * Whether to flip this label sideways (swap its width and height) when placing it on a vertical
     * segment, so that it always runs along the segment (unless {@link #flipped} is set, in which
     * case this ensures the label always runs perpendicular to the segment).
     */
    boolean autoFlip;
    /**
     * Stores the old center of the outside label after the last outside label grow procedure. Used
     * to lazy move the descendant inside labels of this outside label.
     */
    Point2D.Double oldCenter;

    /**
     * Creates a new outside label instance.
     *
     * @param left the left side coordinate
     * @param right the right side coordinate
     * @param top the top coordinate
     * @param bottom the bottom coordinate
     * @param position the relative position of label to the line length
     * @param owner the owner of the label
     * @param constraintType the constraint type for the label
     * @param spacing the spacing of the label
     */
    OutsideLabel(double left, double right, double top, double bottom, double position, Element owner, ConstraintType constraintType, double spacing) {
        super(left, right, top, bottom, owner, constraintType, spacing);
        oldCenter = getCenter();
        this.position = position;
    }

    /**
     * Rotates this label sideways, i.e., swaps the width and the height of the label. The whole
     * diagram is reordered afterwards, or at the end of the current transaction. Use the method
     * {@link #isFlipped()} to check whether the label currently is flipped.
     */
    public void flip() {
        Transaction.Operation.AdjustOperation.prepareTransaction(getDiagram());

        _flip();

        getDiagram().layoutAdjustOutsideLabel(this);
    }

    public double getPosition() { // added by SK
    	return position;
    }
    /**
     * Rotates the label sideways without performing normalization.
     */
    void _flip() {
        flipped = !flipped;
        double tmp = userMinHeight;
        userMinHeight = userMinWidth;
        userMinWidth = tmp;
        tmp = minHeight;
        minHeight = minWidth;
        minWidth = tmp;

        double width = getWidth();
        double height = getHeight();
        Point2D.Double center = getCenter();
        left = center.x - height / 2;
        right = left + height;
        top = center.y - width / 2;
        bottom = top + width;
    }

    /**
     * Checks whether the label currently is rotated.
     *
     * @return whether the label currently is rotated.
     */
    public boolean isFlipped() {
        return flipped;
    }

    /**
     * Sets the auto flip parameter for this label. If auto flip is set, then the label will be
     * always flipped sideways (its width and height swapped) when placed on a vertical segment, so
     * that it always runs along the segment (unless {@link #flipped} is set, in which case this
     * ensures the label always runs perpendicular to the segment).
     *
     * @param state whether to set or unset the auto flip parameter
     */
    public abstract void setAutoFlip(boolean state);

    /**
     * Checks whether the auto flip parameter for this label is set.
     *
     * @return whether the auto flip parameter for this label is set.
     */
    public boolean isAutoFlipped() {
        return autoFlip;
    }

    @Override
    void saveAttributes(org.w3c.dom.Element e, org.w3c.dom.Document doc, LinkedHashMap<Object, Integer> idMap) {
        super.saveAttributes(e, doc, idMap);
        e.setAttribute("position", Double.toString(position));
        e.setAttribute("flipped", Boolean.toString(flipped));
        e.setAttribute("autoFlip", Boolean.toString(autoFlip));
    }

    /**
     * Creates a new OutsideLabel from the given XML element, along with its children. Adds all of
     * them {@code objectMap}.
     *
     * @param e an XML element corresponding to a OutsideLabel
     * @param objectMap a map from element storage IDs to diagram objects
     * @param owner the owner of the label
     */
    OutsideLabel(org.w3c.dom.Element e, LinkedHashMap<Integer, Object> objectMap, Element owner) {
        super(e, objectMap, owner);
        position = Double.parseDouble(e.getAttribute("position"));
        flipped = Boolean.parseBoolean(e.getAttribute("flipped"));
        autoFlip = Boolean.parseBoolean(e.getAttribute("autoFlip"));
        oldCenter = getCenter();
    }

    @Override
    public void resize(Rectangle2D.Double rectangle) {
        double minLength = 10 * getDiagram().getEpsilon();
        if (rectangle.width < minLength) {
            rectangle.width = minLength;
        }
        if (rectangle.height < minLength) {
            rectangle.height = minLength;
        }

        boolean empty = insideLabels.isEmpty();

        if (empty) {
            Transaction.Operation.AdjustOperation.prepareTransaction(getDiagram());
        } else {
            Transaction.Operation.ModifyLabelOperation.prepareTransaction(getDiagram());
        }

        setCenter(new Point2D.Double(rectangle.getCenterX(), rectangle.getCenterY()));

        userMinWidth = rectangle.getWidth();
        userMinHeight = rectangle.getHeight();

        setCurrentMinSize(userMinWidth, userMinHeight);

        if (empty) {
            if (this instanceof LineLabel) {
                ((LineLabel) this).resetBounds();
            }
        }
        getDiagram().layoutModifyLabel(this);
    }

    /**
     * Resizes this label to the given dimensions while keeping its center unchanged if possible.
     *
     * @param width the new width of the label
     * @param height the new height of the label
     */
    public void resize(double width, double height) {
        double minLength = 10 * getDiagram().getEpsilon();
        if (width < minLength) {
            width = minLength;
        }
        if (height < minLength) {
            height = minLength;
        }

        boolean empty = insideLabels.isEmpty();

        if (empty) {
            Transaction.Operation.AdjustOperation.prepareTransaction(getDiagram());
        } else {
            Transaction.Operation.ModifyLabelOperation.prepareTransaction(getDiagram());
        }

        userMinWidth = width;
        userMinHeight = height;

        setCurrentMinSize(userMinWidth, userMinHeight);

        getDiagram().layoutModifyLabel(this);
    }

    @Override
    public void setSpacing(double spacing) {
        Transaction.Operation.AdjustOperation.prepareTransaction(getDiagram());

        this.spacing = spacing;

        getDiagram().layoutAdjust();
    }

    @Override
    public InsideLabel createInsideLabel(Rectangle2D.Double rectangle,
            LayoutConstraints.ConstraintType constraintType,
            double spacing,
            Integer row, Integer column) {
        Transaction.Operation.ModifyLabelOperation.prepareTransaction(getDiagram());

        InsideLabel label = new InsideLabel(rectangle.getMinX(), rectangle.getMaxX(), rectangle.getMinY(), rectangle.getMaxY(), this, constraintType, spacing);
        insideLabels.add(label);

        if (getLayoutConstraints().getType() == LayoutConstraints.ConstraintType.GRID) {
            LayoutConstraints.GridLayoutConstraints constraints = (LayoutConstraints.GridLayoutConstraints) getLayoutConstraints();
            constraints._setCell(label, row, column);
        }

        getDiagram().layoutModifyLabel(this);

        return label;
    }

    /**
     * A class for box outside labels.
     */
    public static class BoxOutsideLabel extends OutsideLabel {

        /**
         * The box side to which the label belongs.
         */
        BoxSide side;

        /**
         * Creates a new box outside label.
         *
         * @param point the point at which to insert the label. It is moved to the necessary side
         * afterwards.
         * @param width the width of the new label
         * @param height the height of the new label
         * @param owner the owner of the label
         * @param side the box side of the label
         * @param position the relative label position on the box side
         * @param constraintType the type of the layout constraints of the new label
         * @param spacing the spacing of the label
         */
        BoxOutsideLabel(Point2D.Double point, double width, double height, Box owner, BoxSide side, double position, ConstraintType constraintType, double spacing) {
            super(point.x - width / 2, point.x + width / 2, point.y - height / 2, point.y + height / 2, position, owner, constraintType, spacing);
            this.side = side;
        }

        @Override
        public Box getOwner() {
            return (Box) super.getOwner();
        }

        @Override
        public void setAutoFlip(boolean state) {
            if (autoFlip != state) {
                autoFlip = state;
                if (!side.isHorizontal()) {
                    flip();
                }
            }
        }

        /**
         * Moves this box outside label to the given point onto the given box. The point is moved to
         * the perimeter of the box if necessary. This operation can change this label's box side.
         *
         * @param point the point to move this label to. If this point is not on the perimeter of
         * this label's box, it is moved there.
         * @param box the new box owner of this label
         */
        public void move(Point2D.Double point, Box box) {
            Diagram newDiagram = box.getDiagram();
            Diagram oldDiagram = getDiagram();
            Transaction.Operation.AdjustOperation.prepareTransaction(newDiagram);
            if (newDiagram != oldDiagram) {
                Transaction.Operation.AdjustOperation.prepareTransaction(oldDiagram);
            }

            box.addOutsideLabel(this);
            if (box != getOwner()) {
                getOwner().removeChild(this);
                setOwner(box);
                if (newDiagram != oldDiagram) {
                    setDiagram(newDiagram);
                    for (Element element : getDescendants()) {
                        element.setDiagram(newDiagram);
                    }
                }
            }

            GeometryHelper.movePointToPerimeter(point, getOwner());

            BoxSide oldSide = side;
            side = BoxSide.findPointSide(point, point, getOwner());

            if (autoFlip && side.isHorizontal() != oldSide.isHorizontal()) {
                _flip();
            }

            switch (side) {
                case LEFT:
                case RIGHT:
                    position = (point.y - box.top) / box.getHeight();
                    break;
                default:
                    position = (point.x - box.left) / box.getWidth();
            }

            calculateBounds(point);

            newDiagram.layoutAdjustOutsideLabel(this);
            if (newDiagram != oldDiagram) {
                oldDiagram.layoutAdjust();
            }
        }

        /**
         * Moves this label to the given side of the given box at the given position.
         *
         * @param side the side of this labels box to move it to
         * @param position the position on this side to place the label at, as a proportion of the
         * whole length of the side. Must be between 0 and 1, inclusive.
         * @param box the new box owner of this label
         */
        public void move(BoxSide side, double position, Box box) {
            if (position < 0 || position > 1) {
                throw new IllegalArgumentException("Box outside label position should be between 0 and 1, inclusive.");
            }

            Diagram newDiagram = box.getDiagram();
            Diagram oldDiagram = getDiagram();
            Transaction.Operation.AdjustOperation.prepareTransaction(newDiagram);
            if (newDiagram != oldDiagram) {
                Transaction.Operation.AdjustOperation.prepareTransaction(oldDiagram);
            }

            BoxSide oldSide = this.side;
            this.side = side;

            this.position = position;

            box.addOutsideLabel(this);
            if (box != getOwner()) {
                getOwner().removeChild(this);
                setOwner(box);
                if (newDiagram != oldDiagram) {
                    setDiagram(newDiagram);
                    for (Element element : getDescendants()) {
                        element.setDiagram(newDiagram);
                    }
                }
            }

            if (autoFlip && side.isHorizontal() != oldSide.isHorizontal()) {
                _flip();
            }

            double x, y;
            if (side == BoxSide.LEFT || side == BoxSide.RIGHT) {
                y = top + position * getHeight();
                if (side == BoxSide.LEFT) {
                    x = left;
                } else {
                    x = right;
                }
            } else {
                x = left + position * getWidth();
                if (side == BoxSide.TOP) {
                    y = top;
                } else {
                    y = bottom;
                }
            }

            calculateBounds(new Point2D.Double(x, y));
            newDiagram.layoutAdjustOutsideLabel(this);
            if (newDiagram != oldDiagram) {
                oldDiagram.layoutAdjust();
            }
        }

        /**
         * Computes the bounds of this label from the given point on the perimeter of this label's
         * box. The label is grown so that this point is at the center of the side of this label
         * touching its box.
         *
         * @param point a point on the perimeter of this label's box from which to grow this label
         */
        void calculateBounds(Point2D.Double point) {
            double width = getWidth(), height = getHeight();
            if (side == BoxSide.LEFT || side == BoxSide.RIGHT) {
                top = point.y - height / 2;
                bottom = top + height;
                if (side == BoxSide.LEFT) {
                    left = point.x - width;
                    right = point.x;
                } else {
                    left = point.x;
                    right = point.x + width;
                }
            } else {
                left = point.x - width / 2;
                right = left + width;
                if (side == BoxSide.TOP) {
                    top = point.y - height;
                    bottom = point.y;
                } else {
                    top = point.y;
                    bottom = point.y + height;
                }
            }
        }

        @Override
        void saveAttributes(org.w3c.dom.Element e, org.w3c.dom.Document doc, LinkedHashMap<Object, Integer> idMap) {
            super.saveAttributes(e, doc, idMap);
            e.setAttribute("orientation", side.name());
        }

        /**
         * Creates a new XML element corresponding to this box outside label in the given document.
         * Includes all its children. Puts the IDs of any new elements in {@code idMap}.
         *
         * @param doc the XML document in which to create the new element
         * @param idMap a map from diagram objects to element storage IDs.
         * @return a new element corresponding to this diagram element
         */
        org.w3c.dom.Element saveToXML(org.w3c.dom.Document doc, LinkedHashMap<Object, Integer> idMap) {
            org.w3c.dom.Element e = doc.createElement(XMLHelper.NAMESPACE + ":outsideLabel");
            saveAttributes(e, doc, idMap);
            return e;
        }

        /**
         * Creates a new BoxOutsideLabel from the given XML element, along with its children. Adds
         * all of them {@code objectMap}.
         *
         * @param e an XML element corresponding to a BoxOutsideLabel
         * @param objectMap a map from element storage IDs to diagram objects
         * @param owner the owner of the label
         */
        BoxOutsideLabel(org.w3c.dom.Element e, LinkedHashMap<Integer, Object> objectMap, Box owner) {
            super(e, objectMap, owner);
            side = BoxSide.valueOf(e.getAttribute("orientation"));
        }
    }

    /**
     * A class for line outside labels.
     */
    public static class LineLabel extends OutsideLabel {

        /**
         * The index of the line segment this label is attached to, starting from the beginning of
         * the label's line.
         */
        int segmentIndex;
        /**
         * Whether the old segment direction was horizontal or vertical.
         */
        boolean segmentHorizontal;
        /**
         * Whether the label should be positioned clockwise along the line, if the label orientation
         * is set to {@link Orientation#BOTH}.
         */
        boolean clockwiseWithBoth;
        /**
         * The angle this line label should be rotated counterclockwise, or {@code null} if the
         * label should not be rotated at all.
         */
        Double rotationAngle;
        /**
         * Whether the label should be rotated.
         */
        boolean rotated;
        /**
         * The current proportional position of this label. If the label is moved, maintains this
         * instead of position until arranging, so that its position is stable.
         */
        double currentPosition;

        /**
         * This enumeration defines the options for limiting on which side of their line can labels
         * be placed.
         */
        public static enum Orientation {

            /**
             * Places the label clockwise from the line, looking from start to end.
             */
            CLOCKWISE,
            /**
             * Places the label counterclockwise from the line, looking from start to end.
             */
            COUNTERCLOCKWISE,
            /**
             * Places the label on top of the line, or to the left for vertical lines.
             */
            LEFT_TOP,
            /**
             * Places the label at the bottom of the line, or to the right for vertical lines.
             */
            RIGHT_BOTTOM,
            /**
             * Places the label over the line.
             */
            CENTER,
            /**
             * The label can be placed on either side of the line.
             */
            BOTH
        }
        /**
         * Stores on which side of the line this label should be placed.
         */
        Orientation orientation;

        /**
         * Returns the orientation of this line label. The orientation is a parameter which defines
         * on which side of the line the label should be placed.
         *
         * @return the orientation of this line label
         */
        public Orientation getOrientation() {
            return orientation;
        }

        /**
         * Sets the orientation of this line label. The orientation is a parameter which defines on
         * which side of the line the label should be placed.
         *
         * @param orientation the new orientation of this line label
         */
        public void setOrientation(Orientation orientation) {
            Transaction.Operation.AdjustOperation.prepareTransaction(getDiagram());

            if (orientation == Orientation.BOTH) {
                ArrayList<Point2D.Double> points = getOwner().lineGeometry.points;
                Line2D.Double segment = new Line2D.Double(points.get(segmentIndex), points.get(segmentIndex + 1));
                clockwiseWithBoth = segment.relativeCCW(getCenter()) < 0;
            }

            this.orientation = orientation;

            getDiagram().layoutAdjust();
        }

        /**
         * Returns the clockwise rotation angle for this label if this label is to be rotated.
         * Otherwise returns 0. Labels for orthogonal lines are never rotated (the method also
         * returns 0).
         *
         * @return the angle this label should be rotated clockwise.
         */
        public double getRotationAngle() {
            Line line = getOwner();
            if (!rotated || line.getType() == LineType.ORTHOGONAL) {
                return 0;
            } else {
                ArrayList<Point2D.Double> points = line.getPoints();
                Point2D.Double first = points.get(segmentIndex);
                Point2D.Double second = points.get(segmentIndex + 1);
                return Math.atan((second.y - first.y) / (second.x - first.x));
            }
        }

        /**
         * Sets whether to rotate this label. Labels for orthogonal lines are never rotated, even if
         * this is set to {@code true}.
         *
         * @param state the rotation state of the label.
         */
        public void setRotation(boolean state) {
            Transaction.Operation.AdjustOperation.prepareTransaction(getDiagram());
            rotated = state;
            getDiagram().layoutAdjust();
        }

        /**
         * Returns whether the label is rotated.Labels for orthogonal lines are never rotated, even
         * if this method returns {@code true}.
         *
         * @return whether the label is rotated.
         */
        public boolean isRotated() {
            return rotated;
        }

        /**
         * Resets the bounds of this label. Doesn't adjust the diagram.
         */
        void resetBounds() {
            Point2D.Double center = getCenter();
            left = center.x - minWidth / 2;
            right = left + minWidth;
            top = center.y - minHeight / 2;
            bottom = top + minHeight;
        }

        /**
         * Creates a new line label instance.
         *
         * @param point the center of the label
         * @param segmentIndex the line segment index for this label
         * @param position the relative position of label to the line length
         * @param orientation the orientation for this label
         * @param width the width of this label
         * @param height the height of this label
         * @param owner the line owner of this label
         * @param constraintType the constraint type for this label
         * @param spacing the spacing of the label
         */
        LineLabel(Point2D.Double point, int segmentIndex, double position, Orientation orientation, double width, double height, Line owner, ConstraintType constraintType, double spacing) {
            super(point.x - width / 2, point.x + width / 2, point.y - height / 2, point.y + height / 2, position, owner, constraintType, spacing);
            this.segmentIndex = segmentIndex;
            this.orientation = orientation;
            this.currentPosition = position;

            if (owner.getType() == LineType.ORTHOGONAL) {
                segmentHorizontal = owner.getPoints().get(segmentIndex).y == owner.getPoints().get(segmentIndex + 1).y;
            }
        }

        @Override
        public Line getOwner() {
            return (Line) super.getOwner();
        }

        @Override
        public void setAutoFlip(boolean state) {
            if (autoFlip != state) {
                autoFlip = state;
                if (!segmentHorizontal) {
                    flip();
                }
            }
        }

        @Override
        void _flip() {
            super._flip();
            if (getOwner().getType() != LineType.ORTHOGONAL) {
                resetBounds();
            }
        }

        /**
         * Moves this label to the given point on the given line. If the point is not on the line,
         * first moves the point onto the line.
         *
         * @param point the point where to move this label
         * @param line the line on which to place this label
         */
        public void move(Point2D.Double point, Line line) {
            Diagram newDiagram = line.getDiagram();
            Diagram oldDiagram = getDiagram();
            Transaction.Operation.AdjustOperation.prepareTransaction(newDiagram);
            if (newDiagram != oldDiagram) {
                Transaction.Operation.AdjustOperation.prepareTransaction(oldDiagram);
            }

            line.addLabel(this);
            if (line != getOwner()) {
                getOwner().removeChild(this);
                setOwner(line);
                if (newDiagram != oldDiagram) {
                    setDiagram(newDiagram);
                    for (Element element : getDescendants()) {
                        element.setDiagram(newDiagram);
                    }
                }
            }

            segmentIndex = GeometryHelper.findNearestSegment(point, line);

            ArrayList<Point2D.Double> points = line.lineGeometry.points;
            Line2D.Double segment = new Line2D.Double(points.get(segmentIndex), points.get(segmentIndex + 1));
            clockwiseWithBoth = segment.relativeCCW(point) < 0;

            GeometryHelper.movePointToLine(point, line, segmentIndex);

            double partLength = line.getLength(0, segmentIndex);
            Point2D.Double segmentStart = points.get(segmentIndex);
            double lineLength = partLength + line.getLength(segmentIndex, points.size() - 1);
            partLength += line.getType() == Line.LineType.ORTHOGONAL ? Math.abs(point.x - segmentStart.x) + Math.abs(point.y - segmentStart.y) : point.distance(segmentStart);
            position = partLength / lineLength;
            currentPosition = position;

            move(point, oldDiagram);
        }

        /**
         * Moves this label to the given relative position on the given line.
         *
         * @param position the relative position where to place this label on the line (should be
         * between 0 and 1, inclusive)
         * @param line the line on which to place this label
         */
        public void move(double position, Line line) {
            if (position < 0 || position > 1) {
                throw new IllegalArgumentException("Line label position should be between 0 and 1, inclusive.");
            }

            Diagram newDiagram = line.getDiagram();
            Diagram oldDiagram = getDiagram();
            Transaction.Operation.AdjustOperation.prepareTransaction(newDiagram);
            if (newDiagram != oldDiagram) {
                Transaction.Operation.AdjustOperation.prepareTransaction(oldDiagram);
            }

            line.addLabel(this);
            if (line != getOwner()) {
                getOwner().removeChild(this);
                setOwner(line);
                if (newDiagram != oldDiagram) {
                    setDiagram(newDiagram);
                    for (Element element : getDescendants()) {
                        element.setDiagram(newDiagram);
                    }
                }
            }

            this.position = position;
            currentPosition = position;
            Pair<Integer, Double> segmentPoint = GeometryHelper.findSegmentAndPoint(position, line);

            segmentIndex = segmentPoint.getFirst();
            double ratio = segmentPoint.getSecond();
            ArrayList<Point2D.Double> points = line.getPoints();
            Point2D.Double first = points.get(segmentIndex), second = points.get(segmentIndex + 1);
            Point2D.Double point = new Point2D.Double(first.x + ratio * (second.x - first.x), first.y + ratio * (second.y - first.y));

            clockwiseWithBoth = false;

            move(point, oldDiagram);
        }

        /**
         * Moves the label center onto the given point and adjusts the diagram.
         *
         * @param point the new center of the label
         * @param oldDiagram the diagram of the label before the moving
         */
        void move(Point2D.Double point, Diagram oldDiagram) {
            setCenter(point);
            Diagram newDiagram = getDiagram();
            newDiagram.layoutAdjustOutsideLabel(this);
            if (newDiagram != oldDiagram) {
                oldDiagram.layoutAdjust();
            }
        }

        /**
         * Checks whether the label should be on the top/left side of its line segment.
         *
         * @return whether the label should be on the top/left side of its line segment.
         */
        boolean isLeftTop() {
            ArrayList<Point2D.Double> points = getOwner().lineGeometry.getPoints();
            Point2D.Double first = points.get(segmentIndex), second = points.get(segmentIndex + 1);
            boolean leftTop = false;

            if (getOwner().getType() == LineType.ORTHOGONAL) {
                switch (orientation) {
                    case LEFT_TOP:
                        leftTop = true;
                        break;
                    case CLOCKWISE:
                        if (segmentHorizontal) {
                            leftTop = first.x > second.x;
                        } else {
                            leftTop = first.y < second.y;
                        }
                        break;
                    case COUNTERCLOCKWISE:
                        if (segmentHorizontal) {
                            leftTop = first.x < second.x;
                        } else {
                            leftTop = first.y > second.y;
                        }
                        break;
                    case BOTH:
                        if (segmentHorizontal) {
                            leftTop = clockwiseWithBoth == first.x > second.x;
                        } else {
                            leftTop = clockwiseWithBoth == first.y < second.y;
                        }
                        break;
                }
            } else {
                switch (orientation) {
                    case LEFT_TOP:
                        leftTop = true;
                        break;
                    case CLOCKWISE:
                        if (first.x == second.x) {
                            leftTop = first.y < second.y;
                        } else {
                            leftTop = second.x < first.x;
                        }
                        break;
                    case COUNTERCLOCKWISE:
                        if (first.x == second.x) {
                            leftTop = first.y > second.y;
                        } else {
                            leftTop = second.x > first.x;
                        }
                        break;
                    case BOTH:
                        if (first.x == second.x) {
                            leftTop = clockwiseWithBoth == first.y < second.y;
                        } else {
                            leftTop = clockwiseWithBoth == second.x < first.x;
                        }
                        break;
                }
            }

            return leftTop;
        }

        /**
         * Checks whether the label should be on the clockwise side of its line segment.
         *
         * @return whether the label should be on the clockwise side of its line segment.
         */
        boolean isClockwise() {
            ArrayList<Point2D.Double> points = getOwner().lineGeometry.getPoints();
            Point2D.Double first = points.get(segmentIndex), second = points.get(segmentIndex + 1);

            switch (orientation) {
                case CLOCKWISE:
                    return true;
                case COUNTERCLOCKWISE:
                    return false;
                case LEFT_TOP:
                    if (first.x == second.x) {
                        return first.y < second.y;
                    } else {
                        return first.x > second.x;
                    }
                case RIGHT_BOTTOM:
                    if (first.x == second.x) {
                        return first.y > second.y;
                    } else {
                        return first.x < second.x;
                    }
                default:
                    return clockwiseWithBoth;
            }
        }

        @Override
        void saveAttributes(org.w3c.dom.Element e, org.w3c.dom.Document doc, LinkedHashMap<Object, Integer> idMap) {
            super.saveAttributes(e, doc, idMap);
            e.setAttribute("segmentIndex", Integer.toString(segmentIndex));
            e.setAttribute("orientation", orientation.name());
        }

        /**
         * Creates a new XML element corresponding to this line outside label in the given document.
         * Includes all its children. Puts the IDs of any new elements in {@code idMap}.
         *
         * @param doc the XML document in which to create the new element
         * @param idMap a map from diagram objects to element storage IDs.
         * @return a new element corresponding to this diagram element
         */
        org.w3c.dom.Element saveToXML(org.w3c.dom.Document doc, LinkedHashMap<Object, Integer> idMap) {
            org.w3c.dom.Element e = doc.createElement(XMLHelper.NAMESPACE + ":outsideLabel");
            saveAttributes(e, doc, idMap);
            return e;
        }

        /**
         * Creates a new LineLabel from the given XML element, along with its children. Adds all of
         * them {@code objectMap}.
         *
         * @param e an XML element corresponding to a LineLabel
         * @param objectMap a map from element storage IDs to diagram objects
         * @param owner the owner of the label
         */
        LineLabel(org.w3c.dom.Element e, LinkedHashMap<Integer, Object> objectMap, Line owner) {
            super(e, objectMap, owner);
            segmentIndex = Integer.valueOf(e.getAttribute("segmentIndex"));
            orientation = Orientation.valueOf(e.getAttribute("orientation"));
            currentPosition = position;

            if (owner.getType() == LineType.ORTHOGONAL) {
                segmentHorizontal = owner.getPoints().get(segmentIndex).y == owner.getPoints().get(segmentIndex + 1).y;
            }
        }
    }
}
