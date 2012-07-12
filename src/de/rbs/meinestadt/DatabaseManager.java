package de.rbs.meinestadt;

import java.sql.*;

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
    
    public static int getStreetID(String street){
        try {
            ResultSet resultSet = getStatement().executeQuery("SELECT * FROM streets WHERE name LIKE '" + street.trim() + "'");
            System.out.println("SELECT * FROM streets WHERE name LIKE '" + street.trim() + "'");
            if(resultSet.next()){
                return resultSet.getInt("id");
            }
            else
                System.out.println("no id found for " + street);

        } catch (SQLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        return -1;

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
