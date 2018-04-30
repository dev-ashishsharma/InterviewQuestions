package com.ashish.coding.challenge;

import org.apache.commons.collections.map.MultiKeyMap;

/**
 * A class to calculate & store the summary data of the trips parsed
 * through the Input File.
 * @author ashishsharma
 *
 */
public class SummaryTripData {
	
	// Multi Key Map to store the summary data of the trips.
	private MultiKeyMap multiKeyMapOfSummaryData;
	
	public SummaryTripData() {
		multiKeyMapOfSummaryData = new MultiKeyMap();
	}
	
	/**
	 * This method calculates the trip summary data & updates the 
	 * multi key map.
	 * @param currentTrip being processed from the list of trips.
	 */
	public void calculateTripSummary(TripData currentTrip) {
		String[] dateTime = currentTrip.getStarted().split(" ");
		String date = dateTime[0];
		// Get trip from Map
		TripSummaryData tripSummaryData = (TripSummaryData) multiKeyMapOfSummaryData.get(date, 
				currentTrip.getCompanyId(), currentTrip.getBusId());
		if (tripSummaryData == null) {
			tripSummaryData = new TripSummaryData();
		}
		updateTripSummaryCounts(currentTrip, tripSummaryData);
		// Update the Amount
		tripSummaryData.addToTotalChargedAmount(currentTrip.getChargedAmount());
		multiKeyMapOfSummaryData.put(date, currentTrip.getCompanyId(), currentTrip.getBusId(), tripSummaryData);
	}

	/**
	 * This method updates the Complete, Incomplete or Cancelled trip counts for the
	 * trip summary data for a specified multi-key.
	 * @param currentTrip being processed
	 * @param tripSummaryData for the specified multiKey.
	 */
	private void updateTripSummaryCounts(TripData currentTrip, TripSummaryData tripSummaryData) {
		switch (currentTrip.getStatus().trim()) {
		case "COMPLETE":
			tripSummaryData.increaseCompletedTrips();
			break;
        case "INCOMPLETE":
			tripSummaryData.increaseIncompleteTrips();
			break;
        case "CANCELLED":
        	    tripSummaryData.increaseCancelledTrips();
	        break;
		default:
			System.out.println("Wrong Case");
			break;
		}
	}
	
	public MultiKeyMap getSummaryData() {
		return multiKeyMapOfSummaryData;
	}
}
