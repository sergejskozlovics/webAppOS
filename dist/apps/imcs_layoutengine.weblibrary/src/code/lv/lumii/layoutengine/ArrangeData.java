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

import java.util.LinkedHashMap;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * This class stores data for arranging the associated container according to specified style.
 *
 * @author jk
 */
public abstract class ArrangeData {

    /**
     * Lists the possible styles in which to arrange a diagram.
     */
    public static enum ArrangeStyle {

        /**
         * Uses the starting element coordinates with as few changes as possible, only normalizing
         * the diagram and fixing any errors.
         */
        NONE,
        /**
         * Arranges the diagram so that all edges are lie in a single direction (currently, down) as
         * much as possible.
         */
        FLOW,
        /**
         * This layout arranges boxes universally. (Better description will be here when algorithm
         * will be completed)
         */
        UNIVERSAL,
        /**
         * Arranges the diagram so that its boxes are laid out symmetrically and line length is
         * minimized.
         */
        SPRING_EMBEDDED,
        /**
         * Arranged this container using the style of its parent. A diagram cannot be of this style.
         */
        INHERITED
    }
    /**
     * The container the data relates to.
     */
    Container owner;

    /**
     * A simple constructor for the abstract class.
     *
     * @param owner the owner of the data
     */
    ArrangeData(Container owner) {
        this.owner = owner;
    }

    /**
     * Returns the arrange style the data corresponds to.
     *
     * @return the arrange style the data corresponds to.
     */
    abstract ArrangeStyle getStyle();

    /**
     * Returns the actual used data for arranging this container. More precisely, returns the data
     * of the lowest ancestor in the hierarchy tree that doesn't have inherited arrange style.
     *
     * @return the actual used data for the container.
     */
    public ArrangeData getUsedData() {
        return this;
    }

    /**
     * Saves the attributes of these arrange data to the given XML element corresponding to them.
     *
     * @param e an XML element corresponding to these arrange data
     * @param doc the XML document in which to save these attributes
     * @param idMap a map from diagram elements to their ID's in the XML document
     */
    abstract void saveAttributes(org.w3c.dom.Element e, org.w3c.dom.Document doc, LinkedHashMap<Object, Integer> idMap);

    /**
     * Creates a new XML element corresponding to this arrange data in the given document.
     *
     * @param doc the XML document in which to create the new element
     * @param idMap a map from diagram objects to element storage IDs.
     * @return a new element corresponding to the arrange data
     */
    org.w3c.dom.Element saveToXML(org.w3c.dom.Document doc, LinkedHashMap<Object, Integer> idMap) {
        org.w3c.dom.Element e = doc.createElement(XMLHelper.NAMESPACE + ":arrangeData");
        e.setAttribute("style", getStyle().toString());
        saveAttributes(e, doc, idMap);
        return e;
    }

    /**
     * Recreates the arrange data from the corresponding XML element.
     *
     * @param e an XML element corresponding to layout constraints
     * @param objectMap a map from element storage IDs to diagram objects
     * @param owner the container the data should belong to
     * @return returns the new arrange data
     */
    static ArrangeData loadFromXML(org.w3c.dom.Element e, LinkedHashMap<Integer, Object> objectMap, Container owner) {
        ArrangeStyle style = ArrangeStyle.valueOf(e.getAttribute("style"));
        switch (style) {
            case NONE:
                return new NoneData(owner);
            case FLOW:
                return new FlowData(owner, e);
            case UNIVERSAL:
                return new UniversalData(owner);
            case SPRING_EMBEDDED:
                return new SpringEmbeddedData(owner);
            case INHERITED:
                return new InheritedData(owner);
        }
        return null;
    }

    /**
     * Fictive data for empty style.
     */
    static public class NoneData extends ArrangeData {

        /**
         * Creates a new empty style arrange data.
         *
         * @param owner the container the data corresponds to
         */
        public NoneData(Container owner) {
            super(owner);
        }

        @Override
        ArrangeStyle getStyle() {
            return ArrangeStyle.NONE;
        }

        @Override
        void saveAttributes(Element e, Document doc, LinkedHashMap<Object, Integer> idMap) {
        }
    }

    /**
     * Flow arrange style data.
     */
    static public class FlowData extends ArrangeData {

        /**
         * The direction of the flow.
         */
        static public enum Direction {

            /**
             * Direction upwards.
             */
            UP,
            /**
             * Direction rightwards.
             */
            RIGHT,
            /**
             * Direction downwards.
             */
            DOWN,
            /**
             * Direction leftwards.
             */
            LEFT
        }
        /**
         * The flow direction for this container.
         */
        Direction flowDirection;

        /**
         * Creates a new flow arrange style data.
         *
         * @param owner the container the data corresponds to
         * @param flowDirection the flow direction
         */
        public FlowData(Container owner, Direction flowDirection) {
            super(owner);
            this.flowDirection = flowDirection;
        }

        /**
         * Creates a new flow data instance from XML element.
         *
         * @param owner the container the data corresponds to
         * @param e the XML element describing the data
         */
        private FlowData(Container owner, Element e) {
            super(owner);
            flowDirection = Direction.valueOf(e.getAttribute("direction"));
        }

        @Override
        ArrangeStyle getStyle() {
            return ArrangeStyle.FLOW;
        }

        /**
         * Sets the direction of the flow.
         *
         * @param flowDirection the new direction of the flow
         */
        public void setFlowDirection(Direction flowDirection) {
            this.flowDirection = flowDirection;
        }

        /**
         * Returns the current direction of the flow.
         *
         * @return the current direction of the flow.
         */
        public Direction getFlowDirection() {
            return flowDirection;
        }

        @Override
        void saveAttributes(Element e, Document doc, LinkedHashMap<Object, Integer> idMap) {
            e.setAttribute("direction", flowDirection.toString());
        }
    }

    /**
     * Universal arrange style data.
     */
    static public class UniversalData extends ArrangeData {

        /**
         * Creates a new universal arrange style data.
         *
         * @param owner the container this data belongs to
         */
        public UniversalData(Container owner) {
            super(owner);
        }

        @Override
        ArrangeStyle getStyle() {
            return ArrangeStyle.UNIVERSAL;
        }

        @Override
        void saveAttributes(Element e, Document doc, LinkedHashMap<Object, Integer> idMap) {
        }
    }

    /**
     * Spring embedded arrange style data.
     */
    static public class SpringEmbeddedData extends ArrangeData {

        /**
         * Creates a new spring embedded style data.
         *
         * @param owner the container this data belongs to
         */
        public SpringEmbeddedData(Container owner) {
            super(owner);
        }

        @Override
        ArrangeStyle getStyle() {
            return ArrangeStyle.SPRING_EMBEDDED;
        }

        @Override
        void saveAttributes(Element e, Document doc, LinkedHashMap<Object, Integer> idMap) {
        }
    }

    /**
     * The data for the inherited arrange style.
     */
    static public class InheritedData extends ArrangeData {

        /**
         * Creates a new inherited style data.
         *
         * @param owner the container this data belongs to
         */
        public InheritedData(Container owner) {
            super(owner);
        }

        @Override
        ArrangeStyle getStyle() {
            return ArrangeStyle.INHERITED;
        }

        @Override
        void saveAttributes(Element e, Document doc, LinkedHashMap<Object, Integer> idMap) {
        }

        @Override
        public ArrangeData getUsedData() {
            Container current = owner;
            while (current.getArrangeStyle() == ArrangeStyle.INHERITED) {
                current = current.getOwner();
            }
            return current.getArrangeData();
        }
    }
}
