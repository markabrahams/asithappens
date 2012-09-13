/*
 * NetFlowCollector.java
 *
 * Created on 20 November 2005, 15:47
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

package nz.co.abrahams.asithappens.netflow;

import java.util.Vector;
import nz.co.abrahams.asithappens.collectors.DataCollector;
import nz.co.abrahams.asithappens.collectors.DataCollectorResponse;
import nz.co.abrahams.asithappens.core.Configuration;
import nz.co.abrahams.asithappens.snmputil.SNMPAccess;
import nz.co.abrahams.asithappens.snmputil.SNMPException;
import nz.co.abrahams.asithappens.storage.DataHeadings;
import nz.co.abrahams.asithappens.storage.DataPoint;
import org.apache.log4j.Logger;

/**
 * Collector for Cisco NetFlow information.
 *
 * @author mark
 */
public class NetFlowCollector implements DataCollector {
    
    public static final int DEFAULT_TABLE_SIZE = 10;
    
    /** Logging provider */
    private static Logger logger = Logger.getLogger(NetFlowCollector.class);

    /** Collector definition */
    NetFlowCollectorDefinition definition;
    
    /** SNMP interface */
    private NetFlowSNMP snmp;

    /** Number of set categories */
    protected int setCount;
    /** Size of TopN table */
    private int tableSize;
    /** Underlying data */
    //protected DataSets data;
    /** Existing flows */
    protected Vector<NetFlowRecord> flows;
    /** Mapping from flow to data set */
    protected Vector<Integer> setMappings;
    /** Last collection times for flows */
    //protected Vector<Long> lastCollections;
    /** Previously configured Netflow direction on interface */
    protected int previousDirection;
    
    /** Creates a new NetFlowCollector. */
    public NetFlowCollector(NetFlowCollectorDefinition definition, NetFlowSNMP snmp)
    throws SNMPException {
        this.definition = definition;
        this.snmp = snmp;
        this.tableSize = Configuration.getPropertyInt("collector.netflow.table.size");
        
        flows = new Vector();
        setMappings = new Vector();
        //lastCollections = new Vector();
        initCollector();
        logger.debug("Created NetFlow collector: " + toString());
    }
    
    /** Initializes the NetFlow collector. */
    protected void initCollector() throws SNMPException {
        previousDirection = snmp.getNetFlowEnable();
        snmp.setNetFlowEnable(definition.getDirection().getOIDIndex());
        snmp.setNetFlowMatchCriteria(definition.getCriteria());
        snmp.setNetFlowTopFlowsTopNTable(tableSize);
        snmp.setNetFlowTopFlowsSortBy(SNMPAccess.NETFLOW_TOPN_SORT_BY_BYTES);
        snmp.setNetFlowTopFlowsCacheTimeout(definition.getPollInterval());
        snmp.setExpedientCollection();
    }
    
    public DataCollectorResponse getNextValues(DataHeadings headings) {
        NetFlowRecord[] table;
        DataPoint[] points;
        int[] tableMappings;
        long currentTime;
        long byteDifference;
        boolean foundSet;
        int headingIndex;
        Vector<String> newSets;
        
        currentTime = System.currentTimeMillis();
        //snmp.setNetFlowTopFlowsCacheTimeout(pollInterval);
        table = snmp.getNetFlowTopFlowsTable(tableSize);
        tableMappings = new int[table.length];
        newSets = new Vector();
        for (int index = 0; index < table.length; index++) {
            tableMappings[index] = -1;
            for (int flow = 0; flow < flows.size(); flow++ ) {
                if ( table[index].matches(flows.elementAt(flow)) ) {
                    tableMappings[index] = flow;
                    flow = flows.size();
                }
            }
            // Entry in flow table does not match any previously seen flows
            // so create new flow record for this flow
            if ( tableMappings[index] == -1 ) {
                //try {
                flows.add(table[index]);
                tableMappings[index] = flows.size() - 1;
                
                foundSet = false;
                logger.debug("Adding flow " + (flows.size() - 1) + " for flow at table position " + index);
                for (int flow = 0; flow < flows.size() - 1; flow++ ) {
                    if ( table[index].matches(flows.elementAt(flow), definition.getOptions()) ) {
                        logger.debug("Flow " + (flows.size() - 1) + " maps to set " + setMappings.elementAt(flow));
                        setMappings.add(new Integer(setMappings.elementAt(flow)));
                        flow = flows.size();
                        foundSet = true;
                    }
                }
                // New flow does not match any data set
                // so create new data set for this flow
                /*
                if ( ! foundSet ) {
                    //setMappings.add(data.addSet(table[index].printable(options)));
                    if ( setCount != setMappings.size() + newSets.size() )
                        logger.warn("Set count for collector (" + setCount + ") has different size to data store ("
                                + setMappings.size() + newSets.size() + ")");
                    setMappings.add(new Integer(setCount));
                    newSets.add(table[index].printable(options));
                    setCount = setMappings.size() + newSets.size();
                    setCount = setMappings.size();
                    logger.debug("Flow " + (flows.size() - 1) + " maps to set " + setMappings.elementAt(setCount - 1));
                }
                 */
                if ( ! foundSet ) {
                    //setMappings.add(data.addSet(table[index].printable(options)));
                    if ( setCount != headings.size() + newSets.size() )
                        logger.warn("Set count for collector (" + setCount + ") has different size to data store ("
                                + headings.size() + newSets.size() + ")");
                    setMappings.add(new Integer(setCount));
                    newSets.add(table[index].printable(definition.getOptions()));
                    //setCount = setMappings.size() + newSets.size();
                    setCount++;
                    logger.debug("Flow " + (flows.size() - 1) + " maps to set " + setMappings.elementAt(setCount - 1));
                }
                
            }
            
        }
        points = new DataPoint[setCount];
        for (int set = 0; set < setCount; set++ ) {
            points[set] = new DataPoint(currentTime, 0);
        }
        for (int index = 0; index < table.length; index++ ) {
            byteDifference = table[index].bytes - flows.elementAt(tableMappings[index]).bytes;
            byteDifference = Math.max(byteDifference, 0);
            headingIndex = setMappings.elementAt(tableMappings[index]);
            points[headingIndex].setValue(points[headingIndex].getValue() + byteDifference);
            //logger.debug("tableIndex=" + index + ", mapping=" + tableMappings[index] + ", setMapping=" + headingIndex);
            flows.set(tableMappings[index], table[index]);
        }
        for (int set = 0; set < setCount; set++ ) {
            points[set].setValue(points[set].getValue() * 8 / ( definition.getPollInterval() / 1000 ) );
        }
        
        return new DataCollectorResponse(points, (String[])newSets.toArray(new String[newSets.size()]), setCount);
    }
    
    /*
    public String getIfDescr() {
        return portString;
    }
    */
    
    /*
    public int getDirection() {
        return direction;
    }
    */
    
    public int getTableSize() {
        return tableSize;
    }
    
    /*
    public NetFlowMatchCriteria getCriteria() {
        return criteria;
    }
    */
    
    /*
    public FlowOptions getOptions() {
        return options;
    }
    */

    public NetFlowCollectorDefinition getDefinition() {
        return definition;
    }    
    
    public void releaseCollector() {
        NetFlowMatchCriteria defaults;
        
        logger.debug("Restoring previous NetFlow state");
        try {
            snmp.setReliableCollection();
            snmp.setNetFlowTopFlowsTopNTable(0);
            snmp.setNetFlowTopFlowsCacheTimeout(0);
            snmp.restoreDefaultNetFlowMatchCriteria(definition.getCriteria());
            if ( Configuration.getProperty("collector.netflow.configuration.restore").equals("on") ) {
                snmp.setNetFlowEnable(previousDirection);
            } else if ( Configuration.getProperty("collector.netflow.configuration.restore").equals("clear") ) {
                snmp.setNetFlowEnable(0);
            }
        } catch (SNMPException e) {
            logger.warn("Problem restoring previous NetFlow state: " + e);
            e.printStackTrace();
        }
    }
}
