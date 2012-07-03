package de.rbs.meinestadt.datatypes;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: rbs
 * Date: 31.05.12
 * Time: 22:33
 * To change this template use File | Settings | File Templates.
 */
public class SpecialBuildingCollection extends ArrayList<SpecialBuilding> {

    public Boolean containsBuilding(String name, Address address){
        for(SpecialBuilding building : this){
            if (building.getName().equals(name) && building.getAddress().equals(address)){
                return true;
            }
        }
        return false;
    }

    public boolean containsBuilding(SpecialBuilding building) {
        return this.containsBuilding(building.getName(), building.getAddress());
    }
}
