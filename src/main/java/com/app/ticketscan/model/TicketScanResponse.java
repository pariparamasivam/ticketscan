package com.app.ticketscan.model;

import com.app.ticketscan.exception.ExceptionResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TicketScanResponse {

	private Integer errorRate;
	
	private ExceptionResponse exceptionDetails;
}
