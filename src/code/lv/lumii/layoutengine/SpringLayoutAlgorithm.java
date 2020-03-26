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

import lv.lumii.layoutengine.util.LayoutLine;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lv.lumii.layoutengine.springembedder.TimedSpringEmbedder;

/**
 * This class implements spring embedder layout algorithm
 *
 * @author karlisf
 */
class SpringLayoutAlgorithm extends ComponentLayout {

    public SpringLayoutAlgorithm(DiagramPart part, LayoutAlgorithm baseLayouter) {
        super(part, baseLayouter);
    }

    @Override
    protected void layoutPass(int pass) {
        if (pass != 1) {
            return;//TODO: layout passes
        }
        ArrayList<AbstractContainer> boxes = part.enclosingContainer.getAbstractContainers(false);
        int boxCount = boxes.size()+ (addDummyEdges ? 1:0);// add one dummy node
        double spacing = 0; // taken into account in node radius calculation
        double degreeModifier = 1;//TODO: options
        
        // build nodes

        double nodeX[] = new double[boxCount];
        double nodeY[] = new double[boxCount];
        double nodeRadius[] = new double[boxCount];

        Map<AbstractContainer, Integer> nodeIdMap = new HashMap<>();

        int i = 0;

        for (AbstractContainer b : boxes) {
            nodeX[i] = b.getCenterX();
            nodeY[i] = b.getCenterY();

            double w = b.right - b.left;
            double h = b.bottom - b.top;

            nodeRadius[i] = Math.sqrt(w * w + h * h) / 2;
            nodeIdMap.put(b, i);

            i++;
        }
        
        if(addDummyEdges)
        {
            // set dummy node parameters
            nodeX[boxCount-1] = 0;
            nodeY[boxCount-1] = 0;
            nodeRadius[boxCount-1] = 1;        
        }

        // build lines
        List<LayoutLine> lines = buildInnerLines();
        int lineCount = lines.size()+(addDummyEdges ? boxCount-1:0);// add lines from dummy node to all others
        int edgeSourceId[] = new int[lineCount];
        int edgeTargetId[] = new int[lineCount];
        double edgeStrength[] = new double[lineCount];        

        i = 0;

        for (LayoutLine curLine : lines) {
            edgeSourceId[i] = nodeIdMap.get(curLine.startElement); //TODO: if line
            edgeTargetId[i] = nodeIdMap.get(curLine.endElement); //TODO: if line
            edgeStrength[i] = 1;
            i++;
        }
        
        if(addDummyEdges)
        {
            double strength = getGraphSize();
            strength = strength * strength / 10;

            if (strength > 0.01) strength = 1.0 / strength;
            else strength = 100;

            if (strength > 100) strength = 100;


            // set dummy line parameters
            int lineId = lines.size();

            for(i=0;i<boxCount-1;i++)
            {
                edgeSourceId[lineId] = i;
                edgeTargetId[lineId] = boxCount-1;
                edgeStrength[lineId] = 0;//strength;            
                lineId++;
            }
        }        
        
        // perform layout
        TimedSpringEmbedder layouter = new TimedSpringEmbedder(boxCount, lineCount, 1);

        layouter.layout(spacing,
                nodeX, nodeY,
                nodeRadius,
                edgeSourceId, edgeTargetId,
                false,
                null,
                degreeModifier,
                edgeStrength,
                boxes.size(),
                boxes,
                part.enclosingContainer.getLayoutConstraints(),
                lines);

        // transfer back data
        i = 0;

        for (AbstractContainer box : boxes) {

            moveAbstrContWithChildren(box, nodeX[i], nodeY[i]);// TODO: may be performance bottleneck with deep nesting hierarchies
            i++;
        }
    }
    
    /**
     * This method estimates the size of the graph.
     */
    private double getGraphSize() 
    {
        double size = 0;

        for (Container c:part.children) {
            Rectangle2D.Double bounds = c.getBounds();
            size += (bounds.height+bounds.width)/2;
        }

        size = Math.sqrt(size) * 3 + 5;

        return size;
    }
    
    private static final boolean addDummyEdges = false;
}
