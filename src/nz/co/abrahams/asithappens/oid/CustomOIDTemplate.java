/*
 * CustomOIDTemplate.java
 *
 * Created on 29 December 2008, 00:17
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

package nz.co.abrahams.asithappens.oid;

import java.util.Vector;

/**
 *
 * @author mark
 */
public class CustomOIDTemplate {
    
    /** Name of template */
    String templateName;
    
    /** Custom OID list */
    Vector<CustomOID> oids;
    
    public CustomOIDTemplate(String templateName, Vector<CustomOID> oids) {
        this.templateName = templateName;
        this.oids = oids;
    }
    
}
