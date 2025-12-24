package example.model.transaction;

import java.time.LocalDateTime;

public class Payment {
	private int paymentId;
	private int bookingId;
	private double amount;
	private String paymentMethod; // Momo, VNPay, BankPro
	private String status; // Pending, Success, Failed
	private LocalDateTime paymentDate;

	public Payment() {
	}

	public Payment(int bookingId, double amount, String paymentMethod, String status) {
		this.bookingId = bookingId;
		this.amount = amount;
		this.paymentMethod = paymentMethod;
		this.status = status;
		this.paymentDate = LocalDateTime.now();
	}

	// Getters and Setters
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
		// Chuyển đổi từ MOMO/VNPAY/CASH sang format database
		if ("MOMO".equalsIgnoreCase(paymentMethod)) {
			this.paymentMethod = "Momo";
		} else if ("VNPAY".equalsIgnoreCase(paymentMethod)) {
			this.paymentMethod = "VNPay";
		} else if ("CASH".equalsIgnoreCase(paymentMethod)) {
			this.paymentMethod = "BankPro"; // Sử dụng BankPro cho tiền mặt
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

	public LocalDateTime getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(LocalDateTime paymentDate) {
		this.paymentDate = paymentDate;
	}
}