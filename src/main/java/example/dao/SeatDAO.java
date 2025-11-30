
package example.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import example.model.Cinema;
import example.model.Room;
import example.model.Seat;

public class SeatDAO {
	public static List<Seat> getSeatsByShowtime(int showtimeId) {
		List<Seat> seats = new ArrayList<>();
		String sql = """
				SELECT s.*, r.id as room_id, r.name as room_name, c.id as cinema_id,
				       t.id as ticket_id
				FROM seats s
				JOIN rooms r ON s.room_id = r.id
				JOIN cinemas c ON r.cinema_id = c.id
				LEFT JOIN tickets t ON t.seat_id = s.id AND t.showtime_id = ?
				WHERE r.id = (SELECT room_id FROM showtimes WHERE id = ?)
				ORDER BY s.row_char, s.number
				""";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, showtimeId);
			ps.setInt(2, showtimeId);
			ResultSet rs = ps.executeQuery();

			Room currentRoom = null;
			while (rs.next()) {
				if (currentRoom == null) {
					Cinema cinema = new Cinema(rs.getInt("cinema_id"), "", "");
					currentRoom = new Room(rs.getInt("room_id"), cinema, rs.getString("room_name"), 0);
				}

				Seat seat = new Seat();
				seat.setId(rs.getInt("id"));
				seat.setRoom(currentRoom);
				seat.setRow(rs.getString("row_char").charAt(0));
				seat.setNumber(rs.getInt("number"));
				seat.setType(rs.getString("type"));

				// Nếu có ticket_id → ghế đã đặt
				if (rs.getObject("ticket_id") != null) {
					// có thể thêm thuộc tính booked = true nếu cần
				}
				seats.add(seat);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return seats;
	}
}

