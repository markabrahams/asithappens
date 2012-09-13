/*
 * DataGraphDAO.java
 *
 * Created on 25 June 2008, 20:41
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

package nz.co.abrahams.asithappens.cartgraph;

import nz.co.abrahams.asithappens.storage.DataSetsDAO;
import nz.co.abrahams.asithappens.storage.DataSets;
import nz.co.abrahams.asithappens.storage.LayoutDAO;
import nz.co.abrahams.asithappens.core.DAOFactory;
import nz.co.abrahams.asithappens.snmputil.SNMPException;
import nz.co.abrahams.asithappens.core.DAOCreationException;
import nz.co.abrahams.asithappens.core.DBUtil;
import nz.co.abrahams.asithappens.core.DBException;
import nz.co.abrahams.asithappens.uiutil.SetDisplay;
import nz.co.abrahams.asithappens.sdn.SDNContainerPanel;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.net.UnknownHostException;
import java.util.Vector;
import java.awt.Rectangle;
import nz.co.abrahams.asithappens.collectors.CollectorDefinition;
import nz.co.abrahams.asithappens.collectors.CollectorDefinitionDAO;
import nz.co.abrahams.asithappens.collectors.DataCollector;
import nz.co.abrahams.asithappens.storage.DeviceDAO;
import org.apache.log4j.Logger;

/**
 *
 * @author mark
 */
public class DataGraphDAO {
    
    public static final String CREATE_GRAPH = "INSERT INTO Graphs (isTemplate, sessionID, collectorID, graphOptionsID, x, y, width, height) " +
            "VALUES (?,?,?,?,?,?,?,?)";
    
    //public static final String CREATE_GRAPH_SETDISPLAY = "INSERT INTO GraphSetDisplays (graphID, setNumber, setDisplayID) VALUES (?,?,?)";
    public static final String CREATE_LAYOUT_GRAPH = "INSERT INTO LayoutGraphs (layoutName, graphID) VALUES (?,?)";
    
    public static final String UPDATE_GRAPH = "UPDATE Graphs SET x = ?, y = ?, width = ?, height = ? " +
            "WHERE graphID = ?";
    
    public static final String RETRIEVE_GRAPH_ID = "SELECT graphID FROM Graphs WHERE sessionID = ?";
    
    public static final String RETRIEVE_SESSION_ID = "SELECT sessionID FROM Graphs WHERE graphID = ?";
    
    public static final String RETRIEVE_COLLECTOR_ID = "SELECT collectorID FROM Graphs WHERE graphID = ?";
    
    public static final String RETRIEVE_OPTIONS_ID = "SELECT graphOptionsID FROM Graphs WHERE graphID = ?";
    
    public static final String RETRIEVE_SETDISPLAYIDS = "SELECT setNumber, setDisplayID FROM GraphSetDisplays " +
            "WHERE graphID = ? ORDER BY setNumber";
    
    //public static final String RETRIEVE_SETDISPLAYID = "SELECT setDisplayID FROM GraphSetDisplays " +
    //        "WHERE graphID = ? AND setNumber = ?";
    
    public static final String RETRIEVE_GRAPH = "SELECT sessionID, graphOptionsID, x, y, width, height " +
            "FROM Graphs WHERE graphID = ?";
    
    public static final String RETRIEVE_SETDISPLAYS = "SELECT setNumber, style, color FROM " +
            "Graphs, GraphSetDisplays, SetDisplays WHERE graphs.graphID = GraphSetDisplays.graphID " +
            "AND GraphSetDisplays.setDisplayID = SetDisplays.setDisplayID " +
            "AND Graphs.graphID = ? ORDER BY GraphSetDisplays.setNumber";
    
    /** Logging provider */
    public static Logger logger = Logger.getLogger(DataGraphDAO.class);
    
    /** Database connection */
    Connection connection;
    
    /** Creates a new DataGraph DAO */
    public DataGraphDAO(Connection connection) {
        this.connection = connection;
    }
    
    public int createGraph(String layoutName, DataGraph graph) throws DBException, UnknownHostException, DAOCreationException {
        LayoutDAO layoutDAO;
        DeviceDAO deviceDAO;
        DataSetsDAO dataSetsDAO;
        CollectorDefinitionDAO definitionDAO;
        TimeSeriesOptionsDAO optionsDAO;
        PreparedStatement statement;
        ResultSet results;
        TimeSeriesContext context;
        int graphID;
        int sessionID;
        int collectorID;
        int graphOptionsID;
        boolean isTemplate;
        
        try {
            context = graph.getContext();
            if ( context.getData().isCollector() ) {
                // Store graph context as part of a layout
                if ( layoutName != null ) {
                    isTemplate = true;
                    //sessionID = context.getData().storeTemplate();
                    //dataSetsDAO = DAOFactory.getDataSetsDAO();
                    //sessionID = dataSetsDAO.createTemplate(context.getData());
                    //dataSetsDAO.closeConnection();
                    
                    deviceDAO = DAOFactory.getDeviceDAO(connection);
                    deviceDAO.create(context.getData().getCollector().getDefinition().getDevice());

                    definitionDAO = DAOFactory.getCollectorDefinitionDAO(connection, context.getData().getCollector().getDefinition());
                    collectorID = definitionDAO.create(context.getData().getCollector().getDefinition());
                    sessionID = 0;
                    
                // All stored collector sessions get their graph context stored
                } else {
                    isTemplate = false;
                    sessionID = context.getData().getSessionID();
                    //collectorID = context.getData().getCollector().store();
                    collectorID = 0;
                }
            } else {
                logger.info("Storing graphs of only collector sessions is supported");
                return -1;
            }
            
            optionsDAO = DAOFactory.getGraphOptionsDAO(connection);
            graphOptionsID = optionsDAO.createGraphOptions(context.getOptions());
            
            statement = connection.prepareStatement(CREATE_GRAPH, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, isTemplate ? 1 : 0);
            statement.setInt(2, sessionID);
            statement.setInt(3, collectorID);
            statement.setInt(4, graphOptionsID);
            statement.setInt(5, graph.getX());
            statement.setInt(6, graph.getY());
            statement.setInt(7, graph.getWidth());
            statement.setInt(8, graph.getHeight());
            statement.executeUpdate();
            results = statement.getGeneratedKeys();
            results.next();
            graphID = results.getInt(1);
            results.close();
            statement.close();
            logger.debug("Adding new graph with ID " + graphID);
            
            if ( layoutName != null ) {
                layoutDAO = DAOFactory.getLayoutDAO();
                statement = connection.prepareStatement(CREATE_LAYOUT_GRAPH);
                statement.setString(1, layoutName);
                statement.setInt(2, graphID);
                statement.executeUpdate();
                statement.close();
            }
            
            return graphID;
        } catch (SQLException e) {
            logger.error("Problem creating data graph in database");
            throw new DBException("Problem creating data graph in database", e);
        }
    }
    
    public void updateGraph(DataGraph graph) throws DBException {
        TimeSeriesOptionsDAO optionsDAO;
        PreparedStatement statement;
        ResultSet results;
        TimeSeriesContext context;
        int graphID;
        int sessionID;
        int graphOptionsID;
        int setDisplayID;
        
        try {
            context = graph.getContext();
            graphID = graph.getGraphID();
            
            statement = connection.prepareStatement(RETRIEVE_OPTIONS_ID);
            statement.setInt(1, graphID);
            results = statement.executeQuery();
            results.next();
            graphOptionsID = results.getInt(1);
            
            optionsDAO = DAOFactory.getGraphOptionsDAO(connection);
            optionsDAO.updateGraphOptions(graphOptionsID, context.getOptions());
            
            statement = connection.prepareStatement(UPDATE_GRAPH);
            statement.setInt(1, graph.getX());
            statement.setInt(2, graph.getY());
            statement.setInt(3, graph.getWidth());
            statement.setInt(4, graph.getHeight());
            statement.setInt(5, graphID);
            statement.executeUpdate();
            statement.close();
            logger.debug("Updating graph with ID " + graphID);
            
            /*
            statement = connection.prepareStatement(RETRIEVE_SETDISPLAYIDS);
            statement.setInt(1, graphID);
            results = statement.executeQuery();
            */
            
        } catch (SQLException e) {
            logger.error("Problem updating data graph in database");
            throw new DBException("Problem updating data graph in database", e);
        }
        
    }
    
    /**
     * Loads graph ID for context information for the given session.
     *
     * @param sessionID  the desired session ID
     * @return           the graph ID
     */
    public int retrieveGraphIDForSession(int sessionID) throws DBException {
        return ((Integer)(DBUtil.retrieveSingleAttributeWithPK(connection, RETRIEVE_GRAPH_ID, sessionID))).intValue();
    }
    
    public DataGraph retrieveGraph(int graphID, DataSets dataSets) throws DBException, UnknownHostException, SNMPException, DAOCreationException {
        PreparedStatement statement;
        ResultSet results;
        int sessionID;
        int collectorID;
        int graphOptionsID;
        TimeSeriesContext context;
        TimeSeriesOptions options;
        Rectangle graphRectangle;
        DataGraph graph;
        CollectorDefinitionDAO definitionDAO;
        CollectorDefinition definition;
        DataCollector collector;
        DataSetsDAO dataSetsDAO;
        TimeSeriesOptionsDAO optionsDAO;
        TimeSeriesContext.XAxisScaling xAxisScaling;
        
        try {
            statement = connection.prepareStatement(RETRIEVE_SESSION_ID);
            statement.setInt(1, graphID);
            results = statement.executeQuery();
            results.next();
            sessionID = results.getInt("sessionID");
            results.close();
            statement.close();
            // If from layout, create the empty DataSets from a template
            if ( dataSets == null ) {
                //dataSetsDAO = DAOFactory.getDataSetsDAO(connection);
                //dataSets = dataSetsDAO.retrieveTemplate(sessionID);
                
                collectorID = DBUtil.retrieveIntWithPK(connection, RETRIEVE_COLLECTOR_ID, graphID);
                definitionDAO = DAOFactory.getCollectorDefinitionDAO(connection, collectorID);
                definition = definitionDAO.retrieve(collectorID);
                collector = definition.spawnCollector();
                dataSets = new DataSets(collector);
                
                xAxisScaling = TimeSeriesContext.XAxisScaling.ConstantPixelWidth;
            // if from saved session we already have data loaded
            } else {
                xAxisScaling = TimeSeriesContext.XAxisScaling.AbsoluteBoundaries;
            }
            
            statement = connection.prepareStatement(RETRIEVE_GRAPH);
            statement.setInt(1, graphID);
            results = statement.executeQuery();
            results.next();
            context = new TimeSeriesContext(dataSets);
            
            graphOptionsID = results.getInt("graphOptionsID");
            graphRectangle = new Rectangle(results.getInt("x"), results.getInt("y"), results.getInt("width"), results.getInt("height"));
            results.close();
            statement.close();
            
            optionsDAO = DAOFactory.getGraphOptionsDAO(connection);
            options = optionsDAO.retrieveGraphOptions(graphOptionsID);
            context.setOptions(options);
            
            logger.debug(context.toString());
            graph = new DataGraph(context, graphRectangle);
            return graph;
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("Problem loading graph with id: " + graphID);
            throw new DBException("Problem loading graph with id: " + graphID);
        }
    }
    
    /**
     * Closes database connection opened for this DAO.
     */
    public void closeConnection() throws DBException {
        try {
            connection.close();
        } catch (SQLException e) {
            logger.error("Error closing database connection for DataGraph DAO");
            throw new DBException("Error closing database connection for DataGraph DAO", e);
        }
    }
    
}
