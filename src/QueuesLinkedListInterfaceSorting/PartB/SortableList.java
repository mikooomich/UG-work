package QueuesLinkedListInterfaceSorting.PartB;


import QueuesLinkedListInterfaceSorting.Collections.List;

/**
* This interface defines the additional sort function extension to the List interface
 * @see Collections.List
 *
 * @course COSC 1P03
 * @assignment #5 (Part B)
 * @version 1.0
 * @date 2022-04-07
*/


public interface SortableList <E extends Comparable<E>>  extends List<E>, Iterable<E> {
    /**
     * Performs a bubble sort given a list
     * @return int the time (number of comparisons) taken to do the sort
     */
    public int Sort();
}
