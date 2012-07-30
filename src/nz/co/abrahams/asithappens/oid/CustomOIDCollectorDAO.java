/*
 * CustomOIDCollectorDAO.java
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

package nz.co.abrahams.asithappens.oid;

import nz.co.abrahams.asithappens.storage.DeviceDAO;
import nz.co.abrahams.asithappens.storage.Device;
import nz.co.abrahams.asithappens.collectors.DataCollectorDAO;
import nz.co.abrahams.asithappens.collectors.DataCollector;
import nz.co.abrahams.asithappens.core.DAOFactory;
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
public class CustomOIDCollectorDAO implements DataCollectorDAO {
    
    /** Collector creation SQL statement */
    public static final String CREATE = "INSERT INTO CustomOIDCollectors (sessionID, device, pollInterval, valueUnits) VALUES (?,?,?,?)";
    /** Collector port creation SQL statement */
    public static final String CREATE_COLLECTOR_OID = "INSERT INTO CustomOIDCollectorOIDs (sessionID, position, oidID) VALUES (?,?,?)";
    
    /** Device retrieval SQL statement */
    public static final String RETRIEVE_DEVICE = "SELECT device FROM CustomOIDCollectors WHERE sessionID = ?";
    /** Polling interval retrieval SQL statement */
    public static final String RETRIEVE_POLLINTERVAL = "SELECT pollInterval FROM CustomOIDCollectors WHERE sessionID = ?";
    /** Value units retrieval SQL statement */
    public static final String RETRIEVE_VALUEUNITS = "SELECT valueUnits FROM CustomOIDCollectors WHERE sessionID = ?";
    /** OID list retrieval SQL statement */
    //public static final String RETRIEVE_COLLECTOR_OIDS = "SELECT position, label, oid, oidType, description " +
    //        "FROM CustomOIDCollectorOIDs, CustomOIDs WHERE CustomOIDCollectorOIDs.oidID = CustomOIDs.oidID AND sessionID = ?";
    public static final String RETRIEVE_COLLECTOR_OIDS = "SELECT oidID FROM CustomOIDCollectorOIDs WHERE sessionID = ? ORDER BY position";
    
    /** Logging provider */
    public static Logger logger = Logger.getLogger(CustomOIDCollectorDAO.class);
    
    /** Database connection */
    Connection connection;
    
    /** Creates a new CustomOIDCollector DAO */
    public CustomOIDCollectorDAO(Connection connection) {
        this.connection = connection;
    }
    
    /**
     * Creates a CustomOIDCollector in the database.
     *
     * @param sessionID      the session ID that the collector belongs to
     * @param dataCollector  the collector to create in the database
     */
    public void create(int sessionID, DataCollector dataCollector) throws DBException {
        CustomOIDCollector collector;
        int[] oidIDs;
        PreparedStatement statement;
        CustomOIDDAO customOIDDAO;
        
        collector = (CustomOIDCollector)dataCollector;
        oidIDs = new int[collector.getOIDs().size()];
        try {
            statement = connection.prepareStatement(CREATE);
            statement.setInt(1, sessionID);
            statement.setString(2, collector.getDevice().getName());
            statement.setLong(3, collector.getPollInterval());
            statement.setString(4, collector.getValueUnits());
            statement.executeUpdate();
            statement.close();
            
            customOIDDAO = DAOFactory.getCustomOIDDAO(connection);
            for ( int i = 0 ; i < collector.getOIDs().size() ; i++ ) {
                oidIDs[i] = customOIDDAO.create(collector.getOIDs().elementAt(i));                
            }
            for ( int i = 0 ; i < collector.getOIDs().size() ; i++ ) {
                statement = connection.prepareStatement(CREATE_COLLECTOR_OID);
                statement.setInt(1, sessionID);
                statement.setInt(2, i);
                statement.setInt(3, oidIDs[i]);
                statement.executeUpdate();
                statement.close();
            }
            
        } catch (SQLException e) {
            logger.error("Problem creating CustomOIDCollector in database for session " + sessionID);
            throw new DBException("Problem creating CustomOIDCollector in database for session " + sessionID, e);
        }
    }
    
    /**
     * Retrieves a CustomOIDCollector defintion from the database.
     *
     * @param sessionID      the session ID of the collector
     * @return               the collector created from the database definition
     */
    public CustomOIDCollector retrieve(int sessionID) throws DBException, UnknownHostException, SNMPException {
        DeviceDAO deviceDAO;
        CustomOIDDAO customOIDDAO;
        String name;
        Device device;
        CustomOIDSNMP snmp;
        long pollInterval;
        String valueUnits;
        Vector<Integer> oidIDs;
        Vector<CustomOID> customOIDs;
        PreparedStatement statement;
        ResultSet results;
        
        name = (String)(DBUtil.retrieveSingleAttributeWithPK(connection, RETRIEVE_DEVICE, sessionID));
        deviceDAO = DAOFactory.getDeviceDAO(connection);
        device = deviceDAO.retrieveDevice(name, false);
        snmp = new CustomOIDSNMP(device);
        pollInterval = ((Long)(DBUtil.retrieveSingleAttributeWithPK(connection, RETRIEVE_POLLINTERVAL, sessionID))).longValue();
        valueUnits = (String)(DBUtil.retrieveSingleAttributeWithPK(connection, RETRIEVE_VALUEUNITS, sessionID));
        oidIDs = new Vector();
        customOIDs = new Vector();
        
        
        try {
            statement = connection.prepareStatement(RETRIEVE_COLLECTOR_OIDS, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            statement.setInt(1, sessionID);
            results = statement.executeQuery();
            while (results.next()) {
                oidIDs.add(new Integer(results.getInt("oidID")));
                System.out.println("oidID: " + results.getInt("oidID"));
            }
            results.close();
            statement.close();
            customOIDDAO = DAOFactory.getCustomOIDDAO(connection);
            for ( int i = 0 ; i < oidIDs.size() ; i++ ) {
                customOIDs.add(customOIDDAO.retrieve(oidIDs.elementAt(i).intValue()));
            }
            return new CustomOIDCollector(snmp, pollInterval, valueUnits, customOIDs);
        } catch (SQLException e) {
            logger.error("Problem retrieving custom OIDs for session " + sessionID);
            throw new DBException("Problem retrieving custom OIDs for session " + sessionID, e);
        }        
    }
    
    /**
     * Closes database connection opened for this DAO.
     */
    public void closeConnection() throws DBException {
        try {
            connection.close();
        } catch (SQLException e) {
            logger.error("Error closing database connection for CustomOIDCollector DAO");
            throw new DBException("Error closing database connection for CustomOIDCollector DAO", e);
        }
    }
    
}
