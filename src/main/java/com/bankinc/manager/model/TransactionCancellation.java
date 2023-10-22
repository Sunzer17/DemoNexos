package com.bankinc.manager.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class TransactionCancellation implements Process {

	@NotNull(message = "cardId is required")
	@Pattern(regexp = "\\d{16}", message = "cardId is not valid")
	private String cardId;

	@NotNull(message = "transactionId is required")
	@Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$", message = "transactionId is not valid")
	private String transactionId;

	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public TransactionCancellation(String cardId, String transactionId) {
		super();
		this.cardId = cardId;
		this.transactionId = transactionId;
	}

}
