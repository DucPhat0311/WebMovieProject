package example.dao;

import example.model.Movie;
import example.dao.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MovieDAO {

    public List<Movie> getShowingMovies() throws Exception {
        List<Movie> list = new ArrayList<>();
        String sql = "SELECT * FROM Movie WHERE status = 'showing' ORDER BY release_date DESC";

        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Movie m = new Movie();
                m.setMovieId(rs.getInt("movie_id"));
                m.setTitle(rs.getString("title"));
                m.setDescription(rs.getString("description"));
                m.setGenreId(rs.getInt("genre_id"));
                m.setDuration(rs.getInt("duration"));
                m.setReleaseDate(rs.getDate("release_date"));
                m.setAgeWarning(rs.getString("age_warning"));
                m.setPosterUrl(rs.getString("poster_url"));
                m.setTrailerUrl(rs.getString("trailer_url"));
                m.setStatus(rs.getString("status"));
                list.add(m);
            }
        }
        return list;
    }

    public Movie getMovieById(int id) throws Exception {
        String sql = "SELECT * FROM Movie WHERE movie_id = ?";
        try (Connection c = DBConnection.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Movie m = new Movie();
                    m.setMovieId(rs.getInt("movie_id"));
                    m.setTitle(rs.getString("title"));
                    m.setDescription(rs.getString("description"));
                    m.setDuration(rs.getInt("duration"));
                    m.setPosterUrl(rs.getString("poster_url"));
                    m.setTrailerUrl(rs.getString("trailer_url"));
                    m.setAgeWarning(rs.getString("age_warning"));
                    m.setReleaseDate(rs.getDate("release_date"));
                    return m;
                }
            }
        }
        return null;
    }
}