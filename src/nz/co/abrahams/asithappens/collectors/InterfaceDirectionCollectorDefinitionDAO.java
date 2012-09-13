/*
 * InterfaceDirectionCollectorDefinitionDAO.java
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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import nz.co.abrahams.asithappens.core.DBException;
import nz.co.abrahams.asithappens.core.DBUtil;
import nz.co.abrahams.asithappens.storage.Direction;

/**
 *
 * @author mark
 */
public abstract class InterfaceDirectionCollectorDefinitionDAO extends CollectorDefinitionDAO {

    public static final String CREATE_INTERFACE_DIRECTION = "INSERT INTO InterfaceDirectionCollectorDefinitions (collectorID, ifDescr, direction) VALUES (?,?,?)";
    public static final String RETRIEVE_IFDESCR = "SELECT ifDescr FROM InterfaceDirectionCollectorDefinitions WHERE collectorID = ?";
    public static final String RETRIEVE_DIRECTION = "SELECT direction FROM InterfaceDirectionCollectorDefinitions WHERE collectorID = ?";
    public static final String DELETE_INTERFACE_DIRECTION = "DELETE FROM InterfaceDirectionCollectorDefinitions WHERE collectorID = ?";
    
    /**
     * Creates a new InterfaceDirectionCollectorDefinitionDAO
     */
    public InterfaceDirectionCollectorDefinitionDAO(Connection connection) {
        super(connection);
    }

    /**
     * Adds a new collector definition with interface and direction.
     *
     * @param collectorID the unique key in the CollectorDefinitions table
     * @param definition the object containing the collector information
     */
    public void create(int collectorID, InterfaceDirectionCollectorDefinition definition) throws DBException {
        PreparedStatement statement;

        try {
            statement = connection.prepareStatement(CREATE_INTERFACE_DIRECTION);
            statement.setInt(1, collectorID);
            statement.setString(2, definition.getIfDescr());
            statement.setInt(3, definition.getDirection().getOIDIndex());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            logger.error("Failed to create new InterfaceDirectionCollectorDefinition DAO");
            throw new DBException("Failed to create new InterfaceDirectionCollectorDefinition DAO", e);
        }
    }

    /**
     * Gets the interface for the collector.
     *
     * @param collectorID the unique key for the collector
     * @return the interface the collector is collecting from
     */
    public String retrieveIfDescr(int collectorID) throws DBException {
        return DBUtil.retrieveStringWithPK(connection, RETRIEVE_IFDESCR, collectorID);
    }

    /**
     * Gets the polling interval for a collector.
     *
     * @param collectorID the unique key for the collector
     * @return the polling interval for the collector
     */
    public Direction retrieveDirection(int collectorID) throws DBException {
        return Direction.getDirectionFromOIDIndex(DBUtil.retrieveIntWithPK(connection, RETRIEVE_DIRECTION, collectorID));
    }

    /**
     * Deletes a collector from the InterfaceDirectionCollectorDefinitions table.
     *
     * @param collectorID the unique key for the collector to delete
     */
    public void delete(int collectorID) throws DBException {
        PreparedStatement statement;

        try {
            statement = connection.prepareStatement(DELETE_INTERFACE_DIRECTION);
            statement.setInt(1, collectorID);
            statement.executeUpdate();
            statement.close();
            deleteCollectorDefinition(collectorID);
            logger.debug("Deleting collector with ID " + collectorID);
        } catch (SQLException e) {
            logger.error("Failed to delete collector with ID " + collectorID + " from database");
            throw new DBException("Failed to delete collector with ID " + collectorID + " from database", e);
        }
    }

}
