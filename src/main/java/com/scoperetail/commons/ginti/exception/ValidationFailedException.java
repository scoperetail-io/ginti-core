package com.scoperetail.commons.ginti.exception;

public class ValidationFailedException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	public ValidationFailedException(final String exception) {
		super(exception);
	}
}