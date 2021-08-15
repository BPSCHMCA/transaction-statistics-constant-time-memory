package com.n26.transaction.validator;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.TimeZone;

import org.springframework.stereotype.Component;

import com.n26.transaction.constant.Constant;
import com.n26.transaction.dto.TransactionData;
import com.n26.transaction.exception.FutureTransactionException;
import com.n26.transaction.exception.InvalidAmountException;
import com.n26.transaction.exception.PastTransactionException;
import com.n26.transaction.exception.TimestampFormatException;

@Component
public class TransactionValidator {

	/**
	 * @param transactionData
	 * 
	 * It validates the transaction
	 * 
	 * @throws InvalidAmountException If transaction amount is invalid
	 * @throws TimestampFormatException If transaction time-stamp is not parse-able
	 * @throws FutureTransactionException If transaction's time-stamp is in future
	 * @throws PastTransactionException Iftransaction's time-stamp is 60 or more seconds older than current time-stamp
	 */
	public void validateTransaction(TransactionData transactionData) {
		try {
			new BigDecimal(transactionData.getAmount());
		} catch (NumberFormatException e) {
			throw new InvalidAmountException(e.getMessage());
		}
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(Constant.TRANSACTION_TIMESTAMP_FORMAT);
			dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
			Instant transactionTimestamp = Instant.ofEpochMilli(dateFormat.parse(transactionData.getTimestamp()).getTime())
					.atOffset(ZoneOffset.UTC).toInstant();
			Instant currentTime = Instant.now();
			if (currentTime.compareTo(transactionTimestamp) < 0) {
				throw new FutureTransactionException("Transaction has future's timestamp");
			} else if (currentTime.toEpochMilli() - transactionTimestamp.toEpochMilli() > 60 * 1000) {
				throw new PastTransactionException("Transaction is more than 60 seconds old");
			}
		} catch (ParseException e) {
			throw new TimestampFormatException(e.getMessage());
		}
	}

}
