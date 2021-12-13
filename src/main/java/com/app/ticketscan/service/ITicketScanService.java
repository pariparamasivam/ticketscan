package com.app.ticketscan.service;

import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;

import com.app.ticketscan.exception.TicketScanBusinessException;

public interface ITicketScanService {

	int getTicketScanErrorRate(InputStream inputStream) throws TicketScanBusinessException;


}
