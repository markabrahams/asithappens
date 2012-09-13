/*
 * MemoryHRCollectorDefinitionDAO.java
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
public class MemoryHRCollectorDefinitionDAO extends CollectorDefinitionDAO implements DataCollectorDAO {
    
    /** Collector creation SQL statement */
    public static final String CREATE = "INSERT INTO MemoryHRCollectors (collectorID, memoryDescr, snmpIndex) VALUES (?,?,?)";
    public static final String RETRIEVE_MEMORY_DESCR = "SELECT memoryDescr FROM MemoryHRCollectors WHERE collectorID = ?";
    public static final String RETRIEVE_SNMP_INDEX = "SELECT snmpIndex FROM MemoryHRCollectors WHERE collectorID = ?";
    /** Collector deletion SQL statement */
    public static final String DELETE = "DELETE FROM BandwidthCollectors WHERE collectorID = ?";
    
    /** Creates a new MemoryHRCollectorDefinition DAO */
    public MemoryHRCollectorDefinitionDAO(Connection connection) {
        super(connection);
    }
    
    /**
     * Creates an MemoryHRCollectorDefinition in the database.
     *
     * @param collectorDefinition  the collector definition to create in the database
     * @return the unique key for the collector
     */
    public int create(CollectorDefinition collectorDefinition) throws DBException {
        MemoryHRCollectorDefinition definition;
        int collectorID;
        PreparedStatement statement;
        
        definition = (MemoryHRCollectorDefinition)collectorDefinition;
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
            logger.error("Problem creating MemoryHRCollectorDefinition in database for session " + collectorID);
            throw new DBException("Problem creating MemoryHRCollectorDefinition in database for session " + collectorID, e);
        }
    }
    
    /**
     * Retrieves a MemoryHRCollectorDefinition from the database.
     *
     * @param collectorID    the collector ID of the collector
     * @return               the collector definition created from the database definition
     */
    public MemoryHRCollectorDefinition retrieve(int collectorID) throws DBException, UnknownHostException {
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
        
        return new MemoryHRCollectorDefinition(title, device, pollInterval, storing, memoryDescr, snmpIndex);
    }
    
    /**
     * Deletes a MemoryHRCollector definition from the database.
     *
     * @param collectorID      the collector ID of the collector
     */
    public void delete(int collectorID) throws DBException {
        DBUtil.deleteWithIntKey(connection, DELETE, collectorID);
        deleteCollectorDefinition(collectorID);
    }

}
