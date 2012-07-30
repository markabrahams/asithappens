/*
 * ProcessorUCDCollector.java
 *
 * Created on 24 February 2006, 22:38
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

package nz.co.abrahams.asithappens.host;

import nz.co.abrahams.asithappens.core.DataType;
import nz.co.abrahams.asithappens.storage.DataHeadings;
import nz.co.abrahams.asithappens.storage.DataPoint;
import nz.co.abrahams.asithappens.storage.Device;
import nz.co.abrahams.asithappens.collectors.DataCollector;
import nz.co.abrahams.asithappens.collectors.DataCollectorResponse;
import nz.co.abrahams.asithappens.snmputil.SNMPException;
import nz.co.abrahams.asithappens.core.DBException;
import java.net.*;
import org.apache.log4j.Logger;

/**
 * Processor load collector.
 *
 * @author  mark
 */
public class ProcessorUCDCollector extends DataCollector {
    
    /** Time tick period in milliseconds */
    private static final int TICK_PERIOD = 10;
    
    /** Number of CPU usage categories */
    private static final int USAGE_CATEGORIES = 7;
    
    /** Logging provider */
    private static Logger logger = Logger.getLogger(ProcessorUCDCollector.class);

    /** SNMP interface */
    private ProcessorUCDSNMP snmp;
    
    /** Values from previous collection */
    protected long[] lastTicks;
    
    /** Last calculated percentages */
    protected double[] lastPercentages;
    
    /** The time that the previous successful counter reading was made */
    protected long lastTime;
    
    /**
     * Creates a new ProcessorUCDCollector.
     *
     * @param device       the name or IP address of the target device
     * @param pollInterval the polling interval in milliseconds
     */
    public ProcessorUCDCollector(ProcessorUCDSNMP snmp, long pollInterval) throws SNMPException {
        super(snmp.getDevice(), pollInterval, DataType.NETSNMP_PROCESSOR);

        this.snmp = snmp;
        lastTime = System.currentTimeMillis();
        lastTicks = fetchUsageCounters();
        snmp.setExpedientCollection();
        lastPercentages = new double[USAGE_CATEGORIES - 1];
        logger.debug("Starting UCD processor collector");
    }
    
    /**
     * Collects inbound and outbound bandwidth statistics for the collection
     * period.
     *
     * @return an array of two data points - one for inbound and outbound
     */
    public DataCollectorResponse getNextValues(DataHeadings headings) {
        long currentTime;
        long[] newTicks;
        long totalTicks;
        DataPoint[] returnData;
        double percentage;
        boolean changedValues;
        boolean clockedCounter;
        
        newTicks = new long[USAGE_CATEGORIES];
        returnData = new DataPoint[dataType.initialSetCount()];
        currentTime = System.currentTimeMillis();
        
        try {
            newTicks = fetchUsageCounters();
            changedValues = false;
            clockedCounter = false;
            totalTicks = 0;
            for ( int i = 0; i < USAGE_CATEGORIES; i++ ) {
                if ( newTicks[i] < lastTicks[i] )
                    clockedCounter = true;
                else if ( newTicks[i] != lastTicks[i] ) {
                    totalTicks += newTicks[i] - lastTicks[i];
                    changedValues = true;
                }
            }
            if ( changedValues && ! clockedCounter ) {
                for ( int i = 0; i < USAGE_CATEGORIES; i++ ) {
                    if ( i < USAGE_CATEGORIES - 1 ) {
                        lastPercentages[i] = (double)( newTicks[i] - lastTicks[i] ) * 100 / totalTicks;
                        returnData[i] = new DataPoint(currentTime, lastPercentages[i]);
                    }
                    lastTicks[i] = newTicks[i];
                }
                lastTime = currentTime;
            } else {
                for ( int i = 0; i < dataType.initialSetCount(); i++ ) {
                    if ( Double.isNaN(lastPercentages[i]) )
                        returnData[i] = new DataPoint(currentTime);
                    else
                        returnData[i] = new DataPoint(currentTime, lastPercentages[i]);
                }
            }
        } catch (SNMPException e) {
            for ( int i = 0; i < dataType.initialSetCount(); i++ ) {
                returnData[i] = new DataPoint(currentTime);
            }
            logger.warn("Timeout fetching processor load");
        }
        
        return new DataCollectorResponse(returnData, new String[0], dataType.initialSetCount());
    }
    
    protected long[] fetchUsageCounters() throws SNMPException {
        long[] counters;
        
        counters = new long[USAGE_CATEGORIES];
        counters[0] = snmp.getSSCPUUser();
        counters[1] = snmp.getSSCPUNice();
        counters[2] = snmp.getSSCPUSystem();
        counters[3] = snmp.getSSCPUWait();
        counters[4] = snmp.getSSCPUKernel();
        counters[5] = snmp.getSSCPUInterrupt();
        counters[6] = snmp.getSSCPUIdle();
        
        return counters;
    }
    
    /** Empty routine as there are no resources to release. */
    public void releaseCollector() {
    }
}
