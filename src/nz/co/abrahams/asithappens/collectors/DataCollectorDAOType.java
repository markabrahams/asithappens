/*
 * DataCollectorDAOType.java
 *
 * Created on 12 January 2010, 00:17
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
 */

package nz.co.abrahams.asithappens.collectors;

import nz.co.abrahams.asithappens.core.DAOCreationException;
import nz.co.abrahams.asithappens.bandwidth.BandwidthCollector;
import nz.co.abrahams.asithappens.response.ResponseCollector;
import nz.co.abrahams.asithappens.response.ResponseWindowsCollector;
import nz.co.abrahams.asithappens.nbar.NBARCollector;
import nz.co.abrahams.asithappens.netflow.NetFlowCollector;
import nz.co.abrahams.asithappens.host.ProcessorHRCollector;
import nz.co.abrahams.asithappens.host.ProcessorUCDCollector;
import nz.co.abrahams.asithappens.host.ProcessorCiscoCollector;
import nz.co.abrahams.asithappens.host.MemoryHRCollector;
import nz.co.abrahams.asithappens.host.MemoryUCDCollector;
import nz.co.abrahams.asithappens.host.MemoryCiscoCollector;
import nz.co.abrahams.asithappens.oid.CustomOIDCollector;
import nz.co.abrahams.asithappens.bandwidth.BandwidthCollectorDAO;
import nz.co.abrahams.asithappens.response.ResponseCollectorDAO;
import nz.co.abrahams.asithappens.response.ResponseWindowsCollectorDAO;
import nz.co.abrahams.asithappens.nbar.NBARCollectorDAO;
import nz.co.abrahams.asithappens.netflow.NetFlowCollectorDAO;
import nz.co.abrahams.asithappens.host.ProcessorHRCollectorDAO;
import nz.co.abrahams.asithappens.host.ProcessorUCDCollectorDAO;
import nz.co.abrahams.asithappens.host.ProcessorCiscoCollectorDAO;
import nz.co.abrahams.asithappens.host.MemoryHRCollectorDAO;
import nz.co.abrahams.asithappens.host.MemoryUCDCollectorDAO;
import nz.co.abrahams.asithappens.host.MemoryCiscoCollectorDAO;
import nz.co.abrahams.asithappens.oid.CustomOIDCollectorDAO;


/**
 *
 * @author mark
 */
public enum DataCollectorDAOType {
    BANDWIDTH(1, BandwidthCollector.class, BandwidthCollectorDAO.class),
    RESPONSE(2, ResponseCollector.class, ResponseCollectorDAO.class),
    RESPONSE_WINDOWS(3, ResponseWindowsCollector.class, ResponseWindowsCollectorDAO.class),
    NBAR(4, NBARCollector.class, NBARCollectorDAO.class),
    NETFLOW(5, NetFlowCollector.class, NetFlowCollectorDAO.class),
    PROCESSOR_HR(6, ProcessorHRCollector.class, ProcessorHRCollectorDAO.class),
    PROCESSOR_UCD(7, ProcessorUCDCollector.class, ProcessorUCDCollectorDAO.class),
    PROCESSOR_CISCO(8, ProcessorCiscoCollector.class, ProcessorCiscoCollectorDAO.class),
    MEMORY_HR(9, MemoryHRCollector.class, MemoryHRCollectorDAO.class),
    MEMORY_UCD(10, MemoryUCDCollector.class, MemoryUCDCollectorDAO.class),
    MEMORY_CISCO(11, MemoryCiscoCollector.class, MemoryCiscoCollectorDAO.class),
    CUSTOM_OID(12, CustomOIDCollector.class, CustomOIDCollectorDAO.class);


    int id;

    Class daoClass;

    Class collectorClass;

    DataCollectorDAOType(int id, Class collectorClass, Class daoClass) {
        this.id = id;
        this.collectorClass = collectorClass;
        this.daoClass = daoClass;
    }

    public Class getDAOClass() {
        return daoClass;
    }

    public static Class getDAOClass(Class collectorClass) throws DAOCreationException {
        for ( int i = 0 ; i < DataCollectorDAOType.values().length ; i++ ) {
            if ( DataCollectorDAOType.values()[i].collectorClass.equals(collectorClass) ) {
                return DataCollectorDAOType.values()[i].daoClass;
            }
        }
        throw new DAOCreationException("Cannot find DAO for collector class " + collectorClass);
    }

    public static Class getDAOClass(int daoID) throws DAOCreationException {
        for ( int i = 0 ; i < DataCollectorDAOType.values().length ; i++ ) {
            if ( DataCollectorDAOType.values()[i].id == daoID ) {
                return DataCollectorDAOType.values()[i].daoClass;
            }
        }
        throw new DAOCreationException("Cannot find DAO type with id " + daoID);
    }

    public static int getDAOID(Class collectorClass) throws DAOCreationException {
        for ( int i = 0 ; i < DataCollectorDAOType.values().length ; i++ ) {
            if ( DataCollectorDAOType.values()[i].collectorClass.equals(collectorClass) ) {
                return DataCollectorDAOType.values()[i].id;
            }
        }
        throw new DAOCreationException("Cannot find DAO id for class " + collectorClass);
    }
}
