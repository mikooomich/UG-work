package DrowningInDataStructures.ClinicSimulator;

public class Patient {
    /**
     * This class contains patient properties.
     * @date 2022-09-21
     */


    String name;
    String gender;
    int age;
    String occupation;
    String healthConditions;
    Timer timeOfArrival;
    int priority;

    private static final String[] POSSIBLE_HEALTH_CONDITIONS =
            {"Pregnant", "Cancer", "Diabetes",
            "Asthma", "Primary Immune Deficiency", "Cardiovascular Disease"};

    private static final String[] PRIORITY_OCCUPATIONS =
            {"Teacher", "Nurse", "Care Giver"};




    public Patient(String lineData) {
        String[] propertiesDump = lineData.split("\t");

        name = propertiesDump[0];
        gender = propertiesDump[1];
        age = Integer.parseInt(propertiesDump[2]);
        occupation = propertiesDump[3];
        healthConditions = propertiesDump[4];
        timeOfArrival = new Timer(propertiesDump[5]) ;
        priority = calculatePriority();
    }


    /**
     * Calculate the priority score based on health conditions.
     *
     * @return int score
     */
    public int calculatePriority() {
        int score = 0;

        // heath conditions. Data does not have patients with more than 1 condition, assignment does not state to handle patients with multiple conditions
        for (int i = 0; i < POSSIBLE_HEALTH_CONDITIONS.length; i++) {
            if (healthConditions.compareTo(POSSIBLE_HEALTH_CONDITIONS[i]) == 0) {
                score +=1;
            }
        }

        // occupations
        for (int i = 0; i < PRIORITY_OCCUPATIONS.length; i++) {
            if (occupation.compareTo(PRIORITY_OCCUPATIONS[i]) == 0) {
                score +=1;
            }
        }

        // age
        if (age >= 60) {
            score +=1;
        }

        return score;
    }

    /**
     * Return priority score
     *
     * @return int score of patient
     */
    public int getPriority() {
        return priority;
    }
}
