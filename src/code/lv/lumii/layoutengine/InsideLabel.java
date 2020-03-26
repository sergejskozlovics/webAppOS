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
import lv.lumii.layoutengine.LayoutConstraints.*;

/**
 * This class describes labels that are contained inside some element.
 *
 * @author JK
 */
public class InsideLabel extends Label {

    /**
     * If this inside label is inside an outside label, points to that outside label. null
     * otherwise.
     */
    OutsideLabel outsideLabel;

    /**
     * Creates a new inside label instance.
     *
     * @param left the left side coordinate
     * @param right the right side coordinate
     * @param top the top side coordinate
     * @param bottom the bottom side coordinate
     * @param owner the owner of the label
     * @param constraintType the constraint type of the label
     * @param spacing the spacing of the label
     */
    InsideLabel(double left, double right, double top, double bottom, AbstractContainer owner, ConstraintType constraintType, double spacing) {
        super(left, right, top, bottom, owner, constraintType, spacing);
        if (owner instanceof OutsideLabel) {
            outsideLabel = (OutsideLabel) owner;
        } else if (owner instanceof InsideLabel) {
            outsideLabel = ((InsideLabel) owner).outsideLabel;
        }
    }

    @Override
    public AbstractContainer getOwner() {
        return (AbstractContainer) owner;
    }

    @Override
    public InsideLabel createInsideLabel(Rectangle2D.Double rectangle,
            LayoutConstraints.ConstraintType constraintType,
            double spacing,
            Integer row, Integer column) {
        if (outsideLabel == null) {
            return super.createInsideLabel(rectangle, constraintType, spacing, row, column);
        } else {
            Transaction.Operation.ModifyLabelOperation.prepareTransaction(getDiagram());

            InsideLabel label = new InsideLabel(rectangle.getMinX(), rectangle.getMaxX(), rectangle.getMinY(), rectangle.getMaxY(), this, constraintType, spacing);
            insideLabels.add(label);

            if (getLayoutConstraints().getType() == LayoutConstraints.ConstraintType.GRID) {
                LayoutConstraints.GridLayoutConstraints constraints = (LayoutConstraints.GridLayoutConstraints) getLayoutConstraints();
                constraints._setCell(label, row, column);
            }

            getDiagram().layoutModifyLabel(outsideLabel);

            return label;
        }
    }

    /**
     * Moves the label, taking it from its current position and placing it at the given position,
     * along with its children. The actual size may differ due to children. The new owner of the
     * label will be stretched to accommodate the label at its desired new position. The whole
     * diagram is reordered afterwards, or at the end of the current transaction.
     *
     * @param newCenter the new desired center of the label
     * @param newOwner the new owner of the label
     * @param growPoint the exact point at which to insert the label, it will then be grow from that
     * point to its desired position as per {@link #resize(java.awt.geom.Rectangle2D.Double)}
     * @param row the row of the label in new owner's grid
     * @param column the column of the label in new owner's grid
     */
    public void move(Point2D.Double newCenter, AbstractContainer newOwner, Point2D.Double growPoint, Integer row, Integer column) {
        if (newOwner.getLayoutConstraints().getType() == ConstraintType.GRID) {
            GridLayoutConstraints constraints = (GridLayoutConstraints) newOwner.getLayoutConstraints();
            constraints._setCell(this, row, column);
            move(newCenter, newOwner, growPoint);
        }
    }

    /**
     * Moves the label, taking it from its current position and placing it at the given position,
     * along with its children. The actual size may differ due to children. The new owner of the
     * label will be stretched to accommodate the label at its desired new position. The whole
     * diagram is reordered afterwards, or at the end of the current transaction.
     *
     * @param newCenter the new desired center of the label
     * @param newOwner the new owner of the label
     * @param growPoint the exact point at which to insert the label, it will then be grow from that
     * point to its desired position as per {@link #resize(java.awt.geom.Rectangle2D.Double)
     * }
     */
    public void move(Point2D.Double newCenter, AbstractContainer newOwner, Point2D.Double growPoint) {
        if (newOwner == this || newOwner.isDescendantOf(this)) {
            throw new IllegalArgumentException("A label cannot be moved into itself.");
        }

        Diagram newDiagram = newOwner.getDiagram();
        Diagram oldDiagram = getDiagram();
        if (newDiagram != oldDiagram) {
            Transaction.Operation.AdjustOperation.prepareTransaction(oldDiagram);
        }

        OutsideLabel oldOutsideLabel = outsideLabel;
        if (newOwner instanceof OutsideLabel || (newOwner instanceof InsideLabel && ((InsideLabel) newOwner).outsideLabel != null)) {
            if (newOwner instanceof OutsideLabel) {
                outsideLabel = (OutsideLabel) newOwner;
            } else {
                outsideLabel = ((InsideLabel) newOwner).outsideLabel;
            }
            Transaction.Operation.ModifyLabelOperation.prepareTransaction(newDiagram);
        } else {
            outsideLabel = null;
            Transaction.Operation.PlaceRectangleOperation.prepareTransaction(newDiagram, newOwner.getPrevRect(true));
        }

        if (outsideLabel != oldOutsideLabel) {
            for (AbstractContainer label : getDescendantRectangles(false, true)) {
                ((InsideLabel) label).outsideLabel = outsideLabel;
            }
        }

        /*
         * Changes label owner.
         */
        newOwner.addInsideLabel(this);
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
         * Adjusts layout.
         */
        if (outsideLabel == null) {
            newDiagram.layoutMoveRectangle(this, newCenter, growPoint);
        } else {
            newDiagram.layoutModifyLabel(outsideLabel);
        }
        if (newDiagram != oldDiagram) {
            oldDiagram.adjust();
        }
    }

    /**
     * Resizes the label, moving it from its current position to the given rectangle, pushing aside
     * other elements as needed. Changes the minimum size of the label to the given size. The actual
     * size may differ due to children. The whole diagram is reordered afterwards, or at the end of
     * the current transaction.
     *
     * @param rectangle the new position of the label to resize it to
     */
    @Override
    public void resize(Rectangle2D.Double rectangle) {
        double minLength = 10 * getDiagram().getEpsilon();
        if (rectangle.width < minLength) {
            rectangle.width = minLength;
        }
        if (rectangle.height < minLength) {
            rectangle.height = minLength;
        }

        if (outsideLabel == null) {
            Transaction.Operation.RectangleResizeOperation.prepareTransaction(getDiagram());
        } else {
            Transaction.Operation.ModifyLabelOperation.prepareTransaction(getDiagram());
        }

        userMinHeight = rectangle.height;
        userMinWidth = rectangle.width;

        if (outsideLabel == null) {
            getDiagram().layoutResizeRectangle(this, rectangle);
        } else {
            getDiagram().layoutModifyLabel(outsideLabel);
        }
    }

    /**
     * Sets the spacing value of the label. The whole diagram is reordered afterwards, or at the end
     * of the current transaction.
     *
     * @param spacing the new spacing value
     */
    @Override
    public void setSpacing(double spacing) {
        if (outsideLabel == null) {
            Transaction.Operation.SetSpacingOperation.prepareTransaction(getDiagram());
        } else {
            Transaction.Operation.ModifyLabelOperation.prepareTransaction(getDiagram());
        }

        if (outsideLabel == null) {
            getDiagram().layoutChangeRectangleSpacing(this, spacing);
        } else {
            getDiagram().layoutModifyLabel(outsideLabel);
        }
    }

    @Override
    void saveAttributes(org.w3c.dom.Element e, org.w3c.dom.Document doc, LinkedHashMap<Object, Integer> idMap) {
        super.saveAttributes(e, doc, idMap);
    }

    /**
     * Creates a new XML element corresponding to this inside label in the given document. Puts the
     * IDs of any new elements in {@code idMap}.
     *
     * @param doc the XML document in which to create the new element
     * @param idMap a map from diagram objects to element storage IDs.
     * @return a new element corresponding to this inside label
     */
    org.w3c.dom.Element saveToXML(org.w3c.dom.Document doc, LinkedHashMap<Object, Integer> idMap) {
        org.w3c.dom.Element e = doc.createElement(XMLHelper.NAMESPACE + ":insideLabel");
        saveAttributes(e, doc, idMap);
        return e;
    }

    /**
     * Creates a new InsideLabel from the given XML element, along with its children. Adds all of
     * them {@code objectMap}.
     *
     * @param e an XML element corresponding to a InsideLabel
     * @param objectMap a map from element storage IDs to diagram objects
     * @param owner the owner of this label
     */
    InsideLabel(org.w3c.dom.Element e, LinkedHashMap<Integer, Object> objectMap, AbstractContainer owner) {
        super(e, objectMap, owner);
        if (owner instanceof OutsideLabel) {
            outsideLabel = (OutsideLabel) owner;
        } else if (owner instanceof InsideLabel) {
            outsideLabel = ((InsideLabel) owner).outsideLabel;
        }
    }
}
