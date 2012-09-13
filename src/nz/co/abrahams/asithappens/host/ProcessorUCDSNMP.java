/*
 * ProcessorUCDSNMP.java
 *
 * Created on 09 January 2010, 04:17
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

import java.net.UnknownHostException;
import nz.co.abrahams.asithappens.snmputil.SNMPAccess;
import nz.co.abrahams.asithappens.snmputil.SNMPException;
import nz.co.abrahams.asithappens.snmputil.SNMPInterface;
import nz.co.abrahams.asithappens.storage.Device;
import org.apache.log4j.Logger;


/**
 *
 * @author mark
 */
public class ProcessorUCDSNMP extends SNMPInterface {

    /** Logging provider */
    private static Logger logger = Logger.getLogger(ProcessorUCDSNMP.class);

    public ProcessorUCDSNMP(Device device) throws UnknownHostException, SNMPException {
        super(device);
        snmpAccess = device.createSNMPReadInterface();
    }

    public long getSSCPUUser() throws SNMPException {
        return snmpAccess.getMIBValueCounter32(SNMPAccess.OID_ssCpuRawUser + ".0");
    }

    public long getSSCPUNice() throws SNMPException {
        return snmpAccess.getMIBValueCounter32(SNMPAccess.OID_ssCpuRawNice + ".0");
    }

    public long getSSCPUSystem() throws SNMPException {
        return snmpAccess.getMIBValueCounter32(SNMPAccess.OID_ssCpuRawSystem + ".0");
    }

    public long getSSCPUIdle() throws SNMPException {
        return snmpAccess.getMIBValueCounter32(SNMPAccess.OID_ssCpuRawIdle + ".0");
    }

    public long getSSCPUWait() throws SNMPException {
        return snmpAccess.getMIBValueCounter32(SNMPAccess.OID_ssCpuRawWait + ".0");
    }

    public long getSSCPUKernel() throws SNMPException {
        return snmpAccess.getMIBValueCounter32(SNMPAccess.OID_ssCpuRawKernel + ".0");
    }

    public long getSSCPUInterrupt() throws SNMPException {
        return snmpAccess.getMIBValueCounter32(SNMPAccess.OID_ssCpuRawInterrupt + ".0");
    }

}
