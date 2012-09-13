/*
 * IPAccountingCheckpointCollector.java
 *
 * Created on 14 November 2012, 21:38
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
package nz.co.abrahams.asithappens.accounting;

import java.net.UnknownHostException;
import java.util.ArrayList;
import nz.co.abrahams.asithappens.collectors.DataCollector;
import nz.co.abrahams.asithappens.collectors.DataCollectorResponse;
import nz.co.abrahams.asithappens.snmputil.SNMPException;
import nz.co.abrahams.asithappens.storage.DataHeadings;
import nz.co.abrahams.asithappens.storage.DataPoint;
import org.apache.log4j.Logger;

/**
 * Collects traffic statistics for an interface of a device. The class uses SNMP
 * to gather data, specifically the "ifInOctets" and "ifOutOctets" variables
 * from the "if" table.
 *
 * For each data collection event, the difference between the current value of
 * the interface traffic counters ("ifInOctets" and "ifOutOctets") and the value
 * gathered at the previous collection event. The number of bits per second is
 * calculated from the increase in bytes over a period roughly equal to the
 * polling interval.
 *
 * @author mark
 */
public class IPAccountingCheckpointCollector implements DataCollector {

    /**
     * Logging provider
     */
    private static Logger logger = Logger.getLogger(IPAccountingCheckpointCollector.class);
    
    /** Collector definition */
    IPAccountingCheckpointCollectorDefinition definition;
    
    /**
     * SNMP interface
     */
    private IPAccountingCheckpointSNMP snmp;
    
    /** Number of set categories */
    protected int setCount;

    /**
     * The time that the previous successful collection was made
     */
    protected long lastTime;

    /**
     * Creates a new IPAccountingCheckpointCollector.
     *
     * @param device the name or IP address of the target device
     * @param pollInterval the polling interval in milliseconds
     */
    public IPAccountingCheckpointCollector(IPAccountingCheckpointCollectorDefinition definition, IPAccountingCheckpointSNMP snmp) throws UnknownHostException, SNMPException {
        //super(snmp.getDevice(), pollInterval, DataType.ACCOUNTING);
        this.definition = definition;
        this.snmp = snmp;
        
        setCount = definition.getInitialHeadings().length;
        //lastBytes = new ArrayList<Integer>();
        lastTime = System.currentTimeMillis();

        logger.info("Starting collector");
        snmp.copyCurrentToCheckpoint();
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
        boolean wrap;

        IPAccountingRecord[] records;
        ArrayList<String> newSets;
        ArrayList<DataPoint> returnData;
        int[] mappings;
        DataPoint[] points;

        collectTime = System.currentTimeMillis();
        newSets = new ArrayList<String>();
        points = new DataPoint[setCount];

        try {
            snmp.copyCurrentToCheckpoint();
            records = snmp.getCkactByts();
            mappings = new int[records.length];

            for (int i = 0; i < records.length; i++) {
                if (!headings.hasKey(records[i].getKey())) {
                    if (setCount != headings.size() + newSets.size()) {
                        logger.warn("Set count for collector (" + setCount + ") has different size to data store ("
                                + headings.size() + newSets.size());
                    }

                    logger.info("Adding \"" + records[i].getKey() + "\" at position " + setCount);
                    mappings[i] = setCount;
                    newSets.add(records[i].getKey());
                    //lastBytes.add(new Integer(records[i].bytes));
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
                logger.debug("i: " + i + " mappings[i]: " + mappings[i] + " record[i]: " + records[i].getValue());
                //points[mappings[i]] = new DataPoint(collectTime, (records[i].bytes - lastBytes.get(mappings[i]).intValue()) * 8000 / (collectTime - lastTime));
                points[mappings[i]] = new DataPoint(collectTime, records[i].getValue() * 8000 / (collectTime - lastTime));
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
    
    public IPAccountingCheckpointCollectorDefinition getDefinition() {
        return definition;
    }

    /**
     * Empty routine as there are no resources to release.
     */
    public void releaseCollector() {
    }
}
