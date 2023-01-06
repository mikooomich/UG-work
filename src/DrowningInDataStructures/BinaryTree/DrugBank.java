package DrowningInDataStructures.BinaryTree;
import java.io.*;

public class DrugBank {
    /**
     * Hello. This class is a Drugbank binary search tree.
     * Implementation of a binary tree.
     * Apparently this implementation has numerous broken parts
     * @date 2022-10-12
     */
    BufferedReader stockpile;
    PrintWriter out;
    PrintWriter out2;


    BinaryNode root;
    Drug specimen;
    String[] data;


    public DrugBank() {
        // file stuff
        try {
            stockpile = new BufferedReader(new FileReader("./src/DrowningInDataStructures/BinaryTree/dockedApproved.tab"));
            out = new PrintWriter(new FileOutputStream("./src/DrowningInDataStructures/BinaryTree/dockedApprovedSorted.tab"));
            out2 = new PrintWriter(new FileOutputStream("./src/DrowningInDataStructures/BinaryTree/result.txt"));
            out.println("Generic Name\tSMILES\tDrug Bank ID\tURL\tDrug Groups\tScore");
            data = new String[2000]; // assume no more than 2000 entries
        }
        catch(Exception e) {
            System.out.println(e);
        }
    }


    /**
     * Calculates the depth of tree relative to a node
     *
     * @param root root node to do the depth operation on
     * @return int depth
     */
    private int depth(BinaryNode root) {
        if (root == null) {
            return -1;
        }

        return Math.max( depth(root.left)+1 , depth(root.right)+1);
    }

    /**
     * Calculate max depth based on a provided ID
     *
     * @param key search key
     * @return int depth
     */
    private int depth1(String key) {
        return depth(search(root, key));
    }

    /**
     * Calculate max depth of whole tree
     *
     * @return int depth
     */
    private int depth2() {
        return depth(root);
    }

    /**
     * Search for a node, given the ID
     *
     * @param root root node
     * @param key search key
     * @return BinaryNode node if found
     */
    private BinaryNode search(BinaryNode root, String key) {
        if (root == null) { // off tree
            return null;
        }
        else if (root.payload.drugBankID.compareTo(key) == 0) { // found
            return root;
        }

        // continue search, traverse similar to inorder traversal
        BinaryNode L = search(root.left, key);
        BinaryNode R = search(root.right, key);

        if (L != null) {
            // only return a non-null result, this methodology is reused for delete
            return L;
        } else {
            return R;
        }

    }
    private BinaryNode search(String key) {
        return search(root, key);
    }


    /**
     * Delete a node provided a search key. Use a similar methodology to search function
     *
     * @param root root node
     * @param key search key
     * @param previous node before the root node (for deletion purposes)
     * @return BinaryNode deleted node
     */
    private BinaryNode delete(BinaryNode root, String key, BinaryNode previous) {
        if (root == null) { // empty tree
            return null;
        }
        else if (root.payload.drugBankID.compareTo(key) == 0) { // found key
            if (previous.left != null && previous.left.payload.drugBankID.compareTo(key) == 0) {
                // node to delete is left child of previous node, we operate on left

                if (root.left == null && root.right == null) { // is leaf, simple delete
                    previous.left = null;
                }
                else if (root.right == null) { // right subtree empty, connect left childline
                    previous.left = root.left;
                }
                else if (root.left == null) { // left subtree empty, connect right childline
                    previous.left = root.right;
                }
                else {
                    // hunt for inorder successor
                    BinaryNode root2 = root;
                    BinaryNode root2Prev = null;
                    try {
                        while (root2.left != null) { // keep traversing to left most node
                            root2Prev = root2;
                            root2 = root2.left;
                        }
                    } catch (Exception e) {}

                    // Case where we reach furthest to the left, but this node has right children
                    if (root2.right != null) {
                        previous.left = root2;
                        root2Prev.left = root2.right;
                    }
                    else { // leaf, we just null
                        previous.left = root2;
                        root2Prev.left = null;
                    }

                }

                return root;

            }
            else if (previous.right != null && previous.right.payload.drugBankID.compareTo(key) == 0) {
                // node to delete is right child of previous node, we operate on right
                // same as left case, however focus on previous.right side
                if (root.left == null && root.right == null) {
                    previous.right = null;
                }
                else if (root.right == null) { // right subtree empty
                    previous.right = root.left;
                }
                else if (root.left == null) { // left subtree empty
                    previous.right = root.right;
                }
                else {
                    // hunt for inorder successor
                    BinaryNode root2 = root;
                    BinaryNode root2Prev = null;
                    try {
                        while (root2.left != null) {
                            root2Prev = root2;
                            root2 = root2.left;
                        }
                    } catch (Exception e) {}

                    if (root2.right != null) {
                        previous.right = root2;
                        root2Prev.left = root2.right;
                    }
                    else {
                        previous.right = root2;
                        root2Prev.left = null;
                    }
                }

                return root;
            }
        }

        // continue search
        BinaryNode L = delete(root.left, key, root);
        BinaryNode R =  delete(root.right, key, root);

        if (L!= null) {
            return L;
        } else {
            return R;
        }
    }

    private BinaryNode delete(String key) {
        return delete(root, key, null);
    }


    /**
     * Traverse tree, print in inorder traversal fashion
     *
     * @param root root node
     */
    private void inOrderTraverse(BinaryNode root) {
        if (root == null) {
            return;
        }

        inOrderTraverse(root.left);

        System.out.println(root.displayNode());
        out.println(root.displayNode());

        inOrderTraverse(root.right);
    }

    private void inOrderTraverse() {
        inOrderTraverse(root);
    }

    /**
     * Read patient stockpile from file, insert into correct place in tree
     */
    private void create()  {
        BinaryNode readhead; // traversal pointer

        for (int i = 0; i < data.length; i++) {
            if (data[i] == null) {
                break;
            }
            specimen = new Drug(data[i]);


            // traverse until reach right place, insert as leaf
            readhead = root;
            for (; ; ) {

                System.out.println("r2");
                if (root == null) { // empty tree
                    root = new BinaryNode(null, null, specimen);
                    break;
                } else if (specimen.drugBankID.compareTo(readhead.payload.drugBankID) < 0) { // focus on left side
                    if (readhead.left != null) { // insert if null, else traverse
                        readhead = readhead.left;
                    }
                    else {
                        readhead.left = new BinaryNode(null, null, specimen); // create leaf
                        break;
                    }
//

                } else { // focus on right side
                    if (readhead.right != null) { // insert if null, else traverse
                        readhead = readhead.right;
                    }
                    else {
                        readhead.right = new BinaryNode(null, null, specimen);
                        break;
                    }
                }

            } // forever


        }
    }


    /**
     * Read from file, insert into initial data array
     */
    private void readData() throws IOException {
        String dataLine = stockpile.readLine();
        int indexCount = 0;
        while (dataLine != null) {
            dataLine = stockpile.readLine();
            data[indexCount] = dataLine;
            indexCount += 1;
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
        DrugBank db = new DrugBank();
        db.readData();


        db.create();
        db.inOrderTraverse();

        db.out2.println(db.depth1("DB01050"));
        db.out2.println(db.depth2());
        db.out2.println(db.search("DB01050").payload.displayDrug());
        db.out2.println(db.search("DB00316").payload.displayDrug());
        db.out2.println(db.delete( "DB01065").payload.displayDrug());


        // the above is this but in a viewable form
//        db.depth1("DB01050");
//        db.depth2();
//        db.search("DB01050");
//        db.search("DB00316");
//        db.delete("DB01065");

        db.kill(); // close files
    }

}
