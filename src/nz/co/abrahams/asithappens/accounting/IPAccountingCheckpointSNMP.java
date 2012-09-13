/*
 * IPAccountingCheckpointSNMP.java
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
import nz.co.abrahams.asithappens.snmputil.SNMPException;
import nz.co.abrahams.asithappens.snmputil.SNMPInterface;
import nz.co.abrahams.asithappens.snmputil.SNMPTableRow;
import nz.co.abrahams.asithappens.storage.Device;
import org.apache.log4j.Logger;

/**
 *
 * @author mark
 */
public class IPAccountingCheckpointSNMP extends SNMPInterface {

    /** Accounting table columns to retrieve - OID ckactByts */
    public static final String[] ACCOUNTING_TABLE = {"1.3.6.1.4.1.9.2.4.9.1.4"};
    public static final int ACCOUNTING_TABLE_INDEX_POSITION_SRC_ADDRESS = 0;
    public static final int ACCOUNTING_TABLE_INDEX_POSITION_DST_ADDRESS = 4;
    public static final int ACCOUNTING_TABLE_COLUMN_CKACTBYTS = 0;
    public static final String OID_actCheckPoint = "1.3.6.1.4.1.9.2.4.11.0";
    public static Logger logger = Logger.getLogger(IPAccountingCheckpointSNMP.class);

    /**
     * Creates the SNMP interface for IP accounting collection.
     *
     * @param device
     * @throws UnknownHostException
     * @throws SNMPException
     */
    public IPAccountingCheckpointSNMP(Device device) throws UnknownHostException, SNMPException {
        super(device);
        snmpAccess = device.createSNMPWriteInterface();
    }

    public void copyCurrentToCheckpoint() throws SNMPException {
        int checkPoint;

        checkPoint = snmpAccess.getMIBValueInteger(OID_actCheckPoint);
        snmpAccess.setMIBValueInteger(OID_actCheckPoint, checkPoint);
    }

    /**
     * Retrieves the checkpoint accounting database via SNMP.
     *
     * @return the checkpoint database table
     */
    public IPAccountingRecord[] getCkactByts() throws SNMPException, UnknownHostException {
        ArrayList<IPAccountingRecord> recordList;
        List<SNMPTableRow> rows;
        Iterator<SNMPTableRow> iterator;
        SNMPTableRow row;
        InetAddress srcAddress;
        InetAddress dstAddress;
        int bytesValue;

        recordList = new ArrayList();
        rows = snmpAccess.getTable(ACCOUNTING_TABLE);
        iterator = rows.iterator();

        while (iterator.hasNext()) {
            row = iterator.next();
            if (!row.isError()) {
                srcAddress = InetAddress.getByName(row.getColumnIndexIpAddress(ACCOUNTING_TABLE_INDEX_POSITION_SRC_ADDRESS));
                dstAddress = InetAddress.getByName(row.getColumnIndexIpAddress(ACCOUNTING_TABLE_INDEX_POSITION_DST_ADDRESS));
                bytesValue = row.getColumnInt(ACCOUNTING_TABLE_COLUMN_CKACTBYTS);
                logger.debug("IP accounting record: src=" + srcAddress + ",dst=" + dstAddress + ",bytes=" + bytesValue);
                recordList.add(new IPAccountingRecord(srcAddress, dstAddress, bytesValue));
            } else {
                logger.warn("Table row error: " + row.getErrorMessage());
            }
        }
        return (IPAccountingRecord[]) recordList.toArray(new IPAccountingRecord[recordList.size()]);

    }
}
