package example.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import example.model.Cinema;
import example.model.Movie;
import example.model.Room;
import example.model.Showtime;

public class ShowtimeDAO {
	public static Showtime getByIdFull(int showtimeId) {
		Showtime st = null;
		String sql = """
				SELECT s.*, m.*, c.id as cinema_id, c.name as cinema_name, c.address,
				       r.id as room_id, r.name as room_name
				FROM showtimes s
				JOIN movies m ON s.movie_id = m.id
				JOIN rooms r ON s.room_id = r.id
				JOIN cinemas c ON r.cinema_id = c.id
				WHERE s.id = ?
				""";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, showtimeId);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				Movie movie = MovieDAO.getByIdFull(rs.getInt("movie_id")); // reuse
				Cinema cinema = new Cinema(rs.getInt("cinema_id"), rs.getString("cinema_name"),
						rs.getString("address"));
				Room room = new Room(rs.getInt("room_id"), cinema, rs.getString("room_name"), 0);

				st = new Showtime();
				st.setId(rs.getInt("id"));
				st.setMovie(movie);
				st.setRoom(room);
				st.setStartTime(rs.getTimestamp("start_time").toLocalDateTime());
				st.setPrice(rs.getBigDecimal("price"));
				st.setFormatCode(rs.getString("format_code"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return st;
	}

	public static List<Showtime> getByMovieId(int movieId) {
		List<Showtime> list = new ArrayList<>();
		String sql = "SELECT * FROM showtimes WHERE movie_id = ? ORDER BY start_time";
		// tương tự như trên, có thể tối ưu bằng cách load room + cinema riêng
		// hiện tại giữ đơn giản
		return list;
	}
}