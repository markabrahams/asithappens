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

import nz.co.abrahams.asithappens.core.DataType;
import nz.co.abrahams.asithappens.storage.DataHeadings;
import nz.co.abrahams.asithappens.storage.DataPoint;
import nz.co.abrahams.asithappens.storage.Device;
import nz.co.abrahams.asithappens.flow.FlowOptions;
import nz.co.abrahams.asithappens.*;
import nz.co.abrahams.asithappens.collectors.DataCollectorResponse;
import nz.co.abrahams.asithappens.collectors.DataCollector;
import nz.co.abrahams.asithappens.snmputil.SNMPException;
import nz.co.abrahams.asithappens.snmputil.SNMPAccess;
import nz.co.abrahams.asithappens.core.DBException;
import nz.co.abrahams.asithappens.core.Configuration;
import java.util.*;
import java.net.UnknownHostException;
import org.apache.log4j.Logger;

/**
 * Collector for Cisco NetFlow information.
 *
 * @author mark
 */
public class NetFlowCollector extends DataCollector {
    
    public static final int DEFAULT_TABLE_SIZE = 10;
    
    /** Logging provider */
    private static Logger logger = Logger.getLogger(NetFlowCollector.class);

    /** SNMP interface */
    private NetFlowSNMP snmp;

    /** An index into the ifTable specifying which interface to collect for */
    private int port;
    /** A textual description of the interface */
    private String portString;
    /** Direction of traffic to examine */
    private int direction;
    /** Size of TopN table */
    private int tableSize;
    /** TopN table index */
    protected int tableIndex;
    /** NetFlow match criteria */
    protected NetFlowMatchCriteria criteria;
    /** Flow mask options */
    protected FlowOptions options;
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
    public NetFlowCollector(NetFlowSNMP snmp, int port, String portString, int direction, int tableSize, long pollInterval, NetFlowMatchCriteria criteria, FlowOptions options)
    throws SNMPException {
        super(snmp.getDevice(), pollInterval, DataType.NETFLOW);

        this.snmp = snmp;
        this.port = port;
        this.portString = portString;
        this.direction = direction;
        this.tableSize = tableSize;
        this.criteria = criteria;
        this.options = options;
        
        flows = new Vector();
        setMappings = new Vector();
        //lastCollections = new Vector();
        initCollector();
        //tableIndex = device.setNBARTopNConfigTable(port, direction, tableSize, (int)pollInterval / 1000);
        logger.debug("Created NetFlow collector: " + toString());
    }
    
    /** Initializes the NetFlow collector. */
    protected void initCollector() throws SNMPException {
        int newDirection;
        
        previousDirection = snmp.getNetFlowEnable(port);
        /*
        if ( previousDirection != direction && previousDirection != DataSets.DIRECTION_BOTH ) {
            newDirection = previousDirection == 0 ? direction : DataSets.DIRECTION_BOTH;
            snmp.setNetFlowEnable(port, newDirection);
        }
         */
        snmp.setNetFlowEnable(port, direction);
        snmp.setNetFlowMatchCriteria(criteria);
        snmp.setNetFlowTopFlowsTopNTable(tableSize);
        snmp.setNetFlowTopFlowsSortBy(SNMPAccess.NETFLOW_TOPN_SORT_BY_BYTES);
        snmp.setNetFlowTopFlowsCacheTimeout(pollInterval);
        //snmp.setNetFlowMatchCriteria(criteria);
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
                    if ( table[index].matches(flows.elementAt(flow), options) ) {
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
                    newSets.add(table[index].printable(options));
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
            points[set].setValue(points[set].getValue() * 8 / ( pollInterval / 1000 ) );
        }
        
        return new DataCollectorResponse(points, (String[])newSets.toArray(new String[newSets.size()]), setCount);
    }
    
    public String getIfDescr() {
        return portString;
    }
    
    public int getDirection() {
        return direction;
    }
    
    public int getTableSize() {
        return tableSize;
    }
    
    public NetFlowMatchCriteria getCriteria() {
        return criteria;
    }
    
    public FlowOptions getOptions() {
        return options;
    }

    public void releaseCollector() {
        NetFlowMatchCriteria defaults;
        
        logger.debug("Restoring previous NetFlow state");
        try {
            snmp.setReliableCollection();
            snmp.setNetFlowTopFlowsTopNTable(0);
            snmp.setNetFlowTopFlowsCacheTimeout(0);
            snmp.restoreDefaultNetFlowMatchCriteria(criteria);
            if ( Configuration.getProperty("collector.netflow.configuration.restore").equals("on") ) {
                snmp.setNetFlowEnable(port, previousDirection);
            } else if ( Configuration.getProperty("collector.netflow.configuration.restore").equals("clear") ) {
                snmp.setNetFlowEnable(port, 0);
            }
        } catch (SNMPException e) {
            logger.warn("Problem restoring previous NetFlow state: " + e);
            e.printStackTrace();
        }
    }
}
