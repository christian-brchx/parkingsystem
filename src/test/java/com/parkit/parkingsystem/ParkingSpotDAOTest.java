package com.parkit.parkingsystem;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.model.ParkingSpot;

@ExtendWith(MockitoExtension.class)
public class ParkingSpotDAOTest {

	@Mock
	static DataBaseConfig dataBaseConfig;
	@Mock
	static Connection con;
	@Mock
	static PreparedStatement ps;
	@Mock
	static ResultSet rs;
	@Mock
	static ParkingSpot parkingSpot;

	ParkingSpotDAO parkingSpotDAO;

	@BeforeEach
	private void setUpPerTest() {
		parkingSpotDAO = new ParkingSpotDAO();
	}

	@Test
	public void getNextAvailableSlotMustReturnTheNumberOfTheAvailableCarSPot() {
		try {
			// GIVEN
			when(rs.next()).thenReturn(true);
			// first available parkingspot is number 4
			when(rs.getInt(1)).thenReturn(4);
			when(ps.executeQuery()).thenReturn(rs);
			when(con.prepareStatement(anyString())).thenReturn(ps);
			when(dataBaseConfig.getConnection()).thenReturn(con);
			parkingSpotDAO.dataBaseConfig = dataBaseConfig;

			// WHEN
			int result = parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);

			// THEN
			// check if search for car'spot
			verify(ps, Mockito.times(1)).setString(1, "CAR");
			// check the parkingspot number returned (4 here)
			assertThat(result).isEqualTo(4);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void updateParkingMustExecuteTheRequestWithRightsArguments() {
		try {
			// GIVEN
			// parkingspot to update is number 4
			when(parkingSpot.getId()).thenReturn(4);
			when(parkingSpot.isAvailable()).thenReturn(false);
			when(ps.executeUpdate()).thenReturn(1);
			when(con.prepareStatement(anyString())).thenReturn(ps);
			when(dataBaseConfig.getConnection()).thenReturn(con);
			parkingSpotDAO.dataBaseConfig = dataBaseConfig;

			// WHEN
			boolean result = parkingSpotDAO.updateParking(parkingSpot);

			// THEN
			// the updated parkingspot must be number 4
			verify(ps, Mockito.times(1)).setInt(2, 4);
			// the parkingspot must be set to available
			verify(ps, Mockito.times(1)).setBoolean(1, false);
			assertThat(result).isEqualTo(true);

		} catch (

		Exception e) {
			e.printStackTrace();
		}
	}

}
