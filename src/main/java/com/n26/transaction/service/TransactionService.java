package com.n26.transaction.service;

import com.n26.transaction.dto.Transaction;
import com.n26.transaction.dto.TransactionStatistics;

public interface TransactionService {

	TransactionStatistics getTransactionsStatistics();

	void makeTransaction(Transaction transaction);

	void deleteTransactions();

}
