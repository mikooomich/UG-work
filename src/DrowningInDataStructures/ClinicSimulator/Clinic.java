package DrowningInDataStructures.ClinicSimulator;

import java.io.*;


/**
 * This class facilitates the operations of the clinic.
 * Implementation of a priority queue
 * @date 2022-09-22
 */
public class Clinic {

    BufferedReader data;
    PrintWriter out;
    Patient thisPatient;

    WaitQueue wq;
    Timer clock;


    public Clinic() throws IOException {

        try {
            data = new BufferedReader(new FileReader("./src/DrowningInDataStructures/ClinicSimulator/patients.txt"));
            out = new PrintWriter(new FileOutputStream("./src/DrowningInDataStructures/ClinicSimulator/dayReport.txt"));
            wq = new WaitQueue();
        }
        catch(Exception e) {
            System.out.println(e);
        }

        // start clinic function
        monitor();

        data.close();
        out.close();

    }

    /**
     * This function runs the clinic until closing time
     */
    private void monitor() throws IOException {
        /**
         * Suppose patient enter vaccine slot at 0:00.00
         * When finished, get kicked out at 0:15.00, new patient enters at 0:15.00
         */


        clock = new Timer("09:00"); // we open at 9AM
        data.readLine(); // get rid of header
        readData();

        System.out.println(clock.getTime());
        System.out.println("Day started\n");
        out.println(clock.getTime());
        out.println("Day started\n");


        while (clock.compareTo(new Timer("16:30")) < 0) { // set arbitrary close at 4:30PM
//            System.out.println(clock.getTime());

            if (thisPatient != null) { // check if there will be a patient coming
                if (clock.compareTo(thisPatient.timeOfArrival) == 0) { // check if it is time for patient to arrive
//                    System.out.println("Inserting" + thisPatient.name);

                    wq.insert(thisPatient);
                    try {
                        readData(); // read next patient from file
                    } catch (EOFException e) {
                        thisPatient = null; // assume file empty means no more patients coming
                    }
                }
            }

            // check if vaccine slot is available
            if (clock.vaccineTime >= 14) {
                Patient person = wq.removeMax();
                if (person != null && wq.head != null) {
                    System.out.println(clock.getTime());
//                  System.out.println("Kicking " + person.name);
                    System.out.println("Entering: " + wq.head.payload.name); // print when called for vaccine

                    out.println(clock.getTime());
                    out.println("Entering: " + wq.head.payload.name);
                }
            }

            // increment timer
            if (wq.head != null) {
                clock.increment(true); // increment vaccineTime when slot is taken
            }
            else {
                clock.increment(false); // do not increment vaccineTime when slot vacant
            }
        }

        System.out.println(clock.getTime());
        System.out.println("Day ended");
        out.println(clock.getTime());
        out.println("Day ended");
    }


    /**
     * Read patient data from file
     */
    private void readData() throws IOException {
        String dataLine = data.readLine();
        if (dataLine == null) {
            throw new EOFException();
        }
        thisPatient = new Patient(dataLine);
    }




    public static void main(String[] args) throws IOException {
        Clinic wah = new Clinic();
    }
}
