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
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import lv.lumii.layoutengine.LayoutConstraints.ConstraintType;
import lv.lumii.layoutengine.LayoutConstraints.*;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * The {@code AbstractContainer} class specifies a group of diagram elements, though it allows only
 * labels to be added to the group.
 *
 * @author k
 */
public abstract class AbstractContainer extends Element {

    //<editor-fold defaultstate="collapsed" desc="attributes">
    /**
     * The left abscissa of the rectangle.
     */
    double left;
    /**
     * The right abscissa of the rectangle.
     */
    double right;
    /**
     * The top ordinate of the rectangle.
     */
    double top;
    /**
     * The bottom ordinate of the rectangle.
     */
    double bottom;
    /**
     * The current minimum width of the rectangle.
     */
    double minWidth;
    /**
     * The current minimum height of the rectangle.
     */
    double minHeight;
    /**
     * The current user-set minimum width of the rectangle, the actual minimum width can never be
     * lower.
     */
    double userMinWidth;
    /**
     * The current user-set minimum height of the rectangle, the actual minimum height can never be
     * lower.
     */
    double userMinHeight;
    /**
     * Whether to hide the container's children.
     */
    protected boolean hide = false;
    /**
     * A static counter for abstract container IDs.
     */
    static private int nextId = 0;
    /**
     * The internal creation ID of the current abstract container, used to resolve placement
     * conflicts deterministically for otherwise identical abstract containers.
     */
    int id;
    /**
     * The desired center position of the container rectangle.
     */
    private Point2D.Double desiredCenter;
    /**
     * The labels inside this element.
     */
    LinkedHashSet<InsideLabel> insideLabels;
    /**
     * The layout description and properties of this container.
     */
    LayoutConstraints layoutConstraints;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="constructors">
    /**
     * Creates a new AbstractContainer, used by subclasses.
     *
     * @param left the left side coordinate
     * @param right the right side coordinate
     * @param top the top side coordinate
     * @param bottom the bottom side coordinate
     * @param owner the owner of the new container
     * @param constraintType the constraint type for this container
     * @param spacing the spacing of the container
     */
    AbstractContainer(double left, double right, double top, double bottom, Element owner, ConstraintType constraintType, double spacing) {
        super(owner, spacing);
        double epsilon = getDiagram().getEpsilon();
        right = Math.max(right, left + 10 * epsilon);
        bottom = Math.max(bottom, top + 10 * epsilon);
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
        userMinWidth = right - left;
        userMinHeight = bottom - top;

        id = nextId++;
        desiredCenter = new Point2D.Double();

        insideLabels = new LinkedHashSet<>();

        setCurrentMinSize(userMinWidth, userMinHeight);

        switch (constraintType) {
            case NONE:
                layoutConstraints = new NoneLayoutConstraints(this);
                break;
            case GRID:
                layoutConstraints = new GridLayoutConstraints(this);
                break;
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="accessors">
    /**
     * Returns the bounding rectangle of the container.
     *
     * @return the bounding rectangle of the container.
     */
    public Rectangle2D.Double getBounds() {
        return new Rectangle2D.Double(left, top, right - left, bottom - top);
    }

    /**
     * Returns the center X of the bounding rectangle
     *
     * @return the center X of the bounding rectangle
     */
    public double getCenterX() {
        return (left + right) / 2;
    }

    /**
     * Returns the center Y of the bounding rectangle
     *
     * @return the center Y of the bounding rectangle
     */
    public double getCenterY() {
        return (top + bottom) / 2;
    }

    /**
     * Sets the bounds of the container rectangle. Note that this method can produce an incorrect
     * diagram.
     *
     * @param bounds the new bounds of the container
     */
    final void setBounds(Rectangle2D.Double bounds) {
        left = bounds.getMinX();
        right = bounds.getMaxX();
        top = bounds.getMinY();
        bottom = bounds.getMaxY();
    }

    /**
     * Sets the center of the container rectangle. Note that this method can produce an incorrect
     * diagram.
     *
     * @param newCenter the new center of the container
     */
    void setCenter(Point2D.Double newCenter) {
        Point2D.Double oldCenter = getCenter();
        transpose(new Point2D.Double(newCenter.x - oldCenter.x, newCenter.y - oldCenter.y));
    }

    /**
     * Returns the center point of the container rectangle.
     *
     * @return the center point of the container rectangle.
     */
    public final Point2D.Double getCenter() {
        return new Point2D.Double((left + right) / 2, (top + bottom) / 2);
    }

    /**
     * Returns the current internal minimum height of the container rectangle.
     *
     * @return the current internal minimum height of the container rectangle.
     */
    double getCurrentMinHeight() {
        return minHeight;
    }

    /**
     * Returns the minimum height of the container rectangle.
     *
     * @return the minimum height of the container rectangle.
     */
    public double getMinHeight() {
        return userMinHeight;
    }

    /**
     * Returns the current internal minimum width of the container rectangle.
     *
     * @return the current internal minimum width of the container rectangle.
     */
    double getCurrentMinWidth() {
        return minWidth;
    }

    /**
     * Returns the minimum width of the container rectangle.
     *
     * @return the minimum width of the container rectangle.
     */
    public double getMinWidth() {
        return userMinWidth;
    }

    /**
     * Sets the current internal minimum size of the container rectangle. This can never be lower
     * than the user specified minimum size stored in {@link #userMinWidth} and
     * {@link #userMinHeight}.
     *
     * @param width the new minimum width
     * @param height the new minimum height
     */
    final void setCurrentMinSize(double width, double height) {
        this.minWidth = Math.max(width, userMinWidth);
        this.minHeight = Math.max(height, userMinHeight);
    }

    /**
     * Returns the abscissa of the left side of the container rectangle.
     *
     * @return the abscissa of the left side of the container rectangle.
     */
    public double getLeft() {
        return left;
    }

    /**
     * Returns the ordinate of the top side of the container rectangle.
     *
     * @return the ordinate of the top side of the container rectangle.
     */
    public double getTop() {
        return top;
    }

    /**
     * Returns the abscissa of the right side of the container rectangle.
     *
     * @return the abscissa of the right side of the container rectangle.
     */
    public double getRight() {
        return right;
    }

    /**
     * Returns the ordinate of the bottom side of the container rectangle.
     *
     * @return the ordinate of the bottom side of the container rectangle.
     */
    public double getBottom() {
        return bottom;
    }

    /**
     * Returns the current width of the container rectangle.
     *
     * @return the current width of the container rectangle.
     */
    public double getWidth() {
        return right - left;
    }

    /**
     * Returns the current height of the container rectangle.
     *
     * @return the current height of the container rectangle.
     */
    public double getHeight() {
        return bottom - top;
    }

    /**
     * Sets the desired center for this container rectangle.
     *
     * @param desiredCenter the new desired center for this container rectangle
     */
    void setDesiredCenter(Point2D.Double desiredCenter) {
        this.desiredCenter = desiredCenter;
    }

    /**
     * Returns the desired center for this container rectangle.
     *
     * @return the desired center for this container rectangle
     */
    Point2D.Double getDesiredCenter() {
        return desiredCenter;
    }

    /**
     * Gets the layout manager of this container.
     *
     * @return the layout manager of this container.
     */
    public LayoutConstraints getLayoutConstraints() {
        if (hide) {
            return new NoneLayoutConstraints(this);
        } else {
            return layoutConstraints;
        }
    }
    

    /**
     * Sets the layout manager to the specified type.
     *
     * @param constraintType the new layout constraint type of this container
     */
    public final void setConstraintType(ConstraintType constraintType) {
        Transaction.Operation.AdjustOperation.prepareTransaction(getDiagram());

        switch (constraintType) {
            case NONE:
                layoutConstraints = new NoneLayoutConstraints(this);
                break;
            case GRID:
                layoutConstraints = new GridLayoutConstraints(this);
                break;
        }

        getDiagram().layoutAdjust();
    }
    //</editor-fold>

    /**
     * Transposes the container rectangle by the given vector. Does not affect any other elements,
     * including its children.
     *
     * @param vector the vector to transpose the container rectangle by
     */
    void transpose(Point2D.Double vector) {
        left += vector.x;
        right += vector.x;
        top += vector.y;
        bottom += vector.y;
    }

    /**
     * Collapses the container rectangle to the given point.
     *
     * @param newCenter the point to collapse the container rectangle to
     */
    void collapse(Point2D.Double newCenter) {
        setBounds(new Rectangle2D.Double(newCenter.x, newCenter.y, 0, 0));
    }

    /**
     * Finds whether the point lies inside the bounds of the container rectangle, including sides.
     * Use {@link #strictContains(java.awt.geom.Point2D.Double)} to exclude sides.
     *
     * @param point the point to check
     * @param epsilon the width of a buffer around the container rectangle points in which are also
     * considered to be contained by it
     * @return whether the point lies inside the bounds of the container rectangle.
     */
    public boolean contains(Point2D.Double point, double epsilon) {
        return left - epsilon <= point.getX() && point.getX() <= right + epsilon
                && top - epsilon <= point.getY() && point.getY() <= bottom + epsilon;
    }

    /**
     * Finds whether the point lies inside the bounds of the container rectangle, excluding
     * sides.Use {@link #contains(java.awt.geom.Point2D.Double, double) } to include sides.
     *
     * @param point the point to check
     * @return whether the point lies inside the bounds of the container rectangle.
     */
    boolean strictContains(Point2D.Double point) {
        return left < point.x && point.x < right
                && top < point.y && point.y < bottom;
    }

    /**
     * Finds whether the given rectangle lies inside the bounds of the container rectangle,
     * including sides.
     *
     * @param rectangle the rectangle to check
     * @param epsilon the width of a buffer around the container rectangle in which the given
     * rectangle is also considered to be contained inside
     * @return whether the given rectangle lies inside the bounds of the container rectangle.
     */
    boolean contains(AbstractContainer rectangle, double epsilon) {
        return left - epsilon <= rectangle.getLeft() && rectangle.getRight() <= right + epsilon
                && top - epsilon <= rectangle.getTop() && rectangle.getBottom() <= bottom + epsilon;
    }

    /**
     * Creates a new inside label as a child of this container. The minimum size of the new label is
     * the size of the given rectangle. The whole diagram is reordered afterwards, or at the end of
     * the current transaction.
     *
     * @param rectangle the position of the new label
     * @param spacing the spacing value for the new label
     * @return the new inside label object.
     */
    public InsideLabel createInsideLabel(Rectangle2D.Double rectangle,
            double spacing) {
        return createInsideLabel(rectangle, ConstraintType.NONE, spacing, null, null);
    }

    /**
     * Creates a new inside label as a child of this container. The minimum size of the new label is
     * the size of the given rectangle. The whole diagram is reordered afterwards, or at the end of
     * the current transaction.
     *
     * @param rectangle the position of the new label
     * @param constraintType the type of the layout constraints for the new label
     * @param spacing the spacing value for the new label
     * @return the new inside label object.
     */
    public InsideLabel createInsideLabel(Rectangle2D.Double rectangle,
            LayoutConstraints.ConstraintType constraintType,
            double spacing) {
        return createInsideLabel(rectangle, constraintType, spacing, null, null);
    }

    /**
     * Creates a new inside label as a child of this container. The minimum size of the new label is
     * the size of the given rectangle. The whole diagram is reordered afterwards, or at the end of
     * the current transaction.
     *
     * @param rectangle the position of the new label
     * @param constraintType the type of the layout constraints for the new label
     * @param spacing the spacing value for the new label
     * @param row the row of the new label in this container's grid
     * @param column the column of the new label in this container's grid
     * @return the new inside label object.
     */
    public InsideLabel createInsideLabel(Rectangle2D.Double rectangle,
            LayoutConstraints.ConstraintType constraintType,
            double spacing,
            Integer row, Integer column) {
        Transaction.Operation.PlaceRectangleOperation.prepareTransaction(getDiagram(), getPrevRect(true));

        InsideLabel label = new InsideLabel(rectangle.getMinX(), rectangle.getMaxX(), rectangle.getMinY(), rectangle.getMaxY(), this, constraintType, spacing);
        insideLabels.add(label);

        if (getLayoutConstraints().getType() == LayoutConstraints.ConstraintType.GRID) {
            LayoutConstraints.GridLayoutConstraints constraints = (LayoutConstraints.GridLayoutConstraints) getLayoutConstraints();
            constraints._setCell(label, row, column);
        }

        getDiagram().layoutPlaceRectangle(label, label.getCenter());

        return label;
    }

    /**
     * Adds a label as a child of this container. Only affects the element hierarchy.
     *
     * @param label the label to add as a child
     */
    void addInsideLabel(InsideLabel label) {
        insideLabels.add(label);
    }

    /**
     * Finds the smallest rectangle enclosing this containers grid and non-line children, including
     * their spacing.
     *
     * @return the smallest such rectangle
     */
    Rectangle2D.Double findGridEnclosingRectangle() {
        double tLeft = Double.POSITIVE_INFINITY, tTop = Double.POSITIVE_INFINITY,
                tRight = Double.NEGATIVE_INFINITY, tBottom = Double.NEGATIVE_INFINITY;
        boolean empty = true;

        LinkedList<AbstractContainer> queue = new LinkedList<>();
        queue.add(this);

        while (!queue.isEmpty()) {
            AbstractContainer container = queue.pop();

            for (AbstractContainer rect : container.getRectangles()) {
                empty = false;

                double tSpacing = rect.getSpacing();
                tLeft = Math.min(tLeft, rect.getLeft() - tSpacing);
                tTop = Math.min(tTop, rect.getTop() - tSpacing);
                tRight = Math.max(tRight, rect.getRight() + tSpacing);
                tBottom = Math.max(tBottom, rect.getBottom() + tSpacing);
            }

            if (container.layoutConstraints instanceof GridLayoutConstraints) {
                empty = false;

                GridLayoutConstraints constraints = (GridLayoutConstraints) container.layoutConstraints;
                int colCount = constraints.getColumnCount();
                if (colCount > 1) {
                    tLeft = Math.min(tLeft, constraints.getColumnRight(1) - constraints.getColumnMinWidth(1));
                    tRight = Math.max(tRight, constraints.getColumnLeft(colCount) + constraints.getColumnMinWidth(colCount));
                } else if (tLeft < tRight) {
                    double d = Math.max(0, (constraints.getColumnMinWidth(1) - (tRight - tLeft)) / 2);
                    tLeft -= d;
                    tRight += d;
                } else {
                    tLeft = 0;
                    tRight = constraints.getColumnMinWidth(1);
                }

                int rowCount = constraints.getRowCount();
                if (rowCount > 1) {
                    tTop = Math.min(tTop, constraints.getRowBottom(1) - constraints.getRowMinHeight(1));
                    tBottom = Math.max(tBottom, constraints.getRowTop(rowCount) + constraints.getRowMinHeight(rowCount));
                } else if (tTop < tBottom) {
                    double d = Math.max(0, (constraints.getRowMinHeight(1) - (tBottom - tTop)) / 2);
                    tTop -= d;
                    tBottom += d;
                } else {
                    tTop = 0;
                    tBottom = constraints.getRowMinHeight(1);
                }
            }

            for (Container c : container.getPureContainers()) {
                queue.add(c);
            }
        }

        if (empty) {
            tLeft = tRight = tTop = tBottom = 0;
        }

        return new Rectangle2D.Double(tLeft, tTop, tRight - tLeft, tBottom - tTop);
    }

    @Override
    ArrayList<Element> getChildren() {
        if (hide) {
            return new ArrayList<>();
        } else {
            return new ArrayList<Element>(insideLabels);
        }
    }

    /**
     * Returns the outside labels of this container.
     *
     * @return the outside labels of this container.
     */
    public ArrayList<InsideLabel> getInsideLabels() {
        if (hide) {
            return new ArrayList<>();
        }
        return new ArrayList<>(insideLabels);
    }

    /**
     * Returns the abstract containers that are children of this container. Includes labels,
     * containers and boxes.
     *
     * @param includeOutsideLabels whether to include outside labels
     * @return the abstract container children of this container.
     */
    ArrayList<AbstractContainer> getAbstractContainers(boolean includeOutsideLabels) {
        if (hide) {
            return new ArrayList<>();
        }
        return new ArrayList<AbstractContainer>(insideLabels);
    }

    /**
     * Returns the rectangular container obtained by going up in the element hierarchy until a
     * rectangular container is found, in effect skipping non-rectangular elements, starting from
     * this container's parent.
     *
     * @return a rectangular container that would be considered this rectangle's parent when
     * ignoring non-rectangular elements in between, or {@code null} if there isn't one.
     */
    AbstractContainer getPrevRect() {
        return getPrevRect(false);
    }

    /**
     * Returns the rectangular container obtained by going up in the element hierarchy until an
     * rectangular container is found, in effect skipping non-rectangular elements, starting from
     * this container or its parent.
     *
     * @param inclusive whether to start from this container.
     * @return a rectangular container that would be considered this container's parent when
     * ignoring non-rectangular elements in between, or {@code null} if there isn't one.
     */
    AbstractContainer getPrevRect(boolean inclusive) {
        if (inclusive && this instanceof Rectangular) {
            return this;
        }

        Element tOwner = getOwner();
        while (tOwner != null && !(tOwner instanceof Rectangular)) {
            tOwner = tOwner.getOwner();
        }
        return (AbstractContainer) tOwner;
    }

    /**
     * Returns the rectangular children of this container. Includes inside labels and boxes.
     *
     * @return the rectangular children of this container.
     */
    ArrayList<AbstractContainer> getRectangles() {
        if (hide) {
            return new ArrayList<>();
        }
        return new ArrayList<AbstractContainer>(insideLabels);
    }

    /**
     * Returns the set of rectangular containers (boxes and labels) obtained by going down in the
     * element hierarchy until a rectangle is found, in effect skipping non-rectangular elements.
     *
     * @param includeOutsideLabels whether to include outside labels
     * @return the set of rectangular containers that would be considered this box's children when
     * ignoring non-rectangular elements in between
     */
    ArrayList<AbstractContainer> getNextRectangles(boolean includeOutsideLabels) {
        if (hide) {
            return new ArrayList<>();
        }
        return new ArrayList<AbstractContainer>(insideLabels);
    }

    /**
     * Returns the set containing all descendant rectangles (boxes and labels) of the container.
     *
     * @param inclusive whether to include this container in the result if it is a rectangle
     * @param includeOutsideLabels whether to include outside labels (then includes also this
     * container's outside labels, if the container is a box)
     * @return the set containing all descendant rectangles of the container.
     */
    ArrayList<AbstractContainer> getDescendantRectangles(boolean inclusive, boolean includeOutsideLabels) {
        if (hide) {
            return new ArrayList<>();
        }
        ArrayList<AbstractContainer> descendants = new ArrayList<>();
        LinkedList<AbstractContainer> queue = new LinkedList<>(getAbstractContainers(includeOutsideLabels));
        while (!queue.isEmpty()) {
            AbstractContainer container = queue.pop();
            if (container instanceof Rectangular) {
                descendants.add(container);
            }
            queue.addAll(container.getAbstractContainers(includeOutsideLabels));
        }
        if (inclusive && this instanceof Rectangular) {
            descendants.add(this);
        }
        if (includeOutsideLabels && this instanceof Box && !(this instanceof Diagram)) {
            descendants.addAll(((Box) this).getOutsideLabels());
        }
        return descendants;
    }

    /**
     * Returns the set containing all descendant abstract containers of the container.
     *
     * @param includeOutsideLabels whether to include outside labels (then includes also this
     * container's outside labels, if the container is a box)
     * @return the set containing all descendant abstract containers of the container.
     */
    ArrayList<AbstractContainer> getDescendantAbstractContainers(boolean includeOutsideLabels) {
        if (hide) {
            return new ArrayList<>();
        }
        ArrayList<AbstractContainer> descendants = new ArrayList<>();
        LinkedList<AbstractContainer> queue = new LinkedList<>(getAbstractContainers(includeOutsideLabels));
        while (!queue.isEmpty()) {
            AbstractContainer container = queue.pop();
            descendants.add(container);
            queue.addAll(container.getAbstractContainers(includeOutsideLabels));
        }
        if (includeOutsideLabels && this instanceof Box && !(this instanceof Diagram)) {
            descendants.addAll(((Box) this).getOutsideLabels());
        }
        return descendants;
    }

    /**
     * Returns the child containers of this container that are not boxes.
     *
     * @return the child containers of this container that are not boxes.
     */
    ArrayList<Container> getPureContainers() {
        return new ArrayList<>();
    }

    /**
     * Finds the smallest rectangle containing all of the container's direct descendant rectangular
     * container, including the spacing of the rectangles.
     *
     * @return the smallest child rectangle enclosing rectangle, or {@code null} is the container
     * has no child rectangles
     */
    public Rectangle2D.Double findNextRectangleEnclosingRectangle() {
        ArrayList<AbstractContainer> nextRectangles = getNextRectangles(false);
        if (nextRectangles.isEmpty()) {
            return null;
        }
        double tLeft = Double.POSITIVE_INFINITY, tTop = Double.POSITIVE_INFINITY,
                tRight = Double.NEGATIVE_INFINITY, tBottom = Double.NEGATIVE_INFINITY;

        for (AbstractContainer rectangle : nextRectangles) {
            double tSpacing = rectangle.getSpacing();
            tLeft = Math.min(tLeft, rectangle.getLeft() - tSpacing);
            tTop = Math.min(tTop, rectangle.getTop() - tSpacing);
            tRight = Math.max(tRight, rectangle.getRight() + tSpacing);
            tBottom = Math.max(tBottom, rectangle.getBottom() + tSpacing);
        }

        return new Rectangle2D.Double(tLeft, tTop, tRight - tLeft, tBottom - tTop);
    }

    /**
     * Finds the smallest rectangle containing all of the container's children, including the
     * spacing of the children.
     *
     * @return the smallest children enclosing rectangle, or {@code null} is the container has no
     * children with dimensions
     */
    public Rectangle2D.Double findDescendantEnclosingRectangle() {
        ArrayList<AbstractContainer> rectangles = getNextRectangles(true);
        if (rectangles.isEmpty()) {
            return null;
        }
        double tLeft = Double.POSITIVE_INFINITY, tTop = Double.POSITIVE_INFINITY,
                tRight = Double.NEGATIVE_INFINITY, tBottom = Double.NEGATIVE_INFINITY;

        for (AbstractContainer rect : rectangles) {
            double tSpacing = rect.getSpacing();
            tLeft = Math.min(tLeft, rect.getLeft() - tSpacing);
            tTop = Math.min(tTop, rect.getTop() - tSpacing);
            tRight = Math.max(tRight, rect.getRight() + tSpacing);
            tBottom = Math.max(tBottom, rect.getBottom() + tSpacing);
        }

        if (layoutConstraints instanceof GridLayoutConstraints) {
            GridLayoutConstraints constraints = (GridLayoutConstraints) layoutConstraints;
            int colCount = constraints.getColumnCount();
            if (colCount > 1) {
                tLeft = Math.min(tLeft, constraints.getColumnRight(1) - constraints.getColumnMinWidth(1));
                tRight = Math.max(tRight, constraints.getColumnLeft(colCount) + constraints.getColumnMinWidth(colCount));
            } else if (tLeft < tRight) {
                double d = Math.max(0, (constraints.getColumnMinWidth(1) - (tRight - tLeft)) / 2);
                tLeft -= d;
                tRight += d;
            } else {
                tLeft = 0;
                tRight = constraints.getColumnMinWidth(1);
            }

            int rowCount = constraints.getRowCount();
            if (rowCount > 1) {
                tTop = Math.min(tTop, constraints.getRowBottom(1) - constraints.getRowMinHeight(1));
                tBottom = Math.max(tBottom, constraints.getRowTop(rowCount) + constraints.getRowMinHeight(rowCount));
            } else if (tTop < tBottom) {
                double d = Math.max(0, (constraints.getRowMinHeight(1) - (tBottom - tTop)) / 2);
                tTop -= d;
                tBottom += d;
            } else {
                tTop = 0;
                tBottom = constraints.getRowMinHeight(1);
            }
        }

        return new Rectangle2D.Double(tLeft, tTop, tRight - tLeft, tBottom - tTop);
    }

    /**
     * Sets this container to hide its children and incident lines, behaving as if it had none.
     */
    void hideChildren() {
        hide = true;
    }

    /**
     * Reverses the effects of {@link #hideChildren()}.
     */
    void showChildren() {
        hide = false;
    }

    @Override
    void removeChild(Element element) {
        if (element instanceof InsideLabel) {
            insideLabels.remove((InsideLabel) element);
        }
        if (layoutConstraints instanceof GridLayoutConstraints) {
            ((GridLayoutConstraints) layoutConstraints)._remove(element);
        }
    }

    @Override
    public void remove() {
        if (getDiagram() == null) {
            return;
        }

        Transaction.Operation.AdjustOperation.prepareTransaction(getDiagram());

        super.remove();
    }

    /**
     * Saves the attributes of this abstract container to the given XML element.
     *
     * @param e an XML element corresponding to this container
     * @param doc the XML document in which to create the new element
     * @param idMap a map from diagram elements to element ID's used for storage.
     */
    void saveAttributes(org.w3c.dom.Element e, org.w3c.dom.Document doc, LinkedHashMap<Object, Integer> idMap) {
        super.saveAttributes(e, idMap);
        e.setAttribute("left", Double.toString(left));
        e.setAttribute("right", Double.toString(right));
        e.setAttribute("top", Double.toString(top));
        e.setAttribute("bottom", Double.toString(bottom));
        e.setAttribute("minWidth", Double.toString(minWidth));
        e.setAttribute("minHeight", Double.toString(minHeight));
        e.setAttribute("userMinWidth", Double.toString(userMinWidth));
        e.setAttribute("userMinHeight", Double.toString(userMinHeight));
        e.setAttribute("boxId", Integer.toString(id));
        for (InsideLabel label : insideLabels) {
            e.appendChild(label.saveToXML(doc, idMap));
        }
        e.appendChild(layoutConstraints.saveToXML(doc, idMap));
    }

    /**
     * Creates a new AbstractContainer from the given XML element. Adds it to {@code objectMap}.
     * Subclasses are responsible for loading layout constraints, as they have to be loaded after
     * any children.
     *
     * @param e an XML element corresponding to a AbstractContainer
     * @param objectMap a map from element storage IDs to diagram objects
     * @param owner the owner of this container
     */
    AbstractContainer(org.w3c.dom.Element e, LinkedHashMap<Integer, Object> objectMap, Element owner) {
        super(e, objectMap, owner);
        left = XMLHelper.loadDouble(e, "left");
        right = XMLHelper.loadDouble(e, "right");
        top = XMLHelper.loadDouble(e, "top");
        bottom = XMLHelper.loadDouble(e, "bottom");
        minWidth = XMLHelper.loadDouble(e, "minWidth");
        minHeight = XMLHelper.loadDouble(e, "minHeight");
        userMinWidth = XMLHelper.loadDouble(e, "userMinWidth");
        userMinHeight = XMLHelper.loadDouble(e, "userMinHeight");
        id = XMLHelper.loadInt(e, "boxId");
        nextId = Math.max(id + 1, nextId);
        desiredCenter = getCenter();

        insideLabels = new LinkedHashSet<>();

        NodeList children = e.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child instanceof org.w3c.dom.Element) {
                org.w3c.dom.Element c = (org.w3c.dom.Element) child;
                String tag = c.getTagName();
                switch (tag) {
                    case XMLHelper.NAMESPACE + ":insideLabel":
                        insideLabels.add(new InsideLabel(c, objectMap, this));
                        break;
                }
            }
        }
    }
}
