//package example.dao;
//
//import example.model.Booking;
//import example.model.BookingDetail;
//import example.model.Seat;
//import example.model.SeatType;
//
//import java.sql.*;
//import java.util.ArrayList;
//import java.util.List;
//
//public class BookingDAO {
//
//	// Lấy danh sách ID ghế đã đặt cho một suất chiếu
//	public List<Integer> getBookedSeatIds(int showtimeId) {
//		List<Integer> bookedSeatIds = new ArrayList<>();
//		String sql1 = "SELECT bd.seat_id FROM bookingdetail bd " + "JOIN booking b ON bd.booking_id = b.booking_id "
//				+ "WHERE b.showtime_id = ? AND b.status = 'Success'";
//		List<Integer> seatIds = new ArrayList<>();
//		String sql2 = "SELECT bd.seat_id FROM booking b " + "JOIN booking_detail bd ON b.booking_id = bd.booking_id "
//				+ "WHERE b.showtime_id = ? AND b.status != 'cancelled'";
//
//		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql1)) {
//
//			ps.setInt(1, showtimeId);
//			ResultSet rs = ps.executeQuery();
//
//			while (rs.next()) {
//				bookedSeatIds.add(rs.getInt("seat_id"));
//				seatIds.add(rs.getInt("seat_id"));
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return bookedSeatIds;
//		return seatIds;
//	}
//
//	// Tạo booking mới
//	public int createBooking(Booking booking) {
//		String sql = "INSERT INTO booking (user_id, showtime_id, total_amount, status, created_at) "
//				+ "VALUES (?, ?, ?, ?, NOW())";
//		int bookingId = -1;
//
//		try (Connection conn = DBConnection.getConnection();
//				PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
//
//			ps.setInt(1, booking.getUserId());
//			ps.setInt(2, booking.getShowtimeId());
//			ps.setDouble(3, booking.getTotalAmount());
//			ps.setString(4, booking.getStatus());
//	public boolean isSeatBooked(int seatId, int showtimeId) {
//		String sql = "SELECT COUNT(*) FROM booking b " + "JOIN booking_detail bd ON b.booking_id = bd.booking_id "
//				+ "WHERE b.showtime_id = ? AND bd.seat_id = ? AND b.status != 'cancelled'";
//
//			int affectedRows = ps.executeUpdate();
//		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
//			ps.setInt(1, showtimeId);
//			ps.setInt(2, seatId);
//			ResultSet rs = ps.executeQuery();
//
//			if (affectedRows > 0) {
//				ResultSet rs = ps.getGeneratedKeys();
//				if (rs.next()) {
//					bookingId = rs.getInt(1);
//				}
//			if (rs.next()) {
//				return rs.getInt(1) > 0;
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//		return bookingId;
//		return false;
//	}
//
//	// Thêm booking detail
//	public boolean addBookingDetail(BookingDetail detail) {
//		String sql = "INSERT INTO bookingdetail (booking_id, seat_id, price) VALUES (?, ?, ?)";
//	public int createBooking(Booking booking, String[] seatCodes, int roomId) {
//		Connection conn = null;
//		PreparedStatement psBooking = null;
//		PreparedStatement psDetail = null;
//		ResultSet rs = null;
//
//		try {
//			conn = DBConnection.getConnection();
//			conn.setAutoCommit(false);
//
//			SeatDAO seatDAO = new SeatDAO();
//			double totalAmount = 0;
//			List<Double> seatPrices = new ArrayList<>();
//			List<Seat> seatList = new ArrayList<>();
//
//			for (String seatCode : seatCodes) {
//				Seat seat = seatDAO.getSeatByCode(seatCode, roomId);
//				if (seat == null) {
//					conn.rollback();
//					return 0;
//				}
//				seatList.add(seat);
//			}
//
//		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
//			double basePrice = getBasePriceByShowtimeId(booking.getShowtimeId());
//			for (Seat seat : seatList) {
//				double seatPrice = calculateSeatPrice(seat, basePrice);
//				seatPrices.add(seatPrice);
//				totalAmount += seatPrice;
//			}
//
//			booking.setTotalAmount(totalAmount);
//
//			String sqlBooking = "INSERT INTO booking (user_id, showtime_id, booking_date, total_amount, status) "
//					+ "VALUES (?, ?, ?, ?, ?)";
//			psBooking = conn.prepareStatement(sqlBooking, Statement.RETURN_GENERATED_KEYS);
//			psBooking.setInt(1, booking.getUserId());
//			psBooking.setInt(2, booking.getShowtimeId());
//			psBooking.setTimestamp(3, booking.getBookingDate());
//			psBooking.setDouble(4, booking.getTotalAmount());
//			psBooking.setString(5, booking.getStatus());
//
//			int rowsAffected = psBooking.executeUpdate();
//			if (rowsAffected == 0) {
//				conn.rollback();
//				return 0;
//			}
//
//			rs = psBooking.getGeneratedKeys();
//			int bookingId = 0;
//			if (rs.next()) {
//				bookingId = rs.getInt(1);
//			}
//
//			String sqlDetail = "INSERT INTO booking_detail (booking_id, seat_id, price) VALUES (?, ?, ?)";
//			psDetail = conn.prepareStatement(sqlDetail);
//
//			for (int i = 0; i < seatList.size(); i++) {
//				Seat seat = seatList.get(i);
//				psDetail.setInt(1, bookingId);
//				psDetail.setInt(2, seat.getSeatId());
//				psDetail.setDouble(3, seatPrices.get(i));
//				psDetail.addBatch();
//			}
//
//			ps.setInt(1, detail.getBookingId());
//			ps.setInt(2, detail.getSeatId());
//			ps.setDouble(3, detail.getPrice());
//			psDetail.executeBatch();
//			conn.commit();
//			return bookingId;
//
//			return ps.executeUpdate() > 0;
//		} catch (SQLException e) {
//			try {
//				if (conn != null)
//					conn.rollback();
//			} catch (SQLException ex) {
//				ex.printStackTrace();
//			}
//			e.printStackTrace();
//			return false;
//			return 0;
//		} finally {
//			try {
//				if (rs != null)
//					rs.close();
//				if (psBooking != null)
//					psBooking.close();
//				if (psDetail != null)
//					psDetail.close();
//				if (conn != null)
//					conn.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
//		}
//	}
//
//	// Lấy seatId từ seatCode (A1, B2, ...)
//	public int getSeatIdByCode(int roomId, String seatRow, int seatNumber) {
//		String sql = "SELECT seat_id FROM seat WHERE room_id = ? AND seat_row = ? AND seat_number = ?";
//	private double calculateSeatPrice(Seat seat, double basePrice) {
//		SeatDAO seatDAO = new SeatDAO();
//		SeatType seatType = seatDAO.getSeatTypeById(seat.getSeatTypeId());
//		if (seatType != null) {
//			return basePrice + seatType.getSurcharge();
//		}
//		return basePrice;
//	}
//
//	private double getBasePriceByShowtimeId(int showtimeId) {
//		String sql = "SELECT base_price FROM showtime WHERE showtime_id = ?";
//		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
//
//			ps.setInt(1, roomId);
//			ps.setString(2, seatRow);
//			ps.setInt(3, seatNumber);
//
//			ps.setInt(1, showtimeId);
//			ResultSet rs = ps.executeQuery();
//			if (rs.next()) {
//				return rs.getInt("seat_id");
//				return rs.getDouble("base_price");
//			}
//		} catch (SQLException e) {
//			e.printStackTrace();