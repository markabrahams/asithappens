/*
 * ControlFiring.java
 *
 * Created on 13 January 2008, 14:41
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
public class ControlFiring extends ControlPrimitive {
    
    public enum MovementType {
        XY, ANGLED, HOMING, CONTROLLED
    }
    
    public MovementType movementType;
    
    public ControlPattern controlPattern;
    
    public boolean useDefaultOffsets;
    
    public int xOffset;
    
    public int yOffset;
    
    public double angle;
    
    public double magnitude;
    
    public double angleRandom;
    
    public double magnitudeRandom;
    
    public double xVelocity;
    
    public double yVelocity;
    
    public double xRandom;
    
    public double yRandom;
    
    public int firingType;
    
    
    /** Creates a new instance of ControlFiring */
    public ControlFiring(int timeOffset, int firingType, MovementType movementType,
            boolean useDefaultOffsets, int xOffset, int yOffset,
            double angle, double magnitude, double angleRandom, double magnitudeRandom,
            double xVelocity, double yVelocity, double xRandom, double yRandom, ControlPattern controlPattern) {
        super(timeOffset);
        this.firingType = firingType;
        this.movementType = movementType;
        this.useDefaultOffsets = useDefaultOffsets;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.angle = angle;
        this.magnitude = magnitude;
        this.angleRandom = angleRandom;
        this.magnitudeRandom = magnitudeRandom;
        this.xVelocity = xVelocity;
        this.yVelocity = yVelocity;
        this.xRandom = xRandom;
        this.yRandom = yRandom;
        this.controlPattern = controlPattern;
    }
    
    /*
    public ControlFiring(int timeOffset, boolean homing, boolean useDefaultOffsets, double totalVelocity, double x, double y, double xRandom, double yRandom) {
        this(timeOffset, 0, 0, false, homing, totalVelocity, x, y, xRandom, yRandom);
    }
     */
    
    public static ControlFiring createWithXY(int timeOffset, int type, double xVelocity, double yVelocity) {
        return new ControlFiring(timeOffset, type, MovementType.XY, true, 0, 0,
                0, 0, 0, 0,
                xVelocity, yVelocity, 0, 0, null);
    }
    
    public static ControlFiring createWithXYRandom(int timeOffset, int type, double xVelocity, double yVelocity, double xRandom, double yRandom) {
        return new ControlFiring(timeOffset, type, MovementType.XY, true, 0, 0,
                0, 0, 0, 0,
                xVelocity, yVelocity, xRandom, yRandom, null);
    }
    
    public static ControlFiring createWithXYOffsetsAndRandom(int timeOffset, int type, int xOffset, int yOffset, double xVelocity, double yVelocity, double xRandom, double yRandom) {
        return new ControlFiring(timeOffset, type, MovementType.XY, false, xOffset, yOffset,
                0, 0, 0, 0,
                xVelocity, yVelocity, xRandom, yRandom, null);
    }
    
    public static ControlFiring createWithAngle(int timeOffset, int type, double angle, double magnitude) {
        return new ControlFiring(timeOffset, type, MovementType.XY, true, 0, 0,
                0, 0, 0, 0,
                magnitude * Math.sin(SpatialObject.ORIENTATION_OFFSET + Math.toRadians(angle)), magnitude * Math.cos(SpatialObject.ORIENTATION_OFFSET + Math.toRadians(angle)), 0, 0, null);
    }
    
    public static ControlFiring createWithAngleRandom(int timeOffset, int type, double angle, double magnitude, double angleRandom, double magnitudeRandom) {
        return new ControlFiring(timeOffset, type, MovementType.ANGLED, true, 0, 0,
                angle, magnitude, angleRandom, magnitudeRandom,
                0, 0, 0, 0, null);
    }
    
    public static ControlFiring createWithAngleOffsets(int timeOffset, int type, int xOffset, int yOffset, double angle, double totalVelocity) {
        return new ControlFiring(timeOffset, type, MovementType.XY, false, xOffset, yOffset,
                0, 0, 0, 0,
                totalVelocity * Math.sin(SpatialObject.ORIENTATION_OFFSET + Math.toRadians(angle)), totalVelocity * Math.cos(SpatialObject.ORIENTATION_OFFSET + Math.toRadians(angle)), 0, 0, null);
    }
    
    public static ControlFiring createWithAngleOffsetsAndRandom(int timeOffset, int type, int xOffset, int yOffset, double angle, double magnitude, double angleRandom, double magnitudeRandom) {
        return new ControlFiring(timeOffset, type, MovementType.ANGLED, false, xOffset, yOffset,
                angle, magnitude, angleRandom, magnitudeRandom,
                0, 0, 0, 0, null);
    }
    
    public static ControlFiring createHoming(int timeOffset, int type, double magnitude) {
        return new ControlFiring(timeOffset, type, MovementType.HOMING, true, 0, 0,
                0, magnitude, 0, 0,
                0, 0, 0, 0, null);
    }
    
    public static ControlFiring createHomingPlusAngle(int timeOffset, int type, double angle, double magnitude) {
        return new ControlFiring(timeOffset, type, MovementType.HOMING, true, 0, 0,
                angle, magnitude, 0, 0,
                0, 0, 0, 0, null);
    }
    
    public static ControlFiring createHomingWithRandom(int timeOffset, int type, double magnitude, double angleRandom, double magnitudeRandom) {
        return new ControlFiring(timeOffset, type, MovementType.HOMING, true, 0, 0,
                0, magnitude, angleRandom, magnitudeRandom,
                0, 0, 0, 0, null);
    }
    
    public static ControlFiring createHomingWithOffsetsRandom(int timeOffset, int type, int xOffset, int yOffset, double magnitude, double angleRandom, double magnitudeRandom) {
        return new ControlFiring(timeOffset, type, MovementType.HOMING, false, xOffset, yOffset,
                0, magnitude, angleRandom, magnitudeRandom,
                0, 0, 0, 0, null);
    }
    
    public static ControlFiring createHomingPlusAngleWithRandom(int timeOffset, int type, double angle, double magnitude, double angleRandom, double magnitudeRandom) {
        return new ControlFiring(timeOffset, type, MovementType.HOMING, true, 0, 0,
                angle, magnitude, angleRandom, magnitudeRandom,
                0, 0, 0, 0, null);
    }
    
    public static ControlFiring createHomingPlusAngleWithOffsetsRandom(int timeOffset, int type, int xOffset, int yOffset, double angle, double magnitude, double angleRandom, double magnitudeRandom) {
        return new ControlFiring(timeOffset, type, MovementType.HOMING, false, xOffset, yOffset,
                angle, magnitude, angleRandom, magnitudeRandom,
                0, 0, 0, 0, null);
    }
    
    public static ControlFiring createControlPattern(int timeOffset, int type, ControlPattern pattern) {
        return new ControlFiring(timeOffset, type, MovementType.CONTROLLED, true, 0, 0,
                0, 0, 0, 0,
                0, 0, 0, 0, pattern);
    }
    
    public static ControlFiring createControlPatternWithOffsets(int timeOffset, int type, int xOffset, int yOffset, ControlPattern pattern) {
        return new ControlFiring(timeOffset, type, MovementType.CONTROLLED, false, xOffset, yOffset,
                0, 0, 0, 0,
                0, 0, 0, 0, pattern);
    }
}
