
package example.dao;

import example.model.Booking;
import example.model.Showtime;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

public class BookingDAO {
	public static boolean createBooking(int userId, int showtimeId, List<Integer> seatIds) {
		Connection conn = null;
		try {
			conn = DBConnection.getConnection();
			conn.setAutoCommit(false);

			// 1. Tạo booking
			String sqlBooking = "INSERT INTO bookings (user_id, showtime_id, total_amount, status) VALUES (?, ?, 0, 'PENDING')";
			PreparedStatement psb = conn.prepareStatement(sqlBooking, Statement.RETURN_GENERATED_KEYS);
			psb.setInt(1, userId);
			psb.setInt(2, showtimeId);
			psb.executeUpdate();

			ResultSet rs = psb.getGeneratedKeys();
			int bookingId = rs.next() ? rs.getInt(1) : 0;

			// 2. Tạo tickets + kiểm tra trùng
			String sqlTicket = "INSERT IGNORE INTO tickets (booking_id, seat_id, showtime_id) VALUES (?, ?, ?)";
			PreparedStatement pst = conn.prepareStatement(sqlTicket);
			for (int seatId : seatIds) {
				pst.setInt(1, bookingId);
				pst.setInt(2, seatId);
				pst.setInt(3, showtimeId);
				pst.addBatch();
			}
			int[] results = pst.executeBatch();

			// Nếu có ghế bị trùng → rollback
			for (int r : results) {
				if (r == 0) { // INSERT IGNORE trả về 0 nếu bị unique key vi phạm
					conn.rollback();
					return false;
				}
			}

			// Cập nhật total_amount
			Showtime st = ShowtimeDAO.getByIdFull(showtimeId);
			BigDecimal total = st.getPrice().multiply(BigDecimal.valueOf(seatIds.size()));
			String update = "UPDATE bookings SET total_amount = ?, status = 'PAID' WHERE id = ?";
			PreparedStatement psu = conn.prepareStatement(update);
			psu.setBigDecimal(1, total);
			psu.setInt(2, bookingId);
			psu.executeUpdate();

			conn.commit();
			return true;

		} catch (SQLException e) {
			if (conn != null)
				try {
					conn.rollback();
				} catch (SQLException ex) {
				}
			e.printStackTrace();
			return false;
		} finally {
			if (conn != null)
				try {
					conn.setAutoCommit(true);
					conn.close();
				} catch (SQLException e) {
				}
		}
	}
}


