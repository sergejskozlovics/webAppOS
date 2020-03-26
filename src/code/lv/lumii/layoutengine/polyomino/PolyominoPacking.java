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

package lv.lumii.layoutengine.polyomino;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

/**
 * The polyomino packing algorithm.
 */
public class PolyominoPacking {

    /**
     * This method performs polyomino packing.
     */
    public void pack(Polyomino[] pm) {

        pmSorted = new Polyomino[pm.length];
        for (int i=0; i<pm.length; i++)
          pmSorted[i] = pm[i];

        // make the initial grid
        this.makeGrid(100, 100, 0);

        // make the random permutation of polyomino cells and
        // calculate the bounding rectangles.

        this.random = new Random(1);

        for (int k = 0; k < pm.length; k++) {
            this.randomizeMino(pm[k]);
        }

        // order the polyominoes in incresing sizepolyominoPackingpolyominoPacking

        Arrays.sort(pmSorted, new Comparator<Polyomino>() {
            @Override
            public int compare(Polyomino o1, Polyomino o2) {
                return Integer.compare(o2.perimeter(), o1.perimeter());
            }
        });

        // place one by one starting from the largest

        for (this.curmino = 0; this.curmino < pm.length; this.curmino++) {
            this.putMino(pmSorted[this.curmino]);
        }

        // center to 0

        for (int k = 0; k < pm.length; k++) {
            pm[k].x -= this.gcx;
            pm[k].y -= this.gcy;
        }
    }

    /**
     * This creates the grid of given dimensions and fills it with the already
     * placed polyominoes.
     *
     */
    private void makeGrid(int dimx, int dimy, int mN) {
        int i;

        //allocate the grid
        this.grid = new byte[dimy][];

        for (i = 0; i < dimy; i++) {
            this.grid[i] = new byte[dimx];
        }

        int dx = dimx / 2 - this.gcx;
        int dy = dimy / 2 - this.gcy;

        this.gcx = dimx / 2;
        this.gcy = dimy / 2;
        this.sizeX = dimx;
        this.sizeY = dimy;

        // mark the positions occupied with the already placed
        // polyominoes.

        for (i = 0; i < mN; i++) {
            Polyomino p = pmSorted[i];
            p.x += dx;
            p.y += dy;

            for (int k = 0; k < p.coord.size(); k++) {
                int xx = p.coord.get(k).getX() + p.x;
                int yy = p.coord.get(k).getY() + p.y;

                this.grid[yy][xx] = 1;
            }
        }
    }

    /**
     * This method ckecks whether p can be placed in (x,y).
     */
    private boolean isFreePlace(int x, int y, Polyomino p) {
        for (int k = 0; k < p.coord.size(); k++) {
            int xx = p.coord.get(k).getX() + x;
            int yy = p.coord.get(k).getY() + y;

            // return false if the polyomino goes outside the grid

            if (xx < 0 || yy < 0 || xx >= this.sizeX || yy >= this.sizeY) {
                return false;
            }

            // or the position is occupied

            if (this.grid[yy][xx] != 0) {
                return false;
            }
        }

        // remember the posiotion
        p.x = x;
        p.y = y;

        return true;
    }

    /**
     * This tries to find a free place in the grid. The function returns true if
     * the placement is secceessful.
     *
     * @return Description of the Returned Value
     */
    private boolean tryPlacing(Polyomino p) {
        int cx = this.gcx - (p.bounds.x2 + p.bounds.x1) / 2;
        int cy = this.gcy - (p.bounds.y2 + p.bounds.y1) / 2;

        // see if the center point is not occupied

        if (this.isFreePlace(cx, cy, p)) {
            return true;
        }

        // try placing in the increasing distance from the center

        for (int d = 1; d < this.sizeX / 2; d++) {
            for (int i = -d; i < d; i++) {
                int i1 = (i + d + 1) / 2;

                if ((i & 1) != 1) {
                    i1 = -i1;
                }

                if (this.isFreePlace(-d + cx, -i1 + cy, p)) {
                    return true;
                }

                if (this.isFreePlace(d + cx, i1 + cy, p)) {
                    return true;
                }

                if (this.isFreePlace(cx - i1, d + cy, p)) {
                    return true;
                }

                if (this.isFreePlace(i1 + cx, -d + cy, p)) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * This method places the given polyomino. The grid is enlarged if
     * necessary.
     *
     */
    private void putMino(Polyomino p) {
        // if the polyomino cannot be placed in the current grid,
        // enlarge it.

        while (!this.tryPlacing(p)) {
            this.sizeX += 20;
            this.sizeY += 20;
            this.makeGrid(this.sizeX, this.sizeY, this.curmino);
        }

        // mark the positions occupied

        for (int k = 0; k < p.coord.size(); k++) {
            int xx = p.coord.get(k).getX() + p.x;
            int yy = p.coord.get(k).getY() + p.y;

            this.grid[yy][xx] = 1;
        }

    }

    /**
     * This method makes a random permutation of polyomino cells and calculates
     * the bounding rectangles of the polyominoes.
     *
     */
    private void randomizeMino(Polyomino p) {
        int i;

        // make the random permutation. Theoretically it speeds up the
        // algorithm a little.

        for (i = 0; i < p.coord.size(); i++) {
            int i1 = this.random.nextInt(p.coord.size() - i) + i;
            Polyomino.IntegerPoint tmp = p.coord.get(i);

            p.coord.set( i, p.coord.get(i1) );
            p.coord.set( i1, tmp);
        }

        //calculate the bounding rectangles

        Polyomino.IntegerRectangle rect = p.bounds;
        rect.x1 = Integer.MAX_VALUE;
        rect.y1 = Integer.MAX_VALUE;
        rect.x2 = 0;
        rect.y2 = 0;
        p.x = 0;
        p.y = 0;

        for (i = 0; i < p.coord.size(); i++) {
            if (p.coord.get(i).getX() < rect.x1) {
                rect.x1 = p.coord.get(i).getX();
            }
            if (p.coord.get(i).getY() < rect.y1) {
                rect.y1 = p.coord.get(i).getY();
            }
            if (p.coord.get(i).getX() > rect.x2) {
                rect.x2 = p.coord.get(i).getX();
            }
            if (p.coord.get(i).getY() > rect.y2) {
                rect.y2 = p.coord.get(i).getY();
            }
        }
    }
    //polyomines
    private Polyomino[] pmSorted;
    // occupied places
    private byte[][] grid;
    // center point of the grid
    private int gcx;
    private int gcy;
    // grid size
    private int sizeX;
    private int sizeY;
    // the number of already placed polyominoes
    private int curmino;
    //random generator
    private Random random;
}
