package example.dao.impl;

import java.sql.Connection;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import example.dao.core.DBConnection;
import example.model.cinema.Cinema;
import example.model.cinema.Room;
import example.model.movie.Movie;
import example.model.schedule.Showtime;

public class ShowtimeDAO {

	public List<Showtime> getAllShowtimes() {
		List<Showtime> list = new ArrayList<>();

		String sql = "SELECT st.showtime_id, st.show_date, st.start_time, st.base_price, " + "m.movie_id, m.title, "
				+ "r.room_id, r.room_name, " + "c.cinema_id, c.cinema_name " + "FROM Showtime st "
				+ "JOIN Movie m ON st.movie_id = m.movie_id " + "JOIN Room r ON st.room_id = r.room_id "
				+ "JOIN Cinema c ON r.cinema_id = c.cinema_id " + "WHERE st.is_active = TRUE " +

				"ORDER BY st.show_date DESC, st.start_time ASC";

		Connection conn = null;
	    PreparedStatement ps = null; 
	    ResultSet rs = null;

	    try {
	        conn = DBConnection.getConnection();
	        if (conn == null) {
	            return list;
	        }

	        ps = conn.prepareStatement(sql);
	        rs = ps.executeQuery(); 		

			while (rs.next()) {
				Movie movie = new Movie();
				movie.setMovieId(rs.getInt("movie_id"));
				movie.setTitle(rs.getString("title"));

				Cinema cinema = new Cinema();
				cinema.setCinemaId(rs.getInt("cinema_id"));
				cinema.setCinemaName(rs.getString("cinema_name"));

				Room room = new Room();
				room.setRoomId(rs.getInt("room_id"));
				room.setRoomName(rs.getString("room_name"));
				room.setCinema(cinema);

				Showtime st = new Showtime();
				st.setShowtimeId(rs.getInt("showtime_id"));
				st.setShowDate(rs.getDate("show_date"));
				st.setStartTime(rs.getTime("start_time"));
				st.setBasePrice(rs.getDouble("base_price"));

				st.setMovie(movie);
				st.setRoom(room);

				list.add(st);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBConnection.closeConnection(conn);
		}
		return list;
	}

	// lấy ngày trong của 1 phim
	public List<Date> getShowDates(int movieId) {
		List<Date> dates = new ArrayList<>();
		String sql = "SELECT DISTINCT show_date FROM Showtime "
				+ "WHERE movie_id = ? AND show_date >= CURDATE() AND is_active = TRUE " + "ORDER BY show_date ASC";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, movieId);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				dates.add(rs.getDate("show_date"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dates;
	}

	public List<Showtime> getShowtimesByDate(int movieId, Date date) {
		List<Showtime> list = new ArrayList<>();

		String sql = "SELECT s.*, c.cinema_name " + "FROM Showtime s " + "JOIN Room r ON s.room_id = r.room_id "
				+ "JOIN Cinema c ON r.cinema_id = c.cinema_id " + "WHERE s.movie_id = ? " + "AND s.show_date = ? "
				+ "AND s.is_active = TRUE " + "AND TIMESTAMP(s.show_date, s.start_time) > NOW() "
				+ "ORDER BY c.cinema_name, s.option_type, s.start_time";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, movieId);
			ps.setDate(2, date);

			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Showtime s = new Showtime(rs.getInt("showtime_id"), rs.getInt("movie_id"), rs.getInt("room_id"),
						rs.getDate("show_date"), rs.getTime("start_time"), rs.getTime("end_time"),
						rs.getDouble("base_price"), rs.getString("option_type"));
				s.setCinemaName(rs.getString("cinema_name"));
				list.add(s);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	// Thien them tam lấy thông tin chi tiết suất chiếu theo ID (bao gồm movie,
	// room, cinema)
	public Showtime getShowtimeById(int showtimeId) {
		Showtime showtime = null;
		String sql = "SELECT s.*, m.title, m.duration, m.poster_url, "
				+ "r.room_name, c.cinema_id, c.cinema_name, c.address " + "FROM showtime s "
				+ "JOIN movie m ON s.movie_id = m.movie_id " + "JOIN room r ON s.room_id = r.room_id "
				+ "JOIN cinema c ON r.cinema_id = c.cinema_id " + "WHERE s.showtime_id = ? AND s.is_active = TRUE";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, showtimeId);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				showtime = new Showtime(rs.getInt("showtime_id"), rs.getInt("movie_id"), rs.getInt("room_id"),
						rs.getDate("show_date"), rs.getTime("start_time"), rs.getTime("end_time"),
						rs.getDouble("base_price"), rs.getString("option_type"));
				showtime.setActive(rs.getBoolean("is_active"));
				showtime.setCinemaName(rs.getString("cinema_name"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return showtime;
	}

	// thien them lấy roomId từ showtimeId
	public int getRoomIdByShowtimeId(int showtimeId) {
		String sql = "SELECT room_id FROM showtime WHERE showtime_id = ?";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, showtimeId);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				return rs.getInt("room_id");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public boolean checkShowtimeOverlap(int roomId, Date showDate, Time newStart, Time newEnd) {
	    String sql = "SELECT COUNT(*) FROM Showtime WHERE room_id = ? AND show_date = ? AND is_active = TRUE " +
	                 "AND ((start_time < ? AND end_time > ?) OR (start_time >= ? AND start_time < ?))";
	                

	    try (Connection conn = DBConnection.getConnection(); 
	         PreparedStatement ps = conn.prepareStatement(sql)) {
	        
	        ps.setInt(1, roomId);
	        ps.setDate(2, showDate);
	        ps.setTime(3, newEnd);   
	        ps.setTime(4, newStart); 
	        ps.setTime(5, newStart);
	        ps.setTime(6, newEnd);
	        
	        ResultSet rs = ps.executeQuery();
	        if (rs.next()) {
	            return rs.getInt(1) > 0; //> 0 là bị trùng
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return false;
	}

	public boolean addShowtime(Showtime st) {
	    String sql = "INSERT INTO Showtime(movie_id, room_id, show_date, start_time, end_time, base_price, option_type, is_active) " +
	                 "VALUES (?, ?, ?, ?, ?, ?, ?, TRUE)";
	    try (Connection conn = DBConnection.getConnection(); 
	         PreparedStatement ps = conn.prepareStatement(sql)) {
	        
	        ps.setInt(1, st.getMovieId());
	        ps.setInt(2, st.getRoomId());
	        ps.setDate(3, st.getShowDate());
	        ps.setTime(4, st.getStartTime());
	        ps.setTime(5, st.getEndTime());
	        ps.setDouble(6, st.getBasePrice());
	        ps.setString(7, st.getOptionType());
	        
	        return ps.executeUpdate() > 0;
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return false;
	}

}
