package org.ifolks.generator.model.exception;

public class EnumerationNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public EnumerationNotFoundException(String message) {
		super(message);
	}

	public EnumerationNotFoundException(String message, Throwable t) {
		super(message, t);
	}
}
