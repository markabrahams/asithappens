/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.co.abrahams.asithappens.snmputil;

import com.ccg.net.ethernet.EthernetAddress;
import org.apache.log4j.Logger;
import org.snmp4j.util.TableEvent;

/**
 *
 * @author mark
 */
public class SNMPTableRow {

    /**
     * Logging provider
     */
    private static Logger logger = Logger.getLogger(SNMPTableRow.class);
    /**
     * SNMP4J table event representing a table row
     */
    TableEvent event;

    public SNMPTableRow(TableEvent event) {
        this.event = event;
    }

    public boolean isError() {
        return event.isError();
    }

    public String getErrorMessage() {
        if (event.isError()) {
            return event.getErrorMessage();
        } else {
            return null;
        }
    }

    /**
     *
     * @return the index suffix of the OID
     */
    public String getColumnIndex() {
        return event.getIndex().toString();
    }

    public int getColumnInt(int column) {
        return event.getColumns()[column].getVariable().toInt();
    }

    public long getColumnLong(int column) {
        return event.getColumns()[column].getVariable().toLong();
    }
    
    public int getColumnIndexInt(int indexPosition) {
        String[] nodes;

        nodes = event.getIndex().toString().split("\\.");
        return Integer.valueOf(nodes[indexPosition]);
    }

    public String getColumnIndexString(int indexPosition) {
        String[] nodes;

        nodes = event.getIndex().toString().split("\\.");
        return nodes[indexPosition];
    }
    
    public String getColumnIndexIpAddress(int indexPosition) {
        String[] nodes;

        nodes = event.getIndex().toString().split("\\.");

        return nodes[indexPosition] + "." + nodes[indexPosition + 1] + "."
                + nodes[indexPosition + 2] + "." + nodes[indexPosition + 3];
    }
    
    public String getColumnIndexMacAddress(int indexPosition) {
        String[] nodes;
        byte[] octets;
        EthernetAddress address;
        
        octets = new byte[6];
        nodes = event.getIndex().toString().split("\\.");
        for (int i = 0; i < 6; i++) {
            //octets[i] = Byte.valueOf(nodes[i]);
            octets[i] = (byte)Integer.parseInt(nodes[indexPosition + i]);
        }
        
        address = EthernetAddress.fromBytes(octets);
        return address.toString();
    }
}
