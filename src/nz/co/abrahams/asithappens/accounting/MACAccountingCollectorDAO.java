/*
 * MACAccountingCollectorDAO.java
 *
 * Created on 31 August 2012, 02:27
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
import nz.co.abrahams.asithappens.snmputil.PortsSelectorSNMP;
import nz.co.abrahams.asithappens.snmputil.SNMPException;
import nz.co.abrahams.asithappens.storage.Device;
import nz.co.abrahams.asithappens.storage.DeviceDAO;
import nz.co.abrahams.asithappens.storage.Direction;
import org.apache.log4j.Logger;

/**
 *
 * @author mark
 */
public class MACAccountingCollectorDAO implements DataCollectorDAO {
    
    /** Collector creation SQL statement */
    public static final String CREATE = "INSERT INTO MACAccountingCollectors (sessionID, device, pollInterval, ifDescr, direction) VALUES (?,?,?,?,?)";
    
    /** Device retrieval SQL statement */
    public static final String RETRIEVE_DEVICE = "SELECT device FROM MACAccountingCollectors WHERE sessionID = ?";
    /** Polling interval retrieval SQL statement */
    public static final String RETRIEVE_POLLINTERVAL = "SELECT pollInterval FROM MACAccountingCollectors WHERE sessionID = ?";
    /** Interface description retrieval SQL statement */
    public static final String RETRIEVE_IFDESCR = "SELECT ifDescr FROM MACAccountingCollectors WHERE sessionID = ?";
    /** Direction retrieval SQL statement */
    public static final String RETRIEVE_DIRECTION = "SELECT direction FROM MACAccountingCollectors WHERE sessionID = ?";
    
    /** Logging provider */
    public static Logger logger = Logger.getLogger(MACAccountingCollectorDAO.class);
    
    /** Database connection */
    Connection connection;
    
    /** Creates a new BandwidthCollector DAO */
    public MACAccountingCollectorDAO(Connection connection) {
        this.connection = connection;
    }
    
    /**
     * Creates a BandwidthCollector in the database.
     *
     * @param sessionID      the session ID that the collector belongs to
     * @param dataCollector  the collector to create in the database
     */
    public void create(int sessionID, DataCollector dataCollector) throws DBException {
        MACAccountingCollector collector;
        PreparedStatement statement;
        
        collector = (MACAccountingCollector)dataCollector;
        try {
            statement = connection.prepareStatement(CREATE);
            statement.setInt(1, sessionID);
            statement.setString(2, collector.getDevice().getName());
            statement.setLong(3, collector.getPollInterval());
            statement.setString(4, collector.getIfDescr());
            statement.setInt(5, collector.getDirection().getOIDIndex());
            statement.executeUpdate();
            statement.close();
                        
        } catch (SQLException e) {
            logger.error("Problem creating MACAccountingCollector in database for session " + sessionID);
            throw new DBException("Problem creating MACAccountingCollector in database for session " + sessionID, e);
        }
    }
    
    /**
     * Retrieves a MACAccountingCollector definition from the database.
     *
     * @param sessionID      the session ID of the collector
     * @return               the collector created from the database definition
     */
    public MACAccountingCollector retrieve(int sessionID) throws DBException, UnknownHostException, SNMPException {
        DeviceDAO deviceDAO;
        String name;
        Device device;
        String ifDescr;
        Direction direction;
        PortsSelectorSNMP portsSNMP;
        int ifIndex;
        MACAccountingSNMP accountingSNMP;
        long pollInterval;
        
        name = (String)(DBUtil.retrieveSingleAttributeWithPK(connection, RETRIEVE_DEVICE, sessionID));
        deviceDAO = DAOFactory.getDeviceDAO();
        device = deviceDAO.retrieveDevice(name, false);
        deviceDAO.closeConnection();
        ifDescr = (String)(DBUtil.retrieveSingleAttributeWithPK(connection, RETRIEVE_IFDESCR, sessionID));
        direction = Direction.getDirectionFromOIDIndex(((Integer)(DBUtil.retrieveSingleAttributeWithPK(connection, RETRIEVE_DIRECTION, sessionID))).intValue());
        portsSNMP = new PortsSelectorSNMP(device, false);
        ifIndex = portsSNMP.getIfIndex(ifDescr);
        
        accountingSNMP = new MACAccountingSNMP(device, ifIndex, ifDescr, direction);
        pollInterval = ((Long)(DBUtil.retrieveSingleAttributeWithPK(connection, RETRIEVE_POLLINTERVAL, sessionID))).longValue();

        return new MACAccountingCollector(accountingSNMP, pollInterval);
    }
    
    /**
     * Closes database connection opened for this DAO.
     */
    public void closeConnection() throws DBException {
        try {
            connection.close();
        } catch (SQLException e) {
            logger.error("Error closing database connection for MACAccountingCollector DAO");
            throw new DBException("Error closing database connection for MACAccountingCollector DAO", e);
        }
    }
    
}
