/*
 * NBARSNMP.java
 *
 * Created on 09 January 2010, 13:12
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

package nz.co.abrahams.asithappens.netflow;

import nz.co.abrahams.asithappens.nbar.*;
import nz.co.abrahams.asithappens.core.Configuration;
import nz.co.abrahams.asithappens.snmputil.SNMPAccess;
import nz.co.abrahams.asithappens.snmputil.SNMPException;
import nz.co.abrahams.asithappens.snmputil.SNMPInterface;
import nz.co.abrahams.asithappens.storage.Device;
import nz.co.abrahams.asithappens.storage.DataSets;
import org.apache.log4j.Logger;
import java.net.UnknownHostException;

/**
 *
 * @author mark
 */
public class NetFlowSNMP extends SNMPInterface {

    /** Logging provider */
    private static Logger logger = Logger.getLogger(NetFlowSNMP.class);

    /* SNMP interface */
    //private SNMPAccess snmpAccess;

    /**
     * Creates the SNMP interface for bandwidth collection.
     *
     * @param device
     * @throws UnknownHostException
     * @throws SNMPException
     */
    public NetFlowSNMP(Device device) throws UnknownHostException, SNMPException {
        super(device);
        snmpAccess = device.createSNMPWriteInterface();
    }

    /**
     * @return the Netflow MIB variable denoting the existance of the Top-N table
     */
    public long getNetFlowTopFlowsTopNTable() throws SNMPException {
        return snmpAccess.getMIBValueGauge32(SNMPAccess.OID_cnfTopFlowsTopN + ".0");
    }

    /**
     * @return the NetFlow MIB variable denoting the NetFlow status of an interface
     */
    public int getNetFlowEnable(int ifIndex) throws SNMPException {
        return snmpAccess.getMIBValueInteger(SNMPAccess.OID_cnfCINetflowEnable + "." + ifIndex);
    }

    /**
     * @param ifIndex   the interface for which to set the NetFlow status
     * @param direction the direction to set the NetFlow status
     */
    public void setNetFlowEnable(int ifIndex, int direction) throws SNMPException {
        snmpAccess.setMIBValueInteger(SNMPAccess.OID_cnfCINetflowEnable + "." + ifIndex, direction);
    }

    /**
     * @param tableSize  the size to set the NetFlow Top-N table
     */
    public void setNetFlowTopFlowsTopNTable(int tableSize) throws SNMPException {
        snmpAccess.setMIBValueGauge32(SNMPAccess.OID_cnfTopFlowsTopN + ".0", tableSize);
    }

    /**
     * @param sortBy  the criteria to sort the NetFlow Top-N table by
     */
    public void setNetFlowTopFlowsSortBy(int sortBy) throws SNMPException {
        snmpAccess.setMIBValueInteger(SNMPAccess.OID_cnfTopFlowsSortBy + ".0", sortBy);
    }

    /**
     * @param pollInterval  the timeout interval to set for NetFlow cache entries
     */
    public void setNetFlowTopFlowsCacheTimeout(long pollInterval) throws SNMPException {
        snmpAccess.setMIBValueGauge32(SNMPAccess.OID_cnfTopFlowsCacheTimeout + ".0", pollInterval);
    }

    /**
     * @param criteria  the match criteria to set in the NetFlow MIB
     */
    public void setNetFlowMatchCriteria(NetFlowMatchCriteria criteria) throws SNMPException {
        NetFlowMatchCriteria unset;

        unset = new NetFlowMatchCriteria();

        snmpAccess.setMIBValueInteger(SNMPAccess.OID_cnfTopFlowsMatchSrcAddressType + ".0", criteria.srcAddressType);
        if ( criteria.srcAddressType != unset.srcAddressType ) {
            snmpAccess.setMIBValueHexString(SNMPAccess.OID_cnfTopFlowsMatchSrcAddress + ".0", criteria.getSrcAddressBytes());
            snmpAccess.setMIBValueGauge32(SNMPAccess.OID_cnfTopFlowsMatchSrcAddressMask + ".0", criteria.srcAddressMask);
        }
        snmpAccess.setMIBValueInteger(SNMPAccess.OID_cnfTopFlowsMatchDstAddressType + ".0", criteria.dstAddressType);
        if ( criteria.dstAddressType != unset.dstAddressType ) {
            snmpAccess.setMIBValueHexString(SNMPAccess.OID_cnfTopFlowsMatchDstAddress + ".0", criteria.getDstAddressBytes());
            snmpAccess.setMIBValueGauge32(SNMPAccess.OID_cnfTopFlowsMatchDstAddressMask + ".0", criteria.dstAddressMask);
        }
        snmpAccess.setMIBValueInteger(SNMPAccess.OID_cnfTopFlowsMatchNhAddressType + ".0", criteria.nhAddressType);
        if ( criteria.nhAddressType != unset.nhAddressType ) {
            snmpAccess.setMIBValueHexString(SNMPAccess.OID_cnfTopFlowsMatchNhAddress + ".0", criteria.getNhAddressBytes());
            snmpAccess.setMIBValueGauge32(SNMPAccess.OID_cnfTopFlowsMatchNhAddressMask + ".0", criteria.nhAddressMask);
        }
        snmpAccess.setMIBValueInteger(SNMPAccess.OID_cnfTopFlowsMatchSrcPortLo + ".0", criteria.srcPortLo);
        snmpAccess.setMIBValueInteger(SNMPAccess.OID_cnfTopFlowsMatchSrcPortHi + ".0", criteria.srcPortHi);
        snmpAccess.setMIBValueInteger(SNMPAccess.OID_cnfTopFlowsMatchDstPortLo + ".0", criteria.dstPortLo);
        snmpAccess.setMIBValueInteger(SNMPAccess.OID_cnfTopFlowsMatchDstPortHi + ".0", criteria.dstPortHi);
        snmpAccess.setMIBValueInteger(SNMPAccess.OID_cnfTopFlowsMatchSrcAS + ".0", criteria.srcAS);
        snmpAccess.setMIBValueInteger(SNMPAccess.OID_cnfTopFlowsMatchDstAS + ".0", criteria.dstAS);
        snmpAccess.setMIBValueInteger(SNMPAccess.OID_cnfTopFlowsMatchTOSByte + ".0", criteria.tosByte);
        snmpAccess.setMIBValueInteger(SNMPAccess.OID_cnfTopFlowsMatchProtocol + ".0", criteria.protocol);
        snmpAccess.setMIBValueOctetString(SNMPAccess.OID_cnfTopFlowsMatchSampler + ".0", criteria.sampler);
        snmpAccess.setMIBValueOctetString(SNMPAccess.OID_cnfTopFlowsMatchClass + ".0", criteria.classMap);
        snmpAccess.setMIBValueGauge32(SNMPAccess.OID_cnfTopFlowsMatchMinPackets + ".0", criteria.minPackets);
        snmpAccess.setMIBValueGauge32(SNMPAccess.OID_cnfTopFlowsMatchMaxPackets + ".0", criteria.maxPackets);
        snmpAccess.setMIBValueGauge32(SNMPAccess.OID_cnfTopFlowsMatchMinBytes + ".0", criteria.minBytes);
        snmpAccess.setMIBValueGauge32(SNMPAccess.OID_cnfTopFlowsMatchMaxBytes + ".0", criteria.maxBytes);
    }

    /**
     * @param current  the current match criteria in the NetFlow MIB
     */
    public void restoreDefaultNetFlowMatchCriteria(NetFlowMatchCriteria current) throws SNMPException {
        NetFlowMatchCriteria defaults;

        defaults = new NetFlowMatchCriteria();
        if ( current.srcAddressType != defaults.srcAddressType )
            snmpAccess.setMIBValueInteger(SNMPAccess.OID_cnfTopFlowsMatchSrcAddressType + ".0", defaults.srcAddressType);
        if ( current.srcAddress != defaults.srcAddress )
            snmpAccess.setMIBValueHexString(SNMPAccess.OID_cnfTopFlowsMatchSrcAddress + ".0", new byte[0]);
        if ( current.srcAddressMask != defaults.srcAddressMask )
            snmpAccess.setMIBValueGauge32(SNMPAccess.OID_cnfTopFlowsMatchSrcAddressMask + ".0", defaults.srcAddressMask);
        if ( current.dstAddressType != defaults.dstAddressType )
            snmpAccess.setMIBValueInteger(SNMPAccess.OID_cnfTopFlowsMatchDstAddressType + ".0", defaults.dstAddressType);
        if ( current.dstAddress != defaults.dstAddress )
            snmpAccess.setMIBValueHexString(SNMPAccess.OID_cnfTopFlowsMatchDstAddress + ".0", new byte[0]);
        if ( current.dstAddressMask != defaults.dstAddressMask )
            snmpAccess.setMIBValueGauge32(SNMPAccess.OID_cnfTopFlowsMatchDstAddressMask + ".0", defaults.dstAddressMask);
        if ( current.nhAddressType != defaults.nhAddressType )
            snmpAccess.setMIBValueInteger(SNMPAccess.OID_cnfTopFlowsMatchNhAddressType + ".0", defaults.nhAddressType);
        if ( current.nhAddress != defaults.nhAddress )
            snmpAccess.setMIBValueHexString(SNMPAccess.OID_cnfTopFlowsMatchNhAddress + ".0", new byte[0]);
        if ( current.nhAddressMask != defaults.nhAddressMask )
            snmpAccess.setMIBValueGauge32(SNMPAccess.OID_cnfTopFlowsMatchNhAddressMask + ".0", defaults.nhAddressMask);
        if ( current.srcPortLo != defaults.srcPortLo )
            snmpAccess.setMIBValueInteger(SNMPAccess.OID_cnfTopFlowsMatchSrcPortLo + ".0", defaults.srcPortLo);
        if ( current.srcPortHi != defaults.srcPortHi )
            snmpAccess.setMIBValueInteger(SNMPAccess.OID_cnfTopFlowsMatchSrcPortHi + ".0", defaults.srcPortHi);
        if ( current.dstPortLo != defaults.dstPortLo )
            snmpAccess.setMIBValueInteger(SNMPAccess.OID_cnfTopFlowsMatchDstPortLo + ".0", defaults.dstPortLo);
        if ( current.dstPortHi != defaults.dstPortHi )
            snmpAccess.setMIBValueInteger(SNMPAccess.OID_cnfTopFlowsMatchDstPortHi + ".0", defaults.dstPortHi);
        if ( current.srcAS != defaults.srcAS )
            snmpAccess.setMIBValueInteger(SNMPAccess.OID_cnfTopFlowsMatchSrcAS + ".0", defaults.srcAS);
        if ( current.dstAS != defaults.dstAS )
            snmpAccess.setMIBValueInteger(SNMPAccess.OID_cnfTopFlowsMatchDstAS + ".0", defaults.dstAS);
        if ( current.tosByte != defaults.tosByte )
            snmpAccess.setMIBValueInteger(SNMPAccess.OID_cnfTopFlowsMatchTOSByte + ".0", defaults.tosByte);
        if ( current.protocol != defaults.protocol )
            snmpAccess.setMIBValueInteger(SNMPAccess.OID_cnfTopFlowsMatchProtocol + ".0", defaults.protocol);
        if ( current.sampler != defaults.sampler )
            snmpAccess.setMIBValueOctetString(SNMPAccess.OID_cnfTopFlowsMatchSampler + ".0", defaults.sampler);
        if ( current.classMap != defaults.classMap )
            snmpAccess.setMIBValueOctetString(SNMPAccess.OID_cnfTopFlowsMatchClass + ".0", defaults.classMap);
        if ( current.minPackets != defaults.minPackets )
            snmpAccess.setMIBValueGauge32(SNMPAccess.OID_cnfTopFlowsMatchMinPackets + ".0", defaults.minPackets);
        if ( current.maxPackets != defaults.maxPackets )
            snmpAccess.setMIBValueGauge32(SNMPAccess.OID_cnfTopFlowsMatchMaxPackets + ".0", defaults.maxPackets);
        if ( current.minBytes != defaults.minBytes )
            snmpAccess.setMIBValueGauge32(SNMPAccess.OID_cnfTopFlowsMatchMinBytes + ".0", defaults.minBytes);
        if ( current.maxBytes != defaults.maxBytes )
            snmpAccess.setMIBValueGauge32(SNMPAccess.OID_cnfTopFlowsMatchMaxBytes + ".0", defaults.maxBytes);
    }

    /**
     * Retrieves the NetFlow top flows table from the MIB.
     *
     * @param tableSize  the number of flow entries to retrieve
     * @return           the ordered set of NetFlow records
     */
    public NetFlowRecord[] getNetFlowTopFlowsTable(int tableSize) {
        NetFlowRecord[] records;
        int i;

        records = new NetFlowRecord[tableSize];
        i = 0;
        try {
            setNetFlowTopFlowsTopNTable(tableSize);

            for (i = 0; i < tableSize; i++) {

                records[i] = new NetFlowRecord(
                        snmpAccess.getMIBValueIpAddress(SNMPAccess.OID_cnfTopFlowsSrcAddress + "." + (i + 1)),
                        snmpAccess.getMIBValueIpAddress(SNMPAccess.OID_cnfTopFlowsDstAddress + "." + (i + 1)),
                        (int)snmpAccess.getMIBValueGauge32(SNMPAccess.OID_cnfTopFlowsSrcPort + "." + (i + 1)),
                        (int)snmpAccess.getMIBValueGauge32(SNMPAccess.OID_cnfTopFlowsDstPort + "." + (i + 1)),
                        (int)snmpAccess.getMIBValueGauge32(SNMPAccess.OID_cnfTopFlowsTOS + "." + (i + 1)),
                        (int)snmpAccess.getMIBValueGauge32(SNMPAccess.OID_cnfTopFlowsProtocol + "." + (i + 1)) );
                records[i].bytes = snmpAccess.getMIBValueGauge32(SNMPAccess.OID_cnfTopFlowsBytes + "." + (i + 1));
            }
            return records;
        }
        // SNMPException, UnknownHostException
        catch (Exception e) {
            NetFlowRecord[] partialRecords;

            partialRecords = new NetFlowRecord[i];
            System.arraycopy(records, 0, partialRecords, 0, i);
            return partialRecords;
        }
    }

}
