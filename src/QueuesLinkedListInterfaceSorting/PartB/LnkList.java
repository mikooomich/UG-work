package QueuesLinkedListInterfaceSorting.PartB;

import java.io.Serializable;
import java.util.Iterator;


import QueuesLinkedListInterfaceSorting.Collections.List;
import QueuesLinkedListInterfaceSorting.Collections.NoItemException;


/**
 * This class is an implementation for a linked-list list.
 *
 * This class will reuse the NoItemException class, Node class, and
 * List interface from Collections package provided from lecture.
 *
 * @course COSC 1P03
 * @assignment #5
 * @version 1.0
 * @date 2022-04-07
 */


public class LnkList< E extends Comparable<E>>  implements List<E>, Serializable{

    Node <E> theList;
    int length;
    Node <E> cursor;
    Node <E> cursorTail; // trailing cursor

    public LnkList() {
        length = 0;
    }



    /** This method adds an item to the list in front of the cursor.
     *  The adding will insert at the cursor, or front, depending
     *  on what is needed to match output with conlist.
     *  If cursor is off the end of the list, the insertion is at end of list.
     *
     * @param  item  the item to be added.
     */
    @Override
    public void add (E item) {

        if (empty()) {
            //insert very front, one element (onto the cursor)
            Node <E> nodeThingy = new Node<E>(item, null);
            theList = nodeThingy;
            cursor  = theList;
        }

        else {

            if (offEnd()) {
                // insert at end because cursor is offend, (in front of cursor)
                Node<E> nodeThingy = new Node<E>(item, null);
                toFront();

                while (cursor.next != null) {
                    advance();
                }
                cursor.next = nodeThingy;
            }
            else {

                if (cursorTail == null) {
                    // insert at very beginning, onto the cursor
                    Node<E> nodeThingy = new Node<E>(item, cursor);
                    theList = nodeThingy;
                    cursor = theList;
                }

                else {
                     if (cursor.next == null) {
                    // insertion at very end. (insert in front of the cursor)

                    Node<E> nodeThingy = new Node<E>(item, null);
                    cursor.next = nodeThingy;
                    }
                    else {
                        // insert in the middle of the list. This is the general case, where it is not one of the special cases.
                        Node<E> nodeThingy = new Node<E>(item, cursor);
                        cursorTail.next = nodeThingy;
                        cursor = nodeThingy;
                    }
                }
            }
        }
        length ++;
    };


    /** This method removes the item at the cursor from the list. The cursor
     * references the item following the one removed. A NoItemException occurs if
     * the cursor was off the end of the list.
     *
     * @return  E  the item removed.
     *
     * @exception  NoItemException  no current item (cursor off list).          */
    @Override
    public E remove () {

        if (offEnd()) {
            throw new NoItemException();
        }

        E thing = cursor.item;

        if (cursorTail == null) {
            // delete from front of list, cursor is assumed to be at the first element
            theList = cursor.next;
            cursor = cursor.next;
        }
        else {
            if (cursor.next != null) {
                // general case
                cursorTail.next = cursor.next;
                cursor = cursor.next;
            }
            else {
                // one item in the last
                cursor = null;
                cursorTail = null;
            }
        }

        length -= 1;
        return thing;
    }


    /** This method returns the item at the cursor.
     *
     * @return  E  the item at the cursor.
     *
     * @exception  NoItemException  no current item (cursor off list).          */
    @Override
    public E get () {
        if (offEnd()) {
            throw new QueuesLinkedListInterfaceSorting.Collections.NoItemException();
        }
        return cursor.item;
    };


    /** This method returns true if the list has no items.
     *
     * @return  boolean  the list has no items.                                 */
    @Override
    public boolean empty ( ) {
        if (theList == null) {
            return  true;
        }
        return false;
    };


    /** This method returns the number of items in the list.
     *
     * @result  int  the number of items in the list.                           */
    @Override
    public int length () {
        return length;
    };


    /** This method moves the cursor to the first item on the list. If the list is
     * empty, the cursor will be off the end of the list.                       */
    @Override
    public void toFront () {
        cursor = theList;
        cursorTail = null;
    };


    /** This method advances the cursor to the next item in the list. If the cursor
     * was off the list, it remains off the list.                               */
    @Override
    public void advance( ) {

        if (offEnd()) {}    // don't do anything if offend
        else {
            cursorTail = cursor;
            cursor = cursor.next;
        }
    };


    /** This method advances the cursor to the next item, starting from the cursor,
     * that matches the specified key. If there is no such entry, the cursor is
     * moved off the end of the list.
     *
     * @param  element the for matching in the search.                         */
    @Override
    public void find ( E element ) {
        if (offEnd()) {}
        else {
            /** This check below searches the current element cursor is on.
            I would remove this check and in the while loop swap
            advance() with the check, but "Exhaustive search for A"
            will not match up with ConList by one less "A" character.
             */
            if (element.compareTo(cursor.item) == 0 ) {
                return; // stop the cursor on the element
            }

            while (cursor.next != null) {
                advance();
                if (element.compareTo(cursor.item) == 0 ) {
                    return;   // stop the cursor on the element
                }
            }

            // not found, offend cursor
            cursor = null;
            cursorTail = null;
        }
    }


    /** This method indicates if the cursor is off the end of the list.
     *
     * @return  boolean  the cursor is off the end of the list.                 */
    @Override
    public boolean offEnd ( ) {
        if (cursor == null) {
            return true;
        }
        else {
            return  false;
        }
    };

    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    @Override
    public Iterator<E> iterator() {
        return new LnkListIterator<E>(this);
    }


}
