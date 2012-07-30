/*
 * GameContext.java
 *
 * Created on 22 December 2007, 16:21
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

import nz.co.abrahams.asithappens.core.Configuration;
import nz.co.abrahams.asithappens.cartgraph.TimeSeriesContext;
import nz.co.abrahams.asithappens.uiutil.SetDisplay;
import nz.co.abrahams.asithappens.*;
import org.apache.log4j.Logger;
import java.util.Vector;

/**
 *
 * @author mark
 */
public class GameContext implements Runnable {
    
    /** Location of game images */
    public static final String IMAGE_DIRECTORY = "images/sdn";
    
    /** Initial game window width */
    public static final int DEFAULT_WINDOW_WIDTH = 750;
    
    /** Initial game window width */
    public static final int DEFAULT_WINDOW_HEIGHT = 750;
    
    /** Time in milliseconds to wait between each main loop iteration */
    public static final int LOOP_PERIOD = 40;
    
    /** Gravity constant in pixels per millisecond per millisecond */
    // Testing for fragments - "moonlike"
    //public static final double GRAVITY = 0.0002;
    // Realistic
    public static final double GRAVITY = 0.0004;
    //public static final double GRAVITY = 0.0005;
    
    /** Time in milliseconds to wait after self has been destroyed */
    public static final int AFTER_DESTRUCTION_WAIT = 5000;
    
    public static final int TRANSITION_NONE = 0;
    
    public static final int TRANSITION_START = 1;
    
    public static final int TRANSITION_BOSS = 2;
    
    public static final int TRANSITION_END = 3;
    
    //public static final int TRANSITION_DESTROYED = 4;
    
    public static final int TRANSITION_GAMEOVER = 4;
    
    public static final int TRANSITION_VICTORY = 5;
    
    public static final int TRANSITION_VICTORY_EXIT = 6;
    
    public static final int TRANSITION_TIMER_START = 5000;
    
    public static final int TRANSITION_TIMER_BOSS = 4000;
    
    public static final int TRANSITION_TIMER_END = 5000;
    
    //public static final int TRANSITION_TIMER_DESTROYED = 3000;
    
    public static final int TRANSITION_TIMER_GAMEOVER = 5000;
    
    public static final int TRANSITION_TIMER_VICTORY = 15000;
    
    public static final int TRANSITION_TIMER_VICTORY_EXIT = 7000;
    
    public static final int VICTORY_STARBOMB_COUNT = 450;
    
    public static final int SELF_NORMAL = 0;
    
    public static final int SELF_DESTROYED = 1;
    
    public static final int SELF_INVINCIBLE = 2;
    
    public static final int SELF_TIMER_DESTROYED = 2000;
    
    public static final int SELF_TIMER_INVINCIBLE = 3000;
    
    public static final int EXTRA_LIFE_INITIAL = 200000;
    
    public static final int EXTRA_LIFE_INCREMENT = 100000;
    
    public static final int INITIAL_LIVES = 5;
    
    public static final int INITIAL_LEVEL = 1;
    
    public static final int INITIAL_SCORE = 0;
    
    
    protected Logger logger;
    
    protected TimeSeriesContext graphContext;
    
    /** Graph type before SDN was started */
    protected SetDisplay.Positioning originalSetPositioning;
    
    /** Object to control repainting of game panel every iteration of main loop */
    protected Object paintMonitor;
    
    /** Thread to handle sound effects */
    protected SoundThread soundThread;
    
    /** Landscape generated from graph */
    protected Landscape landscape;
    
    /** User-controlled ship */
    protected Self self;
    
    /** Current set of firing produced by self */
    protected Vector<Laser> lasers;
    
    /** Current set of active bombs */
    protected Vector<Bomb> bombs;
    
    /** Active explosion particles */
    protected Vector<ExplosionParticle> particles;
    
    /** Enemy types */
    protected EnemyType[] enemyType;
    
    /** Active enemies */
    //protected ActiveEnemies enemies;
    protected Vector<Enemy> enemies;
    
    /** Active spawners */
    //protected ActiveSpawners activeSpawners;
    protected Vector<AttackWaveSpawner> activeSpawners;
    
    /** Active powerups */
    protected Vector<CollectableObject> collectables;
    
    /** Active background images */
    protected Vector<BackgroundImage> backgroundImages;
    
    /** Current game level */
    protected int level;
    
    /** Transition state timer */
    protected int transitionTimer;
    
    /** Game transition state */
    protected int transitionState;
    
    /** Self state timer */
    protected int selfTimer;
    
    /** Self state */
    protected int selfState;
    
    /** Extra life threshold */
    protected int extraLifeThreshold;
    
    /** Game attack pattern generator */
    protected LevelAttackSpawner levelSpawner;
    
    /** Number of lives left */
    protected int lives;
    
    /** Current score */
    protected long score;
    
    /** Laser fire generated by self */
    //protected Vector<Laser> lasers;
    
    /** Thread running main game loop */
    protected Thread gameThread;
    
    /** Switch that indicates whether sound effects are turned on */
    protected boolean soundOn;
    
    /** Switch that indicates whether the game has been started */
    protected boolean gameOn;
    
    /** Period of main loop in milliseconds */
    protected int loopPeriod;
    
    /** Creates a new instance of GameContext */
    public GameContext(TimeSeriesContext graphContext) {
        logger = Logger.getLogger(this.getClass().getName());
        this.graphContext = graphContext;
        paintMonitor = new Object();
        landscape = new Landscape(this, graphContext);
        //Sounds.setSoundOn(false);
        gameOn = false;
        self = new Self(this, graphContext);
        lasers = new Vector();
        bombs = new Vector();
        particles = new Vector();
        enemies = new Vector();
        collectables = new Vector();
        backgroundImages = new Vector();
        activeSpawners = new Vector();
        transitionState = TRANSITION_START;
        transitionTimer = TRANSITION_TIMER_START;
        setSelfState(SELF_INVINCIBLE);
        levelSpawner = new LevelAttackSpawner(this, graphContext);
        extraLifeThreshold = EXTRA_LIFE_INITIAL;
        lives = INITIAL_LIVES;
        level = INITIAL_LEVEL;
        score = INITIAL_SCORE;
        loopPeriod = LOOP_PERIOD;
        if ( Configuration.getPropertyInt("sdn.sound.enabled") == 1 )
            soundOn = true;
        else
            soundOn = false;
        
        enemyType = new EnemyType[EnemyType.NUMBER_OF_TYPES];
        for ( int type = 0 ; type < EnemyType.NUMBER_OF_TYPES ; type++ ) {
            enemyType[type] = new EnemyType(type);
        }
        
    }
    
    public void startGame() {
        double initialGraphTop;
        
        gameOn = true;
        originalSetPositioning = graphContext.getSetsPositioning();
        graphContext.setSetsPositioning(SetDisplay.Positioning.Stacked);
        graphContext.setYRescaleMinimum(Configuration.getPropertyInt("sdn.axis.y.rescale.threshold.minimum"));
        graphContext.setYRescaleMaximum(Configuration.getPropertyInt("sdn.axis.y.rescale.threshold.maximum"));
        graphContext.setYRescalePercentage(Configuration.getPropertyInt("sdn.axis.y.rescale.percentage"));
        //initialGraphTop = Math.max(graphContext.getSummaryData().findMaximumSummedValue() * ( 100 + graphContext.getYRescalePercentage() ) / 100, TimeSeriesContext.MIN_GRAPH_VALUE);
        initialGraphTop = Math.max(graphContext.getSummaryData().findMaximumSummedValue() * 100 / graphContext.getYRescalePercentage(), TimeSeriesContext.MIN_GRAPH_VALUE);
        //initialGraphTop = Math.max(graphContext.getSummaryData().findMaximumSummedValue() * 2, TimeSeriesContext.MIN_GRAPH_VALUE);
        graphContext.setTargetGraphTop(initialGraphTop);
        graphContext.setAutoGraphTop(initialGraphTop);
        graphContext.setGradualGraphTopChange(true);
        graphContext.setHorizontalGridLines(false);
        graphContext.setVerticalGridLines(false);
        gameThread = new Thread(this, "SDN-main");
        gameThread.start();
        soundThread = new SoundThread(this);
        soundThread.start();
        
        soundThread.addSound(Sounds.TRANSITION_START);
    }
    
    
    public void run() {
        long timeAdjustment;
        long sleepTime;
        
        logger.debug("Starting self defending network");
        timeAdjustment = 0;
        
        while ( gameOn ) {
            
            // Calculate time adjustment
            sleepTime = loopPeriod - timeAdjustment;
            if ( sleepTime < 0 ) {
                //logger.warn("Negative sleep time (" + sleepTime + ") due to elapsed time exceeding main loop period (" + loopPeriod + ")");
            } else {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    logger.error("Interrupted sleep");
                }
            }
            timeAdjustment = System.currentTimeMillis();
            
            // Move self
            self.move();
            
            // Move laser fire
            update(lasers);
            
            // Update bombs
            update(bombs);
            
            // Rescale graph top if necessary
            landscape.gradualGraphTopChange();
            
            // Move landscape fragments
            landscape.moveFragmentColumns();
            
            // Move explosion particles
            update(particles);
            
            // Check game attack pattern for new attack waves
            if ( levelSpawner.generationCheck() )
                levelCompleted();
            
            // Check active spawners for new enemies
            update(activeSpawners);
            
            // Move enemies
            update(enemies);
            
            // Move collectable objects
            update(collectables);
            
            // Move background images
            update(backgroundImages);
            
            // Detect laser fire colliding with landscape or enemies
            detectFiringCollision(lasers);
            
            // Detect bombs colliding with landscape or enemies
            detectBombFiringCollision(bombs);
            
            // Detect self colliding with landscape
            self.detectLandscapeCollisions();
            
            // Detect self colliding with enemies
            self.detectEnemyCollisions();
            
            // Detect self colliding with powerups
            self.detectCollectableCollisions();
            
            // Paint updates to graph panel
            synchronized(paintMonitor) {
                paintMonitor.notifyAll();
            }
            
            if ( transitionState != TRANSITION_NONE ) {
                transitionTimer -= loopPeriod;
                if ( transitionTimer < 0 ) {
                    switch ( transitionState ) {
                        case TRANSITION_START:
                            transitionState = TRANSITION_NONE;
                            levelSpawner.setLevelPattern(level);
                            break;
                        case TRANSITION_BOSS:
                            transitionState = TRANSITION_NONE;
                            break;
                        case TRANSITION_END:
                            if ( level < LevelAttackPattern.NUMBER_OF_LEVELS ) {
                                setTransitionState(TRANSITION_START);
                                level++;
                                soundThread.addSound(Sounds.TRANSITION_START);
                            } else {
                                self.setInvincible(true);
                                setSelfState(SELF_INVINCIBLE);
                                selfTimer = TRANSITION_TIMER_VICTORY + TRANSITION_TIMER_VICTORY_EXIT;
                                createFireworks();
                                setTransitionState(TRANSITION_VICTORY);
                            }
                            break;
                        case TRANSITION_GAMEOVER:
                            endGame();
                            break;
                        case TRANSITION_VICTORY:
                            self.setFinished(true);
                            addBackgroundImage(self.getBackgroundImage());
                            setTransitionState(TRANSITION_VICTORY_EXIT);
                            break;
                        case TRANSITION_VICTORY_EXIT:
                            endGame();
                            break;
                    }
                }
            }
            
            if ( selfState != SELF_NORMAL ) {
                selfTimer -= loopPeriod;
                if ( selfTimer < 0 ) {
                    switch ( selfState ) {
                        case SELF_DESTROYED:
                            lives--;
                            if ( lives > 0 ) {
                                //self = new Self(this, graphContext);
                                self.initializePosition();
                                self.setFinished(false);
                                self.setInvincible(true);
                                setSelfState(SELF_INVINCIBLE);
                            } else {
                                setSelfState(SELF_NORMAL);
                                setTransitionState(TRANSITION_GAMEOVER);
                                soundThread.addSound(Sounds.TRANSITION_GAMEOVER);
                            }
                            break;
                        case SELF_INVINCIBLE:
                            self.setInvincible(false);
                            setSelfState(SELF_NORMAL);
                            break;
                    }
                    
                }
            }
            
            timeAdjustment = System.currentTimeMillis() - timeAdjustment;
            // logger.debug("Elapsed Time: " + Long.toString(timeAdjustment));
        }
        
    }
    
    public void update(Vector vector) {
        synchronized(vector) {
            for ( int i = 0 ; i < vector.size() ; i++ ) {
                ((Vector<UpdateableObject>)vector).elementAt(i).update();
                if ( ((Vector<UpdateableObject>)vector).elementAt(i).isFinished() ) {
                    vector.removeElementAt(i);
                    i--;
                }
            }
        }
    }
    
    public void detectFiringCollision(Vector vector) {
        synchronized(vector) {
            for ( int i = 0 ; i < vector.size() ; i++ ) {
                ((Vector<Laser>)vector).elementAt(i).detectCollision();
            }
        }
    }
    
    public void detectBombFiringCollision(Vector vector) {
        synchronized(vector) {
            for ( int i = 0 ; i < vector.size() ; i++ ) {
                ((Vector<Bomb>)vector).elementAt(i).detectCollision();
            }
        }
    }
    
    public void levelCompleted() {
        
    }
    
    public void addLaser(Laser laser) {
        lasers.add(laser);
    }
    
    public void addExplosionParticle(ExplosionParticle particle) {
        synchronized(particles) {
            particles.add(particle);
        }
    }
    
    public int getPanelWidth() {
        return graphContext.getPanelWidth();
    }
    
    public int getPanelHeight() {
        return graphContext.getPanelHeight();
    }
    
    public Object getPaintMonitor() {
        return paintMonitor;
    }
    
    public Self getSelf() {
        return self;
    }
    
    public Vector<Laser> getLasers() {
        return lasers;
    }
    
    public void addBomb(Bomb bomb) {
        bombs.add(bomb);
    }
    
    public Vector<Bomb> getBombs() {
        return bombs;
    }
    
    public int getBombCount() {
        return self.getBombCount();
    }
    
    public int getInvincipillCount() {
        return self.getInvincipillCount();
    }
    
    public void graphDataHasChanged() {
        landscape.graphDataHasChanged();
    }
    
    public void updateGraphTopTarget() {
        graphContext.setYRescaleMinimum(Configuration.getPropertyInt("sdn.axis.y.rescale.threshold.minimum") - self.getElevation());
        graphContext.setYRescaleMaximum(Configuration.getPropertyInt("sdn.axis.y.rescale.threshold.maximum") - self.getElevation());
        graphContext.setYRescalePercentage(Configuration.getPropertyInt("sdn.axis.y.rescale.percentage") - self.getElevation());
        graphContext.setTargetGraphTop(Math.max(graphContext.getSummaryData().findMaximumSummedValue() * 100 / graphContext.getYRescaleMinimum(), TimeSeriesContext.MIN_GRAPH_VALUE));
    }
    
    public Landscape getLandscape() {
        return landscape;
    }
    
    public Vector<ExplosionParticle> getExplosionParticles() {
        return particles;
    }
    
    public EnemyType getEnemyType(int type) {
        return enemyType[type];
    }
    
    public void addEnemy(Enemy enemy) {
        enemies.add(enemy);
    }
    
    public Vector<Enemy> getEnemies() {
        return enemies;
    }
    
    public void addCollectable(CollectableObject collectable) {
        collectables.add(collectable);
    }
    
    public Vector<CollectableObject> getCollectables() {
        return collectables;
    }
    
    public void addBackgroundImage(BackgroundImage backgroundImage) {
        backgroundImages.add(backgroundImage);
    }
    
    public Vector<BackgroundImage> getBackgroundImages() {
        return backgroundImages;
    }
    
    public void addSpawner(AttackWaveSpawner spawner) {
        activeSpawners.add(spawner);
    }
    
    public Vector<AttackWaveSpawner> getSpawners() {
        return activeSpawners;
    }
    
    public LevelAttackSpawner getGameAttackPattern() {
        return levelSpawner;
    }
    
    public long getScore() {
        return score;
    }
    
    public void increaseScore(int points) {
        score += points;
        if ( score >= extraLifeThreshold ) {
            lives++;
            extraLifeThreshold += EXTRA_LIFE_INCREMENT;
            backgroundImages.add(new BackgroundImage(this, graphContext, GameImage.WORDS_BONUS_LIFE, -100, 100, false, ControlPattern.wordsBonusLife));
            soundThread.addSound(Sounds.BONUS_LIFE);
        }
    }
    
    public int getLevel() {
        return level;
    }
    
    public String getLevelDescription() {
        return LevelAttackPattern.levels[level].getDescription();
    }
    
    public String getLevelBossName() {
        return LevelAttackPattern.levels[level].getBossName();
    }
    
    public int getLivesCount() {
        return lives;
    }
    
    public int getTransitionState() {
        return transitionState;
    }
    
    public void setTransitionState(int state) {
        
        if ( transitionState == TRANSITION_GAMEOVER )
            return;
        
        transitionState = state;
        switch ( state ) {
            case TRANSITION_START:
                transitionTimer = TRANSITION_TIMER_START;
                break;
            case TRANSITION_BOSS:
                transitionTimer = TRANSITION_TIMER_BOSS;
                break;
            case TRANSITION_END:
                transitionTimer = TRANSITION_TIMER_END;
                break;
            case TRANSITION_GAMEOVER:
                transitionTimer = TRANSITION_TIMER_GAMEOVER;
                break;
            case TRANSITION_VICTORY:
                transitionTimer = TRANSITION_TIMER_VICTORY;
                break;
            case TRANSITION_VICTORY_EXIT:
                transitionTimer = TRANSITION_TIMER_VICTORY_EXIT;
                break;
        }
    }
    
    public void setSelfState(int state) {
        selfState = state;
        switch ( state ) {
            case SELF_NORMAL:
                selfTimer = 0;
                break;
            case SELF_DESTROYED:
                selfTimer = SELF_TIMER_DESTROYED;
                break;
            case SELF_INVINCIBLE:
                selfTimer = SELF_TIMER_INVINCIBLE;
                break;
        }
        
    }
    
    public int getTransitionTimer() {
        return transitionTimer;
    }
    
    public int getSelfTimer() {
        return selfTimer;
    }
    
    public void toggleSoundEffects() {
        soundOn = ! soundOn;
        //soundThread.setSoundState(soundOn);
    }
    
    public SoundThread getSoundThread() {
        return soundThread;
    }
    
    public boolean isSoundOn() {
        return soundOn;
    }
    
    public boolean isGameOn() {
        return gameOn;
    }
    
    public void endGame() {
        gameOn = false;
        graphContext.setSetsPositioning(originalSetPositioning);
        graphContext.setGradualGraphTopChange(false);
        graphContext.setUseFixedGraphTop(false);
        graphContext.setYRescaleMinimum(Configuration.getPropertyInt("graph.axis.y.rescale.threshold.minimum"));
        graphContext.setYRescaleMaximum(Configuration.getPropertyInt("graph.axis.y.rescale.threshold.maximum"));
        graphContext.setYRescalePercentage(Configuration.getPropertyInt("graph.axis.y.rescale.percentage"));
        if ( soundThread != null )
            soundThread.endThread();
    }
    
    public void createFireworks() {
        for ( int i = 0 ; i < VICTORY_STARBOMB_COUNT ; i++ ) {
            addExplosionParticle(
                    new ExplosionParticle(this, graphContext, ExplosionParticleShape.FIREWORKS_PLUS,
                    ExplosionParticleShape.FIREWORKS_COLORS[(int)(Math.random() * ExplosionParticleShape.FIREWORKS_COLORS.length)],
                    ControlPattern.fireworksStarBombSmall)
                    );
            
            addExplosionParticle(
                    new ExplosionParticle(this, graphContext, ExplosionParticleShape.FIREWORKS_PLUS,
                    ExplosionParticleShape.FIREWORKS_COLORS[(int)(Math.random() * ExplosionParticleShape.FIREWORKS_COLORS.length)],
                    ControlPattern.fireworksStarBombLarge)
                    );
            
        }
        soundThread.addSound(Sounds.TRANSITION_VICTORY);
    }
    
    public void testMethod() {
        Powerup powerup;
        
        /*
        powerup = new Powerup(this, graphContext, Powerup.Type.DAMAGE, graphContext.getPanelWidth(), graphContext.getPanelHeight() * 0, -0.2, 0, null);
        collectables.add(powerup);
        powerup = new Powerup(this, graphContext, Powerup.Type.SPEED, graphContext.getPanelWidth(), graphContext.getPanelHeight() * 0.1, -0.2, 0, null);
        collectables.add(powerup);
        powerup = new Powerup(this, graphContext, Powerup.Type.ELEVATION, graphContext.getPanelWidth(), graphContext.getPanelHeight() * 0.2, -0.2, 0, null);
        collectables.add(powerup);
        powerup = new Powerup(this, graphContext, Powerup.Type.LASER, graphContext.getPanelWidth(), graphContext.getPanelHeight() * 0.3, -0.2, 0, null);
        collectables.add(powerup);
        powerup = new Powerup(this, graphContext, Powerup.Type.BOMBS, graphContext.getPanelWidth(), graphContext.getPanelHeight() * 0.4, -0.2, 0, null);
        collectables.add(powerup);
        powerup = new Powerup(this, graphContext, Powerup.Type.INVINCIBILITY, graphContext.getPanelWidth(), graphContext.getPanelHeight() * 0.5, -0.2, 0, null);
        collectables.add(powerup);
        powerup = new Powerup(this, graphContext, Powerup.Type.NULL, graphContext.getPanelWidth(), graphContext.getPanelHeight() * 0.6, -0.2, 0, null);
        collectables.add(powerup);
        */
        
    }
}
