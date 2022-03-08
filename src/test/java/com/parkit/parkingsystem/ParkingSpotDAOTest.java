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
	public void getNextAvailableSlotMustReturnTheNumberOfTheCarSPot() {
		try {
			// GIVEN
			// Le premier emplacement libre porte le numero 4
			when(rs.next()).thenReturn(true);
			when(rs.getInt(1)).thenReturn(4);
			when(ps.executeQuery()).thenReturn(rs);
			when(con.prepareStatement(anyString())).thenReturn(ps);
			when(dataBaseConfig.getConnection()).thenReturn(con);
			parkingSpotDAO.dataBaseConfig = dataBaseConfig;

			// WHEN
			int result = parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);

			// THEN
			// La recherche doit cibler un emplacement pour une voiture ("CAR")
			verify(ps, Mockito.times(1)).setString(1, "CAR");
			verify(ps, Mockito.times(1)).executeQuery();
			// Le numéro d'emplacement doit correspondre
			assertThat(result).isEqualTo(4);

		} catch (

		Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void getNextAvailableSlotMustReturnMoinsUnIfNoFreeCarSpot() {
		try {
			// GIVEN
			// Il n'y a pas d'emplacement libre
			when(rs.next()).thenReturn(false);
			when(ps.executeQuery()).thenReturn(rs);
			when(con.prepareStatement(anyString())).thenReturn(ps);
			when(dataBaseConfig.getConnection()).thenReturn(con);
			parkingSpotDAO.dataBaseConfig = dataBaseConfig;

			// WHEN
			int result = parkingSpotDAO.getNextAvailableSlot(ParkingType.CAR);

			// THEN
			// La recherche doit cibler un emplacement pour une voiture ("CAR")
			verify(ps, Mockito.times(1)).setString(1, "CAR");
			verify(ps, Mockito.times(1)).executeQuery();
			// Le numéro d'emplacement doit être à -1 car pas d'emplacement libre
			assertThat(result).isEqualTo(-1);

		} catch (

		Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void updateParkingMustExecuteTheRequestWithRightsArguments() {
		try {
			// GIVEN
			// L'emplacement à mettre à jour est le numéro 4 et n'est pas disponible
			when(parkingSpot.getId()).thenReturn(4);
			when(parkingSpot.isAvailable()).thenReturn(false);
			// on indique qu'une ligne a été mise à jour
			when(ps.executeUpdate()).thenReturn(1);
			when(con.prepareStatement(anyString())).thenReturn(ps);
			when(dataBaseConfig.getConnection()).thenReturn(con);
			parkingSpotDAO.dataBaseConfig = dataBaseConfig;

			// WHEN
			boolean result = parkingSpotDAO.updateParking(parkingSpot);

			// THEN
			// La mise à jour doit cibler l'emplacement numéro 4 qui n'est pas disponible
			verify(ps, Mockito.times(1)).setBoolean(1, false);
			verify(ps, Mockito.times(1)).setInt(2, 4);
			// Le traitement doit bien se dérouler
			assertThat(result).isEqualTo(true);

		} catch (

		Exception e) {
			e.printStackTrace();
		}
	}

}
