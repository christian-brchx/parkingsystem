package com.parkit.parkingsystem.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.ResultSet;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.FareCalculatorService;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;

@ExtendWith(MockitoExtension.class)
public class ParkingDataBaseIT {

	private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
	private static ParkingSpotDAO parkingSpotDAO;
	private static TicketDAO ticketDAO;
	private static DataBasePrepareService dataBasePrepareService;

	@Mock
	private static InputReaderUtil inputReaderUtil;

	@BeforeAll
	private static void setUp() throws Exception {
		parkingSpotDAO = new ParkingSpotDAO();
		parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
		ticketDAO = new TicketDAO();
		ticketDAO.dataBaseConfig = dataBaseTestConfig;
		dataBasePrepareService = new DataBasePrepareService();
	}

	@BeforeEach
	private void setUpPerTest() throws Exception {
		when(inputReaderUtil.readSelection()).thenReturn(1);
		when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
		dataBasePrepareService.clearDataBaseEntries();
	}

	@AfterAll
	private static void tearDown() {

	}

	@Test
	public void testParkingACar() {
		ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
		// WHEN
		parkingService.processIncomingVehicle();
		// TODO: check that a ticket is actualy saved in DB and Parking table is updated
		// with availability

		// THEN
		// try to get the ticket for the VehicleRegNumber
		Ticket ticket = ticketDAO.getTicket("ABCDEF");

		// A ticket must be found for the VehicleRegNumber
		assertThat(ticket).isNotNull();
		assertThat(ticket.getVehicleRegNumber()).isEqualTo("ABCDEF");

		// Get the status of the parking selected and updated in the DB
		boolean available = getAvailableStatusParkingFromDB(ticket);

		// parking must be unaivalable
		assertThat(available).isEqualTo(false);
	}

	private boolean getAvailableStatusParkingFromDB(Ticket ticket) {
		boolean available = true;

		Connection connection = null;
		try {
			connection = dataBaseTestConfig.getConnection();

			// read parking status in the DB
			ResultSet rs = connection
					.prepareStatement(
							"select AVAILABLE from parking where PARKING_NUMBER=" + ticket.getParkingSpot().getId())
					.executeQuery();

			if (rs.next()) {
				available = rs.getBoolean(1);
			}
			dataBaseTestConfig.closeResultSet(rs);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dataBaseTestConfig.closeConnection(connection);
		}

		return available;
	}

	@Test
	public void testParkingLotExit() {
		// GIVEN
		// create a ticket
		testParkingACar();
		ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);

		// WHEN
		parkingService.processExitingVehicle();

		// TODO: check that the fare generated and out time are populated correctly in
		// the database

		// THEN
		// try to get the ticket for the VehicleRegNumber
		Ticket ticket = ticketDAO.getTicket("ABCDEF");

		// A ticket must be found for the VehicleRegNumber
		assertThat(ticket).isNotNull();
		// the outTime must be filled by processExitingVehicle (not null)
		assertThat(ticket.getOutTime()).isNotNull();

		// Save the fare calculated by processExistingVehicle and stored in the DB
		Double saveFare = ticket.getPrice();
		// Recalculate the fare with the same inputs
		FareCalculatorService fareCalculatorService = new FareCalculatorService();
		fareCalculatorService.calculateFare(ticket);

		// The result of fare read from the DB must be equal to fare recalculated
		assertThat(saveFare).isEqualTo(ticket.getPrice());
	}

}
