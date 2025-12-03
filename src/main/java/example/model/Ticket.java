package example.model;

import java.math.BigDecimal;

public class Ticket {

	private int id;
	private Booking booking;
	private Seat seat;

	public Ticket() {
	}

	public Ticket(int id, Booking booking, Seat seat) {
		this.id = id;
		this.booking = booking;
		this.seat = seat;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Booking getBooking() {
		return booking;
	}

	public void setBooking(Booking booking) {
		this.booking = booking;
	}

	public Seat getSeat() {
		return seat;
	}

	public void setSeat(Seat seat) {
		this.seat = seat;
	}

}