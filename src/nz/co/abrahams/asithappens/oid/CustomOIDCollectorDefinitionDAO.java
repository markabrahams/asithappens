/*
 * CustomOIDCollectorDefinitionDAO.java
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
package nz.co.abrahams.asithappens.oid;

import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;
import nz.co.abrahams.asithappens.collectors.CollectorDefinition;
import nz.co.abrahams.asithappens.collectors.CollectorDefinitionDAO;
import nz.co.abrahams.asithappens.collectors.DataCollectorDAO;
import nz.co.abrahams.asithappens.core.DAOFactory;
import nz.co.abrahams.asithappens.core.DBException;
import nz.co.abrahams.asithappens.core.DBUtil;
import nz.co.abrahams.asithappens.snmputil.SNMPAccessType;
import nz.co.abrahams.asithappens.snmputil.SNMPException;
import nz.co.abrahams.asithappens.storage.Device;

/**
 *
 * @author mark
 */
public class CustomOIDCollectorDefinitionDAO extends CollectorDefinitionDAO implements DataCollectorDAO {

    /**
     * Collector creation SQL statement
     */
    public static final String CREATE = "INSERT INTO CustomOIDCollectors (collectorID, valueUnits) VALUES (?,?)";
    /**
     * Collector port creation SQL statement
     */
    public static final String CREATE_COLLECTOR_OID = "INSERT INTO CustomOIDCollectorOIDs (collectorID, position, oidID) VALUES (?,?,?)";
    /**
     * Value units retrieval SQL statement
     */
    public static final String RETRIEVE_VALUE_UNITS = "SELECT valueUnits FROM CustomOIDCollectors WHERE collectorID = ?";
    /**
     * OID list retrieval SQL statement
     */
    public static final String RETRIEVE_COLLECTOR_OIDS = "SELECT oidID FROM CustomOIDCollectorOIDs WHERE collectorID = ? ORDER BY position";
    /**
     * Collector deletion SQL statement
     */
    public static final String DELETE = "DELETE FROM CustomOIDCollectors WHERE collectorID = ?";
    /**
     * Collector deletion SQL statement
     */
    public static final String DELETE_COLLECTOR_OIDS = "DELETE FROM CustomOIDCollectorOIDs WHERE collectorID = ?";

    /**
     * Creates a new CustomOIDCollectorDefinition DAO
     */
    public CustomOIDCollectorDefinitionDAO(Connection connection) {
        super(connection);
    }

    /**
     * Creates an CustomOIDCollectorDefinition in the database.
     *
     * @param collectorDefinition the collector definition to create in the
     * database
     * @return the unique key for the collector
     */
    public int create(CollectorDefinition collectorDefinition) throws DBException {
        CustomOIDCollectorDefinition definition;
        int collectorID;
        int[] oidIDs;
        CustomOIDDAO customOIDDAO;
        PreparedStatement statement;

        definition = (CustomOIDCollectorDefinition) collectorDefinition;
        collectorID = createCollectorDefinition(definition);
        oidIDs = new int[definition.getOIDs().size()];

        try {
            statement = connection.prepareStatement(CREATE);
            statement.setInt(1, collectorID);
            statement.setString(2, definition.getUnits());
            statement.executeUpdate();
            statement.close();

            customOIDDAO = DAOFactory.getCustomOIDDAO(connection);
            for (int i = 0; i < definition.getOIDs().size(); i++) {
                oidIDs[i] = customOIDDAO.create(definition.getOIDs().elementAt(i));
            }
            for (int i = 0; i < definition.getOIDs().size(); i++) {
                statement = connection.prepareStatement(CREATE_COLLECTOR_OID);
                statement.setInt(1, collectorID);
                statement.setInt(2, i);
                statement.setInt(3, oidIDs[i]);
                statement.executeUpdate();
                statement.close();
            }
            return collectorID;

        } catch (SQLException e) {
            logger.error("Problem creating CustomOIDCollectorDefinition in database for collector " + collectorID);
            throw new DBException("Problem creating CustomOIDCollectorDefinition in database for collector " + collectorID, e);
        }
    }

    /**
     * Retrieves a CustomOIDCollectorDefinition from the database.
     *
     * @param collectorID the collector ID of the collector
     * @return the collector definition created from the database definition
     */
    public CustomOIDCollectorDefinition retrieve(int collectorID) throws DBException, UnknownHostException {
        String title;
        Device device;
        long pollInterval;
        boolean storing;
        CustomOIDDAO customOIDDAO;
        String valueUnits;
        Vector<Integer> oidIDs;
        Vector<CustomOID> customOIDs;
        PreparedStatement statement;
        ResultSet results;

        title = retrieveTitle(collectorID);
        device = retrieveDevice(collectorID, SNMPAccessType.ReadOnly);
        pollInterval = retrievePollInterval(collectorID);
        storing = retrieveStoring(collectorID);
        valueUnits = DBUtil.retrieveStringWithPK(connection, RETRIEVE_VALUE_UNITS, collectorID);
        oidIDs = new Vector<Integer>();
        customOIDs = new Vector<CustomOID>();

        try {
            statement = connection.prepareStatement(RETRIEVE_COLLECTOR_OIDS, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            statement.setInt(1, collectorID);
            results = statement.executeQuery();
            while (results.next()) {
                oidIDs.add(new Integer(results.getInt("oidID")));
                logger.debug("oidID: " + results.getInt("oidID"));
            }
            results.close();
            statement.close();
            customOIDDAO = DAOFactory.getCustomOIDDAO(connection);
            for (int i = 0; i < oidIDs.size(); i++) {
                customOIDs.add(customOIDDAO.retrieve(oidIDs.elementAt(i).intValue()));
            }
            return new CustomOIDCollectorDefinition(title, device, pollInterval, storing, customOIDs, valueUnits);
        } catch (SQLException e) {
            logger.error("Problem retrieving custom OIDs for collector " + collectorID);
            throw new DBException("Problem retrieving custom OIDs for collector " + collectorID, e);
        }

    }

    public Vector<Integer> retrieveCollectorOIDIDs(int collectorID) throws DBException {
        PreparedStatement statement;
        ResultSet results;
        Vector<Integer> oidIDs;

        oidIDs = new Vector<Integer>();
        try {
            statement = connection.prepareStatement(RETRIEVE_COLLECTOR_OIDS, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            statement.setInt(1, collectorID);
            results = statement.executeQuery();
            while (results.next()) {
                oidIDs.add(new Integer(results.getInt("oidID")));
                logger.debug("oidID: " + results.getInt("oidID"));
            }
            results.close();
            statement.close();
            return oidIDs;
        } catch (SQLException e) {
            throw new DBException("Error retrieving OIDs for collector " + collectorID);
        }
    }

    /**
     * Deletes a Collector definition from the database.
     *
     * @param collectorID the collector ID of the collector
     */
    public void delete(int collectorID) throws DBException {
        Vector<Integer> oidIDs;
        CustomOIDDAO customOIDDAO;

        oidIDs = retrieveCollectorOIDIDs(collectorID);
        DBUtil.deleteWithIntKey(connection, DELETE_COLLECTOR_OIDS, collectorID);
        customOIDDAO = DAOFactory.getCustomOIDDAO(connection);
        for (int i = 0; i < oidIDs.size(); i++) {
            customOIDDAO.delete(oidIDs.elementAt(i));
        }
        DBUtil.deleteWithIntKey(connection, DELETE, collectorID);
    }
}
