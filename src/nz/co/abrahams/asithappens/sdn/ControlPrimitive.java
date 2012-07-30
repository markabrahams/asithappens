/*
 * MovementPrimitive.java
 *
 * Created on 9 January 2008, 00:22
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
public class ControlPrimitive {
    
    public static final int DISPLACEMENT = 0;
    
    public static final int VELOCITY = 1;
    
    public static final int ACCELERATION = 2;
    
    public static final int FIRING = 3;
    
    public static final int NULL = 4;
    
    //public int type;
    
    //public double x;
    
    //public double y;
    
    //public double xRandom;
    
    //public double yRandom;
    
    public int timeOffset;
    
    //public GameImage gameImage;
    
    
    /** Creates a new instance of MovementPrimitive */
    public ControlPrimitive(int timeOffset) {
        this.timeOffset = timeOffset;
        //this.type = type;
        /*
        this.x = x;
        this.y = y;
        this.xRandom = xRandom;
        this.yRandom = yRandom;
         */
    }
    
    //public static MovementPrimitive createDisplacement(int timeOffset, int type, double x, double y, double xRandom, double yRandom)
    
    public String toString() {
        return "timeOffset=" + timeOffset;
                /*
                ",type=" + type +
                ",x=" + x +
                ",y=" + y +
                ",xRandom=" + xRandom +
                ",yRandom=" + yRandom;
                 */
    }
}
