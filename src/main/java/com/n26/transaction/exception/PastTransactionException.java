package com.n26.transaction.exception;

public class PastTransactionException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public PastTransactionException(String message) {
		super(message);
	}

}
