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

import nz.co.abrahams.asithappens.storage.DeviceDAO;
import nz.co.abrahams.asithappens.storage.Device;
import nz.co.abrahams.asithappens.core.DAOFactory;
import nz.co.abrahams.asithappens.snmputil.PortsSelectorSNMP;
import nz.co.abrahams.asithappens.snmputil.SNMPException;
import nz.co.abrahams.asithappens.core.DBException;
import java.util.Vector;
import java.net.UnknownHostException;

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
    protected boolean retrieveRWCommunity;
    
    /** Creates a new instance of PortsSelectorModel */
    public PortsSelectorModel(boolean retrieveRWCommunity) {
        this.retrieveRWCommunity = retrieveRWCommunity;
    }
    
    /*
    public void setDevice(String deviceName, String community) throws UnknownHostException, SNMPException {
        if ( retrieveRWCommunity ) {
            device = new Device(deviceName, null, community, true);
            snmp = new PortsSelectorSNMP(device);
        }
        else {
            device = new Device(deviceName, community, null, false);
            snmp = new PortsSelectorSNMP(device);
        }
    }
    */
    
    public void setDevice(Device newDevice) throws UnknownHostException, SNMPException {
        device = newDevice;
        snmp = new PortsSelectorSNMP(device, retrieveRWCommunity);
    }
    
    public Device getDevice() {
        return device;
    }

    public PortsSelectorSNMP getSNMP() {
        return snmp;
    }

    public void enumeratePorts() throws SNMPException, DBException {
        //DeviceDAO deviceDAO;
        
        snmp.enumeratePorts();
        /*
        deviceDAO = DAOFactory.getDeviceDAO();
        if ( retrieveRWCommunity )
            deviceDAO.updateDevice(snmp.getDevice().getName(), snmp.getDevice().getSNMPVersion(),
                    null, snmp.getDevice().getCommunityWrite(), null, null, null, null);
        else
            deviceDAO.updateDevice(snmp.getDevice().getName(), snmp.getDevice().getSNMPVersion(),
                    snmp.getDevice().getCommunityRead(), null, null, null, null, null);
        deviceDAO.closeConnection();
        */
        
    }
    
    /*
    public String retrieveCommunity(String deviceName) throws DBException {
        Device temporaryDevice;
        
        temporaryDevice = new Device(deviceName);
        if ( retrieveRWCommunity )
            return temporaryDevice.retrieveWriteCommunity();
        else
            return temporaryDevice.retrieveReadCommunity();
    }
    */
    
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
