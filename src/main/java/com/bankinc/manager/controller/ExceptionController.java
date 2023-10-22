package com.bankinc.manager.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.bankinc.manager.exception.CardManagerBusinessException;
import com.bankinc.manager.model.Response;
import com.bankinc.manager.model.Process;

@ControllerAdvice
public class ExceptionController {

	@ExceptionHandler(CardManagerBusinessException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ResponseBody
	public Response handleCardManagerBusinessException(CardManagerBusinessException businessException,
			HttpServletRequest httpServletRequest) {
		Process cardProcess = (Process) httpServletRequest.getAttribute("ReqBody");
		return new Response(cardProcess, businessException.getMessage());
	}
	
	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ResponseBody
	public Response handleIllegalArgumentException(IllegalArgumentException businessException,
			HttpServletRequest httpServletRequest) {
		return new Response(null, businessException.getMessage());
	}


	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ResponseBody
	public Response handleMethodArgumentNotValidException(
			MethodArgumentNotValidException methodArgumentNotValidException, HttpServletRequest httpServletRequest) {
		BindingResult bindingResult = methodArgumentNotValidException.getBindingResult();
		List<FieldError> fieldErrors = bindingResult.getFieldErrors();
		return new Response(null, fieldErrors.stream().map(FieldError::getDefaultMessage).collect(Collectors.toList()));
	}

}
