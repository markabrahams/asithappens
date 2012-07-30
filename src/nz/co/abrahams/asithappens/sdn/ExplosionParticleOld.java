/*
 * ExplosionParticle.java
 *
 * Created on 2 January 2008, 15:49
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
import nz.co.abrahams.asithappens.cartgraph.TimeSeriesContext;
import org.apache.log4j.Logger;

/**
 *
 * @author mark
 */
public class ExplosionParticleOld implements UpdateableObject {
    
    protected Logger logger;
    
    protected GameContext gameContext;
    
    protected TimeSeriesContext graphContext;
    
    public double x;
    
    public double y;
    
    public double xVelocity;
    
    public double yVelocity;
    
    public ExplosionParticleShape shape;
    
    public Color color;
    
    protected long previousUpdateTime;
    
    protected boolean finished;
    
    /*
    protected boolean twoLines;
    
    protected int line1x1;
    
    protected int line1y1;

    protected int line1x2;
    
    protected int line1y2;

    protected int line2x1;
    
    protected int line2y1;

    protected int line2x2;
    
    protected int line2y2;
    */
    
    /** Creates a new instance of ExplosionParticle */
    public ExplosionParticleOld(GameContext gameContext, TimeSeriesContext graphContext, double x, double y, double xVelocity, double yVelocity, ExplosionParticleShape shape, Color color) {
        logger = Logger.getLogger(this.getClass().getName());
        this.gameContext = gameContext;
        this.graphContext = graphContext;
        this.x = x;
        this.y = y;
        this.xVelocity = xVelocity;
        this.yVelocity = yVelocity;
        this.shape = shape;
        this.color = color;
        previousUpdateTime = System.currentTimeMillis();
        finished = false;
    }
    
    public ExplosionParticleOld(GameContext gameContext, TimeSeriesContext graphContext, double x, double y, double xVelocity, double yVelocity) {
        this(gameContext, graphContext, x, y, xVelocity, yVelocity, ExplosionParticleShape.SHAPES[0][0], ExplosionParticleShape.COLORS[0]);
    }
    
    public void update() {
        long newUpdateTime;
        int elapsedTime;
        
        if ( ! finished ) {
            newUpdateTime = System.currentTimeMillis();
            elapsedTime = (int)(newUpdateTime - previousUpdateTime);
            yVelocity += GameContext.GRAVITY * elapsedTime;
            //previousPosition = position;
            x += xVelocity * elapsedTime;
            y += yVelocity * elapsedTime;
            previousUpdateTime = newUpdateTime;
            if ( y >= graphContext.getPanelHeight() || x < 0 || x >= graphContext.getPanelWidth() ) {
                y = Integer.MAX_VALUE;
                xVelocity = 0;
                yVelocity = 0;
                finished = true;
            }
            //logger.debug(toString());
        }
    }
    
    public double getX() {
        return x;
    }
    
    public double getY() {
        return y;
    }
    
    public ExplosionParticleShape getShape() {
        return shape;
    }
   
    public boolean isFinished() {
        return finished;
    }
    
    public String toString() {
        return "Particle: x=" + x +
                ",y=" + y +
                ",xVelocity=" + xVelocity +
                ",yVelocity=" + yVelocity;
    }
}
