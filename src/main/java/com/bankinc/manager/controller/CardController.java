package com.bankinc.manager.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bankinc.manager.exception.CardManagerBusinessException;
import com.bankinc.manager.model.CardActivation;
import com.bankinc.manager.model.CardRecharge;
import com.bankinc.manager.service.CardService;

@RestController
@RequestMapping("/card")
public class CardController {

	Logger logger = LoggerFactory.getLogger(CardController.class);

	@Autowired
	CardService cardService;

	@GetMapping("/{productId}/number")
	public ResponseEntity<Map<String, String>> getCardNumber(
			@PathVariable(name = "productId", required = true) String productId) {
		Validate.matchesPattern(productId, "^\\d{6}$","The productId is not valid");
		logger.trace("El product Id recibido es {}", productId);
		String generatedCardNumber = cardService.createCard(productId);
		Map<String, String> responseMap = new HashMap<>();
		responseMap.put("cardId", generatedCardNumber);
		return new ResponseEntity<>(responseMap, HttpStatus.CREATED);
	}

	@PostMapping("/enroll")
	public ResponseEntity<Map<String, String>> activateCard(@Valid @RequestBody CardActivation cardActivation,
			HttpServletRequest httpServletRequest) throws CardManagerBusinessException {
		httpServletRequest.setAttribute("ReqBody", cardActivation);
		cardService.activateCard(cardActivation);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@DeleteMapping("/{cardId}")
	public ResponseEntity<Map<String, String>> lockCard(
			@PathVariable(name = "cardId", required = true) String cardId) throws CardManagerBusinessException {
		Validate.matchesPattern(cardId, "^\\d{16}$","the CardId is not valid");
		cardService.disableCard(cardId);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@GetMapping("/{cardId}")
	public ResponseEntity<Map<String, Double>> obtainCardBalance(
			@PathVariable(name = "cardId", required = true) String cardId) throws CardManagerBusinessException {
		Validate.matchesPattern(cardId, "^\\d{16}$","the CardId is not valid");
		Double cardBalance = cardService.getBalance(cardId);
		Map<String, Double> responseMap = new HashMap<>();
		responseMap.put("cardBalance", cardBalance);
		return new ResponseEntity<>(responseMap, HttpStatus.OK);
	}
	
	@PostMapping("/balance")
	public ResponseEntity<Map<String, String>> rechargeCard(@Valid @RequestBody CardRecharge cardRecharge,
			HttpServletRequest httpServletRequest) throws CardManagerBusinessException {
		httpServletRequest.setAttribute("ReqBody", cardRecharge);
		cardService.updateBalance(cardRecharge);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	

}
