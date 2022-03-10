package com.parkit.parkingsystem.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;

public class TicketDAO {

	private static final Logger logger = LogManager.getLogger("TicketDAO");

	public DataBaseConfig dataBaseConfig = new DataBaseConfig();

	public boolean saveTicket(Ticket ticket) {
		Connection con = null;
		PreparedStatement ps = null;
		boolean ret = false;
		try {
			con = dataBaseConfig.getConnection();
			ps = con.prepareStatement(DBConstants.SAVE_TICKET);
			// ID, PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME)
			// ps.setInt(1,ticket.getId());
			ps.setInt(1, ticket.getParkingSpot().getId());
			ps.setString(2, ticket.getVehicleRegNumber());
			ps.setDouble(3, ticket.getPrice());
			ps.setTimestamp(4, new Timestamp(ticket.getInTime().getTime()));
			ps.setTimestamp(5, (ticket.getOutTime() == null) ? null : (new Timestamp(ticket.getOutTime().getTime())));
			ps.setInt(6, ticket.getCountPreviousTickets());
			ret = ps.execute();
		} catch (SQLException ex) {
			logger.error("Error fetching next available slot", ex);
		} catch (ClassNotFoundException ex) {
			logger.error("Error getConnection Exception = ", ex);
		} finally {
			if (ps != null)
				dataBaseConfig.closePreparedStatement(ps);
			if (con != null)
				dataBaseConfig.closeConnection(con);
		}
		return ret;
	}

	public Ticket getTicket(String vehicleRegNumber) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Ticket ticket = null;
		try {
			con = dataBaseConfig.getConnection();
			ps = con.prepareStatement(DBConstants.GET_TICKET);
			// ID, PARKING_NUMBER, VEHICLE_REG_NUMBER, PRICE, IN_TIME, OUT_TIME,
			// COUNT_PREVIOUS_TICKETS)
			ps.setString(1, vehicleRegNumber);
			rs = ps.executeQuery();
			if (rs.next()) {
				ticket = new Ticket();
				ParkingSpot parkingSpot = new ParkingSpot(rs.getInt(1), ParkingType.valueOf(rs.getString(7)), false);
				ticket.setParkingSpot(parkingSpot);
				ticket.setId(rs.getInt(2));
				ticket.setVehicleRegNumber(vehicleRegNumber);
				ticket.setPrice(rs.getDouble(3));
				ticket.setInTime(rs.getTimestamp(4));
				ticket.setOutTime(rs.getTimestamp(5));
				ticket.setCountPreviousTickets(rs.getInt(6));
			}
		} catch (SQLException ex) {
			logger.error("Error fetching next available slot", ex);
		} catch (ClassNotFoundException ex) {
			logger.error("Error getConnection Exception = ", ex);
		} finally {
			if (rs != null)
				dataBaseConfig.closeResultSet(rs);
			if (ps != null)
				dataBaseConfig.closePreparedStatement(ps);
			if (con != null)
				dataBaseConfig.closeConnection(con);
		}
		return ticket;
	}

	public boolean updateTicket(Ticket ticket) {
		Connection con = null;
		PreparedStatement ps = null;
		boolean ret = false;
		try {
			con = dataBaseConfig.getConnection();
			ps = con.prepareStatement(DBConstants.UPDATE_TICKET);
			ps.setDouble(1, ticket.getPrice());
			ps.setTimestamp(2, new Timestamp(ticket.getOutTime().getTime()));
			ps.setInt(3, ticket.getId());
			ps.execute();
			ret = true;
		} catch (SQLException ex) {
			logger.error("Error saving ticket info", ex);
		} catch (ClassNotFoundException ex) {
			logger.error("Error getConnection Exception = ", ex);
		} finally {
			if (ps != null)
				dataBaseConfig.closePreparedStatement(ps);
			if (con != null)
				dataBaseConfig.closeConnection(con);
		}
		return ret;
	}

	// STORY#2 RECURRENT USER
	public int countPreviousTicketsOfVehicleRegNumber(String vehicleRegNumber) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int count = 0;
		try {
			con = dataBaseConfig.getConnection();
			ps = con.prepareStatement(DBConstants.COUNT_TICKET_FOR_USER);
			ps.setString(1, vehicleRegNumber);
			rs = ps.executeQuery();
			if (rs.next()) {
				count = rs.getInt(1);
			}
			rs.close();
		} catch (SQLException ex) {
			logger.error("Error counting ticket of vehicleRegNumber : ", vehicleRegNumber, " Exception = ", ex);
		} catch (ClassNotFoundException ex) {
			logger.error("Error getConnection Exception = ", ex);
		} finally {
			if (rs != null)
				dataBaseConfig.closeResultSet(rs);
			if (ps != null)
				dataBaseConfig.closePreparedStatement(ps);
			if (con != null)
				dataBaseConfig.closeConnection(con);
		}
		return count;
	}
}
