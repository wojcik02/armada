package armada;

import java.sql.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class DataBase {

	static Connection conn = null;
	static ObservableList<String> shipNames = FXCollections.observableArrayList();

	public static void connect() {

		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}

		try {
			String url = "jdbc:sqlite:src/main/resources/DataBase/test.db";
			conn = DriverManager.getConnection(url);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}

	}

	public static int getSpeedDB(String name) {
		int speed = 0;

		String sql = "SELECT SPEED FROM SHIPS WHERE NAME = '" + name + "'";

		Statement stmt;
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			speed = rs.getInt("SPEED");

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return speed;
	}
	
	public static int getMaxSpeedDB(String name) {
		int maxSpeed = 0;

		String sql = "SELECT MAX_SPEED FROM SHIPS WHERE NAME = '" + name + "'";

		Statement stmt;
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			maxSpeed = rs.getInt("MAX_SPEED");

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return maxSpeed;
	}

	public static String getImgUrlDB(String name) {
		String imgUrl = "";

		String sql = "SELECT CARD FROM SHIPS WHERE NAME = '" + name + "'";

		Statement stmt;
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			imgUrl = rs.getString("CARD");

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return imgUrl;
	}
	
	
	public static String getSpeedTableDB(String name) {
		String speedTable = "";

		String sql = "SELECT SPEED_TABLE FROM SHIPS WHERE NAME = '" + name + "'";

		Statement stmt;
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			speedTable = rs.getString("SPEED_TABLE");

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return speedTable;
	}

	public static ObservableList<String> getShipList() {
		String sql = "SELECT NAME FROM SHIPS";
		Statement stmt;
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			while (rs.next()) {
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
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {

	}
	
	

	public static int getSizeDb(String name) {
		int size = 0;

		String sql = "SELECT SIZE FROM SHIPS WHERE NAME = '" + name + "'";

		Statement stmt;
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			size = rs.getInt("SIZE");

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return size;
	}

	public static String getFraction(String name) {
		String fraction = "";

		String sql = "SELECT FRAC FROM SHIPS WHERE NAME = '" + name + "'";

		Statement stmt;
		try {
			stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql);
			fraction = rs.getString("FRAC");

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return fraction;
	}

}
