package example.dao;

import example.model.Booking;
import example.model.Seat;
import example.model.SeatType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO {

	public List<Integer> getBookedSeatIds(int showtimeId) {
		List<Integer> seatIds = new ArrayList<>();
		String sql = "SELECT bd.seat_id FROM booking b " + "JOIN bookingdetail bd ON b.booking_id = bd.booking_id "
				+ "WHERE b.showtime_id = ? AND b.status != 'cancelled'";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, showtimeId);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				seatIds.add(rs.getInt("seat_id"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return seatIds;
	}

	public boolean isSeatBooked(int seatId, int showtimeId) {
		String sql = "SELECT COUNT(*) FROM booking b " + "JOIN bookingdetail bd ON b.booking_id = bd.booking_id "
				+ "WHERE b.showtime_id = ? AND bd.seat_id = ? AND b.status != 'cancelled'";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, showtimeId);
			ps.setInt(2, seatId);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				return rs.getInt(1) > 0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public int createBooking(Booking booking, String[] seatCodes, int roomId) {
		Connection conn = null;
		PreparedStatement psBooking = null;
		PreparedStatement psDetail = null;
		ResultSet rs = null;

		try {
			conn = DBConnection.getConnection();
			conn.setAutoCommit(false);

			SeatDAO seatDAO = new SeatDAO();
			double totalAmount = 0;
			List<Double> seatPrices = new ArrayList<>();
			List<Seat> seatList = new ArrayList<>();

			for (String seatCode : seatCodes) {
				Seat seat = seatDAO.getSeatByCode(seatCode, roomId);
				if (seat == null) {
					conn.rollback();
					return 0;
				}
				seatList.add(seat);
			}

			double basePrice = getBasePriceByShowtimeId(booking.getShowtimeId());
			for (Seat seat : seatList) {
				double seatPrice = calculateSeatPrice(seat, basePrice);
				seatPrices.add(seatPrice);
				totalAmount += seatPrice;
			}

			booking.setTotalAmount(totalAmount);

			String sqlBooking = "INSERT INTO booking (user_id, showtime_id, total_amount, status, created_at) "
					+ "VALUES (?, ?, ?, ?, ?)";
			psBooking = conn.prepareStatement(sqlBooking, Statement.RETURN_GENERATED_KEYS);
			psBooking.setInt(1, booking.getUserId());
			psBooking.setInt(2, booking.getShowtimeId());
			psBooking.setDouble(3, booking.getTotalAmount());
			psBooking.setString(4, booking.getStatus());
			psBooking.setTimestamp(5, booking.getBookingDate());

			int rowsAffected = psBooking.executeUpdate();
			if (rowsAffected == 0) {
				conn.rollback();
				return 0;
			}

			rs = psBooking.getGeneratedKeys();
			int bookingId = 0;
			if (rs.next()) {
				bookingId = rs.getInt(1);
			}

			String sqlDetail = "INSERT INTO bookingdetail (booking_id, seat_id, price) VALUES (?, ?, ?)";
			psDetail = conn.prepareStatement(sqlDetail);

			for (int i = 0; i < seatList.size(); i++) {
				Seat seat = seatList.get(i);
				psDetail.setInt(1, bookingId);
				psDetail.setInt(2, seat.getSeatId());
				psDetail.setDouble(3, seatPrices.get(i));
				psDetail.addBatch();
			}

			psDetail.executeBatch();
			conn.commit();
			return bookingId;

		} catch (SQLException e) {
			try {
				if (conn != null)
					conn.rollback();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			e.printStackTrace();
			return 0;
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (psBooking != null)
					psBooking.close();
				if (psDetail != null)
					psDetail.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private double calculateSeatPrice(Seat seat, double basePrice) {
		SeatDAO seatDAO = new SeatDAO();
		SeatType seatType = seatDAO.getSeatTypeById(seat.getSeatTypeId());
		if (seatType != null) {
			return basePrice + seatType.getSurcharge();
		}
		return basePrice;
	}

	private double getBasePriceByShowtimeId(int showtimeId) {
		String sql = "SELECT base_price FROM showtime WHERE showtime_id = ?";
		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, showtimeId);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getDouble("base_price");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	// Lấy thông tin booking theo ID
	public Booking getBookingById(int bookingId) {
		String sql = "SELECT * FROM booking WHERE booking_id = ?";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, bookingId);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				Booking booking = new Booking();
				booking.setBookingId(rs.getInt("booking_id"));
				booking.setUserId(rs.getInt("user_id"));
				booking.setShowtimeId(rs.getInt("showtime_id"));
				booking.setBookingDate(rs.getTimestamp("created_at"));
				booking.setTotalAmount(rs.getDouble("total_amount"));
				booking.setStatus(rs.getString("status"));
				booking.setCreatedAt(rs.getTimestamp("created_at"));
				return booking;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	// Lấy thời gian tạo booking (cho timeout check)
	public Timestamp getBookingCreatedTime(int bookingId) {
		String sql = "SELECT created_at FROM booking WHERE booking_id = ?";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, bookingId);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				return rs.getTimestamp("created_at");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	// Hủy booking
	public boolean cancelBooking(int bookingId) {
		String sql = "UPDATE booking SET status = 'Cancelled' WHERE booking_id = ?";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, bookingId);
			return ps.executeUpdate() > 0;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	// Lấy danh sách ghế đã đặt (JOIN với bảng seat để lấy seat_row và seat_number)
	public List<String> getSelectedSeats(int bookingId) {
		List<String> seats = new ArrayList<>();
		String sql = "SELECT s.seat_row, s.seat_number " + "FROM bookingdetail bd "
				+ "JOIN seat s ON bd.seat_id = s.seat_id " + "WHERE bd.booking_id = ? "
				+ "ORDER BY s.seat_row, s.seat_number";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, bookingId);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				String row = rs.getString("seat_row");
				int number = rs.getInt("seat_number");
				seats.add(row + number);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			System.err.println("Lỗi khi lấy danh sách ghế: " + e.getMessage());//
		}
		return seats;
	}

	// Lấy tất cả booking của user
	public List<Booking> getBookingsByUserId(int userId) {
		List<Booking> bookings = new ArrayList<>();
		String sql = "SELECT * FROM booking WHERE user_id = ? ORDER BY created_at DESC";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, userId);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Booking booking = new Booking();
				booking.setBookingId(rs.getInt("booking_id"));
				booking.setUserId(rs.getInt("user_id"));
				booking.setShowtimeId(rs.getInt("showtime_id"));
				booking.setBookingDate(rs.getTimestamp("created_at"));
				booking.setTotalAmount(rs.getDouble("total_amount"));
				booking.setStatus(rs.getString("status"));
				booking.setCreatedAt(rs.getTimestamp("created_at"));
				bookings.add(booking);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return bookings;
	}

	// Cập nhật status của booking
	public boolean updateBookingStatus(int bookingId, String status) {
		String sql = "UPDATE booking SET status = ? WHERE booking_id = ?";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setString(1, status);
			ps.setInt(2, bookingId);
			return ps.executeUpdate() > 0;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	// Kiểm tra xem booking có còn trong thời gian timeout không
	public boolean isBookingExpired(int bookingId, int timeoutMinutes) {
		Timestamp createdAt = getBookingCreatedTime(bookingId);
		if (createdAt == null)
			return true;

		long diffInMinutes = (System.currentTimeMillis() - createdAt.getTime()) / (60 * 1000);
		return diffInMinutes > timeoutMinutes;
	}

	// Lấy tất cả booking pending quá timeout
	public List<Integer> getExpiredPendingBookings(int timeoutMinutes) {
		List<Integer> expiredBookings = new ArrayList<>();
		String sql = "SELECT booking_id FROM booking " + "WHERE status = 'Pending' "
				+ "AND TIMESTAMPDIFF(MINUTE, created_at, NOW()) > ?";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, timeoutMinutes);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				expiredBookings.add(rs.getInt("booking_id"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return expiredBookings;
	}
}