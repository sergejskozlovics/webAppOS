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
import java.util.LinkedHashMap;
import lv.lumii.layoutengine.Box.BoxSide;
import lv.lumii.layoutengine.LayoutConstraints.*;
import lv.lumii.layoutengine.OutsideLabel.BoxOutsideLabel;
import lv.lumii.layoutengine.OutsideLabel.LineLabel;
import lv.lumii.layoutengine.OutsideLabel.LineLabel.Orientation;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * The base class of all labels. Label can contain other labels.
 *
 * @author karlis
 */
public abstract class Label extends AbstractContainer implements Rectangular {

    //<editor-fold defaultstate="collapsed" desc="constructors">
    /**
     * Creates a new abstract label instance.
     *
     * @param left the left side coordinate
     * @param right the right side coordinate
     * @param top the top side coordinate
     * @param bottom the bottom side coordinate
     * @param owner the owner of the label
     * @param constraintType the constraint type for the label
     * @param spacing the spacing of the label
     */
    Label(double left, double right, double top, double bottom, Element owner, ConstraintType constraintType, double spacing) {
        super(left, right, top, bottom, owner, constraintType, spacing);
    }

    /**
     * Creates a new Label from the given XML element, along with its children. Adds all of them
     * {@code objectMap}.
     *
     * @param e an XML element corresponding to a Label
     * @param objectMap a map from element storage IDs to diagram objects
     * @param owner the owner of the label
     */
    Label(org.w3c.dom.Element e, LinkedHashMap<Integer, Object> objectMap, Element owner) {
        super(e, objectMap, owner);

        NodeList children = e.getChildNodes();
        org.w3c.dom.Element constraintElement = null;
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child instanceof org.w3c.dom.Element) {
                org.w3c.dom.Element c = (org.w3c.dom.Element) child;
                String tag = c.getTagName();
                switch (tag) {
                    case XMLHelper.NAMESPACE + ":layoutConstraints":
                        constraintElement = c;
                        break;
                }
            }
        }
        layoutConstraints = LayoutConstraints.loadFromXML(constraintElement, objectMap, this);
    }
    //</editor-fold>

    /**
     * Resizes the label, moving it from its current relative position to the position defined by
     * the given rectangle center point. Changes the minimum size of the label to the given size.
     * The actual size may differ due to children. The whole diagram is reordered afterwards, or at
     * the end of the current transaction.
     *
     * @param rectangle the new position of the label to resize it to
     */
    abstract void resize(Rectangle2D.Double rectangle);

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
     * Sets the minimum size of the label.
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

    /**
     * Converts this label to an inside label belonging to the new owner. Copies all parameters and
     * children. Does nothing if this label already is an inside label (not even changes the owner;
     * that will be handled by the move operation).
     *
     * @param newOwner the owner of the new inside label.
     * @return the new inside label, or this label if it already is an inside label
     */
    public InsideLabel _convertToInsideLabel(AbstractContainer newOwner) {
        if (this instanceof InsideLabel) {
            return (InsideLabel) this;
        }
        AbstractContainer tOwner = newOwner;
        if (tOwner == this || newOwner.isDescendantOf(this)) {
            throw new IllegalArgumentException("A label cannot be moved into itself.");
        }

        Diagram newDiagram = newOwner.getDiagram();
        Diagram oldDiagram = getDiagram();
        if (newDiagram != oldDiagram) {
            Transaction.Operation.AdjustOperation.prepareTransaction(oldDiagram);
        }

        if (newOwner instanceof OutsideLabel || (newOwner instanceof InsideLabel && ((InsideLabel) newOwner).outsideLabel != null)) {
            Transaction.Operation.ModifyLabelOperation.prepareTransaction(newDiagram);
        } else {
            Transaction.Operation.PlaceRectangleOperation.prepareTransaction(newDiagram, tOwner.getPrevRect(true));
        }

        InsideLabel label = new InsideLabel(left, right, top, bottom, newOwner, layoutConstraints.getType(), spacing);
        label.layoutConstraints = layoutConstraints;
        label.layoutConstraints.container = label;
        label.minWidth = minWidth;
        label.minHeight = minHeight;
        label.userMinWidth = userMinWidth;
        label.userMinHeight = userMinHeight;
        label.hide = hide;
        label.id = id;
        label.setDesiredCenter(getDesiredCenter());
        label.outsideLabel = null;

        label.insideLabels = insideLabels;
        for (InsideLabel child : insideLabels) {
            child.setOwner(label);
        }

        owner.removeChild(this);
        setDiagram(null);

        return label;
    }

    /**
     * Converts this label to an inside label (if it wasn't already), then places it at the given
     * position, along with its children. The actual size may differ due to children. The new owner
     * of the label will be stretched to accommodate the label at its desired new position. The
     * whole diagram is reordered afterwards, or at the end of the current transaction.
     *
     * @param newCenter the new desired center of the label
     * @param newOwner the new owner of the label
     * @param growPoint the exact point at which to insert the label, it will then be grow from that
     * point to its desired position as per {@link #resize(java.awt.geom.Rectangle2D.Double)}
     * @return the new inside label, or this label if it already is an inside label
     */
    public InsideLabel convertToInsideLabel(Point2D.Double newCenter, AbstractContainer newOwner, Point2D.Double growPoint) {
        if (owner instanceof Container && ((Container) owner).layoutConstraints instanceof GridLayoutConstraints) {
            GridLayoutConstraints ownerConstraints = (GridLayoutConstraints) ((Container) owner).layoutConstraints;
            Integer row = ownerConstraints.getRow(this);
            Integer column = ownerConstraints.getColumn(this);
            if (row != null || column != null) {
                return convertToInsideLabel(newCenter, newOwner, growPoint, row, column);
            }
        }
        InsideLabel label = _convertToInsideLabel(newOwner);
        label.move(newCenter, newOwner, growPoint);
        return label;
    }

    /**
     * Converts this label to an inside label (if it wasn't already), then places it at the given
     * position, along with its children. The actual size may differ due to children. The new owner
     * of the label will be stretched to accommodate the label at its desired new position. The
     * whole diagram is reordered afterwards, or at the end of the current transaction.
     *
     * @param newCenter the new desired center of the label
     * @param newOwner the new owner of the label
     * @param growPoint the exact point at which to insert the label, it will then be grow from that
     * point to its desired position as per {@link #resize(java.awt.geom.Rectangle2D.Double)}
     * @param row the row of the label in new owner's grid
     * @param column the column of the label in new owner's grid
     * @return the new inside label, or this label if it already is an inside label
     */
    public InsideLabel convertToInsideLabel(Point2D.Double newCenter, AbstractContainer newOwner, Point2D.Double growPoint, Integer row, Integer column) {
        InsideLabel label = _convertToInsideLabel(newOwner);
        label.move(newCenter, newOwner, growPoint, row, column);
        return label;
    }

    /**
     * Converts this label to a box outside label belonging to the new owner. Copies all parameters
     * and children. Does nothing if this label already is a box outside label (not even changes the
     * owner; that will be handled by the move operation).
     *
     * @param newOwner the owner of the new box outside label.
     * @return the new box outside label, or this label if it already is a box outside label
     */
    public BoxOutsideLabel _convertToBoxOutsideLabel(Box newOwner) {
        if (this instanceof BoxOutsideLabel) {
            return (BoxOutsideLabel) this;
        }

        Diagram newDiagram = newOwner.getDiagram();
        Diagram oldDiagram = getDiagram();
        Transaction.Operation.AdjustOperation.prepareTransaction(newDiagram);
        if (newDiagram != oldDiagram) {
            Transaction.Operation.AdjustOperation.prepareTransaction(oldDiagram);
        }

        BoxOutsideLabel label = new BoxOutsideLabel(getCenter(), getWidth(), getHeight(), newOwner, BoxSide.LEFT, 0, layoutConstraints.getType(), spacing);
        label.layoutConstraints = layoutConstraints;
        label.layoutConstraints.container = label;
        label.minWidth = minWidth;
        label.minHeight = minHeight;
        label.userMinWidth = userMinWidth;
        label.userMinHeight = userMinHeight;
        label.hide = hide;
        label.id = id;
        label.setDesiredCenter(getDesiredCenter());
        if (this instanceof LineLabel) {
            LineLabel thisLineLabel = (LineLabel) this;
            label.flipped = thisLineLabel.flipped;
            label.autoFlip = thisLineLabel.autoFlip;
            if (thisLineLabel.segmentHorizontal) {
                label.side = BoxSide.TOP;
            }
        }

        label.insideLabels = insideLabels;
        for (InsideLabel child : insideLabels) {
            child.setOwner(label);
        }

        owner.removeChild(this);
        setDiagram(null);

        return label;
    }

    /**
     * Converts this label to a box outside label (if it wasn't already), then moves this box
     * outside label to the given point onto the given box. The point is moved to the perimeter of
     * the box if necessary. This operation can change this label's box side.
     *
     * @param point the point to move this label to. If this point is not on the perimeter of this
     * label's box, it is moved there.
     * @param box the new box owner of this label
     * @return the new box outside label, or this label if it already is a box outside label
     */
    public BoxOutsideLabel convertToBoxOutsideLabel(Point2D.Double point, Box box) {
        BoxOutsideLabel label = _convertToBoxOutsideLabel(box);
        label.move(point, box);
        return label;
    }

    /**
     * Converts this label to a box outside label (if it wasn't already), then moves this label to
     * the given side of the given box at the given position.
     *
     * @param side the side of this labels box to move it to
     * @param position the position on this side to place the label at, as a proportion of the whole
     * length of the side. Must be between 0 and 1, inclusive.
     * @param box the new box owner of this label
     * @return the new box outside label, or this label if it already is a box outside label
     */
    public BoxOutsideLabel convertToBoxOutsideLabel(BoxSide side, double position, Box box) {
        BoxOutsideLabel label = _convertToBoxOutsideLabel(box);
        label.move(side, position, box);
        return label;
    }

    /**
     * Converts this label to a line label with the given orientation belonging to the new owner.
     * Copies all parameters and children. Only changes the orientation if this label already is a
     * line label (but doesn't change the owner; that will be handled by the move operation).
     *
     * @param newOwner the new line of the new line label
     * @param orientation the orientation of the new line label
     * @return the new line label, or this label if it already is a line label
     */
    LineLabel _convertToLineLabel(Line newOwner, Orientation orientation) {
        if (this instanceof LineLabel) {
            LineLabel thisLineLabel = (LineLabel) this;
            thisLineLabel.orientation = orientation;
            return thisLineLabel;
        }

        Diagram newDiagram = newOwner.getDiagram();
        Diagram oldDiagram = getDiagram();
        Transaction.Operation.AdjustOperation.prepareTransaction(newDiagram);
        if (newDiagram != oldDiagram) {
            Transaction.Operation.AdjustOperation.prepareTransaction(oldDiagram);
        }

        LineLabel label = new LineLabel(getCenter(), 0, 0, orientation, getWidth(), getHeight(), newOwner, layoutConstraints.getType(), spacing);
        label.layoutConstraints = layoutConstraints;
        label.layoutConstraints.container = label;
        label.minWidth = minWidth;
        label.minHeight = minHeight;
        label.userMinWidth = userMinWidth;
        label.userMinHeight = userMinHeight;
        label.hide = hide;
        label.id = id;
        label.setDesiredCenter(getDesiredCenter());
        if (this instanceof BoxOutsideLabel) {
            BoxOutsideLabel thisBoxLabel = (BoxOutsideLabel) this;
            label.flipped = thisBoxLabel.flipped;
            label.autoFlip = thisBoxLabel.autoFlip;
            label.segmentHorizontal = thisBoxLabel.side.isHorizontal();
        }

        label.insideLabels = insideLabels;
        for (InsideLabel child : insideLabels) {
            child.setOwner(label);
        }

        owner.removeChild(this);
        setDiagram(null);

        return label;
    }

    /**
     * Converts this label to a line label (if it wasn't already), then moves this label to the
     * given point on the given line. If the point is not on the line, first moves the point onto
     * the line.
     *
     * @param point the point where to move this label
     * @param newOwner the line on which to place this label
     * @param orientation the orientation of the new line label
     * @return the new line label, or this label if it already is a line label
     */
    public LineLabel convertToLineLabel(Point2D.Double point, Line newOwner, Orientation orientation) {
        LineLabel label = _convertToLineLabel(newOwner, orientation);
        label.move(point, newOwner);
        return label;
    }

    /**
     * Converts this label to a line label (if it wasn't already), then moves this label to the
     * given relative position on the given line.
     *
     * @param position the relative position where to place this label on the line (should be
     * between 0 and 1, inclusive)
     * @param newOwner the line on which to place this label
     * @param orientation the orientation of the new line label
     * @return the new line label, or this label if it already is a line label
     */
    public LineLabel convertToLineLabel(double position, Line newOwner, Orientation orientation) {
        LineLabel label = _convertToLineLabel(newOwner, orientation);
        label.move(position, newOwner);
        return label;
    }
}
