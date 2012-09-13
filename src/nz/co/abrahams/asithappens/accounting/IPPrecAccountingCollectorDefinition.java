/*
 * IPPrecAccountingCollectorDefinition.java
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
import nz.co.abrahams.asithappens.collectors.InterfaceDirectionCollectorDefinition;
import nz.co.abrahams.asithappens.core.DataType;
import nz.co.abrahams.asithappens.snmputil.PortsSelectorSNMP;
import nz.co.abrahams.asithappens.snmputil.SNMPAccessType;
import nz.co.abrahams.asithappens.snmputil.SNMPException;
import nz.co.abrahams.asithappens.storage.Device;
import nz.co.abrahams.asithappens.storage.Direction;

public class IPPrecAccountingCollectorDefinition extends InterfaceDirectionCollectorDefinition {

    /**
     * Creates a new IPPrecAccountingCollectorDefinition.
     *
     * @param device the name or IP address of the target device
     * @param pollInterval the polling interval in milliseconds
     * @param ifdescr the interface to collect from
     * @param direction the direction of traffic to collect about
     */
    public IPPrecAccountingCollectorDefinition(String title, Device device,
            long pollInterval, boolean storing,
            String ifDescr, Direction direction) {
        super(title, device, pollInterval, DataType.IPPREC_ACCOUNTING, storing, ifDescr, direction);
    }
    
    public IPPrecAccountingCollector spawnCollector() throws UnknownHostException, SNMPException {
        PortsSelectorSNMP portsSNMP;
        int ifIndex;
        IPPrecAccountingSNMP snmp;
        
        portsSNMP = new PortsSelectorSNMP(device, SNMPAccessType.ReadOnly);
        ifIndex = portsSNMP.getIfIndex(ifDescr);
        snmp = new IPPrecAccountingSNMP(device, ifIndex, ifDescr, direction);
        return new IPPrecAccountingCollector(this, snmp);
    }
    
}
