/*
 * BandwidthCollectorDAO.java
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

package nz.co.abrahams.asithappens.bandwidth;

import nz.co.abrahams.asithappens.storage.DeviceDAO;
import nz.co.abrahams.asithappens.storage.Device;
import nz.co.abrahams.asithappens.collectors.DataCollectorDAO;
import nz.co.abrahams.asithappens.collectors.DataCollector;
import nz.co.abrahams.asithappens.core.DAOFactory;
import nz.co.abrahams.asithappens.snmputil.PortsSelectorSNMP;
import nz.co.abrahams.asithappens.snmputil.SNMPException;
import nz.co.abrahams.asithappens.core.DBUtil;
import nz.co.abrahams.asithappens.core.DBException;
import java.sql.Connection;
//import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.net.UnknownHostException;
import java.util.Vector;
import org.apache.log4j.Logger;

/**
 *
 * @author mark
 */
public class BandwidthCollectorDAO implements DataCollectorDAO {
    
    /** Collector creation SQL statement */
    public static final String CREATE = "INSERT INTO BandwidthCollectors (sessionID, device, pollInterval, prefer64BitCounters) VALUES (?,?,?,?)";
    /** Collector port creation SQL statement */
    public static final String CREATE_PORT = "INSERT INTO BandwidthCollectorPorts (sessionID, port) VALUES (?,?)";
    
    /** Device retrieval SQL statement */
    public static final String RETRIEVE_DEVICE = "SELECT device FROM BandwidthCollectors WHERE sessionID = ?";
    /** Polling interval retrieval SQL statement */
    public static final String RETRIEVE_POLLINTERVAL = "SELECT pollInterval FROM BandwidthCollectors WHERE sessionID = ?";
    /** Polling interval retrieval SQL statement */
    public static final String RETRIEVE_USE64BITCOUNTERS = "SELECT prefer64BitCounters FROM BandwidthCollectors WHERE sessionID = ?";
    /** Port retrieval SQL statement */
    public static final String RETRIEVE_PORTS = "SELECT port FROM BandwidthCollectorPorts WHERE sessionID = ?";
    
    /** Logging provider */
    public static Logger logger = Logger.getLogger(BandwidthCollectorDAO.class);
    
    /** Database connection */
    Connection connection;
    
    /** Creates a new BandwidthCollector DAO */
    public BandwidthCollectorDAO(Connection connection) {
        this.connection = connection;
    }
    
    /**
     * Creates a BandwidthCollector in the database.
     *
     * @param sessionID      the session ID that the collector belongs to
     * @param dataCollector  the collector to create in the database
     */
    public void create(int sessionID, DataCollector dataCollector) throws DBException {
        BandwidthCollector collector;
        PreparedStatement statement;
        
        collector = (BandwidthCollector)dataCollector;
        try {
            statement = connection.prepareStatement(CREATE);
            statement.setInt(1, sessionID);
            statement.setString(2, collector.getDevice().getName());
            statement.setLong(3, collector.getPollInterval());
            statement.setInt(4, collector.use64BitCounters() ? 1 : 0);
            statement.executeUpdate();
            statement.close();
            
            for ( int i = 0 ; i < collector.getPortDescriptions().length ; i++ ) {
                statement = connection.prepareStatement(CREATE_PORT);
                statement.setInt(1, sessionID);
                statement.setString(2, collector.getPortDescriptions()[i]);
                statement.executeUpdate();
                statement.close();
            }
            
        } catch (SQLException e) {
            logger.error("Problem creating BandwidthCollector in database for session " + sessionID);
            throw new DBException("Problem creating BandwidthCollector in database for session " + sessionID, e);
        }
    }
    
    /**
     * Retrieves a BandwidthCollector definition from the database.
     *
     * @param sessionID      the session ID of the collector
     * @return               the collector created from the database definition
     */
    public BandwidthCollector retrieve(int sessionID) throws DBException, UnknownHostException, SNMPException {
        DeviceDAO deviceDAO;
        String name;
        Device device;
        PortsSelectorSNMP portsSNMP;
        BandwidthSNMP bandwidthSNMP;
        long pollInterval;
        boolean prefer64BitCounters;
        String[] ifDescriptions;
        int[] ifIndicies;
        
        name = (String)(DBUtil.retrieveSingleAttributeWithPK(connection, RETRIEVE_DEVICE, sessionID));
        deviceDAO = DAOFactory.getDeviceDAO();
        device = deviceDAO.retrieveDevice(name, false);
        deviceDAO.closeConnection();
        bandwidthSNMP = new BandwidthSNMP(device);
        pollInterval = ((Long)(DBUtil.retrieveSingleAttributeWithPK(connection, RETRIEVE_POLLINTERVAL, sessionID))).longValue();
        prefer64BitCounters = ((Byte)(DBUtil.retrieveSingleAttributeWithPK(connection, RETRIEVE_USE64BITCOUNTERS, sessionID))).intValue() == 1;
        ifDescriptions = retrieveBandwidthCollectorPorts(sessionID);
        portsSNMP = new PortsSelectorSNMP(device, false);
        ifIndicies = portsSNMP.getIfIndexArray(ifDescriptions);

        bandwidthSNMP = new BandwidthSNMP(device);
        return new BandwidthCollector(bandwidthSNMP, pollInterval, ifIndicies, ifDescriptions, prefer64BitCounters);
    }
    
    public String[] retrieveBandwidthCollectorPorts(int sessionID) throws DBException {
        PreparedStatement statement;
        ResultSet results;
        Vector<String> portsVector;
        String[] ports;
        
        try {
            portsVector = new Vector<String>();
            statement = connection.prepareStatement(RETRIEVE_PORTS);
            statement.setInt(1, sessionID);
            results = statement.executeQuery();
            while (results.next())
                portsVector.add(new String(results.getString("port")));
            ports = new String[portsVector.size()];
            for ( int port = 0 ; port < portsVector.size() ; port++ )
                ports[port] = ((String)(portsVector.elementAt(port)));
            results.close();
            statement.close();
            return ports;
        } catch (SQLException e) {
            logger.error("Cannot retrieve port list for bandwidth session ID " + sessionID + " from database");
            throw new DBException("Cannot retrieve port list for bandwidth session ID " + sessionID + " from database", e);
        }
    }
    
    
    /**
     * Closes database connection opened for this DAO.
     */
    public void closeConnection() throws DBException {
        try {
            connection.close();
        } catch (SQLException e) {
            logger.error("Error closing database connection for BandwidthCollector DAO");
            throw new DBException("Error closing database connection for BandwidthCollector DAO", e);
        }
    }
    
}
