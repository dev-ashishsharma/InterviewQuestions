package com.ashish.coding.challenge;

/**
 * This class stores the trip data received which cannot be processed
 * for any mistake or error. This is written to unprocessableTaps.csv
 * @author ashishsharma
 *
 */
public class UnprocessedTripData {
	private String dateTime;
	private String id;
	private String tapType;
	private String stopId;
	private String companyId;
	private String busId;
	private String hashedPan;
	private String reason;
	
	public UnprocessedTripData(String dateTime, String id, String tapType, String stopId, String companyId,
			String busId, String hashedPan, String reason) {
		this.dateTime = dateTime;
		this.id = id;
		this.tapType = tapType;
		this.stopId = stopId;
		this.companyId = companyId;
		this.busId = busId;
		this.hashedPan = hashedPan;
		this.reason = reason;
	}

	/**
	 * Getters of Fields.
	 */
	public String getDateTime() {
		return dateTime;
	}
	public String getReason() {
		return reason;
	}
	public String getId() {
		return id;
	}
	public String getTapType() {
		return tapType;
	}
	public String getStopId() {
		return stopId;
	}
	public String getCompanyId() {
		return companyId;
	}
	public String getBusId() {
		return busId;
	}
	public String getHashedPan() {
		return hashedPan;
	}
}
