/*
 * SNMPTableRecord.java
 *
 * Created on 23 August 2012, 22:13
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

/**
 *
 * @author mark
 */
public interface SNMPTableRecord {
    
    /** Gets unique key for record */
    public String getKey();
    
    /** Gets counter value for record */
    public long getValue();
}
