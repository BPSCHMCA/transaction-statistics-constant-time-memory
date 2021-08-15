package com.n26.transaction.util;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import com.n26.transaction.constant.Constant;
import com.n26.transaction.dto.Transaction;
import com.n26.transaction.dto.TransactionData;
import com.n26.transaction.dto.TransactionStatistics;

final public class TestDataUtil {

	public static TransactionStatistics getTransactionStatistics(BigDecimal sum, BigDecimal avg, BigDecimal max,
			BigDecimal min, long count) {
		return new TransactionStatistics(sum, avg, max, min, count);
	}
	
	public static TransactionData getTransactionData(String amount, String timestamp) {
		return new TransactionData(amount, timestamp);
	}
	
	public static TransactionData getTransactionData(String amount) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(Constant.TRANSACTION_TIMESTAMP_FORMAT);
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		return getTransactionData(amount, dateFormat.format(new Date()));
	}
	
	public static TransactionData getTransactionData() {
		SimpleDateFormat dateFormat = new SimpleDateFormat(Constant.TRANSACTION_TIMESTAMP_FORMAT);
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		return getTransactionData("123", dateFormat.format(new Date()));
	}
	
	public static Transaction getTransaction(TransactionData transactionData) {
		return new Transaction(transactionData);
	}

}
