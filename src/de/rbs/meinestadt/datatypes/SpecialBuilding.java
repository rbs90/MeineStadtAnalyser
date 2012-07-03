package de.rbs.meinestadt.datatypes;

/**
 * Created by IntelliJ IDEA.
 * User: rbs
 * Date: 31.05.12
 * Time: 02:16
 * To change this template use File | Settings | File Templates.
 */
public class SpecialBuilding {
    private Address address;
    private int branch_id;
    private String name;

    public SpecialBuilding(String name, String addressAnalyse, int branch_id) {
        this.address = new Address(addressAnalyse);
    }

    public Address getAddress() {
        return address;
    }

    public int getBranch_id() {
        return branch_id;
    }

    public String getName() {
        return name;
    }
}
