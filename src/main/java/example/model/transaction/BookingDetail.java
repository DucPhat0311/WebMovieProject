package example.model.transaction;

public class BookingDetail {
	private int bookingDetailId;
	private int bookingId;
	private int seatId;
	private double price;

	public BookingDetail() {
	}

	public BookingDetail(int bookingDetailId, int bookingId, int seatId, double price) {
		this.bookingDetailId = bookingDetailId;
		this.bookingId = bookingId;
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