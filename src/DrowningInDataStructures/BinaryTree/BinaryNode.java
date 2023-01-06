package DrowningInDataStructures.BinaryTree;
public class BinaryNode {
        /**
         * This class is a binary Node containing a Drug.
         * @date 2022-10-06
         */


        BinaryNode left;
        BinaryNode right;
        Drug payload;

        public BinaryNode( BinaryNode left, BinaryNode right, Drug payload) {
            this.left = left;
            this.right = right;
            this.payload = payload;
        }

        public String displayNode() {
            return payload.displayDrug();

        }


}
