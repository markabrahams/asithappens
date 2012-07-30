/*
 * CustomOIDSNMP.java
 *
 * Created on 10 January 2010, 01:13
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

package nz.co.abrahams.asithappens.oid;

import nz.co.abrahams.asithappens.snmputil.SNMPException;
import nz.co.abrahams.asithappens.snmputil.SNMPTypeException;
import nz.co.abrahams.asithappens.snmputil.SNMPInterface;
import nz.co.abrahams.asithappens.storage.Device;
import org.apache.log4j.Logger;
import java.net.UnknownHostException;

/**
 *
 * @author mark
 */
public class CustomOIDSNMP extends SNMPInterface {

    /** Logging provider */
    private static Logger logger = Logger.getLogger(CustomOIDSNMP.class);

    /**
     * Creates the SNMP interface for custom OID collection.
     *
     * @param device
     * @throws UnknownHostException
     * @throws SNMPException
     */
    public CustomOIDSNMP(Device device) throws UnknownHostException, SNMPException {
        super(device);
        snmpAccess = device.createSNMPReadInterface();
    }

    public long getCustomOIDInteger32(CustomOID oid) throws SNMPException, SNMPTypeException {
        return snmpAccess.getMIBValueInteger(oid.oid);
    }

    public long getCustomOIDGauge32(CustomOID oid) throws SNMPException, SNMPTypeException {
        return snmpAccess.getMIBValueGauge32(oid.oid);
    }

    public long getCustomOIDCounter32(CustomOID oid) throws SNMPException, SNMPTypeException {
        return snmpAccess.getMIBValueCounter32(oid.oid);
    }

    public long getCustomOIDCounter64(CustomOID oid) throws SNMPException, SNMPTypeException {
        return snmpAccess.getMIBValueCounter64(oid.oid);
    }


}
