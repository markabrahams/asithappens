/*
 * MovementPrimitiveVelocity.java
 *
 * Created on 13 January 2008, 14:39
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
public class ControlVelocity extends ControlPrimitive {
    
    public double x;
    
    public double y;
    
    public double xRandom;
    
    public double yRandom;
    
    //public boolean withTerminalDistance;
    
    public boolean haltUntilTerminal;
    
    public double terminalXDistance;
    
    public double terminalYDistance;
    
    /** Creates a new instance of MovementPrimitiveVelocity */
    public ControlVelocity(int timeOffset, double x, double y, double xRandom, double yRandom,
            boolean haltUntilTerminal, double xTerminalDistance, double yTerminalDistance) {
        super(timeOffset);
        this.x = x;
        this.y = y;
        this.xRandom = xRandom;
        this.yRandom = yRandom;
        this.haltUntilTerminal = haltUntilTerminal;
        this.terminalXDistance = xTerminalDistance;
        this.terminalYDistance = yTerminalDistance;
    }
    
    public static ControlVelocity create(int timeOffset, double x, double y) {
        return new ControlVelocity(timeOffset, x, y, 0, 0, false, Double.NaN, Double.NaN);
    }
    
    public static ControlVelocity createWithRandom(int timeOffset, double x, double y, double xRandom, double yRandom) {
        return new ControlVelocity(timeOffset, x, y, xRandom, yRandom, false, Double.NaN, Double.NaN);
    }
    
    public static ControlVelocity createWithTerminal(int timeOffset, double x, double y, double terminalXDistance, double terminalYDistance) {
        return new ControlVelocity(timeOffset, x, y, 0, 0, false, terminalXDistance, terminalYDistance);
    }
    
    public static ControlVelocity createWithTerminalHalted(int timeOffset, double x, double y, double terminalXDistance, double terminalYDistance) {
        return new ControlVelocity(timeOffset, x, y, 0, 0, true, terminalXDistance, terminalYDistance);
    }
    
}
