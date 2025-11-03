package example.strategy;

public class PaymentNavigator {
	private Payment payment;

	public void setPayment(Payment payment) {
		this.payment = payment;
	}

	public void executePayment(double amount) {
		payment.pay(amount);
	}
}
