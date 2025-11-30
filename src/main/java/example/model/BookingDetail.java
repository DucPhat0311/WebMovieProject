package example.model;

public class BookingDetail {
	private int bookingDetailId;
    private int bookingId;
    private int showtimeId;
    private int seatId;
    private double price;
    
    public BookingDetail() {}

    public BookingDetail(int bookingId, int showtimeId, int seatId, double price) {
		super();
		this.bookingId = bookingId;
		this.showtimeId = showtimeId;
		this.seatId = seatId;
		this.price = price;
	}
    
	public BookingDetail(int bookingDetailId, int bookingId, int showtimeId, int seatId, double price) {
		super();
		this.bookingDetailId = bookingDetailId;
		this.bookingId = bookingId;
		this.showtimeId = showtimeId;
		this.seatId = seatId;
		this.price = price;
	}

	public int getBookingDetailId() {
		return bookingDetailId;
	}

	public void setBookingDetailId(int bookingDetailId) {
		this.bookingDetailId = bookingDetailId;
	}

	public int getBookingId() {
		return bookingId;
	}

	public void setBookingId(int bookingId) {
		this.bookingId = bookingId;
	}

	public int getShowtimeId() {
		return showtimeId;
	}

	public void setShowtimeId(int showtimeId) {
		this.showtimeId = showtimeId;
	}

	public int getSeatId() {
		return seatId;
	}

	public void setSeatId(int seatId) {
		this.seatId = seatId;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}
    
    
}
