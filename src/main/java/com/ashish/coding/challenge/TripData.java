package com.ashish.coding.challenge;

import java.math.BigDecimal;

/**
 * This class stores a single trip data.
 * @author ashishsharma
 *
 */
public class TripData {
	
	private String started;
	private String finished;
	private long durationSeconds;
	private String fromStop;
	private String toStop;
	private BigDecimal chargedAmount;
	private String companyId;
	private String busId;
	private String hashedPAN;
	private String status;
	
	/**
	 * Getter of fields.
	 */
	public String getStarted() {
		return started;
	}
	public void setStarted(String started) {
		this.started = started;
	}
	public String getFinished() {
		return finished;
	}
	public void setFinished(String finished) {
		this.finished = finished;
	}
	public long getDurationSeconds() {
		return durationSeconds;
	}
	public void setDurationSeconds(long durationSeconds) {
		this.durationSeconds = durationSeconds;
	}
	public String getFromStop() {
		return fromStop;
	}
	public void setFromStop(String fromStop) {
		this.fromStop = fromStop;
	}
	public String getToStop() {
		return toStop;
	}
	public void setToStop(String toStop) {
		this.toStop = toStop;
	}
	public BigDecimal getChargedAmount() {
		return chargedAmount;
	}
	public void setChargedAmount(BigDecimal chargedAmount) {
		this.chargedAmount = chargedAmount;
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getBusId() {
		return busId;
	}
	public void setBusId(String busId) {
		this.busId = busId;
	}
	public String getHashedPAN() {
		return hashedPAN;
	}
	public void setHashedPAN(String hashedPAN) {
		this.hashedPAN = hashedPAN;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	

}
