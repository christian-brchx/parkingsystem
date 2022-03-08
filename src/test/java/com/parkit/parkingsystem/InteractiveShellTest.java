package com.parkit.parkingsystem;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.service.InteractiveShell;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;

@ExtendWith(MockitoExtension.class)
public class InteractiveShellTest {

	@Mock
	private static InputReaderUtil inputReaderUtil;
	@Mock
	private static ParkingService parkingService;
	@Mock
	private static ParkingSpotDAO parkingSpotDAO;
	@Mock
	private static TicketDAO ticketDAO;

	@Test
	public void mustCallprocessIncomingVehicleWhenUserChooseNewVehicleEntering() {
		try {
			// GIVEN
			when(inputReaderUtil.readSelection()).thenReturn(1, 3);

			// WHEN
			InteractiveShell.loadInterface(inputReaderUtil, parkingService);

			// THEN
			verify(inputReaderUtil, Mockito.times(2)).readSelection();
			verify(parkingService, Mockito.times(1)).processIncomingVehicle();
			verify(parkingService, Mockito.times(0)).processExitingVehicle();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("InteractiveShellTest : Failed to set up test mock objects");
		}
	}

	@Test
	public void mustCallprocessIncomingVehicleWhenUserChooseExitingVehicle() {
		try {
			// GIVEN
			when(inputReaderUtil.readSelection()).thenReturn(2, 3);

			// WHEN
			InteractiveShell.loadInterface(inputReaderUtil, parkingService);

			// THEN
			verify(inputReaderUtil, Mockito.times(2)).readSelection();
			verify(parkingService, Mockito.times(1)).processExitingVehicle();
			verify(parkingService, Mockito.times(0)).processIncomingVehicle();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("InteractiveShellTest : Failed to set up test mock objects");
		}

	}

	@Test
	public void mustCallNoprocessWhenUserChooseExitApplication() {
		try {
			// GIVEN
			when(inputReaderUtil.readSelection()).thenReturn(3);

			// WHEN
			InteractiveShell.loadInterface(inputReaderUtil, parkingService);

			// THEN
			verify(inputReaderUtil, Mockito.times(1)).readSelection();
			verify(parkingService, Mockito.times(0)).processIncomingVehicle();
			verify(parkingService, Mockito.times(0)).processExitingVehicle();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("InteractiveShellTest : Failed to set up test mock objects");
		}

	}

}
