package com.bankinc.manager.model;

import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class ProcessResponse {

	@JsonProperty("requestBody")
	protected Process process;

	@JsonProperty("errorMessage")
	protected List<String> message;

	protected ProcessResponse(Process process, String message) {
		this.process = process;
		this.message = Arrays.asList(message);
	}

	protected ProcessResponse(Process process, List<String> message) {
		this.process = process;
		this.message = message;
	} 
	
	

	
}
