/*
 * ControlExplosionParticle.java
 *
 * Created on 13 May 2008, 17:48
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
public class ControlExplosionParticle extends ControlPrimitive {
    
    public double xVelocity;
    
    public double yVelocity;
    
    /** Creates a new instance of ControlExplosionParticle */
    public ControlExplosionParticle(int timeOffset, double xVelocity, double yVelocity) {
        super(timeOffset);
        this.xVelocity = xVelocity;
        this.yVelocity = yVelocity;
    }
    
    public static ControlExplosionParticle createWithAngle(int timeOffset, double angle, double magnitude) {
        return new ControlExplosionParticle(timeOffset,
                magnitude * Math.sin(SpatialObject.ORIENTATION_OFFSET + Math.toRadians(angle)),
                magnitude * Math.cos(SpatialObject.ORIENTATION_OFFSET + Math.toRadians(angle)) );
    }
    
}
