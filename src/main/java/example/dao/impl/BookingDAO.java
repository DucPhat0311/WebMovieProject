package example.dao.impl;

import example.dao.core.DBConnection;
import example.model.cinema.Seat;
import example.model.cinema.SeatType;
import example.model.system.User;
import example.model.transaction.Booking;
import example.util.Constant;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO {

	// ================ 1. TẠO BOOKING MỚI (CÓ CHECK TRÙNG GHẾ) ================
	public int createBookingWithSeats(Booking booking, List<String> seatCodes, int roomId) {
		Connection conn = null;
		PreparedStatement psCheck = null;
		PreparedStatement psBooking = null;
		PreparedStatement psDetail = null;
		ResultSet rs = null;

		try {
			conn = DBConnection.getConnection();
			conn.setAutoCommit(false); // Bắt đầu Transaction

			// Bước 1: Check concurrency - Tìm ghế trùng (Cả PENDING và SUCCESS đều tính là
			// đã đặt)
			StringBuilder sqlCheck = new StringBuilder();
			sqlCheck.append("SELECT s.seat_row, s.seat_number FROM bookingdetail bd ");
			sqlCheck.append("JOIN booking b ON bd.booking_id = b.booking_id ");
			sqlCheck.append("JOIN seat s ON bd.seat_id = s.seat_id ");
			sqlCheck.append("WHERE b.showtime_id = ? ");
			sqlCheck.append("AND b.status IN (?, ?) "); // Quan trọng: Check cả 2 trạng thái

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

			// Bước 2: Insert Booking
			String sqlBooking = "INSERT INTO booking (user_id, showtime_id, total_amount, status, created_at) VALUES (?, ?, ?, ?, ?)";
			psBooking = conn.prepareStatement(sqlBooking, Statement.RETURN_GENERATED_KEYS);
			psBooking.setInt(1, booking.getUserId());
			psBooking.setInt(2, booking.getShowtimeId());
			psBooking.setDouble(3, booking.getTotalAmount());
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

			// Bước 3: Insert Booking Detail (Từng ghế)
			String sqlDetail = "INSERT INTO bookingdetail (booking_id, seat_id, price) VALUES (?, ?, ?)";
			psDetail = conn.prepareStatement(sqlDetail);

			SeatDAO seatDAO = new SeatDAO();
			double basePrice = getBasePriceByShowtimeId(booking.getShowtimeId());

			for (String code : seatCodes) {
				Seat seat = seatDAO.getSeatByCode(code, roomId);
				if (seat != null) {
					double realPrice = calculateSeatPrice(seat, basePrice);
					psDetail.setInt(1, bookingId);
					psDetail.setInt(2, seat.getSeatId());
					psDetail.setDouble(3, realPrice);
					psDetail.addBatch();
				}
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

	// ================ 2. LẤY ID GHẾ ĐÃ ĐẶT (CHO GIAO DIỆN CHỌN GHẾ)
	// ================
	public List<Integer> getBookedSeatIds(int showtimeId) {
		List<Integer> bookedSeatIds = new ArrayList<>();
		// Lấy tất cả ghế thuộc booking PENDING hoặc SUCCESS
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

	// ================ 3. LẤY TÊN GHẾ ĐÃ CHỌN (CHO IN VÉ - TicketServlet)
	// ================
	public List<String> getSelectedSeats(int bookingId) {
		List<String> seats = new ArrayList<>();
		String sql = "SELECT s.seat_row, s.seat_number FROM bookingdetail bd "
				+ "JOIN seat s ON bd.seat_id = s.seat_id " + "WHERE bd.booking_id = ?";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, bookingId);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				// Ghép thành chuỗi (ví dụ: A1, B5)
				seats.add(rs.getString("seat_row") + rs.getInt("seat_number"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return seats;
	}

	// ================ 4. CÁC HÀM XỬ LÝ CHECKOUT / TIMEOUT ================

	// Lấy thời gian tạo booking
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

	// Kiểm tra booking có hết hạn chưa
	public boolean isBookingExpired(Integer bookingId, int timeoutMinutes) {
		java.sql.Timestamp createdAt = getBookingCreatedTime(bookingId);
		if (createdAt == null)
			return true;

		long currentTime = System.currentTimeMillis();
		long bookingTime = createdAt.getTime();
		long diffMinutes = (currentTime - bookingTime) / (60 * 1000);

		return diffMinutes >= timeoutMinutes;
	}

	// Hủy booking (dùng cho nút Hủy hoặc Timeout)
	public boolean cancelBookingAndReleaseSeats(Integer bookingId) {
		return updateBookingStatus(bookingId, Constant.BOOKING_CANCELLED);
	}

	// Cập nhật trạng thái (Dùng cho Payment Success hoặc Cancel)
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

	// Auto cleanup (Dùng cho SeatSelectionServlet để dọn dẹp data rác)
	public int cancelExpiredPendingBookings(int timeoutMinutes) {
		String sql = "UPDATE booking SET status = ? WHERE status = ? AND created_at < ?";
		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			long limitTime = System.currentTimeMillis() - (timeoutMinutes * 60 * 1000L);
			ps.setString(1, Constant.BOOKING_CANCELLED);
			ps.setString(2, Constant.BOOKING_PENDING);
			ps.setTimestamp(3, new Timestamp(limitTime));
			return ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	// ================ 5. CÁC HÀM UTILS / GETTER KHÁC ================

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
		String sql = "SELECT * FROM booking WHERE booking_id = ?";
		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, bookingId);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				Booking booking = new Booking();
				booking.setBookingId(rs.getInt("booking_id"));
				booking.setUserId(rs.getInt("user_id"));
				booking.setShowtimeId(rs.getInt("showtime_id"));
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

	private double calculateSeatPrice(Seat seat, double basePrice) {
		SeatDAO seatDAO = new SeatDAO();
		SeatType seatType = seatDAO.getSeatTypeById(seat.getSeatTypeId());
		return (seatType != null) ? basePrice + seatType.getSurcharge() : basePrice;
	}

	private double getBasePriceByShowtimeId(int showtimeId) {
		String sql = "SELECT base_price FROM showtime WHERE showtime_id = ?";
		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ps.setInt(1, showtimeId);
			ResultSet rs = ps.executeQuery();
			if (rs.next())
				return rs.getDouble("base_price");
		} catch (SQLException e) {
			e.printStackTrace();
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
				Booking booking = extractBookingFromResultSet(rs);
				bookings.add(booking);
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
				Booking booking = extractBookingFromResultSet(rs);
				bookings.add(booking);
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

			ps.setString(1, "%" + keyword + "%");
			ps.setString(2, "%" + keyword + "%");
			ps.setString(3, "%" + keyword + "%");
			ps.setString(4, "%" + keyword + "%");

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Booking booking = extractBookingFromResultSet(rs);
				bookings.add(booking);
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
				Booking booking = extractBookingFromResultSet(rs);
				bookings.add(booking);
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

	// Helper method để extract booking từ ResultSet
	private Booking extractBookingFromResultSet(ResultSet rs) throws SQLException {
		Booking booking = new Booking();
		booking.setBookingId(rs.getInt("booking_id"));
		booking.setUserId(rs.getInt("user_id"));
		booking.setShowtimeId(rs.getInt("showtime_id"));
		booking.setBookingDate(rs.getTimestamp("created_at"));
		booking.setTotalAmount(rs.getDouble("total_amount"));
		booking.setStatus(rs.getString("status"));
		booking.setCreatedAt(rs.getTimestamp("created_at"));

		// Set thông tin user
		User user = new User();
		user.setFullName(rs.getString("full_name"));
		user.setEmail(rs.getString("email"));
		user.setPhone(rs.getString("phone"));
		// Cần thêm setter cho user trong Booking model

		return booking;
	}

	// ================ THỐNG KÊ DASHBOARD ================

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
		String sql = "SELECT COUNT(*) as count " + "FROM booking " + "WHERE DATE(created_at) = CURDATE()";

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
		String sql = "SELECT COUNT(*) as count " + "FROM booking "
				+ "WHERE status = ? AND DATE(created_at) = CURDATE()";

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
		String sql = "SELECT * FROM booking " + "WHERE DATE(created_at) = CURDATE() " + "ORDER BY created_at DESC "
				+ "LIMIT 10";

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
}