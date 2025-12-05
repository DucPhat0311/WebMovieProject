package example.model;

import java.sql.Timestamp;

public class Payment {
	private int paymentId;
    private int bookingId;
    private String method; // Momo, VNPay, BankPro
    private String status; // Pending, Success, Failed
    private double amount;
    private Timestamp paidAt;
    
	public Payment(int paymentId, int bookingId, String method, double amount, Timestamp paidAt) {
		super();
		this.paymentId = paymentId;
		this.bookingId = bookingId;
		this.method = method;
		this.status = Constant.PAYMENT_PENDING;
		this.amount = amount;
		this.paidAt = paidAt;
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
