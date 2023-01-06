package DrowningInDataStructures.ClinicSimulator;

public class WaitQueue {
    /**
     * This class represents a priority wait queue for the clinic.
     * @date 2022-09-22
     */


    Node head = null; // start of queue
    Node tail = null; // end of queue

    // for traversing, operations
    Node a; // main traversal node
    Node b; // extra node


    public WaitQueue() {
    }

    /**
     * Insert node into the queue based on priority
     *
     * @param patient patient to insert
     */
    public void insert(Patient patient) {

        if (head == null) { // empty queue
            head = new Node(null, null, patient);
            tail = head;
        }
        else if (patient.getPriority() <= tail.payload.getPriority())  { // insert at very end
            // assume always insert at end if lowest priority score (the lowest score patient with the latest time means end of line)
            tail.next = new Node(tail, null, patient);
            tail = tail.next;
        }

        else { // somewhere in the middle
            /**
             * Search for spot in line, insert.
             * Compare priority first, use time arrival as tiebreaker
             * The traversal should never reach the end of queue as this is handled by above cases
             */

            a = head;

            while (a != null && head != null) {
                if (a.payload.getPriority() < patient.getPriority()) {
                    if (a.prev == null) { // insert at front of queue, after 1st position (which is in vaccination slot)
                        a = a.next;

                        // perform addition
                        a.prev = new Node(head, a, patient); // insert in front of queue
                        head.next = a.prev;
                        return;
                    }
                    else { // in middle of queue
                        // perform addition
                        b = new Node(a.prev, a, patient);
                        a.prev.next = b;
                        a.prev = b;
                        return;
                    }
                }

                else if (a.payload.getPriority() == patient.getPriority()) { // tiebreaker with arrival time
                    if (a.payload.timeOfArrival.compareTo(patient.timeOfArrival) > 0) {
                        // perform addition
                        b = new Node(a.prev, a, patient);
                        a.prev.next = b;
                        a.prev = b;
                        return;
                    }
                }

                traverse();
            } // end while loop
//            System.out.println("something went wrong because this should not reach end");
        }

    }

    /**
     * Traverse to next element
     */
    private void traverse() {
        a = a.next;
    }

    /**
     * Remove the patient at the front of queue
     * @return Patient
     */
    public Patient removeMax() {

        if (head == null) {
            return null;
        }
        Patient nextInLine = head.payload;

        if (head.next != null) { // 2 or more items left in queue
            head = head.next;
            head.prev = null;
            return nextInLine;
        }
        // only 1 item left, remove it
        head = null;
        return nextInLine;
    }


}
