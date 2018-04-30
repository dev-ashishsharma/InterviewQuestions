package com.ashish.coding.challenge;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bouncycastle.util.encoders.Hex;

import com.ashish.coding.challenge.filters.DataFilter;
import com.ashish.coding.challenge.writer.CsvFileWriter;

public class TripsData {
	
	private HashMap<String, List<TripData>> cardTrips;
	private HashMap<String, List<String>> previousTripRecord;
	private SummaryTripData summaryData;
	public enum TAP_VALUE {ON, OFF, NONE;}
	public enum STOP_ID {Stop1, Stop2, Stop3, NONE;}
	public enum TRIP_STATUS {COMPLETE, INCOMPLETE, CANCELLED;}
	
	public TripsData() {
		cardTrips = new HashMap<>();
		previousTripRecord = new HashMap<>();
		summaryData = new SummaryTripData();
	}

	/**
	 * This method recorrds the trips data and writes the csv files.
	 * @param tripTransactions all the valid transactions on input file.
	 */
	public void identifyTrips(List<List<String>> tripTransactions) {
		//Loop through all data and record the trips.
		for (List<String> singleTransaction : tripTransactions) {
			recordTripData(singleTransaction);
		}
		// Write the Trips.csv & Summary.csv files.
		CsvFileWriter.writeTripsCsvFile(cardTrips);
		CsvFileWriter.writeSummaryTripsCsvFile(summaryData.getSummaryData());
	}
	
	/**
	 * This method is the main worker which identifies and records the trip data from the
	 * input file.
	 * @param singleTransaction is a row of the input file which is one trip record.
	 */
	public void recordTripData(List<String> singleTransaction) {
		TAP_VALUE currentTap = TAP_VALUE.valueOf(singleTransaction.get(DataFilter.TransactionStringIndexer.TAP_TYPE.ordinal()).trim());
		STOP_ID currentStopId = STOP_ID.valueOf(singleTransaction.get(DataFilter.TransactionStringIndexer.STOP_ID.ordinal()).trim());
		String panId = singleTransaction.get(DataFilter.TransactionStringIndexer.PAN.ordinal()).trim();
		TAP_VALUE previousTap = TAP_VALUE.NONE; 
		STOP_ID previousStopId = STOP_ID.NONE;
		BigDecimal totalFare = BigDecimal.ZERO;
		List<String> previousTrip = previousTripRecord.get(panId);
		if (previousTrip != null) {
			previousTap = TAP_VALUE.valueOf(previousTrip.get(DataFilter.TransactionStringIndexer.TAP_TYPE.ordinal()).trim());
			previousStopId = STOP_ID.valueOf(previousTrip.get(DataFilter.TransactionStringIndexer.STOP_ID.ordinal()).trim());
		}
		// First TAP should always be ON
		switch (currentTap) {
		case ON:
			if (TAP_VALUE.ON.equals(previousTap)) {
				// Case of an INCOMPLETE Trip: Where ON after ON
				STOP_ID farthestStop = calculateFarthestStop(previousStopId);
				totalFare = getFare(previousStopId, farthestStop);
				calculateTrip(panId, totalFare, singleTransaction, previousTrip,TRIP_STATUS.INCOMPLETE);
				previousTap = currentTap;
				previousStopId = currentStopId;
				previousTripRecord.remove(panId);
				// Update Map with new ON data.
				previousTripRecord.put(panId, singleTransaction);
			} 
			else {
				// Case of Previous either NONE or OFF. Both cases
				// need to process next Tap to decide trip status.
				previousTap = currentTap;
				previousStopId = currentStopId;
				previousTripRecord.put(panId, singleTransaction);
			}
			break;
		case OFF:
			if (TAP_VALUE.ON.equals(previousTap)) {
				if (!isTripCancelled(currentStopId, previousStopId)) {
					// This is COMPLETED TRIP
					totalFare = getFare(currentStopId, previousStopId);
					calculateTrip(panId, totalFare, singleTransaction, previousTrip,TRIP_STATUS.COMPLETE);
				} else {
					// THIS IS CANCELLED TRIP
					calculateTrip(panId, totalFare, singleTransaction, previousTrip,TRIP_STATUS.CANCELLED);
				}
				previousStopId = STOP_ID.NONE;
				previousTap = TAP_VALUE.NONE;
				previousTripRecord.remove(panId);
			} 
			else if(TAP_VALUE.OFF.equals(previousTap)){
				//This should never happen
				//NO consecutive OFF. Duplicates are already removed.
			}
			break;

		default:
			System.out.println("Unexpected TAP Value");
			break;
		}
	}
	
	private void calculateTrip(String panId, BigDecimal chargedAmount, List<String> endTrip, List<String> startTrip, TRIP_STATUS tripStatus) {
		TripData currentTrip = new TripData();
		String started = startTrip.get(DataFilter.TransactionStringIndexer.DATE.ordinal()).trim();
		String finished = endTrip.get(DataFilter.TransactionStringIndexer.DATE.ordinal()).trim();
		// String durationSec
		String fromStopId = startTrip.get(DataFilter.TransactionStringIndexer.STOP_ID.ordinal()).trim();
		String toStopId = endTrip.get(DataFilter.TransactionStringIndexer.STOP_ID.ordinal()).trim();
		String companyId = startTrip.get(DataFilter.TransactionStringIndexer.COMPANY.ordinal()).trim();
		String busId = startTrip.get(DataFilter.TransactionStringIndexer.BUS_ID.ordinal()).trim();
		String hashedCardNumber =  getHashedCardNumber(panId);
		
		currentTrip.setStarted(started);
		currentTrip.setFinished(finished);
		currentTrip.setFromStop(fromStopId);
		currentTrip.setToStop(toStopId);
		currentTrip.setCompanyId(companyId);
		currentTrip.setBusId(busId);
		currentTrip.setChargedAmount(chargedAmount);
		currentTrip.setStatus(tripStatus.toString().trim());
		currentTrip.setHashedPAN(hashedCardNumber);
		
		// Add the Current trip to the total Trips Data record.
		addCurrentTripToTripsData(currentTrip, panId);
		// Calculate the Trip summary for this Current Trip.
		summaryData.calculateTripSummary(currentTrip);
	}

	/**
	 * Add the current trip to total trip against the PAN id.
	 * @param currentTrip to be added
	 * @param panId the key of the map.
	 */
	private void addCurrentTripToTripsData(TripData currentTrip, String panId) {
		if (cardTrips.containsKey(panId)) {
			List<TripData> trips = cardTrips.get(panId);
			trips.add(currentTrip);
			cardTrips.put(panId, trips);
		} else {
			List<TripData> trips = new ArrayList<>();
			trips.add(currentTrip);
			cardTrips.put(panId, trips);
		}
	}

	/**
	 * The method hashes the panId to ensure its written into the file.
	 * @param panId to be hashed
	 * @return hashedCardNumber of the panId.
	 */
	public static String getHashedCardNumber(String panId) {
		String hashedNumber = "";
		if (!panId.trim().isEmpty()) {
			try {
				MessageDigest digest = MessageDigest.getInstance("SHA-256");
				byte[] hash = digest.digest(panId.getBytes(StandardCharsets.UTF_8));
				hashedNumber = new String(Hex.encode(hash));
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
		return hashedNumber;
	}

	/**
	 * The method is used to identify if trip starts and ends at same stop.
	 * @param currentStop 
	 * @param previousStop
	 * @return true if current stop equals previous stop.
	 */
	private boolean isTripCancelled(STOP_ID currentStop, STOP_ID previousStop) {
		return currentStop.equals(previousStop);
	}
	
	/**
	 * This method is used to calculate the farthest stop for a trip
	 * which has not been completed by a TAP OFF.
	 * @param startStop where TAP ON was done.
	 * @return farthestStop from the startStop.
	 */
	private STOP_ID calculateFarthestStop(STOP_ID startStop) {
		STOP_ID farthestStop = STOP_ID.NONE;
		
		switch (startStop) {
		case Stop1:
			farthestStop = STOP_ID.Stop3;
			break;
		case Stop2:
			farthestStop = STOP_ID.Stop3;
			break;
		case Stop3:
			farthestStop = STOP_ID.Stop1;
			break;
		case NONE:
			break;
		}
		return farthestStop;
	}
	
	/**
	 * This method returns the fare based on start and end stop.
	 * @param startStop
	 * @param endStop
	 * @return totalFare between startStop & endStop.
	 */
	private BigDecimal getFare (STOP_ID startStop, STOP_ID endStop) {
		BigDecimal totalFare = BigDecimal.ZERO;
		
		switch (startStop) {
		case Stop1:
			if (STOP_ID.Stop2.equals(endStop)) {
				totalFare = BigDecimal.valueOf(3.25);
			} else if (STOP_ID.Stop3.equals(endStop)){
				totalFare = BigDecimal.valueOf(7.30);
			}
			break;
		case Stop2:
			if (STOP_ID.Stop1.equals(endStop)) {
				totalFare = BigDecimal.valueOf(3.25);
			} else if (STOP_ID.Stop3.equals(endStop)){
				totalFare = BigDecimal.valueOf(5.50);
			}
			break;
		case Stop3:
			if (STOP_ID.Stop1.equals(endStop)) {
				totalFare = BigDecimal.valueOf(7.30);
			} else if (STOP_ID.Stop2.equals(endStop)){
				totalFare = BigDecimal.valueOf(5.50);
			}
			break;
		default:
			System.out.println("No Fare Calculation Needed");
			break;
		}
		return totalFare;
	}
}
