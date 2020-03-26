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

package lv.lumii.layoutengine.polyomino;

import java.util.ArrayList;

/**
 * This class defines a polyomino.
 */
public final class Polyomino {

    /**
     * Represents rectangle enclosing connected component
     */
    public double minX=Double.POSITIVE_INFINITY, maxX=Double.NEGATIVE_INFINITY,
                    minY=Double.POSITIVE_INFINITY, maxY=Double.NEGATIVE_INFINITY;

    /**
     * Spacings from rectangle enclosing connected component
     */
    public double spacingX, spacingY;
    
    //the resulting placement coordinates
    public int x, y;
    /**
     * Polyomino cells
     */
    public ArrayList<IntegerPoint> coord;
    IntegerRectangle bounds = new IntegerRectangle();

    int perimeter() {
        return bounds.x2 - bounds.x1 + bounds.y2 - bounds.y1;
    }
    

    /**
     * This auxilary class defines an integer point.
     */
    public static final class IntegerPoint {

        /**
         * This method creates a new integer point.
         */
        public IntegerPoint() {
        }

        public IntegerPoint(int x, int y) {
            this.x = x;
            this.y = y;
        }

        /**
         * This method gets the x coordinate of the point.
         */
        public int getX() {
            return this.x;
        }

        /**
         * This method sets the x coordinate of the point.
         */
        public void setX(int x) {
            this.x = x;
        }

        /**
         * This method gets the y coordinate of the point.
         */
        public int getY() {
            return this.y;
        }

        /**
         * This method sets the y coordinate of the point.
         */
        public void setY(int y) {
            this.y = y;
        }
        int x;
        int y;
    }

    static class IntegerRectangle {

        int x1;
        int y1;
        int x2;
        int y2;
    }
}
