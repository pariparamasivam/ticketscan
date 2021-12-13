package com.app.ticketscan.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.ResourceUtils;

import com.app.ticketscan.exception.TicketScanBusinessException;
import com.app.ticketscan.service.impl.TicketScanServiceImpl;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class TicketScanServiceTests {

	@InjectMocks
	ITicketScanService ticketScanService = new TicketScanServiceImpl();

	@Mock
	InputStream inputStream;

	@Test
	public void scanErrorRateBusinessOk() throws Exception {
		File file = ResourceUtils.getFile("classpath:notes.txt");
		InputStream is = new FileInputStream(file);
		int response = ticketScanService.getTicketScanErrorRate(is);
		assertEquals(71, response);

	}

	@Test
	public void scanErrorRateBusinessException() throws Exception {

		Assertions.assertThrows(TicketScanBusinessException.class, () -> {
			ticketScanService.getTicketScanErrorRate(null);
		});

	}

	@Test
	public void scanErrorRateBusinessExceptionForInvalidData() throws Exception {
		File file = ResourceUtils.getFile("classpath:invalidNotes.txt");
		InputStream is = new FileInputStream(file);
		Assertions.assertThrows(TicketScanBusinessException.class, () -> {
			ticketScanService.getTicketScanErrorRate(is);
		});

	}

}
