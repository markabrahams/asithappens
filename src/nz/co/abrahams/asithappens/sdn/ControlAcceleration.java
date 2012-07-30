/*
 * MovementPrimitiveAcceleration.java
 *
 * Created on 13 January 2008, 14:40
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
public class ControlAcceleration extends ControlPrimitive {
    
    public double x;
    
    public double y;
    
    public double xRandom;
    
    public double yRandom;
    
    public boolean haltUntilTerminal;
    
    public double terminalXVelocity;
    
    public double terminalYVelocity;
    
    /** Creates a new instance of MovementPrimitiveAcceleration */
    public ControlAcceleration(int timeOffset, double x, double y, double xRandom, double yRandom,
            boolean haltUntilTerminal, double terminalXVelocity, double terminalYVelocity) {
        super(timeOffset);
        this.x = x;
        this.y = y;
        this.xRandom = xRandom;
        this.yRandom = yRandom;
        this.haltUntilTerminal = haltUntilTerminal;
        this.terminalXVelocity = terminalXVelocity;
        this.terminalYVelocity = terminalYVelocity;
    }
    
    public static ControlAcceleration create(int timeOffset, double x, double y) {
        return new ControlAcceleration(timeOffset, x, y, 0, 0, false, Double.NaN, Double.NaN);
    }
    
    public static ControlAcceleration createWithRandom(int timeOffset, double x, double y, double xRandom, double yRandom) {
        return new ControlAcceleration(timeOffset, x, y, xRandom, yRandom, false, Double.NaN, Double.NaN);
    }
    
    public static ControlAcceleration createWithTerminalVelocity(int timeOffset, double x, double y,
            double terminalXVelocity, double terminalYVelocity) {
        return new ControlAcceleration(timeOffset, x, y, 0, 0, false, terminalXVelocity, terminalYVelocity);
    }
    
    public static ControlAcceleration createWithTerminalVelocityHalted(int timeOffset, double x, double y,
            double terminalXVelocity, double terminalYVelocity) {
        return new ControlAcceleration(timeOffset, x, y, 0, 0, true, terminalXVelocity, terminalYVelocity);
    }
    
    public static ControlAcceleration createWithRandomAndTerminalVelocity(int timeOffset, double x, double y, double xRandom, double yRandom,
            double terminalXVelocity, double terminalYVelocity) {
        return new ControlAcceleration(timeOffset, x, y, xRandom, yRandom, false, terminalXVelocity, terminalYVelocity);
    }
}
