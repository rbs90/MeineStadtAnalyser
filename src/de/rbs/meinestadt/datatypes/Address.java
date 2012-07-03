package de.rbs.meinestadt.datatypes;

/**
 * Created by IntelliJ IDEA.
 * User: rbs
 * Date: 18.06.12
 * Time: 20:11
 * To change this template use File | Settings | File Templates.
 */
public class Address {
    Street street;
    int region_id;
    int hnr;
    char hnrChar;

    public Address(Street street, int region_id, int hnr, char hnrChar) {
        this.street = street;
        this.region_id = region_id;
        this.hnr = hnr;
        this.hnrChar = hnrChar;
    }
    
    public Address(String analyseString){
        String analysed = analyseString.replaceAll("Str.", "Straﬂe").replaceAll("str.", "straﬂe");
        String[] frontSplit = analysed.split(",")[0].split(" ");
        String hnrSplit = frontSplit[frontSplit.length - 1];
        if (hnrSplit.matches("[\\d]+")){ //no letter
            hnr = Integer.parseInt(hnrSplit);
            hnrChar = ' ';
        }
        else {
            int hnrLenght = hnrSplit.length();
            hnrChar = hnrSplit.trim().charAt(hnrLenght - 1); //get last char
            System.out.println("hnr_char: " + hnrChar);
            try{
                if(hnrLenght != 1){
                    hnr = Integer.parseInt(hnrSplit.substring(0, hnrLenght - 2).trim());
                    System.out.println("M1 - hnr: " + hnr);
                    street = new Street(analysed.substring(0, analysed.length() - frontSplit[frontSplit.length - 1].length()));
                }
                else{
                    hnr = Integer.parseInt(frontSplit[frontSplit.length - 2 ].trim());
                    System.out.println("M2 - hnr: " + hnr);
                }
            }
            catch (NumberFormatException e){
                e.printStackTrace();
            }
        }




        
    }

    public Street getStreet() {
        return street;
    }

    public int getRegion_id() {
        return region_id;
    }

    public int getHnr() {
        return hnr;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Address){
            Address instance = (Address) obj;
            if(instance.getHnr() == getHnr()
                    && instance.getRegion_id() == getRegion_id()
                    && instance.getStreet() == instance.getStreet()){
                return true;
            }
        }
        else
            return false;

        //never reached:
        return false;
    }
}
