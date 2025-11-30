package example.dao;

import example.model.User;
import example.model.Role;

import java.sql.*;
import java.time.LocalDate;

public class UserDAO {

    // Đăng nhập
    public static User login(String emailOrUsername, String password) {
        String sql = "SELECT * FROM users WHERE (email = ? OR username = ?) AND password = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, emailOrUsername);
            ps.setString(2, emailOrUsername);
            ps.setString(3, password); // trong thực tế phải dùng BCrypt!

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapUser(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Lấy user theo ID (dùng cho booking history, profile...)
    public static User getById(int userId) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapUser(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Đăng ký tài khoản mới
    public static boolean register(User user) {
        String sql = """
            INSERT INTO users (username, email, password, phone_number, birth_date, gender, role)
            VALUES (?, ?, ?, ?, ?, ?, 'CUSTOMER')
            """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword()); // nên hash trước khi lưu
            ps.setString(4, user.getPhoneNumber());

            if (user.getBirthDate() != null) {
                ps.setDate(5, Date.valueOf(user.getBirthDate()));
            } else {
                ps.setNull(5, Types.DATE);
            }

            ps.setObject(6, user.getGender(), Types.BOOLEAN);

            int affected = ps.executeUpdate();
            if (affected > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    user.setId(rs.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Kiểm tra email hoặc username đã tồn tại chưa
    public static boolean isExists(String email, String username) {
        String sql = "SELECT 1 FROM users WHERE email = ? OR username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ps.setString(2, username);
            return ps.executeQuery().next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Helper: chuyển ResultSet → User object
    private static User mapUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setPhoneNumber(rs.getString("phone_number"));

        Date birth = rs.getDate("birth_date");
        user.setBirthDate(birth != null ? birth.toLocalDate() : null);

        Integer gender = (Integer) rs.getObject("gender");
        user.setGender(gender != null ? gender == 1 : null);

        String roleStr = rs.getString("role");
        user.setRole(roleStr != null ? Role.valueOf(roleStr) : Role.CUSTOMER);

        return user;
    }
}