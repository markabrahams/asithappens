/*
 * CollectorDefinitionDAO.java
 *
 * Created on 3 September 2012, 00:25
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
import java.sql.*;
import nz.co.abrahams.asithappens.core.*;
import nz.co.abrahams.asithappens.snmputil.SNMPAccessType;
import nz.co.abrahams.asithappens.storage.Device;
import nz.co.abrahams.asithappens.storage.DeviceDAO;
import org.apache.log4j.Logger;

/**
 *
 * @author mark
 */
public abstract class CollectorDefinitionDAO {

    public static final String CREATE_COLLECTOR_DEFINITION = "INSERT INTO CollectorDefinitions (collectorType, storing, device, pollInterval, dataType, units) VALUES (?,?,?,?,?,?)";
    public static final String RETRIEVE_TITLE = "SELECT title FROM CollectorDefinitions WHERE collectorID = ?";
    public static final String RETRIEVE_STORING = "SELECT storing FROM CollectorDefinitions WHERE collectorID = ?";
    public static final String RETRIEVE_DEVICE = "SELECT device FROM CollectorDefinitions WHERE collectorID = ?";
    public static final String RETRIEVE_POLLINTERVAL = "SELECT pollInterval FROM CollectorDefinitions WHERE collectorID = ?";
    public static final String RETRIEVE_DATATYPE = "SELECT dataType FROM CollectorDefinitions WHERE collectorID = ?";
    public static final String RETRIEVE_UNITS = "SELECT units FROM CollectorDefinitions WHERE collectorID = ?";
    public static final String DELETE_COLLECTOR_DEFINITION = "DELETE FROM CollectorDefinitions WHERE collectorID = ?";
    /**
     * Logging provider
     */
    public static Logger logger = Logger.getLogger(CollectorDefinitionDAO.class);
    /**
     * Database connection
     */
    protected Connection connection;

    /**
     * Creates a new instance of DataSetsDAO
     */
    public CollectorDefinitionDAO(Connection connection) {
        this.connection = connection;
    }

    public abstract int create(CollectorDefinition definition) throws DBException;
    public abstract CollectorDefinition retrieve(int collectorID) throws DBException, UnknownHostException;
    public abstract void delete(int collectorID) throws DBException;
    
    /**
     * Adds a new collector definition to the "collectordefinitions" table.
     *
     * @param definition the CollectorDefinition object containing the collector
     * information
     * @return the unique key created for the new session
     */
    public int createCollectorDefinition(CollectorDefinition definition) throws DBException {
        PreparedStatement statement;
        ResultSet results;
        DeviceDAO deviceDAO;
        int collectorID;

        try {
            deviceDAO = DAOFactory.getDeviceDAO(connection);
            deviceDAO.create(definition.getDevice());
            statement = connection.prepareStatement(CREATE_COLLECTOR_DEFINITION, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, CollectorDefinitionDAOType.getDAOID(definition.getClass()));
            statement.setInt(2, definition.getStoring() ? 1 : 0);
            statement.setString(3, definition.getDevice().getName());
            statement.setLong(4, definition.getPollInterval());
            statement.setInt(5, definition.getDataType().id);
            statement.setString(6, definition.getUnits());
            statement.executeUpdate();

            results = statement.getGeneratedKeys();
            results.next();
            collectorID = results.getInt(1);
            results.close();
            statement.close();
            logger.debug("Adding new collector with ID " + collectorID);
            return collectorID;
        } catch (SQLException e) {
            logger.error("Failed to add new collector to database");
            throw new DBException("Failed to add new collector to database", e);
        } catch (DAOCreationException e) {
            logger.error("Failed to add new session to database");
            throw new DBException("Failed to add new session to database", e);            
        }
    }

    /**
     * Gets the title for the collector.
     *
     * @param collectorID the unique key for the collector
     * @return the title for the collector
     */
    public String retrieveTitle(int collectorID) throws DBException {
        return DBUtil.retrieveStringWithPK(connection, RETRIEVE_TITLE, collectorID);
    }

    /**
     * Gets the storing flag for the collector.
     *
     * @param collectorID the unique key for the collector
     * @return true if the collector will store data in the database
     */
    public boolean retrieveStoring(int collectorID) throws DBException {
        return DBUtil.retrieveBooleanWithPK(connection, RETRIEVE_STORING, collectorID);
    }

    public Device retrieveDevice(int collectorID, SNMPAccessType authType) throws DBException, UnknownHostException {
        DeviceDAO deviceDAO;
        String name;
        Device device;

        name = DBUtil.retrieveStringWithPK(connection, RETRIEVE_DEVICE, collectorID);
        deviceDAO = DAOFactory.getDeviceDAO();
        device = deviceDAO.retrieveDevice(name, authType);
        deviceDAO.closeConnection();

        return device;
    }

    /**
     * Gets the polling interval for a collector.
     *
     * @param collectorID the unique key for the collector
     * @return the polling interval for the collector
     */
    public long retrievePollInterval(int collectorID) throws DBException {
        return DBUtil.retrieveLongWithPK(connection, RETRIEVE_POLLINTERVAL, collectorID);
    }

    /**
     * Gets the data type collected by the collector.
     *
     * @param collectorID the unique key for the session
     * @return the type of data collected
     */
    public DataType retrieveDataType(int collectorID) throws DBException {
        return DataType.getType(DBUtil.retrieveIntWithPK(connection, RETRIEVE_DATATYPE, collectorID));
    }

    /**
     * Gets the units for the values collected.
     *
     * @param collectorID the unique key for the collector
     * @return the units for the collected values
     */
    public String retrieveUnits(int collectorID) throws DBException {
        return DBUtil.retrieveStringWithPK(connection, RETRIEVE_UNITS, collectorID);
    }

    /**
     * Deletes a collector from the "collectordefinitions" table.
     *
     * @param collectorID the unique key for the collector to delete
     */
    public void deleteCollectorDefinition(int collectorID) throws DBException {
        PreparedStatement statement;

        try {
            statement = connection.prepareStatement(DELETE_COLLECTOR_DEFINITION);
            statement.setInt(1, collectorID);
            statement.executeUpdate();
            statement.close();
            logger.debug("Deleting collector with ID " + collectorID);
        } catch (SQLException e) {
            logger.error("Failed to delete collector with ID " + collectorID + " from database");
            throw new DBException("Failed to delete collector with ID " + collectorID + " from database", e);
        }
    }

    /**
     * Closes database connection opened for this DAO.
     */
    public void closeConnection() throws DBException {
        try {
            connection.close();
        } catch (SQLException e) {
            logger.error("Error closing database connection for CollectorDefinition DAO");
            throw new DBException("Error closing database connection for CollectorDefinition DAO", e);
        }
    }
}
