/*
 * NBARCollector.java
 *
 * Created on 11 June 2005, 23:47
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

package nz.co.abrahams.asithappens.nbar;

import nz.co.abrahams.asithappens.core.DataType;
import nz.co.abrahams.asithappens.storage.DataHeadings;
import nz.co.abrahams.asithappens.storage.DataPoint;
//import nz.co.abrahams.asithappens.storage.Device;
import nz.co.abrahams.asithappens.collectors.DataCollectorResponse;
import nz.co.abrahams.asithappens.collectors.DataCollector;
import nz.co.abrahams.asithappens.snmputil.SNMPException;
import nz.co.abrahams.asithappens.core.DBException;
import org.apache.log4j.Logger;
import java.util.Vector;
import java.net.UnknownHostException;

/**
 * Collector for Cisco NBAR information from the NBAR protocol discovery MIB.
 *
 * @author mark
 */
public class NBARCollector extends DataCollector {
    
    /** The direction representing inbound traffic */
    private static final int IN_DIRECTION = 1;
    /** The direction representing outbound traffic */
    private static final int OUT_DIRECTION = 2;
    
    public static final int DEFAULT_TABLE_SIZE = 10;
    public static final int MAXIMUM_TABLE_INDEX = 50;
    
    private static Logger logger = Logger.getLogger(NBARCollector.class);

    /** SNMP interface */
    private NBARSNMP snmp;

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
    
    /** Creates a new NBARCollector. */
    public NBARCollector(NBARSNMP snmp, long pollInterval, int port, String portString, int direction, int tableSize)
    throws SNMPException {
        super(snmp.getDevice(), pollInterval, DataType.NBAR);

        this.snmp = snmp;
        this.port = port;
        this.portString = portString;
        this.direction = direction;
        this.tableSize = tableSize;
        
        tableIndex = snmp.setNBARTopNConfigTable(port, direction, tableSize, (int)pollInterval / 1000);
        logger.debug("Created NBAR collector: " + toString());
        snmp.setExpedientCollection();
    }
    
    //protected void initCollector() {
    //}
    
    public DataCollectorResponse getNextValues(DataHeadings headings) {
        DataPoint[] points;
        String[] protocols;
        long[] rates;
        int fetchedTableSize;
        int[] mappings;
        int i;
        int keyExists;
        long currentTime;
        Vector<String> newSets;
        
        //logger.debug("Getting next values");
        mappings = new int[tableSize];
        newSets = new Vector();
        //rates = new long[tableSize];
        currentTime = System.currentTimeMillis();
        try {
            snmp.setNBARTopNNewInterval(tableIndex);
            protocols = snmp.getNBARTopNProtocols(tableIndex, tableSize);
            rates = snmp.getNBARTopNRates(tableIndex, tableSize);
            fetchedTableSize = Math.min(protocols.length, rates.length);
            
            // Create new data set columns where necessary
            for ( i = 0; i < fetchedTableSize; i++ ) {
                mappings[i] = -1;
                if ( rates[i] > 0 ) {
                    try {
                        mappings[i] = headings.getIndex(protocols[i]);
                        //logger.debug("Found " + protocols[i] + " at position " + data.getHeadingIndex(protocols[i]));
                    } catch (NullPointerException e) {
                        if ( setCount != headings.size() + newSets.size())
                            logger.warn("Set count for collector (" + setCount + ") has different size to data store ("
                                    + headings.size() + newSets.size());
                        
                        logger.info("Adding " + protocols[i] + " at position " + setCount);
                        mappings[i] = setCount;
                        newSets.add(protocols[i]);
                        //mappings[i] = data.addSet(protocols[i]);
                        setCount = headings.size() + newSets.size();
                        //data.addValue(mappings[i], new DataPoint(currentTime - pollInterval, 0));
                    }
                }
            }
            
            //points = new DataPoint[data.getNumberOfSets()];
            points = new DataPoint[setCount];
            
            for ( i = 0; i < setCount; i++ ) {
                points[i] = new DataPoint(currentTime, 0);
            }
            for ( i = 0; i < fetchedTableSize; i++ ) {
                if ( mappings[i] != -1 )
                    points[mappings[i]].setValue(rates[i]);
            }
            //return new DataCollectorResponse(points, (String[])newSets.toArray(), setCount);
            return new DataCollectorResponse(points, (String[])newSets.toArray(new String[newSets.size()]), setCount);
        } catch (Exception e) {
            logger.warn("Exception collecting values: " + e);
            e.printStackTrace();
            points = new DataPoint[setCount];
            for (int set = 0; set < setCount; set++ )
                points[set] = new DataPoint(currentTime, 0);
            //return new DataCollectorResponse(points, (String[])newSets.toArray(), setCount);
            return new DataCollectorResponse(points, (String[])newSets.toArray(new String[newSets.size()]), setCount);
        }
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
    
    public String toString() {
        StringBuffer buffer;
        buffer = new StringBuffer();
        buffer.append("Device=" + snmp.getDevice() + ", ");
        buffer.append("IfIndex=" + port + ", ");
        buffer.append("Port=" + portString + ", ");
        buffer.append("Direction=" + direction + ", ");
        buffer.append("TableIndex=" + tableIndex + ", ");
        buffer.append("PollInterval=" + pollInterval + ", ");
        buffer.append("TableSize=" + tableSize);
        
        return buffer.toString();
    }

    public void releaseCollector() {
        logger.debug("Attempting to destroy Top-N table");
        try {
            snmp.setReliableCollection();
            snmp.setNBARTopNDestroy(tableIndex);
        } catch (SNMPException e) {
            logger.warn("Problem destroying Top-N table: " + e);
            e.printStackTrace();
        }
    }
}
