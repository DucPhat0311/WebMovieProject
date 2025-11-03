package example.strategy;

public class ViettelMoney implements Payment {

	@Override
	public void pay(double amount) {
	    System.out.println("You have just made a payment with ViettelMoney of " + amount + " VND. Good job!!!");
	}
}
