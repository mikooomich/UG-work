package DrowningInDataStructures.ClinicSimulator;

public class Timer {

    /**
     * This class is a timer
     * @date 2022-09-22
     */

    int hour;
    int minute;
    int vaccineTime;



    public Timer(String time) { // <hour>:<minute>
        String[] thing = time.split(":");
        hour = Integer.parseInt(thing[0]);
        minute = Integer.parseInt(thing[1]);
        vaccineTime = -1;
        // necessary to initiate at -1 instead of 0. Starting with empty queue, the first patient has a
        // 14-minute session while others have 15-minute session; add 1 extra minute to correct this
    }

    /**
     * Return the time on the timer in string format
     *
     * @return String time the time in string format
     */
    public String getTime() {
        String time;

        // fill in extra zeros lost during int conversion
        if (hour < 10) {
            time = "0" + hour;
        }
        else {
            time = String.valueOf(hour);
        }
        time += ":";

        if (minute < 10) {
            time += "0" + minute;
        }
        else {
            time += minute;
        }


        return time;
    }

    /**
     * Set vaccineTime back to "first run" state (when no more patients in queue)
     */
    private void reset() {
        vaccineTime = -1;
    }

    /**
     * Compare two times, determine which one is larger (later in the day)
     * 1 if later, -1 if earlier, 0 if same
     *
     * @param two time to compare to
     * @return int
     */
    public int compareTo(Timer two) {
        // 1 if later, -1 if earlier, 0 if same
        if (this.hour == two.hour &&  this.minute == two.minute) {
            return 0;
        }
        if (this.hour >= two.hour) {
              if (this.minute > two.minute) { // check minute before deciding return
                return 1;
            }
        }
        return -1;
    }

    /**
     * Increment the timer by 1 minute (for clinic use)
     * Reset minute and vaccineTime on overflow
     * Only increment vaccineTime if queue is not empty
     *
     * @param patientInQueue status if queue is empty
     */
    public void increment(boolean patientInQueue) {
        minute +=1;

        if (patientInQueue == true) {
            vaccineTime +=1;
        }
        else {
            reset(); // queue empty, return to "first run" state
        }

        // overflow conditions
        if (vaccineTime >= 15) {
            vaccineTime = 0; // signal we can take another patient
        }
        if (minute >= 60) {
            hour ++;
            minute = 0;
        }
    }

}
