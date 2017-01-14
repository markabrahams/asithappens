/*
 * DataSetsDAO.java
 *
 * Created on 22 June 2008, 16:25
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
package nz.co.abrahams.asithappens.storage;

import java.net.UnknownHostException;
import java.sql.*;
import java.util.Vector;
import nz.co.abrahams.asithappens.collectors.*;
import nz.co.abrahams.asithappens.core.DAOCreationException;
import nz.co.abrahams.asithappens.core.DAOFactory;
import nz.co.abrahams.asithappens.core.DBException;
import nz.co.abrahams.asithappens.core.DBUtil;
import nz.co.abrahams.asithappens.snmputil.SNMPException;
import org.apache.log4j.Logger;

/**
 *
 * @author mark
 */
public class DataSetsDAO {

    public static final String CREATE_SESSION = "INSERT INTO Sessions (dataTypeID, collectorID, startTime, finishTime, collecting, title) VALUES (?,?,?,?,?,?)";
    public static final String CREATE_TEMPLATE = "INSERT INTO Sessions (dataTypeID, collectorID, collecting, title) VALUES (?,?,?,?)";
    public static final String DELETE_SESSION = "DELETE FROM Sessions WHERE sessionID = ?";
    public static final String DELETE_SESSION_LABELS = "DELETE FROM Labels WHERE sessionID = ?";
    public static final String DELETE_SESSION_HEADINGS = "DELETE FROM Headings WHERE sessionID = ?";
    public static final String DELETE_SESSION_DATA = "DELETE FROM Data WHERE sessionID = ?";
    public static final String UPDATE_SESSION = "UPDATE Sessions SET startTime = ?, finishTime = ?, title = ?, collecting = ? WHERE sessionID = ?";
    public static final String UPDATE_SESSION_ATTRIBUTE = "UPDATE Sessions SET ? = ? WHERE sessionID = ?";
    public static final String UPDATE_SESSION_TITLE = "UPDATE Sessions SET title = ? WHERE sessionID = ?";
    public static final String UPDATE_SESSION_STARTTIME = "UPDATE Sessions SET startTime = ? WHERE sessionID = ?";
    public static final String UPDATE_SESSION_FINISHTIME = "UPDATE Sessions SET finishTime = ? WHERE sessionID = ?";
    public static final String UPDATE_SESSION_COLLECTING = "UPDATE Sessions SET collecting = ? WHERE sessionID = ?";
    public static final String RETRIEVE_SESSION_EXISTS = "SELECT sessionID FROM Sessions WHERE sessionID = ?";
    public static final String RETRIEVE_SESSION_COLLECTORID = "SELECT collectorID FROM Sessions WHERE sessionID = ?";
    public static final String RETRIEVE_SESSION_DATATYPEID = "SELECT dataTypeID FROM Sessions WHERE sessionID = ?";
    public static final String RETRIEVE_SESSION_DEVICE = "SELECT device FROM Sessions WHERE sessionID = ?";
    public static final String RETRIEVE_SESSION_POLLINTERVAL = "SELECT pollInterval FROM Sessions WHERE sessionID = ?";
    public static final String RETRIEVE_SESSION_PORT = "SELECT port FROM Sessions WHERE sessionID = ?";
    public static final String RETRIEVE_SESSION_STARTTIME = "SELECT startTime FROM Sessions WHERE sessionID = ?";
    public static final String RETRIEVE_SESSION_FINISHTIME = "SELECT finishTime FROM Sessions WHERE sessionID = ?";
    public static final String RETRIEVE_SESSION_COLLECTING = "SELECT collecting FROM Sessions WHERE sessionID = ?";
    public static final String RETRIEVE_SESSION_TITLE = "SELECT title FROM Sessions WHERE sessionID = ?";
    public static final String RETRIEVE_SESSION_DIRECTION = "SELECT direction FROM Sessions WHERE sessionID = ?";
    //public static final String RETRIEVE_SESSION_IDS = "SELECT sessionID FROM Sessions WHERE userVisible = \'1\' ORDER BY startTime";
    public static final String RETRIEVE_SESSION_IDS = "SELECT sessionID FROM Sessions ORDER BY startTime";
    public static final String RETRIEVE_SESSION_POINT_COUNT = "SELECT COUNT(*) FROM Data WHERE sessionID = ? AND position = ? AND time >= ? AND time <= ?";
    public static final String RETRIEVE_TEMPLATE = "SELECT dataTypeID, collectorID, collecting, title FROM Sessions WHERE sessionID = ?";
    public static final String RETRIEVE_PROCESSOR_TYPE = "SELECT collectorType FROM ProcessorCollectors WHERE sessionID = ?";
    public static final String RETRIEVE_MEMORY_TYPE = "SELECT collectorType FROM MemoryCollectors WHERE sessionID = ?";
    /**
     * Logging provider
     */
    public static Logger logger = Logger.getLogger(DataSetsDAO.class);
    /**
     * Database connection
     */
    Connection connection;

    /**
     * Creates a new instance of DataSetsDAO
     */
    public DataSetsDAO(Connection connection) {
        this.connection = connection;
    }
    
    public Connection getConnection() {
        return connection;
    }

    /**
     * Adds a new session to the "sessions" table. This is typically called to
     * initialize a session with a view to storing subsequently collected data
     * in the "data" table. The method makes an assumption that data collection
     * will start immediately after the session is created, and therefore sets
     * the "collecting" field in the session table to true.
     *
     * @param dataSets the DataSets object containing the session information
     * @return the unique key created for the new session
     */
    public int createSession(DataSets dataSets) throws DBException {
        PreparedStatement statement;
        ResultSet results;
        int sessionID;
        CollectorDefinitionDAO definitionDAO;
        int collectorID;

        try {
            definitionDAO = DAOFactory.getCollectorDefinitionDAO(connection, dataSets.getCollector().getDefinition());
            collectorID = definitionDAO.create(dataSets.getCollector().getDefinition());
            statement = connection.prepareStatement(CREATE_SESSION, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, dataSets.getDataType().id);
            statement.setInt(2, collectorID);
            statement.setLong(3, 0);
            statement.setLong(4, 0);
            statement.setInt(5, 1);
            statement.setString(6, dataSets.getTitle());
            statement.executeUpdate();

            results = statement.getGeneratedKeys();
            results.next();
            sessionID = results.getInt(1);
            results.close();
            statement.close();
            logger.debug("Adding new session with ID " + sessionID);
            return sessionID;
        } catch (SQLException e) {
            logger.error("Failed to add new session to database");
            throw new DBException("Failed to add new session to database", e);
        } catch (DAOCreationException e) {
            logger.error("Failed to add new session to database");
            throw new DBException("Failed to add new session to database", e);            
        }
    }

    /**
     * Deletes a session from the "sessions" table. This also deletes the
     * following: <ul> <li> the data for that session from the "data" table <li>
     * the headings for that session from the "headings" table <li> the labels
     * for that session from the "labels" table </ul>
     *
     * @param sessionID the unique key for the session to delete
     */
    public void deleteSession(int sessionID) throws DBException {
        PreparedStatement statement;
        //ResultSet results;
        int collectorID;
        DataCollectorDAO definitionDAO;

        try {
            connection.setAutoCommit(false);
            statement = connection.prepareStatement(DELETE_SESSION_DATA);
            statement.setInt(1, sessionID);
            statement.executeUpdate();
            statement.close();
            statement = connection.prepareStatement(DELETE_SESSION_LABELS);
            statement.setInt(1, sessionID);
            statement.executeUpdate();
            statement.close();
            statement = connection.prepareStatement(DELETE_SESSION_HEADINGS);
            statement.setInt(1, sessionID);
            statement.executeUpdate();
            statement.close();
            
            // Delete collector definition
            //statement = connection.prepareStatement(RETRIEVE_SESSION_COLLECTORID);
            //statement.setInt(1, sessionID);
            //results = statement.executeQuery();
            //collectorID = results.getInt(1);
            //results.close();
            //statement.close();
            collectorID = DBUtil.retrieveIntWithPK(connection, RETRIEVE_SESSION_COLLECTORID, sessionID);
            definitionDAO = (DataCollectorDAO) DAOFactory.getCollectorDefinitionDAO(connection, sessionID);
            definitionDAO.delete(collectorID);
            
            // Delete session
            statement = connection.prepareStatement(DELETE_SESSION);
            statement.setInt(1, sessionID);
            statement.executeUpdate();
            connection.commit();
            connection.setAutoCommit(true);
            statement.close();
            logger.debug("Deleting session with ID " + sessionID);
        } catch (SQLException e) {
            logger.error("Failed to delete session with ID " + sessionID + " from database");
            throw new DBException("Failed to delete session with ID " + sessionID + " from database", e);
        } catch (DAOCreationException e) {
            logger.error("Failed to delete session with ID " + sessionID + " from database");
            throw new DBException("Failed to delete session with ID " + sessionID + " from database", e);
        }
    }
    //public void updateSessionAttribute(int sessionID, String columnName, String value)

    /**
     * Updates session information in the database.
     *
     * @param dataSets the source of the data to use to update the database
     */
    public void updateSession(DataSets dataSets) throws DBException {
        PreparedStatement statement;

        try {
            statement = connection.prepareStatement(UPDATE_SESSION);
            statement.setLong(1, dataSets.getStartTime());
            statement.setLong(2, dataSets.getFinishTime());
            statement.setString(3, dataSets.getTitle());
            statement.setInt(4, dataSets.isCollecting() ? 1 : 0);
            statement.setInt(5, dataSets.getSessionID());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            logger.error("Failed to update session with ID " + dataSets.getSessionID() + " in database");
            throw new DBException("Failed to update session with ID " + dataSets.getSessionID() + " in database", e);
        }
    }

    /**
     * Sets an attribute of a session in the "sessions" table.
     *
     * @param sessionID the unique key for the session
     * @param columnName the name of the attribute to set
     * @param value the new value for the attribute
     */
    private void updateSessionAttribute(int sessionID, String columnName, String value) throws DBException {
        PreparedStatement statement;

        try {
            statement = connection.prepareStatement(UPDATE_SESSION_ATTRIBUTE);
            statement.setString(1, columnName);
            statement.setString(2, value);
            statement.setInt(3, sessionID);
            if (statement.executeUpdate() == 0) {
                logger.error("Cannot find session with ID " + sessionID + " to update " + columnName);
                throw new DBException("Cannot find session with ID " + sessionID + " to update " + columnName);
            }
            statement.close();
        } catch (SQLException e) {
            logger.error("Cannot update session with ID " + sessionID + " to update " + columnName);
            throw new DBException("Cannot update session with ID " + sessionID + " to update " + columnName, e);
        }
    }

    /**
     * Updates the title for a database session.
     *
     * @param sessionID the unique key for the session
     * @param title the new title for the session
     */
    public void updateSessionTitle(int sessionID, String title) throws DBException {
        PreparedStatement statement;

        try {
            statement = connection.prepareStatement(UPDATE_SESSION_TITLE);
            statement.setString(1, title);
            statement.setInt(2, sessionID);
            if (statement.executeUpdate() == 0) {
                logger.error("Cannot find session with ID " + sessionID + " to update title");
                throw new DBException("Cannot find session with ID " + sessionID + " to update title");
            }
            statement.close();
            logger.debug("Updating title for session with ID " + sessionID + " to " + "'" + title + "'");
        } catch (SQLException e) {
            logger.error("Cannot update session with ID " + sessionID + " to update title");
            throw new DBException("Cannot update session with ID " + sessionID + " to update title", e);
        }
    }

    /**
     * Updates the start time for a database session.
     *
     * @param sessionID the unique key for the session
     * @param title the new start time for the session
     */
    public void updateSessionStartTime(int sessionID, long time) throws DBException {
        PreparedStatement statement;

        try {
            statement = connection.prepareStatement(UPDATE_SESSION_STARTTIME);
            statement.setLong(1, time);
            statement.setInt(2, sessionID);
            if (statement.executeUpdate() == 0) {
                logger.error("Cannot find session with ID " + sessionID + " to update start time");
                throw new DBException("Cannot find session with ID " + sessionID + " to update start time");
            }
            statement.close();
            logger.debug("Updating start time for session with ID " + sessionID + " to " + "'" + time + "'");
        } catch (SQLException e) {
            logger.error("Cannot update session with ID " + sessionID + " to update start time");
            throw new DBException("Cannot update session with ID " + sessionID + " to update start time", e);
        }
    }

    /**
     * Updates the finish time for a database session.
     *
     * @param sessionID the unique key for the session
     * @param title the new finish time for the session
     */
    public void updateSessionFinishTime(int sessionID, long time) throws DBException {
        PreparedStatement statement;

        try {
            statement = connection.prepareStatement(UPDATE_SESSION_FINISHTIME);
            statement.setLong(1, time);
            statement.setInt(2, sessionID);
            if (statement.executeUpdate() == 0) {
                logger.error("Cannot find session with ID " + sessionID + " to update finish time");
                throw new DBException("Cannot find session with ID " + sessionID + " to update finish time");
            }
            statement.close();
            logger.debug("Updating finish time for session with ID " + sessionID + " to " + "'" + time + "'");
        } catch (SQLException e) {
            logger.error("Cannot update session with ID " + sessionID + " to update finish time");
            throw new DBException("Cannot update session with ID " + sessionID + " to update finish time", e);
        }
    }

    /**
     * Updates the finish time for a database session.
     *
     * @param sessionID the unique key for the session
     * @param title the new finish time for the session
     */
    public void updateSessionCollectingState(int sessionID, boolean state) throws DBException {
        PreparedStatement statement;

        try {
            statement = connection.prepareStatement(UPDATE_SESSION_COLLECTING);
            statement.setInt(1, state ? 1 : 0);
            statement.setInt(2, sessionID);
            if (statement.executeUpdate() == 0) {
                logger.error("Cannot find session with ID " + sessionID + " to update collecting state");
                throw new DBException("Cannot find session with ID " + sessionID + " to update collecting state");
            }
            statement.close();
            logger.debug("Updating collecting state for session with ID " + sessionID + " to " + "'" + state + "'");
        } catch (SQLException e) {
            logger.error("Cannot update session with ID " + sessionID + " to update collecting state");
            throw new DBException("Cannot update session with ID " + sessionID + " to update collecting state", e);
        }
    }

    /**
     * Checks for the existence of a session in the "sessions" table.
     *
     * @param sessionID the unique key for the session to check
     */
    public boolean retrieveSessionExists(int sessionID) {

        try {
            DBUtil.retrieveSingleAttributeWithPK(connection, RETRIEVE_SESSION_EXISTS, sessionID);
            return true;
        } catch (DBException e) {
            return false;
        }
    }

    /**
     * Retrieves session information
     */
    public void retrieveSession(int sessionID) {
        // TODO
    }

    /**
     * Gets the data type ID for a session.
     *
     * @param sessionID the unique key for the session
     * @return the data type ID corresponding to the session's type
     */
    public int retrieveSessionDataTypeID(int sessionID) throws DBException {
        return ((Integer) (DBUtil.retrieveSingleAttributeWithPK(connection, RETRIEVE_SESSION_DATATYPEID, sessionID))).intValue();
    }

    /**
     * Gets the device target for a session.
     *
     * @param sessionID the unique key for the session
     * @return the device as stored in the "sessions" table (name or IP address)
     */
    public String retrieveSessionDevice(int sessionID) throws DBException {
        return ((String) (DBUtil.retrieveSingleAttributeWithPK(connection, RETRIEVE_SESSION_DEVICE, sessionID)));
    }

    /**
     * Gets the polling interval for a session.
     *
     * @param sessionID the unique key for the session
     * @return the polling interval in milliseconds
     */
    public long retrieveSessionPollInterval(int sessionID) throws DBException {
        return ((Long) (DBUtil.retrieveSingleAttributeWithPK(connection, RETRIEVE_SESSION_POLLINTERVAL, sessionID))).longValue();
    }

    /**
     * Gets the device port for a session. This will be null if a port is not
     * relevant to the data type being collected e.g. the "Response" data type.
     *
     * @param sessionID the unique key for the session
     * @return the name of the port on the device
     */
    public String retrieveSessionPort(int sessionID) throws DBException {
        return ((String) (DBUtil.retrieveSingleAttributeWithPK(connection, RETRIEVE_SESSION_PORT, sessionID)));
    }

    /**
     * Gets the starting date for a session. The date is expressed as a "long"
     * representing the number of milliseconds since midnight, Jan 1, 1970.
     *
     * @param sessionID the unique key for the session
     * @return the date that the data collection for the session began
     */
    public long retrieveSessionStartTime(int sessionID) throws DBException {
        return ((Long) (DBUtil.retrieveSingleAttributeWithPK(connection, RETRIEVE_SESSION_STARTTIME, sessionID))).longValue();
    }

    /**
     * Gets the finishing date for a session. The date is expressed as a "long"
     * representing the number of milliseconds since midnight, Jan 1, 1970.
     *
     * @param sessionID the unique key for the session
     * @return the date that the data collection for the session finished
     */
    public long retrieveSessionFinishTime(int sessionID) throws DBException {
        return ((Long) (DBUtil.retrieveSingleAttributeWithPK(connection, RETRIEVE_SESSION_FINISHTIME, sessionID))).longValue();
    }

    /**
     * Gets the collecting state for a session.
     *
     * @param sessionID the unique key for the session
     * @return true if data is currently being collected for the session,
     * otherwise false
     */
    public boolean retrieveSessionCollectingState(int sessionID) throws DBException {
        return ((Byte) (DBUtil.retrieveSingleAttributeWithPK(connection, RETRIEVE_SESSION_COLLECTING, sessionID))) == 1;
    }

    /**
     * Gets the user-supplied title for a session.
     *
     * @param sessionID the unique key for the session
     * @return the name of the title of the session
     */
    public String retrieveSessionTitle(int sessionID) throws DBException {
        return ((String) (DBUtil.retrieveSingleAttributeWithPK(connection, RETRIEVE_SESSION_TITLE, sessionID)));
    }

    /**
     * Gets the collector DAO ID for a session.
     *
     * @param sessionID the unique key for the session
     * @return the DAO ID for collection
     */
    /*
    public int retrieveSessionCollectorDAOID(int sessionID) throws DBException {
        return DBUtil.retrieveIntWithPK(connection, RETRIEVE_SESSION_COLLECTOR_DAOID, sessionID);
    }
    */
    
    /**
     * Gets the collector ID for a session.
     *
     * @param sessionID the unique key for the session
     * @return the collector ID for collection
     */
    public int retrieveSessionCollectorID(int sessionID) throws DBException {
        return DBUtil.retrieveIntWithPK(connection, RETRIEVE_SESSION_COLLECTORID, sessionID);
    }

    public DataAttributesCollector retrieveSessionAttributes(int sessionID) throws DBException {
        CollectorDefinitionDAO definitionDAO;
        //DataType dataType;
        int collectorID;
        CollectorDefinition definition;
        String title;

        try {
            //dataType = DataType.types[retrieveSessionDataTypeID(sessionID)];
            collectorID = retrieveSessionCollectorID(sessionID);
            title = retrieveSessionTitle(sessionID);
            definitionDAO = DAOFactory.getCollectorDefinitionDAO(connection, collectorID);
            definition = definitionDAO.retrieve(collectorID);
            return new DataAttributesCollector(definition, title);
        }
        catch (Exception e) {
            logger.error("Error creating session attributes for sessionID " + sessionID);
            throw new DBException("Error creating session attributes for sessionID " + sessionID, e);
        }
    }

    /**
     * Gets a list of the session ID's present in the "sessions" table.
     *
     * @return an integer array containing all session ID's
     */
    public int[] retrieveSessionIDs() throws DBException {
        Vector sessionsVector;
        int[] sessions;
        Statement statement;
        ResultSet results;

        try {
            sessionsVector = new Vector();
            statement = connection.createStatement();
            results = statement.executeQuery(RETRIEVE_SESSION_IDS);
            while (results.next()) {
                sessionsVector.add(new Integer(results.getInt("sessionID")));
            }
            sessions = new int[sessionsVector.size()];
            for (int session = 0; session < sessionsVector.size(); session++) {
                sessions[session] = ((Integer) (sessionsVector.elementAt(session))).intValue();
            }
            results.close();
            statement.close();
            logger.debug("Retrieving list of session IDs");
            return sessions;
        } catch (SQLException e) {
            logger.error("Cannot retrieve session ID list from database");
            throw new DBException("Cannot retrieve session ID list from database", e);
        }
    }

    /**
     * Returns the number of data points stored for the given set of a session
     * between the given times.
     *
     * @param sessionID the unique key for the session
     * @param set the set from which to count data point values
     * @param startTime the lower bound for the count
     * @param finishTime the upper bound for the count
     */
    public int retrieveNumberOfPoints(int sessionID, int set, long startTime, long finishTime) throws DBException {
        PreparedStatement statement;
        //CachedRowSet rowSet;
        ResultSet result;
        int values;

        try {
            //statement = connection.prepareStatement(RETRIEVE_SESSION_POINT_COUNT, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            statement = connection.prepareStatement(RETRIEVE_SESSION_POINT_COUNT);
            statement.setInt(1, sessionID);
            statement.setInt(2, set);
            statement.setLong(3, startTime);
            statement.setLong(4, finishTime);

            //rowSet = DBUtil.getSingleRow(connection, statement);
            // values = rowSet.getInt(1);
            result = statement.executeQuery();
            result.next();
            values = result.getInt(1);
            result.close();
            statement.close();
            return values;
        } catch (SQLException e) {
            logger.error("Problem counting values for session " + sessionID + " set " + set);
            throw new DBException("Problem counting values for session " + sessionID + " set " + set, e);
        }
    }

    public int createTemplate(DataSets dataSets) throws DBException, UnknownHostException, DAOCreationException {
        DeviceDAO deviceDAO;
        DataCollectorDAO collectorDAO;
        CollectorDefinitionDAO definitionDAO;
        PreparedStatement statement;
        ResultSet results;
        int sessionID;
        int collectorID;

        try {
            deviceDAO = DAOFactory.getDeviceDAO(connection);
            deviceDAO.create(dataSets.getCollector().getDefinition().getDevice());
            //deviceDAO.closeConnection();

            definitionDAO = DAOFactory.getCollectorDefinitionDAO(connection, dataSets.getCollector().getDefinition());
            collectorID = definitionDAO.create(dataSets.getCollector().getDefinition());

            statement = connection.prepareStatement(CREATE_TEMPLATE, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, dataSets.getDataType().id);
            //statement.setInt(2, CollectorDefinitionDAOType.getDAOID(dataSets.getCollector().getDefinition().getClass()));
            statement.setInt(2, collectorID);
            //statement.setInt(3, 0);
            statement.setInt(3, 0);
            statement.setString(4, dataSets.getTitle());
            //statement.setInt(7, dataSets.isStoring() ? 1 : 0);
            statement.executeUpdate();

            results = statement.getGeneratedKeys();
            results.next();
            sessionID = results.getInt(1);
            results.close();
            statement.close();

            logger.debug("Adding new session with ID " + sessionID);
            return sessionID;
        } catch (SQLException e) {
            logger.error("Failed to add new session to database");
            throw new DBException("Failed to add new session to database", e);
        }
    }

    public DataSets retrieveTemplate(int sessionID) throws DBException, UnknownHostException, SNMPException, DAOCreationException {
        PreparedStatement statement;
        ResultSet results;
        int collectorID;
        CollectorDefinitionDAO definitionDAO;
        DataCollector collector;
        CollectorDefinition definition;
        DataSets data;
        //Device device;
        //int dataTypeID;
        //boolean storing;
        //String portString;
        //String collectorType;

            collectorID = DBUtil.retrieveIntWithPK(connection, RETRIEVE_SESSION_COLLECTORID, sessionID);
            definitionDAO = DAOFactory.getCollectorDefinitionDAO(connection, sessionID);
            definition = definitionDAO.retrieve(collectorID);

            //statement = connection.prepareStatement(RETRIEVE_TEMPLATE);
            //statement.setInt(1, sessionID);
            //results = statement.executeQuery();
            //results.next();

            /*
             * data = new DataSets(collector.getDataType(), collector,
             * collector.getDevice(), results.getInt("pollInterval"),
             * results.getString("port"), results.getInt("direction"),
             * results.getString("title"), results.getBoolean("storing"));
             */
            collector = definition.spawnCollector();
            //data = new DataSets(collector, results.getString("title"), results.getBoolean("storing"));
            data = new DataSets(collector);
            //results.close();
            //statement.close();

            // Nasty - fix it up please
            /*
            if (collector.getDefinition().getDataType() == DataType.OID) {
                CustomOIDCollector oidCollector;
                oidCollector = (CustomOIDCollector) collector;
                for (int i = 0; i < oidCollector.getOIDs().size(); i++) {
                    data.addSet(oidCollector.getOIDs().elementAt(i).label);
                }
                data.setValueUnits(oidCollector.getValueUnits());
            }
            */
            return data;
        /*
        } catch (SQLException e) {
            logger.error("Problem loading session template with ID: " + sessionID);
            throw new DBException("Problem loading session template with ID: " + sessionID);
        }
        */
    }

    /**
     * Closes database connection opened for this DAO.
     */
    public void closeConnection() throws DBException {
        try {
            connection.close();
        } catch (SQLException e) {
            logger.error("Error closing database connection for DataSets DAO");
            throw new DBException("Error closing database connection for DataSets DAO", e);
        }
    }
}
