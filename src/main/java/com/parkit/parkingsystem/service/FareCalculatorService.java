package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

	private static double calculateFareRules(double duration, double rate, Ticket ticket) {
		double price = 0;
		// fee only if duration >= 30 minutes (1/2 hour),
		if (duration >= 0.5) {
			price = duration * calculateRateRules(ticket, rate);
		}
		return price;
	}

	private static double calculateRateRules(Ticket ticket, double rate) {
		// 5% discount for RecurrentUser
		if (ticket.ofRecurrentUser()) {
			return rate * 0.95;
		} else {
			return rate;
		}
	}

	public void calculateFare(Ticket ticket) {
		if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))) {
			throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
		}

		// int inHour = ticket.getInTime().getHours();
		// int outHour = ticket.getOutTime().getHours();
		long inHour = ticket.getInTime().getTime();
		long outHour = ticket.getOutTime().getTime();
		// TODO: Some tests are failing here. Need to check if this logic is correct
		// calcul de la durée en ms et conversion en h
		// utilisation d'un double pour conserver les décimales
		double duration = (double) (outHour - inHour) / 1000 / 60 / 60;

		switch (ticket.getParkingSpot().getParkingType()) {
		case CAR: {
			ticket.setPrice(calculateFareRules(duration, Fare.CAR_RATE_PER_HOUR, ticket));
			break;
		}
		case BIKE: {
			ticket.setPrice(calculateFareRules(duration, Fare.BIKE_RATE_PER_HOUR, ticket));
			break;
		}
		default:
			throw new IllegalArgumentException("Unkown Parking Type");
		}
	}
}