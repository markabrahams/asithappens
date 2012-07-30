/*
 * ControlContainment.java
 *
 * Created on 8 April 2008, 22:08
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
public class ControlContainment extends ControlPrimitive {
    
    public double containmentLowerX;
    
    public double containmentLowerY;
    
    public double containmentUpperX;
    
    public double containmentUpperY;
    
    /** Creates a new instance of ControlContainment */
    public ControlContainment(int timeOffset, double containmentLowerX, double containmentLowerY, double containmentUpperX, double containmentUpperY) {
        super(timeOffset);
        this.containmentLowerX = containmentLowerX;
        this.containmentLowerY = containmentLowerY;
        this.containmentUpperX = containmentUpperX;
        this.containmentUpperY = containmentUpperY;
    }
    
    public static ControlContainment create(int timeOffset, double containmentLowerX, double containmentLowerY, double containmentUpperX, double containmentUpperY) {
        return new ControlContainment(timeOffset, containmentLowerX, containmentLowerY, containmentUpperX, containmentUpperY);
    }
}
