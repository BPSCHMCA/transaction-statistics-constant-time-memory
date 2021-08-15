package com.n26.transaction.service.impl;

import static com.n26.transaction.helper.TransactionServiceHelper.addNewTransactionStatistic;
import static com.n26.transaction.helper.TransactionServiceHelper.updateTransactionStatisticsOnGivenSecond;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.n26.transaction.dto.Transaction;
import com.n26.transaction.dto.TransactionStatistics;
import com.n26.transaction.service.TransactionService;

import static com.n26.transaction.helper.TransactionServiceHelper.*;

@Service
@Configuration
@ConfigurationProperties(prefix = "transaction.timestamp")
public class TransactionServiceImpl implements TransactionService {

	private Map<Long, TransactionStatistics> transactionStatisticsMap = new ConcurrentHashMap<>();

	@Value("${old_limit_in_seconds:60}")
	private long transactionTimestampOldLimit;

	/**
	 * This operation will be executed in O(1) time as it always fetches 60 entries
	 * from map and calculate transaction statistics on these entries only. Here,
	 * the map already has the calculated transaction statistics for all the
	 * transactions done on a given second.
	 */
	@Override
	public TransactionStatistics getTransactionsStatistics() {
		long currentTimeInSeconds = Instant.now().getEpochSecond();
		BigDecimal sum = getSum(currentTimeInSeconds, transactionTimestampOldLimit, transactionStatisticsMap);
		long count = getCount(currentTimeInSeconds, transactionTimestampOldLimit, transactionStatisticsMap);
		BigDecimal avg = count == 0l ? new BigDecimal(0) : sum.divide(new BigDecimal(count), 2, RoundingMode.HALF_UP);
		return new TransactionStatistics(sum, avg,
				getMax(currentTimeInSeconds, transactionTimestampOldLimit, transactionStatisticsMap),
				getMin(currentTimeInSeconds, transactionTimestampOldLimit, transactionStatisticsMap), count)
						.setBigDecimalScale();
	}

	/**
	 * This method executes in O(1) time and always use O(1) memory space as it has
	 * second value of transaction's time-stamp as key and calculated statistics as
	 * value. So, all the transactions done on a given second will be referenced by
	 * a single key & value.
	 * 
	 * This map can have max 70 entries, 60 entries for the transactions done in
	 * last 60 seconds and 10 might be old entries due to delay in fixed scheduled
	 * job to clear old entries. Thus, used memory space will always remain
	 * constant.
	 */
	@Override
	public void makeTransaction(final Transaction transaction) {
		long transactionTimeInSeconds = transaction.getTimestamp().getEpochSecond();
		TransactionStatistics transactionStatsOnGivenSecond = transactionStatisticsMap.get(transactionTimeInSeconds);
		if (Objects.nonNull(transactionStatsOnGivenSecond)) {
			updateTransactionStatisticsOnGivenSecond(transaction, transactionStatsOnGivenSecond);
		} else {
			addNewTransactionStatistic(transaction, transactionTimeInSeconds, transactionStatisticsMap);
		}
	}

	/**
	 * It is a service to clear out all the entries from transaction statistics map.
	 */
	@Override
	public void deleteTransactions() {
		transactionStatisticsMap.clear();
	}

	/**
	 * It is scheduled job that runs on every 10 seconds' interval with initial
	 * delay of 60 seconds as we'll have no old transaction on the map in first 60
	 * seconds.
	 * 
	 * The job clears out all the entries from the map which are done more than 60
	 * seconds prior to current time-stamp.
	 */
	@Scheduled(fixedRate = 10 * 1000, initialDelay = 60 * 1000)
	public void deleteOldTransactions() {
		long currentTimeInSeconnds = Instant.now().getEpochSecond();
		transactionStatisticsMap.entrySet()
				.removeIf(entry -> currentTimeInSeconnds - entry.getKey() > transactionTimestampOldLimit);
	}

}
