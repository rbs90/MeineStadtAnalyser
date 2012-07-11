package de.rbs.meinestadt.datatypes;

import de.rbs.meinestadt.DatabaseManager;

/**
 * Created by IntelliJ IDEA.
 * User: rbs
 * Date: 18.06.12
 * Time: 22:45
 * To change this template use File | Settings | File Templates.
 */
public class Street {
    
    String name;
    int id;
    
    public Street(String name) {
        this.name = name.trim();
        this.id = DatabaseManager.getStreetID(name);
        System.out.println("found id " +  id);
    }

    public Street(String name, int id) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }
}
