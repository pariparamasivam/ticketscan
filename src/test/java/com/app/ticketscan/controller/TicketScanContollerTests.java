package com.app.ticketscan.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.ResourceUtils;

import com.app.ticketscan.exception.TicketScanBusinessException;
import com.app.ticketscan.model.TicketScanResponse;
import com.app.ticketscan.service.ITicketScanService;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
public class TicketScanContollerTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private ITicketScanService ticketScanService;

	@Test
	public void scanErrorRateInvalidFileTypeTest() throws Exception {
		File file = ResourceUtils.getFile("classpath:notes.doc");
		InputStream targetStream = new FileInputStream(file);

		MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "notes.doc", "application/msword",
				targetStream);
		mockMvc.perform(MockMvcRequestBuilders.multipart("/ticketscan/v1/api/errorrate").file(mockMultipartFile)
				.contextPath("/ticketscan")).andExpect(status().isNotAcceptable());
	}

	@Test
	public void scanErrorRateResponseOk() throws Exception {
		TicketScanResponse mockResponse = new TicketScanResponse(71, null);

		ObjectMapper mapper = new ObjectMapper();
		String expectedJSON = mapper.writeValueAsString(mockResponse);

		File file = ResourceUtils.getFile("classpath:notes.txt");
		InputStream targetStream = new FileInputStream(file);
		when(ticketScanService.getTicketScanErrorRate(any())).thenReturn(71);
		MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "notes.txt", "text/plain", targetStream);
		MvcResult actualResult = mockMvc.perform(MockMvcRequestBuilders.multipart("/ticketscan/v1/api/errorrate")
				.file(mockMultipartFile).contextPath("/ticketscan")).andExpect(status().isOk()).andReturn();
		JSONAssert.assertEquals(expectedJSON, actualResult.getResponse().getContentAsString(), false);

	}

	@Test
	public void scanErrorRateBusinessException() throws Exception {
		when(ticketScanService.getTicketScanErrorRate(any())).thenThrow(TicketScanBusinessException.class);
		File file = ResourceUtils.getFile("classpath:notes.txt");
		InputStream targetStream = new FileInputStream(file);

		MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "notes.txt", "text/plain", targetStream);
		mockMvc.perform(MockMvcRequestBuilders.multipart("/ticketscan/v1/api/errorrate").file(mockMultipartFile)
				.contextPath("/ticketscan")).andExpect(status().is5xxServerError());
	}

}
