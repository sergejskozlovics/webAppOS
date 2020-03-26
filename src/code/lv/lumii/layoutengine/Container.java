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
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import lv.lumii.layoutengine.ArrangeData.ArrangeStyle;
import lv.lumii.layoutengine.ArrangeData.FlowData.Direction;
import lv.lumii.layoutengine.GeometryHelper.PointComparator;
import lv.lumii.layoutengine.LayoutConstraints.*;
import lv.lumii.layoutengine.OutsideLabel.BoxOutsideLabel;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * The {@code Container} class specifies a group of diagram elements. The geometrical layout of the
 * elements is specified by its {@link #layoutConstraints}.
 *
 * @author karlis
 */
public class Container extends AbstractContainer {

    //<editor-fold defaultstate="collapsed" desc="attributes">
    /**
     * The set containing all child boxes of this container.
     */
    LinkedHashSet<Box> boxes;
    /**
     * The set containing all child containers of this container. Note that this set contains only
     * such containers that are not instances of any other class extending the {@link Container}
     * class.
     */
    LinkedHashSet<Container> containers;
    /**
     * The set containing all child lines of this container.
     */
    LinkedHashSet<Line> lines;
    /**
     * The arrange data for this container.
     */
    ArrangeData arrangeData;
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="constructors">
    /**
     * Creates a new empty container belonging to the given owner.
     *
     * @param left the left side coordinate
     * @param right the right side coordinate
     * @param top the top side coordinate
     * @param bottom the bottom side coordinate
     * @param owner the owner of the new container
     * @param arrangeStyle the style according to which to arrange the elements of this container
     * @param constraintType the type of the constraints for this container
     * @param spacing the spacing of the new container
     */
    Container(double left, double right, double top, double bottom, Container owner, ArrangeStyle arrangeStyle, ConstraintType constraintType, double spacing) {
        super(left, right, top, bottom, owner, constraintType, spacing);
        boxes = new LinkedHashSet<>();
        containers = new LinkedHashSet<>();
        lines = new LinkedHashSet<>();
        setArrangeStyle(arrangeStyle);
    }
    

    /**
     * Creates a new empty container belonging to the given owner.
     *
     * @param owner the owner of the new container
     * @param arrangeStyle the style according to which to arrange the elements of this container
     * @param constraintType the type of the constraints for this container
     */
    Container(Container owner, ArrangeStyle arrangeStyle, ConstraintType constraintType) {
        this(0, 0, 0, 0, owner, arrangeStyle, constraintType, Double.MAX_VALUE);
    }

    /**
     * Creates a new empty container belonging to the given owner.
     *
     * @param owner the owner of the new container
     */
    Container(Container owner) {
        this(owner, ArrangeStyle.INHERITED, ConstraintType.NONE);
    }
    //</editor-fold>
    
     /**
     * Returns empty clone of container.
     * @param addLayoutConstraints If true layout constraints are added to clone.
     * Note that LayoutConstraint that is added is the same object and not a clone.
     * @return Empty clone of container. 
     */
    Container getEmptyClone(boolean addLayoutConstraints){
        
        Container cloneCont= new Container(left, right, top, bottom, this, this.getArrangeStyle(), this.layoutConstraints.getType(), spacing);
        cloneCont.arrangeData=this.arrangeData;
        if(addLayoutConstraints){
            cloneCont.layoutConstraints=layoutConstraints;
        }
        return cloneCont;
    }
    
    @Override
    public void setSpacing(double spacing) {
        throw new UnsupportedOperationException("Container spacing is not supported.");
    }

    @Override
    public ArrayList<Element> getChildren() {
        if (hide) {
            return new ArrayList<>();
        }
        ArrayList<Element> children = super.getChildren();
        children.addAll(boxes);
        children.addAll(lines);
        children.addAll(containers);
        return children;
    }

    /**
     * Returns all child boxes of this container.
     *
     * @return all child boxes of this container.
     */
    public ArrayList<Box> getBoxes() {
        if (hide) {
            return new ArrayList<>();
        }
        return new ArrayList<>(boxes);
    }

    @Override
    ArrayList<AbstractContainer> getNextRectangles(boolean includeOutsideLabels) {
        if (hide) {
            return new ArrayList<>();
        }

        ArrayList<AbstractContainer> rectangles = new ArrayList<>();

        LinkedList<Container> containerQueue = new LinkedList<>();
        containerQueue.add(this);
        while (!containerQueue.isEmpty()) {
            Container container = containerQueue.pop();
            rectangles.addAll(container.getInsideLabels());
            for (Box box : container.boxes) {
                rectangles.add(box);
            }
            if (includeOutsideLabels) {
                for (Box box : container.boxes) {
                    rectangles.addAll(box.getOutsideLabels());
                }
                for (Line line : container.getLines()) {
                    rectangles.addAll(line.getLabels());
                }
            }
            containerQueue.addAll(container.getPureContainers());
        }

        return rectangles;
    }

    /**
     * Returns the set of boxes obtained by going down in the element hierarchy until a box is
     * found, in effect skipping other elements.
     *
     * @return the set of boxes that would be considered this container's children when ignoring
     * non-box elements in between
     */
    ArrayList<Box> getNextBoxes() {
        if (hide) {
            return new ArrayList<>();
        }

        ArrayList<Box> nextBoxes = new ArrayList<>();

        LinkedList<Container> containerQueue = new LinkedList<>();
        containerQueue.add(this);
        while (!containerQueue.isEmpty()) {
            Container container = containerQueue.pop();
            for (Box box : container.boxes) {
                nextBoxes.add(box);
            }
            containerQueue.addAll(container.getPureContainers());
        }

        return nextBoxes;
    }

    /**
     * Returns the box obtained by going up in the element hierarchy until a box is found, in effect
     * skipping non-box elements, starting from this container's parent.
     *
     * @return box that would be considered this box's parent when ignoring non-box elements in
     * between, or {@code null} if there isn't one.
     */
    Box getPrevBox() {
        return getPrevBox(false);
    }

    /**
     * Returns the box obtained by going up in the element hierarchy until a box is found, in effect
     * skipping non-box elements, starting from this container or its parent.
     *
     * @param inclusive whether to start from this container.
     * @return box that would be considered this box's parent when ignoring non-box elements in
     * between, or {@code null} if there isn't one.
     */
    Box getPrevBox(boolean inclusive) {
        if (inclusive && this instanceof Box) {
            return (Box) this;
        }

        Element tOwner = getOwner();
        while (tOwner != null && !(tOwner instanceof Box)) {
            tOwner = tOwner.getOwner();
        }
        return (Box) tOwner;
    }

    /**
     * Returns the set containing all descendant boxes of the container.
     *
     * @return the set containing all descendant boxes of the container.
     */
    public ArrayList<Box> getDescendantBoxes() {
        if (hide) {
            return new ArrayList<>();
        }
        return getDescendantBoxes(false);
    }

    /**
     * Returns the set containing all descendant boxes of the container.
     *
     * @param inclusive whether to include this container in the result if it is a box
     *
     * @return the set containing all descendant boxes of the container.
     */
    public ArrayList<Box> getDescendantBoxes(boolean inclusive) {
        if (hide) {
            return new ArrayList<>();
        }
        ArrayList<Box> descendants = new ArrayList<>();
        LinkedList<Container> queue = new LinkedList<>(getContainers());
        while (!queue.isEmpty()) {
            Container container = queue.pop();
            if (container instanceof Box) {
                descendants.add((Box) container);
            }
            queue.addAll(container.getContainers());
        }
        if (inclusive && this instanceof Box) {
            descendants.add((Box) this);
        }
        return descendants;
    }

    /**
     * Returns the set containing all descendant containers of the container.
     *
     * @return the set containing all descendant containers of the container.
     */
    public ArrayList<Container> getDescendantContainers() {
        if (hide) {
            return new ArrayList<>();
        }
        ArrayList<Container> descendants = new ArrayList<>();
        LinkedList<Container> queue = new LinkedList<>(getContainers());
        while (!queue.isEmpty()) {
            Container container = queue.pop();
            descendants.add(container);
            queue.addAll(container.getContainers());
        }
        return descendants;
    }

    /**
     * Returns the set containing all descendant labels of the container.
     *
     * @return the set containing all descendant labels of the container.
     */
    public ArrayList<OutsideLabel> getDescendantOutsideLabels() {
        if (hide) {
            return new ArrayList<>();
        }
        ArrayList<OutsideLabel> descendants = new ArrayList<>();
        for (Box box : getDescendantBoxes()) {
            descendants.addAll(box.getOutsideLabels());
        }
        for (Line line : getDescendantLines()) {
            descendants.addAll(line.getLabels());
        }
        return descendants;
    }

    /**
     * Returns the set containing all descendant labels of the container.
     *
     * @return the set containing all descendant labels of the container.
     */
    public ArrayList<BoxOutsideLabel> getDescendantBoxOutsideLabels() {
        if (hide) {
            return new ArrayList<>();
        }
        ArrayList<BoxOutsideLabel> descendants = new ArrayList<>();
        for (Box box : getDescendantBoxes()) {
            descendants.addAll(box.getOutsideLabels());
        }
        return descendants;
    }

    /**
     * Returns the set containing all descendant lines of the container.
     *
     * @return the set containing all descendant lines of the container.
     */
    public ArrayList<Line> getDescendantLines() {
        if (hide) {
            return new ArrayList<>();
        }
        ArrayList<Line> descendants = new ArrayList<>(lines);
        LinkedList<Container> queue = new LinkedList<>(getContainers());
        while (!queue.isEmpty()) {
            Container container = queue.pop();
            descendants.addAll(container.getLines());
            queue.addAll(container.getContainers());
        }
        return descendants;
    }

    /**
     * Returns all containers that are children of this container. The returned set contains boxes
     * and containers that are not instantiated by another class extending the {@link Container}
     * class. To get boxes and non-box containers separately, use the direct access
     * {@link #getBoxes()} and {@link #getPureContainers()} methods.
     *
     * @return the set of all this container's child containers.
     */
    public ArrayList<Container> getContainers() {
        if (hide) {
            return new ArrayList<>();
        }
        ArrayList<Container> allContainers = new ArrayList<>();
        allContainers.addAll(boxes);
        allContainers.addAll(containers);
        return allContainers;
    }

    /**
     * Returns all containers that are owned by this container. Note that this method does not
     * return the boxes that are owned by this container though they also are instances of
     * {@link Container} class (for that purpose use the {@link #getContainers()} method).
     *
     * @return the set of all this container's child pure containers.
     */
    @Override
    public ArrayList<Container> getPureContainers() {
        if (hide) {
            return new ArrayList<>();
        }
        return new ArrayList<>(containers);
    }

    @Override
    ArrayList<AbstractContainer> getAbstractContainers(boolean includeOutsideLabels) {
        if (hide) {
            return new ArrayList<>();
        }
        ArrayList<AbstractContainer> abstractContainers = super.getAbstractContainers(includeOutsideLabels);
        abstractContainers.addAll(boxes);
        abstractContainers.addAll(containers);
        if (includeOutsideLabels) {
            for (Box box : boxes) {
                abstractContainers.addAll(box.getOutsideLabels());
            }
            for (Line line : lines) {
                abstractContainers.addAll(line.getLabels());
            }
        }
        return abstractContainers;
    }

    @Override
    ArrayList<AbstractContainer> getRectangles() {
        if (hide) {
            return new ArrayList<>();
        }
        ArrayList<AbstractContainer> rectangles = super.getRectangles();
        rectangles.addAll(boxes);
        return rectangles;
    }

    /**
     * Returns all child lines of this container.
     *
     * @return all child lines of this container.
     */
    public ArrayList<Line> getLines() {
        if (hide) {
            return new ArrayList<>();
        }
        return new ArrayList<>(lines);
    }

    @Override
    public Container getOwner() {
        return (Container) owner;
    }

    /**
     * Returns the currently set arrange data for this container. If this returns {@code INHERITED},
     * use {@code #getUsedArrangeData} to find the arrange data to be actually used.
     *
     * @return the currently set arrange data for this container
     */
    public ArrangeData getArrangeData() {
        return arrangeData;
    }

    /**
     * Returns the currently used arrange data for this container.
     *
     * @return the currently used arrange data for this container
     */
    public ArrangeData getUsedArrangeData() {
        return arrangeData.getUsedData();
    }

    /**
     * Sets the style for the container according to which to arrange its elements.
     *
     * @param arrangeStyle the new arrange style for the container
     */
    public final void setArrangeStyle(ArrangeStyle arrangeStyle) {
        if (this instanceof Diagram && arrangeStyle == ArrangeStyle.INHERITED) {
            throw new IllegalArgumentException("A diagram cannot have INHERITED as its style.");
        }
        switch (arrangeStyle) {
            case NONE:
                arrangeData = new ArrangeData.NoneData(this);
                break;
            case FLOW:
                arrangeData = new ArrangeData.FlowData(this, Direction.DOWN);
                break;
            case SPRING_EMBEDDED:
                arrangeData = new ArrangeData.SpringEmbeddedData(this);
                break;
            case UNIVERSAL:
                arrangeData = new ArrangeData.UniversalData(this);
                break;
            case INHERITED:
                arrangeData = new ArrangeData.InheritedData(this);
                break;
        }
    }

    /**
     * Gets the style set for this container, including possibly {@link ArrangeStyle#INHERITED}. Use
     * {@link #getUsedArrangeStyle()} to get the style actually used.
     *
     * @return the style of the container according to which to arrange its elements.
     */
    public ArrangeStyle getArrangeStyle() {
        return arrangeData.getStyle();
    }

    /**
     * Gets the style of the container according to which its elements will be arranged. The same as
     * {@link #getArrangeStyle()} unless that is {@link ArrangeStyle#INHERITED}, in which case goes
     * up the element hierarchy until the first element wit ha different style.
     *
     * @return the style of the container according to which to arrange its elements.
     */
    public ArrangeStyle getUsedArrangeStyle() {
        Container styleOwner = this;
        while (styleOwner.getArrangeStyle() == ArrangeStyle.INHERITED) {
            styleOwner = styleOwner.getOwner();
        }
        return styleOwner.getArrangeStyle();
    }

    /**
     * Creates a new box as a child of this container. The minimum size of the new box is the size
     * of the given rectangle. The whole diagram is reordered afterwards, or at the end of the
     * current transaction.
     *
     * @param rectangle the position of the new box
     * @param spacing the spacing value for the new box
     * @return the new box object.
     */
    public Box createBox(Rectangle2D.Double rectangle,
            double spacing) {
        return createBox(rectangle, ArrangeStyle.INHERITED, ConstraintType.NONE, spacing, null, null);
    }

    /**
     * Creates a new box as a child of this container. The minimum size of the new box is the size
     * of the given rectangle. The whole diagram is reordered afterwards, or at the end of the
     * current transaction.
     *
     * @param rectangle the position of the new box
     * @param arrangeStyle the style according to which to arrange the elements of the new box
     * @param constraintType the type of the layout constraints for the new box
     * @param spacing the spacing value for the new box
     * @return the new box object.
     */
    public Box createBox(Rectangle2D.Double rectangle,
            ArrangeStyle arrangeStyle,
            ConstraintType constraintType,
            double spacing) {
        return createBox(rectangle, arrangeStyle, constraintType, spacing, null, null);
    }

    /**
     * Creates a new box as a child of this container. The minimum size of the new box is the size
     * of the given rectangle. The whole diagram is reordered afterwards, or at the end of the
     * current transaction.
     *
     * @param rectangle the position of the new box
     * @param arrangeStyle the style according to which to arrange the elements of the new box
     * @param constraintType the type of the layout constraints for the new box
     * @param spacing the spacing value for the new box
     * @param row the row of the new box in this container's grid
     * @param column the column of the new box in this container's grid
     * @return the new box object.
     */
    public Box createBox(Rectangle2D.Double rectangle,
            ArrangeStyle arrangeStyle,
            ConstraintType constraintType,
            double spacing,
            Integer row, Integer column) {
        Transaction.Operation.PlaceRectangleOperation.prepareTransaction(getDiagram(), getPrevRect(true));

        // by SK =>
        // fixing zero-width or zero-height rectangles
        if (rectangle.getWidth() < 3.000001)
          rectangle = new Rectangle2D.Double(rectangle.getX(), rectangle.getY(), rectangle.getWidth()+3.0, rectangle.getHeight());
        if (rectangle.getHeight() < 3.000001)
          rectangle = new Rectangle2D.Double(rectangle.getX(), rectangle.getY(), rectangle.getWidth(), rectangle.getHeight()+3.0);
        // <= by SK

        Box box = new Box(rectangle.getMinX(), rectangle.getMaxX(), rectangle.getMinY(), rectangle.getMaxY(), this, arrangeStyle, constraintType, spacing);
        boxes.add(box);

        if (getLayoutConstraints().getType() == ConstraintType.GRID) {
            GridLayoutConstraints constraints = (GridLayoutConstraints) getLayoutConstraints();
            constraints._setCell(box, row, column);
        }

        getDiagram().layoutPlaceRectangle(box, box.getCenter());
        return box;
    }

    /**
     * Creates a new container as a child of this container.
     *
     * @return the new container object.
     */
    public Container createContainer() {
        return createContainer(ArrangeStyle.INHERITED, ConstraintType.NONE);
    }

    /**
     * Creates a new container as a child of this container.
     *
     * @param arrangeStyle the style according to which to arrange the elements of the new container
     * @param constraintType the type of the layout constraints for the new container
     * @return the new container object.
     */
    public Container createContainer(ArrangeStyle arrangeStyle, ConstraintType constraintType) {
        Container container = new Container(this, arrangeStyle, constraintType);
        containers.add(container);

        return container;
    }

    /**
     * Adds a box as a child of this container. Only affects the element hierarchy.
     *
     * @param box the box to add as a child
     */
    void addBox(Box box) {
        boxes.add(box);
    }

    /**
     * Adds a container as a child of this container. Only affects the element hierarchy.
     *
     * @param container the container to add as a child
     */
    void addContainer(Container container) {
        containers.add(container);
    }

    /**
     * Adds a line as a child of this container. Only affects the element hierarchy.
     *
     * @param line the line to add as a child
     */
    void addLine(Line line) {
        lines.add(line);
    }

    @Override
    void removeChild(Element element) {
        if (element instanceof Box) {
            boxes.remove((Box) element);
        } else if (element instanceof Line) {
            lines.remove((Line) element);
        } else if (element instanceof Container) {
            containers.remove((Container) element);
        }
        super.removeChild(element);
    }

    /**
     * Moves the container (its grid and children) by the given vector. The actual size may differ
     * due to children. The new owner of the container will be stretched to accommodate the
     * container children at its desired new position. The whole diagram is reordered afterwards, or
     * at the end of the current transaction.
     *
     * @param moveVector the move vector for the container grid and children
     * @param newOwner the new owner of the container
     * @param row the row of the container in new owner's grid
     * @param column the column of the container in new owner's grid
     */
    public void move(Point2D.Double moveVector, Container newOwner, Integer row, Integer column) {
        if (newOwner.getLayoutConstraints().getType() == ConstraintType.GRID) {
            GridLayoutConstraints constraints = (GridLayoutConstraints) newOwner.getLayoutConstraints();
            constraints._setCell(this, row, column);
            move(moveVector, newOwner);
        }
    }

    /**
     * Moves the container (its grid and children) by the given vector. The actual size may differ
     * due to children. The new owner of the container will be stretched to accommodate the
     * container children at its desired new position. The whole diagram is reordered afterwards, or
     * at the end of the current transaction.
     *
     * @param moveVector the move vector for the container grid and children
     * @param newOwner the new owner of the container
     */
    public void move(Point2D.Double moveVector, Container newOwner) {
        if (newOwner == this || newOwner.isDescendantOf(this)) {
            throw new IllegalArgumentException("A container cannot be moved into itself.");
        }

        Diagram newDiagram = newOwner.getDiagram();
        Diagram oldDiagram = getDiagram();
        if (newDiagram != oldDiagram) {
            Transaction.Operation.AdjustOperation.prepareTransaction(oldDiagram, true);
        }
        Transaction.Operation.PlaceRectangleOperation.prepareTransaction(newDiagram, newOwner.getPrevRect(true));

        /*
         * Finds the lines to retrace after children box moving.
         */
        LinkedHashSet<Line> linesToRetrace = new LinkedHashSet<>();
        for (Box box : getDescendantBoxes()) {
            linesToRetrace.addAll(box.incidentLines);
        }

        /*
         * Removes lines from old owners.
         */
        for (Line line : linesToRetrace) {
            line.getOwner().removeChild(line);
        }

        /*
         * Changes container owner.
         */
        newOwner.addContainer(this);
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

        /*
         * Adjusts layout.
         */
        newDiagram.layoutMoveContainer(this, moveVector);
        if (newDiagram != oldDiagram) {
            oldDiagram.layoutAdjust();
        }
    }

    /**
     * Creates a new box with the properties of this container. The children of this container are
     * also set as the children of the new box. Sets also the new owner for the box. If this
     * container already is a box, returns it.
     *
     * @param newOwner the new owner of the converted box
     * @return the container converted from this box
     */
    Box _convertToBox(Container newOwner) {
        if (this instanceof Box) {
            return (Box) this;
        }

        Diagram newDiagram = newOwner.getDiagram();
        Diagram oldDiagram = getDiagram();
        if (newDiagram != oldDiagram) {
            Transaction.Operation.AdjustOperation.prepareTransaction(oldDiagram, true);
        }
        Transaction.Operation.PlaceRectangleOperation.prepareTransaction(newDiagram, newOwner.getPrevRect(true));

        Rectangle2D.Double ger = findGridEnclosingRectangle();
        left = Math.min(left, ger.getMinX());
        right = Math.max(right, ger.getMaxX());
        top = Math.min(top, ger.getMinY());
        bottom = Math.max(bottom, ger.getMaxY());
        Box box = new Box(left, right, top, bottom, newOwner, getArrangeStyle(), layoutConstraints.getType(), spacing);

        box.layoutConstraints = layoutConstraints;
        box.layoutConstraints.container = box;

        box.minWidth = minWidth;
        box.minHeight = minHeight;
        box.userMinWidth = userMinWidth;
        box.userMinHeight = userMinHeight;
        box.hide = hide;
        box.id = id;
        box.setDesiredCenter(getDesiredCenter());
        box.arrangeData = arrangeData;

        box.containers = containers;
        box.boxes = boxes;
        box.lines = lines;
        box.insideLabels = insideLabels;
        for (Element element : box.getChildren()) {
            element.setOwner(box);
        }

        owner.removeChild(this);
        setDiagram(null);

        return box;
    }

    /**
     * Converts this container to a box, keeping all the common properties intact. Places it at the
     * given position, along with its children. The actual size may differ due to children. The new
     * owner of the box will be stretched to accommodate the box at its desired new position. The
     * whole diagram is reordered afterwards, or at the end of the current transaction.
     *
     * @param newCenter the new center of the box
     * @param newOwner the new owner of the box
     * @param growPoint the exact point at which to insert the box, it will then be grow from that
     * point to its desired position as per {@link Box#resize(java.awt.geom.Rectangle2D.Double)}
     * @return the box converted from this container and placed at the given position
     */
    public Box convertToBox(Point2D.Double newCenter, Container newOwner, Point2D.Double growPoint) {
        Container oldOwner = getOwner();
        if (newOwner == oldOwner && oldOwner.layoutConstraints instanceof GridLayoutConstraints) {
            GridLayoutConstraints ownerConstraints = (GridLayoutConstraints) oldOwner.layoutConstraints;
            Integer row = ownerConstraints.getRow(this);
            Integer column = ownerConstraints.getColumn(this);
            if (row != null || column != null) {
                return convertToBox(newCenter, newOwner, growPoint, row, column);
            }
        }
        Box box = _convertToBox(newOwner);
        box.move(newCenter, newOwner, growPoint);
        return box;
    }

    /**
     * Converts this container to a box, keeping all the common properties intact. Places it at the
     * given position, along with its children. The actual size may differ due to children. The new
     * owner of the box will be stretched to accommodate the box at its desired new position. The
     * whole diagram is reordered afterwards, or at the end of the current transaction. If the new
     * owner has grid constraints, puts the new box into the given cell.
     *
     * @param newCenter the new center of the box
     * @param newOwner the new owner of the box
     * @param growPoint the exact point at which to insert the box, it will then be grow from that
     * point to its desired position as per {@link Box#resize(java.awt.geom.Rectangle2D.Double)}
     * @param row the row of the box in new owner's grid
     * @param column the column of the box in new owner's grid
     * @return the box converted from this container and placed at the given position
     */
    public Box convertToBox(Point2D.Double newCenter, Container newOwner, Point2D.Double growPoint, Integer row, Integer column) {
        Box box = _convertToBox(newOwner);
        box.move(newCenter, newOwner, growPoint, row, column);
        return box;
    }

    /**
     * Creates a new container with the properties of this container, if it is an instance of box.
     * The children of this box are also set as the children of the new container. Sets also the new
     * owner for the container. If this container already is not a box, returns it.
     *
     * @param newOwner the new owner for the new container
     * @return the container converted from this box
     */
    Container _convertToContainer(Container newOwner) {
        if (!(this instanceof Box)) {
            return this;
        }

        Diagram newDiagram = newOwner.getDiagram();
        Diagram oldDiagram = getDiagram();
        if (newDiagram != oldDiagram) {
            Transaction.Operation.AdjustOperation.prepareTransaction(oldDiagram, true);
        }
        Transaction.Operation.PlaceRectangleOperation.prepareTransaction(newDiagram, newOwner.getPrevRect(true));

        Container container = new Container(left, right, top, bottom, newOwner, getArrangeStyle(), layoutConstraints.getType(), spacing);
        container.layoutConstraints = layoutConstraints;
        container.layoutConstraints.container = container;
        container.minWidth = minWidth;
        container.minHeight = minHeight;
        container.userMinWidth = userMinWidth;
        container.userMinHeight = userMinHeight;
        container.hide = hide;
        container.id = id;
        container.setDesiredCenter(getDesiredCenter());
        container.arrangeData = arrangeData;

        container.containers = containers;
        container.boxes = boxes;
        container.lines = lines;
        container.insideLabels = insideLabels;
        for (Element element : container.getChildren()) {
            element.setOwner(container);
        }

        for (Line line : ((Box) this).getIncidentLines()) {
            line.remove(false);
        }

        owner.removeChild(this);
        setDiagram(null);

        return container;
    }

    /**
     * Converts this box to a container, keeping all the common properties intact. Moves its grid
     * and children by the given vector. The new owner of the container will be stretched to
     * accommodate the children and grid of the container at its desired new position. The whole
     * diagram is reordered afterwards, or at the end of the current transaction.
     *
     * @param moveVector the move vector for the container grid and children
     * @param newOwner the new owner of the container
     * @return the container converted from this box and placed at the given position
     */
    public Container convertToContainer(Point2D.Double moveVector, Container newOwner) {
        Container oldOwner = getOwner();
        if (newOwner == oldOwner && oldOwner.layoutConstraints instanceof GridLayoutConstraints) {
            GridLayoutConstraints ownerConstraints = (GridLayoutConstraints) oldOwner.layoutConstraints;
            Integer row = ownerConstraints.getRow(this);
            Integer column = ownerConstraints.getColumn(this);
            if (row != null || column != null) {
                return convertToContainer(moveVector, newOwner, row, column);
            }
        }
        Container container = _convertToContainer(newOwner);
        container.move(moveVector, newOwner);
        return container;
    }

    /**
     * Converts this box to a container, keeping all the common properties intact. Moves its grid
     * and children by the given vector. The new owner of the container will be stretched to
     * accommodate the children and grid of the container at its desired new position. The whole
     * diagram is reordered afterwards, or at the end of the current transaction.
     *
     * @param moveVector the move vector for the container grid and children
     * @param newOwner the new owner of the container
     * @param row the row of the container in new owner's grid
     * @param column the column of the container in new owner's grid
     * @return the container converted from this box and placed at the given position
     */
    public Container convertToContainer(Point2D.Double moveVector, Container newOwner, Integer row, Integer column) {
        Container container = _convertToContainer(newOwner);
        container.move(moveVector, newOwner, row, column);
        return container;
    }

    /**
     * Finds the smallest rectangle containing all of the container's children, including the
     * spacing of the children.
     *
     * @return the smallest children enclosing rectangle, or {@code null} is the container has no
     * children with dimensions
     */
    @Override
    public Rectangle2D.Double findDescendantEnclosingRectangle() {
        Rectangle2D.Double bounds = super.findDescendantEnclosingRectangle();
        if (bounds == null) {
            return null;
        }

        double tLeft = bounds.getMinX();
        double tTop = bounds.getMinY();
        double tRight = bounds.getMaxX();
        double tBottom = bounds.getMaxY();
        for (Line line : getDescendantLines()) {
            double tSpacing = line.getSpacing();
            if ((line.lineGeometry!=null) && (line.lineGeometry.points != null)) { // first null check by SK
                for (Point2D.Double point : line.lineGeometry.points) {
                    tLeft = Math.min(tLeft, point.x - tSpacing);
                    tTop = Math.min(tTop, point.y - tSpacing);
                    tRight = Math.max(tRight, point.x + tSpacing);
                    tBottom = Math.max(tBottom, point.y + tSpacing);
                }
            }
        }

        return new Rectangle2D.Double(tLeft, tTop, tRight - tLeft, tBottom - tTop);
    }

    /**
     * Finds the smallest rectangle containing all of the container's direct descendant child
     * containers, including the spacing of the container bounds.
     *
     * @return the smallest child container enclosing rectangle, or {@code null} is the container
     * has no child containers
     */
    public Rectangle2D.Double findChildContainerEnclosingRectangle() {
        ArrayList<AbstractContainer> rectangles = getNextRectangles(false);
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

        return new Rectangle2D.Double(tLeft, tTop, tRight - tLeft, tBottom - tTop);
    }

    /**
     * Returns the approximate geometric shape of this container. If container has grid constraints,
     * returns an orthogonal rectangle bounding the grid. Otherwise returns a convex hull enclosing
     * all descendants of the container. If the container is empty, returns {@code null}.
     *
     * @return the approximate geometric shape of this container
     */
    public Path2D.Double getShape() {
        Path2D.Double path = null;

        LinkedList<Container> queue = new LinkedList<>();
        queue.add(this);
        while (!queue.isEmpty()) {
            Container container = queue.pop();
            if (container.layoutConstraints instanceof GridLayoutConstraints) {
                path = new Path2D.Double(Path2D.WIND_EVEN_ODD, 5);
                GridLayoutConstraints grid = (GridLayoutConstraints) container.layoutConstraints;
                double gridLeft = grid.getColumnLeft(1);
                double gridRight = grid.getColumnRight(grid.getColumnCount());
                double gridTop = grid.getRowTop(1);
                double gridBottom = grid.getRowBottom(grid.getRowCount());
                path.moveTo(gridLeft, gridTop);
                path.lineTo(gridRight, gridTop);
                path.lineTo(gridRight, gridBottom);
                path.lineTo(gridLeft, gridBottom);
                path.closePath();
                return path;
            }
            queue.addAll(container.getPureContainers());
        }

        ArrayList<Point2D.Double> points = new ArrayList<>();
        for (AbstractContainer rectangle : getNextRectangles(true)) {
            Point2D.Double[] corners = GeometryHelper.getChainPoints(rectangle);
            for (int i = 0; i < 4; i++) {
                points.add(corners[i]);
            }
        }
        for (Line line : getLines()) {
            for (Point2D.Double point : line.lineGeometry.getPoints()) {
                points.add(point);
            }
        }

        int pointCount = points.size();

        if (pointCount > 0) {
            Collections.sort(points, new PointComparator());
            Point2D.Double leftmostPoint = points.get(0), rightmostPoint = points.get(pointCount - 1);
            ArrayList<Point2D.Double> upperPoints = new ArrayList<>();
            ArrayList<Point2D.Double> lowerPoints = new ArrayList<>();
            upperPoints.add(leftmostPoint);
            lowerPoints.add(leftmostPoint);
            for (int i = 1; i < pointCount; i++) {
                Point2D.Double point = points.get(i);
                if (i == pointCount - 1 || Line2D.relativeCCW(leftmostPoint.x, leftmostPoint.y, point.x, point.y, rightmostPoint.x, rightmostPoint.y) == -1) {
                    while (upperPoints.size() >= 2) {
                        Point2D.Double prev = upperPoints.get(upperPoints.size() - 2);
                        Point2D.Double last = upperPoints.get(upperPoints.size() - 1);
                        if (Line2D.relativeCCW(prev.x, prev.y, last.x, last.y, point.x, point.y) == -1) {
                            break;
                        }
                        upperPoints.remove(upperPoints.size() - 1);
                    }
                    upperPoints.add(points.get(i));
                }
                if (i == pointCount - 1 || Line2D.relativeCCW(rightmostPoint.x, rightmostPoint.y, point.x, point.y, leftmostPoint.x, leftmostPoint.y) == -1) {
                    while (lowerPoints.size() >= 2) {
                        Point2D.Double prev = lowerPoints.get(lowerPoints.size() - 2);
                        Point2D.Double last = lowerPoints.get(lowerPoints.size() - 1);
                        if (Line2D.relativeCCW(point.x, point.y, last.x, last.y, prev.x, prev.y) == -1) {
                            break;
                        }
                        lowerPoints.remove(lowerPoints.size() - 1);
                    }
                    lowerPoints.add(points.get(i));
                }
            }

            Point2D.Double start = upperPoints.get(0);
            path = new Path2D.Double(Path2D.WIND_EVEN_ODD, upperPoints.size() + lowerPoints.size() - 1);
            path.moveTo(start.x, start.y);
            for (int i = 1; i < upperPoints.size(); i++) {
                Point2D.Double point = upperPoints.get(i);
                path.lineTo(point.x, point.y);
            }
            for (int i = lowerPoints.size() - 2; i > 0; i--) {
                Point2D.Double point = lowerPoints.get(i);
                path.lineTo(point.x, point.y);
            }
            path.closePath();
        }

        return path;
    }

    /**
     * Finds the least common ancestor of this and the given containers. The complexity of this
     * method is O(h) with h being the maximal height from the root either to this or the given
     * container.
     *
     * @param c the given container
     * @return the least common ancestor of this and the given containers
     */
    Container findLCA(Container c) {
        if (getDiagram() != c.getDiagram()) {
            return null;
        }

        /*
         * The stacks holding the list of the ancestors for each of the two given containers.
         */
        LinkedList<Container> aStack = new LinkedList<>(),
                bStack = new LinkedList<>();
        for (Container predecessor = this; predecessor != null;
                predecessor = predecessor.getOwner()) {
            aStack.push(predecessor);
        }
        for (Container predecessor = c; predecessor != null;
                predecessor = predecessor.getOwner()) {
            bStack.push(predecessor);
        }

        /*
         * In each stack, the top-most element represents the root of the hierarchy, the next
         * element represents the child of the root container and in the same time one of the
         * ancestors for the given container, and so on.
         */

        /*
         * While the elements on the top of each stack are equal, marks this element as the
         * potential LCA of the given containers. When the elements on the top of each stack are not
         * equal, the ancestor variable will hold the right LCA container.
         */
        Container ancestor = null;
        while (!aStack.isEmpty() && !bStack.isEmpty()
                && aStack.peek() == bStack.peek()) {
            ancestor = aStack.pop();
            bStack.pop();
        }

        return ancestor;
    }

    /**
     * Saves the attributes of this Container to the given XML element. Attributes include any
     * children it has. Adds the ID's of this and any descendant elements to {@code idMap}.
     *
     * @param e an XML element corresponding to this Container
     * @param doc the XML document in which to create any children
     * @param idMap a map from diagram elements to element ID's used for storage.
     */
    @Override
    void saveAttributes(org.w3c.dom.Element e, org.w3c.dom.Document doc, LinkedHashMap<Object, Integer> idMap) {
        for (Box b : boxes) {
            e.appendChild(b.saveToXML(doc, idMap));
        }
        for (Container c : containers) {
            e.appendChild(c.saveToXML(doc, idMap));
        }

        super.saveAttributes(e, doc, idMap);

        org.w3c.dom.Element linesElement = doc.createElement(XMLHelper.NAMESPACE + ":lines");
        e.appendChild(linesElement);
        for (Line line : lines) {
            linesElement.appendChild(line.saveToXML(doc, idMap));
        }
        e.appendChild(linesElement);

        e.appendChild(arrangeData.saveToXML(doc, idMap));
    }

    /**
     * Creates a new XML element corresponding to this container in the given document. Includes all
     * its children. Puts the IDs of any new elements in {@code idMap}.
     *
     * @param doc the XML document in which to create the new element
     * @param idMap a map from diagram objects to element storage IDs.
     * @return a new element corresponding to this diagram element
     */
    org.w3c.dom.Element saveToXML(org.w3c.dom.Document doc, LinkedHashMap<Object, Integer> idMap) {
        org.w3c.dom.Element e = doc.createElement(XMLHelper.NAMESPACE + ":container");
        saveAttributes(e, doc, idMap);
        return e;
    }

    /**
     * Creates a new Container from the given XML element, along with its children. Adds all of them
     * {@code objectMap}.
     *
     * @param e an XML element corresponding to a Container
     * @param objectMap a map from element storage IDs to diagram objects
     * @param owner the owner of the container
     */
    Container(org.w3c.dom.Element e, LinkedHashMap<Integer, Object> objectMap, Container owner) {
        super(e, objectMap, owner);
        boxes = new LinkedHashSet<>();
        containers = new LinkedHashSet<>();
        lines = new LinkedHashSet<>();
        NodeList children = e.getChildNodes();

        org.w3c.dom.Element arrangeElement = null, linesElement = null;
        org.w3c.dom.Element constraintElement = null;
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child instanceof org.w3c.dom.Element) {
                org.w3c.dom.Element c = (org.w3c.dom.Element) child;
                String tag = c.getTagName();
                switch (tag) {
                    case (XMLHelper.NAMESPACE + ":container"):
                        containers.add(new Container(c, objectMap, this));
                        break;
                    case (XMLHelper.NAMESPACE + ":box"):
                        boxes.add(new Box(c, objectMap, this));
                        break;
                    case (XMLHelper.NAMESPACE + ":arrangeData"):
                        arrangeElement = c;
                        break;
                    case (XMLHelper.NAMESPACE + ":lines"):
                        linesElement = c;
                        break;
                    case XMLHelper.NAMESPACE + ":layoutConstraints":
                        constraintElement = c;
                        break;
                }
            }
        }
        layoutConstraints = LayoutConstraints.loadFromXML(constraintElement, objectMap, this);

        if (linesElement != null) {
            org.w3c.dom.NodeList linesChildren = linesElement.getChildNodes();
            for (int i = 0; i < linesChildren.getLength(); i++) {
                org.w3c.dom.Node child = linesChildren.item(i);
                if (child instanceof org.w3c.dom.Element) {
                    org.w3c.dom.Element c = (org.w3c.dom.Element) child;
                    lines.add(new Line(c, objectMap, this));
                }
            }
        }

        if (arrangeElement != null) {
            arrangeData = ArrangeData.loadFromXML(arrangeElement, objectMap, this);
        } else {
            setArrangeStyle(ArrangeStyle.valueOf(e.getAttribute("arrangeStyle")));
        }
    }
}
