package com.n26.transaction.helper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.Map;
import java.util.Objects;
import java.util.stream.LongStream;

import com.n26.transaction.dto.Transaction;
import com.n26.transaction.dto.TransactionStatistics;

final public class TransactionServiceHelper {

	/**
	 * @param transaction
	 * @param transactionStatsOnGivenSecond
	 * 
	 * It updates the transaction statistics of a given second with a new
	 * transaction done on the same second. The idea is to keep all the
	 * transactions' statistics together which are done on the same value of the
	 * second, but might have different value for millisecond/nanosecond, no matter.
	 * 
	 * As here, the implemented logic updates the existing statistics for a new
	 * transaction, so it always completes in O(1) time complexity.
	 */
	public static void updateTransactionStatisticsOnGivenSecond(final Transaction transaction,
			TransactionStatistics transactionStatsOnGivenSecond) {
		transactionStatsOnGivenSecond.setSum(transactionStatsOnGivenSecond.getSum().add(transaction.getAmount()));
		transactionStatsOnGivenSecond.setCount(transactionStatsOnGivenSecond.getCount() + 1);
		transactionStatsOnGivenSecond.setAvg(
				transactionStatsOnGivenSecond.getSum().divide(new BigDecimal(transactionStatsOnGivenSecond.getCount()), 2, RoundingMode.HALF_UP));
		if (transactionStatsOnGivenSecond.getMin().compareTo(transaction.getAmount()) > 0) {
			transactionStatsOnGivenSecond.setMin(transaction.getAmount());
		}
		if (transactionStatsOnGivenSecond.getMax().compareTo(transaction.getAmount()) < 0) {
			transactionStatsOnGivenSecond.setMax(transaction.getAmount());
		}
	}

	/**
	 * @param transaction
	 * @param transactionTimeInSeconds
	 * @param transactionStatisticsMap
	 */
	public static void addNewTransactionStatistic(final Transaction transaction, long transactionTimeInSeconds,
			Map<Long, TransactionStatistics> transactionStatisticsMap) {
		transactionStatisticsMap.put(transactionTimeInSeconds, new TransactionStatistics(transaction.getAmount(),
				transaction.getAmount(), transaction.getAmount(), transaction.getAmount(), 1));
	}

	/**
	 * @param currentTimeInSeconds
	 * @param transactionTimestampOldLimit
	 * @param transactionStatisticsMap
	 * 
	 * It returns the sum of all the transactions done in last 60 seconds. 
	 * 
	 * It executes in constant time as it always iterates 60 times and sum ups the values.
	 */
	public static BigDecimal getSum(long currentTimeInSeconds, long transactionTimestampOldLimit,
			Map<Long, TransactionStatistics> transactionStatisticsMap) {
		return LongStream.iterate(currentTimeInSeconds, i -> i - 1).limit(transactionTimestampOldLimit)
				.mapToObj(timeInSeconds -> transactionStatisticsMap.get(timeInSeconds)).filter(Objects::nonNull)
				.map(transactionStat -> transactionStat.getSum()).reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	/**
	 * @param currentTimeInSeconds
	 * @param transactionTimestampOldLimit
	 * @param transactionStatisticsMap
	 * 
	 * It returns the minimum amount of all the transactions done in last 60 seconds. 
	 * 
	 * It executes in constant time as it always iterates 60 times and finds the minimum amount value.
	 */
	public static BigDecimal getMin(long currentTimeInSeconds, long transactionTimestampOldLimit,
			Map<Long, TransactionStatistics> transactionStatisticsMap) {
		return LongStream.iterate(currentTimeInSeconds, i -> i - 1).limit(transactionTimestampOldLimit)
				.mapToObj(timeInSeconds -> transactionStatisticsMap.get(timeInSeconds)).filter(Objects::nonNull)
				.map(transactionStat -> transactionStat.getMin()).min(Comparator.naturalOrder())
				.orElseGet(() -> new BigDecimal(0));
	}

	/**
	 * @param currentTimeInSeconds
	 * @param transactionTimestampOldLimit
	 * @param transactionStatisticsMap
	 * 
	 * It returns the max of all the transactions done in last 60 seconds. 
	 * 
	 * It executes in constant time as it always iterates 60 times and finds the maximum amount value.
	 */
	public static BigDecimal getMax(long currentTimeInSeconds, long transactionTimestampOldLimit,
			Map<Long, TransactionStatistics> transactionStatisticsMap) {
		return LongStream.iterate(currentTimeInSeconds, i -> i - 1).limit(transactionTimestampOldLimit)
				.mapToObj(timeInSeconds -> transactionStatisticsMap.get(timeInSeconds)).filter(Objects::nonNull)
				.map(transactionStat -> transactionStat.getMax()).max(Comparator.naturalOrder())
				.orElseGet(() -> new BigDecimal(0));
	}

	/**
	 * @param currentTimeInSeconds
	 * @param transactionTimestampOldLimit
	 * @param transactionStatisticsMap
	 * 
	 * It returns the count of all the transactions done in last 60 seconds. 
	 * 
	 * It executes in constant time as it always iterates 60 times and sum ups the transaction count.
	 */
	public static long getCount(long currentTimeInSeconds, long transactionTimestampOldLimit,
			Map<Long, TransactionStatistics> transactionStatisticsMap) {
		return LongStream.iterate(currentTimeInSeconds, i -> i - 1).limit(transactionTimestampOldLimit)
				.mapToObj(timeInSeconds -> transactionStatisticsMap.get(timeInSeconds)).filter(Objects::nonNull)
				.map(transactionStat -> transactionStat.getCount()).reduce(Long::sum).orElseGet(() -> 0l);
	}

}
