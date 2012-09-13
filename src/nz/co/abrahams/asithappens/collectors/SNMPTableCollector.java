/*
 * SNMPTableCollector.java
 *
 * Created on 23 August 2012, 22:13
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

import java.net.UnknownHostException;
import java.util.ArrayList;
import nz.co.abrahams.asithappens.core.DataType;
import nz.co.abrahams.asithappens.snmputil.SNMPException;
import nz.co.abrahams.asithappens.snmputil.SNMPType;
import nz.co.abrahams.asithappens.storage.DataHeadings;
import nz.co.abrahams.asithappens.storage.DataPoint;
import org.apache.log4j.Logger;

/**
 * Collects MAC Accounting information.
 * 
 * @author mark
 */
public class SNMPTableCollector implements DataCollector {

    /**
     * Logging provider
     */
    private static Logger logger = Logger.getLogger(SNMPTableCollector.class);
    /** Collector definition */
    CollectorDefinition definition;    
    /**
     * SNMP table polling interface
     */
    private SNMPTableInterface snmp;
    /** SNMP type of counter */
    private SNMPType snmpType;
    /** Data type of counter */
    //private DataType dataType;
    /** Number of set categories */
    protected int setCount;
    /**
     * The number of bytes the previous collection
     */
    private ArrayList<IncreasingCounter> lastBytes;
    /**
     * The time that the previous successful collection was made
     */
    //protected ArrayList<Long> lastTime;
    protected long lastTime;

    /**
     * Creates a new SNMPTableCollector.
     *
     * @param pollInterval the polling interval in milliseconds
     */
    public SNMPTableCollector(CollectorDefinition definition, SNMPTableInterface snmp, SNMPType snmpType) throws UnknownHostException, SNMPException {
        //super(snmp.getDevice(), pollInterval, dataType);
        this.definition = definition;
        this.snmpType = snmpType;
        this.snmp = snmp;
        lastBytes = new ArrayList<IncreasingCounter>();
        lastTime = System.currentTimeMillis();

        logger.info("Starting collector");
        snmp.setExpedientCollection();

    }

    /**
     * Collects inbound and outbound bandwidth statistics for the collection
     * period.
     *
     * @return an array of two data points - one for inbound and outbound
     */
    public DataCollectorResponse getNextValues(DataHeadings headings) {
        long collectTime;

        SNMPTableRecord[] records;
        ArrayList<String> newSets;
        ArrayList<DataPoint> returnData;
        int[] mappings;
        DataPoint[] points;

        collectTime = System.currentTimeMillis();
        newSets = new ArrayList<String>();
        points = new DataPoint[setCount];

        try {
            records = snmp.getSNMPTable();
            mappings = new int[records.length];

            for (int i = 0; i < records.length; i++) {
                // Requires change if more than one record maps to the same key
                //if (!headings.hasKey(records[i].getKey()) && !newSets.contains(records[i].getKey())) {
                if (!headings.hasKey(records[i].getKey())) {
                    if (setCount != headings.size() + newSets.size()) {
                        logger.warn("Set count for collector (" + setCount + ") has different size to data store ("
                                + headings.size() + newSets.size());
                    }

                    logger.info("Adding \"" + records[i].getKey() + "\" at position " + setCount);
                    mappings[i] = setCount;
                    newSets.add(records[i].getKey());
                    lastBytes.add(new IncreasingCounter(snmpType, records[i].getValue()));
                    setCount = headings.size() + newSets.size();
                } else {
                    mappings[i] = headings.getIndex(records[i].getKey());
                }
            }

            points = new DataPoint[setCount];

            for (int i = 0; i < setCount; i++) {
                points[i] = new DataPoint(collectTime, 0);
            }
            for (int i = 0; i < mappings.length; i++) {
                //logger.debug("i: " + i + " mappings[i]: " + mappings[i] + " record[i]: " + records[i].bytes);
                points[mappings[i]] = new DataPoint(collectTime,
                        lastBytes.get(mappings[i]).newValue(records[i].getValue()) *
                        8000 / (collectTime - lastTime));
            }
            lastTime = collectTime;


        } catch (SNMPException e) {
            points = new DataPoint[setCount];
            for (int set = 0; set < setCount; set++) {
                points[set] = new DataPoint(collectTime, 0);
            }
            System.out.println(e);
            //return new DataCollectorResponse(points, (String[])newSets.toArray(new String[newSets.size()]), setCount);
        } catch (UnknownHostException e) {
        }


        //return new DataCollectorResponse(points, (String[]) newSets.toArray(), setCount);
        return new DataCollectorResponse(points, (String[]) newSets.toArray(new String[newSets.size()]), setCount);

    }
    
    /*
    public SNMPType getSNMPType() {
        return snmpType;
    }
    */
    
    public CollectorDefinition getDefinition() {
        return definition;
    }    
    
    /**
     * Empty routine as there are no resources to release.
     */
    public void releaseCollector() {
    }
}
