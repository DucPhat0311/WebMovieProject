package example.model;

public class Booking {

	private int id;
	private User user;
	private Showtime showtime;
	private LocalDateTime bookingDateTime;
	private BigDecimal totalAmount;
	private String status = "PENDING"; // PENDING, PAID, CANCELLED
	private List<Ticket> tickets = new ArrayList<>();

	public Booking() {
	}

	public void addTicket(Ticket ticket) {
		tickets.add(ticket);
		ticket.setBooking(this);
		calculateTotalAmount();
	}

	public void calculateTotalAmount() {
		if (showtime == null || tickets.isEmpty()) {
			totalAmount = BigDecimal.ZERO;
			return;
		}
		totalAmount = showtime.getPrice().multiply(BigDecimal.valueOf(tickets.size()));
	}

	// Getters & Setters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Showtime getShowtime() {
		return showtime;
	}

	public void setShowtime(Showtime showtime) {
		this.showtime = showtime;
	}

	public LocalDateTime getBookingDateTime() {
		return bookingDateTime;
	}

	public void setBookingDateTime(LocalDateTime bookingDateTime) {
		this.bookingDateTime = bookingDateTime;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<Ticket> getTickets() {
		return tickets;
	}

	public void setTickets(List<Ticket> tickets) {
		this.tickets = tickets;
	}

}