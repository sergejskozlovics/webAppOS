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

import java.awt.geom.Rectangle2D.Double;
import java.util.ArrayList;
import lv.lumii.layoutengine.AbstractContainer;
import lv.lumii.layoutengine.Container;

/**
 * Class that represents box used in UniversalLayoutAlgorithm.
 * @author Jan
 */
public class BoxInGrid {

        /**
         * Container that represents the same box as BoxInGrid object.
         */
        public AbstractContainer box;
        /**
         * Coordinates of left-top corner of BoxInGrin in grid[][] array.
         */
        public int x, y;
        /**
         * x-double and y-double. These coordinates are used to not move real boxes.
         */
        public double xd,yd;
        /**
         * Horizontal and vertical size that box is taking in grid. nH-horizontal. nV- vertical
         */
        public int nH, nV;
        /**
         * Original size of the box.
         */
        public double hor, vert;
        /**
         * Place in array boxes[].
         */
        public int boxesPlace;
        /**
         * Position in QuadraticOptimization array.
         */
        public int posInOpt;
        /**
         * Variable is true if at the moment box is putted to grid[][] array.
         */
        public boolean inGrid;
        /**
         * Stores all lines that are connected to box.
         */
        public ArrayList<Line> lines;
        /**
         * Number of row taken by box.
         */
        public int gridRow;
        /**
         * Number of column taken by box.
         */
        public int gridCol;

        /**
         * Constructor.
         * @param box1 Container object that represents the same box as BoxInGrid object.
         */
        public BoxInGrid(AbstractContainer box1) {
            box = box1;
            lines = new ArrayList<Line>();
            inGrid = false;
            Double bounds = box.getBounds();
            hor=bounds.width;
            vert=bounds.height;
            //xd=box.getCenterX();
            //yd=box.getCenterY();
            gridRow=-1; // if they will remain -1 then it means that box is not set to LayoutConstraint grid
            gridCol=-1;
        }

    }
