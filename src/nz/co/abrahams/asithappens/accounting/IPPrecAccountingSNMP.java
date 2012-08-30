/*
 * IPPrecAccountingSNMP.java
 *
 * Created on 22 August 2012, 20:31
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

import com.ccg.net.ethernet.EthernetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import nz.co.abrahams.asithappens.snmputil.SNMPAccess;
import nz.co.abrahams.asithappens.snmputil.SNMPException;
import nz.co.abrahams.asithappens.snmputil.SNMPInterface;
import nz.co.abrahams.asithappens.snmputil.SNMPTableRow;
import nz.co.abrahams.asithappens.storage.Device;
import nz.co.abrahams.asithappens.storage.Direction;
import org.apache.log4j.Logger;

/**
 *
 * @author mark
 */
public class IPPrecAccountingSNMP extends SNMPInterface {
    
    // IP Precedence accounting table
    public static final String IPPREC_ACCOUNTING_TABLE = "1.3.6.1.4.1.9.9.84.1.1.1.1.4";
    
    public static final int IPPREC_ACCOUNTING_TABLE_INDEX_POSITION_DIRECTION = 0;
    public static final int IPPREC_ACCOUNTING_TABLE_INDEX_POSITION_IPPREC = 0;
    /** cipMacSwitchedBytes column position */
    public static final int IPPREC_ACCOUNTING_TABLE_COLUMN_BYTES = 0;
    
    public static Logger logger = Logger.getLogger(IPPrecAccountingSNMP.class);    
    
    /** ifIndex of interface for IP precedence accounting collecting */
    protected int ifIndex;
    
    /** ifDescr of interface for IP precedence accounting collecting */
    protected String ifDescr;
    
    /** direction for MAC accounting collection */
    protected Direction direction;
    
    /** OID index offset - set for the "Both" direction */
    protected int oidIndexOffset;
    
    /** OID of table to fetch */
    protected String[] tableOID;
    
    /**
     * Creates the SNMP interface for IP accounting collection.
     *
     * @param device
     * @throws UnknownHostException
     * @throws SNMPException
     */
    public IPPrecAccountingSNMP(Device device, int ifIndex, String ifDescr, Direction direction) throws UnknownHostException, SNMPException {
        super(device);
        this.ifIndex = ifIndex;
        this.ifDescr = ifDescr;
        this.direction = direction;
        snmpAccess = device.createSNMPReadInterface();
        
        tableOID = new String[1];
        if ( direction == Direction.Both ) {
            tableOID[0] = IPPREC_ACCOUNTING_TABLE + "." + ifIndex;
            oidIndexOffset = 1;
        } else {
            tableOID[0] = IPPREC_ACCOUNTING_TABLE + "." + ifIndex + "." + direction.getOIDIndex();
            oidIndexOffset = 0;
        }
    }
    
    /**
     * Retrieves the checkpoint accounting database via SNMP.
     *
     * @return the checkpoint database table
     */
    public IPPrecAccountingRecord[] getIPPrecAccountingTable() throws SNMPException, UnknownHostException {
        ArrayList<IPPrecAccountingRecord> recordList;
        List<SNMPTableRow> rows;
        Iterator<SNMPTableRow> iterator;
        SNMPTableRow row;
        String macAddress;
        int ipPrec;
        int recordDirection;
        long bytesValue;
        
        rows = snmpAccess.getTable(tableOID);
        recordList = new ArrayList();
        iterator = rows.iterator();

        while (iterator.hasNext()) {
            row = iterator.next();
            if (!row.isError()) {
                //ifIndex = row.getColumnIndexInt(MAC_ACCOUNTING_TABLE_INDEX_POSITION_IFINDEX);
                ipPrec = row.getColumnIndexInt(IPPREC_ACCOUNTING_TABLE_INDEX_POSITION_IPPREC + oidIndexOffset);
                bytesValue = row.getColumnLong(IPPREC_ACCOUNTING_TABLE_COLUMN_BYTES);
                logger.debug("IP precedence accounting record: prec=" + ipPrec + ",bytes=" + bytesValue);
                if ( direction == Direction.Both ) {
                    recordDirection = row.getColumnIndexInt(IPPREC_ACCOUNTING_TABLE_INDEX_POSITION_DIRECTION);
                    recordList.add(new IPPrecAccountingRecord(ipPrec, Direction.getDirectionFromOIDIndex(recordDirection), bytesValue));
                } else {
                    recordList.add(new IPPrecAccountingRecord(ipPrec, null, bytesValue));
                }
                    
            } else {
                logger.warn("Table row error: " + row.getErrorMessage());
            }
        }
        return (IPPrecAccountingRecord[]) recordList.toArray(new IPPrecAccountingRecord[recordList.size()]);

    }
    
    public String getIfDescr() {
        return ifDescr;
    }
    
    public Direction getDirection() {
        return direction;
    }
    
}
