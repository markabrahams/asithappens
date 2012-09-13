/*
 * NetFlowCollectorDefinition.java
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
package nz.co.abrahams.asithappens.netflow;

import java.net.UnknownHostException;
import nz.co.abrahams.asithappens.collectors.InterfaceDirectionCollectorDefinition;
import nz.co.abrahams.asithappens.core.DataType;
import nz.co.abrahams.asithappens.flow.FlowOptions;
import nz.co.abrahams.asithappens.snmputil.PortsSelectorSNMP;
import nz.co.abrahams.asithappens.snmputil.SNMPAccessType;
import nz.co.abrahams.asithappens.snmputil.SNMPException;
import nz.co.abrahams.asithappens.storage.Device;
import nz.co.abrahams.asithappens.storage.Direction;

public class NetFlowCollectorDefinition extends InterfaceDirectionCollectorDefinition {

    /** Match criteria for which packets to observe */
    protected NetFlowMatchCriteria criteria;
    
    /** Flow grouping options */
    protected FlowOptions options;
    
    /** NetFlow table size */
    protected int tableSize;
    
    /**
     * Creates a new NetFlowCollectorDefinition.
     *
     * @param device the name or IP address of the target device
     * @param pollInterval the polling interval in milliseconds
     */
    public NetFlowCollectorDefinition(String title, Device device,
            long pollInterval, boolean storing,
            String ifDescr, Direction direction,
            NetFlowMatchCriteria criteria, FlowOptions options, int tableSize) {
        super(title, device, pollInterval, DataType.NETFLOW, storing, ifDescr, direction);
        this.criteria = criteria;
        this.options = options;
        this.tableSize = tableSize;
    }
    
    public NetFlowMatchCriteria getCriteria() {
        return criteria;
    }
    
    public FlowOptions getOptions() {
        return options;
    }
    
    public int getTableSize() {
        return tableSize;
    }

    public NetFlowCollector spawnCollector() throws UnknownHostException, SNMPException {
        PortsSelectorSNMP portsSNMP;
        int ifIndex;
        NetFlowSNMP snmp;
        
        portsSNMP = new PortsSelectorSNMP(device, SNMPAccessType.ReadWrite);
        ifIndex = portsSNMP.getIfIndex(ifDescr);
        snmp = new NetFlowSNMP(device, ifIndex);
        return new NetFlowCollector(this, snmp);
    }
    
}
