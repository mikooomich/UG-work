package QueuesLinkedListInterfaceSorting.PartB;

import QueuesLinkedListInterfaceSorting.Collections.*;


/** This class serves as a wrapper class for the basic data type char so that it
  * may be used as a Keyed item.
  *
  * @see  List
  * @see  Keyed
  *
  * @author  D. Hughes
  *
  * @version  1.0 (Mar. 2008)                                                    */


class KeyedChar implements Comparable<KeyedChar> {
    
    char  theChar;  // the key (and data) of the item for the test
    
    
    /** The constructor wraps the char as a Keyed item.
      * 
      * @param  c  the character to be wrapped.                                  */
    
    KeyedChar ( char c ) {
        
        theChar = c;
        
    }; // constructor

    public int compareTo(KeyedChar C) {
        if (this.theChar == C.theChar) {
            return 0;
        } else if (this.theChar > C.theChar) {
            return 1;
        } else {
            return -1;
        }
    }

} // KeyedChar
