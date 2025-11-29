//package example.dao;
//
//import example.model.Ticket;
//import java.sql.*;
//import java.util.List;
//import java.util.ArrayList;
//
//public class TicketDAO {
//
//	public boolean createTicket(Ticket ticket) throws SQLException {
//		String sql = "INSERT INTO tickets (booking_id, seat_id, showtime_id) VALUES (?, ?, ?)";
//
//		try (Connection conn = JDBCUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
//
//			stmt.setInt(1, ticket.getBookingId());
//			stmt.setInt(2, ticket.getSeatId());
//			stmt.setInt(3, ticket.getShowtimeId());
//
//			return stmt.executeUpdate() > 0;
//		}
//	}
//
//	public List<Ticket> getTicketsByBookingId(int bookingId) throws SQLException {
//		List<Ticket> tickets = new ArrayList<>();
//		String sql = "SELECT * FROM tickets WHERE booking_id = ?";
//
//		try (Connection conn = JDBCUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
//
//			stmt.setInt(1, bookingId);
//			ResultSet rs = stmt.executeQuery();
//
//			while (rs.next()) {
//				Ticket ticket = new Ticket();
//				ticket.setId(rs.getInt("id"));
//				ticket.setBookingId(rs.getInt("booking_id"));
//				ticket.setSeatId(rs.getInt("seat_id"));
//				ticket.setShowtimeId(rs.getInt("showtime_id"));
//				tickets.add(ticket);
//			}
//		}
//		return tickets;
//	}
//
//	public boolean isSeatBookedForShowtime(int seatId, int showtimeId) throws SQLException {
//		String sql = "SELECT COUNT(*) FROM tickets WHERE seat_id = ? AND showtime_id = ?";
//
//		try (Connection conn = JDBCUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
//
//			stmt.setInt(1, seatId);
//			stmt.setInt(2, showtimeId);
//			ResultSet rs = stmt.executeQuery();
//
//			if (rs.next()) {
//				return rs.getInt(1) > 0;
//			}
//		}
//		return false;
//	}
//
//	public void createTicketsBatch(List<Ticket> tickets) throws SQLException {
//		String sql = "INSERT INTO tickets (booking_id, seat_id, showtime_id) VALUES (?, ?, ?)";
//
//		try (Connection conn = JDBCUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
//
//			for (Ticket ticket : tickets) {
//				stmt.setInt(1, ticket.getBookingId());
//				stmt.setInt(2, ticket.getSeatId());
//				stmt.setInt(3, ticket.getShowtimeId());
//				stmt.addBatch();
//			}
//
//			stmt.executeBatch();
//		}
//	}
//}