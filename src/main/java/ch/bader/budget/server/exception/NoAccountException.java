package ch.bader.budget.server.exception;

public class NoAccountException extends Exception {

	private static final long serialVersionUID = 1814762412662580549L;

	public int id;

	public NoAccountException(int id) {
		super();
		this.id = id;
	}

}
