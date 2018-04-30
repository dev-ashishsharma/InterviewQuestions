package com.ashish.coding.challenge.writer;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapIterator;
import org.apache.commons.collections.keyvalue.MultiKey;
import org.apache.commons.collections.map.MultiKeyMap;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import com.ashish.coding.challenge.TripData;
import com.ashish.coding.challenge.TripSummaryData;
import com.ashish.coding.challenge.UnprocessedTripData;

public class CsvFileWriter {

	/**
	 * Write the trips.csv file.
	 * 
	 * @param cardTrips
	 *            total trips record.
	 */
	public static void writeTripsCsvFile(HashMap<String, List<TripData>> cardTrips) {
		FileWriter fileWriter = null;
		CSVPrinter csvFilePrinter = null;
		CSVFormat csvFileFormat = CSVFormat.DEFAULT.withRecordSeparator("\n");
		String completePath = "trips.csv";
		String[] FILE_HEADER = { "started", "finished", "DurationSec", "fromStopId", "toStopId", "ChargeAmount",
				"CompanyId", "BusId", "HashedPan", "Status" };

		try {
			fileWriter = new FileWriter(completePath);
			csvFilePrinter = new CSVPrinter(fileWriter, csvFileFormat);

			csvFilePrinter.printRecord(FILE_HEADER);
			List<String> printData = new ArrayList<>();
			for (Map.Entry<String, List<TripData>> entry : cardTrips.entrySet()) {
				for (TripData trip : entry.getValue()) {
					printData.add(trip.getStarted());
					printData.add(trip.getFinished());
					printData.add(trip.getFromStop());
					printData.add(trip.getToStop());
					printData.add(trip.getChargedAmount().toString());
					printData.add(trip.getCompanyId());
					printData.add(trip.getBusId());
					printData.add(trip.getHashedPAN());
					printData.add(trip.getStatus());
					csvFilePrinter.printRecord(printData);
					printData.clear();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fileWriter.flush();
				fileWriter.close();
				csvFilePrinter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	/**
	 * Write the unprocessedTaps.csv
	 * 
	 * @param unprocessedData
	 *            which could not be parsed.
	 */
	public static void writeUnprocessedTripsCsvFile(List<UnprocessedTripData> unprocessedData) {

		FileWriter fileWriter = null;
		CSVPrinter csvFilePrinter = null;
		CSVFormat csvFileFormat = CSVFormat.DEFAULT.withRecordSeparator("\n");
		String completePath = "unprocessableTaps.csv";
		String[] FILE_HEADER = { "id", "dateTime", "TapType", "StopId", "CompanyId", "BusId", "PAN" };
		try {
			fileWriter = new FileWriter(completePath);
			csvFilePrinter = new CSVPrinter(fileWriter, csvFileFormat);

			csvFilePrinter.printRecord(FILE_HEADER);
			List<String> printData = new ArrayList<>();
			for (UnprocessedTripData unprocessedTripData : unprocessedData) {
				printData.add(unprocessedTripData.getId());
				printData.add(unprocessedTripData.getDateTime());
				printData.add(unprocessedTripData.getTapType());
				printData.add(unprocessedTripData.getStopId());
				printData.add(unprocessedTripData.getCompanyId());
				printData.add(unprocessedTripData.getBusId());
				printData.add(unprocessedTripData.getHashedPan());
				printData.add(unprocessedTripData.getReason());
				csvFilePrinter.printRecord(printData);
				printData.clear();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fileWriter.flush();
				fileWriter.close();
				csvFilePrinter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Write the summary.csv File.
	 * @param summaryData based on specified keys.
	 */
	public static void writeSummaryTripsCsvFile(MultiKeyMap summaryData) {

		FileWriter fileWriter = null;
		CSVPrinter csvFilePrinter = null;
		CSVFormat csvFileFormat = CSVFormat.DEFAULT.withRecordSeparator("\n");
		String completePath = "summary.csv";
		String[] FILE_HEADER = { "date", "CompanyId", "BusId", "CompleteTripCount", "IncompleteTripCount",
				"CancelledTripCount", "TotalCharges" };
		try {
			fileWriter = new FileWriter(completePath);
			csvFilePrinter = new CSVPrinter(fileWriter, csvFileFormat);

			csvFilePrinter.printRecord(FILE_HEADER);
			List<String> printData = new ArrayList<>();
			MapIterator it = summaryData.mapIterator();
			while (it.hasNext()) {
				it.next();

				MultiKey mk = (MultiKey) it.getKey();

				for (Object subKey : mk.getKeys()) {
					printData.add((String) subKey);
				}
				TripSummaryData summary = (TripSummaryData) it.getValue();
				printData.add(String.valueOf(summary.getCompletedTrips()));
				printData.add(String.valueOf(summary.getIncompleteTrips()));
				printData.add(String.valueOf(summary.getCancelledTrips()));
				printData.add(summary.getTotalChargedAmount().toString());
				csvFilePrinter.printRecord(printData);
				printData.clear();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fileWriter.flush();
				fileWriter.close();
				csvFilePrinter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
