package com.parkit.parkingsystem.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class InputReaderUtil {

	private ScannerUtil scanUtil;
	private static final Logger logger = LogManager.getLogger("InputReaderUtil");

	public InputReaderUtil(ScannerUtil scanUtil) {
		this.scanUtil = scanUtil;
	}

	public int readSelection() {
		try {
			int input = Integer.parseInt(scanUtil.nextLine());
			return input;
		} catch (Exception e) {
			logger.error("Error while reading user input from Shell");
			System.out.println("Error reading input. Please enter valid number for proceeding further");
			return -1;
		}
	}

	public String readVehicleRegistrationNumber() throws Exception {
		try {
			String vehicleRegNumber = scanUtil.nextLine();
			if (vehicleRegNumber == null || vehicleRegNumber.trim().length() == 0) {
				throw new IllegalArgumentException("Invalid input provided");
			}
			return vehicleRegNumber;
		} catch (Exception e) {
			logger.error("Error while reading user input from Shell");
			System.out.println("Error reading input. Please enter a valid string for vehicle registration number");
			throw e;
		}
	}

}
