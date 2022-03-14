package com.parkit.parkingsystem;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;

@ExtendWith(MockitoExtension.class)
public class TicketDAOTest {
	@Mock
	static DataBaseConfig dataBaseConfig;
	@Mock
	static Connection con;
	@Mock
	static PreparedStatement ps;
	@Mock
	static ResultSet rs;

	TicketDAO ticketDAO;

	@BeforeEach
	private void setUpPerTest() {
		ticketDAO = new TicketDAO();
	}

	@Test
	public void saveTicketMustExecuteSqlQueryWithRightArguments() {
		// GIVEN
		Date inTime = new Date();
		Ticket ticket = new Ticket();
		ticket.setInTime(inTime);
		ParkingSpot parkingSpot = new ParkingSpot(5, ParkingType.CAR, false);
		ticket.setParkingSpot(parkingSpot);
		ticket.setVehicleRegNumber("TEST01");
		try {
			when(ps.execute()).thenReturn(true);
			when(con.prepareStatement(anyString())).thenReturn(ps);
			when(dataBaseConfig.getConnection()).thenReturn(con);
			ticketDAO.dataBaseConfig = dataBaseConfig;
			// WHEN
			ticketDAO.saveTicket(ticket);

			// THEN
			// Search for "TEST01" ticket with the right inTime and the right parking number
			// (5)
			verify(ps, Mockito.times(1)).setInt(1, 5);
			verify(ps, Mockito.times(1)).setString(2, "TEST01");
			verify(ps, Mockito.times(1)).setTimestamp(4, new Timestamp(inTime.getTime()));

		} catch (

		Exception e) {
			e.printStackTrace();
		}

	}

	@Test
	public void getTicketMustExecuteSqlQueryWithRightArguments() {
		// GIVEN
		try {
			when(rs.next()).thenReturn(true);
			when(rs.getInt(1)).thenReturn(1);
			when(rs.getString(7)).thenReturn("CAR");
			when(ps.executeQuery()).thenReturn(rs);
			when(con.prepareStatement(anyString())).thenReturn(ps);
			when(dataBaseConfig.getConnection()).thenReturn(con);
			ticketDAO.dataBaseConfig = dataBaseConfig;

			// WHEN
			Ticket ticket = ticketDAO.getTicket("TEST01");

			// THEN
			// Search for TEST01 must return "TEST01"
			verify(ps, Mockito.times(1)).setString(1, "TEST01");
			assertThat(ticket.getVehicleRegNumber()).isEqualTo("TEST01");

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Test
	public void countPreviousTicketsOfVehicleRegNumberMustCatchSQLExceptionAndReturnZero() {
		// GIVEN
		try {
			when(con.prepareStatement(anyString())).thenThrow(new SQLException());
			when(dataBaseConfig.getConnection()).thenReturn(con);
			ticketDAO.dataBaseConfig = dataBaseConfig;

			// WHEN
			int countPreviousTicket = ticketDAO.countPreviousTicketsOfVehicleRegNumber("TEST01");

			// THEN
			// in case of Exception, must return 0 previousTicket
			assertThat(countPreviousTicket).isEqualTo(0);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
