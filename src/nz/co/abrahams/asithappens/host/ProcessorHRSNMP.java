/*
 * ProcessorHRSNMP.java
 *
 * Created on 08 January 2010, 00:12
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
public class ProcessorHRSNMP extends SNMPInterface {

    /** Logging provider */
    private static Logger logger = Logger.getLogger(ProcessorHRSNMP.class);

    /** SNMP host resources processors list */
    protected int[] processorsIndex;

    /** SNMP host resources processors descriptions */
    protected String[] processorsDescr;

    public ProcessorHRSNMP(Device device) throws UnknownHostException, SNMPException {
        super(device);
        snmpAccess = device.createSNMPReadInterface();
    }

    public void enumerateHostProcessors() throws SNMPException {
        int count;
        Vector<String> descrVector;
        Vector<Integer> indexVector;
        String processorIndexOID;
        String processorDescrOID;
        String processorTypeOID;
        String processorDescrValue;
        int processorIndexValue;
        LinkedList getNextPair;

        descrVector = new Vector();
        indexVector = new Vector();
        getNextPair = snmpAccess.getNextMIBValue(SNMPAccess.OID_hrDeviceIndex, SNMPType.Integer32);
        processorIndexOID = (String)getNextPair.getFirst();

        try {
            //while ( processorIndexOID.matches(SNMPAccess.OID_hrDeviceIndex + ".*") ) {
            while ( true ) {
                processorIndexValue = ((Integer)getNextPair.getLast()).intValue();
                processorDescrOID = SNMPAccess.OID_hrDeviceDescr + "." + String.valueOf(processorIndexValue);
                processorTypeOID = SNMPAccess.OID_hrDeviceType + "." + String.valueOf(processorIndexValue);
                processorDescrValue = snmpAccess.getMIBValueString(processorDescrOID);
                logger.debug("Found device at " + processorIndexOID + ": " + processorDescrValue);

                if ( snmpAccess.getMIBValueOID(processorTypeOID).matches(".*" + SNMPAccess.HR_DEVICETYPE_processor) ) {
                    descrVector.add(processorDescrValue);
                    indexVector.add(new Integer(processorIndexValue));
                    logger.debug("Adding processor \"" + processorDescrValue + "\" to list");
                }

                getNextPair = snmpAccess.getNextMIBValue(processorIndexOID, SNMPType.Integer32, SNMPAccess.OID_hrDeviceIndex);
                processorIndexOID = (String)getNextPair.getFirst();
            }
        } catch (SNMPScopeException e) {
        }

        processorsIndex = new int[indexVector.size()];
        processorsDescr = new String[descrVector.size()];
        for ( int i = 0; i < indexVector.size(); i++ ) {
            processorsIndex[i] = indexVector.elementAt(i);
            processorsDescr[i] = descrVector.elementAt(i);
        }

    }

    public int getProcessorLoad(int processorIndex) throws SNMPException {
        return snmpAccess.getMIBValueInteger(SNMPAccess.OID_hrProcessorLoad + "." + processorIndex);
    }

    public int[] getProcessorsIndex() {
        return processorsIndex;
    }

    public String[] getProcessorsDescr() {
        return processorsDescr;
    }

}
