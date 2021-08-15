package com.n26.transaction.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.n26.transaction.exception.FutureTransactionException;
import com.n26.transaction.exception.InvalidAmountException;
import com.n26.transaction.exception.PastTransactionException;
import com.n26.transaction.exception.TimestampFormatException;

@ControllerAdvice
public class TransactionExceptionHandler {

	@ExceptionHandler({TimestampFormatException.class, InvalidAmountException.class, FutureTransactionException.class})
	public ResponseEntity<Void> handleInputException() {
		return new ResponseEntity<Void>(HttpStatus.UNPROCESSABLE_ENTITY);
	}
	
	@ExceptionHandler({PastTransactionException.class})
	public ResponseEntity<Void> handlePastTransactionException() {
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}
}
