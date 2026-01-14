package example.dao.impl;

import example.dao.core.DBConnection;
import example.model.cinema.Seat;
import example.model.cinema.SeatType;
import example.model.system.User;
import example.model.transaction.Booking;
import example.model.transaction.Payment;
import example.util.Constant;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class BookingDAO {

	// TẠO BOOKING MỚI (CÓ CHECK TRÙNG GHẾ & TÍNH TIỀN SECURE)
	public int createBookingWithSeats(Booking booking, List<String> seatCodes, int roomId) {
		Connection conn = null;
		PreparedStatement psCheck = null;
		PreparedStatement psBooking = null;
		PreparedStatement psDetail = null;
		ResultSet rs = null;

		try {
			conn = DBConnection.getConnection();
			conn.setAutoCommit(false); // Bắt đầu Transaction

			// --- BƯỚC 1: Check concurrency - Tìm ghế trùng ---
			StringBuilder sqlCheck = new StringBuilder();
			sqlCheck.append("SELECT s.seat_row, s.seat_number FROM bookingdetail bd ");
			sqlCheck.append("JOIN booking b ON bd.booking_id = b.booking_id ");
			sqlCheck.append("JOIN seat s ON bd.seat_id = s.seat_id ");
			sqlCheck.append("WHERE b.showtime_id = ? ");
			sqlCheck.append("AND b.status IN (?, ?) "); // Check cả PENDING và SUCCESS

			sqlCheck.append("AND CONCAT(s.seat_row, s.seat_number) IN (");
			for (int i = 0; i < seatCodes.size(); i++) {
				sqlCheck.append(i == 0 ? "?" : ",?");
			}
			sqlCheck.append(")");

			psCheck = conn.prepareStatement(sqlCheck.toString());

			int paramIndex = 1;
			psCheck.setInt(paramIndex++, booking.getShowtimeId());
			psCheck.setString(paramIndex++, Constant.BOOKING_PENDING);
			psCheck.setString(paramIndex++, Constant.BOOKING_SUCCESS);

			for (String seatCode : seatCodes) {
				psCheck.setString(paramIndex++, seatCode);
			}

			rs = psCheck.executeQuery();
			if (rs.next()) {
				conn.rollback();
				return -1; // Có ghế đã bị người khác đặt
			}

			double basePrice = getBasePriceByShowtimeId(conn, booking.getShowtimeId());
			double realTotalAmount = 0;
			SeatDAO seatDAO = new SeatDAO();

			List<SeatPriceDTO> seatDetailsToInsert = new ArrayList<>();

			for (String code : seatCodes) {
				Seat seat = seatDAO.getSeatByCode(code, roomId);

				if (seat == null) {
					conn.rollback();
					return 0;
				}

				double seatPrice = calculateSeatPrice(seat, basePrice);
				realTotalAmount += seatPrice;

				seatDetailsToInsert.add(new SeatPriceDTO(seat.getSeatId(), seatPrice));
			}

			// --- BƯỚC 3: Insert Booking ---
			String sqlBooking = "INSERT INTO booking (user_id, showtime_id, total_amount, status, created_at) VALUES (?, ?, ?, ?, ?)";
			psBooking = conn.prepareStatement(sqlBooking, Statement.RETURN_GENERATED_KEYS);
			psBooking.setInt(1, booking.getUserId());
			psBooking.setInt(2, booking.getShowtimeId());
			psBooking.setDouble(3, realTotalAmount);
			psBooking.setString(4, Constant.BOOKING_PENDING);
			psBooking.setTimestamp(5, new Timestamp(System.currentTimeMillis()));

			int affectedRows = psBooking.executeUpdate();
			if (affectedRows == 0) {
				conn.rollback();
				return 0;
			}

			int bookingId = 0;
			try (ResultSet generatedKeys = psBooking.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					bookingId = generatedKeys.getInt(1);
				} else {
					conn.rollback();
					return 0;
				}
			}

			// --- BƯỚC 4: Insert Booking Detail ---
			String sqlDetail = "INSERT INTO bookingdetail (booking_id, seat_id, price) VALUES (?, ?, ?)";
			psDetail = conn.prepareStatement(sqlDetail);

			for (SeatPriceDTO dto : seatDetailsToInsert) {
				psDetail.setInt(1, bookingId);
				psDetail.setInt(2, dto.seatId);
				psDetail.setDouble(3, dto.price);
				psDetail.addBatch();
			}

			psDetail.executeBatch();
			conn.commit(); // Hoàn tất Transaction
			return bookingId;

		} catch (SQLException e) {
			e.printStackTrace();
			if (conn != null) {
				try {
					conn.rollback();
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			}
			return 0;
		} finally {
			closeResources(rs, psCheck, psBooking, psDetail, conn);
		}
	}

	// LẤY ID GHẾ ĐÃ ĐẶT
	public List<Integer> getBookedSeatIds(int showtimeId) {
		List<Integer> bookedSeatIds = new ArrayList<>();
		String sql = "SELECT bd.seat_id FROM bookingdetail bd " + "JOIN booking b ON bd.booking_id = b.booking_id "
				+ "WHERE b.showtime_id = ? AND b.status IN (?, ?)";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, showtimeId);
			ps.setString(2, Constant.BOOKING_PENDING);
			ps.setString(3, Constant.BOOKING_SUCCESS);

			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				bookedSeatIds.add(rs.getInt("seat_id"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return bookedSeatIds;
	}

	public List<String> getSelectedSeats(int bookingId) {
		List<String> seats = new ArrayList<>();
		String sql = "SELECT s.seat_row, s.seat_number FROM bookingdetail bd "
				+ "JOIN seat s ON bd.seat_id = s.seat_id WHERE bd.booking_id = ?";
		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, bookingId);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				seats.add(rs.getString("seat_row") + rs.getInt("seat_number"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return seats;
	}

	// CÁC HÀM XỬ LÝ CHECKOUT / TIMEOUT

	public java.sql.Timestamp getBookingCreatedTime(Integer bookingId) {
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

	public boolean isBookingExpired(Integer bookingId, int timeoutMinutes) {
		java.sql.Timestamp createdAt = getBookingCreatedTime(bookingId);
		if (createdAt == null)
			return true;

		long currentTime = System.currentTimeMillis();
		long bookingTime = createdAt.getTime();
		long diffMinutes = (currentTime - bookingTime) / (60 * 1000);

		return diffMinutes >= timeoutMinutes;
	}

	public boolean cancelBookingAndReleaseSeats(Integer bookingId) {
		return updateBookingStatus(bookingId, Constant.BOOKING_CANCELLED);
	}

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

	public void cancelExpiredPendingBookings(int timeoutMinutes) {

		String selectSql = "SELECT booking_id, created_at FROM booking WHERE status = ?";
		String updateSql = "UPDATE booking SET status = ? WHERE booking_id = ?";

		try (Connection conn = DBConnection.getConnection();
				PreparedStatement psSelect = conn.prepareStatement(selectSql);
				PreparedStatement psUpdate = conn.prepareStatement(updateSql)) {

			psSelect.setString(1, Constant.BOOKING_PENDING);
			ResultSet rs = psSelect.executeQuery();

			int cancelledCount = 0;
			long timeoutMillis = timeoutMinutes * 60 * 1000L;
			long currentTime = System.currentTimeMillis();

			while (rs.next()) {
				int bookingId = rs.getInt("booking_id");
				Timestamp createdAt = rs.getTimestamp("created_at");

				long elapsedMillis = currentTime - createdAt.getTime();

				if (elapsedMillis >= timeoutMillis) {
					psUpdate.setString(1, Constant.BOOKING_CANCELLED);
					psUpdate.setInt(2, bookingId);
					psUpdate.executeUpdate();
					cancelledCount++;
				}
			}

			if (cancelledCount > 0) {
				System.out.println("[" + new java.util.Date() + "] Đã hủy " + cancelledCount + " booking quá hạn");
			}

		} catch (SQLException e) {
			System.err.println("Lỗi khi dọn dẹp booking quá hạn: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public boolean isSeatBooked(int seatId, int showtimeId) {
		String sql = "SELECT COUNT(*) FROM booking b " + "JOIN bookingdetail bd ON b.booking_id = bd.booking_id "
				+ "WHERE b.showtime_id = ? AND bd.seat_id = ? AND b.status IN (?, ?)";
		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, showtimeId);
			ps.setInt(2, seatId);
			ps.setString(3, Constant.BOOKING_PENDING);
			ps.setString(4, Constant.BOOKING_SUCCESS);
			ResultSet rs = ps.executeQuery();
			if (rs.next())
				return rs.getInt(1) > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	public Booking getBookingById(int bookingId) {
		Booking booking = null;
		String sql = "SELECT * FROM booking WHERE booking_id = ?";
		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, bookingId);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				booking = new Booking();
				booking.setBookingId(rs.getInt("booking_id"));
				booking.setUserId(rs.getInt("user_id"));
				booking.setShowtimeId(rs.getInt("showtime_id"));
				booking.setTotalAmount(rs.getDouble("total_amount"));
				booking.setStatus(rs.getString("status"));
				booking.setCreatedAt(rs.getTimestamp("created_at"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return booking;
	}

	public boolean completeCheckout(int bookingId, Payment payment) {
		Connection conn = null;
		PreparedStatement psPayment = null;
		PreparedStatement psUpdateBooking = null;

		String sqlPayment = "INSERT INTO payment (booking_id, method, status, amount, paid_at) VALUES (?, ?, ?, ?, ?)";

		String sqlUpdate = "UPDATE booking SET status = ? WHERE booking_id = ? AND status = ?";

		try {
			conn = DBConnection.getConnection();
			conn.setAutoCommit(false); // 1. Bắt đầu Transaction

			// Bước 1: Insert Payment
			psPayment = conn.prepareStatement(sqlPayment);
			psPayment.setInt(1, bookingId);
			psPayment.setString(2, payment.getPaymentMethod());
			psPayment.setString(3, payment.getStatus());
			psPayment.setDouble(4, payment.getAmount());
			psPayment.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
			psPayment.executeUpdate();

			// Bước 2: Update Booking Status
			psUpdateBooking = conn.prepareStatement(sqlUpdate);
			psUpdateBooking.setString(1, Constant.BOOKING_SUCCESS);
			psUpdateBooking.setInt(2, bookingId);
			psUpdateBooking.setString(3, Constant.BOOKING_PENDING);

			int rowsUpdated = psUpdateBooking.executeUpdate();

			if (rowsUpdated > 0) {
				conn.commit();
				return true;
			} else {
				conn.rollback();
				return false;
			}

		} catch (SQLException e) {
			try {
				if (conn != null)
					conn.rollback();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (psPayment != null)
					psPayment.close();
			} catch (Exception e) {
			}
			try {
				if (psUpdateBooking != null)
					psUpdateBooking.close();
			} catch (Exception e) {
			}
			try {
				if (conn != null)
					conn.close();
			} catch (Exception e) {
			}
		}
	}

	private double calculateSeatPrice(Seat seat, double basePrice) {
		SeatDAO seatDAO = new SeatDAO();
		SeatType seatType = seatDAO.getSeatTypeById(seat.getSeatTypeId());
		return (seatType != null) ? basePrice + seatType.getSurcharge() : basePrice;
	}

	private double getBasePriceByShowtimeId(Connection conn, int showtimeId) throws SQLException {
		String sql = "SELECT base_price FROM showtime WHERE showtime_id = ?";
		boolean isNewConnection = (conn == null);
		Connection localConn = isNewConnection ? DBConnection.getConnection() : conn;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = localConn.prepareStatement(sql);
			ps.setInt(1, showtimeId);
			rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getDouble("base_price");
			}
		} finally {
			if (rs != null)
				rs.close();
			if (ps != null)
				ps.close();
			if (isNewConnection && localConn != null)
				localConn.close();
		}
		return 0;
	}

	private void closeResources(ResultSet rs, PreparedStatement ps1, PreparedStatement ps2, PreparedStatement ps3,
			Connection conn) {
		try {
			if (rs != null)
				rs.close();
		} catch (Exception e) {
		}
		try {
			if (ps1 != null)
				ps1.close();
		} catch (Exception e) {
		}
		try {
			if (ps2 != null)
				ps2.close();
		} catch (Exception e) {
		}
		try {
			if (ps3 != null)
				ps3.close();
		} catch (Exception e) {
		}
		try {
			if (conn != null)
				conn.close();
		} catch (Exception e) {
		}
	}

	// DANH SÁCH & TÌM KIẾM

	public List<Booking> getAllBookings() {
		List<Booking> bookings = new ArrayList<>();
		String sql = "SELECT b.*, u.full_name, u.email, u.phone, m.title, "
				+ "c.cinema_name, r.room_name, st.show_date, st.start_time " + "FROM booking b "
				+ "JOIN user u ON b.user_id = u.user_id " + "JOIN showtime st ON b.showtime_id = st.showtime_id "
				+ "JOIN movie m ON st.movie_id = m.movie_id " + "JOIN room r ON st.room_id = r.room_id "
				+ "JOIN cinema c ON r.cinema_id = c.cinema_id " + "ORDER BY b.created_at DESC";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				bookings.add(extractBookingFromResultSet(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return bookings;
	}

	public List<Booking> getBookingsByStatus(String status) {
		List<Booking> bookings = new ArrayList<>();
		String sql = "SELECT b.*, u.full_name, u.email, u.phone, m.title, "
				+ "c.cinema_name, r.room_name, st.show_date, st.start_time " + "FROM booking b "
				+ "JOIN user u ON b.user_id = u.user_id " + "JOIN showtime st ON b.showtime_id = st.showtime_id "
				+ "JOIN movie m ON st.movie_id = m.movie_id " + "JOIN room r ON st.room_id = r.room_id "
				+ "JOIN cinema c ON r.cinema_id = c.cinema_id " + "WHERE b.status = ? " + "ORDER BY b.created_at DESC";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, status);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				bookings.add(extractBookingFromResultSet(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return bookings;
	}

	public List<Booking> searchBookings(String keyword) {
		List<Booking> bookings = new ArrayList<>();
		String sql = "SELECT b.*, u.full_name, u.email, u.phone, m.title, "
				+ "c.cinema_name, r.room_name, st.show_date, st.start_time " + "FROM booking b "
				+ "JOIN user u ON b.user_id = u.user_id " + "JOIN showtime st ON b.showtime_id = st.showtime_id "
				+ "JOIN movie m ON st.movie_id = m.movie_id " + "JOIN room r ON st.room_id = r.room_id "
				+ "JOIN cinema c ON r.cinema_id = c.cinema_id "
				+ "WHERE b.booking_id LIKE ? OR u.full_name LIKE ? OR u.email LIKE ? OR m.title LIKE ? "
				+ "ORDER BY b.created_at DESC";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			String kw = "%" + keyword + "%";
			ps.setString(1, kw);
			ps.setString(2, kw);
			ps.setString(3, kw);
			ps.setString(4, kw);

			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				bookings.add(extractBookingFromResultSet(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return bookings;
	}

	public List<Booking> getBookingsByDateRange(Date fromDate, Date toDate) {
		List<Booking> bookings = new ArrayList<>();
		String sql = "SELECT b.*, u.full_name, u.email, u.phone, m.title, "
				+ "c.cinema_name, r.room_name, st.show_date, st.start_time " + "FROM booking b "
				+ "JOIN user u ON b.user_id = u.user_id " + "JOIN showtime st ON b.showtime_id = st.showtime_id "
				+ "JOIN movie m ON st.movie_id = m.movie_id " + "JOIN room r ON st.room_id = r.room_id "
				+ "JOIN cinema c ON r.cinema_id = c.cinema_id " + "WHERE DATE(b.created_at) BETWEEN ? AND ? "
				+ "ORDER BY b.created_at DESC";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setDate(1, new java.sql.Date(fromDate.getTime()));
			ps.setDate(2, new java.sql.Date(toDate.getTime()));
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				bookings.add(extractBookingFromResultSet(rs));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return bookings;
	}

	public int getTotalSeatsByBooking(int bookingId) {
		String sql = "SELECT COUNT(*) as total_seats FROM bookingdetail WHERE booking_id = ?";
		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, bookingId);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getInt("total_seats");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	private Booking extractBookingFromResultSet(ResultSet rs) throws SQLException {
		Booking booking = new Booking();
		booking.setBookingId(rs.getInt("booking_id"));
		booking.setUserId(rs.getInt("user_id"));
		booking.setShowtimeId(rs.getInt("showtime_id"));
		booking.setBookingDate(rs.getTimestamp("created_at"));
		booking.setTotalAmount(rs.getDouble("total_amount"));
		booking.setStatus(rs.getString("status"));
		booking.setCreatedAt(rs.getTimestamp("created_at"));

		User user = new User();
		user.setFullName(rs.getString("full_name"));
		user.setEmail(rs.getString("email"));
		user.setPhone(rs.getString("phone"));

		return booking;
	}

	// THỐNG KÊ DASHBOARD

	public double getTodayRevenue() {
		String sql = "SELECT COALESCE(SUM(total_amount), 0) as revenue " + "FROM booking "
				+ "WHERE DATE(created_at) = CURDATE() AND status = ?";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, Constant.BOOKING_SUCCESS);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getDouble("revenue");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public double getTotalRevenue() {
		String sql = "SELECT COALESCE(SUM(total_amount), 0) as total_revenue " + "FROM booking " + "WHERE status = ?";
		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, Constant.BOOKING_SUCCESS);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getDouble("total_revenue");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public int getTodayBookingsCount() {
		String sql = "SELECT COUNT(*) as count FROM booking WHERE DATE(created_at) = CURDATE()";
		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getInt("count");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public int getBookingCountByStatus(String status) {
		String sql = "SELECT COUNT(*) as count FROM booking WHERE status = ? AND DATE(created_at) = CURDATE()";
		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setString(1, status);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return rs.getInt("count");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public List<Booking> getTodayBookings() {
		List<Booking> bookings = new ArrayList<>();
		String sql = "SELECT * FROM booking WHERE DATE(created_at) = CURDATE() ORDER BY created_at DESC LIMIT 10";
		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Booking booking = new Booking();
				booking.setBookingId(rs.getInt("booking_id"));
				booking.setUserId(rs.getInt("user_id"));
				booking.setShowtimeId(rs.getInt("showtime_id"));
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

	private static class SeatPriceDTO {
		int seatId;
		double price;

		SeatPriceDTO(int seatId, double price) {
			this.seatId = seatId;
			this.price = price;
		}
	}
}