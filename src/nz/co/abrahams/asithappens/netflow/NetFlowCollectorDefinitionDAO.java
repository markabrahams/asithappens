/*
 * NetFlowCollectorDefinitionDAO.java
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

package nz.co.abrahams.asithappens.netflow;

import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import nz.co.abrahams.asithappens.collectors.CollectorDefinition;
import nz.co.abrahams.asithappens.collectors.DataCollectorDAO;
import nz.co.abrahams.asithappens.collectors.InterfaceDirectionCollectorDefinitionDAO;
import nz.co.abrahams.asithappens.core.DAOFactory;
import nz.co.abrahams.asithappens.core.DBException;
import nz.co.abrahams.asithappens.core.DBUtil;
import nz.co.abrahams.asithappens.flow.FlowOptions;
import nz.co.abrahams.asithappens.flow.FlowOptionsDAO;
import nz.co.abrahams.asithappens.snmputil.SNMPAccessType;
import nz.co.abrahams.asithappens.snmputil.SNMPException;
import nz.co.abrahams.asithappens.storage.Device;
import nz.co.abrahams.asithappens.storage.Direction;

/**
 *
 * @author mark
 */
public class NetFlowCollectorDefinitionDAO extends InterfaceDirectionCollectorDefinitionDAO implements DataCollectorDAO {
    
    /** Collector creation SQL statement */
    public static final String CREATE = "INSERT INTO NetFlowCollectors (collectorID, tableSize) VALUES (?,?)";
    public static final String RETRIEVE_TABLE_SIZE = "SELECT tableSize FROM NetFlowCollectors WHERE collectorID = ?";
    /** Collector deletion SQL statement */
    public static final String DELETE = "DELETE FROM NetFlowCollectors WHERE collectorID = ?";
    /** Collector match criteria deletion SQL statement */
    public static final String DELETE_MATCH_CRITERIA = "DELETE FROM NetFlowMatchCriteria WHERE collectorID = ?";
    /** Collector flow options deletion SQL statement */
    public static final String DELETE_FLOW_OPTIONS = "DELETE FROM FlowOptions WHERE collectorID = ?";
    
    /** Creates a new BandwidthCollector DAO */
    public NetFlowCollectorDefinitionDAO(Connection connection) {
        super(connection);
    }
    
    /**
     * Creates an NetFlowCollectorDefinition in the database.
     *
     * @param collectorDefinition  the collector definition to create in the database
     * @return                     the unique key for the collector
     */
    public int create(CollectorDefinition collectorDefinition) throws DBException {
        NetFlowCollectorDefinition definition;
        int collectorID;
        PreparedStatement statement;
        NetFlowMatchCriteriaDAO criteriaDAO;
        FlowOptionsDAO optionsDAO;
        
        definition = (NetFlowCollectorDefinition)collectorDefinition;
        collectorID = createCollectorDefinition(definition);
        super.create(collectorID, definition);
        
        try {
            statement = connection.prepareStatement(CREATE);
            statement.setInt(1, collectorID);
            statement.setInt(2, definition.getTableSize());
            statement.executeUpdate();
            statement.close();
            
            criteriaDAO = DAOFactory.getNetFlowMatchCriteriaDAO(connection);
            criteriaDAO.create(collectorID, definition.getCriteria());
            
            optionsDAO = DAOFactory.getFlowOptionsDAO(connection);
            optionsDAO.create(collectorID, definition.getOptions());
            return collectorID;
                        
        } catch (SQLException e) {
            logger.error("Problem creating NetFlowCollectorDefinition in database for session " + collectorID);
            throw new DBException("Problem creating NetFlowCollectorDefinition in database for session " + collectorID, e);
        }
    }
    
    /**
     * Retrieves a NetFlowCollectorDefinition from the database.
     *
     * @param collectorID    the collector ID of the collector
     * @return               the collector definition created from the database definition
     */
    public NetFlowCollectorDefinition retrieve(int collectorID) throws DBException, UnknownHostException {
        String title;
        Device device;
        long pollInterval;
        boolean storing;
        String ifDescr;
        Direction direction;
        int tableSize;
        NetFlowMatchCriteriaDAO criteriaDAO;
        NetFlowMatchCriteria criteria;
        FlowOptionsDAO optionsDAO;
        FlowOptions options;
        
        title = retrieveTitle(collectorID);
        device = retrieveDevice(collectorID, SNMPAccessType.ReadOnly);
        pollInterval = retrievePollInterval(collectorID);
        storing = retrieveStoring(collectorID);
        ifDescr = retrieveIfDescr(collectorID);
        direction = retrieveDirection(collectorID);
        tableSize = DBUtil.retrieveIntWithPK(connection, RETRIEVE_TABLE_SIZE, collectorID);
        
        criteriaDAO = DAOFactory.getNetFlowMatchCriteriaDAO(connection);
        criteria = criteriaDAO.retrieve(collectorID);
        optionsDAO = DAOFactory.getFlowOptionsDAO(connection);
        options = optionsDAO.retrieve(collectorID);
        
        return new NetFlowCollectorDefinition(title, device, pollInterval, storing,
                ifDescr, direction, criteria, options, tableSize);
    }
    
    /**
     * Deletes a NetFlowCollector definition from the database.
     *
     * @param collectorID      the collector ID of the collector
     */
    @Override
    public void delete(int collectorID) throws DBException {
        DBUtil.deleteWithIntKey(connection, DELETE_MATCH_CRITERIA, collectorID);
        DBUtil.deleteWithIntKey(connection, DELETE_FLOW_OPTIONS, collectorID);
        DBUtil.deleteWithIntKey(connection, DELETE, collectorID);
        super.delete(collectorID);
    }

}
