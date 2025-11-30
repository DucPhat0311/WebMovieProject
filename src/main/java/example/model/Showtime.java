package example.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Showtime {
	private int id;
	private Movie movie;
	private Room room;
	private LocalDateTime startTime;
	private BigDecimal price;
	private String formatCode; // Thay Format enum bằng String

	public Showtime() {
	}

	// Getter & Setter
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Movie getMovie() {
		return movie;
	}

	public void setMovie(Movie movie) {
		this.movie = movie;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}

	public LocalDateTime getEndTime() {
		if (movie == null || startTime == null) {
			return null;
		}
		return startTime.plusMinutes(movie.getDuration());
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getFormatCode() {
		return formatCode;
	}

	public void setFormatCode(String formatCode) {
		this.formatCode = formatCode;
	}

	// Helper method để lấy Format enum
	public Format getFormat() {
		return Format.fromCode(formatCode);
	}
}
