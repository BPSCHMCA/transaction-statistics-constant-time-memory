package com.n26.transaction.exception;

public class FutureTransactionException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public FutureTransactionException(String message) {
		super(message);
	}

}
