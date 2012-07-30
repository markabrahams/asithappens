/*
 * Landscape.java
 *
 * Created on 24 December 2007, 15:16
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
import java.util.Vector;
import java.util.Iterator;
import java.awt.Point;
import org.apache.log4j.Logger;

/**
 *
 * @author mark
 */
public class Landscape {
    
    public static final double FIXED_VELOCITY = 0.5;
    
    public static final int RANDOM_DECIMATION_OFFSET = 6;
    
    //public static final int RESCALE_PERIOD = 100;
    public static final int RESCALE_PERIOD = 50;
    
    public static final int CD_PROBABILITY = 1000;
    
    public static final double CD_X_VELOCITY_MINIMUM = 0.02;
    
    public static final double CD_X_VELOCITY_RANDOM = 0.18;
    
    public static final double CD_Y_VELOCITY_MINIMUM = 0.3;
    
    public static final double CD_Y_VELOCITY_RANDOM = 0.3;
    
    protected Logger logger;
    
    protected GameContext gameContext;
    
    protected TimeSeriesContext graphContext;
    
    //protected SummaryData summary;
    
    protected int summaryOffset;
    
    protected Vector<FragmentedColumn> fragmentedColumns;
    
    protected long rescalingPreviousUpdateTime;
    
    protected int rescalingCounter;
    
    /** Creates a new instance of Landscape */
    public Landscape(GameContext gameContext, TimeSeriesContext graphContext) {
        logger = Logger.getLogger(this.getClass().getName());
        this.gameContext = gameContext;
        this.graphContext = graphContext;
        fragmentedColumns = new Vector();
        //summary = null;
        summaryOffset = graphContext.getPanelWidth();
        rescalingPreviousUpdateTime = System.currentTimeMillis();
        rescalingCounter = 0;
    }
    
    public void graphDataHasChanged() {
        //summary = newSummary;
        if ( graphContext.getSummaryData() != null ) {
            //logger.debug("Panel width: " + graphContext.getPanelWidth() + ", points: " + summary.getNumberOfPoints());
            summaryOffset = Math.max(graphContext.getPanelWidth() - graphContext.getSummaryData().getNumberOfPoints(), 0);
        } else
            summaryOffset = graphContext.getPanelWidth();
        // Maybe build hash table of xPixel locations for fragmented columns
        
    }
    
    public synchronized Point collisionWithDiagonal(int startX, int startY, int endX, int endY) {
        int deltaX;
        int deltaY;
        int x;
        int y;
        int m;
        
        deltaX = endX - startX;
        deltaY = endY - startY;
        
        // Singleton case
        if ( deltaX == 0 && deltaY == 0 ) {
            if ( isLandscapeAt(startX, startY) )
                return new Point(startX, startY);
            else
                return null;
        }
        
        // Flat diagonal case
        if ( Math.abs(deltaX) >= Math.abs(deltaY) ) {
            m = deltaY / deltaX;
            if ( deltaX > 0 ) {
                for ( x = 0 ; x < deltaX ; x++ ) {
                    y = (int)(m * x);
                    //if ( startY + y < graphContext.getPanelHeight() ) {
                    if ( isLandscapeAt(startX + x, startY + y) )
                        return new Point(startX + x, startY + y);
                    //} else
                    //    return null;
                }
            } else {
                for ( x = 0 ; x > deltaX ; x-- ) {
                    y = (int)(m * x);
                    //if ( startY + y < graphContext.getPanelHeight() ) {
                    if ( isLandscapeAt(startX + x, startY + y) )
                        return new Point(startX + x, startY + y);
                    //} else
                    //    return null;
                }
            }
        }
        // Steep diagonal case
        else {
            m = deltaX / deltaY;
            if ( deltaY > 0 ) {
                for ( y = 0 ; y < deltaY ; y++ ) {
                    x = (int)(m * y);
                    //if ( startY + y < graphContext.getPanelHeight() ) {
                    if ( isLandscapeAt(startX + x, startY + y) )
                        return new Point(startX + x, startY + y);
                    //} else
                    //    return null;
                }
            } else {
                for ( y = 0 ; y > deltaY ; y-- ) {
                    x = (int)(m * y);
                    //if ( startY + y >= 0 ) {
                    if ( isLandscapeAt(startX + x, startY + y) )
                        return new Point(startX + x, startY + y);
                    //} else
                    //    return null;
                }
            }
            
        }
        
        return null;
    }
    
    public synchronized boolean isLandscapeAt(int x, int y) {
        Iterator<Fragment> iterator;
        FragmentedColumn column;
        Fragment fragment;
        double yValue;
        
        //synchronized(graphContext.getSummarySemaphore()) {
        
        //logger.debug(summary.toString());
        //logger.debug("x: " + x + ", offset: " + summaryOffset);
        
        // Check for graph data not started at x pixel
        if ( x < summaryOffset )
            return false;
        
        // Check for y pixel out of bounds
        //if ( y < 0 || y >= graphContext.getPanelHeight() )
        //    return false;
        
        yValue = graphContext.getValueFromYPixel(y);
        column = getFragmentedColumnAt(x);
        
        // Check for unfragmented column landscape
        if ( column == null ) {
            if ( yValue < graphContext.getSummaryData().getSummedValue(x - summaryOffset) ) {
                //logger.debug("Found landscape at: (" + x + ", " + y + ") - yValue=" + yValue + ",dataValue=" + graphContext.getSummaryData().getSummedValue(x - summaryOffset) );
                return true;
            } else
                return false;
        }
        
        return column.isLandscapeAt(yValue);
        
        // Check for fragmented column landscape
            /*
            iterator = column.iterator();
            while ( iterator.hasNext() ) {
                fragment = iterator.next();
                if ( yValue >= fragment.getPosition() && yValue < fragment.getPosition() + fragment.getLength() )
                    return true;
            }
            return false;
             */
        //}
    }
    
    /** Assumes DataSets and summary have been externally synchronized. */
    public void decimateDiameter(int xCenter, int yCenter, int diameter) {
        double radius;
        int[] startIndicies;
        int destroyedPixels;
        
        startIndicies = new int[graphContext.getSummaryData().getNumberOfSets()];
        for ( int set = 0 ; set < startIndicies.length ; set++ ) {
            startIndicies[set] = -1;
        }
        
        destroyedPixels = 0;
        radius = diameter / 2;
        for ( int x = (int)(-radius) ; x < (int)radius ; x++ ) {
            if ( x + xCenter >= summaryOffset && x + xCenter < summaryOffset + graphContext.getSummaryData().getNumberOfPoints() )
                destroyedPixels += decimateColumn( x + xCenter, yCenter + (int)( ( Math.random() - 0.5 ) * RANDOM_DECIMATION_OFFSET ), (int)(Math.sqrt( (radius * radius) - (x * x) )) * 2, startIndicies );
        }
        //if ( gameContext.isSoundOn() )
        //    Sounds.EXPLOSION_LANDSCAPE.play();
        //logger.debug("Destroyed pixels:" + destroyedPixels);
        if ( Math.random() * CD_PROBABILITY < destroyedPixels )
            createCD(xCenter, yCenter);
        
        gameContext.getSoundThread().addSound(Sounds.EXPLOSION_LANDSCAPE);
    }
    
    public synchronized int decimateColumn(int x, int y, int length, int[] startIndicies) {
        double totalYValueForColumn;
        FragmentedColumn column;
        Fragment fragment;
        long fragmentTime;
        FragmentedColumnPosition startOfDecimation;
        FragmentedColumnPosition endOfDecimation;
        int destroyedPixels;
        
        //synchronized(graphContext.getSummarySemaphore()) {
        
        totalYValueForColumn = graphContext.getSummaryData().getSummedValue(x - summaryOffset);
        column = getFragmentedColumnAt(x);
        
        // Create single-fragment column if it is currently unfragmented
        if ( column == null ) {
            fragmentTime = (long)(graphContext.getTimeFromXPixel(x)) + (long)(graphContext.getSummaryData().getPixelTime() / 2);
            fragment = new Fragment(graphContext, fragmentTime, 0, totalYValueForColumn, 0);
            //logger.debug("Creating first fragment: " + fragment);
            column = new FragmentedColumn(gameContext, graphContext, fragmentTime);
            column.add(fragment);
            fragmentedColumns.add(column);
        } else {
            fragmentTime = column.getTime();
        }
        destroyedPixels = column.explode(x, summaryOffset, (int)(y - 0.5 + ( length / 2 )), (int)(y - 0.5 - ( length / 2 )), gameContext.getSelf().getParticleVelocity());
        column.decimateAt(x, summaryOffset, (int)(y - 0.5 + ( length / 2 )), (int)(y - 0.5 - ( length / 2 )), startIndicies);
        
        return destroyedPixels;
        //}
    }
    
    public synchronized FragmentedColumn getFragmentedColumnAt(int xPixel) {
        double startTime;
        double endTime;
        long columnTime;
        
        startTime = graphContext.getTimeFromXPixel(xPixel);
        endTime = graphContext.getTimeFromXPixel(xPixel + 1);
        for ( int i = 0 ; i < fragmentedColumns.size() ; i++ ) {
            // Fix for next line: ArrayIndexOutOfBounds 0 >= 0 or 3 >= 3 etc
            columnTime = fragmentedColumns.elementAt(i).getTime();
            if ( columnTime >= startTime && columnTime < endTime ) {
                return fragmentedColumns.elementAt(i);
            }
        }
        return null;
    }
    
    public synchronized void moveFragmentColumns() {
        // Fix: add check/consolidation for multiple fragmentColumns occupying the same x pixel
        for ( int i = 0 ; i < fragmentedColumns.size() ; i++ ) {
            fragmentedColumns.elementAt(i).moveFragments();
            if ( ! fragmentedColumns.elementAt(i).isFragmented() )
                fragmentedColumns.removeElementAt(i);
        }
    }
    
    public synchronized void gradualGraphTopChange() {
        if ( ! graphContext.getUseFixedGraphTop() && graphContext.getAutoGraphTop() != graphContext.getTargetGraphTop() ) {
            long newUpdateTime;
            double delta;
            
            newUpdateTime = System.currentTimeMillis();
            rescalingCounter += (int)(newUpdateTime - rescalingPreviousUpdateTime);
            rescalingPreviousUpdateTime = newUpdateTime;
            
            if ( rescalingCounter > RESCALE_PERIOD ) {
                rescalingCounter -= RESCALE_PERIOD;
                delta = graphContext.getGraphTop() / graphContext.getPanelHeight();
                if ( graphContext.getAutoGraphTop() < graphContext.getTargetGraphTop() ) {
                    graphContext.setAutoGraphTop(Math.min(graphContext.getAutoGraphTop() + delta, graphContext.getTargetGraphTop() ) );
                } else {
                    graphContext.setAutoGraphTop(Math.max(graphContext.getAutoGraphTop() - delta, graphContext.getTargetGraphTop() ) );
                }
            }
        }
    }
    
    public void createCD(int cdX, int cdY) {
        double random;
        double inverseSum;
        double threshold;
        boolean found;
        InstallCD cd;
        InstallCD.Type newCDType;
        
        random = Math.random();
        threshold = 0;
        newCDType = InstallCD.Type.AIX;
        found = false;
        
        for ( InstallCD.Type type : InstallCD.Type.values() ) {
            threshold += type.probability;
            if ( ! found && random <= threshold) {
                newCDType = type;
                found = true;
            }
        }
        
        cd = new InstallCD(gameContext, graphContext, newCDType, cdX, cdY,
                - CD_X_VELOCITY_MINIMUM - Math.random() * CD_X_VELOCITY_RANDOM,
                - CD_Y_VELOCITY_MINIMUM - Math.random() * CD_Y_VELOCITY_RANDOM);
        gameContext.addCollectable(cd);
    }
}
