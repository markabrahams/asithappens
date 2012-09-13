/*
 * BandwidthCollectorDefinitionDAO.java
 *
 * Created on 3 September 2012, 00:37
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

import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import nz.co.abrahams.asithappens.collectors.CollectorDefinition;
import nz.co.abrahams.asithappens.collectors.CollectorDefinitionDAO;
import nz.co.abrahams.asithappens.collectors.DataCollectorDAO;
import nz.co.abrahams.asithappens.core.DBException;
import nz.co.abrahams.asithappens.core.DBUtil;
import nz.co.abrahams.asithappens.snmputil.PortsSelectorSNMP;
import nz.co.abrahams.asithappens.snmputil.SNMPAccessType;
import nz.co.abrahams.asithappens.snmputil.SNMPException;
import nz.co.abrahams.asithappens.storage.Device;

/**
 *
 * @author mark
 */
public class BandwidthCollectorDefinitionDAO extends CollectorDefinitionDAO implements DataCollectorDAO {
    
    /** Collector creation SQL statement */
    public static final String CREATE = "INSERT INTO BandwidthCollectors (collectorID, prefer64BitCounters) VALUES (?,?)";
    /** Collector port creation SQL statement */
    public static final String CREATE_PORT = "INSERT INTO BandwidthCollectorPorts (collectorID, port) VALUES (?,?)";
    
    /** Polling interval retrieval SQL statement */
    public static final String RETRIEVE_USE64BITCOUNTERS = "SELECT prefer64BitCounters FROM BandwidthCollectors WHERE collectorID = ?";
    /** Port retrieval SQL statement */
    public static final String RETRIEVE_PORTS = "SELECT port FROM BandwidthCollectorPorts WHERE collectorID = ?";

    /** Collector deletion SQL statement */
    public static final String DELETE = "DELETE FROM BandwidthCollectors WHERE collectorID = ?";
    /** Collector ports deletion SQL statement */
    public static final String DELETE_PORTS = "DELETE FROM BandwidthCollectorPorts WHERE collectorID = ?";
    
    /** Creates a new BandwidthCollector DAO */
    public BandwidthCollectorDefinitionDAO(Connection connection) {
        super(connection);
    }
    
    /**
     * Creates a BandwidthCollector definition in the database.
     *
     * @param collectorDefinition  the collector to create in the database
     */
    public int create(CollectorDefinition collectorDefinition) throws DBException {
        BandwidthCollectorDefinition definition;
        PreparedStatement statement;
        int collectorID;
        
        definition = (BandwidthCollectorDefinition)collectorDefinition;
        collectorID = createCollectorDefinition(definition);
        
        try {
            statement = connection.prepareStatement(CREATE);
            statement.setInt(1, collectorID);
            statement.setInt(2, definition.getPrefer64BitCounters() ? 1 : 0);
            statement.executeUpdate();
            statement.close();
            
            for ( int i = 0 ; i < definition.getIfDescrs().length ; i++ ) {
                statement = connection.prepareStatement(CREATE_PORT);
                statement.setInt(1, collectorID);
                statement.setString(2, definition.getIfDescrs()[i]);
                statement.executeUpdate();
                statement.close();
            }
            
            return collectorID;
            
        } catch (SQLException e) {
            logger.error("Problem creating BandwidthCollectorDefinition in database for collector " + collectorID);
            throw new DBException("Problem creating BandwidthCollectorDefinition in database for collector " + collectorID, e);
        }
    }
    
    /**
     * Retrieves a BandwidthCollector definition from the database.
     *
     * @param collectorID      the collector ID of the collector
     * @return                 the collector created from the database definition
     */
    public BandwidthCollectorDefinition retrieve(int collectorID) throws DBException, UnknownHostException {
        String title;
        Device device;
        long pollInterval;
        boolean storing;
        boolean prefer64BitCounters;
        PortsSelectorSNMP portsSNMP;
        String[] ifDescriptions;
        
        title = retrieveTitle(collectorID);
        device = retrieveDevice(collectorID, SNMPAccessType.ReadOnly);
        pollInterval = retrievePollInterval(collectorID);
        storing = retrieveStoring(collectorID);
        
        prefer64BitCounters = DBUtil.retrieveBooleanWithPK(connection, RETRIEVE_USE64BITCOUNTERS, collectorID);
        ifDescriptions = retrieveBandwidthCollectorPorts(collectorID);

        return new BandwidthCollectorDefinition(title, device, pollInterval, storing,
                ifDescriptions, prefer64BitCounters);
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
     * Deletes a BandwidthCollector definition from the database.
     *
     * @param collectorID      the collector ID of the collector
     */
    public void delete(int collectorID) throws DBException {
        DBUtil.deleteWithIntKey(connection, DELETE, collectorID);
        DBUtil.deleteWithIntKey(connection, DELETE_PORTS, collectorID);
        deleteCollectorDefinition(collectorID);
    }
        
}
