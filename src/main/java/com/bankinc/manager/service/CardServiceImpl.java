package com.bankinc.manager.service;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bankinc.manager.entity.Card;
import com.bankinc.manager.exception.CardManagerBusinessException;
import com.bankinc.manager.model.CardActivation;
import com.bankinc.manager.model.CardRecharge;
import com.bankinc.manager.repository.CardRepository;

@Service
public class CardServiceImpl implements CardService {

	@Autowired
	CardRepository cardRepository;

	@Override
	public String createCard(String productId) {
		Card card = new Card(generateCardNumber(productId), calculateExpirationDate());
		return cardRepository.save(card).getCardNumber();
	}

	@Override
	public void activateCard(CardActivation cardActivation) throws CardManagerBusinessException {
		Optional<Card> optionalCard = cardRepository.findByCardNumber(cardActivation.getCardId());
		if (optionalCard.isPresent()) {
			Card card = optionalCard.get();
			if (Boolean.FALSE.equals(card.getIsEnabled())) {
				card.setHolderName(cardActivation.getCardHolderName());
				card.setIsEnabled(true);
				cardRepository.save(card);
				return;
			}
			throw new CardManagerBusinessException("This cardId is already activated");
		}
		throw new CardManagerBusinessException("The cardId does not exist");
	}

	@Override
	public void disableCard(String cardId) throws CardManagerBusinessException {
		Optional<Card> optionalCard = cardRepository.findByCardNumber(cardId);
		if (optionalCard.isPresent()) {
			Card card = optionalCard.get();
			if (Boolean.TRUE.equals(card.getIsEnabled())) {
				card.setIsEnabled(false);
				cardRepository.save(card);
				return;
			}
			throw new CardManagerBusinessException("This card is already locked");
		}
		throw new CardManagerBusinessException("The cardId does not exist");
	}

	@Override
	public void updateBalance(CardRecharge cardRecharge) throws CardManagerBusinessException {
		Optional<Card> optionalCard = cardRepository.findByCardNumber(cardRecharge.getCardId());
		if (optionalCard.isPresent()) {
			Card card = optionalCard.get();
			if (Boolean.TRUE.equals(card.getIsEnabled())) {
				card.setBalance(cardRecharge.getBalance() + card.getBalance());
				cardRepository.save(card);
				return;
			}
			throw new CardManagerBusinessException("This card is locked");
		}
		throw new CardManagerBusinessException("The cardId does not exist");
	}

	@Override
	public Double getBalance(String cardId) throws CardManagerBusinessException {
		Optional<Card> optionalCard = cardRepository.findByCardNumber(cardId);
		if (optionalCard.isPresent()) {
			Card card = optionalCard.get();
			if (Boolean.TRUE.equals(card.getIsEnabled())) {
				return card.getBalance();
			}
			throw new CardManagerBusinessException("This card is locked");
		}
		throw new CardManagerBusinessException("The cardId does not exist");
	}

	private String generateCardNumber(String productId) {
		String regexPattern = "0123456789";
		return productId + RandomStringUtils.random(10, regexPattern);
	}

	private Date calculateExpirationDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.YEAR, 3);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 999);
		return calendar.getTime();
	}

}
