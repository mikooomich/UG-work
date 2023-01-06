package DrowningInDataStructures.Graph;

public class VertexWithPriority implements Comparable<VertexWithPriority> {
    /**
     * This class is a representation of a Vertex (Drug) with priority, so I can use Java util's priority queue
     * @date 2022-12-04
     */
    Vertex vertex;
    String priority;


    public VertexWithPriority(Vertex thing, String priority) {
        vertex = thing;
        this.priority = priority;
    }



    /**
     * compare to function for priority queue
     *
     * @param o the object to be compared.
     * @return
     */
    @Override
    public int compareTo(VertexWithPriority o) {
        int thing = this.priority.compareTo(o.priority);
        return thing;
    }
}
