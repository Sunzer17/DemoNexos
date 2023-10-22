package com.bankinc.manager.model;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class CardRecharge implements Process {
	
	@NotNull(message = "cardId is required")
	@Pattern(regexp = "\\d{16}",message = "cardId is not valid")
	private String cardId;
	
	@NotNull(message = "balance is required")
	@Min(value = 1,message = "the value should not be less than 1$")
	@Max(value = 20000,message = "the value should not be more than $20000")
	private Double balance;

	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	

	

	

	
	
	
	

}
