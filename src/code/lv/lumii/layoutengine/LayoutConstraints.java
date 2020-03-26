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
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * This class defines the interface of the layout managers that arrange diagram elements according
 * to the layout descriptions of the diagram and its containers.
 *
 * @author Evgeny
 */
public abstract class LayoutConstraints {

    /**
     * The possible layout constraint types.
     */
    public static enum ConstraintType {

        /**
         * No additional constraints on the layout of the diagram are placed.
         */
        NONE,
        /**
         * The grid type lays out the diagrams elements in a grid.
         */
        GRID
    }
    /**
     * The container these constraints belong to.
     */
    AbstractContainer container;

    /**
     * Transposes any positional elements of these constraints by the given vector.
     *
     * @param moveVector the vector to transpose the constraints by
     */
    abstract void transpose(Point2D.Double moveVector);

    /**
     * A line storing a grid line for a set of constraints, along with its minimum distance to the
     * next line.
     */
    static class ConstraintLine {

        /**
         * The coordinate of the grid line.
         */
        double pos;
        /**
         * The line's minimum distance to the next line.
         */
        double distance;
        /**
         * The spacing of the line (in both directions).
         */
        double spacing;

        /**
         * Creates a new grid line.
         *
         * @param pos the line's coordinate
         * @param distance the line's minimum distance to the next line.
         * @param spacing the minimum width of the free space on either side of this line
         */
        public ConstraintLine(double pos, double distance, double spacing) {
            this.pos = pos;
            this.distance = distance;
            this.spacing = spacing;
        }

        /**
         * Creates a new constraint line from the given XML element corresponding to one.
         *
         * @param c an XML element corresponding to a constraint line
         */
        ConstraintLine(org.w3c.dom.Element c) {
            pos = Double.valueOf(c.getAttribute("pos"));
            distance = Double.valueOf(c.getAttribute("distance"));
            spacing = Double.valueOf(c.getAttribute("spacing"));
        }

        /**
         * Saves this constraint line to an XML element.
         *
         * @param doc the XML document in which to create the new element
         * @param idMap a map from diagram objects to their storage IDs
         * @return a new XML element corresponding to this constraint line
         */
        org.w3c.dom.Element saveToXML(Document doc, LinkedHashMap<Object, Integer> idMap) {
            org.w3c.dom.Element e = doc.createElement(XMLHelper.NAMESPACE + ":constraintLine");
            e.setAttribute("pos", Double.toString(pos));
            e.setAttribute("distance", Double.toString(distance));
            e.setAttribute("spacing", Double.toString(spacing));
            return e;
        }
    }

    /**
     * Creates a new instance of layout constraints.
     *
     * @param container the container the new constraints will belong to.
     */
    LayoutConstraints(AbstractContainer container) {
        this.container = container;
    }

    /**
     * Returns this layout's type.
     *
     * @return this layout's type.
     */
    public abstract ConstraintType getType();

    /**
     * Saves the attributes of these Layout Constraints to the given XML element. Uses {@code idMap}
     * to store element constraints.
     *
     * @param e an XML element corresponding to these Layout Constraints.
     * @param doc the XML document in which to create any children
     * @param idMap a map from diagram elements to element ID's used for storage.
     */
    abstract void saveAttributes(org.w3c.dom.Element e, org.w3c.dom.Document doc, LinkedHashMap<Object, Integer> idMap);

    /**
     * Creates a new XML element corresponding to these constraints in the given document.
     *
     * @param doc the XML document in which to create the new element
     * @param idMap a map from diagram objects to element storage IDs.
     * @return a new element corresponding to this diagram element
     */
    org.w3c.dom.Element saveToXML(org.w3c.dom.Document doc, LinkedHashMap<Object, Integer> idMap) {
        org.w3c.dom.Element e = doc.createElement(XMLHelper.NAMESPACE + ":layoutConstraints");
        e.setAttribute("type", getType().toString());
        saveAttributes(e, doc, idMap);
        return e;
    }

    /**
     * Recreates layout constraints from the given XML element corresponding to them. All the
     * children of {@code owner} must have been created and placed in {@code objectMap} already.
     *
     * @param e an XML element corresponding to layout constraints
     * @param objectMap a map from element storage IDs to diagram objects
     * @param owner the container these constraints should belong to
     * @return returns the new layout constraints
     */
    static LayoutConstraints loadFromXML(org.w3c.dom.Element e, LinkedHashMap<Integer, Object> objectMap, AbstractContainer owner) {
        ConstraintType type = ConstraintType.valueOf(e.getAttribute("type"));
        switch (type) {
            case NONE:
                return new NoneLayoutConstraints(owner);
            case GRID:
                return new GridLayoutConstraints(e, objectMap, owner);
        }
        return null;
    }

    //<editor-fold defaultstate="collapsed" desc="GridLayoutConstraints">
    /**
     * These constraints implement the grid borders in the container. The grid is ensured in both
     * global layout and diagram editing operations.
     *
     * @author karlis
     */
    public static class GridLayoutConstraints extends LayoutConstraints {

        /**
         * Consecutive horizontal row borders.
         */
        ArrayList<ConstraintLine> horizontalConstraints;
        /**
         * Consecutive vertical column borders.
         */
        ArrayList<ConstraintLine> verticalConstraints;
        /**
         * A map from elements to their columns, if any.
         */
        private final LinkedHashMap<Element, Integer> elementColumns;
        /**
         * A map from elements to their rows, if any.
         */
        private final LinkedHashMap<Element, Integer> elementRows;
        /**
         * Minimum column width.
         */
        private double defaultColumnMinWidth = 10;
        /**
         * Minimum row height.
         */
        private double defaultRowMinHeight = 10;
        /**
         * Default spacing for grid column borders.
         */
        private double defaultColumnSpacing = 0;
        /**
         * Default spacing for grid row borders.
         */
        private double defaultRowSpacing = 0;

        /**
         * Creates a new grid constraints object for the given container.
         *
         * @param container the container for which to create the grid constraints
         */
        GridLayoutConstraints(AbstractContainer container) {
            super(container);
            horizontalConstraints = new ArrayList<>();
            horizontalConstraints.add(new ConstraintLine(Double.NaN, defaultRowMinHeight, Double.NaN));
            verticalConstraints = new ArrayList<>();
            verticalConstraints.add(new ConstraintLine(Double.NaN, defaultColumnMinWidth, Double.NaN));
            elementColumns = new LinkedHashMap<>();
            elementRows = new LinkedHashMap<>();
        }

        /**
         * Checks whether the given element belongs to the owner of these constraints, throws an
         * exception if it doesn't.
         *
         * @param elem the element to check
         * @throws IllegalArgumentException if the element does not belong to the owner of these
         * constraints
         */
        void checkOwnership(Element elem) throws IllegalArgumentException {
            if (elem.getOwner() != container) {
                throw new IllegalArgumentException("The given element is not a child of the owner of these constraints.");
            }
        }

        /**
         * Returns the column number of the given element, or {@code null} if none. Column numbers
         * are 1-based.
         *
         * @param elem the element whose column to get.
         * @return the column number of the given element, or {@code null} if none.
         */
        public Integer getColumn(Element elem) {
            return elementColumns.get(elem);
        }

        /**
         * Returns the row number of the given element, or {@code null} if none. Row numbers are
         * 1-based.
         *
         * @param elem the element whose row number to get.
         * @return the row number of the given element, or {@code null} if none.
         */
        public Integer getRow(Element elem) {
            return elementRows.get(elem);
        }

        /**
         * Sets the column of the element. The element should be one of the elements contained in
         * the associated container. The action is carried out immediately or at the end of
         * transaction of the diagram. Column numbers are 1-based.
         *
         * @param elem the element whose column to set
         * @param column the new column number of the element, can be {@code null}
         */
        public void setColumn(Element elem, Integer column) {
            setCell(elem, getRow(elem), column);
        }

        /**
         * Sets the row of the element. The element should be one of the elements contained in the
         * associated container. The action is carried out immediately or at the end of transaction
         * of the diagram. Row numbers are 1-based.
         *
         * @param elem the element whose row to set
         * @param row the new row number of the element, can be {@code null}
         */
        public void setRow(Element elem, Integer row) {
            setCell(elem, row, getColumn(elem));
        }

        /**
         * Sets the cell of the element. The element should be one of the elements contained in the
         * associated container. The action is carried out immediately or at the end of transaction
         * of the diagram. Column and row numbers are 1-based.
         *
         * @param elem the element whose cell to set
         * @param row the new row of the element, can be {@code null}
         * @param column the new column of the element, can be {@code null}
         */
        public void setCell(Element elem, Integer row, Integer column) {
            assert row == null || row > 0;
            assert column == null || column > 0;
            if (elem instanceof AbstractContainer) {
                Diagram diagram = container.getDiagram();

                Transaction.Operation.PlaceRectangleOperation.prepareTransaction(diagram, container.getPrevRect(true));

                checkOwnership(elem);
                addColumnsTo(column);
                addRowsTo(row);
                if (row != null) {
                    elementRows.put(elem, row);
                } else {
                    elementRows.remove(elem);
                }
                if (column != null) {
                    elementColumns.put(elem, column);
                } else {
                    elementColumns.remove(elem);
                }

                elem.getDiagram().layoutSetCell(elem);
            } else {
                throw new UnsupportedOperationException("Setting cells for elements other than containers is not supported yet.");
            }
        }

        /**
         * Internally sets the cell of the element without performing normalization. The element
         * should be one of the elements contained in the associated container. Column and row
         * numbers are 1-based.
         *
         * @param elem the element whose cell to set
         * @param row the new row of the element, can be {@code null}
         * @param column the new column of the element, can be {@code null}
         */
        void _setCell(Element elem, Integer row, Integer column) {
            assert row == null || row > 0;
            assert column == null || column > 0;
            if (elem instanceof AbstractContainer) {
                addColumnsTo(column);
                addRowsTo(row);
                if (row != null) {
                    elementRows.put(elem, row);
                } else {
                    elementRows.remove(elem);
                }
                if (column != null) {
                    elementColumns.put(elem, column);
                } else {
                    elementColumns.remove(elem);
                }
            } else {
                throw new UnsupportedOperationException("Setting cells for elements other than Boxes and InsideLabels is not supported yet.");
            }
        }

        /**
         * Removes the given element from the grid. Nothing else is changed.
         *
         * @param element the element to remove from the grid
         */
        void _remove(Element element) {
            elementColumns.remove(element);
            elementRows.remove(element);
        }

        /**
         * If necessary, adds new column borders until there are {@code column} columns.
         *
         * @param column the number of columns to ensure
         */
        private void addColumnsTo(Integer column) {
            if (column != null) {
                AbstractContainer rect = container.getPrevRect(true);
                double prev = rect.right;
                for (int i = verticalConstraints.size(); i < column; i++) {
                    verticalConstraints.add(new ConstraintLine(prev, defaultColumnMinWidth, defaultColumnSpacing));
                }
            }
        }

        /**
         * If necessary, adds new row borders until there are {@code row} row.
         *
         * @param row the number of rows to ensure
         */
        private void addRowsTo(Integer row) {
            if (row != null) {
                AbstractContainer rect = container.getPrevRect(true);
                double prev = rect.bottom;
                for (int i = horizontalConstraints.size(); i < row; i++) {
                    horizontalConstraints.add(new ConstraintLine(prev, defaultRowMinHeight, defaultRowSpacing));
                }
            }
        }

        /**
         * Adds new columns to the right of the last column. The width of each new column will be
         * equal to the default column width.
         *
         * @param count the number of the columns to add
         */
        public void addColumns(int count) {
            Diagram diagram = container.getDiagram();
            Transaction.Operation.AdjustOperation.prepareTransaction(diagram);

            AbstractContainer rect = container.getPrevRect(true);
            double prev = rect.right;
            for (int i = 0; i < count; i++) {
                verticalConstraints.add(new ConstraintLine(prev, defaultColumnMinWidth, defaultColumnSpacing));
            }

            diagram.layoutAdjust();
        }

        /**
         * Adds new rows to the bottom of the last row. The height of each new row will be equal to
         * the default row height.
         *
         * @param count the number of the rows to add
         */
        public void addRows(int count) {
            Diagram diagram = container.getDiagram();
            Transaction.Operation.AdjustOperation.prepareTransaction(diagram);

            AbstractContainer rect = container.getPrevRect(true);
            double prev = rect.bottom;
            for (int i = 0; i < count; i++) {
                horizontalConstraints.add(new ConstraintLine(prev, defaultRowMinHeight, defaultRowSpacing));
            }

            diagram.layoutAdjust();
        }

        /**
         * Sets the new default column width used for new columns and when resetting column widths.
         * The width must be positive.
         *
         * @param width the new default column width
         */
        public void setDefaultColumnWidth(double width) {
            if (width <= 0) {
                throw new IllegalArgumentException("Column default width must be positive.");
            }
            defaultColumnMinWidth = width;
        }

        /**
         * Sets the new default row height used for new rows and when resetting row heights.
         *
         * @param height the new default row height
         */
        public void setDefaultRowHeight(double height) {
            if (height <= 0) {
                throw new IllegalArgumentException("Row default height must be positive.");
            }
            defaultRowMinHeight = height;
        }

        /**
         * Returns the default column width used for new columns and when resetting column widths.
         *
         * @return the default column width
         */
        public double getDefaultColumnWidth() {
            return defaultColumnMinWidth;
        }

        /**
         * Returns the default row height used for new rows and when resetting row heights.
         *
         * @return the default row height
         */
        public double getDefaultRowHeight() {
            return defaultRowMinHeight;
        }

        /**
         * Resets the minimum width of all columns to the current default column width.
         */
        public void resetColumnWidths() {
            Diagram diagram = container.getDiagram();
            Transaction.Operation.AdjustOperation.prepareTransaction(diagram);

            for (ConstraintLine constraint : verticalConstraints) {
                constraint.distance = defaultColumnMinWidth;
            }
            diagram.layoutAdjust();
        }

        /**
         * Resets the minimum height of all rows to the current default row height.
         */
        public void resetRowHeights() {
            Diagram diagram = container.getDiagram();
            Transaction.Operation.AdjustOperation.prepareTransaction(diagram);

            for (ConstraintLine constraint : horizontalConstraints) {
                constraint.distance = defaultRowMinHeight;
            }
            diagram.layoutAdjust();
        }

        /**
         * Sets the spacing of the right border of the given column.
         *
         * @param column the column whose right border's spacing to set.
         * @param spacing the new spacing of the border
         */
        public void setColumnBorderSpacing(int column, double spacing) {
            Diagram diagram = container.getDiagram();
            Transaction.Operation.AdjustOperation.prepareTransaction(diagram);

            if (column < 1 || getColumnCount() <= column) {
                throw new IllegalArgumentException("The index of the border should be between 1 and (column count)-1, inclusive.");
            }
            if (spacing < 0) {
                throw new IllegalArgumentException("Spacing should be non-negative.");
            }
            verticalConstraints.get(column).spacing = spacing;

            diagram.layoutAdjust();
        }

        /**
         * Sets the spacing of the bottom border of the given row.
         *
         * @param row the row whose bottom border's spacing to set.
         * @param spacing the new spacing of the border
         */
        public void setRowBorderSpacing(int row, double spacing) {
            Diagram diagram = container.getDiagram();
            Transaction.Operation.AdjustOperation.prepareTransaction(diagram);

            if (row < 1 || getRowCount() <= row) {
                throw new IllegalArgumentException("The index of the border should be between 1 and (row count)-1, inclusive.");
            }
            if (spacing < 0) {
                throw new IllegalArgumentException("Spacing should be non-negative.");
            }
            horizontalConstraints.get(row).spacing = spacing;

            diagram.layoutAdjust();
        }

        /**
         * Gets the spacing of the right border of the given column.
         *
         * @param column the column whose right border's spacing to get.
         * @return the spacing of the border
         */
        public double getColumnBorderSpacing(int column) {
            if (column < 1 || getColumnCount() <= column) {
                throw new IllegalArgumentException("The index of the border should be between 1 and (column count)-1, inclusive.");
            }
            return verticalConstraints.get(column).spacing;
        }

        /**
         * Gets the spacing of the bottom border of the given row.
         *
         * @param row the row whose bottom borders spacing to get.
         * @return the spacing of the border
         */
        public double getRowBorderSpacing(int row) {
            if (row < 1 || getRowCount() <= row) {
                throw new IllegalArgumentException("The index of the border should be between 1 and (row count)-1, inclusive.");
            }
            return horizontalConstraints.get(row).spacing;
        }

        /**
         * Sets the default spacing for column borders, used when creating new columns.
         *
         * @param spacing the new default column border spacing.
         */
        public void setDefaultColumnSpacing(double spacing) {
            if (spacing < 0) {
                throw new IllegalArgumentException("Spacing should be non-negative.");
            }
            defaultColumnSpacing = spacing;
        }

        /**
         * Sets the default spacing for row borders, used when creating new row.
         *
         * @param spacing the new default row border spacing.
         */
        public void setDefaultRowSpacing(double spacing) {
            if (spacing < 0) {
                throw new IllegalArgumentException("Spacing should be non-negative.");
            }
            defaultRowSpacing = spacing;
        }

        /**
         * Gets the default spacing for column borders, used when creating new columns.
         *
         * @return the default spacing for column borders
         */
        public double getDefaultColumnSpacing() {
            return defaultColumnSpacing;
        }

        /**
         * Gets the default spacing for row borders, used when creating new rows.
         *
         * @return the default spacing for row borders
         */
        public double getDefaultRowSpacing() {
            return defaultRowSpacing;
        }

        /**
         * Resets the spacing of every column border to the current default value.
         */
        public void resetColumnSpacings() {
            Diagram diagram = container.getDiagram();
            Transaction.Operation.AdjustOperation.prepareTransaction(diagram);

            for (int i = 1; i < getColumnCount(); i++) {
                verticalConstraints.get(i).spacing = defaultColumnSpacing;
            }

            diagram.layoutAdjust();
        }

        /**
         * Resets the spacing of every row border to the current default value.
         */
        public void resetRowSpacings() {
            Diagram diagram = container.getDiagram();
            Transaction.Operation.AdjustOperation.prepareTransaction(diagram);

            for (int i = 1; i < getRowCount(); i++) {
                horizontalConstraints.get(i).spacing = defaultRowSpacing;
            }

            diagram.layoutAdjust();
        }

        /**
         * Merges consecutive grid lanes from {@code first} to {@code last}, inclusive, into a
         * single lane by erasing intermediate grid constraint lines.
         *
         * @param elementLanes element lane indices
         * @param constraints grid constraint lines
         * @param first the index of the first lane to merge
         * @param last the index of the last lane to merge
         * @return the new grid constraint lines
         */
        private static ArrayList<ConstraintLine> mergeLanes(LinkedHashMap<Element, Integer> elementLanes, ArrayList<ConstraintLine> constraints, int first, int last) {
            int d = last - first;
            for (Map.Entry<Element, Integer> entry : elementLanes.entrySet()) {
                Integer lane = entry.getValue();
                if (lane != null && lane >= first) {
                    if (lane < last) {
                        entry.setValue(first);
                    } else {
                        entry.setValue(lane - d);
                    }
                }
            }

            ArrayList<ConstraintLine> newConstraints = new ArrayList<>();
            for (int i = 0; i < constraints.size(); i++) {
                if (i < first || i >= last) {
                    newConstraints.add(constraints.get(i));
                }
            }
            return newConstraints;
        }

        /**
         * Merges consecutive grid columns from {@code first} to {@code last}, inclusive, into a
         * single column by erasing intermediate grid constraint lines. The columns are renumbered
         * automatically.
         *
         * @param first the index of the first column to merge
         * @param last the index of the last column to merge
         */
        public void mergeColumns(int first, int last) {
            if (first > last) {
                throw new IllegalArgumentException("The first column index should not be greater than the last column index");
            }
            if (first < 1 || last > getColumnCount()) {
                throw new IllegalArgumentException("The indices of the first and last columns should be between 1 and column count, inclusive");
            }

            verticalConstraints = mergeLanes(elementColumns, verticalConstraints, first, last);
        }

        /**
         * Merges consecutive grid rows from {@code first} to {@code last}, inclusive, into a single
         * row by erasing intermediate grid constraint lines. The rows are renumbered automatically.
         *
         * @param first the index of the first row to merge
         * @param last the index of the last row to merge
         */
        public void mergeRows(int first, int last) {
            if (first > last) {
                throw new IllegalArgumentException("The first row index should not be greater than the last row index");
            }
            if (first < 1 || last > getRowCount()) {
                throw new IllegalArgumentException("The indices of the first and last rows should be between 1 and row count, inclusive");
            }

            horizontalConstraints = mergeLanes(elementRows, horizontalConstraints, first, last);
        }

        /**
         * Returns a list of elements belonging to the specified cell.
         *
         * @param row the row of the cell
         * @param column the column of the cell
         * @return a list of elements belonging to the specified cell
         */
        public ArrayList<Element> getCellElements(Integer row, Integer column) {
            ArrayList<Element> result = new ArrayList<>();
            for (Element elem : container.getChildren()) {
                if (getRow(elem).equals(row) && getColumn(elem).equals(column)) {
                    result.add(elem);
                }
            }
            return result;
        }

        /**
         * Returns a list of elements belonging to the specified row.
         *
         * @param row the row whose elements to get
         * @return a list of elements belonging to the specified row
         */
        public ArrayList<Element> getRowElements(Integer row) {
            ArrayList<Element> result = new ArrayList<>();
            for (Element elem : container.getChildren()) {
                if (getRow(elem).equals(row)) {
                    result.add(elem);
                }
            }
            return result;
        }

        /**
         * Returns a list of elements belonging to the specified column.
         *
         * @param column the column whose elements to get
         * @return a list of elements belonging to the specified column
         */
        public ArrayList<Element> getColumnElements(Integer column) {
            ArrayList<Element> result = new ArrayList<>();
            for (Element elem : container.getChildren()) {
                if (getColumn(elem).equals(column)) {
                    result.add(elem);
                }
            }
            return result;
        }

        /**
         * Returns the number of columns in this grid.
         *
         * @return the number of columns in this grid.
         */
        public int getColumnCount() {
            return verticalConstraints.size();
        }

        /**
         * Returns the number of rows in this grid.
         *
         * @return the number of rows in this grid.
         */
        public int getRowCount() {
            return horizontalConstraints.size();
        }

        /**
         * Gets the left coordinate of the column.
         *
         * @param column the column whose coordinate to get
         * @return the left coordinate of the column
         */
        public double getColumnLeft(int column) {
            assert column >= 1 && column <= getColumnCount();
            if (column == 1) {
                if (container instanceof Rectangular) {
                    return container.left;
                } else {
                    Container prevContainer = (Container) container;
                    Container prevOwner = prevContainer.getOwner();
                    while (true) {
                        if (prevOwner.layoutConstraints.getType() == ConstraintType.GRID) {
                            GridLayoutConstraints constraints = (GridLayoutConstraints) prevOwner.layoutConstraints;
                            Integer currColumn = constraints.getColumn(prevContainer);
                            if (currColumn != null) {
                                return constraints.getColumnLeft(currColumn);
                            }
                        }
                        if (prevOwner instanceof Box) {
                            return prevOwner.left;
                        }
                        prevContainer = prevOwner;
                        prevOwner = prevContainer.getOwner();
                    }
                }
            } else {
                return verticalConstraints.get(column - 1).pos;
            }
        }

        /**
         * Gets the right coordinate of the column.
         *
         * @param column the column whose coordinate to get
         * @return the right coordinate of the column
         */
        public double getColumnRight(int column) {
            assert column >= 1 && column <= getColumnCount();
            if (column == getColumnCount()) {
                if (container instanceof Rectangular) {
                    return container.right;
                } else {
                    Container prevContainer = (Container) container;
                    Container prevOwner = prevContainer.getOwner();
                    while (true) {
                        if (prevOwner.layoutConstraints.getType() == ConstraintType.GRID) {
                            GridLayoutConstraints constraints = (GridLayoutConstraints) prevOwner.layoutConstraints;
                            Integer currColumn = constraints.getColumn(prevContainer);
                            if (currColumn != null) {
                                return constraints.getColumnRight(currColumn);
                            }
                        }
                        if (prevOwner instanceof Box) {
                            return prevOwner.right;
                        }
                        prevContainer = prevOwner;
                        prevOwner = prevContainer.getOwner();
                    }
                }
            } else {
                return verticalConstraints.get(column).pos;
            }
        }

        /**
         * Gets the top coordinate of the row.
         *
         * @param row the row whose coordinate to get
         * @return the top coordinate of the row
         */
        public double getRowTop(int row) {
            assert row >= 1 && row <= getRowCount();
            if (row == 1) {
                if (container instanceof Rectangular) {
                    return container.top;
                } else {
                    Container prevContainer = (Container) container;
                    Container prevOwner = prevContainer.getOwner();
                    while (true) {
                        if (prevOwner.layoutConstraints.getType() == ConstraintType.GRID) {
                            GridLayoutConstraints constraints = (GridLayoutConstraints) prevOwner.layoutConstraints;
                            Integer currRow = constraints.getRow(prevContainer);
                            if (currRow != null) {
                                return constraints.getRowTop(currRow);
                            }
                        }
                        if (prevOwner instanceof Box) {
                            return prevOwner.top;
                        }
                        prevContainer = prevOwner;
                        prevOwner = prevContainer.getOwner();
                    }
                }
            } else {
                return horizontalConstraints.get(row - 1).pos;
            }
        }

        /**
         * Gets the bottom coordinate of the row.
         *
         * @param row the row whose coordinate to get
         * @return the bottom coordinate of the row
         */
        public double getRowBottom(int row) {
            assert row >= 1 && row <= getRowCount();
            if (row == getRowCount()) {
                if (container instanceof Rectangular) {
                    return container.bottom;
                } else {
                    Container prevContainer = (Container) container;
                    Container prevOwner = prevContainer.getOwner();
                    while (true) {
                        if (prevOwner.layoutConstraints.getType() == ConstraintType.GRID) {
                            GridLayoutConstraints constraints = (GridLayoutConstraints) prevOwner.layoutConstraints;
                            Integer currRow = constraints.getRow(prevContainer);
                            if (currRow != null) {
                                return constraints.getRowBottom(currRow);
                            }
                        }
                        if (prevOwner instanceof Box) {
                            return prevOwner.bottom;
                        }
                        prevContainer = prevOwner;
                        prevOwner = prevContainer.getOwner();
                    }
                }
            } else {
                return horizontalConstraints.get(row).pos;
            }
        }

        /**
         * Sets the left coordinate of the column.
         *
         * @param column the column to set left coordinate to
         * @param x the abscissa of the new column left
         */
        public void setColumnLeft(int column, double x) {
            assert column > 1 && column <= getColumnCount();

            Diagram diagram = container.getDiagram();
            Transaction.Operation.AdjustOperation.prepareTransaction(diagram);
            verticalConstraints.get(column - 1).pos = x;

            diagram.layoutAdjust();
        }

        /**
         * Sets the right coordinate of the column.
         *
         * @param column the column to set right coordinate to
         * @param x the abscissa of the new column right
         */
        public void setColumnRight(int column, double x) {
            assert column >= 1 && column < getColumnCount();
            setColumnLeft(column + 1, x);
        }

        /**
         * Sets the top coordinate of the row.
         *
         * @param row the row to set top coordinate to
         * @param y the ordinate of the new row top
         */
        public void setRowTop(int row, double y) {
            assert row > 1 && row <= getRowCount();

            Diagram diagram = container.getDiagram();
            Transaction.Operation.AdjustOperation.prepareTransaction(diagram);
            horizontalConstraints.get(row - 1).pos = y;

            diagram.layoutAdjust();
        }

        /**
         * Sets the bottom coordinate of the row.
         *
         * @param row the row to set bottom coordinate to
         * @param y the ordinate of the new row bottom
         */
        public void setRowBottom(int row, double y) {
            assert row >= 1 && row < getRowCount();
            setRowTop(row + 1, y);
        }

        /**
         * Sets the x coordinate of the left column border. This is an internal function and does
         * not call normalization.
         *
         * @param column the column whose left border to change
         * @param x the new abscissa of the border
         */
        void _setColumnLeft(int column, double x) {
            assert column > 1 && column <= getColumnCount();
            verticalConstraints.get(column - 1).pos = x;
        }

        /**
         * Sets the x coordinate of the right column border. This is an internal function and does
         * not call normalization.
         *
         * @param column the column whose right border to change
         * @param x the new abscissa of the border
         */
        public void _setColumnRight(int column, double x) {
            assert column > 0 && column < getColumnCount();
            verticalConstraints.get(column).pos = x;
        }

        /**
         * Sets the y coordinate of the top row border. This is an internal function and does not
         * call normalization.
         *
         * @param row the row whose top border to change
         * @param y the new ordinate of the border
         */
        void _setRowTop(int row, double y) {
            assert row > 1 && row <= getRowCount();
            horizontalConstraints.get(row - 1).pos = y;
        }

        /**
         * Sets the y coordinate of the bottom row border. This is an internal function and does not
         * call normalization.
         *
         * @param row the row whose bottom border to change
         * @param y the new ordinate of the border
         */
        public void _setRowBottom(int row, double y) {
            assert row > 0 && row < getRowCount();
            horizontalConstraints.get(row).pos = y;
        }

        /**
         * Sets the minimum width of the column. The action is carried out immediately or at the end
         * of transaction of the diagram. The minimum width must be positive.
         *
         * @param column the column whose minimum width to set
         * @param width the new minimum width of the column
         */
        public void setColumnMinWidth(int column, double width) {
            if (width <= 0) {
                throw new IllegalArgumentException("Column width must be positive.");
            }
            Diagram diagram = container.getDiagram();
            Transaction.Operation.AdjustOperation.prepareTransaction(diagram);
            addColumnsTo(column);
            verticalConstraints.get(column - 1).distance = width;

            diagram.layoutAdjust();
        }

        /**
         * Sets the minimum height of the row. The action is carried out immediately or at the end
         * of transaction of the diagram. The minimum height must be positive.
         *
         * @param row the row whose height to set
         * @param height the new minimum height of the row
         */
        public void setRowMinHeight(int row, double height) {
            if (height <= 0) {
                throw new IllegalArgumentException("Row height must be positive.");
            }
            Diagram diagram = container.getDiagram();
            Transaction.Operation.AdjustOperation.prepareTransaction(diagram);
            addRowsTo(row);
            horizontalConstraints.get(row - 1).distance = height;

            diagram.layoutAdjust();
        }

        /**
         * Gets the minimum width of the given column.
         *
         * @param column the column whose minimum width to get
         * @return the minimum width of the given column.
         */
        public double getColumnMinWidth(int column) {
            return verticalConstraints.get(column - 1).distance;
        }

        /**
         * Gets the minimum height of the given row.
         *
         * @param row the row whose minimum height to get
         * @return the minimum height of the given row.
         */
        public double getRowMinHeight(int row) {
            return horizontalConstraints.get(row - 1).distance;
        }

        /**
         * Gets the current height of the given row.
         *
         * @param row the row whose height to get
         * @return the current height of the given row.
         */
        public double getRowHeight(int row) {
            return getRowBottom(row) - getRowTop(row);
        }

        /**
         * Returns the current width of the given column.
         *
         * @param column the column whose width to get
         * @return the current width of the given column
         */
        public double getColumnWidth(int column) {
            return getColumnRight(column) - getColumnLeft(column);
        }

        @Override
        public ConstraintType getType() {
            return ConstraintType.GRID;
        }

        @Override
        void saveAttributes(org.w3c.dom.Element e, org.w3c.dom.Document doc, LinkedHashMap<Object, Integer> idMap) {
            e.setAttribute("defaultWidth", Double.toString(defaultColumnMinWidth));
            e.setAttribute("defaultHeight", Double.toString(defaultRowMinHeight));
            e.setAttribute("defaultColumnSpacing", Double.toString(defaultColumnSpacing));
            e.setAttribute("defaultRowSpacing", Double.toString(defaultRowSpacing));

            org.w3c.dom.Element horizontalLinesElement = doc.createElement(XMLHelper.NAMESPACE + ":horizontalLines");
            for (ConstraintLine line : horizontalConstraints) {
                horizontalLinesElement.appendChild(line.saveToXML(doc, idMap));
            }

            org.w3c.dom.Element verticalLinesElement = doc.createElement(XMLHelper.NAMESPACE + ":verticalLines");
            for (ConstraintLine line : verticalConstraints) {
                verticalLinesElement.appendChild(line.saveToXML(doc, idMap));
            }

            e.appendChild(horizontalLinesElement);
            e.appendChild(verticalLinesElement);

            for (Element child : container.getAbstractContainers(false)) {
                org.w3c.dom.Element elementCell = doc.createElement(XMLHelper.NAMESPACE + ":element");
                e.appendChild(elementCell);
                elementCell.setAttribute("element", idMap.get(child).toString());
                Integer row = getRow(child);
                Integer column = getColumn(child);
                if (row != null) {
                    elementCell.setAttribute("row", row.toString());
                }
                if (column != null) {
                    elementCell.setAttribute("column", column.toString());
                }
            }
        }

        //<editor-fold defaultstate="collapsed" desc="Grid arrangind">
        /**
         * A class describing position and lane number of one grid element. A lane is either a row
         * or a column.
         */
        static class LaneElement implements Comparable<LaneElement> {

            /**
             * The position of the element. It is x coordinate if the lane is a column and y
             * coordinate if the lane is a row.
             */
            double pos;
            /**
             * The number of the lane the element belongs to.
             */
            int lane;
            /**
             * Whether not to move the element from its position.
             */
            boolean constant;
            /**
             * The left bound of this element. Used for constant elements to limit border placement.
             */
            double left;
            /**
             * The right bound of this element. Used for constant elements to limit border
             * placement.
             */
            double right;

            /**
             * Creates a new {@code LaneElement}.
             *
             * @param pos the position of the element
             * @param lane the lane number to which this element belongs to.
             * @param constant whether this element should remain in its position
             * @param left the left bound of this element
             * @param right the right bound of this element
             */
            public LaneElement(double pos, int lane, boolean constant, double left, double right) {
                this.pos = pos;
                this.lane = lane;
                this.constant = constant;
                this.left = left;
                this.right = right;
            }

            /**
             * Compares two lane elements by their position first and by their lane second.
             *
             * @param x the {@code LaneElement} to compare to
             * @return a value less than 0, equal to 0 or greater than 0, if this element is
             * accordingly less, equal or greater than the given.
             */
            @Override
            public int compareTo(LaneElement x) {
                int cmp = Double.compare(pos, x.pos);
                return cmp == 0 ? Integer.compare(lane, x.lane) : cmp;
            }
        }

        /**
         * This method places one dimension grid lane borders in such a way that the number of the
         * elements to move inside their lanes is the minimum possible. If there is more than one
         * way to do that, chooses a way that minimizes the number of borders replaced. Does this
         * using dynamic programming.
         *
         * @param elements the lane elements in the grid
         * @param arrangeColumns whether to arrange row or column borders
         * @param startBound the mostleft bound of the grid
         * @param endBound the rightmost bound of the grid
         * @return the new grid line border positions
         */
        ArrayList<Double> arrangeGridBorders(
                ArrayList<LaneElement> elements,
                boolean arrangeColumns,
                double startBound, double endBound) {

            ArrayList<LayoutConstraints.ConstraintLine> borders = arrangeColumns ? verticalConstraints : horizontalConstraints;

            /*
             * The potential positions where to place grid lane borders are only between the consequtive
             * element positions. Thus we need to sort the elements; later the indices of the element
             * list will serve as indices of the grid lane placement positions.
             */
            Collections.sort(elements);
            int elementCount = elements.size();
            int borderCount = borders.size();

            /*
             * There are {@code borderCount} borders numbered from 1 to {@code borderCount}. There are
             * also {@code elementCount}+1 border placement positions (between elements). If the i-th
             * border is between (k-1)-th and k-th element, then borderPlace[i] is equal to k.
             */
            int[] borderPlace = new int[borderCount + 1];
            for (int elemPtr = 0, borderPtr = 1; borderPtr < borderCount; borderPtr++) {
                while (elemPtr < elementCount && borders.get(borderPtr).pos > elements.get(elemPtr).pos) {
                    elemPtr++;
                }
                borderPlace[borderPtr] = elemPtr;
            }
            borderPlace[borderCount] = elementCount;

            //<editor-fold defaultstate="collapsed" desc="slower version, but also minimizes moved constraint lines">
            //            /*
            //             * For i-th border, it is allowed to put it from minLeft[i] to maxRight[i] placement
            //             * positions, inclusive. These values are determined from the elements that must remain
            //             * in their positions that bound the placement of the border from both sides.
            //             */
            //            int[] minLeft = new int[borderCount + 1], maxRight = new int[borderCount + 1];
            //            for (int i = 1; i <= borderCount; i++) {
            //                minLeft[i] = 0;
            //                maxRight[i] = elementCount;
            //            }
            //            for (int i = 0; i < elementCount; i++) {
            //                if (elements.get(i).constant) {
            //                    int lane = elements.get(i).lane;
            //                    if (maxRight[lane - 1] == elementCount) {
            //                        maxRight[lane - 1] = i;
            //                    }
            //                    minLeft[lane] = i + 1;
            //                }
            //            }
            //
            //            /*
            //             * dpElements[i][k] -- the minimum number of elements to be moved, if we place i-th
            //             * border in k-th place.
            //             *
            //             * dpBorders[i][k] -- the minimum number of borders to be moved if we place i-th border
            //             * in k-th position, and dpElements[i][k] elements are moved
            //             *
            //             * dpPrevious[i][k] -- the place where to put (i-1)-th border to achieve minimum state,
            //             * if we put i-th border in k-th place. Used to reconstruct the border placement.
            //             */
            //            int[][] dpElements = new int[borderCount + 1][elementCount + 1],
            //                    dpBorders = new int[borderCount + 1][elementCount + 1],
            //                    dpPrevious = new int[borderCount + 1][elementCount + 1];
            //            for (int border = 1; border <= borderCount; border++) {
            //                /*
            //                 * Calculates the partial sum array: lineElementCount[i] is the number of elements
            //                 * of border-th lane to the left of i-th element.
            //                 */
            //                int[] lineElementCount = new int[elementCount + 1];
            //                for (int i = 1; i <= elementCount; i++) {
            //                    lineElementCount[i] = lineElementCount[i - 1] + (elements.get(i - 1).lane == border ? 1 : 0);
            //                }
            //
            //                /*
            //                 * dpElements[border-1][i] is added the number of border-th elements that are to the
            //                 * left of i-th place. These elements must be moved to the right of i-th place to be
            //                 * inside border-th lane. Then updates dpElements[border-1][i] to the minimum of the
            //                 * prefix, and remembers the best place on this prefix for (border-1)-th border in
            //                 * dpMinimum[i].
            //                 */
            //                int[] dpMinimum = new int[elementCount + 1];
            //                for (int i = 1; i <= elementCount; i++) {
            //                    dpElements[border - 1][i] += lineElementCount[i];
            //                    dpMinimum[i] = i;
            //                    int j = dpMinimum[i - 1];
            //                    if (dpElements[border - 1][i] > dpElements[border - 1][j]
            //                            || (dpElements[border - 1][i] == dpElements[border - 1][j]
            //                            && dpBorders[border - 1][i] > dpBorders[border - 1][j])) {
            //                        dpMinimum[i] = j;
            //                    }
            //                }
            //
            //                /*
            //                 * Actually calculates the dp values. If we place border-th border in i-th place,
            //                 * then the best state is: number of border-th lane's elements that are to the right
            //                 * of i-th place plus the number of elements of the best state where to put
            //                 * (border-1)-th border.
            //                 */
            //                for (int i = 0; i <= elementCount; i++) {
            //                    dpElements[border][i] = elementCount + 1;
            //                }
            //                for (int i = minLeft[border]; i <= maxRight[border]; i++) {
            //                    int j = dpMinimum[i];
            //                    dpElements[border][i] = lineElementCount[elementCount] - lineElementCount[i]
            //                            + dpElements[border - 1][j];
            //                    dpBorders[border][i] = (i == borderPlace[border] ? 0 : 1) + dpBorders[border - 1][j];
            //                    dpPrevious[border][i] = j;
            //                }
            //            }
            //
            //            /*
            //             * Reconstructs the resulting border positions using the dpPrevious array. But first
            //             * finds the best state for the last border from which it starts the reconstruction.
            //             */
            //            int[] result = new int[borderCount];
            //            int p = elementCount;
            //            for (int border = borderCount - 1; border > 0; border--) {
            //                p = result[border] = dpPrevious[border + 1][p];
            //            }
            //</editor-fold>
            //<editor-fold desc="fast version">
            /*
             * Finds the longest non-decreasing lane element subsequence.
             */
            int[] dp = new int[elementCount + 1];
            int[] index = new int[elementCount + 1];
            int[] parent = new int[elementCount];
            Arrays.fill(dp, Integer.MAX_VALUE);
            Arrays.fill(parent, -1);
            dp[0] = 0;
            index[0] = -1;
            int start = 1;
            for (int i = 0; i < elementCount; i++) {
                int value = elements.get(i).lane;
                int left = start, right = i + 1;
                while (left < right) {
                    int mid = (left + right) / 2;
                    if (value < dp[mid]) {
                        right = mid;
                    } else {
                        left = mid + 1;
                    }
                }
                if (dp[left - 1] <= value && value < dp[left]) {
                    dp[left] = value;
                    index[left] = i;
                    parent[i] = index[left - 1];
                    if (elements.get(i).constant) {
                        start = left + 1;
                    }
                }
            }

            int[] result = new int[borderCount];
            int currBorder = borderCount - 1;
            int k = -1;
            for (int i = elementCount; i >= 0; i--) {
                if (dp[i] < Integer.MAX_VALUE) {
                    k = index[i];
                    break;
                }
            }

            while (k >= 0) {
                int value = elements.get(k).lane;
                while (value <= currBorder) {
                    result[currBorder--] = k + 1;
                }
                k = parent[k];
            }
            //</editor-fold>

            /*
             * Decides the positions (coordinates) for the borders. If the border was not moved, it
             * remains in its previous position. Sets the startBound or endBound if the place is
             * correspondingly the firs tor the last.
             */
            ArrayList<Double> positions = new ArrayList<>();
            for (int i = 0; i < borderCount; i++) {
                int id = result[i];
                positions.add(id == borderPlace[i]
                        ? (Math.min(endBound, Math.max(borders.get(i).pos, startBound)))
                        : id == 0 ? startBound
                        : id == elementCount ? endBound
                        : (elements.get(id - 1).pos + elements.get(id).pos) / 2);
            }

            /**
             * Updates lane borders so that rectangles on constant positions are strictly inside
             * their lanes.
             */
            double[] minLeft = new double[borderCount + 1];
            double[] maxRight = new double[borderCount + 1];
            for (int i = 0; i < borderCount; i++) {
                minLeft[i] = Double.NEGATIVE_INFINITY;
                maxRight[i] = Double.POSITIVE_INFINITY;
            }
            for (LaneElement element : elements) {
                if (element.constant) {
                    int lane = element.lane;
                    minLeft[lane] = Math.max(minLeft[lane], element.right);
                    maxRight[lane - 1] = Math.min(maxRight[lane - 1], element.left);
                }
            }
            for (int i = 1; i < borderCount; i++) {
                minLeft[i] = Math.max(minLeft[i - 1], minLeft[i]);
            }
            for (int i = borderCount - 2; i >= 0; i--) {
                maxRight[i] = Math.min(maxRight[i + 1], maxRight[i]);
            }
            for (int i = 0; i < borderCount; i++) {
                if (positions.get(i) < minLeft[i]) {
                    positions.set(i, minLeft[i]);
                } else if (maxRight[i] < positions.get(i)) {
                    positions.set(i, maxRight[i]);
                }
            }

            return positions;
        }
        //</editor-fold>

        /**
         * Recreates grid layout constraints from the given XML element corresponding to them. All
         * the children of {@code owner} must have been created and placed in {@code objectMap}
         * already.
         *
         * @param e an XML element corresponding to grid layout constraints
         * @param objectMap a map from element storage IDs to diagram objects
         * @param owner the container these constraints should belong to
         */
        GridLayoutConstraints(org.w3c.dom.Element e, LinkedHashMap<Integer, Object> objectMap, AbstractContainer owner) {
            super(owner);
            defaultColumnMinWidth = Double.valueOf(e.getAttribute("defaultWidth"));
            defaultRowMinHeight = Double.valueOf(e.getAttribute("defaultHeight"));
            defaultColumnSpacing = Double.valueOf(e.getAttribute("defaultColumnSpacing"));
            defaultRowSpacing = Double.valueOf(e.getAttribute("defaultRowSpacing"));

            horizontalConstraints = new ArrayList<>();
            verticalConstraints = new ArrayList<>();

            org.w3c.dom.Element horizontalLinesElement
                    = (org.w3c.dom.Element) e.getElementsByTagName(XMLHelper.NAMESPACE + ":horizontalLines").item(0);

            org.w3c.dom.Element verticalLinesElement
                    = (org.w3c.dom.Element) e.getElementsByTagName(XMLHelper.NAMESPACE + ":verticalLines").item(0);

            NodeList horizontalLinesChildren = horizontalLinesElement.getChildNodes();
            for (int i = 0; i < horizontalLinesChildren.getLength(); i++) {
                Node child = horizontalLinesChildren.item(i);
                if (child instanceof org.w3c.dom.Element) {
                    org.w3c.dom.Element c = (org.w3c.dom.Element) child;
                    if (c.getTagName().equals(XMLHelper.NAMESPACE + ":constraintLine")) {
                        horizontalConstraints.add(new ConstraintLine(c));
                    }
                }
            }

            NodeList verticalLinesChildren = verticalLinesElement.getChildNodes();
            for (int i = 0; i < verticalLinesChildren.getLength(); i++) {
                Node child = verticalLinesChildren.item(i);
                if (child instanceof org.w3c.dom.Element) {
                    org.w3c.dom.Element c = (org.w3c.dom.Element) child;
                    if (c.getTagName().equals(XMLHelper.NAMESPACE + ":constraintLine")) {
                        verticalConstraints.add(new ConstraintLine(c));
                    }
                }
            }

            elementRows = new LinkedHashMap<>();
            elementColumns = new LinkedHashMap<>();

            NodeList elements = e.getElementsByTagName(XMLHelper.NAMESPACE + ":element");
            for (int i = 0; i < elements.getLength(); i++) {
                org.w3c.dom.Element elementCell = (org.w3c.dom.Element) elements.item(i);
                Element element = (Element) objectMap.get(Integer.parseInt(elementCell.getAttribute("element")));
                if (elementCell.hasAttribute("row")) {
                    elementRows.put(element, Integer.valueOf(elementCell.getAttribute("row")));
                }
                if (elementCell.hasAttribute("column")) {
                    elementColumns.put(element, Integer.valueOf(elementCell.getAttribute("column")));
                }
            }
        }

        /**
         * Transposes the grid by transposing every grid line.
         *
         * @param moveVector the vector to move the grid by
         */
        @Override
        void transpose(Point2D.Double moveVector) {
            for (ConstraintLine line : horizontalConstraints) {
                line.pos += moveVector.y;
            }
            for (ConstraintLine line : verticalConstraints) {
                line.pos += moveVector.x;
            }
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="NoneLayoutConstraints">
    /**
     * These constraints do not constrain anything.
     */
    public static class NoneLayoutConstraints extends LayoutConstraints {

        /**
         * Creates a new lack of layout constraints.
         *
         * @param container the container without any constraints
         */
        NoneLayoutConstraints(AbstractContainer container) {
            super(container);
        }

        @Override
        public ConstraintType getType() {
            return ConstraintType.NONE;
        }

        @Override
        void saveAttributes(org.w3c.dom.Element e, org.w3c.dom.Document doc, LinkedHashMap<Object, Integer> idMap) {
        }

        /**
         * No constraints to transpose.
         *
         * @param moveVector the vector to transpose the constraints by
         */
        @Override
        void transpose(Point2D.Double moveVector) {
        }
    }
    //</editor-fold>
}
