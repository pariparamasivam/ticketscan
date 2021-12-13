package com.app.ticketscan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
@SpringBootApplication
@ComponentScan(basePackages = { "com.app.ticketscan" })
@Configuration
@OpenAPIDefinition(info = @Info(title = "Ticket Scan API", version = "1", description = "Ticket Scan Service"))
public class TicketScanApplication {

	public static void main(String[] args) {
		SpringApplication.run(TicketScanApplication.class, args);
	}

}
