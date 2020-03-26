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

import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import lv.lumii.layoutengine.ArrangeData.ArrangeStyle;
import lv.lumii.layoutengine.LayoutConstraints.ConstraintType;
import lv.lumii.layoutengine.LayoutConstraints.GridLayoutConstraints;
import lv.lumii.layoutengine.Line.LineType;
import lv.lumii.layoutengine.OutsideLabel.BoxOutsideLabel;
import lv.lumii.layoutengine.Transaction.Operation.LineOperation;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * This class specifies the box element type.
 *
 * @author karlis
 */
public class Box extends Container implements Connectible, Rectangular {

    //<editor-fold defaultstate="collapsed" desc="attributes">
    /**
     * The list of lines incident to this box.
     */
    final LinkedHashSet<Line> incidentLines;
    /**
     * The set of the outside labels that belong to this box.
     */
    final LinkedHashSet<BoxOutsideLabel> outsideLabels;

    //<editor-fold defaultstate="collapsed" desc="constructors">
    /**
     * Creates a new Box.
     *
     * @param left the left side coordinate
     * @param right the right side coordinate
     * @param top the top side coordinate
     * @param bottom the bottom side coordinate
     * @param owner the element that will own the new box
     * @param arrangeStyle the style in which to arrange the box elements
     * @param constraintType the constraints on the layout of the box elements
     * @param spacing the new box's spacing
     */
    Box(double left, double right, double top, double bottom,
            Container owner,
            ArrangeStyle arrangeStyle,
            ConstraintType constraintType,
            double spacing) {
        super(left, right, top, bottom, owner, arrangeStyle, constraintType, spacing);

        incidentLines = new LinkedHashSet<>();
        outsideLabels = new LinkedHashSet<>();
    }

    /**
     * Creates a new Box.
     *
     * @param left the left side coordinate
     * @param right the right side coordinate
     * @param top the top side coordinate
     * @param bottom the bottom side coordinate
     * @param owner the element that will own the new box
     * @param spacing the new box's spacing
     */
    Box(double left, double right, double top, double bottom,
            Container owner,
            double spacing) {
        this(left, right, top, bottom, owner, ArrangeStyle.INHERITED, ConstraintType.NONE, spacing);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="accessors">
    /**
     * Sets the spacing value of the box. The whole diagram is reordered afterwards, or at the end
     * of the current transaction.
     *
     * @param spacing the new spacing value
     */
    @Override
    public void setSpacing(double spacing) {
        Transaction.Operation.SetSpacingOperation.prepareTransaction(getDiagram());

        getDiagram().layoutChangeRectangleSpacing(this, spacing);
    }

    /**
     * Sets the minimum height. The whole diagram is reordered afterwards, or at the end of the
     * current transaction.
     *
     * @param minHeight the new minimum height
     */
    public void setMinHeight(double minHeight) {
        this.userMinHeight = minHeight;
        this.minHeight = Math.max(this.minHeight, userMinHeight);
        if (userMinHeight > getHeight()) {
            resize(new Rectangle2D.Double(left, top - (userMinHeight - getHeight()) / 2,
                    right - left, userMinHeight));
        }
    }

    /**
     * Sets the minimum width. The whole diagram is reordered afterwards, or at the end of the
     * current transaction.
     *
     * @param minWidth the new minimum width
     */
    public void setMinWidth(double minWidth) {
        this.userMinWidth = minWidth;
        this.minWidth = Math.max(this.minWidth, userMinWidth);
        if (userMinWidth > getWidth()) {
            resize(new Rectangle2D.Double(left - (userMinWidth - getWidth()) / 2, top, userMinWidth, bottom - top));
        }
    }

    /**
     * Sets the minimum size of the box.
     *
     * @param width the new minimum width
     * @param height the new minimum height
     */
    public void setMinSize(double width, double height) {
        userMinHeight = height;
        minHeight = Math.max(minHeight, userMinHeight);
        userMinWidth = width;
        minWidth = Math.max(minWidth, userMinWidth);
        double oldWidth = getWidth();
        double oldHeight = getHeight();
        double newLeft = userMinWidth > oldWidth ? left - (userMinWidth - oldWidth) / 2 : left;
        double newTop = userMinHeight > oldHeight ? top - (userMinHeight - oldHeight) / 2 : top;
        double newWidth = userMinWidth > oldWidth ? userMinWidth : oldWidth;
        double newHeight = userMinHeight > oldHeight ? userMinHeight : oldHeight;
        if (userMinHeight > oldHeight || userMinWidth > oldWidth) {
            resize(new Rectangle2D.Double(newLeft, newTop, newWidth, newHeight));
        }
    }

    @Override
    public ArrayList<Element> getChildren() {
        if (hide) {
            return new ArrayList<>();
        } else {
            ArrayList<Element> children = super.getChildren();
            children.addAll(outsideLabels);
            return children;
        }
    }

    @Override
    public ArrayList<Line> getIncidentLines() {
        if (hide) {
            return new ArrayList<>();
        }
        return new ArrayList<>(incidentLines);
    }

    /**
     * Returns the outside labels that belong to this box.
     *
     * @return the outside labels that belong to this box.
     */
    public ArrayList<BoxOutsideLabel> getOutsideLabels() {
        return new ArrayList<>(outsideLabels);
    }
    //</editor-fold>

    /**
     * Resizes the box, moving it from its current position to the given rectangle, pushing aside
     * other elements as needed. Changes the minimum size of the box to the given size. The actual
     * size may differ due to children. The whole diagram is reordered afterwards, or at the end of
     * the current transaction.
     *
     * @param rectangle the new position of the box to resize it to
     */
    public void resize(Rectangle2D.Double rectangle) {
        double minLength = 10 * getDiagram().getEpsilon();
        if (rectangle.width < minLength) {
            rectangle.width = minLength;
        }
        if (rectangle.height < minLength) {
            rectangle.height = minLength;
        }

        Transaction.Operation.RectangleResizeOperation.prepareTransaction(getDiagram());

        userMinHeight = rectangle.height;
        userMinWidth = rectangle.width;

        getDiagram().layoutResizeRectangle(this, rectangle);
    }

    /**
     * Moves the box, taking it from its current position and placing it at the given position,
     * along with its children. The actual size may differ due to children. The new owner of the box
     * will be stretched to accommodate the box at its desired new position. The whole diagram is
     * reordered afterwards, or at the end of the current transaction.
     *
     * @param newCenter the new desired center of the box
     * @param newOwner the new owner of the box
     * @param growPoint the exact point at which to insert the box, it will then be grow from that
     * point to its desired position as per {@link #resize(java.awt.geom.Rectangle2D.Double)}
     * @param row the row of the box in new owner's grid
     * @param column the column of the box in new owner's grid
     */
    public void move(Point2D.Double newCenter, Container newOwner, Point2D.Double growPoint, Integer row, Integer column) {
        if (newOwner.getLayoutConstraints().getType() == ConstraintType.GRID) {
            GridLayoutConstraints constraints = (GridLayoutConstraints) newOwner.getLayoutConstraints();
            constraints._setCell(this, row, column);
            move(newCenter, newOwner, growPoint);
        }
    }

    /**
     * Moves the box, taking it from its current position and placing it at the given position,
     * along with its children. The actual size may differ due to children. The new owner of the box
     * will be stretched to accommodate the box at its desired new position. The whole diagram is
     * reordered afterwards, or at the end of the current transaction.
     *
     * @param newCenter the new desired center of the box
     * @param newOwner the new owner of the box
     * @param growPoint the exact point at which to insert the box, it will then be grow from that
     * point to its desired position as per {@link #resize(java.awt.geom.Rectangle2D.Double)
     * }
     */
    public void move(Point2D.Double newCenter, Container newOwner, Point2D.Double growPoint) {
        if (newOwner == this || newOwner.isDescendantOf(this)) {
            throw new IllegalArgumentException("A box cannot be moved into itself.");
        }

        Diagram newDiagram = newOwner.getDiagram();
        Diagram oldDiagram = getDiagram();
        if (newDiagram != oldDiagram) {
            Transaction.Operation.AdjustOperation.prepareTransaction(oldDiagram, true);
        }
        Transaction.Operation.PlaceRectangleOperation.prepareTransaction(newDiagram, newOwner.getPrevRect(true));

        /*
         * Finds the lines to retrace after box moving.
         */
        LinkedHashSet<Line> linesToRetrace = new LinkedHashSet<>(incidentLines);
        for (Box box : getDescendantBoxes()) {
            linesToRetrace.addAll(box.incidentLines);
        }

     if (newOwner != getOwner()) { // by SK
        /*
         * Removes lines from old owners.
         */
        for (Line line : linesToRetrace) {
            line.getOwner().removeChild(line);
        }

        /*
         * Changes box owner.
         */
        newOwner.addBox(this);
        if (newOwner != getOwner()) {
            getOwner().removeChild(this);
            setOwner(newOwner);
            if (newDiagram != oldDiagram) {
                setDiagram(newDiagram);
                for (Element element : getDescendants()) {
                    element.setDiagram(newDiagram);
                }
            }
        }

        /*
         * Sets new line owners.
         */
        for (Line line : linesToRetrace) {
            line.setOwner(Line.findOwner((Connectible) line.getStart(), (Connectible) line.getEnd()));
            Container lineOwner = line.getOwner();
            if (lineOwner != null) {
                lineOwner.addLine(line);
                if (newDiagram != oldDiagram) {
                    line.setDiagram(newDiagram);
                    for (Element element : line.getDescendants()) {
                        element.setDiagram(newDiagram);
                    }
                }
            }
        }
     } // by SK
        /*
         * Adjusts layout.
         */
        newDiagram.layoutMoveRectangle(this, newCenter, growPoint);
        if (newDiagram != oldDiagram) {
            oldDiagram.layoutAdjust();
        }
    }

    /**
     * Moves the box by the given vector, along with its children. The actual size may differ due to
     * children. The new owner of the box will be stretched to accommodate the box at its desired
     * new position. The whole diagram is reordered afterwards, or at the end of the current
     * transaction.
     *
     * @param moveVector the move vector for the box
     * @param newOwner the new owner of the box
     * @param row the row of the box in new owner's grid
     * @param column the column of the box in new owner's grid
     */
    @Override
    public void move(Point2D.Double moveVector, Container newOwner, Integer row, Integer column) {
        Point2D.Double newCenter = getCenter();
        newCenter.x += moveVector.x;
        newCenter.y += moveVector.y;
        move(newCenter, newOwner, newCenter, row, column);
    }

    /**
     * Moves the box by the given vector, along with its children. The actual size may differ due to
     * children. The new owner of the box will be stretched to accommodate the box at its desired
     * new position. The whole diagram is reordered afterwards, or at the end of the current
     * transaction.
     *
     * @param moveVector the move vector for the box
     * @param newOwner the new owner of the box
     */
    @Override
    public void move(Point2D.Double moveVector, Container newOwner) {
        Point2D.Double newCenter = getCenter();
        newCenter.x += moveVector.x;
        newCenter.y += moveVector.y;
        move(newCenter, newOwner, newCenter);
    }

    /**
     * Finds the the least common ancestor box of this and the given box.
     *
     * @param box the box with which to find the LCA box.
     * @return the the least common ancestor box of this and the given box
     */
    Box findLCABox(Box box) {
        Container lcaContainer = findLCA(box);
        if (!(lcaContainer instanceof Box)) {
            lcaContainer = lcaContainer.getPrevBox();
        }
        return (Box) lcaContainer;
    }

    /**
     * Returns the bounding rectangle of the box.
     *
     * @return the bounding rectangle of the box.
     */
    @Override
    public Path2D.Double getShape() {
        Path2D.Double path = new Path2D.Double(Path2D.WIND_EVEN_ODD, 5);
        path.moveTo(left, top);
        path.lineTo(right, top);
        path.lineTo(right, bottom);
        path.lineTo(left, bottom);
        path.closePath();
        return path;
    }

    /**
     * Creates a new outside label for this box. The position and the box side for the new label is
     * determined by the given point, which is moved to the box perimeter if necessary. The minimum
     * size of the new label is the size of the given rectangle. The whole diagram is reordered
     * afterwards, or at the end of the current transaction.
     *
     * @param width the width for the new label
     * @param height the height for the new label
     * @param center the point from which to determine the position of the label relative to some
     * box side; the center of the new label will try to be as close to this point as possible
     * @param constraintType the type of the layout constraints for the new label
     * @param spacing the spacing value for the new label
     * @return the new inside label object.
     */
    public BoxOutsideLabel createOutsideLabel(double width, double height, Point2D.Double center, ConstraintType constraintType, double spacing) {
        Transaction.Operation.AdjustOperation.prepareTransaction(getDiagram());

        GeometryHelper.movePointToPerimeter(center, this);

        BoxSide side = BoxSide.findPointSide(center, center, this);
        double position;
        switch (side) {
            case LEFT:
            case RIGHT:
                position = (center.y - top) / getHeight();
                break;
            default:
                position = (center.x - left) / getWidth();
        }

        return createOutsideLabel(width, height, center, side, position, constraintType, spacing);
    }

    /**
     * Creates a new outside label for this box. The minimum size of the new label is the size of
     * the given rectangle. The whole diagram is reordered afterwards, or at the end of the current
     * transaction.
     *
     * @param width the width for the new label
     * @param height the height for the new label
     * @param side the box side for the new label
     * @param position the relative position on the box side for the new label
     * @param constraintType the type of the layout constraints for the new label
     * @param spacing the spacing value for the new label
     * @return the new inside label object.
     */
    public BoxOutsideLabel createOutsideLabel(double width, double height, BoxSide side, double position, ConstraintType constraintType, double spacing) {
        if (position < 0 || position > 1) {
            throw new IllegalArgumentException("Box outside label position should be between 0 and 1, inclusive.");
        }

        Transaction.Operation.AdjustOperation.prepareTransaction(getDiagram());

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

        return createOutsideLabel(width, height, new Point2D.Double(x, y), side, position, constraintType, spacing);
    }

    /**
     * Creates a new outside label for this box. The minimum size of the new label is the size of
     * the given rectangle. The whole diagram is reordered afterwards, or at the end of the current
     * transaction.
     *
     * @param width the width for the new label
     * @param height the height for the new label
     * @param point the point on the perimeter of the box which denotes where to insert the label
     * @param side the box side for the new label
     * @param position the relative position on the box side for the new label
     * @param constraintType the type of the layout constraints for the new label
     * @param spacing the spacing value for the new label
     * @return the new inside label object.
     */
    BoxOutsideLabel createOutsideLabel(double width, double height, Point2D.Double point, BoxSide side, double position, ConstraintType constraintType, double spacing) {
        BoxOutsideLabel label = new BoxOutsideLabel(point, width, height, this, side, position, constraintType, spacing);
        outsideLabels.add(label);

        getDiagram().layoutAdjustOutsideLabel(label);

        return label;
    }

    @Override
    public Line connectTo(Connectible element, LineType lineType, double spacing) {
        if (element instanceof Box) {
            Box box = (Box) element;
            LineOperation.NewLineOperation.prepareTransaction(getDiagram(), this, box);

            Container lineOwner = Line.findOwner(this, element);
            Line line = new Line(this, box, lineOwner, spacing);
            lineOwner.addLine(line);
            incidentLines.add(line);

            ((Box) element).incidentLines.add(line);
            getDiagram().layoutTraceLine(line, lineType);
            return line;
        } else {
            throw new UnsupportedOperationException("Lines conneting to lines not implemented yet");
        }
    }

    @Override
    public Line connectTo(Connectible element, LineType lineType, double spacing, ArrayList<Point2D.Double> points) {
        return connectTo(element, lineType, spacing, points, false);
    }

    @Override
    public Line connectTo(Connectible element, LineType lineType, double spacing, ArrayList<Point2D.Double> points, boolean cleanup) {
        if (element instanceof Box) {
            Box box = (Box) element;
            LineOperation.SetPointsOperation.prepareTransaction(getDiagram(), this, box);

            Container lineOwner = Line.findOwner(this, element);
            Line line = new Line(this, box, lineOwner, spacing);
            lineOwner.addLine(line);
            incidentLines.add(line);

            ((Box) element).incidentLines.add(line);
            try { // try-catch by SK
            	getDiagram().layoutSetLinePoints(line, lineType, points, cleanup);
            }
            catch(Throwable t) {
            	lineOwner.removeChild(line);
            	incidentLines.remove(line);
            	((Box) element).incidentLines.remove(line);
            	throw t;
            }
            return line;
        } else {
            throw new UnsupportedOperationException("Lines conneting to lines not implemented yet");
        }
    }

    /**
     * Adds a label as a child of this box. Only affects the element hierarchy.
     *
     * @param label the label to add as a child
     */
    void addOutsideLabel(BoxOutsideLabel label) {
        outsideLabels.add(label);
    }

    @Override
    public void remove() {
        if (getDiagram() == null) {
            return;
        }

        Transaction.Operation.AdjustOperation.prepareTransaction(getDiagram());

        LinkedHashSet<Line> tIncidentLines = new LinkedHashSet<>(incidentLines);
        for (Box box : getDescendantBoxes()) {
            tIncidentLines.addAll(box.incidentLines);
        }
        for (Line line : tIncidentLines) {
            line.remove(false);
        }

        super.remove();
    }

    @Override
    void removeChild(Element element) {
        if (element instanceof BoxOutsideLabel) {
            outsideLabels.remove((BoxOutsideLabel) element);
        } else {
            super.removeChild(element);
        }
    }

    /**
     * Stores the side of a box that a points lies on.
     */
    public static enum BoxSide {

        /**
         * The top side of a box.
         */
        TOP,
        /**
         * The right side of a box.
         */
        RIGHT,
        /**
         * The bottom side of a box.
         */
        BOTTOM,
        /**
         * The left side of a box.
         */
        LEFT;
        /**
         * A bit mask corresponding to the side of a box, with 0b0001 corresponding to top, 0b0010
         * to right, 0b0100 to bottom and 0b1000 to left.
         */
        final int mask;

        /**
         * Constructs a new {@code BoxSide}.
         */
        BoxSide() {
            mask = 1 << ordinal();
        }

        /**
         * Finds which side of the given box the given point lies on. If the given point lies on a
         * corner of the box, choose the side perpendicular to the line between {@code point} and
         * {@code prevPoint}.
         *
         * @param point the point to check
         * @param prevPoint a point that should share a coordinate with the point to check
         * @param box the box to check
         * @return the side of the given box the given point lies on
         */
        static BoxSide findPointSide(Point2D.Double point, Point2D.Double prevPoint, Box box) {
            if (!GeometryHelper.isPointOnPerimeter(box, point)) {
                throw new IllegalArgumentException("Point must lie on the perimeter of the box "+box.getLeft()+" "+box.getRight()+" "+box.getTop()+" "+box.getBottom()+" pnt: "+point.getX()+" "+point.getY());
            }
            boolean onLeft = point.x == box.left,
                    onRight = point.x == box.right,
                    onTop = point.y == box.top,
                    onBottom = point.y == box.bottom;

            /**
             * If the point is not on a corner, there is no ambiguity.
             */
            if (onLeft && !(onTop || onBottom)) {
                return BoxSide.LEFT;
            }
            if (onRight && !(onTop || onBottom)) {
                return BoxSide.RIGHT;
            }
            if (onTop && !(onLeft || onRight)) {
                return BoxSide.TOP;
            }
            if (onBottom && !(onLeft || onRight)) {
                return BoxSide.BOTTOM;
            }

            /**
             * If the point lies on a corner, use the side perpendicular to the line between point
             * and prevPoint.
             */
            if (point.x == prevPoint.x) {
                if (onTop) {
                    return BoxSide.TOP;
                } else {
                    return BoxSide.BOTTOM;
                }
            } else {
                if (onLeft) {
                    return BoxSide.LEFT;
                } else {
                    return BoxSide.RIGHT;
                }
            }
        }

        /**
         * Returns the coordinate of the given boxes given side.
         *
         * @param box the box to check
         * @param side the side whose coordinate to find
         * @return the coordinate of the given boxes given side.
         */
        static double getCoordinate(Box box, BoxSide side) {
            if (side == LEFT) {
                return box.getLeft();
            } else if (side == RIGHT) {
                return box.getRight();
            } else if (side == TOP) {
                return box.getTop();
            } else {
                return box.getBottom();
            }
        }

        /**
         * Checks whether the side is horizontal.
         *
         * @return whether the side is horizontal.
         */
        boolean isHorizontal() {
            return this == TOP || this == BOTTOM;
        }
    }

    @Override
    void saveAttributes(org.w3c.dom.Element e, org.w3c.dom.Document doc, LinkedHashMap<Object, Integer> idMap) {
        super.saveAttributes(e, doc, idMap);
        for (BoxOutsideLabel label : outsideLabels) {
            e.appendChild(label.saveToXML(doc, idMap));
        }
    }

    @Override
    org.w3c.dom.Element saveToXML(org.w3c.dom.Document doc, LinkedHashMap<Object, Integer> idMap) {
        org.w3c.dom.Element e = doc.createElement(XMLHelper.NAMESPACE + ":box");
        saveAttributes(e, doc, idMap);
        return e;
    }

    /**
     * Creates a new Box from the given XML element, along with its children. Adds all of them
     * {@code objectMap}.
     *
     * @param e an XML element corresponding to a Box
     * @param objectMap a map from element storage IDs to diagram objects
     * @param owner the owner of the label
     */
    Box(org.w3c.dom.Element e, LinkedHashMap<Integer, Object> objectMap, Container owner) {
        super(e, objectMap, owner);
        incidentLines = new LinkedHashSet<>();

        outsideLabels = new LinkedHashSet<>();
        NodeList children = e.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child instanceof org.w3c.dom.Element) {
                org.w3c.dom.Element c = (org.w3c.dom.Element) child;
                String tag = c.getTagName();
                switch (tag) {
                    case XMLHelper.NAMESPACE + ":outsideLabel":
                        outsideLabels.add(new BoxOutsideLabel(c, objectMap, this));
                        break;
                }
            }
        }
    }
}
