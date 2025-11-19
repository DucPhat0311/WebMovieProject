package example.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Showtime {

    private int id;
    private Movie movie;
    private Room room;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private BigDecimal price;
    private String format;

    public Showtime() {}

    public Showtime(int id, Movie movie, Room room,
                    LocalDateTime startTime, LocalDateTime endTime,
                    BigDecimal price, String format) {
        this.id = id;
        this.movie = movie;
        this.room = room;
        this.startTime = startTime;
        this.endTime = endTime;
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

    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public String getFormat() { return format; }
    public void setFormat(String format) { this.format = format; }
}
