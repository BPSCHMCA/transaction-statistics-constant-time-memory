package com.n26.transaction.validator;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static com.n26.transaction.util.TestDataUtil.getTransactionData;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.n26.transaction.constant.Constant;
import com.n26.transaction.dto.TransactionData;
import com.n26.transaction.exception.FutureTransactionException;
import com.n26.transaction.exception.InvalidAmountException;
import com.n26.transaction.exception.PastTransactionException;
import com.n26.transaction.exception.TimestampFormatException;

import name.falgout.jeffrey.testing.junit5.MockitoExtension;

@RunWith(JUnitPlatform.class)
@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
public class TransactionValidatorTest {

	@InjectMocks
	private TransactionValidator validator;

	@Test
	public void should_throw_InvalidAmountException() {
		TransactionData transactionData = getTransactionData("SPG");
		assertThrows(InvalidAmountException.class, () -> validator.validateTransaction(transactionData));
	}
	
	@Test
	public void should_throw_TimestampFormatException() {
		TransactionData transactionData = getTransactionData("123", "12/2/2021");
		assertThrows(TimestampFormatException.class, () -> validator.validateTransaction(transactionData));
	}
	
	@Test
	public void should_throw_FutureTransactionException() {
		SimpleDateFormat dateFormat = new SimpleDateFormat(Constant.TRANSACTION_TIMESTAMP_FORMAT);
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

		TransactionData transactionData = getTransactionData("123", dateFormat.format(new Date(new Date().getTime() + 5000)));
		assertThrows(FutureTransactionException.class, () -> validator.validateTransaction(transactionData));
	}
	
	@Test
	public void should_throw_PastTransactionException() {
		SimpleDateFormat dateFormat = new SimpleDateFormat(Constant.TRANSACTION_TIMESTAMP_FORMAT);
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		
		TransactionData transactionData = getTransactionData("123", dateFormat.format(new Date(new Date().getTime() - 61000)));
		assertThrows(PastTransactionException.class, () -> validator.validateTransaction(transactionData));
	}

}
