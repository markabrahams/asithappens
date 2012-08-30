/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.co.abrahams.asithappens.accounting;

import java.net.InetAddress;
import nz.co.abrahams.asithappens.storage.Direction;

/**
 *
 * @author mark
 */
public class MACAccountingRecord {
    
    
    public String macAddress;
    
    public Direction direction;
    
    public long bytes;
    
    public MACAccountingRecord(String macAddress, Direction direction, long bytes) {
        this.macAddress = macAddress;
        this.direction = direction;
        this.bytes = bytes;
    }
    
    public String getKey() {
        if (direction != null) {
            return macAddress + "," + direction.toString();
        } else {
            return macAddress;
        }
    }
}
