package com.bankinc.manager.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bankinc.manager.exception.CardManagerBusinessException;
import com.bankinc.manager.model.TransactionCancellation;
import com.bankinc.manager.model.TransactionRequest;
import com.bankinc.manager.model.TransactionSummary;
import com.bankinc.manager.service.TransactionService;

@RestController
@RequestMapping("/transaction")
public class TransactionController {

	Logger logger = LoggerFactory.getLogger(TransactionController.class);

	@Autowired
	TransactionService transactionService;

	@PostMapping("/purchase")
	public ResponseEntity<TransactionSummary> processTransaction(@Valid @RequestBody TransactionRequest transactionRequest,
			HttpServletRequest httpServletRequest) throws CardManagerBusinessException {
		httpServletRequest.setAttribute("ReqBody", transactionRequest);
		return new ResponseEntity<>(transactionService.processPurchase(transactionRequest),HttpStatus.OK);
	}
	
	@PostMapping("/anulation")
	public ResponseEntity<Map<String, String>> cancelTransaction(@Valid @RequestBody TransactionCancellation transactionCancellation,
			HttpServletRequest httpServletRequest) throws CardManagerBusinessException {
		httpServletRequest.setAttribute("ReqBody", transactionCancellation);
		transactionService.cancelPurchase(transactionCancellation);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@GetMapping("/{transactionId}")
	public ResponseEntity<TransactionSummary> getTransaction(@PathVariable(name = "transactionId", required = true) String transactionId) throws CardManagerBusinessException {
		Validate.matchesPattern(transactionId, "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$","the transactionId is not valid");
		return new ResponseEntity<>(transactionService.getPurchase(transactionId),HttpStatus.OK);
	}

}
