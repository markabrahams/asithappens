/*
 * CustomOID.java
 *
 * Created on 6 June 2008, 18:14
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
 *
 */

package nz.co.abrahams.asithappens.oid;

import nz.co.abrahams.asithappens.snmputil.SNMPType;
import nz.co.abrahams.asithappens.core.DBException;

/**
 *
 * @author mark
 */
public class CustomOID {
    
    public static final SNMPType DEFAULT_TYPE = SNMPType.Integer32;
    
    public String label;
    
    public String oid;
    
    public SNMPType type;
    
    public String description;
    
    /** Creates a new instance of CustomOID */
    public CustomOID() {
        type = DEFAULT_TYPE;
    }
    
    public CustomOID(String label, String oid, SNMPType type, String description) {
        this.label = label;
        this.oid = oid;
        this.type = type;
        this.description = description;
    }

}
