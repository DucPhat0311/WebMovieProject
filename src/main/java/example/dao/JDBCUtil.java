package example.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCUtil {
	private static final String URL = "jdbc:mysql://localhost:3306/movie?useSSL=false&serverTimezone=UTC";
	private static final String USER = "root";
	private static final String PASSWORD = "voduti05**";

	/**
	 * B1: Tạo kết nối đến database MySQL B2: Load MySQL Driver bằng Class.forName()
	 * B3: Sử dụng DriverManager để lấy Connection B4: Trả về Connection cho các DAO
	 * sử dụng
	 */
	public static Connection getConnection() {
		Connection c = null;
		try {
			// B1: Load MySQL JDBC Driver
			Class.forName("com.mysql.cj.jdbc.Driver");

			// B2: Tạo kết nối với thông tin URL, username, password
			c = DriverManager.getConnection(URL, USER, PASSWORD);

		} catch (ClassNotFoundException e) {
			// B3: Xử lý lỗi không tìm thấy Driver
			System.err.println("ERROR: MySQL Driver not found!");
			e.printStackTrace();
		} catch (SQLException e) {
			// B4: Xử lý lỗi kết nối database
			System.err.println("ERROR: Database connection failed!");
			e.printStackTrace();
		}
		// B5: Trả về connection (có thể là null nếu kết nối thất bại)
		return c;
	}

	/**
	 * B1: Đóng kết nối database khi không sử dụng B2: Kiểm tra connection không
	 * null trước khi đóng B3: Giải phóng tài nguyên, tránh memory leak
	 */
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