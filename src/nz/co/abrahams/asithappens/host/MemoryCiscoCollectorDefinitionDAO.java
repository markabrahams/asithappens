/*
 * MemoryCiscoCollectorDefinitionDAO.java
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

package nz.co.abrahams.asithappens.host;

import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import nz.co.abrahams.asithappens.collectors.CollectorDefinition;
import nz.co.abrahams.asithappens.collectors.CollectorDefinitionDAO;
import nz.co.abrahams.asithappens.collectors.DataCollectorDAO;
import nz.co.abrahams.asithappens.core.DBException;
import nz.co.abrahams.asithappens.core.DBUtil;
import nz.co.abrahams.asithappens.snmputil.SNMPAccessType;
import nz.co.abrahams.asithappens.snmputil.SNMPException;
import nz.co.abrahams.asithappens.storage.Device;

/**
 *
 * @author mark
 */
public class MemoryCiscoCollectorDefinitionDAO extends CollectorDefinitionDAO implements DataCollectorDAO {
    
    /** Collector creation SQL statement */
    public static final String CREATE = "INSERT INTO MemoryCiscoCollectors (collectorID, memoryDescr, snmpIndex) VALUES (?,?,?)";
    public static final String RETRIEVE_MEMORY_DESCR = "SELECT memoryDescr FROM MemoryCiscoCollectors WHERE collectorID = ?";
    public static final String RETRIEVE_SNMP_INDEX = "SELECT snmpIndex FROM MemoryCiscoCollectors WHERE collectorID = ?";
    /** Collector deletion SQL statement */
    public static final String DELETE = "DELETE FROM MemoryCiscoCollectors WHERE collectorID = ?";
    
    /** Creates a new MemoryCiscoCollectorDefinition DAO */
    public MemoryCiscoCollectorDefinitionDAO(Connection connection) {
        super(connection);
    }
    
    /**
     * Creates an MemoryCiscoCollectorDefinition in the database.
     *
     * @param collectorDefinition  the collector definition to create in the database
     * @return the unique key for the collector
     */
    public int create(CollectorDefinition collectorDefinition) throws DBException {
        MemoryCiscoCollectorDefinition definition;
        int collectorID;
        PreparedStatement statement;
        
        definition = (MemoryCiscoCollectorDefinition)collectorDefinition;
        collectorID = createCollectorDefinition(definition);
        
        try {
            statement = connection.prepareStatement(CREATE);
            statement.setInt(1, collectorID);
            statement.setString(2, definition.getMemoryString());
            statement.setInt(3, definition.getMemoryIndex());
            statement.executeUpdate();
            statement.close();
            
            return collectorID;
                        
        } catch (SQLException e) {
            logger.error("Problem creating MemoryCiscoCollectorDefinition in database for session " + collectorID);
            throw new DBException("Problem creating MemoryCiscoCollectorDefinition in database for session " + collectorID, e);
        }
    }
    
    /**
     * Retrieves a MemoryCiscoCollectorDefinition from the database.
     *
     * @param collectorID    the collector ID of the collector
     * @return               the collector definition created from the database definition
     */
    public MemoryCiscoCollectorDefinition retrieve(int collectorID) throws DBException, UnknownHostException {
        String title;
        Device device;
        long pollInterval;
        boolean storing;
        String memoryDescr;
        int snmpIndex;
        
        title = retrieveTitle(collectorID);
        device = retrieveDevice(collectorID, SNMPAccessType.ReadOnly);
        pollInterval = retrievePollInterval(collectorID);
        storing = retrieveStoring(collectorID);
        memoryDescr = DBUtil.retrieveStringWithPK(connection, RETRIEVE_MEMORY_DESCR, collectorID);
        snmpIndex = DBUtil.retrieveIntWithPK(connection, RETRIEVE_SNMP_INDEX, collectorID);
        
        return new MemoryCiscoCollectorDefinition(title, device, pollInterval, storing, memoryDescr, snmpIndex);
    }
    
    /**
     * Deletes a MemoryCiscoCollector definition from the database.
     *
     * @param collectorID      the collector ID of the collector
     */
    public void delete(int collectorID) throws DBException {
        DBUtil.deleteWithIntKey(connection, DELETE, collectorID);
        deleteCollectorDefinition(collectorID);
    }

}
