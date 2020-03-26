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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lv.lumii.layoutengine.universalLayout;

/**
 * Class that represents line between BoxInGrid objects used in
 * UniversalLayoutAlgorithm.
 *
 * @author Jan
 */
public class Line {

    /**
     * Box where connection is going.
     */
    public BoxInGrid boxIG;
    /**
     * If it is true then end of the line is connected directly to box
     * represented by object "boxIG".
     */
    public boolean endIsOriginal;
    /**
     * If !endIsOriginal then these are the coordinates of a place where line is
     * connected. (0,0) point is boxes left-top corner.
     */
    public int endX, endY;
    /**
     * Coordinates of connection in real numbers. Similar to endX and endY.
     */
    public double realEndX, realEndY;
    /**
     * If it is true then start of the line is connected directly to box that is
     * represented by object that contains this Line object.
     */
    public boolean startIsOriginal;
    /**
     * If !startIsOriginal then these are the coordinates of a place where line
     * is connected. (0,0) point is boxes left-top corner.
     */
    public int startX, startY;
    /**
    * Coordinates of connection in real numbers. Similar to startX and startY.
    */   
    public double realStartX, realStartY;

    /**
     * Creates Line object. This object will be kept in one of BoxInGrid
     * objects.
     *
     * @param connectedBoxIG BoxInGrid that is connected to BoxInGrid object
     * that contains this Line.
     * @param startIsOriginal True if line is directly connected to BoxInGrid
     * object that contains this Line object. False if it is connected to one of
     * the Boxes inside this box.
     * @param endIsOriginal True if line is directly connected to BoxInGrid
     * object, link to which is kept in this Line object under name boxIG. False
     * if line is connected to one of the boxes that is inside boxIG.
     */
    public Line(BoxInGrid connectedBoxIG, boolean startIsOriginal, boolean endIsOriginal) {
        this.boxIG = connectedBoxIG;
        this.startIsOriginal = startIsOriginal;
        this.endIsOriginal = endIsOriginal;
    }

    /**
     *Sets realStartX and realStartY.
     * @param realX Real x coordinate of box from which line is going.
     * @param realY Real y coordinate of box from which line is going.
     */
    public void setRealStartPoint(double realX, double realY) {
        if (!startIsOriginal) {
            realStartX = realX;
            realStartY = realY;
        } else {
            System.err.println("startIsOriginal is true, so start point can't be set.");
        }
    }

    /**
     *Sets realEndX and realEndY.
     * @param realX Real x coordinate of box to which line is going.
     * @param realY Real y coordinate of box to which line is going.
     */
    public void setRealEndPoint(double realX, double realY) {
        if (!endIsOriginal) {
            realEndX = realX;
            realEndY = realY;
        } else {
            System.err.println("endIsOriginal is true, so end point can't be set.");
        }
    }
}
