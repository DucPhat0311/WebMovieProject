package example.model;

import java.sql.Timestamp;

public class Payment {
	private int paymentId;
	private int bookingId;
	private String method; // Momo, VNPay, BankPro
	private String status; // Pending, Success, Failed
	private double amount;
	private Timestamp paidAt;

	public Payment() {
	}

	// Dùng cho INSERT (Khi user bấm Thanh toán) ---
	// Chỉ cần biết Booking nào, Phương thức gì, Bao nhiêu tiền.
	// Tự động set Status = PENDING và PaidAt = NULL
	public Payment(int bookingId, String method, double amount) {
		this.bookingId = bookingId;
		this.method = method;
		this.amount = amount;

		// Mặc định ban đầu
		this.status = Constant.PAYMENT_PENDING;
		this.paidAt = null;
	}

	// Dùng cho SELECT (Xem lịch sử giao dịch) ---
	// Nhận đầy đủ thông tin từ DB
	public Payment(int paymentId, int bookingId, String method, String status, double amount, Timestamp paidAt) {
		super();
		this.paymentId = paymentId;
		this.bookingId = bookingId;
		this.method = method;
		this.status = status; // Nhận status thực tế (Success/Failed)
		this.amount = amount;
		this.paidAt = paidAt; // Nhận thời gian thực tế
	}

	public int getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(int paymentId) {
		this.paymentId = paymentId;
	}

	public int getBookingId() {
		return bookingId;
	}

	public void setBookingId(int bookingId) {
		this.bookingId = bookingId;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public Timestamp getPaidAt() {
		return paidAt;
	}

	public void setPaidAt(Timestamp paidAt) {
		this.paidAt = paidAt;
	}

}
