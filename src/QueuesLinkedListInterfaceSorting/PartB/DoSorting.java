package QueuesLinkedListInterfaceSorting.PartB;




/**
 * This class does is a testing class for the sorting (Part B).
 * KeyedChar from collections package will be reused
 *
 * @course COSC 1P03
 * @assignment #5
 * @version 1.0
 * @date 2022-04-07
 */
public class DoSorting {



    public DoSorting(){
        SortableList<Integer> list;   // a list
//        display = new ASCIIDisplayer(50,35);
        list = new SortImpl<Integer>();

        System.out.println("Sorting");
        System.out.println("\n");
        testList(list);
//        display.close();
    }


    private void testList(SortableList<Integer> list) {
        /**
         * This method fills the list, prints it, sorts it, prints it, then sorts the sorted list, prints again
         */
        for (int i = 0; i < 100; i++) {
            // fill list
            list.add(new Integer((int) Math.floor(Math.random() * (100 + 1) + 0)));
        }

//        for (int i = 0; i < 100; i++) {
//            // fill list
//            list.add(new Integer(i));
//        }


        System.out.println("Original list");
        System.out.println("\n");
        for ( Integer intThing : list ) { //
            System.out.println(intThing.intValue());
        }

        System.out.println("\n");
        System.out.println("\n");
        System.out.println("Number of comparisons taken to sort: " + list.Sort());
        System.out.println("\n");
        System.out.println("\n");

        System.out.println("Sorted list");
        System.out.println("\n");
        for ( Integer intThing : list ) {
            System.out.println(intThing.intValue());
        }




        System.out.println("\n");
        System.out.println("\n");
        System.out.println("Number of comparisons taken to sort the sorted list: " + list.Sort());
        System.out.println("\n");
        System.out.println("\n");

        System.out.println("Sorted list (2)");
        System.out.println("\n");
        for ( Integer intThing : list ) {
            System.out.println(intThing.intValue());
        }
    }

    public static void main ( String[] args ) { new DoSorting(); };

}
