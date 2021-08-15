package com.n26.transaction.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.n26.transaction.dto.Transaction;
import com.n26.transaction.dto.TransactionData;
import com.n26.transaction.dto.TransactionStatistics;
import com.n26.transaction.service.TransactionService;
import com.n26.transaction.validator.TransactionValidator;

import static org.springframework.http.HttpStatus.*;

@RestController
public class TransactionController {

	@Autowired
	private TransactionService service;

	@Autowired
	private TransactionValidator validator;

	@GetMapping("/statistics")
	public ResponseEntity<TransactionStatistics> getTransactionsStatistics() {
		return new ResponseEntity<>(service.getTransactionsStatistics(), OK);
	}

	@PostMapping("/transactions")
	public ResponseEntity<Void> makeTransaction(@RequestBody TransactionData transactionData) {
		validator.validateTransaction(transactionData);
		service.makeTransaction(new Transaction(transactionData));
		return new ResponseEntity<>(CREATED);
	}

	@DeleteMapping("/transactions")
	public ResponseEntity<Void> deleteTransactions() {
		service.deleteTransactions();
		return new ResponseEntity<Void>(NO_CONTENT);
	}

}
