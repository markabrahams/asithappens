/*
 * PortsSelectorSNMP.java
 *
 * Created on 06 January 2010, 20:47
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

package nz.co.abrahams.asithappens.snmputil;

import nz.co.abrahams.asithappens.core.Configuration;
import nz.co.abrahams.asithappens.core.DBException;
import nz.co.abrahams.asithappens.snmputil.SNMPAccess;
import nz.co.abrahams.asithappens.snmputil.SNMPException;
import nz.co.abrahams.asithappens.snmputil.SNMPInterface;
import nz.co.abrahams.asithappens.storage.Device;
import nz.co.abrahams.asithappens.storage.DeviceDAO;
import org.apache.log4j.Logger;
import java.util.Vector;
import java.util.LinkedList;
import java.net.UnknownHostException;

/**
 *
 * @author mark
 */
public class PortsSelectorSNMP extends SNMPInterface {

    /* SNMP interface */
    //private SNMPAccess snmpAccess;

    /** Logging provider */
    private static Logger logger = Logger.getLogger(PortsSelectorSNMP.class);

    /** SNMP ifIndex list */
    protected int[] portsIndex;

    /** SNMP ifDescr list */
    protected String[] portsDescr;

    /** SNMP ifAlias list */
    protected String[] portsAlias;

    /**
     * Creates the SNMP interface for ports selection.
     *
     * @param device
     * @throws UnknownHostException
     * @throws SNMPException
     */
    public PortsSelectorSNMP(Device device) throws UnknownHostException, SNMPException {
        super(device);
        if ( device.getCommunityRead() != null )
            snmpAccess = device.createSNMPReadInterface();
        else
            snmpAccess = device.createSNMPWriteInterface();
    }


    /** @return the ifIndex list for the device */
    public int[] getPortsIndex() {
        return portsIndex;
    }

    /** @return the ifDescr list for the device */
    public String[] getPortsDescr() {
        return portsDescr;
    }

    /**
     * Retrieve the SNMP ifDescr of an interface given its ifIndex.
     *
     * @param port  ifIndex of interface
     * @return      retrieved ifDescr of interface
     */
    public String getIfDescr(int port) throws SNMPException {
        return snmpAccess.getMIBValueString(SNMPAccess.OID_IFDESCR + "." + String.valueOf(port));
    }

    /**
     * Retrieve the SNMP ifIndex of an interface given its ifDescr.
     *
     * @param portString  ifDescr of interface
     * @return            retrieved ifIndex of interface
     */
    public int getIfIndex(String portString) throws SNMPException {
        int portIndex;

        portIndex = -1;
        if ( ! hasEnumerated() )
            enumeratePorts();
        for ( int i = 0 ; i < getPortsIndex().length ; i++ ) {
            if ( getPortsDescr()[i].equals(portString) ) {
                portIndex = getPortsIndex()[i];
            }
        }
        // should check for -1 index and throw SNMPException here?
        return portIndex;
    }

    /**
     * Retrieve the SNMP ifIndicies of an array of interfaces their ifDescr.
     *
     * @param portString  ifDescr array of interfaces
     * @return            retrieved ifIndex values of interfaces
     */
    public int[] getIfIndexArray(String[] portStrings) throws SNMPException {
        int portIndicies[];

        portIndicies = new int[portStrings.length];
        if ( ! hasEnumerated() )
            enumeratePorts();
        for ( int i = 0 ; i < portsIndex.length ; i++ ) {
            for ( int j = 0 ; j < portStrings.length ; j++ ) {
                if ( portsDescr[i].equals(portStrings[j]) ) {
                    portIndicies[j] = portsIndex[i];
                }
            }
        }
        return portIndicies;

    }

    public boolean hasEnumerated() {
        return portsIndex != null;
    }

    /**
     * Enumerates interfaces on a device in via the interfaces MIB table. The
     * interfaces and description are stored in the portsIndex and portsDescr
     * instance variables.
     */
    public void enumeratePorts() throws SNMPException {
        int numInterfaces;
        int count;
        String[] tempPortsDescr;
        String[] tempPortsAlias;
        int[] tempPortsIndex;
        String ifIndexOID;
        String ifDescrOID;
        String ifDescrValue;
        String ifAliasOID;
        String ifAliasValue;
        int ifIndexValue;
        LinkedList getNextPair;
        DeviceDAO deviceDAO;
        boolean fetchIfAlias;

        numInterfaces = snmpAccess.getMIBValueInteger(SNMPAccess.OID_IFNUMBER);
        logger.debug("Number of interfaces: " + numInterfaces);
        tempPortsDescr = new String[numInterfaces];
        tempPortsAlias = new String[numInterfaces];
        tempPortsIndex = new int[numInterfaces];
        ifIndexOID = SNMPAccess.OID_IFNUMBER;
        count = 0;
        ifAliasValue = new String("");
        if ( Configuration.getPropertyInt("device.ports.ifalias") == 1 )
            fetchIfAlias = true;
        else
            fetchIfAlias = false;

        try {
            for ( count = 0 ; count < numInterfaces ; count++ ) {
                getNextPair = snmpAccess.getNextMIBValue(ifIndexOID, SNMPType.Integer32, SNMPAccess.OID_IFINDEX);
                ifIndexOID = (String)getNextPair.getFirst();
                ifIndexValue = ((Integer)getNextPair.getLast()).intValue();
                ifDescrOID = SNMPAccess.OID_IFDESCR + "." + String.valueOf(ifIndexValue);
                ifDescrValue = snmpAccess.getMIBValueString(ifDescrOID);
                if ( fetchIfAlias ) {
                    ifAliasOID = SNMPAccess.OID_IFALIAS + "." + String.valueOf(ifIndexValue);
                    try {
                        ifAliasValue = snmpAccess.getMIBValueString(ifAliasOID);
                    } catch (SNMPException e) {
                        fetchIfAlias = false;
                    }
                }
                logger.debug("Found interface: ifIndex=" + ifIndexValue + ", ifDescr=" + ifDescrValue);

                tempPortsDescr[count] = ifDescrValue;
                tempPortsIndex[count] = ifIndexValue;
                if ( fetchIfAlias )
                    tempPortsAlias[count] = ifAliasValue;
            }
            portsIndex = tempPortsIndex;
            portsDescr = tempPortsDescr;
            if ( fetchIfAlias )
                portsAlias = tempPortsAlias;

        } catch (SNMPScopeException e) {
            // Create partial port list if exception occurs after successfully enumerating some ports
            if ( count > 0 ) {
                portsIndex = new int[count];
                portsDescr = new String[count];
                System.arraycopy(tempPortsIndex, 0, portsIndex, 0, count);
                System.arraycopy(tempPortsDescr, 0, portsDescr, 0, count);
                if ( fetchIfAlias ) {
                    portsAlias = new String[count];
                    System.arraycopy(tempPortsAlias, 0, portsAlias, 0, count);
                }
                //e.printStackTrace();
                logger.warn("Port enumeration partially failed");
            } else {
                e.printStackTrace();
                logger.error("Port enumeration failed");
                throw new SNMPException("Port enumeration failed", e);
            }
        }

        /*
        deviceDAO = DAOFactory.getDeviceDAO();
        if ( useWriteCommunity )
            deviceDAO.updateDevice(name, null, communityWrite, null, null);
        else
            deviceDAO.updateDevice(name, communityRead, null, null, null);
        deviceDAO.closeConnection();
         */
        /*
        catch (SNMPTypeException e) {
            logger.error("Unexpected SNMP variable type");
            throw new SNMPException("Unexpected SNMP variable type");
        }
         */
    }

    public Vector getPortsVector() {
        Vector data;
        Vector row;

        if ( portsIndex != null ) {
            data = new Vector();
            for ( int i = 0 ; i < portsIndex.length ; i++ ) {
                row = new Vector();
                row.addElement(portsDescr[i]);
                if ( portsAlias != null )
                    row.addElement(portsAlias[i]);
                else
                    row.addElement("");
                data.addElement(row);
            }
            return data;
        } else {
            return null;
        }
    }

}
