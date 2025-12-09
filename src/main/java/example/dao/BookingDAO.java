//package example.dao;
//
//import example.model.Booking;
//import example.model.Seat;
//import example.model.SeatType;
//
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.util.ArrayList;
//import java.util.*;
//
//import com.mysql.cj.x.protobuf.MysqlxDatatypes.Scalar.String;
//
//public class BookingDAO {
////	public List<Integer> getBookedSeatIds(int showtimeId) {
////		List<Integer> seatIds = new ArrayList<>();
////		
////
////		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
////			ps.setInt(1, showtimeId);
////			ResultSet rs = ps.executeQuery();
////
////			while (rs.next()) {
////				seatIds.add(rs.getInt("seat_id"));
////			}
////		} catch (SQLException e) {
////			e.printStackTrace();
////		}
////		return seatIds;
////	}
////
////	public boolean isSeatBooked(int seatId, int showtimeId) {
////	
////
////		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
////			ps.setInt(1, showtimeId);
////			ps.setInt(2, seatId);
////			ResultSet rs = ps.executeQuery();
////
////			if (rs.next()) {
////				return rs.getInt(1) > 0;
////			}
////		} catch (SQLException e) {
////			e.printStackTrace();
////		}
////		return false;
////	}
//
////	public int createBooking(Booking booking, String[] seatCodes, int roomId) {
////		Connection conn = null;
////		PreparedStatement psBooking = null;
////		PreparedStatement psDetail = null;
////		ResultSet rs = null;
////
////		try {
////			conn = DBConnection.getConnection();
////			conn.setAutoCommit(false);
////
////			SeatDAO seatDAO = new SeatDAO();
////			double totalAmount = 0;
////			List<Double> seatPrices = new ArrayList<>();
////			List<Seat> seatList = new ArrayList<>();
////
////			for (String seatCode : seatCodes) {
////				Seat seat = seatDAO.getSeatByCode(seadCode, roomId);
////				if (seat == null) {
////					conn.rollback();
////					return 0;
////				}
////				seatList.add(seat);
////			}
////
////			double basePrice = getBasePriceByShowtimeId(booking.getShowtimeId());
////			for (Seat seat : seatList) {
////				double seatPrice = calculateSeatPrice(seat, basePrice);
////				seatPrices.add(seatPrice);
////				totalAmount += seatPrice;
////			}
////
////			booking.setTotalAmount(totalAmount);
////
////		
////			psBooking.setInt(1, booking.getUserId());
////			psBooking.setInt(2, booking.getShowtimeId());
////			psBooking.setTimestamp(3, booking.getBookingDate());
////			psBooking.setDouble(4, booking.getTotalAmount());
////			psBooking.setString(5, booking.getStatus());
////
////			int rowsAffected = psBooking.executeUpdate();
////			if (rowsAffected == 0) {
////				conn.rollback();
////				return 0;
////			}
////
////			rs = psBooking.getGeneratedKeys();
////			int bookingId = 0;
////			if (rs.next()) {
////				bookingId = rs.getInt(1);
////			}
////
////	
////
////			for (int i = 0; i < seatList.size(); i++) {
////				Seat seat = seatList.get(i);
////				psDetail.setInt(1, bookingId);
////				psDetail.setInt(2, seat.getSeatId());
////				psDetail.setDouble(3, seatPrices.get(i));
////				psDetail.addBatch();
////			}
////
////			psDetail.executeBatch();
////			conn.commit();
////			return bookingId;
////
////		} catch (SQLException e) {
////			try {
////				if (conn != null)
////					conn.rollback();
////			} catch (SQLException ex) {
////				ex.printStackTrace();
////			}
////			e.printStackTrace();
////			return 0;
////		} finally {
////			try {
////				if (rs != null)
////					rs.close();
////				if (psBooking != null)
////					psBooking.close();
////				if (psDetail != null)
////					psDetail.close();
////				if (conn != null)
////					conn.close();
////			} catch (SQLException e) {
////				e.printStackTrace();
////			}
////		}
////	}
//
//	private double calculateSeatPrice(Seat seat, double basePrice) {
//		SeatDAO seatDAO = new SeatDAO();
//		SeatType seatType = seatDAO.getSeatTypeById(seat.getSeatTypeId());
//		if (seatType != null) {
//			return basePrice + seatType.getSurcharge();
//		}
//		return basePrice;
//	}
//
////	private double getBasePriceByShowtimeId(int showtimeId) {
////		String sql = "SELECT base_price FROM showtime WHERE showtime_id = ?";
////		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
////			ps.setInt(1, showtimeId);
////			ResultSet rs = ps.executeQuery();
////			if (rs.next()) {
////				return rs.getDouble("base_price");
////			}
////		} catch (SQLException e) {
////			e.printStackTrace();
////		}
////		return 0;
////	}
//}
//	
//
