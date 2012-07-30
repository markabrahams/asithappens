/*
 * DataSetDAO.java
 *
 * Created on 24 June 2008, 00:11
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

import nz.co.abrahams.asithappens.core.DBException;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.log4j.Logger;

/**
 *
 * @author mark
 */
public class DataSetDAO {
    
    /** Logging provider */
    public static Logger logger = Logger.getLogger(DataSetDAO.class);
    
    public static final String RETRIEVE = "SELECT time, value FROM sessions, data WHERE sessions.sessionID = ? AND sessions.sessionID = data.sessionID AND position = ? AND time >= ? AND time < ? ORDER BY time";
    
    /** Database connection */
    Connection connection;
    
    /** Creates a new instance of DataSetDAO */
    public DataSetDAO(Connection connection) {
        this.connection = connection;
    }
    
    /**
     * Queries the "data" table for a set of data values within a given time
     * interval.
     *
     * @param sessionID  the unique key for the session
     * @param set        the number of the data set within the session
     * @param startTime  the start of the time interval
     * @param finishTime the finish of the time interval
     * @return           the set of data values
     */
    public DataSet retrieve(int sessionID, int set, long startTime, long finishTime) throws DBException {
        PreparedStatement statement;
        ResultSet results;
        DataSet dataSet;
        
        dataSet = new DataSet();
        try {
            statement = connection.prepareStatement(RETRIEVE);
            statement.setInt(1, sessionID);
            statement.setInt(2, set);
            statement.setLong(3, startTime);
            statement.setLong(4, finishTime);
            results = statement.executeQuery();
            while (results.next()) {
                dataSet.add(new DataPoint(results.getLong(1), results.getDouble(2)));
            }
            results.close();
            statement.close();
            return dataSet;
        } catch (SQLException e) {
            logger.error("Cannot retrieve data for session ID " + sessionID + " from database");
            throw new DBException("Cannot retrieve data for session ID " + sessionID + " from database", e);
        }
    }
    
    /**
     * Queries the "data" table for an entire set of data values.
     *
     * @param sessionID  the unique key for the session
     * @param set        the number of the data set within the session
     * @return           the set of data values
     */
    public DataSet retrieve(int sessionID, int set) throws DBException {
        return retrieve(sessionID, set, 0, Long.MAX_VALUE);
    }
    
    /**
     * Closes database connection opened for this DAO.
     */
    public void closeConnection() throws DBException {
        try {
            connection.close();
        } catch (SQLException e) {
            logger.error("Error closing database connection for DataSet DAO");
            throw new DBException("Error closing database connection for DataSet DAO", e);
        }
    }
    
}
