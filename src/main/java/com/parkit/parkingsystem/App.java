package com.parkit.parkingsystem;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.service.InteractiveShell;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import com.parkit.parkingsystem.util.ScannerUtil;

public class App {
	private static final Logger logger = LogManager.getLogger("App");

	final static InputReaderUtil inputReaderUtil = new InputReaderUtil(new ScannerUtil());
	final static ParkingSpotDAO parkingSpotDAO = new ParkingSpotDAO();
	final static TicketDAO ticketDAO = new TicketDAO();
	final static ParkingService parkingService = new ParkingService(inputReaderUtil,
			parkingSpotDAO, ticketDAO);

	public static void main(String args[]) {
		logger.info("Initializing Parking System");
		InteractiveShell.loadInterface(inputReaderUtil, parkingService);
	}
}
