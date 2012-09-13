/*
 * BandwidthCollectorDefinition.java
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
package nz.co.abrahams.asithappens.bandwidth;

import java.net.UnknownHostException;
import nz.co.abrahams.asithappens.accounting.*;
import nz.co.abrahams.asithappens.collectors.CollectorDefinition;
import nz.co.abrahams.asithappens.core.DataType;
import nz.co.abrahams.asithappens.snmputil.SNMPException;
import nz.co.abrahams.asithappens.storage.Device;

public class BandwidthCollectorDefinition extends CollectorDefinition {

    /** Units */
    public static final String UNITS = "bps";
    
    /** Initial headings */
    public static final String[] INITIAL_HEADINGS = {"In", "Out"};
    
    /** ifIndex array of all interfaces to collect from */
    //protected int[] ifIndices;
    
    /** ifDescr array of all interfaces to collect from */
    protected String[] ifDescrs;
    
    /** Prefer 64-bit counters if present */
    protected boolean prefer64BitCounters;

    /**
     * Creates a new BandwidthCollectorDefinition.
     *
     * @param device the name or IP address of the target device
     * @param pollInterval the polling interval in milliseconds
     */
    public BandwidthCollectorDefinition(String title, Device device,
            long pollInterval, boolean storing,
            String[] ifDescrs, boolean prefer64BitCounters) {
        super(title, device, pollInterval, DataType.BANDWIDTH, UNITS, storing);
        this.ifDescrs = ifDescrs;
        this.prefer64BitCounters = prefer64BitCounters;
    }
    
    public String[] getInitialHeadings() {
        return INITIAL_HEADINGS;
    }
    
    public String getDescription() {
        return super.getDescription() + " (" + getIfDescrString() + ")";
    }
    
    public String[] getIfDescrs() {
        return ifDescrs;
    }
    
    public boolean getPrefer64BitCounters() {
        return prefer64BitCounters;
    }
    
    public String getIfDescrString() {
        String portString;
        
        portString = new String();
        for ( int i = 0 ; i < ifDescrs.length ; i++) {
            portString = portString + ifDescrs[i];
            if ( i < ifDescrs.length - 1 )
                portString = portString + ",";
        }
        return portString;
    }
    
    public BandwidthCollector spawnCollector() throws UnknownHostException, SNMPException {
        BandwidthSNMP snmp;
        
        snmp = new BandwidthSNMP(device);
        return new BandwidthCollector(this, snmp);
    }

}
