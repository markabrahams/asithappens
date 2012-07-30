/*
 * FlowOptionsDAO.java
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

package nz.co.abrahams.asithappens.flow;

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
public class FlowOptionsDAO {
    
    /** FlowOptions creation SQL statement */
    public static final String CREATE = "INSERT INTO FlowOptions " +
                "(sessionID, ipProtocol, ipSourceAddress, ipDestinationAddress, " +
                "tosByte, tcpUdpSourcePort, tcpUdpDestinationPort) " +
                "VALUES (?,?,?,?,?,?,?)";
    
    /** FlowOptions retrieval SQL statement */
    public static final String RETRIEVE = "SELECT ipProtocol, " +
                "ipSourceAddress, ipDestinationAddress, " +
                "tosByte, tcpUdpSourcePort, tcpUdpDestinationPort " +
                "FROM FlowOptions WHERE sessionID = ?";
    
    /** Logging provider */
    public static Logger logger = Logger.getLogger(FlowOptionsDAO.class);
    
    /** Database connection */
    Connection connection;
    
    /** Creates a new FlowOptions DAO */
    public FlowOptionsDAO(Connection connection) {
        this.connection = connection;
    }
    
    /**
     * Creates a FlowOptions in the database.
     *
     * @param sessionID      the session ID that the MatchCriteria belongs to
     * @param criteria       the criteria to create in the database
     */
    public void create(int sessionID, FlowOptions options) throws DBException {
        PreparedStatement statement;
        
        try {
            statement = connection.prepareStatement(CREATE);
            statement.setInt(1, sessionID);
            statement.setInt(2, options.ipProtocol ? 1 : 0);
            statement.setInt(3, options.ipSourceAddress ? 1 : 0);
            statement.setInt(4, options.ipDestinationAddress ? 1 : 0);
            statement.setInt(5, options.tosByte ? 1 : 0);
            statement.setInt(6, options.tcpUdpSourcePort ? 1 : 0);
            statement.setInt(7, options.tcpUdpDestinationPort ? 1 : 0);
            statement.executeUpdate();
            statement.close();
            
        } catch (SQLException e) {
            logger.error("Problem creating FlowOptions in database for session " + sessionID);
            throw new DBException("Problem creating FlowOptions in database for session " + sessionID, e);
        }
    }
    
    public FlowOptions retrieve(int sessionID) throws DBException {
        PreparedStatement statement;
        ResultSet result;
        FlowOptions options;
        
        try {
            statement = connection.prepareStatement(RETRIEVE);
            statement.setInt(1, sessionID);
            result = statement.executeQuery();
            result.next();
            options = new FlowOptions(result.getInt("ipProtocol") == 1,
                    result.getInt("ipSourceAddress") == 1, result.getInt("ipDestinationAddress") == 1,
                    result.getInt("tosByte") == 1, result.getInt("tcpUdpSourcePort") == 1,
                    result.getInt("tcpUdpDestinationPort") == 1);
            result.close();
            statement.close();
            return options;
        } catch (SQLException e) {
            throw new DBException("Problem creating FlowOptions for sessionID " + sessionID, e);
        }
    }
    
    /**
     * Closes database connection opened for this DAO.
     */
    public void closeConnection() throws DBException {
        try {
            connection.close();
        } catch (SQLException e) {
            logger.error("Error closing database connection for FlowOptions DAO");
            throw new DBException("Error closing database connection for FlowOptions DAO", e);
        }
    }
    
}
