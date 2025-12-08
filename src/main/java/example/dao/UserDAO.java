package example.dao;

import example.model.User;
import java.sql.*;

public class UserDAO {

	// Kiểm tra đăng nhập
	public User login(String email, String password) {
		String sql = "SELECT * FROM User WHERE email = ? AND password = ?";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setString(1, email);
			ps.setString(2, password);

			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return new User(rs.getInt("user_id"), rs.getString("email"), rs.getString("password"),
						rs.getString("full_name"), rs.getString("gender"), rs.getString("phone"),
						rs.getTimestamp("created_at"), rs.getString("role"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	// Kiểm tra email đã tồn tại chưa
	public boolean checkEmailExists(String email) {
		String sql = "SELECT COUNT(*) FROM User WHERE email = ?";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setString(1, email);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				return rs.getInt(1) > 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	// Đăng ký user mới
	public boolean register(User user) {
		String sql = "INSERT INTO User (email, password, full_name, gender, phone, created_at, role) VALUES (?, ?, ?, ?, ?, ?, ?)";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setString(1, user.getEmail());
			ps.setString(2, user.getPassword());
			ps.setString(3, user.getFullName());
			ps.setString(4, user.getGender());
			ps.setString(5, user.getPhone());
			ps.setTimestamp(6, user.getCreatedAt());
			ps.setString(7, user.getRole());

			return ps.executeUpdate() > 0;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}