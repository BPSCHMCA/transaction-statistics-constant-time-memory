package com.n26.transaction.dto;

public class TransactionData {

	private String amount;

	private String timestamp;
	
	public TransactionData() {
		super();
	}

	public TransactionData(String amount, String timestamp) {
		super();
		this.amount = amount;
		this.timestamp = timestamp;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

}
