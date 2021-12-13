package com.app.ticketscan.controller;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.app.ticketscan.constants.TicketScanConstants;
import com.app.ticketscan.exception.ExceptionResponse;
import com.app.ticketscan.exception.InvalidFileTypeException;
import com.app.ticketscan.exception.TicketScanBusinessException;
import com.app.ticketscan.model.TicketScanResponse;
import com.app.ticketscan.service.ITicketScanService;

/**
 * Provides end point to scan ticket notes
 * 
 * @author Pari Paramasivam
 *
 */
@RestController
@RequestMapping("/v1/api")
public class TicketScanController {

	Logger logger = LoggerFactory.getLogger(TicketScanController.class);

	@Autowired
	private ITicketScanService ticketScanService;
	
	/**
	 * End point to accept text file, process it and calculates error rate.
	 * Respond with error code for invalid file/data.
	 * 
	 * @param file MultipartFile
	 * @return ResponseEntity<TicketScanResponse>
	 * @throws TicketScanBusinessException
	 */
	@PostMapping(value = "/errorrate")
	public ResponseEntity<TicketScanResponse> scanErrorRate(
			@RequestPart(value = "file", required = true) MultipartFile file) throws TicketScanBusinessException {
		logger.info("Into getScan method in class {}", this.getClass());
		TicketScanResponse ticketScanResponse = new TicketScanResponse();
		try {

			String fileName = file.getOriginalFilename();
			String fileType = fileName.substring(fileName.lastIndexOf('.') + 1);
			logger.info("Checking input file type.");
			if (!fileType.equals(TicketScanConstants.ALLOWED_FILE_TYPE)) {
				logger.info("Invalid file type error.{}", this.getClass());
				throw new InvalidFileTypeException(TicketScanConstants.INVALID_FILE_TYPE_MSG);
			}
			int errorRate = ticketScanService.getTicketScanErrorRate(file.getInputStream());
			ticketScanResponse.setErrorRate(errorRate);
		} catch (InvalidFileTypeException e) {
			ExceptionResponse exceptionDetails = new ExceptionResponse(e.getMessage());
			exceptionDetails.setStatus(HttpStatus.NOT_ACCEPTABLE.toString());
			logger.info("Set error details in response");
			ticketScanResponse.setExceptionDetails(exceptionDetails);
			return new ResponseEntity<TicketScanResponse>(ticketScanResponse, HttpStatus.NOT_ACCEPTABLE);
		} catch (IOException | TicketScanBusinessException e) {
			ExceptionResponse exceptionDetails = new ExceptionResponse(e.getMessage());
			exceptionDetails.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.toString());
			logger.info("Set error details in response");
			ticketScanResponse.setExceptionDetails(exceptionDetails);
			return new ResponseEntity<TicketScanResponse>(ticketScanResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<TicketScanResponse>(ticketScanResponse, HttpStatus.OK);
	}
}
