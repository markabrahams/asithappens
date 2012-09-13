/*
 * MemoryUCDCollector.java
 *
 * Created on 28 February 2006, 05:35
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

import nz.co.abrahams.asithappens.collectors.DataCollector;
import nz.co.abrahams.asithappens.collectors.DataCollectorResponse;
import nz.co.abrahams.asithappens.host.MemoryUCDCollectorDefinition.MemoryUCDType;
import nz.co.abrahams.asithappens.snmputil.SNMPException;
import nz.co.abrahams.asithappens.storage.DataHeadings;
import nz.co.abrahams.asithappens.storage.DataPoint;
import org.apache.log4j.Logger;

/**
 * Memory usage collector for Net-SNMP devices.
 *
 * @author  mark
 */
public class MemoryUCDCollector implements DataCollector {
    
    /** Collector memory pools */
    public static final String[] UCD_MEMORY_TYPES = { "Real", "Swap" };
    
    /** Real memory pool type */
    public static final int UCD_MEMORY_REAL = 0;
    
    /** Swap memory pool type */
    public static final int UCD_MEMORY_SWAP = 1;
    
    /** Logging provider */
    private static Logger logger = Logger.getLogger(MemoryUCDCollector.class);

    /** Collector definition */
    protected MemoryUCDCollectorDefinition definition;
    
    /** SNMP interface */
    private MemoryUCDSNMP snmp;

    /** Total memory for device */
    protected int totalMemory;
    
    /** Type of memory */
    //protected int memoryType;
    
    /**
     * Creates a new MemoryUCDCollector.
     *
     */
    public MemoryUCDCollector(MemoryUCDCollectorDefinition definition, MemoryUCDSNMP snmp) throws SNMPException {
        //super(snmp.getDevice(), pollInterval, DataType.STORAGE);
        this.definition = definition;
        this.snmp = snmp;
        //this.memoryType = memoryType;
        
        if ( definition.getMemoryType() == MemoryUCDType.Real )
            totalMemory = snmp.getUCDMemTotalReal();
        else if ( definition.getMemoryType() == MemoryUCDType.Swap )
            totalMemory = snmp.getUCDMemTotalSwap();
        snmp.setExpedientCollection();
        
    }
    
    /**
     * Collects inbound and outbound bandwidth statistics for the collection
     * period.
     *
     * @return an array of two data points - one for inbound and outbound
     */
    public DataCollectorResponse getNextValues(DataHeadings headings) {
        long currentTime;
        DataPoint[] point;
        
        point = new DataPoint[1];
        currentTime = System.currentTimeMillis();
        
        try {
            if ( definition.getMemoryType() == MemoryUCDType.Real )
                point[0] = new DataPoint(currentTime, (double)(totalMemory - snmp.getUCDMemAvailReal() ) * 1000);
            else if ( definition.getMemoryType() == MemoryUCDType.Swap )
                point[0] = new DataPoint(currentTime, (double)(totalMemory - snmp.getUCDMemAvailSwap() ) * 1000);
        } catch (SNMPException e) {
            point[0] = new DataPoint(currentTime);
            logger.warn("Timeout fetching processor load");
        }
        
        return new DataCollectorResponse(point, new String[0], definition.getInitialHeadings().length);
    }
    
    public MemoryUCDCollectorDefinition getDefinition() {
        return definition;
    }    
    
    /*
    public int getMemoryType() {
        return memoryType;
    }
    */
    
    /** Empty routine as there are no resources to release. */
    public void releaseCollector() {
    }
}
