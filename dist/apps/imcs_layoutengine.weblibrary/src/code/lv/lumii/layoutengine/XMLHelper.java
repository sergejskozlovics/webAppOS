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
import org.w3c.dom.*;

/**
 * Stores common methods for saving and loading diagrams to XML.
 */
class XMLHelper {

    /**
     * The XML namespace used for diagram storage.
     */
    static final String NAMESPACE = "jk";
    /**
     * The URI of the diagram namespace. TODO: Use a real URI.
     */
    static String NAMESPACE_URI = "http://www.lumii.lv/jk";

    /**
     * Save the given rectangle to XML.
     *
     * @param doc the XML document in which to create the new XML element
     * @param rectangle the rectangle from which to create an XML element
     * @param name the name of the created XML element
     * @return an XML element corresponding to the given rectangle
     */
    static org.w3c.dom.Element save(Document doc, Rectangle2D.Double rectangle, String name) {
        org.w3c.dom.Element e = doc.createElement(name);

        e.setAttribute("x", Double.toString(rectangle.x));
        e.setAttribute("y", Double.toString(rectangle.y));
        e.setAttribute("w", Double.toString(rectangle.width));
        e.setAttribute("h", Double.toString(rectangle.height));

        return e;
    }

    /**
     * Save the given point to XML.
     *
     * @param doc the XML document in which to create the new XML element
     * @param point the point from which to create an XML element
     * @param name the name of the created XML element
     * @return an XML element corresponding to the given point
     */
    static Node save(Document doc, Point2D.Double point, String name) {
        org.w3c.dom.Element e = doc.createElement(name);

        e.setAttribute("x", Double.toString(point.x));
        e.setAttribute("y", Double.toString(point.y));

        return e;
    }

    /**
     * Loads a rectangle from the given XML element
     *
     * @param element an XML element corresponding to a rectangle
     * @return the rectangle created from the given XML element
     */
    static Rectangle2D.Double loadRectangle(org.w3c.dom.Element element) {
        double x, y, w, h;
        x = Double.valueOf(element.getAttribute("x"));
        y = Double.valueOf(element.getAttribute("y"));
        w = Double.valueOf(element.getAttribute("w"));
        h = Double.valueOf(element.getAttribute("h"));
        return new Rectangle2D.Double(x, y, w, h);
    }

    /**
     * Loads a point from the given XML element
     *
     * @param element an XML element corresponding to a point
     * @return the point created from the given XML element
     */
    static Point2D.Double loadPoint(org.w3c.dom.Element element) {
        double x, y;
        x = Double.valueOf(element.getAttribute("x"));
        y = Double.valueOf(element.getAttribute("y"));
        return new Point2D.Double(x, y);
    }

    /**
     * Loads a Double attribute from the given XML element
     *
     * @param e an XML element with a double attribute
     * @param name the name of the attribute to load
     * @return a Double value corresponding to the given attribute, or {@code  Double.NaN} if there
     * isn't such an attribute.
     */
    static double loadDouble(org.w3c.dom.Element e, String name) {
        return e.hasAttribute(name) ? Double.parseDouble(e.getAttribute(name)) : Double.NaN;
    }

    /**
     * Loads an integer attribute from the given XML element
     *
     * @param e an XML element with an integer attribute
     * @param name the name of the attribute to load
     * @return an integer value corresponding to the given attribute, or 0 if there isn't such an
     * attribute.
     */
    static int loadInt(org.w3c.dom.Element e, String name) {
        return e.hasAttribute(name) ? Integer.parseInt(e.getAttribute(name)) : 0;
    }
}
