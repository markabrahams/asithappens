/*
 * IPAccountingRecord.java
 *
 * Created on 21 August 2012, 22:48
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
import nz.co.abrahams.asithappens.collectors.SNMPTableRecord;

/**
 *
 * @author mark
 */
public class IPAccountingRecord implements SNMPTableRecord {
    
    
    public InetAddress srcAddress;
    
    public InetAddress dstAddress;
    
    public long bytes;
    
    public IPAccountingRecord(InetAddress srcAddress, InetAddress dstAddress, long bytes) {
        this.srcAddress = srcAddress;
        this.dstAddress = dstAddress;
        this.bytes = bytes;
    }
    
    public String getKey() {
        return srcAddress.getHostAddress() + " -> " + dstAddress.getHostAddress();
    }
    
    public long getValue() {
        return bytes;
    }
}
