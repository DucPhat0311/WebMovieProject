//package example.dao;
//
//import example.model.User;
//import example.model.Role;
//import java.sql.*;
//
//public class UserDAO {
//
//	/**
//	 * ĐĂNG NHẬP: Kiểm tra thông tin đăng nhập B1: Kết nối database B2: Tạo câu SQL
//	 * SELECT với tham số email và password B3: Thực thi query và xử lý kết quả B4:
//	 * Map dữ liệu từ ResultSet sang User object B5: Đóng tất cả resources và trả về
//	 * user
//	 */
//	public User login(String email, String password) {
//		User user = null;
//		Connection conn = null;
//		PreparedStatement stmt = null;
//		ResultSet rs = null;
//
//		try {
//			// B1: Lấy connection từ JDBCUtil
//			conn = JDBCUtil.getConnection();
//
//			// B2: Tạo câu SQL với tham số để tránh SQL Injection
//			String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
//
//			// B3: Tạo PreparedStatement và set tham số
//			stmt = conn.prepareStatement(sql);
//			stmt.setString(1, email);
//			stmt.setString(2, password);
//
//			// B4: Thực thi query và nhận ResultSet
//			rs = stmt.executeQuery();
//
//			// B5: Kiểm tra và xử lý kết quả
//			if (rs.next()) {
//				user = new User();
//				// B6: Map dữ liệu từ database sang User object
//				user.setId(rs.getInt("id"));
//				user.setUsername(rs.getString("username"));
//				user.setEmail(rs.getString("email"));
//				user.setPassword(rs.getString("password"));
//
//				// B7: Xử lý các field có thể null
//				java.sql.Date birthDate = rs.getDate("birth_date");
//				if (birthDate != null) {
//					user.setBirthDate(birthDate.toLocalDate());
//				}
//
//				user.setGender(rs.getBoolean("gender"));
//				user.setPhoneNumber(rs.getString("phone_number"));
//
//				// B8: Xử lý role - quan trọng cho phân quyền
//				String roleString = rs.getString("role");
//				if (roleString != null) {
//					user.setRole(Role.valueOf(roleString));
//				} else {
//					System.err.println("WARNING: Role is null for user: " + email);
//					user.setRole(Role.CUSTOMER); // Default role
//				}
//
//				System.out.println("LOGIN SUCCESS: " + user.getUsername());
//			}
//
//		} catch (SQLException e) {
//			// B9: Xử lý lỗi SQL
//			System.err.println("LOGIN ERROR: " + e.getMessage());
//		} finally {
//			// B10: LUÔN đóng resources trong finally block
//			try {
//				if (rs != null)
//					rs.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//			try {
//				if (stmt != null)
//					stmt.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//			JDBCUtil.closeConnection(conn);
//		}
//		return user;
//	}
//
//	/**
//	 * ĐĂNG KÝ: Thêm user mới vào database B1: Kiểm tra email/username đã tồn tại
//	 * chưa B2: Tạo câu SQL INSERT với đúng thứ tự cột B3: Set các tham số cho
//	 * PreparedStatement B4: Xử lý các giá trị có thể null B5: Thực thi update và
//	 * trả về kết quả
//	 */
//	public boolean register(User user) {
//		Connection conn = null;
//		PreparedStatement stmt = null;
//
//		try {
//			// B1: Kết nối database
//			conn = JDBCUtil.getConnection();
//
//			// B2: Kiểm tra trùng email và username
//			if (isEmailExists(conn, user.getEmail()) || isUsernameExists(conn, user.getUsername())) {
//				System.out.println("REGISTER FAILED: Email or username already exists - " + user.getEmail());
//				return false;
//			}
//
//			// B3: Tạo câu SQL INSERT - QUAN TRỌNG: đúng thứ tự cột trong database
//			String sql = "INSERT INTO users (username, birth_date, gender, phone_number, email, password, role) "
//					+ "VALUES (?, ?, ?, ?, ?, ?, ?)";
//
//			// B4: Tạo PreparedStatement
//			stmt = conn.prepareStatement(sql);
//
//			// B5: Set các tham số theo đúng thứ tự trong SQL
//			stmt.setString(1, user.getUsername());
//
//			// B6: Xử lý birth_date - có thể null
//			if (user.getBirthDate() != null) {
//				stmt.setDate(2, java.sql.Date.valueOf(user.getBirthDate()));
//			} else {
//				stmt.setNull(2, java.sql.Types.DATE);
//			}
//
//			// B7: Xử lý gender - có thể null
//			if (user.getGender() != null) {
//				stmt.setBoolean(3, user.getGender());
//			} else {
//				stmt.setNull(3, java.sql.Types.BOOLEAN);
//			}
//
//			// B8: Set các tham số còn lại
//			stmt.setString(4, user.getPhoneNumber());
//			stmt.setString(5, user.getEmail());
//			stmt.setString(6, user.getPassword());
//			stmt.setString(7, user.getRole().name());
//
//			// B9: Thực thi INSERT và nhận số dòng bị ảnh hưởng
//			int rowsAffected = stmt.executeUpdate();
//
//			// B10: Log kết quả thành công
//			if (rowsAffected > 0) {
//				System.out.println("REGISTER SUCCESS: " + user.getUsername());
//			}
//
//			// B11: Trả về true nếu insert thành công (1 dòng được thêm)
//			return rowsAffected > 0;
//
//		} catch (SQLException e) {
//			// B12: Xử lý lỗi SQL
//			System.err.println("REGISTER ERROR: " + e.getMessage());
//			return false;
//		} finally {
//			// B13: Đóng resources
//			try {
//				if (stmt != null)
//					stmt.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//			JDBCUtil.closeConnection(conn);
//		}
//	}
//
//	/**
//	 * KIỂM TRA EMAIL TỒN TẠI B1: Tạo query đếm số user có email trùng B2: Thực thi
//	 * query và trả về kết quả
//	 */
//	private boolean isEmailExists(Connection conn, String email) throws SQLException {
//		String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
//		PreparedStatement stmt = conn.prepareStatement(sql);
//		stmt.setString(1, email);
//		ResultSet rs = stmt.executeQuery();
//		rs.next();
//		int count = rs.getInt(1);
//		rs.close();
//		stmt.close();
//		return count > 0;
//	}
//
//	/**
//	 * KIỂM TRA USERNAME TỒN TẠI B1: Tạo query đếm số user có username trùng B2:
//	 * Thực thi query và trả về kết quả
//	 */
//	private boolean isUsernameExists(Connection conn, String username) throws SQLException {
//		String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
//		PreparedStatement stmt = conn.prepareStatement(sql);
//		stmt.setString(1, username);
//		ResultSet rs = stmt.executeQuery();
//		rs.next();
//		int count = rs.getInt(1);
//		rs.close();
//		stmt.close();
//		return count > 0;
//	}
//}