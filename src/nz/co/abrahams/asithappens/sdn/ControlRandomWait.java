/*
 * ControlRandomWait.java
 *
 * Created on 27 March 2008, 22:47
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
public class ControlRandomWait extends ControlPrimitive {

    public int randomWait;
    
    /** Creates a new instance of ControlRandomWait */
    public ControlRandomWait(int timeOffset, int randomWait) {
        super(timeOffset);
        this.randomWait = randomWait;
    }
    
    public static ControlRandomWait create(int timeOffset, int randomWait) {
        return new ControlRandomWait(timeOffset, randomWait);
    }
    
}
