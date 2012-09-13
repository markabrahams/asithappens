/*
 * Device.java
 *
 * Created on 19 January 2005, 23:41
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
package nz.co.abrahams.asithappens.storage;

import java.net.InetAddress;
import java.net.UnknownHostException;
import nz.co.abrahams.asithappens.core.DAOFactory;
import nz.co.abrahams.asithappens.core.DBException;
import nz.co.abrahams.asithappens.snmputil.*;
import org.apache.log4j.Logger;
import org.doomdark.uuid.EthernetAddress;

/**
 * A device that is targeted for collecting data from.
 *
 * @author mark
 */
public class Device {

    /*
     * Logging provider
     */
    protected static Logger logger = Logger.getLogger(Device.class);
    /**
     * Device name
     */
    protected String name;
    /**
     * IP address
     */
    protected InetAddress address;
    /**
     * MAC address
     */
    protected EthernetAddress ethernetAddress;
    /**
     * SNMP read-only version
     */
    protected SNMPVersion snmpVersionRead;
    /**
     * SNMP read-only community string
     */
    protected String communityRead;
    /**
     * SNMP read-write version
     */
    protected SNMPVersion snmpVersionWrite;
    /**
     * SNMP read-write community string
     */
    protected String communityWrite;
    /**
     * SNMPv3 read-only user
     */
    protected USMUser usmUserRead;
    /**
     * SNMPv3 read-write user
     */
    protected USMUser usmUserWrite;

    /**
     * /** SNMP capability flag
     */
    //protected boolean snmpCapable;
    /**
     * Creates a new Device
     *
     * @param name device name
     */
    public Device(String name) {
        this.name = name;
    }

    /**
     * Creates a new Device with given IP and MAC addresses
     *
     * @param address IP address of device
     * @param ethernetAddress MAC address of device
     */
    public Device(String name, SNMPVersion snmpVersionRead, SNMPVersion snmpVersionWrite,
            String communityRead, String communityWrite,
            USMUser usmUserRead, USMUser usmUserWrite,
            String address, String ethernetAddress)
            throws UnknownHostException {
        this(name);
        this.snmpVersionRead = snmpVersionRead;
        this.snmpVersionWrite = snmpVersionWrite;
        this.communityRead = communityRead;
        this.communityWrite = communityWrite;
        this.usmUserRead = usmUserRead;
        this.usmUserWrite = usmUserWrite;
        //this.useWriteCommunity = useWrite;

        if (address != null && !address.equals("null")) {
            //logger.debug("Device address: " + address);
            this.address = InetAddress.getByName(address);

        }
        if (ethernetAddress != null) {
            this.ethernetAddress = new EthernetAddress(ethernetAddress);
        }
    }

    // Note - Move this to a new class called "FlowHost"
    /**
     * Creates a new Device with given IP and MAC addresses
     *
     * @param address IP address of device
     * @param ethernetAddress MAC address of device
     */
    public Device(InetAddress address, EthernetAddress ethernetAddress) {
        this(address.getHostAddress());

        this.address = address;
        this.ethernetAddress = ethernetAddress;
    }

    // Note - Move this to new class called "FlowHost"
    /**
     * Creates a new Device with given MAC address
     *
     * @param ethernetAddress MAC address of device
     */
    public Device(EthernetAddress ethernetAddress) {
        this(ethernetAddress.toString());
        this.ethernetAddress = ethernetAddress;
    }

    /**
     * @return device name
     */
    public String getName() {
        return name;
    }

    /**
     * Attempts to resolve the device name to an IP address using the local name
     * service.
     *
     * @return the IP address of the device
     */
    public InetAddress resolveAddress() throws UnknownHostException {
        try {
            address = InetAddress.getByName(name);
            return address;
        } catch (UnknownHostException e) {
            logger.warn("Cannot resolve host name \"" + name + "\"");
            throw e;
        }
    }

    /**
     * Creates a read-only interface to the SNMP access class.
     *
     * @return read-only SNMP interface
     */
    public SNMPAccess createSNMPReadInterface() throws UnknownHostException, SNMPException {
        if (snmpVersionRead == SNMPVersion.v1) {
            if ( communityRead != null ) {
                return new SNMPAccess(getResolvedAddress(), communityRead);
            } else {
                throw new SNMPNoCredentialsException("No read-only community found");
            }
        } else {
            if ( usmUserRead != null ) {
                return new SNMPAccess(getResolvedAddress(), usmUserRead);
            } else {
                throw new SNMPNoCredentialsException("No read-only user found");
            }
        }
    }

    /**
     * Creates a read-write interface to the SNMP access class.
     *
     * @return read-write SNMP interface
     */
    public SNMPAccess createSNMPWriteInterface() throws UnknownHostException, SNMPException {
        if (snmpVersionWrite == SNMPVersion.v1) {
            if ( communityWrite != null ) {
                return new SNMPAccess(getResolvedAddress(), communityWrite);
            } else {
                throw new SNMPNoCredentialsException("No read-write community found");                
            }
        } else {
            if ( usmUserWrite != null ) {
                return new SNMPAccess(getResolvedAddress(), usmUserWrite);
            } else {
                throw new SNMPNoCredentialsException("No read-write user found");                
            }
        }
    }

    /**
     * @return the IP address of the device
     */
    public InetAddress getResolvedAddress() throws UnknownHostException {
        if (address == null) {
            address = resolveAddress();
        }

        return address;
    }

    public InetAddress getAddress() {
        return address;
    }

    /**
     * @return the MAC address of the device
     */
    public EthernetAddress getEthernetAddress() {
        return ethernetAddress;
    }

    public SNMPVersion getSNMPVersionRead() {
        return snmpVersionRead;
    }

    public SNMPVersion getSNMPVersionWrite() {
        return snmpVersionWrite;
    }

    /**
     * @return the SNMP read community string for the device
     */
    public String getCommunityRead() {
        return communityRead;
    }

    /**
     * @return the SNMP write community string for the device
     */
    public String getCommunityWrite() {
        return communityWrite;
    }

    public USMUser getUsmUserRead() {
        return usmUserRead;
    }

    public USMUser getUsmUserWrite() {
        return usmUserWrite;
    }

    @Override
    public String toString() {
        return name;
    }
}
