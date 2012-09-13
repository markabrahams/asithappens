/*
 * SNMPTableCollectorDefinitionDAO.java
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

package nz.co.abrahams.asithappens.collectors;

import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import nz.co.abrahams.asithappens.accounting.IPAccountingActiveSNMP;
import nz.co.abrahams.asithappens.core.DBException;
import nz.co.abrahams.asithappens.core.DBUtil;
import nz.co.abrahams.asithappens.core.DataType;
import nz.co.abrahams.asithappens.snmputil.SNMPAccessType;
import nz.co.abrahams.asithappens.snmputil.SNMPException;
import nz.co.abrahams.asithappens.snmputil.SNMPType;
import nz.co.abrahams.asithappens.storage.Device;

/**
 *
 * @author mark
 */
public class SNMPTableCollectorDefinitionDAO extends CollectorDefinitionDAO implements DataCollectorDAO {
    
    /** Collector creation SQL statement */
    public static final String CREATE = "INSERT INTO SNMPTableCollectors (collectorID, dataType, snmpType) VALUES (?,?,?)";
    public static final String RETRIEVE_DATA_TYPE = "SELECT dataType FROM SNMPTableCollectors WHERE collectorID = ?";
    public static final String RETRIEVE_SNMP_TYPE = "SELECT snmpType FROM SNMPTableCollectors WHERE collectorID = ?";
    /** Collector deletion SQL statement */
    public static final String DELETE = "DELETE FROM SNMPTableCollectors WHERE collectorID = ?";
    
    /** Creates a new SNMPTableCollectorDefinition DAO */
    public SNMPTableCollectorDefinitionDAO(Connection connection) {
        super(connection);
    }
    
    /**
     * Creates an SNMPTableCollectorDefinition in the database.
     *
     * @param collectorDefinition  the collector definition to create in the database
     * @return                     the unique key for the collector
     */
    public int create(CollectorDefinition collectorDefinition) throws DBException {
        SNMPTableCollectorDefinition definition;
        int collectorID;
        PreparedStatement statement;
        
        definition = (SNMPTableCollectorDefinition)collectorDefinition;
        collectorID = createCollectorDefinition(definition);
        
        try {
            statement = connection.prepareStatement(CREATE);
            statement.setInt(1, collectorID);
            statement.setInt(2, definition.getDataType().id);
            statement.setInt(3, definition.getSNMPType().ordinal());
            statement.executeUpdate();
            statement.close();
            
            return collectorID;
                        
        } catch (SQLException e) {
            logger.error("Problem creating SNMPTableCollectorDefinition in database for session " + collectorID);
            throw new DBException("Problem creating SNMPTableCollectorDefinition in database for session " + collectorID, e);
        }
    }
    
    /**
     * Retrieves a SNMPTableCollectorDefinition from the database.
     *
     * @param collectorID    the collector ID of the collector
     * @return               the collector definition created from the database definition
     */
    public SNMPTableCollectorDefinition retrieve(int collectorID) throws DBException, UnknownHostException {
        String title;
        Device device;
        long pollInterval;
        boolean storing;
        DataType dataType;
        SNMPType snmpType;
        IPAccountingActiveSNMP snmpInterface;
        
        title = retrieveTitle(collectorID);
        device = retrieveDevice(collectorID, SNMPAccessType.ReadOnly);
        pollInterval = retrievePollInterval(collectorID);
        storing = retrieveStoring(collectorID);
        
        dataType = DataType.getType(DBUtil.retrieveIntWithPK(connection, RETRIEVE_DATA_TYPE, collectorID));
        snmpType = SNMPType.values()[DBUtil.retrieveIntWithPK(connection, RETRIEVE_SNMP_TYPE, collectorID)];
        
        snmpInterface = null;
        return new SNMPTableCollectorDefinition(title, device, pollInterval, storing, dataType, snmpInterface, snmpType);
    }
    
    /**
     * Deletes a SNMPTableCollector definition from the database.
     *
     * @param collectorID      the collector ID of the collector
     */
    public void delete(int collectorID) throws DBException {
        DBUtil.deleteWithIntKey(connection, DELETE, collectorID);
        deleteCollectorDefinition(collectorID);
    }

}
