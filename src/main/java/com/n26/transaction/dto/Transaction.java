package com.n26.transaction.dto;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.TimeZone;

import com.n26.transaction.constant.Constant;
import com.n26.transaction.exception.InvalidAmountException;
import com.n26.transaction.exception.TimestampFormatException;

public class Transaction {
	
	public Transaction(BigDecimal amount, Instant timestamp) {
		super();
		this.amount = amount;
		this.timestamp = timestamp;
	}

	public Transaction(TransactionData transactionData) {
		super();
		setAmount(transactionData.getAmount());
		setTimestamp(transactionData.getTimestamp());
	}

	private BigDecimal amount;

	private Instant timestamp;

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public void setAmount(String amount) {
		try {
			this.amount = new BigDecimal(amount);
		} catch (NumberFormatException e) {
			throw new InvalidAmountException(e.getMessage());
		}
	}

	public Instant getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Instant timestamp) {
		this.timestamp = timestamp;
	}

	public void setTimestamp(String timestamp) {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(Constant.TRANSACTION_TIMESTAMP_FORMAT);
			dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
			this.timestamp = Instant.ofEpochMilli(dateFormat.parse(timestamp).getTime()).atOffset(ZoneOffset.UTC).toInstant();
		} catch (ParseException e) {
			throw new TimestampFormatException(e.getMessage());
		}
	}

}
