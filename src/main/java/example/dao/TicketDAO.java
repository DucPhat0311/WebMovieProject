
package example.dao;

import example.model.Ticket;
import example.model.Seat;
import example.model.Showtime;
import example.model.Booking;
import example.model.Cinema;
import example.model.Room;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TicketDAO {

	// 1. Kiểm tra ghế đã được đặt chưa (rất quan trọng cho chọn ghế)
	public static boolean isSeatBooked(int showtimeId, int seatId) {
		String sql = "SELECT 1 FROM tickets WHERE showtime_id = ? AND seat_id = ? LIMIT 1";
		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, showtimeId);
			ps.setInt(2, seatId);
			return ps.executeQuery().next();

		} catch (SQLException e) {
			e.printStackTrace();
			return true; // an toàn: nếu lỗi → coi như đã đặt
		}
	}

	// 2. Lấy tất cả vé của 1 booking (dùng để in vé, hiển thị lịch sử đặt)
	public static List<Ticket> getTicketsByBookingId(int bookingId) {
		List<Ticket> tickets = new ArrayList<>();
		String sql = """
				SELECT t.*, s.row_char, s.number, s.type,
				       r.id as room_id, r.name as room_name,
				       c.id as cinema_id, c.name as cinema_name, c.address
				FROM tickets t
				JOIN seats s ON t.seat_id = s.id
				JOIN rooms r ON s.room_id = r.id
				JOIN cinemas c ON r.cinema_id = c.id
				WHERE t.booking_id = ?
				""";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, bookingId);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				// Tạo Seat
				Cinema cinema = new Cinema(rs.getInt("cinema_id"), rs.getString("cinema_name"),
						rs.getString("address"));
				Room room = new Room(rs.getInt("room_id"), cinema, rs.getString("room_name"), 0);
				Seat seat = new Seat();
				seat.setId(rs.getInt("seat_id"));
				seat.setRoom(room);
				seat.setRow(rs.getString("row_char").charAt(0));
				seat.setNumber(rs.getInt("number"));
				seat.setType(rs.getString("type"));

				// Tạo Ticket
				Ticket ticket = new Ticket();
				ticket.setId(rs.getInt("id"));
				ticket.setSeat(seat);
				// booking sẽ được set sau nếu cần
				tickets.add(ticket);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tickets;
	}

	// 3. Lấy tất cả vé của 1 suất chiếu (dùng cho admin xem ghế đã bán)
	public static List<Integer> getBookedSeatIds(int showtimeId) {
		List<Integer> seatIds = new ArrayList<>();
		String sql = "SELECT seat_id FROM tickets WHERE showtime_id = ?";

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

	// 4. Hủy tất cả vé của 1 booking (khi user hủy đơn hoặc admin xóa)
	public static boolean cancelTicketsByBooking(int bookingId) {
		String sql = "DELETE FROM tickets WHERE booking_id = ?";
		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, bookingId);
			return ps.executeUpdate() > 0;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	// 5. Tạo nhiều vé cùng lúc (dùng trong BookingDAO cũng được, nhưng để đây cho
	// rõ ràng)
	public static boolean createTickets(int bookingId, int showtimeId, List<Integer> seatIds) {
		String sql = "INSERT IGNORE INTO tickets (booking_id, seat_id, showtime_id) VALUES (?, ?, ?)";
		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			conn.setAutoCommit(false);
			for (int seatId : seatIds) {
				ps.setInt(1, bookingId);
				ps.setInt(2, seatId);
				ps.setInt(3, showtimeId);
				ps.addBatch();
			}
			int[] results = ps.executeBatch();
			conn.commit();

			// Nếu có ghế nào bị trùng → rollback (dù dùng IGNORE nhưng vẫn kiểm tra)
			for (int r : results) {
				if (r == 0) {
					conn.rollback();
					return false;
				}
			}
			return true;

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
}

