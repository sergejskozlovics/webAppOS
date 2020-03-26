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
import java.util.*;
import lv.lumii.layoutengine.Box.BoxSide;
import lv.lumii.layoutengine.LayoutConstraints.GridLayoutConstraints;
import lv.lumii.layoutengine.OutsideLabel.BoxOutsideLabel;
import lv.lumii.layoutengine.util.LayoutLine;

/**
 *
 * This class implements the general framework for performing layout on nested graphs
 */
class LayoutAlgorithm {

    /**
     * The pass number for each container in which it is laid out
     */
    Map<AbstractContainer, Integer> laidOutInPass;
    /**
     * Mapping from enclosing containers to diagramParts
     */
    Map<AbstractContainer, DiagramPart> componentPartMap;

    /**
     * This method performs the layout
     *
     * @param diagram the diagram to arrange
     */
    public void layout(Diagram diagram) {
        LayoutSplitter spliter = new LayoutSplitter();
        ArrayList<DiagramPart> parts = spliter.split(diagram, true);//TODO: currently full spliting is performed

        ArrayList<DiagramPart[]> parts2=new ArrayList<>();
        for(DiagramPart part: parts){
            ArrayList<DiagramPart> conComponents=splitToConComp(part);
            DiagramPart[] conCompArr = conComponents.toArray(new DiagramPart[conComponents.size()]);
            parts2.add(conCompArr);
        }
        

        laidOutInPass = new HashMap<>();
        componentPartMap = new HashMap<>();

        // create map of which DiagramPart corresponds to which container
        // TODO: what to do ion case when parts are more than one contianer? 
        for (DiagramPart part : parts) {
            componentPartMap.put(part.enclosingContainer, part);
        }

        // create a lyouter for each component
        ArrayList<ComponentLayout[]> layouters2=new ArrayList<>();

        for (int i=0; i<parts2.size(); i++){
            DiagramPart[] partArr=parts2.get(i);
            layouters2.add(i, new ComponentLayout[partArr.length] );
            
            ComponentLayout[] layouterArr=layouters2.get(i);
            for(int j=0; j<partArr.length; j++){
                layouterArr[j]=ComponentLayout.createLayouter(partArr[j], this);
            }         
        }

        buildOuterLines(diagram);

        // set min size; add margins
        //is same thing needed for insideLabels???
        for(ComponentLayout[] layouterArr: layouters2){
            for (ComponentLayout layouter : layouterArr) {
                for (AbstractContainer aC : layouter.part.enclosingContainer.getAbstractContainers(false)) {
                    double w = 0, h = 0;
                    if (aC instanceof Box) {
                        Box b = (Box) aC;
                        w=aC.getCurrentMinWidth();
                        
                        w = b.getMinWidth() + 2 * b.getSpacing();
                        h = b.getMinHeight() + 2 * b.getSpacing();
                        b.setCurrentMinSize(b.getMinWidth(), b.getMinHeight());
                    }

                    aC.setBounds(new Rectangle2D.Double(0, 0, w, h));
                }
            }
        }

        // perform 3 passes

        for(int i=0; i<parts.size(); i++){
            ComponentLayout[] curLayout=layouters2.get(i);
            for(int j=0; j<curLayout.length; j++){
                curLayout[j].layoutPass(1);
            }
            
            ConnectedComponentPacking.pack(parts2.get(i), 100, 1, 1);
            
            adjustBounds(parts.get(i).enclosingContainer);
            assignLayoutNumbers(parts.get(i), 1);
        }
        
        for(int i=parts.size()-1; i>=0; i--){
            adjustBoundsSize(parts.get(i).enclosingContainer);
            assignLayoutNumbers(parts.get(i), 2);
        }
        
        for(int i=0; i<parts.size(); i++){
            adjustBoundsSize(parts.get(i).enclosingContainer);
            assignLayoutNumbers(parts.get(i), 3);            
        }
        
        
        
        /*
        for (ComponentLayout layouter : layouters) {
            layouter.layoutPass(1);
            
            // in the first pass set the parent rectangle as the bounding rectangle of children
            adjustBounds(layouter.part.enclosingContainer);
            assignLayoutNumbers(layouter.part, 1);
        }
        */

 /*
        for (ListIterator<ComponentLayout> iter = layouters.listIterator(layouters.size()); iter.hasPrevious();) {
            ComponentLayout layouter = iter.previous();
            layouter.layoutPass(2);

            // in the second pass only adjust the size of the container, keeping the old position
            // the new position should be adjusted by layout
            adjustBoundsSize(layouter.part.enclosingContainer);// maybe not necessary?
            assignLayoutNumbers(layouter.part, 2);
        }

        for (ComponentLayout layouter : layouters) {
            layouter.layoutPass(3);
            // in the third pass only adjust the size of the container, keeping the old position
            // the new position should be adjusted by layout
            adjustBoundsSize(layouter.part.enclosingContainer);
            assignLayoutNumbers(layouter.part, 3);
        }
*/
        
        
        /*
         * First, arranges the boxes of the diagram. Second, retraces all lines.
         */
        // collapse everything
        // not needed, done in Normalizer.arrange
//        for (ComponentLayout layouter : layouters) {
//         for (Container c : layouter.part.children) {
//         if (c instanceof Box) {
//         Box b = (Box) c;
//         b.setCurrentMinSize(b.getMinWidth(), b.getMinHeight());// restore enlarged box size
//         b.collapse(b.getCenter());
//         }
//         }
//         }
//        diagram.updateBoundsFromRectangles();
        // normalize
        Normalizer.arrange(diagram);

        for (BoxOutsideLabel label : diagram.getDescendantBoxOutsideLabels()) {
            BoxSide side = label.side;
            Box box = label.getOwner();
            if (side.isHorizontal()) {
                label.setCenter(new Point2D.Double(box.left + label.position * box.getWidth(), 0));
            } else {
                label.setCenter(new Point2D.Double(0, box.top + label.position * box.getHeight()));
            }
        }

        
        diagram.startTransaction();
        ArrayList<Line> lines = diagram.getDescendantLines();
        for (Line line : lines) {
            line.retrace(line.getType());
        }
        diagram.endTransaction();
        LineOptimizer.arrangeLineEnds(diagram);
        diagram.adjust();
        for (Line line : lines) {
            line.resetLayoutStartSides();
            line.resetLayoutEndSides();
        }

        Adjuster.growOutsideLabels(diagram);
    }

    /**
     * This method adjusts the size of the container to include its children. The center is kept
     * fixed
     *
     * @param enclosingContainer the container to be adjusted
     */
    private void adjustBoundsSize(Container enclosingContainer) {
        Rectangle2D childBounds = enclosingContainer.findChildContainerEnclosingRectangle();

        if (childBounds == null) {
            childBounds = new Rectangle2D.Double(0, 0, 0, 0);
        }

        double w = childBounds.getWidth(), h = childBounds.getHeight();

        if (enclosingContainer instanceof Box) {
            Box b = (Box) enclosingContainer;
            w = Math.max(w, b.getMinWidth() + 2 * b.getSpacing());
            h = Math.max(h, b.getMinHeight() + 2 * b.getSpacing());
            //b.setCurrentMinSize(w, h);
        }

        Point2D.Double center = enclosingContainer.getCenter();
        enclosingContainer.setBounds(new Rectangle2D.Double(center.x - w / 2, center.y - h / 2, w, h));
    }

    /**
     * This method sets the bounds of the container to match its children
     *
     * @param enclosingContainer the container to be adjusted
     */
    private void adjustBounds(Container enclosingContainer) {
        Rectangle2D childBounds = enclosingContainer.findChildContainerEnclosingRectangle();

        if (childBounds == null) {
            childBounds = new Rectangle2D.Double(0, 0, 0, 0);
        }

        double w = childBounds.getWidth(), h = childBounds.getHeight();

        if (enclosingContainer instanceof Box) {
            Box b = (Box) enclosingContainer;
            w = Math.max(w, b.getMinWidth() + 2 * b.getSpacing());
            h = Math.max(h, b.getMinHeight() + 2 * b.getSpacing());
            //b.setCurrentMinSize(w, h);            
        }

        enclosingContainer.setBounds(new Rectangle2D.Double(childBounds.getCenterX() - w / 2, childBounds.getCenterY() - h / 2, w, h));
    }

    /**
     * Assigns the latest pass number for each container in which it is laid out.
     */
    private void assignLayoutNumbers(DiagramPart part, int passNo) {
        for (Container c : part.children) {
            this.laidOutInPass.put(c, passNo);
        }
    }

    /**
     * builds the lists of lines crossing the borders of containers
     *
     * @param diagram
     */
    private void buildOuterLines(Diagram diagram) {

        for (Line a : diagram.getDescendantLines()) {
            if (a.getStart() instanceof Container && a.getEnd() instanceof Container) //TODO: lines to other lines
            {
                Container start = (Container) a.getStart().getOwner();
                Container end = (Container) a.getEnd().getOwner();
                Container commonOwner = a.getOwner();

                while (start != commonOwner) {
                    DiagramPart part = this.componentPartMap.get(start);
                    part.outerLines.add(a);
                    start = start.getOwner();
                }

                while (end != commonOwner) {
                    DiagramPart part = this.componentPartMap.get(end);
                    part.outerLines.add(a);
                    end = end.getOwner();
                }
            }
        }
    }

    /**
     * This method splits DiagramPart to connected components. 
     * All connected components that have at least one AbstractContainer in grid
     * are put into one connected component.
     * @param part
     * @return 
     */
    private ArrayList<DiagramPart> splitToConComp(DiagramPart part) {

        GridLayoutConstraints constr=null;
        
        LayoutConstraints abstractConstr = part.enclosingContainer.getLayoutConstraints();
        if (abstractConstr.getType() == LayoutConstraints.ConstraintType.GRID) {
            constr = (LayoutConstraints.GridLayoutConstraints) abstractConstr;
        }

        List<LayoutLine> lines = buildInnerLines(part);
        ArrayList<DiagramPart> conComponents = new ArrayList<>();
        ArrayList<AbstractContainer> aContainers= part.enclosingContainer.getAbstractContainers(false);

        
        /*creating incident list*/
        HashMap<AbstractContainer, Integer> map = new HashMap<>();
        for (int i = 0; i < aContainers.size(); i++) {
            map.put(aContainers.get(i), i);
        }


        ArrayList<ArrayList<Integer>> incList = new ArrayList<>();
        
        /*
         * Each containers list with every line that starts from it.
         */
        ArrayList<ArrayList<LayoutLine>> contLines = new ArrayList<>();
        for (int i = 0; i < aContainers.size(); i++) {
            incList.add(i, new ArrayList<Integer>());
            contLines.add(i, new ArrayList<LayoutLine>());
        }
        
        //putting values into incident list
        for (int i = 0; i < lines.size(); i++) {
            LayoutLine line = lines.get(i);
            Container contA = line.startElement;
            Container contB = line.endElement;   
            Integer indexA = map.get(contA);
            Integer indexB = map.get(contB);
            if (!incList.get(indexA).contains(indexB)) {
                incList.get(indexA).add(indexB);
            }
            if (!incList.get(indexB).contains(indexA)) {
                incList.get(indexB).add(indexA);
            }
            contLines.get(indexA).add(line);
        }

        /*end of creating incident list*/

        /*finding connected components with BFS*/

        Container enclosingContCopy; //=part.enclosingContainer.getEmptyClone(true);   
        Container enclosingContCopyForGrid=part.enclosingContainer.getEmptyClone(true);
        //DiagramPart conCompForGrid = new DiagramPart(enclosingContCopy);        
        
        boolean[] contVisited = new boolean[aContainers.size()];
        for (int i = 0; i < contVisited.length; i++) {
            if (!contVisited[i]) {
                boolean compInGrid=false;
                enclosingContCopy=part.enclosingContainer.getEmptyClone(false);
                LinkedList<Integer> queue = new LinkedList<>();
                queue.add(i);
                contVisited[i] = true;
                while (!queue.isEmpty()) {
                    int t = queue.removeFirst();
                    AbstractContainer aCont=aContainers.get(t);
                    if(constr!=null){
                        if(constr.getRow(aCont)!=null || constr.getColumn(aCont)!=null){ //if cont is in grid then all component also is in grid
                            compInGrid=true;
                        }
                    }
                    
                    Class classOfACont= aCont.getClass();
                    if(classOfACont == Container.class){
                        enclosingContCopy.addContainer((Container)aCont);
                    }else if(classOfACont == InsideLabel.class){
                        enclosingContCopy.addInsideLabel((InsideLabel)aCont);
                    }else if(classOfACont == Box.class){ 
                        enclosingContCopy.addBox((Box)aCont);
                    }
                    
                    /*
                     * Adding lines to enclosing container
                     */
                    ArrayList<LayoutLine> containerLines= contLines.get(t);
                    if(containerLines!=null){
                        for(LayoutLine line: containerLines){
                            enclosingContCopy.addLine(line.originalLine);
                        }
                    }
                        
                    for (int j : incList.get(t)) {
                        if (!contVisited[j]) {
                            contVisited[j] = true;
                            queue.add(j);
                        }
                    }
                }
                
                if(!compInGrid){
                    DiagramPart conComp=new DiagramPart(enclosingContCopy);
                    conComponents.add(conComp);
                }else{
                    enclosingContCopyForGrid.containers.addAll(enclosingContCopy.getPureContainers());
                    enclosingContCopyForGrid.insideLabels.addAll(enclosingContCopy.getInsideLabels());
                    enclosingContCopyForGrid.boxes.addAll(enclosingContCopy.getBoxes());
                    enclosingContCopyForGrid.lines.addAll(enclosingContCopy.getLines());
                }
            }
        }
        
        if(constr!=null && (!enclosingContCopyForGrid.containers.isEmpty()
                || !enclosingContCopyForGrid.insideLabels.isEmpty()
                || !enclosingContCopyForGrid.boxes.isEmpty())){
            DiagramPart conComp=new DiagramPart(enclosingContCopyForGrid);
            conComponents.add(conComp);
        }
        
        return conComponents;
    }
	
    
    
    /**
     * This method builds the lines connecting the elements of this layout part. If a line endpoint
     * goes into deeper nested box, it is connected to a parent that is in the current layout part.
     * Lines going outside of this part are not included in the returned list.
     */
    private List<LayoutLine> buildInnerLines(DiagramPart part) {
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