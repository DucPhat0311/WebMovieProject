package example.model;

import java.sql.Date;
import java.sql.Time;

public class Showtime {
	private int showtimeId;
	private int movieId;
	private int roomId;
	private Date showDate;
	private Time startTime;
	private Time endTime;
	private double basePrice;
	private String optionType; // Long tieng, Phu De
	private boolean isActive;

	public Showtime(int showtimeId, int movieId, int roomId, Date showDate, Time startTime, Time endTime,
			double basePrice, String optionType) {
		super();
		this.showtimeId = showtimeId;
		this.movieId = movieId;
		this.roomId = roomId;
		this.showDate = showDate;
		this.startTime = startTime;
		this.endTime = endTime;
		this.basePrice = basePrice;
		this.optionType = Constant.Language_Sub;
		this.isActive = true;
	}

	public int getShowtimeId() {
		return showtimeId;
	}

	public void setShowtimeId(int showtimeId) {
		this.showtimeId = showtimeId;
	}

	public int getMovieId() {
		return movieId;
	}

	public void setMovieId(int movieId) {
		this.movieId = movieId;
	}

	public int getRoomId() {
		return roomId;
	}

	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}

	public Date getShowDate() {
		return showDate;
	}

	public void setShowDate(Date showDate) {
		this.showDate = showDate;
	}

	public Time getStartTime() {
		return startTime;
	}

	public void setStartTime(Time startTime) {
		this.startTime = startTime;
	}

	public Time getEndTime() {
		return endTime;
	}

	public void setEndTime(Time endTime) {
		this.endTime = endTime;
	}

	public double getBasePrice() {
		return basePrice;
	}

	public void setBasePrice(double basePrice) {
		this.basePrice = basePrice;
	}

	public String getOptionType() {
		return optionType;
	}

	public void setOptionType(String optionType) {
		this.optionType = optionType;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

}
