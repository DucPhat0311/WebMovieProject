package example.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Showtime {

    private int id;
    private Movie movie;
    private Room room;
    private LocalDateTime startTime;
    private BigDecimal price;
    private Format format;

    public Showtime() {}

    public Showtime(int id, Movie movie, Room room,
                    LocalDateTime startTime,
                    BigDecimal price, Format format) {
        this.id = id;
        this.movie = movie;
        this.room = room;
        this.startTime = startTime;
        this.price = price;
        this.format = format;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Movie getMovie() { return movie; }
    public void setMovie(Movie movie) { this.movie = movie; }

    public Room getRoom() { return room; }
    public void setRoom(Room room) { this.room = room; }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public LocalDateTime getEndTime() {
        if (movie == null || startTime == null) {
            return null; 
        }
        return startTime.plusMinutes(movie.getDuration());
    }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public Format getFormat() { return format; }
    public void setFormat(Format format) { this.format = format; }
}
