/*
 * SDNDataPanel.java
 *
 * Created on 22 December 2007, 15:36
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

import nz.co.abrahams.asithappens.storage.SummaryData;
import nz.co.abrahams.asithappens.storage.DataLabel;
import nz.co.abrahams.asithappens.cartgraph.TimeSeriesDataPanel;
import nz.co.abrahams.asithappens.cartgraph.TimeSeriesContext;
import nz.co.abrahams.asithappens.uiutil.SetDisplay;
import nz.co.abrahams.asithappens.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import org.apache.log4j.Logger;

/**
 *
 * @author mark
 */
public class SDNDataPanel extends TimeSeriesDataPanel {
    
    public static final int LIVES_GAP = 5;
    
    public static final int LIVES_DISPLAY_MAXIMUM = 8;
    
    public static final int BOMB_GAP = 0;
    
    public static final int BOMB_DISPLAY_MAXIMUM = 17;
    
    public static final int INVINCIPILL_DISPLAY_MAXIMUM = 8;
    
    public static final int INDICATOR_LETTER_X = 5;
    
    public static final int INDICATOR_BOX_X = 20;
    
    public static final int INDICATOR_FILL_X_GAP = 4;
    
    public static final int INDICATOR_FILL_LENGTH = 120;
    
    public static final int INDICATOR_Y_GAP = 10;
    
    public static final int INDICATOR_Y_INITIAL_OFFSET = 26;
    
    public static final int INDICATOR_FILL_Y_GAP = 4;
    
    public static final int INDICATOR_FILL_HEIGHT = 4;
    
    public static final int SCORE_X_OFFSET = 20;
    
    public static final int SCORE_TITLE_Y_OFFSET = 20;
    
    public static final int SCORE_VALUE_Y_OFFSET = 40;
    
    public static final int BOMBS_X_OFFSET = 200;
    
    public static final int BOMBS_Y_OFFSET = 20;
    
    public static final int RAINBOW_ROUTER_GAP = 6;
    
    public static final int CD_YAHTZEE_Y_OFFSET = 40;
    
    //public static final Font DEFAULT_FONT = new Font("Lucida Sans Typewriter", Font.BOLD, 16);
    
    public static final Font DEFAULT_FONT = new Font("Monospaced", Font.BOLD, 16);
    
    public static final Font TRANSITION_DEFAULT_FONT = new Font("SansSerif", Font.BOLD, 36);
    
    public static final Font TRANSITION_START_DESCRIPTION_FONT = new Font("SanSerif", Font.BOLD, 56);
    
    public static final Font TRANSITION_START_BOSS_FONT = new Font("SanSerif", Font.BOLD, 30);
    
    public static final Font TRANSITION_END_DESCRIPTION_FONT = new Font("SanSerif", Font.BOLD, 20);
    
    public static final Font TRANSITION_BOSS_WARNING_FONT = new Font("SanSerif", Font.ITALIC, 72);
    
    public static final Font TRANSITION_BOSS_APPROACHING_FONT = new Font("SanSerif", Font.ITALIC, 36);
    
    public static final Font TRANSITION_GAMEOVER_FONT = new Font("SanSerif", Font.BOLD, 72);
    
    
    public static final int TRANSITION_START_LEVEL_OFFSET = -100;
    
    public static final int TRANSITION_START_DESCRIPTION_OFFSET = 0;
    
    public static final int TRANSITION_START_BOSS_OFFSET = 60;
    
    public static final int TRANSITION_END_LEVEL_OFFSET = -100;
    
    public static final int TRANSITION_END_DESCRIPTION_OFFSET = 0;
    
    public static final int TRANSITION_BOSS_WARNING_OFFSET = -100;
    
    public static final int TRANSITION_BOSS_NAME_OFFSET = 0;
    
    public static final int TRANSITION_BOSS_FLASHING_PERIOD = 1000;
    
    public static final int TRANSITION_VICTORY_OFFSET = -100; 
    
    public static final int SELF_INVINCIBLE_FLASHING_PERIOD = 100;
    
    
    public static final Color SCORE_FOREGROUND = Color.RED;
    
    public static final Color BOMBS_FOREGROUND = Color.BLUE;
    
    public static final Color SHADOW_BACKGROUND = Color.BLACK;
    
    public static final Color[] LASER_SHADES = {
        new Color(1f, 0f, 0f),
        new Color(0.9f, 0.1f, 0.1f),
        new Color(0.8f, 0.2f, 0.2f),
        new Color(0.7f, 0.3f, 0.3f),
        new Color(0.6f, 0.4f, 0.4f),
        new Color(0.5f, 0.5f, 0.5f)
    };
    
    
    public static final int SHADOW_OFFSET = 1;
    
    
    protected GameContext game;
    
    /** Creates a new instance of SDNDataPanel */
    public SDNDataPanel(TimeSeriesContext context, GameContext game) {
        super(context);
        this.game = game;
        setFont(DEFAULT_FONT);
    }
    
    /**
     * Draws the graph.
     *
     * @param g  the graphics object to draw on
     */
    public void paintComponent(Graphics g) {
        int set, point;
        int oldx, oldy, x, y;
        int xOffset;
        int panelWidth, panelHeight;
        //int pixelInterval;
        double graphTop;
        double value, valueSum;
        boolean graphBreak;
        SummaryData summary;
        DataLabel label;
        int labelWidth, labelHeight, labelPadding;
        FragmentedColumn fragmentedColumn;
        int centerX;
        int centerY;
        String textString;
        
        panelWidth = getWidth();
        panelHeight = getHeight();
        if ( panelWidth != context.getPanelWidth() || panelHeight != context.getPanelHeight() ) {
            logger.debug("Adjusting panel width to " + panelWidth + " and panel height to " + panelHeight);
            context.setPanelWidth(panelWidth);
            context.setPanelHeight(panelHeight);
            context.generateSummaryData();
            revalidate();
        }
        
        synchronized(context.getData()) {
            //logger.debug("Painting TimeSeriesDataPanel: panelWidth=" + panelWidth + " panelHeight=" + panelHeight);
            synchronized(context.getSummarySemaphore()) {
                
                synchronized(game.getLandscape()) {
                    
                    summary = context.getSummaryData();
                    if ( summary == null )
                        return;
                    
                    graphTop = context.getGraphTop();
                    xOffset = panelWidth - summary.getNumberOfPoints();
                    g.clearRect(0, 0, panelWidth - 1, panelHeight - 1);
                    
                    // Draw grid markings if behind
                    if ( context.horizontalGridLines() && ! context.linesInFront() ) {
                        drawHorizontalGridLines(g, panelWidth, panelHeight);
                    }
                    if ( context.verticalGridLines() && ! context.linesInFront() ) {
                        drawVerticalGridLines(g, summary, panelWidth, panelHeight);
                    }
                    
                    if ( context.getSetsPositioning() == SetDisplay.Positioning.Grounded ) {
                        // not supported
                    } else if ( context.getSetsPositioning() == SetDisplay.Positioning.Stacked ) {
                        
                        for ( point = 0 ; point < summary.getNumberOfPoints() ; point++ ) {
                            x = xOffset + point;
                            fragmentedColumn = game.getLandscape().getFragmentedColumnAt(x);
                            
                            if ( fragmentedColumn == null || fragmentedColumn.size() == 0 ) {
                                
                                valueSum = 0;
                                for ( set = 0 ; set < summary.getNumberOfSets() ; set++ ) {
                                    g.setColor(context.getSetDisplayColor(set));
                                    value = summary.getValue(set, point);
                                    if ( ! Double.isNaN(value) ) {
                                        oldy = (int)(panelHeight - 1 - valueSum * panelHeight / graphTop);
                                        valueSum += value;
                                        y = (int)(panelHeight - 1 - valueSum * panelHeight / graphTop);
                                        if ( value > 0 )
                                            g.drawLine(x, oldy, x, y);
                                        
                                    }
                                }
                            }
                            // column is fragmented
                            else {
                                int fragmentNumber;
                                double fragmentTop;
                                Fragment fragment;
                                int startY;
                                int endY;
                                double partOfLine;
                                double initialOffset;
                                
                                valueSum = 0;
                                fragmentNumber = 0;
                                fragment = fragmentedColumn.elementAt(fragmentNumber);
                                fragmentTop = fragment.getLength();
                                startY = 0;
                                initialOffset = 0;
                                
                                for ( set = 0 ; set < summary.getNumberOfSets() ; set++ ) {
                                    g.setColor(context.getSetDisplayColor(set));
                                    value = summary.getValue(set, point);
                                    partOfLine = value;
                                    if ( ! Double.isNaN(value) ) {
                                        while ( fragmentTop < initialOffset + partOfLine && fragmentNumber < fragmentedColumn.size() ) {
                                            //startY = (int)(panelHeight - 1 - ( ( fragment.getPosition() + initialOffset ) * panelHeight / graphTop ) );
                                            //endY = (int)(panelHeight - 1 - ( ( fragment.getPosition() + fragment.getLength() ) * panelHeight / graphTop) );
                                            startY = context.getYPixelFromValue(fragment.getPosition() + initialOffset);
                                            endY = context.getYPixelFromValue(fragment.getPosition() + fragment.getLength() );
                                            g.drawLine(x, startY, x, endY);
                                            partOfLine -= (fragment.getLength() - initialOffset);
                                            initialOffset = 0;
                                            fragmentNumber++;
                                            // Fix: Next line sometimes throws an ArrayIndexOutOfBoundsException
                                            if ( fragmentNumber < fragmentedColumn.size() ) {
                                                fragment = fragmentedColumn.elementAt(fragmentNumber);
                                                fragmentTop = fragment.getLength();
                                            }
                                        }
                                        //startY = (int)(panelHeight - 1 - ( ( fragment.getPosition() + initialOffset ) * panelHeight / graphTop ) );
                                        //endY = (int)(panelHeight - 1 - ( ( fragment.getPosition() + initialOffset + partOfLine ) * panelHeight / graphTop) );
                                        startY = context.getYPixelFromValue(fragment.getPosition() + initialOffset);
                                        endY = context.getYPixelFromValue(fragment.getPosition() + initialOffset + partOfLine);
                                        g.drawLine(x, startY, x, endY);
                                        initialOffset += partOfLine;
                                        //oldy = (int)(panelHeight - 1 - valueSum * panelHeight / graphTop);
                                        //valueSum += value;
                                        //y = (int)(panelHeight - 1 - valueSum * panelHeight / graphTop);
                                        //if ( value > 0 || set == 0 )
                                        //    g.drawLine(x, oldy, x, y);
                                        
                                    }
                                }
                            }
                        }
                    }
                    
                    if ( context.horizontalGridLines() && context.linesInFront() ) {
                        drawHorizontalGridLines(g, panelWidth, panelHeight);
                    }
                    if ( context.verticalGridLines() && context.linesInFront() ) {
                        drawVerticalGridLines(g, summary, panelWidth, panelHeight);
                    }
                    
                    // Draw labels
                    if ( context.getShowLabels() ) {
                        g.setColor(Color.BLACK);
                        for ( int i = 0; i < context.getData().getLabels().getLabelCount() ; i++ ) {
                            label = context.getData().getLabels().getLabel(i);
                            x = context.getXPixelFromTime(label.getTime());
                            y = context.getYPixelFromValue(label.getValue());
                            if ( label.getTime() > summary.getOriginTime(panelWidth) && label.getTime() < summary.getLastTime() ) {
                                if ( label.getValue() < context.getGraphTop() ) {
                                    labelWidth = g.getFontMetrics().stringWidth(label.getLabel());
                                    //labelHeight = g.getFontMetrics().getAscent() + g.getFontMetrics().getDescent();
                                    labelHeight = g.getFontMetrics().getAscent();
                                    //labelPadding = g.getFontMetrics().getLeading();
                                    //labelPadding = 4;
                                    g.clearRect(x - labelWidth / 2 - LABEL_PADDING, y - labelHeight - LABEL_PADDING, labelWidth + LABEL_PADDING * 2, labelHeight + LABEL_PADDING * 2 );
                                    g.drawRect(x - labelWidth / 2 - LABEL_PADDING, y - labelHeight - LABEL_PADDING, labelWidth + LABEL_PADDING * 2, labelHeight + LABEL_PADDING * 2 );
                                    g.drawString(label.getLabel(), x - labelWidth / 2, y);
                                }
                            }
                        }
                    }
                }
            }
        }
        
        // Draw lives left
        for ( int i = 0 ; i < Math.min(game.getLivesCount() - 1, LIVES_DISPLAY_MAXIMUM); i++ ) {
            g.drawImage(GameImage.SELF_MINI.getImage(), LIVES_GAP + ( GameImage.SELF_MINI.getWidth() + LIVES_GAP ) * i, LIVES_GAP, null);
        }
        
        // Draw invincipill count
        for ( int i = 0 ; i < Math.min(game.getInvincipillCount(), INVINCIPILL_DISPLAY_MAXIMUM); i++ ) {
            g.drawImage(GameImage.POWERUP_INVINCIBILITY_MINI.getImage(), game.getPanelWidth() - LIVES_GAP - ( GameImage.POWERUP_INVINCIBILITY_MINI.getWidth() + LIVES_GAP ) * ( i + 1 ), LIVES_GAP, null);
        }
        
        // Draw bomb count
        for ( int i = 0 ; i < Math.min(game.getBombCount(), BOMB_DISPLAY_MAXIMUM); i++ ) {
            g.drawImage(GameImage.BOMB_MINI.getImage(), BOMB_GAP + ( GameImage.BOMB_MINI.getWidth() + BOMB_GAP ) * i, context.getPanelHeight() - BOMB_GAP - GameImage.BOMB_MINI.getHeight(), null);
        }
        
        // Draw powerup indicators
        drawIndicator(g, 1, "L", (double)(game.getSelf().getLaserCount()) / game.getSelf().LASER_MAXIMUM_COUNT);
        drawIndicator(g, 2, "E", ( (double)(game.getSelf().getElevation()) - game.getSelf().ELEVATION_INDICATOR_FLOOR ) / ( game.getSelf().ELEVATION_MAXIMUM - game.getSelf().ELEVATION_INDICATOR_FLOOR ) );
        drawIndicator(g, 3, "S", ( game.getSelf().getVelocity() - game.getSelf().VELOCITY_INDICATOR_FLOOR ) / ( game.getSelf().VELOCITY_MAXIMUM - game.getSelf().VELOCITY_INDICATOR_FLOOR ));
        //drawIndicator(g, 4, "P", ( ( (double)(game.getSelf().getLaserDamageEnemy()) / game.getSelf().getLaserCount() ) - game.getSelf().LASER_DAMAGE_INDICATOR_FLOOR ) / ( game.getSelf().LASER_DAMAGE_ENEMY_MAXIMUM - game.getSelf().LASER_DAMAGE_INDICATOR_FLOOR ) );
        drawIndicator(g, 4, "P", ( (double)(game.getSelf().getLasersSummedDamageEnemy()) - ( game.getSelf().LASER_DAMAGE_ENEMY_SECONDARY_INITIAL * game.getSelf().getLaserCount() ) ) / ( ( game.getSelf().LASER_DAMAGE_ENEMY_MAXIMUM - game.getSelf().LASER_DAMAGE_ENEMY_SECONDARY_INITIAL ) * game.getSelf().getLaserCount() ) );
        
        // Draw router rainbow icons
        for ( Powerup.Type type : Powerup.Type.values() ) {
            if ( game.getSelf().getRouterRainbow(type.index) ) {
                g.drawImage(type.icon.getImage(), game.getPanelWidth() - ( ( Powerup.Type.values().length - type.index ) * ( Powerup.Type.DAMAGE.icon.getWidth() + RAINBOW_ROUTER_GAP ) ), game.getPanelHeight() - ( Powerup.Type.DAMAGE.icon.getHeight() + RAINBOW_ROUTER_GAP ), null);
            }
        }
        
        // Draw CD yahtzee icons
        x = game.getPanelWidth();
        for ( InstallCD.Type type : InstallCD.Type.values() ) {
            x -= type.gameImage.getWidth();
            for ( int count = 0 ; count < game.getSelf().getCdYahtzeeCount(type.index) ; count++ ) {
                g.drawImage(type.gameImage.getImage(), x, game.getPanelHeight() - CD_YAHTZEE_Y_OFFSET - ( (count + 1) * type.gameImage.getHeight() ), null );
            }
        }
        
        // Draw score
        centerX = getWidth() / 2;
        textString = "SCORE";
        drawShadowString(g, textString, SCORE_FOREGROUND, SHADOW_BACKGROUND, centerX - ( g.getFontMetrics().stringWidth(textString) / 2 ), SCORE_TITLE_Y_OFFSET, SHADOW_OFFSET, SHADOW_OFFSET);
        textString = Long.toString(game.getScore());
        drawShadowString(g, textString, SCORE_FOREGROUND, SHADOW_BACKGROUND, centerX - ( g.getFontMetrics().stringWidth(textString) / 2 ), SCORE_VALUE_Y_OFFSET, SHADOW_OFFSET, SHADOW_OFFSET);
        
        // Draw ship
        if ( ! game.getSelf().isFinished() ) {
            if ( game.getSelf().isInvincible() ) {
                if ( game.getSelfTimer() % SELF_INVINCIBLE_FLASHING_PERIOD < SELF_INVINCIBLE_FLASHING_PERIOD / 2 )
                    g.drawImage(game.getSelf().getImage(), game.getSelf().getX(), game.getSelf().getY(), null);
            } else
                g.drawImage(game.getSelf().getImage(), game.getSelf().getX(), game.getSelf().getY(), null);
        }
        
        // Draw laser fire
        synchronized(game.getLasers()) {
            for ( int i = 0 ; i < game.getLasers().size() ; i++ ) {
                Laser laser = game.getLasers().elementAt(i);
                int width = Math.max( ( ( laser.getEnemyDamage() - game.getSelf().LASER_DAMAGE_ENEMY_PRIMARY_INITIAL ) / game.getSelf().LASER_DAMAGE_ENEMY_INCREMENT ) * 2 + 1, 1);
                //g.setColor(laser.getColor());
                //g.drawLine(laser.getStartX(), (int)laser.getY(), laser.getEndX(), (int)laser.getY());
                if ( width == 1 ) {
                    int colorIndex = ( game.getSelf().LASER_DAMAGE_ENEMY_PRIMARY_INITIAL - laser.getEnemyDamage() ) / game.getSelf().LASER_DAMAGE_ENEMY_INCREMENT;
                    g.setColor(LASER_SHADES[Math.min(Math.max(colorIndex, 0), LASER_SHADES.length - 1)]);
                    g.drawLine(laser.getStartX(), (int)laser.getY(), laser.getEndX(), (int)laser.getY());
                } else {
                    for ( y = - ( width - 1 ) / 2 ; y <= ( width - 1 ) / 2 ; y++ ) {
                        int xLaserOffset = Math.abs(y * y) + Math.abs(10 * y);
                        g.setColor(LASER_SHADES[Math.abs(y)]);
                        g.drawLine(Math.max(laser.getStartX() - ( xLaserOffset / 3 ), laser.getXCreation() + (int)(Math.abs(laser.getY() - laser.getYCreation()))), (int)(laser.getY() + y), Math.max(laser.getEndX() - xLaserOffset, laser.getXCreation() + (int)(Math.abs(laser.getY() - laser.getYCreation()))), (int)(laser.getY() + y));
                    }
                }
            }
        }
        
        // Draw bombs
        synchronized(game.getBombs()) {
            for ( int i = 0 ; i < game.getBombs().size() ; i++ ) {
                int xPixel;
                int yPixel;
                Bomb bomb;
                
                bomb = game.getBombs().elementAt(i);
                xPixel = (int)(game.getBombs().elementAt(i).getX());
                yPixel = (int)(game.getBombs().elementAt(i).getY());
                g.drawImage(GameImage.BOMB.getImage(), xPixel, yPixel, null);
            }
        }
        
        // Draw explosion particles
        synchronized(game.getExplosionParticles()) {
            //g.setColor(Color.BLACK);
            for ( int i = 0; i < game.getExplosionParticles().size() ; i++ ) {
                int xParticle;
                int yParticle;
                ExplosionParticle particle;
                
                particle = game.getExplosionParticles().elementAt(i);
                xParticle = (int)(particle.getX());
                yParticle = (int)(particle.getY());
                g.setColor(particle.color);
                
                // Customized shape
                g.drawLine(xParticle + particle.getShape().line1x1, yParticle + particle.getShape().line1y1,
                        xParticle + particle.getShape().line1x2, yParticle + particle.getShape().line1y2);
                if ( particle.getShape().twoLines ) {
                    g.drawLine(xParticle + particle.getShape().line2x1, yParticle + particle.getShape().line2y1,
                            xParticle + particle.getShape().line2x2, yParticle + particle.getShape().line2y2);
                }
                
            }
        }
        
        // Draw background images
        synchronized(game.getBackgroundImages()) {
            for ( int i = 0 ; i < game.getBackgroundImages().size() ; i++ ) {
                int xPixel;
                int yPixel;
                
                xPixel = (int)(game.getBackgroundImages().elementAt(i).getX());
                yPixel = (int)(game.getBackgroundImages().elementAt(i).getY());
                
                g.drawImage(game.getBackgroundImages().elementAt(i).getImage(), xPixel, yPixel, null);
            }
        }
        
        // Draw powerups
        synchronized(game.getCollectables()) {
            for ( int i = 0 ; i < game.getCollectables().size() ; i++ ) {
                int xPixel;
                int yPixel;
                
                xPixel = (int)(game.getCollectables().elementAt(i).getX());
                yPixel = (int)(game.getCollectables().elementAt(i).getY());
                
                g.drawImage(game.getCollectables().elementAt(i).getImage(), xPixel, yPixel, null);
            }
        }
        
        // Draw enemies
        synchronized(game.getEnemies()) {
            for ( int i = 0 ; i < game.getEnemies().size() ; i++ ) {
                int xPixel;
                int yPixel;
                
                xPixel = (int)(game.getEnemies().elementAt(i).getX());
                yPixel = (int)(game.getEnemies().elementAt(i).getY());
                
                g.drawImage(game.getEnemies().elementAt(i).getImage(), xPixel, yPixel, null);
            }
        }
        
        //drawShadowString(g, "SCORE: " + game.getScore(), SCORE_FOREGROUND, SHADOW_BACKGROUND, SCORE_X_OFFSET, SCORE_Y_OFFSET, SHADOW_OFFSET, SHADOW_OFFSET);
        //drawShadowString(g, "BOMBS: " + game.getSelf().getBombCount(), BOMBS_FOREGROUND, SHADOW_BACKGROUND, BOMBS_X_OFFSET, BOMBS_Y_OFFSET, SHADOW_OFFSET, SHADOW_OFFSET);
        //drawShadowString(g, "LIVES: " + game.getLivesCount(), BOMBS_FOREGROUND, SHADOW_BACKGROUND, BOMBS_X_OFFSET + 200, BOMBS_Y_OFFSET, SHADOW_OFFSET, SHADOW_OFFSET);
        
        if ( game.getTransitionState() != GameContext.TRANSITION_NONE ) {
            
            //centerX = getWidth() / 2;
            centerY = getHeight() / 2;
            
            switch ( game.getTransitionState() ) {
                case GameContext.TRANSITION_START:
                    textString = "Level " + game.getLevel();
                    g.setFont(TRANSITION_DEFAULT_FONT);
                    drawShadowString(g, textString, Color.RED, Color.BLACK,
                            centerX - ( g.getFontMetrics().stringWidth(textString) / 2 ),
                            centerY + TRANSITION_START_LEVEL_OFFSET, 2, 2);
                    textString = game.getLevelDescription();
                    g.setFont(TRANSITION_START_DESCRIPTION_FONT);
                    drawShadowString(g, textString, Color.RED, Color.BLACK,
                            centerX - ( g.getFontMetrics().stringWidth(textString) / 2 ),
                            centerY + TRANSITION_START_DESCRIPTION_OFFSET, 2, 2);
                    textString = "with the " + game.getLevelBossName();
                    g.setFont(TRANSITION_START_BOSS_FONT);
                    drawShadowString(g, textString, Color.RED, Color.BLACK,
                            centerX - ( g.getFontMetrics().stringWidth(textString) / 2 ),
                            centerY + TRANSITION_START_BOSS_OFFSET, 2, 2);
                    //setFont(DEFAULT_FONT);
                    break;
                case GameContext.TRANSITION_END:
                    textString = "Level " + game.getLevel() + " cleared !";
                    g.setFont(TRANSITION_DEFAULT_FONT);
                    drawShadowString(g, textString, Color.RED, Color.BLACK,
                            centerX - ( g.getFontMetrics().stringWidth(textString) / 2 ), centerY + TRANSITION_END_LEVEL_OFFSET, SHADOW_OFFSET, SHADOW_OFFSET);
                    //textString = game.getLevelDescription();
                    //g.setFont(TRANSITION_START_DESCRIPTION_FONT);
                    //drawShadowString(g, textString, Color.RED, Color.BLACK,
                    //        centerX - ( g.getFontMetrics().stringWidth(textString) / 2 ), centerY + TRANSITION_START_DESCRIPTION_OFFSET, SHADOW_OFFSET, SHADOW_OFFSET);
                    textString = game.getLevelDescription() + " with the " + game.getLevelBossName() + " completed";
                    g.setFont(TRANSITION_END_DESCRIPTION_FONT);
                    drawShadowString(g, textString, Color.RED, Color.BLACK,
                            centerX - ( g.getFontMetrics().stringWidth(textString) / 2 ), centerY + TRANSITION_END_DESCRIPTION_OFFSET, SHADOW_OFFSET, SHADOW_OFFSET);
                    break;
                case GameContext.TRANSITION_BOSS:
                    if ( game.getTransitionTimer() % TRANSITION_BOSS_FLASHING_PERIOD < TRANSITION_BOSS_FLASHING_PERIOD / 2 ) {
                        textString = "Warning !";
                        g.setFont(TRANSITION_BOSS_WARNING_FONT);
                        drawShadowString(g, textString, Color.RED, Color.BLACK,
                                centerX - ( g.getFontMetrics().stringWidth(textString) / 2 ), centerY + TRANSITION_BOSS_WARNING_OFFSET, SHADOW_OFFSET, SHADOW_OFFSET);
                        textString = game.getLevelBossName() + " approaching";
                        g.setFont(TRANSITION_BOSS_APPROACHING_FONT);
                        drawShadowString(g, textString, Color.RED, Color.BLACK,
                                centerX - ( g.getFontMetrics().stringWidth(textString) / 2 ), centerY + TRANSITION_BOSS_NAME_OFFSET, SHADOW_OFFSET, SHADOW_OFFSET);
                    }
                    break;
                case GameContext.TRANSITION_GAMEOVER:
                    textString = "GAME  OVER";
                    g.setFont(TRANSITION_GAMEOVER_FONT);
                    drawShadowString(g, textString, Color.RED, Color.BLACK,
                            centerX - ( g.getFontMetrics().stringWidth(textString) / 2 ), centerY + TRANSITION_BOSS_WARNING_OFFSET, SHADOW_OFFSET, SHADOW_OFFSET);
                    break;
                case GameContext.TRANSITION_VICTORY:
                    textString = "NETWORK";
                    g.setFont(TRANSITION_GAMEOVER_FONT);
                    drawShadowString(g, textString, Color.RED, Color.BLACK,
                            centerX - ( g.getFontMetrics().stringWidth(textString) / 2 ), centerY + TRANSITION_VICTORY_OFFSET, SHADOW_OFFSET, SHADOW_OFFSET);
                    textString = "DEFENDED !";
                    g.setFont(TRANSITION_GAMEOVER_FONT);
                    drawShadowString(g, textString, Color.RED, Color.BLACK,
                            centerX - ( g.getFontMetrics().stringWidth(textString) / 2 ), centerY, SHADOW_OFFSET, SHADOW_OFFSET);
                    break;
                default:
                    break;
            }
        }
    }
    
    protected void drawShadowString(Graphics g, String string, Color foreground, Color background, int x, int y, int shadowOffsetX, int shadowOffsetY) {
        g.setColor(background);
        g.drawString(string, x + shadowOffsetX, y + shadowOffsetY);
        g.setColor(foreground);
        g.drawString(string, x, y);
    }
    
    protected void drawIndicator(Graphics g, int position, String label, double percentage) {
        int topY;
        
        topY = getHeight() - ( INDICATOR_Y_INITIAL_OFFSET + ( g.getFontMetrics().getHeight() + INDICATOR_Y_GAP ) * position );
        drawShadowString(g, label, Color.BLACK, Color.YELLOW, INDICATOR_LETTER_X, topY + g.getFontMetrics().getHeight() - 4, SHADOW_OFFSET, SHADOW_OFFSET);
        g.setColor(Color.YELLOW);
        g.fillRoundRect(INDICATOR_BOX_X, topY, INDICATOR_FILL_X_GAP + INDICATOR_FILL_LENGTH + INDICATOR_FILL_X_GAP, g.getFontMetrics().getHeight(), INDICATOR_FILL_X_GAP, INDICATOR_FILL_Y_GAP);
        g.setColor(Color.BLACK);
        g.drawRoundRect(INDICATOR_BOX_X, topY, INDICATOR_FILL_X_GAP + INDICATOR_FILL_LENGTH + INDICATOR_FILL_X_GAP, g.getFontMetrics().getHeight(), INDICATOR_FILL_X_GAP, INDICATOR_FILL_Y_GAP);
        g.setColor(Color.RED);
        g.fillRect(INDICATOR_BOX_X + INDICATOR_FILL_X_GAP, topY + INDICATOR_FILL_Y_GAP, (int)(INDICATOR_FILL_LENGTH * percentage), g.getFontMetrics().getHeight() - INDICATOR_FILL_Y_GAP - INDICATOR_FILL_Y_GAP);
        /*
        for ( int y = 0; y < g.getFontMetrics().getHeight() - INDICATOR_FILL_Y_GAP - INDICATOR_FILL_Y_GAP ; y++ ) {
            g.drawLine(INDICATOR_BOX_X + INDICATOR_FILL_X_GAP, topY + INDICATOR_FILL_Y_GAP + y, INDICATOR_BOX_X + INDICATOR_FILL_X_GAP + (int)(INDICATOR_FILL_LENGTH * percentage), topY + INDICATOR_FILL_Y_GAP + y);
        }
         */
    }
}
