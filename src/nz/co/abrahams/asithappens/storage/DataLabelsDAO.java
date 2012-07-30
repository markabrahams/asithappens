/*
 * DataLabelsDAO.java
 *
 * Created on 24 June 2008, 20:10
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
public class DataLabelsDAO {
    
    public static final String CREATE = "INSERT INTO labels (sessionID, time, value, name) VALUES (?,?,?,?)";
    
    public static final String DELETE = "DELETE FROM labels WHERE sessionID = ? AND index = ?";
    
    public static final String RETRIEVE = "SELECT time, value, name FROM labels WHERE sessionID = ?";

    /** Logging provider */
    public static Logger logger = Logger.getLogger(DataLabelsDAO.class);
    
    /** Database connection */
    Connection connection;
    
    /** Creates a new instance of DataLabelsDAO */
    public DataLabelsDAO(Connection connection) {
        this.connection = connection;
    }
    
    /**
     * Adds a label to the given session.
     *
     * @param sessionID the unique key for the session
     * @param label     the label to add
     */
    public int create(int sessionID, DataLabel label) throws DBException {
        PreparedStatement statement;
        ResultSet results;
        int index;
        
        try {
            statement = connection.prepareStatement(CREATE, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, sessionID);
            statement.setLong(2, label.getTime());
            statement.setDouble(3, label.getValue());
            statement.setString(4, label.getLabel());
            
            statement.executeUpdate();
            results = statement.getGeneratedKeys();
            if (results.next()) {
                index = results.getInt(1);
            } else {
                index = -1;
            }
            
            results.close();
            statement.close();
            return index;
        } catch (SQLException e) {
            logger.error("Cannot add label for session ID " + sessionID + " to database");
            throw new DBException("Cannot add label for session ID " + sessionID + " to database", e);
        }
    }
    
    /**
     * Deletes the indexed label from the given session.
     *
     * @param sessionID the unique key for the session
     * @param index     the unique index for the label
     * @return          true upon a successful deletion
     */
    public boolean delete(int sessionID, int index) throws DBException {
        PreparedStatement statement;
        String updateString;
        boolean success;
        
        try {
            statement = connection.prepareStatement(DELETE);
            statement.setInt(1, sessionID);
            statement.setInt(2, index);
            success = statement.executeUpdate() == 1;
            statement.close();
            return success;
        } catch (SQLException e) {
            logger.error("Cannot delete label for session ID " + sessionID + " from database");
            throw new DBException("Cannot delete label for session ID " + sessionID + " from database", e);
        }
    }
    
    
    /**
     * Fetches the set of labels stored for the given session.
     *
     * @param sessionID the unique key for the session
     * @return          the set of labels
     */
    public DataLabels retrieve(int sessionID) throws DBException {
        PreparedStatement statement;
        ResultSet results;
        DataLabels labels;
        
        labels = new DataLabels(sessionID);
        try {
            statement = connection.prepareStatement(RETRIEVE);
            statement.setInt(1, sessionID);
            results = statement.executeQuery();
            while (results.next()) {
                labels.addLabel(new DataLabel(results.getLong("time"), results.getDouble("value"), results.getString("name")), true);
            }
            results.close();
            statement.close();
            return labels;
        } catch (SQLException e) {
            logger.error("Problem retrieving labels for session " + sessionID);
            throw new DBException("Problem retrieving labels for session " + sessionID, e);
        }
    }
    
    /**
     * Closes database connection opened for this DAO.
     */
    public void closeConnection() throws DBException {
        try {
            connection.close();
        } catch (SQLException e) {
            logger.error("Error closing database connection for DataLabelsDAO");
            throw new DBException("Error closing database connection for DataLabels DAO", e);
        }
    }
    
}
