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
import java.util.LinkedHashSet;
import lv.lumii.layoutengine.ArrangeData.ArrangeStyle;

/**
 * Part of the diagram with the same layout style. It forms a connected part of the nesting subtree
 * with a common layout style.
 */
class DiagramPart {

    /**
     * The containers of this diagram part whose children to layout according to the part's style.
     */
    LinkedHashSet<Container> containers = new LinkedHashSet<>();
    /**
     * The children of this diagram part's containers to layout according to the part's style.
     */
    ArrayList<Container> children = new ArrayList<>();
    /**
     * The layout style to use in arranging this diagram part.
     */
    private ArrangeStyle style;
    /**
     * The container that is the root of this layout part.
     */
    Container enclosingContainer;
    /**
     * The lines leaving the enclosing container
     */
    LinkedHashSet<Line> outerLines = new LinkedHashSet<>();

    /**
     * Creates a new layout part with a specific layout style which consists of the given container
     * only. This constructor may be used for a single container children layout arrange.
     *
     * @param container the container whose children to layout
     */
    DiagramPart(Container container) {
        containers.add(container);
        children.addAll(container.getContainers());
        style = container.getUsedArrangeStyle();
        enclosingContainer = container;
    }

    /**
     * Merges this part with another.
     *
     * @param childPart the part that has to be added to this part
     */
    void merge(DiagramPart childPart) {
        containers.addAll(childPart.containers);
        children.addAll(childPart.children);
    }

    /**
     * Returns the layout style for this part.
     *
     * @return the layout style for this part.
     */
    ArrangeStyle getStyle() {
        return style;
    }
}
