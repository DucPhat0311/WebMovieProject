package example.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Corresponds to table showtimes
 * Use LocalDateTime for show start time.
 * Keep both id references and optional nested movie/room objects.
 */
public class Showtime {
    private int id;
    private int movieId;
    private Movie movie;      // optional, fill when needed
    private int roomId;
    private LocalDateTime startTime;
    private BigDecimal price; // use BigDecimal for money
    private String format;    // "2D", "IMAX", "3D", "Sub", etc.

    public Showtime() {}

    public Showtime(int id, int movieId, int roomId, LocalDateTime startTime, BigDecimal price, String format) {
        this.id = id;
        this.movieId = movieId;
        this.roomId = roomId;
        this.startTime = startTime;
        this.price = price;
        this.format = format;
    }

    // --- getters / setters ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getMovieId() { return movieId; }
    public void setMovieId(int movieId) { this.movieId = movieId; }

    public Movie getMovie() { return movie; }
    public void setMovie(Movie movie) { this.movie = movie; }

    public int getRoomId() { return roomId; }
    public void setRoomId(int roomId) { this.roomId = roomId; }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public String getFormat() { return format; }
    public void setFormat(String format) { this.format = format; }

    @Override
    public String toString() {
        return "Showtime{" +
                "id=" + id +
                ", movieId=" + movieId +
                ", roomId=" + roomId +
                ", startTime=" + startTime +
                ", price=" + price +
                ", format='" + format + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Showtime)) return false;
        Showtime showtime = (Showtime) o;
        return id == showtime.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
