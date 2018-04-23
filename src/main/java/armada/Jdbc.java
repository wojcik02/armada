package armada;

import java.sql.*;


public class Jdbc {

    static Connection conn = null;

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
	
	


