package QueuesLinkedListInterfaceSorting.Collections;



import java.io.*;
import java.util.*;


/** This class represents an implementation of the List interface using contiguous
  * storage (array).
  *
  * @see  List
  * @see  Keyed
  * @see  NoSpaceException
  * @see  NoItemException
  *
  * @author  D. Hughes
  *
  * @version  1.0 (Mar. 2007)                                                    */

public class ConList <E extends Comparable<E>> implements List<E>, Serializable {
    
    
    E[]  items;   // the items in the list
    int  cursor;  // the list cursor
    int  length;  // the length of the list
    
    
    /** This constructor creates a new empty list capable of holding 100 items.  */
    
    public ConList ( ) {
        
        this(100);
        
    }; // constructor
    
    
    /** This constructor creates a new empty list capable of holding a specified
      * number of items.
      *
      * @param  size  the maximum size of the list.                              */
    
    public ConList ( int size ) {
        
        items = (E[]) new Comparable[size];
        cursor = 0;
        length = 0;
        
    }; // Constructor
    
    
    /** This method returns an interator that iterates through the list from the
      * front as requried by Iterable<E>.
      * 
      * @return  Iterator<E>  an iterator on the list.                           */

    public Iterator<E> iterator ( ) {
        
        return new ConListIterator<E>(this);
        
    };  // iterator

    
    /** This method adds an item to the list in front of the cursor. If the cursor
      * is off the end of the list, the insertion is at the end of the list. The
      * cursor references the item just added. ListOverflow occurs if there is no
      * room to add another item.
      *
      * @param  item  the item to be added.
      *
      * @exception  NoSpaceException  no more room to add items.                 */
    
    public void add ( E item ) {
        
        int  j;
        
        if ( length >= items.length ) {
            throw new NoSpaceException();
        }
        else {
            for ( j = length-1 ; j>=cursor ; j-- ) {
                items[j+1] = items[j];
            };
            items[cursor] = item;
            length = length + 1;
        };
        
    }; // add
    
    
    /** This method removes the item at the cursor from the list. The cursor
      * references the item following the one removed. A NoItemException occurs if
      * the cursor was off the end of the list.
      *
      * @result  E  the item removed.
      *
      * @exception  NoItemException  no current item (cursor off list).          */
    
    public E remove ( ) {
        
        E    i;
        int  j;
        
        if ( cursor >= length ) {
            throw new NoItemException();
        }
        else {
            i = items[cursor];
            for ( j=cursor+1 ; j<length ; j++ ) {
                items[j-1] = items[j];
            };
            length = length-1;
            items[length] = null;
            return i;
        }
        
    }; // remove
    
    
    /** This method returns the item at the cursor. A NoItemException occurs if the
      * cursor was off the end of the list.
      *
      * @result  E  the item at the cursor.
      *
      * @exception  NoItemException  no current item (cursor off list).          */
    
    public E get ( ) {
        
        if ( cursor >= length ) {
            throw new NoItemException();
        }
        else {
            return items[cursor];
        }
        
    }; // get
    
    
    /** This method returns true if the list has no items.
      *
      * @return  boolean  the list has no items.                                 */
    
    public boolean empty ( ) {
        
        return length == 0;
        
    }; // empty
    
    
    /** This method returns the number of items in the list.
      *
      * @result  int  the number of items in the list.                           */
    
    public int length ( ) {
        
        return length;
        
    }; // length
    
    
    /** This method moves the cursor to the first item on the list. If the list is
      * empty, the cursor will be off the end of the list.                       */
    
    public void toFront ( ) {
        
        cursor = 0;
        
    }; // toFront
    
    
    /** This method advances the cursor to the next item in the list. If the cursor
      * was off the list, it remains off the list.                               */
    
    public void advance( ) {
        
        if ( cursor < length ) {
            cursor = cursor + 1;
        };
        
    }; // advance
    
    
    /** This method advances the cursor to the next item, starting from the cursor,
      * that matches the specified key. If there is no such entry, the cursor is
      * moved off the end of the list.
      *
      * @param  key  the key for matching in the search.                         */
    
    public void find ( E element ) {
        
        while ( cursor < length && element.compareTo(items[cursor])!=0)  {
            cursor = cursor + 1;
        };
        
    }; // find
    
    
    /** This method indicates if the cursor is off the end of the list.
      *
      * @return  boolean  the cursor is off the end of the list.                 */
    
    public boolean offEnd ( ) {
        
        return cursor >= length;
        
    }; // offEnd
    
    
} // ConList
