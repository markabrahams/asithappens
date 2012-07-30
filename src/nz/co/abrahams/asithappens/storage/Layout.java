/*
 * Layout.java
 *
 * Created on 21 October 2007, 21:38
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
import nz.co.abrahams.asithappens.snmputil.SNMPException;
import nz.co.abrahams.asithappens.core.DAOCreationException;
import nz.co.abrahams.asithappens.core.DBUtil;
import nz.co.abrahams.asithappens.core.DBException;
import nz.co.abrahams.asithappens.cartgraph.DataGraphDAO;
import nz.co.abrahams.asithappens.cartgraph.DataGraph;
import java.util.*;
import java.net.*;
import java.sql.Connection;
import org.apache.log4j.Logger;

/**
 *
 * @author mark
 */
public class Layout {
    
    /** Graph number */
    //private static int graphID = 0;
    
    /** Logging provider */
    protected static final Logger logger = Logger.getLogger(Layout.class);
    
    /** Graph list */
    private static Vector<DataGraph> list = new Vector();
    
    /** Creates a new instance of Layout */
    public Layout() {
    }
    
    public static synchronized void addGraphToCurrent(DataGraph graph) {
        list.add(graph);
    }
    
    public static synchronized void removeGraphFromCurrent(DataGraph graph) {
        list.remove(graph);
    }
    
    public static synchronized void saveLayout(String layout) throws DBException, UnknownHostException, DAOCreationException {
        LayoutDAO layoutDAO;
        DataGraphDAO graphDAO;
        DataGraph graph;
        Iterator<DataGraph> iterator;
        
        layoutDAO = DAOFactory.getLayoutDAO();
        layoutDAO.create(layout, list.iterator());
        layoutDAO.closeConnection();
        
        /*
        while ( iterator.hasNext() ) {
            graph = iterator.next();
            logger.info("Graph: " + graph.getContext().getData().getTitle());
            if ( graph.getContext().getData().isCollector() ) {
                logger.info("  (a collector)");
                //collector = graph.getContext().getData().getCollector();
                //graph.getContext().getData().getCollector().store(layout);
                //graph.store(layout);
                graphDAO.create(layout);
            } else
                logger.info("  (not a collector)");
        }
         */
        
    }
    
    public static synchronized void loadLayout(String name) throws DBException, UnknownHostException, SNMPException, DAOCreationException {
        Connection connection;
        LayoutDAO layoutDAO;
        DataGraphDAO graphDAO;
        int[] graphIDs;
        
        connection = DBUtil.getConnection();
        logger.debug("Loading layout \'" + name + "\'");
        layoutDAO = DAOFactory.getLayoutDAO(connection);
        graphIDs = layoutDAO.retrieveLayoutGraphs(name);
        //layoutDAO.closeConnection();
        
        graphDAO = DAOFactory.getDataGraphDAO(connection);
        for ( int graph = 0 ; graph < graphIDs.length ; graph++ ) {
            graphDAO.retrieveGraph(graphIDs[graph], null);
        }
        graphDAO.closeConnection();
        
    }
    
    public static synchronized String[] getLayoutsList() throws DBException {
        LayoutDAO layoutDAO;
        String[] layoutList;
        
        layoutDAO = DAOFactory.getLayoutDAO();
        layoutList = layoutDAO.retrieveLayoutList();
        layoutDAO.closeConnection();
        return layoutList;
        
    }
    
    public static synchronized void deleteLayout(String name) throws DBException {
        LayoutDAO layoutDAO;
        
        layoutDAO = DAOFactory.getLayoutDAO();
        layoutDAO.deleteLayout(name);
        layoutDAO.closeConnection();
        
    }
    
}
