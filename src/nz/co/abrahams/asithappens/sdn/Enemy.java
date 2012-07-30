/*
 * Enemy.java
 *
 * Created on 6 January 2008, 13:14
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
import java.awt.Point;
import java.awt.Color;

/**
 *
 * @author mark
 */
public class Enemy extends SpatialObject {
    
    public static final int TYPE_USER = 1;
    
    public static final int TYPE_PROCUREMENT = 2;
    
    public static final int LOWER_X_BOUND = 1;
    
    public static final int UPPER_X_BOUND = 1;
    
    public static final int LOWER_Y_BOUND = 1;
    
    public static final int UPPER_Y_BOUND = 1;
    
    public static final int MAXIMUM_EXPLOSION_PARTICLE = 5;
    
    public static final double MAXIMUM_PARTICLE_VELOCITY = 0.5;
    
    public static final int MASS_SCALING_FACTOR = 4;
    
    public static final double TERMINAL_X_VELOCITY = 0.5;
    
    public static final double TERMINAL_Y_VELOCITY = 0.5;
    
    
    protected int type;
    
    //protected GameImage gameImage;
    
    protected boolean shootable;
    
    protected int vitality;
    
    protected int points;
    
    protected long previousUpdateFiringTime;
    
    protected int firingCounter;
    
    protected int firingIndex;
    
    protected ControlPattern firingPattern;
    
    protected boolean finishedFiringPattern;
    
    protected int firingXOffset;
    
    protected int firingYOffset;
    
    /** Creates a new instance of Enemy */
    public Enemy(GameContext gameContext, TimeSeriesContext graphContext,
            double initialX, double initialY,
            double initialXVelocity, double initialYVelocity,
            int type, ControlPattern movementPattern,
            ControlPattern firingPattern) {
        /*
        super(gameContext, graphContext, xInPixelDomain, initialX, yInPixelDomain, initialY,
                initialXVelocity, initialYVelocity, TERMINAL_X_VELOCITY, TERMINAL_Y_VELOCITY, subjectToGravity,
                lowerXBound, upperXBound, lowerYBound, upperYBound, movementPattern);
         */
        super(gameContext, graphContext, gameContext.getEnemyType(type).gameImage, initialX, initialY,
                initialXVelocity, initialYVelocity, TERMINAL_X_VELOCITY, TERMINAL_Y_VELOCITY,
                gameContext.getEnemyType(type).subjectToGravity,
                gameContext.getEnemyType(type).lowerXBound,
                gameContext.getEnemyType(type).upperXBound,
                gameContext.getEnemyType(type).lowerYBound,
                gameContext.getEnemyType(type).upperYBound,
                movementPattern);
        
        this.type = type;
        this.firingPattern = firingPattern;
        //gameImage = gameContext.getEnemyType(type).gameImage;
        shootable = gameContext.getEnemyType(type).shootable;
        vitality = gameContext.getEnemyType(type).vitality;
        points = gameContext.getEnemyType(type).points;
        firingXOffset = gameContext.getEnemyType(type).firingXOffset;
        firingYOffset = gameContext.getEnemyType(type).firingYOffset;
        firingCounter = 0;
        firingIndex = 0;
        finishedFiringPattern = false;
        previousUpdateFiringTime = System.currentTimeMillis();
        
    }
    
    public Enemy(GameContext gameContext, TimeSeriesContext graphContext,
            double initialX, double initialY,
            double initialXVelocity, double initialYVelocity,
            int type, ControlPattern movementPattern) {
        this(gameContext, graphContext, initialX, initialY, initialXVelocity, initialYVelocity, type, movementPattern, null);
    }
    
    
    public void destroy(int damage, Point point) {
        int totalMass;
        int explodedMass;
        
        
        vitality -= damage;
        if ( vitality <= 0 ) {
            
            // Create explosion particles
            totalMass = gameImage.getWidth() * gameImage.getHeight();
            explodedMass = 0;
            
            while ( explodedMass < totalMass ) {
                int particleSize;
                ExplosionParticle particle;
                int explosionPixels;
                int patternIndex;
                ExplosionParticleShape particleShape;
                Color color;
                
                explosionPixels = (int)(Math.random() * ExplosionParticleShape.SHAPES.length);
                patternIndex = (int)(Math.random() * ExplosionParticleShape.SHAPES[explosionPixels].length);
                particleShape = ExplosionParticleShape.SHAPES[explosionPixels][patternIndex];
                color = ExplosionParticleShape.COLORS[(int)(Math.random() * ExplosionParticleShape.COLORS.length)];
                //logger.debug("Particle created: ");
                //logger.debug(particle.toString());
                
                particleSize = (int)(Math.random() * MAXIMUM_EXPLOSION_PARTICLE + 1);
                particle = new ExplosionParticle(gameContext, graphContext,
                        x + Math.random() * gameImage.getWidth(),
                        y + Math.random() * gameImage.getHeight(),
                        (Math.random() - 0.5) * MAXIMUM_PARTICLE_VELOCITY + xVelocity,
                        (Math.random() - 0.5) * MAXIMUM_PARTICLE_VELOCITY + yVelocity,
                        particleShape, color, null);
                gameContext.addExplosionParticle(particle);
                //explodedMass += particleSize * 2;
                // violate conservation of mass, but explosions were too big
                explodedMass += particleSize * MASS_SCALING_FACTOR;
            }
            
            gameContext.addBackgroundImage(new BackgroundImage(gameContext, graphContext, GameImage.EXPLOSION_50X50,
                    x + ( gameImage.getWidth() / 2 ) - ( GameImage.EXPLOSION_50X50.getWidth() / 2 ),
                    y + ( gameImage.getHeight() / 2 ) - ( GameImage.EXPLOSION_50X50.getHeight() / 2 ),
                    false, ControlPattern.explosionEnemy));
            
            // Procurement destruction produces a powerup
            if ( gameContext.enemyType[type].yieldsPowerup ) {
                /*
                if ( gameContext.getSelf().isAtCapacity(gameContext.enemyType[type].powerupType) )
                    gameContext.addCollectable(new Powerup(gameContext, graphContext, Powerup.Type.NULL, x, y, - Powerup.VELOCITY, 0, null));
                else
                    gameContext.addCollectable(new Powerup(gameContext, graphContext, gameContext.enemyType[type].powerupType, x, y, - Powerup.VELOCITY, 0, null));
                 */
                
                if ( Math.random() > 1d / Powerup.Type.values().length ) {
                    gameContext.addCollectable(new Powerup(gameContext, graphContext, gameContext.enemyType[type].powerupType, x, y, - Powerup.VELOCITY, 0, null));
                } else {
                    gameContext.addCollectable(new Powerup(gameContext, graphContext, Powerup.Type.NULL, x, y, - Powerup.VELOCITY, 0, null));
                }
            }
            
            gameContext.increaseScore(gameContext.enemyType[type].points);
            finished = true;
        }
        
        if ( gameContext.enemyType[type].type == EnemyType.BOSS_RECEIVER ) {
            Powerup.Type powerupType;
            
            if ( Math.random() < 1d / Powerup.Type.values().length ) {
                powerupType = Powerup.Type.typeFromIndex((int)(Math.random() * Powerup.Type.values().length));
                gameContext.addCollectable(new Powerup(gameContext, graphContext, powerupType, point.x, point.y, - Powerup.VELOCITY * 2, 0, null));
            }
        }
    }
    
    public Point collisionWithSpatialObject(SpatialObject other) {
        Point point;
        
        for ( int otherYOffset = 0 ; otherYOffset < other.getGameImage().getHeight() ; otherYOffset++ ) {
            point = collisionWithHorizontal((int)(other.x) + other.getGameImage().getLeftOffset(otherYOffset), (int)(other.x) + other.getGameImage().getRightOffset(otherYOffset), (int)other.y + otherYOffset);
            if ( point != null )
                return point;
        }
        return null;
    }
    
    public Point collisionWithDiagonalOld(int startX, int startY, int endX, int endY) {
        //int enemyXOrigin;
        //int enemyYOrigin;
        int enemyWidth;
        int enemyHeight;
        //int xPixel;
        //int yPixel;
        
        //enemyXOrigin = (int)getXPixel();
        //enemyYOrigin = (int)getYPixel();
        enemyWidth = gameImage.getWidth();
        enemyHeight = gameImage.getHeight();
        
        // First pass - quick rectangle bounding collision detection
        if ( startX < (int)x + enemyWidth && endX > (int)x &&
                startY < (int)y + enemyHeight && endY > (int)y ) {
            double m;  // line gradient
            double c;  // line y-axis cut
            int cutsTopX;
            int cutsBottomX;
            int cutsLeftY;
            int cutsRightY;
            
            // Vertical line (infinite gradient)
            if ( startX == endX ) {
                //if ( startX >= enemyXOrigin && startX < enemyXOrigin + enemyWidth ) {
                //logger.debug("Collision: startX=" + startX + ",startY=" + startY + ",endX=" + endX + ",endY=" + endY + ",x=" + x + ",y=" + y + ",eWidth=" + enemyWidth + ",eHeight" + enemyHeight);
                if ( startY < (int)y ) {
                    return new Point(startX, (int)y);
                } else if ( startY > (int)y + enemyHeight - 1 ) {
                    return new Point(startX, (int)y + enemyHeight - 1);
                } else {
                    return new Point(startX, startY);
                }
                //return true;
            }
            
            m = ( endY - startY ) / ( endX - startX );
            c = startY - ( m * startX );
            
            // Bug fix - need to determine correct ordering if cutting two sides
            cutsLeftY = (int)(m * x + c);
            if ( cutsLeftY >= y && cutsLeftY < y + enemyHeight ) {
                //logger.debug("Collision: startX=" + startX + ",startY=" + startY + ",endX=" + endX + ",endY=" + endY + ",m=" + m + ",c=" + c + ",x=" + x + ",y=" + y + ",eWidth=" + enemyWidth + ",eHeight" + enemyHeight + ",cutsLeftY=" + cutsLeftY);
                return new Point((int)x, cutsLeftY);
                //return true;
            }
            cutsTopX = (int)( ( (int)y - c ) / m);
            if ( cutsTopX >= x && cutsTopX < x + enemyWidth ) {
                //logger.debug("Collision: startX=" + startX + ",startY=" + startY + ",endX=" + endX + ",endY=" + endY + ",m=" + m + ",c=" + c + ",x=" + x + ",y=" + y + ",eWidth=" + enemyWidth + ",eHeight" + enemyHeight + ",cutsTopX=" + cutsTopX);
                return new Point(cutsTopX, (int)y);
                //return true;
            }
            cutsBottomX = (int)( ( y + enemyHeight - 1 - c ) / m);
            if ( cutsBottomX >= x && cutsBottomX < x + enemyWidth ) {
                //logger.debug("Collision: startX=" + startX + ",startY=" + startY + ",endX=" + endX + ",endY=" + endY + ",m=" + m + ",c=" + c + ",x=" + x + ",y=" + y + ",eWidth=" + enemyWidth + ",eHeight" + enemyHeight + ",cutsBottomX=" + cutsBottomX);
                return new Point(cutsBottomX, (int)y + enemyHeight - 1);
                //return true;
            }
            cutsRightY = (int)(m * ( x + enemyWidth - 1 ) + c);
            if ( cutsRightY >= y && cutsRightY < y + enemyHeight ) {
                //logger.debug("Collision: startX=" + startX + ",startY=" + startY + ",endX=" + endX + ",endY=" + endY + ",m=" + m + ",c=" + c + ",x=" + x + ",y=" + y + ",eWidth=" + enemyWidth + ",eHeight" + enemyHeight + ",cutsRightY=" + cutsRightY);
                return new Point((int)x, cutsRightY);
                //return true;
            }
        }
        
        //return false;
        return null;
    }
    
    public Point collisionWithDiagonal(int startX, int startY, int endX, int endY) {
        int enemyWidth;
        int enemyHeight;
        
        enemyWidth = gameImage.getWidth();
        enemyHeight = gameImage.getHeight();
        
        // First pass - quick rectangle bounding collision detection
        if ( startX < (int)x + enemyWidth && endX > (int)x &&
                startY < (int)y + enemyHeight && endY > (int)y ) {
            int yChange;
            int ySwap;
            
            if ( startY > endY ) {
                ySwap = startY;
                startY = endY;
                endY = ySwap;
            }
            
            for ( int yCheck = startY ; yCheck <= endY ; yCheck++ ) {
                if ( yCheck >= (int)y && yCheck - (int)y < gameImage.getHeight() &&
                        startX < (int)x + gameImage.getRightOffset(yCheck - (int)y) &&
                        endX > (int)x + gameImage.getLeftOffset(yCheck - (int)y) ) {
                    return new Point(Math.max(startX, (int)x + gameImage.getLeftOffset(yCheck - (int)y)), yCheck);
                }
                
            }
            
        }
        
        return null;
    }
    
    public Point collisionWithHorizontal(int startX, int endX, int lineY) {
        int enemyWidth;
        int enemyHeight;
        
        enemyWidth = gameImage.getWidth();
        enemyHeight = gameImage.getHeight();
        
        // First pass - quick rectangle bounding collision detection
        if ( lineY < (int)y + enemyHeight && lineY >= (int)y ) {
            if ( startX < (int)x + gameImage.getRightOffset(lineY - (int)y) &&
                    endX > (int)x + gameImage.getLeftOffset(lineY - (int)y) ) {
                return new Point(Math.max(startX, (int)x + gameImage.getLeftOffset(lineY - (int)y)), lineY);
            }
        }
        return null;
    }
    
    public void update() {
        super.update();
        if ( firingPattern != null )
            updateFiring();
    }
    
    public void updateFiring() {
        long newUpdateTime;
        int elapsedTime;
        ControlPrimitive nextPrimitive;
        
        newUpdateTime = System.currentTimeMillis();
        elapsedTime = (int)(newUpdateTime - previousUpdateFiringTime);
        previousUpdateFiringTime = newUpdateTime;
        
        firingCounter += elapsedTime;
        nextPrimitive = firingPattern.elementAt(firingIndex);
        while ( ! finishedFiringPattern && nextPrimitive.timeOffset < firingCounter ) {
            if ( nextPrimitive instanceof ControlFiring ) {
                /*
                fireBullet(((ControlFiring) nextPrimitive).homing,
                        ((ControlFiring) nextPrimitive).magnitude,
                        nextPrimitive.x, nextPrimitive.y);
                 */
                fireBullet((ControlFiring)nextPrimitive);
            } else if ( nextPrimitive instanceof ControlRandomWait ) {
                ControlRandomWait control = (ControlRandomWait)nextPrimitive;
                firingCounter -= (int)(Math.random() * control.randomWait);
            }
            
            
            //primitiveIterationsCount++;
            firingIndex++;
            if ( firingIndex >= firingPattern.size() ) {
                if ( firingPattern.isRepeating() ) {
                    firingIndex = firingPattern.getRepeatFrom();
                    //timeOffsetCounter -= nextPrimitive.timeOffset;
                    nextPrimitive = firingPattern.elementAt(firingIndex);
                    firingCounter = 0;
                } else {
                    finishedFiringPattern = true;
                    firingIndex = 0;
                }
            } else
                nextPrimitive = firingPattern.elementAt(firingIndex);
        }
    }
    
    public Image getImage() {
        return gameImage.getImage();
    }
    
    public GameImage getGameImage() {
        return gameImage;
    }
    
    public boolean isShootable() {
        return shootable;
    }
}
