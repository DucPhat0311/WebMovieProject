package example.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import example.model.Artist;
import example.model.Genre;
import example.model.Movie;

public class MovieDAO {

	public List<Movie> getAllMovies() {
		List<Movie> list = new ArrayList<>();

		String sql = "SELECT * FROM Movie";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Movie m = new Movie(rs.getInt("movie_id"), rs.getString("title"), rs.getString("description"),
						rs.getInt("duration"), rs.getDate("release_date"), rs.getString("age_warning"),
						rs.getString("poster_url"), rs.getString("trailer_url"), rs.getBoolean("is_active"));
				list.add(m);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return list;
	}
	
	public List<Movie> get8NowShowingMovies() {
		List<Movie> list = new ArrayList<>();

		String sql = "SELECT * FROM Movie WHERE release_date <= CURDATE() AND is_active = TRUE ORDER BY release_date DESC LIMIT 8";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Movie m = new Movie(rs.getInt("movie_id"), rs.getString("title"), rs.getString("description"),
						rs.getInt("duration"), rs.getDate("release_date"), rs.getString("age_warning"),
						rs.getString("poster_url"), rs.getString("trailer_url"), rs.getBoolean("is_active"));
				list.add(m);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}


	public List<Movie> getNowShowingMovies() {
		List<Movie> list = new ArrayList<>();

		String sql = "SELECT * FROM Movie WHERE release_date <= CURDATE() AND is_active = TRUE ORDER BY release_date DESC";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Movie m = new Movie(rs.getInt("movie_id"), rs.getString("title"), rs.getString("description"),
						rs.getInt("duration"), rs.getDate("release_date"), rs.getString("age_warning"),
						rs.getString("poster_url"), rs.getString("trailer_url"), rs.getBoolean("is_active"));
				list.add(m);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public List<Movie> get8ComingSoonMovies() {
		List<Movie> list = new ArrayList<>();

		String sql = "SELECT * FROM Movie WHERE release_date > CURDATE() AND is_active = TRUE ORDER BY release_date ASC LIMIT 8";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Movie m = new Movie(rs.getInt("movie_id"), rs.getString("title"), rs.getString("description"),
						rs.getInt("duration"), rs.getDate("release_date"), rs.getString("age_warning"),
						rs.getString("poster_url"), rs.getString("trailer_url"), rs.getBoolean("is_active"));
				list.add(m);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public List<Movie> getComingSoonMovies() {
		List<Movie> list = new ArrayList<>();

		String sql = "SELECT * FROM Movie WHERE release_date > CURDATE() AND is_active = TRUE ORDER BY release_date ASC";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				Movie m = new Movie(rs.getInt("movie_id"), rs.getString("title"), rs.getString("description"),
						rs.getInt("duration"), rs.getDate("release_date"), rs.getString("age_warning"),
						rs.getString("poster_url"), rs.getString("trailer_url"), rs.getBoolean("is_active"));
				list.add(m);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public Movie getMovieById(int id) {
		String sql = "SELECT * FROM Movie WHERE movie_id = ?";
		Movie movie = null;
		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				movie = new Movie(rs.getInt("movie_id"), rs.getString("title"), rs.getString("description"),
						rs.getInt("duration"), rs.getDate("release_date"), rs.getString("age_warning"),
						rs.getString("poster_url"), rs.getString("trailer_url"), rs.getBoolean("is_active"));
				movie.setGenres(getGenresByMovieId(id));
				movie.setActors(getActorsByMovieId(id));
				movie.setDirectors(getDirectorsByMovieId(id));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return movie;
	}

	public List<Genre> getGenresByMovieId(int movieId) {
		List<Genre> list = new ArrayList<>();

		String sql = "SELECT g.* FROM Genre g " + "JOIN MovieGenre mg ON g.genre_id = mg.genre_id "
				+ "WHERE mg.movie_id = ?";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, movieId);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Genre g = new Genre(rs.getInt("genre_id"), rs.getString("name"));
				list.add(g);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}

	public List<Artist> getActorsByMovieId(int movieId) {
		List<Artist> list = new ArrayList<>();

		String sql = "SELECT a.* FROM Artist a " + "JOIN Actor ac ON a.artist_id = ac.artist_id "
				+ "WHERE ac.movie_id = ?";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, movieId);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Artist artist = new Artist(rs.getInt("artist_id"), rs.getString("name"));
				list.add(artist);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public List<Artist> getDirectorsByMovieId(int movieId) {
		List<Artist> list = new ArrayList<>();

		String sql = "SELECT a.* FROM Artist a " + "JOIN Director d ON a.artist_id = d.artist_id "
				+ "WHERE d.movie_id = ?";

		try (Connection conn = DBConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setInt(1, movieId);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Artist artist = new Artist(rs.getInt("artist_id"), rs.getString("name"));
				list.add(artist);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public void insertMovie(Movie m) {
		String sql = "INSERT INTO Movie (title, description, duration, release_date, age_warning, poster_url, trailer_url, is_active) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

		try (Connection conn = new DBConnection().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setString(1, m.getTitle());
			ps.setString(2, m.getDescription());
			ps.setInt(3, m.getDuration());
			ps.setDate(4, m.getReleaseDate());
			ps.setString(5, m.getAgeWarning());
			ps.setString(6, m.getPosterUrl());
			ps.setString(7, m.getTrailerUrl());
			ps.setBoolean(8, true);

			ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public List<Movie> getMoviesByPage(String type, int amount, int offset) {
	    List<Movie> list = new ArrayList<>();
	    String sql = "";

	    if ("now".equals(type)) {
	        sql = "SELECT * FROM Movie WHERE release_date <= CURDATE() AND is_active = TRUE ORDER BY release_date DESC LIMIT ? OFFSET ?";
	    } else if ("coming".equals(type)) {
	        sql = "SELECT * FROM Movie WHERE release_date > CURDATE() AND is_active = TRUE ORDER BY release_date ASC LIMIT ? OFFSET ?";
	    }

	    try (Connection conn = new DBConnection().getConnection();
	         PreparedStatement ps = conn.prepareStatement(sql)) {

	        ps.setInt(1, amount); 
	        ps.setInt(2, offset); 

	        ResultSet rs = ps.executeQuery();
	        while (rs.next()) {
	            Movie m = new Movie();
	            m.setMovieId(rs.getInt("movie_id"));
	            m.setTitle(rs.getString("title"));
	            m.setPosterUrl(rs.getString("poster_url"));
	            m.setAgeWarning(rs.getString("age_warning")); 
	            list.add(m);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return list;
	}
	
	public List<Movie> getNextMovies(String type, int amount, int offset) {
	    List<Movie> list = new ArrayList<>();
	    String sql = "";
	    
	    if ("now".equals(type)) {
	        sql = "SELECT * FROM Movie WHERE release_date <= CURDATE() AND is_active = 1 ORDER BY release_date DESC LIMIT ? OFFSET ?";
	    } else {
	        sql = "SELECT * FROM Movie WHERE release_date > CURDATE() AND is_active = 1 ORDER BY release_date ASC LIMIT ? OFFSET ?";
	    }

	    try (Connection conn = new DBConnection().getConnection();
	         PreparedStatement ps = conn.prepareStatement(sql)) {
	         
	        ps.setInt(1, amount);
	        ps.setInt(2, offset); 
	        
	        ResultSet rs = ps.executeQuery();
	        while (rs.next()) {
	            Movie m = new Movie();
	            m.setMovieId(rs.getInt("movie_id"));
	            m.setTitle(rs.getString("title"));
	            m.setPosterUrl(rs.getString("poster_url"));
	            m.setAgeWarning(rs.getString("age_warning"));
	            list.add(m);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return list;
	}
}
