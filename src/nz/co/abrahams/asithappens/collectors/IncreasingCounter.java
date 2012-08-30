/*
 * IncreasingCounter.java
 *
 * Created on 20 August 2012, 21:17
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
package nz.co.abrahams.asithappens.collectors;

import nz.co.abrahams.asithappens.snmputil.SNMPType;

/**
 *
 * @author mark
 */
public class IncreasingCounter {
    
    protected SNMPType type;
    
    protected long counter;
    
    public IncreasingCounter(SNMPType type, long initialValue) {
        this.counter = initialValue;
        this.type = type;
    }
    
    public long newValue(long newValue) {
        long difference;
        
        difference = newValue - counter;
        // handle wrap
        if ( newValue < counter ) {
            difference = difference + (type.maximum - type.minimum);
        }
        counter = newValue;
        return difference;
    }
}
