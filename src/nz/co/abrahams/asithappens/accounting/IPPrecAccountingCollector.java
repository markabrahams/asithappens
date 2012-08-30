/*
 * IPPrecAccountingCollector.java
 *
 * Created on 22 August 2012, 08:51
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
import nz.co.abrahams.asithappens.collectors.IncreasingCounter;
import nz.co.abrahams.asithappens.core.DataType;
import nz.co.abrahams.asithappens.snmputil.SNMPException;
import nz.co.abrahams.asithappens.snmputil.SNMPType;
import nz.co.abrahams.asithappens.storage.DataHeadings;
import nz.co.abrahams.asithappens.storage.DataPoint;
import nz.co.abrahams.asithappens.storage.Direction;
import org.apache.log4j.Logger;

/**
 * Collects MAC Accounting information.
 * 
 * @author mark
 */
public class IPPrecAccountingCollector extends DataCollector {

    /**
     * Logging provider
     */
    private static Logger logger = Logger.getLogger(IPPrecAccountingCollector.class);
    /**
     * SNMP interface
     */
    private IPPrecAccountingSNMP snmp;
    /**
     * The number of bytes the previous collection
     */
    //private ArrayList<Long> lastBytes;
    private ArrayList<IncreasingCounter> lastBytes;
    /**
     * The time that the previous successful collection was made
     */
    //protected ArrayList<Long> lastTime;
    protected long lastTime;

    /**
     * Creates a new AccountingCollector.
     *
     * @param snmp the IP precedence accounting SNMP interface
     * @param pollInterval the polling interval in milliseconds
     */
    public IPPrecAccountingCollector(IPPrecAccountingSNMP snmp, long pollInterval) throws UnknownHostException, SNMPException {
        super(snmp.getDevice(), pollInterval, DataType.IPPREC_ACCOUNTING);
        this.snmp = snmp;
        //lastBytes = new ArrayList<Long>();
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
        boolean wrap;
        long increase;

        IPPrecAccountingRecord[] records;
        ArrayList<String> newSets;
        ArrayList<DataPoint> returnData;
        int[] mappings;
        DataPoint[] points;

        collectTime = System.currentTimeMillis();
        newSets = new ArrayList<String>();
        points = new DataPoint[setCount];

        try {
            records = snmp.getIPPrecAccountingTable();
            mappings = new int[records.length];

            for (int i = 0; i < records.length; i++) {
                //if (!headings.hasKey(records[i].getKey()) && !newSets.contains(records[i].getKey())) {
                if (!headings.hasKey(records[i].getKey())) {
                    if (setCount != headings.size() + newSets.size()) {
                        logger.warn("Set count for collector (" + setCount + ") has different size to data store ("
                                + headings.size() + newSets.size());
                    }

                    logger.info("Adding \"" + records[i].getKey() + "\" at position " + setCount);
                    mappings[i] = setCount;
                    newSets.add(records[i].getKey());
                    lastBytes.add(new IncreasingCounter(SNMPType.Counter32, records[i].bytes));
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
                        lastBytes.get(mappings[i]).newValue(records[i].bytes) *
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
    
    public String getIfDescr() {
        return snmp.getIfDescr();
    }
    
    public Direction getDirection() {
        return snmp.getDirection();
    }

    /**
     * Empty routine as there are no resources to release.
     */
    public void releaseCollector() {
    }
}
