package org.ifolks.generator.model.exception;

public class InvalidXmlSourceAndScriptFileException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	public InvalidXmlSourceAndScriptFileException (String message) {
		super(message);
	}

	public InvalidXmlSourceAndScriptFileException (String message, Throwable t) {
		super(message, t);
	}
}
