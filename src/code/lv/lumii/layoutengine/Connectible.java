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
import java.util.LinkedHashSet;
import lv.lumii.layoutengine.Line.LineType;

/**
 * Specifies elements that can be connected to a line, and the methods common to such elements.
 *
 * @author k
 */
public interface Connectible {

    /**
     * Adds a line to this diagram. A line can connect boxes or other lines. The whole diagram is
     * reordered afterwards, or at the end of the current transaction.
     *
     * @param element the element to connect to this Connectible
     * @param lineType the type of the new line
     * @param spacing the width of the empty buffer around the line
     * @return the new line connecting this Connectible with the given element.
     */
    public Line connectTo(Connectible element, LineType lineType, double spacing);

    /**
     * Adds a line to this diagram going along the given points. A line can connect boxes or other
     * lines. The whole diagram is reordered afterwards, or at the end of the current transaction.
     *
     * @param element the element to connect to this Connectible
     * @param lineType the type of the new line
     * @param spacing the width of the empty buffer around the line
     * @param points the desired vertices of the new line
     * @return the new line connecting this Connectible with the given element.
     */
    public Line connectTo(Connectible element, LineType lineType, double spacing, ArrayList<Point2D.Double> points);

    /**
     * Adds a line to this diagram going along the given points. A line can connect boxes or other
     * lines. The whole diagram is reordered afterwards, or at the end of the current transaction.
     *
     * @param element the element to connect to this Connectible
     * @param lineType the type of the new line
     * @param spacing the width of the empty buffer around the line
     * @param points the desired vertices of the new line
     * @param cleanup whether to clean up the given line, removing unnecessary corners
     * @return the new line connecting this Connectible with the given element.
     */
    public Line connectTo(Connectible element, LineType lineType, double spacing, ArrayList<Point2D.Double> points, boolean cleanup);

    /**
     * Returns a list of all lines incident to this element.
     *
     * @return a list of all lines incident to this element.
     */
    public ArrayList<Line> getIncidentLines();
}