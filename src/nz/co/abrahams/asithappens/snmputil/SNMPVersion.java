/*
 * Created on 3 August 2012, 09:00
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
package nz.co.abrahams.asithappens.snmputil;

/**
 *
 * @author mark
 */
public enum SNMPVersion {
    
    v1(1), v3(3);
    
    int index;

    SNMPVersion(int index) {
        this.index = index;
    }

    public static SNMPVersion getSNMPVersion(int desiredIndex) throws SNMPException {
        for ( SNMPVersion version : SNMPVersion.values() ) {
            if ( version.index == desiredIndex ) {
                return version;
            }
        }
        throw new SNMPException("SNMP version " + desiredIndex + " not found");
    }

    public int getIndex() {
        return index;
    }
    
}
