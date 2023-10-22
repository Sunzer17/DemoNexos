package com.bankinc.manager.service;

import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bankinc.manager.entity.Card;
import com.bankinc.manager.entity.Transaction;
import com.bankinc.manager.entity.TransactionStatusType;
import com.bankinc.manager.exception.CardManagerBusinessException;
import com.bankinc.manager.model.TransactionCancellation;
import com.bankinc.manager.model.TransactionRequest;
import com.bankinc.manager.model.TransactionSummary;
import com.bankinc.manager.repository.CardRepository;
import com.bankinc.manager.repository.TransactionRepository;

@Service
public class TransactionServiceImpl implements TransactionService {

	@Autowired
	CardRepository cardRepository;

	@Autowired
	TransactionRepository transactionRepository;

	@Override
	public TransactionSummary processPurchase(TransactionRequest transactionRequest)
			throws CardManagerBusinessException {
		Optional<Card> optionalCard = cardRepository.findByCardNumber(transactionRequest.getCardId());
		if (optionalCard.isPresent()) {
			Card card = optionalCard.get();
			Double cardBalance = card.getBalance();
			Double transactionPrice = transactionRequest.getPrice();
			if (Boolean.TRUE.equals(card.getIsEnabled())) {
				Date currentDate = new Date();
				if (currentDate.before(card.getExpirationDate())) {
					if (transactionPrice < cardBalance) {
						card.setBalance(cardBalance - transactionPrice);
						cardRepository.save(card);
						Transaction transaction = new Transaction(currentDate, TransactionStatusType.PROCESSING,
								transactionPrice, card);
						transactionRepository.save(transaction);
						return new TransactionSummary(transaction);
					}
					throw new CardManagerBusinessException("The price is higher than the available balance");
				}
				throw new CardManagerBusinessException("The card is Expired");
			}
			throw new CardManagerBusinessException("The card is locked");
		}
		throw new CardManagerBusinessException("The cardId does not exist");
	}

	@Override
	public void cancelPurchase(TransactionCancellation transactionCancellation) throws CardManagerBusinessException {
		Optional<Transaction> optionalTransaction = transactionRepository
				.findById(transactionCancellation.getTransactionId());
		if (optionalTransaction.isPresent()) {
			Transaction transaction = optionalTransaction.get();
			Date currentDate = new Date();
			if (transaction.getPurchaseStatus().equals(TransactionStatusType.CANCELED)) {
				throw new CardManagerBusinessException(
						"The transaction is already Cancelled");
			}
			Long hoursPassed = ChronoUnit.HOURS.between(currentDate.toInstant(),
					optionalTransaction.get().getPurchaseDate().toInstant());
			if (hoursPassed < 24 || transaction.getPurchaseStatus().equals(TransactionStatusType.ACCEPTED)) {
				Card transactionCard = transaction.getCard();
				transactionCard.setBalance(transactionCard.getBalance() + transaction.getPurchasePrice());
				transaction.setPurchaseStatus(TransactionStatusType.CANCELED);
				cardRepository.save(transactionCard);
				transactionRepository.save(transaction);
				return;
			}
			throw new CardManagerBusinessException(
					"is not possible to cancel it. It have already passed more than one day");
		}
		throw new CardManagerBusinessException("The transactionId does not exist");

	}

	@Override
	public TransactionSummary getPurchase(String transactionId) throws CardManagerBusinessException {
		Optional<Transaction> optionalTransaction = transactionRepository.findById(transactionId);
		if (optionalTransaction.isPresent()) {
			Transaction transaction = optionalTransaction.get();
			return new TransactionSummary(transaction);
		}
		throw new CardManagerBusinessException("The transactionId does not exist");

	}

}
