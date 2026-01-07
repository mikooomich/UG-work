package QueuesLinkedListInterfaceSorting.PartA;

import java.util.Iterator;


/**
 * This class does iterator magic for the LnkList.
 *
 * @course COSC 1P03
 * @assignment #5
 * @version 1.0
 * @date 2022-04-07
 */
public class LnkListIterator<E extends Comparable<E>> implements Iterator<E> {

    LnkList thisList;
    // assume create a copy of the cursors
    Node <E> cursor; // cursor
    Node <E> cursorTail; // trailing cursor

    public LnkListIterator(LnkList<E> list) {
        thisList = list;
        // Assuming iterating from beginning
        cursor = thisList.theList;
        cursorTail = null;
    }


    /**
     * Returns {@code true} if the iteration has more elements.
     * (In other words, returns {@code true} if {@link #next} would
     * return an element rather than throwing an exception.)
     *
     * @return {@code true} if the iteration has more elements
     */
    @Override
    public boolean hasNext() {

        if (cursor != null) {
            return true;
        }
        return false;
    }

    /**
     * Returns the next element in the iteration.
     *
     * @return the next element in the iteration
     * @throws NoSuchElementException if the iteration has no more elements
     */
    @Override
    public E next() {
        if (! hasNext()) {
            throw new NoSuchElementException("No more elements");
        }
        
        E item = cursor.item;
        cursorTail = cursor;
        cursor = cursor.next;
        return item;
    }

}
