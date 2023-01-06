package DrowningInDataStructures.ClinicSimulator;

public class Node {
    /**
     * This class is a Node containing a patient.
     * @date 2022-09-21
     */

    Node prev;
    Node next;
    Patient payload;

    public Node(Node head, Node tail, Patient payload) {
        this.prev = head;
        this.next = tail;
        this.payload = payload;
    }
}
