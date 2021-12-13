package com.app.ticketscan;

import static org.junit.Assert.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.app.ticketscan.controller.TicketScanController;
import com.app.ticketscan.service.ITicketScanService;

@SpringBootTest
public class TicketScanApplicationTests {
	
	@Autowired
	TicketScanController controller;
	
	@Autowired
	ITicketScanService ticketscanService;
	@Test
	public void contextLoads() {
		assertNotNull(controller);
		assertNotNull(ticketscanService);
	}

}
