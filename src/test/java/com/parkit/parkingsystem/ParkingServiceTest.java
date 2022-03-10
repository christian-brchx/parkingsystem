package com.parkit.parkingsystem;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.withinPercentage;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;

@ExtendWith(MockitoExtension.class)
public class ParkingServiceTest {

	private static ParkingService parkingService;

	@Mock
	private static InputReaderUtil inputReaderUtil;
	@Mock
	private static ParkingSpotDAO parkingSpotDAO;
	@Mock
	private static TicketDAO ticketDAO;

	@BeforeEach
	private void setUpPerTest() {
		parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
	}

	@Test
	public void processIncomingVehicleMustCallCountPreviousTicketAndSaveTheCounter() {
		try {
			// GIVEN
			final ArgumentCaptor<Ticket> ticketCaptor = ArgumentCaptor.forClass(Ticket.class);
			when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
			// Select a type of car (1)
			when(inputReaderUtil.readSelection()).thenReturn(1);
			when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(1);
			// we assume here that there are 5 previous tickets for this vehicle in the
			// ticket table
			when(ticketDAO.countPreviousTicketsOfVehicleRegNumber(anyString())).thenReturn(5);

			// WHEN
			parkingService.processIncomingVehicle();

			// THEN
			// saveTicket method must be called once.
			verify(ticketDAO, Mockito.times(1)).saveTicket(ticketCaptor.capture());
			final List<Ticket> tickets = ticketCaptor.getAllValues();
			// only one call, so we get the first element (the ticket) of the list
			// we check if the saved ticket contains the right countpreviousticket (5 here)
			assertThat(tickets.get(0).getCountPreviousTickets()).isEqualTo(5);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("processIncomingVehicleWithRecuringUSer : Failed to set up test mock objects");
		}
	}

	@Test
	public void processExitingVehicleMustSetTheParkingSpotAvailable() {
		try {
			// GIVEN
			when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
			ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
			Ticket ticket = new Ticket();
			ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
			ticket.setParkingSpot(parkingSpot);
			ticket.setVehicleRegNumber("ABCDEF");
			ticket.setCountPreviousTickets(0);
			when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
			when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);
			when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);

			// WHEN
			parkingService.processExitingVehicle();

			// THEN
			// check the parkingspot if available and update DB is done
			assertThat(ticket.getParkingSpot().isAvailable()).isEqualTo(true);
			verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("processExitingVehicleTest : Failed to set up test mock objects");
		}

	}

	@Test
	public void processExitingVehicleMustCalculatetheRightPriceForCurrentUSer() {
		// GIVEN
		try {
			final ArgumentCaptor<Ticket> ticketCaptor = ArgumentCaptor.forClass(Ticket.class);
			when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
			ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
			Ticket ticket = new Ticket();
			// one hour of park
			ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
			ticket.setParkingSpot(parkingSpot);
			ticket.setVehicleRegNumber("ABCDEF");
			ticket.setCountPreviousTickets(5);
			when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
			when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);

			when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);

			// WHEN
			parkingService.processExitingVehicle();

			// THEN
			// updateTicket method must be called once.
			verify(ticketDAO, Mockito.times(1)).updateTicket(ticketCaptor.capture());
			final List<Ticket> tickets = ticketCaptor.getAllValues();
			// only one call, so we get the first element (the ticket) of the list
			// we check if the saved ticket contains the right price (1 h * 1,5 * 95%)
			assertThat(tickets.get(0).getPrice()).isCloseTo(1.425, withinPercentage(0.01));

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("processExitingVehicleTest : Failed to set up test mock objects");
		}

	}

}
