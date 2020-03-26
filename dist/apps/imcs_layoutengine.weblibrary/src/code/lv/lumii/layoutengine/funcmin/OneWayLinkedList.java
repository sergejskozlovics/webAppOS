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

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lv.lumii.layoutengine.funcmin;

import java.util.Objects;
import lv.lumii.layoutengine.funcmin.CycleReducer.Adj;

/**
 * A class that implements a (one-way) linked list.
Unlike STL, our implementation stores only one
Element per element.
Also, a move_to_front operation is implemented.
(In STL we need to erase and then push_front the element,
so data is copied and Elements to it may become invalid.
Our implementation doesn't copy data. move_to_front
operates only with Elements.)

Use TemporaryIterator instead of iterator.
(Each TemporaryIterator stores a Element to the previous
list element, so, after erasing or moving element after
that previous, TemporaryIterator
may point to another element. But all operations
with iterators are like in STL, i.e.,
 *begin() points to the first data item.
If you want a permanent
Element to element, use Element (e.g., &*it where it is
a TemporaryIterator) that points to the same
data item even when element was moved to front using
move_to_front operation.)

Author: Sergejs Kozlovics (in LaTeX: Sergejs Kozlovi\v{c}s)

Last modified: 31.08.2006. 16:15.

 * @author k
 */
public class OneWayLinkedList {

//#undef LINKED_LIST_SIZE
    // define, if you want a fast size() method
//#include <assert.h>
    static class Element {

        Element next;
        Adj data;
    };
    //typedef element_t* Element;
    Element front_ptr, back_ptr;
//#ifdef LINKED_LIST_SIZE
//  int size;
//#endif // LINKED_LIST_SIZE

    /**
     *  in fact, TemporaryIterator points to the previous element,
     *  so it can't be used as permanent iterator for accessing
     * the element
     */
    static class TemporaryIterator {

        Element prev_ptr;

        TemporaryIterator() {
            prev_ptr = null;
        }

        TemporaryIterator(Element p) {
            prev_ptr = p;
        }

        void inc() {
            // prefix ++operator
            prev_ptr = prev_ptr.next;
        }
//    inline TemporaryIterator operator++(int)
//      // postfix operator++
//    {
//      Element saved_ptr = prev_ptr;
//      prev_ptr = prev_ptr->next;
//      return saved_ptr;
//    }

        Adj value() {
            return prev_ptr.next.data;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 53 * hash + Objects.hashCode(this.prev_ptr);
            return hash;
        }

//	inline Adj operator->()
//    {
//      return &(operator*());
//    }
        @Override
        public boolean equals(Object b) {
            if (b instanceof TemporaryIterator) {
                return prev_ptr == ((TemporaryIterator) b).prev_ptr;
            }
            return false;
        }
//    
//    boolean operator!=(const TemporaryIterator &it)
//    {
//      return prev_ptr != (it.prev_ptr);
//    }
    }

    OneWayLinkedList() {
        front_ptr = new Element();
        back_ptr = front_ptr;

        // assert back_ptr.next == null;
//#ifdef LINKED_LIST_SIZE
//    size = 0;
//#endif // LINKED_LIST_SIZE
    }
//  OneWayLinkedList( OneWayLinkedList l)
//  {
//    front_ptr = null;
//    back_ptr = null;
//
////    assert(back_ptr->next == NULL);
////#ifdef LINKED_LIST_SIZE
////    size = 0;
////#endif // LINKED_LIST_SIZE
//	
//	Element p;
//	for (p = l.front_ptr; p!=NULL; p.next)
//		push_back(p->data);
//	/*
//	// We could use the following code that works fine
//	// with Borland C++ Builder 6.0 but fails with
//	// Microsoft Visual Studio 2003 because of
//	// const linked_list<Adj> type.
//	
//	linked_list<Adj>::TemporaryIterator it;
//    for (it=l.begin(); it!=l.end(); it++)
//      push_back(*it);
//	*/
//  }
//  ~linked_list()
//  {
//    clear();
//  }

    boolean empty() {
        return (front_ptr.next == null);
    }

    void push_front(Adj data) {
        Element p = new Element();
        p.data = data; // ? memcpy

        p.next = front_ptr.next;
        front_ptr.next = p;
        if (back_ptr == front_ptr) {
            back_ptr = p;
        }
//  #ifdef LINKED_LIST_SIZE
//    size++;
//  #endif // LINKED_LIST_SIZE
    }

    Adj front() {
        //if(front_ptr.next == null) return null;
        return front_ptr.next.data;
    }

    void pop_front() {
        front_ptr.next = front_ptr.next.next;
        if(front_ptr.next == null) {
            back_ptr=front_ptr;
        }
//    #ifdef LINKED_LIST_SIZE
//    size--;
//    #endif // LINKED_LIST_SIZE
    }

    void move_to_front(TemporaryIterator it) // returns an iterator pointing to the element that follows
    // the deleted element
    {
        Element p = it.prev_ptr.next;
        if (p == front_ptr.next) {
            it.prev_ptr = p;
            return;
        }
        it.prev_ptr.next = p.next;
        p.next = front_ptr.next;
        front_ptr.next = p;

        if ((p == back_ptr) && (p.next != null)) {
            // we've moved the last element and there are other elements
            // after that moved...
            back_ptr = it.prev_ptr;
            // back_ptr should point to the previous element because
            // the last element was moved
        }
    }

    TemporaryIterator push_back(Adj data) {
        Element p = new Element();
        p.data = data;
        p.next = null;

        Element old_back_ptr = back_ptr;

        back_ptr.next = p;
        back_ptr = p;
//    #ifdef LINKED_LIST_SIZE
//    size++;
//    #endif // LINKED_LIST_SIZE
        TemporaryIterator t = new TemporaryIterator(old_back_ptr);
        return t;
    }

    void erase(TemporaryIterator it) // returns an iterator pointing to the element that follows
    // the deleted element
    {
        Element tmp = it.prev_ptr.next.next;

        it.prev_ptr.next = tmp;
        if (tmp == null) {
            back_ptr = it.prev_ptr;
        }
//    #ifdef LINKED_LIST_SIZE
//    size--;
//    #endif // LINKED_LIST_SIZE
    }

    TemporaryIterator begin() {
        return new TemporaryIterator(front_ptr);
    }

    TemporaryIterator end() {
        return new TemporaryIterator(back_ptr);
    }

    void clear() {
        front_ptr = new Element();
        back_ptr = front_ptr;
    }

    int size() {
//    #ifdef LINKED_LIST_SIZE
//    return this->size;
//    #else
        int result = 0;
        for (TemporaryIterator it = begin();
                !it.equals(end());
                it.inc()) {
            result++;
        }
        return result;
//    #endif // LINKED_LIST_SIZE
    }

    void truncate(TemporaryIterator it) // erases all elements starting from *it to the end
    {
        back_ptr = it.prev_ptr;
        back_ptr.next = null;
    }
}
//#endif // LINKED_LIST_H
//
//}
