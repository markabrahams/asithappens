/*
 * Fragment.java
 *
 * Created on 24 December 2007, 16:07
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

import nz.co.abrahams.asithappens.cartgraph.TimeSeriesContext;
import org.apache.log4j.Logger;

/**
 *
 * @author mark
 */
public class Fragment {
    
    // Extreme testing value
    //public static final double GRAVITY = 0.0001;

    // Testing value
    //public static final double GRAVITY = 0.0002;
    
    protected Logger logger;
    
    protected TimeSeriesContext graphContext;
    
    public double position;
    
    public long time;
    
    public double length;
    
    public double previousPosition;
    
    public double velocity;
    
    protected long previousUpdateTime;
    
    protected boolean atBottom;
    
    /** Creates a new instance of Fragment */
    public Fragment(TimeSeriesContext graphContext, long time, double position, double length, double velocity) {
        logger = Logger.getLogger(this.getClass().getName());
        this.graphContext = graphContext;
        this.time = time;
        this.length = length;
        this.position = position;
        previousPosition = position;
        this.velocity = velocity;
        //this.velocity = 0.002 * graphContext.getYValuesPerPixel();
        previousUpdateTime = System.currentTimeMillis();
        if ( position <= 0 )
            atBottom = true;
        else
            atBottom = false;
    }
    
    public void move() {
        long newUpdateTime;
        int elapsedTime;
        
        //if ( ! atBottom ) {
            newUpdateTime = System.currentTimeMillis();
            elapsedTime = (int)(newUpdateTime - previousUpdateTime);
            //velocity -= ( GRAVITY * graphContext.getYValuesPerPixel() ) * elapsedTime;
            velocity -= ( GameContext.GRAVITY * graphContext.getYValuesPerPixel() ) * elapsedTime;
            previousPosition = position;
            position += velocity * elapsedTime;
            previousUpdateTime = newUpdateTime;
            if ( position <= 0 ) {
                position = 0;
                velocity = 0;
                atBottom = true;
            }
            //logger.debug(toString());
        //}
    }
    
    public double getPosition() {
        return position;
    }
    
    public void setPosition(double value) {
        position = value;
    }
    
    public double getLength() {
        return length;
    }
    
    public void setLength(double value) {
        length = value;
    }
    
    public double getVelocity() {
        return velocity;
    }
    
    public void setVelocity(double value) {
        velocity = value;
    }
    
    public String toString() {
        StringBuffer buffer;
        
        buffer = new StringBuffer();
        buffer.append("time=" + time + ",");
        buffer.append("position=" + position + ",");
        buffer.append("length=" + length + ",");
        buffer.append("velocity=" + velocity + ",");
        buffer.append("atBottom=" + atBottom);
        return buffer.toString();
        
    }
}
