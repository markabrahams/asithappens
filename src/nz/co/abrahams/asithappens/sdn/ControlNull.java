/*
 * MovementPrimitiveNull.java
 *
 * Created on 4 February 2008, 20:08
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

package nz.co.abrahams.asithappens.sdn;

/**
 *
 * @author mark
 */
public class ControlNull extends ControlPrimitive {
    
    /** Creates a new instance of MovementPrimitiveNull */
    public ControlNull(int timeOffset) {
        super(timeOffset);
    }
    
    public static ControlNull create(int timeOffset) {
        return new ControlNull(timeOffset);
    }
}
