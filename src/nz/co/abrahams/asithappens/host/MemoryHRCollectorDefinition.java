/*
 * MemoryHRCollectorDefinition.java
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

public class MemoryHRCollectorDefinition extends CollectorDefinition {

    /** Units */
    public static final String UNITS = "bytes";
    
    /** Initial headings */
    public static final String[] INITIAL_HEADINGS = {"Used"};
    
    /** Description of memory/storage object */
    protected String memoryString;

    /** Index into memory table */
    protected int memoryIndex;

    /**
     * Creates a new MemoryCiscoCollectorDefinition.
     *
     * @param device the name or IP address of the target device
     * @param pollInterval the polling interval in milliseconds
     */
    public MemoryHRCollectorDefinition(String title, Device device,
            long pollInterval, boolean storing,
            String memoryString, int memoryIndex) {
        super(title, device, pollInterval, DataType.STORAGE, UNITS, storing);
        this.memoryString = memoryString;
        this.memoryIndex = memoryIndex;
    }
    
    public String[] getInitialHeadings() {
        return INITIAL_HEADINGS;
    }
    
    public String getMemoryString() {
        return memoryString;
    }

    public int getMemoryIndex() {
        return memoryIndex;
    }
    
    @Override
    public String getDescription() {
        return super.getDescription() + " (" + memoryString + ")";
    }

    public MemoryHRCollector spawnCollector() throws UnknownHostException, SNMPException {
        MemoryHRSNMP snmp;
        
        snmp = new MemoryHRSNMP(device);
        return new MemoryHRCollector(this, snmp);
    }
    
}
