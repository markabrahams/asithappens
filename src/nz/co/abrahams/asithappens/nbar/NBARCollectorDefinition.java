/*
 * NBARCollectorDefinition.java
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
package nz.co.abrahams.asithappens.nbar;

import java.net.UnknownHostException;
import nz.co.abrahams.asithappens.collectors.InterfaceDirectionCollectorDefinition;
import nz.co.abrahams.asithappens.core.DataType;
import nz.co.abrahams.asithappens.snmputil.PortsSelectorSNMP;
import nz.co.abrahams.asithappens.snmputil.SNMPAccessType;
import nz.co.abrahams.asithappens.snmputil.SNMPException;
import nz.co.abrahams.asithappens.storage.Device;
import nz.co.abrahams.asithappens.storage.Direction;

public class NBARCollectorDefinition extends InterfaceDirectionCollectorDefinition {

    /** NBAR table size */
    protected int tableSize;
        
    /**
     * Creates a NBARCollectorDefinition.
     *
     * @param device the name or IP address of the target device
     * @param pollInterval the polling interval in milliseconds
     */
    public NBARCollectorDefinition(String title, Device device, long pollInterval, boolean storing,
            String ifDescr, Direction direction, int tableSize) {
        super(title, device, pollInterval, DataType.NBAR, storing, ifDescr, direction);
        this.tableSize = tableSize;
    }
    
    public int getTableSize() {
        return tableSize;
    }

    public NBARCollector spawnCollector() throws UnknownHostException, SNMPException {
        PortsSelectorSNMP portsSNMP;
        int ifIndex;
        NBARSNMP snmp;
        
        portsSNMP = new PortsSelectorSNMP(device, SNMPAccessType.ReadWrite);
        ifIndex = portsSNMP.getIfIndex(ifDescr);
        snmp = new NBARSNMP(device, ifIndex, direction);
        return new NBARCollector(this, snmp);
    }
    
}
