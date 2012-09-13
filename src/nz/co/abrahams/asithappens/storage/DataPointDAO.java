/*
 * DataPointDAO.java
 *
 * Created on 21 June 2008, 23:13
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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import nz.co.abrahams.asithappens.core.DBException;
import org.apache.log4j.Logger;


/**
 *
 * @author mark
 */
public class DataPointDAO {
    
    public static Logger logger = Logger.getLogger(DataPointDAO.class);
    
    public static final String CREATE = "INSERT INTO Data VALUES (?,?,?,?)";
    
    /** Database connection */
    Connection connection;
    
    /** Creates a new instance of DataPointDAO */
    public DataPointDAO(Connection connection) {
        this.connection = connection;
    }
    
    public void create(int sessionID, int set, DataPoint point) throws DBException {
        PreparedStatement statement;
        String updateString;
        int index;
        
        try {
            statement = connection.prepareStatement(CREATE);
            statement.setInt(1, sessionID);
            statement.setInt(2, set);
            statement.setLong(3, point.getTime());
            statement.setDouble(4, point.getValue());
            statement.executeUpdate();
            
            /*
            if ( statement.executeUpdate() == 1 ) {
                if ( getStartTime(sessionID) == 0 )
                    setStartTime(sessionID, time);
                setFinishTime(sessionID, time);
                statement.close();
                return true;
            } else {
                statement.close();
                return false;
            }
            */
            
            statement.close();
        } catch (SQLException e) {
            logger.error("Problem adding values for " + sessionID);
            throw new DBException("Problem adding values for " + sessionID, e);
        }
        
    }
    
    /**
     * Closes database connection opened for this DAO.
     */
    public void closeConnection() throws DBException {
        try {
            connection.close();
        } catch (SQLException e) {
            logger.error("Error closing database connection for DataPoint DAO");
            throw new DBException("Error closing database connection for DataPoint DAO", e);
        }
    }
    
}
