/*
 * InstallCD.java
 *
 * Created on 19 April 2008, 11:38
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
import java.awt.Image;

/**
 *
 * @author mark
 */
public class InstallCD extends SpatialObject implements CollectableObject {
    
    public static final int LOWER_X_BOUND = 0;
    
    public static final int UPPER_X_BOUND = 1;
    
    public static final int LOWER_Y_BOUND = 1;
    
    public static final int UPPER_Y_BOUND = 1;
    
    public static final double YAHTZEE_EXIT_VELOCITY = 0.8;
    
    public static final int YAHTZEE_TIME_INTERVAL = 50;
    
    public static final int YAHTZEE_BONUS = 50000;
    
    public enum Type {
        
        AIX(0, GameImage.CD_AIX, 0.32, 3000, 6),
        OSX(1, GameImage.CD_OSX, 0.26, 5000, 5),
        SOLARIS(2, GameImage.CD_SOLARIS, 0.2, 8000, 4),
        BSD(3, GameImage.CD_BSD, 0.14, 12000, 3),
        LINUX(4, GameImage.CD_LINUX, 0.08, 20000, 2);
        
        Type(int index, GameImage gameImage, double probability, int points, int numberForYahtzee) {
            this.index = index;
            this.gameImage = gameImage;
            this.probability = probability;
            this.points = points;
            this.numberForYahtzee = numberForYahtzee;
        }
        
        public int index;
        
        public GameImage gameImage;
        
        public double probability;
        
        public int points;
        
        public int numberForYahtzee;
    };
    
    protected Type type;
    
    /** Creates a new instance of InstallCD */
    public InstallCD(GameContext gameContext, TimeSeriesContext graphContext,
            Type type, double initialX, double initialY,
            double initialXVelocity, double initialYVelocity) {
        super(gameContext, graphContext, type.gameImage, initialX, initialY,
                initialXVelocity, initialYVelocity, Double.NaN, Double.NaN, true,
                LOWER_X_BOUND, UPPER_X_BOUND, LOWER_Y_BOUND, UPPER_Y_BOUND, null);
        this.type = type;
    }
    
    public void collected() {
        
        gameContext.increaseScore(type.points);
        
        if ( gameContext.getSelf().increaseCdYahtzee(type) ) {
            int x;
            int timeOffset;
            ControlPattern exitPattern;
            
            gameContext.getSelf().clearCdYahtzee();
            gameContext.increaseScore(YAHTZEE_BONUS);
            x = gameContext.getPanelWidth();
            timeOffset = 0;
            for ( InstallCD.Type type : InstallCD.Type.values() ) {
                x -= type.gameImage.getWidth();
                for ( int count = type.numberForYahtzee - 1 ; count >= 0 ; count-- ) {
                    exitPattern = new ControlPattern(false);
                    exitPattern.add(ControlVelocity.create(timeOffset, 0, - YAHTZEE_EXIT_VELOCITY));
                    gameContext.addBackgroundImage(new BackgroundImage(gameContext, graphContext, type.gameImage, x, gameContext.getPanelHeight() - SDNDataPanel.CD_YAHTZEE_Y_OFFSET - ( (count + 1) * type.gameImage.getHeight() ), false, exitPattern));
                    gameContext.addBackgroundImage(new BackgroundImage(gameContext, graphContext, GameImage.WORDS_INSTALL_YAHTZEE,
                        0, 0, false, ControlPattern.wordsInstallYahtzee));
                    timeOffset += YAHTZEE_TIME_INTERVAL;
                }
            }
            
        }
        
        gameContext.getSoundThread().addSound(Sounds.COLLECT_CD);
        finished = true;
    }
    
}
