/*
 * AttackWavePrimitive.java
 *
 * Created on 10 January 2008, 17:51
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
public class AttackWavePrimitive {
    
    public long timeOffset;
    
    public int enemyType;
    
    public double initialX;
    
    public double initialY;
    
    public ControlPattern movementPattern;
    
    public ControlPattern firingPattern;
    
    /** Creates a new instance of AttackWavePrimitive */
    public AttackWavePrimitive(long timeOffset, int enemyType, double initialX, double initialY, ControlPattern movementPattern, ControlPattern firingPattern) {
        this.timeOffset = timeOffset;
        this.enemyType = enemyType;
        this.initialX = initialX;
        this.initialY = initialY;
        this.movementPattern = movementPattern;
        this.firingPattern = firingPattern;
    }
    
    public AttackWavePrimitive(long timeOffset, int enemyType, double initialX, double initialY, ControlPattern movementPattern) {
        this(timeOffset, enemyType, initialX, initialY, movementPattern, null);
    }
    
}
