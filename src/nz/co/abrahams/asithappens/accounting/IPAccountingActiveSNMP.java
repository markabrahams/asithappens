/*
 * IPAccountingActiveSNMP.java
 *
 * Created on 15 August 2012, 22:48
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
package nz.co.abrahams.asithappens.accounting;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import nz.co.abrahams.asithappens.collectors.SNMPTableInterface;
import nz.co.abrahams.asithappens.snmputil.SNMPException;
import nz.co.abrahams.asithappens.snmputil.SNMPTableRow;
import nz.co.abrahams.asithappens.storage.Device;
import org.apache.log4j.Logger;

/**
 *
 * @author mark
 */
public class IPAccountingActiveSNMP extends SNMPTableInterface {

    /** IP Accounting active database table */
    public static final String[] IP_ACCOUNTING_TABLE = {"1.3.6.1.4.1.9.2.4.7.1.4"};
    /** Source address index position */
    public static final int IP_ACCOUNTING_TABLE_INDEX_POSITION_SRC_ADDRESS = 0;
    /** Destination address index position */
    public static final int IP_ACCOUNTING_TABLE_INDEX_POSITION_DST_ADDRESS = 4;
    /** Results column */
    public static final int IP_ACCOUNTING_TABLE_COLUMN_ACTBYTS = 0;
    /** Logging provider */
    public static Logger logger = Logger.getLogger(IPAccountingActiveSNMP.class);

    /**
     * Creates the SNMP interface for IP accounting collection.
     *
     * @param device
     * @throws UnknownHostException
     * @throws SNMPException
     */
    public IPAccountingActiveSNMP(Device device) throws UnknownHostException, SNMPException {
        super(device);
        snmpAccess = device.createSNMPReadInterface();
    }

    /**
     * Retrieves the checkpoint accounting database via SNMP.
     *
     * @return the checkpoint database table
     */
    public IPAccountingRecord[] getSNMPTable() throws SNMPException, UnknownHostException {
        ArrayList<IPAccountingRecord> recordList;
        List<SNMPTableRow> rows;
        Iterator<SNMPTableRow> iterator;
        SNMPTableRow row;
        InetAddress srcAddress;
        InetAddress dstAddress;
        int bytesValue;

        recordList = new ArrayList();
        rows = snmpAccess.getTable(IP_ACCOUNTING_TABLE);
        iterator = rows.iterator();

        while (iterator.hasNext()) {
            row = iterator.next();
            if (!row.isError()) {
                srcAddress = InetAddress.getByName(row.getColumnIndexIpAddress(IP_ACCOUNTING_TABLE_INDEX_POSITION_SRC_ADDRESS));
                dstAddress = InetAddress.getByName(row.getColumnIndexIpAddress(IP_ACCOUNTING_TABLE_INDEX_POSITION_DST_ADDRESS));
                bytesValue = row.getColumnInt(IP_ACCOUNTING_TABLE_COLUMN_ACTBYTS);
                logger.debug("IP accounting record: src=" + srcAddress + ",dst=" + dstAddress + ",bytes=" + bytesValue);
                recordList.add(new IPAccountingRecord(srcAddress, dstAddress, bytesValue));
            } else {
                logger.warn("Table row error: " + row.getErrorMessage());
            }
        }
        return (IPAccountingRecord[]) recordList.toArray(new IPAccountingRecord[recordList.size()]);

    }
}
