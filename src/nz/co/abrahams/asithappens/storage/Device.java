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

import nz.co.abrahams.asithappens.core.DAOFactory;
import nz.co.abrahams.asithappens.snmputil.SNMPException;
import nz.co.abrahams.asithappens.snmputil.SNMPAccess;
import nz.co.abrahams.asithappens.snmputil.USMUser;
import nz.co.abrahams.asithappens.snmputil.USMLevel;
import nz.co.abrahams.asithappens.snmputil.USMAuthProtocol;
import nz.co.abrahams.asithappens.snmputil.USMPrivProtocol;
import nz.co.abrahams.asithappens.core.DBException;
import java.net.*;
import org.doomdark.uuid.EthernetAddress;
import org.apache.log4j.Logger;

/**
 * A device that is targetted for collecting data from.
 *
 * @author mark
 */
public class Device {
    
    /* Logging provider */
    protected static Logger logger = Logger.getLogger(Device.class);
    /** Device name */
    protected String name;
    /** IP address */
    protected InetAddress address;
    /** MAC address */
    protected EthernetAddress ethernetAddress;
    /** SNMP read-only community string */
    protected String communityRead;
    /** SNMP read-write community string */
    protected String communityWrite;
    /** SNMPv3 read-only user */
    private USMUser usmNameRead;
    /** SNMPv3 read-write user */
    private USMUser usmNameWrite;

    /** Indicates whether ro or rw community is to be used */
    protected boolean useWriteCommunity;

    private String userNameRead;
    private String userNameWrite;
    private USMLevel userLevelRead;
    private USMLevel userLevelWrite;
    private USMAuthProtocol userAuthProtocolRead;
    private USMAuthProtocol userAuthProtocolWrite;
    private String userAuthKeyRead;
    private String userAuthKeyWrite;
    private USMPrivProtocol userPrivProtocolRead;
    private USMPrivProtocol userPrivProtocolWrite;
    private String userPrivKeyRead;
    private String userPrivKeyWrite;

    /** 
    /** SNMP capability flag */
    //protected boolean snmpCapable;
    
    /**
     * Creates a new Device
     *
     * @param name  device name
     */
    public Device(String name) {
        this.name = name;
    }
    
    /**
     * Creates a new Device with SNMP capabiliity
     *
     * @param name      device name
     * @param community SNMP community string
     */
    public Device(String name, String communityRead, String communityWrite, boolean useWrite) throws UnknownHostException, SNMPException {
        this(name);
        this.communityRead = communityRead;
        this.communityWrite = communityWrite;
        this.useWriteCommunity = useWrite;
    }



    /**
     * Creates a new Device with given IP and MAC addresses
     *
     * @param address          IP address of device
     * @param ethernetAddress  MAC address of device
     */
    public Device(InetAddress address, EthernetAddress ethernetAddress) {
        this(address.getHostAddress());
        
        this.address = address;
        this.ethernetAddress = ethernetAddress;
    }
    
    /**
     * Creates a new Device with given MAC address
     *
     * @param ethernetAddress  MAC address of device
     */
    public Device(EthernetAddress ethernetAddress) {
        this(ethernetAddress.toString());
        this.ethernetAddress = ethernetAddress;
    }
    
    /**
     * Creates a new Device with given IP and MAC addresses
     *
     * @param address          IP address of device
     * @param ethernetAddress  MAC address of device
     */
    public Device(String name, String communityRead, String communityWrite, boolean useWrite, String address, String ethernetAddress) throws UnknownHostException, SNMPException {
        this(name, communityRead, communityWrite, useWrite);
        
        if ( address != null && ! address.equals("null") ) {
            logger.info("Device address: " + address);
            this.address = InetAddress.getByName(address);
            
        }
        if ( ethernetAddress != null )
            this.ethernetAddress = new EthernetAddress(ethernetAddress);
    }

    public Device(String name,
            String communityRead,
            String communityWrite,
            String address,
            EthernetAddress ethernetAddress,
            String userNameRead,
            USMLevel userLevelRead,
            USMAuthProtocol userAuthProtocolRead,
            String userAuthKeyRead,
            USMPrivProtocol userPrivProtocolRead,
            String userPrivKeyRead,
            String userNameWrite,
            USMLevel userLevelWrite,
            USMAuthProtocol userAuthProtocolWrite,
            String userAuthKeyWrite,
            USMPrivProtocol userPrivProtocolWrite,
            String userPrivKeyWrite
            ) {
        this.name = name;
        this.communityRead = communityRead;
        this.communityWrite = communityWrite;
        //this.address = address;
        this.ethernetAddress = ethernetAddress;
        this.userNameRead = userNameRead;
        this.userLevelRead = userLevelRead;
        this.userAuthProtocolRead = userAuthProtocolRead;
        this.userAuthKeyRead = userAuthKeyRead;
        this.userPrivProtocolRead = userPrivProtocolRead;
        this.userPrivKeyRead = userPrivKeyRead;
        this.userNameWrite = userNameWrite;
        this.userLevelWrite = userLevelWrite;
        this.userAuthProtocolWrite = userAuthProtocolWrite;
        this.userAuthKeyWrite = userAuthKeyWrite;
        this.userPrivProtocolWrite = userPrivProtocolWrite;
        this.userPrivKeyWrite = userPrivKeyWrite;
    }

    /** @return device name */
    public String getName() {
        return name;
    }
    
    /**
     * Attempts to resolve the device name to an IP address using the local
     * name service.
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
    public SNMPAccess createSNMPInterface(String community) throws UnknownHostException, SNMPException {
        snmpCapable = false;
        snmpAccess = new SNMPAccess(getResolvedAddress(), community);
        //this.communityRead = community;
        snmpCapable = true;
        return snmpAccess;
    }
    */

    /**
     * Creates a read-only interface to the SNMP access class.
     *
     * @return read-only SNMP interface
     */
    public SNMPAccess createSNMPReadInterface() throws UnknownHostException, SNMPException {
        return new SNMPAccess(getResolvedAddress(), communityRead);
    }

    /**
     * Creates a read-write interface to the SNMP access class.
     *
     * @return read-write SNMP interface
     */
    public SNMPAccess createSNMPWriteInterface() throws UnknownHostException, SNMPException {
        return new SNMPAccess(getResolvedAddress(), communityWrite);
    }

    /** Prefer expedient SNMP collection with no retries on failure */
    /*
    public void setExpedientCollection() {
        if ( snmpAccess != null)
            snmpAccess.setExpedientCollection();
    }
    */

    /** Prefer reliable SNMP collection with retries on failure */
    /*
    public void setReliableCollection() {
        if ( snmpAccess != null)
            snmpAccess.setReliableCollection();
    }
     */
    
    /** @return the IP address of the device */
    public InetAddress getResolvedAddress() throws UnknownHostException {
        if ( address == null )
            address = resolveAddress();
        
        return address;
    }
    
    public InetAddress getAddress() {
        return address;
    }
    
    /** @return the MAC address of the device */
    public EthernetAddress getEthernetAddress() {
        return ethernetAddress;
    }
    
    /** @return the SNMP read community string for the device */
    public String getCommunityRead() {
        return communityRead;
    }
    
    /** @return the SNMP write community string for the device */
    public String getCommunityWrite() {
        return communityWrite;
    }
    
    public String retrieveReadCommunity() throws DBException {
        DeviceDAO deviceDAO;
        String community;
        
        deviceDAO = DAOFactory.getDeviceDAO();
        community = deviceDAO.retrieveDeviceReadCommunity(name);
        deviceDAO.closeConnection();
        return community;
    }
    
    public String retrieveWriteCommunity() throws DBException {
        DeviceDAO deviceDAO;
        String community;
        
        deviceDAO = DAOFactory.getDeviceDAO();
        community = deviceDAO.retrieveDeviceWriteCommunity(name);
        deviceDAO.closeConnection();
        return community;
    }

    public String toString() {
        return name;
    }
    
}
