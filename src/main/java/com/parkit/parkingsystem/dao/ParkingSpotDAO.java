package com.parkit.parkingsystem.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;

public class ParkingSpotDAO {
	private static final Logger logger = LogManager.getLogger("ParkingSpotDAO");

	public DataBaseConfig dataBaseConfig = new DataBaseConfig();

	public int getNextAvailableSlot(ParkingType parkingType) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		int result = -1;
		boolean availableSlot = true;
		try {
			con = dataBaseConfig.getConnection();
			ps = con.prepareStatement(DBConstants.GET_NEXT_PARKING_SPOT);
			ps.setString(1, parkingType.toString());
			rs = ps.executeQuery();
			if (rs.next()) {
				result = rs.getInt(1);
			} else {
				availableSlot = false;
			}
		} catch (SQLException ex) {
			if (availableSlot) {
				logger.error("Error fetching next available slot", ex);
			}
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
		return result;
	}

	public boolean updateParking(ParkingSpot parkingSpot) {
		// update the availability for that parking slot
		Connection con = null;
		PreparedStatement ps = null;
		boolean ret = false;
		try {
			con = dataBaseConfig.getConnection();
			ps = con.prepareStatement(DBConstants.UPDATE_PARKING_SPOT);
			ps.setBoolean(1, parkingSpot.isAvailable());
			ps.setInt(2, parkingSpot.getId());
			int updateRowCount = ps.executeUpdate();
			ret = (updateRowCount == 1);
		} catch (SQLException ex) {
			logger.error("Error updating parking info", ex);
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

}
