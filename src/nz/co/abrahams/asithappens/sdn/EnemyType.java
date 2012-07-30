/*
 * EnemyType.java
 *
 * Created on 22 January 2008, 04:09
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
public class EnemyType {
    
    public static final int NUMBER_OF_TYPES = 29;
    
    public static final int USER_UFO = 0;
    
    public static final int USER_EGG = 1;
    
    public static final int PROCUREMENT_DAMAGE = 2;
    
    public static final int FIRING_CIRCLE = 3;
    
    public static final int DEVELOPER_BIRD = 4;
    
    public static final int BOSS_SUPERVISOR = 5;
    
    public static final int BOSS_TEAM_LEADER = 6;
    
    public static final int BOSS_NETWORKS_MANAGER = 7;
    
    public static final int BOSS_OPERATIONS_MANAGER = 8;
    
    public static final int BOSS_IT_EXECUTIVE = 9;
    
    public static final int BOSS_CIO = 10;
    
    public static final int BOSS_CEO = 11;
    
    public static final int BOSS_CHAIRMAN = 12;
    
    public static final int BOSS_DIRECTOR = 27;
    
    public static final int BOSS_RECEIVER = 28;
    
    public static final int USER_BURGER = 13;
    
    public static final int DEVELOPER_PLANE = 14;
    
    public static final int DEVELOPER_BUBBLE = 15;
    
    public static final int FIRING_MISSILE = 16;
    
    public static final int FIRING_EMAIL = 17;
    
    public static final int FIRING_PHONE = 18;
    
    public static final int FIRING_CHAIR = 19;
    
    public static final int ARCHITECT = 20;
    
    public static final int PROCUREMENT_SPEED = 21;
    
    public static final int PROCUREMENT_ELEVATION = 22;
    
    public static final int PROCUREMENT_BOMBS = 23;
    
    public static final int PROCUREMENT_LASER = 24;
    
    public static final int PROCUREMENT_INVINCIBILITY = 25;
    
    public static final int PROCUREMENT_NULL = 26;
    
    
    public static final int SHOOTABLE_LOWER_X_BOUND = 1;
    
    public static final int SHOOTABLE_UPPER_X_BOUND = 1;
    
    public static final int SHOOTABLE_LOWER_Y_BOUND = 1;
    
    public static final int SHOOTABLE_UPPER_Y_BOUND = 1;
    
    public static final int BULLET_LOWER_X_BOUND = 0;
    
    public static final int BULLET_UPPER_X_BOUND = 0;
    
    public static final int BULLET_LOWER_Y_BOUND = 0;
    
    public static final int BULLET_UPPER_Y_BOUND = 0;
    
    public static final int MAXIMUM_EXPLOSION_PARTICLE = 5;
    
    public static final double MAXIMUM_PARTICLE_VELOCITY = 0.1;
    
    public static final double TERMINAL_X_VELOCITY = 0.5;
    
    public static final double TERMINAL_Y_VELOCITY = 0.5;
    
    //protected Logger logger;
    
    public int type;
    
    public boolean shootable;
    
    public int vitality;
    
    public int points;
    
    public boolean subjectToGravity;
    
    public double randomFireProbability;
    
    public GameImage gameImage;
    
    public boolean yieldsPowerup;
    
    public Powerup.Type powerupType;
    
    public double lowerXBound;
    
    public double upperXBound;
    
    public double lowerYBound;
    
    public double upperYBound;
    
    public int firingXOffset;
    
    public int firingYOffset;
    
    /** Creates a new instance of EnemyType */
    public EnemyType(int type) {
        this.type = type;
        subjectToGravity = false;
        yieldsPowerup = false;
        
        switch ( type ) {
            case USER_EGG:
                setShootableEnemyBounds();
                vitality = 10;
                points = 200;
                gameImage = GameImage.USER_EGG;
                firingXOffset = 0;
                firingYOffset = gameImage.getHeight() / 2;
                break;
            case USER_UFO:
                setShootableEnemyBounds();
                vitality = 20;
                points = 300;
                gameImage = GameImage.USER_UFO;
                firingXOffset = gameImage.getWidth() / 2;
                firingYOffset = gameImage.getHeight() / 2;
                break;
            case USER_BURGER:
                setShootableEnemyBounds();
                vitality = 28;
                points = 400;
                gameImage = GameImage.USER_BURGER;
                firingXOffset = gameImage.getWidth() / 2;
                firingYOffset = gameImage.getHeight() / 2;
                break;
            case DEVELOPER_BIRD:
                setShootableEnemyBounds();
                vitality = 100;
                points = 1000;
                gameImage = GameImage.DEVELOPER_BIRD;
                firingXOffset = -20;
                firingYOffset = 28;
                break;
            case DEVELOPER_PLANE:
                setShootableEnemyBounds();
                vitality = 150;
                points = 1200;
                gameImage = GameImage.DEVELOPER_PLANE;
                firingXOffset = 0;
                firingYOffset = 30;
                break;
            case DEVELOPER_BUBBLE:
                setShootableEnemyBounds();
                vitality = 300;
                points = 2000;
                gameImage = GameImage.DEVELOPER_BUBBLE;
                firingXOffset = 0;
                firingYOffset = 30;
                break;
            case ARCHITECT:
                setShootableEnemyBounds();
                vitality = 600;
                points = 3000;
                gameImage = GameImage.ARCHITECT;
                firingXOffset = 0;
                firingYOffset = 60;
                break;
            case PROCUREMENT_DAMAGE:
                setShootableEnemyBounds();
                vitality = 200;
                points = 500;
                gameImage = GameImage.PROCUREMENT;
                firingXOffset = gameImage.getWidth() / 2;
                firingYOffset = gameImage.getHeight() / 2;
                yieldsPowerup = true;
                powerupType = Powerup.Type.DAMAGE;
                break;
            case PROCUREMENT_SPEED:
                setShootableEnemyBounds();
                vitality = 200;
                points = 500;
                gameImage = GameImage.PROCUREMENT;
                firingXOffset = gameImage.getWidth() / 2;
                firingYOffset = gameImage.getHeight() / 2;
                yieldsPowerup = true;
                powerupType = Powerup.Type.SPEED;
                break;
            case PROCUREMENT_ELEVATION:
                setShootableEnemyBounds();
                vitality = 200;
                points = 500;
                gameImage = GameImage.PROCUREMENT;
                firingXOffset = gameImage.getWidth() / 2;
                firingYOffset = gameImage.getHeight() / 2;
                yieldsPowerup = true;
                powerupType = Powerup.Type.ELEVATION;
                break;
            case PROCUREMENT_LASER:
                setShootableEnemyBounds();
                vitality = 200;
                points = 500;
                gameImage = GameImage.PROCUREMENT;
                firingXOffset = gameImage.getWidth() / 2;
                firingYOffset = gameImage.getHeight() / 2;
                yieldsPowerup = true;
                powerupType = Powerup.Type.LASER;
                break;
            case PROCUREMENT_BOMBS:
                setShootableEnemyBounds();
                vitality = 200;
                points = 500;
                gameImage = GameImage.PROCUREMENT;
                firingXOffset = gameImage.getWidth() / 2;
                firingYOffset = gameImage.getHeight() / 2;
                yieldsPowerup = true;
                powerupType = Powerup.Type.BOMBS;
                break;
            case PROCUREMENT_INVINCIBILITY:
                setShootableEnemyBounds();
                vitality = 200;
                points = 500;
                gameImage = GameImage.PROCUREMENT;
                firingXOffset = gameImage.getWidth() / 2;
                firingYOffset = gameImage.getHeight() / 2;
                yieldsPowerup = true;
                powerupType = Powerup.Type.INVINCIBILITY;
                break;
            case PROCUREMENT_NULL:
                setShootableEnemyBounds();
                vitality = 200;
                points = 500;
                gameImage = GameImage.PROCUREMENT;
                firingXOffset = gameImage.getWidth() / 2;
                firingYOffset = gameImage.getHeight() / 2;
                yieldsPowerup = true;
                powerupType = Powerup.Type.NULL;
                break;
            case FIRING_CIRCLE:
                setBulletBounds();
                gameImage = GameImage.FIRING_CIRCLE;
                break;
            case FIRING_MISSILE:
                setBulletBounds();
                gameImage = GameImage.FIRING_MISSILE;
                break;
            case FIRING_EMAIL:
                setBulletBounds();
                shootable = true;
                vitality = 20;
                points = 50;
                gameImage = GameImage.FIRING_EMAIL;
                break;
            case FIRING_PHONE:
                setBulletBounds();
                subjectToGravity = true;
                shootable = true;
                vitality = 20;
                points = 100;
                gameImage = GameImage.FIRING_PHONE;
                break;
            case FIRING_CHAIR:
                setBulletBounds();
                subjectToGravity = true;
                shootable = true;
                vitality = 24;
                points = 150;
                gameImage = GameImage.FIRING_CHAIR;
                break;
            case BOSS_SUPERVISOR:
                setShootableEnemyBounds();
                vitality = 1500;
                points = 10000;
                gameImage = GameImage.BOSS_SUPERVISOR;
                firingXOffset = gameImage.getWidth() / 2;
                firingYOffset = gameImage.getHeight() / 2;
                break;
            case BOSS_TEAM_LEADER:
                setShootableEnemyBounds();
                vitality = 3000;
                points = 15000;
                gameImage = GameImage.BOSS_TEAM_LEADER;
                firingXOffset = gameImage.getWidth() / 2;
                firingYOffset = gameImage.getHeight() / 2;
                break;
            case BOSS_NETWORKS_MANAGER:
                setShootableEnemyBounds();
                vitality = 3000;
                points = 20000;
                gameImage = GameImage.BOSS_NETWORKS_MANAGER;
                firingXOffset = gameImage.getWidth() / 2;
                firingYOffset = gameImage.getHeight() / 2;
                break;
            case BOSS_OPERATIONS_MANAGER:
                setShootableEnemyBounds();
                vitality = 4000;
                points = 30000;
                gameImage = GameImage.BOSS_OPERATIONS_MANAGER;
                firingXOffset = gameImage.getWidth() / 2;
                firingYOffset = gameImage.getHeight() / 2;
                break;
            case BOSS_IT_EXECUTIVE:
                setShootableEnemyBounds();
                vitality = 5000;
                points = 40000;
                gameImage = GameImage.BOSS_IT_EXECUTIVE;
                firingXOffset = gameImage.getWidth() / 2;
                firingYOffset = gameImage.getHeight() / 2;
                break;
            case BOSS_CIO:
                setShootableEnemyBounds();
                vitality = 6000;
                points = 50000;
                gameImage = GameImage.BOSS_CIO;
                firingXOffset = -2;
                firingYOffset = 140;
                break;
            case BOSS_CEO:
                setShootableEnemyBounds();
                vitality = 6000;
                points = 60000;
                gameImage = GameImage.BOSS_CEO;
                firingXOffset = gameImage.getWidth() / 2;
                firingYOffset = gameImage.getHeight() / 2;
                break;
            case BOSS_DIRECTOR:
                setShootableEnemyBounds();
                vitality = 2000;
                points = 10000;
                gameImage = GameImage.BOSS_DIRECTOR;
                firingXOffset = gameImage.getWidth() / 2;
                firingYOffset = gameImage.getHeight() / 2;
                break;
            case BOSS_CHAIRMAN:
                setShootableEnemyBounds();
                vitality = 6000;
                points = 40000;
                gameImage = GameImage.BOSS_CHAIRMAN;
                firingXOffset = gameImage.getWidth() / 2;
                firingYOffset = gameImage.getHeight() / 2;
                break;
            case BOSS_RECEIVER:
                setShootableEnemyBounds();
                vitality = 50000;
                points = 100000;
                gameImage = GameImage.BOSS_RECEIVER;
                firingXOffset = gameImage.getWidth() / 2;
                firingYOffset = gameImage.getHeight() / 2;
                break;
        }
    }
    
    protected void setShootableEnemyBounds() {
        shootable = true;
        lowerXBound = SHOOTABLE_LOWER_X_BOUND;
        upperXBound = SHOOTABLE_UPPER_X_BOUND;
        lowerYBound = SHOOTABLE_LOWER_Y_BOUND;
        upperYBound = SHOOTABLE_UPPER_Y_BOUND;
    }
    
    protected void setBulletBounds() {
        shootable = false;
        lowerXBound = BULLET_LOWER_X_BOUND;
        upperXBound = BULLET_UPPER_X_BOUND;
        lowerYBound = BULLET_LOWER_Y_BOUND;
        upperYBound = BULLET_UPPER_Y_BOUND;
    }
}
