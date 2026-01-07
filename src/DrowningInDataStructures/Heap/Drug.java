package DrowningInDataStructures.Heap;


public class Drug {
    /**
     * This class is a representation of a Drug with it's attributes/properties.
     *
     * @course COSC 2P03
     * @assignment #2
     * @version 1.0
     * @date 2022-10-12
     */
    String genericName;
    String smiles;
    String drugBankID;
    String url;
    String drugGroups;
    float score;




    public Drug(String lineData) {
        String[] propertiesDump = lineData.split("\t");

        genericName = propertiesDump[0];
        smiles = propertiesDump[1];
        drugBankID = propertiesDump[2];
        url = propertiesDump[3];
        drugGroups = propertiesDump[4];
        score = Float.parseFloat(propertiesDump[5]);
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

}
