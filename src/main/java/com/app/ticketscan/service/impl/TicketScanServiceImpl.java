package com.app.ticketscan.service.impl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.app.ticketscan.constants.TicketScanConstants;
import com.app.ticketscan.exception.TicketScanBusinessException;
import com.app.ticketscan.service.ITicketScanService;

/**
 * 
 * @author Pari Paramasivam
 *
 */
@Service
public class TicketScanServiceImpl implements ITicketScanService {

	Logger logger = LoggerFactory.getLogger(TicketScanServiceImpl.class);

	/**
	 * Get input stream and calculates scan error rate. Throws
	 * TicketScanBusinessException for invalid data, file and for any unexpected
	 * scenarios
	 * 
	 * @param inputStrem
	 * @return
	 * @throws TicketScanBusinessException
	 */
	@Override
	public int getTicketScanErrorRate(InputStream inputStream) throws TicketScanBusinessException {
		logger.info("Inside method getTicketScanErrorRate {}", this.getClass());

		int errorRate = 0;
		try {
			// Reading file content from input as list of strings(each line in file)
			List<String> notesLines = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
					.lines().collect(Collectors.toList());
			// Calculate error rate
			errorRate = calculateErrorRate(notesLines);
		} catch (Exception e) {
			throw new TicketScanBusinessException(e.getMessage());
		}
		return errorRate;
	}

	/**
	 * Calculate error rate Identify valid value ranges from first section (rules
	 * section). Second section (your ticket) need not to be processed as we are
	 * finding error rate from invalid nearby tickets. Read each tickets from third
	 * section (nearby tickets) and verify each values in the ticket is valid by
	 * referring valid values from rules section. Sum up invalid values and return
	 * it.
	 */

	private int calculateErrorRate(List<String> notesLines) {
		logger.info("Inside method calculateErrorRate {}", this.getClass());
		Set<Integer> validValuesSet = new HashSet<Integer>();
		int scanningErrorRate = 0;

		String process = TicketScanConstants.RULES;

		logger.info("Loop through each line in the notes");
		for (String notesLine : notesLines) {

			switch (process) {
			// Process rules section, create Set containing valid values
			case TicketScanConstants.RULES: {
				if (notesLine == null || notesLine.isEmpty()) {
					process = TicketScanConstants.YOURTICKET;
					break;
				}
				String[] validFieldRanges = notesLine
						.replaceAll(TicketScanConstants.REGEX_ALLOW_INT_ONLY, TicketScanConstants.WHITE_SPACE).trim()
						.split(TicketScanConstants.WHITE_SPACE);
				createValidValues(validFieldRanges, validValuesSet);
			}
			// Process your ticket section, doing nothing
			case TicketScanConstants.YOURTICKET: {
				if (notesLine == null || notesLine.isEmpty()) {
					process = TicketScanConstants.NEARBYTICKETS;
					break;
				}
			}
			// Process nearby tickets section, scan each value in nearby tickets and sum up
			// invalid values
			case TicketScanConstants.NEARBYTICKETS: {
				if (notesLine == null || notesLine.isEmpty()) {
					process = TicketScanConstants.END;
					break;
				} else if (notesLine.contains(TicketScanConstants.COLON)) {
					continue;
				} else {
					List<Integer> nearbyTicketDetails = Stream.of(notesLine.split(TicketScanConstants.COMMA_DELIMITER))
							.map(String::trim).map(Integer::parseInt).collect(Collectors.toList());
					int nearbyTicketDtlSize = nearbyTicketDetails.size();
					for (int index = 0; index < nearbyTicketDtlSize; index++) {

						if (validValuesSet.contains(nearbyTicketDetails.get(index))) {
							continue;
						} else {
							logger.info("------Invalid value - : " + nearbyTicketDetails.get(index));
							scanningErrorRate = scanningErrorRate + nearbyTicketDetails.get(index);
						}

					}
				}

			}

			default:
				break;
			}

		}

		logger.info("Scanning Error Rate: " + scanningErrorRate);
		return scanningErrorRate;
	}

	/**
	 * Add values in the given range in Set input field validFieldRanges has four
	 * values. First two and last two forms range.
	 */
	private void createValidValues(String[] validFieldRanges, Set<Integer> validValuesSet) {
		for (int i = Integer.parseInt(validFieldRanges[0]); i <= Integer.parseInt(validFieldRanges[1]); i++) {
			validValuesSet.add(i);
		}
		for (int i = Integer.parseInt(validFieldRanges[2]); i <= Integer.parseInt(validFieldRanges[3]); i++) {
			validValuesSet.add(i);
		}
	}

}
