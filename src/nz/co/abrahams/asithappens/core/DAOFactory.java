/*
 * DAOFactory.java
 *
 * Created on 21 June 2008, 23:22
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

package nz.co.abrahams.asithappens.core;

import nz.co.abrahams.asithappens.collectors.DataCollectorDAOType;
import nz.co.abrahams.asithappens.storage.DataHeadingsDAO;
import nz.co.abrahams.asithappens.storage.DataSetsDAO;
import nz.co.abrahams.asithappens.storage.DeviceDAO;
import nz.co.abrahams.asithappens.storage.DataSetDAO;
import nz.co.abrahams.asithappens.storage.DataPointDAO;
import nz.co.abrahams.asithappens.storage.DataLabelsDAO;
import nz.co.abrahams.asithappens.storage.LayoutDAO;
import nz.co.abrahams.asithappens.flow.FlowOptionsDAO;
import nz.co.abrahams.asithappens.oid.CustomOIDCollectorDAO;
import nz.co.abrahams.asithappens.oid.CustomOIDDAO;
import nz.co.abrahams.asithappens.netflow.NetFlowMatchCriteriaDAO;
import nz.co.abrahams.asithappens.netflow.NetFlowCollectorDAO;
import nz.co.abrahams.asithappens.nbar.NBARCollectorDAO;
import nz.co.abrahams.asithappens.host.ProcessorHRCollectorDAO;
import nz.co.abrahams.asithappens.host.MemoryUCDCollectorDAO;
import nz.co.abrahams.asithappens.host.MemoryCiscoCollectorDAO;
import nz.co.abrahams.asithappens.host.ProcessorCiscoCollectorDAO;
import nz.co.abrahams.asithappens.host.ProcessorUCDCollectorDAO;
import nz.co.abrahams.asithappens.host.MemoryHRCollectorDAO;
import nz.co.abrahams.asithappens.response.ResponseWindowsCollectorDAO;
import nz.co.abrahams.asithappens.response.ResponseCollectorDAO;
import nz.co.abrahams.asithappens.collectors.DataCollectorDAO;
import nz.co.abrahams.asithappens.collectors.DataCollector;
import nz.co.abrahams.asithappens.cartgraph.TimeSeriesOptionsDAO;
import nz.co.abrahams.asithappens.cartgraph.DataGraphDAO;
import nz.co.abrahams.asithappens.uiutil.SetDisplayDAO;
import nz.co.abrahams.asithappens.bandwidth.BandwidthCollectorDAO;
import java.sql.Connection;
import java.lang.reflect.InvocationTargetException;

/**
 *
 * @author mark
 */
public class DAOFactory {
    
    public static final String RETRIEVE_DATATYPEID = "SELECT dataTypeID FROM Sessions WHERE sessionID = ?";
    public static final String RETRIEVE_COLLECTOR = "SELECT collector FROM Sessions WHERE sessionID = ?";
    public static final String RETRIEVE_COLLECTORDAOID = "SELECT collectorDAOID FROM Sessions WHERE sessionID = ?";
    public static final String DAO_PACKAGE_NAME = "nz.co.abrahams.asithappens";
    
    /** Private constructor - class provides static methods only */
    private DAOFactory() {
    }
    
    public static DataSetsDAO getDataSetsDAO() throws DBException {
        return new DataSetsDAO(DBUtil.getConnection());
    }
    
    public static DataSetsDAO getDataSetsDAO(Connection connection) throws DBException {
        return new DataSetsDAO(connection);
    }
    
    public static DataSetDAO getDataSetDAO() throws DBException {
        return new DataSetDAO(DBUtil.getConnection());
    }
    
    public static DataSetDAO getDataSetDAO(Connection connection) throws DBException {
        return new DataSetDAO(connection);
    }
    
    public static DataPointDAO getDataPointDAO() throws DBException {
        return new DataPointDAO(DBUtil.getConnection());
    }
    
    public static DataPointDAO getDataPointDAO(Connection connection) throws DBException {
        return new DataPointDAO(connection);
    }
    
    public static DataHeadingsDAO getDataHeadingsDAO() throws DBException {
        return new DataHeadingsDAO(DBUtil.getConnection());
    }
    
    public static DataHeadingsDAO getDataHeadingsDAO(Connection connection) throws DBException {
        return new DataHeadingsDAO(connection);
    }
    
    public static DataLabelsDAO getDataLabelsDAO() throws DBException {
        return new DataLabelsDAO(DBUtil.getConnection());
    }
    
    public static DataLabelsDAO getDataLabelsDAO(Connection connection) throws DBException {
        return new DataLabelsDAO(connection);
    }
    
    public static DeviceDAO getDeviceDAO() throws DBException {
        return new DeviceDAO(DBUtil.getConnection());
    }
    
    public static DeviceDAO getDeviceDAO(Connection connection) throws DBException {
        return new DeviceDAO(connection);
    }
    
    public static LayoutDAO getLayoutDAO() throws DBException {
        return new LayoutDAO(DBUtil.getConnection());
    }
    
    public static LayoutDAO getLayoutDAO(Connection connection) throws DBException {
        return new LayoutDAO(connection);
    }
    
    public static DataGraphDAO getDataGraphDAO() throws DBException {
        return new DataGraphDAO(DBUtil.getConnection());
    }
    
    public static DataGraphDAO getDataGraphDAO(Connection connection) throws DBException {
        return new DataGraphDAO(connection);
    }
    
    public static TimeSeriesOptionsDAO getGraphOptionsDAO() throws DBException {
        return new TimeSeriesOptionsDAO(DBUtil.getConnection());
    }
    
    public static TimeSeriesOptionsDAO getGraphOptionsDAO(Connection connection) throws DBException {
        return new TimeSeriesOptionsDAO(connection);
    }
    
    public static SetDisplayDAO getSetDisplayDAO() throws DBException {
        return new SetDisplayDAO(DBUtil.getConnection());
    }
    
    public static SetDisplayDAO getSetDisplayDAO(Connection connection) throws DBException {
        return new SetDisplayDAO(connection);
    }
    
    /*
    public static DataCollectorDAO getDataCollectorDAO() {
        return new DataCollectorDAO(DBUtil.getConnection());
    }
     
    public static DataCollectorDAO getDataCollectorDAO(Connection connection) {
        return new DataCollectorDAO(connection);
    }
     */
    
    public static BandwidthCollectorDAO getBandwidthCollectorDAO(Connection connection) throws DBException {
        return new BandwidthCollectorDAO(connection);
    }
    
    public static BandwidthCollectorDAO getBandwidthCollectorDAO() throws DBException {
        return new BandwidthCollectorDAO(DBUtil.getConnection());
    }
    
    public static ResponseCollectorDAO getResponseCollectorDAO(Connection connection) throws DBException {
        return new ResponseCollectorDAO(connection);
    }
    
    public static ResponseCollectorDAO getResponseCollectorDAO() throws DBException {
        return new ResponseCollectorDAO(DBUtil.getConnection());
    }
    
    public static ResponseWindowsCollectorDAO getResponseWindowsCollectorDAO(Connection connection) throws DBException {
        return new ResponseWindowsCollectorDAO(connection);
    }
    
    public static ResponseWindowsCollectorDAO getResponseWindowsCollectorDAO() throws DBException {
        return new ResponseWindowsCollectorDAO(DBUtil.getConnection());
    }
    
    public static NBARCollectorDAO getNBARCollectorDAO(Connection connection) throws DBException {
        return new NBARCollectorDAO(connection);
    }
    
    public static NBARCollectorDAO getNBARCollectorDAO() throws DBException {
        return new NBARCollectorDAO(DBUtil.getConnection());
    }
    
    public static NetFlowCollectorDAO getNetFlowCollectorDAO(Connection connection) throws DBException {
        return new NetFlowCollectorDAO(connection);
    }
    
    public static NetFlowCollectorDAO getNetFlowCollectorDAO() throws DBException {
        return new NetFlowCollectorDAO(DBUtil.getConnection());
    }
    
    public static NetFlowMatchCriteriaDAO getNetFlowMatchCriteriaDAO(Connection connection) throws DBException {
        return new NetFlowMatchCriteriaDAO(connection);
    }
    
    public static NetFlowMatchCriteriaDAO getNetFlowMatchCriteriaDAO() throws DBException {
        return new NetFlowMatchCriteriaDAO(DBUtil.getConnection());
    }
    
    public static FlowOptionsDAO getFlowOptionsDAO(Connection connection) throws DBException {
        return new FlowOptionsDAO(connection);
    }
    
    public static FlowOptionsDAO getFlowOptionsDAO() throws DBException {
        return new FlowOptionsDAO(DBUtil.getConnection());
    }
    
    public static MemoryCiscoCollectorDAO getMemoryCiscoCollectorDAO(Connection connection) throws DBException {
        return new MemoryCiscoCollectorDAO(connection);
    }
    
    public static MemoryCiscoCollectorDAO getMemoryCiscoCollectorDAO() throws DBException {
        return new MemoryCiscoCollectorDAO(DBUtil.getConnection());
    }
    
    public static MemoryHRCollectorDAO getMemoryHRCollectorDAO(Connection connection) throws DBException {
        return new MemoryHRCollectorDAO(connection);
    }
    
    public static MemoryHRCollectorDAO getMemoryHRCollectorDAO() throws DBException {
        return new MemoryHRCollectorDAO(DBUtil.getConnection());
    }
    
    public static MemoryUCDCollectorDAO getMemoryUCDCollectorDAO(Connection connection) throws DBException {
        return new MemoryUCDCollectorDAO(connection);
    }
    
    public static MemoryUCDCollectorDAO getMemoryUCDCollectorDAO() throws DBException {
        return new MemoryUCDCollectorDAO(DBUtil.getConnection());
    }
    
    public static ProcessorCiscoCollectorDAO getProcessorCiscoCollectorDAO(Connection connection) throws DBException {
        return new ProcessorCiscoCollectorDAO(connection);
    }
    
    public static ProcessorCiscoCollectorDAO getProcessorCiscoCollectorDAO() throws DBException {
        return new ProcessorCiscoCollectorDAO(DBUtil.getConnection());
    }
    
    public static ProcessorHRCollectorDAO getProcessorHRCollectorDAO(Connection connection) throws DBException {
        return new ProcessorHRCollectorDAO(connection);
    }
    
    public static ProcessorHRCollectorDAO getProcessorHRCollectorDAO() throws DBException {
        return new ProcessorHRCollectorDAO(DBUtil.getConnection());
    }
    
    public static ProcessorUCDCollectorDAO getProcessorUCDCollectorDAO(Connection connection) throws DBException {
        return new ProcessorUCDCollectorDAO(connection);
    }
    
    public static ProcessorUCDCollectorDAO getProcessorUCDCollectorDAO() throws DBException {
        return new ProcessorUCDCollectorDAO(DBUtil.getConnection());
    }
    
    public static CustomOIDCollectorDAO getCustomOIDCollectorDAO(Connection connection) throws DBException {
        return new CustomOIDCollectorDAO(connection);
    }

    public static CustomOIDCollectorDAO getCustomOIDCollectorDAO() throws DBException {
        return new CustomOIDCollectorDAO(DBUtil.getConnection());
    }
    
    public static CustomOIDDAO getCustomOIDDAO(Connection connection) throws DBException {
        return new CustomOIDDAO(connection);
    }

    public static CustomOIDDAO getCustomOIDDAO() throws DBException {
        return new CustomOIDDAO(DBUtil.getConnection());
    }
    
    public static DataCollectorDAO getDataCollectorDAO(Connection connection, DataCollector collector) throws DBException, DAOCreationException {
        /*
        String className;
        Class daoClass;
        
        try {
            className = collector.getClass().getName() + "DAO";
            return getDataCollectorDAOInstance(Class.forName(className), connection);
        } catch (ClassNotFoundException e) {
            throw new DAOCreationException("Cannot find DAO class", e);
        }
        */
        Class daoClass;

        daoClass = DataCollectorDAOType.getDAOClass(collector.getClass());
        return getDataCollectorDAOInstance(daoClass, connection);

    }
    
    public static DataCollectorDAO getDataCollectorDAO(int sessionID) throws DBException, DAOCreationException {
        return getDataCollectorDAO(DBUtil.getConnection(), sessionID);
    }
    
    public static DataCollectorDAO getDataCollectorDAO(Connection connection, int sessionID) throws DBException, DAOCreationException {
        /*
        String collectorName;
        Class daoClass;
        DataCollectorDAO collectorDAO;
        
        collectorName = DAO_PACKAGE_NAME + "." + (String)(DBUtil.retrieveSingleAttributeWithPK(connection, RETRIEVE_COLLECTOR, sessionID));
        if ( collectorName.equals(DAO_PACKAGE_NAME + ".ResponseCollector") && Configuration.getProperty("collector.response.class").equals("windows") )
            collectorName = collectorName + "Windows";
        collectorName = collectorName + "DAO";
        try {
            daoClass = Class.forName(collectorName);
            return getDataCollectorDAOInstance(daoClass, connection);
        } catch (ClassNotFoundException e) {
            throw new DAOCreationException("Cannot find DAO class " + collectorName, e);
        }
        */
        int daoID;
        Class daoClass;

        daoID = ((Integer)(DBUtil.retrieveSingleAttributeWithPK(connection, RETRIEVE_COLLECTORDAOID, sessionID))).intValue();
        daoClass = DataCollectorDAOType.getDAOClass(daoID);
        return getDataCollectorDAOInstance(daoClass, connection);
        
    }
    
    private static DataCollectorDAO getDataCollectorDAOInstance(Class daoClass, Connection connection) throws DAOCreationException {
        Class[] types;
        Object[] arguments;
        
        types = new Class[1];
        types[0] = Connection.class;
        
        arguments = new Object[1];
        arguments[0] = connection;
        
        try {
            return (DataCollectorDAO)(daoClass.getConstructor(types).newInstance(arguments));
        } catch (NoSuchMethodException e) {
            throw new DAOCreationException("Cannot find constructor method for DAO instance", e);
        } catch (InstantiationException e) {
            throw new DAOCreationException("Cannot create DAO instance", e);
        } catch (IllegalAccessException e) {
            throw new DAOCreationException("Illegal access exception creating DAO instance", e);
        } catch (InvocationTargetException e) {
            throw new DAOCreationException("Invocation target exception creating DAO instance", e);
        }
    }
}
