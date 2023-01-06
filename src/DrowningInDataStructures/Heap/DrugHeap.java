package DrowningInDataStructures.Heap;

import java.io.*;

public class DrugHeap {
    /**
     * Hello. This class is a DrugHeap heap implemented as an array.
     * Implementation for a min heap data structure.
     * @date 2022-11-05
     */
    BufferedReader stockpile;
    PrintWriter out; // sorted file
    PrintWriter out2; // inorder result file


    Drug[] data; // main data
    int heapLength = 0;


    public DrugHeap() {
        // file stuff
        try {
            stockpile = new BufferedReader(new FileReader("./src/DrowningInDataStructures/Heap/dockedApproved.tab"));
            out = new PrintWriter(new FileOutputStream("./src/DrowningInDataStructures/Heap/dockedApprovedSorted.tab"));
            out2 = new PrintWriter(new FileOutputStream("./src/DrowningInDataStructures/Heap/dockedApprovedInOrder.tab"));
            out.println("Generic Name\tSMILES\tDrug Bank ID\tURL\tDrug Groups\tScore");
            out2.println("Generic Name\tSMILES\tDrug Bank ID\tURL\tDrug Groups\tScore");

            stockpile.readLine(); // read out the header

            data = new Drug[2000]; // assume no more than 2000 entries
            data[0] = null;
        }
        catch (Exception e) {System.out.println(e);}
    }


    /**
     * push the "node" down until it is in the right place in heap
     *
     * @param i parent
     */
    private void trickleDown(int i) {
        // base case
        if (data[i] == null || i*2 >= 2000 || data[i*2] == null ) // the >= 2000 fixes oob
            return;

        // swap with smaller child
        int childToUse;

        try {
            if (data[i*2].drugBankID.compareTo(data[2 * i + 1].drugBankID) > 0) {
                // do right child
                childToUse = 2 * i + 1;
            } else {
                // do left child
                childToUse = 2*i;
            }
        }
        catch (Exception error) {
// still want to have working program if no right child, but left child is existing
            childToUse = 2*i;
        }


        // compare smallest child with parent, swap if parent is larger
        if (data[i].drugBankID.compareTo(data[childToUse].drugBankID) > 0) {
            Drug temp = data[i];
            data[i] = data[childToUse];
            data[childToUse] = temp;

            trickleDown(childToUse);
        }
    }


    /**
     * removes the min index drug (root)
     * set index 1 as last element in heap, Use trickledown to fix heap ordering
     * @return Drug
     */
    private Drug removeMin() {
        Drug element = data[1];

        // replace data with last element
        data[1] = data[heapLength];
        data[heapLength] = null;
        heapLength -= 1;
        trickleDown(1); // always top node
//        buildHeap();
        return element;
    }


    /**
     * converts an unsorted array into a heap structure
     */
    private void buildHeap() {
        for (int i = (int) Math.floor(heapLength /2); i > 0; i--) {
            trickleDown(i);
        }
    }



    /**
     * Traverse tree, print in inorder traversal fashion
     *
     * @param root the "root" index
     */
    private void inOrderTraverse(int root) {
        if (root >= 2000 || data[root] == null) { // the >= 2000 fixes oob
            return;
        }

        inOrderTraverse(root * 2);
//        System.out.println(data[root].drugBankID);
        out2.println(data[root].displayDrug());
        inOrderTraverse(root * 2 +1);
    }

    private void inOrderTraverse() {
        inOrderTraverse(1);
    }




    /**
     * write a sorted heap to file
     */
    private void heapSort() {
        // copy to new array
        Drug[] sorted = new Drug[heapLength];
        for (int i = 0; i< sorted.length; i ++) {
            sorted[i] = data[i];
        }

//        remove the top
        for (int i = 1; i < sorted.length; i++) {
            out.println(removeMin().displayDrug());
        }

    }



    private void prtArray() {
        /**
         * for debugging
         */
        for (int i = 1; i <data.length; i++) {
            if(data[i] == null){break;}
            System.out.println(data[i].drugBankID);
//            out2.println(data[i].displayDrug());
        }
    }








    /**
     * Read from file, insert into data array
     */
    private void readData() throws IOException {
        String dataLine = stockpile.readLine();
        int indexCount = 1; // heap starts from index 1

        while (dataLine != null) {
            data[indexCount] = new Drug(dataLine);
            dataLine = stockpile.readLine();
            indexCount += 1;
            heapLength +=1;
        }
    }


    /**
     * Close files
     */
    public void kill() throws IOException {
        stockpile.close();
        out.close();
        out2.close();
    }










    public static void main(String[] args) throws IOException {
        // starting
        DrugHeap db = new DrugHeap();
        db.readData();

        // main thingy
        db.buildHeap();
        db.inOrderTraverse();
        db.heapSort();

        db.kill(); // close files
    }

}
