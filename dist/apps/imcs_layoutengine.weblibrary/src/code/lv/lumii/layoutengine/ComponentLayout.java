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
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Layout algorithm for a part of the diagram with a common layout style
 */
abstract class ComponentLayout {

    /**
     * The part of the diagram that should be laid out
     */
    protected DiagramPart part;
    /**
     * The algorithm performing the layout of the nesting hierarchy
     */
    protected LayoutAlgorithm baseLayouter;

    /**
     * A technical constructor.
     *
     * @param part The part of the diagram that should be laid out
     */
    public ComponentLayout(DiagramPart part, LayoutAlgorithm baseLayouter) {
        this.part = part;
        this.baseLayouter = baseLayouter;
    }

    /**
     * performs one pass of the layout. This method should be overridden in each subclass
     * corresponding to some layout style.
     *
     * @param pass Layout of a nested diagram is performed in 3 passes. The first pass is bottom-up;
     * the container sizes are calculated in this pass The second pass is top-down; an approximate
     * positions of containers are calculated The 3rd pass is bottom-up; the exact sizes and
     * positions of containers are calculated. All passes should respect the information calculated
     * in previous passes.
     */
    protected abstract void layoutPass(int pass);

    /**
     * This method creates an appropriate layout algorithm for the layout style of this part.
     *
     * @param part the part that should be laid out
     * @param baseLayouter the layout algorithm performing the whole layout
     * @return the appropriate layout algorithm
     */
    public static ComponentLayout createLayouter(DiagramPart part, LayoutAlgorithm baseLayouter) {
        switch (part.getStyle()) {
            case SPRING_EMBEDDED:
                return new SpringLayoutAlgorithm(part, baseLayouter);
            case FLOW:
                return new FlowLayoutAlgorithm(part, baseLayouter);
            case UNIVERSAL:
                return new UniversalLayoutAlgorithm(part, baseLayouter);
            default:
                throw new UnsupportedOperationException("Unsupported layout style " + part.getStyle().toString());
        }
    }

    /**
     * This method builds the lines connecting the elements of this layout part. If a line endpoint
     * goes into deeper nested box, it is connected to a parent that is in the current layout part.
     * Lines going outside of this part are not included in the returned list.
     */
    protected List<LayoutLine> buildInnerLines() {
        List<LayoutLine> lines = new ArrayList<>();
        HashSet<Container> boxSet = new HashSet<>(part.children);

        for (Container c : part.containers) { //at the moment there is only one container- enclosingContainer
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

    /**
     * This method returns the latest pass in which the given container was positioned.
     *
     * @param c the container
     * @return the pass number. Returns 0, if the container is never positioned
     */
    protected int getLaidOutPass(Container c) {
        Integer pass = baseLayouter.laidOutInPass.get(c);
        if (pass == null) {
            return 0;
        }

        return pass;
    }

    /**
     * This method returns the list of outer containers adjacent to the given one in the flattened
     * nesting hierarchy.
     */
    protected List<Container> getOuterNeighbors(Container c) {
        ArrayList<Container> neighbors = new ArrayList<>();

        if (c instanceof Connectible) {
            Connectible box = (Connectible) c;

            // first check connecting lines
            for (Line adj : box.getIncidentLines()) {
                if (part.outerLines.contains(adj)) {
                    Element other = adj.getStart();
                    if (other == c) {
                        other = adj.getEnd();
                    }
                    if (other instanceof Container) {
                        neighbors.add((Container) other);
                    }
                }
            }

            // check lines leaving c
            DiagramPart nestedPart = this.baseLayouter.componentPartMap.get(c);

            if (nestedPart != null) {
                for (Line adj : nestedPart.outerLines) {
                    if (part.outerLines.contains(adj)) {
                        Element other = adj.getStart();

                        while (other != null) {
                            other = other.getOwner();
                            if (other == c) {
                                break;
                            }
                        }

                        if (other == c) {
                            other = adj.getEnd();
                        } else {
                            other = adj.getStart();
                        }

                        if (other instanceof Container) {
                            neighbors.add((Container) other);
                        }
                    }
                }
            }

        }

        return neighbors;
    }

    /**
     * Moves the box and its descendant containers to the new place.
     *
     * @param box the box to be moved
     * @param newCenterX the x coordinate of the new center
     * @param newCenterY the y coordinate of the new center
     */
    protected void moveBoxWithChildren(Container box, double newCenterX, double newCenterY) {
        Point2D.Double moveVector = new Point2D.Double(newCenterX - box.getCenterX(), newCenterY - box.getCenterY());
        box.transpose(moveVector);

        for (Container descendantBox : box.getDescendantContainers()) {
            descendantBox.transpose(moveVector);
        }
    }
    
    protected void moveAbstrContWithChildren(AbstractContainer aCont, double newCenterX, double newCenterY){
        Point2D.Double moveVector = new Point2D.Double(newCenterX - aCont.getCenterX(), newCenterY - aCont.getCenterY());
        aCont.transpose(moveVector);
        for (AbstractContainer descendantACont : aCont.getDescendantAbstractContainers(false)) {
            descendantACont.transpose(moveVector);
        }
    }
}
