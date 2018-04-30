package com.ashish.coding.challenge;

import java.math.BigDecimal;

/**
 * This class is a placeholder for the trip summary data for 
 * specified keys.
 * @author ashishsharma
 *
 */
public class TripSummaryData {
	private int completedTrips;
	private int incompleteTrips;
	private int cancelledTrips;
	private BigDecimal totalChargedAmount;
	
	public TripSummaryData() {
		this.completedTrips = 0;
		this.incompleteTrips = 0;
		this.cancelledTrips = 0;
		this.totalChargedAmount = BigDecimal.ZERO;
	}
	
	public BigDecimal getTotalChargedAmount() {
		return totalChargedAmount;
	}
	/**
	 * Add the charge to totalChargedAmount.
	 * @param charge to be added.
	 */
	public void addToTotalChargedAmount (BigDecimal charge) {
		this.totalChargedAmount = this.totalChargedAmount.add(charge);
	}
	public int getCompletedTrips() {
		return completedTrips;
	}
	/**
	 * Increase the COMPLETE trip count by 1.
	 */
	public void increaseCompletedTrips() {
		this.completedTrips++;
	}
	public int getIncompleteTrips() {
		return incompleteTrips;
	}
	/**
	 * Increase the INCOMPLETE trip count by 1.
	 */
	public void increaseIncompleteTrips() {
		this.incompleteTrips++;
	}
	public int getCancelledTrips() {
		return cancelledTrips;
	}
	/**
	 * Increase the CANCELLED trip count by 1.
	 */
	public void increaseCancelledTrips() {
		this.cancelledTrips++;
	}
	
	
}
