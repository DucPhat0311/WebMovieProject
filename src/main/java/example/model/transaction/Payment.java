package example.model.transaction;

import java.util.Date;

public class Payment {
	private int paymentId;
	private int bookingId;
	private double amount;
	private String paymentMethod;
	private String status;
	private Date paymentDate;

	public Payment() {
	}

	public Payment(int bookingId, double amount, String paymentMethod, String status) {
		this.bookingId = bookingId;
		this.amount = amount;
		this.setPaymentMethod(paymentMethod);
		this.status = status;
		this.paymentDate = new Date();
	}

	public Payment(int paymentId, int bookingId, double amount, String paymentMethod, String status, Date paymentDate) {
		this.paymentId = paymentId;
		this.bookingId = bookingId;
		this.amount = amount;
		this.setPaymentMethod(paymentMethod);
		this.status = status;
		this.paymentDate = paymentDate;
	}

	// Getters và Setters
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

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		if ("MOMO".equalsIgnoreCase(paymentMethod)) {
			this.paymentMethod = "Momo";
		} else if ("VNPAY".equalsIgnoreCase(paymentMethod)) {
			this.paymentMethod = "VNPay";
		} else if ("CASH".equalsIgnoreCase(paymentMethod)) {
			this.paymentMethod = "BankPro";
		} else {
			this.paymentMethod = paymentMethod;
		}
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}

	public void setPaymentDate(java.time.LocalDateTime localDateTime) {
		if (localDateTime != null) {
			this.paymentDate = java.sql.Timestamp.valueOf(localDateTime);
		}
	}
}