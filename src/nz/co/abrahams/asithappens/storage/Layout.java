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

import java.net.UnknownHostException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;
import nz.co.abrahams.asithappens.cartgraph.DataGraph;
import nz.co.abrahams.asithappens.cartgraph.DataGraphDAO;
import nz.co.abrahams.asithappens.core.DAOCreationException;
import nz.co.abrahams.asithappens.core.DAOFactory;
import nz.co.abrahams.asithappens.core.DBException;
import nz.co.abrahams.asithappens.core.DBUtil;
import nz.co.abrahams.asithappens.snmputil.SNMPException;
import org.apache.log4j.Logger;

/**
 *
 * @author mark
 */
public class Layout {
    
    /** Logging provider */
    protected static final Logger logger = Logger.getLogger(Layout.class);
    
    /** List of current graphs */
    private static ArrayList<DataGraph> list = new ArrayList();
    
    /** Creates a new instance of Layout */
    public Layout() {
    }
    
    public static synchronized void addGraphToCurrent(DataGraph graph) {
        list.add(graph);
    }
    
    public static synchronized void removeGraphFromCurrent(DataGraph graph) {
        list.remove(graph);
    }
    
    public static ArrayList<DataGraph> getCurrentGraphs() {
        return list;
    }
    
    public static synchronized boolean currentIsEmpty() {
        return list.isEmpty();
    }
    
    public static synchronized void saveLayout(String layout) throws DBException, UnknownHostException, DAOCreationException {
        LayoutDAO layoutDAO;
        
        layoutDAO = DAOFactory.getLayoutDAO();
        layoutDAO.create(layout, list.iterator());
        layoutDAO.closeConnection();
                
    }
    
    public static synchronized boolean exists(String layout) throws DBException, DAOCreationException {
        LayoutDAO layoutDAO;
        boolean exists;
        
        layoutDAO = DAOFactory.getLayoutDAO();
        exists = layoutDAO.exists(layout);
        layoutDAO.closeConnection();
        return exists;
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
