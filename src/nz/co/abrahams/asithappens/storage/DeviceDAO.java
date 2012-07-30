/*
 * DeviceDAO.java
 *
 * Created on 24 June 2008, 23:15
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

import nz.co.abrahams.asithappens.snmputil.SNMPException;
import nz.co.abrahams.asithappens.snmputil.USMLevel;
import nz.co.abrahams.asithappens.snmputil.USMAuthProtocol;
import nz.co.abrahams.asithappens.snmputil.USMPrivProtocol;
import nz.co.abrahams.asithappens.core.DBUtil;
import nz.co.abrahams.asithappens.core.DBException;
import java.util.Vector;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.net.UnknownHostException;
import org.apache.log4j.Logger;

/**
 *
 * @author mark
 */
public class DeviceDAO {
    
    public static final String CREATE = "INSERT INTO devices (name, ipAddress, hardwareAddress, communityRead, communityWrite) VALUES (?,?,?,?,?)";
    public static final String CREATE_NAME = "INSERT INTO devices (name) VALUES (?)";
    
    public static final String UPDATE_COMMUNITY_READ = "UPDATE devices SET communityRead = ? WHERE name = ?";
    public static final String UPDATE_COMMUNITY_WRITE = "UPDATE devices SET communityWrite = ? WHERE name = ?";
    public static final String UPDATE_IP_ADDRESS = "UPDATE devices SET ipAddress = ? WHERE name = ?";
    public static final String UPDATE_HARDWARE_ADDRESS = "UPDATE devices SET hardwareAddress = ? WHERE name = ?";
    
    public static final String RETRIEVE_DEVICE = "SELECT communityRead, communityWrite, ipAddress, hardwareAddress, " +
            "userNameRead, userLevelRead, userAuthProtocolRead, userAuthKeyRead, userPrivProtocolRead, userPrivKeyRead, " +
            "userNameWrite, userLevelWrite, userAuthProtocolWrite, userAuthKeyWrite, userPrivProtocolWrite, userPrivKeyWrite, " +
            "FROM devices WHERE name = ?";
    public static final String RETRIEVE_COMMUNITY_READ = "SELECT communityRead FROM devices WHERE name = ?";
    public static final String RETRIEVE_COMMUNITY_WRITE = "SELECT communityWrite FROM devices WHERE name = ?";
    public static final String RETRIEVE_DEVICE_EXISTS = "SELECT count(*) FROM devices WHERE name = ?";
    public static final String RETRIEVE_DEVICES = "SELECT name FROM devices ORDER BY name";
    
    /** Vendor-specific code for "no data" exception */
    private static final int EXCEPTION_NO_DATA = 2000;
    
    /** Logging provider */
    public static Logger logger = Logger.getLogger(DeviceDAO.class);
    
    /** Database connection */
    Connection connection;
    
    /** Creates a new Device DAO */
    public DeviceDAO(Connection connection) {
        this.connection = connection;
    }
    
    public void create(Device device) throws DBException, UnknownHostException {
        //PreparedStatement statement;
        //ResultSet results;
        //int deviceID;
        String ipAddressString;
        String hardwareAddressString;
        
        ipAddressString = null;
        hardwareAddressString = null;
        if ( device.getAddress() != null )
            ipAddressString = device.getAddress().getHostAddress();
        if ( device.getEthernetAddress() != null )
            hardwareAddressString = device.getEthernetAddress().toString();
        
        updateDevice(device.getName(), device.getCommunityRead(), device.getCommunityWrite(), ipAddressString, hardwareAddressString);
        /*
        try {
            statement = connection.prepareStatement(CREATE);
            statement.setString(1, device.getName());
            statement.setString(2, ipAddressString);
            statement.setString(3, hardwareAddressString);
            statement.setString(4, device.getCommunityRead());
            statement.setString(5, device.getCommunityWrite());
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            logger.error("Problem creating device " + device.getName() + " in database");
            throw new DatabaseException("Problem creating device " + device.getName() + " in database", e);
        }
         */
    }
    
    public void updateDevice(String name, String communityRead, String communityWrite, String ipAddress, String hardwareAddress) throws DBException {
        PreparedStatement statement;
        
        try {
            if ( ! retrieveDeviceExists(name) ) {
                statement = connection.prepareStatement(CREATE_NAME);
                statement.setString(1, name);
                statement.executeUpdate();
                statement.close();
            }
            
            if ( communityRead != null ) {
                statement = connection.prepareStatement(UPDATE_COMMUNITY_READ);
                statement.setString(1, communityRead);
                statement.setString(2, name);
                statement.executeUpdate();
                statement.close();
            }
            if ( communityWrite != null ) {
                statement = connection.prepareStatement(UPDATE_COMMUNITY_WRITE);
                statement.setString(1, communityWrite);
                statement.setString(2, name);
                statement.executeUpdate();
                statement.close();
            }
            if ( ipAddress != null ) {
                statement = connection.prepareStatement(UPDATE_IP_ADDRESS);
                statement.setString(1, ipAddress);
                statement.setString(2, name);
                statement.executeUpdate();
                statement.close();
            }
            if ( hardwareAddress != null ) {
                statement = connection.prepareStatement(UPDATE_HARDWARE_ADDRESS);
                statement.setString(1, hardwareAddress);
                statement.setString(2, name);
                statement.executeUpdate();
                statement.close();
            }
        } catch (SQLException e) {
            logger.error("Problem updating device information for " + name);
            throw new DBException("Problem updating device information for " + name, e);
        }
    }
    
    public Device retrieveDevice(String name, boolean useWrite) throws DBException, UnknownHostException, SNMPException {
        PreparedStatement statement;
        ResultSet results;
        String communityRead;
        String communityWrite;
        String ipAddress;
        String hardwareAddress;
        String userNameRead;
        USMLevel userLevelRead;
        USMAuthProtocol userAuthProtocolRead;
        String userAuthKeyRead;
        USMPrivProtocol userPrivProtocolRead;
        String userPrivKeyRead;
        String userNameWrite;
        USMLevel userLevelWrite;
        USMAuthProtocol userAuthProtocolWrite;
        String userAuthKeyWrite;
        USMPrivProtocol userPrivProtocolWrite;
        String userPrivKeyWrite;

        
        try {
            statement = connection.prepareStatement(RETRIEVE_DEVICE);
            statement.setString(1, name);
            results = statement.executeQuery();
            results.next();
            ipAddress = results.getString("ipAddress");
            if ( results.wasNull() )
                ipAddress = null;
            hardwareAddress = results.getString("hardwareAddress");
            if ( results.wasNull() )
                hardwareAddress = null;
            communityRead = results.getString("communityRead");
            if ( results.wasNull() )
                communityRead = null;
            communityWrite = results.getString("communityWrite");
            if ( results.wasNull() )
                communityWrite = null;

            userNameRead = results.getString("userNameRead");
            if ( results.wasNull() )
                userNameRead = null;
            userLevelRead = USMLevel.getLevel(results.getInt("userLevelRead"));
            userAuthProtocolRead = USMAuthProtocol.getAuthProtocol(results.getInt("userLevelAuthProtocolRead"));
            userAuthKeyRead = results.getString("userAuthKeyRead");
            if ( results.wasNull() )
                userAuthKeyRead = null;
            userPrivProtocolRead = USMPrivProtocol.getPrivProtocol(results.getInt("userLevelPrivProtocolRead"));
            userPrivKeyRead = results.getString("userPrivKeyRead");
            if ( results.wasNull() )
                userPrivKeyRead = null;

            userNameWrite = results.getString("userNameWrite");
            if ( results.wasNull() )
                userNameWrite = null;
            userLevelWrite = USMLevel.getLevel(results.getInt("userLevelWrite"));
            userAuthProtocolWrite = USMAuthProtocol.getAuthProtocol(results.getInt("userLevelAuthProtocolWrite"));
            userAuthKeyWrite = results.getString("userAuthKeyWrite");
            if ( results.wasNull() )
                userAuthKeyWrite = null;
            userPrivProtocolWrite = USMPrivProtocol.getPrivProtocol(results.getInt("userLevelPrivProtocolWrite"));
            userPrivKeyWrite = results.getString("userPrivKeyWrite");
            if ( results.wasNull() )
                userPrivKeyWrite = null;

            if ( communityRead == null && communityWrite == null ) {
                return new Device(name);
            } else {
                return new Device(name, results.getString("communityRead"), results.getString("communityWrite"), useWrite, ipAddress, hardwareAddress);
            }
        } catch (SQLException e) {
            logger.error("Problem retrieving device information for " + name);
            throw new DBException("Problem retrieving device information for " + name, e);
        }
    }
    
    public String retrieveDeviceReadCommunity(String name) throws DBException {
        try {
            return (String)(DBUtil.retrieveSingleAttributeWithStringPK(connection, RETRIEVE_COMMUNITY_READ, name));
        } catch (DBException e) {
            return null;
        }
    }
    
    public String retrieveDeviceWriteCommunity(String name) throws DBException {
        try {
            return (String)(DBUtil.retrieveSingleAttributeWithStringPK(connection, RETRIEVE_COMMUNITY_WRITE, name));
        } catch (DBException e) {
            return null;
        }
    }
    
    public boolean retrieveDeviceExists(String name) throws DBException {
        PreparedStatement statement;
        ResultSet results;
        
        try {
            statement = connection.prepareStatement(RETRIEVE_DEVICE_EXISTS);
            statement.setString(1, name);
            results = statement.executeQuery();
            results.next();
            if ( results.getInt(1) != 0 )
                return true;
            else
                return false;
        } catch (SQLException e) {
            logger.error("Error code: " + e.getErrorCode());
            if ( e.getErrorCode() == EXCEPTION_NO_DATA )
                return false;
            else {
                logger.error("Problem checking for device " + name);
                throw new DBException("Problem checking for device " + name, e);
            }
        }
    }

    public String[] retrieveDevices() throws DBException {
        PreparedStatement statement;
        ResultSet results;
        Vector<String> devices;
        String[] deviceString;

        try {
            statement = connection.prepareStatement(RETRIEVE_DEVICES);
            results = statement.executeQuery();
            devices = new Vector<String>();
            while ( results.next() ) {
                devices.add(results.getString("name"));
            }
            deviceString = new String[devices.size()];
            return devices.toArray(deviceString);
        } catch (SQLException e) {
            logger.error("Problem retrieving device information");
            throw new DBException("Problem retrieving device information", e);
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
