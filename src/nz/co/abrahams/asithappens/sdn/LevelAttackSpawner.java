/*
 * LevelAttackSpawner.java
 *
 * Created on 10 January 2008, 19:08
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
import java.util.Vector;
import org.apache.log4j.Logger;


/**
 *
 * @author mark
 */
public class LevelAttackSpawner extends Vector<LevelAttackPrimitive> {
    
    protected GameContext gameContext;
    
    protected TimeSeriesContext graphContext;
    
    protected Logger logger;
    
    /** Master game attack pattern */
    protected LevelAttackPattern attackPattern;
    
    /** Clock in milliseconds since the start of the game */
    protected long attackClock;
    
    /** Time at which previous  check was made */
    protected long previousTime;
    
    /** Flag indicating that the attack clock is currently halred */
    protected boolean clockHalted;
    
    /** Flag indicating that the level has been completed */
    protected boolean finished;
    
    /** Index of next attack pattern to trigger */
    protected int primitiveIndex;
    
    /** Creates a new instance of GameAttackPattern */
    public LevelAttackSpawner(GameContext gameContext, TimeSeriesContext graphContext) {
        logger = Logger.getLogger(this.getClass().getName());
        this.gameContext = gameContext;
        this.graphContext = graphContext;
        
        /*
        attackPattern = LevelAttackPattern.levels[0];
        attackClock = 0;
        previousTime = System.currentTimeMillis();
        finished = false;
        clockHalted = false;
        primitiveIndex = 0;
         */
        attackPattern = LevelAttackPattern.levels[0];
        setLevelPattern(0);
    }
    
    public boolean generationCheck() {
        long newTime;
        
        newTime = System.currentTimeMillis();
        if ( ! clockHalted && attackPattern != null ) {
            attackClock += newTime - previousTime;
            while ( primitiveIndex < attackPattern.size() && attackPattern.elementAt(primitiveIndex).timeOffset < attackClock ) {
                gameContext.addSpawner(new AttackWaveSpawner(gameContext, graphContext,
                        attackPattern.elementAt(primitiveIndex).wavePattern,
                        attackPattern.elementAt(primitiveIndex).firingPattern,
                        attackPattern.elementAt(primitiveIndex).xCreationOffset,
                        attackPattern.elementAt(primitiveIndex).yCreationOffset,
                        attackPattern.elementAt(primitiveIndex).generationHaltsClock,
                        attackPattern.elementAt(primitiveIndex).isBoss) );
                
                if ( attackPattern.elementAt(primitiveIndex).isBoss ) {
                    clockHalted = true;
                    gameContext.setTransitionState(GameContext.TRANSITION_BOSS);
                    gameContext.getSoundThread().addSound(Sounds.TRANSITION_BOSS);
                }
                else if ( attackPattern.elementAt(primitiveIndex).generationHaltsClock ) {
                    clockHalted = true;
                }
                else if ( primitiveIndex == attackPattern.size() - 1 )
                    finishLevel();
                
                primitiveIndex++;
            }
        } else {
            boolean stillHalted;
            
            stillHalted = false;
            for ( int i = 0 ; i < gameContext.getSpawners().size() ; i++ ) {
                if ( gameContext.getSpawners().elementAt(i).clockHaltCondition() )
                    stillHalted = true;
            }
            if ( ! stillHalted) {
                clockHalted = false;
                if ( primitiveIndex == attackPattern.size() ) {
                    finishLevel();
                }
            }
        }
        previousTime = newTime;
        return finished;
    }
    
    public void finishLevel() {
        gameContext.setTransitionState(GameContext.TRANSITION_END);
        finished = true;
        gameContext.getSoundThread().addSound(Sounds.TRANSITION_LEVEL);
        logger.info("Finished level");
    }
    
    public void setLevelPattern(int level) {
        if ( level <= LevelAttackPattern.NUMBER_OF_LEVELS )
            attackPattern = LevelAttackPattern.levels[level];
        else
            attackPattern = null;
        attackClock = 0;
        previousTime = System.currentTimeMillis();
        finished = false;
        clockHalted = false;
        primitiveIndex = 0;
    }
}
