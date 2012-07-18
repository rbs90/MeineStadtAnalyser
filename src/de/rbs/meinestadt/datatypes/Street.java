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
    int region_id;
    
    public Street(String name, int region_id) {
        this.name = name.trim();
        this.id = DatabaseManager.getStreetID(name, region_id);
        this.region_id = region_id;
    }

    public Street(String name, int id, int region_id) {
        this.id = id;
        this.name = name;
        this.region_id = region_id;
        
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }
}
