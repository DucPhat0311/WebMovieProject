package example.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import example.dao.core.DBConnection;
import example.model.system.User;

public class UserDAO {

	public boolean emailExists(String email) {
		String sql = "SELECT COUNT(*) FROM user WHERE email = ?";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, email);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				return rs.getInt(1) > 0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean register(User user) {
		String sql = "INSERT INTO user (full_name, email, password, phone, gender, role) VALUES (?, ?, ?, ?, ?, 'user')";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, user.getFullName());
			stmt.setString(2, user.getEmail());
			stmt.setString(3, user.getPassword());
			stmt.setString(4, user.getPhone());
			stmt.setString(5, user.getGender());

			int rowsAffected = stmt.executeUpdate();
			return rowsAffected > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	public User login(String email, String password) {
		String sql = "SELECT * FROM user WHERE email = ? AND password = ?";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setString(1, email);
			stmt.setString(2, password);

			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				User user = new User();
				user.setUserId(rs.getInt("user_id"));
				user.setFullName(rs.getString("full_name"));
				user.setEmail(rs.getString("email"));
				user.setPhone(rs.getString("phone"));
				user.setGender(rs.getString("gender"));
				user.setRole(rs.getString("role"));
				user.setCreatedAt(rs.getTimestamp("created_at"));
				return user;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public User getUserById(int userId) {
		String sql = "SELECT * FROM user WHERE user_id = ?";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, userId);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				User user = new User();
				user.setUserId(rs.getInt("user_id"));
				user.setFullName(rs.getString("full_name"));
				user.setEmail(rs.getString("email"));
				user.setPhone(rs.getString("phone"));
				user.setGender(rs.getString("gender"));
				user.setRole(rs.getString("role"));
				user.setCreatedAt(rs.getTimestamp("created_at"));
				return user;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public User findByEmail(String email) {
	    User user = null;
	    String sql = "SELECT * FROM user WHERE email = ?"; 

	    try {
	        Connection conn = new DBConnection().getConnection(); 
	        PreparedStatement ps = conn.prepareStatement(sql);
	        ps.setString(1, email);
	        ResultSet rs = ps.executeQuery();

	        if (rs.next()) {
	            user = new User();
	            user.setUserId(rs.getInt("user_id"));
	            user.setEmail(rs.getString("email"));
	            user.setPassword(rs.getString("password"));
	            user.setRole(rs.getString("role"));
	            user.setFullName(rs.getString("full_name"));
	        }
	        conn.close();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return user;
	}
}