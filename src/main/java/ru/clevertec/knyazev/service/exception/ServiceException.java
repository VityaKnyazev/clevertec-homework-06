package ru.clevertec.knyazev.service.exception;

public class ServiceException extends Exception {
	private static final long serialVersionUID = -5241777766393884340L;
	
	public ServiceException(String message) {
		super(message);
	}
	
	public ServiceException(String message, Exception exception) {
		super(message, exception);
	}
}
