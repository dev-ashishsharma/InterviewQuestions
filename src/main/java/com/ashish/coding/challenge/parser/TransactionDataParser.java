package com.ashish.coding.challenge.parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.ashish.coding.challenge.TripsData;
import com.ashish.coding.challenge.UnprocessedTripData;
import com.ashish.coding.challenge.filters.DataFilter;
import com.ashish.coding.challenge.writer.CsvFileWriter;

public class TransactionDataParser {
	
	//Store the unprocessed Data.
	private static List<UnprocessedTripData> totalUnprocessedData;
	// Total Trips data.
	private TripsData tripsData;
	
	public TransactionDataParser() {
		totalUnprocessedData = new ArrayList<>();
		tripsData = new TripsData();
	}

	/**
	 * Parse the Transaction data provided in Input file and caculate the
	 * trips as well as the unprocessed Data.
	 * @param completeFilePath of the file which contains Transactions.
	 */
	public void parseTransactionData(String completeFilePath) {
		try (Stream<String> lines = Files.lines(Paths.get(completeFilePath))) {
			List<List<String>> transactionData = lines
					.skip(1)
					.filter(filterTransactionData)
					.distinct()
					.map(line -> Arrays.asList(line.split(",")))
					.collect(Collectors.toList());
			// Data is Filtered. Calculate the Trips.
			tripsData.identifyTrips(transactionData);
		} catch (IOException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		}
	}
	
	/**
	 * Filter the Transaction data to ensure that incorrect records are
	 * written to unprocessedTaps.csv.
	 */
	public static Predicate<String> filterTransactionData = (line) -> {
		String[] values = line.split(",");
		String errorMessage = DataFilter.filterTransactionData(values, totalUnprocessedData);
		// Write the Unprocessed Trips Data to File.
		CsvFileWriter.writeUnprocessedTripsCsvFile(totalUnprocessedData);
		return errorMessage.trim().isEmpty();
	};

}
