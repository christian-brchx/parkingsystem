package com.parkit.parkingsystem;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.withinPercentage;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Date;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;

public class FareCalculatorServiceTest {

	private static FareCalculatorService fareCalculatorService;
	private Ticket ticket;

	@BeforeAll
	private static void setUp() {
		fareCalculatorService = new FareCalculatorService();
	}

	@BeforeEach
	private void setUpPerTest() {
		ticket = new Ticket();
	}

	@Test
	public void calculateFareCarWithOneHourParkingTime() {
		Date inTime = new Date();
		Date outTime = (Date) inTime.clone();
		// set the input time 60' before
		inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		ticket.setCountPreviousTickets(0);
		fareCalculatorService.calculateFare(ticket);
		// 60 minutes parking time should give parking fare
		assertThat(ticket.getPrice()).isCloseTo(Fare.CAR_RATE_PER_HOUR, withinPercentage(0.001));
	}

	@Test
	public void calculateFareBikeWithOneHourParkingTime() {
		Date inTime = new Date();
		Date outTime = (Date) inTime.clone();
		// set the input time 60' before
		inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		ticket.setCountPreviousTickets(0);
		fareCalculatorService.calculateFare(ticket);
		// 60 minutes parking time should give parking fare
		assertThat(ticket.getPrice()).isCloseTo(Fare.BIKE_RATE_PER_HOUR, withinPercentage(0.001));
	}

	@Test
	public void calculateFareUnkownType() {
		Date inTime = new Date();
		Date outTime = (Date) inTime.clone();
		inTime.setTime(System.currentTimeMillis() - (60 * 60 * 1000));
		ParkingSpot parkingSpot = new ParkingSpot(1, null, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		ticket.setCountPreviousTickets(0);
		assertThrows(NullPointerException.class, () -> fareCalculatorService.calculateFare(ticket));
	}

	@Test
	public void calculateFareBikeWithFutureInTime() {
		Date inTime = new Date();
		Date outTime = (Date) inTime.clone();
		// set the input time 60' before
		inTime.setTime(System.currentTimeMillis() + (60 * 60 * 1000));
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		ticket.setCountPreviousTickets(0);
		assertThrows(IllegalArgumentException.class, () -> fareCalculatorService.calculateFare(ticket));
	}

	@Test
	public void calculateFareBikeWithLessThanOneHourParkingTime() {
		Date inTime = new Date();
		Date outTime = (Date) inTime.clone();
		// set the input time 45' before
		inTime.setTime(System.currentTimeMillis() - (45 * 60 * 1000));
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.BIKE, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		ticket.setCountPreviousTickets(0);
		fareCalculatorService.calculateFare(ticket);
		// 45 minutes parking time should give 3/4th parking fare bike
		assertThat(ticket.getPrice()).isCloseTo(0.75 * Fare.BIKE_RATE_PER_HOUR, withinPercentage(0.001));
	}

	@Test
	public void calculateFareCarWithLessThanOneHourParkingTime() {
		Date inTime = new Date();
		Date outTime = (Date) inTime.clone();
		// set the input time 45' before
		inTime.setTime(System.currentTimeMillis() - (45 * 60 * 1000));
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		ticket.setCountPreviousTickets(0);
		fareCalculatorService.calculateFare(ticket);
		// 45 minutes parking time should give 3/4th parking fare car
		assertThat(ticket.getPrice()).isCloseTo(0.75 * Fare.CAR_RATE_PER_HOUR, withinPercentage(0.001));
	}

	@Test
	public void calculateFareBikeLessThanThirtyMinutesParkingTimeRecurrentUser() {
		Date inTime = new Date();
		Date outTime = (Date) inTime.clone();
		// set the input time 15' before
		inTime.setTime(System.currentTimeMillis() - (15 * 60 * 1000));
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		// recurrent user if previous tickets found
		ticket.setCountPreviousTickets(1);
		fareCalculatorService.calculateFare(ticket);
		// 15 minutes parking time should give 0 parking fare
		assertEquals(0, ticket.getPrice());
	}

	@Test
	public void calculateFareCarWithMoreThanOneHourParkingTimeRecurrentUSer() {
		Date inTime = new Date();
		Date outTime = (Date) inTime.clone();
		inTime.setTime(System.currentTimeMillis() - (120 * 60 * 1000));
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		// recurrent user if previous tickets found
		ticket.setCountPreviousTickets(1);
		fareCalculatorService.calculateFare(ticket);
		// 120 minutes parking time for recurrent user should give 2 parking fare * 0.95
		assertThat(ticket.getPrice()).isCloseTo(2 * 0.95 * Fare.CAR_RATE_PER_HOUR, withinPercentage(0.001));
	}

	@Test
	public void calculateFareCarWithLessThanThirtyMinutesParkingTime() {
		Date inTime = new Date();
		Date outTime = (Date) inTime.clone();
		inTime.setTime(System.currentTimeMillis() - (15 * 60 * 1000));
		// 15 minutes parking time should give 0 parking fare
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		ticket.setCountPreviousTickets(0);
		fareCalculatorService.calculateFare(ticket);
		// free for under 30 minutes
		assertEquals(0, ticket.getPrice());
	}

	@Test
	public void calculateFareCarWithThirtyMinutesParkingTime() {
		Date inTime = new Date();
		Date outTime = (Date) inTime.clone();
		inTime.setTime(System.currentTimeMillis() - (30 * 60 * 1000));
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		ticket.setCountPreviousTickets(0);
		fareCalculatorService.calculateFare(ticket);
		// 30 minutes parking time should give 1/2th parking fare
		assertThat(ticket.getPrice()).isCloseTo(0.5 * Fare.CAR_RATE_PER_HOUR, withinPercentage(0.001));
	}

	@Test
	public void calculateFareCarWithMoreThanADayParkingTime() {
		Date inTime = new Date();
		Date outTime = (Date) inTime.clone();
		inTime.setTime(System.currentTimeMillis() - (25 * 60 * 60 * 1000));
		ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);

		ticket.setInTime(inTime);
		ticket.setOutTime(outTime);
		ticket.setParkingSpot(parkingSpot);
		ticket.setCountPreviousTickets(0);
		fareCalculatorService.calculateFare(ticket);
		// 25 hours parking time should give 25 * parking fare per hour
		assertThat(ticket.getPrice()).isCloseTo(25 * Fare.CAR_RATE_PER_HOUR, withinPercentage(0.001));
	}

}
