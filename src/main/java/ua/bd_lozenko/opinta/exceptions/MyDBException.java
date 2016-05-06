package ua.bd_lozenko.opinta.exceptions;

@SuppressWarnings("serial")
public class MyDBException extends Exception {
	public MyDBException() {
		super();
	}

	public MyDBException(String message, Throwable cause) {
		super(message, cause);
	}

	public MyDBException(String message) {
		super(message);
	}

}