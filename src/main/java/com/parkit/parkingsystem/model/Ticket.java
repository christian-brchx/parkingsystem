package com.parkit.parkingsystem.model;

import java.util.Date;

public class Ticket {
	private int id = 0;
	private ParkingSpot parkingSpot = null;
	private String vehicleRegNumber = "";
	private double price = 0;
	private Date inTime = null;
	private Date outTime = null;
	private int countPreviousTickets;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ParkingSpot getParkingSpot() {
		if (this.parkingSpot != null) {
			ParkingSpot parkingSpot = new ParkingSpot(this.parkingSpot.getId(),
					this.parkingSpot.getParkingType(),
					this.parkingSpot.isAvailable());
			return parkingSpot;
		} else
			return null;
	}

	public void setParkingSpot(ParkingSpot parkingSpot) {
		if (this.parkingSpot == null) {
			this.parkingSpot = new ParkingSpot(parkingSpot.getId(),
					parkingSpot.getParkingType(),
					parkingSpot.isAvailable());
		} else {
			this.parkingSpot.setId(parkingSpot.getId());
			this.parkingSpot.setParkingType(parkingSpot.getParkingType());
			this.parkingSpot.setAvailable(parkingSpot.isAvailable());
		}
	}

	public String getVehicleRegNumber() {
		return vehicleRegNumber;
	}

	public void setVehicleRegNumber(String vehicleRegNumber) {
		this.vehicleRegNumber = vehicleRegNumber;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public Date getInTime() {
		if (this.inTime != null) {
			Date inTime = (Date) this.inTime.clone();
			return inTime;
		} else
			return null;
	}

	public void setInTime(Date inTime) {
		if (inTime != null) {
			if (this.inTime == null) {
				this.inTime = (Date) inTime.clone();
			} else {
				this.inTime.setTime(inTime.getTime());
			}
		} else {
			this.inTime = null;
		}
	}

	public Date getOutTime() {
		if (this.outTime != null) {
			Date outTime = (Date) this.outTime.clone();
			return outTime;
		} else
			return null;
	}

	public void setOutTime(Date outTime) {
		if (outTime == null) {
			this.outTime = null;
		} else {
			if (this.outTime != null) {
				this.outTime.setTime(outTime.getTime());
			} else {
				this.outTime = (Date) outTime.clone();
			}
		}
	}

	// STORY#2 RECURRENT USER
	public int getCountPreviousTickets() {
		return countPreviousTickets;
	}

	public void setCountPreviousTickets(int countPreviousTickets) {
		this.countPreviousTickets = countPreviousTickets;
	}

	public boolean ofRecurrentUser() {
		if (getCountPreviousTickets() > 0) {
			return true;
		} else
			return false;
	}
}
