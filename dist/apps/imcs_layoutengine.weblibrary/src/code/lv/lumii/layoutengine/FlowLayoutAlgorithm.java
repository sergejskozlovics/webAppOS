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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lv.lumii.layoutengine.LayoutConstraints.ConstraintType;
import lv.lumii.layoutengine.LayoutConstraints.GridLayoutConstraints;

/**
 * This class implements the main control structures of the flow layout algorithm
 *
 * @author karlisf, paulis
 */
class FlowLayoutAlgorithm extends ComponentLayout {

    public FlowLayoutAlgorithm(DiagramPart part, LayoutAlgorithm baseLayouter) {
        super(part, baseLayouter);
    }

    @Override
    protected void layoutPass(int pass) {
        if (pass > 2) {
            return;//TODO: layout passes
        }
        
		double flowSpacing;
        int flowDirection = 1;
        
        ArrangeData.FlowData flowData = (ArrangeData.FlowData) (this.part.enclosingContainer.getUsedArrangeData());
        switch (flowData.getFlowDirection()) {
        case RIGHT:
        	flowDirection = 2;
        	break;
        
        case LEFT:
        	flowDirection = -2;
        	break;
        
        case UP:
        	flowDirection = -1;
        	break;

        case DOWN:
        	flowDirection = 1;
        	break;
        	
        }
        
        ArrayList<AbstractContainer> boxes = this.part.enclosingContainer.getAbstractContainers(false);
        int boxCount = boxes.size();

    	int rowNumber = 0;
    	int colNumber = 0;
    	GridLayoutConstraints constraints = null;

    	if (this.part.enclosingContainer.getLayoutConstraints().getType() == ConstraintType.GRID)
        {
        	constraints = (GridLayoutConstraints) this.part.enclosingContainer.getLayoutConstraints();
        }
        
        double centerX = 0;
        double centerY = 0;
        double nodeX[] = new double[boxCount];
        double nodeY[] = new double[boxCount];
        double nodeW[] = new double[boxCount];
        double nodeH[] = new double[boxCount];
    	double nodeExternalX[] = new double[boxCount];
    	double nodeExternalY[] = new double[boxCount];
        int nodeRow[] = new int[boxCount];
        int nodeCol[] = new int[boxCount];

        Map<AbstractContainer, Integer> nodeIdMap = new HashMap<>();

//        System.out.println("pass: " + pass + " boxCount: " + boxCount + " " + this.part.enclosingContainer.getCenter());

		double wSum = 0;
		double hSum = 0;
		
        int i = 0;

        for (AbstractContainer b : boxes) {

            nodeX[i] = b.getCenterX();
            nodeY[i] = b.getCenterY();

            double w = b.right - b.left;
            double h = b.bottom - b.top;

            nodeW[i] = w;
            nodeH[i] = h;
    		
			wSum += w;
			hSum += h;
   		
    		nodeExternalX[i] = Double.NaN;
    		nodeExternalY[i] = Double.NaN;

    		if (pass == 2)
    		{
        		centerX = this.part.enclosingContainer.getCenterX();
        		centerY = this.part.enclosingContainer.getCenterY();

                        List<Container> outerNeighbors;
                        if(b instanceof Container){
                            outerNeighbors = this.getOuterNeighbors((Container)b);
                        }else{
                            outerNeighbors=new ArrayList<>();
                        }
	            int laidOutNeigborCount = 0;
	            double externalX = 0;
	            double externalY = 0;

//	            System.out.print(i + ": ");
	            for (Container ob : outerNeighbors)
	            {
	            	if (this.getLaidOutPass(ob) == 2)
	            	{
	            		laidOutNeigborCount++;
	            		externalX += ob.getCenterX();
	            		externalY += ob.getCenterY();
//	                	System.out.print(" " + ob.getCenter());
	            	}
	            }
//	        	System.out.println();
	        	
	        	if (laidOutNeigborCount > 0)
	        	{
	        		nodeExternalX[i] = externalX / laidOutNeigborCount; 
	        		nodeExternalY[i] = externalY / laidOutNeigborCount; 
	        	}
    		}

            if (constraints != null)
            {
        		nodeRow[i] = -1;
        		nodeCol[i] = -1;

        		Integer row = constraints.getRow(b);
            	Integer col = constraints.getColumn(b);
            	
            	if (row != null)
            	{
            		nodeRow[i] = row - 1;
            	}
            	
            	if (col != null)
            	{
            		nodeCol[i] = col - 1;
            	}
            }
            
            nodeIdMap.put(b, i);
            i++;
        }

        List<LayoutLine> lines = buildInnerLines();
        int lineCount = lines.size();
        int edgeSourceId[] = new int[lineCount];
        int edgeTargetId[] = new int[lineCount];
        boolean[] eDirected = new boolean[lineCount];

        i = 0;

        for (LayoutLine curLine : lines) {
            edgeSourceId[i] = nodeIdMap.get(curLine.startElement); //TODO: if line
            edgeTargetId[i] = nodeIdMap.get(curLine.endElement); //TODO: if line
            eDirected[i] = true;
            //double r1 = (nodeW[edgeSourceId[i]] + nodeH[edgeSourceId[i]])/2;
            //double r2 = (nodeW[edgeTargetId[i]] + nodeH[edgeTargetId[i]])/2;
            //System.out.println(r1 + " " + r2);
            //Directed[i] = /*(i == lineCount/3 || i == lineCount/2)? false: */true;
            //eDirected[i] = (r1 < 65 && r2 < 65)? false: true;
            
            if (eDirected[i])
            {
	            if (flowDirection == 1)
	            {
	            	curLine.originalLine.setLayoutStartSides(0b0100);
	            	curLine.originalLine.setLayoutEndSides(0b0001);
	            }
	            if (flowDirection == -1)
	            {
	            	curLine.originalLine.setLayoutStartSides(0b0001);
	            	curLine.originalLine.setLayoutEndSides(0b0100);
	            }
	            if (flowDirection == 2)
	            {
	            	curLine.originalLine.setLayoutStartSides(0b0010);
	            	curLine.originalLine.setLayoutEndSides(0b1000);
	            }
	            if (flowDirection == -2)
	            {
	            	curLine.originalLine.setLayoutStartSides(0b1000);
	            	curLine.originalLine.setLayoutEndSides(0b0010);
	            }
            }
            else
            {
            	curLine.originalLine.setLayoutStartSides(0b1111);
            	curLine.originalLine.setLayoutEndSides(0b1111);
            }
            
            i++;
        }

		if (flowDirection == 1 || flowDirection == -1)
		{
			flowSpacing = hSum / boxCount;
		}
		else
		{
			flowSpacing = wSum / boxCount;
		}

        FlowLayoutKernelAlgorithm flowLayoutKernelAlgorithm = new FlowLayoutKernelAlgorithm();

        flowLayoutKernelAlgorithm.performLayout(
        		centerX,
        		centerY,
                nodeX, nodeY,
                nodeW, nodeH,
                nodeExternalX, nodeExternalY,
                constraints,
                nodeRow, nodeCol,
                edgeSourceId, edgeTargetId,
                eDirected,
                flowSpacing,
                flowDirection,
                true);
        
//		System.out.println("kernel **  pass" + pass);
//		for (i = 0; i < boxCount; i++)
//		{
//			System.out.println(nodeX[i] + " " + nodeY[i] + " " + nodeW[i] + " " + nodeH[i]);
//		}

        i = 0;

        for (AbstractContainer box : boxes) {

            moveAbstrContWithChildren(box, nodeX[i], nodeY[i]);// TODO: may be performance bottleneck with deep nesting hierarchies
            i++;
        }
    }
}
