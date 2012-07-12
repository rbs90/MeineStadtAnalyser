package de.rbs.meinestadt.datatypes;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: rbs
 * Date: 18.06.12
 * Time: 20:11
 * To change this template use File | Settings | File Templates.
 */
public class Address {
	private final static Pattern STREET_WITHOUT_HNR = Pattern.compile("\\D+");
	private final static Pattern STREET_WITH_HNR = Pattern.compile("(\\D+)(\\d+)");
	private final static Pattern STREET_WITH_HNR_AND_LETTER = Pattern.compile("(\\D+)(\\d+)\\s(\\D)");
	private final static Pattern STREET_WITH_HNR_AND_LETTER_CONNECTED = Pattern.compile("(\\D+)(\\d+)(\\D)");
	
	Street street;
    int region_id;
    int hnr;
    char hnrChar;
    Boolean hasHnrChar = false;
    Boolean hasHnr = false;

    public Address(Street street, int region_id, int hnr, char hnrChar) {
        this.street = street;
        this.region_id = region_id;
        this.hnr = hnr;
        this.hnrChar = hnrChar;
    }
    
    public Address(String analyseString) {
        String analysed = analyseString.replaceAll("Str\\.", "Straﬂe").replaceAll("str\\.", "straﬂe");
        String street = analysed.split(",")[0];
        
       
        if(street.matches(STREET_WITH_HNR.pattern())){ //Straﬂenname 105
        	Matcher matcher = STREET_WITH_HNR.matcher(street);
        	if(matcher.find()){
        		this.street = new Street(matcher.group(1));
        		hnr = Integer.parseInt(matcher.group(2));
        		hasHnr = true;
        	}
        }
        else if(street.matches(STREET_WITH_HNR_AND_LETTER.pattern())){ //Straﬂenname 105 a
        	Matcher matcher = STREET_WITH_HNR_AND_LETTER.matcher(street);
        	if(matcher.find()){
        		this.street = new Street(matcher.group(1));
        		hnr = Integer.parseInt(matcher.group(2));
        		hasHnr = true;
        		hnrChar = matcher.group(3).charAt(0);
        		hasHnrChar = true;
        	}
        }
        else if(street.matches(STREET_WITHOUT_HNR.pattern())){ //Straﬂenname
        	this.street = new Street(street.trim());
        }
        else if(street.matches(STREET_WITH_HNR_AND_LETTER_CONNECTED.pattern())){ //Straﬂenname
        	Matcher matcher = STREET_WITH_HNR_AND_LETTER_CONNECTED.matcher(street);
        	if(matcher.find()){
        		this.street = new Street(matcher.group(1));
        		hnr = Integer.parseInt(matcher.group(2));
        		hasHnr = true;
        		hnrChar = matcher.group(3).charAt(0);
        		hasHnrChar = true;
        	}
        }
        else
        	System.out.println("INVALID STREET: " + street);
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

    public char getHnrChar() {
        return hnrChar;
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

	public boolean hasHnr() {
		return hasHnr;
	}
	
	public boolean hasHnrChar() {
		return hasHnrChar;
	}
}
