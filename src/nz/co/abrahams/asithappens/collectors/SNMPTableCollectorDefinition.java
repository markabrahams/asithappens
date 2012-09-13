/*
 * IPAccountingCheckpointCollectorDefinition.java
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
package nz.co.abrahams.asithappens.collectors;

import java.net.UnknownHostException;
import nz.co.abrahams.asithappens.core.DataType;
import nz.co.abrahams.asithappens.snmputil.SNMPException;
import nz.co.abrahams.asithappens.snmputil.SNMPType;
import nz.co.abrahams.asithappens.storage.Device;

public class SNMPTableCollectorDefinition extends CollectorDefinition {

    /** Units */
    public static final String UNITS = "bps";
    
    /** Initial headings */
    public static final String[] INITIAL_HEADINGS = {};
    
    /** SNMP table interface */
    protected SNMPTableInterface snmp;
    
    /** SNMP type of value collected */
    protected SNMPType snmpType;

    /**
     * Creates a new IPAccountingCheckpointCollectorDefinition.
     *
     * @param device the name or IP address of the target device
     * @param pollInterval the polling interval in milliseconds
     */
    public SNMPTableCollectorDefinition(String title, Device device,
            long pollInterval, boolean storing,
            DataType dataType, SNMPTableInterface snmp, SNMPType snmpType) {
        super(title, device, pollInterval, dataType, UNITS, storing);
        this.snmp = snmp;
        this.snmpType = snmpType;
    }
    
    public String[] getInitialHeadings() {
        return INITIAL_HEADINGS;
    }
    
    public SNMPTableInterface getTableInterface() {
        return snmp;
    }
    
    public SNMPType getSNMPType() {
        return snmpType;
    }
    
    public SNMPTableCollector spawnCollector() throws UnknownHostException, SNMPException {
        //return new SNMPTableCollector(this, snmp);
        return null;
    }

}
