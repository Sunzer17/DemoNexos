package com.bankinc.manager.service;

import com.bankinc.manager.exception.CardManagerBusinessException;
import com.bankinc.manager.model.CardActivation;
import com.bankinc.manager.model.CardRecharge;

public interface CardService {

	String createCard(String productId);
	Double getBalance(String cardId) throws CardManagerBusinessException;
	void disableCard(String cardId) throws CardManagerBusinessException;
	void activateCard(CardActivation cardActivation) throws CardManagerBusinessException;
	void updateBalance(CardRecharge cardRecharge) throws CardManagerBusinessException;

}
