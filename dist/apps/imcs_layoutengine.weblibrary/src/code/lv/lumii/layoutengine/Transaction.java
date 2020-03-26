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
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import lv.lumii.layoutengine.Diagram.State;
import lv.lumii.layoutengine.LayoutConstraints.GridLayoutConstraints;
import lv.lumii.layoutengine.LayoutConstraints.GridLayoutConstraints.LaneElement;
import lv.lumii.layoutengine.Line.LineType;
import lv.lumii.layoutengine.OutsideLabel.LineLabel;
import lv.lumii.layoutengine.Transaction.Operation.LineOperation;
import lv.lumii.layoutengine.Transaction.Operation.ModifyLabelOperation;

/**
 * A class for storing operation data in transaction process and transaction algorithms, which allow
 * for much more efficient execution of multiple diagram operations than multiple one-time operation
 * execution. The idea of the transaction is to collect desired geometrical change data and perform
 * the layout of the diagram at once in the end of the transaction. However, hierarchical changes
 * are not being collected and are executed along with the operation call.
 *
 * Each diagram holds one instance of this class. If enabled, the transaction of the diagram is one
 * of the specific transactions, which work for the concrete type of the diagram operations.
 *
 * @author Evgeny Vihrov
 */
abstract class Transaction {

    //<editor-fold defaultstate="collapsed" desc="attributes">
    /**
     * The diagram of the transaction.
     */
    Diagram diagram;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="constructors">
    /**
     * Abstract constructor, assigns the diagram of the transaction.
     *
     * @param diagram the diagram of the transaction.
     */
    Transaction(Diagram diagram) {
        this.diagram = diagram;
    }
    //</editor-fold>

    /**
     * Adds a new operation to the transaction at the end of the collected list of operations.
     *
     * @param operation the new operation.
     */
    abstract void addOperation(Operation operation);

    /**
     * Performs the transaction process on the collected operations.
     */
    abstract void perform();

    /**
     * A class for storing diagram operations.
     */
    static abstract class Operation {

        //<editor-fold defaultstate="collapsed" desc="AdjustOperation">
        /**
         * This class handles operations that only need diagram adjustment afterwards.
         */
        static class AdjustOperation extends Operation {

            /**
             * Prepares the current transaction of a diagram for the next adjust operation. If the
             * previous transaction has a different type, then performs the previous transaction and
             * creates a new adjust transaction.
             *
             * @param diagram the diagram for which to prepare the transaction
             */
            static void prepareTransaction(Diagram diagram) {
                prepareTransaction(diagram, false);
            }

            /**
             * Prepares the current transaction of a diagram for the next adjust operation. If the
             * previous transaction has a different type, then performs the previous transaction and
             * creates a new adjust transaction.
             *
             * @param diagram the diagram for which to prepare the transaction
             * @param removeNullLines whether to remove nonexistent lines connecting boxes between
             * different diagrams
             */
            static void prepareTransaction(Diagram diagram, boolean removeNullLines) {
                if (diagram.state != State.MANUAL) {
                    Transaction transaction = diagram.transaction;
                    if (transaction == null) {
                        diagram.transaction = new AdjustTransaction(diagram);
                    } else if (!(transaction instanceof AdjustTransaction)) {
                        transaction.perform();
                        diagram.transaction = new AdjustTransaction(diagram);
                    }
                    if (removeNullLines) {
                        ((AdjustTransaction) diagram.transaction).removeNullLines = true;
                    }
                }
            }
        }
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="RectangleResizeOperation">
        /**
         * This class handles the operation of the bounds resizing.
         */
        static class RectangleResizeOperation extends Operation {

            /**
             * The element to resize.
             */
            AbstractContainer rectangle;
            /**
             * The position and dimensions in which the element should be resized.
             */
            Rectangle2D.Double bounds;

            /**
             * Creates a new element resizing operation.
             *
             * @param rectangle the element to resize
             * @param bounds the position and dimensions in which the element should be resized
             */
            RectangleResizeOperation(AbstractContainer rectangle, Rectangle2D.Double bounds) {
                this.rectangle = rectangle;
                this.bounds = bounds;
            }

            /**
             * Prepares the current transaction of a diagram for the next element resizing
             * operation. If the previous transaction has a different type, then performs the
             * previous transaction and creates a new element resizing transaction.
             *
             * @param diagram the diagram for which to prepare transaction
             */
            static void prepareTransaction(Diagram diagram) {
                if (diagram.state != State.MANUAL) {
                    Transaction transaction = diagram.transaction;
                    if (transaction == null) {
                        diagram.transaction = new RectangleResizeTransaction(diagram);
                    } else if (!(transaction instanceof RectangleResizeTransaction)) {
                        transaction.perform();
                        diagram.transaction = new RectangleResizeTransaction(diagram);
                    }
                }
            }
        }
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="SetSpacingOperation">
        /**
         * This class handles the element spacing setting operation. It either stores a element
         * spacing change operation, or a line spacing change operation.
         */
        static class SetSpacingOperation extends Operation {

            /**
             * The element whose spacing to change.
             */
            AbstractContainer rectangle;
            /**
             * The line whose spacing to change.
             */
            Line line;
            /**
             * The new spacing of the element.
             */
            double spacing;

            /**
             * Creates a new element spacing change operation.
             *
             * @param rectangle the element whose spacing to change
             * @param spacing the new spacing of the element
             */
            SetSpacingOperation(AbstractContainer rectangle, double spacing) {
                this.rectangle = rectangle;
                this.spacing = spacing;
            }

            /**
             * Creates a new line spacing change operation.
             *
             * @param line the line whose spacing to change
             * @param spacing the new spacing of the line
             */
            SetSpacingOperation(Line line, double spacing) {
                this.line = line;
                this.spacing = spacing;
            }

            /**
             * Prepares the current transaction of a diagram for the next spacing change operation.
             * If the previous transaction has a different type, then performs the previous
             * transaction and creates a new spacing change transaction.
             *
             * @param diagram the diagram for which to prepare transaction
             */
            static void prepareTransaction(Diagram diagram) {
                if (diagram.state != State.MANUAL) {
                    Transaction transaction = diagram.transaction;
                    if (transaction == null) {
                        diagram.transaction = new SetSpacingTransaction(diagram);
                    } else if (!(transaction instanceof SetSpacingTransaction)) {
                        transaction.perform();
                        diagram.transaction = new SetSpacingTransaction(diagram);
                    }
                }
            }
        }
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="LineOperation">
        /**
         * A superclass for various line operation.
         */
        static abstract class LineOperation extends Operation {

            /**
             * The line on which this operation will act.
             */
            Line line;

            /**
             * A list of possible line operation types.
             */
            static enum Type {

                /**
                 * Creating a new line.
                 */
                NEWLINE {
                            @Override
                            Class getTransactionClass() {
                                return LineTransaction.NewLineTransaction.class;
                            }

                            @Override
                            Transaction newTransaction(Diagram diagram, Box aParent, Box bParent, Box lca) {
                                return new LineTransaction.NewLineTransaction(diagram, aParent, bParent, lca);
                            }
                        },
                /**
                 * Setting a lines points.
                 */
                SETPOINTS {
                            @Override
                            Class getTransactionClass() {
                                return LineTransaction.SetPointsTransaction.class;
                            }

                            @Override
                            Transaction newTransaction(Diagram diagram, Box aParent, Box bParent, Box lca) {
                                return new LineTransaction.SetPointsTransaction(diagram, aParent, bParent, lca);
                            }
                        },
                /**
                 * Cleaning up a line.
                 */
                CLEANUP {
                            @Override
                            Class getTransactionClass() {
                                return LineTransaction.CleanupTransaction.class;
                            }

                            @Override
                            Transaction newTransaction(Diagram diagram, Box aParent, Box bParent, Box lca) {
                                return new LineTransaction.CleanupTransaction(diagram, aParent, bParent, lca);
                            }
                        };

                /**
                 * Finds the class of transactions corresponding to this operation type.
                 *
                 * @return the class of transactions corresponding to this operation type.
                 */
                abstract Class getTransactionClass();

                /**
                 * Creates a new transaction corresponding to this operation type.
                 *
                 * @param diagram the diagram in which to create the new transaction
                 * @param aParent the common parent of one of the boxes connected by the lines in
                 * the new transaction
                 * @param bParent the common parent of the other of the boxes connected by the lines
                 * in the new transaction
                 * @param lca the LCA of the boxes connected by the lines in the new transaction
                 * @return a new transaction corresponding to this operation type
                 */
                abstract Transaction newTransaction(Diagram diagram, Box aParent, Box bParent, Box lca);
            }

            /**
             * Creates a new line operation for the given line.
             *
             * @param line the line for which to create the line operation.
             */
            LineOperation(Line line) {
                this.line = line;
                if (!(line.getStart() instanceof Box) || !(line.getEnd() instanceof Box)) {
                    throw new UnsupportedOperationException("Lines connected to lines are not supported yet.");
                }
            }

            /**
             * Prepares the diagram for a line operation of the given type. If the new operation can
             * be placed into the current operation does so. Otherwise performs the previous
             * transaction. If it was a line transaction, does so without adjusting the diagram.
             *
             * @param diagram the diagram in which the new operation will take place
             * @param a the start element of the line on which this operation will be performed
             * @param b the end element of the line on which this operation will be performed
             * @param type the operation type for this line operation
             */
            static void prepareTransaction(Diagram diagram, Box a, Box b, Type type) {
                if (diagram.state != State.MANUAL) {
                    Transaction transaction = diagram.transaction;

                    Class currentClass = null;
                    if (transaction != null) {
                        currentClass = transaction.getClass();
                    }
                    Class newClass = type.getTransactionClass();

                    Box aParent = a.getPrevBox(), bParent = b.getPrevBox(), lca;
                    if (a == b) {
                        lca = aParent;
                    } else {
                        lca = a.findLCABox(b);
                    }
                    if (transaction instanceof LineTransaction) {
                        LineTransaction t = (LineTransaction) transaction;
                        if (currentClass == newClass) {
                            if (!(((aParent == t.firstParent && bParent == t.secondParent) || (aParent == t.secondParent && bParent == t.firstParent)) && lca == t.lca)) {
                                t.performWithoutAdjust();
                                diagram.transaction = type.newTransaction(diagram, aParent, bParent, lca);
                            }
                        } else {
                            t.performWithoutAdjust();
                            diagram.transaction = type.newTransaction(diagram, aParent, bParent, lca);
                        }
                    } else {
                        if (transaction != null) {
                            transaction.perform();
                        }
                        diagram.transaction = type.newTransaction(diagram, aParent, bParent, lca);
                    }
                }
            }

            //<editor-fold defaultstate="collapsed" desc="NewLineOperation">
            /**
             * This class handles the line tracing operation.
             */
            static class NewLineOperation extends LineOperation {

                /**
                 * The desired type of the line geometry.
                 */
                LineType lineType;

                /**
                 * Creates a new line tracing operation.
                 *
                 * @param line the line to trace
                 * @param lineType the desired type of the line geometry
                 */
                public NewLineOperation(Line line, LineType lineType) {
                    super(line);
                    this.lineType = lineType;
                }

                /**
                 * Prepares the current transaction of a diagram for the line tracing operation. If
                 * the previous transaction has a different type, then performs the previous
                 * transaction and creates a new line tracing transaction.
                 *
                 * @param diagram the diagram for which to prepare transaction
                 * @param a the start element of the line that will be traced
                 * @param b the end element of the line that will be traced
                 */
                static void prepareTransaction(Diagram diagram, Box a, Box b) {
                    LineOperation.prepareTransaction(diagram, a, b, Type.NEWLINE);
                }

                /**
                 * Prepares the current transaction of a diagram for the line tracing operation. If
                 * the previous transaction has a different type, then performs the previous
                 * transaction and creates a new line tracing transaction. Does this also if the
                 * line to trace in the next operation does not have its geometry constructed yet,
                 * since we assume it should have been built during the previous operations that
                 * will be executed along performing the previous transaction.
                 *
                 * @param diagram the diagram for which to prepare transaction
                 * @param lineGeometry the geometry of the line
                 * @param a the start element of the line that will be traced
                 * @param b the end element of the line that will be traced
                 */
                static void prepareTransaction(Diagram diagram, LineGeometry lineGeometry, Box a, Box b) {
                    if (diagram.state != State.MANUAL) {
                        Transaction transaction = diagram.transaction;
                        if (transaction != null && lineGeometry == null) {
                            if (transaction instanceof LineTransaction) {
                                ((LineTransaction) transaction).performWithoutAdjust();
                            } else {
                                transaction.perform();
                            }
                            diagram.transaction = null;
                        }
                        prepareTransaction(diagram, a, b);
                    }
                }
            }
            //</editor-fold>

            //<editor-fold defaultstate="collapsed" desc="SetPointsOperation">
            /**
             * This class handles the line point setting operation.
             */
            static class SetPointsOperation extends LineOperation {

                /**
                 * The new desired type of the line geometry.
                 */
                LineType lineType;
                /**
                 * The new points of the line.
                 */
                ArrayList<Point2D.Double> points;
                /**
                 * Whether to cleanup this line.
                 */
                boolean cleanup;

                /**
                 * Creates a new line point setting operation.
                 *
                 * @param line the line whose points to change
                 * @param lineType the new desired type of the line geometry
                 * @param points the new points of the line
                 * @param cleanup whether to clean up the resulting line in order to minimize corner
                 * count
                 */
                public SetPointsOperation(Line line, LineType lineType, ArrayList<Point2D.Double> points, boolean cleanup) {
                    super(line);
                    this.lineType = lineType;
                    this.points = points;
                    this.cleanup = cleanup;
                }

                /**
                 * Prepares the current transaction of a diagram for the next line point setting
                 * operation. If the previous transaction has a different type, then performs the
                 * previous transaction and creates a new line point setting transaction.
                 *
                 * @param diagram the diagram for which to prepare transaction
                 * @param a the start element of the line
                 * @param b the end element of the line
                 */
                static void prepareTransaction(Diagram diagram, Box a, Box b) {
                    LineOperation.prepareTransaction(diagram, a, b, Type.SETPOINTS);
                }
            }
            //</editor-fold>

            //<editor-fold defaultstate="collapsed" desc="CleanupOperation">
            /**
             * This class handles the line cleanup operation.
             */
            static class CleanupOperation extends LineOperation {

                /**
                 * Creates a new line point setting operation.
                 *
                 * @param line the line whose points to change
                 */
                public CleanupOperation(Line line) {
                    super(line);
                }

                /**
                 * Prepares the current transaction of a diagram for the next line point setting
                 * operation. If the previous transaction has a different type, then performs the
                 * previous transaction and creates a new line point setting transaction.
                 *
                 * @param diagram the diagram for which to prepare transaction
                 * @param a the start element of the line
                 * @param b the end element of the line
                 */
                static void prepareTransaction(Diagram diagram, Box a, Box b) {
                    prepareTransaction(diagram, a, b, Type.CLEANUP);
                }
            }
            //</editor-fold>
        }
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="PlaceRectangleOperation">
        /**
         * This class handles bounds placement operations - creating bounds, moving bounds, setting
         * bounds cells in grids.
         */
        static class PlaceRectangleOperation extends Operation {

            /**
             * The rectangle for which to perform the operation.
             */
            AbstractContainer rectangle;

            /**
             * Creates a new rectangle placing operation.
             *
             * @param rectangle the rectangle to place.
             */
            public PlaceRectangleOperation(AbstractContainer rectangle) {
                this.rectangle = rectangle;
            }

            /**
             * Prepares the diagram for a place rectangle operation. If the new operation can be
             * placed into the current operation does so. Otherwise performs the previous
             * transaction.
             *
             * @param diagram the diagram for which to prepare transaction
             * @param prevRect the upper rectangular container in which to place rectangles
             */
            static void prepareTransaction(Diagram diagram, AbstractContainer prevRect) {
                if (diagram.state != State.MANUAL) {
                    Transaction transaction = diagram.transaction;
                    if (transaction == null) {
                        diagram.transaction = new PlaceRectangleTransaction(diagram, prevRect);
                    } else if (!(transaction instanceof PlaceRectangleTransaction)) {
                        transaction.perform();
                        diagram.transaction = new PlaceRectangleTransaction(diagram, prevRect);
                    } else {
                        PlaceRectangleTransaction setCellTransaction = (PlaceRectangleTransaction) transaction;
                        if (setCellTransaction.prevRect != prevRect) {
                            transaction.perform();
                            diagram.transaction = new PlaceRectangleTransaction(diagram, prevRect);
                        }
                    }
                }
            }

            /**
             * This operation handles bounds placement after creation or movement.
             */
            static class RectangleMoveOperation extends PlaceRectangleOperation {

                /**
                 * The new desired center of the bounds.
                 */
                Point2D.Double newCenter;
                /**
                 * The point where to insert and grow the bounds.
                 */
                Point2D.Double growPoint;

                /**
                 * Creates a new bounds movement operation. Also used for bounds creation.
                 *
                 * @param rectangle the bounds to move
                 * @param newCenter the desired new center of the bounds
                 * @param growPoint the point from which to grow the new bounds
                 */
                RectangleMoveOperation(AbstractContainer rectangle, Point2D.Double newCenter, Point2D.Double growPoint) {
                    super(rectangle);
                    this.newCenter = newCenter;
                    this.growPoint = growPoint;
                }
            }
        }
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="ModifyLabelOperation">
        /**
         * This class handles outside label inside label modification operations.
         */
        static class ModifyLabelOperation extends Operation {

            /**
             * The outside label whose inside labels to modify.
             */
            OutsideLabel label;

            /**
             * Creates a new label modification operation.
             *
             * @param label the label whose inside labels to modify
             */
            public ModifyLabelOperation(OutsideLabel label) {
                this.label = label;
            }

            /**
             * Prepares the diagram for a label modification operation. If the new operation can be
             * placed into the current operation does so. Otherwise performs the previous
             * transaction.
             *
             * @param diagram the diagram for which to prepare transaction
             */
            static void prepareTransaction(Diagram diagram) {
                if (diagram.state != State.MANUAL) {
                    Transaction transaction = diagram.transaction;
                    if (transaction == null) {
                        diagram.transaction = new ModifyLabelTransaction(diagram);
                    } else if (!(transaction instanceof ModifyLabelTransaction)) {
                        transaction.perform();
                        diagram.transaction = new ModifyLabelTransaction(diagram);
                    }
                }
            }
        }
        //</editor-fold>
    }

    //<editor-fold defaultstate="collapsed" desc="AdjustTransaction">
    /**
     * This transaction handles operations that need a single diagram layout adjusting afterwards.
     */
    private static class AdjustTransaction extends Transaction {

        /**
         * Whether to remove lines connected to this diagram's boxes without owner. This can be
         * necessary if the other end of the line has been moved to a different diagram and that
         * diagram's {@link PlaceRectangleTransaction} has not been performed yet.
         */
        boolean removeNullLines;

        /**
         * Creates a new adjust transaction.
         *
         * @param diagram the diagram of the transaction
         */
        AdjustTransaction(Diagram diagram) {
            super(diagram);
        }

        @Override
        void addOperation(Operation operation) {
            assert operation instanceof Operation.AdjustOperation;
        }

        @Override
        void perform() {
            if (removeNullLines) {
                for (Box box : diagram.getDescendantBoxes()) {
                    for (Line line : box.getIncidentLines()) {
                        if (line.owner == null) {
                            ((Box) line.getStart()).incidentLines.remove(line);
                            ((Box) line.getEnd()).incidentLines.remove(line);
                        }
                    }
                }
            }
            diagram.updateEpsilon();
            diagram.adjust();
            Adjuster.growOutsideLabels(diagram);
            Adjuster.updateBoxCurrentMinSize(diagram);
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="BoxResizeTransaction">
    /**
     * This transaction handles the element resizing operations.
     */
    private static class RectangleResizeTransaction extends Transaction {

        /**
         * The rectangles to resize.
         */
        ArrayList<AbstractContainer> rectangles;
        /**
         * The positions and dimensions in which the rectangles should be resized.
         */
        ArrayList<Rectangle2D.Double> bounds;

        /**
         * Creates a new element resizing transactions.
         *
         * @param diagram the diagram of the transaction
         */
        public RectangleResizeTransaction(Diagram diagram) {
            super(diagram);
            rectangles = new ArrayList<>();
            bounds = new ArrayList<>();
        }

        @Override
        void addOperation(Operation operation) {
            assert operation instanceof Operation.RectangleResizeOperation;
            Operation.RectangleResizeOperation tOperation = (Operation.RectangleResizeOperation) operation;
            rectangles.add(tOperation.rectangle);
            bounds.add(tOperation.bounds);
        }

        @Override
        void perform() {
            Adjuster.resizeRectangles(rectangles, bounds);
            Adjuster.growOutsideLabels(diagram);
            Adjuster.updateBoxCurrentMinSize(diagram);
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="SetSpacingTransaction">
    /**
     * This transaction handles the spacing change operations.
     */
    private static class SetSpacingTransaction extends Transaction {

        /**
         * The rectangles whose spacings to change.
         */
        ArrayList<AbstractContainer> rectangles;
        /**
         * The lines whose spacings to change.
         */
        ArrayList<Line> lines;
        /**
         * The new spacings of the boxes.
         */
        ArrayList<Double> rectangleSpacings;
        /**
         * The new spacing of the lines.
         */
        ArrayList<Double> lineSpacings;

        /**
         * Creates a new spacing change transaction.
         *
         * @param diagram the diagram of the transaction
         */
        public SetSpacingTransaction(Diagram diagram) {
            super(diagram);
            rectangles = new ArrayList<>();
            lines = new ArrayList<>();
            rectangleSpacings = new ArrayList<>();
            lineSpacings = new ArrayList<>();
        }

        @Override
        void addOperation(Operation operation) {
            assert operation instanceof Operation.SetSpacingOperation;
            Operation.SetSpacingOperation tOperation = (Operation.SetSpacingOperation) operation;
            if (tOperation.rectangle != null) {
                rectangles.add(tOperation.rectangle);
                rectangleSpacings.add(tOperation.spacing);
            } else if (tOperation.line != null) {
                lines.add(tOperation.line);
                lineSpacings.add(tOperation.spacing);
            }
        }

        @Override
        void perform() {
            Adjuster.changeSpacings(rectangles, rectangleSpacings, lines, lineSpacings);
            Adjuster.growOutsideLabels(diagram);
            Adjuster.updateBoxCurrentMinSize(diagram);
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="LineTransaction">
    /**
     * A superclass for transaction laying out lines.
     */
    private abstract static class LineTransaction extends Transaction {

        /**
         * first common parent of the boxes connected by the lines in this transaction.
         */
        Box firstParent;
        /**
         * The second common parent of the boxes connected by the lines in this transaction.
         */
        Box secondParent;
        /**
         * The LCA of the boxes connected by the lines in this transaction. Can in some cases be
         * nested deeper than either parent, if the lines connect to the inside of a element.
         */
        Box lca;

        /**
         * Creates a new transaction for laying out lines. Only lines with the same set of obstacle
         * boxes can be handled in the same transaction. This set is defined by the parent of the
         * connectible boxes and their LCA.
         *
         * @param diagram the diagram in which to create the new transaction
         * @param firstParent the common parent of one of the boxes connected by the lines in the
         * new transaction
         * @param secondParent the common parent of the other of the boxes connected by the lines in
         * the new transaction
         * @param lca the LCA of the boxes connected by the lines in the new transaction
         */
        LineTransaction(Diagram diagram, Box firstParent, Box secondParent, Box lca) {
            super(diagram);
            this.firstParent = firstParent;
            this.secondParent = secondParent;
            this.lca = lca;
        }

        /**
         * Performs this line transaction without adjusting the diagram. Used when performing
         * multiple line transaction in a row, as adjusting is needed only after the last one.
         */
        abstract void performWithoutAdjust();

        //<editor-fold defaultstate="collapsed" desc="NewLineTransaction">
        /**
         * This transaction handles the line tracing operations. Currently, it only traces together
         * lines whose end-boxes have the same pair of parents and the same LCA.
         */
        private static class NewLineTransaction extends LineTransaction {

            /**
             * The lines to trace.
             */
            ArrayList<Line> lines;
            /**
             * The new desired geometrical types of the lines.
             */
            ArrayList<LineType> lineTypes;

            /**
             * Creates a new line tracing transaction.
             *
             * @param diagram the diagram of the transaction.
             * @param firstParent the first common parent of the boxes connected by the lines in
             * this transaction
             * @param secondParent the second common parent of the boxes connected by the lines in
             * this transaction
             * @param lca the LCA of the boxes connected by the lines in this transaction. Can in
             * some cases be nested deeper than either parent, if the lines connect to the inside of
             * a element
             */
            public NewLineTransaction(Diagram diagram, Box firstParent, Box secondParent, Box lca) {
                super(diagram, firstParent, secondParent, lca);
                lines = new ArrayList<>();
                lineTypes = new ArrayList<>();
            }

            @Override
            void addOperation(Operation operation) {
                assert operation instanceof LineOperation.NewLineOperation;
                LineOperation.NewLineOperation tOperation = (LineOperation.NewLineOperation) operation;
                lines.add(tOperation.line);
                lineTypes.add(tOperation.lineType);
            }

            @Override
            void perform() {
                // => try-catch by SK
                try {
                  performWithoutAdjust();
                  diagram.adjust();
                  Adjuster.growOutsideLabels(diagram);
                  Adjuster.updateBoxCurrentMinSize(diagram);
                }
                catch(Throwable t) {}
                // <= try-catch by SK
            }

            /**
             * Traces the lines stored in this transaction, without normalizing the diagram.
             */
            @Override
            void performWithoutAdjust() {
                Tracer tracer = new Tracer(firstParent, secondParent, lca);
                for (int i = 0; i < lines.size(); i++) {
                    lines.get(i).trace(lineTypes.get(i), tracer);
                }

                // => try-catch by SK
                try {
                  LineOptimizer.snapNewLines(lines);
                }
                catch(Throwable t) {}
                // <= try-catch by SK

                /**
                 * Places the labels after the lines were retraced.
                 */
                for (Line line : lines) {
                    line._resetLabels();
                }
            }
        }
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="SetPointsTransaction">
        /**
         * This transaction handles the line point setting operations.
         */
        private static class SetPointsTransaction extends LineTransaction {

            /**
             * The lines whose points to change.
             */
            ArrayList<Line> lines;
            /**
             * The new desired geometrical types of the lines.
             */
            ArrayList<LineType> lineTypes;
            /**
             * The new points of the lines.
             */
            ArrayList<ArrayList<Point2D.Double>> pointLists;
            /**
             * For each line stores whether it should be cleaned up after its new points are set.
             */
            ArrayList<Boolean> cleanupFlags;

            /**
             * Creates a new line point setting transaction.
             *
             * @param diagram the diagram of the transaction
             * @param firstParent the first common parent of the boxes connected by the lines in
             * this transaction
             * @param secondParent the second common parent of the boxes connected by the lines in
             * this transaction
             * @param lca the LCA of the boxes connected by the lines in this transaction. Can in
             * some cases be nested deeper than either parent, if the lines connect to the inside of
             * a element
             */
            public SetPointsTransaction(Diagram diagram, Box firstParent, Box secondParent, Box lca) {
                super(diagram, firstParent, secondParent, lca);
                lines = new ArrayList<>();
                lineTypes = new ArrayList<>();
                pointLists = new ArrayList<>();
                cleanupFlags = new ArrayList<>();
            }

            @Override
            void addOperation(Operation operation) {
                assert operation instanceof LineOperation.SetPointsOperation;
                LineOperation.SetPointsOperation tOperation = (LineOperation.SetPointsOperation) operation;
                lines.add(tOperation.line);
                lineTypes.add(tOperation.lineType);
                pointLists.add(tOperation.points);
                cleanupFlags.add(tOperation.cleanup);
            }

            @Override
            void perform() {
                // => try-catch by SK
                try {
                  performWithoutAdjust();
                  diagram.adjust();
                  Adjuster.growOutsideLabels(diagram);
                  Adjuster.updateBoxCurrentMinSize(diagram);
                }
                catch(Throwable t) {}
                // <= try-catch by SK
            }

            /**
             * Sets the points of the stored lines and cleans up any lines marked for cleanup.
             */
            @Override
            void performWithoutAdjust() {
                EnumMap<Line.LineType, ArrayList<Integer>> lineIds = new EnumMap<>(Line.LineType.class);
                for (Line.LineType lineType : Line.LineType.values()) {
                    lineIds.put(lineType, new ArrayList<Integer>());
                }
                for (int i = 0; i < lineTypes.size(); i++) {
                    lineIds.get(lineTypes.get(i)).add(i);
                }
                for (Line.LineType lineType : Line.LineType.values()) {
                    ArrayList<Integer> ids = lineIds.get(lineType);
                    ArrayList<Line> linesToSet = new ArrayList<>();
                    ArrayList<ArrayList<Point2D.Double>> pointsToSet = new ArrayList<>();
                    for (int i : ids) {
                        linesToSet.add(lines.get(i));
                        pointsToSet.add(pointLists.get(i));
                    }
                    if (!linesToSet.isEmpty()) {
                      // => try-catch by SK
                      try {
                        Adjuster.setLinePoints(linesToSet, lineType, pointsToSet);
                      }
                      catch(Throwable t) {}
                      // <= try-catch by SK
                    }
                }

                ArrayList<Line> linesToCleanup = new ArrayList<>();
                for (int i : lineIds.get(Line.LineType.ORTHOGONAL)) {
                    if (cleanupFlags.get(i)) {
                        linesToCleanup.add(lines.get(i));
                    }
                }
                LineOptimizer.cleanupLines(linesToCleanup);
            }
        }
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="CleanupTransaction">
        /**
         * This transaction handles line cleanup operations.
         */
        private static class CleanupTransaction extends LineTransaction {

            /**
             * The lines to clean up.
             */
            ArrayList<Line> lines;

            /**
             * Creates a new line point setting transaction.
             *
             * @param diagram the diagram of the transaction
             * @param firstParent the first common parent of the boxes connected by the lines in
             * this transaction
             * @param secondParent the second common parent of the boxes connected by the lines in
             * this transaction
             * @param lca the LCA of the boxes connected by the lines in this transaction. Can in
             * some cases be nested deeper than either parent, if the lines connect to the inside of
             * a element
             */
            public CleanupTransaction(Diagram diagram, Box firstParent, Box secondParent, Box lca) {
                super(diagram, firstParent, secondParent, lca);
                lines = new ArrayList<>();
            }

            @Override
            void addOperation(Operation operation) {
                assert operation instanceof LineOperation.CleanupOperation;
                LineOperation.CleanupOperation tOperation = (LineOperation.CleanupOperation) operation;
                if (tOperation.line.getType() == Line.LineType.ORTHOGONAL) {
                    lines.add(tOperation.line);
                }
            }

            @Override
            void perform() {
                performWithoutAdjust();
                diagram.adjust();
                Adjuster.growOutsideLabels(diagram);
                Adjuster.updateBoxCurrentMinSize(diagram);
            }

            /**
             * Cleans up the stored lines, without normalization.
             */
            @Override
            void performWithoutAdjust() {
                LineOptimizer.cleanupLines(lines);
            }
        }
        //</editor-fold>
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="PlaceRectangleTransaction">
    /**
     * This transaction handles the place and set cell operations for rectangular abstract
     * containers. Works only for the elements that all have the same owner.
     */
    private static class PlaceRectangleTransaction extends Transaction {

        /**
         * The new desired centers of the rectangles.
         */
        LinkedHashMap<AbstractContainer, Point2D.Double> newCenters;
        /**
         * The points where to insert and grow the rectangles.
         */
        LinkedHashMap<AbstractContainer, Point2D.Double> growPoints;
        /**
         * The elements whose cells have been set. These elements are the ones that are moved if any
         * need to be moved to maintain grid consistency.
         */
        ArrayList<AbstractContainer> elements;
        /**
         * For each descendant container of the upper rectangle maps the sets of its descendant
         * elements that can be moved.
         */
        HashMap<AbstractContainer, LinkedHashSet<AbstractContainer>> descendantsToMove;
        /**
         * The element all the moved boxes and elements with new cells set in this transaction
         * belong to.
         */
        AbstractContainer prevRect;

        /**
         * Creates a new bounds placing transaction.
         *
         * @param diagram the diagram of the transaction
         * @param prevRect the common upper rectangular container of the elements in the transaction
         */
        public PlaceRectangleTransaction(Diagram diagram, AbstractContainer prevRect) {
            super(diagram);
            this.prevRect = prevRect;
            newCenters = new LinkedHashMap<>();
            growPoints = new LinkedHashMap<>();
            elements = new ArrayList<>();
            descendantsToMove = new HashMap<>();
        }

        @Override
        void addOperation(Operation operation) {
            assert operation instanceof Operation.PlaceRectangleOperation;
            if (operation instanceof Operation.PlaceRectangleOperation.RectangleMoveOperation) {
                Operation.PlaceRectangleOperation.RectangleMoveOperation tOperation
                        = (Operation.PlaceRectangleOperation.RectangleMoveOperation) operation;
                AbstractContainer rectangle = tOperation.rectangle;
                newCenters.put(rectangle, tOperation.newCenter);
                growPoints.put(rectangle, tOperation.growPoint);
            } else {
                Operation.PlaceRectangleOperation tOperation = (Operation.PlaceRectangleOperation) operation;
                elements.add(tOperation.rectangle);
            }
        }

        /**
         * Arranges the grid row and column borders of the given container and all its descendants.
         * If some element is moved during this phase, sets its growPoint to the center where it was
         * put.
         *
         * @param container the container whose grid to arrange
         */
        private void arrangeNestedGrid(AbstractContainer container) {
            if (container.getLayoutConstraints() instanceof GridLayoutConstraints) {
                LinkedHashSet<AbstractContainer> toMove = descendantsToMove.get(container);
                if (toMove == null) {
                    toMove = new LinkedHashSet<>();
                }
                GridLayoutConstraints constraints = (GridLayoutConstraints) container.getLayoutConstraints();
                ArrayList<LaneElement> laneElements;
                ArrayList<Double> positions;

                /*
                 * Arranges column borders.
                 */
                laneElements = new ArrayList<>();
                for (AbstractContainer rect : container.getRectangles()) {
                    Integer col = constraints.getColumn(rect);
                    if (col != null) {
                        laneElements.add(new LaneElement(newCenters.containsKey(rect) ? newCenters.get(rect).x : rect.getCenterX(), col, !toMove.contains(rect), rect.left, rect.right));
                    }
                }
                for (Container childContainer : container.getPureContainers()) {
                    Integer col = constraints.getColumn(childContainer);
                    boolean constant = !toMove.contains(childContainer);
                    if (col != null) {
                        for (AbstractContainer rect : childContainer.getNextRectangles(false)) {
                            laneElements.add(new LaneElement(rect.getCenterX(), col, constant, rect.left, rect.right));
                        }
                    }
                }
                positions = constraints.arrangeGridBorders(laneElements, true,
                        constraints.getColumnLeft(1), constraints.getColumnRight(constraints.getColumnCount()));
                for (int i = 1; i < positions.size(); i++) {
                    constraints._setColumnRight(i, positions.get(i));
                }

                /*
                 * Arranges row borders.
                 */
                laneElements = new ArrayList<>();
                for (AbstractContainer rect : container.getRectangles()) {
                    Integer row = constraints.getRow(rect);
                    if (row != null) {
                        laneElements.add(new LaneElement(newCenters.containsKey(rect) ? newCenters.get(rect).y : rect.getCenterY(), row, !toMove.contains(rect), rect.top, rect.bottom));
                    }
                }
                for (Container childContainer : container.getPureContainers()) {
                    Integer row = constraints.getRow(childContainer);
                    boolean constant = !toMove.contains(childContainer);
                    if (row != null) {
                        for (AbstractContainer rect : childContainer.getNextRectangles(false)) {
                            laneElements.add(new LaneElement(rect.getCenterY(), row, constant, rect.top, rect.bottom));
                        }
                    }
                }
                positions = constraints.arrangeGridBorders(laneElements, false,
                        constraints.getRowTop(1), constraints.getRowBottom(constraints.getRowCount()));
                for (int i = 1; i < positions.size(); i++) {
                    constraints._setRowBottom(i, positions.get(i));
                }

                /*
                 * Moves the elements to its cell if they aren't inside it.
                 */
                for (AbstractContainer element : container.getAbstractContainers(true)) {
                    Integer row = constraints.getRow(element), col = constraints.getColumn(element);

                    double left, top, right, bottom;
                    if (col != null) {
                        left = constraints.getColumnLeft(col);
                        right = constraints.getColumnRight(col);
                    } else {
                        left = constraints.getColumnLeft(1);
                        right = constraints.getColumnRight(constraints.getColumnCount());
                    }
                    if (row != null) {
                        top = constraints.getRowTop(row);
                        bottom = constraints.getRowBottom(row);
                    } else {
                        top = constraints.getRowTop(1);
                        bottom = constraints.getRowBottom(constraints.getRowCount());
                    }

                    if (element instanceof Rectangular) {
                        if (toMove.contains(element)) {
                            moveRectangleIntoBounds(element, left, right, top, bottom);
                        }
                    } else if (element instanceof Container) {
                        LinkedHashSet<AbstractContainer> currToMove = descendantsToMove.get(element);
                        if (currToMove != null) {
                            for (AbstractContainer rect : descendantsToMove.get(element)) {
                                moveRectangleIntoBounds(rect, left, right, top, bottom);
                            }
                        }
                    }
                }
            }

            for (Container childContainer : container.getPureContainers()) {
                arrangeNestedGrid(childContainer);
            }
        }

        /**
         * Moves the rectangle into the given bounds.
         *
         * @param rect the rectangle to move
         * @param left the left bound
         * @param right the right bound
         * @param top the top bound
         * @param bottom the bottom bound
         */
        private void moveRectangleIntoBounds(AbstractContainer rect, double left, double right, double top, double bottom) {
            Point2D.Double growPoint = growPoints.get(rect);
            boolean moveRect = growPoint != null;
            if (!moveRect) {
                growPoint = rect.getCenter();
            }
            if (!GeometryHelper.contains(left, top, right, bottom, growPoint, 0)) {
                GeometryHelper.movePointInside(growPoint, left, top, right, bottom, 0);
            }
            if (!moveRect) {
                growPoints.put(rect, growPoint);
                newCenters.put(rect, growPoint);
            }
        }

        @Override
        void perform() {
            if (prevRect instanceof Diagram) {
                diagram.updateBounds();
            }

            LinkedHashSet<AbstractContainer> toMove = new LinkedHashSet<>(elements);
            toMove.addAll(newCenters.keySet());
            descendantsToMove.put(prevRect, toMove);
            for (AbstractContainer element : toMove) {
                AbstractContainer predecessor = (AbstractContainer) element.getOwner();
                while (predecessor != prevRect) {
                    LinkedHashSet<AbstractContainer> currToMove = descendantsToMove.get(predecessor);
                    if (currToMove == null) {
                        currToMove = new LinkedHashSet<>();
                        currToMove.add(element);
                        descendantsToMove.put(predecessor, currToMove);
                    } else {
                        currToMove.add(element);
                    }
                    predecessor = (AbstractContainer) predecessor.getOwner();
                }
            }

            arrangeNestedGrid(prevRect);

            ArrayList<AbstractContainer> rectangles = new ArrayList<>(newCenters.keySet());

            /*
             * Gets all lines to retrace from the boxes that were moved (also in grid border
             * arrangement step).
             */
            LinkedHashSet<Line> linesToRetrace = new LinkedHashSet<>();
            for (AbstractContainer rect : rectangles) {
                if (rect instanceof Box) {
                    Box box = (Box) rect;
                    for (Line line : box.getIncidentLines()) {
                        if (line.owner == null) {
                            ((Box) line.getStart()).incidentLines.remove(line);
                            ((Box) line.getEnd()).incidentLines.remove(line);
                        }
                    }
                    linesToRetrace.addAll(box.incidentLines);
                    for (Box descendantBox : box.getDescendantBoxes()) {
                        for (Line line : descendantBox.getIncidentLines()) {
                            if (line.owner == null) {
                                ((Box) line.getStart()).incidentLines.remove(line);
                                ((Box) line.getEnd()).incidentLines.remove(line);
                            }
                        }
                        linesToRetrace.addAll(descendantBox.incidentLines);
                    }
                }
            }

            

            LinkedHashMap<Box, Point2D.Double> oldCenters = new LinkedHashMap<>();
            for (Line line : linesToRetrace) {
                if (line.getStart() instanceof Box && line.getEnd() instanceof Box) {
                    Box start = (Box) line.getStart();
                    Box end = (Box) line.getEnd();
                    oldCenters.put(start, start.getCenter());
                    oldCenters.put(end, end.getCenter());
                }
            }

            for (Line line : linesToRetrace) {
                line.getOwner().removeChild(line);
            }

            for (Line line : diagram.getLines()) 
            	if (!line.hasGeometry())
            		throw new RuntimeException("geom "+line);

            /*
             * Finally places all the rectangles that have to be moved.
             */
            Adjuster.placeRectangles(rectangles,
                    new ArrayList<>(newCenters.values()),
                    new ArrayList<>(growPoints.values()),
                    prevRect);

            diagram.updateBounds();

            /**
             * Line adjusting.
             */
            diagram.transaction = null;
            State oldState = diagram.state;
            diagram.startTransaction();
            double epsilon = diagram.getEpsilon();
            Point2D.Double O = new Point2D.Double(0, 0);
            for (Line line : linesToRetrace) {
                line.getOwner().addLine(line);

                if (line.getStart() instanceof Box && line.getEnd() instanceof Box) {
                    Box start = (Box) line.getStart(), end = (Box) line.getEnd();

                    Point2D.Double startMove = new Point2D.Double(start.getCenterX() - oldCenters.get(start).x, start.getCenterY() - oldCenters.get(start).y);
                    Point2D.Double endMove = new Point2D.Double(end.getCenterX() - oldCenters.get(end).x, end.getCenterY() - oldCenters.get(end).y);
                    LineType type = line.getType();
                    if (Math.abs(startMove.x - endMove.x) < epsilon && Math.abs(startMove.y - endMove.y) < epsilon) {
                        for (Point2D.Double point : line.lineGeometry.points) {
                            point.x += startMove.x;
                            point.y += startMove.y;
                        }
                        if (type != LineType.ORTHOGONAL) {
                            for (LineLabel label : line.labels) {
                                label.transpose(startMove);
                            }
                        }
                        line.setPoints(line.getPoints());
                    } else {
                        if (Math.max(startMove.distance(O), endMove.distance(O)) > diagram.getRetraceDistance()
                                && (!GeometryHelper.boxIntersectsLine(start, line, true)
                                || !GeometryHelper.boxIntersectsLine(end, line, true))) {
                            line.retrace();
                        } else if (type == LineType.ORTHOGONAL) {
                            line.setPoints(line.getPoints());
                        } else {
                            Point2D.Double startPoint = line.getStartPoint();
                            Point2D.Double endPoint = line.getEndPoint();
                            startPoint.x += startMove.x;
                            startPoint.y += startMove.y;
                            endPoint.x += endMove.x;
                            endPoint.y += endMove.y;
                            LineOptimizer.cutLine(line);
                            LineOptimizer.adjustPolylineLabelPlacement(line);
                        }
                    }
                }
            }
            diagram.endTransaction();

            ArrayList<Line> straightLines = new ArrayList<>();
            for (Line line : linesToRetrace) {
                if ((line.getType() == LineType.STRAIGHT || line.getType() == LineType.POLYLINE) && line.lineGeometry.points.size() == 2) {
                    straightLines.add(line);
                }
            }
            LineOptimizer.arrangeStraightLineEnds(straightLines);

            diagram.state = oldState;

            Adjuster.growOutsideLabels(diagram);

            Adjuster.updateBoxCurrentMinSize(diagram);
            }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="ModifyLabelTransaction">
    /**
     * This transaction handles the outside label inside label modification operations.
     */
    static class ModifyLabelTransaction extends Transaction {

        /**
         * The outside labels whose inside labels to modify.
         */
        HashSet<OutsideLabel> labels;

        /**
         * Creates a new label modification transaction.
         *
         * @param diagram the diagram to perform the transaction in
         */
        public ModifyLabelTransaction(Diagram diagram) {
            super(diagram);
            labels = new HashSet<>();
        }

        @Override
        void addOperation(Operation operation) {
            assert operation instanceof ModifyLabelOperation;
            labels.add(((ModifyLabelOperation) operation).label);
        }

        @Override
        void perform() {
            diagram.updateEpsilon();
            for (OutsideLabel label : labels) {
                Normalizer.arrange(label);
            }
            for (Label label : labels) {
                if (label instanceof LineLabel) {
                    LineLabel lineLabel = (LineLabel) label;
                    if (lineLabel.getOwner().getType() != LineType.ORTHOGONAL) {
                        lineLabel.resetBounds();
                    }
                }
            }
            Adjuster.growOutsideLabels(diagram);
            Adjuster.updateBoxCurrentMinSize(diagram);
        }
    }
    //</editor-fold>
}
