/*
 * BandwidthCollector.java
 *
 * Created on 11 November 2003, 12:14
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

package nz.co.abrahams.asithappens.bandwidth;

import nz.co.abrahams.asithappens.core.DataType;
import nz.co.abrahams.asithappens.storage.DataHeadings;
import nz.co.abrahams.asithappens.storage.DataPoint;
import nz.co.abrahams.asithappens.storage.Device;
import nz.co.abrahams.asithappens.collectors.DataCollector;
import nz.co.abrahams.asithappens.collectors.DataCollectorResponse;
import nz.co.abrahams.asithappens.snmputil.SNMPException;
import org.apache.log4j.Logger;
import java.net.UnknownHostException;

/**
 * Collects traffic statistics for an interface of a device.  The class uses
 * SNMP to gather data, specifically the "ifInOctets" and "ifOutOctets"
 * variables from the "if" table.
 *
 * For each data collection event, the difference between the current value of
 * the interface traffic counters ("ifInOctets" and "ifOutOctets") and the value
 * gathered at the previous collection event.  The number of bits per second
 * is calculated from the increase in bytes over a period roughly equal to the
 * polling interval.
 *
 * @author  mark
 */
public class BandwidthCollector extends DataCollector {
    
    /** Number of directions */
    private static final int DIRECTIONS = 2;
    
    /** The direction representing inbound traffic */
    private static final int IN_DIRECTION = 0;

    /** The direction representing outbound traffic */
    private static final int OUT_DIRECTION = 1;
    
    /** Logging provider */
    private static Logger logger = Logger.getLogger(BandwidthCollector.class);

    /** SNMP interface */
    private BandwidthSNMP snmp;

    /** Use 64-bit interface counters for collection */
    private boolean use64BitCounters;
    
    /** Indices into the ifTable specifying which interfaces to collect for */
    private int[] ports;
    
    /** Descriptions of the interfaces to collect for */
    private String[] portDescriptions;
    
    /** The number of bytes the previous collection */
    private long[][] lastBytes;
    
    /** The time that the previous successful collection was made */
    protected long[] lastTime;
    
    /**
     * Creates a new BandwidthCollector.
     *
     * @param device               the name or IP address of the target device
     * @param ports                the set of interfaces from the ifTable to collect data about
     * @param pollInterval         the polling interval in milliseconds
     * @param prefer64BitCounters  use 64-bit counters if available
     */
    public BandwidthCollector(BandwidthSNMP snmp, long pollInterval, int[] ports, String[] portDescriptions, boolean prefer64BitCounters) throws UnknownHostException, SNMPException {
        super(snmp.getDevice(), pollInterval, DataType.BANDWIDTH);
        this.snmp = snmp;
        this.ports = ports;
        this.portDescriptions = portDescriptions;
        lastBytes = new long[DIRECTIONS][ports.length];
        lastTime = new long[DIRECTIONS];
        
        for ( int dir = 0; dir < DIRECTIONS; dir++ ) {
            for ( int i = 0; i < ports.length; i++ ) {
                lastBytes[dir][i] = -1;
            }
        }
        use64BitCounters = false;
        
        try {
            if ( prefer64BitCounters ) {
                logger.debug("Attempting to create BandwidthCollector using 64-bit counters");
                for ( int dir = 0; dir < DIRECTIONS; dir++ ) {
                    lastTime[dir] = System.currentTimeMillis() / 2;
                    for (int i = 0; i < ports.length; i++) {
                        if ( dir == IN_DIRECTION)
                            lastBytes[dir][i] = snmp.getIfHCInOctets(ports[i]);
                        else
                            lastBytes[dir][i] = snmp.getIfHCOutOctets(ports[i]);
                    }
                    lastTime[dir] += System.currentTimeMillis() / 2;
                }
                use64BitCounters = true;
                logger.debug("Created BandwidthCollector using 64-bit counters");
            }
        } catch (SNMPException e) {
            logger.debug("Timeout fetching initial 64-bit interface counters");
        }
        try {
            if ( ! use64BitCounters ) {
                logger.debug("Attempting to create BandwidthCollector using 32-bit counters");
                for ( int dir = 0; dir < DIRECTIONS; dir++ ) {
                    lastTime[dir] = System.currentTimeMillis() / 2;
                    for (int i = 0; i < ports.length; i++) {
                        if ( dir == IN_DIRECTION )
                            lastBytes[dir][i] = snmp.getIfInOctets(ports[i]);
                        else
                            lastBytes[dir][i] = snmp.getIfOutOctets(ports[i]);
                    }
                    lastTime[dir] += System.currentTimeMillis() / 2;
                    logger.debug("Created BandwidthCollector using 32-bit counters");
                }
            }
        } catch (SNMPException e) {
            logger.warn("Timeout fetching initial interface counters");
            throw new SNMPException("Timeout fetching initial interface counters");
        } finally {
            snmp.setExpedientCollection();
        }
        
    }
    
    /**
     * Collects inbound and outbound bandwidth statistics for the collection
     * period.
     *
     * @return an array of two data points - one for inbound and outbound
     */
    public DataCollectorResponse getNextValues(DataHeadings headings) {
        long collectTime;
        long[][] newBytes;
        boolean wrap;
        long sum;
        DataPoint[] returnData;
        
        returnData = new DataPoint[DIRECTIONS];
        newBytes = new long[DIRECTIONS][ports.length];
        for ( int dir = 0; dir < DIRECTIONS; dir++ ) {
            collectTime = System.currentTimeMillis();
            try {
                for ( int i = 0; i < ports.length; i++ ) {
                    if ( use64BitCounters ) {
                        if ( dir == IN_DIRECTION )
                            newBytes[dir][i] = snmp.getIfHCInOctets(ports[i]);
                        else
                            newBytes[dir][i] = snmp.getIfHCOutOctets(ports[i]);
                    } else {
                        if ( dir == IN_DIRECTION )
                            newBytes[dir][i] = snmp.getIfInOctets(ports[i]);
                        else
                            newBytes[dir][i] = snmp.getIfOutOctets(ports[i]);
                    }
                    
                }
                collectTime = collectTime / 2 + System.currentTimeMillis() / 2;
                
                wrap = false;
                sum = 0;
                for ( int i = 0; i < ports.length; i++ ) {
                    if ( newBytes[dir][i] < lastBytes[dir][i] )
                        wrap = true;
                    else
                        sum += newBytes[dir][i] - lastBytes[dir][i];
                    lastBytes[dir][i] = newBytes[dir][i];
                }
                if ( ! wrap )
                    returnData[dir] = new DataPoint(collectTime, sum * 8000 / (collectTime - lastTime[dir]) );
                else
                    returnData[dir] = new DataPoint(collectTime);
                lastTime[dir] = collectTime;
                
            } catch (SNMPException e) {
                returnData[dir] = new DataPoint(collectTime);
                logger.warn("Error collecting bandwith values from " + device);
            }
            
        }
        return new DataCollectorResponse(returnData, new String[0], setCount);
    }
    
    public boolean use64BitCounters() {
        return use64BitCounters;
    }
    
    public String[] getPortDescriptions() {
        return portDescriptions;
    }
    
    /** Empty routine as there are no resources to release. */
    public void releaseCollector() {
    }
}
