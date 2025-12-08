package example.dao;

import example.model.Booking;
import example.model.BookingDetail;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO {

	// Lấy danh sách ID ghế đã đặt cho một suất chiếu
	public List<Integer> getBookedSeatIds(int showtimeId) {
		List<Integer> bookedSeatIds = new ArrayList<>();
		String sql = "SELECT bd.seat_id FROM bookingdetail bd " + "JOIN booking b ON bd.booking_id = b.booking_id "
				+ "WHERE b.showtime_id = ? AND b.status = 'Success'";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, showtimeId);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				bookedSeatIds.add(rs.getInt("seat_id"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return bookedSeatIds;
	}

	// Tạo booking mới
	public int createBooking(Booking booking) {
		String sql = "INSERT INTO booking (user_id, showtime_id, total_amount, status, created_at) "
				+ "VALUES (?, ?, ?, ?, NOW())";
		int bookingId = -1;

		try (Connection conn = DBConnection.getConnection();
				PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

			ps.setInt(1, booking.getUserId());
			ps.setInt(2, booking.getShowtimeId());
			ps.setDouble(3, booking.getTotalAmount());
			ps.setString(4, booking.getStatus());

			int affectedRows = ps.executeUpdate();

			if (affectedRows > 0) {
				ResultSet rs = ps.getGeneratedKeys();
				if (rs.next()) {
					bookingId = rs.getInt(1);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return bookingId;
	}

	// Thêm booking detail
	public boolean addBookingDetail(BookingDetail detail) {
		String sql = "INSERT INTO bookingdetail (booking_id, seat_id, price) VALUES (?, ?, ?)";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, detail.getBookingId());
			ps.setInt(2, detail.getSeatId());
			ps.setDouble(3, detail.getPrice());

			return ps.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	// Lấy seatId từ seatCode (A1, B2, ...)
	public int getSeatIdByCode(int roomId, String seatRow, int seatNumber) {
		String sql = "SELECT seat_id FROM seat WHERE room_id = ? AND seat_row = ? AND seat_number = ?";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, roomId);
			ps.setString(2, seatRow);
			ps.setInt(3, seatNumber);

			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getInt("seat_id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
}