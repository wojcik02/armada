package armada;

import java.sql.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class DataBase {

    static Connection conn = null;
	static ObservableList<String> shipNames =  FXCollections.observableArrayList( );


    public static void connect() {
    	
    	try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}

        try {
            String url = "jdbc:sqlite:src/main/resources/DataBase/test.db";
            conn = DriverManager.getConnection(url);
            System.out.println("Connection has been established.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
    }


    
    public static int getSpeedDB(String name) {
    	int speed = 0;
    	
        String sql = "SELECT SPEED FROM SHIPS WHERE NAME = '"+name+"'";

    	Statement stmt;
		try {
			stmt = conn.createStatement();
			ResultSet rs    = stmt.executeQuery(sql);
			speed=rs.getInt("SPEED"); 
			System.out.println("Pobra³em prêkoœæ z bazy");
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
        
    	return speed;
    }
    
    public static String getImgUrlDB(String name) {
    	String imgUrl = "";
    	
        String sql = "SELECT CARD FROM SHIPS WHERE NAME = '"+name+"'";

    	Statement stmt;
		try {
			stmt = conn.createStatement();
			ResultSet rs    = stmt.executeQuery(sql);
			imgUrl=rs.getString("CARD"); 
			System.out.println("Pobra³em kartê z bazy");
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
        
    	return imgUrl;
    }
    
    public static ObservableList<String> getShipList() {
        String sql = "SELECT NAME FROM SHIPS";
    	Statement stmt;
		try {
			stmt = conn.createStatement();
			ResultSet rs    = stmt.executeQuery(sql);
			while(rs.next()) {
				shipNames.add(rs.getString("NAME"));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
        
    	return shipNames;
    }

    
    public static void disConnect() {
    	
        try {
			conn.close();
			System.out.println("Database Disconnected");
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }	
    	
    public static void main(String[] args) {
    }	
    
}
	
	


