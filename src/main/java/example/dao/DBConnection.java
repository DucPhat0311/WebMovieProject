package example.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
	private static final String URL = "jdbc:mysql://localhost:3306/movie?useSSL=false&serverTimezone=UTC&characterEncoding=UTF-8";
	private static final String USER = "root";
	private static final String PASSWORD = "123456";

	public static Connection getConnection() {
		Connection conn = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(URL, USER, PASSWORD);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
		return conn;
	}

	public static void closeConnection(Connection c) {
		try {
			if (c != null && !c.isClosed()) {
				c.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
