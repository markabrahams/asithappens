/*
 * PortsSelectorModel.java
 *
 * Created on 28 May 2008, 21:53
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
 *
 */

package nz.co.abrahams.asithappens.uiutil;

import java.net.UnknownHostException;
import java.util.Vector;
import nz.co.abrahams.asithappens.core.DBException;
import nz.co.abrahams.asithappens.snmputil.PortsSelectorSNMP;
import nz.co.abrahams.asithappens.snmputil.SNMPAccessType;
import nz.co.abrahams.asithappens.snmputil.SNMPException;
import nz.co.abrahams.asithappens.storage.Device;

/**
 *
 * @author mark
 */
public class PortsSelectorModel {
    
    /** The device for which ports are enumerated */
    public Device device;
    
    /** The device for which ports are enumerated */
    public PortsSelectorSNMP snmp;

    /** Set false to retrieve RO community, true for RW */
    //protected boolean retrieveRWCommunity;
    protected SNMPAccessType snmpAccessType;
    
    /** Creates a new instance of PortsSelectorModel */
    public PortsSelectorModel(SNMPAccessType snmpAccessType) {
        this.snmpAccessType = snmpAccessType;
    }
        
    public void setDevice(Device newDevice) throws UnknownHostException, SNMPException {
        device = newDevice;
        snmp = new PortsSelectorSNMP(device, snmpAccessType);
    }
    
    public Device getDevice() {
        return device;
    }

    public PortsSelectorSNMP getSNMP() {
        return snmp;
    }

    public void enumeratePorts() throws SNMPException, DBException {
        snmp.enumeratePorts();
    }
        
    public Vector getTableData() {
        return snmp.getPortsVector();
    }
    
    public Vector getTableHeadings() {
        Vector headings;
        
        headings = new Vector();
        headings.addElement("Port");
        headings.addElement("Description");
        return headings;
    }
    
    public boolean hasEnumerated() {
        return snmp != null && snmp.hasEnumerated();
    }
    
}
