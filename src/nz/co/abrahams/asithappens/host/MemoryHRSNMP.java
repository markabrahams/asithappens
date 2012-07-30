/*
 * MemoryHRSNMP.java
 *
 * Created on 09 January 2010, 05:01
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
import java.net.UnknownHostException;
import java.util.Vector;
import java.util.LinkedList;


/**
 *
 * @author mark
 */
public class MemoryHRSNMP extends SNMPInterface {

    /** Logging provider */
    private static Logger logger = Logger.getLogger(MemoryHRSNMP.class);

    /** SNMP host resources storage object list */
    protected int[] storageIndex;

    /** SNMP host resources storage object descriptions */
    protected String[] storageDescr;

    public MemoryHRSNMP(Device device) throws UnknownHostException, SNMPException {
        super(device);
        snmpAccess = device.createSNMPReadInterface();
    }

    public void enumerateHostStorage() throws SNMPException {
        int count;
        Vector<String> descrVector;
        Vector<Integer> indexVector;
        String storageIndexOID;
        String storageDescrOID;
        String storageTypeOID;
        String storageDescrValue;
        int storageIndexValue;
        LinkedList getNextPair;

        descrVector = new Vector();
        indexVector = new Vector();
        getNextPair = snmpAccess.getNextMIBValue(SNMPAccess.OID_hrStorageDescr, SNMPType.OctetString);
        storageDescrOID = (String)getNextPair.getFirst();

        try {
            //while ( storageDescrOID.matches(SNMPAccess.OID_hrStorageDescr + ".*") ) {
            while ( true ) {
                storageDescrValue = (String)getNextPair.getLast();
                storageIndexValue = Integer.parseInt(storageDescrOID.substring(storageDescrOID.lastIndexOf('.') + 1));

                descrVector.add(storageDescrValue);
                indexVector.add(new Integer(storageIndexValue));
                logger.debug("Adding storage object at " + storageIndexValue + ": \"" + storageDescrValue + "\" to list");

                getNextPair = snmpAccess.getNextMIBValue(storageDescrOID, SNMPType.OctetString, SNMPAccess.OID_hrStorageDescr);
                storageDescrOID = (String)getNextPair.getFirst();
            }
        }
        // Normal exit condition - end of storage table reached
        catch (SNMPScopeException e) {
        }

        storageIndex = new int[indexVector.size()];
        storageDescr = new String[descrVector.size()];
        for ( int i = 0; i < indexVector.size(); i++ ) {
            storageIndex[i] = indexVector.elementAt(i);
            storageDescr[i] = descrVector.elementAt(i);
        }
    }

    public int getStorageAllocationUnits(int storageIndex) throws SNMPException {
        return snmpAccess.getMIBValueInteger(SNMPAccess.OID_hrStorageAllocationUnits + "." + storageIndex);
    }

    public int getStorageUsed(int storageIndex) throws SNMPException {
        return snmpAccess.getMIBValueInteger(SNMPAccess.OID_hrStorageUsed + "." + storageIndex);
    }

    public int[] getStorageIndex() {
        return storageIndex;
    }

    public String[] getStorageDescr() {
        return storageDescr;
    }

}
