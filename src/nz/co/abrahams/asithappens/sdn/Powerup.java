/*
 * Powerup.java
 *
 * Created on 15 January 2008, 22:08
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

//import java.awt.Image;
import nz.co.abrahams.asithappens.cartgraph.TimeSeriesContext;

/**
 *
 * @author mark
 */
public class Powerup extends SpatialObject implements CollectableObject {
    
    public static final int LOWER_X_BOUND = 0;
    
    public static final int UPPER_X_BOUND = 1;
    
    public static final int LOWER_Y_BOUND = 0;
    
    public static final int UPPER_Y_BOUND = 1;
    
    public static final int ROUTER_RAINBOW_BONUS = 50000;
    
    public enum Type {
        DAMAGE(0, GameImage.POWERUP_DAMAGE, GameImage.POWERUP_DAMAGE_MINI, 100),
        SPEED(1, GameImage.POWERUP_SPEED, GameImage.POWERUP_SPEED_MINI, 100),
        ELEVATION(2, GameImage.POWERUP_ELEVATION, GameImage.POWERUP_ELEVATION_MINI, 100),
        LASER(3, GameImage.POWERUP_LASER, GameImage.POWERUP_LASER_MINI, 100),
        BOMBS(4, GameImage.POWERUP_BOMBS, GameImage.POWERUP_BOMBS_MINI, 100),
        INVINCIBILITY(5, GameImage.POWERUP_INVINCIBILITY, GameImage.POWERUP_INVINCIBILITY_MINI, 100),
        NULL(6, GameImage.POWERUP_NULL, GameImage.POWERUP_NULL_MINI, 1000);
        
        public int index;
        
        public GameImage gameImage;
        
        public GameImage icon;
        
        public int score;
        
        Type(int index, GameImage gameImage, GameImage icon, int score) {
            this.index = index;
            this.gameImage = gameImage;
            this.icon = icon;
            this.score = score;
        }
        
        public static Type typeFromIndex(int index) {
            
            if ( index == DAMAGE.index )
                return Powerup.Type.DAMAGE;
            else if ( index == SPEED.index )
                return Powerup.Type.SPEED;
            else if ( index == ELEVATION.index )
                return Powerup.Type.ELEVATION;
            else if ( index == LASER.index )
                return Powerup.Type.LASER;
            else if ( index == BOMBS.index )
                return Powerup.Type.BOMBS;
            else if ( index == INVINCIBILITY.index )
                return Powerup.Type.INVINCIBILITY;
            else
                return Powerup.Type.NULL;
        }
    }
    
    public static final double VELOCITY = 0.1;
    
    public static final double BOMB_INCREASE_MINIMUM = 2;
    
    public static final double BOMB_INCREASE_MAXIMUM = 5;
    
    public static ControlPattern exitPattern;
    
    static {
        exitPattern = new ControlPattern(false);
        exitPattern.add(ControlVelocity.createWithRandom(0, -0.18, -0.45, 0.01, 0.02));
    }
    
    
    protected Type type;
    
    /** Creates a new instance of Powerup */
    public Powerup(GameContext gameContext, TimeSeriesContext graphContext,
            Type type, double initialX, double initialY,
            double initialXVelocity, double initialYVelocity,
            ControlPattern pattern) {
        super(gameContext, graphContext, type.gameImage, initialX, initialY,
                initialXVelocity, initialYVelocity, Double.NaN, Double.NaN, false,
                LOWER_X_BOUND, UPPER_X_BOUND, LOWER_Y_BOUND, UPPER_Y_BOUND, pattern);
        this.type = type;
    }
    
    public void collected() {
        
        switch (type) {
            case DAMAGE:
                gameContext.getSelf().increaseLaserPower();
                break;
            case LASER:
                gameContext.getSelf().increaseLaserCount();
                break;
            case BOMBS:
                gameContext.getSelf().increaseBombCount((int)(Math.random() * ( BOMB_INCREASE_MAXIMUM - BOMB_INCREASE_MINIMUM ) + BOMB_INCREASE_MINIMUM));
                break;
            case SPEED:
                gameContext.getSelf().increaseVelocity();
                break;
            case INVINCIBILITY:
                gameContext.getSelf().increaseInvincipills();
                break;
            case ELEVATION:
                gameContext.getSelf().increaseElevation();
                gameContext.updateGraphTopTarget();
                break;
            case NULL:
                break;
        }
        
        gameContext.increaseScore(type.score);
        if ( gameContext.getSelf().setRouterRainbow(type.index) ) {
            gameContext.getSelf().clearRouterRainbow();
            gameContext.increaseScore(ROUTER_RAINBOW_BONUS);
            for ( Type type : Type.values() ) {
                gameContext.addBackgroundImage(new BackgroundImage(gameContext, graphContext, type.icon,
                        gameContext.getPanelWidth() - ( ( Type.values().length - type.index ) * ( Type.DAMAGE.icon.getWidth() + SDNDataPanel.RAINBOW_ROUTER_GAP ) ),
                        gameContext.getPanelHeight() - ( Powerup.Type.DAMAGE.icon.getHeight() + SDNDataPanel.RAINBOW_ROUTER_GAP),
                        true, exitPattern));
                /*
                gameContext.addBackgroundImage(new BackgroundImage(gameContext, graphContext, GameImage.WORDS_ROUTER_RAINBOW,
                        0, 0, false, ControlPattern.wordsRainbowRouter));
                 */
            }
            gameContext.addBackgroundImage(new BackgroundImage(gameContext, graphContext, GameImage.WORDS_ROUTER_RAINBOW,
                    0, 0, false, ControlPattern.wordsRainbowRouter));
        }
        
        gameContext.getSoundThread().addSound(Sounds.COLLECT_POWERUP);
        finished = true;
    }
    
    public static Type typeFromIndex(int index) {
        if ( index < 1 )
            return Powerup.Type.DAMAGE;
        else if ( index < 2 )
            return Powerup.Type.SPEED;
        else if ( index < 3 )
            return Powerup.Type.ELEVATION;
        else if ( index < 4 )
            return Powerup.Type.LASER;
        else if ( index < 5 )
            return Powerup.Type.BOMBS;
        else if ( index < 6 )
            return Powerup.Type.INVINCIBILITY;
        else
            return Powerup.Type.NULL;
    }
    /*
    public GameImage getGameImage() {
        return type.gameImage;
    }
     
    public Image getImage() {
        return type.gameImage.getImage();
    }
     */
    
}
