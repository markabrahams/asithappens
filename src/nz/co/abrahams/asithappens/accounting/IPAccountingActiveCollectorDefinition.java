/*
 * IPAccountingActiveCollectorDefinition.java
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
package nz.co.abrahams.asithappens.accounting;

import java.net.UnknownHostException;
import nz.co.abrahams.asithappens.collectors.CollectorDefinition;
import nz.co.abrahams.asithappens.collectors.SNMPTableCollector;
import nz.co.abrahams.asithappens.core.DataType;
import nz.co.abrahams.asithappens.snmputil.SNMPException;
import nz.co.abrahams.asithappens.snmputil.SNMPType;
import nz.co.abrahams.asithappens.storage.Device;

public class IPAccountingActiveCollectorDefinition extends CollectorDefinition {

    /** Units */
    public static final String UNITS = "bps";
    
    /** Initial headings */
    public static final String[] INITIAL_HEADINGS = {};

    /**
     * Creates a new IPAccountingCheckpointCollectorDefinition.
     *
     * @param device the name or IP address of the target device
     * @param pollInterval the polling interval in milliseconds
     */
    public IPAccountingActiveCollectorDefinition(String title, Device device,
            long pollInterval, boolean storing) {
        super(title, device, pollInterval, DataType.ACCOUNTING, UNITS, storing);
    }
    
    public String[] getInitialHeadings() {
        return INITIAL_HEADINGS;
    }
    
    public SNMPTableCollector spawnCollector() throws UnknownHostException, SNMPException {
        IPAccountingActiveSNMP snmp;
        
        snmp = new IPAccountingActiveSNMP(device);
        //return new IPAccountingActiveCollector(this, snmp);
        return new SNMPTableCollector(this, snmp, SNMPType.Integer32);
    }

}
