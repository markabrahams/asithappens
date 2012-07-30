/*
 * CustomOIDDAO.java
 *
 * Created on 30 December 2008, 22:40
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

import nz.co.abrahams.asithappens.snmputil.SNMPType;
import nz.co.abrahams.asithappens.core.DBException;
import java.sql.*;
import org.apache.log4j.Logger;


/**
 *
 * @author mark
 */
public class CustomOIDDAO {
    
    public static Logger logger = Logger.getLogger(CustomOIDDAO.class);
    
    public static final String CREATE = "INSERT INTO CustomOIDs (label, oid, oidType, description) VALUES (?,?,?,?)";
    
    public static final String RETRIEVE = "SELECT label, oid, oidType, description FROM CustomOIDs WHERE oidID = ?";
    
    /** Database connection */
    Connection connection;
    
    /** Creates a new instance of DataPointDAO */
    public CustomOIDDAO(Connection connection) {
        this.connection = connection;
    }
    
    public int create(CustomOID oid) throws DBException {
        PreparedStatement statement;
        ResultSet results;
        int oidID;
        
        try {
            statement = connection.prepareStatement(CREATE);
            statement.setString(1, oid.label);
            statement.setString(2, oid.oid);
            statement.setString(3, oid.type.name());
            statement.setString(4, oid.description);
            statement.executeUpdate();
            results = statement.getGeneratedKeys();
            results.next();
            oidID = results.getInt(1);
            results.close();
            statement.close();
            return oidID;            
        } catch (SQLException e) {
            logger.error("Problem adding values for OID " + oid.label + " (" + oid.oid + ")");
            throw new DBException("Problem adding values for OID " + oid.label + " (" + oid.oid + ")", e);
        }
        
    }
    
    public CustomOID retrieve(int oidID) throws DBException {
        PreparedStatement statement;
        ResultSet results;
        CustomOID customOID;
        
        try {
            statement = connection.prepareStatement(RETRIEVE);
            statement.setInt(1, oidID);
            results = statement.executeQuery();
            results.first();
            customOID = new CustomOID(results.getString("label"), results.getString("oid"), SNMPType.valueOf(results.getString("oidType")), results.getString("description"));
            results.close();
            statement.close();
            return customOID;
        } catch (SQLException e) {
            logger.error("Problem retrieving CustomOID from database");
            throw new DBException("Problem retrieving CustomOID in database", e);
        }
    }
    
    /**
     * Closes database connection opened for this DAO.
     */
    public void closeConnection() throws DBException {
        try {
            connection.close();
        } catch (SQLException e) {
            logger.error("Error closing database connection for CustomOID DAO");
            throw new DBException("Error closing database connection for CustomOID DAO", e);
        }
    }
    
}
