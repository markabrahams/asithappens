/*
 * LayoutDAO.java
 *
 * Created on 25 June 2008, 17:43
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

import java.net.UnknownHostException;
import java.sql.*;
import java.util.Iterator;
import java.util.Vector;
import nz.co.abrahams.asithappens.cartgraph.DataGraph;
import nz.co.abrahams.asithappens.cartgraph.DataGraphDAO;
import nz.co.abrahams.asithappens.collectors.CollectorDefinitionDAO;
import nz.co.abrahams.asithappens.core.DAOCreationException;
import nz.co.abrahams.asithappens.core.DAOFactory;
import nz.co.abrahams.asithappens.core.DBException;
import org.apache.log4j.Logger;

/**
 *
 * @author mark
 */
public class LayoutDAO {
    
    public static final String CREATE = "INSERT INTO Layouts (layoutName) VALUES (?)";
    
    public static final String RETRIEVE = "SELECT layoutName FROM Layouts WHERE layoutName = ?";
    public static final String RETRIEVE_GRAPHS = "SELECT graphID FROM LayoutGraphs WHERE layoutName = ?";
    public static final String RETRIEVE_LIST = "SELECT layoutName FROM Layouts ORDER BY layoutName";
    
    public static final String RETRIEVE_COLLECTORS = "SELECT CollectorID FROM Graphs, LayoutGraphs WHERE Graphs.graphID = LayoutGraphs.graphID AND layoutName = ?";
    
    //public static final String DELETE_GRAPHS = "DELETE Graphs FROM Graphs, LayoutGraphs WHERE Graphs.graphID = LayoutGraphs.graphID AND layoutName = ?";
    public static final String DELETE_GRAPH = "DELETE FROM Graphs WHERE graphID = ?";
    public static final String DELETE_LAYOUTGRAPHS = "DELETE FROM LayoutGraphs WHERE layoutName = ?";
    public static final String DELETE_LAYOUT = "DELETE FROM Layouts WHERE layoutName = ?";
    
    /** Logging provider */
    public static Logger logger = Logger.getLogger(LayoutDAO.class);
    
    /** Database connection */
    Connection connection;
    
    /** Creates a new Layout DAO */
    public LayoutDAO(Connection connection) {
        this.connection = connection;
    }
    
    public void create(String layout, Iterator<DataGraph> iterator) throws DBException, UnknownHostException, DAOCreationException {
        PreparedStatement statement;
        DataGraph graph;
        DataGraphDAO graphDAO;
        
        try {
            statement = connection.prepareStatement(CREATE);
            statement.setString(1, layout);
            statement.executeUpdate();
            
            graphDAO = DAOFactory.getDataGraphDAO();
            while ( iterator.hasNext() ) {
                graph = iterator.next();
                logger.info("Graph: " + graph.getContext().getData().getTitle());
                if ( graph.getContext().getData().isCollector() ) {
                    logger.info("  (a collector)");
                    //collector = graph.getContext().getData().getCollector();
                    //graph.getContext().getData().getCollector().store(layout);
                    //graph.store(layout);
                    graphDAO.createGraph(layout, graph);
                } else
                    logger.info("  (not a collector)");
            }
        } catch (SQLException e) {
            logger.error("Problem creating layout " + layout + " in database");
            throw new DBException("Problem creating layout " + layout + " in database", e);
        }
        
    }
    
    /**
     * Retrieves the list of graph ID's that make up the given layout
     *
     * @param layout  the name of the layout
     * @return        an array containing the graph ID's
     */
    public int[] retrieveLayoutGraphs(String layout) throws DBException {
        PreparedStatement statement;
        ResultSet results;
        //DataGraph[] graphs;
        int[] graphIDs;
        int index;
        int count;
        Vector<Integer> graphVector;
        
        try {
            statement = connection.prepareStatement(RETRIEVE_GRAPHS);
            statement.setString(1, layout);
            results = statement.executeQuery();
            graphVector = new Vector();
            while ( results.next() ) {
                logger.debug("Retrieving graph with ID " + results.getInt(1));
                graphVector.add(new Integer(results.getInt(1)));
            }
            
            graphIDs = new int[graphVector.size()];
            for ( int i = 0 ; i < graphVector.size() ; i++ ) {
                graphIDs[i] = graphVector.elementAt(i).intValue();
            }
            
            return graphIDs;
        } catch (SQLException e) {
            throw new DBException("Problem loading graphs for layout " + layout, e);
        }
    }
    
    public String[] retrieveLayoutList() throws DBException {
        Statement statement;
        ResultSet results;
        Vector layoutsVector;
        String[] layouts;
        
        try {
            layoutsVector = new Vector();
            statement = connection.createStatement();
            results = statement.executeQuery(RETRIEVE_LIST);
            while (results.next())
                layoutsVector.add(new String(results.getString("layoutName")));
            layouts = new String[layoutsVector.size()];
            for ( int layout = 0 ; layout < layoutsVector.size() ; layout++ )
                layouts[layout] = ((String)(layoutsVector.elementAt(layout)));
            results.close();
            statement.close();
            return layouts;
        } catch (SQLException e) {
            logger.error("Cannot retrieve layout list from database");
            throw new DBException("Cannot retrieve layout list from database", e);
        }
    }
    
    public boolean deleteLayout(String name) throws DBException {
        PreparedStatement statement;
        ResultSet collectorsResults;
        Vector<Integer> collectorIDs;
        int collectorID;
        int[] graphIDs;
        DataSetsDAO dataSetsDAO;
        CollectorDefinitionDAO definitionDAO;
        
        try {
            statement = connection.prepareStatement(RETRIEVE_COLLECTORS);
            statement.setString(1, name);
            collectorsResults = statement.executeQuery();
            collectorIDs = new Vector();
            while ( collectorsResults.next() ) {
                collectorIDs.add(new Integer(collectorsResults.getInt(1)));
            }
            collectorsResults.close();
            statement.close();
            
            for ( int i = 0 ; i < collectorIDs.size() ; i++ ) {
                collectorID = collectorIDs.elementAt(i);
                definitionDAO = DAOFactory.getCollectorDefinitionDAO(connection, collectorID);
                definitionDAO.delete(collectorID);
            }
            
            graphIDs = retrieveLayoutGraphs(name);
            for ( int i = 0 ; i < graphIDs.length ; i++ ) {
                statement = connection.prepareStatement(DELETE_GRAPH);
                statement.setInt(1, graphIDs[i]);
                statement.executeUpdate();
                statement.close();
            }
            
            statement = connection.prepareStatement(DELETE_LAYOUTGRAPHS);
            statement.setString(1, name);
            statement.executeUpdate();
            statement.close();
            
            statement = connection.prepareStatement(DELETE_LAYOUT);
            statement.setString(1, name);
            statement.executeUpdate();
            statement.close();
            return true;
        } catch (SQLException e) {
            logger.error("Problem deleting layout " + name);
            throw new DBException("Cannot delete layout " + name + " from database", e);
        } catch (DAOCreationException e) {
            logger.error("Problem deleting layout " + name);
            throw new DBException("Cannot delete layout " + name + " from database", e);
        }
    }
    
    public boolean exists(String name) throws DBException {
        PreparedStatement statement;
        ResultSet results;
        boolean exists;
        
        try {
            statement = connection.prepareStatement(RETRIEVE);
            statement.setString(1, name);
            results = statement.executeQuery();
            exists = results.next();
            results.close();
            statement.close();
            return exists;
        } catch (SQLException e) {
            logger.error("Database error checking for layout " + name);
            throw new DBException("Database error checking for layout " + name, e);
        }
        
    }
    
    private void deletebySession(String deleteString, int sessionID) throws SQLException {
        PreparedStatement statement;
        
        statement = connection.prepareStatement(deleteString);
        statement.setInt(1, sessionID);
        statement.executeUpdate();
        statement.close();
        
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
