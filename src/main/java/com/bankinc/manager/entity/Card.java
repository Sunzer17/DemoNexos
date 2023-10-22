package com.bankinc.manager.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CARDS")
public class Card implements Serializable {

	private static final long serialVersionUID = -3146228863674267240L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "RECORD_ID")
	private Integer registerId;

	@Column(name = "CARD_NUMBER", length = 16, unique = true)
	private String cardNumber;

	@Column(name = "HOLDER_NAME", length = 50)
	private String holderName;

	@Column(name = "EXPIRATION_DATE")
	private Date expirationDate;

	@Column(name = "BALANCE")
	private Double balance;
	
	@Column(name = "ENABLED")
	private Boolean isEnabled;

	public Card() {
	}

	public Card(String cardNumber, Date expirationDate) {
		this.cardNumber = cardNumber;
		this.expirationDate = expirationDate;
		this.isEnabled = false;
		this.balance = 0.0;
	}

	public Integer getRegisterId() {
		return registerId;
	}

	public void setRegisterId(Integer registerId) {
		this.registerId = registerId;
	}

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getHolderName() {
		return holderName;
	}

	public void setHolderName(String holderName) {
		this.holderName = holderName;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	public Double getBalance() {
		return balance;
	}

	public void setBalance(Double balance) {
		this.balance = balance;
	}

	public Boolean getIsEnabled() {
		return isEnabled;
	}

	public void setIsEnabled(Boolean isEnabled) {
		this.isEnabled = isEnabled;
	}
	
	
	
	

}
