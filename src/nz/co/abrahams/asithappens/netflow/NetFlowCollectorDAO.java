/*
 * NetFlowCollectorDAO.java
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

import nz.co.abrahams.asithappens.storage.DeviceDAO;
import nz.co.abrahams.asithappens.storage.Device;
import nz.co.abrahams.asithappens.flow.FlowOptions;
import nz.co.abrahams.asithappens.flow.FlowOptionsDAO;
import nz.co.abrahams.asithappens.*;
import nz.co.abrahams.asithappens.collectors.DataCollectorDAO;
import nz.co.abrahams.asithappens.collectors.DataCollector;
import nz.co.abrahams.asithappens.core.DAOFactory;
import nz.co.abrahams.asithappens.snmputil.PortsSelectorSNMP;
import nz.co.abrahams.asithappens.snmputil.SNMPException;
import nz.co.abrahams.asithappens.core.DBUtil;
import nz.co.abrahams.asithappens.core.DBException;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.net.UnknownHostException;
import java.util.Vector;
import org.apache.log4j.Logger;

/**
 *
 * @author mark
 */
public class NetFlowCollectorDAO implements DataCollectorDAO {
    
    /** Collector creation SQL statement */
    public static final String CREATE = "INSERT INTO NetFlowCollectors (sessionID, device, pollInterval, ifDescr, direction, tableSize) VALUES (?,?,?,?,?,?)";
    
    /** Device retrieval SQL statement */
    public static final String RETRIEVE_DEVICE = "SELECT device FROM NetFlowCollectors WHERE sessionID = ?";
    /** Polling interval retrieval SQL statement */
    public static final String RETRIEVE_POLLINTERVAL = "SELECT pollInterval FROM NetFlowCollectors WHERE sessionID = ?";
    /** Table size retrieval SQL statement */
    public static final String RETRIEVE_IFDESCR = "SELECT ifDescr FROM NetFlowCollectors WHERE sessionID = ?";
    /** Table size retrieval SQL statement */
    public static final String RETRIEVE_DIRECTION = "SELECT direction FROM NetFlowCollectors WHERE sessionID = ?";
    /** Table size retrieval SQL statement */
    public static final String RETRIEVE_TABLESIZE = "SELECT tableSize FROM NetFlowCollectors WHERE sessionID = ?";
    
    /** Logging provider */
    public static Logger logger = Logger.getLogger(NetFlowCollectorDAO.class);
    
    /** Database connection */
    Connection connection;
    
    /** Creates a new NetFlowCollector DAO */
    public NetFlowCollectorDAO(Connection connection) {
        this.connection = connection;
    }
    
    /**
     * Creates a NetFlowCollector in the database.
     *
     * @param sessionID      the session ID that the NetFlowCollector belongs to
     * @param dataCollector  the collector to create in the database
     */
    public void create(int sessionID, DataCollector dataCollector) throws DBException {
        NetFlowMatchCriteriaDAO criteriaDAO;
        FlowOptionsDAO optionsDAO;
        NetFlowCollector collector;
        PreparedStatement statement;
        int deviceID;
        
        collector = (NetFlowCollector)dataCollector;
        try {
            statement = connection.prepareStatement(CREATE);
            statement.setInt(1, sessionID);
            statement.setString(2, collector.getDevice().getName());
            statement.setLong(3, collector.getPollInterval());
            statement.setString(4, collector.getIfDescr());
            statement.setInt(5, collector.getDirection());
            statement.setInt(6, collector.getTableSize());
            statement.executeUpdate();
            statement.close();
            
            criteriaDAO = DAOFactory.getNetFlowMatchCriteriaDAO(connection);
            criteriaDAO.create(sessionID, collector.getCriteria());
            
            optionsDAO = DAOFactory.getFlowOptionsDAO(connection);
            optionsDAO.create(sessionID, collector.getOptions());
            
        } catch (SQLException e) {
            logger.error("Problem creating NetFlowCollector in database for session " + sessionID);
            throw new DBException("Problem creating NetFlowCollector in database for session " + sessionID, e);
        }
    }
    
    /**
     * Retrieves a NetFlowCollector defintion from the database.
     *
     * @param sessionID      the session ID of the collector
     * @return               the collector created from the database definition
     */
    public NetFlowCollector retrieve(int sessionID) throws DBException, UnknownHostException, SNMPException {
        DeviceDAO deviceDAO;
        NetFlowMatchCriteriaDAO criteriaDAO;
        FlowOptionsDAO optionsDAO;
        String name;
        Device device;
        PortsSelectorSNMP portsSNMP;
        NetFlowSNMP netflowSNMP;
        long pollInterval;
        int ifIndex;
        String ifDescr;
        int direction;
        int tableSize;
        NetFlowMatchCriteria criteria;
        FlowOptions options;
        
        name = (String)(DBUtil.retrieveSingleAttributeWithPK(connection, RETRIEVE_DEVICE, sessionID));
        deviceDAO = DAOFactory.getDeviceDAO();
        device = deviceDAO.retrieveDevice(name, true);
        deviceDAO.closeConnection();
        pollInterval = ((Long)(DBUtil.retrieveSingleAttributeWithPK(connection, RETRIEVE_POLLINTERVAL, sessionID))).longValue();
        ifDescr = (String)(DBUtil.retrieveSingleAttributeWithPK(connection, RETRIEVE_IFDESCR, sessionID));
        direction = ((Integer)(DBUtil.retrieveSingleAttributeWithPK(connection, RETRIEVE_DIRECTION, sessionID))).intValue();
        tableSize = ((Integer)(DBUtil.retrieveSingleAttributeWithPK(connection, RETRIEVE_TABLESIZE, sessionID))).intValue();
        portsSNMP = new PortsSelectorSNMP(device);
        /*
        device.enumeratePorts();
        ifIndex = -1;
        for ( int i = 0 ; i < device.getPortsIndex().length ; i++ ) {
            if ( device.getPortsDescr()[i].equals(ifDescr) ) {
                ifIndex = device.getPortsIndex()[i];
            }
        }
         */
        ifIndex = portsSNMP.getIfIndex(ifDescr);

        criteriaDAO = DAOFactory.getNetFlowMatchCriteriaDAO(connection);
        criteria = criteriaDAO.retrieve(sessionID);
        optionsDAO = DAOFactory.getFlowOptionsDAO(connection);
        options = optionsDAO.retrieve(sessionID);
        netflowSNMP = new NetFlowSNMP(device);
        return new NetFlowCollector(netflowSNMP, ifIndex, ifDescr, direction, tableSize, pollInterval, criteria, options);
    }
    
    /**
     * Closes database connection opened for this DAO.
     */
    public void closeConnection() throws DBException {
        try {
            connection.close();
        } catch (SQLException e) {
            logger.error("Error closing database connection for NetFlowCollector DAO");
            throw new DBException("Error closing database connection for NetFlowCollector DAO", e);
        }
    }
    
}
