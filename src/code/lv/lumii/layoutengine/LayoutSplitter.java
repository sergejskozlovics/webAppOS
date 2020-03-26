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

import java.util.ArrayList;
import lv.lumii.layoutengine.ArrangeData.ArrangeStyle;

/**
 * This class performs diagram splitting for layout.
 */
class LayoutSplitter {

    /**
     * The list of parts that should be laid out separately.
     */
    private ArrayList<DiagramPart> parts;
    /**
     * Whether to lay out each container independently. If false, each connected part of the nesting
     * subtree with a common layout style should be laid out separately.
     */
    private boolean splitAll = false;

    /**
     * This method splits the given diagram for layout.
     *
     * @param diagram the given diagram
     * @param splitAll if true, each container should be laid out independently; if false, each
     * connected part of the nesting subtree with a common layout style should be laid out
     * separately
     * @return the list of diagram parts ordered from bottom to top of the nesting tree
     */
    ArrayList<DiagramPart> split(Container diagram, boolean splitAll) {
        this.splitAll = splitAll;
        parts = new ArrayList<>();
        DiagramPart topPart = performSplit(diagram, diagram.getArrangeStyle());

        if (topPart != null) {
            parts.add(topPart);
        }
        return parts;
    }

    /**
     * The recursive method for splitting.
     *
     * @param container the current container whose children have to be considered
     * @param parentStyle the layout style of the parent container
     * @return the diagram part containing children of this container
     */
    private DiagramPart performSplit(Container container, ArrangeStyle parentStyle) {
        ArrayList<Container> children = container.getContainers();

        ArrangeStyle style = container.getArrangeStyle();
        if (style == ArrangeStyle.INHERITED) {
            style = parentStyle;
        }
        DiagramPart currentPart = new DiagramPart(container);

        for (Container child : children) {
            DiagramPart childPart = performSplit(child, style);

            if (childPart != null) {
                if (childPart.getStyle() != style || splitAll) {
                    parts.add(childPart);
                } else {
                    currentPart.merge(childPart);
                }
            }
        }

        if (currentPart.children.isEmpty()) {
            currentPart = null;
        }
        return currentPart;
    }
}
