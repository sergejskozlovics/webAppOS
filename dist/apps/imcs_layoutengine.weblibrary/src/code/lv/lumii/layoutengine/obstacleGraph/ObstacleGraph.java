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

package lv.lumii.layoutengine.obstacleGraph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.NavigableSet;
import java.util.TreeSet;
import lv.lumii.layoutengine.obstacleGraph.Segment.VerticalPart;
import lv.lumii.layoutengine.util.Pair;

/**
 * Contains methods for calculating the obstacle graph.
 *
 * @author jk
 */
public class ObstacleGraph {

    /**
     * Finds the obstacle graph of the given {@code ArrayList} of {@code OrthogonalSegment}
     * segments.
     *
     * @param <S> The segment class with which this obstacle graph will work.
     * @param segments the vertical segments for which to find the obstacle graph
     * @return an {@code ArrayList} of pairs of vertical segments, with each pair corresponding to
     * an edge in the obstacle graph going from the first segment of the pair to the second.
     */
    public static <S extends Segment> ArrayList<Pair<S, S>> findObstacleGraph(ArrayList<S> segments) {
        ArrayList<Pair<S, S>> graph = new ArrayList<>();

        Collections.sort(segments);

        TreeSet<VerticalPart<S>> sweepline = new TreeSet<>();
        /*
         * Adds dummy buffer parts to both ends of the sweepline, prevents analysing special cases.
         */
        sweepline.add(new VerticalPart<S>(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, null));
        sweepline.add(new VerticalPart<S>(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, null));

        /*
         * The main cycle through the segments in order from left to right.
         */
        for (S segment : segments) {
            /*
             * The parts to start and end checking overlap with the current segment from.
             */
            VerticalPart<S> startPart = sweepline.lower(new VerticalPart<>(segment.getStart(), segment.getStart(), segment));
            /*
             * A TreeSet containing the parts to check for overlap with the current segment.
             * Includes both startPart and endPart.
             */
            NavigableSet<VerticalPart<S>> parts = sweepline.tailSet(startPart, startPart.intersects(segment));
            startPart = parts.first();
            /*
             * The parts to iterate through, with mid being the part to currently check.
             */
            VerticalPart<S> upper, mid, lower;
            Iterator<VerticalPart<S>> it = parts.iterator();
            upper = sweepline.first();
            mid = it.next();
            /*
             * The cycle through the parts, stops when the current part is below the current
             * segment.
             */
            while (mid.intersects(segment)) {
                it.remove();
                lower = it.hasNext() ? it.next() : null;
                /*
                 * If the current part intersects the current segment and its original segment is
                 * not blocked by the parts neighbours, adds an edge to the obstacle graph.
                 */
                S originalSegment = mid.getSegment();
                if (!(upper.intersects(segment) && upper.getSegment().getPos() > originalSegment.getPos()
                        && upper.getBottom() > mid.getSegment().getStart())) {
                    if (!(lower.intersects(segment) && lower.getSegment().getPos() > originalSegment.getPos()
                            && mid.getSegment().getEnd() > lower.getTop())) {
                        graph.add(new Pair<>(originalSegment, segment));
                    }
                }
                /*
                 * Advances the part cycle.
                 */
                upper = mid;
                mid = lower;
            }

            /*
             * The current segment always gets added to the sweepline whole.
             */
            VerticalPart<S> segmentPart = new VerticalPart<>(segment);
            sweepline.add(segmentPart);
            if (startPart.intersects(segment)
                    && startPart.getTop() < segmentPart.getTop()
                    && startPart.getBottom() > segmentPart.getTop()) {
                sweepline.add(new VerticalPart<>(startPart.getTop(), segmentPart.getTop(), startPart.getSegment()));
            }
            if (upper.intersects(segment)
                    && upper.getBottom() > segmentPart.getBottom()
                    && upper.getTop() < segmentPart.getBottom()) {
                sweepline.add(new VerticalPart<>(segmentPart.getBottom(), upper.getBottom(), upper.getSegment()));
            }
        }
        return graph;
    }
}
