package com.bankinc.manager;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.bankinc.manager.entity.TransactionStatusType;
import com.bankinc.manager.model.CardActivation;
import com.bankinc.manager.model.CardRecharge;
import com.bankinc.manager.model.TransactionCancellation;
import com.bankinc.manager.model.TransactionRequest;
import com.bankinc.manager.model.TransactionSummary;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BankIncApplicationTests {

	@Autowired
	MockMvc mockMvc;

	static String cardIdTest = "";
	static String TransactionIdTest = "";

	@Test
	@Order(1)
	void generateCardsTest(@Autowired ObjectMapper mapper) throws Exception {
		MvcResult mvcResult = mockMvc.perform(get("/card/{productId}/number", "146265")).andExpect(status().isCreated())
				.andReturn();
		String cardId = (String) mapper.readValue(mvcResult.getResponse().getContentAsString(), HashMap.class)
				.get("cardId");
		Assertions.assertEquals(16, cardId.length());
		BankIncApplicationTests.cardIdTest = cardId;
	}

	@Test
	@Order(2)
	void successfulCardActivationTest(@Autowired ObjectMapper mapper) throws Exception {
		CardActivation activation = new CardActivation(cardIdTest, "Carlos Gomez");
		String activationBodyContent = mapper.writeValueAsString(activation);
		mockMvc.perform(post("/card/enroll").contentType(MediaType.APPLICATION_JSON).content(activationBodyContent))
				.andExpect(status().isOk()).andReturn();
	}

	@Test
	@Order(3)
	void rechargeCardTest(@Autowired ObjectMapper mapper) throws Exception {
		CardRecharge recharge = new CardRecharge(cardIdTest, 100.0);
		String rechargeBodyContent = mapper.writeValueAsString(recharge);
		mockMvc.perform(post("/card/balance").contentType(MediaType.APPLICATION_JSON).content(rechargeBodyContent))
				.andExpect(status().isOk()).andReturn();
	}

	@Test
	@Order(4)
	void cardBalanceTest(@Autowired ObjectMapper mapper) throws Exception {
		MvcResult mvcResult = mockMvc.perform(get("/card/{cardId}", cardIdTest).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();
		Double cardBalance = (Double) mapper.readValue(mvcResult.getResponse().getContentAsString(), HashMap.class)
				.get("cardBalance");
		Assertions.assertEquals(100.0, cardBalance);

	}

	@Test
	@Order(5)
	void purchaseArticleTest(@Autowired ObjectMapper mapper) throws Exception {
		TransactionRequest transactionRequest = new TransactionRequest(cardIdTest, 50.0);
		String transactionBodyContent = mapper.writeValueAsString(transactionRequest);
		MvcResult mvcResult = mockMvc.perform(
				post("/transaction/purchase").contentType(MediaType.APPLICATION_JSON).content(transactionBodyContent))
				.andExpect(status().isOk()).andReturn();
		TransactionSummary transactionSummary = mapper.readValue(mvcResult.getResponse().getContentAsString(),
				TransactionSummary.class);
		Assertions.assertNotNull(transactionSummary.getTransactionId());
		Assertions.assertNotNull(transactionSummary.getPurchaseDate());
		Assertions.assertEquals(TransactionStatusType.PROCESSING, transactionSummary.getPurchaseStatus());
		TransactionIdTest = transactionSummary.getTransactionId();
	}

	@Test
	@Order(6)
	void cancelPurchaseTest(@Autowired ObjectMapper mapper) throws Exception {
		TransactionCancellation transactionCancellation = new TransactionCancellation(cardIdTest, TransactionIdTest);
		String transactionBodyContent = mapper.writeValueAsString(transactionCancellation);
		mockMvc.perform(
				post("/transaction/anulation").contentType(MediaType.APPLICATION_JSON).content(transactionBodyContent))
				.andExpect(status().isOk());

	}

	@Test
	@Order(7)
	void getPurchaseDataTest(@Autowired ObjectMapper mapper) throws Exception {
		MvcResult mvcResult = mockMvc.perform(get("/transaction/{transactionId}", TransactionIdTest))
				.andExpect(status().isOk()).andReturn();
		TransactionSummary transactionSummary = mapper.readValue(mvcResult.getResponse().getContentAsString(),
				TransactionSummary.class);
		Assertions.assertNotNull(transactionSummary.getTransactionId());
		Assertions.assertNotNull(transactionSummary.getPurchaseDate());
		Assertions.assertEquals(TransactionStatusType.CANCELED, transactionSummary.getPurchaseStatus());

	}

	@Test
	@Order(8)
	void disableCardTest(@Autowired ObjectMapper mapper) throws Exception {
		mockMvc.perform(delete("/card/{cardId}", cardIdTest).contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk()).andReturn();
	}

	@Test
	@Order(9)
	void puchaseWithLockedCardTest(@Autowired ObjectMapper mapper) throws Exception {
		TransactionRequest transactionRequest = new TransactionRequest(cardIdTest, 50.0);
		String transactionBodyContent = mapper.writeValueAsString(transactionRequest);
		mockMvc.perform(
				post("/transaction/purchase").contentType(MediaType.APPLICATION_JSON).content(transactionBodyContent))
				.andExpect(status().isBadRequest()).andReturn();
	}

	@Test
	void fieldValidationTest(@Autowired ObjectMapper mapper) throws Exception {
		TransactionRequest transactionRequest = new TransactionRequest("1234567", 50.0);
		String transactionBodyContent = mapper.writeValueAsString(transactionRequest);

		mockMvc.perform(
				post("/transaction/purchase").contentType(MediaType.APPLICATION_JSON).content(transactionBodyContent))
				.andExpect(status().isBadRequest()).andReturn();

		mockMvc.perform(get("/card/{productId}/number", "123")).andExpect(status().isBadRequest()).andReturn();
	}

}
