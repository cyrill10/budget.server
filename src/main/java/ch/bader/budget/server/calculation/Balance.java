package ch.bader.budget.server.calculation;

public class Balance {

	private float balance;

	public Balance(float balance) {
		this.balance = balance;
	}

	public float getBalance() {
		return balance;
	}

	public void add(float number) {
		this.balance += number;
	}

	public void subtract(float number) {
		this.balance -= number;
	}

	public void add(Number number) {
		add(number.floatValue());
	}

	public void subtract(Number number) {
		subtract(number.floatValue());
	}

}
