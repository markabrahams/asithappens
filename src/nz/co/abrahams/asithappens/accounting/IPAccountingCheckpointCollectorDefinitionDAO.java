/*
 * IPAccountingCheckpointCollectorDefinitionDAO.java
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

package nz.co.abrahams.asithappens.accounting;

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
public class IPAccountingCheckpointCollectorDefinitionDAO extends CollectorDefinitionDAO implements DataCollectorDAO {
    
    /** Collector creation SQL statement */
    public static final String CREATE = "INSERT INTO IPAccountingCheckpointCollectors (collectorID) VALUES (?)";
    /** Collector deletion SQL statement */
    public static final String DELETE = "DELETE FROM IPAccountingCheckpointCollectors WHERE collectorID = ?";
    
    /** Creates a new BandwidthCollector DAO */
    public IPAccountingCheckpointCollectorDefinitionDAO(Connection connection) {
        super(connection);
    }
    
    /**
     * Creates an IPAccountingCheckpointCollectorDefinition in the database.
     *
     * @param collectorDefinition  the collector definition to create in the database
     * @return the unique key for the collector
     */
    public int create(CollectorDefinition collectorDefinition) throws DBException {
        IPAccountingCheckpointCollectorDefinition definition;
        int collectorID;
        PreparedStatement statement;
        
        definition = (IPAccountingCheckpointCollectorDefinition)collectorDefinition;
        collectorID = createCollectorDefinition(definition);
        
        try {
            statement = connection.prepareStatement(CREATE);
            statement.setInt(1, collectorID);
            statement.executeUpdate();
            statement.close();
            
            return collectorID;
                        
        } catch (SQLException e) {
            logger.error("Problem creating IPAccountingCheckpointCollectorDefinition in database for session " + collectorID);
            throw new DBException("Problem creating IPAccountingCheckpointCollectorDefinition in database for session " + collectorID, e);
        }
    }
    
    /**
     * Retrieves a IPAccountingCheckpointCollectorDefinition from the database.
     *
     * @param collectorID    the collector ID of the collector
     * @return               the collector definition created from the database definition
     */
    public IPAccountingCheckpointCollectorDefinition retrieve(int collectorID) throws DBException, UnknownHostException {
        String title;
        Device device;
        long pollInterval;
        boolean storing;
        
        title = retrieveTitle(collectorID);
        device = retrieveDevice(collectorID, SNMPAccessType.ReadWrite);
        pollInterval = retrievePollInterval(collectorID);
        storing = retrieveStoring(collectorID);
        
        return new IPAccountingCheckpointCollectorDefinition(title, device, pollInterval, storing);
    }
    
    /**
     * Deletes a IPAccountingCheckpointCollector definition from the database.
     *
     * @param collectorID      the collector ID of the collector
     */
    public void delete(int collectorID) throws DBException {
        DBUtil.deleteWithIntKey(connection, DELETE, collectorID);
        deleteCollectorDefinition(collectorID);
    }

}
