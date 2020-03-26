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

import lv.lumii.layoutengine.obstacleGraph.Segment;

/**
 * Class that extends Segment class, it is used in UniversalLayoutAlgorithm.
 * @author Jan
 */
 public class SimpleOrthogonalSegment extends Segment{
        
     /**
      * Object from which segment was created.
      */
        public BoxInGrid boxIG;
        
        /**
         * Constructor of segment.
         * @param pos The position of the segment.
         * @param start The start coordinate of the segment.
         * @param end The end coordinate of the segment.
         */
        public SimpleOrthogonalSegment(double pos, double start, double end) {
            super(pos, start, end);
            boxIG=null;
        }
    }
