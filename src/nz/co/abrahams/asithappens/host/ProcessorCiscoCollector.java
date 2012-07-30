/*
 * ProcessorCiscoCollector.java
 *
 * Created on 27 February 2006, 22:27
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
 * Processor load collector for Cisco devices.
 *
 * @author  mark
 */
public class ProcessorCiscoCollector extends DataCollector {
    
    /** Logging provider */
    private static Logger logger = Logger.getLogger(ProcessorCiscoCollector.class);

    /** SNMP interface */
    private ProcessorCiscoSNMP snmp;
    
    /**
     * Creates a new ProcessorCiscoCollector.
     *
     * @param device       the name or IP address of the target device
     * @param pollInterval the polling interval in milliseconds
     */
    public ProcessorCiscoCollector(ProcessorCiscoSNMP snmp, long pollInterval) throws SNMPException {
        super(snmp.getDevice(), pollInterval, DataType.PROCESSOR);

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
            point[0] = new DataPoint(currentTime, snmp.getCiscoProcessorLoad());
        } catch (SNMPException e) {
            point[0] = new DataPoint(currentTime);
            logger.warn("Timeout fetching processor load");
        }
        
        return new DataCollectorResponse(point, new String[0], dataType.initialSetCount());
    }

    /** Empty routine as there are no resources to release. */
    public void releaseCollector() {
    }
}
