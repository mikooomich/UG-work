package QueuesLinkedListInterfaceSorting.PartB;


public class SortImpl<E extends Comparable<E>> extends LnkList<E> implements SortableList<E>  {
    /**
     * This class is an implementation for a linked-list list, with sorting.
     *
     * This class will reuse the NoItemException class, Node class, and
     * List interface from Collections package provided from lecture.
     * This class will reuse part A
     *
     * @author Michael Zhou
     * @course COSC 1P03
     * @assignment #5 (Part B)
     * @version 1.0
     * @date 2022-04-07
     */


    int numComparisons;

    public SortImpl() {
        length = 0;
    }



    /**
     * Performs a bubble sort on a list
     *
     * @return the time (comparisons) taken to do the sort
     */
    @Override
    public int Sort() {
        numComparisons = 0;
        // assuming 2 elements or greater because one element or no elements is already in order
        if (length() >= 2) {

            for (int i = length() ; i >= 1; i--) {

                cursor = theList.next; // always start at front
                cursorTail = theList;
                boolean noSwaps = true;


                while (cursor != null) {
                    numComparisons +=1;

                    if (cursor.item.compareTo(cursorTail.item) <= -1 ) {
                        // swap items
                        E placeholder = cursor.item;
                        cursor.item = cursorTail.item;
                        cursorTail.item = placeholder;
                        noSwaps = false;
                    }

                    advance();
                }

                if (noSwaps) { // if no swaps take place, list is in order
                    return numComparisons;
                }
            }
        }

        return numComparisons;
    }

}
