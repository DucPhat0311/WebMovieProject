package example.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCUtil {
	private static final String URL = "jdbc:mysql://localhost:3306/movie?useSSL=false&serverTimezone=UTC";
	private static final String USER = "root";
	private static final String PASSWORD = "voduti05**";

	public static Connection getConnection() {
		Connection c = null;
		try {
			// DEBUG CODE - PHẢI CÓ ĐOẠN NÀY
			System.out.println("=== JDBC DEBUG ===");
			System.out.println("Loading MySQL driver...");

			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("MySQL driver loaded successfully!");

			System.out.println("Connecting to: " + URL);
			System.out.println("Username: " + USER);

			c = DriverManager.getConnection(URL, USER, PASSWORD);

			System.out.println("Database connection SUCCESS!");
			System.out.println("=== END DEBUG ===");

		} catch (ClassNotFoundException e) {
			System.err.println("ERROR: MySQL Driver not found!");
			e.printStackTrace();
		} catch (SQLException e) {
			System.err.println("ERROR: Database connection failed!");
			e.printStackTrace();
		}
		return c;
	}

	public static void closeConnection(Connection c) {
		try {
			if (c != null) {
				c.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}