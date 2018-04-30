package com.ashish.coding.challenge.filters;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import com.ashish.coding.challenge.TripsData;
import com.ashish.coding.challenge.UnprocessedTripData;

/**
 * Filter class for the trip records.
 * @author ashishsharma
 */
public class DataFilter {

	// Enum for trip record mapping.
	public static enum TransactionStringIndexer {
		ID, DATE, TAP_TYPE, STOP_ID, COMPANY, BUS_ID,PAN;
	}
	
	// Date Time Formatter.
	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
	
	/**
	 * This method filters the trip record to ensure that incorrect data, missing data and duplicate records
	 * are removed before the trips are analyzed.
	 * @param csvSeparatedData input file
	 * @param unprocessedData which cannot be processed.
	 * @return
	 */
	public static String filterTransactionData(String[] csvSeparatedData, List<UnprocessedTripData> unprocessedData) {
		String errorMessage = "";
		String panData = "",stopData ="",tapData="",timeDateData = "";
		boolean pass = false;
		
		// Check if all fields are present.
		if (csvSeparatedData.length == TransactionStringIndexer.values().length) {
			
			panData = csvSeparatedData[TransactionStringIndexer.PAN.ordinal()].trim();
			stopData = csvSeparatedData[TransactionStringIndexer.STOP_ID.ordinal()].trim();
			tapData = csvSeparatedData[TransactionStringIndexer.TAP_TYPE.ordinal()].trim();
			timeDateData = csvSeparatedData[TransactionStringIndexer.DATE.ordinal()].trim();
			
			if (!validateTransactionData(p -> !p.trim().isEmpty(), panData)) {
				errorMessage = "PAN is Incorrect";
			}
			else if (!validateTransactionData(p -> {
				return p.trim().equalsIgnoreCase("Stop1") || p.trim().equalsIgnoreCase("Stop2")
						|| p.trim().equalsIgnoreCase("Stop3");
			}, stopData)) {
				errorMessage = "Stop Data is Incorrect";
			}
			else if (!validateTransactionData(p -> {
				return p.trim().equalsIgnoreCase("ON") || p.trim().equalsIgnoreCase("OFF");
			}, tapData)) {
				errorMessage = "TAP Data is Incorrect";
			}
			else if(!validateTransactionData(p -> {
				try {
				LocalDateTime transactionDateTime = LocalDateTime.
						parse(csvSeparatedData[TransactionStringIndexer.DATE.ordinal()].trim(), DATE_FORMATTER);
				return transactionDateTime.toLocalDate().compareTo(LocalDate.now()) <= 0;
				} catch (DateTimeParseException e) {
					return false;
				}
			}, timeDateData)) {
				errorMessage = "Date Time Data is Incorrect";
			}
			pass = errorMessage.trim().isEmpty();
		} else {
			// Identify which data is missing when length is Less.
			// Pattern matcher can be used. Currently just storing it for unprocessableTaps purposes.
			errorMessage = "Incorrect Data";
		}
		
		if (!pass) {
			String id = csvSeparatedData[TransactionStringIndexer.ID.ordinal()].trim();
			String company = csvSeparatedData[TransactionStringIndexer.COMPANY.ordinal()].trim();
			String busId = csvSeparatedData[TransactionStringIndexer.BUS_ID.ordinal()].trim();
			String hashedPan = TripsData.getHashedCardNumber(panData);
			UnprocessedTripData data = new UnprocessedTripData(timeDateData, id, tapData, 
					stopData, company, busId, hashedPan, errorMessage);
			unprocessedData.add(data);
		}
		return errorMessage;
	}

	/**
	 * Validate the transaction based on behaviour provided.
	 * @param predicate the behaviour.
	 * @param data to be validated
	 * @return true if data is successfully validated.
	 */
	private static boolean validateTransactionData(Predicate<String> predicate, String data) {
		return predicate.test(data);
	}
}
