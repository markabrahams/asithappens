/*
 * IPAccountingCheckpointCollectorDAO.java
 *
 * Created on 31 August 2012, 01:12
 *
 * AsItHappens - real-time network monitor
 * Copyright (C) 2006  Mark Abrahams
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 */

package nz.co.abrahams.asithappens.accounting;

import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import nz.co.abrahams.asithappens.collectors.DataCollector;
import nz.co.abrahams.asithappens.collectors.DataCollectorDAO;
import nz.co.abrahams.asithappens.core.DAOFactory;
import nz.co.abrahams.asithappens.core.DBException;
import nz.co.abrahams.asithappens.core.DBUtil;
import nz.co.abrahams.asithappens.snmputil.SNMPException;
import nz.co.abrahams.asithappens.storage.Device;
import nz.co.abrahams.asithappens.storage.DeviceDAO;
import org.apache.log4j.Logger;

/**
 *
 * @author mark
 */
public class IPAccountingCheckpointCollectorDAO implements DataCollectorDAO {
    
    /** Collector creation SQL statement */
    public static final String CREATE = "INSERT INTO IPAccountingCheckpointCollectors (sessionID, device, pollInterval) VALUES (?,?,?)";
    
    /** Device retrieval SQL statement */
    public static final String RETRIEVE_DEVICE = "SELECT device FROM IPAccountingCheckpointCollectors WHERE sessionID = ?";
    /** Polling interval retrieval SQL statement */
    public static final String RETRIEVE_POLLINTERVAL = "SELECT pollInterval FROM IPAccountingCheckpointCollectors WHERE sessionID = ?";
    
    /** Logging provider */
    public static Logger logger = Logger.getLogger(IPAccountingCheckpointCollectorDAO.class);
    
    /** Database connection */
    Connection connection;
    
    /** Creates a new BandwidthCollector DAO */
    public IPAccountingCheckpointCollectorDAO(Connection connection) {
        this.connection = connection;
    }
    
    /**
     * Creates a BandwidthCollector in the database.
     *
     * @param sessionID      the session ID that the collector belongs to
     * @param dataCollector  the collector to create in the database
     */
    public void create(int sessionID, DataCollector dataCollector) throws DBException {
        IPAccountingCheckpointCollector collector;
        PreparedStatement statement;
        
        collector = (IPAccountingCheckpointCollector)dataCollector;
        try {
            statement = connection.prepareStatement(CREATE);
            statement.setInt(1, sessionID);
            statement.setString(2, collector.getDevice().getName());
            statement.setLong(3, collector.getPollInterval());
            statement.executeUpdate();
            statement.close();
                        
        } catch (SQLException e) {
            logger.error("Problem creating IPAccountingCheckpointCollector in database for session " + sessionID);
            throw new DBException("Problem creating IPAccountingCheckpointCollector in database for session " + sessionID, e);
        }
    }
    
    /**
     * Retrieves a IPAccountingCheckpointCollector definition from the database.
     *
     * @param sessionID      the session ID of the collector
     * @return               the collector created from the database definition
     */
    public IPAccountingCheckpointCollector retrieve(int sessionID) throws DBException, UnknownHostException, SNMPException {
        DeviceDAO deviceDAO;
        String name;
        Device device;
        IPAccountingCheckpointSNMP accountingSNMP;
        long pollInterval;
        
        name = (String)(DBUtil.retrieveSingleAttributeWithPK(connection, RETRIEVE_DEVICE, sessionID));
        deviceDAO = DAOFactory.getDeviceDAO();
        device = deviceDAO.retrieveDevice(name, false);
        deviceDAO.closeConnection();
        accountingSNMP = new IPAccountingCheckpointSNMP(device);
        pollInterval = ((Long)(DBUtil.retrieveSingleAttributeWithPK(connection, RETRIEVE_POLLINTERVAL, sessionID))).longValue();

        return new IPAccountingCheckpointCollector(accountingSNMP, pollInterval);
    }
    
    /**
     * Closes database connection opened for this DAO.
     */
    public void closeConnection() throws DBException {
        try {
            connection.close();
        } catch (SQLException e) {
            logger.error("Error closing database connection for IPAccountingCheckpointCollector DAO");
            throw new DBException("Error closing database connection for IPAccountingCheckpointCollector DAO", e);
        }
    }
    
}
