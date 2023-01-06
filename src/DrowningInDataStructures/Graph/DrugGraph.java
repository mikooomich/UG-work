package DrowningInDataStructures.Graph;

import java.io.*;
import java.util.*;

public class DrugGraph {
    /**
     * This class is a implementation for a graph for drugs
     * Implementation for a graph data structure. Both weighted and unweighted.
     * @date 2022-12-05
     */

    BufferedReader stockpile; // read from docApproved
    BufferedReader weightReader;
    PrintWriter out; // Prim uses

    double threshold = 0.7;
    Vertex[] vertices; // main vertices

    double[][] W; // weighted
    int[][] A; // 0-1 matrix

    LinkedList<Vertex> simpleQueue; // my makeshift "queue"
    public DrugGraph() {
        // file stuff
        try {
            stockpile = new BufferedReader(new FileReader("./src/DrowningInDataStructures/Graph/dockedApproved.tab"));
            weightReader = new BufferedReader(new FileReader("./src/DrowningInDataStructures/Graph/sim_mat.tab"));
            out = new PrintWriter(new FileOutputStream("./src/DrowningInDataStructures/Graph/MSTPrimResult.tab"));

            stockpile.readLine(); // read out the header
            simpleQueue = new LinkedList<>();
            vertices = new Vertex[2000]; // assume no more than 2000 entries
        }
        catch (Exception e) {System.out.println(e);}
    }



    public static void main(String[] args) throws IOException {
//        System.out.println("Hewwo world!");
        DrugGraph dg = new DrugGraph();
        dg.readData();

        System.out.println("number modules");
        System.out.println("total modules " + dg.findModules());

        dg.keepAModule(0);
        System.out.println("\n\n\nnumber modules after keep");
        System.out.println("total modules " + dg.findModules());
        dg.reset();

        System.out.println("\n\n\nFind shortest");
        dg.findShortestPath("DB01050", "DB00316", "unweighted");
        dg.reset();
        dg.findShortestPath("DB01050", "DB00316", "weighted");
        dg.reset();

        System.out.println("\n\n\nPrim's mst");
        dg.MSTPrim();

        dg.kill();
    }


    /**
     * close files
     */
    public void kill() throws IOException {
        stockpile.close();
        out.close();
    }

    /**
     * Resets the ALL vertex properties
     */
    private void reset() {
        for (int i = 0; i < vertices.length && vertices[i] != null; i++) {
            vertices[i].reset();
        }
    }


    /**
     * get the size of a group given ID
     * @param module ID of group
     * @return size
     */
    private int getModuleSize(int module){
        int numOccurrences = 0;

        for (int i = 0; i < vertices.length && vertices[i] != null; i++) {
            // num occurrences counter
            if (vertices[i].groupID == module) {
                numOccurrences++;
            }
        }

        return numOccurrences;
    }


    /**
     * sort groups by larger size gets lowest id
     */
    private void sortGroups() {
        LinkedList<Integer> list = new LinkedList(); // ids of stuff to process
        while (true) {
            boolean foundSomething = false;

            // for all possible group id numbers
            for (int i = 1; i < vertices.length && vertices[i] != null; i++) {
                /**
                 * sort by swapping: if current group is larger than previous group, swap
                 */
                if (getModuleSize(i-1) < getModuleSize(i)) {
                    foundSomething = true;

                    // find & save all index of vertexes that have i as groupid
                    for (int j = 0; j < vertices.length && vertices[j] != null; j++) {
                        if (vertices[j].groupID == i) {
                            list.addLast(j);
                        }
                    }

                    // set groups i-1 to i
                    for (int j = 0; j < vertices.length && vertices[j] != null; j++) {
                        if (vertices[j].groupID == i-1) {
                            vertices[j].setGroupID(i);
                        }
                    }

                    // set groups i to i-1
                    while (!list.isEmpty()) {vertices[list.removeFirst()].setGroupID(i-1);}

                }
            } // for

            if (foundSomething == false) { // means groups are in order now
                break;
            }
        }
    }





    /**
     * Read from file, insert into vertices array
     */
    private void readData() throws IOException {
        // build vertices array
        String dataLine = stockpile.readLine();
        int indexCount = 0;

        while (dataLine != null) {
            vertices[indexCount] = new Vertex(dataLine, indexCount);
            dataLine = stockpile.readLine();
            indexCount += 1;
        }

        // build adjacency matixes
        /**
         * read one line to determine length
         * build the adjacency matrix with data from file
         */
        String[] propertiesDump = weightReader.readLine().split("\t");
        W = new double[propertiesDump.length][propertiesDump.length];
        A = new int[propertiesDump.length][propertiesDump.length];

        for (int col=0; col < W[0].length; col++) {
            double item = Double.parseDouble(propertiesDump[col]);
            if (1- item <= threshold && 0 != col) {
                W[0][col] = 1 - item;
                A[0][col] = 1;
            }
            else {
                W[0][col] = Double.POSITIVE_INFINITY;
                A[0][col] = 0;
            }
        }


        /**
         * now do the same but with rest of matrix
         */
        for (int row =1; row < W.length; row++) {
            propertiesDump = weightReader.readLine().split("\t");

            for (int col=0; col < W[row].length; col++) {
                double item = Double.parseDouble(propertiesDump[col]);

                if (1- item <= threshold && row != col) {
                    W[row][col] = 1 - item;
                    A[row][col] = 1;
                }
                else {
                    W[row][col] = Double.POSITIVE_INFINITY;
                    A[row][col] = 0;
                }
            }
        } // for
    }


    /**
     * Do breadth first search to find all connected vertex
     *
     * @param i starting index
     */
    public void BFS(int i) {
        Vertex primaryVertex = vertices[i];
        primaryVertex.visit();
        simpleQueue.addLast(primaryVertex);


        while (simpleQueue.isEmpty() == false) {
            primaryVertex = simpleQueue.removeFirst();
            int indexOFtHing = primaryVertex.index; // this is the "i", but I have stored the index in the object itself

            for (int thing = 0; thing < A[indexOFtHing].length; thing ++) { // search for all possible ajacencies
                 // iterate row, if there is not zero or infinity, do the following code
                if (A[indexOFtHing][thing] !=0) {
                    Vertex secondaryVertex = vertices[thing]; // thing traverses horizontally, pull the corresponding vertex in vertex array

                    if (secondaryVertex.wasVistited == 0) { // if not visited, visit
                        secondaryVertex.visit();
                        simpleQueue.addLast(secondaryVertex);
                    }
                }

            } // for
        } // while
    }

    /**
     * Find number of disconnected modules
     * @return
     */
    public int findModules() {
        // ifnd num of disocnected componets


        /**
         * iterate through all vertices, do bfs to mark vertices in the same connected group
         * set the group id of visited vertexes
         * once set, mark the vertex in "limbo" so the next bfs or next id set process don't see it
         *
         * wasvisited states
         * 0 - not visited
         * 1 - visited (current group)
         * -1 - visited (previous groups)
         */

        int idCount = 0; // id to set thr group to
        for (int i = 0; i < vertices.length && vertices[i] != null; i++) {
            if (vertices[i].wasVistited == 0) { // do bfs if not visited already
                BFS(i);

                // vertexes have been marked, add id &set to limbo
                for (int j = 0; j < vertices.length && vertices[j] != null; j++) {
                    if (vertices[j].wasVistited == 1) {
                        vertices[j].setGroupID(idCount);
                        vertices[j].enterLimbo();
                    }
                } // inner for

                idCount++;
            }
        }


        // to satisfy the "Module 0 should be larger then Module 1, and so on" requirement
        sortGroups();



            /**
             * Scanning function
             * Find how large each group is
             * Find how many groups there are
             */
            int idOfgroup = 0;
            System.out.println("ID ::: #elements");

            while (true) {
                /**
                 * iterate many times, increment ID
                 * stop when no matching IDs found. This is the total number of groups
                 */
                boolean foundSomething = false;
                int numOccurrences = 0; // number of nodes in a group

                for (int i = 0; i < vertices.length; i++) {
                    if (vertices[i] == null) {
                        break;
                    }

                    // num occurrences counter
                    if (vertices[i].groupID == idOfgroup) {
                        numOccurrences++;
                        foundSomething = true;
                    }
                }

                if (foundSomething == false) {
                    break;
                } // means we have exhausted all groups
                System.out.println(idOfgroup + ":::" + numOccurrences);
                idOfgroup++; // id counter
            }

            System.out.println("ID ::: #elements");
            return idOfgroup; // number of modules
        }



    public void keepAModule(int moduleID) {
        int[][] resultingA = new int[A.length][A.length];
        double[][] resultingW = new double[A.length][A.length];

        // initialize to zero
        for (int i = 0; i < resultingA.length; i++) {
            for (int j = 0; j < resultingA.length; j++) {
                resultingA[i][j] = 0;
                resultingW[i][j] = Double.POSITIVE_INFINITY;
            }
        }

//         remove any modules ids that do not have the matching id (because my findModules scans ids only)
        for (int i = 0; i < vertices.length; i++) {
            if (vertices[i] == null) {break;}
            if (vertices[i].groupID != moduleID) {
                vertices[i].setGroupID(-1);
            }

        }


        for (int i = 0; i < resultingA.length; i++) {
            for (int j = 0; j < resultingA.length; j++) {
                Vertex a = vertices[i];
                Vertex b = vertices[j];



                // Hello, I have no idea and am genuinely confused why it also copies over random vertices even though all IDs that *NOT == module ID* have been explicitly set to -1 AAAAAAAAAA
                // There are many words going through my head I can't say out loud right now
                // But it doesn't seem to severally impact the later parts(?) idk
                if (a.groupID == moduleID && b.groupID == moduleID && i != j) {// copy to new arrays if module id matches
                    resultingA[i][j] = A[i][j];
                    resultingW[i][j] = W[i][j];
                }
            } // inner for
        } // for

        A = resultingA;
        W = resultingW;
    }







    private void MSTPrim() {
        PriorityQueue<VertexWithPriority> pQueue = new PriorityQueue<>();
        Vertex primaryVertex = vertices[21]; // I just pick random vertex known to exist in the group
        primaryVertex.distance = 0;

        // enqueue
        pQueue.offer(new VertexWithPriority(primaryVertex, "0"));


        while (pQueue.isEmpty() == false) {
            primaryVertex = pQueue.remove().vertex;
            int indexOFtHing = primaryVertex.index; // this is the "i", but I have stored the index in the object itself

            if (primaryVertex.wasVistited == 0) {
                primaryVertex.visit();

                for (int thing = 0; thing < W[indexOFtHing].length; thing++) { // search for all possible ajacencies
                    Vertex secondaryVertex = vertices[thing]; // the adjacent vertex to compare against

                    // ensure adjacent vertex that satisfys
                    if (secondaryVertex.distance >  W[indexOFtHing][thing] && secondaryVertex.wasVistited == 0 ) { // code to ignore 0 edges: && W[indexOFtHing][thing] != Double.POSITIVE_INFINITY && W[indexOFtHing][thing] > 0
                        secondaryVertex.distance = W[indexOFtHing][thing];
                        secondaryVertex.path = primaryVertex;

                        // enqueue
                        pQueue.offer(new VertexWithPriority(secondaryVertex, String.valueOf(secondaryVertex.distance)));
                    }
                } // for
            }
        } // while




        // print out vertexes with their corresponding distances
        // print out total distance
        double finalDist = 0;
        for (int i = 0; i < vertices.length && vertices[i] != null; i++) {
            if (vertices[i].distance != Double.POSITIVE_INFINITY) {
                out.println(vertices[i].drugBankID + "--" + vertices[i].distance);
                finalDist += vertices[i].distance;
            }
        }

        System.out.println(finalDist);
    }

    /**
     * Find shorted path of vertexes
     *
     * @param fromDrug starting drug id
     * @param toDrug ending drug id
     * @param method weighter or unweighted
     * @return
     */
    private String findShortestPath(String fromDrug, String toDrug, String method) {
        String path = ""; // path (list) of drug vertexes
        int from = 0;
        int to = 0;

        // search for indexes of corresponding to the id
        for (int i = 0; i < vertices.length && vertices[i] != null; i++) {
            if (fromDrug.compareTo(vertices[i].drugBankID) == 0){
                from = i;
            }
            if (toDrug.compareTo(vertices[i].drugBankID) == 0) {
                to = i;
            }
        }

        /**
         * Dijkstra
         */
        if (method == "weighted") {
            PriorityQueue<VertexWithPriority> pQueue = new PriorityQueue<>();
            Vertex primaryVertex = vertices[from];
            primaryVertex.distance = 0;

            pQueue.offer(new VertexWithPriority(primaryVertex, "0")); //enqueue


            while (pQueue.isEmpty() == false) {
                primaryVertex = pQueue.remove().vertex;
                int rowIndex = primaryVertex.index; // this is row index "i", but I have stored the index in the object itself

                if (primaryVertex.wasVistited == 0) {
                    primaryVertex.visit();

                    for (int adjacentIndex = 0; adjacentIndex < W[rowIndex].length; adjacentIndex++) { // search for all possible ajacencies
                        Vertex secondaryVertex = vertices[adjacentIndex]; // the adjacent vertex to compare against

                        // ensure adjacent vertex that satisfys
                        if (secondaryVertex.distance > (primaryVertex.distance + W[rowIndex][adjacentIndex]) && W[rowIndex][adjacentIndex] > 0 && W[rowIndex][adjacentIndex] != Double.POSITIVE_INFINITY) {
                            secondaryVertex.distance = primaryVertex.distance + W[rowIndex][adjacentIndex];
                            secondaryVertex.path = primaryVertex;

                            // enqueue
                            pQueue.offer(new VertexWithPriority(secondaryVertex, String.valueOf(secondaryVertex.distance)));
                        }

                    } // for
                }

            } // while
        }

        /**
         * unweighted
         */
        else {
            Vertex primaryVertex = vertices[from];
            primaryVertex.visit();

            primaryVertex.distance = 0;
            simpleQueue.addLast(primaryVertex);

            while (simpleQueue.isEmpty() == false) {
                primaryVertex = simpleQueue.removeFirst();
                primaryVertex.visit();
                int rowIndex = primaryVertex.index; // this is the "i", but I have stored the index in the object itself

                for (int adjacentIndex = 0; adjacentIndex < A[rowIndex].length; adjacentIndex ++) { // search for all possible ajacencies
                    Vertex secondaryVertex = vertices[adjacentIndex]; // the adjacent vertex to compare against

                    // if can improve distance
                    if (secondaryVertex.distance > primaryVertex.distance +1 && A[rowIndex][adjacentIndex] !=0) {
                        secondaryVertex.distance = primaryVertex.distance +1;
                        secondaryVertex.path = primaryVertex;

                        simpleQueue.addLast(secondaryVertex);

                    }

                } // for
            } // while

        }


        // print out the chain of vertexes to get from start to end
        Vertex pointer = vertices[to];

        while (pointer != null && pointer.path != null) {
            path =  "-----" + pointer.drugBankID+ path;
            pointer = pointer.path;

            if (pointer.drugBankID.compareTo(fromDrug) == 0) {
                break;
            }
        }
        path = vertices[from].drugBankID + path;

        System.out.println(path);
        return path;
    }

} // class
