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

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import nz.co.abrahams.asithappens.bandwidth.BandwidthCollectorDefinitionDAO;
import nz.co.abrahams.asithappens.cartgraph.DataGraphDAO;
import nz.co.abrahams.asithappens.cartgraph.TimeSeriesOptionsDAO;
import nz.co.abrahams.asithappens.collectors.CollectorDefinition;
import nz.co.abrahams.asithappens.collectors.CollectorDefinitionDAO;
import nz.co.abrahams.asithappens.collectors.CollectorDefinitionDAOType;
import nz.co.abrahams.asithappens.flow.FlowOptionsDAO;
import nz.co.abrahams.asithappens.host.*;
import nz.co.abrahams.asithappens.nbar.NBARCollectorDefinitionDAO;
import nz.co.abrahams.asithappens.netflow.NetFlowCollectorDefinitionDAO;
import nz.co.abrahams.asithappens.netflow.NetFlowMatchCriteriaDAO;
import nz.co.abrahams.asithappens.oid.CustomOIDCollectorDefinitionDAO;
import nz.co.abrahams.asithappens.oid.CustomOIDDAO;
import nz.co.abrahams.asithappens.response.ResponseCollectorDefinitionDAO;
import nz.co.abrahams.asithappens.snmputil.USMUserDAO;
import nz.co.abrahams.asithappens.storage.*;
import nz.co.abrahams.asithappens.uiutil.SetDisplayDAO;

/**
 *
 * @author mark
 */
public class DAOFactory {
    
    public static final String RETRIEVE_DATATYPEID = "SELECT dataTypeID FROM Sessions WHERE sessionID = ?";
    public static final String RETRIEVE_COLLECTOR = "SELECT collector FROM Sessions WHERE sessionID = ?";
    //public static final String RETRIEVE_COLLECTOR_TYPE = "SELECT CollectorDefinitions.collectorType FROM Sessions, CollectorDefinitions WHERE sessionID = ? AND Sessions.collectorID = CollectorDefinitions.collectorID";
    public static final String RETRIEVE_COLLECTOR_TYPE = "SELECT collectorType FROM CollectorDefinitions WHERE collectorID = ?";
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
    
    public static USMUserDAO getUSMUserDAO(Connection connection) throws DBException {
        return new USMUserDAO(connection);
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
    
    public static BandwidthCollectorDefinitionDAO getBandwidthDefinitionCollectorDAO(Connection connection) throws DBException {
        return new BandwidthCollectorDefinitionDAO(connection);
    }
    
    public static BandwidthCollectorDefinitionDAO getBandwidthDefinitionCollectorDAO() throws DBException {
        return new BandwidthCollectorDefinitionDAO(DBUtil.getConnection());
    }
    
    /*
    public static CollectorDefinitionDAO getCollectorDefinitionDAO() throws DBException {
        return new CollectorDefinitionDAO(DBUtil.getConnection());
    }
     
    public static CollectorDefinitionDAO getCollectorDefinitionDAO(Connection connection) {
        return new CollectorDefinitionDAO(connection);
    }
    */
    
    /*
    public static InterfaceDirectionCollectorDefinitionDAO getInterfaceDirectionCollectorDefinitionDAO() throws DBException {
        return new InterfaceDirectionCollectorDefinitionDAO(DBUtil.getConnection());
    }
     
    public static InterfaceDirectionCollectorDefinitionDAO getInterfaceDirectionCollectorDefinitionDAO(Connection connection) {
        return new InterfaceDirectionCollectorDefinitionDAO(connection);
    }
    
    public static BandwidthCollectorDAO getBandwidthCollectorDAO(Connection connection) throws DBException {
        return new BandwidthCollectorDAO(connection);
    }
    
    public static BandwidthCollectorDAO getBandwidthCollectorDAO() throws DBException {
        return new BandwidthCollectorDAO(DBUtil.getConnection());
    }
    */
    
    public static ResponseCollectorDefinitionDAO getResponseCollectorDAO(Connection connection) throws DBException {
        return new ResponseCollectorDefinitionDAO(connection);
    }
    
    public static ResponseCollectorDefinitionDAO getResponseCollectorDAO() throws DBException {
        return new ResponseCollectorDefinitionDAO(DBUtil.getConnection());
    }
    
    /*
    public static ResponseWindowsCollectorDAO getResponseWindowsCollectorDAO(Connection connection) throws DBException {
        return new ResponseWindowsCollectorDAO(connection);
    }
    
    public static ResponseWindowsCollectorDAO getResponseWindowsCollectorDAO() throws DBException {
        return new ResponseWindowsCollectorDAO(DBUtil.getConnection());
    }
    */
    
    public static NBARCollectorDefinitionDAO getNBARCollectorDAO(Connection connection) throws DBException {
        return new NBARCollectorDefinitionDAO(connection);
    }
    
    public static NBARCollectorDefinitionDAO getNBARCollectorDAO() throws DBException {
        return new NBARCollectorDefinitionDAO(DBUtil.getConnection());
    }
    
    public static NetFlowCollectorDefinitionDAO getNetFlowCollectorDAO(Connection connection) throws DBException {
        return new NetFlowCollectorDefinitionDAO(connection);
    }
    
    public static NetFlowCollectorDefinitionDAO getNetFlowCollectorDAO() throws DBException {
        return new NetFlowCollectorDefinitionDAO(DBUtil.getConnection());
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
    
    public static MemoryCiscoCollectorDefinitionDAO getMemoryCiscoCollectorDAO(Connection connection) throws DBException {
        return new MemoryCiscoCollectorDefinitionDAO(connection);
    }
    
    public static MemoryCiscoCollectorDefinitionDAO getMemoryCiscoCollectorDAO() throws DBException {
        return new MemoryCiscoCollectorDefinitionDAO(DBUtil.getConnection());
    }
    
    public static MemoryHRCollectorDefinitionDAO getMemoryHRCollectorDAO(Connection connection) throws DBException {
        return new MemoryHRCollectorDefinitionDAO(connection);
    }
    
    public static MemoryHRCollectorDefinitionDAO getMemoryHRCollectorDAO() throws DBException {
        return new MemoryHRCollectorDefinitionDAO(DBUtil.getConnection());
    }
    
    public static MemoryUCDCollectorDefinitionDAO getMemoryUCDCollectorDAO(Connection connection) throws DBException {
        return new MemoryUCDCollectorDefinitionDAO(connection);
    }
    
    public static MemoryUCDCollectorDefinitionDAO getMemoryUCDCollectorDAO() throws DBException {
        return new MemoryUCDCollectorDefinitionDAO(DBUtil.getConnection());
    }
    
    public static ProcessorCiscoCollectorDefinitionDAO getProcessorCiscoCollectorDAO(Connection connection) throws DBException {
        return new ProcessorCiscoCollectorDefinitionDAO(connection);
    }
    
    public static ProcessorCiscoCollectorDefinitionDAO getProcessorCiscoCollectorDAO() throws DBException {
        return new ProcessorCiscoCollectorDefinitionDAO(DBUtil.getConnection());
    }
    
    public static ProcessorHRCollectorDefinitionDAO getProcessorHRCollectorDAO(Connection connection) throws DBException {
        return new ProcessorHRCollectorDefinitionDAO(connection);
    }
    
    public static ProcessorHRCollectorDefinitionDAO getProcessorHRCollectorDAO() throws DBException {
        return new ProcessorHRCollectorDefinitionDAO(DBUtil.getConnection());
    }
    
    public static ProcessorUCDCollectorDefinitionDAO getProcessorUCDCollectorDAO(Connection connection) throws DBException {
        return new ProcessorUCDCollectorDefinitionDAO(connection);
    }
    
    public static ProcessorUCDCollectorDefinitionDAO getProcessorUCDCollectorDAO() throws DBException {
        return new ProcessorUCDCollectorDefinitionDAO(DBUtil.getConnection());
    }
    
    public static CustomOIDCollectorDefinitionDAO getCustomOIDCollectorDAO(Connection connection) throws DBException {
        return new CustomOIDCollectorDefinitionDAO(connection);
    }

    public static CustomOIDCollectorDefinitionDAO getCustomOIDCollectorDAO() throws DBException {
        return new CustomOIDCollectorDefinitionDAO(DBUtil.getConnection());
    }
    
    public static CustomOIDDAO getCustomOIDDAO(Connection connection) throws DBException {
        return new CustomOIDDAO(connection);
    }

    public static CustomOIDDAO getCustomOIDDAO() throws DBException {
        return new CustomOIDDAO(DBUtil.getConnection());
    }
    
    
    public static CollectorDefinitionDAO getCollectorDefinitionDAO(Connection connection, CollectorDefinition definition) throws DBException, DAOCreationException {
        Class daoClass;

        daoClass = CollectorDefinitionDAOType.getDAOClass(definition.getClass());
        return getCollectorDefinitionDAOInstance(daoClass, connection);

    }
    
    public static CollectorDefinitionDAO getCollectorDefinitionDAO(int collectorID) throws DBException, DAOCreationException {
        return getCollectorDefinitionDAO(DBUtil.getConnection(), collectorID);
    }
    
    public static CollectorDefinitionDAO getCollectorDefinitionDAO(Connection connection, int collectorID) throws DBException, DAOCreationException {
        int daoTypeID;
        Class daoClass;

        daoTypeID = DBUtil.retrieveIntWithPK(connection, RETRIEVE_COLLECTOR_TYPE, collectorID);
        daoClass = CollectorDefinitionDAOType.getDAOClass(daoTypeID);
        return getCollectorDefinitionDAOInstance(daoClass, connection);
        
    }
    
    private static CollectorDefinitionDAO getCollectorDefinitionDAOInstance(Class daoClass, Connection connection) throws DAOCreationException {
        Class[] types;
        Object[] arguments;
        
        types = new Class[1];
        types[0] = Connection.class;
        
        arguments = new Object[1];
        arguments[0] = connection;
        
        try {
            return (CollectorDefinitionDAO)(daoClass.getConstructor(types).newInstance(arguments));
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
