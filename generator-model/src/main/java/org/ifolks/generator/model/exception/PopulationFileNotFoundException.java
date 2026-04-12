package org.ifolks.generator.model.exception;

public class PopulationFileNotFoundException extends RuntimeException {

private static final long serialVersionUID = 1L;
	
	public PopulationFileNotFoundException (String message) {
		super(message);
	}

	public PopulationFileNotFoundException (String message, Throwable t) {
		super(message, t);
	}
}
