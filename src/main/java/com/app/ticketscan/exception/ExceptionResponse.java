package com.app.ticketscan.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionResponse {

	private String errorMessage;
	private String status;
	

	public ExceptionResponse(String message) {
		this.errorMessage = message;
	}
}