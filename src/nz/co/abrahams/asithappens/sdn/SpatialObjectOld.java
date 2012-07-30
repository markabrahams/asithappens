/*
 * GameObject.java
 *
 * Created on 6 January 2008, 13:36
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
public class SpatialObjectOld implements UpdateableObject {
    
    protected Logger logger;
    
    protected GameContext gameContext;
    
    protected TimeSeriesContext graphContext;
    
    protected boolean xInPixelDomain;
    
    protected boolean yInPixelDomain;
    
    protected double x;
    
    protected double y;
    
    protected double previousX;
    
    protected double previousY;
    
    protected double xVelocity;
    
    protected double yVelocity;
    
    protected double terminalXVelocity;
    
    protected double terminalYVelocity;
    
    protected double xAcceleration;
    
    protected double yAcceleration;
    
    protected double gravityAcceleration;
    
    protected boolean subjectToGravity;
    
    protected double lowerXBound;
    
    protected double upperXBound;
    
    protected double lowerYBound;
    
    protected double upperYBound;
    
    protected ControlPattern pattern;
    
    protected int primitiveIterationsCount;
    
    protected long previousUpdateTime;
    
    protected int primitiveIndex;
    
    protected long timeOffsetCounter;
    
    protected boolean finishedMovementPattern;
    
    protected boolean finished;
    
    /** Creates a new instance of GameObject */
    public SpatialObjectOld(GameContext gameContext, TimeSeriesContext graphContext,
            boolean xInPixelDomain, double initialX,
            boolean yInPixelDomain, double initialY,
            double initialXVelocity, double initialYVelocity, double terminalXVelocity, double terminalYVelocity,
            boolean subjectToGravity, double lowerXBound, double upperXBound,
            double lowerYBound, double upperYBound, ControlPattern pattern) {
        logger = Logger.getLogger(this.getClass().getName());
        this.gameContext = gameContext;
        this.graphContext = graphContext;
        this.xInPixelDomain = xInPixelDomain;
        this.yInPixelDomain = yInPixelDomain;
        x = initialX;
        y = initialY;
        xVelocity = initialXVelocity;
        yVelocity = initialYVelocity;
        this.terminalXVelocity = terminalXVelocity;
        this.terminalYVelocity = terminalYVelocity;
        xAcceleration = 0;
        yAcceleration = 0;
        this.subjectToGravity = subjectToGravity;
        this.lowerXBound = lowerXBound;
        this.upperXBound = upperXBound;
        this.lowerYBound = lowerYBound;
        this.upperYBound = upperYBound;
        this.pattern = pattern;
        primitiveIterationsCount = 0;
        primitiveIndex = 0;
        previousUpdateTime = System.currentTimeMillis();
        finished = false;
        if ( subjectToGravity )
            gravityAcceleration = GameContext.GRAVITY;
        logger.debug("Created new game object: " + toString());
    }
    
    public void update() {
        long newUpdateTime;
        int elapsedTime;
        
        if ( ! finished ) {
            newUpdateTime = System.currentTimeMillis();
            elapsedTime = (int)(newUpdateTime - previousUpdateTime);
            previousUpdateTime = newUpdateTime;
            
            //if ( pattern != null )
            //    adjustForMovementPattern(elapsedTime);
            
            xVelocity += xAcceleration * elapsedTime;
            
            if ( yInPixelDomain )
                yVelocity += ( yAcceleration + gravityAcceleration ) * elapsedTime;
            else
                yVelocity += ( yAcceleration - gravityAcceleration * graphContext.getYValuesPerPixel() ) * elapsedTime;
            
            /*
            if ( ! Double.isNaN(terminalXVelocity) ) {
                // Note: need to code for time & value domains
                if ( xInPixelDomain ) {
                    xVelocity = Math.min(xVelocity, terminalXVelocity);
                } else {
                    xVelocity = Math.min(xVelocity, terminalXVelocity * graphContext.getXTimePerPixel() );
                }
            }

            if ( ! Double.isNaN(terminalYVelocity) ) {
                // Note: need to code for time & value domains
                if ( yInPixelDomain ) {
                    yVelocity = Math.min(yVelocity, terminalYVelocity);
                } else {
                    yVelocity = Math.min(yVelocity, terminalYVelocity * graphContext.getYValuesPerPixel() );
                }
            }
            */
            
            previousX = x;
            previousY = y;
            //previousPosition = position;
            x += xVelocity * elapsedTime;
            y += yVelocity * elapsedTime;
            //previousUpdateTime = newUpdateTime;
            
            // Finish object if outside x bounds
            if ( xInPixelDomain ) {
                if ( x < - graphContext.getPanelWidth() * lowerXBound )
                    finished = true;
                else if ( x > graphContext.getPanelWidth() * ( upperXBound + 1 ) )
                    finished = true;
            } else {
                if ( x < graphContext.getSummaryData().getOriginTime(graphContext.getPanelWidth()) * ( lowerXBound + 1 ) - graphContext.getSummaryData().getLastTime() * lowerXBound )
                    finished = true;
                else if ( x > graphContext.getSummaryData().getLastTime() * ( upperXBound + 1 ) - graphContext.getSummaryData().getOriginTime(graphContext.getPanelWidth()) * upperXBound )
                    finished = true;
            }
            
            // Finish object if outside y bounds
            if ( yInPixelDomain ) {
                if ( y >= graphContext.getPanelHeight() * ( upperYBound + 1 ) )
                    finished = true;
                else if ( y < - graphContext.getPanelHeight() * lowerYBound )
                    finished = true;
            } else {
                if ( y < - graphContext.getGraphTop() * lowerYBound )
                    finished = true;
                else if ( y > graphContext.getGraphTop() * ( upperYBound + 1 ) )
                    finished = true;
            }
            
            if ( finished ) {
                x = - Integer.MAX_VALUE;
                y = - Integer.MAX_VALUE;
                xVelocity = 0;
                yVelocity = 0;
                xAcceleration = 0;
                yAcceleration = 0;
            }
            //logger.debug(toString());
        }
        
    }
    
    /*
    public void adjustForMovementPattern(int elapsedTime) {
        //boolean morePrimitives;
        ControlPrimitive nextPrimitive;
        
        timeOffsetCounter += elapsedTime;
        //morePrimitives = true;
        nextPrimitive = pattern.elementAt(primitiveIndex);
        while ( ! finishedMovementPattern && nextPrimitive.timeOffset < timeOffsetCounter ) {
            if ( nextPrimitive instanceof ControlDisplacement ) {
                if ( xInPixelDomain )
                    x = nextPrimitive.x;
                else
                    x = graphContext.getTimeFromXPixel((int)nextPrimitive.x);
                if ( yInPixelDomain )
                    y = nextPrimitive.y;
                else
                    y = graphContext.getValueFromYPixel((int)nextPrimitive.y);
            } else if ( nextPrimitive instanceof ControlVelocity ) {
                if ( xInPixelDomain )
                    xVelocity = nextPrimitive.x;
                else
                    xVelocity = nextPrimitive.x * graphContext.getXTimePerPixel();
                if ( yInPixelDomain )
                    yVelocity = nextPrimitive.y;
                else
                    yVelocity = nextPrimitive.y * graphContext.getYValuesPerPixel();
            } else if ( nextPrimitive instanceof ControlAcceleration ) {
                if ( xInPixelDomain )
                    xAcceleration = nextPrimitive.x;
                else
                    xAcceleration = nextPrimitive.x * graphContext.getXTimePerPixel();
                if ( yInPixelDomain )
                    yAcceleration = nextPrimitive.y;
                else
                    yAcceleration = nextPrimitive.y * graphContext.getYValuesPerPixel();
            } else if ( nextPrimitive instanceof ControlFiring ) {
                Enemy bullet;
                double bulletXVelocity;
                double bulletYVelocity;
                double scaleFactor;
                double xDistanceDifference;
                double yDistanceDifference;
                
                if ( ((ControlFiring) nextPrimitive).homing ) {
                    xDistanceDifference = gameContext.getSelf().getX() - x;
                    yDistanceDifference = gameContext.getSelf().getY() - y;
                    scaleFactor = Math.sqrt( 1 / ( xDistanceDifference * xDistanceDifference + yDistanceDifference * yDistanceDifference ) );
                    bulletXVelocity = ((ControlFiring) nextPrimitive).magnitude * xDistanceDifference * scaleFactor;
                    bulletYVelocity = ((ControlFiring) nextPrimitive).magnitude * yDistanceDifference * scaleFactor;
                } else {
                    bulletXVelocity = nextPrimitive.x;
                    bulletYVelocity = nextPrimitive.y;
                }
                bullet = new Enemy(gameContext, graphContext, x, y, bulletXVelocity, bulletYVelocity, EnemyType.BULLET, null);
                gameContext.addEnemy(bullet);
            }
            
            primitiveIterationsCount++;
            primitiveIndex++;
            if ( primitiveIndex >= pattern.size() ) {
                primitiveIndex = pattern.getRepeatFrom();
                timeOffsetCounter -= nextPrimitive.timeOffset;
                if ( ! pattern.isRepeating() )
                    finishedMovementPattern = true;
            }
            nextPrimitive = pattern.elementAt(primitiveIndex);
            //if ( ! pattern.isRepeating() && primitiveIterationsCount >= pattern.getIterations() )
            //    finishedMovementPattern = true;
        }
    }
     */
    
    public double getXPixel() {
        if ( xInPixelDomain )
            return x;
        else
            return graphContext.getXPixelFromTime((long)x);
    }
    
    public double getYPixel() {
        if ( yInPixelDomain )
            return y;
        else
            return graphContext.getYPixelFromValue(y);
    }
    
    public double getX() {
        return x;
    }
    
    public void setX(double newX) {
        previousX = x;
        x = newX;
    }
    
    public double getY() {
        return y;
    }
    
    public void setXAcceleration(double acceleration) {
        xAcceleration = acceleration;
    }

    public void setY(double newY) {
        previousY = y;
        y = newY;
    }
    
    public void setMovementPattern(ControlPattern pattern) {
        this.pattern = pattern;
    }
    
    public boolean isFinished() {
        return finished;
    }
    
    public String toString() {
        return "xInPixelDomain=" + xInPixelDomain +
                ",yInPixelDomain=" + yInPixelDomain +
                ",x=" + x + ",y=" + y +
                ",xVelocity=" + xVelocity +
                ",yVelocity=" + yVelocity +
                ",xAcceleration=" + xAcceleration +
                ",yAcceleration=" + yAcceleration +
                ",subjectToGravity=" + subjectToGravity +
                ",lowerXBound=" + lowerXBound +
                ",upperXBound=" + upperXBound +
                ",lowerYBound=" + lowerYBound +
                ",upperYBound=" + upperYBound;
    }
}
