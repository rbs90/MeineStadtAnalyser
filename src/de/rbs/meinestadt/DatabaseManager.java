package de.rbs.meinestadt;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

import de.rbs.meinestadt.datatypes.*;
import de.rbs.meinestadt.utils.Levenshtein;

/**
 * Created by IntelliJ IDEA.
 * User: rbs
 * Date: 11.07.12
 * Time: 09:31
 * To change this template use File | Settings | File Templates.
 */
public class DatabaseManager {

    private static Connection conn;

    public static void init() {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:test.db");
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (ClassNotFoundException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public static Statement getStatement(){
        try {
            return conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return null;
    }

    public static Connection getConnection(){
        return conn;
    }
    
    public static int getStreetID(String street, int region_id){
        try {
            ResultSet resultSet = getStatement().executeQuery("SELECT * FROM streets WHERE name LIKE '" + street.trim() + "'");
            //System.out.println("SELECT * FROM streets WHERE name LIKE '" + street.trim() + "'");
            if(resultSet.next()){
                return resultSet.getInt("id");
            }
            else {
            	return getBestStreetMatch(street, region_id);
            }

        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return -1;

    }
    
    public static int getBestStreetMatch(String name, int region){
    	HashMap<Integer, String> streetMap = new HashMap<>();
    	int bestMatch = 100000;
    	int bestMatch_id = -1;
    	
    	try {
			ResultSet resultSet = getStatement().executeQuery("SELECT * FROM streets WHERE region = " + region);
			while(resultSet.next()){
				int id = resultSet.getInt("id");
				streetMap.put(id, resultSet.getString("name"));
				int levenshtein = Levenshtein.computeLevenshteinDistance(name, resultSet.getString("name"));
				if(levenshtein < bestMatch ){
					bestMatch = levenshtein;
					bestMatch_id = id; 
				}
			}
			
			System.out.println("Best Match for " + name + " (" + region + ") is " + bestMatch_id + " with distance of " + bestMatch);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bestMatch_id;
    	
    }

	public static void close() {
		try {
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
