package example.dao.impl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import example.dao.core.DBConnection;
import example.model.cinema.Seat;
import example.model.cinema.SeatType;

public class SeatDAO {

	public List<Seat> getSeatsByRoomId(int roomId) {
		List<Seat> seats = new ArrayList<>();
		String sql = "SELECT s.* FROM seat s WHERE s.room_id = ? ORDER BY s.seat_row, s.seat_number";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, roomId);
			try (ResultSet rs = ps.executeQuery()) {
				while (rs.next()) {
					Seat seat = mapResultSetToSeat(rs);
					seats.add(seat);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return seats;
	}

	public Seat getSeatById(int seatId) {
		String sql = "SELECT * FROM seat WHERE seat_id = ?";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, seatId);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return mapResultSetToSeat(rs);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Seat getSeatByCode(String seatCode, int roomId) {
		if (seatCode == null || seatCode.length() < 2) {
			return null;
		}

		String row = seatCode.replaceAll("[0-9]", "");
		String numberStr = seatCode.replaceAll("[^0-9]", "");

		if (row.isEmpty() || numberStr.isEmpty()) {
			return null;
		}

		int number;
		try {
			number = Integer.parseInt(numberStr);
		} catch (NumberFormatException e) {
			return null;
		}

		String sql = "SELECT * FROM seat WHERE seat_row = ? AND seat_number = ? AND room_id = ?";
		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setString(1, row);
			ps.setInt(2, number);
			ps.setInt(3, roomId);

			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return mapResultSetToSeat(rs);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public SeatType getSeatTypeById(int seatTypeId) {
		String sql = "SELECT * FROM seattype WHERE seat_type_id = ?";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, seatTypeId);
			try (ResultSet rs = ps.executeQuery()) {
				if (rs.next()) {
					return new SeatType(rs.getInt("seat_type_id"), rs.getString("name"), rs.getDouble("surcharge"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	private Seat mapResultSetToSeat(ResultSet rs) throws SQLException {
		return new Seat(rs.getInt("seat_id"), rs.getInt("room_id"), rs.getInt("seat_type_id"), rs.getString("seat_row"),
				rs.getInt("seat_number"));
	}
}