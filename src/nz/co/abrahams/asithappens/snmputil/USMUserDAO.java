/*
 * USMUserDAO.java
 *
 * Created on 31 July 2012, 22:15
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
package nz.co.abrahams.asithappens.snmputil;

import java.net.UnknownHostException;
import java.sql.*;
import nz.co.abrahams.asithappens.core.DBException;
import nz.co.abrahams.asithappens.storage.Device;
import nz.co.abrahams.asithappens.storage.DeviceDAO;
import org.apache.log4j.Logger;

/**
 *
 * @author mark
 */
public class USMUserDAO {

    public static final String CREATE = "INSERT INTO usmusers "
            + "(userName, userLevel, userAuthProtocol, userAuthKey, userPrivProtocol, userPrivKey) "
            + "VALUES (?,?,?,?,?,?)";
    
    public static final String RETRIEVE = "SELECT "
            + "userName, userLevel, userAuthProtocol, userAuthKey, userPrivProtocol, userPrivKey "
            + "FROM usmusers WHERE userID = ?";
    
    public static final String UPDATE = "UPDATE usmusers SET "
            + "userName = ?, userLevel = ?, userAuthProtocol = ?, userAuthKey = ?, "
            + "userPrivProtocol = ?, userPrivKey = ? WHERE userID = ?";
    
    /**
     * Logging provider
     */
    public static Logger logger = Logger.getLogger(USMUserDAO.class);
    /**
     * Database connection
     */
    Connection connection;

    /**
     * Creates a new Device DAO
     */
    public USMUserDAO(Connection connection) {
        this.connection = connection;
    }

    public int create(USMUser user) throws DBException {
        PreparedStatement statement;
        ResultSet results;
        int userID;

        try {
            statement = connection.prepareStatement(CREATE, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, user.getUserName());
            statement.setInt(2, user.getUserLevel().getIndex());
            statement.setInt(3, user.getUserAuthProtocol().getIndex());
            statement.setString(4, user.getUserAuthKey());
            statement.setInt(5, user.getUserPrivProtocol().getIndex());
            statement.setString(6, user.getUserPrivKey());
            statement.executeUpdate();
            results = statement.getGeneratedKeys();
            results.next();
            userID = results.getInt(1);
            statement.close();
            return userID;
        } catch (SQLException e) {
            logger.error("Problem creating user " + user.getUserName());
            throw new DBException("Problem creating user " + user.getUserName(), e);
        }
    }

    public USMUser retrieve(int userID) throws DBException, SNMPException {
        PreparedStatement statement;
        ResultSet results;
        String userName = null;
        USMLevel userLevel;
        USMAuthProtocol userAuthProtocol;
        String userAuthKey;
        USMPrivProtocol userPrivProtocol;
        String userPrivKey;

        try {
            statement = connection.prepareStatement(RETRIEVE);
            statement.setInt(1, userID);
            results = statement.executeQuery();
            results.next();
            userName = results.getString("userName");
            if (results.wasNull()) {
                userName = null;
            }
            userLevel = USMLevel.getLevel(results.getInt("userLevel"));
            userAuthProtocol = USMAuthProtocol.getAuthProtocol(results.getInt("userAuthProtocol"));
            userAuthKey = results.getString("userAuthKey");
            if (results.wasNull()) {
                userAuthKey = null;
            }
            userPrivProtocol = USMPrivProtocol.getPrivProtocol(results.getInt("userPrivProtocol"));
            userPrivKey = results.getString("userPrivKey");
            if (results.wasNull()) {
                userPrivKey = null;
            }

            return new USMUser(userName, userLevel, userAuthProtocol, userAuthKey, userPrivProtocol, userPrivKey);

        } catch (SQLException e) {
            logger.error("Problem USM user information for " + userName);
            throw new DBException("Problem retrieving USM user information for " + userName, e);
        }
    }

    public void update(int userID, USMUser user) throws DBException {
        PreparedStatement statement;
        try {
            statement = connection.prepareStatement(UPDATE);
            statement.setString(1, user.getUserName());
            statement.setInt(2, user.getUserLevel().getIndex());
            statement.setInt(3, user.getUserAuthProtocol().getIndex());
            statement.setString(4, user.getUserAuthKey());
            statement.setInt(5, user.getUserPrivProtocol().getIndex());
            statement.setString(6, user.getUserPrivKey());
            statement.setInt(7, userID);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            logger.error("Problem updating user " + user.getUserName());
            throw new DBException("Problem updating user " + user.getUserName(), e);
        }        
    }

    /**
     * Closes database connection opened for this DAO.
     */
    public void closeConnection() throws DBException {
        try {
            connection.close();
        } catch (SQLException e) {
            logger.error("Error closing database connection for USMUser DAO");
            throw new DBException("Error closing database connection for USMUser DAO", e);
        }
    }
}
