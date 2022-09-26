package QueuesLinkedListInterfaceSorting.Collections;

import QueuesLinkedListInterfaceSorting.PartA.LnkList;



/** This class is a test class for testing the List implementations: ConList and
  * LnkList. It performs a number of tests including add, remove, traversing,
  * iteratating, searching and tests of the exceptions.
  *
  * @see  List
  * @see  Keyed
  * @see  ConList
  * @see  LnkList
  *
  * @author  D. Hughes
  *
  * @version  1.0 (Mar. 2008)                                                    */

public class TestLists {

    
    
    /** The constructor uses the method testList to test the list implementations*/
    
    public TestLists ( ) {
        
        List<KeyedChar> l;   // a list
        System.out.println();
//        out = new ASCIIDisplayer(50,35);
        
        l = new ConList<KeyedChar>(100);
        System.out.println("Testing Contiguous List");
        System.out.println("\n\n");
        testList(l);
        System.out.println("\n\n");
        
      l = new LnkList<KeyedChar>();
        System.out.println("Testing Linked List");
//        out.();
        System.out.println("\n");
        testList(l);

//        out.close();
        
    }; // constructor
    
    
    /** This method does the actual test of a list.
      *
      * @param  l  a list to test                                                */
    
    private void testList ( List<KeyedChar> l ) {
        
        char   c;
        
        System.out.println("Initial list length: ");
        System.out.println(l.length());
        System.out.println("\n");
        System.out.println("\n");
        System.out.println("Loading list, sequential order...");
        System.out.println("\n");
        for ( c='A' ; c<='H' ; c++ ) {
            l.add(new KeyedChar(c));
            l.advance();
        };
        System.out.println("List loaded.");
        System.out.println("\n");
        System.out.println("List length: ");
        System.out.println(l.length());
        System.out.println("\n");
        System.out.println("Dumping list (sequential order)..");
        l.toFront();
        while ( ! l.offEnd() ) {
            System.out.println(l.remove().theChar);
        };
        System.out.println("\n");
        System.out.println("List length: ");
        System.out.println(l.length());
        System.out.println("\n");
        System.out.println("\n");
        System.out.println("Loading list, reverse order...");
        System.out.println("\n");
        for ( c='A' ; c<='H' ; c++ ) {
            l.add(new KeyedChar(c));
        };
        System.out.println("List loaded.");
        System.out.println("\n");
        System.out.println("List length: ");
        System.out.println(l.length());
        System.out.println("\n");
        System.out.println("Dumping list (reverse order) using iterator..");
        for ( KeyedChar d : l ) {
            System.out.println(d.theChar);
        };
        System.out.println("\n");
        System.out.println("List length: ");
        System.out.println(l.length());
        System.out.println("\n");
        System.out.println("\n");
        System.out.println("Searching List");
        System.out.println("\n");
        for ( c='B' ; c<='H' ; c++ ) {
            l.add(new KeyedChar('A'));
            l.advance();
            l.add(new KeyedChar(c));
            l.advance();
        };
        l.add(new KeyedChar('A'));
        System.out.println("List: ");
        for ( KeyedChar d : l ) {
            System.out.println(d.theChar);
        };
        System.out.println("\n");
        System.out.println("Search for B from front:");
        l.toFront();
        l.find(new KeyedChar('B'));
        if ( ! l.offEnd() ) {
            System.out.println(" found");
        }
        else {
            System.out.println(" not found");
        };
        System.out.println("\n");
        System.out.println("Search for B without advance:");
        l.find(new KeyedChar('B'));
        if ( ! l.offEnd() ) {
            System.out.println(" found");
        }
        else {
            System.out.println(" not found");
        };
        System.out.println("\n");
        System.out.println("Search for B with advance:");
        l.advance();
        l.find(new KeyedChar('B'));
        if ( ! l.offEnd() ) {
            System.out.println(" found");
        }
        else {
            System.out.println(" not found");
        };
        System.out.println("\n");
        System.out.println("Exhaustive search for A: ");
        l.toFront();
        while ( true ) {
            l.find(new KeyedChar('A'));
            if ( l.offEnd() ) break;
            System.out.println(l.get().theChar);
            l.advance();
        };
        System.out.println("\n");
        System.out.println("\n");
        System.out.println("Add X in front of D: ");
        l.toFront();
        l.find(new KeyedChar('D'));
        l.add(new KeyedChar('X'));
        for ( KeyedChar d : l ) {
            System.out.println(d.theChar);
        };
        System.out.println("\n");
        
        
        System.out.println("Output with iterator:");
        java.util.Iterator<KeyedChar> j  = l.iterator();  //implement as explicit iterator
        while(j.hasNext()){
        	System.out.println(j.next().theChar);
        } 
        System.out.println("\n");
        
        System.out.println("Remove X:            ");
        l.toFront();
        l.find(new KeyedChar('X'));
        c = l.remove().theChar;
        l.toFront();
        while ( ! l.offEnd() ) {
            System.out.println(l.get().theChar);
            l.advance();
        };
        System.out.println("\n");
        System.out.println("\n");
        l.find(new KeyedChar('Z'));
        System.out.println("Get at off end: ");
        try {
            System.out.println(l.get().theChar);
        }
        catch ( NoItemException e ) {
            System.out.println("off list");
        };
        System.out.println("\n");
        System.out.println("Remove at off end: ");
        try {
            System.out.println(l.remove().theChar);
        }
        catch ( NoItemException e ) {
            System.out.println("off list");
        };
        System.out.println("\n");
        System.out.println("Advance at off end: ");
        l.advance();
        System.out.println("OK");
        System.out.println("\n");
        
    }; // testList
    
    
    public static void main ( String[] args ) { new TestLists(); };
    
    
} // TestLists