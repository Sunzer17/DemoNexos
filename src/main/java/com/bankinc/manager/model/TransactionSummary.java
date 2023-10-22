package com.bankinc.manager.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.bankinc.manager.entity.Transaction;
import com.bankinc.manager.entity.TransactionStatusType;

public class TransactionSummary implements Process {

	private String transactionId;

	private String purchaseDate;

	private TransactionStatusType purchaseStatus;

	private Double purchasePrice;

	private String cardId;

	
	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getPurchaseDate() {
		return purchaseDate;
	}

	public void setPurchaseDate(String purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

	public TransactionStatusType getPurchaseStatus() {
		return purchaseStatus;
	}

	public void setPurchaseStatus(TransactionStatusType purchaseStatus) {
		this.purchaseStatus = purchaseStatus;
	}

	public Double getPurchasePrice() {
		return purchasePrice;
	}

	public void setPurchasePrice(Double purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

	public String getCardId() {
		return cardId;
	}

	public void setCardId(String cardId) {
		this.cardId = cardId;
	}

	public TransactionSummary() {
	}

	public TransactionSummary(Transaction transaction) {
		this.transactionId = transaction.getId();
		this.purchaseDate = formatDate(transaction.getPurchaseDate());
		this.purchasePrice = transaction.getPurchasePrice();
		this.purchaseStatus = transaction.getPurchaseStatus();
		this.cardId = transaction.getCard().getCardNumber();
	}
	
	private String formatDate(Date date) {
		String pattern = "MM-dd-yyyy HH:mm";
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
		return simpleDateFormat.format(date);
	}

}
