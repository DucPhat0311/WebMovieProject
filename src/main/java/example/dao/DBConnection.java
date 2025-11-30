package example.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

	private static final String URL = "jdbc:mysql://localhost:3306/cinema_booking?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
	private static final String USER = "root";
	private static final String PASS = "voduti05**";

	static {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Không tìm thấy MySQL Connector/J! Thêm file JAR vào WEB-INF/lib!", e);
		}
	}

	public static Connection getConnection() {
		try {
			Connection conn = DriverManager.getConnection(URL, USER, PASS);
			if (conn != null) {
				System.out.println("Kết nối database thành công! DB: cinema_booking");
			}
			return conn;
		} catch (SQLException e) {
			System.err.println("LỖI KẾT NỐI DATABASE:");
			e.printStackTrace();
			return null;
		}
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