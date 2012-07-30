/*
 * Bomb.java
 *
 * Created on 26 January 2008, 09:28
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

/**
 *
 * @author mark
 */
public class Bomb extends SpatialObject {
    
    public static final int LOWER_X_BOUND = 0;
    
    public static final int UPPER_X_BOUND = 0;
    
    public static final int LOWER_Y_BOUND = 0;
    
    public static final int UPPER_Y_BOUND = 0;
    
    public static final double TERMINAL_Y_VELOCITY = 0.5;
    
    //protected GameImage gameImage;
    
    protected double terminalYVelocitiy;
    
    protected int damage;
    
    /** Creates a new instance of Bomb */
    public Bomb(GameContext gameContext, TimeSeriesContext graphContext, int damage, double initialX, double initialY,
            double initialXVelocity, double initialYVelocity) {
        super(gameContext, graphContext, GameImage.BOMB, initialX, initialY, initialXVelocity, initialYVelocity, Double.NaN, TERMINAL_Y_VELOCITY,
                true, LOWER_X_BOUND, UPPER_X_BOUND, LOWER_Y_BOUND, UPPER_Y_BOUND, null);
        this.damage = damage;
        //gameImage = GameImage.BOMB;
    }
    
    public void detectCollision() {
        boolean hitSomething;
        
        //logger.debug("startX=" + (int)previousStartX + ",startY=" + (int)previousY + ",endY=" + (int)laserStart.getX() + ",endY=" + (int)y);
        
        // the old way
        hitSomething = detectCollisionWithDiagonal((int)previousX, (int)previousY, (int)x, (int)y);
        if ( ! hitSomething )
            hitSomething = detectCollisionWithDiagonal((int)x, (int)y, (int)x + gameImage.getWidth(), (int)y + gameImage.getHeight());
        
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
                gameContext.getLandscape().decimateDiameter(point.x, point.y, gameContext.getSelf().getBombLandscapeDecimation());
                //gameContext.getLandscape().decimateColumn(xPixel, y, gameContext.getSelf().getDecimationLength());
                // Note to self: Consider the effect of the next statement carefully
                graphContext.generateSummaryData();
                gameContext.graphDataHasChanged();
            }
        }
        destroy(point);
        /*
        x = point.x;
        xVelocity = 0;
        xAcceleration = 0;
        finished = true;
        //Sounds.play(Sounds.EXPLOSION_BOMB);
        //if ( gameContext.isSoundOn() )
        //    Sounds.EXPLOSION_BOMB.play();
        gameContext.getSoundThread().addSound(Sounds.EXPLOSION_BOMB);
        gameContext.addBackgroundImage(new BackgroundImage(gameContext, graphContext, GameImage.EXPLOSION_120X120,
                point.x - ( GameImage.EXPLOSION_120X120.getWidth() / 2 ), point.y - ( GameImage.EXPLOSION_120X120.getHeight() / 2 ),
                false, LevelAttackPattern.explosionBombedLandscape));
         */
    }
    
    protected void enemyCollision(Point point, int enemyIndex) {
        gameContext.getEnemies().elementAt(enemyIndex).destroy(damage, point);
        destroy(point);
        /*
        x = point.x;
        xVelocity = 0;
        xAcceleration = 0;
        finished = true;
        //Sounds.play(Sounds.EXPLOSION_BOMB);
        //if ( gameContext.isSoundOn() )
        //    Sounds.EXPLOSION_BOMB.play();
        gameContext.getSoundThread().addSound(Sounds.EXPLOSION_BOMB);
         */
    }
    
    protected void destroy(Point point) {
        x = point.x;
        xVelocity = 0;
        xAcceleration = 0;
        finished = true;
        //Sounds.play(Sounds.EXPLOSION_BOMB);
        //if ( gameContext.isSoundOn() )
        //    Sounds.EXPLOSION_BOMB.play();
        gameContext.getSoundThread().addSound(Sounds.EXPLOSION_BOMB);
        gameContext.addBackgroundImage(new BackgroundImage(gameContext, graphContext, GameImage.EXPLOSION_120X120,
                point.x - ( GameImage.EXPLOSION_120X120.getWidth() / 2 ), point.y - ( GameImage.EXPLOSION_120X120.getHeight() / 2 ),
                false, ControlPattern.explosionBombedLandscape));
        
    }
}
