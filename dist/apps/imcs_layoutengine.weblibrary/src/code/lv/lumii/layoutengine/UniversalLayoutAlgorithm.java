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
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import lv.lumii.layoutengine.LayoutConstraints.GridLayoutConstraints;
import lv.lumii.layoutengine.funcmin.QuadraticOptimizer;
import lv.lumii.layoutengine.obstacleGraph.ObstacleGraph;
import lv.lumii.layoutengine.util.Pair;
import lv.lumii.layoutengine.universalLayout.BoxInGrid;
import lv.lumii.layoutengine.universalLayout.Line;
import lv.lumii.layoutengine.universalLayout.SimpleOrthogonalSegment;

/**
 * Class for arranging graph universally. Algorithm reduces edge length and
 * vertices are arranged compactly and orthogonally.
 *
 * @author Jan
 */
class UniversalLayoutAlgorithm extends ComponentLayout {
    
    /**
     * Determines if boxes with only one adjacent line are treated differently in new place search.
     */
    final boolean oneLineSpecialCase=true;
    
    /**
     * This is only for testing random layout. True if start with a random layout.
     */
    final boolean startRandom=false;
    /**
     * This is only for testing BFS layout. True if start with BFS layout.
     */
    final boolean startBFS=false;

    /**
     * Array with all boxes.
     */
    BoxInGrid[] boxes;
    /**
     * Map from Container element to BoxInGrid element that represents the same box.
     */
    LinkedHashMap<AbstractContainer, BoxInGrid> map;
    /**
     * Used to get information about layout constraints.
     */
    GridLayoutConstraints constr;
    /**
     * True if LayoutConstraint type is GRID.
     */
    boolean gridExist;
    /**
     * Coordinates of rows and columns.
     */
    int[] rows, columns;
    /**
     * Coordinates of rows and columns in real numbers.
     */
    double[] rowsd, columnsd;
    /**
     * The bigger is SLIDE_COEF the less boxes will try to be in one horizontal or vertical line.
     */
    final int SLIDE_COEF = 20;

    /**
     * Grid that contains links to BoxInGrid if cell is taken or null if cell is empty.
     */
    BoxInGrid[][] grid;
    /**
     * Grid that contains number of horizontal and vertical lines going through cell.
     */
    //int[][] lineGrid;
    /**
     * Size of one cell height and width in grid array.
     */
    int gridSize;
    /**
     * Sizes of grid and lineGrid arrays.
     */
    int horSize, vertSize;
    
    //next are varianbles for improveBoxPlaces and their functions
    /**
     * Defines how much to reduce crossings. Biggest crCoef means that more
     * crossings will be reduced, but other criteria will be less important.
     */
    final double crCoef = 0.5;
    /**
     * Number of cells taken by box.
     */
    int cellCount;
    /**
     * Best coordinates for box found so far.
     */
    int bestX, bestY;
    /**
     * Variable for creating all random numbers.
     */
    Random random;
    /**
     * Defines cost of best place found so far.
     * It includes sum of line length, random value and crossings.
     */
    double bestPlaceCost;
    /**
     * Defines how important is random number in place cost calculation.
     */
    double randomCoef;
    /**
     * Defines cost of current place found so far.
     * It includes sum of line length, random value and crossings.
     */    
    double curPlaceCost;
    /**
     * True if free place for box was already found.
     */
    boolean placeFound;
    /**
     * Defines rectangle where a box can be placed. Does not take into account size of the box.
     */
    int leftBorder, rightBorder, topBorder, bottomBorder;



    /**
     * Moves boxes closer to each other by finding maximal box in each column
     * and row, and then moving all boxes in column or row.
     */
    void compress() {

        //All of this arrays are taking borders into account
        double[] maxColL = new double[horSize];//the most left box side in column
        double[] maxColR = new double[horSize];//the most right box side in column
        double[] maxRowT = new double[vertSize];//the most top box side in row
        double[] maxRowB = new double[vertSize];//the most bottom box side in row

        for (int i = 0; i < horSize; i++) {
            maxColL[i] = Double.POSITIVE_INFINITY;
            maxColR[i] = Double.NEGATIVE_INFINITY;
        }
        for (int i = 0; i < vertSize; i++) {
            maxRowT[i] = Double.POSITIVE_INFINITY;
            maxRowB[i] = Double.NEGATIVE_INFINITY;
        }



        for (BoxInGrid boxIG : boxes) {  //calculating sides of each column and row
            //int radiusH = boxIG.nH / 2;
            //int radiusV = boxIG.nV / 2;
            AbstractContainer box = boxIG.box;
            /*
             *if value in array is NaN, then it is not going to change,
             *because every comparator with NaN returns false
             */
            if (boxIG.xd - (box.right - box.left) / 2 < maxColL[boxIG.x]) {
                maxColL[boxIG.x] = boxIG.xd - (box.right - box.left) / 2;
            }
            if (boxIG.xd + (box.right - box.left) / 2 > maxColR[boxIG.x]) {
                maxColR[boxIG.x] = boxIG.xd + (box.right - box.left) / 2;
            }
            if (boxIG.yd - (box.bottom - box.top) / 2 < maxRowT[boxIG.y]) {
                maxRowT[boxIG.y] = boxIG.yd - (box.bottom - box.top) / 2;
            }
            if (boxIG.yd + (box.bottom - box.top) / 2 > maxRowB[boxIG.y]) {
                maxRowB[boxIG.y] = boxIG.yd + (box.bottom - box.top) / 2;
            }
            
            /*
             *if radiusH is more than 0, it means we can't compress any columns for whom belong boxIG.
             *So we assing NaN to them.
             */
            if (boxIG.nH > 1) {
                maxColR[boxIG.x] = Double.NaN;
                maxColL[boxIG.x + boxIG.nH - 1] = Double.NaN;
                for (int i = boxIG.x + 1; i <= boxIG.x + boxIG.nH - 2; i++) {
                    maxColR[i] = Double.NaN;
                    maxColL[i] = Double.NaN;
                }
            }

            //same as previous, but with rows
            if (boxIG.nV > 1) {
                maxRowB[boxIG.y] = Double.NaN;
                maxRowT[boxIG.y + boxIG.nV -1] = Double.NaN;
                for (int i = boxIG.y + 1; i <= boxIG.y + boxIG.nH - 2; i++) {
                    maxRowT[i] = Double.NaN;
                    maxRowB[i] = Double.NaN;
                }
            }

        }


        //now calculating each column new position
        double[] horChanges = new double[horSize];
        double[] vertChanges = new double[vertSize];

        horChanges[0] = 0;

        for (int i = 1; i < horSize; i++) {
            if (Double.isNaN(maxColL[i])) { //Means that next row must be moved exactly the same as previous
                horChanges[i] = horChanges[i - 1];
                maxColR[i] = maxColR[i] - horChanges[i];
            } else if (maxColL[i] == Double.POSITIVE_INFINITY) {  //Means that there are no boxes in this row
                maxColR[i] = maxColR[i - 1];    //here can be problems if algorithm will be changed
            } else {  //Means there is a box in i column, so column can be moved closer to i-1 column
                if (maxColR[i - 1] != Double.NEGATIVE_INFINITY) {
                    horChanges[i] = maxColL[i] - maxColR[i - 1];
                } else {
                    horChanges[i] = 0;
                }
                maxColR[i] = maxColR[i] - horChanges[i];
            }
        }

        //next is almost copy paste of previous for cycle
        vertChanges[0] = 0;
        for (int i = 1; i < vertSize; i++) {
            if (Double.isNaN(maxRowT[i])) {
                vertChanges[i] = vertChanges[i - 1];
                maxRowB[i] = maxRowB[i] - vertChanges[i];
            } else if (maxRowT[i] == Double.POSITIVE_INFINITY) {
                maxRowB[i] = maxRowB[i - 1];
            } else {
                if (maxRowB[i - 1] != Double.NEGATIVE_INFINITY) {
                    vertChanges[i] = maxRowT[i] - maxRowB[i - 1];
                } else {
                    vertChanges[i] = 0;
                }
                maxRowB[i] = maxRowB[i] - vertChanges[i];
            }
        }

        //here will be changing box coordinates according to horChanges and vertChanges
        for (BoxInGrid boxIG : boxes) {
            boxIG.xd -= horChanges[boxIG.x];
            boxIG.yd -= vertChanges[boxIG.y];
        }
    }
    
    /**
     * Returns rectangle that represents graph margins.
     * By graph are meant its vertices or rowsd and columnsd values.
     * @return array with such values in each index:
     * 0 - left border
     * 1 - top border
     * 2 - right border
     * 3 - bottom border
     */
    double[] getGraphEnclosingRect(){
                double minX = Double.POSITIVE_INFINITY,  //most left x coordinate that taken by box of Box object. 
                minY = Double.POSITIVE_INFINITY, //most top y coordinate that taken by box of Box object
                maxX = Double.NEGATIVE_INFINITY, //similar
                maxY = Double.NEGATIVE_INFINITY; //similar
        
        for (BoxInGrid boxIG : boxes) {
            AbstractContainer box = boxIG.box;
            
            //getting borders of the graph
            if (boxIG.xd - (box.right - box.left) / 2 < minX) {
                minX = boxIG.xd - (box.right - box.left) / 2;
            }
            if (boxIG.xd + (box.right - box.left) / 2 > maxX) {
                maxX = boxIG.xd + (box.right - box.left) / 2;
            }
            if (boxIG.yd - (box.bottom - box.top) / 2 < minY) {
                minY = boxIG.yd - (box.bottom - box.top) / 2;
            }
            if (boxIG.yd + (box.bottom - box.top) / 2 > maxY) {
                maxY = boxIG.yd + (box.bottom - box.top) / 2;
            }
            
//            System.out.println("Center x: "+(boxIG.xd - (box.right - box.left))+" center y: "+(boxIG.yd - (box.bottom - box.top)));
//            System.out.println("Size x: "+(box.right - box.left)+" size y: "+(box.bottom - box.top));
        }
        
        if(columnsd!=null){
            if(columnsd.length>0){
                if(columnsd[0]<minX){
                    minX=columnsd[0];
                }
                if(columnsd[columnsd.length-1]>maxX){
                    maxX=columnsd[columnsd.length-1];
                }
            }
        }
        if(rowsd!=null){
            if(rowsd.length>0){
                if(rowsd[0]<minY){
                    minY=rowsd[0];
                }
                if(rowsd[rowsd.length-1]>maxY){
                    maxY=rowsd[rowsd.length-1];
                }       
            }
        }
        
        double[] rect=new double[]{minX, minY, maxX, maxY};
        return rect;
        
    }

    /**
     * Returns how much grid points box is taking horizontally in grid array.
     *
     * @param box Box to which return value will be calculated.
     * @return Number of grid points that box is taking horizontally in grid array.
     */
    int getNHor(AbstractContainer box) {
        int NHor = (int) Math.ceil((box.right - box.left) / gridSize);
        return NHor;
    }

    /**
     * Returns how much grid points box is taking vertically in grid array.
     *
     * @param box Box to which return value will be calculated.
     * @return Number of grid points that box is taking vertically in grid array.
     */
    int getNVert(AbstractContainer box) {
        int NVert = (int) Math.ceil((box.bottom - box.top) / gridSize);
        return NVert;
    }

    /**
     * Checks if place in grid is free. Place is defined by it's left-top corner (x,y)
     * and size of box (nH, nV).
     *
     * @param x Left-top corner of box (or area).
     * @param y Left-top corner of of box (or area).
     * @param nH Number of grid points taken by box horizontally.
     * @param nV Number of grid points taken by box vertically.
     * @return true- if place is free. false- if place is taken.
     */
    boolean placeIsFree(int x, int y, int nH, int nV) {
        if (x < 0 || x + nH -1 >= horSize || y < 0 || y + nV -1 >= vertSize) {
            return false;
        } else {
            for (int i = x; i <= x + nH -1; i++) {
                for (int j = y; j <= y + nV -1; j++) {
                    if (grid[i][j] != null) {
                        return false;
                    }
                }
            }
            return true;
        }
    }

    /**
     * Puts given box to grid array and makes essential changes in lineGrid array.
     *
     * @param boxIG Box with all parameters already assigned.
     */
    void putBox(BoxInGrid boxIG) {
        //int lineCoef = 1;
        for (int i = boxIG.x; i <= boxIG.x + boxIG.nH - 1; i++) {
            for (int j = boxIG.y; j <= boxIG.y + boxIG.nV - 1; j++) {
                if(grid[i][j]!=null){
                    System.out.println("Error: putting box "+boxIG+" to place of box "+grid[i][j]);
                }
                grid[i][j] = boxIG;
            }
        }

        boxIG.inGrid = true;
        //adding values to lineGrid
        /* not changed
        for (Line line : boxIG.lines) {
            BoxInGrid boxIG2 = line.boxIG;
            if (boxIG2.inGrid) {
                if (boxIG.y == boxIG2.y) {
                    if (boxIG.x + 1 < boxIG2.x) {
                        for (int i = boxIG.x + 1; i < boxIG2.x; i++) {
                            lineGrid[i][boxIG.y] += lineCoef;
                        }
                    } else if (boxIG2.x + 1 < boxIG.x) {
                        for (int i = boxIG2.x + 1; i < boxIG.x; i++) {
                            lineGrid[i][boxIG.y] += lineCoef;
                        }
                    }
                }
                if (boxIG.x == boxIG2.x) {
                    if (boxIG.y + 1 < boxIG2.y) {
                        for (int i = boxIG.y + 1; i < boxIG2.y; i++) {
                            lineGrid[boxIG.x][i] += lineCoef;
                        }
                    } else if (boxIG2.y + 1 < boxIG.y) {
                        for (int i = boxIG2.y + 1; i < boxIG.y; i++) {
                            lineGrid[boxIG.x][i] += lineCoef;
                        }
                    }
                }
            }
        }
        */
    }

    /**
     * Removes box from the grid array
     *
     * @param boxIG Does not change boxIG parameters
     */
    void removeBox(BoxInGrid boxIG) {
        //int lineCoef = 1;
        for (int i = boxIG.x; i <= boxIG.x + boxIG.nH - 1; i++) {
            for (int j = boxIG.y; j <= boxIG.y + boxIG.nV - 1; j++) {
                grid[i][j] = null;
            }
        }

        boxIG.inGrid = false;
        //decreasing values in lineGrid
        /* not changed
        for (Line line : boxIG.lines) {
            BoxInGrid boxIG2 = line.boxIG;
            if (boxIG2.inGrid) {
                if (boxIG.y == boxIG2.y) {
                    if (boxIG.x + 1 < boxIG2.x) {
                        for (int i = boxIG.x + 1; i < boxIG2.x; i++) {
                            lineGrid[i][boxIG.y] -= lineCoef;
                        }
                    } else if (boxIG2.x + 1 < boxIG.x) {
                        for (int i = boxIG2.x + 1; i < boxIG.x; i++) {
                            lineGrid[i][boxIG.y] -= lineCoef;
                        }
                    }
                }
                if (boxIG.x == boxIG2.x) {
                    if (boxIG.y + 1 < boxIG2.y) {
                        for (int i = boxIG.y + 1; i < boxIG2.y; i++) {
                            lineGrid[boxIG.x][i] -= lineCoef;
                        }
                    } else if (boxIG2.y + 1 < boxIG.y) {
                        for (int i = boxIG2.y + 1; i < boxIG.y; i++) {
                            lineGrid[boxIG.x][i] -= lineCoef;
                        }
                    }
                }
            }
        }
        */
    }

    /**
     * Function that return how much grid point are taken by boxes excluding
     * boxIG. It also takes results of nearby calculation as a parameters.
     *
     * @param boxIG Box that is trying to move to (x,y).
     * @param x New x coordinate of box.
     * @param y New y coordinate of box.
     * @param movingDir Number that tells where is the new place relatively to the old place.
     * Old place is place where takenCellCount was previously calculated. 
     * 0-previous place was not calculated;
     * 1-new place is one cell up relatively to old place;
     * 2-new place is one cell right relatively to old place;
     * 3-new place is one cell down relatively to old place;
     * 4-new place is one cell left relatively to old place;
     * @return Number of grid point are taken by boxes excluding boxIG
     */
    int calculateTakenCellCount(BoxInGrid boxIG, int x, int y, int movingDir) {
        //System.out.println("calculateTakenCellCount"+x+" "+y+" takenCellCount="+takenCellCount);
        int radiusH = boxIG.nH / 2;
        int radiusV = boxIG.nV / 2;


        int iFrom = Math.max(x, leftBorder);
        int iTo = Math.min(x + boxIG.nH -1, rightBorder);
        int jFrom = Math.max(y, topBorder);
        int jTo = Math.min(y + boxIG.nV - 1, bottomBorder);

        if (movingDir == 0) { //if this function for this box is run for the first time in this iteration
            cellCount = 0;
            for (int i = iFrom; i <= iTo; i++) {
                for (int j = jFrom; j <= jTo; j++) {
                    if (grid[i][j] != null) {
                        cellCount++;
                    }
                }
            }
        } else if (movingDir == 1) { //box is moving up

            if (y + boxIG.nV <= bottomBorder && y + boxIG.nV >= topBorder) {
                for (int i = iFrom; i <= iTo; i++) {
                    if (grid[i][y + boxIG.nV] != null) {
                        cellCount--;
                    }
                }
            }

            if (y >= topBorder && y <= bottomBorder) {
                for (int i = iFrom; i <= iTo; i++) {
                    if (grid[i][y] != null) {
                        cellCount++;
                    }
                }
            }

        } else if (movingDir == 3) { //box is moving down

            if (y - 1 >= topBorder && y - 1 <= bottomBorder) {
                for (int i = iFrom; i <= iTo; i++) {
                    if (grid[i][y - 1] != null) {
                        cellCount--;
                    }
                }
            }

            if (y + boxIG.nV - 1 <= bottomBorder && y + boxIG.nV - 1 >= topBorder) {
                for (int i = iFrom; i <= iTo; i++) {
                    if (grid[i][y + boxIG.nV -1] != null) {
                        cellCount++;
                    }
                }
            }

        } else if (movingDir == 4) { //box is moving left

            if (x + boxIG.nH <= rightBorder && x + boxIG.nH >= leftBorder) {
                for (int j = jFrom; j <= jTo; j++) {
                    if (grid[x + boxIG.nH][j] != null) {
                        cellCount--;
                    }
                }
            }

            if (x >= leftBorder && x <= rightBorder) {
                for (int j = jFrom; j <= jTo; j++) {
                    if (grid[x][j] != null) {
                        cellCount++;
                    }
                }
            }

        } else if (movingDir == 2) { //box is moving right

            if (x - 1 >= leftBorder && x - 1 <= rightBorder) {
                for (int j = jFrom; j <= jTo; j++) {
                    if (grid[x - 1][j] != null) {
                        cellCount--;
                    }
                }
            }

            if (x + boxIG.nH - 1 <= rightBorder && x + boxIG.nH - 1 >= leftBorder) {
                for (int j = jFrom; j <= jTo; j++) {
                    if (grid[x + boxIG.nH - 1][j] != null) {
                        cellCount++;
                    }
                }
            }

        } else {
            System.err.println("Error in function placeIsFreeToMove!!!");
        }

        //System.out.println("takenCellCount= "+takenCellCount);
        return cellCount;

    }
    /**
     * Checks if boxIG1 can be moved to boxIG2 center and boxIG2 to center of boxIG1. 
     *
     * @param boxIG1 Box to check.
     * @param boxIG2 Box to check.
     * @return true - if two boxes can be swapped. false - if boxes can not be swapped.
     */
    // Not changed. look at line 1406.
    boolean boxesCanBeSwapped(BoxInGrid boxIG1, BoxInGrid boxIG2) {
        int radiusH1 = boxIG1.nH / 2;
        int radiusV1 = boxIG1.nV / 2;
        int radiusH2 = boxIG2.nH / 2;
        int radiusV2 = boxIG2.nV / 2;

        if (boxIG1.x - radiusH2 < 0 || boxIG1.x + radiusH2 >= horSize 
                || boxIG1.y - radiusV2 < 0 || boxIG1.y + radiusV2 >= vertSize
                || boxIG2.x - radiusH1 < 0 || boxIG2.x + radiusH1 >= horSize 
                || boxIG2.y - radiusV1 < 0 || boxIG2.y + radiusV1 >= vertSize) {
            return false;
        }

        for (int i = boxIG1.x - radiusH2; i <= boxIG1.x + radiusH2; i++) {
            for (int j = boxIG1.y - radiusV2; j <= boxIG1.y + radiusV2; j++) {
                if (grid[i][j] != null && grid[i][j] != boxIG1) {
                    return false;
                }
            }
        }

        for (int i = boxIG2.x - radiusH1; i <= boxIG2.x + radiusH1; i++) {
            for (int j = boxIG2.y - radiusV1; j <= boxIG2.y + radiusV1; j++) {
                if (grid[i][j] != null && grid[i][j] != boxIG2) {
                    return false;
                }
            }
        }

        if (boxIG1.gridCol != -1) {
            if (boxIG2.x < columns[boxIG1.gridCol] + radiusH1 
                    || boxIG2.x >= columns[boxIG1.gridCol + 1] - radiusH1) {
                return false;
            }
        }
        if (boxIG1.gridRow != -1) {
            if (boxIG2.y < rows[boxIG1.gridRow] + radiusV1 
                    || boxIG2.y >= rows[boxIG1.gridRow + 1] - radiusV1) {
                return false;
            }
        }

        if (boxIG2.gridCol != -1) {
            if (boxIG1.x < columns[boxIG2.gridCol] + radiusH2 
                    || boxIG1.x >= columns[boxIG2.gridCol + 1] - radiusH2) {
                return false;
            }
        }
        if (boxIG2.gridRow != -1) {
            if (boxIG1.y < rows[boxIG2.gridRow] + radiusV2 
                    || boxIG1.y >= rows[boxIG2.gridRow + 1] - radiusV2) {
                return false;
            }
        }


        return true;
    }
    

    /**
     * Swaps two boxes x and y coordinates. Does not change anything in grid.
     *
     * @param boxIG1 One box to swap.
     * @param boxIG2 Another box to swap.
     */
    /*
     * Not changed. Same as line 568.
     */
    void swapBoxPlaces(BoxInGrid boxIG1, BoxInGrid boxIG2) {
        int tmpX = boxIG1.x;
        int tmpY = boxIG1.y;
        boxIG1.x = boxIG2.x;
        boxIG1.y = boxIG2.y;
        boxIG2.x = tmpX;
        boxIG2.y = tmpY;
    }

    /**
     * Function puts all BoxInGrid elements to grid. All BoxInGrid properties
     * must be assigned before. Function moves all graph to center of the grid.
     * If grid is too small function increases it.
     */
    void putAllBoxesToGrid() {
        //variables that show size of the graph
        int minX, minY, maxX, maxY;
        /*
         * Problem is here. If grid does not exist. columns[0] is equal to 0 and columns[columns.length-1]
         * is equal to horSize as they were assigned later in this function.
         * As a result if there is a box for example in maxX, then grid will be expanded.
         * That is a mistake, because left side of the grid can be empty.
         */
        
        if(!gridExist){
            minX=Integer.MAX_VALUE;
            minY=Integer.MAX_VALUE;
            maxX=Integer.MIN_VALUE;
            maxY=Integer.MIN_VALUE;
        }else{
            minX=columns[0];
            maxX=columns[columns.length-1]-1;
            minY=rows[0];
            maxY=rows[rows.length-1]-1;
        }
      
        for (BoxInGrid boxIG : boxes) {
            if (boxIG.x < minX) {
                minX = boxIG.x;
            }
            if (boxIG.x + boxIG.nH - 1 > maxX) {
                maxX = boxIG.x + boxIG.nH - 1;
            }
            if (boxIG.y < minY) {
                minY = boxIG.y;
            }
            if (boxIG.y + boxIG.nV - 1> maxY) {
                maxY = boxIG.y + boxIG.nV - 1;
            }
        }
        
        /*
        if(rows[rows.length-1]-1>maxX){
            maxX=rows[rows.length-1]-1;
        }
        if(rows[0]<minX){
            minX=rows[0];
        }
        if(columns[columns.length-1]-1>maxY){
            maxY=columns[columns.length-1]-1;
        }
        if(columns[0]<minX){
            minX=columns[0];
        }
        */
        
//        System.out.println("horSize="+horSize+" vertSize="+vertSize);
//        System.out.println("minX="+minX+" maxX="+maxX+" minY="+minY+" maxY="+maxY);
        
        int diffH = 0, diffV = 0;

        if (maxX - minX >= horSize) {  //grid will be expanded
            diffH = maxX - minX - horSize + 1;
            horSize += diffH * 10 / 7 + 4;
        }
        if (maxY - minY >= vertSize) {  //grid will be expanded
            diffV = maxY - minY - vertSize + 1;
            vertSize += diffV * 10 / 7 + 4;
        }


        if (diffH > 0 || diffV > 0) {
            grid = new BoxInGrid[horSize][vertSize];
            //lineGrid = new int[horSize][vertSize];
            //System.out.println("Grid expanded after putting all boxes to grid! It is now: "+horSize+" x "+vertSize);
        }

        //defines vector by which to move all boxes, rows and columns to put graph in the center of grid
        int moveHor = (horSize - (maxX - minX)) / 2 - minX;
        int moveVert = (vertSize - (maxY - minY)) / 2 - minY;


        //moving rows by moveVert
        rows[0] = 0;
        for (int i = 1; i < rows.length - 1; i++) {
            rows[i] += moveVert;
        }
        rows[rows.length - 1] = vertSize;

        //moving columns by moveHor
        columns[0] = 0;
        for (int i = 1; i < columns.length - 1; i++) {
            columns[i] += moveHor;
        }
        columns[columns.length - 1] = horSize;

        //moving all boxes by vector (moveHor, moveVert)
        for (BoxInGrid boxIG : boxes) {
            boxIG.x = boxIG.x + moveHor;
            boxIG.y = boxIG.y + moveVert;
            putBox(boxIG);
        }

    }

    /**
     * Sets boxes to grid according to their real positions. Approximately:
     * boxIG.x=box.getCenterX(); boxIG.y=box.getCenterY();
     *
     * @param useRealSize box size in grid can be greater than 1 if useRealSize
     * is true. Otherwise all boxes are sized 1x1.
     */
    void setBoxesToGrid(boolean useRealSize) {
        
        double maxSize = 0;
        double minSize = Double.POSITIVE_INFINITY;
        
        double[] rect=getGraphEnclosingRect();
        
        double  minX = rect[0],  //most left x coordinate that taken by box of Box object. 
                minY = rect[1], //most top y coordinate that taken by box of Box object
                maxX = rect[2], //similar
                maxY = rect[3]; //similar
        
        for (BoxInGrid boxIG : boxes) {
            AbstractContainer box = boxIG.box;
            
            //getting smalest and biggest box size
            if (box.bottom - box.top > maxSize) {
                maxSize = box.bottom - box.top;
            }
            if (box.right - box.left > maxSize) {
                maxSize = box.right - box.left;
            }

            if (box.bottom - box.top < minSize) {
                minSize = box.bottom - box.top;
            }
            if (box.right - box.left < minSize) {
                minSize = box.right - box.left;
            }

        }



        //setting gridSize;
         /*
         If sizes of biggest and smallest boxes doesn't differ too much
         than gridSize is bigger that the biggest box.
         It means that all boxes in grid will have size 1x1 and will be treated as same size boxes.
               
         If boxes sizes difference is bigger than we treat boxes as different sized.
         */
        
        //something bad
//        System.out.println("minX="+minX+" maxX="+maxX+" minY="+minY+" maxY="+maxY);
        if (maxSize <= minSize * 4) {
            //maybe there is a need to add max here like in next lines.
            gridSize = (int) Math.max(maxSize + 2, Math.max((maxX - minX) / 500, (maxY - minY) / 500) );
        } else {
            //If Java heap space error occurs than number 1500 must be decreased
            gridSize = (int) Math.max(3*minSize/2, Math.max((maxX - minX) / 500, (maxY - minY) / 500));
        }
        
        if(gridSize<=0){ // if there are no boxes
            gridSize=30;;
        }


        firstNormalize(true);
        firstNormalize(false);
        //firstNormalize(true);
        //firstNormalize(false);
        
        rect=getGraphEnclosingRect();
        
        minX = rect[0]; //most left x coordinate that taken by box of Box object. 
        minY = rect[1]; //most top y coordinate that taken by box of Box object
        maxX = rect[2]; //similar
        maxY = rect[3]; //similar

        double extraSpace = 1.2;
        horSize = (int) Math.ceil(((maxX - minX) / gridSize + 4) * extraSpace);
        vertSize = (int) Math.ceil(((maxY - minY) / gridSize + 4) * extraSpace);
//        System.out.println("Grid creation after firstNormalize. horSize="+horSize+" vertSize="+vertSize+ " boxes.length="+boxes.length+" gridSize="+gridSize);
        grid = new BoxInGrid[horSize][vertSize];
        //lineGrid = new int[horSize][vertSize];

        //System.out.println("gridSize= " + gridSize + " horSize= " + horSize + " vertSize= " + vertSize);

        for (BoxInGrid boxIG : boxes) {
            AbstractContainer box = boxIG.box;


            /*
             * If useRealSize is false algorithm treats all boxes as same sized.
             * It gives more possibilities to manipulate with boxs places.
             */
            if (useRealSize) {
                boxIG.nH = getNHor(box);  //nH is how much grid points box is taking horizontally
                boxIG.nV = getNVert(box); //nV is how much grid points box is taking vertically
            } else {
                boxIG.nH = 1;
                boxIG.nV = 1;              
            }
            
            if (boxIG.nH % 2 == 0) {
                boxIG.x = (int) Math.round((boxIG.xd - gridSize * (boxIG.nH / 2) + gridSize / 2) / gridSize);
                boxIG.y = (int) Math.round((boxIG.yd - gridSize * (boxIG.nV / 2) + gridSize / 2) / gridSize);
            } else {
                boxIG.x = (int) Math.round((boxIG.xd - gridSize * (boxIG.nH / 2)) / gridSize);
                boxIG.y = (int) Math.round((boxIG.yd - gridSize * (boxIG.nV / 2)) / gridSize);
            }

            /*
             * In this loop real line start point are calculated if line is connected to nested box.
             */
            for (Line line : boxIG.lines) {
                if (!line.startIsOriginal) {
                    line.startX = Math.max(0,
                            Math.min(boxIG.nH - 1, (int) Math.round(line.realStartX / gridSize)));
                    line.startY = Math.max(0,
                            Math.min(boxIG.nV - 1, (int) Math.round(line.realStartY / gridSize)));
                }
                if (!line.endIsOriginal) {
                    line.endX = Math.max(0,
                            Math.min(line.boxIG.nH - 1, (int) Math.round(line.realEndX / gridSize)));
                    line.endY = Math.max(0,
                            Math.min(line.boxIG.nV - 1, (int) Math.round(line.realEndY / gridSize)));
                }
            }
        }

        /*
         * Assigning row and column integer values. Double values were calculated in firstNormalize() function.
         */
        rows = new int[rowsd.length];
        for (int i = 0; i < rowsd.length; i++) {
            rows[i] = (int) Math.round(rowsd[i] / gridSize);
        }

        columns = new int[columnsd.length];
        for (int i = 0; i < columnsd.length; i++) {
            columns[i] = (int) Math.round(columnsd[i] / gridSize);
        }

        /*
        for(BoxInGrid box : boxes) {
            Collections.shuffle(box.lines);
        }
        */
        
        if (startBFS) {
            boolean[] boxVisited = new boolean[boxes.length];
            for (int i = 0; i < boxVisited.length; i++) {
                if (!boxVisited[i]) {
                    LinkedList<Integer> queue = new LinkedList<Integer>();
                    queue.add(i);
                    boxVisited[i] = true;
                    while (!queue.isEmpty()) {
                        int t = queue.removeFirst();
                        putToBestPlace(boxes[t]);

                        for (Line line : boxes[t].lines) {
                            int j = line.boxIG.boxesPlace;  //were can be mistake, because of sorting boxes
                            if (!boxVisited[j]) {
                                boxVisited[j] = true;
                                queue.add(j);
                            }
                        }
                    }
                }
            }
        } else {
            putAllBoxesToGrid();
        }
    }

    /**
     * Function for testing BFS based layout. Puts given box to it's best place.
     * Best place here means point closest to (x,y) where x is median of 
     * x coordinates of boxes adjacent to boxIG and y is median of y coordinates
     * of boxes adjacent to boIG.
     *
     * @param boxIG Box that will be putted to it's best place.
     */
    void putToBestPlace(BoxInGrid boxIG) {
        
        randomCoef=0.001;
        //random=new Random();
        //random.setSeed(1);
       
        if (boxIG.gridCol != -1) {
                    leftBorder = columns[boxIG.gridCol];
                    rightBorder = columns[boxIG.gridCol + 1] - 1;
                } else {
                    leftBorder = 0;
                    rightBorder = horSize - 1;
                }

                if (boxIG.gridRow != -1) {
                    topBorder = rows[boxIG.gridRow];
                    bottomBorder = rows[boxIG.gridRow + 1] - 1;
                } else {
                    topBorder = 0;
                    bottomBorder = vertSize - 1;
                }

                //System.out.println("Box x and y= "+boxIG1.x+" "+boxIG1.y+" Box nH and nV= "+boxIG1.nH+" "+boxIG1.nV+" leftBorder="+leftBorder+" rightBorder="+rightBorder+" topBorder="+topBorder+" bottomBorder="+bottomBorder);



                
//                int sumX=0;
//                int sumY=0;
//                int conBoxCount=0;
                ArrayList<Integer> xCoord=new ArrayList<>();
                ArrayList<Integer> yCoord=new ArrayList<>();
                for(int i=0; i<boxIG.lines.size(); i++){
                    if(boxIG.lines.get(i).boxIG.inGrid){
                        xCoord.add(boxIG.lines.get(i).boxIG.x);
                        yCoord.add(boxIG.lines.get(i).boxIG.y);
//                        sumX+=boxIG.lines.get(i).boxIG.x;
//                        sumY+=boxIG.lines.get(i).boxIG.y;
//                        conBoxCount++;
                    }
                }
                
                double[] xCArr=new double[xCoord.size()];
                double[] yCArr=new double[yCoord.size()];
                for(int i=0; i<xCArr.length; i++){
                    xCArr[i]=xCoord.get(i);
                    yCArr[i]=yCoord.get(i);
                }
                
                int medX, medY;
                if(xCArr.length==0){
                    medX=horSize/2;
                    medY=vertSize/2;
                }else{
                    medX=(int)Math.round(findMedian(xCArr));
                    medY=(int)Math.round(findMedian(yCArr));
                }
                
                
                
                bestPlaceCost = Double.POSITIVE_INFINITY;


                int rX=0;
                int rY=0;
                bestX = -1;
                bestY = -1;
                placeFound = false;


                rX = 0;
                rY = 0;
                moveAndCheckPlace(boxIG, medX, medY, 0);
                
                /*
                 * If rX==0 it means that while loop is run for the first time. 
                 * The reason for adding this condition in to let find better place than in median.
                 * So places in radius 1 also will be checked.
                 */
                while (!placeFound || rX == 0) { //
                    rX++;
                    rY++;

                    /*going one step up */
                    moveAndCheckPlace(boxIG, medX - rX + 1, medY - rY, 1);


                    /* goint to the right top*/
                    for (int i = medX - rX + 2; i <= medX + rX; i++) {
                        moveAndCheckPlace(boxIG, i, medY - rY, 2);
                    }

                    /* going to bottom right */
                    for (int j = medY - rY + 1; j <= medY + rY; j++) {
                        moveAndCheckPlace(boxIG, medX + rX, j, 3);
                    }

                    /*going to left bottom */
                    for (int i = medX + rX - 1; i >= medX - rX; i--) {
                        moveAndCheckPlace(boxIG, i, medY + rY, 4);
                    }

                    /*going to top right */
                    for (int j = medY + rY - 1; j >= medY - rY; j--) {
                        moveAndCheckPlace(boxIG, medX - rX, j, 1);
                    }

                }

             
                boxIG.x = bestX;
                boxIG.y = bestY;
                putBox(boxIG);

    }

    /**
     * Function takes 2 segments [min1, max1] and [min2, max2] and calculates
     * distance between them. Segments are one-dimensional. If they overlap
     * function returns 0.
     *
     * @param min1 Smaller coordinate of first segment.
     * @param max1 Bigger coordinate of first segment.
     * @param min2 Smaller coordinate of second segment.
     * @param max2 Bigger coordinate of second segment.
     * @return Distance between segments or 0 if they overlap.
     */
    int segmentDistance(int min1, int max1, int min2, int max2) {
        int[] a = new int[4];
        a[0] = min1;
        a[1] = max1;
        a[2] = min2;
        a[3] = max2;

        int[] order = new int[4];

        int min = Integer.MAX_VALUE;
        int minIndex = 0; //initialized just to let compile
        for (int i = 1; i <= 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (order[j] == 0 && a[j] < min) {
                    min = a[j];
                    minIndex = j;
                }
            }
            order[minIndex] = i;
            min = Integer.MAX_VALUE;
        }

        if (((order[0] == 1 || order[0] == 2) && (order[1] == 3 || order[1] == 4))
                || ((order[0] == 3 || order[0] == 4) && (order[1] == 1 || order[1] == 2))) {
            return 0;
        } else {
            return a[order[2] - 1] - a[order[1] - 1];
        }
    }

    /**
     * Calculates sum of all distances between boxIG and boxes that are
     * adjacent to it. Distance is calculated by special function. So it is not
     * euclidian or Manhattan distance.
     *
     * @param boxIG Box which lines will be calculated.
     * @return Sum of all line lengths that are connected to boxIG.
     */
    double connectedLineLength(BoxInGrid boxIG) {
        double length = 0;
        for (Line line : boxIG.lines) {
            BoxInGrid boxIGc = line.boxIG;
            if(boxIGc.inGrid){
                int horDist;
                int vertDist;
                if (line.endIsOriginal) {
                    if (line.startIsOriginal) {
                        vertDist = segmentDistance(boxIG.y, 
                                boxIG.y + boxIG.nV - 1, boxIGc.y, boxIGc.y + boxIGc.nV - 1);
                        horDist = segmentDistance(boxIG.x,
                                boxIG.x + boxIG.nH - 1, boxIGc.x, boxIGc.x + boxIGc.nH - 1);
                    } else {
                        vertDist = segmentDistance(boxIG.y + line.startY, boxIG.y + line.startY,
                                boxIGc.y, boxIGc.y + boxIGc.nV - 1);
                        horDist = segmentDistance(boxIG.x + line.startX, boxIG.x + line.startX,
                                boxIGc.x, boxIGc.x + boxIGc.nH - 1);
                    }
                } else {
                    if (line.startIsOriginal) {
                        vertDist = segmentDistance(boxIG.y, boxIG.y + boxIG.nV - 1,
                                boxIGc.y + line.endY, boxIGc.y + line.endY);
                        horDist = segmentDistance(boxIG.x, boxIG.x + boxIG.nH - 1,
                                boxIGc.x + line.endX, boxIGc.x + line.endX);
                    } else {
                        vertDist = segmentDistance(boxIG.y + line.startY, boxIG.y + line.startY,
                                boxIGc.y + line.endY, boxIGc.y + line.endY);
                        horDist = segmentDistance(boxIG.x + line.startX, boxIG.x + line.startX,
                                boxIGc.x + line.endX, boxIGc.x + line.endX);
                    }
                }

                length += Math.sqrt(horDist * horDist + vertDist * vertDist)
                        + Math.min(Math.abs( boxIG.x + (double)boxIG.nH/2 
                        - (boxIGc.x + (double)boxIGc.nH/2) ) / ((double)boxIG.nH/2 + (double)boxIGc.nH/2),
                        Math.abs( boxIG.y + (double)boxIG.nV/2 - (boxIGc.y + (double)boxIGc.nV/2) ) 
                        / ((double)boxIG.nV/2 + (double)boxIGc.nV/2)) / SLIDE_COEF;
            }
        }
        return length;
    }

    /**
     * Function finds median in given array.
     *
     * @param arr Array, where to find median.
     * @return Median found in array.
     */
    double findMedian(double[] arr) {
        //THIS can be improved
        Arrays.sort(arr);
        if (arr.length == 0) {
            return Double.NaN;
        } else if (arr.length % 2 == 1) {
            return arr[arr.length / 2];
        } else {
            return (arr[(arr.length - 1) / 2] + arr[arr.length / 2]) / 2;
        }


        /*
         int leftMedian=arr.length/2;
         int rightMedian=(arr.length+1)/2; //if array length is odd leftMedian will be the same as rightMedian.
         //Othervise they will be 2 numbers in the middle
         int l=0, r=arr.length-1;
         int oldL=l, oldR=r;
         while(l<leftMedian || r>rightMedian){
         int pivot=arr[(r-l)/2];
         while(l<r){
         while(arr[l]<=pivot){
         l++;
         }
         while(arr[r]>pivot){
         r++;
         }
         if(l<r){
         int tmp=arr[l];
         arr[l]=arr[r];
         arr[r]=tmp;
         }
         }
         l--;
         r++;
                
         if(r<=leftMedian){
         l=r;
         oldL=l;
         r=oldR;
         }else if(l>=rightMedian){
         r=l;
         oldR=r;
         l=oldL;
         }else{  //it means that l==leftMedian r==rightMedian
         //all stays the same??? :/
         }
         }
         */

    }

    /**
     * Returns median of all x coordinates of boxes that are adjacent to boxIG.
     *
     * @param boxIG box to which median of x coordinate of adjacent boxes will be calculated.
     * @return median of all x coordinates of boxes that are adjacent to boxIG.
     * If there are no adjacent boxes than x coordinate of boxIG will be returned.
     */
    double getMedianXOfConnectedBoxes(BoxInGrid boxIG) {
        double[] xValues = new double[boxIG.lines.size()];
        for (int i = 0; i < xValues.length; i++) {
            BoxInGrid boxIGc=boxIG.lines.get(i).boxIG;
            if (boxIG.lines.get(i).endIsOriginal) {
                xValues[i] = boxIGc.x + ((double)boxIGc.nH)/2 - 0.5;
            } else {
                xValues[i] = boxIGc.x + boxIG.lines.get(i).endX;
            }
        }
        double med = findMedian(xValues);
        if (!Double.isNaN(med)) {
            return med;
        } else {
            return boxIG.x + ((double)boxIG.nH)/2 - 0.5;
        }
    }

    /**
     * Returns median of all y coordinates of boxes that are adjacent to boxIG
     *
     * @param boxIG box to which median of y coordinate of adjacent boxes will be calculated.
     * @return median of all y coordinates of boxes that are adjacent to boxIG.
     * If there are no adjacent boxes than x coordinate of boxIG will be returned.
     */
    double getMedianYOfConnectedBoxes(BoxInGrid boxIG) {
        double[] yValues = new double[boxIG.lines.size()];
        for (int i = 0; i < yValues.length; i++) {
            BoxInGrid boxIGc=boxIG.lines.get(i).boxIG;
            if (boxIG.lines.get(i).endIsOriginal) {
                yValues[i] = boxIGc.y + ((double)boxIGc.nV)/2 - 0.5;
            } else {
                yValues[i] = boxIGc.y + boxIG.lines.get(i).endY;
            }
        }
        double med = findMedian(yValues);
        if (!Double.isNaN(med)) {
            return med;
        } else {
            return boxIG.y + ((double)boxIG.nV)/2 - 0.5;
        }
    }

    /**
     * Function checks if given boxIG can be put to rectangle defined by leftBorder, rightBorder, topBorder, bottomBorder in place (x, y).
     * (leftBorder, topBorder) is top-left corner of the rectangle and (rightBorder, bottomBorder) bottom-right corner.
     * @param boxIG Box to check.
     * @param x first coordinate of place to check.
     * @param y second coordinate of place to check.
     * @return true if boxIG can be put in (x,y) or false if it can not be put there.
     */
    boolean inRectangle(BoxInGrid boxIG, int x, int y) {
        if (x < leftBorder || x + boxIG.nH - 1 > rightBorder || y < topBorder || y + boxIG.nV - 1 > bottomBorder) {
            return false;
        }else{
            return true;
        }
    }

    
    /**
     * Function that is "moving" box by 1 cell in direction defined by movingDir.
     * This function makes all the work necessary for 1 such move.
     * @param boxIG1 Box to "move".
     * @param x coordinate of place where box checking is moved. 
     * @param y coordinate of place where box checking is moved. 
     * @param movingDir Number that tells where is the new box relatively to box
     * where takenCellCount was calculated. 0-means that it was not calculated.
     * takanCellCount is then 0; 1-one cell up; 2-one cell right; 3-one cell
     * down; 4-one cell left;
     */
    void moveAndCheckPlace(BoxInGrid boxIG1, int x, int y, int movingDir) {
        cellCount = calculateTakenCellCount(boxIG1, x, y, movingDir);
        if (cellCount == 0 && inRectangle(boxIG1, x, y)) {
            double rand = random.nextDouble() * randomCoef;
            boxIG1.x = x;
            boxIG1.y = y;
            curPlaceCost = connectedLineLength(boxIG1) + rand /*+ lineGrid[boxIG1.x][boxIG1.y] *crCoef */;
            if (curPlaceCost < bestPlaceCost) {
                //System.out.println("inside curPlaceCost < bestPlaceCost. curPlaceCost= "+curPlaceCost);
                bestPlaceCost = curPlaceCost;
                bestX = boxIG1.x;
                bestY = boxIG1.y;
                placeFound = true;
            }

        }
    }

    /**
     * Improves places of boxes by changing their positions to positions nearby
     * or swapping box places. Also Normalization is called from this function.
     */
    
    void improveBoxPlaces() {
        
        
        int prevChanges;
        int newChanges = 0;
        int iterations = 0;
        int timesToIterate;
        
        int normalizationRate;
        final double lowestRandomCoef=0.2;
        
        if(startRandom){
            timesToIterate=90*(int)Math.sqrt(boxes.length);
            randomCoef=((horSize+vertSize)/5);
        }else if(startBFS){
            timesToIterate=10*(int)Math.sqrt(boxes.length);
            randomCoef=((horSize+vertSize)/50);
        }else{
            timesToIterate=100;
            randomCoef=3;
        }
        
        if(randomCoef<2*lowestRandomCoef){
            randomCoef=2*lowestRandomCoef;
        }
        
        if(timesToIterate>150){
            normalizationRate=9;
        }else{
            normalizationRate=3;
        }

        
        double k=Math.pow( lowestRandomCoef/randomCoef, 1/(double)timesToIterate );

        do {

            iterations++;
            randomCoef*=k;
            prevChanges = newChanges;
            newChanges = 0;
            
            for (int i1 = 0; i1 < boxes.length; i1++) {  //trying to move box to nearby places
                BoxInGrid boxIG1 = boxes[i1];

                if (boxIG1.gridCol != -1) {
                    leftBorder = columns[boxIG1.gridCol];
                    rightBorder = columns[boxIG1.gridCol + 1] - 1;
                } else {
                    leftBorder = 0;
                    rightBorder = horSize - 1;
                }

                if (boxIG1.gridRow != -1) {
                    topBorder = rows[boxIG1.gridRow];
                    bottomBorder = rows[boxIG1.gridRow + 1] - 1;
                } else {
                    topBorder = 0;
                    bottomBorder = vertSize - 1;
                }

                //System.out.println("Box x and y= "+boxIG1.x+" "+boxIG1.y+" Box nH and nV= "+boxIG1.nH+" "+boxIG1.nV+" leftBorder="+leftBorder+" rightBorder="+rightBorder+" topBorder="+topBorder+" bottomBorder="+bottomBorder);



                double medianX = getMedianXOfConnectedBoxes(boxIG1);
                double medianY = getMedianYOfConnectedBoxes(boxIG1);
                
                
                //maybe two randoms need to be done.
                medianX+= (random.nextDouble()-0.5)*randomCoef*boxIG1.nH;
                
                medianY+= (random.nextDouble()-0.5)*randomCoef*boxIG1.nV;
                
                /*
                 * place for left-top corner of the boxIG
                 */
                int x1=(int)Math.round(medianX - (double)boxIG1.nH/2 + 0.5);
                int y1=(int)Math.round(medianY - (double)boxIG1.nV/2 + 0.5);
                
                x1=Math.min( Math.max(x1, leftBorder), rightBorder );
                y1=Math.min( Math.max(y1, topBorder), bottomBorder );
                
                
                double rand;
                bestPlaceCost = Double.POSITIVE_INFINITY;
                removeBox(boxIG1);
                
                /*
                 * Coordinates of right-top corner of the box.
                 */
                int x2, y2;
                /*
                 * distance from rectangle (x1, y1, x2, y2) to potential box's place.
                 */
                int dist = 0;
                bestX = -1;
                bestY = -1;
                placeFound = false;

                int defaultX = boxIG1.x;
                int defaultY = boxIG1.y;
                //double defLineLength=connectedLineLength(boxIG1)+rand + lineGrid[boxIG1.x][boxIG1.y]*crCoef;

                // regular start
                if (boxIG1.lines.size() > 1 || boxIG1.lines.isEmpty() || !oneLineSpecialCase) {
                    x2=x1;
                    y2=y1;
                    moveAndCheckPlace(boxIG1, x1, y1, 0);
                } else { //start with big rectangle created by two box radiuses.
                    x1=boxIG1.lines.get(0).boxIG.x - (boxIG1.nH - 1);
                    y1=boxIG1.lines.get(0).boxIG.y - (boxIG1.nV - 1);
                    x2=boxIG1.lines.get(0).boxIG.x + boxIG1.lines.get(0).boxIG.nH - 1;
                    y2=boxIG1.lines.get(0).boxIG.y + boxIG1.lines.get(0).boxIG.nV - 1;
                    //medianX = boxIG1.lines.get(0).boxIG.x;
                    //medianY = boxIG1.lines.get(0).boxIG.y;
                    //this place always will be taken:
                    moveAndCheckPlace(boxIG1, x1, y1, 0);
                    //cellCount = calculateTakenCellCount(boxIG1, x1, y1, 0);
                }


                for (Line line : boxIG1.lines) {
                    if (!line.endIsOriginal) {
                        //System.out.println("endX= " + line.endX + " endY= " + line.endY);
                    }
                }
                
                /*
                 * If rX==0 it means that while loop is run for the first time. 
                 * The reason for adding this condition in to let find better place than in median.
                 * So places in radius 1 also will be checked.
                 */
                while (!placeFound || dist == 0) { 
                    dist++;

                    /*going one step up */
                    moveAndCheckPlace(boxIG1, x1 - dist + 1, y1 - dist, 1);


                    /* goint to the right top*/
                    for (int i = x1 - dist + 2; i <= x2 + dist; i++) {
                        moveAndCheckPlace(boxIG1, i, y1-dist, 2);
                    }

                    /* going to bottom right */
                    for (int j = y1-dist+1; j <= y2+dist; j++) {
                        moveAndCheckPlace(boxIG1, x2+dist, j, 3);
                    }

                    /*going to left bottom */
                    for (int i = x2+dist-1; i >= x1-dist; i--) {
                        moveAndCheckPlace(boxIG1, i, y2+dist, 4);
                    }

                    /*going to top left */
                    for (int j = y2+dist-1; j >= y1-dist; j--) {
                        moveAndCheckPlace(boxIG1, x1-dist, j, 1);
                    }

                }

                //System.out.println("rX= "+rX+"rY= "+rY);


                /*
                 * If BoxIG was already in it's best place, 
                 * than algorithm tryies to change it with boxes nearby.
                 */

                boxIG1.x = defaultX;
                boxIG1.y = defaultY;
                putBox(boxIG1);
                boolean swap = false;
                //next assignments done only to overcome "might not be initialized"
                int bestXForBox1=0, bestYForBox1=0, bestXForBox2=0, bestYForBox2=0;
                BoxInGrid bestBoxToSwap=boxIG1;
                
               
                if (defaultX == bestX && defaultY == bestY) {
                    double bestLineDif = 0;
                    rand = random.nextDouble() * randomCoef/100;
                    double defLineLength1 = connectedLineLength(boxIG1) + rand;
                    double defLineLength2;
                    double newLineLength;
                    //int rH = boxIG1.nH / 2;
                    //int rV = boxIG1.nV / 2;


                    //places where to try to swap box
                    int[][] coord;
                    
                    coord = new int[][]{{boxIG1.x-1, boxIG1.y + boxIG1.nV/2},
                        {boxIG1.x + boxIG1.nH/2, boxIG1.y - 1},
                        {boxIG1.x + boxIG1.nH, boxIG1.y + boxIG1.nV/2},
                        {boxIG1.x + boxIG1.nH/2, boxIG1.y + boxIG1.nV}};                
                    

                    for (int i = 0; i < 4; i++) {
                        if (coord[i][0] >= leftBorder && coord[i][0] <= rightBorder
                                && coord[i][1] >= topBorder && coord[i][1] <= bottomBorder) {

                            if (grid[ coord[i][0]][ coord[i][1]] != null) {
                                BoxInGrid boxIG2 = grid[ coord[i][0]][ coord[i][1]];
                                removeBox(boxIG1);
                                removeBox(boxIG2);
                                int xForBox1;
                                int yForBox1;
                                int xForBox2;
                                int yForBox2;
                                if(boxIG1.nH % 2==boxIG2.nH % 2){
                                    xForBox1=(int)Math.round((boxIG2.x + (double)boxIG2.nH/2 -0.5) - (double)boxIG1.nH/2+0.5 );
                                    xForBox2=(int)Math.round((boxIG1.x + (double)boxIG1.nH/2 -0.5) - (double)boxIG2.nH/2+0.5 );
                                }else{
                                    xForBox1=(int)Math.round((boxIG2.x + (double)boxIG2.nH/2 -0.5) - (double)boxIG1.nH/2);
                                    xForBox2=(int)Math.round((boxIG1.x + (double)boxIG1.nH/2 -0.5) - (double)boxIG2.nH/2);
                                }
                                if(boxIG1.nV % 2==boxIG2.nV % 2){
                                    yForBox1=(int)Math.round((boxIG2.y + (double)boxIG2.nV/2 -0.5) - (double)boxIG1.nV/2+0.5 );
                                    yForBox2=(int)Math.round((boxIG1.y + (double)boxIG1.nV/2 -0.5) - (double)boxIG2.nV/2+0.5 );
                                }else{
                                    yForBox1=(int)Math.round((boxIG2.y + (double)boxIG2.nV/2 -0.5) - (double)boxIG1.nV/2);
                                    yForBox2=(int)Math.round((boxIG1.y + (double)boxIG1.nV/2 -0.5) - (double)boxIG2.nV/2);
                                }
                                int cellCountBox1 = calculateTakenCellCount(boxIG1, xForBox1, yForBox1, 0);
                                int cellCountBox2 = calculateTakenCellCount(boxIG2, xForBox2, yForBox2, 0);
                                int oldX1=boxIG1.x;
                                int oldY1=boxIG1.y;
                                int oldX2=boxIG2.x;
                                int oldY2=boxIG2.y;
                                if(cellCountBox1==0 && cellCountBox2==0
                                        && inRectangle(boxIG1, xForBox1, yForBox1)
                                        && inRectangle(boxIG2, xForBox2, yForBox2) ){
                                    
                                    rand = random.nextDouble() * randomCoef/100;
                                    defLineLength2 = connectedLineLength(boxIG2) + rand;
                                    
                                    boxIG1.x=xForBox1;
                                    boxIG1.y=yForBox1;
                                    boxIG2.x=xForBox2;
                                    boxIG2.y=yForBox2;
                                    putBox(boxIG1);
                                    putBox(boxIG2);
                                    
                                    rand = random.nextDouble() * randomCoef/100;
                                    newLineLength = connectedLineLength(boxIG1) + connectedLineLength(boxIG2) 
                                            + 2 * rand;
                                    
                                    if (defLineLength1 + defLineLength2 - newLineLength > bestLineDif) {
                                        bestLineDif = defLineLength1 + defLineLength2 - newLineLength;
                                        swap = true;
                                        bestXForBox1=xForBox1;
                                        bestYForBox1=yForBox1;
                                        bestXForBox2=xForBox2;
                                        bestYForBox2=yForBox2;
                                        bestBoxToSwap=boxIG2;
                                    }         
                                    
                                    removeBox(boxIG1);
                                    removeBox(boxIG2);
                                }
                                boxIG1.x = oldX1;
                                boxIG1.y = oldY1;
                                boxIG2.x = oldX2;
                                boxIG2.y = oldY2;
                                putBox(boxIG1);
                                putBox(boxIG2);
                                
//                                if (boxesCanBeSwapped(boxIG1, boxIG2)) {
//                                    rand = random.nextFloat() * randomCoef/100;
//                                    defLineLength2 = connectedLineLength(boxIG2) + rand;
//                                    swapBoxPlaces(boxIG1, boxIG2);
//                                    rand = random.nextFloat() * randomCoef/100;
//                                    newLineLength = connectedLineLength(boxIG1) + connectedLineLength(boxIG2) 
//                                            + 2 * rand/200;
//                                    if (defLineLength1 + defLineLength2 - newLineLength > bestLineDif) {
//                                        bestX = boxIG1.x;
//                                        bestY = boxIG1.y;
//                                        bestLineDif = defLineLength1 + defLineLength2 - newLineLength;
//                                        swap = true;
//                                    }
//                                    swapBoxPlaces(boxIG1, boxIG2);
//                                }
                            }

                        }

                    }
                }
                //------------
                

                if (swap) {
                    removeBox(boxIG1);
                    removeBox(bestBoxToSwap);
                    boxIG1.x=bestXForBox1;
                    boxIG1.y=bestYForBox1;
                    bestBoxToSwap.x=bestXForBox2;
                    bestBoxToSwap.y=bestYForBox2;
                    putBox(boxIG1);
                    putBox(bestBoxToSwap);
//                    System.out.println("Boxes swapped");
                } else {
                    removeBox(boxIG1);
                    boxIG1.x = bestX;
                    boxIG1.y = bestY;
                    //System.out.println("bestX= "+bestX+"bestY= "+bestY+ "placeFound= "+placeFound+" nH= "+boxIG1.nH+" nV= "+boxIG1.nV);
                    putBox(boxIG1);
                    //System.out.println("box moved to median");
                }


            }


            /*
             * In some iterations boxes are normalized.
             * 
             * At first there are big spaces left between them. Later this distance is decresing.
             * This approach prevents graph layout from beeing too wide or too narrow.
             * 
             * At first all boxes in graph are sized 1x1. Later they are expanded to their original size.
             * It happens when second argument in normalize function is true
             */
            
            
            
            if( timesToIterate - iterations - normalizationRate*3 <0 ){
                randomCoef=0;
            }
            
            if(startRandom || startBFS || true){
                int extendingTime=timesToIterate/2;
                boolean extend=false;
                if(iterations==extendingTime){
                    extend=true;
                }
                
                if(extend){
                    
                    normalize(true,true, 3);
                    normalize(false, true, 3);
                    
                }else if(iterations % normalizationRate ==0){
                    boolean hor;
                    if(iterations % 2 ==0){
                        hor=true;
                    }else{
                        hor=false;
                    }
                    
                    if (iterations <= extendingTime) {
                        normalize(hor, false, 3);
                    } else {
                        normalize(hor, false, Math.max(1, 1 + 
                                (double)(timesToIterate - iterations - normalizationRate*3)
                                 /(timesToIterate - extendingTime) ) );
                    }
                }
                
            }else{
                
                if (iterations == 1) {
                    normalize(false, false, 3);
                    normalize(true, false, 2);
                } else if (iterations == (timesToIterate / 10) * 3) {
                    normalize(false, true, 2); //these coeficients probably must change with different box sizes
                    normalize(true, true, 3);
                } else if (iterations == (timesToIterate / 10) * 5) {
                    normalize(false, false, 1.5);
                } else if (iterations == (timesToIterate / 10) * 6) {
                    normalize(true, false, 1.5);
                } else if (iterations == (timesToIterate / 10) * 7) {
                    normalize(true, false, 1);
                } else if (iterations == (timesToIterate / 10) * 8) {
                    normalize(false, false, 1);
                } else if (iterations == (timesToIterate / 10) * 9) {
                    normalize(false, false, 1);
                } else if (iterations == timesToIterate) {
                    normalize(true, false, 1);
                    normalize(false, false, 1);
                    //Normalize(true, false, 1);
                }
                
            }
            
            
            //System.out.print("iterations= "+iterations+"  randomCoef= "+randomCoef);
            //printAllLineLength();
        } while (iterations <= timesToIterate);

        /*
         for(int i=0; i<vertSize; i++){
         for(int j=0; j<horSize; j++){
         System.out.print(lineGrid[j][i]+" ");
         }
         System.out.println();
         }
         System.out.println();
         for(int i=0; i<vertSize; i++){
         for(int j=0; j<horSize; j++){
         if(grid[j][i]!=null){
         System.out.print("# ");
         }else{
         System.out.print("0 ");
         }
         }
         System.out.println();
         }            
         */
    }

    /**
     * Normalizes box places. Works with BoxInGrid object and changes their x
     * and y attributes and with rows[] and columns[] arrays.
     *
     * @param horizontally True- if horizontally. False if vertically
     * @param extendBoxSize True if boxes size were 1 and it is needed to make
     * it bigger(extend to real box size). False in any other case.
     * @param extraSpaceCoef Defines how much empty place to left between boxes 
     * relatively to size of boxes. Minimum value should be 1.
     */
    void normalize(boolean horizontally, boolean extendBoxSize, double extraSpaceCoef) {
        //System.out.println("Normalization!");

        /*
         * EPS is needed to ensure that 2 boxes will never be in one grid point after normalization.
         * It must be bigger than epsilon in QuadraticOptimizer which is 0.001 by default.
         */
        final double EPS = 0.002;

        /*
         * extraVarCount is number of lines that ends in nested box
         */
        int extraVarCount = 0;
        for (BoxInGrid boxIG : boxes) {
            for (Line line : boxIG.lines) {
                if (!line.startIsOriginal) {
                    extraVarCount++;
                }
            }
        }

        QuadraticOptimizer optimizer;

        /*
         * firstGridIndex is index of first variable that represents grid column or row from gridLayoutConstraints
         */
        final int firstGridIndex = boxes.length + extraVarCount;
        /*
         * Assigning grid variables if grid exist
         */
        if (gridExist) {
            if (horizontally) {
                optimizer = new QuadraticOptimizer(firstGridIndex + columns.length);
                for (int i = firstGridIndex; i < firstGridIndex + columns.length; i++) {
                    optimizer.setVariable(i, columns[i - firstGridIndex]);
                }
                for(int i = firstGridIndex; i < firstGridIndex + columns.length -1; i++){
                    optimizer.addInequality(i, i+1, EPS);
                    optimizer.addQuadraticDifference(i, i+1, 1);
                }
            } else {
                optimizer = new QuadraticOptimizer(firstGridIndex + rows.length);
                for (int i = firstGridIndex; i < firstGridIndex + rows.length; i++) {
                    optimizer.setVariable(i, rows[i - firstGridIndex]);
                }
                for(int i = firstGridIndex; i < firstGridIndex + rows.length -1; i++){
                    optimizer.addInequality(i, i+1, EPS);
                    optimizer.addQuadraticDifference(i, i+1, 1);
                }
            }
        } else {
            optimizer = new QuadraticOptimizer(boxes.length + extraVarCount);
        }


        /*
         * Assigning all boxes to optimizer. If extendBoxSize is true, 
         * than extension is done at this moment and new box sizes are taken into account.
         */
        if (horizontally) {
            int k = 0;
            for (int j = 0; j < vertSize; j++) {
                BoxInGrid lastBox = null;
                for (int i = 0; i < horSize; i++) {
                    if (grid[i][j] != null) {
                        //If next if is true, it means that we have met the box for the first time
                        if (grid[i][j].x == i && grid[i][j].y == j) {

                            optimizer.setVariable(k, grid[i][j].x + (double)grid[i][j].nH/2 -0.5);
                            grid[i][j].posInOpt = k;
                            optimizer.addQuadraticConstantDifference(k, grid[i][j].x + (double)grid[i][j].nH/2 - 0.5, EPS);
                            if (extendBoxSize) {
                                grid[i][j].nH = getNHor(grid[i][j].box);
                            }
                            if (grid[i][j].gridCol != -1) {
                                /* new comment
                                 * If box is in i-th column than it's first possible column place in grid is columns[i],
                                 * but last place is in columns[i+1]-1. That's why in first line distance is smeller by one
                                 * than in second line.
                                 */
                                optimizer.addInequality(firstGridIndex + grid[i][j].gridCol, grid[i][j].posInOpt,
                                        EPS + extraSpaceCoef * ((double)grid[i][j].nH / 2 - 0.5));
                                optimizer.addInequality(grid[i][j].posInOpt, firstGridIndex + grid[i][j].gridCol + 1,
                                        EPS + extraSpaceCoef * ((double)grid[i][j].nH / 2 + 0.5));
                            }
                            k++;

                        }

                        if (lastBox != null && lastBox != grid[i][j]) { //if a new (not first) box is met in a row
                            optimizer.addInequality(lastBox.posInOpt, grid[i][j].posInOpt,
                                    EPS + extraSpaceCoef * ((double)grid[i][j].nH / 2 + (double)lastBox.nH / 2));
                        }

                        lastBox = grid[i][j];
                    }
                }
            }


        } else { //vertically
            int k = 0;
            for (int i = 0; i < horSize; i++) {
                BoxInGrid lastBox = null;
                for (int j = 0; j < vertSize; j++) {
                    if (grid[i][j] != null) {
                        //If next if is true, it means that we have met the box for the first time
                        if (grid[i][j].x == i && grid[i][j].y == j) {

                            optimizer.setVariable(k, grid[i][j].y + (double)grid[i][j].nV/2 -0.5);
                            grid[i][j].posInOpt = k;
                            optimizer.addQuadraticConstantDifference(k, grid[i][j].y + (double)grid[i][j].nV/2 - 0.5, EPS);
 
                            if (extendBoxSize) {
                                grid[i][j].nV = getNVert(grid[i][j].box);
                            }
                            if (grid[i][j].gridRow != -1) {
                                /* new comment
                                 * If box is in i-th column than it's first possible column place in grid is columns[i],
                                 * but last place is in columns[i+1]-1. That's why in first line distance is smeller by one
                                 * than in second line.
                                 */
                                optimizer.addInequality(firstGridIndex + grid[i][j].gridRow, grid[i][j].posInOpt,
                                        EPS + extraSpaceCoef * ((double)grid[i][j].nV / 2 - 0.5));
                                optimizer.addInequality(grid[i][j].posInOpt, firstGridIndex + grid[i][j].gridRow + 1,
                                        EPS + extraSpaceCoef * ((double)grid[i][j].nV / 2 + 0.5));
                            }
                            k++;
                        }
                        if (lastBox != null && lastBox != grid[i][j]) { //if a new (not first) box is met in a column
                            optimizer.addInequality(lastBox.posInOpt, grid[i][j].posInOpt,
                                    EPS + extraSpaceCoef * ((double)grid[i][j].nV / 2 + (double)lastBox.nV / 2));
                        }

                        lastBox = grid[i][j];
                    }
                }
            }

        }


        int k = boxes.length;
        for (BoxInGrid boxIG1 : boxes) {
            for (Line line : boxIG1.lines) {
                /*
                 * ExtendBoxSize is done only once. 
                 * In this moment there is a need to calculate position of lines
                 * that are connected to nested boxes. 
                 */
                if (extendBoxSize) {
                    if (!line.startIsOriginal) {
                        if (horizontally) {
                            line.startX = Math.max(0,
                                    Math.min(boxIG1.nH - 1, (int) Math.round(line.realStartX / gridSize)));
                        } else {
                            line.startY = Math.max(0,
                                    Math.min(boxIG1.nV - 1, (int) Math.round(line.realStartY / gridSize)));
                        }
                    }
                    if (!line.endIsOriginal) {
                        if (horizontally) {
                            line.endX = Math.max(0,
                                    Math.min(line.boxIG.nH - 1, (int) Math.round(line.realEndX / gridSize)));
                        } else {
                            line.endY = Math.max(0,
                                    Math.min(line.boxIG.nV - 1, (int) Math.round(line.realEndY / gridSize)));
                        }
                    }
                }

                if (boxIG1.posInOpt < line.boxIG.posInOpt) { //it makes them not to copy connections
                    int firstElement, secondElement;
                    /*
                     * if line is connected to nested box than new variable is added to optimizer. 
                     That variable represents place of the line start and is moving together with boxes center.
                     */
                    if (!line.startIsOriginal) {
                        if (horizontally) {
                            optimizer.setVariable(k, boxIG1.x + line.startX);
                            optimizer.addEquality(boxIG1.posInOpt, k, line.startX);
                        } else {
                            optimizer.setVariable(k, boxIG1.y + line.startY);
                            optimizer.addEquality(boxIG1.posInOpt, k, line.startY);
                        }
                        firstElement = k;
                        k++;
                    } else {
                        //if line is connected directly to BoxIG1 than no new variables are added.
                        firstElement = boxIG1.posInOpt;
                    }

                    /*
                     * Similar to previous if
                     */
                    if (!line.endIsOriginal) {
                        if (horizontally) {
                            optimizer.setVariable(k, line.boxIG.x + line.endX);
                            optimizer.addEquality(line.boxIG.posInOpt, k, line.endX);
                        } else {
                            optimizer.setVariable(k, line.boxIG.y + line.endY);
                            optimizer.addEquality(line.boxIG.posInOpt, k, line.endY);
                        }
                        secondElement = k;
                        k++;
                    } else {
                        secondElement = line.boxIG.posInOpt;
                    }
                    /*
                     * This is added to minimize distance between line start and end elements
                     */
                    optimizer.addQuadraticDifference(firstElement, secondElement, 1);
                }
            }
        }

        double[] newValues = optimizer.performOptimization();

        /*
         * Removing boxes from grid. removeBox function also removes lines from lineGrid
         */
        if (!extendBoxSize) {
            for (BoxInGrid boxIG : boxes) {
                removeBox(boxIG);
            }
        } else {
            for (BoxInGrid boxIG : boxes) {
                int tmpNH = boxIG.nH;
                int tmpNV = boxIG.nV;
                /*
                 * Before removing box must have the same size as it have in grid
                 */
                if (horizontally) {
                    boxIG.nH = 1;
                } else {
                    boxIG.nV = 1;
                }
                removeBox(boxIG);
                /*
                 * Puting back new sizes
                 */
                boxIG.nH = tmpNH;
                boxIG.nV = tmpNV;
            }
        }

        /*
         * Assining newly calculated x or y values
         */
        for (BoxInGrid boxIG : boxes) {
            if (horizontally) {
                boxIG.x = (int) Math.round(newValues[boxIG.posInOpt] - (double)boxIG.nH/2 + 0.5);
            } else {
                boxIG.y = (int) Math.round(newValues[boxIG.posInOpt] - (double)boxIG.nV/2 + 0.5);
            }
        }

        /*
         * Assigning newly calculated column or row values
         */
        if (gridExist) {
            if (horizontally) {
                for (int i = firstGridIndex; i < firstGridIndex + columns.length; i++) {
                    columns[i - firstGridIndex] = (int) Math.round(newValues[i]);
                    //System.out.print(columns[i-firstGridIndex]+" ");
                }
            } else {
                for (int i = firstGridIndex; i < firstGridIndex + rows.length; i++) {
                    rows[i - firstGridIndex] = (int) Math.round(newValues[i]);
                    //System.out.print(rows[i-firstGridIndex]+" ");
                }
            }
        }

        putAllBoxesToGrid();


    }

    /**
     * Sets xd and yd coordinates to coordinates obtained from BoxInGrid x and y
     * fields, starting from point (x,y).
     *
     * @param x First coordinate of starting point.
     * @param y Second coordinate of starting point.
     */
    void setBoxCoordinates(int x, int y) {
        final double extraSpace=15;
        for (BoxInGrid boxIG : boxes) {
            boxIG.xd = (boxIG.x + (double)boxIG.nH/2 - 0.5) * (gridSize + extraSpace) + x;
            boxIG.yd = (boxIG.y + (double)boxIG.nV/2 - 0.5) * (gridSize + extraSpace) + y;
        }
    }

    /**
     * Return an obstacle graph for subgraph. Works with boxIG.xd and boxIG.yd
     * values. Assumes that all boxes have size gridSize*gridSize.
     *
     * @param horizontal True if horizontal obstacle graph is needed. False if
     * vertical.
     * @return Pairs of segments that are connected in obstacle graph.
     */
    ArrayList<Pair<SimpleOrthogonalSegment, SimpleOrthogonalSegment>> getObstacleGraph(boolean horizontal) {
        ArrayList<SimpleOrthogonalSegment> segments = new ArrayList<>();
        if (horizontal) {
            for (BoxInGrid boxIG : boxes) {
                SimpleOrthogonalSegment segment = new SimpleOrthogonalSegment(boxIG.xd,
                        boxIG.yd - gridSize / 2, boxIG.yd + gridSize / 2);
                segment.boxIG = boxIG;
                segments.add(segment);
                
            }

        } else { //vertical

            for (BoxInGrid boxIG : boxes) {
                SimpleOrthogonalSegment segment = new SimpleOrthogonalSegment(boxIG.yd,
                        boxIG.xd - gridSize / 2, boxIG.xd + gridSize / 2);
                segment.boxIG = boxIG;
                segments.add(segment);
       
            }
            
        }

        return ObstacleGraph.findObstacleGraph(segments);
    }

    /**
     * Normalizes boxes before they are putted in grid. Uses gridSize min
     * distance between boxes. Works with their xd, yd fields.
     *
     * @param horizontally True if horizontal normalization is needed. False if
     * vertical.
     */
    void firstNormalize(boolean horizontally) {
        ArrayList<Pair<SimpleOrthogonalSegment, SimpleOrthogonalSegment>> conPairs = getObstacleGraph(horizontally);
        QuadraticOptimizer optimizer;
        /*
         * EPS is needed to ensure that 2 boxes will never be in one grid point after normalization.
         * It must be biggest than epsilon in QuadraticOptimizer which is 0.001 by default.
         */
        final double EPS = 0.002;

        Integer colCount = 1, rowCount = 1;

        if (horizontally) {
            if (gridExist) {
                colCount = constr.getColumnCount();
                if (colCount == null) {
                    colCount = 1;
                }
            }
            optimizer = new QuadraticOptimizer(boxes.length + colCount +1);
            columnsd = new double[colCount +1];

        } else {
            if (gridExist) {
                rowCount = constr.getRowCount();
                if (rowCount == null) {
                    rowCount = 1;
                }
            }
            optimizer = new QuadraticOptimizer(boxes.length + rowCount + 1);
            rowsd = new double[rowCount + 1];
        }

        int k = 0;

        //putting boxes to optimizer
        if (horizontally) {
            for (BoxInGrid boxIG : boxes) {
                optimizer.setVariable(k, boxIG.xd);
                boxIG.posInOpt = k;
                optimizer.addQuadraticConstantDifference(k, boxIG.xd, EPS);
                k++;
            }
        } else {
            for (BoxInGrid boxIG : boxes) {
                optimizer.setVariable(k, boxIG.yd);
                boxIG.posInOpt = k;
                optimizer.addQuadraticConstantDifference(k, boxIG.yd, EPS);
                k++;
            }
        }


        if (gridExist) {
            //setting grid variables
            if (horizontally) {
                optimizer.setVariable(k, constr.getColumnLeft(1));
                optimizer.addQuadraticConstantDifference(k, constr.getColumnLeft(1), EPS);
                for (int i = k+1; i < k+1 + colCount; i++) {
                    optimizer.setVariable(i, constr.getColumnRight(i - k));
                    optimizer.addQuadraticConstantDifference(i, constr.getColumnRight(i - k), EPS);
                }
                
                
                for(int i = k; i < k + colCount; i++){
                    optimizer.addInequality(i, i+1, EPS);
                    optimizer.addQuadraticDifference(i, i+1, 1);
                }
                
            } else {
                optimizer.setVariable(k, constr.getRowBottom(1));
                optimizer.addQuadraticConstantDifference(k, constr.getRowBottom(1), EPS);
                for (int i = k+1; i < k+1 + rowCount; i++) {
                    optimizer.setVariable(i, constr.getRowBottom(i - k));
                    optimizer.addQuadraticConstantDifference(i, constr.getRowBottom(i - k), EPS);
                }
                
                for(int i = k; i < k + rowCount; i++){
                    optimizer.addInequality(i, i+1, EPS);
                    optimizer.addQuadraticDifference(i, i+1, 1);
                }
                
            }
        }

        //asigning min distance beetwen boxes that are connected in ObstacleGraph
        for (Pair<SimpleOrthogonalSegment, SimpleOrthogonalSegment> pair : conPairs) {
            SimpleOrthogonalSegment s1 = pair.getFirst();
            SimpleOrthogonalSegment s2 = pair.getSecond();
            /*
             * In next line coeficient in last argument represent how much boxes will be close to each other.
             */
            
//            System.out.println("gridSize in firstNormalize="+gridSize);
            if(!startBFS && !startRandom){
                if(horizontally){
                    optimizer.addInequality(s1.boxIG.posInOpt, s2.boxIG.posInOpt, EPS + 5 * gridSize);
                }else{
                    optimizer.addInequality(s1.boxIG.posInOpt, s2.boxIG.posInOpt, EPS + 3 * gridSize);
                }
            }else{
                if(horizontally){
                    optimizer.addInequality(s1.boxIG.posInOpt, s2.boxIG.posInOpt, EPS + 3 * gridSize);
                }else{
                    optimizer.addInequality(s1.boxIG.posInOpt, s2.boxIG.posInOpt, EPS + 3 * gridSize);
                }
            }
        }

        //adding ineqaulities so boxes can't cross grid lines
        
        if (gridExist) {
            for (BoxInGrid boxIG : boxes) {
                if (horizontally) {
                    Integer col = constr.getColumn(boxIG.box);
                    if (col != null) {
                        boxIG.gridCol = col - 1;
                        optimizer.addInequality(boxes.length + boxIG.gridCol, boxIG.posInOpt, EPS+gridSize);
                        optimizer.addInequality(boxIG.posInOpt, boxes.length + boxIG.gridCol+1, EPS + 2*gridSize);
                    }
                } else {
                    Integer row = constr.getRow(boxIG.box);
                    if (row != null) {
                        boxIG.gridRow = row - 1;
                        optimizer.addInequality(boxes.length + boxIG.gridRow, boxIG.posInOpt, EPS+gridSize);
                        optimizer.addInequality(boxIG.posInOpt, boxes.length + boxIG.gridRow+1, EPS + 2*gridSize);
                    }
                }

            }
        }
        

        //adds quadratic difference beetwen all connenced boxes
        for (BoxInGrid boxIG1 : boxes) {
            for (Line line : boxIG1.lines) {
                if (boxIG1.posInOpt < line.boxIG.posInOpt) { //this if prevents coping connections
                    optimizer.addQuadraticDifference(boxIG1.posInOpt, line.boxIG.posInOpt, 1);
                }
            }
        }
        double[] newValues = optimizer.performOptimization();

        //Changes x or y coordinate to coordinate calculated in optimization
        for (BoxInGrid boxIG : boxes) {
            if (horizontally) {
                boxIG.xd = newValues[boxIG.posInOpt];
            } else { //vertically
                boxIG.yd = newValues[boxIG.posInOpt];
            }
        }

        //Assigns newly calculated values of grid columns or rows
        //if (gridExist) {
            if (horizontally) {
                for (int i = boxes.length; i < boxes.length + colCount + 1; i++) {
                    columnsd[i-boxes.length] = newValues[i];
                }
            } else {
                for (int i = boxes.length; i < boxes.length + rowCount + 1; i++) {
                    rowsd[i-boxes.length] = newValues[i];
                }
            }
        //}
    }
    
    /**
     * Creates and adds Line objects to BoxInGrid objects.
     */
    void addLinesToBoxesInGrid() {
        List<LayoutLine> allLines = buildInnerLines();

        for (LayoutLine line : allLines) {
            Container boxA = line.startElement;
            Container boxB = line.endElement;

            BoxInGrid boxIGA = map.get(boxA);
            BoxInGrid boxIGB = map.get(boxB);

            Container startBox = (Container) line.originalLine.getStart();
            Container endBox = (Container) line.originalLine.getEnd();

            boolean AIsOriginal = (startBox == boxA);
            boolean BIsOriginal = (endBox == boxB);

            Line lineForA = new Line(boxIGB, AIsOriginal, BIsOriginal);
            Line lineForB = new Line(boxIGA, BIsOriginal, AIsOriginal);


            if (!AIsOriginal) {  
                lineForA.setRealStartPoint(startBox.getCenterX() - boxA.left,
                        startBox.getCenterY() - boxA.top);
                lineForB.setRealEndPoint(startBox.getCenterX() - boxA.left,
                        startBox.getCenterY() - boxA.top);
            }

            if (!BIsOriginal) {
                lineForB.setRealStartPoint(endBox.getCenterX() - boxB.left,
                        endBox.getCenterY() - boxB.top);
                lineForA.setRealEndPoint(endBox.getCenterX() - boxB.left,
                        endBox.getCenterY() - boxB.top);
            }

            boxIGA.lines.add(lineForA);
            boxIGB.lines.add(lineForB);

        }
    }
    
    /**
     * Function get's all line length. Created for testing.
     */
    double getAverageLineLength(){
        double boxSizeSum=0;
        double length=0;
        int lineCount=0;
        //double crossings=0;
        for(BoxInGrid boxIG: boxes){
            boxSizeSum+=boxIG.nH+boxIG.nV;
            length+=connectedLineLength(boxIG);
            lineCount+=boxIG.lines.size();
            //crossings+=lineGrid[boxIG.x][boxIG.y]*crCoef;
        }
        length=length/2;
        lineCount/=2;
        
        boxSizeSum/=2;
        
        double averageBoxSize=boxSizeSum/boxes.length;
        //System.out.println(" Sum length= "+(length+crossings)+" Sum length with respect to boxes size= "+(length+crossings)/averageBoxSize);
        return length/lineCount;
    }
    
    
    /**
     * Constructor.
     * @param part The part of the diagram that should be laid out.
     * @param baseLayouter The algorithm performing the layout of the nesting hierarchy.
     */
    UniversalLayoutAlgorithm(DiagramPart part, LayoutAlgorithm baseLayouter) {
        super(part, baseLayouter);
    }

    /**
     * Function that runs layout algorithm.
     * @param pass Number of iteration of this function.
     */
    @Override
    protected void layoutPass(int pass) {
        if (pass != 1) {
            return;
        }
            random = new Random();
            random.setSeed(120141);
            runUniversalLayout(part.enclosingContainer);
    }

    /**
     * Main function that layouts the graph universally inside a given container.
     * @param container Container where boxes will be arranged.
     */
    void runUniversalLayout(Container container) {
//        if (!part.containers.contains(container)) { //if this container is empty do nothing and stop recurtion
//            return;
//        }

       
        
        ArrayList<AbstractContainer> containers = part.enclosingContainer.getAbstractContainers(false);
        //ArrayList<AbstractContainer> containers = container.getRectangles(); //bez grupam
        //container.getAbstractContainers(false); //ar grupam

        //making array with boxes from all boxes
        Rectangle2D.Double rectangle = new Rectangle2D.Double();
        double hSum = 0;
        double vSum = 0;
        int count = 0;
        for (AbstractContainer box : containers) {
            hSum += box.right - box.left;
            vSum += box.bottom - box.top;
            count++;
        }

        double height, width;
        if (count > 0) {
            height = vSum / count;
            width = hSum / count;
        } else {
            height = 100;
            width = 100;
        }
        boxes = new BoxInGrid[containers.size()];
        map = new LinkedHashMap<>();
        int ii = 0;
        for (AbstractContainer box : containers) {
            boxes[ii] = new BoxInGrid(box);
            boxes[ii].boxesPlace=ii;
            //rectangle.setRect(box.getCenterX() - width / 2, box.getCenterY() - height / 2, width, height);
            //box.setBounds(rectangle);
            map.put(box, boxes[ii]);
            ii++;
        }

        /*
         * First SpringLayoutAlgorithm is run to set box places.
         */
        if(!startRandom && !startBFS){
            SpringLayoutAlgorithm sp = new SpringLayoutAlgorithm(part, baseLayouter);
//!!!!! Maybe it is not needed. Look in layout algorithm.
            for (int i = 1; i <= 3; i++) {
                sp.layoutPass(i);
            }
        }else{
            for(AbstractContainer con: containers){
                con.setCenter(new Point2D.Double( 5*width*random.nextDouble()*Math.sqrt(containers.size()) ,
                                                    5*height*random.nextDouble()*Math.sqrt(containers.size()) ) );
            }
        }
         



        for (BoxInGrid boxIG : boxes) {
            boxIG.xd = boxIG.box.getCenterX();
            boxIG.yd = boxIG.box.getCenterY();
            

        }

        addLinesToBoxesInGrid();

        LayoutConstraints abstractConstr = part.enclosingContainer.getLayoutConstraints();
        if (abstractConstr.getType() == LayoutConstraints.ConstraintType.GRID) {
            constr = (GridLayoutConstraints) abstractConstr;
            gridExist = true;
        } else {
            gridExist = false;
        }


        Arrays.sort(boxes, new Comparator<BoxInGrid>() {
            @Override
            public int compare(BoxInGrid boxIG1, BoxInGrid boxIG2) {
                return (int) (Math.round(boxIG2.hor * boxIG2.vert - boxIG1.hor * boxIG1.vert));
            }
        });
        

        //this for loop probably is needed only if BFS is launched
        for (int i = 0; i < boxes.length; i++) {
            boxes[i].boxesPlace=i;
        }

        setBoxesToGrid(false);
        improveBoxPlaces();


        setBoxCoordinates(0, 0);
        //compress();

        for (BoxInGrid boxIG : boxes) {
            moveAbstrContWithChildren(boxIG.box, boxIG.xd, boxIG.yd);
        }
        

    }
    
    void printGrid(){
        System.out.println("grid:");
        for(int j=0; j<vertSize; j++){
            for(int i=0; i<horSize; i++){
                if(grid[i][j]!=null){
                    System.out.print("1 ");
                }else{
                    System.out.print("0 ");
                }
            }
            System.out.println();
        }
    }  
}
