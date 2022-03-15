package com.parkit.parkingsystem;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;

public class TicketTest {

	private Ticket ticket;

	@BeforeEach
	private void SetUpPerTest() {
		ticket = new Ticket();
	}

	@Test
	public void getParkingSpotMustReturnTheParkingSpotCreatedWithSetParkingSpot() {
		// GIVEN

		// WHEN
		ticket.setParkingSpot(new ParkingSpot(1, ParkingType.BIKE, false));

		// THEN
		ParkingSpot parkingSpotCheck = ticket.getParkingSpot();
		assertThat(parkingSpotCheck.getId()).isEqualTo(1);
		assertThat(parkingSpotCheck.getParkingType()).isEqualTo(ParkingType.BIKE);
		assertThat(parkingSpotCheck.isAvailable()).isEqualTo(false);
	}

	@Test
	public void getParkingSpotMustReturnTheParkingSpotUpdatedWithSetParkingSpot() {
		// GIVEN
		ticket.setParkingSpot(new ParkingSpot(1, ParkingType.BIKE, false));
		ParkingSpot parkingSpot = ticket.getParkingSpot();
		parkingSpot.setParkingType(ParkingType.CAR);
		parkingSpot.setId(2);
		parkingSpot.setAvailable(true);

		// WHEN
		ticket.setParkingSpot(parkingSpot);

		// THEN
		ParkingSpot parkingSpotCheck = ticket.getParkingSpot();
		assertThat(parkingSpotCheck.getId()).isEqualTo(2);
		assertThat(parkingSpotCheck.getParkingType()).isEqualTo(ParkingType.CAR);
		assertThat(parkingSpotCheck.isAvailable()).isEqualTo(true);
	}

	@Test
	public void getInTimeMustReturnTheInTimeSetWithSetInTime() {
		// GIVEN
		Date inTime = new Date();

		// WHEN
		ticket.setInTime(inTime);

		// THEN
		Date inTimeCheck = ticket.getInTime();
		assertThat(inTimeCheck.getTime()).isEqualTo(inTime.getTime());
	}

	@Test
	public void getOutTimeMustReturnTheOutTimeSetWithSetOutTime() {
		// GIVEN
		Date outTime = new Date();

		// WHEN
		ticket.setOutTime(outTime);

		// THEN
		Date outTimeCheck = ticket.getOutTime();
		assertThat(outTimeCheck.getTime()).isEqualTo(outTime.getTime());
	}

}
