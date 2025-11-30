package example.dao;

import example.model.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MovieDAO {

    // Lấy phim + toàn bộ thể loại, đạo diễn, diễn viên (có vai)
    public static Movie getByIdFull(int movieId) {
        Movie movie = null;
        String sql = """
            SELECT m.*, ar.description as age_desc
            FROM movies m
            LEFT JOIN age_ratings ar ON m.age_rating_code = ar.code
            WHERE m.id = ?
            """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, movieId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                movie = new Movie();
                movie.setId(rs.getInt("id"));
                movie.setTitle(rs.getString("title"));
                movie.setDuration(rs.getInt("duration"));
                movie.setCountry(rs.getString("country"));
                movie.setProducer(rs.getString("producer"));
                movie.setReleaseDate(rs.getDate("release_date") != null ? 
                    rs.getDate("release_date").toLocalDate() : null);
                movie.setContent(rs.getString("content"));
                movie.setPosterUrl(rs.getString("poster_url"));
                movie.setVideoUrl(rs.getString("video_url"));
                movie.setRating(rs.getDouble("rating"));
                movie.setAgeRatingCode(rs.getString("age_rating_code"));

                // Load genres
                movie.setGenres(loadGenres(movieId, conn));
                // Load directors
                movie.setDirectors(loadDirectors(movieId, conn));
                // Load actors + role
                movie.setActors(loadMovieActors(movieId, conn));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return movie;
    }

    // Phim đang chiếu
    public static List<Movie> getNowShowing() {
        return getMoviesByStatus("NOW");
    }

    // Phim sắp chiếu
    public static List<Movie> getComingSoon() {
        return getMoviesByStatus("SOON");
    }

    private static List<Movie> getMoviesByStatus(String type) {
        List<Movie> list = new ArrayList<>();
        String sql = type.equals("NOW") ?
            "SELECT DISTINCT m.* FROM movies m JOIN showtimes s ON m.id = s.movie_id WHERE s.start_time >= CURDATE()" :
            "SELECT m.* FROM movies m WHERE m.release_date > CURDATE() AND NOT EXISTS (SELECT 1 FROM showtimes s WHERE s.movie_id = m.id)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Movie m = new Movie();
                m.setId(rs.getInt("id"));
                m.setTitle(rs.getString("title"));
                m.setPosterUrl(rs.getString("poster_url"));
                m.setReleaseDate(rs.getDate("release_date").toLocalDate());
                m.setAgeRatingCode(rs.getString("age_rating_code"));
                list.add(m);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Helper methods
    private static List<Genre> loadGenres(int movieId, Connection conn) throws SQLException {
        List<Genre> genres = new ArrayList<>();
        String sql = "SELECT g.id, g.name FROM genres g JOIN movie_genres mg ON g.id = mg.genre_id WHERE mg.movie_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, movieId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                genres.add(new Genre(rs.getInt("id"), rs.getString("name")));
            }
        }
        return genres;
    }

    private static List<Director> loadDirectors(int movieId, Connection conn) throws SQLException {
        List<Director> dirs = new ArrayList<>();
        String sql = "SELECT d.id, d.name FROM directors d JOIN movie_directors md ON d.id = md.director_id WHERE md.movie_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, movieId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                dirs.add(new Director(rs.getInt("id"), rs.getString("name")));
            }
        }
        return dirs;
    }

    private static List<MovieActor> loadMovieActors(int movieId, Connection conn) throws SQLException {
        List<MovieActor> actors = new ArrayList<>();
        String sql = """
            SELECT a.id, a.name, ma.role_name 
            FROM actors a 
            JOIN movie_actors ma ON a.id = ma.actor_id 
            WHERE ma.movie_id = ?
            """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, movieId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Actor actor = new Actor(rs.getInt("id"), rs.getString("name"));
                MovieActor ma = new MovieActor(movieId, actor.getId(), rs.getString("role_name"));
                ma.setActor(actor);
                actors.add(ma);
            }
        }
        return actors;
    }
}