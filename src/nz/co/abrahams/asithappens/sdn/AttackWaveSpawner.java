/*
 * AttackWaveSpawner.java
 *
 * Created on 10 January 2008, 20:58
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
public class AttackWaveSpawner implements UpdateableObject {
    
    protected GameContext gameContext;
    
    protected TimeSeriesContext graphContext;
    
    protected Logger logger;
    
    /** Offset clock */
    protected long offsetClock;
    
    /** Previous generation check time */
    protected long previousTime;
    
    /** Attack wave pattern to spawn from */
    protected AttackWavePattern wavePattern;
    
    /** x offset applied to the creation of each enemy in fractions of the screen width */
    public double xCreationOffset;
    
    /** y offset applied to the creation of each enemy in fractions of the screen height */
    public double yCreationOffset;
    
    /** Index of enemy to create next */
    protected int enemyIndex;
    
    /** Flag to indicate that attack wave generation period halts the attack clock */
    protected boolean generationHaltsClock;
    
    /** Flag to indicate that attack wave activation period halts the attack clock */
    protected boolean activeHaltsClock;
    
    /** List of enemies created from this spawner */
    Vector<Enemy> spawned;
    
    /** Indicates spawner is finished generating enemies */
    protected boolean finishedGenerating;
    
    /** Indicates spawner is finished and can be removed */
    protected boolean finished;
    
    /** Firing control pattern that overrides firing control pattern for indiviudal enemies */
    protected ControlPattern firingPattern;
    
    /** Creates a new instance of AttackWaveSpawner */
    public AttackWaveSpawner(GameContext gameContext, TimeSeriesContext graphContext, AttackWavePattern wavePattern, ControlPattern firingPattern,
            double xCreationOffset, double yCreationOffset, boolean generationHaltsClock, boolean activeHaltsClock) {
        logger = Logger.getLogger(this.getClass().getName());
        this.gameContext = gameContext;
        this.graphContext = graphContext;
        this.wavePattern = wavePattern;
        this.firingPattern = firingPattern;
        this.xCreationOffset = xCreationOffset;
        this.yCreationOffset = yCreationOffset;
        this.generationHaltsClock = generationHaltsClock;
        this.activeHaltsClock = activeHaltsClock;
        enemyIndex = 0;
        finished = false;
        previousTime = System.currentTimeMillis();
        spawned = new Vector();
        logger.debug("Created attack wave spawner");
    }
    
    public void update() {
        long newTime;
        Enemy newEnemy;
        boolean allFinished;
        ControlPattern newFiringPattern;
        
        newTime = System.currentTimeMillis();
        offsetClock += newTime - previousTime;
        previousTime = newTime;
        
        //logger.debug("enemyIndex: " + enemyIndex + ", size: " + wavePattern.size() + ", timeOffset: " + wavePattern.elementAt(enemyIndex).timeOffset + ", offsetClock: " + offsetClock);
        while ( enemyIndex < wavePattern.size() && wavePattern.elementAt(enemyIndex).timeOffset < offsetClock ) {
            //logger.debug("Adding new enemy: ");
            /*
            newEnemy = new EnemyUser(gameContext, graphContext,
                    graphContext.getTimeGraphOrigin() + wavePattern.elementAt(enemyIndex).initialX * graphContext.getTimeGraphWidth(),
                    wavePattern.elementAt(enemyIndex).initialY * graphContext.getGraphTop(),
                    0, 0, GameImage.USER, wavePattern.elementAt(enemyIndex).movementPattern);
             */
            
            if ( firingPattern == null )
                newFiringPattern = wavePattern.elementAt(enemyIndex).firingPattern;
            else
                newFiringPattern = firingPattern;
            
            newEnemy = new Enemy(gameContext, graphContext,
                    ( wavePattern.elementAt(enemyIndex).initialX + xCreationOffset ) * graphContext.getPanelWidth(),
                    ( wavePattern.elementAt(enemyIndex).initialY + yCreationOffset ) * graphContext.getPanelHeight(),
                    0, 0, wavePattern.elementAt(enemyIndex).enemyType,
                    wavePattern.elementAt(enemyIndex).movementPattern,
                    newFiringPattern);
            
            enemyIndex++;
            gameContext.addEnemy(newEnemy);
            
            if ( enemyIndex == wavePattern.size() ) {
                finishedGenerating = true;
                if ( ! activeHaltsClock )
                    finished = true;
            }
            
            if ( activeHaltsClock ) {
                spawned.add(newEnemy);
            }
        }
        
        if ( activeHaltsClock && finishedGenerating && ! finished ) {
            allFinished = true;
            for ( int i = 0 ; i < spawned.size() ; i++ ) {
                if ( ! spawned.elementAt(i).finished )
                    allFinished = false;
            }
            if ( allFinished )
                finished = true;
        }
    }
    
    public boolean clockHaltCondition() {
        if ( activeHaltsClock )
            return ! finished;
        else if ( generationHaltsClock )
            return ! finishedGenerating;
        else
            return false;
    }
    
    public boolean isFinished() {
        return finished;
    }
}
