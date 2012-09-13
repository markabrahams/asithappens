/*
 * ResponseCollectorDefinitionDAO.java
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

package nz.co.abrahams.asithappens.response;

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
public class ResponseCollectorDefinitionDAO extends CollectorDefinitionDAO implements DataCollectorDAO {
    
    /** Collector creation SQL statement */
    public static final String CREATE = "INSERT INTO ResponseCollectors (collectorID) VALUES (?)";
    /** Collector deletion SQL statement */
    public static final String DELETE = "DELETE FROM ResponseCollectors WHERE collectorID = ?";
    
    /** Creates a new ResponseCollectorDefinition DAO */
    public ResponseCollectorDefinitionDAO(Connection connection) {
        super(connection);
    }
    
    /**
     * Creates an ResponseCollectorDefinition in the database.
     *
     * @param collectorDefinition  the collector definition to create in the database
     * @return                     the unique key for the collector
     */
    public int create(CollectorDefinition collectorDefinition) throws DBException {
        ResponseCollectorDefinition definition;
        int collectorID;
        PreparedStatement statement;
        
        definition = (ResponseCollectorDefinition)collectorDefinition;
        collectorID = createCollectorDefinition(definition);
        
        try {
            statement = connection.prepareStatement(CREATE);
            statement.setInt(1, collectorID);
            statement.executeUpdate();
            statement.close();
            
            return collectorID;
                        
        } catch (SQLException e) {
            logger.error("Problem creating ResponseCollectorDefinition in database for collector " + collectorID);
            throw new DBException("Problem creating ResponseCollectorDefinition in database for collector " + collectorID, e);
        }
    }
    
    /**
     * Retrieves a ResponseCollectorDefinition from the database.
     *
     * @param collectorID    the collector ID of the collector
     * @return               the collector definition created from the database definition
     */
    public ResponseCollectorDefinition retrieve(int collectorID) throws DBException, UnknownHostException {
        String title;
        Device device;
        long pollInterval;
        boolean storing;
        
        title = retrieveTitle(collectorID);
        device = retrieveDevice(collectorID, SNMPAccessType.ReadOnly);
        pollInterval = retrievePollInterval(collectorID);
        storing = retrieveStoring(collectorID);
        
        return new ResponseCollectorDefinition(title, device, pollInterval, storing);
    }
    
    /**
     * Deletes a ResponseCollector definition from the database.
     *
     * @param collectorID      the collector ID of the collector
     */
    public void delete(int collectorID) throws DBException {
        DBUtil.deleteWithIntKey(connection, DELETE, collectorID);
        deleteCollectorDefinition(collectorID);
    }

}
