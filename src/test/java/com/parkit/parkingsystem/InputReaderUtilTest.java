package com.parkit.parkingsystem;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.parkit.parkingsystem.util.InputReaderUtil;
import com.parkit.parkingsystem.util.ScannerUtil;

@ExtendWith(MockitoExtension.class)
public class InputReaderUtilTest {
	@Mock
	private static ScannerUtil scanUtil;

	private static InputReaderUtil inputReaderUtil;

	@BeforeEach
	private void setUpPerTest() {
		inputReaderUtil = new InputReaderUtil(scanUtil);
	}

	@Test
	public void readSelectionMustReturntheNumberEntered() {
		// GIVEN
		when(scanUtil.nextLine()).thenReturn("2");
		// WHEN
		int numberReturned = inputReaderUtil.readSelection();

		// THEN
		assertThat(numberReturned).isEqualTo(2);

	}

	@Test
	public void readSelectionMustCatchInvalidEntry() {
		// GIVEN
		when(scanUtil.nextLine()).thenReturn("z");
		// WHEN
		int numberReturned = inputReaderUtil.readSelection();

		// THEN
		assertThat(numberReturned).isEqualTo(-1);
	}

	@Test
	public void readVehicleRegistrationNumberMustReturntheRegistrationNumberEntered() {
		// GIVEN
		when(scanUtil.nextLine()).thenReturn("ABCDE");

		// WHEN
		try {
			String numberReturned = inputReaderUtil.readVehicleRegistrationNumber();

			// THEN
			assertThat(numberReturned).isEqualTo("ABCDE");
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Test readVehicleRegistration : Failed to call");
		}
	}

	@Test
	// @Disabled
	public void readVehicleRegistrationNumberMustCatchInvalidEntry() {
		// GIVEN
		when(scanUtil.nextLine()).thenReturn("");

		// WHEN
		assertThrows(IllegalArgumentException.class, () -> inputReaderUtil.readVehicleRegistrationNumber());

		// THEN
	}
}
