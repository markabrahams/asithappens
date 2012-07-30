/*
 * MovementPrimitiveDisplacement.java
 *
 * Created on 13 January 2008, 14:37
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
public class ControlDisplacement extends ControlPrimitive {
    
    public boolean scaled;
    
    public boolean relative;
    
    public double x;
    
    public double y;
    
    public double xRandom;
    
    public double yRandom;
    
    /** Creates a new instance of MovementPrimitiveDisplacement */
    public ControlDisplacement(int timeOffset, boolean scaled, boolean relative, double x, double y, double xRandom, double yRandom) {
        super(timeOffset);
        this.scaled = scaled;
        this.relative = relative;
        this.x = x;
        this.y = y;
        this.xRandom = xRandom;
        this.yRandom = yRandom;
    }
    
    public static ControlDisplacement create(int timeOffset, double x, double y) {
        return new ControlDisplacement(timeOffset, false, false, x, y, 0, 0);
    }
    
    public static ControlDisplacement createScaled(int timeOffset, double x, double y) {
        return new ControlDisplacement(timeOffset, true, false, x, y, 0, 0);
    }
    
    public static ControlDisplacement createRelative(int timeOffset, double x, double y) {
        return new ControlDisplacement(timeOffset, false, true, x, y, 0, 0);
    }
    
    public static ControlDisplacement createRelativeWithRandom(int timeOffset, double x, double y, double xRandom, double yRandom) {
        return new ControlDisplacement(timeOffset, false, true, x, y, xRandom, yRandom);
    }
}
