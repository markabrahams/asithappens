/*
 * ControlFinish.java
 *
 * Created on 25 April 2008, 04:30
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
public class ControlFinish extends ControlPrimitive {
    
    public boolean explosion;
    
    /** Creates a new instance of ControlFinish */
    public ControlFinish(int timeOffset, boolean explosion) {
        super(timeOffset);
        this.explosion = explosion;
    }
    
    public static ControlFinish create(int timeOffset) {
        return new ControlFinish(timeOffset, false);
    }
    
    public static ControlFinish createWithExplosion(int timeOffset) {
        return new ControlFinish(timeOffset, true);
    }
}
