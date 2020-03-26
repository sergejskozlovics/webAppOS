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

import lv.lumii.layoutengine.util.LayoutLine;//maybe will be changed to something else

import java.awt.geom.Point2D;

import java.util.*;
import lv.lumii.layoutengine.polyomino.Polyomino;
import lv.lumii.layoutengine.polyomino.PolyominoPacking;
import lv.lumii.layoutengine.LayoutConstraints.*;

/**
 * Pack rectangles using polyomino packing
 *
 */
public class ConnectedComponentPacking {
    
    /**
     * Used when rounding double 
     */
    final static double reserveSpacing=0.1;
    
    /**
     * Function that arranges connected components of graph that is obtained from parts.
     * @param parts Array with all graph that must be arranged by it's connected components.
     * @param spacing Minimal spacing around every connected component.
     * @param aspectRatio ? Something that probably depends on boxs' size (hor * vert) ?
     * @param proportionalSpacing Minimal proportional spacing around every con. comp.
     * Full spacing is calculated as spacing+proportionalSpacing*size
     */
    public static void pack(DiagramPart[] parts, double spacing, double aspectRatio, double proportionalSpacing) {
   

        Polyomino[] polyominos=new Polyomino[parts.length];
        for(int i=0; i<polyominos.length; i++){
            polyominos[i]=new Polyomino();
        }
        /*
         * Calculating size of enclosing rectangle to con. comp.
         */
        for(int i=0; i<parts.length; i++){
            
            for(AbstractContainer aCont: parts[i].enclosingContainer.getAbstractContainers(false)){
                if (aCont.getLeft() < polyominos[i].minX) {
                    polyominos[i].minX = aCont.getLeft();
                }
                if (aCont.getRight() > polyominos[i].maxX) {
                    polyominos[i].maxX = aCont.getRight();
                }
                if (aCont.getTop() < polyominos[i].minY) {
                    polyominos[i].minY = aCont.getTop();
                }
                if (aCont.getBottom() > polyominos[i].maxY) {
                    polyominos[i].maxY = aCont.getBottom();
                }
            }
            
            
            polyominos[i].spacingX=spacing+proportionalSpacing*(polyominos[i].maxX - polyominos[i].minX);
            polyominos[i].spacingY=spacing+proportionalSpacing*(polyominos[i].maxY - polyominos[i].minY);
            
        }
        
        double area=0;
        
        for(Polyomino polyomino: polyominos){
            area+=(polyomino.maxX - polyomino.minX + 2*polyomino.spacingX + reserveSpacing)
                    *(polyomino.maxY - polyomino.minY + 2*polyomino.spacingY + reserveSpacing);
        }
        
        double stepX=Math.sqrt(area / (polyominos.length * 16));
        
        // adjust respecting the aspect ratio
        double fstep = 2 / (1 + aspectRatio);
        double stepY = stepX * aspectRatio * fstep;

        stepX *= fstep;
        
        fillPolyominos(polyominos, parts, stepX, stepY);
        
        PolyominoPacking packer = new PolyominoPacking();
        packer.pack(polyominos);
        
        /*
         * Moving all containers to it's new places.
         */
        for(int i=0; i<polyominos.length; i++){
            Polyomino polyomino=polyominos[i];
            Point2D.Double moveVector = new Point2D.Double(polyomino.x*stepX - (polyomino.minX-polyomino.spacingX),
                polyomino.y*stepY - (polyomino.minY-polyomino.spacingY));
   
            
            for(AbstractContainer aCont: parts[i].enclosingContainer.getAbstractContainers(false)){
                aCont.transpose(moveVector);
                for(AbstractContainer descendantACont: aCont.getDescendantAbstractContainers(false)){
                    descendantACont.transpose(moveVector);
                }            
            }
            
        }
        

     
    }

    /**
     * After run of this function polyominos will contain all it's cells
     * @param polyominos Array with polyominos where minX, maxX, minY, maxY, 
     * spacingX and spacingY are already assigned their values.
     * @param conComponents Array with connected components. Order of elements must be the same as in polyominos.
     * @param stepX Horizontal size of mino cell.
     * @param stepY Vertical size of mino cell.
     */
    private static void fillPolyominos(Polyomino[] polyominos, DiagramPart[] conComponents, double stepX, double stepY){
            
        for(int i=0; i<conComponents.length; i++){
            /*
             * For every i polyominos[i] represents conComponents[i]
             */
            Polyomino polyomino=polyominos[i];
            DiagramPart conComp=conComponents[i];
            
            /*
             * size of the rectangle in grid units
             */
            int l = (int) Math.ceil((polyomino.maxX- polyomino.minX + reserveSpacing) / stepX);
            int h = (int) Math.ceil((polyomino.maxY- polyomino.minY + reserveSpacing) / stepY);
            
            /*
             * Recalculating spacings with grid size taken into account.
             * stepX represent empty space beetween left polyomino border and most left container.
             * stepY is similar.
             */
            polyomino.spacingX= ( l*stepX - (polyomino.maxX-polyomino.minX) ) / 2;
            polyomino.spacingY= ( h*stepY - (polyomino.maxY-polyomino.minY) ) / 2;
            
            /**
             * (deflection vector) Vector by which every object(container or line) must be moved before calculation to polyomino is done.
             */
            Point2D.Double deflVect=new Point2D.Double(polyomino.spacingX-polyomino.minX,
                                                    polyomino.spacingY-polyomino.minY);
            
            /**
             * Represents a grid (polyomino ) where cells equal to true are taken
             * and cells equal to false are not taken.
             */
            boolean[][] grid= new boolean[l][h];
            
            /*
             * Assigning true values to all places in grid that are taken by containers.
             */
            for(AbstractContainer aCont :conComp.enclosingContainer.getAbstractContainers(false)){
                int jXFrom=(int) Math.floor( (aCont.getLeft()+deflVect.x)/stepX );
                int jXTo=(int) Math.floor( (aCont.getRight()+deflVect.x)/stepX );
                int jYFrom=(int) Math.floor( (aCont.getTop()+deflVect.y)/stepY );
                int jYTo=(int) Math.floor( (aCont.getBottom()+deflVect.y)/stepY );
                
                
                for(int jX=jXFrom; jX<=jXTo; jX++){
                    for(int jY=jYFrom; jY<=jYTo; jY++){
                        grid[jX][jY]=true;
                    }
                }
            }
            
            /*
             * Assigning true values to all grid cells that are crossed by lines.
             */
            
            
            //LayoutLines[] lines=buildInnerLines(conComp);
            for(LayoutLine line: buildInnerLines(conComp)){
                
                Point2D.Double start=line.startElement.getCenter();
                start.x+=deflVect.x;
                start.y+=deflVect.y;
                Point2D.Double end=line.endElement.getCenter();
                end.x+=deflVect.x;
                end.y+=deflVect.y;
                
                /*
                 * Let's simplify calculations by assuming that vector is not showing to the "left"
                 */
                if(end.x<start.x){
                    Point2D.Double tmp=end;
                    end=start;
                    start=tmp;
                }
                
                /*
                 * Calculating vector collinear to line and with |y|==stepY
                 */
                Point2D.Double lineVect=new Point2D.Double(end.x-start.x, end.y-start.y);
                lineVect.x=(lineVect.x*stepY/Math.abs(lineVect.y));
                int stepYGrid;
                if(lineVect.y>=0){
                    lineVect.y=stepY;
                    stepYGrid=1;
                }else{
                    lineVect.y=-stepY;
                    stepYGrid=-1;
                }
                
                /*
                 * Point in grid coord, where line ends
                 */
                int endX=(int) Math.floor(end.x/stepX);
                int endY=(int) Math.floor(end.y/stepY);
                int curX=(int) Math.floor(start.x/stepX);
                int curY=(int) Math.floor(start.y/stepY);
                
                double nextCrWithHorLineX;
                if(stepYGrid==1){
                   nextCrWithHorLineX=start.x+((curY+1)*stepY-start.y)*lineVect.x/lineVect.y;
                }else{
                    nextCrWithHorLineX=start.x+(curY*stepY-start.y)*lineVect.x/lineVect.y;
                }

                int nextCrWithHorLineXInt=(int) Math.floor( nextCrWithHorLineX / stepX );   
                
                /*
                 * In every iteration of cycle some part of a row in grid is assigned true. 
                 * When line goes outside of the row, new row is taken and action repeats.
                 */
                for(; curY!=endY; curY+=stepYGrid){
                    boolean nextCol=false;
                    if(curX<nextCrWithHorLineXInt){
                        nextCol=true;
                    }
                    for(;curX<=nextCrWithHorLineXInt && curX<=endX; curX++){
                        grid[curX][curY]=true;
                    }
                    if(!nextCol){
                        curX--;
                    }
                    nextCrWithHorLineX+=lineVect.x;
                    nextCrWithHorLineXInt=(int)( nextCrWithHorLineX / stepX );
                }
                for(; curX<=endX; curX++){ //this is needed, because previous if will leave one last row in curY==endY
                    grid[curX][curY]=true;
                }
                
                
            }
            
            
            /*
             * Assigning polyomino to coord arrayList.
             */
            polyomino.coord = new ArrayList<>();
            for(int jY=0; jY<h; jY++){
                for(int jX=0; jX<l; jX++){              
                    if(grid[jX][jY]){
                        polyomino.coord.add( new Polyomino.IntegerPoint(jX, jY) );
                    }
                }
            }
            
        }
    }
    
    /**
     * This method builds the lines connecting the elements of this layout part. If a line endpoint
     * goes into deeper nested box, it is connected to a parent that is in the current layout part.
     * Lines going outside of this part are not included in the returned list.
     */
    static private List<LayoutLine> buildInnerLines(DiagramPart part) {
        List<LayoutLine> lines = new ArrayList<>();
        HashSet<Container> boxSet = new HashSet<>(part.children);

        for (Container c : part.containers) {
            for (Line incidentLine : c.getLines()) {
                if (incidentLine.getStart() instanceof Container && incidentLine.getEnd() instanceof Container) {
                    Container start = (Container) incidentLine.getStart();

                    while (start != null && !boxSet.contains(start)) {
                        start = start.getOwner();
                    }

                    Container end = (Container) incidentLine.getEnd();

                    while (end != null && !boxSet.contains(end)) {
                        end = end.getOwner();
                    }

                    if (start != null && end != null) {
                        LayoutLine flatLine = new LayoutLine(incidentLine, start, end);
                        lines.add(flatLine);
                    } else {
                        //edge connecting child to parent throws exception
                        throw new RuntimeException("error in nested lines");
                    }
                }
            }

        }

        return lines;
    }
    
    
}
