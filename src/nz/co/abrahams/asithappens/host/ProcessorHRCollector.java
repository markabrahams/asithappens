/*
 * ProcessorHRCollector.java
 *
 * Created on 24 February 2006, 02:17
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

import java.net.UnknownHostException;
import nz.co.abrahams.asithappens.collectors.DataCollector;
import nz.co.abrahams.asithappens.collectors.DataCollectorResponse;
import nz.co.abrahams.asithappens.snmputil.SNMPException;
import nz.co.abrahams.asithappens.storage.DataHeadings;
import nz.co.abrahams.asithappens.storage.DataPoint;
import org.apache.log4j.Logger;

/**
 * Processor load collector.
 *
 * @author  mark
 */
public class ProcessorHRCollector implements DataCollector {
    
    /** Logging provider */
    private static Logger logger = Logger.getLogger(ProcessorHRCollector.class);

    /** Collector definition */
    ProcessorHRCollectorDefinition definition;

    /** SNMP interface */
    private ProcessorHRSNMP snmp;
    
    /** An index into the host resource device table specifying the processor to collect for */
    private int processor;
    
    /**
     * Creates a new BandwidthCollector.
     *
     * @param device       the name or IP address of the target device
     * @param processor    the index from the processor table to collect data about
     * @param pollInterval the polling interval in milliseconds
     */
    public ProcessorHRCollector(ProcessorHRCollectorDefinition definition, ProcessorHRSNMP snmp) throws UnknownHostException, SNMPException {
        //super(snmp.getDevice(), pollInterval, DataType.PROCESSOR);
        //this.processor = processor;
        
        //snmp = new ProcessorHRSNMP(device);
        this.definition = definition;
        this.snmp = snmp;
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
        double load;
        DataPoint[] point;
        
        point = new DataPoint[1];
        currentTime = System.currentTimeMillis();
        
        try {
            point[0] = new DataPoint(currentTime, snmp.getProcessorLoad(definition.getProcessorIndex()));
        } catch (SNMPException e) {
            //e.printStackTrace();
            point[0] = new DataPoint(currentTime);
            logger.warn("Timeout fetching processor load");
        }
        
        return new DataCollectorResponse(point, new String[0], definition.getInitialHeadings().length);
    }
    
    public ProcessorHRCollectorDefinition getDefinition() {
        return definition;
    }    
    
    public int getIndex() {
        return processor;
    }

    /** Empty routine as there are no resources to release. */
    public void releaseCollector() {
    }
}
