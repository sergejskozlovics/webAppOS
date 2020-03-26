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

/**
 * Orthogonal segment for obstacle graph calculation.
 *
 * @author jk
 */
public class Segment implements Comparable<Segment> {

    /**
     * The position of the segment.
     */
    public double pos;
    /**
     * The start coordinate of the segment.
     */
    public double start;
    /**
     * The end coordinate of the segment.
     */
    public double end;

    //<editor-fold defaultstate="collapsed" desc="constructors">
    /**
     * Creates a new Segment object.
     *
     * @param pos the position of the new Segment
     * @param start the start of the new Segment
     * @param end the end of the new Segment
     */
    public Segment(double pos, double start, double end) {
        this.pos = pos;
        this.start = start;
        this.end = end;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="accessors">
    /**
     * Returns the segments position.
     *
     * @return the segments position.
     */
    public double getPos() {
        return pos;
    }

    /**
     * Changes the position of the segment without moving the corresponding element.
     *
     * @param pos the new abscissa
     */
    public void setPos(double pos) {
        this.pos = pos;
    }

    /**
     * Returns the start position of the segment.
     *
     * @return the start position of the segment.
     */
    public double getStart() {
        return start;
    }

    /**
     * Returns the end position of the segment.
     *
     * @return the end position of the segment.
     */
    public double getEnd() {
        return end;
    }

    /**
     * Sets the start position of the segment.
     *
     * @param start the new start position of the segment.
     */
    public void setStart(double start) {
        this.start = start;
    }

    /**
     * Sets the end position of the segment.
     *
     * @param end the new end position of the segment.
     */
    public void setEnd(double end) {
        this.end = end;
    }
    //</editor-fold>

    /**
     * Compares the two specified segments by their {@code pos} value.
     *
     * @param s the segment to compare to
     * @return the value 0 if both {@code pos} are numerically equal; a value less than 0 if the
     * value of the first {@code pos} is numerically less than that of the second; and a value
     * greater than 0 if d1 the value of the first {@code pos} is numerically greater than that of
     * the second.
     */
    @Override
    public int compareTo(Segment s) {
        return Double.compare(pos, s.pos);
    }

    /**
     * A class for storing sweep-line segment projections on vertical axis.
     *
     * @param <S> the segment class whose projections the parts will store.
     */
    public static class VerticalPart<S extends Segment> implements Comparable {

        //<editor-fold defaultstate="collapsed" desc="attributes">
        /**
         * The top ordinate of the part.
         */
        private final double top;
        /**
         * The bottom ordinate of the part.
         */
        private final double bottom;
        /**
         * The segment the part belongs to.
         */
        private final S segment;
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="constructors">
        /**
         * Creates a new VerticalPart object.
         *
         * @param top the top ordinate of the part
         * @param bottom the bottom ordinate of the part
         * @param segment the ID of the segment this part belongs to
         */
        public VerticalPart(double top, double bottom, S segment) {
            this.top = top;
            this.bottom = bottom;
            this.segment = segment;
        }

        /**
         * Creates a new VerticalPart object as a projection of a Segment instance.
         *
         * @param segment the Segment to project
         */
        public VerticalPart(S segment) {
            this.top = segment.getStart();
            this.bottom = segment.getEnd();
            this.segment = segment;
        }
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="accessors">
        /**
         * Returns the top ordinate of the part.
         *
         * @return the top ordinate of the part.
         */
        public double getTop() {
            return top;
        }

        /**
         * Returns the bottom ordinate of the part.
         *
         * @return the bottom ordinate of the part.
         */
        public double getBottom() {
            return bottom;
        }

        /**
         * Returns the ID of the segment this part belongs to.
         *
         * @return the ID of the segment this part belongs to.
         */
        public S getSegment() {
            return segment;
        }
        //</editor-fold>

        /**
         * Returns whether this part intersects the projection of the given segment.
         *
         * @param segment - the segment to check for intersection
         * @return whether this part intersects the projection of the given segment.
         */
        public boolean intersects(S segment) {
            return Math.max(top, segment.getStart()) < Math.min(bottom, segment.getEnd());
        }

        /**
         * Compares this part to another {@code VerticalPart}. Compares with a priority to the top
         * endpoint, than the bottom one.
         *
         * @param op the {@code VerticalPart} to compare to
         * @return -1 if this part is smaller (higher than p), 0 if they are equal and 1 if this
         * part is lower.
         */
        @Override
        public int compareTo(Object op) {
            VerticalPart p = (VerticalPart) op;
            int cmp = top < p.top ? -1 : top > p.top ? 1 : 0;//((Double) top).compareTo(p.top);
            return cmp == 0 ? bottom < p.bottom ? -1 : bottom > p.bottom ? 1 : 0 : cmp;//((Double) bottom).compareTo(p.bottom) : cmp;
        }
    }
}
