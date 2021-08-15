package com.n26.transaction.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.n26.transaction.dto.Transaction;
import com.n26.transaction.dto.TransactionData;
import com.n26.transaction.dto.TransactionStatistics;
import com.n26.transaction.service.impl.TransactionServiceImpl;

import name.falgout.jeffrey.testing.junit5.MockitoExtension;

import static com.n26.transaction.util.TestDataUtil.*;

@RunWith(JUnitPlatform.class)
@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

	@InjectMocks
	private TransactionServiceImpl service;

	@BeforeEach
	public void setup() {
		ReflectionTestUtils.setField(service, "transactionTimestampOldLimit", 60);
	}

	@Test
	public void should_get_transaction_statistics() {
		TransactionData transactionData = getTransactionData("10");
		Transaction transaction = new Transaction(transactionData);

		service.makeTransaction(transaction);
		service.makeTransaction(new Transaction(getTransactionData("20")));

		TransactionStatistics transactionStatistics = service.getTransactionsStatistics();

		assertEquals(2l, transactionStatistics.getCount());
		assertEquals(new BigDecimal("30.00"), transactionStatistics.getSum());
		assertEquals(new BigDecimal("20.00"), transactionStatistics.getMax());
		assertEquals(new BigDecimal("10.00"), transactionStatistics.getMin());
		assertEquals(new BigDecimal("15.00"), transactionStatistics.getAvg());
	}

	@Test
	public void should_create_transaction() {
		TransactionData transactionData = getTransactionData("123");
		Transaction transaction = new Transaction(transactionData);

		service.makeTransaction(transaction);

		service.makeTransaction(new Transaction(getTransactionData("456")));
	}

	@Test
	public void should_delete_all_transactions() {
		service.deleteTransactions();
	}
}
