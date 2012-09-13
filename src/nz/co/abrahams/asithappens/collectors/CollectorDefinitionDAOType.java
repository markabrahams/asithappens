/*
 * CollectorDefinitionDAOType.java
 *
 * Created on 4 September 2012, 21:12
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

import nz.co.abrahams.asithappens.accounting.*;
import nz.co.abrahams.asithappens.bandwidth.BandwidthCollectorDefinition;
import nz.co.abrahams.asithappens.bandwidth.BandwidthCollectorDefinitionDAO;
import nz.co.abrahams.asithappens.core.DAOCreationException;
import nz.co.abrahams.asithappens.host.*;
import nz.co.abrahams.asithappens.nbar.NBARCollectorDefinition;
import nz.co.abrahams.asithappens.nbar.NBARCollectorDefinitionDAO;
import nz.co.abrahams.asithappens.netflow.NetFlowCollectorDefinition;
import nz.co.abrahams.asithappens.netflow.NetFlowCollectorDefinitionDAO;
import nz.co.abrahams.asithappens.oid.CustomOIDCollectorDefinition;
import nz.co.abrahams.asithappens.oid.CustomOIDCollectorDefinitionDAO;
import nz.co.abrahams.asithappens.response.ResponseCollectorDefinition;
import nz.co.abrahams.asithappens.response.ResponseCollectorDefinitionDAO;


/**
 *
 * @author mark
 */
public enum CollectorDefinitionDAOType {
    BANDWIDTH(1, BandwidthCollectorDefinition.class, BandwidthCollectorDefinitionDAO.class),
    RESPONSE(2, ResponseCollectorDefinition.class, ResponseCollectorDefinitionDAO.class),
    RESPONSE_WINDOWS(3, ResponseCollectorDefinition.class, ResponseCollectorDefinitionDAO.class),
    NBAR(4, NBARCollectorDefinition.class, NBARCollectorDefinitionDAO.class),
    NETFLOW(5, NetFlowCollectorDefinition.class, NetFlowCollectorDefinitionDAO.class),
    PROCESSOR_HR(6, ProcessorHRCollectorDefinition.class, ProcessorHRCollectorDefinitionDAO.class),
    PROCESSOR_UCD(7, ProcessorUCDCollectorDefinition.class, ProcessorUCDCollectorDefinitionDAO.class),
    PROCESSOR_CISCO(8, ProcessorCiscoCollectorDefinition.class, ProcessorCiscoCollectorDefinitionDAO.class),
    MEMORY_HR(9, MemoryHRCollectorDefinition.class, MemoryHRCollectorDefinitionDAO.class),
    MEMORY_UCD(10, MemoryUCDCollectorDefinition.class, MemoryUCDCollectorDefinitionDAO.class),
    MEMORY_CISCO(11, MemoryCiscoCollectorDefinition.class, MemoryCiscoCollectorDefinitionDAO.class),
    CUSTOM_OID(12, CustomOIDCollectorDefinition.class, CustomOIDCollectorDefinitionDAO.class),
    IP_ACCOUNTING_ACTIVE(13, IPAccountingActiveCollectorDefinition.class, IPAccountingActiveCollectorDefinitionDAO.class),
    IP_ACCOUNTING_CHECKPOINT(14, IPAccountingCheckpointCollectorDefinition.class, IPAccountingCheckpointCollectorDefinitionDAO.class),
    IPPREC_ACCOUNTING(15, IPPrecAccountingCollectorDefinition.class, IPPrecAccountingCollectorDefinitionDAO.class),
    MAC_ACCOUNTING(16, MACAccountingCollectorDefinition.class, MACAccountingCollectorDefinitionDAO.class);

    int id;

    Class daoClass;

    Class definitionClass;

    CollectorDefinitionDAOType(int id, Class definitionClass, Class daoClass) {
        this.id = id;
        this.definitionClass = definitionClass;
        this.daoClass = daoClass;
    }

    public Class getDAOClass() {
        return daoClass;
    }

    public static Class getDAOClass(Class definitionClass) throws DAOCreationException {
        for ( int i = 0 ; i < CollectorDefinitionDAOType.values().length ; i++ ) {
            if ( CollectorDefinitionDAOType.values()[i].definitionClass.equals(definitionClass) ) {
                return CollectorDefinitionDAOType.values()[i].daoClass;
            }
        }
        throw new DAOCreationException("Cannot find DAO for collector class " + definitionClass);
    }

    public static Class getDAOClass(int daoID) throws DAOCreationException {
        for ( int i = 0 ; i < CollectorDefinitionDAOType.values().length ; i++ ) {
            if ( CollectorDefinitionDAOType.values()[i].id == daoID ) {
                return CollectorDefinitionDAOType.values()[i].daoClass;
            }
        }
        throw new DAOCreationException("Cannot find DAO type with id " + daoID);
    }

    public static int getDAOID(Class definitionClass) throws DAOCreationException {
        for ( int i = 0 ; i < CollectorDefinitionDAOType.values().length ; i++ ) {
            if ( CollectorDefinitionDAOType.values()[i].definitionClass.equals(definitionClass) ) {
                return CollectorDefinitionDAOType.values()[i].id;
            }
        }
        throw new DAOCreationException("Cannot find DAO id for class " + definitionClass);
    }
}
