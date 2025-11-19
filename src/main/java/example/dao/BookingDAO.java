package example.dao;

import example.model.Booking;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

public class BookingDAO {

	public int createBooking(Booking booking) throws SQLException {
		String sql = "INSERT INTO bookings (user_id, showtime_id, booking_date) VALUES (?, ?, ?)";

		try (Connection conn = JDBCUtil.getConnection();
				PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

			stmt.setInt(1, booking.getUserId());
			stmt.setInt(2, booking.getShowtimeId());

			// Convert LocalDateTime to Timestamp
			if (booking.getBookingDate() != null) {
				stmt.setTimestamp(3, Timestamp.valueOf(booking.getBookingDate()));
			} else {
				stmt.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
			}

			int affectedRows = stmt.executeUpdate();

			if (affectedRows > 0) {
				ResultSet rs = stmt.getGeneratedKeys();
				if (rs.next()) {
					return rs.getInt(1);
				}
			}
		}
		return -1;
	}

	public Booking getBookingById(int bookingId) throws SQLException {
		String sql = "SELECT * FROM bookings WHERE id = ?";

		try (Connection conn = JDBCUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, bookingId);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				Booking booking = new Booking();
				booking.setId(rs.getInt("id"));
				booking.setUserId(rs.getInt("user_id"));
				booking.setShowtimeId(rs.getInt("showtime_id"));

				Timestamp timestamp = rs.getTimestamp("booking_date");
				if (timestamp != null) {
					booking.setBookingDate(timestamp.toLocalDateTime());
				}

				return booking;
			}
		}
		return null;
	}
}