/*
 * SNMPTableCollectorDAO.java
 *
 * Created on 31 August 2012, 02:53
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

package nz.co.abrahams.asithappens.collectors;

import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import nz.co.abrahams.asithappens.accounting.IPAccountingActiveSNMP;
import nz.co.abrahams.asithappens.core.DAOFactory;
import nz.co.abrahams.asithappens.core.DBException;
import nz.co.abrahams.asithappens.core.DBUtil;
import nz.co.abrahams.asithappens.core.DataType;
import nz.co.abrahams.asithappens.snmputil.SNMPException;
import nz.co.abrahams.asithappens.snmputil.SNMPType;
import nz.co.abrahams.asithappens.storage.Device;
import nz.co.abrahams.asithappens.storage.DeviceDAO;
import org.apache.log4j.Logger;

/**
 *
 * @author mark
 */
public class SNMPTableCollectorDAO implements DataCollectorDAO {
    
    /** Collector creation SQL statement */
    public static final String CREATE = "INSERT INTO SNMPTableCollectors (sessionID, device, pollInterval, dataTypeID, snmpTypeID) VALUES (?,?,?,?,?)";
    
    /** Device retrieval SQL statement */
    public static final String RETRIEVE_DEVICE = "SELECT device FROM SNMPTableCollectors WHERE sessionID = ?";
    /** Polling interval retrieval SQL statement */
    public static final String RETRIEVE_POLLINTERVAL = "SELECT pollInterval FROM SNMPTableCollectors WHERE sessionID = ?";
    /** Data type ID retrieval SQL statement */
    public static final String RETRIEVE_DATATYPEID = "SELECT dataTypeID FROM SNMPTableCollectors WHERE sessionID = ?";
    /** SNMP type ID retrieval SQL statement */
    public static final String RETRIEVE_SNMPTYPEID = "SELECT snmpTypeID FROM SNMPTableCollectors WHERE sessionID = ?";
    
    /** Logging provider */
    public static Logger logger = Logger.getLogger(SNMPTableCollectorDAO.class);
    
    /** Database connection */
    Connection connection;
    
    /** Creates a new BandwidthCollector DAO */
    public SNMPTableCollectorDAO(Connection connection) {
        this.connection = connection;
    }
    
    /**
     * Creates a BandwidthCollector in the database.
     *
     * @param sessionID      the session ID that the collector belongs to
     * @param dataCollector  the collector to create in the database
     */
    public void create(int sessionID, DataCollector dataCollector) throws DBException {
        SNMPTableCollector collector;
        PreparedStatement statement;
        
        collector = (SNMPTableCollector)dataCollector;
        try {
            statement = connection.prepareStatement(CREATE);
            statement.setInt(1, sessionID);
            statement.setString(2, collector.getDevice().getName());
            statement.setLong(3, collector.getPollInterval());
            statement.setInt(4, collector.getDataType().id);
            statement.setInt(5, collector.getSNMPType().ordinal());
            statement.executeUpdate();
            statement.close();
                        
        } catch (SQLException e) {
            logger.error("Problem creating SNMPTableCollector in database for session " + sessionID);
            throw new DBException("Problem creating SNMPTableCollector in database for session " + sessionID, e);
        }
    }
    
    /**
     * Retrieves a SNMPTableCollector definition from the database.
     *
     * @param sessionID      the session ID of the collector
     * @return               the collector created from the database definition
     */
    public SNMPTableCollector retrieve(int sessionID) throws DBException, UnknownHostException, SNMPException {
        DeviceDAO deviceDAO;
        String name;
        Device device;
        DataType dataType;
        SNMPType snmpType;
        SNMPTableInterface snmpInterface;
        long pollInterval;
        
        name = (String)(DBUtil.retrieveSingleAttributeWithPK(connection, RETRIEVE_DEVICE, sessionID));
        deviceDAO = DAOFactory.getDeviceDAO();
        device = deviceDAO.retrieveDevice(name, false);
        deviceDAO.closeConnection();
        dataType = DataType.getType(((Integer)(DBUtil.retrieveSingleAttributeWithPK(connection, RETRIEVE_DATATYPEID, sessionID))).intValue());
        if ( dataType.id == DataType.ACCOUNTING.id ) {
            snmpInterface = new IPAccountingActiveSNMP(device);
        } else {
            snmpInterface = null;
        }
        snmpType = SNMPType.values()[((Integer)(DBUtil.retrieveSingleAttributeWithPK(connection, RETRIEVE_SNMPTYPEID, sessionID))).intValue()];
        pollInterval = ((Long)(DBUtil.retrieveSingleAttributeWithPK(connection, RETRIEVE_POLLINTERVAL, sessionID))).longValue();

        return new SNMPTableCollector(dataType, snmpType, snmpInterface, pollInterval);
    }
    
    /**
     * Closes database connection opened for this DAO.
     */
    public void closeConnection() throws DBException {
        try {
            connection.close();
        } catch (SQLException e) {
            logger.error("Error closing database connection for SNMPTableCollector DAO");
            throw new DBException("Error closing database connection for SNMPTableCollector DAO", e);
        }
    }
    
}
