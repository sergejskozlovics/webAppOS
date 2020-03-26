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

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import lv.lumii.layoutengine.Box.BoxSide;
import lv.lumii.layoutengine.OutsideLabel.LineLabel;
import lv.lumii.layoutengine.OutsideLabel.LineLabel.Orientation;
import lv.lumii.layoutengine.Transaction.Operation.LineOperation;
import lv.lumii.layoutengine.util.Pair;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Line connects diagram elements
 *
 * @author karlis
 */
public class Line extends Element implements Connectible {

    //<editor-fold defaultstate="collapsed" desc="attributes">
    /**
     * The start element of the line.
     */
    private Connectible startElement;
    /**
     * The end element of the line.
     */
    private Connectible endElement;
    /**
     * The geometrical representation of this line.
     */
    LineGeometry lineGeometry = null;
    public boolean hasGeometry() {
    	return lineGeometry != null;
    }
    /**
     * The set of lines incident to this line.
     */
    private final LinkedHashSet<Line> incidentLines;
    /**
     * The set of labels that belong to this line.
     */
    final LinkedHashSet<LineLabel> labels;
    /**
     * The mask denoting the user allowed start box sides for this line
     */
    private int userStartSides;
    /**
     * the mask denoting the user allowed end box sides for this line
     */
    private int userEndSides;
    /**
     * The mask denoting the layout allowed start box sides for this line
     */
    private int layoutStartSides;
    /**
     * the mask denoting the layout allowed end box sides for this line
     */
    private int layoutEndSides;

    /**
     * Defines the line type.
     */
    public static enum LineType {

        /**
         * Line connecting two points with a straight segment.
         */
        STRAIGHT,
        /**
         * Each line segment of this type is parallel to one of the coordinate axis.
         */
        ORTHOGONAL,
        /**
         * Connects any list of points.
         */
        POLYLINE,
        /**
         * A curved line based on the stored points.
         */
        SPLINE
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="constructors">
    /**
     * Creates a new line as a diagram element, without assigning it any geometry.
     *
     * @param startElement the starting element of the new line
     * @param endElement the end element of the new line
     * @param owner the owner of the new line
     * @param spacing the spacing of the new line
     */
    Line(Connectible startElement, Connectible endElement, Container owner, double spacing) {    	
        super(owner, spacing);
        this.startElement = startElement;
        this.endElement = endElement;
        userStartSides = userEndSides = 0b1111;
        layoutStartSides = layoutEndSides = 0b1111;

        incidentLines = new LinkedHashSet<>();
        labels = new LinkedHashSet<>();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="accessors">
    /**
     * Returns the starting element of the line, could be a box or a line.
     *
     * @return the starting element of the line, could be a box or a line.
     */
    public final Element getStart() {
        if (startElement instanceof Element) {
            return (Element) startElement;
        } else {
            throw new ClassCastException("Start element is not an Element.");
        }
    }

    /**
     * Returns the ending element of the line, could be a box or a line.
     *
     * @return the ending element of the line, could be a box or a line.
     */
    public final Element getEnd() {
        if (endElement instanceof Element) {
            return (Element) endElement;
        } else {
            throw new ClassCastException("End element is not an Element.");
        }
    }

    /**
     * Returns the points representing the geometrical form of the line.
     *
     * @return the points representing the geometrical form of the line.
     */
    public ArrayList<Point2D.Double> getPoints() {
        if (lineGeometry == null) {
//            lineGeometry = new LineGeometry.StraightLine(startElement, endElement, lineGeometry.points);
//            setPoints(lineGeometry.getPoints());
            return null;
        }
        ArrayList<Point2D.Double> points = new ArrayList<>();
        for (Point2D.Double point : lineGeometry.getPoints()) {
            Point2D.Double q = new Point2D.Double();
            q.setLocation(point.getX(), point.getY());
            points.add(q);
        }
        return points;
    }

    /**
     * Sets the points of this line. The points may be corrected to match the line type. The whole
     * diagram is reordered afterwards, or at the end of the current transaction.
     *
     * @param points the new points of the line, their exact meaning may vary depending on the
     * current line type
     */
    public void setPoints(ArrayList<Point2D.Double> points) {
   		setPoints(points, false);
    }

    /**
     * Sets the points of this line. The points may be corrected to match the line type. The whole
     * diagram is reordered afterwards, or at the end of the current transaction.
     *
     * @param points the new points of the line, their exact meaning may vary depending on the
     * current line type
     * @param cleanup whether to clean up the resulting line in order to minimize corner count
     */
    public void setPoints(ArrayList<Point2D.Double> points, boolean cleanup) {
        LineOperation.SetPointsOperation.prepareTransaction(getDiagram(), (Box) startElement, (Box) endElement);

        getDiagram().layoutSetLinePoints(this, getType(), points, cleanup);
    }

    /**
     * Sets the points of this line and changes its start and end elements. The points may be
     * corrected to match the line type and to connect the given elements. The whole diagram is
     * reordered afterwards, or at the end of the current transaction.
     *
     * @param points the new points of the line, their exact meaning may vary depending on the
     * current line type
     * @param newStart the new start of the line
     * @param newEnd the new end of the line
     * @param cleanup whether to clean up the resulting line in order to minimize corner count
     */
    public void setPoints(ArrayList<Point2D.Double> points, Element newStart, Element newEnd, boolean cleanup) {
        if (!(newStart instanceof Box) || !(newEnd instanceof Box)) {
            throw new UnsupportedOperationException("Lines connected to lines are not supported yet.");
        }
        Box startBox = (Box) newStart;
        Box endBox = (Box) newEnd;
        if (startBox.getDiagram() != endBox.getDiagram()) {
            throw new UnsupportedOperationException("Lines cannot connect boxes between diagrams.");
        }
        LineOperation.SetPointsOperation.prepareTransaction(getDiagram(), startBox, endBox);
        ((Box) startElement).incidentLines.remove(this);
        ((Box) endElement).incidentLines.remove(this);
        getOwner().removeChild(this);
        startBox.incidentLines.add(this);
        endBox.incidentLines.add(this);
        setOwner(Line.findOwner(startBox, endBox));
        startElement = startBox;
        endElement = endBox;
        getDiagram().layoutSetLinePoints(this, getType(), points, cleanup);
    }

    /**
     * Sets the points of this line and changes its start and end elements. The points may be
     * corrected to match the line type and to connect the given elements. The whole diagram is
     * reordered afterwards, or at the end of the current transaction.
     *
     * @param points the new points of the line, their exact meaning may vary depending on the
     * current line type
     * @param newStart the new start of the line
     * @param newEnd the new end of the line
     */
    public void setPoints(ArrayList<Point2D.Double> points, Element newStart, Element newEnd) {
        setPoints(points, newStart, newEnd, false);
    }

    /**
     * Returns the starting point that is on the border of the start element of this line.
     *
     * @return the starting point that is on the border of the start element of this line.
     */
    public Point2D.Double getStartPoint() {
        return lineGeometry.getStartPoint();
    }

    /**
     * Returns the ending point that is on the border of the end element of this line.
     *
     * @return the ending point that is on the border of the end element of this line.
     */
    public Point2D.Double getEndPoint() {
        return lineGeometry.getEndPoint();
    }

    /**
     * Returns the geometrical type of the line.
     *
     * @return the geometrical type of the line.
     */
    public LineType getType() {
        return lineGeometry.getType();
    }

    /**
     * Changes the line geometry to the given line type, possibly taking the previous line layout
     * into account.
     *
     * @param type the line type to change the line to
     */
    public void setType(LineType type) {
        if (getType() == type) {
            return;
        }
        if (type == LineType.ORTHOGONAL) {
            retrace(type);
        } else if (type == LineType.STRAIGHT) {
            lineGeometry = new LineGeometry.StraightLine(startElement, endElement, lineGeometry.points);
            setPoints(lineGeometry.getPoints());
        } else if (type == LineType.POLYLINE) {
            lineGeometry = new LineGeometry.Polyline(startElement, endElement, lineGeometry.points);
            setPoints(lineGeometry.getPoints());
        } else {
            throw new UnsupportedOperationException("This line type is not supported yet.");
        }
    }

    /**
     * Returns the spacing value of the line.
     *
     * @return the spacing value of the line.
     */
    @Override
    public double getSpacing() {
        return spacing;
    }

    /**
     * Sets the spacing value of the line. The whole diagram is reordered afterwards, or at the end
     * of the current transaction.
     *
     * @param spacing the spacing of the line
     */
    @Override
    public void setSpacing(double spacing) {
        Transaction.Operation.SetSpacingOperation.prepareTransaction(getDiagram());

        getDiagram().layoutChangeLineSpacing(this, spacing);
    }

    /**
     * Returns the full length of this line. Traverses the line to calculate this.
     *
     * @return the full length of this line
     */
    double getLength() {
        return lineGeometry.getLength();
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
        return lineGeometry.getLength(startIndex, endIndex);
    }

    /**
     * Adds a label as a child of this line. Only affects the element hierarchy.
     *
     * @param label the label to add as a child
     */
    void addLabel(LineLabel label) {
        labels.add(label);
    }

    @Override
    ArrayList<Element> getChildren() {
        return new ArrayList<Element>(labels);
    }

    @Override
    public ArrayList<Line> getIncidentLines() {
        return new ArrayList<>(incidentLines);
    }

    /**
     * Returns the labels of the line.
     *
     * @return the labels of the line.
     */
    public ArrayList<LineLabel> getLabels() {
        return new ArrayList<>(labels);
    }

    @Override
    public final Container getOwner() {
        return (Container) owner;
    }

    /**
     * Returns the geometry of the line.
     *
     * @return the geometry of the line.
     */
    LineGeometry getLineGeometry() {
        return lineGeometry;
    }

    /**
     * Sets the sides of the start box this line is allowed to start from. The sides are given as an
     * array of {@code BoxSides}, at least one side must be allowed.
     *
     * @param sides the array of allowed start box sides for this line
     */
    public void setStartSides(BoxSide[] sides) {
        int newSides = 0b0000;
        for (BoxSide side : sides) {
            if (side != null) {
                newSides |= side.mask;
            }
        }
        setStartSides(newSides);
    }

    /**
     * Sets the sides of the end box this line is allowed to end at. The sides are given as an array
     * of {@code BoxSides}, at least one side must be allowed.
     *
     * @param sides the array of allowed end box sides for this line
     */
    public void setEndSides(BoxSide[] sides) {
        int newSides = 0b0000;
        for (BoxSide side : sides) {
            if (side != null) {
                newSides |= side.mask;
            }
        }
        setEndSides(newSides);
    }

    /**
     * Sets the sides of the start box this line is allowed to start from. The sides are given as an
     * mask, at least one side must be allowed. The mask bits are as follows:
     * <pre>
     * 0b0001: top
     * 0b0010: right
     * 0b0100: bottom
     * 0b1000: left
     * </pre>
     *
     * @param sides the mask of allowed start box sides for this line
     */
    public void setStartSides(int sides) {
        if (sides == 0b0000) {
            throw new IllegalArgumentException("At least one side must be allowed.");
        }
        userStartSides = sides;

 	// workaround on the problem "Point must lie on the perimeter of the box":
 	// SK added try-catch
	try{
        	if (((1 << BoxSide.findPointSide(getStartPoint(), lineGeometry.getPoints().get(1), (Box) startElement).ordinal()) & getUsedStartSides()) == 0) {
	            setPoints(lineGeometry.getPoints());
        	}
	}
	catch(Throwable t) {
	    try {
	     	setPoints(lineGeometry.getPoints());
	    }
	    catch(Throwable tt) {}
	}

    }

    /**
     * Sets the sides of the end box this line is allowed to end at. The sides are given as a mask,
     * at least one side must be allowed. The mask bits are as follows:
     * <pre>
     * 0b0001: top
     * 0b0010: right
     * 0b0100: bottom
     * 0b1000: left
     * </pre>
     *
     * @param sides the mask of allowed end box sides for this line
     */
    public void setEndSides(int sides) {
        if (sides == 0b0000) {
            throw new IllegalArgumentException("At least one side must be allowed.");
        }
        userEndSides = sides;
 	// SK added try-catch
	try{
	        if (((1 << BoxSide.findPointSide(getEndPoint(), lineGeometry.getPoints().get(lineGeometry.getPoints().size() - 2), (Box) endElement).ordinal()) & getUsedEndSides()) == 0) {
        	    setPoints(lineGeometry.getPoints());
	        }
	}
	catch(Throwable t) {
	    try {
	     	setPoints(lineGeometry.getPoints());
	    }
	    catch(Throwable tt) {}
	}
    }

    /**
     * Returns the mask of the allowed start sides for this line. The mask bits are as follows:
     * <pre>
     * 0b0001: top
     * 0b0010: right
     * 0b0100: bottom
     * 0b1000: left
     * </pre>
     *
     * @return the mask of the allowed start sides for this line.
     */
    public int getStartSides() {
        return userStartSides;
    }

    /**
     * Returns the mask of the allowed end sides for this box. The mask bits are as follows:
     * <pre>
     * 0b0001: top
     * 0b0010: right
     * 0b0100: bottom
     * 0b1000: left
     * </pre>
     *
     * @return the mask of the allowed end sides for this box.
     */
    public int getEndSides() {
        return userEndSides;
    }

    /**
     * Resets the start sides of this line to allow it to start from any side of the start box.
     */
    public void resetStartSides() {
        userStartSides = 0b1111;
    }

    /**
     * Resets the end sides of this line to allow it to end at any side of the end box.
     */
    public void resetEndSides() {
        userEndSides = 0b1111;
    }

    /**
     * Sets the sides of the start box this line is allowed to start from by the layouter. The sides
     * are given as an mask, at least one side must be allowed. If this conflicts with the sides set
     * by the user, the user's sides take precedence. The mask bits are as follows:
     * <pre>
     * 0b0001: top
     * 0b0010: right
     * 0b0100: bottom
     * 0b1000: left
     * </pre>
     *
     * @param sides the mask of allowed start box sides for this line
     */
    void setLayoutStartSides(int sides) {
        assert sides != 0;
        layoutStartSides = sides;
    }

    /**
     * Sets the sides of the end box this line is allowed to end at by the layouter. The sides are
     * given as an mask, at least one side must be allowed. If this conflicts with the sides set by
     * the user, the user's sides take precedence. The mask bits are as follows:
     * <pre>
     * 0b0001: top
     * 0b0010: right
     * 0b0100: bottom
     * 0b1000: left
     * </pre>
     *
     * @param sides the mask of allowed end box sides for this line
     */
    void setLayoutEndSides(int sides) {
        assert sides != 0;
        layoutEndSides = sides;
    }

    /**
     * Returns the mask of the layouter allowed start sides for this line. The mask bits are as
     * follows:
     * <pre>
     * 0b0001: top
     * 0b0010: right
     * 0b0100: bottom
     * 0b1000: left
     * </pre>
     *
     * @return the mask of the allowed start sides for this line.
     */
    int getLayoutStartSides() {
        return layoutStartSides;
    }

    /**
     * Returns the mask of the layouter allowed end sides for this line. The mask bits are as
     * follows:
     * <pre>
     * 0b0001: top
     * 0b0010: right
     * 0b0100: bottom
     * 0b1000: left
     * </pre>
     *
     * @return the mask of the allowed end sides for this box.
     */
    int getLayoutEndSides() {
        return layoutEndSides;
    }

    /**
     * Resets the layout start sides of this line to allow it to start from any side of the start
     * box.
     */
    void resetLayoutStartSides() {
        layoutStartSides = 0b1111;
    }

    /**
     * Resets the layout end sides of this line to allow it to end at any side of the end box.
     */
    void resetLayoutEndSides() {
        layoutEndSides = 0b1111;
    }

    /**
     * Returns the mask of the actually allowed start sides for this line. This is formed as an
     * {@code AND} between the user and layout start sides. If that leaves no available sides, user
     * sides are used. The mask bits are as follows:
     * <pre>
     * 0b0001: top
     * 0b0010: right
     * 0b0100: bottom
     * 0b1000: left
     * </pre>
     *
     * @return the mask of the allowed start sides for this box.
     */
    int getUsedStartSides() {
        int sides = layoutStartSides & userStartSides;
        if (sides == 0) {
            sides = userStartSides;
        }
        return sides;
    }

    /**
     * Returns the mask of the actually allowed end sides for this line. This is formed as an
     * {@code AND} between the user and layout end sides. If that leaves no available sides, user
     * sides are used. The mask bits are as follows:
     * <pre>
     * 0b0001: top
     * 0b0010: right
     * 0b0100: bottom
     * 0b1000: left
     * </pre>
     *
     * @return the mask of the allowed end sides for this box.
     */
    int getUsedEndSides() {
        int sides = layoutEndSides & userEndSides;
        if (sides == 0) {
            sides = userEndSides;
        }
        return sides;
    }
    //</editor-fold>

    @Override
    public void remove() {
        remove(true);
    }

    /**
     * Removes this line. The whole diagram is reordered afterwards, or at the end of the current
     * transaction if {@code adjust} is set to {@code true}.
     *
     * @param adjust whether to adjust the diagram after removing this line.
     */
    @Override
    public void remove(boolean adjust) {
        if (getDiagram() == null) {
            return;
        }

        if (adjust) {
            Transaction.Operation.AdjustOperation.prepareTransaction(getDiagram());
        }

        ((Box) startElement).incidentLines.remove(this);
        ((Box) endElement).incidentLines.remove(this);

        for (Line line : incidentLines) {
            line.remove(false);
        }

        super.remove(adjust);
    }

    @Override
    void removeChild(Element element) {
        if (element instanceof LineLabel) {
            labels.remove((LineLabel) element);
        } else {
            throw new IllegalArgumentException("Line can only have children that are labels.");
        }
    }

    /**
     * Creates a new label for this line. The position for the new label is determined by the given
     * point, which is moved to the line perimeter if necessary. The minimum size of the new label
     * is the size of the given rectangle. The whole diagram is reordered afterwards, or at the end
     * of the current transaction.
     *
     * @param width the width for the new label
     * @param height the height for the new label
     * @param point the point from which to determine the position of the label relative to the line
     * perimeter
     * @param orientation the orientation for the label positioning
     * @param constraintType the type of the layout constraints for the new label
     * @param spacing the spacing value for the new label
     * @return the new inside label object.
     */
    public LineLabel createLabel(double width, double height, Point2D.Double point, Orientation orientation, LayoutConstraints.ConstraintType constraintType, double spacing) {
        Transaction.Operation.AdjustOperation.prepareTransaction(getDiagram());

        int segmentIndex = GeometryHelper.findNearestSegment(point, this);

        Line2D.Double segment = new Line2D.Double(lineGeometry.getPoints().get(segmentIndex), lineGeometry.getPoints().get(segmentIndex + 1));
        boolean clockwise = segment.relativeCCW(point) < 0;

        GeometryHelper.movePointToLine(point, this, segmentIndex);

        double partLength = getLength(0, segmentIndex);
        Point2D.Double segmentStart = lineGeometry.getPoints().get(segmentIndex);
        double lineLength = partLength + getLength(segmentIndex, lineGeometry.getPoints().size() - 1);
        partLength += getType() == LineType.ORTHOGONAL ? Math.abs(point.x - segmentStart.x) + Math.abs(point.y - segmentStart.y) : point.distance(segmentStart);
        double position = partLength / lineLength;

        LineLabel label = createLabel(width, height, point, position, segmentIndex, orientation, constraintType, spacing);
        label.clockwiseWithBoth = clockwise;
        return label;
    }

    /**
     * Creates a new label for this line. The minimum size of the new label is the size of the given
     * rectangle. The whole diagram is reordered afterwards, or at the end of the current
     * transaction.
     *
     * @param width the width for the new label
     * @param height the height for the new label
     * @param position the relative position on the line perimeter for the new label
     * @param orientation the orientation for the label positioning
     * @param constraintType the type of the layout constraints for the new label
     * @param spacing the spacing value for the new label
     * @return the new inside label object.
     */
    public LineLabel createLabel(double width, double height, double position, Orientation orientation, LayoutConstraints.ConstraintType constraintType, double spacing) {
        if (position < 0 || position > 1) {
            throw new IllegalArgumentException("Line label position should be between 0 and 1, inclusive.");
        }

        //SK:Transaction.Operation.AdjustOperation.prepareTransaction(getDiagram());
        

        Pair<Integer, Double> segmentPoint = GeometryHelper.findSegmentAndPoint(position, this);

        int segmentIndex = segmentPoint.getFirst();
        double ratio = segmentPoint.getSecond();
        Point2D.Double first = lineGeometry.getPoints().get(segmentIndex), second = lineGeometry.getPoints().get(segmentIndex + 1);
        Point2D.Double point = new Point2D.Double(first.x + ratio * (second.x - first.x), first.y + ratio * (second.y - first.y));

        return createLabel(width, height, point, position, segmentIndex, orientation, constraintType, spacing);
    }

    /**
     * Creates a new label for this line. The minimum size of the new label is the size of the given
     * rectangle. The whole diagram is reordered afterwards, or at the end of the current
     * transaction.
     *
     * @param width the width for the new label
     * @param height the height for the new label
     * @param center the point from which to determine the position of the label relative to the
     * line perimeter; the center of the new label will try to be as close to this point as possible
     * @param position the relative position on the line perimeter for the new label
     * @param segmentIndex the index of the start point for the line segment on which to place the
     * label
     * @param orientation the orientation for the label positioning
     * @param constraintType the type of the layout constraints for the new label
     * @param spacing the spacing value for the new label
     * @return the new inside label object.
     */
    LineLabel createLabel(double width, double height, Point2D.Double center, double position, int segmentIndex, Orientation orientation, LayoutConstraints.ConstraintType constraintType, double spacing) {
        LineLabel label = new LineLabel(center, segmentIndex, position, orientation, width, height, this, constraintType, spacing);
        labels.add(label);

        //SK:getDiagram().layoutAdjustOutsideLabel(label);

        return label;
    }

    @Override
    public Line connectTo(Connectible element, LineType lineType, double spacing) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Line connectTo(Connectible element, LineType lineType, double spacing, ArrayList<Point2D.Double> points) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Line connectTo(Connectible element, LineType lineType, double spacing, ArrayList<Point2D.Double> points, boolean cleanup) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * Traces this line to connect its end elements using its current line type.
     */
    void trace() {
        trace(getType());
    }

    /**
     * Traces this line to connect its end elements using the given line type.
     *
     * @param lineType the lineType in which to trace the line
     */
    void trace(LineType lineType) {
        if (startElement instanceof Box && endElement instanceof Box) {
            Box a = (Box) startElement, b = (Box) endElement;
            Tracer tracer = new Tracer(a.getPrevBox(), b.getPrevBox(), a.findLCABox(b));
            trace(lineType, tracer);
        } else {
            throw new UnsupportedOperationException("Lines conneting to lines not implemented yet.");
        }
    }


    /**
     * Trace this line to connect its end elements using the given line type and existing
     * {@link Tracer}. The tracer has to be able to trace this line.
     *
     * @param lineType the lineType in which to trace the line
     * @param tracer the Tracer with which to trace this line
     */
    void trace(LineType lineType, Tracer tracer) {
        switch (lineType) {
            case STRAIGHT:
                lineGeometry = new LineGeometry.StraightLine(startElement, endElement);
                LineOptimizer.correctStraightLine(this);
                break;
            case ORTHOGONAL:
                lineGeometry = new LineGeometry.OrthogonalLine(startElement, endElement, tracer, getUsedStartSides(), getUsedEndSides());
                ((LineGeometry.OrthogonalLine) lineGeometry).findEndSides(startElement, endElement);
                break;
            case POLYLINE:
                lineGeometry = new LineGeometry.Polyline(startElement, endElement, getUsedStartSides(), getUsedEndSides(), spacing);
                if (startElement != endElement) {
                    LineOptimizer.correctStraightLine(this);
                }
                break;
        }
    }

    /**
     * Retraces this line from scratch so that it connects its end elements. The whole diagram is
     * reordered afterwards, or at the end of the current transaction.
     */
    public void retrace() {
        retrace(getType());
    }

    /**
     * Finds the container that should be the owner of a line connecting the given elements. Finds
     * the LCA of the two elements. If the LCA is one of the elements connected, returns its parent.
     *
     * @param startElement the first element the new line would connect
     * @param endElement the second element the new line would connect
     * @return the container that should be the owner of a line connecting the given elements
     */
    static Container findOwner(Connectible startElement, Connectible endElement) {
        if (startElement instanceof Box && endElement instanceof Box) {
            Box start = (Box) startElement;
            Box end = (Box) endElement;
            Container owner = start.findLCA(end);
            if (owner == start) {
                owner = start.getOwner();
            } else if (owner == end) {
                owner = end.getOwner();
            }
            return owner;
        } else {
            throw new UnsupportedOperationException("Lines not connected to boxes are not suported yet");
        }
    }

    /**
     * Retraces this line from scratch so that it connects its end elements. The whole diagram is
     * reordered afterwards, or at the end of the current transaction.
     *
     * @param type the new line type
     */
    public void retrace(LineType type) {
        if (!(startElement instanceof Box) || !(endElement instanceof Box)) {
            throw new UnsupportedOperationException("Lines connected to lines are not supported yet.");
        }
        LineOperation.NewLineOperation.prepareTransaction(getDiagram(), lineGeometry, (Box) startElement, (Box) endElement);

        getDiagram().layoutTraceLine(this, type);
    }

    /**
     * Retraces this line from scratch so that it connects its new end elements. The whole diagram
     * is reordered afterwards, or at the end of the current transaction.
     *
     * @param type the new line type
     * @param newStart the new start of the line
     * @param newEnd the new end of the line
     */
    public void retrace(LineType type, Element newStart, Element newEnd) {
        if (!(newStart instanceof Box) || !(newEnd instanceof Box)) {
            throw new UnsupportedOperationException("Lines connected to lines are not supported yet.");
        }
        Box startBox = (Box) newStart;
        Box endBox = (Box) newEnd;
        if (startBox.getDiagram() != endBox.getDiagram()) {
            throw new UnsupportedOperationException("Lines cannot connect boxes between diagrams.");
        }
        LineOperation.NewLineOperation.prepareTransaction(getDiagram(), lineGeometry, startBox, endBox);
        ((Box) startElement).incidentLines.remove(this);
        ((Box) endElement).incidentLines.remove(this);
        getOwner().removeChild(this);
        startBox.incidentLines.add(this);
        endBox.incidentLines.add(this);
        setOwner(Line.findOwner(startBox, endBox));
        startElement = startBox;
        endElement = endBox;

        getDiagram().layoutTraceLine(this, type);
    }

    /**
     * Retraces this line from scratch so that it connects its new end elements. The whole diagram
     * is reordered afterwards, or at the end of the current transaction.
     *
     * @param newStart the new start of the line
     * @param newEnd the new end of the line
     */
    public void retrace(Element newStart, Element newEnd) {
        retrace(getType(), newStart, newEnd);
    }

    /**
     * Cleans up this line, removing unnecessary corners.
     */
    public void cleanup() {
        LineOperation.CleanupOperation.prepareTransaction(getDiagram(), (Box) startElement, (Box) endElement);
        getDiagram().layoutCleanupLine(this);
    }

    /**
     * Returns whether the line's points currently form an orthogonal line.
     *
     * @return whether the line's points currently form an orthogonal line
     */
    boolean isOrthogonal() {
        return lineGeometry.isOrthogonal();
    }

    /**
     * Resets this line label positions according to their relative positions on the line.
     */
    void _resetLabels() {
        ArrayList<Point2D.Double> points = lineGeometry.points;
        boolean orthogonal = getType() == LineType.ORTHOGONAL;

        ArrayList<LineLabel> labelList = new ArrayList<>(labels);
        Collections.sort(labelList, new LineLabelComparator());

        /**
         * Uses the two pointer method for label and line segment arrays to place the labels in
         * linear time.
         */
        double lineLength = getLength();
        double partLength = 0;
        int segmentIndex = -1;
        double segmentLength = 0;
        Point2D.Double first = null, second = null;
        for (LineLabel label : labelList) {
            double posLength = lineLength * label.position;
            while (segmentIndex < points.size() - 2 && partLength + segmentLength <= posLength) {
                partLength += segmentLength;
                segmentIndex++;
                first = points.get(segmentIndex);
                second = points.get(segmentIndex + 1);
                segmentLength = orthogonal ? Math.abs(first.x - second.x) + Math.abs(first.y - second.y) : first.distance(second);
            }

            assert first != null && second != null;
            label.segmentIndex = segmentIndex;
            double ratio = (posLength - partLength) / segmentLength;
            double x = first.x + ratio * (second.x - first.x);
            double y = first.y + ratio * (second.y - first.y);
            label.setCenter(new Point2D.Double(x, y));
        }
    }

    /**
     * Compares line labels by their position value. Use to sort line labels by their position in
     * ascending order.
     */
    static class LineLabelComparator implements Comparator<LineLabel> {

        @Override
        public int compare(LineLabel label1, LineLabel label2) {
            return Double.compare(label1.position, label2.position);
        }
    }

    /**
     * Creates a new XML element corresponding to this line in the given document. Puts the IDs of
     * any new elements in {@code idMap}.
     *
     * @param doc the XML document in which to create the new element
     * @param idMap a map from diagram objects to element storage IDs.
     * @return a new element corresponding to this line
     */
    org.w3c.dom.Element saveToXML(Document doc, LinkedHashMap<Object, Integer> idMap) {
        org.w3c.dom.Element e = doc.createElement(XMLHelper.NAMESPACE + ":line");

        super.saveAttributes(e, idMap);

        e.setAttribute("start", idMap.get(startElement).toString());
        e.setAttribute("end", idMap.get(endElement).toString());

        e.setAttribute("spacing", Double.toString(spacing));
        e.setAttribute("userStartSides", Integer.toBinaryString(userStartSides));
        e.setAttribute("userEndSides", Integer.toBinaryString(userEndSides));
        e.setAttribute("layoutStartSides", Integer.toBinaryString(layoutStartSides));
        e.setAttribute("layoutEndSides", Integer.toBinaryString(layoutEndSides));

        e.appendChild(lineGeometry.saveToXML(doc, idMap));
        for (LineLabel label : labels) {
            e.appendChild(label.saveToXML(doc, idMap));
        }

        return e;
    }

    /**
     * Creates a new Line from the given XML element, along with its children. Adds all of them
     * {@code objectMap}. The boxes the line connects should have already be placed in
     * {@code objectMap}.
     *
     * @param e an XML element corresponding to a Line
     * @param objectMap a map from element storage IDs to diagram objects
     * @param owner the owner of the line
     */
    Line(org.w3c.dom.Element e, LinkedHashMap<Integer, Object> objectMap, Container owner) {
        super(e, objectMap, owner);

        incidentLines = new LinkedHashSet<>();

        spacing = Double.parseDouble(e.getAttribute("spacing"));
        userStartSides = Integer.parseInt(e.getAttribute("userStartSides"), 2);
        userEndSides = Integer.parseInt(e.getAttribute("userEndSides"), 2);
        layoutStartSides = Integer.parseInt(e.getAttribute("layoutStartSides"), 2);
        layoutEndSides = Integer.parseInt(e.getAttribute("layoutEndSides"), 2);

        startElement = (Connectible) objectMap.get(Integer.parseInt(e.getAttribute("start")));
        endElement = (Connectible) objectMap.get(Integer.parseInt(e.getAttribute("end")));

        lineGeometry = LineGeometry.loadFromXML((org.w3c.dom.Element) e.getElementsByTagName(
                XMLHelper.NAMESPACE + ":lineGeometry").item(0));

        getOwner().addLine(this);
        ((Box) startElement).incidentLines.add(this);
        ((Box) endElement).incidentLines.add(this);

        labels = new LinkedHashSet<>();
        NodeList children = e.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child instanceof org.w3c.dom.Element) {
                org.w3c.dom.Element c = (org.w3c.dom.Element) child;
                String tag = c.getTagName();
                switch (tag) {
                    case XMLHelper.NAMESPACE + ":outsideLabel":
                        labels.add(new LineLabel(c, objectMap, this));
                        break;
                }
            }
        }

        if (lineGeometry.getType() == LineType.ORTHOGONAL) {
            ((LineGeometry.OrthogonalLine) lineGeometry).findEndSides(startElement, endElement);
        }
    }
}
