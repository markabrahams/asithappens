/*
 * FragmentedColumn.java
 *
 * Created on 24 December 2007, 15:30
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

import nz.co.abrahams.asithappens.storage.IntegerPair;
import nz.co.abrahams.asithappens.storage.DataSet;
import nz.co.abrahams.asithappens.storage.DataPoint;
import nz.co.abrahams.asithappens.cartgraph.TimeSeriesContext;
import nz.co.abrahams.asithappens.*;
import java.util.*;
import java.awt.Color;
import org.apache.log4j.Logger;

/**
 *
 * @author mark
 */
public class FragmentedColumn extends Vector<Fragment> {
    
    // Extreme testing value
    public static final double VELOCITY = 0.1;
    
    // Testing value
    //public static final double VELOCITY = 0.2;
    
    // Smallest acceptable fragment in pixels
    public static final int SMALLEST_FRAGMENT = 8;
    
    public static final int SMALL_FRAGMENT_ROUNDING_NONE = 0;
    
    public static final int SMALL_FRAGMENT_ROUNDING_DOWN = 1;
    
    public static final int SMALL_FRAGMENT_ROUNDING_UP = 2;
    
    protected Logger logger;
    
    protected GameContext gameContext;
    
    protected TimeSeriesContext graphContext;
    
    protected long time;
    
    protected boolean fragmented;
    
    /** Creates a new instance of Decimation */
    public FragmentedColumn(GameContext gameContext, TimeSeriesContext graphContext, long time) {
        logger = Logger.getLogger(this.getClass().getName());
        this.gameContext = gameContext;
        this.graphContext = graphContext;
        this.time = time;
        fragmented = true;
    }
    
    public synchronized void moveFragments() {
        
        // Move each fragment
        for ( int i = 0 ; i < size() ; i++ ) {
            elementAt(i).move();
        }
        
        // Coalesce any fragment that has landed on a lower fragment
        for ( int i = 1 ; i < size() ; i++ ) {
            double previousTop;
            
            previousTop = elementAt(i - 1).position + elementAt(i - 1).length;
            if ( elementAt(i).getPosition() < previousTop ) {
                elementAt(i - 1).setLength(elementAt(i - 1).getLength() + elementAt(i).getLength());
                removeElementAt(i);
                i--;
            }
        }
        
        // Check for the return to a single unfragmented and grounded column
        if ( size() == 1 && elementAt(0).getPosition() < 1 )
            fragmented = false;
        
    }
    
    public synchronized FragmentedColumnPosition findFragmentPosition(int yPixel, int rounding) {
        double yValue;
        double dataYValue;
        double nextThreshold;
        boolean onLandscape;
        Fragment fragment;
        FragmentedColumnPosition columnPosition;
        
        columnPosition = new FragmentedColumnPosition();
        yValue = graphContext.getValueFromYPixel(yPixel);
        dataYValue = 0;
        columnPosition.setPanelValue(yValue);
        onLandscape = false;
        
        //if ( yValue < 0 ) {
        // Note - size should never be zero here, but it is sometimes, so root cause should be fixed
        if ( size() != 0 && yValue < elementAt(0).getPosition() ) {
            columnPosition.setFragmentNumber(-1);
            columnPosition.setFragmentOffset(elementAt(0).getPosition() - yValue);
            columnPosition.setOnLandscape(false);
            columnPosition.setDataValue(yValue);
            return columnPosition;
        }
        
        for ( int fragmentNumber = 0 ; fragmentNumber < size() ; fragmentNumber++ ) {
            fragment = elementAt(fragmentNumber);
            if ( fragmentNumber == size() - 1 ) {
                nextThreshold = Double.MAX_VALUE;
            } else {
                nextThreshold = elementAt(fragmentNumber + 1).getPosition();
            }
            if ( yValue < nextThreshold ) {
                columnPosition.setFragmentNumber(fragmentNumber);
                columnPosition.setFragmentOffset(yValue - fragment.getPosition());
                
                // Rounding to eliminate small fragments
                if ( rounding == SMALL_FRAGMENT_ROUNDING_UP ) {
                    if ( yValue < ( fragment.getPosition() + fragment.getLength() ) && ( fragment.getPosition() + fragment.getLength() ) - yValue < SMALLEST_FRAGMENT * graphContext.getYValuesPerPixel() )
                        columnPosition.setFragmentOffset(fragment.getLength());
                } else if ( rounding == SMALL_FRAGMENT_ROUNDING_DOWN ) {
                    if ( yValue - fragment.getPosition() < SMALLEST_FRAGMENT * graphContext.getYValuesPerPixel() )
                        columnPosition.setFragmentOffset(0);
                }
                
                //if ( yValue < fragment.getPosition() + fragment.getLength() ) {
                if ( columnPosition.getFragmentOffset() < fragment.getLength() ) {
                    columnPosition.setOnLandscape(true);
                    //columnPosition.setDataValue(dataYValue + (yValue - fragment.getPosition()) );
                    columnPosition.setDataValue(dataYValue + columnPosition.getFragmentOffset() );
                } else {
                    // Make adjustment to end of preceding fragment
                    //columnPosition.setDataValue(getValueAtFragmentEnd(columnPosition.getFragmentNumber()));
                    columnPosition.setDataValue(dataYValue + fragment.getLength());
                }
                return columnPosition;
            }
            dataYValue += fragment.getLength();
        }
        
        //logger.warn("Fragment not found for y pixel: " + yPixel);
        //logger.warn("Column: " + toString());
        return columnPosition;
        
    }
    
    public synchronized void findSetPosition(FragmentedColumnPosition columnPosition, int xSummaryPoint) {
        //double yValue;
        double dataValue;
        double totalValue;
        double nextThreshold;
        int totalSets;
        
        //if ( columnPosition == null )
        //    columnPosition = findColumnPosition(yPixel);
        //yValue = graphContext.getValueFromYPixel(yPixel);
        
        if ( columnPosition.getFragmentNumber() == -1 ) {
            columnPosition.setSetNumber(-1);
            columnPosition.setSetOffset(columnPosition.getDataValue());
            //return columnPosition;
            return;
        }
        
        // Make adjustment to end of preceding fragment
        if ( ! columnPosition.isOnLandscape() )
            columnPosition.setDataValue(getValueAtFragmentEnd(columnPosition.getFragmentNumber()));
        
        totalValue = 0;
        totalSets = graphContext.getSummaryData().getNumberOfSets();
        for ( int set = 0 ; set < totalSets ; set++ ) {
            dataValue = graphContext.getSummaryData().getValue(set, xSummaryPoint);
            if ( set == totalSets - 1 ) {
                nextThreshold = Double.MAX_VALUE;
            } else {
                nextThreshold = totalValue + dataValue;
            }
            if ( columnPosition.getDataValue() < nextThreshold ) {
                columnPosition.setSetNumber(set);
                columnPosition.setSetOffset(columnPosition.getDataValue() - totalValue);
                //return columnPosition;
                return;
            }
            totalValue += dataValue;
        }
        //return columnPosition;
        logger.warn("Fragment not at x point: " + xSummaryPoint);
        logger.warn("Column position: " + columnPosition.toString());
        return;
    }
    
    public synchronized double getValueAtFragmentEnd(int fragment) {
        double totalValue;
        
        totalValue = 0;
        for ( int i = 0 ; i <= Math.min(fragment, size() - 1) ; i++ ) {
            totalValue += elementAt(i).getLength();
        }
        return totalValue;
    }
    
    /*
    public void adjustEndSetPosition(FragmentedColumnPosition position, Fragment fragment) {
        double targetValue;
        double totalValue;
     
        totalValue = 0;
        targetValue = getValueAtFragmentEnd(position.getFragmentNumber());
        for ( int i = 0 ; i <= Math.min(fragment, size() - 1) ; i++ ) {
            totalValue += elementAt(i).getLength();
        }
     
    }
     
     */
    
    /** Assumes external synchronization of Summary Data */
    public synchronized void decimateAt(int xPixel, int xOffset, int startYPixel, int endYPixel, int[] startIndicies) {
        FragmentedColumnPosition startPosition;
        FragmentedColumnPosition endPosition;
        Fragment startFragment;
        Fragment endFragment;
        Fragment newFragment;
        
        //synchronized(graphContext.getData()) {
        
        //logger.debug("Fragmented column before decimation:");
        //logger.debug(toString());
        
        startPosition = findFragmentPosition(startYPixel, SMALL_FRAGMENT_ROUNDING_DOWN);
        endPosition = findFragmentPosition(endYPixel, SMALL_FRAGMENT_ROUNDING_UP);
        findSetPosition(startPosition, xPixel - xOffset);
        findSetPosition(endPosition, xPixel - xOffset);
        
        if ( startPosition.getFragmentNumber() != -1 )
            startFragment = elementAt(startPosition.getFragmentNumber());
        else
            startFragment = null;
        if ( endPosition.getFragmentNumber() != -1 )
            endFragment = elementAt(endPosition.getFragmentNumber());
        else
            endFragment = null;
        
        //logger.debug("startPosition: " + startPosition);
        //logger.debug("endPosition: " + endPosition);
        
        // Case where one fragment splits into two - add a new upper fragment
        if ( startPosition.getFragmentNumber() == endPosition.getFragmentNumber() && endPosition.isOnLandscape() ) {
            //if ( startFragment.getLength() - endPosition.getFragmentOffset() > SMALLEST_FRAGMENT * graphContext.getYValuesPerPixel() ) {
            newFragment = new Fragment(graphContext, time, startFragment.getPosition() + endPosition.getFragmentOffset(), startFragment.getLength() - endPosition.getFragmentOffset(), VELOCITY * graphContext.getYValuesPerPixel());
            //logger.debug("Adding new fragment: " + newFragment);
            add(startPosition.getFragmentNumber() + 1, newFragment);
            //}
            // Case where upper fragment is too small - remove fragment
            //else {
            //    endPosition = findSetPosition(endPosition, xPixel - xOffset, graphContext.getYPixelFromValue(startFragment.getPosition() + startFragment.getLength()));
            //}
        }
        
        // Shorten original lower fragment if start position is on landscape
        if ( startPosition.isOnLandscape() ) {
            startFragment.setLength(startPosition.getFragmentOffset());
            startFragment.setVelocity(startFragment.getVelocity() - VELOCITY * graphContext.getYValuesPerPixel());
            // Case where lower fragment is of zero length - remove fragment
            if ( startFragment.getLength() <= 0 ) {
                // Case where lower fragment is too small - remove fragment
                //if ( startFragment.getLength() < SMALLEST_FRAGMENT * graphContext.getYValuesPerPixel() ) {
                remove(startFragment);
                startPosition.setFragmentNumber(startPosition.getFragmentNumber() - 1);
                endPosition.setFragmentNumber(endPosition.getFragmentNumber() - 1);
            }
        }
        
        // Shorten original upper fragment if end position is on landscape
        if ( startPosition.getFragmentNumber() != endPosition.getFragmentNumber() && endPosition.isOnLandscape() ) {
            endFragment.setPosition(endFragment.getPosition() + endPosition.getFragmentOffset());
            endFragment.setLength(endFragment.getLength() - endPosition.getFragmentOffset());
            endFragment.setVelocity(endFragment.getVelocity() + VELOCITY * graphContext.getYValuesPerPixel());
            // Case where upper fragment is too small - remove fragment
            
            //if ( endFragment.getLength() < SMALLEST_FRAGMENT * graphContext.getYValuesPerPixel() ) {
            //    remove(endPosition.getFragmentNumber());
            //}
            
        }
        
        // Case where upper fragment is completely destroyed - remove fragment
        if ( startPosition.getFragmentNumber() != endPosition.getFragmentNumber() && ! endPosition.isOnLandscape() ) {
            remove(endPosition.getFragmentNumber());
        }
        
        // Remove any fragments in the middle
        if ( startPosition.getFragmentNumber() + 1 < endPosition.getFragmentNumber())
            removeRange(startPosition.getFragmentNumber() + 1, endPosition.getFragmentNumber() - 1);
        
        // There is no fragmentation if the column has no fragments (i.e. is completely destroyed)
        if ( size() == 0 )
            fragmented = false;
        
        // Reduce data set values representing the current x pixel
        for ( int set = Math.max(startPosition.getSetNumber(), 0) ; set <= endPosition.getSetNumber()  ; set++ ) {
            DataSet points;
            Iterator iterator;
            DataPoint point;
            double summaryPoint;
            double reductionNumerator;
            
            DataSet dataSet;
            int nextIndex;
            IntegerPair range;
            
            // Find numerator for fraction of summary point set value to keep
            summaryPoint = graphContext.getSummaryData().getValue(set, xPixel - xOffset);
            if ( set == startPosition.getSetNumber() && set == endPosition.getSetNumber() ) {
                reductionNumerator = Math.max(summaryPoint - ( endPosition.getSetOffset() - startPosition.getSetOffset() ), 0);
            } else if ( set == startPosition.getSetNumber() ) {
                reductionNumerator = startPosition.getSetOffset();
            } else if ( set == endPosition.getSetNumber() ) {
                reductionNumerator = Math.max(summaryPoint - endPosition.getSetOffset(), 0);
            } else {
                reductionNumerator = 0;
            }
            
            // Reduce data points whose time falls within the single summary pixel
            
            /*
            points = graphContext.getData().getDataSet(set).pointsBetween(graphContext.getTimeFromXPixel(xPixel), graphContext.getTimeFromXPixel(xPixel + 1));
            iterator = points.iterator();
            while ( iterator.hasNext() ) {
                point = (DataPoint)(iterator.next());
                if ( summaryPoint > 0 )
                    point.setValue(point.getValue() * reductionNumerator / summaryPoint);
                else
                    point.setValue(0);
            }
             */
            
            dataSet = graphContext.getData().getDataSet(set);
            if ( startIndicies[set] == -1 )
                range = dataSet.pointIndiciesBetween(graphContext.getTimeFromXPixel(xPixel), graphContext.getTimeFromXPixel(xPixel + 1));
            else
                range = dataSet.pointIndiciesBetween(graphContext.getTimeFromXPixel(xPixel), graphContext.getTimeFromXPixel(xPixel + 1), startIndicies[set]);
            nextIndex = range.first;
            while ( nextIndex <= range.second ) {
                point = dataSet.elementAt(nextIndex);
                if ( summaryPoint > 0 )
                    point.setValue(point.getValue() * reductionNumerator / summaryPoint);
                else
                    point.setValue(0);
                nextIndex++;
            }
            startIndicies[set] = range.second;
        }
        //logger.debug("Fragmented column after decimation:");
        //logger.debug(toString());
        
    }
    
    /**
     * @return the number of pixels destroyed
     */
    public int explode(int xPixel, int xOffset, int startYPixel, int endYPixel, double velocity) {
        double xVelocity;
        double yVelocity;
        ExplosionParticle particle;
        int explosionPixels;
        int patternIndex;
        ExplosionParticleShape pattern;
        Color color;
        int scoreIncrement;
        int destroyedPixels;
        
        
        scoreIncrement = 0;
        destroyedPixels = 0;
        for ( int y = startYPixel ; y >= endYPixel ; y-- ) {
            if ( isLandscapeAt(graphContext.getValueFromYPixel(y)) ) {
                destroyedPixels++;
                //if ( gameContext.getLandscape().isLandscapeAt(xPixel, y) ) {
                // Keep explosion particle mass statistically equal to exploding landscape mass
                if ( (int)(Math.random() * (1 + ExplosionParticleShape.SHAPES.length / 2)) == 0 ) {
                    xVelocity = (Math.random() - 0.5) * velocity;
                    yVelocity = Math.random() * velocity * ( y - ( ( startYPixel + endYPixel ) / 2));
                    explosionPixels = (int)(Math.random() * ExplosionParticleShape.SHAPES.length);
                    patternIndex = (int)(Math.random() * ExplosionParticleShape.SHAPES[explosionPixels].length);
                    pattern = ExplosionParticleShape.SHAPES[explosionPixels][patternIndex];
                    color = ExplosionParticleShape.COLORS[(int)(Math.random() * ExplosionParticleShape.COLORS.length)];
                    particle = new ExplosionParticle(gameContext, graphContext, (double)xPixel, (double)y, xVelocity, yVelocity, pattern, color, null);
                    //logger.debug("Particle created: ");
                    //logger.debug(particle.toString());
                    gameContext.addExplosionParticle(particle);
                    scoreIncrement++;
                }
            }
        }
        gameContext.increaseScore(scoreIncrement);
        return destroyedPixels;
    }
    
    public boolean isFragmented() {
        return fragmented;
    }
    
    public boolean isLandscapeAt(double yValue) {
        Iterator<Fragment> iterator;
        Fragment fragment;
        
        iterator = iterator();
        while ( iterator.hasNext() ) {
            fragment = iterator.next();
            if ( yValue >= fragment.getPosition() && yValue < fragment.getPosition() + fragment.getLength() )
                return true;
        }
        return false;
    }
    
    public long getTime() {
        return time;
    }
    
    public int getXPixel() {
        return graphContext.getXPixelFromTime(time);
    }
    
    public String toString() {
        StringBuffer buffer;
        
        buffer = new StringBuffer();
        for ( int i = 0 ; i < size() ; i++ ) {
            buffer.append("Fragment " + i + ": " + elementAt(i).toString());
            if ( i < size() - 1 )
                buffer.append("\n");
        }
        
        return buffer.toString();
        
    }
}
