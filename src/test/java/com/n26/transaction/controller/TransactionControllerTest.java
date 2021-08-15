package com.n26.transaction.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.n26.transaction.constant.Constant;
import com.n26.transaction.dto.TransactionData;
import com.n26.transaction.dto.TransactionStatistics;
import com.n26.transaction.service.TransactionService;
import com.n26.transaction.validator.TransactionValidator;

import static com.n26.transaction.util.TestDataUtil.*;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = { TransactionController.class })
public class TransactionControllerTest {

	@MockBean
	private TransactionService service;

	@MockBean
	private TransactionValidator validator;

	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;

	@Test
	public void should_get_transaction_statistics() throws Exception {
		TransactionStatistics transactionStatistics = getTransactionStatistics(new BigDecimal("1.23"),
				new BigDecimal("1.23"), new BigDecimal("1.23"), new BigDecimal("1.23"), 1);
		given(service.getTransactionsStatistics()).willReturn(transactionStatistics);

		mockMvc.perform(get("/statistics").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}
	
	@Test
	public void should_post_transaction() throws Exception {
		SimpleDateFormat dateFormat = new SimpleDateFormat(Constant.TRANSACTION_TIMESTAMP_FORMAT);
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		TransactionData transactionData = getTransactionData("123", dateFormat.format(new Date()));
		
		mockMvc.perform(post("/transactions")
				.content(objectMapper.writeValueAsString(transactionData))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated());
	}
	
	@Test
	public void should_get_unprocessableResponseCode() throws Exception {
		SimpleDateFormat dateFormat = new SimpleDateFormat(Constant.TRANSACTION_TIMESTAMP_FORMAT);
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		TransactionData transactionData = getTransactionData("SPG", dateFormat.format(new Date()));
		
		mockMvc.perform(post("/transactions")
				.content(objectMapper.writeValueAsString(transactionData))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnprocessableEntity());
	}
	
	@Test
	public void should_get_badRequestResponseCode() throws Exception {
		mockMvc.perform(post("/transactions")
				.content(objectMapper.writeValueAsString("SPG"))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest());
	}
	
	@Test
	public void should_delete_transaction_statistics() throws Exception {
		TransactionStatistics transactionStatistics = getTransactionStatistics(new BigDecimal("1.23"),
				new BigDecimal("1.23"), new BigDecimal("1.23"), new BigDecimal("1.23"), 1);
		given(service.getTransactionsStatistics()).willReturn(transactionStatistics);

		mockMvc.perform(delete("/transactions"))
				.andExpect(status().isNoContent());
	}
}
