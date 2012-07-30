/*
 * Self.java
 *
 * Created on 22 December 2007, 12:37
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
import nz.co.abrahams.asithappens.*;
import java.awt.Image;
import java.awt.Color;
import java.awt.Point;
import javax.imageio.ImageIO;
import java.io.File;

/**
 *
 * @author mark
 */
public class Self {
    
    public static final double SELF_INITIAL_POSITION_WIDTH = 0.02;
    
    public static final double SELF_INITIAL_POSITION_HEIGHT = 0.4;
    
    public static final int LASER_OFFSET_X = 50;
    
    public static final int LASER_OFFSET_Y = 13;
    
    public static final int LASER_Y_RANDOMIZATION = 7;
    
    public static final int MOVEMENT_PERIOD = 5;
    
    public static final double PARTICLE_VELOCITY_INCREMENT = 0.1;
    
    public static final double VELOCITY_INITIAL = 0.24;
    
    public static final double VELOCITY_INCREMENT = 0.02;
    
    public static final double VELOCITY_MAXIMUM = 0.36;
    
    public static final double VELOCITY_INCREMENT_DROPS = 1;
    
    public static final double VELOCITY_INDICATOR_FLOOR = 0.2;
    
    public static final int ELEVATION_INITIAL = 0;
    
    public static final int ELEVATION_INCREMENT = 2;
    
    public static final int ELEVATION_MAXIMUM = 12;
    
    public static final int ELEVATION_INCREMENT_DROPS = 1;
    
    public static final int ELEVATION_INDICATOR_FLOOR = -1;
    
    public static final int INVINCIPILLS_INITIAL = 0;
    
    public static final int INVINCIPILLS_MAXIMUM = 8;
    
    public static final int MAXIMUM_EXPLOSION_PARTICLE = 8;
    
    public static final double MAXIMUM_PARTICLE_VELOCITY = 0.3;
    
    public static final int LASER_INITIAL_COUNT = 1;
    
    public static final int LASER_MAXIMUM_COUNT = 3;
    
    public static final int LASER_MAXIMUM_ACTIVE = 25;
    
    public static final int LASER_INITIAL_POWER_LEVEL = 1;
    
    public static final int LASER_MAXIMUM_POWER_LEVEL = 7;
    
    public static final int LASER_DAMAGE_ENEMY_PRIMARY_INITIAL = 20;
    
    public static final int LASER_DAMAGE_ENEMY_SECONDARY_INITIAL = 12;
    
    public static final int LASER_DAMAGE_ENEMY_MAXIMUM = 40;
    
    public static final int LASER_DAMAGE_ENEMY_INCREMENT = 4;
    
    public static final int LASER_DAMAGE_INCREMENT_DROPS = 1;
    
    public static final int LASER_DAMAGE_INDICATOR_FLOOR = 4;
    
    public static final int LASER_DAMAGE_LANDSCAPE_PRIMARY_INITIAL = 12;
    
    public static final int LASER_DAMAGE_LANDSCAPE_SECONDARY_INITIAL = 4;
    
    public static final int LASER_DAMAGE_LANDSCAPE_MAXIMUM = 32;
    
    public static final int LASER_DAMAGE_LANDSCAPE_INCREMENT = 4;
    
    public static final int LASER_MINIMUM_GAP = 100;
    
    //public static final int LASER_DECIMATION_INCREMENT = 2;
    
    //public static final int LASER_INITIAL_DECIMATION_LENGTH = 7;
    
    //public static final int LASER_MAXIMUM_DECIMATION_LENGTH = 50;
    
    //public static final double LASER_INITIAL_INITIAL_X_VELOCITY = 0.5;
    
    //public static final double LASER_INITIAL_TERMINAL_X_VELOCITY = 0.6;
    
    public static final double LASER_INITIAL_INITIAL_X_VELOCITY = 0.5;
    
    public static final double LASER_INITIAL_TERMINAL_X_VELOCITY = 8.0;
    
    public static final double LASER_INITIAL_TERMINAL_Y_VELOCITY = 0.1;
    
    public static final double LASER_INITIAL_START_INITIAL_X_ACCELERATION = 0.001;
    
    public static final double LASER_INITIAL_END_INITIAL_ACCELERATION = 0.005;
    
    public static final double LASER_INITIAL_Y_VELOCITY = 0.4;
    
    public static final double LASER_INITIAL_Y_ACCELERATION = 0.001;
    
    //public static final int[] LASER_ENEMY_DAMAGES = { 6, 8, 12, 16, 20, 24, 28, 32, 34, 36, 38, 40 };
    
    //public static final int[] LASER_ENEMY_DAMAGES = { 8, 12, 16, 20, 24, 28, 32, 34, 36, 38, 40 };
    
    //public static final int[] LASER_LANDSCAPE_DECIMATIONS = { 7, 13, 19, 25, 31, 37, 43 };
    
    //public static final int[] LASER_LANDSCAPE_DECIMATIONS = { 6, 8, 10, 12, 15, 18, 21, 24, 27, 30, 33 };
    
    public static final int SELF_COLLISION_DAMAGE = 200;
    
    public static final int BOMB_INITIAL_COUNT = 5;
    
    public static final int BOMB_INITIAL_DAMAGE = 300;
    
    public static final int BOMB_MAXIMUM_COUNT = 1000;
    
    public static final int BOMB_LANDSCAPE_DECIMATION = 120;
    
    public static final int[] CD_YAHTZEE_MAXIMUMS = { 6, 5, 4, 3, 2 };
    
    
    protected GameContext gameContext;
    
    protected TimeSeriesContext graphContext;
    
    protected int x;
    
    protected int y;
    
    protected boolean movingLeft;
    
    protected boolean movingRight;
    
    protected boolean movingUp;
    
    protected boolean movingDown;
    
    protected double velocity;
    
    protected int elevation;
    
    protected int invincipills;
    
    protected long previousUpdateTime;
    
    protected static GameImage selfImage;
    
    protected double particleVelocity;
    
    protected boolean invincible;
    
    protected boolean finished;
    
    protected int afterDestructionCounter;
    
    public int laserCount;
    
    public int laserDamageEnemy;
    
    public int[] lasersDamageEnemy;
    
    public int laserDamageLandscape;
    
    public int laserPowerLevel;
    
    public double laserInitialXVelocity;
    
    public double laserTerminalXVelocity;
    
    public double laserTerminalYVelocity;
    
    public double laserStartInitialXAcceleration;
    
    public double laserEndInitialXAcceleration;
    
    protected int bombCount;
    
    protected int bombDamage;
    
    protected boolean[] routerRainbow;
    
    protected int[] cdYahtzee;
    
    /** Creates a new instance of Self */
    public Self(GameContext gameContext, TimeSeriesContext graphContext) {
        this.gameContext = gameContext;
        this.graphContext = graphContext;
        movingLeft = false;
        movingRight = false;
        movingUp = false;
        movingDown = false;
        velocity = VELOCITY_INITIAL;
        elevation = ELEVATION_INITIAL;
        invincipills = INVINCIPILLS_INITIAL;
        previousUpdateTime = System.currentTimeMillis();
        particleVelocity = PARTICLE_VELOCITY_INCREMENT;
        invincible = true;
        finished = false;
        afterDestructionCounter = 0;
        
        // Firing capabilities
        laserCount = LASER_INITIAL_COUNT;
        laserDamageEnemy = LASER_DAMAGE_ENEMY_PRIMARY_INITIAL;
        //laserDamageLandscape = LASER_DAMAGE_LANDSCAPE_PRIMARY_INITIAL;
        laserPowerLevel = LASER_INITIAL_POWER_LEVEL;
        laserInitialXVelocity = LASER_INITIAL_INITIAL_X_VELOCITY;
        laserTerminalXVelocity = LASER_INITIAL_TERMINAL_X_VELOCITY;
        laserTerminalYVelocity = LASER_INITIAL_TERMINAL_Y_VELOCITY;
        laserStartInitialXAcceleration = LASER_INITIAL_START_INITIAL_X_ACCELERATION;
        laserEndInitialXAcceleration = LASER_INITIAL_END_INITIAL_ACCELERATION;
        bombCount = BOMB_INITIAL_COUNT;
        bombDamage = BOMB_INITIAL_DAMAGE;
        
        lasersDamageEnemy = new int[LASER_MAXIMUM_COUNT];
        for ( int i = 0 ; i < LASER_MAXIMUM_COUNT ; i++ ) {
            if ( i == 0 )
                lasersDamageEnemy[i] = LASER_DAMAGE_ENEMY_PRIMARY_INITIAL;
            else
                lasersDamageEnemy[i] = LASER_DAMAGE_ENEMY_SECONDARY_INITIAL;
        }
        
        routerRainbow = new boolean[Powerup.Type.values().length];
        clearRouterRainbow();
        cdYahtzee = new int[InstallCD.Type.values().length];
        clearCdYahtzee();
        
        selfImage = GameImage.SELF;
        
        initializePosition();
    }
    
    public void initializePosition() {
        x = (int)(gameContext.getPanelWidth() * SELF_INITIAL_POSITION_WIDTH);
        y = (int)(gameContext.getPanelHeight() * SELF_INITIAL_POSITION_HEIGHT);
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    public void startMovingLeft() {
        movingLeft = true;
    }
    
    public void startMovingRight() {
        movingRight = true;
    }
    
    public void startMovingUp() {
        movingUp = true;
    }
    
    public void startMovingDown() {
        movingDown = true;
    }
    
    public void stopMovingLeft() {
        movingLeft = false;
    }
    
    public void stopMovingRight() {
        movingRight = false;
    }
    
    public void stopMovingUp() {
        movingUp = false;
    }
    
    public void stopMovingDown() {
        movingDown = false;
    }
    
    public void move() {
        long newUpdateTime;
        int elapsedTime;
        
        newUpdateTime = System.currentTimeMillis();
        elapsedTime = (int)(newUpdateTime - previousUpdateTime);
        
        
        if ( movingLeft && ! movingRight )
            x -= velocity * elapsedTime;
        else if ( movingRight && ! movingLeft )
            x += velocity * elapsedTime;
        
        if ( movingUp && ! movingDown )
            y -= velocity * elapsedTime;
        else if ( movingDown && ! movingUp )
            y += velocity * elapsedTime;
        
        if ( x < 0 ) {
            movingLeft = false;
            x = 0;
        } else if ( x > graphContext.getPanelWidth() - selfImage.getWidth() ) {
            movingRight = false;
            x = graphContext.getPanelWidth() - selfImage.getWidth();
        }
        
        if ( y < 0 ) {
            movingUp = false;
            y = 0;
        } else if ( y > graphContext.getPanelHeight() - selfImage.getHeight() ) {
            movingDown = false;
            y = graphContext.getPanelHeight() - selfImage.getHeight();
        }
        
        
        previousUpdateTime = newUpdateTime;
        
    }
    
    public void detectEnemyCollisions() {
        Enemy collidingEnemy;
        
        collidingEnemy = null;
        
        // Find first enemy that laser would collide with
        for ( int i = 0 ; i < gameContext.getEnemies().size() ; i++ ) {
            int enemyXOrigin;
            int enemyYOrigin;
            int enemyWidth;
            int enemyHeight;
            Enemy nextEnemy;
            
            nextEnemy = gameContext.getEnemies().elementAt(i);
            enemyXOrigin = (int)(nextEnemy.getX());
            enemyYOrigin = (int)(nextEnemy.getY());
            enemyWidth = nextEnemy.getGameImage().getWidth();
            enemyHeight = nextEnemy.getGameImage().getHeight();
            
            // First pass - quick rectangle bounding collision detection
            if ( x < enemyXOrigin + enemyWidth && x + selfImage.getWidth() > enemyXOrigin &&
                    y < enemyYOrigin + enemyHeight && y + selfImage.getHeight() > enemyYOrigin ) {
                
                // Second pass - slower horizontal pixel profile matching
                int selfXOffset;
                int enemyXOffset;
                int xOverlapWidth;
                int selfYOffset;
                int enemyYOffset;
                int yOverlapHeight;
                
                selfXOffset = Math.max(enemyXOrigin - x, 0);
                enemyXOffset = Math.max(x - enemyXOrigin, 0);
                xOverlapWidth = Math.min(x + selfImage.getWidth(), enemyXOrigin + enemyWidth) -
                        Math.max(x, enemyXOrigin);
                selfYOffset = Math.max(enemyYOrigin - y, 0);
                enemyYOffset = Math.max(y - enemyYOrigin, 0);
                yOverlapHeight = Math.min(y + selfImage.getHeight(), enemyYOrigin + enemyHeight) -
                        Math.max(y, enemyYOrigin);
                
                if ( selfImage.collisionOverlap(nextEnemy.getGameImage(), selfXOffset, selfYOffset, enemyXOffset, enemyYOffset, xOverlapWidth, yOverlapHeight))
                    collidingEnemy = nextEnemy;
            }
        }
        
        if ( collidingEnemy != null && ! finished && ! invincible ) {
            collidingEnemy.destroy(SELF_COLLISION_DAMAGE, new Point(x, y));
            destroy();
        }
    }
    
    public void detectCollectableCollisions() {
        CollectableObject collidingCollectable;
        
        collidingCollectable = null;
        
        // Find first powerup that ship would collide with
        for ( int i = 0 ; i < gameContext.getCollectables().size() ; i++ ) {
            int powerupXOrigin;
            int powerupYOrigin;
            int powerupWidth;
            int powerupHeight;
            CollectableObject nextCollectable;
            
            nextCollectable = gameContext.getCollectables().elementAt(i);
            powerupXOrigin = (int)(nextCollectable.getX());
            powerupYOrigin = (int)(nextCollectable.getY());
            powerupWidth = nextCollectable.getGameImage().getWidth();
            powerupHeight = nextCollectable.getGameImage().getHeight();
            
            // First pass - quick rectangle bounding collision detection
            if ( x < powerupXOrigin + powerupWidth && x + selfImage.getWidth() > powerupXOrigin &&
                    y < powerupYOrigin + powerupHeight && y + selfImage.getHeight() > powerupYOrigin && ! finished ) {
                nextCollectable.collected();
                //gameContext.getSoundThread().addSound(Sounds.POWERUP_COLLECTION);
            }
        }
        
    }
    
    public void detectLandscapeCollisions() {
        boolean collided = false;
        
        if ( ! finished && ! invincible ) {
            for ( int xPixel = 0 ; xPixel < selfImage.getWidth() ; xPixel++ ) {
                if ( gameContext.getLandscape().isLandscapeAt(x + xPixel, y + selfImage.getTopOffset(xPixel)) ) {
                    synchronized(graphContext.getData()) {
                        synchronized(graphContext.getSummarySemaphore()) {
                            gameContext.getLandscape().decimateDiameter(x + xPixel, y + selfImage.getTopOffset(xPixel), selfImage.getHeight());
                            //destroy();
                            collided = true;
                        }
                    }
                }
                if ( gameContext.getLandscape().isLandscapeAt(x + xPixel, y + selfImage.getBottomOffset(xPixel)) ) {
                    synchronized(graphContext.getData()) {
                        synchronized(graphContext.getSummarySemaphore()) {
                            gameContext.getLandscape().decimateDiameter(x + xPixel, y + selfImage.getBottomOffset(xPixel), selfImage.getHeight());
                            //destroy();
                            collided = true;
                        }
                    }
                }
            }
        }
        if ( collided )
            destroy();
    }
    
    
    public void destroy() {
        int totalMass;
        int explodedMass;
        
        totalMass = selfImage.getWidth() * selfImage.getHeight();
        explodedMass = 0;
        
        while ( explodedMass < totalMass ) {
            int particleSize;
            ExplosionParticle particle;
            
            particleSize = (int)(Math.random() * MAXIMUM_EXPLOSION_PARTICLE + 1);
            particle = new ExplosionParticle(gameContext, graphContext,
                    (int)(x + Math.random() * selfImage.getWidth()),
                    (int)(y + Math.random() * selfImage.getHeight()),
                    (Math.random() - 0.5) * MAXIMUM_PARTICLE_VELOCITY,
                    (Math.random() - 0.5) * MAXIMUM_PARTICLE_VELOCITY );
            //logger.debug("Adding explosion particle: " + particle);
            gameContext.addExplosionParticle(particle);
            explodedMass += particleSize;
        }
        gameContext.getSoundThread().addSound(Sounds.EXPLOSION_SELF);
        finished = true;
        gameContext.setSelfState(GameContext.SELF_DESTROYED);
        
        // Reset accessories
        bombCount = BOMB_INITIAL_COUNT;
        bombDamage = BOMB_INITIAL_DAMAGE;
        invincipills = INVINCIPILLS_INITIAL;
        
        // Decrease laser damage and count
        for ( int i = 0 ; i < laserCount ; i++ ) {
            lasersDamageEnemy[i] -= LASER_DAMAGE_INCREMENT_DROPS * LASER_DAMAGE_ENEMY_INCREMENT;
            if ( i == 0 ) {
                lasersDamageEnemy[i] = Math.max(lasersDamageEnemy[i], LASER_DAMAGE_ENEMY_PRIMARY_INITIAL);
            }
            else {
                //lasersDamageEnemy[i] = Math.max(lasersDamageEnemy[i], LASER_DAMAGE_ENEMY_SECONDARY_INITIAL);
                if ( lasersDamageEnemy[i] < LASER_DAMAGE_ENEMY_SECONDARY_INITIAL ) {
                    laserCount--;
                    lasersDamageEnemy[i] = LASER_DAMAGE_ENEMY_SECONDARY_INITIAL;
                }
            }
        }
        
        /*
        laserDamageEnemy = Math.max(laserDamageEnemy - ( LASER_DAMAGE_INCREMENT_DROPS * LASER_DAMAGE_ENEMY_INCREMENT * laserCount ), LASER_DAMAGE_ENEMY_PRIMARY_INITIAL);
        laserDamageLandscape = Math.max(laserDamageLandscape - ( LASER_DAMAGE_INCREMENT_DROPS * LASER_DAMAGE_LANDSCAPE_INCREMENT * laserCount ), LASER_DAMAGE_LANDSCAPE_INITIAL);
        if ( laserCount > 1 )
            laserCount--;
        laserDamageEnemy = Math.min(laserDamageEnemy, LASER_DAMAGE_ENEMY_MAXIMUM * laserCount);
        laserDamageLandscape = Math.min(laserDamageLandscape, LASER_DAMAGE_LANDSCAPE_MAXIMUM * laserCount);
         */
        
        // Decrease speed
        velocity = Math.max(velocity - ( VELOCITY_INCREMENT_DROPS * VELOCITY_INCREMENT ), VELOCITY_INITIAL);
        // Decrease elevation
        elevation = Math.max(elevation - ( ELEVATION_INCREMENT_DROPS * ELEVATION_INCREMENT ), ELEVATION_INITIAL);
        
        clearRouterRainbow();
        //clearCdYahtzee();
    }
    
    public GameImage getGameImage() {
        return selfImage;
    }
    
    public Image getImage() {
        return selfImage.getImage();
    }
    
    public void fireLaser() {
        Laser newLaser;
        double endInitialXAcceleration;
        double startInitialXAcceleration;
        double initialYVelocity;
        double initialYAcceleration;
        double terminalYVelocity;
        int landscapeDamage;
        
        //if ( ! finished && gameContext.getLasers().size() < LASER_MAXIMUM_ACTIVE ) {
        if ( ! finished && gameContext.getLasers().size() < LASER_MAXIMUM_ACTIVE &&
                ( gameContext.getLasers().size() == 0 || gameContext.getLasers().lastElement().getX() > x + LASER_MINIMUM_GAP ) ) {
            for ( int i = 0 ; i < laserCount ; i++ ) {
                if ( i == 0 ) {
                    endInitialXAcceleration = laserEndInitialXAcceleration;
                    startInitialXAcceleration = laserStartInitialXAcceleration;
                    initialYVelocity = 0;
                    initialYAcceleration = 0;
                    terminalYVelocity = 0;
                } else if ( i % 2 == 1 ) {
                    endInitialXAcceleration = laserEndInitialXAcceleration - ( 0.0001 * (int)((i + 2) / 2));
                    startInitialXAcceleration = laserStartInitialXAcceleration - ( 0.0001 * (int)((i + 2) / 2));
                    initialYVelocity = (int)((i + 2) / 2) * LASER_INITIAL_Y_VELOCITY;
                    initialYAcceleration = - (int)((i + 2) / 2) * LASER_INITIAL_Y_ACCELERATION;
                    terminalYVelocity = laserTerminalYVelocity;
                } else {
                    endInitialXAcceleration = laserEndInitialXAcceleration - ( 0.0001 * (int)(i / 2) );
                    startInitialXAcceleration = laserStartInitialXAcceleration - ( 0.0001 * (int)(i / 2) );
                    initialYVelocity = - (int)(i / 2) * LASER_INITIAL_Y_VELOCITY;
                    initialYAcceleration = (int)(i / 2) * LASER_INITIAL_Y_ACCELERATION;
                    terminalYVelocity = - laserTerminalYVelocity;
                }
                landscapeDamage = (int)(LASER_DAMAGE_LANDSCAPE_SECONDARY_INITIAL + (
                        ( (double)(lasersDamageEnemy[i]) - LASER_DAMAGE_ENEMY_SECONDARY_INITIAL ) /
                        ( LASER_DAMAGE_ENEMY_MAXIMUM - LASER_DAMAGE_ENEMY_SECONDARY_INITIAL ) *
                        ( LASER_DAMAGE_LANDSCAPE_MAXIMUM - LASER_DAMAGE_LANDSCAPE_SECONDARY_INITIAL ) ));
                newLaser = new Laser(gameContext, graphContext, lasersDamageEnemy[i], landscapeDamage,
                        x + LASER_OFFSET_X, (int)(y + LASER_OFFSET_Y + LASER_Y_RANDOMIZATION * (Math.random() - 0.5)),
                        laserInitialXVelocity, initialYVelocity, laserTerminalXVelocity, terminalYVelocity,
                        startInitialXAcceleration, endInitialXAcceleration, initialYAcceleration);
                gameContext.addLaser(newLaser);
            }
            gameContext.getSoundThread().addSound(Sounds.FIRE_LASER);
            //System.out.println("Firing laser: laserCount=" + laserCount + ",laserDamageEnemy=" + laserDamageEnemy + ",laserDamageLandscape=" + laserDamageLandscape);
        }
    }
    
    public void fireBomb() {
        if ( ! finished && bombCount != 0 ) {
            gameContext.addBomb(new Bomb(gameContext, graphContext, bombDamage, x, y + selfImage.getHeight(), 0, 0));
            bombCount--;
            gameContext.getSoundThread().addSound(Sounds.FIRE_BOMB);
        }
    }
    
    public void takeInvincipill() {
        if ( ! finished && ! invincible && invincipills != 0 ) {
            invincible = true;
            gameContext.setSelfState(GameContext.SELF_INVINCIBLE);
            invincipills--;
            gameContext.getSoundThread().addSound(Sounds.INVINCIPILL);
        }
    }
    
    /*
    public int getLaserLandscapeDecimation(int laserNumber) {
        return LASER_LANDSCAPE_DECIMATIONS[laserPowerLevel - 1] * ( 1 - ( (int)(( laserNumber + 1 ) / 2) / (LASER_MAXIMUM_COUNT + 1) ) );
    }
     
    public int getLaserEnemyDamage(int laserNumber) {
        return LASER_ENEMY_DAMAGES[laserPowerLevel - 1] * ( 1 - ( laserNumber / (LASER_MAXIMUM_COUNT + 1) ) );
    }
     */
    
    public boolean isAtCapacity(Powerup.Type type) {
        switch ( type ) {
            case DAMAGE:
                return laserDamageEnemy >= LASER_DAMAGE_ENEMY_MAXIMUM;
            case SPEED:
                return velocity >= VELOCITY_MAXIMUM;
            case ELEVATION:
                return elevation >= ELEVATION_MAXIMUM;
            case LASER:
                return laserCount >= LASER_MAXIMUM_COUNT;
            case BOMBS:
                return bombCount >= BOMB_MAXIMUM_COUNT;
            case INVINCIBILITY:
                return invincipills >= this.INVINCIPILLS_MAXIMUM;
            default:
                return false;
        }
    }
    
    public void clearRouterRainbow() {
        for ( int i = 0 ; i < routerRainbow.length ; i++ )
            routerRainbow[i] = false;
    }
    
    public boolean getRouterRainbow(int index) {
        return routerRainbow[index];
    }
    
    public boolean setRouterRainbow(int index) {
        boolean rainbow;
        
        routerRainbow[index] = true;
        rainbow = true;
        for ( int i = 0 ; i < routerRainbow.length ; i++ ) {
            if ( ! routerRainbow[i] )
                rainbow = false;
        }
        return rainbow;
    }
    
    public void clearCdYahtzee() {
        for ( int i = 0 ; i < cdYahtzee.length ; i++ )
            cdYahtzee[i] = 0;
    }
    
    public boolean increaseCdYahtzee(InstallCD.Type type) {
        boolean yahtzee;
        
        cdYahtzee[type.index] = Math.min(cdYahtzee[type.index] + 1, type.numberForYahtzee);
        yahtzee = true;
        for ( InstallCD.Type checkType : InstallCD.Type.values() ) {
            if ( cdYahtzee[checkType.index] < checkType.numberForYahtzee )
                yahtzee = false;
        }
        return yahtzee;
    }
    
    public int getCdYahtzeeCount(int index) {
        return cdYahtzee[index];
    }
    
    public void increaseLaserPower() {
        laserPowerLevel = Math.min(laserPowerLevel + 1, LASER_MAXIMUM_POWER_LEVEL);
        for ( int i = 0 ; i < laserCount ; i++ ) {
            lasersDamageEnemy[i] = Math.min(lasersDamageEnemy[i] + LASER_DAMAGE_ENEMY_INCREMENT, LASER_DAMAGE_ENEMY_MAXIMUM);
        }
        //laserDamageEnemy = Math.min(laserDamageEnemy + LASER_DAMAGE_ENEMY_INCREMENT, LASER_DAMAGE_ENEMY_MAXIMUM * laserCount);
        //laserDamageLandscape = Math.min(laserDamageLandscape + LASER_DAMAGE_LANDSCAPE_INCREMENT, LASER_DAMAGE_LANDSCAPE_MAXIMUM * laserCount);
    }
    
    public void increaseLaserCount() {
        laserCount = Math.min(laserCount + 1, LASER_MAXIMUM_COUNT);
    }
    
    public void increaseBombCount(int increase) {
        bombCount += increase;
    }
    
    public void increaseElevation() {
        elevation = Math.min(elevation + ELEVATION_INCREMENT, ELEVATION_MAXIMUM);
    }
    
    public void increaseVelocity() {
        velocity = Math.min(velocity + VELOCITY_INCREMENT, VELOCITY_MAXIMUM);
    }
    
    public void increaseInvincipills() {
        invincipills = Math.min(invincipills + 1, INVINCIPILLS_MAXIMUM);
    }
    
    public int getLaserCount() {
        return laserCount;
    }
    
    public int getLaserDamageEnemy() {
        return laserDamageEnemy;
    }
    
    public int getLasersSummedDamageEnemy() {
        int sum;
        
        sum = 0;
        for ( int i = 0 ; i < laserCount ; i++ ) {
            sum += lasersDamageEnemy[i];
        }
        return sum;
    }
    
    public int getBombLandscapeDecimation() {
        return BOMB_LANDSCAPE_DECIMATION;
    }
    
    public int getBombCount() {
        return bombCount;
    }
    
    public double getVelocity() {
        return velocity;
    }
    
    public int getElevation() {
        return elevation;
    }
    
    public int getInvincipillCount() {
        return invincipills;
    }
    
    public double getParticleVelocity() {
        return particleVelocity;
    }
    
    public void setInvincible(boolean flag) {
        invincible = flag;
    }
    
    public boolean isInvincible() {
        return invincible;
    }
    
    public void setFinished(boolean flag) {
        finished = flag;
    }
    
    public boolean isFinished() {
        return finished;
    }
    
    public BackgroundImage getBackgroundImage() {
        return new BackgroundImage(gameContext, graphContext,
            GameImage.SELF, x, y, false, ControlPattern.selfVictoryExit);
    }
}
