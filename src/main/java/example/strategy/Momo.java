package example.strategy;

public class Momo implements Payment {

	@Override
	public void pay(double amount) {
	    System.out.println("You have just made a payment with Momo of " + amount + " VND. Good job!!!");
	}
}
