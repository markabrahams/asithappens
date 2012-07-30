/*
 * Laser.java
 *
 * Created on 23 December 2007, 11:27
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
import java.awt.Point;
import java.awt.Color;

/**
 *
 * @author mark
 */
public class Laser extends SpatialObject {
    
    public static final double UPDATE_PERIOD = 5;
    
    /*
    public static final double INITIAL_START_X_ACCELERATION = 0.01;
     
    public static final double INITIAL_END_X_ACCELERATION = 0.02;
     
    public static final double INITIAL_START_X_VELOCITY = 0;
     
    public static final double INITIAL_END_X_VELOCITY = 0.01;
     */
    
    //public static final double INITIAL_START_X_ACCELERATION = 0.005;
    
    //public static final double INITIAL_END_X_ACCELERATION = 0.01;
    
    //public static final double INITIAL_START_X_VELOCITY = 0;
    
    //public static final double INITIAL_END_X_VELOCITY = 0.005;
    
    public static final int LOWER_X_BOUND = 0;
    
    public static final int UPPER_X_BOUND = 1;
    
    public static final int LOWER_Y_BOUND = 0;
    
    public static final int UPPER_Y_BOUND = 0;
    
    protected GameContext gameContext;
    
    protected TimeSeriesContext graphContext;
    
    //protected double startX;
    
    //protected int endX;
    
    //protected Color color;
    
    protected int enemyDamage;
    
    protected int landscapeDamage;
    
    //protected double previousStartX;
    
    //protected double previousLaserY;
    
    //protected double previousEndX;
    
    //protected int y;
    
    protected boolean hitSomething;
    
    protected int xTermination;
    
    protected int yTermination;
    
    protected int xCreation;
    
    protected int yCreation;
    
    //protected double startXVelocity;
    
    //protected double endXVelocity;
    
    //protected double startXAcceleration;
    
    //protected double endXAcceleration;
    
    //protected SpatialObject laserStart;
    
    protected SpatialObject laserEnd;
    
    protected long lastUpdateTime;
    
    //protected boolean finished;
    
    /** Creates a new instance of Laser */
    public Laser(GameContext gameContext, TimeSeriesContext graphContext, int enemyDamage, int landscapeDamage, double initialX, double initialY,
            double initialXVelocity, double initialYVelocity, double terminalXVelocity, double terminalYVelocity,
            double startInitialXAcceleration, double endInitialXAcceleration,
            double initialYAcceleration) {
        super(gameContext, graphContext, null, initialX, initialY, initialXVelocity, initialYVelocity, terminalXVelocity, terminalYVelocity,
                false, LOWER_X_BOUND, UPPER_X_BOUND, LOWER_Y_BOUND, UPPER_Y_BOUND, null);
        xAcceleration = startInitialXAcceleration;
        yAcceleration = initialYAcceleration;
        
        /* old end x initialization
        super(gameContext, graphContext, true, initialX, true, initialY, initialXVelocity, initialYVelocity, terminalXVelocity, 0,
                false, LOWER_X_BOUND, UPPER_X_BOUND, LOWER_Y_BOUND, UPPER_Y_BOUND, null);
         */
        this.gameContext = gameContext;
        this.graphContext = graphContext;
        //this.color = color;
        this.enemyDamage = enemyDamage;
        this.landscapeDamage = landscapeDamage;
        //startX = initialX;
        //endX = x;
        //previousStartX = initialX;
        //previousEndX = initialX;
        //previousLaserY = initialY;
        hitSomething = false;
        xTermination = graphContext.getPanelWidth() - 1;
        yTermination = graphContext.getPanelHeight() - 1;
        xCreation = (int)initialX;
        yCreation = (int)initialY;
        //this.y = y;
        
        //startXVelocity = initialXVelocity;
        //endXVelocity = INITIAL_END_X_VELOCITY;
        //startXAcceleration = startInitialXAcceleration;
        //xAcceleration = startInitialXAcceleration;
        //yAcceleration = initialYAcceleration;
        //this.terminalXVelocity = terminalXVelocity;
        //terminalYVelocity = 0;
        
        /*
        laserStart = new SpatialObject(gameContext, graphContext, true, initialX, true, initialY, initialXVelocity, 0, terminalXVelocity, 0,
                false, LOWER_X_BOUND, UPPER_X_BOUND, LOWER_Y_BOUND, UPPER_Y_BOUND, null);
        laserStart.setXAcceleration(startInitialXAcceleration);
         */
        
        laserEnd = new SpatialObject(gameContext, graphContext, null, initialX, initialY, initialXVelocity, initialYVelocity, terminalXVelocity, 0,
                false, LOWER_X_BOUND, UPPER_X_BOUND, LOWER_Y_BOUND, UPPER_Y_BOUND, null);
        laserEnd.setXAcceleration(endInitialXAcceleration);
        laserEnd.setYAcceleration(initialYAcceleration);
        
        //endXAcceleration = INITIAL_END_X_ACCELERATION;
        //lastUpdateTime = System.currentTimeMillis();
        //finished = false;
    }
    
    public void update() {
        super.update();
        laserEnd.update();
        laserEnd.setY(y);
        
        if ( x > laserEnd.getX() ) {
            //logger.debug("Laser termination: x=" + x + ",endX=" + laserEnd.getX());
            x = xTermination;
            y = yTermination;
            finished = true;
        }
        
        //logger.debug("Laser start: " + toString());
        //logger.debug("Laser end: " + laserEnd.toString());
    }
    
    public void detectCollision() {
        
        //logger.debug("startX=" + (int)previousStartX + ",startY=" + (int)previousY + ",endY=" + (int)laserStart.getX() + ",endY=" + (int)y);
        /*
        if ( ! hitSomething )
            hitSomething = detectCollisionWithDiagonal((int)previousStartX, (int)previousLaserY, (int)laserStart.getX(), (int)y);
        if ( ! hitSomething )
            hitSomething = detectCollisionWithDiagonal(Math.min((int)previousEndX, (int)laserStart.getX()), (int)y, (int)x, (int)y);
         **/
        if ( ! hitSomething )
            hitSomething = detectCollisionWithDiagonal((int)previousX, (int)previousY, (int)x, (int)y);
        if ( ! hitSomething )
            hitSomething = detectCollisionWithDiagonal(Math.min((int)laserEnd.getPreviousX(), (int)x), (int)laserEnd.getY(), (int)laserEnd.getX(), (int)laserEnd.getY());
    }
    
    public boolean detectCollisionWithDiagonal(int startX, int startY, int endX, int endY) {
        
        int hitEnemyIndex;
        Point enemyCollisionPoint;
        Point landscapeCollisionPoint;
        Point nextCollisionPoint;
        
        enemyCollisionPoint = null;
        hitEnemyIndex = -1;
        
        // Find first enemy that laser would collide with
        for ( int i = 0 ; i < gameContext.getEnemies().size() ; i++ ) {
            
            // Detects collisions due to movement diagonal
            if ( gameContext.getEnemies().elementAt(i).isShootable() ) {
                if ( startY == endY )
                    nextCollisionPoint = gameContext.getEnemies().elementAt(i).collisionWithHorizontal(startX, endX, startY);
                else
                    nextCollisionPoint = gameContext.getEnemies().elementAt(i).collisionWithDiagonal(startX, startY, endX, endY);
                if ( nextCollisionPoint != null && ( enemyCollisionPoint == null || nextCollisionPoint.x < enemyCollisionPoint.x ) ) {
                    enemyCollisionPoint = nextCollisionPoint;
                    hitEnemyIndex = i;
                }
            }
        }
        
        if ( enemyCollisionPoint != null ) {
            landscapeCollisionPoint = gameContext.getLandscape().collisionWithDiagonal(startX, startY, enemyCollisionPoint.x, enemyCollisionPoint.y);
        } else {
            landscapeCollisionPoint = gameContext.getLandscape().collisionWithDiagonal(startX, startY, endX, endY);
        }
        
        if ( landscapeCollisionPoint != null ) {
            landscapeCollision(landscapeCollisionPoint);
            return true;
        } else if ( enemyCollisionPoint != null ) {
            enemyCollision(enemyCollisionPoint, hitEnemyIndex);
            return true;
        }
        return false;
        
    }
    
    protected void landscapeCollision(Point point) {
        
        synchronized(graphContext.getData()) {
            synchronized(graphContext.getSummarySemaphore()) {
                gameContext.getLandscape().decimateDiameter(point.x, point.y, landscapeDamage);
                //gameContext.getLandscape().decimateRadius(point.x, point.y, gameContext.getSelf().getLaserLandscapeDecimation());
                //gameContext.getLandscape().decimateColumn(xPixel, y, gameContext.getSelf().getDecimationLength());
                // Note to self: Consider the effect of the next statement carefully
                graphContext.generateSummaryData();
                gameContext.graphDataHasChanged();
            }
        }
        terminateLaserEnd(point);
        //Sounds.play(Sounds.EXPLOSION_LANDSCAPE);
    }
    
    protected void enemyCollision(Point point, int enemyIndex) {
        gameContext.getEnemies().elementAt(enemyIndex).destroy(enemyDamage, point);
        terminateLaserEnd(point);
        //Sounds.play(Sounds.EXPLOSION_ENEMY);
        //if ( gameContext.isSoundOn() )
        //    Sounds.EXPLOSION_ENEMY.play();
        gameContext.getSoundThread().addSound(Sounds.EXPLOSION_ENEMY);
    }
    
    protected void terminateLaserEnd(Point point) {
        xTermination = point.x;
        laserEnd.setX(point.x);
        yTermination = point.y;
        laserEnd.setY(point.y);
        laserEnd.setXVelocity(0);
        laserEnd.setXAcceleration(0);
        laserEnd.setYVelocity(0);
        laserEnd.setYAcceleration(0);
        yAcceleration = 0;
        //logger.debug("Terminating laser at x=" + point.x + ",y=" + point.y);
    }
    
    public int getStartX() {
        return (int)x;
    }
    
    public int getEndX() {
        return (int)laserEnd.getX();
    }
    
    public int getPreviousEndX() {
        //return (int)previousEndX;
        return (int)laserEnd.getPreviousX();
    }
    
    public int getXCreation() {
        return xCreation;
    }
    
    public int getYCreation() {
        return yCreation;
    }
    
    public int getXTermination() {
        return xTermination;
    }
    
    public void setXTermination(int value) {
        xTermination = value;
    }
    
    public int getEnemyDamage() {
        return enemyDamage;
    }
    
    /*
    public Color getColor() {
        return color;
    }
    */
    
    /*
    public boolean isFinished() {
        return finished;
    }
     */
}
