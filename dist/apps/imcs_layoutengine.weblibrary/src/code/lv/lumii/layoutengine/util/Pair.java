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

package lv.lumii.layoutengine.util;

import java.util.Objects;

/**
 * A class for storing an ordered pair of objects.
 *
 * @param <T1> the type of the first element
 * @param <T2> the type of the second element
 * @author Evgeny
 */
public class Pair<T1 extends Comparable<? super T1>, T2 extends Comparable<? super T2>> implements Comparable<Pair<T1, T2>> {

    //<editor-fold defaultstate="collapsed" desc="attributes">
    /**
     * The first element of the pair.
     */
    private T1 first;
    /**
     * The second element of the pair.
     */
    private T2 second;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="constructors">
    /**
     * Creates a new Pair.
     *
     * @param first the first member of the pair
     * @param second the second member of the pair
     */
    public Pair(T1 first, T2 second) {
        this.first = first;
        this.second = second;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="accessors">
    /**
     * Returns the first element of the pair.
     *
     * @return the first element of the pair.
     */
    public T1 getFirst() {
        return first;
    }

    /**
     * Returns the second element of the pair.
     *
     * @return the second element of the pair.
     */
    public T2 getSecond() {
        return second;
    }
    //</editor-fold>

    /**
     * Compares the pair to another pair with priority given to the first object.
     *
     * @param o the pair to compare to
     * @return the result of the comparison.
     */
    @Override
    public int compareTo(Pair<T1, T2> o) {
        int cmp = ((Comparable<? super T1>) first).compareTo(o.first);
        return cmp == 0 ? ((Comparable<? super T2>) second).compareTo(o.second) : cmp;
    }

    /**
     * Returns a string representation of the pair as the string representations of the pair`s
     * elements separated by a comma in parenthesis.
     *
     * @return a string representation of the pair.
     */
    @Override
    public String toString() {
        return "(" + first.toString() + ", " + second.toString() + ")";
    }

    /**
     * Returns whether this pair is equal to the given object. Always false if the given object is
     * not a {@code Pair}. Pairs are compared using the {@code equals} method to compare both
     * members of each pair.
     *
     * @param obj the object to compare this pair to
     * @return whether the given object is a pair equal to this one
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Pair) {

            Pair pair = (Pair) obj;
            if (pair.getFirst().getClass() == first.getClass() && pair.getSecond().getClass() == second.getClass()) {
                return first.equals(pair.getFirst()) && second.equals(pair.getSecond());
            }
        }
        return false;
    }

    /**
     * Hashes this pair using the hashes of both its elements.
     *
     * @return a hash corresponding to this pair.
     */
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + Objects.hashCode(this.first);
        hash = 89 * hash + Objects.hashCode(this.second);
        return hash;
    }
}
