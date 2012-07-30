/*
 * MovementPattern.java
 *
 * Created on 9 January 2008, 00:27
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

import java.util.Vector;

/**
 *
 * @author mark
 */
public class ControlPattern extends Vector<ControlPrimitive> {
    
    
    public static final double FIREWORKS_STARBOMB_SLOW_VELOCITY = 0.05;
    
    public static final double FIREWORKS_STARBOMB_FAST_VELOCITY = 0.1;
    
    public static ControlPattern wordsBonusLife;
    
    public static ControlPattern wordsRainbowRouter;
    
    public static ControlPattern wordsInstallYahtzee;

    public static ControlPattern explosionEnemy;
    
    public static ControlPattern explosionBombedLandscape;
    
    public static ControlPattern fireworksStarBombSmall;
    
    public static ControlPattern fireworksStarBombLarge;
    
    public static ControlPattern selfVictoryExit;
    
    
    static {
        explosionEnemy = new ControlPattern(false);
        explosionEnemy.add(ControlImage.create(0, GameImage.EXPLOSION_50X50));
        explosionEnemy.add(ControlDisplacement.createRelative(50, 5, 5));
        explosionEnemy.add(ControlImage.create(50, GameImage.EXPLOSION_40X40));
        explosionEnemy.add(ControlDisplacement.createRelative(100, 5, 5));
        explosionEnemy.add(ControlImage.create(100, GameImage.EXPLOSION_30X30));
        explosionEnemy.add(ControlDisplacement.createRelative(150, 5, 5));
        explosionEnemy.add(ControlImage.create(150, GameImage.EXPLOSION_20X20));
        explosionEnemy.add(ControlDisplacement.createRelative(200, 5, 5));
        explosionEnemy.add(ControlImage.create(200, GameImage.EXPLOSION_10X10));
        explosionEnemy.add(ControlFinish.create(250));
        
        explosionBombedLandscape = new ControlPattern(false);
        explosionBombedLandscape.add(ControlImage.create(0, GameImage.EXPLOSION_120X120));
        explosionBombedLandscape.add(ControlDisplacement.createRelative(50, 10, 10));
        explosionBombedLandscape.add(ControlImage.create(50, GameImage.EXPLOSION_100X100));
        explosionBombedLandscape.add(ControlDisplacement.createRelative(100, 10, 10));
        explosionBombedLandscape.add(ControlImage.create(100, GameImage.EXPLOSION_80X80));
        explosionBombedLandscape.add(ControlDisplacement.createRelative(150, 10, 10));
        explosionBombedLandscape.add(ControlImage.create(150, GameImage.EXPLOSION_60X60));
        explosionBombedLandscape.add(ControlDisplacement.createRelative(200, 5, 5));
        explosionBombedLandscape.add(ControlImage.create(200, GameImage.EXPLOSION_50X50));
        explosionBombedLandscape.add(ControlDisplacement.createRelative(250, 5, 5));
        explosionBombedLandscape.add(ControlImage.create(250, GameImage.EXPLOSION_40X40));
        explosionBombedLandscape.add(ControlDisplacement.createRelative(300, 5, 5));
        explosionBombedLandscape.add(ControlImage.create(300, GameImage.EXPLOSION_30X30));
        explosionBombedLandscape.add(ControlDisplacement.createRelative(350, 5, 5));
        explosionBombedLandscape.add(ControlImage.create(350, GameImage.EXPLOSION_20X20));
        explosionBombedLandscape.add(ControlDisplacement.createRelative(400, 5, 5));
        explosionBombedLandscape.add(ControlImage.create(400, GameImage.EXPLOSION_10X10));
        explosionBombedLandscape.add(ControlFinish.create(450));
        
        wordsBonusLife = new ControlPattern(false);
        wordsBonusLife.add(ControlDisplacement.createScaled(0, -0.2, 0.03));
        wordsBonusLife.add(ControlVelocity.create(0, 0.2, 0));
        wordsBonusLife.add(ControlVelocity.create(800, 0, 0));
        wordsBonusLife.add(ControlVelocity.create(2800, -0.2, 0));
        
        wordsRainbowRouter = new ControlPattern(false);
        wordsRainbowRouter.add(ControlDisplacement.createScaled(0, 0.6, 1));
        wordsRainbowRouter.add(ControlVelocity.create(0, 0, -0.1));
        wordsRainbowRouter.add(ControlVelocity.create(350, 0, 0));
        wordsRainbowRouter.add(ControlVelocity.create(2350, 0.2, 0));
        
        wordsInstallYahtzee = new ControlPattern(false);
        wordsInstallYahtzee.add(ControlDisplacement.createScaled(0, 1, 0.74));
        wordsInstallYahtzee.add(ControlVelocity.create(500, -0.11, 0));
        wordsInstallYahtzee.add(ControlVelocity.create(1500, 0, 0));
        wordsInstallYahtzee.add(ControlVelocity.create(3500, 0.1, 0));
        
        fireworksStarBombSmall = new ControlPattern(false);
        fireworksStarBombSmall.add(ControlRandomWait.create(0, GameContext.TRANSITION_TIMER_VICTORY + GameContext.TRANSITION_TIMER_VICTORY_EXIT));
        fireworksStarBombSmall.add(ControlDisplacement.createScaled(0, 0, 1));
        fireworksStarBombSmall.add(ControlDisplacement.createRelativeWithRandom(0, 0, 0, GameContext.DEFAULT_WINDOW_WIDTH * 1.5, 0));
        fireworksStarBombSmall.add(ControlVelocity.createWithRandom(0, 0, -0.7, 0.5, 0.5));
        fireworksStarBombSmall.add(ControlRandomWait.create(500, 800));
        fireworksStarBombSmall.add(ControlExplosionParticle.createWithAngle(500, 0, FIREWORKS_STARBOMB_SLOW_VELOCITY));
        fireworksStarBombSmall.add(ControlExplosionParticle.createWithAngle(500, 30, FIREWORKS_STARBOMB_SLOW_VELOCITY));
        fireworksStarBombSmall.add(ControlExplosionParticle.createWithAngle(500, 60, FIREWORKS_STARBOMB_SLOW_VELOCITY));
        fireworksStarBombSmall.add(ControlExplosionParticle.createWithAngle(500, 90, FIREWORKS_STARBOMB_SLOW_VELOCITY));
        fireworksStarBombSmall.add(ControlExplosionParticle.createWithAngle(500, 120, FIREWORKS_STARBOMB_SLOW_VELOCITY));
        fireworksStarBombSmall.add(ControlExplosionParticle.createWithAngle(500, 150, FIREWORKS_STARBOMB_SLOW_VELOCITY));
        fireworksStarBombSmall.add(ControlExplosionParticle.createWithAngle(500, 180, FIREWORKS_STARBOMB_SLOW_VELOCITY));
        fireworksStarBombSmall.add(ControlExplosionParticle.createWithAngle(500, 210, FIREWORKS_STARBOMB_SLOW_VELOCITY));
        fireworksStarBombSmall.add(ControlExplosionParticle.createWithAngle(500, 240, FIREWORKS_STARBOMB_SLOW_VELOCITY));
        fireworksStarBombSmall.add(ControlExplosionParticle.createWithAngle(500, 270, FIREWORKS_STARBOMB_SLOW_VELOCITY));
        fireworksStarBombSmall.add(ControlExplosionParticle.createWithAngle(500, 300, FIREWORKS_STARBOMB_SLOW_VELOCITY));
        fireworksStarBombSmall.add(ControlExplosionParticle.createWithAngle(500, 330, FIREWORKS_STARBOMB_SLOW_VELOCITY));
        fireworksStarBombSmall.add(ControlFinish.create(500));
        
        fireworksStarBombLarge = new ControlPattern(false);
        fireworksStarBombLarge.add(ControlRandomWait.create(0, GameContext.TRANSITION_TIMER_VICTORY + GameContext.TRANSITION_TIMER_VICTORY_EXIT ));
        fireworksStarBombLarge.add(ControlDisplacement.createScaled(0, 0, 1));
        fireworksStarBombLarge.add(ControlDisplacement.createRelativeWithRandom(0, 0, 0, GameContext.DEFAULT_WINDOW_WIDTH * 1.5, 0));
        fireworksStarBombLarge.add(ControlVelocity.createWithRandom(0, 0, -0.7, 0.5, 0.5));
        fireworksStarBombLarge.add(ControlRandomWait.create(500, 800));
        fireworksStarBombLarge.add(ControlExplosionParticle.createWithAngle(500, 0, FIREWORKS_STARBOMB_FAST_VELOCITY));
        fireworksStarBombLarge.add(ControlExplosionParticle.createWithAngle(500, 30, FIREWORKS_STARBOMB_FAST_VELOCITY));
        fireworksStarBombLarge.add(ControlExplosionParticle.createWithAngle(500, 60, FIREWORKS_STARBOMB_FAST_VELOCITY));
        fireworksStarBombLarge.add(ControlExplosionParticle.createWithAngle(500, 90, FIREWORKS_STARBOMB_FAST_VELOCITY));
        fireworksStarBombLarge.add(ControlExplosionParticle.createWithAngle(500, 120, FIREWORKS_STARBOMB_FAST_VELOCITY));
        fireworksStarBombLarge.add(ControlExplosionParticle.createWithAngle(500, 150, FIREWORKS_STARBOMB_FAST_VELOCITY));
        fireworksStarBombLarge.add(ControlExplosionParticle.createWithAngle(500, 180, FIREWORKS_STARBOMB_FAST_VELOCITY));
        fireworksStarBombLarge.add(ControlExplosionParticle.createWithAngle(500, 210, FIREWORKS_STARBOMB_FAST_VELOCITY));
        fireworksStarBombLarge.add(ControlExplosionParticle.createWithAngle(500, 240, FIREWORKS_STARBOMB_FAST_VELOCITY));
        fireworksStarBombLarge.add(ControlExplosionParticle.createWithAngle(500, 270, FIREWORKS_STARBOMB_FAST_VELOCITY));
        fireworksStarBombLarge.add(ControlExplosionParticle.createWithAngle(500, 300, FIREWORKS_STARBOMB_FAST_VELOCITY));
        fireworksStarBombLarge.add(ControlExplosionParticle.createWithAngle(500, 330, FIREWORKS_STARBOMB_FAST_VELOCITY));
        fireworksStarBombLarge.add(ControlFinish.create(500));
        
        selfVictoryExit = new ControlPattern(false);
        selfVictoryExit.add(ControlVelocity.create(0, 0.3, 0));
    }
    
    protected int iterations;
    
    protected int repeatFrom;
    
    protected boolean repeating;
    
    protected ControlPrimitive finalMovement;
    
    /** Creates a new instance of MovementPattern */
    public ControlPattern(int repeatFrom) {
        //this.iterations = iterations;
        this.repeatFrom = repeatFrom;
        repeating = true;
    }
    
    public ControlPattern() {
        iterations = -1;
        repeating = false;
        repeatFrom = 0;
    }
    
    public ControlPattern(boolean repeating) {
        this.repeating = repeating;
        repeatFrom = 0;
    }
    
    public int getIterations() {
        return iterations;
    }
    
    public int getRepeatFrom() {
        return repeatFrom;
    }
    
    public boolean isRepeating() {
        return repeating;
    }
    
    public String toString() {
        StringBuffer buffer;
        
        buffer = new StringBuffer();
        for ( int i = 0 ; i < size() ; i++ ) {
            buffer.append(elementAt(i).toString() + "/n");
        }
        
        return buffer.toString();
    }
}
