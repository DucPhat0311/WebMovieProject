package example.dao;

import example.model.Showtime;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ShowtimeDAO {

	public Showtime getShowtimeById(int showtimeId) throws SQLException {
		String sql = "SELECT * FROM showtimes WHERE id = ?";

		try (Connection conn = JDBCUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, showtimeId);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				Showtime showtime = new Showtime();
				showtime.setId(rs.getInt("id"));
				showtime.setMovieId(rs.getInt("movie_id"));
				showtime.setRoomId(rs.getInt("room_id"));

				Timestamp timestamp = rs.getTimestamp("start_time");
				if (timestamp != null) {
					showtime.setStartTime(timestamp.toLocalDateTime());
				}

				showtime.setPrice(rs.getBigDecimal("price"));
				showtime.setFormat(rs.getString("format"));
				return showtime;
			}
		}
		return null;
	}

	public int getRoomIdByShowtime(int showtimeId) throws SQLException {
		String sql = "SELECT room_id FROM showtimes WHERE id = ?";

		try (Connection conn = JDBCUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

			stmt.setInt(1, showtimeId);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				return rs.getInt("room_id");
			}
		}
		return -1;
	}
}