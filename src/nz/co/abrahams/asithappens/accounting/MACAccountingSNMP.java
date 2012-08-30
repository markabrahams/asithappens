/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
public class MACAccountingSNMP extends SNMPInterface {
    
    /** cipMacSwitchedBytes column of MAC accounting table */
    public static final String MAC_ACCOUNTING_TABLE = "1.3.6.1.4.1.9.9.84.1.2.1.1.4";
    
    //public static final int MAC_ACCOUNTING_TABLE_INDEX_POSITION_IFINDEX = 0;
    public static final int MAC_ACCOUNTING_TABLE_INDEX_POSITION_DIRECTION = 0;
    public static final int MAC_ACCOUNTING_TABLE_INDEX_POSITION_MAC_ADDRESS = 0;
    /** cipMacSwitchedBytes column position */
    public static final int MAC_ACCOUNTING_TABLE_COLUMN_BYTES = 0;
    
    public static Logger logger = Logger.getLogger(MACAccountingSNMP.class);    
    
    /** ifIndex of interface for MAC accounting collecting */
    protected int ifIndex;
    
    /** ifDescr of interface for MAC accounting collecting */
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
    public MACAccountingSNMP(Device device, int ifIndex, String ifDescr, Direction direction) throws UnknownHostException, SNMPException {
        super(device);
        this.ifIndex = ifIndex;
        this.ifDescr = ifDescr;
        this.direction = direction;
        snmpAccess = device.createSNMPReadInterface();
        
        tableOID = new String[1];
        if ( direction == Direction.Both ) {
            tableOID[0] = MAC_ACCOUNTING_TABLE + "." + ifIndex;
            oidIndexOffset = 1;
        } else {
            tableOID[0] = MAC_ACCOUNTING_TABLE + "." + ifIndex + "." + direction.getOIDIndex();
            oidIndexOffset = 0;
        }
    }
    
    /**
     * Retrieves the checkpoint accounting database via SNMP.
     *
     * @return the checkpoint database table
     */
    public MACAccountingRecord[] getMACAccountingTable() throws SNMPException, UnknownHostException {
        ArrayList<MACAccountingRecord> recordList;
        List<SNMPTableRow> rows;
        Iterator<SNMPTableRow> iterator;
        SNMPTableRow row;
        String macAddress;
        int recordDirection;
        long bytesValue;
        
        rows = snmpAccess.getTable(tableOID);
        recordList = new ArrayList();
        iterator = rows.iterator();

        while (iterator.hasNext()) {
            row = iterator.next();
            if (!row.isError()) {
                //ifIndex = row.getColumnIndexInt(MAC_ACCOUNTING_TABLE_INDEX_POSITION_IFINDEX);
                macAddress = row.getColumnIndexMacAddress(MAC_ACCOUNTING_TABLE_INDEX_POSITION_MAC_ADDRESS + oidIndexOffset);
                bytesValue = row.getColumnLong(MAC_ACCOUNTING_TABLE_COLUMN_BYTES);
                logger.debug("MAC accounting record: mac=" + macAddress + ",bytes=" + bytesValue);
                if ( direction == Direction.Both ) {
                    recordDirection = row.getColumnIndexInt(MAC_ACCOUNTING_TABLE_INDEX_POSITION_DIRECTION);
                    recordList.add(new MACAccountingRecord(macAddress, Direction.getDirectionFromOIDIndex(recordDirection), bytesValue));
                } else {
                    recordList.add(new MACAccountingRecord(macAddress, null, bytesValue));
                }
                    
            } else {
                logger.warn("Table row error: " + row.getErrorMessage());
            }
        }
        return (MACAccountingRecord[]) recordList.toArray(new MACAccountingRecord[recordList.size()]);

    }
    
    public String getIfDescr() {
        return ifDescr;
    }
    
    public Direction getDirection() {
        return direction;
    }
    
}
