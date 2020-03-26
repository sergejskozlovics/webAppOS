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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;

/**
 * The base class of all diagram elements.
 *
 * @author karlis
 */
public abstract class Element {

    //<editor-fold defaultstate="collapsed" desc="attributes">
    /**
     * The element this element belongs to in the element hierarchy, which is strictly a tree.
     */
    Element owner;
    /**
     * The diagram this element is a part of. A diagram's diagram will usually be itself.
     */
    private Diagram diagram;
    /**
     * The width of the empty buffer around the element.
     */
    double spacing;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="constructors">
    /**
     * Creates a new element belonging to the given owner. The new elements diagram will be the same
     * as the owners, unless the owner given is {@code null} and the element itself is a diagram, in
     * which case it will be its own diagram.
     *
     * @param owner the owner of the new element, can be {@code null} if the element is a diagram.
     * @param spacing the value of free buffer around this element
     */
    Element(Element owner, double spacing) {
        setOwner(owner);
        if (owner != null) {
            if (owner instanceof Diagram) {
                diagram = (Diagram) owner;
            } else {
                diagram = owner.getDiagram();
            }
        } else if (this instanceof Diagram) {
            diagram = (Diagram) this;
        } else {
            throw new IllegalArgumentException("Non-diagram elements must have an owner.");
        }

        this.spacing = spacing;
        getDiagram().adjustEpsilon(spacing);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="accessors">
    /**
     * Each element can be owned by some other element. When an owner is deleted, it deletes all
     * objects owned by it. A box's owner is a container (or one of its derived classes — another
     * box or diagram), a line is owned by a container. A label is owned by a box, a line, a label
     * or a container. Container's owner is a container. A diagram is not owned by any other
     * element.
     *
     * @return the owner of this object, or possibly {@code null}, if this element is a diagram.
     */
    public Element getOwner() {
        return owner;
    }

    /**
     * Sets the owner of this element.
     *
     * @param owner the owner of this element.
     */
    final void setOwner(Element owner) {
        this.owner = owner;
    }

    /**
     * Returns the children of this element.
     *
     * @return the children of this element.
     */
    abstract ArrayList<Element> getChildren();

    /**
     * Checks whether this element is a descendant of the given element.
     *
     * @param element the potential predecessor of this element
     * @return whether this element is a descendant of the given element
     */
    boolean isDescendantOf(Element element) {
        Element predecessor = owner;
        while (predecessor != null) {
            if (predecessor == element) {
                return true;
            }
            predecessor = predecessor.owner;
        }
        return false;
    }

    /**
     * The diagram the object is in. Can return {@code null} if this element itself is a diagram.
     *
     * @return the diagram this object is in, or possibly itself if this object is a top level
     * diagram.
     */
    public final Diagram getDiagram() {
        return diagram;
    }

    /**
     * Sets the diagram of the element.
     *
     * @param diagram the new diagram.
     */
    void setDiagram(Diagram diagram) {
        this.diagram = diagram;
    }

    /**
     * Returns the descendant elements of this element.
     *
     * @return the descendant elements of this element.
     */
    ArrayList<Element> getDescendants() {
        ArrayList<Element> descendants = new ArrayList<>();
        LinkedList<Element> queue = new LinkedList<>();
        queue.add(this);
        while (!queue.isEmpty()) {
            Element element = queue.pop();
            ArrayList<Element> children = element.getChildren();
            descendants.addAll(children);
            queue.addAll(children);
        }
        return descendants;
    }

    /**
     * Returns the spacing of the element.
     *
     * @return the spacing of the element.
     */
    public double getSpacing() {
        return spacing;
    }

    /**
     * Sets the spacing of the element.
     *
     * @param spacing the value to set
     */
    public abstract void setSpacing(double spacing);

    /**
     * Sets the spacing value of the element without adjusting the diagram layout.
     *
     * @param spacing the new spacing value
     */
    final void _setSpacing(double spacing) {
        this.spacing = spacing;
    }
    //</editor-fold>

    /**
     * Removes the element and its descendant elements. The whole diagram is reordered afterwards,
     * or at the end of the current transaction.
     */
    public void remove() {
        remove(true);
    }

    /**
     * Removes the element and its descendant elements. The whole diagram is reordered afterwards,
     * or at the end of the current transaction if {@code adjust} is set to {@code true}.
     *
     * @param adjust whether to adjust the diagram after the element removal
     */
    public void remove(boolean adjust) {
        for (Element descendant : getDescendants()) {
            descendant.setDiagram(null);
        }
        owner.removeChild(this);
        Diagram tDiagram = getDiagram();
        setDiagram(null);

        if (adjust) {
            tDiagram.layoutAdjust();
        }
    }

    /**
     * Removes the given element from this element's children. Removes it only from this element's
     * data structures, does not change element owners.
     *
     * @param element the child element to remove
     */
    abstract void removeChild(Element element);

    /**
     * Saves the attributes of this diagram element to the given XML element. Adds the ID's of this
     * and any descendant elements to {@code idMap}.
     *
     * @param e an XML element corresponding to this diagram element
     * @param idMap a map from diagram elements to element ID's used for storage.
     */
    void saveAttributes(org.w3c.dom.Element e, LinkedHashMap<Object, Integer> idMap) {
        int id = idMap.size();
        idMap.put(this, id);
        e.setAttribute("id", Integer.toString(id));
        e.setAttribute("spacing", Double.toString(spacing));
    }

    /**
     * Creates a new diagram element from the given XML element. Add this element to
     * {@code objectMap}.
     *
     * @param e an XML element corresponding to a diagram element
     * @param objectMap a map from element storage IDs to diagram objects
     * @param owner the owner of the element
     */
    Element(org.w3c.dom.Element e, LinkedHashMap<Integer, Object> objectMap, Element owner) {
        int id = Integer.parseInt(e.getAttribute("id"));
        if (e.hasAttribute("owner")) {
            this.owner = (Element) objectMap.get(Integer.parseInt(e.getAttribute("owner")));
        } else {
            this.owner = owner;
        }
        if (owner == null) {
            diagram = (Diagram) this;
        } else {
            diagram = this.owner.getDiagram();
        }

        spacing = XMLHelper.loadDouble(e, "spacing");

        objectMap.put(id, this);
    }
}
