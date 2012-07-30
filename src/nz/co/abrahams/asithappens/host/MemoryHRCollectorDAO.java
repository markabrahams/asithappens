/*
 * MemoryHRCollectorDAO.java
 *
 * Created on 28 June 2008, 01:28
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

package nz.co.abrahams.asithappens.host;

import nz.co.abrahams.asithappens.storage.DeviceDAO;
import nz.co.abrahams.asithappens.storage.Device;
import nz.co.abrahams.asithappens.collectors.DataCollector;
import nz.co.abrahams.asithappens.collectors.DataCollectorDAO;
import nz.co.abrahams.asithappens.core.DAOFactory;
import nz.co.abrahams.asithappens.snmputil.SNMPException;
import nz.co.abrahams.asithappens.core.DBUtil;
import nz.co.abrahams.asithappens.core.DBException;
import java.sql.Connection;
//import java.sql.Statement;
import java.sql.PreparedStatement;
//import java.sql.ResultSet;
import java.sql.SQLException;
import java.net.UnknownHostException;
//import java.util.Vector;
import org.apache.log4j.Logger;

/**
 *
 * @author mark
 */
public class MemoryHRCollectorDAO implements DataCollectorDAO {
    
    /** Collector creation SQL statement */
    public static final String CREATE = "INSERT INTO MemoryHRCollectors (sessionID, device, pollInterval, snmpIndex) VALUES (?,?,?,?)";
    
    /** Device retrieval SQL statement */
    public static final String RETRIEVE_DEVICE = "SELECT device FROM MemoryHRCollectors WHERE sessionID = ?";

    /** Poll interval retrieval SQL statement */
    public static final String RETRIEVE_POLLINTERVAL = "SELECT pollInterval FROM MemoryHRCollectors WHERE sessionID = ?";

    /** SNMP index retrieval SQL statement */
    public static final String RETRIEVE_INDEX = "SELECT snmpIndex FROM MemoryHRCollectors WHERE sessionID = ?";

    /** Collector retrieval SQL statement */
    //public static final String RETRIEVE = "SELECT (device, pollInterval, snmpIndex) FROM MemoryHRCollectors WHERE sessionID = ?";
    
    
    /** Logging provider */
    public static Logger logger = Logger.getLogger(MemoryHRCollectorDAO.class);
    
    /** Database connection */
    Connection connection;
    
    /** Creates a new MemoryHRCollector DAO */
    public MemoryHRCollectorDAO(Connection connection) {
        this.connection = connection;
    }
    
    /**
     * Creates a MemoryHRCollector in the database.
     *
     * @param sessionID      the session ID that the collector belongs to
     * @param dataCollector  the collector to create in the database
     */
    public void create(int sessionID, DataCollector dataCollector) throws DBException {
        MemoryHRCollector collector;
        PreparedStatement statement;
        int deviceID;
        
        collector = (MemoryHRCollector)dataCollector;
        try {
            statement = connection.prepareStatement(CREATE);
            statement.setInt(1, sessionID);
            statement.setString(2, collector.getDevice().getName());
            statement.setLong(3, collector.getPollInterval());
            statement.setInt(4, collector.getIndex());
            statement.executeUpdate();
            statement.close();
            
        } catch (SQLException e) {
            logger.error("Problem creating MemoryHRCollector in database for session " + sessionID);
            throw new DBException("Problem creating MemoryHRCollector in database for session " + sessionID, e);
        }
    }
    
    /**
     * Retrieves a MemoryHRCollector defintion from the database.
     *
     * @param sessionID      the session ID of the collector
     * @return               the collector created from the database definition
     */
    public MemoryHRCollector retrieve(int sessionID) throws DBException, UnknownHostException, SNMPException {
        DeviceDAO deviceDAO;
        String name;
        Device device;
        MemoryHRSNMP snmp;
        long pollInterval;
        int snmpIndex;
        
        name = (String)(DBUtil.retrieveSingleAttributeWithPK(connection, RETRIEVE_DEVICE, sessionID));
        deviceDAO = DAOFactory.getDeviceDAO(connection);
        device = deviceDAO.retrieveDevice(name, false);
        snmp = new MemoryHRSNMP(device);
        pollInterval = ((Long)(DBUtil.retrieveSingleAttributeWithPK(connection, RETRIEVE_POLLINTERVAL, sessionID))).longValue();
        snmpIndex = ((Integer)(DBUtil.retrieveSingleAttributeWithPK(connection, RETRIEVE_INDEX, sessionID))).intValue();
        //device.enumeratePorts();
        return new MemoryHRCollector(snmp, pollInterval, snmpIndex);
    }
    
    /**
     * Closes database connection opened for this DAO.
     */
    public void closeConnection() throws DBException {
        DBUtil.closeConnection(connection, this.getClass().getName());
    }
    
}
