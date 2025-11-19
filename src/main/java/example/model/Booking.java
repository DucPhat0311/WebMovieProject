package example.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Booking {

    private int id;
    private User user;
    private Showtime showtime;
    private LocalDateTime bookingDateTime;
    private BigDecimal totalAmount;
    private List<Ticket> tickets = new ArrayList<>();

    public Booking() {}

    public Booking(int id, User user, Showtime showtime, LocalDateTime bookingDateTime,
                 BigDecimal totalAmount) {
        this.id = id;
        this.user = user;
        this.showtime = showtime;
        this.bookingDateTime = bookingDateTime;
        this.totalAmount = totalAmount;
    }

    public void addTicket(Ticket ticket) {
        tickets.add(ticket);
        ticket.setBooking(this);
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Showtime getShowtime() { return showtime; }
    public void setShowtime(Showtime showtime) { this.showtime = showtime; }

    public LocalDateTime getBookingDateTime() { return bookingDateTime; }
    public void setBookingDateTime(LocalDateTime bookingDateTime) { this.bookingDateTime = bookingDateTime; }

	public List<Ticket> getTickets() {
		return tickets;
	}
	
	// tính lại tổng tiền
	public void calculateTotalAmount() {
	    this.totalAmount = tickets.stream()
	        .map(Ticket::getPrice)
	        .reduce(BigDecimal.ZERO, BigDecimal::add);
	}
}
