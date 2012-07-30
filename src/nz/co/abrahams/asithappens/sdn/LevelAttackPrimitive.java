/*
 * GameAttackPrimitive.java
 *
 * Created on 10 January 2008, 19:09
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
public class LevelAttackPrimitive {
    
    /** Time offset for generation of this attack wave */
    public long timeOffset;
    
    /** Attack wave pattern */
    public AttackWavePattern wavePattern;
    
    /** Flag to halt attack clock while generating this attack wave */
    public boolean generationHaltsClock;
    
    /** Flag to halt attack clock while this attack wave is active */
    public boolean isBoss;
    
    /** x offset applied to the creation of each enemy in fractions of the screen width */
    public double xCreationOffset;
    
    /** y offset applied to the creation of each enemy in fractions of the screen height */
    public double yCreationOffset;
    
    /** Firing control pattern that overrides firing control pattern for indiviudal enemies */
    public ControlPattern firingPattern;
    
    /** Creates a new instance of GameAttackPrimitive */
    public LevelAttackPrimitive(long timeOffset, AttackWavePattern wavePattern, ControlPattern firingPattern,
            double xCreationOffset, double yCreationOffset,
            boolean generationHaltsClock, boolean activeHaltsClock ) {
        this.timeOffset = timeOffset;
        this.wavePattern = wavePattern;
        this.firingPattern = firingPattern;
        this.xCreationOffset = xCreationOffset;
        this.yCreationOffset = yCreationOffset;
        this.generationHaltsClock = generationHaltsClock;
        this.isBoss = activeHaltsClock;
    }
    
    public LevelAttackPrimitive(long timeOffset, AttackWavePattern wavePattern,
            boolean generationHaltsClock, boolean activeHaltsClock ) {
        this(timeOffset, wavePattern, null, 0, 0, generationHaltsClock, activeHaltsClock);
    }
    
    public static LevelAttackPrimitive create(long timeOffset, AttackWavePattern wavePattern) {
        return new LevelAttackPrimitive(timeOffset, wavePattern, null, 0, 0, false, false);
    }
    
    public static LevelAttackPrimitive createWithOffsets(long timeOffset, AttackWavePattern wavePattern,
            double xCreationOffset, double yCreationOffset) {
        return new LevelAttackPrimitive(timeOffset, wavePattern, null, xCreationOffset, yCreationOffset, false, false);
    }
    
    public static LevelAttackPrimitive createWithFiring(long timeOffset, AttackWavePattern wavePattern,
            ControlPattern firingPattern) {
        return new LevelAttackPrimitive(timeOffset, wavePattern, firingPattern, 0, 0, false, false);
    }
    
    public static LevelAttackPrimitive createWithFiringAndOffsets(long timeOffset, AttackWavePattern wavePattern,
            ControlPattern firingPattern, double xCreationOffset, double yCreationOffset) {
        return new LevelAttackPrimitive(timeOffset, wavePattern, firingPattern, xCreationOffset, yCreationOffset, false, false);
    }
    
    public static LevelAttackPrimitive createBoss(long timeOffset, AttackWavePattern wavePattern) {
        return new LevelAttackPrimitive(timeOffset, wavePattern, null, 0, 0, true, true);
    }    
    
}
