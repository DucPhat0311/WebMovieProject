package example.dao;

import example.model.User;
import example.model.Role;
import java.sql.*;

public class UserDAO {

	public User login(String email, String password) {
		System.out.println("=== LOGIN DEBUG START ===");
		System.out.println("Input - Email: '" + email + "', Password: '" + password + "'");

		User user = null;
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = JDBCUtil.getConnection();
			System.out.println("Database connection: " + (conn != null ? "SUCCESS" : "FAILED"));

			// SQL với debug
			String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
			System.out.println("SQL: " + sql);

			stmt = conn.prepareStatement(sql);
			stmt.setString(1, email);
			stmt.setString(2, password);
			System.out.println("Parameters set - Email: '" + email + "', Password: '" + password + "'");

			rs = stmt.executeQuery();

			boolean hasResult = rs.next();
			System.out.println("Query has result: " + hasResult);

			if (hasResult) {
				user = new User();
				user.setId(rs.getInt("id"));
				user.setUsername(rs.getString("username"));
				user.setEmail(rs.getString("email"));
				user.setPassword(rs.getString("password"));

				// Xử lý birthDate
				java.sql.Date birthDate = rs.getDate("birth_date");
				if (birthDate != null) {
					user.setBirthDate(birthDate.toLocalDate());
				}

				user.setGender(rs.getBoolean("gender"));
				user.setPhoneNumber(rs.getString("phone_number"));

				// QUAN TRỌNG: SET ROLE
				String roleString = rs.getString("role");
				System.out.println("Role from database: " + roleString);

				if (roleString != null) {
					Role role = Role.valueOf(roleString);
					user.setRole(role);
					System.out.println("Role set: " + role);
				} else {
					System.err.println("WARNING: Role is null in database!");
					user.setRole(Role.CUSTOMER); // Default role
				}

				System.out.println("User found - ID: " + user.getId() + ", Username: " + user.getUsername());
			} else {
				System.out.println("No user found with these credentials");
			}

		} catch (SQLException e) {
			System.err.println("SQL ERROR: " + e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			JDBCUtil.closeConnection(conn);
		}

		System.out.println("Returning user: " + (user != null ? user.getUsername() : "null"));
		System.out.println("=== LOGIN DEBUG END ===");
		return user;
	}

	public boolean register(User user) {
	    Connection conn = null;
	    PreparedStatement stmt = null;

	    try {
	        conn = JDBCUtil.getConnection();

	        // Kiểm tra email và username tồn tại
	        if (isEmailExists(conn, user.getEmail()) || isUsernameExists(conn, user.getUsername())) {
	            return false;
	        }

	        // SỬA: Đúng thứ tự database
	        String sql = "INSERT INTO users (username, birth_date, gender, phone_number, email, password, role) " +
	                    "VALUES (?, ?, ?, ?, ?, ?, ?)";

	        stmt = conn.prepareStatement(sql);
	        stmt.setString(1, user.getUsername());
	        
	        // birth_date
	        if (user.getBirthDate() != null) {
	            stmt.setDate(2, java.sql.Date.valueOf(user.getBirthDate()));
	        } else {
	            stmt.setNull(2, java.sql.Types.DATE);
	        }
	        
	        // gender
	        if (user.getGender() != null) {
	            stmt.setBoolean(3, user.getGender());
	        } else {
	            stmt.setNull(3, java.sql.Types.BOOLEAN);
	        }
	        
	        stmt.setString(4, user.getPhoneNumber());
	        stmt.setString(5, user.getEmail());
	        stmt.setString(6, user.getPassword());        // password
	        stmt.setString(7, user.getRole().name());     // role

	        int rowsAffected = stmt.executeUpdate();
	        return rowsAffected > 0;

	    } catch (SQLException e) {
	        e.printStackTrace();
	        return false;
	    } finally {
	        // close resources
	    }
	}

	private boolean isEmailExists(Connection conn, String email) throws SQLException {
		String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setString(1, email);
		ResultSet rs = stmt.executeQuery();
		rs.next();
		int count = rs.getInt(1);
		rs.close();
		stmt.close();
		return count > 0;
	}

	private boolean isUsernameExists(Connection conn, String username) throws SQLException {
		String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setString(1, username);
		ResultSet rs = stmt.executeQuery();
		rs.next();
		int count = rs.getInt(1);
		rs.close();
		stmt.close();
		return count > 0;
	}
}