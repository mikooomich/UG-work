package QueuesLinkedListInterfaceSorting.Collections;
import java.util.*;


/** This class represents an interator on a ConList as required by the interface
  * Iterable<E> of the List interface.
  * 
  * @see  List
  * @see  Keyed
  * 
  * @author  D. Hughes
  * 
  * @version  1.0 (Mar. 2008).                                                   */

class ConListIterator < E extends Comparable<E> > implements Iterator<E> {
    
    
    int         cursor;  // the cursor that iterates through the list
     ConList<E>  list;    // the list being iterated over
    
    
    /** This constructor constructs an iterator on the specified ConList.
      * 
      * @param  l  the list to be iterated over.                                 */
    
    ConListIterator ( ConList<E> l ) {
        
        list = l;
        cursor = 0;
        
    };  // constructor
    
    
    /** This method returns true if there are more items in the list.
      * 
      * @return  boolean  more items on the list.                                */
    
    public boolean hasNext ( ) {  // from Iterator
        
        return cursor < list.length;
        
    };  // hasNext
    
    
    /** This method returns the next item in the list.
      * 
      * @retuen  E  the next item on the list.                                   */
    
    public E next ( ) {  // from Iterator
        
        E  i;
        
        if ( cursor >= list.length ) {
            throw new NoSuchElementException();
        }
        else {
            i = list.items[cursor];
            cursor = cursor + 1;
            return i;
        }
        
    };  // next
    
    
    /** Removal is not supported so this method throws an
      * UnsupportedOperationException.
      * 
      * @exception  UnsupportedOperationException  remove is not supported.      */
    
    public void remove ( ) {  // from Iterator
        
        throw new UnsupportedOperationException();
        
    };  // remove
    
    
}  // LnkListIterator