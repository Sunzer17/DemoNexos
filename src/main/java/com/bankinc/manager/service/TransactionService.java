package com.bankinc.manager.service;

import com.bankinc.manager.exception.CardManagerBusinessException;
import com.bankinc.manager.model.TransactionCancellation;
import com.bankinc.manager.model.TransactionRequest;
import com.bankinc.manager.model.TransactionSummary;

public interface TransactionService {
	
	TransactionSummary processPurchase(TransactionRequest transactionRequest) throws CardManagerBusinessException;
	void cancelPurchase(TransactionCancellation transactionCancellation) throws CardManagerBusinessException;
	TransactionSummary getPurchase(String transactionId) throws CardManagerBusinessException;

}
