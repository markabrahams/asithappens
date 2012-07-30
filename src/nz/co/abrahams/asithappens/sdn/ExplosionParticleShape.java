/*
 * ExplosionParticlePattern.java
 *
 * Created on 23 January 2008, 22:42
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

import java.awt.Color;

/**
 *
 * @author mark
 */
public class ExplosionParticleShape {
    
    public static final ExplosionParticleShape[][] SHAPES = {
        // One-point pattern
        {
            new ExplosionParticleShape(false, 0, 0, 0, 0, 0, 0, 0, 0)
        } ,
        // Two-point patterns
        {
            new ExplosionParticleShape(false, 0, 0, 0, 1, 0, 0, 0, 0),
                    new ExplosionParticleShape(false, 0, 0, 0, 1, 0, 0, 0, 0)
        } ,
        // Three-point patterns
        {
            new ExplosionParticleShape(false, -1, 0, 1, 0, 0, 0, 0, 0),
                    new ExplosionParticleShape(false, 0, -1, 0, 1, 0, 0, 0, 0),
                    new ExplosionParticleShape(false, -1, -1, 1, 1, 0, 0, 0, 0),
                    new ExplosionParticleShape(false, 1, -1, -1, 1, 0, 0, 0, 0),
                    new ExplosionParticleShape(true, 0, 0, 0, 1, 0, 1, 0, 1),
                    new ExplosionParticleShape(true, 0, 0, 0, 1, 1, 1, 1, 1),
                    new ExplosionParticleShape(true, 0, 0, 0, 0, 0, 1, 1, 1),
                    new ExplosionParticleShape(true, 1, 0, 1, 0, 0, 1, 1, 1),
        } ,
        // Four-point patterns
        {
            new ExplosionParticleShape(false, -1, 0, 2, 0, 0, 0, 0, 0),
                    new ExplosionParticleShape(false, 0, -1, 0, 2, 0, 0, 0, 0),
                    new ExplosionParticleShape(false, -1, -1, 2, 2, 0, 0, 0, 0),
                    new ExplosionParticleShape(false, 1, -1, -2, 2, 0, 0, 0, 0),
                    new ExplosionParticleShape(true, 0, 0, 1, 0, 0, 1, 1, 1),
                    new ExplosionParticleShape(true, -1, 0, 1, 0, 0, 1, 0, 1),
                    new ExplosionParticleShape(true, -1, 0, 1, 0, 0, -1, 0, -1),
                    new ExplosionParticleShape(true, 0, -1, 0, 1, 1, 0, 1, 0),
                    new ExplosionParticleShape(true, 0, -1, 0, 1, -1, 0, -1, 0)
        } ,
        // Five-point patterns
        {
            new ExplosionParticleShape(true, -1, 0, 2, 0, 0, 1, 0, 1),
                    new ExplosionParticleShape(true, -1, 0, 2, 0, 0, -1, 0, -1),
                    new ExplosionParticleShape(true, -1, 0, 2, 0, 1, 1, 1, 1),
                    new ExplosionParticleShape(true, -1, 0, 2, 0, 1, -1, 1, -1),
                    new ExplosionParticleShape(true, -1, 0, 1, 0, 0, -1, 0, 1),
                    new ExplosionParticleShape(true, -1, -1, 1, 1, 1, -1, -1, 1)
        } ,
        // Six-point patterns
        {
            new ExplosionParticleShape(true, -1, 0, 2, 0, 0, -1, 0, 1),
                    new ExplosionParticleShape(true, -1, 0, 2, 0, 1, -1, 1, -1),
                    new ExplosionParticleShape(true, 0, -1, 0, 2, -1, 1, 1, 1),
                    new ExplosionParticleShape(true, 0, -1, 0, 2, -1, 0, 1, 0),
                    new ExplosionParticleShape(true, -1, 0, 1, 0, 0, 1, 2, 1),
                    new ExplosionParticleShape(true, 0, 0, 2, 0, -1, 1, 1, 1),
                    new ExplosionParticleShape(true, 0, -1, 0, 1, 1, 0, 1, 2)
        } ,
        // Seven-point patterns
        {
            new ExplosionParticleShape(true, -1, 0, 1, 0, -1, 1, 2, 1),
                    new ExplosionParticleShape(true, -1, 0, 2, 0, 1, -1, 1, -1),
                    new ExplosionParticleShape(true, 0, -1, 0, 2, 1, -1, 1, 1),
                    new ExplosionParticleShape(true, 0, -1, 0, 2, 1, -1, 1, 1)
        } ,
        // Eight-point patterns
        {
            new ExplosionParticleShape(true, -2, 0, 2, 0, 0, 1, 4, 1),
                    new ExplosionParticleShape(true, 0, 0, 4, 0, -2, 1, 2, 1),
                    new ExplosionParticleShape(true, -2, 0, 2, 0, -1, 1, 3, 1),
                    new ExplosionParticleShape(true, -1, 0, 3, 0, -2, 1, 2, 1),
                    new ExplosionParticleShape(true, 0, -2, 0, 2, 1, 0, 1, 4),
                    new ExplosionParticleShape(true, 0, 0, 0, 4, 1, -2, 1, 2),
                    new ExplosionParticleShape(true, 0, -2, 0, 2, 1, -1, 1, 3),
                    new ExplosionParticleShape(true, 0, -1, 0, 3, 1, -2, 1, 2)
        } ,
        // Nine-point patterns
        {
            new ExplosionParticleShape(true, -2, 0, 2, 0, 0, 1, 5, 1),
                    new ExplosionParticleShape(true, 0, 0, 5, 0, -2, 1, 2, 1),
                    new ExplosionParticleShape(true, -2, 0, 2, 0, -1, 1, 4, 1),
                    new ExplosionParticleShape(true, -1, 0, 4, 0, -2, 1, 2, 1),
                    new ExplosionParticleShape(true, 0, -2, 0, 2, 1, 0, 1, 5),
                    new ExplosionParticleShape(true, 0, 0, 0, 5, 1, -2, 1, 2),
                    new ExplosionParticleShape(true, 0, -2, 0, 2, 1, -1, 1, 4),
                    new ExplosionParticleShape(true, 0, -1, 0, 4, 1, -2, 1, 2)
        }
    };
    
    public static final Color[] COLORS = {
        new Color(0, 0, 0),
        new Color(20, 20, 20),
        new Color(40, 40, 40),
        new Color(60, 60, 60),
        new Color(80, 80, 80),
        new Color(100, 100, 100),
        new Color(120, 120, 120),
        new Color(140, 140, 140)
    };
    
    public static final ExplosionParticleShape FIREWORKS_PLUS = new ExplosionParticleShape(true, -2, 0, 2, 0, 0, -2, 0, 2);
    
    public static final Color[] FIREWORKS_COLORS = {
        Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.BLUE, Color.PINK, Color.MAGENTA
    };
    
    
    public boolean twoLines;
    
    public int line1x1;
    
    public int line1y1;
    
    public int line1x2;
    
    public int line1y2;
    
    public int line2x1;
    
    public int line2y1;
    
    public int line2x2;
    
    public int line2y2;
    
    
    /** Creates a new instance of ExplosionParticlePattern */
    public ExplosionParticleShape(boolean twoLines, int line1x1, int line1y1, int line1x2, int line1y2, int line2x1, int line2y1, int line2x2, int line2y2) {
        this.twoLines = twoLines;
        this.line1x1 = line1x1;
        this.line1y1 = line1y1;
        this.line1x2 = line1x2;
        this.line1y2 = line1y2;
        this.line2x1 = line2x1;
        this.line2y1 = line2y1;
        this.line2x2 = line2x2;
        this.line2y2 = line2y2;
    }
    
}
