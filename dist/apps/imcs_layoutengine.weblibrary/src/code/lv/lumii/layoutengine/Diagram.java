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
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import lv.lumii.layoutengine.Adjuster.ManualAdjuster;
import lv.lumii.layoutengine.ArrangeData.ArrangeStyle;
import lv.lumii.layoutengine.LayoutConstraints.ConstraintType;
import lv.lumii.layoutengine.LayoutConstraints.GridLayoutConstraints;
import lv.lumii.layoutengine.Line.LineType;
import lv.lumii.layoutengine.OutsideLabel.BoxOutsideLabel;
import lv.lumii.layoutengine.OutsideLabel.LineLabel;
import lv.lumii.layoutengine.Transaction.Operation;
import lv.lumii.layoutengine.Transaction.Operation.LineOperation;

/**
 * This class defines the diagram element. A diagram stores a fully independent set of elements and
 * has methods for manipulating and arranging these elements.
 *
 * @author karlis
 */
public class Diagram extends Box {

    //<editor-fold defaultstate="collapsed" desc="attributes">
    /**
     * Default epsilon value, currently should never be used in an actual diagram.
     */
    private static final double DEFAULT_EPSILON = Double.MAX_VALUE;
    /**
     * The default distance a box has to be moved before its lines are retraced instead of
     * reconnected.
     */
    private static final double DEFAULT_RETRACE_DISTANCE = 500;
    /**
     * A special value for comparing various diagram elements' attributes. It defines the smallest
     * value by which some two elements can differ while still being considered equal.
     */
    private double epsilon = DEFAULT_EPSILON;
    /**
     * The current distance a box has to be moved before its lines are retraced instead of
     * reconnected.
     */
    private double retraceDistance = DEFAULT_RETRACE_DISTANCE;
    /**
     * The current bounding rectangle of this diagram, bounding the union of its children and the
     * user-defined bounds.
     */
    Rectangle2D.Double bounds;
    /**
     * The user-defined bounding rectangle of this diagram.
     */
    Rectangle2D.Double defaultBounds;
    /**
     * A structure for storing the current transaction.
     */
    Transaction transaction;

    /**
     * The states diagram can be in.
     */
    public enum State {

        /**
         * The normal diagram state, the diagrams correctness is ensured.
         */
        DEFAULT,
        /**
         * The diagram is in transaction mode, similar operations are stored and performed together.
         */
        TRANSACTION,
        /**
         * The diagram is in manual mode, only line connection and rectangle nesting is maintained.
         */
        MANUAL,
        /**
         * The diagram is in automatic mode, it is fully rearranged after every operation.
         */
        AUTOMATIC
    }
    /**
     * The state of the diagram.
     */
    State state;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="constructors">
    /**
     * Creates a new diagram.
     *
     * @param rectangle the bounding rectangle of the new diagram
     * @param arrangeStyle the style according to which to arrange the elements of the diagram
     * @param constraintType the type of the layout constraints for the diagram
     */
    public Diagram(Rectangle2D.Double rectangle, ArrangeStyle arrangeStyle, ConstraintType constraintType) {
        super(rectangle.getMinX(), rectangle.getMaxX(), rectangle.getMinY(), rectangle.getMaxY(), null, arrangeStyle, constraintType, 10);
        if (arrangeStyle == ArrangeStyle.INHERITED) {
            throw new IllegalArgumentException("A diagram cannot have INHERITED as its style.");
        }
        bounds = defaultBounds = rectangle;
        state = State.DEFAULT;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="accessors">
    /**
     * Returns the current state of the diagram.
     *
     * @return the current state of the diagram.
     */
    public State getState() {
        return state;
    }

    /**
     * Returns the epsilon of the diagram.
     *
     * @return the epsilon of the diagram.
     */
    double getEpsilon() {
        return epsilon;
    }

    /**
     * Returns the distance a box has to be moved before its lines are retraced instead of
     * reconnected.
     *
     * @return the distance a box has to be moved before its lines are retraced instead of
     * reconnected
     */
    public double getRetraceDistance() {
        return retraceDistance;
    }

    /**
     * Sets the distance a box has to be moved before its lines are retraced instead of reconnected.
     *
     * @param distance the new minimum retrace distance
     */
    public void setRetraceDistance(double distance) {
        retraceDistance = distance;
    }

    /**
     * Returns union of the user-defined default bounds and children enclosing rectangle.
     *
     * @return union of the user-defined default bounds and children enclosing rectangle.
     */
    @Override
    public Rectangle2D.Double getBounds() {
        Rectangle2D.Double q = new Rectangle2D.Double();

        if (bounds == null)
            q.setRect(defaultBounds.getX(), defaultBounds.getY(), defaultBounds.getWidth(), defaultBounds.getHeight());
        else
            q.setRect(bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight());

        return q;
    }
    //</editor-fold>

    /**
     * Lays out the diagram from scratch according to its current position without necessarily
     * maintaining existing element positions.
     */
    public void arrange() {
        if (state == State.TRANSACTION) {
            endTransaction();
        }

        State oldState = state;
        state = State.DEFAULT;

        LayoutAlgorithm arranger = new LayoutAlgorithm();
        arranger.layout(this);

        state = oldState;

        if (state == State.TRANSACTION) {
            startTransaction();
        }
    }

    /**
     * Starts a transaction of diagram operations. While a transaction is active, operations are not
     * executed immediately, they are collected and executed on {@link #endTransaction}, if
     * possible. Some operations may still be performed earlier, particularly if multiple operation
     * types are mixed in a single transaction. Transactions achieve better performance than
     * performing operations individually. Note that changes to the diagram's element hierarchy are
     * still immediate, only changes to their position are delayed.
     */
    public void startTransaction() {
        if (state != State.MANUAL) {
            state = State.TRANSACTION;
        }
    }

    /**
     * Performs all the work needed to effect all accumulated position changes since
     * {@link #startTransaction}, then ends the current transaction.
     */
    public void endTransaction() {
    	

        if (state != State.MANUAL) {
            if (transaction != null) {
                transaction.perform();
                transaction = null;
            }
            state = State.DEFAULT;
        }
    }

    /**
     * Sets the diagram to manual mode, where only line connections and rectangle nesting are
     * maintained.
     */
    public void startManual() {
        if (state == State.TRANSACTION) {
            endTransaction();
        }
        state = State.MANUAL;
    }

    /**
     * Ends manual mode, fixing the diagram if necessary.
     */
    public void endManual() {
        state = State.DEFAULT;
        Normalizer.arrange(this);

        startTransaction();
        for (Line line : getDescendantLines()) {
            line.setPoints(line.lineGeometry.points);
        }
        endTransaction();

        Adjuster.growOutsideLabels(this);

        updateBounds();
    }

    /**
     * Places the rectangle into the diagram and adjusts the element positions so that they comply
     * to the layout description of the diagram. If there is an active transaction stores the
     * operation instead.
     *
     * @param rectangle the rectangle to place
     * @param growPoint the point at which to insert the rectangle, sometimes determines the
     * relative positions of elements if the rectangle overlaps other elements
     */
    void layoutPlaceRectangle(AbstractContainer rectangle, Point2D.Double growPoint) {
        if (state != State.MANUAL) {
            adjustEpsilon(rectangle.getSpacing());
            transaction.addOperation(new Operation.PlaceRectangleOperation.RectangleMoveOperation(rectangle, rectangle.getCenter(), growPoint));
            if (state != State.TRANSACTION) {
                endTransaction();
            }
        } else {
            ManualAdjuster.updateParentRectangles(rectangle);
        }
    }

    /**
     * Moves the given rectangle and adjusts the element positions so that they comply to the layout
     * description of the diagram. If there is an active transaction stores the operation instead.
     *
     * @param rectangle the rectangle to move
     * @param newCenter the new center of the rectangle
     * @param growPoint the point at which to insert the rectangle, sometimes determines the
     * relative positions of elements if the rectangle overlaps other elements
     */
    void layoutMoveRectangle(AbstractContainer rectangle, Point2D.Double newCenter, Point2D.Double growPoint) {
        if (state != State.MANUAL) {
            transaction.addOperation(new Operation.PlaceRectangleOperation.RectangleMoveOperation(rectangle, newCenter, growPoint));
            if (state != State.TRANSACTION) {
                endTransaction();
//startTransaction(); //SK
            }
            // by SK:
            else {
                //endTransaction();
                //startTransaction();
            }
        } else {
            Point2D.Double moveVector = new Point2D.Double(newCenter.x - rectangle.getCenterX(), newCenter.y - rectangle.getCenterY());

            ArrayList<AbstractContainer> rectangles = rectangle.getDescendantRectangles(true, true);
            for (AbstractContainer rect : rectangles) {
                rect.transpose(moveVector);
                rect.getLayoutConstraints().transpose(moveVector);
            }

            if (rectangle instanceof Container) {
                for (Line line : ((Container) rectangle).getDescendantLines()) {
                    for (Point2D.Double point : line.lineGeometry.points) {
                        point.x += moveVector.x;
                        point.y += moveVector.y;
                    }
                }
                if (rectangle instanceof Box) {
                    for (Line line : ((Box) rectangle).incidentLines) {
                        if (line.getStart() == line.getEnd()) {
                            for (Point2D.Double point : line.lineGeometry.points) {
                                point.x += moveVector.x;
                                point.y += moveVector.y;
                            }
                        }
                    }
                }
            }

            for (AbstractContainer rect : rectangles) {
                if (rect instanceof Box) {
                    for (Line line : ((Box) rect).incidentLines) {
                        ManualAdjuster.reconnectEndPoints(line);
                        for (LineLabel label : line.getLabels()) {
                            layoutAdjustOutsideLabel(label);
                        }
                    }
                }
            }

            ManualAdjuster.updateParentRectangles(rectangle);

            updateBounds();
        }
    }

    /**
     * Moves the given container (its grid and children) and adjusts the element positions so that
     * they comply to the layout description of the diagram. If there is an active transaction
     * stores the operation instead.
     *
     * @param container the rectangle to move
     * @param moveVector the move vector for the container grid and children
     */
    void layoutMoveContainer(Container container, Point2D.Double moveVector) {
        if (state != State.MANUAL) {
            LinkedList<Container> queue = new LinkedList<>();
            queue.add(container);
            while (!queue.isEmpty()) {
                Container curr = queue.pop();
                curr.layoutConstraints.transpose(moveVector);
                queue.addAll(curr.containers);
            }

            for (AbstractContainer rectangle : container.getNextRectangles(false)) {
                Point2D.Double oldRectangleCenter = rectangle.getCenter();
                Point2D.Double newRectangleCenter = new Point2D.Double(oldRectangleCenter.x + moveVector.x, oldRectangleCenter.y + moveVector.y);
                transaction.addOperation(new Operation.PlaceRectangleOperation.RectangleMoveOperation(rectangle, newRectangleCenter, newRectangleCenter));
            }

            if (state != State.TRANSACTION) {
                endTransaction();
            }
        } else {
            ArrayList<AbstractContainer> rectangles = container.getDescendantRectangles(true, true);
            for (AbstractContainer rect : rectangles) {
                rect.transpose(moveVector);
                rect.getLayoutConstraints().transpose(moveVector);
            }

            for (Line line : container.getDescendantLines()) {
                for (Point2D.Double point : line.lineGeometry.points) {
                    point.x += moveVector.x;
                    point.y += moveVector.y;
                }
            }

            for (AbstractContainer rect : rectangles) {
                if (rect instanceof Box) {
                    for (Line line : ((Box) rect).incidentLines) {
                        ManualAdjuster.reconnectEndPoints(line);
                        for (LineLabel label : line.getLabels()) {
                            layoutAdjustOutsideLabel(label);
                        }
                    }
                }
            }

            ManualAdjuster.updateParentRectangles(container);

            updateBounds();
        }
    }

    /**
     * Resizes the rectangle to its new position in the diagram and adjusts the element positions so
     * that they comply to the layout description of the diagram. If there is an active transaction
     * stores the operation instead.
     *
     * @param rectangle the rectangle to resize
     * @param bounds the new position of the resized rectangle
     */
    void layoutResizeRectangle(AbstractContainer rectangle, Rectangle2D.Double bounds) {
        if (state != State.MANUAL) {
            transaction.addOperation(new Operation.RectangleResizeOperation(rectangle, bounds));
            if (state != State.TRANSACTION) {
                endTransaction();
            }
        } else {
            Rectangle2D.Double cer = rectangle.findDescendantEnclosingRectangle();

            if (cer == null) {
                rectangle.setBounds(bounds);
            } else {
                rectangle.left = Math.min(cer.getMinX(), bounds.getMinX());
                rectangle.right = Math.max(cer.getMaxX(), bounds.getMaxX());
                rectangle.top = Math.min(cer.getMinY(), bounds.getMinY());
                rectangle.bottom = Math.max(cer.getMaxY(), bounds.getMaxY());
            }

            if (rectangle.getLayoutConstraints() instanceof GridLayoutConstraints) {
                GridLayoutConstraints constraints = (GridLayoutConstraints) rectangle.getLayoutConstraints();
                for (LayoutConstraints.ConstraintLine border : constraints.verticalConstraints) {
                    border.pos = Math.min(Math.max(border.pos, rectangle.left), rectangle.right);
                }
                for (LayoutConstraints.ConstraintLine border : constraints.horizontalConstraints) {
                    border.pos = Math.min(Math.max(border.pos, rectangle.top), rectangle.bottom);
                }
            }

            ManualAdjuster.updateParentRectangles(rectangle);

            if (rectangle instanceof Box) {
                for (Line line : ((Box) rectangle).incidentLines) {
                    ManualAdjuster.reconnectEndPoints(line);
                }
                for (BoxOutsideLabel label : ((Box) rectangle).getOutsideLabels()) {
                    layoutAdjustOutsideLabel(label);
                }
            }

            updateBounds();
        }
    }

    /**
     * Immediately adjusts the element layout to better satisfy layout constraints.
     */
    void adjust() {
        if (state != State.MANUAL) {
            Adjuster.adjust(this);
        }
        updateBounds();
    }

    /**
     * Changes the spacing of the given rectangle and adjusts the element positions so that they
     * comply to the layout description of the diagram. If there is an active transaction stores the
     * operation instead.
     *
     * @param rectangle the rectangle whose spacing to change
     * @param spacing the new spacing of the rectangle
     */
    void layoutChangeRectangleSpacing(AbstractContainer rectangle, double spacing) {
        if (state != State.MANUAL) {
            transaction.addOperation(new Operation.SetSpacingOperation(rectangle, spacing));
            if (state != State.TRANSACTION) {
                endTransaction();
            }
        } else {
            rectangle._setSpacing(spacing);

            double oldSpacing = rectangle.spacing;
            double oldPotentialEpsilon = oldSpacing * Normalizer.STARTING_SIZE_RATIO * Normalizer.EPSILON_RATIO;
            double newPotentialEpsilon = this.spacing * Normalizer.STARTING_SIZE_RATIO * Normalizer.EPSILON_RATIO;
            if (newPotentialEpsilon < getDiagram().getEpsilon()) {
                getDiagram().adjustEpsilon(spacing);
            } else if (getDiagram().getEpsilon() == oldPotentialEpsilon) {
                getDiagram().updateEpsilon();
            }

            updateBounds();
        }
    }

    /**
     * Changes the spacing of the given line and adjusts the element positions so that they comply
     * to the layout description of the diagram. If there is an active transaction stores the
     * operation instead.
     *
     * @param line the line whose spacing to change
     * @param spacing the new spacing of the line
     */
    void layoutChangeLineSpacing(Line line, double spacing) {
        if (state != State.MANUAL) {
            transaction.addOperation(new Operation.SetSpacingOperation(line, spacing));
            if (state != State.TRANSACTION) {
                endTransaction();
            }
        } else {
            line._setSpacing(spacing);
            updateBounds();
        }
    }

    /**
     * Traces the given line so that it connects its end elements, discarding any existing geometry
     * the line may have had. If there is an active transaction stores the operation instead.
     *
     * @param line the line to retrace
     * @param lineType the possibly new type of the line
     */
    public void layoutTraceLine(Line line, Line.LineType lineType) {
        if (state != State.MANUAL) {
            transaction.addOperation(new LineOperation.NewLineOperation(line, lineType));
            if (state != State.TRANSACTION) {
                endTransaction();
            }
            // => by SK: force layout in a transaction after a loop has been added;
            // otherwise there was a null pointer exception;
            else {
              if (line.getStart() == line.getEnd()) {
                endTransaction();
                startTransaction();
              }
            }
            // <= by SK
        } else {
            switch (lineType) {
                case ORTHOGONAL:
                    line.lineGeometry = new LineGeometry.OrthogonalLine((Connectible) line.getStart(), (Connectible) line.getEnd(), line.getSpacing());
                    break;
                case STRAIGHT:
                    line.lineGeometry = new LineGeometry.StraightLine((Connectible) line.getStart(), (Connectible) line.getEnd());
                    break;
                case POLYLINE:
                    line.lineGeometry = new LineGeometry.Polyline((Connectible) line.getStart(), (Connectible) line.getEnd(), 0b1111, 0b1111, line.spacing);
                    break;
            }
            LineOptimizer.cutLine(line);
            adjustBounds(line);
        }
    }

    /**
     * Sets the points of the given line and adjusts the element positions so that they comply to
     * the layout description of the diagram. If there is an active transaction stores the operation
     * instead.
     *
     * @param line the line whose points to set
     * @param lineType the possibly new type of the line
     * @param points the new points of the line
     * @param cleanup whether to clean up the resulting line in order to minimize corner count
     */
    void layoutSetLinePoints(Line line, LineType lineType, ArrayList<Point2D.Double> points, boolean cleanup) {

        if (state != State.MANUAL) {
        	ManualAdjuster.setPoints(line, lineType, points); // << added by SK
        	updateBounds(); // << added by SK
            transaction.addOperation(new LineOperation.SetPointsOperation(line, lineType, points, cleanup));
            if (state != State.TRANSACTION) {
                endTransaction();
            }
        } else {
            ManualAdjuster.setPoints(line, lineType, points);
            updateBounds();
        }
    }

    /**
     * Cleans up the given line. If there is an active transaction stores the operation instead.
     *
     * @param line the line to clean up
     */
    void layoutCleanupLine(Line line) {
        if (state != State.MANUAL) {
            transaction.addOperation(new LineOperation.CleanupOperation(line));
            if (state != State.TRANSACTION) {
                endTransaction();
            }
        } else {
            // Do nothing.
        }
    }

    /**
     * Adjusts element positions so that they comply to the layout description of the diagram after
     * any changes in the diagram, if necessary. If there is an active transaction stores the
     * operation instead.
     */
    void layoutAdjust() {
        if (state != State.MANUAL) {
            transaction.addOperation(new Operation.AdjustOperation());
            if (state != State.TRANSACTION) {
                endTransaction();
            }
        }
    }

    /**
     * Adjusts element positions so that the given element lies in the correct cell of its parent's
     * grid after such a cell has been specified.
     *
     * @param element the element whose cell was set
     */
    void layoutSetCell(Element element) {
        if (element instanceof AbstractContainer) {
            if (state != State.MANUAL) {
                if (element instanceof Rectangular) {
                    transaction.addOperation(new Operation.PlaceRectangleOperation((AbstractContainer) element));
                } else {
                    for (AbstractContainer rectangle : ((Container) element).getNextRectangles(false)) {
                        transaction.addOperation(new Operation.PlaceRectangleOperation(rectangle));
                    }
                }

                if (state != State.TRANSACTION) {
                    endTransaction();
                }
            } else {
                updateBounds();
            }
        } else {
            throw new UnsupportedOperationException("Only boxes, labels and containers can be assigned a cell.");
        }
    }

    /**
     * Modifies the inside labels of the given outside label and adjusts the diagram element
     * positions. If there is an active transaction stores the operation instead.
     *
     * @param label the outside label whose inside labels to modify
     */
    void layoutModifyLabel(OutsideLabel label) {
        if (state != State.MANUAL) {
            if (label.getInsideLabels().isEmpty()) {
                transaction.addOperation(new Operation.AdjustOperation());
            } else {
                transaction.addOperation(new Operation.ModifyLabelOperation(label));
            }
            if (state != State.TRANSACTION) {
                endTransaction();
            }
        } else {
            layoutAdjustOutsideLabel(label);
        }
    }

    /**
     * Adjusts the diagram to accommodate changes in the given label. Normally equivalent to
     * {@link #layoutAdjust()}, but updates the labels size, contents and position when in manual
     * mode.
     *
     * @param label the label for which to adjust the diagram
     */
    void layoutAdjustOutsideLabel(OutsideLabel label) {
        /*SK:if (state != State.MANUAL) {
            layoutAdjust();
        } else*/ {            
            if (label instanceof BoxOutsideLabel) {
                BoxOutsideLabel boxLabel = (BoxOutsideLabel) label;
                Point2D.Double center = boxLabel.getCenter();
                Box box = boxLabel.getOwner();
                boxLabel.right = boxLabel.left + boxLabel.getCurrentMinWidth();
                boxLabel.bottom = boxLabel.top + boxLabel.getCurrentMinHeight();
                GeometryHelper.movePointToPerimeter(center, box);
                boxLabel.side = BoxSide.findPointSide(center, center, box);
                boxLabel.calculateBounds(center);
            } else {
                LineLabel lineLabel = (LineLabel) label;
                Point2D.Double center = lineLabel.getCenter();
                Line line = lineLabel.getOwner();
                lineLabel.segmentIndex = GeometryHelper.findNearestSegment(center, line);
                GeometryHelper.movePointToLine(center, line, lineLabel.segmentIndex);
                ArrayList<Point2D.Double> points = line.getPoints();
                
                int segmentIndex = lineLabel.segmentIndex;

                Point2D.Double first = points.get(segmentIndex), second = points.get(segmentIndex + 1);
                boolean segmentHorizontal = first.y == second.y;

                if (segmentHorizontal != lineLabel.segmentHorizontal) {
                    if (lineLabel.autoFlip) {
                        lineLabel._flip();
                    }
                    lineLabel.segmentHorizontal = segmentHorizontal;
                }
                
                GeometryHelper.placeLabelOnSegmentPoint(lineLabel,
                        points.get(lineLabel.segmentIndex), points.get(lineLabel.segmentIndex + 1), center);
            }

          //SK:  Normalizer.arrange(label);
        }
    }

    /**
     * Updates the diagram bounds to contain all its children.
     */
    void updateBounds() {
        bounds = findDescendantEnclosingRectangle();
        setBounds(getBounds());
    }

    /**
     * Updates the diagram bounds to contain the given rectangle.
     *
     * @param rectangle the given rectangle
     */
    void adjustBounds(AbstractContainer rectangle) {
        double tLeft = Math.min(bounds.getMinX(), rectangle.left);
        double tRight = Math.max(bounds.getMaxX(), rectangle.right);
        double tTop = Math.min(bounds.getMinY(), rectangle.top);
        double tBottom = Math.max(bounds.getMaxY(), rectangle.bottom);
        bounds = new Rectangle2D.Double(tLeft, tTop, tRight - tLeft, tBottom - tTop);
    }

    /**
     * Updates the diagram bounds to contain the given line.
     *
     * @param line the given line
     */
    void adjustBounds(Line line) {
        double tLeft = bounds.getMinX();
        double tTop = bounds.getMinY();
        double tRight = bounds.getMaxX();
        double tBottom = bounds.getMaxY();
        double tSpacing = line.getSpacing();
        if (line.lineGeometry.points != null) {
            for (Point2D.Double point : line.lineGeometry.points) {
                tLeft = Math.min(tLeft, point.x - tSpacing);
                tTop = Math.min(tTop, point.y - tSpacing);
                tRight = Math.max(tRight, point.x + tSpacing);
                tBottom = Math.max(tBottom, point.y + tSpacing);
            }
        }
        bounds = new Rectangle2D.Double(tLeft, tTop, tRight - tLeft, tBottom - tTop);
    }

    /**
     * Updates the diagram bounds to contain all its child boxes, ignoring lines.
     */
    void updateBoundsFromRectangles() {
        bounds = findNextRectangleEnclosingRectangle();
        setBounds(getBounds());
    }

    /**
     * Updates the diagram epsilon value. Iterates through all the diagram rectangles to find the
     * minimal spacing value.
     */
    void updateEpsilon() {
        epsilon = Double.MAX_VALUE;
        for (AbstractContainer rect : getDescendantRectangles(false, true)) {
            epsilon = Math.min(epsilon, rect.getSpacing());
        }
        epsilon = epsilon * Normalizer.STARTING_SIZE_RATIO * Normalizer.EPSILON_RATIO;
    }

    /**
     * Adjusts the diagram epsilon value, keeping it the same or lowering it if the given spacing is
     * the lowest in the diagram. Works faster than {@link #updateEpsilon()}.
     *
     * @param spacing the spacing value to check.
     */
    void adjustEpsilon(double spacing) {
        epsilon = Math.min(epsilon, spacing * Normalizer.STARTING_SIZE_RATIO * Normalizer.EPSILON_RATIO);
    }

    /**
     * Returns the smallest spacing of the elements in the diagram that participate in
     * normalization. Does this by inverting the operations used to obtain the diagram epsilon,
     * which is calculated from the smallest spacing.
     *
     * @return the smallest spacing of the diagram elements.
     */
    double getMinSpacing() {
        return epsilon / (Normalizer.STARTING_SIZE_RATIO * Normalizer.EPSILON_RATIO);
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Diagram removal is not supported");
    }

    /**
     * Saves this diagram to an XML element in the given document. Maps the objects of this diagram
     * in {@code idMap}, using {@code idMap.size()} as the ID for each new element.
     *
     * @param doc the document in which to create the element
     * @param idMap a map from the objects of this diagram to their IDs in the resulting XML.
     * @return an XML element corresponding to this diagram
     */
    @Override
    public org.w3c.dom.Element saveToXML(org.w3c.dom.Document doc, LinkedHashMap<Object, Integer> idMap) {
        if (state == State.TRANSACTION && transaction != null) {
            transaction.perform();
            transaction = null;
        }
        org.w3c.dom.Element e = doc.createElement(XMLHelper.NAMESPACE + ":diagram");
        e.setAttribute("xmlns:" + XMLHelper.NAMESPACE, XMLHelper.NAMESPACE_URI);
        e.setAttribute("epsilon", Double.toString(epsilon));
        e.setAttribute("state", state.toString());

        e.appendChild(XMLHelper.save(doc, getBounds(), XMLHelper.NAMESPACE + ":bounds"));
        e.appendChild(XMLHelper.save(doc, defaultBounds, XMLHelper.NAMESPACE + ":defaultBounds"));

        super.saveAttributes(e, doc, idMap);

        return e;
    }

    /**
     * Loads a diagram from the given XML element representing one. Maps the objects of the
     * resulting diagram in {@code objectMap}, from their IDs in the given XML.
     *
     * @param element an XML element representing a diagram.
     * @param objectMap a map from object IDs in the XML to the objects of this diagram.
     */
    public Diagram(org.w3c.dom.Element element, LinkedHashMap<Integer, Object> objectMap) {
        super(element, objectMap, null);
        epsilon = Double.valueOf(element.getAttribute("epsilon"));
        bounds = XMLHelper.loadRectangle((org.w3c.dom.Element) element.getElementsByTagName(
                XMLHelper.NAMESPACE + ":bounds").item(0));
        defaultBounds = XMLHelper.loadRectangle((org.w3c.dom.Element) element.getElementsByTagName(
                XMLHelper.NAMESPACE + ":defaultBounds").item(0));
        state = State.valueOf(element.getAttribute("state"));
    }
}
