/*
 * MemoryUCDSNMP.java
 *
 * Created on 09 January 2010, 05:43
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

package nz.co.abrahams.asithappens.host;

import nz.co.abrahams.asithappens.snmputil.SNMPAccess;
import nz.co.abrahams.asithappens.snmputil.SNMPInterface;
import nz.co.abrahams.asithappens.snmputil.SNMPException;
import nz.co.abrahams.asithappens.storage.Device;
import org.apache.log4j.Logger;
import java.net.UnknownHostException;


/**
 *
 * @author mark
 */
public class MemoryUCDSNMP extends SNMPInterface {

    /** Logging provider */
    private static Logger logger = Logger.getLogger(MemoryUCDSNMP.class);

    public MemoryUCDSNMP(Device device) throws UnknownHostException, SNMPException {
        super(device);
        snmpAccess = device.createSNMPReadInterface();
    }

    public int getUCDMemTotalReal() throws SNMPException {
        return snmpAccess.getMIBValueInteger(SNMPAccess.OID_memTotalReal + ".0");
    }

    public int getUCDMemAvailReal() throws SNMPException {
        return snmpAccess.getMIBValueInteger(SNMPAccess.OID_memAvailReal + ".0");
    }

    public int getUCDMemTotalSwap() throws SNMPException {
        return snmpAccess.getMIBValueInteger(SNMPAccess.OID_memTotalSwap + ".0");
    }

    public int getUCDMemAvailSwap() throws SNMPException {
        return snmpAccess.getMIBValueInteger(SNMPAccess.OID_memAvailSwap + ".0");
    }

}
