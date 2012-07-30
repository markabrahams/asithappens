/*
 * SetDisplayDAO.java
 *
 * Created on 26 June 2008, 17:43
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

package nz.co.abrahams.asithappens.uiutil;

import nz.co.abrahams.asithappens.core.DBException;
import nz.co.abrahams.asithappens.*;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import org.apache.log4j.Logger;

/**
 *
 * @author mark
 */
public class SetDisplayDAO {
    
    public static final String CREATE = "INSERT INTO SetDisplays (style, color) VALUES (?,?)";
    
    public static final String UPDATE = "UPDATE SetDisplays SET style = ?, color = ? WHERE setDisplayID = ?";
    
    public static final String RETRIEVE = "SELECT setNumber, style, color FROM " +
            "GraphOptions, GraphOptionsSetDisplays, SetDisplays WHERE GraphOptions.graphOptionsID = GraphOptionsSetDisplays.graphOptionsID " +
            "AND GraphOptionsSetDisplays.setDisplayID = SetDisplays.setDisplayID " +
            "AND GraphOptions.graphOptionsID = ? ORDER BY GraphOptionsSetDisplays.setNumber";
    
    
    /** Logging provider */
    public static Logger logger = Logger.getLogger(SetDisplayDAO.class);
    
    /** Database connection */
    Connection connection;
    
    /** Creates a new SetDisplay DAO */
    public SetDisplayDAO(Connection connection) {
        this.connection = connection;
    }
    
    public int create(SetDisplay setDisplay) throws DBException {
        PreparedStatement statement;
        ResultSet results;
        int setDisplayID;
        
        try {
            statement = connection.prepareStatement(CREATE, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, setDisplay.style.toString());
            statement.setInt(2, setDisplay.color.getRGB());
            statement.executeUpdate();
            results = statement.getGeneratedKeys();
            results.next();
            setDisplayID = results.getInt(1);
            results.close();
            statement.close();
            return setDisplayID;
        } catch (SQLException e) {
            logger.error("Problem creating SetDisplay in database");
            throw new DBException("Problem creating SetDisplay in database", e);
        }
    }
    
    public void update(int setDisplayID, SetDisplay setDisplay) throws DBException {
        PreparedStatement statement;
        
        try {
            statement = connection.prepareStatement(UPDATE);
            statement.setString(1, setDisplay.style.toString());
            statement.setInt(2, setDisplay.color.getRGB());
            statement.setInt(3, setDisplayID);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            logger.error("Problem updating SetDisplay in database");
            throw new DBException("Problem updating SetDisplay in database", e);
        }
    }
    
    public Vector<SetDisplay> retrieve(int graphOptionsID) throws DBException {
        PreparedStatement statement;
        ResultSet results;
        Vector<SetDisplay> setDisplays;
        
        try {
            statement = connection.prepareStatement(RETRIEVE);
            statement.setInt(1, graphOptionsID);
            results = statement.executeQuery();
            setDisplays = new Vector();
            while ( results.next() ) {
                setDisplays.add(new SetDisplay(SetDisplay.Style.valueOf(results.getString("style")),
                        new java.awt.Color(results.getInt("color"))));
            }
            results.close();
            statement.close();
            return setDisplays;
        } catch (SQLException e) {
            logger.error("Problem retrieving SetDisplays from database");
            throw new DBException("Problem retrieving SetDisplays in database", e);
        }
        
    }
    
    /**
     * Closes database connection opened for this DAO.
     */
    public void closeConnection() throws DBException {
        try {
            connection.close();
        } catch (SQLException e) {
            logger.error("Error closing database connection for SetDisplayDAO");
            throw new DBException("Error closing database connection for SetDisplay DAO", e);
        }
    }
    
}
