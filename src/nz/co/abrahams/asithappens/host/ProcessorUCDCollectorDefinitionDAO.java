/*
 * ProcessorUCDCollectorDefinitionDAO.java
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
public class ProcessorUCDCollectorDefinitionDAO extends CollectorDefinitionDAO implements DataCollectorDAO {
    
    /** Collector creation SQL statement */
    public static final String CREATE = "INSERT INTO ProcessorUCDCollectors (collectorID) VALUES (?)";
    /** Collector deletion SQL statement */
    public static final String DELETE = "DELETE FROM ProcessorUCDCollectors WHERE collectorID = ?";
    
    /** Creates a new ProcessorUCDCollectorDefinition DAO */
    public ProcessorUCDCollectorDefinitionDAO(Connection connection) {
        super(connection);
    }
    
    /**
     * Creates an ProcessorUCDCollectorDefinition in the database.
     *
     * @param collectorDefinition  the collector definition to create in the database
     * @return                     the unique key for the collector
     */
    public int create(CollectorDefinition collectorDefinition) throws DBException {
        ProcessorUCDCollectorDefinition definition;
        int collectorID;
        PreparedStatement statement;
        
        definition = (ProcessorUCDCollectorDefinition)collectorDefinition;
        collectorID = createCollectorDefinition(definition);
        
        try {
            statement = connection.prepareStatement(CREATE);
            statement.setInt(1, collectorID);
            statement.executeUpdate();
            statement.close();
            
            return collectorID;
                        
        } catch (SQLException e) {
            logger.error("Problem creating ProcessorUCDCollectorDefinition in database for session " + collectorID);
            throw new DBException("Problem creating ProcessorUCDCollectorDefinition in database for session " + collectorID, e);
        }
    }
    
    /**
     * Retrieves a ProcessorUCDCollectorDefinition from the database.
     *
     * @param collectorID    the collector ID of the collector
     * @return               the collector definition created from the database definition
     */
    public ProcessorUCDCollectorDefinition retrieve(int collectorID) throws DBException, UnknownHostException {
        String title;
        Device device;
        long pollInterval;
        boolean storing;
        
        title = retrieveTitle(collectorID);
        device = retrieveDevice(collectorID, SNMPAccessType.ReadOnly);
        pollInterval = retrievePollInterval(collectorID);
        storing = retrieveStoring(collectorID);
        
        return new ProcessorUCDCollectorDefinition(title, device, pollInterval, storing);
    }
    
    /**
     * Deletes a ProcessorUCDCollector definition from the database.
     *
     * @param collectorID      the collector ID of the collector
     */
    public void delete(int collectorID) throws DBException {
        DBUtil.deleteWithIntKey(connection, DELETE, collectorID);
        deleteCollectorDefinition(collectorID);
    }

}
