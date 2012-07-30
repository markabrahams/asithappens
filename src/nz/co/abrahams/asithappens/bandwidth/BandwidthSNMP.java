/*
 * BandwidthSNMP.java
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

package nz.co.abrahams.asithappens.bandwidth;

import nz.co.abrahams.asithappens.snmputil.SNMPAccess;
import nz.co.abrahams.asithappens.snmputil.SNMPException;
import nz.co.abrahams.asithappens.snmputil.SNMPInterface;
import nz.co.abrahams.asithappens.storage.Device;
import java.net.UnknownHostException;

/**
 *
 * @author mark
 */
public class BandwidthSNMP extends SNMPInterface {

    /* SNMP interface */
    //private SNMPAccess snmpAccess;

    /**
     * Creates the SNMP interface for bandwidth collection.
     *
     * @param device
     * @throws UnknownHostException
     * @throws SNMPException
     */
    public BandwidthSNMP(Device device) throws UnknownHostException, SNMPException {
        super(device);
        snmpAccess = device.createSNMPReadInterface();
    }

    /**
     * Retrieves the ifInOctets counter via SNMP for the given interface.
     *
     * @param port  the ifIndex of the desired interface
     * @return      the ifInOctets counter value
     */
    public long getIfInOctets(int ifIndex) throws SNMPException {
        return snmpAccess.getMIBValueCounter32(SNMPAccess.OID_IFINOCTETS + "." + Integer.toString(ifIndex));
    }

    /**
     * Retrieves the ifOutOctets counter via SNMP for the given interface.
     *
     * @param port  the ifIndex of the desired interface
     * @return      the ifOutOctets counter value
     */
    public long getIfOutOctets(int ifIndex) throws SNMPException {
        return snmpAccess.getMIBValueCounter32(SNMPAccess.OID_IFOUTOCTETS + "." + Integer.toString(ifIndex));
    }

    /**
     * Retrieves the ifHCInOctets counter via SNMP for the given interface.
     *
     * @param port  the ifIndex of the desired interface
     * @return      the ifHCInOctets counter value
     */
    public long getIfHCInOctets(int ifIndex) throws SNMPException {
        return snmpAccess.getMIBValueCounter64(SNMPAccess.OID_IFHCINOCTETS + "." + Integer.toString(ifIndex));
    }

    /**
     * Retrieves the ifHCOutOctets counter via SNMP for the given interface.
     *
     * @param port  the ifIndex of the desired interface
     * @return      the ifHCOutOctets counter value
     */
    public long getIfHCOutOctets(int ifIndex) throws SNMPException {
        return snmpAccess.getMIBValueCounter64(SNMPAccess.OID_IFHCOUTOCTETS + "." + Integer.toString(ifIndex));
    }

}
