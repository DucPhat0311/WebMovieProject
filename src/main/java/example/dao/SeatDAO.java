package example.dao;

import example.composite.Seat;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SeatDAO {

	public List<Seat> getSeatsByRoomId(int roomId) throws SQLException {
		List<Seat> seats = new ArrayList<>();
		String sql = "SELECT * FROM seats WHERE room_id = ?";

		try (Connection conn = JDBCUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, roomId);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				Seat seat = new Seat(rs.getInt("id"), rs.getString("seat_code"), rs.getInt("seat_row"),
						rs.getInt("seat_col"));

				if (rs.getBoolean("is_booked")) {
					seat.book();
				}

				seats.add(seat);
			}
		}
		return seats;
	}

	public List<Seat> getBookedSeatsByShowtime(int showtimeId) throws SQLException {
		List<Seat> bookedSeats = new ArrayList<>();
		String sql = "SELECT s.* FROM seats s " + "JOIN tickets t ON s.id = t.seat_id " + "WHERE t.showtime_id = ?";

		try (Connection conn = JDBCUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, showtimeId);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				Seat seat = new Seat(rs.getInt("id"), rs.getString("seat_code"), rs.getInt("seat_row"),
						rs.getInt("seat_col"));
				seat.book();
				bookedSeats.add(seat);
			}
		}
		return bookedSeats;
	}

	public boolean isSeatAvailable(int seatId, int showtimeId) throws SQLException {
		String sql = "SELECT COUNT(*) FROM tickets WHERE seat_id = ? AND showtime_id = ?";

		try (Connection conn = JDBCUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, seatId);
			stmt.setInt(2, showtimeId);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				return rs.getInt(1) == 0;
			}
		}
		return false;
	}

	public Seat getSeatById(int seatId) throws SQLException {
		String sql = "SELECT * FROM seats WHERE id = ?";

		try (Connection conn = JDBCUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, seatId);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				Seat seat = new Seat(rs.getInt("id"), rs.getString("seat_code"), rs.getInt("seat_row"),
						rs.getInt("seat_col"));

				if (rs.getBoolean("is_booked")) {
					seat.book();
				}
				return seat;
			}
		}
		return null;
	}
}