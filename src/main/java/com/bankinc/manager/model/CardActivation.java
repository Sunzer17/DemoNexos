package com.bankinc.manager.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class CardActivation implements Process {
	
	@NotNull(message = "cardId is required")
	@Pattern(regexp = "\\d{16}",message = "cardId is not valid")
	private String cardId;
	
	@NotNull(message = "cardHolderName is required")
	@Pattern(regexp = "^[A-Za-z]+ [A-Za-z]+$",message = "Card Holder name is not valid")		   
	private String cardHolderName;

	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	public String getCardHolderName() {
		return cardHolderName;
	}

	public void setCardHolderName(String cardHolderName) {
		this.cardHolderName = cardHolderName;
	}
	
	
	

}
