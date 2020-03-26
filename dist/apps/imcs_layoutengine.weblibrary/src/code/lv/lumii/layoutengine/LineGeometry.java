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
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.ListIterator;
import lv.lumii.layoutengine.Box.BoxSide;
import lv.lumii.layoutengine.Line.LineType;
import lv.lumii.layoutengine.OutsideLabel.LineLabel;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * This class defines the geometrical representation of the line.
 *
 * @author k
 */
abstract class LineGeometry {

    //<editor-fold defaultstate="collapsed" desc="attributes">
    /**
     * The list contains the base points of the line.
     */
    protected ArrayList<Point2D.Double> points;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="accessors">
    /**
     * Returns the list with the line's base points.
     *
     * @return the list with the line's base points.
     */
    public ArrayList<Point2D.Double> getPoints() {
        return points;
    }

    /**
     * Returns the first point of the line.
     *
     * @return the first point of the line.
     */
    public Point2D.Double getStartPoint() {
        return points.get(0);
    }

    /**
     * Returns the last point of the line.
     *
     * @return the last point of the line.
     */
    public Point2D.Double getEndPoint() {
        return points.get(points.size() - 1);
    }

    /**
     * Returns the type of the line.
     *
     * @return the type of the line.
     */
    abstract LineType getType();
    //</editor-fold>

    /**
     * Removes unnecessary points from the line.
     */
    abstract void cullPoints();

    /**
     * Defines the line geometry by setting point coordinates of line vertices. For each line type
     * also checks whether the given points confirm to the specification of the type.
     *
     * @param points the new points of the line.
     */
    abstract void setPoints(ArrayList<Point2D.Double> points);

    /**
     * Removes unnecessary points from the line while also updating the segment indices of the
     * line's labels.
     *
     * @param labels the labels of this line whose segment indices to update
     */
    abstract void cullPoints(Collection<LineLabel> labels);

    /**
     * Returns the full length of this line. Traverses the line to calculate this.
     *
     * @return the full length of this line
     */
    double getLength() {
        double length = 0;
        for (int i = 0; i + 1 < points.size(); i++) {
            length += GeometryHelper.findDistance(points.get(i), points.get(i + 1));
        }
        return length;
    }

    /**
     * Returns the length of this line from point number {@code startIndex} to point number
     * {@code endIndex}. Traverses the line to calculate this.
     *
     * @param startIndex the index of the start point of the line part whose length to return
     * @param endIndex the index of the end point of the line part whose length to return
     * @return the length of the line part between the given points
     */
    double getLength(int startIndex, int endIndex) {
        double length = 0;
        for (int i = startIndex; i < endIndex; i++) {
            length += GeometryHelper.findDistance(points.get(i), points.get(i + 1));
        }
        return length;
    }

    /**
     * Returns whether the line's points currently form an orthogonal line.
     *
     * @return whether the line's points currently form an orthogonal line
     */
    boolean isOrthogonal() {
/*    	System.out.println(points.size());
    	 for (int i = 0; i + 1 < points.size(); i++) {
         	System.out.println(points.get(i).y+" "+points.get(i + 1).y+" "+points.get(i).x+" "+points.get(i+1).x);
    	 }*/
        for (int i = 0; i + 1 < points.size(); i++) {
            if (points.get(i).y != points.get(i + 1).y
                    && points.get(i).x != points.get(i + 1).x) {
            	
            	// added by SK >>: small adjust
            	if (Math.abs( points.get(i).y - points.get(i + 1).y) <=5) {
            		points.get(i+1).y = points.get(i).y;
            	}
            	else
                	if (Math.abs( points.get(i).x - points.get(i + 1).x) <=5) {
                		points.get(i+1).x = points.get(i).x;
                	}
                	else
                // added by SK <<
                		return false;
            }
        }
        return true;
    }

    /**
     * Creates a new XML element corresponding to this line geometry in the given document.
     *
     * @param doc the XML document in which to create the new element
     * @param idMap a map from diagram objects to element storage IDs.
     * @return a new element corresponding to this line geometry
     */
    Node saveToXML(Document doc, LinkedHashMap<Object, Integer> idMap) {
        org.w3c.dom.Element e = doc.createElement(XMLHelper.NAMESPACE + ":lineGeometry");
        e.setAttribute("lineType", getType().toString());
        for (Point2D.Double p : points) {
            e.appendChild(XMLHelper.save(doc, p, XMLHelper.NAMESPACE + ":point"));
        }
        return e;
    }

    /**
     * Creates a new line geometry from the given XML element corresponding to one.
     *
     * @param e an XML element corresponding to a line geometry
     * @return the new line geometry
     */
    static LineGeometry loadFromXML(Element e) {
        Line.LineType type = Line.LineType.valueOf(e.getAttribute("lineType"));
        LineGeometry lg = null;
        switch (type) {
            case ORTHOGONAL:
                lg = new OrthogonalLine();
                break;
            case STRAIGHT:
                lg = new StraightLine();
                break;
            case POLYLINE:
                lg = new Polyline();
                break;
        }
        lg.points = new ArrayList<>();
        NodeList points = e.getElementsByTagName(XMLHelper.NAMESPACE + ":point");
        for (int i = 0; i < points.getLength(); i++) {
            org.w3c.dom.Element point = (org.w3c.dom.Element) points.item(i);
            lg.points.add(XMLHelper.loadPoint(point));
        }
        return lg;
    }

    //<editor-fold defaultstate="collapsed" desc="StraightLine">
    /**
     * This class defines the straight line type.
     */
    static class StraightLine extends LineGeometry {

        /**
         * Creates a new straight line.
         *
         * @param startElement the start element of the line
         * @param endElement the end element of the line
         */
        StraightLine(Connectible startElement, Connectible endElement) {
            if (startElement instanceof Box && endElement instanceof Box) {
                Box a = (Box) startElement, b = (Box) endElement;
                points = new ArrayList<>();
                points.add(a.getCenter());
                points.add(b.getCenter());
            } else {
                throw new UnsupportedOperationException("Lines conneting to lines not implemented yet.");
            }
        }

        /**
         * Creates a straight line with no points, for use in XML loading.
         */
        private StraightLine() {
        }

        /**
         * Creates a new straight line going along the given vertices.
         *
         * @param startElement the start element of the line
         * @param endElement the end element of the line
         * @param points the vertices of the line
         */
        public StraightLine(Connectible startElement, Connectible endElement, ArrayList<Point2D.Double> points) {
            if (startElement instanceof Box && endElement instanceof Box) {
                setPoints(points);
            } else {
                throw new UnsupportedOperationException("Lines conneting to lines not implemented yet.");
            }
        }

        @Override
        LineType getType() {
            return LineType.STRAIGHT;
        }

        @Override
        void cullPoints() {
            if (points.size() > 2) {
                ArrayList<Point2D.Double> newPoints = new ArrayList<>();
                newPoints.add(points.get(0));
                newPoints.add(points.get(points.size() - 1));
                points = newPoints;
            }
        }

        @Override
        void cullPoints(Collection<LineLabel> labels) {
            for (LineLabel label : labels) {
                label.segmentIndex = 0;
            }
            cullPoints();
        }

        @Override
        final void setPoints(ArrayList<Point2D.Double> points) {
            /*
             * Checks the minimal size of the array.
             */
            if (points.size() < 2) {
                throw new IllegalArgumentException("The number of points is less than 2.");
            }

            this.points = new ArrayList<>();
            this.points.add(points.get(0));
            this.points.add(points.get(points.size() - 1));
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="OrthogonalLine">
    /**
     * This class defines the orthogonal line type.
     */
    static class OrthogonalLine extends LineGeometry {

        /**
         * The side of the start box this line connects to, if any.
         */
        BoxSide startSide;
        /**
         * The side of the end box this line connects to, if any.
         */
        BoxSide endSide;

        /**
         * Creates and traces a new orthogonal line.
         *
         * @param startElement the start element of the line
         * @param endElement the end element of the line
         * @param tracer the Tracer with which to trace this line
         * @param startSides the mask denoting the allowed start box sides for this line
         * @param endSides the mask denoting the allowed end box sides for this line
         */
        OrthogonalLine(Connectible startElement, Connectible endElement, Tracer tracer, int startSides, int endSides) {
            if (startElement instanceof Box && endElement instanceof Box) {
                points = tracer.trace((Box) startElement, (Box) endElement, startSides, endSides);
                cullPoints();
            } else {
                throw new UnsupportedOperationException("Lines conneting to lines not implemented yet.");
            }
        }

        /**
         * Creates a orthogonal line with no points, for use in XML loading.
         */
        private OrthogonalLine() {
        }

        /**
         * Creates a new orthogonal line going along the given vertices.
         *
         * @param startElement the start element of the line
         * @param endElement the end element of the line
         * @param points the vertices of the line
         */
        OrthogonalLine(Connectible startElement, Connectible endElement, ArrayList<Point2D.Double> points) {
            if (startElement instanceof Box && endElement instanceof Box) {
                setPoints(points);
                cullPoints();
            } else {
                throw new UnsupportedOperationException("Lines conneting to lines not implemented yet.");
            }
        }

        /**
         * Creates a new orthogonal line connecting the given elements, ignoring any overlaps. For
         * use in manual mode.
         *
         * @param startElement the start of the new line
         * @param endElement the end of the new line
         * @param spacing the spacing of the new line
         */
        OrthogonalLine(Connectible startElement, Connectible endElement, double spacing) {
            if (startElement instanceof Box && endElement instanceof Box) {
                Box start = (Box) startElement, end = (Box) endElement;
                if (start == end) {
                    points = Tracer.connectOneBox(start, 0b1111, 0b1111, spacing);
                } else {
                    points = new ArrayList<>();
                    double l = Math.max(start.left, end.left);
                    double r = Math.min(start.right, end.right);
                    double t = Math.max(start.top, end.top);
                    double b = Math.min(start.bottom, end.bottom);
                    if (l < r) {
                        double x = (l + r) / 2;
                        double[] ay = {start.top, start.bottom}, by = {end.top, end.bottom};
                        int ai = 0, bi = 0;
                        double dist = Double.POSITIVE_INFINITY;
                        for (int i = 0; i < 2; i++) {
                            for (int j = 0; j < 2; j++) {
                                double newDist = Math.abs(ay[i] - by[j]);
                                if (newDist < dist) {
                                    dist = newDist;
                                    ai = i;
                                    bi = j;
                                }
                            }
                        }
                        points.add(new Point2D.Double(x, ay[ai]));
                        points.add(new Point2D.Double(x, by[bi]));
                    } else if (t < b) {
                        double y = (t + b) / 2;
                        double[] ax = {start.left, start.right}, bx = {end.left, end.right};
                        int ai = 0, bi = 0;
                        double dist = Double.POSITIVE_INFINITY;
                        for (int i = 0; i < 2; i++) {
                            for (int j = 0; j < 2; j++) {
                                double newDist = Math.abs(ax[i] - bx[j]);
                                if (newDist < dist) {
                                    dist = newDist;
                                    ai = i;
                                    bi = j;
                                }
                            }
                        }
                        points.add(new Point2D.Double(ax[ai], y));
                        points.add(new Point2D.Double(bx[bi], y));
                    } else {
                        int xIndex = Math.abs(start.left - end.left) < Math.abs(start.right - end.left) ? 3 : 1;
                        int yIndex = Math.abs(start.top - end.top) < Math.abs(start.bottom - end.top) ? 0 : 2;
                        int startIndex = (xIndex + 1) % 4 == yIndex ? xIndex : yIndex;
                        int endIndex = (startIndex + 3) % 4;
                        Point2D.Double startPoint = GeometryHelper.findSideMidPoint(start, BoxSide.values()[startIndex]);
                        Point2D.Double endPoint = GeometryHelper.findSideMidPoint(end, BoxSide.values()[endIndex]);
                        Point2D.Double midPoint = startIndex % 2 == 0
                                ? new Point2D.Double(startPoint.x, endPoint.y)
                                : new Point2D.Double(endPoint.x, startPoint.y);
                        points.add(startPoint);
                        points.add(midPoint);
                        points.add(endPoint);
                    }
                }
            } else {
                throw new UnsupportedOperationException("Lines conneting to lines not implemented yet.");
            }
        }

        /**
         * Finds the box sides this line connects to.
         *
         * @param startElement the start element of the line
         * @param endElement the end element of the line
         */
        void findEndSides(Connectible startElement, Connectible endElement) {
            startSide = BoxSide.findPointSide(points.get(0), points.get(1), (Box) startElement);
            endSide = BoxSide.findPointSide(points.get(points.size() - 1), points.get(points.size() - 2), (Box) endElement);
        }

        @Override
        LineType getType() {
            return LineType.ORTHOGONAL;
        }

        @Override
        final void cullPoints() {
            ListIterator<Point2D.Double> it = points.listIterator();

            while (it.nextIndex() < points.size() - 2) {
                /*
                 * Three consequent points of the edge's polyline. If all lie on a line, mid will be
                 * removed and the iterator moved one position backwards.
                 */
                Point2D.Double left = it.next();
                Point2D.Double mid = it.next();
                Point2D.Double right = it.next();
                if ((left.getX() == mid.getX() && left.getX() == right.getX())
                        || (left.getY() == mid.getY() && left.getY() == right.getY())) {
                    /*
                     * If all points lie on the same line.
                     */
                    it.previous();
                    it.previous();
                    it.remove();
                    it.previous();
                    if (it.hasPrevious()) {
                        it.previous();
                    }
                } else {
                    /*
                     * Else, move the iterator two positions backwards so in the next iteration the
                     * next vertex triple will be checked.
                     */
                    it.previous();
                    it.previous();
                }
            }
        }

        @Override
        final void cullPoints(Collection<LineLabel> labels) {
            int[] indexMap = new int[points.size() - 1];
            ArrayList<Point2D.Double> oldPoints = new ArrayList<>(points);

            ListIterator<Point2D.Double> it = points.listIterator();

            while (it.nextIndex() < points.size() - 2) {
                /*
                 * Three consequent points of the edge's polyline. If all lie on a line, mid will be
                 * removed and the iterator moved one position backwards.
                 */
                Point2D.Double left = it.next();
                Point2D.Double mid = it.next();
                Point2D.Double right = it.next();
                if ((left.getX() == mid.getX() && left.getX() == right.getX())
                        || (left.getY() == mid.getY() && left.getY() == right.getY())) {
                    /*
                     * If all points lie on the same line.
                     */
                    it.previous();
                    it.previous();
                    it.remove();
                    it.previous();
                    if (it.hasPrevious()) {
                        it.previous();
                    }
                } else {
                    /*
                     * Else, move the iterator two positions backwards so in the next iteration the
                     * next vertex triple will be checked.
                     */
                    it.previous();
                    it.previous();
                }
            }

            int i = 0;
            for (int j = 0; j + 1 < points.size(); j++) {
                do {
                    indexMap[i++] = j;
                } while (!oldPoints.get(i).equals(points.get(j + 1)));
            }
            while (i + 1 < oldPoints.size()) {
                indexMap[i++] = points.size() - 2;
            }

            for (LineLabel label : labels) {
                label.segmentIndex = indexMap[label.segmentIndex];
            }
        }

        @Override
        final void setPoints(ArrayList<Point2D.Double> points) {
            /*
             * Checks the minimal size of the array.
             */
            if (points.size() < 2) {
                throw new IllegalArgumentException("The number of points is less than 2.");
            }

            this.points = points;

            if (!isOrthogonal()) {
                throw new IllegalArgumentException("The given line is not orthogonal.");
            }
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Polyline">
    /**
     * This class defines the polyline line type.
     */
    static class Polyline extends LineGeometry {

        /**
         * The maximum absolute value of the sine of an angle in a polyline for which the
         * corresponding point can be culled, producing a straight segment.
         */
        static final double MAXIMUM_ANGLE_SIN = 0.1;
        /**
         * The old start point of the line before performed geometry actions.
         */
        Point2D.Double oldStartPoint;
        /**
         * The old second point of the line before performed geometry actions.
         */
        Point2D.Double oldSecondPoint;
        /**
         * The old penultimate point of the line before performed geometry actions.
         */
        Point2D.Double oldPenultimatePoint;
        /**
         * The old end point of the line before performed geometry actions.
         */
        Point2D.Double oldEndPoint;

        /**
         * Creates a new polyline.
         *
         * @param startElement the start element of the line
         * @param endElement the end element of the line
         * @param startSides the mask denoting the allowed start box sides for this line
         * @param endSides the mask denoting the allowed end box sides for this line
         * @param spacing the spacing of the line
         */
        Polyline(Connectible startElement, Connectible endElement, int startSides, int endSides, double spacing) {
            if (startElement instanceof Box && endElement instanceof Box) {
                Box a = (Box) startElement, b = (Box) endElement;
                if (a != b) {
                    points = new ArrayList<>();
                    points.add(a.getCenter());
                    points.add(b.getCenter());
                } else {
                    points = Tracer.connectOneBox(a, startSides, endSides, spacing);
                }
                oldStartPoint = points.get(0);
                oldSecondPoint = points.get(1);
                oldPenultimatePoint = points.get(points.size() - 2);
                oldEndPoint = points.get(points.size() - 1);
            } else {
                throw new UnsupportedOperationException("Lines connecting to lines not implemented yet.");
            }
        }

        /**
         * Creates a new polyline going along the given vertices.
         *
         * @param startElement the start element of the line
         * @param endElement the end element of the line
         * @param points the vertices of the line
         */
        public Polyline(Connectible startElement, Connectible endElement, ArrayList<Point2D.Double> points) {
            if (startElement instanceof Box && endElement instanceof Box) {
                setPoints(points);
                oldStartPoint = points.get(0);
                oldSecondPoint = points.get(1);
                oldPenultimatePoint = points.get(points.size() - 2);
                oldEndPoint = points.get(points.size() - 1);
            } else {
                throw new UnsupportedOperationException("Lines connecting to lines not implemented yet.");
            }
        }

        /**
         * Creates a polyline with no points, for use in XML loading.
         */
        private Polyline() {
        }

        @Override
        LineType getType() {
            return LineType.POLYLINE;
        }

        @Override
        void cullPoints() {
            ListIterator<Point2D.Double> it = points.listIterator();

            while (it.nextIndex() < points.size() - 2) {
                /*
                 * Three consequent points of the edge's polyline. If all lie on a line, mid will be
                 * removed and the iterator moved one position backwards.
                 */
                Point2D.Double left = it.next();
                Point2D.Double mid = it.next();
                Point2D.Double right = it.next();
                double firstLen = left.distance(mid);
                double secondLen = mid.distance(right);
                double crossProduct = (right.x - left.x) * (mid.y - left.y) - (right.y - left.y) * (mid.x - left.x);
                double sin = Math.abs(crossProduct / (firstLen * secondLen));
                if (sin < MAXIMUM_ANGLE_SIN) {
                    /*
                     * If all points lie on the same line.
                     */
                    it.previous();
                    it.previous();
                    it.remove();
                    it.previous();
                    if (it.hasPrevious()) {
                        it.previous();
                    }
                } else {
                    /*
                     * Else, move the iterator two positions backwards so in the next iteration the
                     * next vertex triple will be checked.
                     */
                    it.previous();
                    it.previous();
                }
            }
        }

        @Override
        void cullPoints(Collection<LineLabel> labels) {
            int[] indexMap = new int[points.size() - 1];
            ArrayList<Point2D.Double> oldPoints = new ArrayList<>(points);

            cullPoints();

            int i = 0;
            for (int j = 0; j + 1 < points.size(); j++) {
                do {
                    indexMap[i++] = j;
                } while (!oldPoints.get(i).equals(points.get(j + 1)));
            }
            while (i + 1 < oldPoints.size()) {
                indexMap[i++] = points.size() - 2;
            }

            for (LineLabel label : labels) {
                label.segmentIndex = indexMap[label.segmentIndex];
            }
        }

        @Override
        final void setPoints(ArrayList<Point2D.Double> points
        ) {
            /*
             * Checks the minimal size of the array.
             */
            if (points.size() < 2) {
                throw new IllegalArgumentException("The number of points is less than 2.");
            }

            this.points = points;
        }
    }
    //</editor-fold>
}
