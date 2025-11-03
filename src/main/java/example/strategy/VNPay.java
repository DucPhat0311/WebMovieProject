package example.strategy;

public class VNPay implements Payment {

	@Override
	public void pay(double amount) {
	    System.out.println("You have just made a payment with VNPay of " + amount + " VND. Good job!!!");
	}
}