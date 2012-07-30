/*
 * NetFlowMatchCriteriaDAO.java
 *
 * Created on 28 June 2008, 01:28
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

package nz.co.abrahams.asithappens.netflow;

import nz.co.abrahams.asithappens.core.DBException;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Vector;
import org.apache.log4j.Logger;

/**
 *
 * @author mark
 */
public class NetFlowMatchCriteriaDAO {
    
    /** NetFlowMatchCriteria creation SQL statement */
    public static final String CREATE = "INSERT INTO NetFlowMatchCriteria " +
            "(sessionID, srcAddressType, srcAddressMask, dstAddressType, dstAddressMask, " +
            "srcPortLo, srcPortHi, dstPortLo, dstPortHi, srcAS, dstAS, protocol, tosByte, " +
            "nhAddressType, nhAddressMask, inputIf, outputIf, sampler, classMap, " +
            "minPackets, maxPackets, minBytes, maxBytes) " +
            "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    
    /** Update source address SQL statement */
    public static final String UPDATE_SRCADDRESS = "UPDATE netflowMatchCriteria SET srcAddress = ?";
    /** Update source address SQL statement */
    public static final String UPDATE_DSTADDRESS = "UPDATE netflowMatchCriteria SET dstAddress = ?";
    /** Update source address SQL statement */
    public static final String UPDATE_NHADDRESS = "UPDATE netflowMatchCriteria SET nhAddress = ?";
    
    /** NetFlowMatchCriteria retrieval SQL statement */
    public static final String RETRIEVE = "SELECT srcAddressType, srcAddress, srcAddressMask, dstAddressType, dstAddress, dstAddressMask, " +
                    "srcPortLo, srcPortHi, dstPortLo, dstPortHi, srcAS, dstAS, protocol, tosByte, " +
                    "nhAddressType, nhAddress, nhAddressMask, inputIf, outputIf, sampler, classMap, " +
                    "minPackets, maxPackets, minBytes, maxBytes FROM netflowMatchCriteria WHERE sessionID = ?";
    
    /** Logging provider */
    public static Logger logger = Logger.getLogger(NetFlowMatchCriteriaDAO.class);
    
    /** Database connection */
    Connection connection;
    
    /** Creates a new NetFlowMatchCriteria DAO */
    public NetFlowMatchCriteriaDAO(Connection connection) {
        this.connection = connection;
    }
    
    /**
     * Creates a NetFlowMatchCriteria in the database.
     *
     * @param sessionID      the session ID that the MatchCriteria belongs to
     * @param criteria       the criteria to create in the database
     */
    public void create(int sessionID, NetFlowMatchCriteria criteria) throws DBException {
        PreparedStatement statement;
        int deviceID;
        
        try {
            statement = connection.prepareStatement(CREATE);
            statement.setInt(1, sessionID);
            statement.setInt(2, criteria.srcAddressType);
            statement.setInt(3, criteria.srcAddressMask);
            statement.setInt(4, criteria.dstAddressType);
            statement.setInt(5, criteria.dstAddressMask);
            statement.setInt(6, criteria.srcPortLo);
            statement.setInt(7, criteria.srcPortHi);
            statement.setInt(8, criteria.dstPortLo);
            statement.setInt(9, criteria.dstPortHi);
            statement.setInt(10, criteria.srcAS);
            statement.setInt(11, criteria.dstAS);
            statement.setInt(12, criteria.protocol);
            statement.setInt(13, criteria.tosByte);
            statement.setInt(14, criteria.nhAddressType);
            statement.setInt(15, criteria.nhAddressMask);
            statement.setInt(16, criteria.inputIf);
            statement.setInt(17, criteria.outputIf);
            statement.setString(18, criteria.sampler);
            statement.setString(19, criteria.classMap);
            statement.setInt(20, criteria.minPackets);
            statement.setInt(21, criteria.maxPackets);
            statement.setInt(22, criteria.minBytes);
            statement.setInt(23, criteria.maxBytes);
            statement.executeUpdate();
            statement.close();
            
            if ( criteria.srcAddress != null ) {
                statement = connection.prepareStatement(UPDATE_SRCADDRESS);
                statement.setInt(1, sessionID);
                statement.executeUpdate();
                statement.close();
            }
            
            if ( criteria.dstAddress != null ) {
                statement = connection.prepareStatement(UPDATE_DSTADDRESS);
                statement.setInt(1, sessionID);
                statement.executeUpdate();
                statement.close();
            }
            
            if ( criteria.nhAddress != null ) {
                statement = connection.prepareStatement(UPDATE_NHADDRESS);
                statement.setInt(1, sessionID);
                statement.executeUpdate();
                statement.close();
            }
            
        } catch (SQLException e) {
            logger.error("Problem creating NetFlowMatchCriteria in database for session " + sessionID);
            throw new DBException("Problem creating NetFlowMatchCriteria in database for session " + sessionID, e);
        }
    }
    
    public NetFlowMatchCriteria retrieve(int sessionID) throws DBException, UnknownHostException {
        PreparedStatement statement;
        ResultSet result;
        NetFlowMatchCriteria criteria;
        
        try {
            statement = connection.prepareStatement(RETRIEVE);
            statement.setInt(1, sessionID);
            result = statement.executeQuery();
            result.next();
            criteria = new NetFlowMatchCriteria();
            criteria.srcAddressType = result.getInt("srcAddressType");
            criteria.srcAddress = InetAddress.getByName(result.getString("srcAddress"));
            criteria.srcAddressMask = result.getInt("srcAddressMask");
            criteria.dstAddressType = result.getInt("dstAddressType");
            criteria.dstAddress = InetAddress.getByName(result.getString("dstAddress"));
            criteria.dstAddressMask = result.getInt("dstAddressMask");
            criteria.srcPortLo = result.getInt("srcPortLo");
            criteria.srcPortHi = result.getInt("srcPortHi");
            criteria.dstPortLo = result.getInt("dstPortLo");
            criteria.dstPortHi = result.getInt("dstPortHi");
            criteria.srcAS = result.getInt("srcAS");
            criteria.dstAS = result.getInt("dstAS");
            criteria.protocol = result.getInt("protocol");
            criteria.tosByte = result.getInt("tosByte");
            criteria.nhAddressType = result.getInt("nhAddressType");
            criteria.nhAddress = InetAddress.getByName(result.getString("nhAddress"));
            criteria.nhAddressMask = result.getInt("nhAddressMask");
            criteria.inputIf = result.getInt("inputIf");
            criteria.outputIf = result.getInt("outputIf");
            criteria.sampler = result.getString("sampler");
            criteria.classMap = result.getString("classMap");
            criteria.minPackets = result.getInt("minPackets");
            criteria.maxPackets = result.getInt("maxPackets");
            criteria.minBytes = result.getInt("minBytes");
            criteria.maxBytes = result.getInt("maxBytes");
            result.close();
            statement.close();
            return criteria;
        } catch (SQLException e) {
            throw new DBException("Problem creating NetFlow match criteria for sessionID " + sessionID, e);
        }
    }
    
    /**
     * Closes database connection opened for this DAO.
     */
    public void closeConnection() throws DBException {
        try {
            connection.close();
        } catch (SQLException e) {
            logger.error("Error closing database connection for NetFlowMatchCriteria DAO");
            throw new DBException("Error closing database connection for NetFlowMatchCriteria DAO", e);
        }
    }
    
}
