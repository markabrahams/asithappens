/*
 * MemoryCiscoSNMP.java
 *
 * Created on 09 January 2010, 05:24
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
import nz.co.abrahams.asithappens.snmputil.SNMPType;
import nz.co.abrahams.asithappens.snmputil.SNMPInterface;
import nz.co.abrahams.asithappens.snmputil.SNMPException;
import nz.co.abrahams.asithappens.snmputil.SNMPScopeException;
import nz.co.abrahams.asithappens.storage.Device;
import org.apache.log4j.Logger;
import java.util.Vector;
import java.util.LinkedList;
import java.net.UnknownHostException;


/**
 *
 * @author mark
 */
public class MemoryCiscoSNMP extends SNMPInterface {

    /** Logging provider */
    private static Logger logger = Logger.getLogger(MemoryCiscoSNMP.class);

    /** SNMP host resources storage object list */
    protected int[] storageIndex;

    /** SNMP host resources storage object descriptions */
    protected String[] storageDescr;

    public MemoryCiscoSNMP(Device device) throws UnknownHostException, SNMPException {
        super(device);
        snmpAccess = device.createSNMPReadInterface();
    }

    public void enumerateCiscoMemoryPools() throws SNMPException {
        Vector<String> descrVector;
        Vector<Integer> indexVector;
        String storageIndexOID;
        String poolNameOID;
        String storageTypeOID;
        String poolNameValue;
        int poolIndexValue;
        LinkedList getNextPair;

        descrVector = new Vector();
        indexVector = new Vector();
        getNextPair = snmpAccess.getNextMIBValue(SNMPAccess.OID_ciscoMemoryPoolName, SNMPType.OctetString);
        poolNameOID = (String)getNextPair.getFirst();

        try {
            //while ( poolNameOID.matches(SNMPAccess.OID_ciscoMemoryPoolName + ".*") ) {
            while ( true ) {
                poolNameValue = (String)getNextPair.getLast();
                poolIndexValue = Integer.parseInt(poolNameOID.substring(poolNameOID.lastIndexOf('.') + 1));

                descrVector.add(poolNameValue);
                indexVector.add(new Integer(poolIndexValue));
                logger.debug("Adding memory pool object at " + poolIndexValue + ": \"" + poolNameValue + "\" to list");

                getNextPair = snmpAccess.getNextMIBValue(poolNameOID, SNMPType.OctetString, SNMPAccess.OID_ciscoMemoryPoolName);
                poolNameOID = (String)getNextPair.getFirst();
            }
        } catch (SNMPScopeException e) {
        }

        storageIndex = new int[indexVector.size()];
        storageDescr = new String[descrVector.size()];
        for ( int i = 0; i < indexVector.size(); i++ ) {
            storageIndex[i] = indexVector.elementAt(i);
            storageDescr[i] = descrVector.elementAt(i);
        }
    }

    public long getCiscoMemoryUsed(int memoryIndex) throws SNMPException {
        return snmpAccess.getMIBValueGauge32(SNMPAccess.OID_ciscoMemoryPoolUsed + "." + memoryIndex);
    }

    public int[] getStorageIndex() {
        return storageIndex;
    }

    public String[] getStorageDescr() {
        return storageDescr;
    }

}
