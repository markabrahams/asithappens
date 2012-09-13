/*
 * ProcessorHRCollectorDefinition.java
 *
 * Created on 1 September 2012, 10:28
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
import nz.co.abrahams.asithappens.collectors.CollectorDefinition;
import nz.co.abrahams.asithappens.core.DataType;
import nz.co.abrahams.asithappens.snmputil.SNMPException;
import nz.co.abrahams.asithappens.storage.Device;

public class ProcessorHRCollectorDefinition extends CollectorDefinition {

    /** Units */
    public static final String UNITS = "%age";
    
    /** Initial headings */
    public static final String[] INITIAL_HEADINGS = {"Load"};
    
    /** Text description of processor */
    protected String processorString;

    /** Index into processor table */
    protected int processorIndex;

    /**
     * Creates a new ProcessorHRCollectorDefinition.
     *
     * @param device the name or IP address of the target device
     * @param pollInterval the polling interval in milliseconds
     */
    public ProcessorHRCollectorDefinition(String title, Device device,
            long pollInterval, boolean storing,
            String processorString, int processorIndex) {
        super(title, device, pollInterval, DataType.PROCESSOR, UNITS, storing);
        this.processorString = processorString;
        this.processorIndex = processorIndex;
    }
    
    public String[] getInitialHeadings() {
        return INITIAL_HEADINGS;
    }
    
    public String getProcessorString() {
        return processorString;
    }

    public int getProcessorIndex() {
        return processorIndex;
    }

    @Override
    public String getDescription() {
        return super.getDescription() + " (" + processorString + ")";
    }

    public ProcessorHRCollector spawnCollector() throws UnknownHostException, SNMPException {
        ProcessorHRSNMP snmp;
        
        snmp = new ProcessorHRSNMP(device);
        return new ProcessorHRCollector(this, snmp);
    }
    
}
