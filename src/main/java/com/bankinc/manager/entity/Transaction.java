package com.bankinc.manager.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "TRANSACTIONS")
public class Transaction implements Serializable {

	private static final long serialVersionUID = -3146228863674267240L;

	@Id
	@Column(name = "TRANSACTION_ID")
	@GeneratedValue(generator = "uuid-hibernate-generator")
	@GenericGenerator(name = "uuid-hibernate-generator", strategy = "org.hibernate.id.UUIDGenerator")
	private String id;

	@Column(name = "TRANSACTION_DATE")
	private Date purchaseDate;

	@Enumerated(EnumType.STRING)
	@Column(name = "STATUS")
	private TransactionStatusType purchaseStatus;

	@Column(name = "PRICE")
	private Double purchasePrice;

	@ManyToOne(optional = false)
	@JoinColumn(name = "CARD_RECORD_ID")
	private Card card;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getPurchaseDate() {
		return purchaseDate;
	}

	public void setPurchaseDate(Date purchaseDate) {
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

	public Card getCard() {
		return card;
	}

	public void setCard(Card card) {
		this.card = card;
	}
	
	public Transaction() {
	}

	public Transaction(Date purchaseDate, TransactionStatusType purchaseStatus, Double purchasePrice, Card card) {
		this.purchaseDate = purchaseDate;
		this.purchaseStatus = purchaseStatus;
		this.purchasePrice = purchasePrice;
		this.card = card;
	}

}
