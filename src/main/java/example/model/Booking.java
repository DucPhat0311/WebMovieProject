package example.model;

public class Booking {
    private int bookingId;
    private int userId;
    private int showtimeId;
    private String bookingTime;
    private double totalAmount;
    
    public Booking() {}
    
    
	public Booking(int bookingId, int userId, int showtimeId, String bookingTime, double totalAmount) {
		super();
		this.bookingId = bookingId;
		this.userId = userId;
		this.showtimeId = showtimeId;
		this.bookingTime = bookingTime;
		this.totalAmount = totalAmount;
	}

	public Booking( int userId, int showtimeId, String bookingTime, double totalAmount) {
		super();
		this.userId = userId;
		this.showtimeId = showtimeId;
		this.bookingTime = bookingTime;
		this.totalAmount = totalAmount;
	}





	public int getBookingId() {
		return bookingId;
	}
	public void setBookingId(int bookingId) {
		this.bookingId = bookingId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getShowtimeId() {
		return showtimeId;
	}
	public void setShowtimeId(int showtimeId) {
		this.showtimeId = showtimeId;
	}
	public String getBookingTime() {
		return bookingTime;
	}
	public void setBookingTime(String bookingTime) {
		this.bookingTime = bookingTime;
	}
	public double getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}
    
    
}