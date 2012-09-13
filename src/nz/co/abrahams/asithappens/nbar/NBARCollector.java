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

import java.util.Vector;
import nz.co.abrahams.asithappens.collectors.DataCollector;
import nz.co.abrahams.asithappens.collectors.DataCollectorResponse;
import nz.co.abrahams.asithappens.snmputil.SNMPException;
import nz.co.abrahams.asithappens.storage.DataHeadings;
import nz.co.abrahams.asithappens.storage.DataPoint;
import org.apache.log4j.Logger;

/**
 * Collector for Cisco NBAR information from the NBAR protocol discovery MIB.
 *
 * @author mark
 */
public class NBARCollector implements DataCollector {
    
    public static final int DEFAULT_TABLE_SIZE = 10;
    public static final int MAXIMUM_TABLE_INDEX = 50;
    
    private static Logger logger = Logger.getLogger(NBARCollector.class);

    /** Collector definition */
    NBARCollectorDefinition definition;
    
    /** SNMP interface */
    private NBARSNMP snmp;

    /** Number of set categories */
    protected int setCount;
    /** TopN table index */
    protected int tableIndex;
    
    /** Creates a new NBARCollector. */
    public NBARCollector(NBARCollectorDefinition definition, NBARSNMP snmp)
    throws SNMPException {
        this.definition = definition;
        this.snmp = snmp;
        
        tableIndex = snmp.setNBARTopNConfigTable(definition.getTableSize(), (int)(definition.getPollInterval()) / 1000);
        logger.debug("Created NBAR collector: " + toString());
        snmp.setExpedientCollection();
    }
    
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
        
        mappings = new int[definition.getTableSize()];
        newSets = new Vector();
        currentTime = System.currentTimeMillis();
        try {
            snmp.setNBARTopNNewInterval(tableIndex);
            protocols = snmp.getNBARTopNProtocols(tableIndex, definition.getTableSize());
            rates = snmp.getNBARTopNRates(tableIndex, definition.getTableSize());
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
            //e.printStackTrace();
            points = new DataPoint[setCount];
            for (int set = 0; set < setCount; set++ )
                points[set] = new DataPoint(currentTime, 0);
            return new DataCollectorResponse(points, (String[])newSets.toArray(new String[newSets.size()]), setCount);
        }
    }
        
    public NBARCollectorDefinition getDefinition() {
        return definition;
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
