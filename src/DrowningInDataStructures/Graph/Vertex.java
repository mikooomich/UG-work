package DrowningInDataStructures.Graph;

public class Vertex {

    /**
     * This class is a representation of a Vertex (Drug) with its attributes/properties.
     * @date 2022-12-04
     */

    // attributes
    String genericName;
    String smiles;
    String drugBankID;
    String url;
    String drugGroups;
    float score;


    // necessary for graph stuff
    int wasVistited;
    double distance;
    int groupID = -1;
    Vertex path;

    // additional stuff to make my life easier
    int index; // index of vertex in vertices array
//    String priority; // priority for priority queue


    public Vertex(String lineData, int index) {
        String[] propertiesDump = lineData.split("\t");

        genericName = propertiesDump[0];
        smiles = propertiesDump[1];
        drugBankID = propertiesDump[2];
        url = propertiesDump[3];
        drugGroups = propertiesDump[4];
        score = Float.parseFloat(propertiesDump[5]);

        this.index = index;

        // set defaults
        wasVistited = 0;
        distance = Double.POSITIVE_INFINITY;
//        priority = String.valueOf(Double.POSITIVE_INFINITY);
    }


    /**
     * Compile the attributes to a string separated by tabs
     *
     * @return String
     */
    public String displayDrug() {
//        return drugBankID;
        return genericName +"\t" + drugBankID +"\t" + smiles +"\t" + url +"\t" + drugGroups +"\t" + score;
    }

    /**
     * methods for setting wasvisted to
     * wasvisited states
     *      false   0 - not visited
     *      true    1 - visited (current group)
     *      limbo   -1 - visited (previous groups)
     */
    public void visit() {
        wasVistited = 1;
    }
    public void unvisit() {
        wasVistited = 0;
    }
    public void enterLimbo() {
        wasVistited = -1;
    }


    public void setGroupID(int i) {
        groupID = i;
    }

    /**
     * resets the vertex to the default values
     */
    public void reset() {
        wasVistited = 0;
        distance = Double.POSITIVE_INFINITY;
//        priority = String.valueOf(Double.POSITIVE_INFINITY);
    }
}
