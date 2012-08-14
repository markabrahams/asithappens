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
import nz.co.abrahams.asithappens.snmputil.SNMPAccess;
import nz.co.abrahams.asithappens.snmputil.SNMPException;
import nz.co.abrahams.asithappens.snmputil.SNMPVersion;
import nz.co.abrahams.asithappens.snmputil.USMUser;
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
     * SNMP version
     */
    protected SNMPVersion snmpVersion;
    /**
     * SNMP read-only community string
     */
    protected String communityRead;
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
     * Indicates whether ro or rw community is to be used
     */
    protected boolean useWriteCommunity;

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
     * Creates a new Device with SNMP capabiliity
     *
     * @param name device name
     * @param community SNMP community string
     */
    /*
     * public Device(String name, String communityRead, String communityWrite,
     * boolean useWrite) throws UnknownHostException, SNMPException {
     * this(name); this.communityRead = communityRead; this.communityWrite =
     * communityWrite; this.useWriteCommunity = useWrite; }
     */
    /**
     * Creates a new Device with SNMP capability
     *
     * @param name device name
     * @param community SNMP community string
     */
    /*
     * public Device(String name, USMUser usmUserRead, USMUser usmUserWrite,
     * boolean useWrite) throws UnknownHostException, SNMPException {
     * this(name); this.usmUserRead = usmUserRead; this.usmUserWrite =
     * usmUserWrite; this.useWriteCommunity = useWrite; }
     */
    /**
     * Creates a new Device with given IP and MAC addresses
     *
     * @param address IP address of device
     * @param ethernetAddress MAC address of device
     */
    public Device(String name, SNMPVersion snmpVersion,
            String communityRead, String communityWrite,
            USMUser usmUserRead, USMUser usmUserWrite,
            boolean useWrite, String address, String ethernetAddress)
            throws UnknownHostException, SNMPException {
        this(name);
        this.snmpVersion = snmpVersion;
        this.communityRead = communityRead;
        this.communityWrite = communityWrite;
        this.usmUserRead = usmUserRead;
        this.usmUserWrite = usmUserWrite;
        this.useWriteCommunity = useWrite;

        if (address != null && !address.equals("null")) {
            logger.info("Device address: " + address);
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
     * Creates an interface to the SNMP access class.
     *
     * @param community SNMP community string
     */
    /*
     * public SNMPAccess createSNMPInterface(String community) throws
     * UnknownHostException, SNMPException { snmpCapable = false; snmpAccess =
     * new SNMPAccess(getResolvedAddress(), community); //this.communityRead =
     * community; snmpCapable = true; return snmpAccess; }
     */
    /**
     * Creates a read-only interface to the SNMP access class.
     *
     * @return read-only SNMP interface
     */
    public SNMPAccess createSNMPReadInterface() throws UnknownHostException, SNMPException {
        if (snmpVersion == SNMPVersion.v1) {
            return new SNMPAccess(getResolvedAddress(), communityRead);
        } else {
            return new SNMPAccess(getResolvedAddress(), usmUserRead);
        }
    }

    /**
     * Creates a read-write interface to the SNMP access class.
     *
     * @return read-write SNMP interface
     */
    public SNMPAccess createSNMPWriteInterface() throws UnknownHostException, SNMPException {
        if (snmpVersion == SNMPVersion.v1) {
            return new SNMPAccess(getResolvedAddress(), communityWrite);
        } else {
            return new SNMPAccess(getResolvedAddress(), usmUserWrite);
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

    public SNMPVersion getSNMPVersion() {
        return snmpVersion;
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

    /*
     * public String retrieveReadCommunity() throws DBException { DeviceDAO
     * deviceDAO; String community;
     *
     * deviceDAO = DAOFactory.getDeviceDAO(); community =
     * deviceDAO.retrieveDeviceReadCommunity(name); deviceDAO.closeConnection();
     * return community; }
     *
     * public String retrieveWriteCommunity() throws DBException { DeviceDAO
     * deviceDAO; String community;
     *
     * deviceDAO = DAOFactory.getDeviceDAO(); community =
     * deviceDAO.retrieveDeviceWriteCommunity(name);
     * deviceDAO.closeConnection(); return community; }
     *
     * public USMUser retrieveReadUser() throws DBException, SNMPException {
     * DeviceDAO deviceDAO; USMUser user;
     *
     * deviceDAO = DAOFactory.getDeviceDAO(); user =
     * deviceDAO.retrieveDeviceReadUser(name); deviceDAO.closeConnection();
     * return user; }
     *
     * public USMUser retrieveWriteUser() throws DBException, SNMPException {
     * DeviceDAO deviceDAO; USMUser user;
     *
     * deviceDAO = DAOFactory.getDeviceDAO(); user =
     * deviceDAO.retrieveDeviceWriteUser(name); deviceDAO.closeConnection();
     * return user; }
     */
    @Override
    public String toString() {
        return name;
    }
}
