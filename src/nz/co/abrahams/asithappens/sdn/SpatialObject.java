/*
 * SpatialObject.java
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
import java.awt.Image;

/**
 *
 * @author mark
 */
public class SpatialObject implements UpdateableObject {
    
    public final static double ORIENTATION_OFFSET = Math.PI / 2;
    
    protected Logger logger;
    
    protected GameContext gameContext;
    
    protected TimeSeriesContext graphContext;
    
    protected GameImage gameImage;
    
    //protected boolean xInPixelDomain;
    
    //protected boolean yInPixelDomain;
    
    protected double x;
    
    protected double y;
    
    protected double previousX;
    
    protected double previousY;
    
    protected double terminalXDistance;
    
    protected double terminalYDistance;
    
    protected double xVelocity;
    
    protected double yVelocity;
    
    protected double previousXVelocity;
    
    protected double previousYVelocity;
    
    protected double terminalXVelocity;
    
    protected double terminalYVelocity;
    
    protected double xAcceleration;
    
    protected double yAcceleration;
    
    protected boolean subjectToGravity;
    
    protected double gravityAcceleration;
    
    protected double lowerXBound;
    
    protected double upperXBound;
    
    protected double lowerYBound;
    
    protected double upperYBound;
    
    protected boolean containment;
    
    protected double containmentLowerX;
    
    protected double containmentLowerY;
    
    protected double containmentUpperX;
    
    protected double containmentUpperY;
    
    protected ControlPattern pattern;
    
    protected int primitiveIterationsCount;
    
    protected long previousUpdateTime;
    
    protected int primitiveIndex;
    
    protected long timeOffsetCounter;
    
    protected boolean haltedControlPattern;
    
    protected boolean finishedControlPattern;
    
    protected boolean finished;
    
    /** Creates a new instance of SpatialObject */
    public SpatialObject(GameContext gameContext, TimeSeriesContext graphContext, GameImage gameImage,
            double initialX, double initialY,
            double initialXVelocity, double initialYVelocity, double terminalXVelocity, double terminalYVelocity,
            boolean subjectToGravity, double lowerXBound, double upperXBound,
            double lowerYBound, double upperYBound, ControlPattern pattern) {
        logger = Logger.getLogger(this.getClass().getName());
        this.gameContext = gameContext;
        this.graphContext = graphContext;
        this.gameImage = gameImage;
        //this.xInPixelDomain = xInPixelDomain;
        //this.yInPixelDomain = yInPixelDomain;
        x = initialX;
        y = initialY;
        previousX = initialX;
        previousY = initialY;
        terminalXDistance = Double.NaN;
        terminalYDistance = Double.NaN;
        xVelocity = initialXVelocity;
        yVelocity = initialYVelocity;
        previousXVelocity = initialXVelocity;
        previousYVelocity = initialYVelocity;
        this.terminalXVelocity = terminalXVelocity;
        this.terminalYVelocity = terminalYVelocity;
        xAcceleration = 0;
        yAcceleration = 0;
        this.subjectToGravity = subjectToGravity;
        this.lowerXBound = lowerXBound;
        this.upperXBound = upperXBound;
        this.lowerYBound = lowerYBound;
        this.upperYBound = upperYBound;
        containment = false;
        this.pattern = pattern;
        primitiveIterationsCount = 0;
        primitiveIndex = 0;
        previousUpdateTime = System.currentTimeMillis();
        haltedControlPattern = false;
        finishedControlPattern = false;
        finished = false;
        if ( subjectToGravity )
            gravityAcceleration = GameContext.GRAVITY;
        else
            gravityAcceleration = 0;
        //logger.debug("Created new game object: " + toString());
    }
    
    public void update() {
        long newUpdateTime;
        int elapsedTime;
        
        if ( ! finished ) {
            newUpdateTime = System.currentTimeMillis();
            elapsedTime = (int)(newUpdateTime - previousUpdateTime);
            previousUpdateTime = newUpdateTime;
            
            if ( pattern != null && ! haltedControlPattern )
                adjustForControlPattern(elapsedTime);
            
            previousXVelocity = xVelocity;
            previousYVelocity = yVelocity;
            
            xVelocity += xAcceleration * elapsedTime;
            yVelocity += ( yAcceleration + gravityAcceleration ) * elapsedTime;
            
            // Check for terminal velocity
            if ( ! Double.isNaN(terminalXVelocity) ) {
                if ( previousXVelocity <= terminalXVelocity) {
                    if ( xVelocity > terminalXVelocity ) {
                        xVelocity = terminalXVelocity;
                        xAcceleration = 0;
                        terminalXVelocity = Double.NaN;
                        if ( Double.isNaN(terminalYVelocity) )
                            haltedControlPattern = false;
                        //logger.debug("Reached terminal x velocity");
                    }
                } else {
                    if ( xVelocity <= terminalXVelocity ) {
                        xVelocity = terminalXVelocity;
                        xAcceleration = 0;
                        terminalXVelocity = Double.NaN;
                        if ( Double.isNaN(terminalYVelocity) )
                            haltedControlPattern = false;
                        //logger.debug("Reached terminal x velocity");
                    }
                }
            }
            
            if ( ! Double.isNaN(terminalYVelocity) ) {
                if ( previousYVelocity <= terminalYVelocity) {
                    if ( yVelocity > terminalYVelocity ) {
                        yVelocity = terminalYVelocity;
                        yAcceleration = 0;
                        terminalYVelocity = Double.NaN;
                        if ( Double.isNaN(terminalXVelocity) )
                            haltedControlPattern = false;
                        //logger.debug("Reached terminal y velocity");
                    }
                } else {
                    if ( yVelocity <= terminalYVelocity ) {
                        yVelocity = terminalYVelocity;
                        yAcceleration = 0;
                        terminalYVelocity = Double.NaN;
                        if ( Double.isNaN(terminalXVelocity) )
                            haltedControlPattern = false;
                        //logger.debug("Reached terminal y velocity");
                    }
                }
            }
            
            // Adjust displacement
            previousX = x;
            previousY = y;
            x += xVelocity * elapsedTime;
            y += yVelocity * elapsedTime;
            
            
            // Check for terminal displacement
            if ( ! Double.isNaN(terminalXDistance) ) {
                if ( previousX <= terminalXDistance * graphContext.getPanelWidth() ) {
                    if ( x >= terminalXDistance * graphContext.getPanelWidth() ) {
                        x = terminalXDistance * graphContext.getPanelWidth();
                        xVelocity = 0;
                        xAcceleration = 0;
                        terminalXDistance = Double.NaN;
                        if ( Double.isNaN(terminalYDistance) )
                            haltedControlPattern = false;
                        //logger.debug("Reached terminal x distance: " + terminalXDistance);
                    }
                } else {
                    if ( x <= terminalXDistance * graphContext.getPanelWidth() ) {
                        x = terminalXDistance * graphContext.getPanelWidth();
                        xVelocity = 0;
                        xAcceleration = 0;
                        terminalXDistance = Double.NaN;
                        if ( Double.isNaN(terminalYDistance) )
                            haltedControlPattern = false;
                        //logger.debug("Reached terminal x distance: " + terminalXDistance);
                    }
                }
            }
            
            if ( ! Double.isNaN(terminalYDistance) ) {
                if ( previousY <= terminalYDistance * graphContext.getPanelHeight() ) {
                    if ( y >= terminalYDistance * graphContext.getPanelHeight() ) {
                        y = terminalYDistance * graphContext.getPanelHeight();
                        yVelocity = 0;
                        yAcceleration = 0;
                        terminalYDistance = Double.NaN;
                        if ( Double.isNaN(terminalXDistance) )
                            haltedControlPattern = false;
                        //logger.debug("Reached terminal y distance: " + terminalYDistance);
                    }
                } else {
                    if ( y <= terminalYDistance * graphContext.getPanelHeight() ) {
                        y = terminalYDistance * graphContext.getPanelHeight();
                        yVelocity = 0;
                        yAcceleration = 0;
                        terminalYDistance = Double.NaN;
                        if ( Double.isNaN(terminalXDistance) )
                            haltedControlPattern = false;
                        //logger.debug("Reached terminal y distance: " + terminalYDistance);
                    }
                }
            }
            
            // Containment within bounds
            if ( containment ) {
                
                if ( x < containmentLowerX * gameContext.getPanelWidth() ) {
                    x = containmentLowerX * gameContext.getPanelWidth();
                    haltX();
                } else if ( x > containmentUpperX * gameContext.getPanelWidth() ) {
                    x = containmentUpperX * gameContext.getPanelWidth();
                    haltX();
                }
                
                if ( y < containmentLowerY * gameContext.getPanelHeight() ) {
                    y = containmentLowerY * gameContext.getPanelHeight();
                    haltY();
                } else if ( y > containmentUpperY * gameContext.getPanelHeight() ) {
                    y = containmentUpperY * gameContext.getPanelHeight();
                    haltY();
                }
                
            }
            
            
            // Finish object if outside x bounds
            if ( x < - graphContext.getPanelWidth() * lowerXBound )
                finished = true;
            else if ( x > graphContext.getPanelWidth() * ( upperXBound + 1 ) )
                finished = true;
            
            // Finish object if outside y bounds
            if ( y >= graphContext.getPanelHeight() * ( upperYBound + 1 ) )
                finished = true;
            else if ( y < - graphContext.getPanelHeight() * lowerYBound )
                finished = true;
            
            if ( finished ) {
                //x = - Integer.MAX_VALUE;
                //y = - Integer.MAX_VALUE;
                xVelocity = 0;
                yVelocity = 0;
                xAcceleration = 0;
                yAcceleration = 0;
            }
            //logger.debug(toString());
        }
        
    }
    
    public void adjustForControlPattern(int elapsedTime) {
        ControlPrimitive nextPrimitive;
        
        timeOffsetCounter += elapsedTime;
        nextPrimitive = pattern.elementAt(primitiveIndex);
        
        while ( ! finishedControlPattern && ! haltedControlPattern && nextPrimitive.timeOffset < timeOffsetCounter ) {
            
            if ( nextPrimitive instanceof ControlDisplacement ) {
                ControlDisplacement control = (ControlDisplacement)nextPrimitive;
                
                if ( control.relative ) {
                    x = x + control.x + ( control.xRandom * ( Math.random() - 0.5 ) );
                    y = y + control.y + ( control.yRandom * ( Math.random() - 0.5 ) );
                } else {
                    x = control.x + ( control.xRandom * ( Math.random() - 0.5 ) );
                    y = control.y + ( control.yRandom * ( Math.random() - 0.5 ) );
                }
                
                if ( control.scaled ) {
                    x = x * graphContext.getPanelWidth();
                    y = y * graphContext.getPanelHeight();
                    logger.info("x: " + x + ", y: " + y);
                }
                
            } else if ( nextPrimitive instanceof ControlVelocity ) {
                ControlVelocity control = (ControlVelocity)nextPrimitive;
                xVelocity = control.x + ( control.xRandom * ( Math.random() - 0.5 ) );
                yVelocity = control.y + ( control.yRandom * ( Math.random() - 0.5 ) );
                terminalXDistance = control.terminalXDistance;
                terminalYDistance = control.terminalYDistance;
                if ( control.haltUntilTerminal && ( ! Double.isNaN(terminalXDistance) || ! Double.isNaN(terminalYDistance) ) ) {
                    haltedControlPattern = true;
                }
            } else if ( nextPrimitive instanceof ControlAcceleration ) {
                ControlAcceleration control = (ControlAcceleration)nextPrimitive;
                xAcceleration = control.x + ( control.xRandom * ( Math.random() - 0.5 ) );
                yAcceleration = control.y + ( control.yRandom * ( Math.random() - 0.5 ) );
                terminalXVelocity = control.terminalXVelocity;
                terminalYVelocity = control.terminalYVelocity;
                if ( control.haltUntilTerminal && ( ! Double.isNaN(terminalXVelocity) || ! Double.isNaN(terminalYVelocity) ) ) {
                    haltedControlPattern = true;
                }
            } else if ( nextPrimitive instanceof ControlFiring ) {
                fireBullet((ControlFiring)nextPrimitive);
            } else if ( nextPrimitive instanceof ControlRandomWait ) {
                ControlRandomWait control = (ControlRandomWait)nextPrimitive;
                timeOffsetCounter -= (int)(Math.random() * control.randomWait);
            } else if ( nextPrimitive instanceof ControlContainment ) {
                ControlContainment control = (ControlContainment)nextPrimitive;
                containment = true;
                containmentLowerX = control.containmentLowerX;
                containmentLowerY = control.containmentLowerY;
                containmentUpperX = control.containmentUpperX;
                containmentUpperY = control.containmentUpperY;
            } else if ( nextPrimitive instanceof ControlImage ) {
                gameImage = ((ControlImage)nextPrimitive).gameImage;
            } else if ( nextPrimitive instanceof ControlFinish ) {
                ControlFinish control = (ControlFinish)nextPrimitive;
                finished = true;
                if ( control.explosion ) {
                    gameContext.addBackgroundImage(new BackgroundImage(gameContext, graphContext, GameImage.EXPLOSION_50X50,
                            x + ( gameImage.getWidth() / 2 ) - ( GameImage.EXPLOSION_50X50.getWidth() / 2 ),
                            y + ( gameImage.getHeight() / 2 ) - ( GameImage.EXPLOSION_50X50.getHeight() / 2 ),
                            false, ControlPattern.explosionEnemy));
                    gameContext.getSoundThread().addSound(Sounds.EXPLOSION_SPATIAL);
                    
                }
                
            } else if ( nextPrimitive instanceof ControlExplosionParticle ) {
                ControlExplosionParticle control = (ControlExplosionParticle)nextPrimitive;
                //logger.info("Particle: controlX=" + control.xVelocity + ", controlY=" + control.yVelocity);
                gameContext.addExplosionParticle(new ExplosionParticle(gameContext, graphContext, x, y, control.xVelocity, control.yVelocity, ExplosionParticleShape.FIREWORKS_PLUS, ((ExplosionParticle)this).color, null));
                
            }
            
            
            primitiveIterationsCount++;
            primitiveIndex++;
            if ( primitiveIndex >= pattern.size() ) {
                primitiveIndex = pattern.getRepeatFrom();
                //timeOffsetCounter -= nextPrimitive.timeOffset;
                nextPrimitive = pattern.elementAt(primitiveIndex);
                timeOffsetCounter = nextPrimitive.timeOffset;
                if ( ! pattern.isRepeating() )
                    finishedControlPattern = true;
            } else
                nextPrimitive = pattern.elementAt(primitiveIndex);
            //if ( ! pattern.isRepeating() && primitiveIterationsCount >= pattern.getIterations() )
            //    finishedMovementPattern = true;
        }
    }
    
    /*
    public void fireBullet(boolean homing, double totalVelocity, double bulletXVelocity, double bulletYVelocity) {
        Enemy bullet;
        double scaleFactor;
        double xDistanceDifference;
        double yDistanceDifference;
        double xBulletOrigin;
        double yBulletOrigin;
     
        xBulletOrigin = x + ((Enemy)this).gameImage.getWidth() / 2;
        yBulletOrigin = y + ((Enemy)this).gameImage.getHeight() / 2;
        if ( homing ) {
            xDistanceDifference = gameContext.getSelf().getX() - xBulletOrigin;
            yDistanceDifference = gameContext.getSelf().getY() - yBulletOrigin;
            scaleFactor = Math.sqrt( 1 / ( xDistanceDifference * xDistanceDifference + yDistanceDifference * yDistanceDifference ) );
            bulletXVelocity = totalVelocity * xDistanceDifference * scaleFactor;
            bulletYVelocity = totalVelocity * yDistanceDifference * scaleFactor;
        }
     
        bullet = new Enemy(gameContext, graphContext, xBulletOrigin, yBulletOrigin, bulletXVelocity, bulletYVelocity, EnemyType.BULLET, null);
        gameContext.addEnemy(bullet);
    }
     */
    
    public void fireBullet(ControlFiring control) {
        Enemy bullet;
        //double scaleFactor;
        double xDistanceDifference;
        double yDistanceDifference;
        double xBulletOrigin;
        double yBulletOrigin;
        double bulletXVelocity;
        double bulletYVelocity;
        double angle;
        double magnitudeRandom;
        
        if ( control.useDefaultOffsets ) {
            //xBulletOrigin = x + ((Enemy)this).gameImage.getWidth() / 2;
            //yBulletOrigin = y + ((Enemy)this).gameImage.getHeight() / 2;
            xBulletOrigin = x + ((Enemy)this).firingXOffset;
            yBulletOrigin = y + ((Enemy)this).firingYOffset;
        } else {
            xBulletOrigin = x + control.xOffset;
            yBulletOrigin = y + control.yOffset;
        }
        
        if ( control.movementType == ControlFiring.MovementType.HOMING || control.movementType == ControlFiring.MovementType.ANGLED ) {
            
            if ( control.movementType == ControlFiring.MovementType.HOMING ) {
                xDistanceDifference = gameContext.getSelf().getX() - xBulletOrigin;
                yDistanceDifference = gameContext.getSelf().getY() - yBulletOrigin;
                angle = Math.atan2(xDistanceDifference, yDistanceDifference);
                //scaleFactor = Math.sqrt( 1 / ( xDistanceDifference * xDistanceDifference + yDistanceDifference * yDistanceDifference ) );
                //bulletXVelocity = control.magnitude * xDistanceDifference * scaleFactor;
                //bulletYVelocity = control.magnitude * yDistanceDifference * scaleFactor;
            } else {
                //angle = 0;
                angle = ORIENTATION_OFFSET;
            }
            
            angle = angle + Math.toRadians(control.angle + ( ( Math.random() - 0.5 ) * control.angleRandom ) );
            magnitudeRandom = ( Math.random() - 0.5 ) * control.magnitudeRandom;
            bulletXVelocity = ( control.magnitude + magnitudeRandom ) * Math.sin(angle);
            bulletYVelocity = ( control.magnitude + magnitudeRandom ) * Math.cos(angle);
            bullet = new Enemy(gameContext, graphContext, xBulletOrigin, yBulletOrigin, bulletXVelocity, bulletYVelocity, control.firingType, null);
            
        } else if ( control.movementType == ControlFiring.MovementType.XY ) {
            bulletXVelocity = control.xVelocity;
            bulletYVelocity = control.yVelocity;
            bullet = new Enemy(gameContext, graphContext, xBulletOrigin, yBulletOrigin, bulletXVelocity, bulletYVelocity, control.firingType, null);
            
        //} else if ( control.movementType == ControlFiring.MovementType.CONTROLLED ) {
        } else {
            bullet = new Enemy(gameContext, graphContext, xBulletOrigin, yBulletOrigin, 0, 0, control.firingType, control.controlPattern);
            //logger.info("Controlled firing: " + control.firingType);
        }
        
        //bullet = new Enemy(gameContext, graphContext, xBulletOrigin, yBulletOrigin, bulletXVelocity, bulletYVelocity, control.firingType, null);
        gameContext.addEnemy(bullet);
    }
    
    public boolean detectCollisionWithOther(SpatialObject other) {
        int otherXOrigin;
        int otherYOrigin;
        int otherWidth;
        int otherHeight;
        
        otherXOrigin = (int)(other.getX());
        otherYOrigin = (int)(other.getY());
        otherWidth = other.getGameImage().getWidth();
        otherHeight = other.getGameImage().getHeight();
        
        // First pass - quick rectangle bounding collision detection
        if ( x < otherXOrigin + otherWidth && x + gameImage.getWidth() > otherXOrigin &&
                y < otherYOrigin + otherHeight && y + gameImage.getHeight() > otherYOrigin ) {
            
            // Second pass - slower horizontal pixel profile matching
            int thisXOffset;
            int otherXOffset;
            int xOverlapWidth;
            int thisYOffset;
            int otherYOffset;
            int yOverlapHeight;
            
            thisXOffset = Math.max(otherXOrigin - (int)x, 0);
            otherXOffset = Math.max((int)x - otherXOrigin, 0);
            xOverlapWidth = Math.min((int)x + gameImage.getWidth(), otherXOrigin + otherWidth) -
                    Math.max((int)x, otherXOrigin);
            thisYOffset = Math.max(otherYOrigin - (int)y, 0);
            otherYOffset = Math.max((int)y - otherYOrigin, 0);
            yOverlapHeight = Math.min((int)y + gameImage.getHeight(), otherYOrigin + otherHeight) -
                    Math.max((int)y, otherYOrigin);
            
            if ( gameImage.collisionOverlap(other.getGameImage(), thisXOffset, thisYOffset, otherXOffset, otherYOffset, xOverlapWidth, yOverlapHeight))
                return true;
        }
        return false;
    }
    
    public void haltX() {
        xVelocity = 0;
        xAcceleration = 0;
        terminalXDistance = Double.NaN;
        terminalXVelocity = Double.NaN;
        if ( Double.isNaN(terminalYDistance) && Double.isNaN(terminalYVelocity) )
            haltedControlPattern = false;
    }
    
    public void haltY() {
        yVelocity = 0;
        yAcceleration = 0;
        terminalYDistance = Double.NaN;
        terminalYVelocity = Double.NaN;
        if ( Double.isNaN(terminalXDistance) && Double.isNaN(terminalXVelocity) )
            haltedControlPattern = false;
    }
    
    public GameImage getGameImage() {
        return gameImage;
    }
    
    public Image getImage() {
        return gameImage.getImage();
    }
    
    public double getX() {
        return x;
    }
    
    public void setX(double newX) {
        previousX = x;
        x = newX;
    }
    
    public double getPreviousX() {
        return previousX;
    }
    
    public double getY() {
        return y;
    }
    
    public double getPreviousY() {
        return previousY;
    }
    
    public void setXVelocity(double velocity) {
        xVelocity = velocity;
    }
    
    public void setYVelocity(double velocity) {
        yVelocity = velocity;
    }
    
    public void setXAcceleration(double acceleration) {
        xAcceleration = acceleration;
    }
    
    public void setYAcceleration(double acceleration) {
        yAcceleration = acceleration;
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
        return  "x=" + x + ",y=" + y +
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
