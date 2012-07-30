/*
 * ProcessorCiscoSNMP.java
 *
 * Created on 09 January 2010, 04:45
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
public class ProcessorCiscoSNMP extends SNMPInterface {

    /** Logging provider */
    private static Logger logger = Logger.getLogger(ProcessorCiscoSNMP.class);

    public ProcessorCiscoSNMP(Device device) throws UnknownHostException, SNMPException {
        super(device);
        snmpAccess = device.createSNMPReadInterface();
    }

    public int getCiscoProcessorLoad() throws SNMPException {
        return snmpAccess.getMIBValueInteger(SNMPAccess.OID_oldCiscoCPUBusyPer + ".0");
    }

}
