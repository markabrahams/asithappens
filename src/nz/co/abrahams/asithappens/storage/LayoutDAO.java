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

import nz.co.abrahams.asithappens.core.DAOFactory;
import nz.co.abrahams.asithappens.core.DAOCreationException;
import nz.co.abrahams.asithappens.core.DBException;
import nz.co.abrahams.asithappens.cartgraph.DataGraphDAO;
import nz.co.abrahams.asithappens.cartgraph.DataGraph;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.net.UnknownHostException;
import java.util.Vector;
import java.util.Iterator;
import org.apache.log4j.Logger;

/**
 *
 * @author mark
 */
public class LayoutDAO {
    
    public static final String CREATE = "INSERT INTO Layouts (layoutName) VALUES (?)";
    
    public static final String RETRIEVE_GRAPHS = "SELECT graphID FROM LayoutGraphs WHERE layoutName = ?";
    public static final String RETRIEVE_LIST = "SELECT layoutName FROM Layouts ORDER BY layoutName";
    
    public static final String RETRIEVE_SESSIONS = "SELECT sessionID FROM Graphs, LayoutGraphs WHERE Graphs.graphID = LayoutGraphs.graphID AND layoutName = ?";
    
    public static final String DELETE_BANDWIDTHCOLLECTORS = "DELETE FROM BandwidthCollectors WHERE sessionID = ?";
    public static final String DELETE_BANDWIDTHCOLLECTORSPORTS = "DELETE FROM BandwidthCollectors WHERE sessionID = ?";
    public static final String DELETE_RESPONSECOLLECTORS = "DELETE FROM ResponseCollectors WHERE sessionID = ?";
    public static final String DELETE_NBARCOLLECTORS = "DELETE FROM NbarCollectors WHERE sessionID = ?";
    public static final String DELETE_NETFLOWCOLLECTORS = "DELETE FROM NetflowCollectors WHERE sessionID = ?";
    public static final String DELETE_NETFLOWMATCHCRITERIA = "DELETE FROM NetflowMatchCriteria WHERE sessionID = ?";
    public static final String DELETE_FLOWOPTIONS = "DELETE FROM FlowOptions WHERE sessionID = ?";
    public static final String DELETE_PROCESSORCISCOCOLLECTORS = "DELETE FROM ProcessorCiscoCollectors WHERE sessionID = ?";
    public static final String DELETE_PROCESSORHRCOLLECTORS = "DELETE FROM ProcessorHRCollectors WHERE sessionID = ?";
    public static final String DELETE_PROCESSORUCDCOLLECTORS = "DELETE FROM ProcessorUCDCollectors WHERE sessionID = ?";
    public static final String DELETE_MEMORYCISCOCOLLECTORS = "DELETE FROM MemoryCiscoCollectors WHERE sessionID = ?";
    public static final String DELETE_MEMORYHRCOLLECTORS = "DELETE FROM MemoryHRCollectors WHERE sessionID = ?";
    public static final String DELETE_MEMORYUCDCOLLECTORS = "DELETE FROM MemoryUCDCollectors WHERE sessionID = ?";
    public static final String DELETE_CUSTOMOIDCOLLECTORS = "DELETE FROM CustomOIDCollectors WHERE sessionID = ?";
    public static final String DELETE_CUSTOMOIDCOLLECTOROIDS = "DELETE FROM CustomOIDCollectorOIDs WHERE sessionID = ?";
        
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
        ResultSet sessionsResults;
        Vector<Integer> sessionIDs;
        int sessionID;
        int[] graphIDs;
        
        try {
            statement = connection.prepareStatement(RETRIEVE_SESSIONS);
            statement.setString(1, name);
            sessionsResults = statement.executeQuery();
            sessionIDs = new Vector();
            while ( sessionsResults.next() ) {
                sessionIDs.add(new Integer(sessionsResults.getInt(1)));
            }
            sessionsResults.close();
            statement.close();
            
            for ( int i = 0 ; i < sessionIDs.size() ; i++ ) {
                sessionID = sessionIDs.elementAt(i);
                deletebySession(DELETE_BANDWIDTHCOLLECTORS, sessionID);
                deletebySession(DELETE_BANDWIDTHCOLLECTORSPORTS, sessionID);
                deletebySession(DELETE_RESPONSECOLLECTORS, sessionID);
                deletebySession(DELETE_NBARCOLLECTORS, sessionID);
                deletebySession(DELETE_NETFLOWCOLLECTORS, sessionID);
                deletebySession(DELETE_NETFLOWMATCHCRITERIA, sessionID);
                deletebySession(DELETE_FLOWOPTIONS, sessionID);
                deletebySession(DELETE_PROCESSORCISCOCOLLECTORS, sessionID);
                deletebySession(DELETE_PROCESSORHRCOLLECTORS, sessionID);
                deletebySession(DELETE_PROCESSORUCDCOLLECTORS, sessionID);
                deletebySession(DELETE_MEMORYCISCOCOLLECTORS, sessionID);
                deletebySession(DELETE_MEMORYHRCOLLECTORS, sessionID);
                deletebySession(DELETE_MEMORYUCDCOLLECTORS, sessionID);
                deletebySession(DELETE_CUSTOMOIDCOLLECTOROIDS, sessionID);
                deletebySession(DELETE_CUSTOMOIDCOLLECTORS, sessionID);
            }
            
            /*
            statement = connection.prepareStatement(DELETE_GRAPHS);
            statement.setString(1, name);
            statement.executeUpdate();
            statement.close();
             */
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
