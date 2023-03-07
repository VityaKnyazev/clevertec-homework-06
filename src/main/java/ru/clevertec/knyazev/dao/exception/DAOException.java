package ru.clevertec.knyazev.dao.exception;

public class DAOException extends Exception {
	private static final long serialVersionUID = 6188586970315281684L;

	public DAOException(String message, Exception cause) {
		super(message, cause);
	}

	public DAOException(String message) {
		super(message);
	}

}
