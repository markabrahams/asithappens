/*
 * MemoryCiscoCollector.java
 *
 * Created on 1 March 2006, 04:46
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
import nz.co.abrahams.asithappens.snmputil.SNMPException;
import nz.co.abrahams.asithappens.storage.DataHeadings;
import nz.co.abrahams.asithappens.storage.DataPoint;
import org.apache.log4j.Logger;

/**
 * Memory usage collector for Cisco devices.
 *
 * @author  mark
 */
public class MemoryCiscoCollector implements DataCollector {
    
    /** Logging provider */
    private static Logger logger = Logger.getLogger(MemoryCiscoCollector.class);
    
    /** Collector definition */
    MemoryCiscoCollectorDefinition definition;

    /** SNMP interface */
    private MemoryCiscoSNMP snmp;

    /** Index of object in storage table */
    //protected int memoryIndex;
    
    /**
     * Creates a new ProcessorCiscoCollector.
     *
     * @param device       the name or IP address of the target device
     * @param pollInterval the polling interval in milliseconds
     */
    public MemoryCiscoCollector(MemoryCiscoCollectorDefinition definition, MemoryCiscoSNMP snmp) throws SNMPException {
        //super(snmp.getDevice(), pollInterval, DataType.STORAGE);
        this.definition = definition;
        this.snmp = snmp;
        //this.memoryIndex = memoryIndex;
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
            point[0] = new DataPoint(currentTime, snmp.getCiscoMemoryUsed(definition.getMemoryIndex()));
        } catch (SNMPException e) {
            point[0] = new DataPoint(currentTime);
            logger.warn("Timeout fetching storage used");
        }
        
        return new DataCollectorResponse(point, new String[0], definition.getInitialHeadings().length);
    }
    
    /*
    public int getIndex() {
        return memoryIndex;
    }
    */
    
    public MemoryCiscoCollectorDefinition getDefinition() {
        return definition;
    }
    
    /** Empty routine as there are no resources to release. */
    public void releaseCollector() {
    }
}
