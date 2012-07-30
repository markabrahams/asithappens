/*
 * DataHeadingsDAO.java
 *
 * Created on 24 June 2008, 00:47
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
public class DataHeadingsDAO {
    
    public static final String CREATE = "INSERT INTO headings VALUES (?,?,?)";
    public static final String RETRIEVE = "SELECT position, name FROM headings WHERE sessionID = ?";
    
    /** Logging provider */
    public static Logger logger = Logger.getLogger(DataHeadingsDAO.class);
    
    /** Database connection */
    Connection connection;
    
    /** Creates a new DataHeadings DAO */
    public DataHeadingsDAO(Connection connection) {
        this.connection = connection;
    }
    
    /**
     * Adds a data set name to the "headings" table.
     *
     * @param sessionID  the unique key for the session
     * @param set        the number of the set to be named
     * @param name       the name of the set
     * @return           true if the heading name was added successfully
     */
    public boolean create(int sessionID, int set, String name) throws DBException {
        PreparedStatement statement;
        String updateString;
        boolean success;
        
        try {
            statement = connection.prepareStatement(CREATE);
            statement.setInt(1, sessionID);
            statement.setInt(2, set);
            statement.setString(3, name);
            success = statement.executeUpdate() == 1;
            statement.close();
            return success;
        } catch (SQLException e) {
            logger.error("Problem adding heading " + name + " for session " + sessionID);
            throw new DBException("Problem adding heading " + name + " for session " + sessionID, e);
        }
    }
    
    /**
     * Fetches data set headings for the given session.
     *
     * @param sessionID the unique key for the session
     * @return          an array of heading names
     */
    public String[] retrieve(int sessionID) throws DBException {
        PreparedStatement statement;
        ResultSet results;
        int headingsCount;
        String[] headings;
        
        try {
            //statement = connection.createStatement();
            statement = connection.prepareStatement(RETRIEVE, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            statement.setInt(1, sessionID);
            results = statement.executeQuery();
            results.last();
            headingsCount = results.getRow();
            headings = new String[headingsCount];
            results.beforeFirst();
            while (results.next()) {
                headings[results.getInt("position")] = results.getString("name");
            }
            results.close();
            statement.close();
            return headings;
        } catch (SQLException e) {
            logger.error("Problem retrieving headings for session " + sessionID);
            throw new DBException("Problem retrieving headings for session " + sessionID, e);
        }
    }
    
    
    /**
     * Closes database connection opened for this DAO.
     */
    public void closeConnection() throws DBException {
        try {
            connection.close();
        } catch (SQLException e) {
            logger.error("Error closing database connection for DataHeadings DAO");
            throw new DBException("Error closing database connection for DataHeadings DAO", e);
        }
    }
    
}
