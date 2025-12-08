package example.dao;

import example.model.Seat;
import example.model.SeatType;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SeatDAO {

	// Lấy tất cả ghế trong một phòng
	public List<Seat> getSeatsByRoomId(int roomId) {
		List<Seat> seats = new ArrayList<>();
		String sql = "SELECT s.*, st.name as seat_type_name, st.surcharge " + "FROM seat s "
				+ "JOIN seattype st ON s.seat_type_id = st.seat_type_id " + "WHERE s.room_id = ? "
				+ "ORDER BY s.seat_row, s.seat_number";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, roomId);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Seat seat = new Seat(rs.getInt("seat_id"), rs.getInt("room_id"), rs.getInt("seat_type_id"),
						rs.getString("seat_row"), rs.getInt("seat_number"));
				seats.add(seat);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return seats;
	}

	// Lấy ghế theo ID
	public Seat getSeatById(int seatId) {
		Seat seat = null;
		String sql = "SELECT * FROM seat WHERE seat_id = ?";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, seatId);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				seat = new Seat(rs.getInt("seat_id"), rs.getInt("room_id"), rs.getInt("seat_type_id"),
						rs.getString("seat_row"), rs.getInt("seat_number"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return seat;
	}

	// Lấy loại ghế theo ID
	public SeatType getSeatTypeById(int seatTypeId) {
		SeatType seatType = null;
		String sql = "SELECT * FROM seattype WHERE seat_type_id = ?";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, seatTypeId);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				seatType = new SeatType(rs.getInt("seat_type_id"), rs.getString("name"), rs.getDouble("surcharge"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return seatType;
	}
}