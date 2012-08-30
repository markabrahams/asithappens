/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nz.co.abrahams.asithappens.accounting;

import nz.co.abrahams.asithappens.storage.Direction;

/**
 *
 * @author mark
 */
public class IPPrecAccountingRecord {
    
    
    public int ipPrec;
    
    public Direction direction;
    
    public long bytes;
    
    public IPPrecAccountingRecord(int ipPrec, Direction direction, long bytes) {
        this.ipPrec = ipPrec;
        this.direction = direction;
        this.bytes = bytes;
    }
    
    public String getKey() {
        if (direction != null) {
            return Integer.toString(ipPrec) + "," + direction.toString();
        } else {
            return Integer.toString(ipPrec);
        }
    }
}
