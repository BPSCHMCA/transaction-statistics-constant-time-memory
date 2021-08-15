package com.n26.transaction.helper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import com.google.common.collect.ImmutableMap;
import com.n26.transaction.dto.Transaction;
import com.n26.transaction.dto.TransactionData;
import com.n26.transaction.dto.TransactionStatistics;

import static com.n26.transaction.util.TestDataUtil.*;
import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RunWith(JUnitPlatform.class)
public class TransactionServiceHelperTest {

	private long transactionTimestampOldLimit;

	private Transaction transaction;

	private TransactionStatistics transactionStatistics;

	private Map<Long, TransactionStatistics> transactionStatisticsMap;

	@BeforeEach
	public void setupTest() {
		transactionTimestampOldLimit = 60;
		TransactionData transactionData = getTransactionData("123");
		transaction = getTransaction(transactionData);
		transactionStatistics = getTransactionStatistics(new BigDecimal("4.00"), new BigDecimal("2.00"),
				new BigDecimal("3.00"), new BigDecimal("1.00"), 2);
		transactionStatisticsMap = ImmutableMap.of(transaction.getTimestamp().getEpochSecond(), transactionStatistics);
	}

	@Test
	public void should_update_transactionStatistics_by_new_transaction() {
		TransactionServiceHelper.updateTransactionStatisticsOnGivenSecond(transaction, transactionStatistics);

		assertEquals(3l, transactionStatistics.getCount());
	}

	@Test
	public void should_add_new_transaction_statistics() {
		TransactionData transactionData = getTransactionData("123");
		Transaction transaction = getTransaction(transactionData);
		long transactionTimeInSeconds = transaction.getTimestamp().getEpochSecond();
		Map<Long, TransactionStatistics> transactionStatisticsMap = new ConcurrentHashMap<>();

		TransactionServiceHelper.addNewTransactionStatistic(transaction, transactionTimeInSeconds,
				transactionStatisticsMap);

		assertFalse(transactionStatisticsMap.isEmpty());
	}

	@Test
	public void should_get_all_transactions_sum_in_given_time_interval() {
		long currentTimeInSeconds = Instant.now().getEpochSecond();
		BigDecimal expected = new BigDecimal("4.00");

		BigDecimal actual = TransactionServiceHelper.getSum(currentTimeInSeconds, transactionTimestampOldLimit,
				transactionStatisticsMap);

		assertEquals(expected, actual);
	}

	@Test
	public void should_get_all_transactions_min_in_given_time_interval() {
		long currentTimeInSeconds = Instant.now().getEpochSecond();
		BigDecimal expected = new BigDecimal("1.00");

		BigDecimal actual = TransactionServiceHelper.getMin(currentTimeInSeconds, transactionTimestampOldLimit,
				transactionStatisticsMap);

		assertEquals(expected, actual);
	}

	@Test
	public void should_get_all_transactions_max_in_given_time_interval() {
		long currentTimeInSeconds = Instant.now().getEpochSecond();
		BigDecimal expected = new BigDecimal("3.00");

		BigDecimal actual = TransactionServiceHelper.getMax(currentTimeInSeconds, transactionTimestampOldLimit,
				transactionStatisticsMap);

		assertEquals(expected, actual);
	}

	@Test
	public void should_get_all_transactions_count_in_given_time_interval() {
		long currentTimeInSeconds = Instant.now().getEpochSecond();
		long expected = 2l;

		long actual = TransactionServiceHelper.getCount(currentTimeInSeconds, transactionTimestampOldLimit,
				transactionStatisticsMap);

		assertEquals(expected, actual);
	}

}
