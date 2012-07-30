/*
 * LevelAttackPattern.java
 *
 * Created on 11 January 2008, 22:10
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
public class LevelAttackPattern extends Vector<LevelAttackPrimitive> {
    
    public static final double SPEED_CRAWL = 0.25;
    
    public static final double SPEED_SLOW = 0.3;
    
    public static final double SPEED_MEDIUM = 0.35;
    
    public static final double SPEED_FAST = 0.4;
    
    public static final double SPEED_LIGHTNING = 0.45;
    
    
    /*
     * Control Pattern movement declarations
     *
     * Naming convention: [shape][shape-detail]Moving[direction]Fire[firing]
     *
     * shape - [ straight | swoop | parabola | shm | staircase | snake | diamond | loop ]
     * direction - [ Left | Right | Up | Down ]
     * firing - [ <bullet-type> ] [ Single | Light | Medium | Heavy ] [ Early | Late ]
     */
    public static ControlPattern staticMovingNowhere;
    
    public static ControlPattern straightMovingLeft;
    
    public static ControlPattern straightMovingLeftSlowly;
    
    public static ControlPattern straightMovingLeftFiringEmail;
    
    public static ControlPattern straightMovingRight;
    
    public static ControlPattern straightMovingUp;
    
    public static ControlPattern straightMovingDown;
    
    public static ControlPattern straightSlowMovingLeft;
    
    public static ControlPattern straightSlowMovingRight;
    
    public static ControlPattern straightMovingLeftUp;
    
    public static ControlPattern straightMovingLeftDown;
    
    public static ControlPattern straightMovingRightUp;
    
    public static ControlPattern straightSwoopMovingLeft;
    
    public static ControlPattern straightSwoopMovingLeftFire1BulletEarly;
    
    public static ControlPattern straightSwoopMovingLeftFire1BulletMiddle;
    
    public static ControlPattern arcMovingUpLeft;
    
    public static ControlPattern arcMovingLeftUp;
    
    public static ControlPattern arcMovingDownLeft;
    
    public static ControlPattern arcMovingLeftDown;
    
    public static ControlPattern parabolaMovingLeftUp;
    
    public static ControlPattern parabolaMovingLeftDown;
    
    public static ControlPattern rectangle2CornersMovingLeftUp;
    
    public static ControlPattern rectangle2CornersMovingLeftDown;
    
    public static ControlPattern rectangle4CornersMovingLeftUp;
    
    public static ControlPattern rectangle4CornersMovingLeftDown;
    
    public static ControlPattern diamond1CornerMovingLeftUp;
    
    public static ControlPattern diamond1CornerMovingLeftDown;
    
    public static ControlPattern diamond3CornersMovingLeftUp;
    
    public static ControlPattern diamond3CornersMovingLeftDown;
    
    public static ControlPattern staircase1StepMovingLeftDown;
    
    public static ControlPattern staircase5StepsMovingLeftDown;
    
    public static ControlPattern staircase1StepMovingLeftUp;
    
    public static ControlPattern staircase5StepsMovingLeftUp;
    
    public static ControlPattern staircase5StepsMovingRightUp;
    
    public static ControlPattern zigzag5CornersMovingLeftDown;
    
    public static ControlPattern squarewaveLargeAmplitudeMovingLeft;
    
    public static ControlPattern squarewaveLargeAmplitudeMovingRight;
    
    public static ControlPattern squarewaveLargeAmplitudeMovingUp;
    
    public static ControlPattern squarewaveLargeAmplitudeMovingDown;
    
    public static ControlPattern squarewaveLargeAmplitudeMovingRightThenLeft;
    
    public static ControlPattern sawtoothLargeAmplituteMovingLeft;
    
    public static ControlPattern shmMovingLeft;
    
    public static ControlPattern shmInfinitessimalAmplitudeMovingLeft;
    
    public static ControlPattern shmTinyAmplitudeMovingLeft;
    
    public static ControlPattern shmSmallAmplitudeMovingLeft;
    
    public static ControlPattern shmMediumAmplitudeMovingLeft;
    
    public static ControlPattern shmLargeAmplitudeMovingLeft;
    
    public static ControlPattern shmHugeAmplitudeMovingLeft;
    
    public static ControlPattern loopSingleUpMovingLeft;
    
    public static ControlPattern loopSingleDownMovingLeft;
    
    public static ControlPattern loopXSingleRightMovingLeftUp;
    
    public static ControlPattern loopXSingleRightMovingLeftDown;
    
    public static ControlPattern loopXSingleRightMovingLeftDownFiringHomingCircle;
    
    public static ControlPattern loopVSingleRightMovingLeftUp;
    
    public static ControlPattern loopVSingleRightMovingLeftDown;
    
    public static ControlPattern diamondFire4Bullets;
    
    public static ControlPattern stopgoDownMovingLeft;
    
    public static ControlPattern stopgoUpMovingLeft;
    
    public static ControlPattern stopgoDownUpMovingLeft;
    
    public static ControlPattern plane3CornersMovingLeft;
    
    public static ControlPattern plane5CornersMovingLeft;
    
    public static ControlPattern plane6CornersMovingLeft;
    
    public static ControlPattern bossSupervisorMovement;
    
    public static ControlPattern bossTeamLeaderMovement;
    
    public static ControlPattern bossNetworksManagerMovement;
    
    public static ControlPattern bossOperationsManagerMovement;
    
    public static ControlPattern bossITExecutiveMovement;
    
    public static ControlPattern bossCIOMovement;
    
    public static ControlPattern bossCEOMovement;
    
    public static ControlPattern bossChairmanMovement;
    
    public static ControlPattern bossDirectorMovement;
    
    public static ControlPattern bossReceiverMovement;
    
    /*
     * Firing pattern declarations
     *
     * Naming convention: firing[direction][speed][when][intensity][type]
     *
     * direction - [ Left | Homing ]
     * speed - [ Crawl | Slow | Fast | Lightning ]
     * when - [ Early | Late ]
     * intensity - [ Single | Spread<n> | Light | Medium | Heavy ]
     * bullet-type - [ Circle | Missile | Email | Phone | Chair ]
     */
    
    public static ControlPattern firingLeftExplodingChair;
    
    public static ControlPattern firingUpExplodingChair;
    
    public static ControlPattern firingLeftSingleCircle;
    
    public static ControlPattern firingLeftEarlySingleCircle;
    
    public static ControlPattern firingLeftLateSingleCircle;
    
    public static ControlPattern firingLeftLightCircles;
    
    public static ControlPattern firingLeftCircles;
    
    public static ControlPattern firingLeftEarlySingleMissile;
    
    public static ControlPattern firingLeftSingleEmail;
    
    public static ControlPattern firingLeftFastLateSingleEmail;
    
    public static ControlPattern firingLeftSpread5Circle;
    
    public static ControlPattern firingLeftSlow4Missiles;
    
    public static ControlPattern firingLeft4Missiles;
    
    public static ControlPattern firingLeftLightMissiles;
    
    public static ControlPattern firingRotaryCirclesLeftUpLightPhones;
    
    public static ControlPattern firingRotaryCirclesLeftUpLightChairs;
    
    public static ControlPattern firingHomingEarlySingleCircle;
    
    public static ControlPattern firingHomingSingleCircle;
    
    public static ControlPattern firingHomingLateSingleCircle;
    
    public static ControlPattern firingHomingFastEarlySingleCircle;
    
    public static ControlPattern firingHomingFastSingleCircle;
    
    public static ControlPattern firingHomingFastLateSingleCircle;
    
    public static ControlPattern firingHoming2Circles;
    
    public static ControlPattern firingHomingLightCircles;
    
    public static ControlPattern firingHomingCircles;
    
    public static ControlPattern firingHomingFastCircles;
    
    public static ControlPattern firingLeftCircleHomingCircle;
    
    public static ControlPattern firingHomingLightCircleLeftLightEmail;
    
    public static ControlPattern firingHomingLightCircleLeftEarlySinglePhone;
    
    public static ControlPattern firingHomingLightCircleLeftSinglePhone;
    
    public static ControlPattern firingHomingLightCircleLeftDownLateSinglePhone;
    
    public static ControlPattern firingHomingLightCircleLeftLateSinglePhone;
    
    public static ControlPattern firingHomingLightCircleLeftLightPhone;
    
    public static ControlPattern firingLeftSingleEmailHomingSingleCircle;
    
    public static ControlPattern firingLeftHeavyEmail;
    
    public static ControlPattern firingStraightSpread3Missile;
    
    public static ControlPattern firingHomingSpread5Circle;
    
    public static ControlPattern firingHomingSingleCircleLeftSingleChair;
    
    public static ControlPattern firingSingleChairSingleEmailSinglePhone;
    
    
    public static ControlPattern bossSupervisorFiring;
    
    public static ControlPattern bossTeamLeaderFiring;
    
    public static ControlPattern bossNetworksManagerFiring;
    
    public static ControlPattern bossOperationsManagerFiring;
    
    public static ControlPattern bossITExecutiveFiring;
    
    public static ControlPattern bossCIOFiring;
    
    public static ControlPattern bossCEOFiring;
    
    public static ControlPattern bossDirectorFiring;
    
    public static ControlPattern bossChairmanFiring;
    
    public static ControlPattern bossReceiverFiring;
    
    
    /*
     * Attack wave pattern declarations
     *
     * Naming convention:
     * [enemy-major][enemy-minor][enemy-count][movement-pattern]
     * From[start-edge][start-location-on-edge]
     * Firing[bullet-count][bullet-type][bullet-direction]
     *
     * Ordering:
     * 1) by enemy major type
     * 2) by enemy minor type
     * 3) by difficulty
     */
    public static AttackWavePattern userEgg5StraightFromRightTop;
    
    public static AttackWavePattern userEgg3StraightFromLeftBottom;
    
    public static AttackWavePattern userEgg3StraightDownFromTopRight;
    
    public static AttackWavePattern userEgg3StraightUpFromBottomRight;
    
    public static AttackWavePattern userEgg3StraightLeftUpFromRightBottom;
    
    public static AttackWavePattern userEgg3StraightLeftDownFromRightTop;
    
    public static AttackWavePattern userEgg3StraightRightUpFromBottomLeft;
    
    public static AttackWavePattern userEgg6StraightSwoopFromRightTop;
    
    public static AttackWavePattern userEgg2StraightSwoopFromRightTop;
    
    public static AttackWavePattern userEgg4StraightSwoopFromRightTop;
    
    
    public static AttackWavePattern userUfo1StaticFromTopMiddle;
    
    public static AttackWavePattern userUfo3StraightSwoopFromRightBottomFiring3Circles;
    
    public static AttackWavePattern userUfo4ArcUpLeftFromRightBottom;
    
    public static AttackWavePattern userUfo4ArcLeftUpFromRightBottom;
    
    public static AttackWavePattern userUfo4ArcDownLeftFromRightTop;
    
    public static AttackWavePattern userUfo4ArcLeftDownFromRightTop;
    
    public static AttackWavePattern userUfo4ArcUpLeftFromRightBottomFiringCircleSingleHoming;
    
    public static AttackWavePattern userUfo4ArcLeftUpFromRightBottomFiringCircleSingleHoming;
    
    public static AttackWavePattern userUfo4ArcDownLeftFromRightTopFiringCircleSingleHoming;
    
    public static AttackWavePattern userUfo4ArcLeftDownFromRightTopFiringCircleSingleHoming;
    
    public static AttackWavePattern userUfo8ArcAlternateHorizontalFromRight;
    
    public static AttackWavePattern userUfo8ArcAlternateVerticalFromRight;
    
    public static AttackWavePattern userUfo3ParabolaFromRightBottom;
    
    public static AttackWavePattern userUfo3ParabolaFromRightTop;
    
    public static AttackWavePattern userUfo5Staircase1StepFromRightTop;
    
    public static AttackWavePattern userUfo5Staircase1StepFromRightBottom;
    
    public static AttackWavePattern userUfo5Staircase5StepsFromRightBottom;
    
    public static AttackWavePattern userUfo5Staircase5StepsFromRightTop;
    
    public static AttackWavePattern userUfo4LoopSingleFromRightTop;
    
    public static AttackWavePattern userUfo5LoopSingleFromRightMiddle;
    
    public static AttackWavePattern userUfo5LoopSingleFromRightTop;
    
    public static AttackWavePattern userUfo5LoopXSingleRightFromRightBottom;
    
    public static AttackWavePattern userUfo5LoopXSingleRightFromRightTop;
    
    public static AttackWavePattern userUfo5LoopVSingleRightFromRightBottom;
    
    public static AttackWavePattern userUfo5LoopVSingleRightFromRightTop;
    
    public static AttackWavePattern userUfo5DiamondFromRightTopFiring20Circles;
    
    
    public static AttackWavePattern userBurger4Rectangle2CornersFromRightMiddle;
    
    public static AttackWavePattern userBurger4Rectangle2CornersFromRightTop;
    
    public static AttackWavePattern userBurger4Rectangle4CornersFromRightMiddle;
    
    public static AttackWavePattern userBurger4Rectangle4CornersFromRightTop;
    
    public static AttackWavePattern userBurger4Diamond1CornerFromRightTop;
    
    public static AttackWavePattern userBurger4Diamond1CornerFromRightBottom;
    
    public static AttackWavePattern userBurger4Diamond3CornersFromRightTop;
    
    public static AttackWavePattern userBurger4Diamond3CornersFromRightBottom;
    
    public static AttackWavePattern userBurger3StraightSwoopFromRightBottomFiring3Circles;
    
    public static AttackWavePattern userBurger3Staircase1StepDownFromRightTop;
    
    public static AttackWavePattern userBurger3Staircase1StepUpFromRightBottom;
    
    public static AttackWavePattern userBurger4Staircase5StepDownFromRightTop;
    
    public static AttackWavePattern userBurger4Staircase5StepUpFromRightBottom;
    
    public static AttackWavePattern userBurger4Staircase5StepUpFromLeftBottom;
    
    
    public static AttackWavePattern developerBird1ParabolaFromRightTop;
    
    public static AttackWavePattern developerBird1StraightFromRightTop;
    
    public static AttackWavePattern developerBird1StopGoDownFromRightTop;
    
    public static AttackWavePattern developerBird1StopGoUpFromRightMiddle;
    
    public static AttackWavePattern developerBird1StopGoDownUpFromRightTop;
    
    
    public static AttackWavePattern developerPlane3ParabolaFromRightMiddle;
    
    public static AttackWavePattern developerPlane1StraightFromRightMiddle;
    
    public static AttackWavePattern developerPlane1ZigzagFromRightMiddle;
    
    public static AttackWavePattern developerPlane3CornersFromRightTop;
    
    public static AttackWavePattern developerPlane5CornersFromRightTop;
    
    public static AttackWavePattern developerPlane6CornersFromRightTop;
    
    
    public static AttackWavePattern developerBubble1StraightFromRightTop;
    
    public static AttackWavePattern developerBubble1StraightFromLeftBottom;
    
    public static AttackWavePattern developerBubble1SquarewaveFromRightMiddle;
    
    public static AttackWavePattern developerBubble1SquarewaveRightFromBottomLeft;
    
    public static AttackWavePattern developerBubble1SquarewaveUpFromBottomLeft;
    
    public static AttackWavePattern developerBubble1SquarewaveDownFromTopRight;
    
    public static AttackWavePattern developerBubble1SquarewaveRightLeftFromBottomLeft;
    
    
    public static AttackWavePattern architectFromRightBottom;
    
    
    public static AttackWavePattern procurement1ShmFromRightTop;
    
    public static AttackWavePattern procurement1ShmSmallAmplitudeFromRightTop;
    
    public static AttackWavePattern procurementElevationFromRightTop;
    
    public static AttackWavePattern procurementSpeedFromRightTop;
    
    public static AttackWavePattern procurementBombsFromRightTop;
    
    public static AttackWavePattern procurementDamageFromRightTop;
    
    public static AttackWavePattern procurementInvincibilityFromRightTop;
    
    public static AttackWavePattern procurementLaserFromRightTop;
    
    public static AttackWavePattern procurementNullFromRightTop;
    
    
    public static AttackWavePattern bossSupervisor;
    
    public static AttackWavePattern bossTeamLeader;
    
    public static AttackWavePattern bossNetworksManager;
    
    public static AttackWavePattern bossOperationsManager;
    
    public static AttackWavePattern bossITExecutive;
    
    public static AttackWavePattern bossCIO;
    
    public static AttackWavePattern bossCEO;
    
    public static AttackWavePattern bossChairman;
    
    public static AttackWavePattern bossReceiver;
    
    
    
    public static final int NUMBER_OF_LEVELS = 9;
    
    
    
    public static LevelAttackPattern[] levels;
    
    public String description;
    
    public String bossName;
    
    
    static {
        
        /*
         * MOVEMENT PATTERNS
         */
        staticMovingNowhere = new ControlPattern(false);
        staticMovingNowhere.add(ControlFiring.createHoming(1000, EnemyType.FIRING_CIRCLE, 0.2));
        staticMovingNowhere.add(ControlFiring.createHoming(1500, EnemyType.FIRING_CIRCLE, 0.3));
        staticMovingNowhere.add(ControlFiring.createHoming(2000, EnemyType.FIRING_CIRCLE, 0.2));
        staticMovingNowhere.add(ControlFiring.createHoming(2500, EnemyType.FIRING_CIRCLE, 0.2));
        staticMovingNowhere.add(ControlFiring.createWithXY(5000, EnemyType.FIRING_CIRCLE, -0.2, 0));
        staticMovingNowhere.add(ControlFiring.createWithXY(6000, EnemyType.FIRING_CIRCLE, 0.2, 0));
        staticMovingNowhere.add(ControlFiring.createWithXY(7000, EnemyType.FIRING_CIRCLE, 0, -0.2));
        staticMovingNowhere.add(ControlFiring.createWithXY(8000, EnemyType.FIRING_CIRCLE, 0, 0.2));
        staticMovingNowhere = null;
        
        straightMovingLeft = new ControlPattern(false);
        straightMovingLeft.add(ControlDisplacement.createRelativeWithRandom(0, 0, 0, 0, 100));
        straightMovingLeft.add(ControlVelocity.createWithRandom(0, -0.2, 0, 0.1, 0));
        
        straightMovingLeftSlowly = new ControlPattern(false);
        straightMovingLeftSlowly.add(ControlVelocity.createWithRandom(0, -0.12, 0, 0.08, 0));
        
        straightMovingLeftFiringEmail = new ControlPattern(false);
        straightMovingLeftFiringEmail.add(ControlVelocity.createWithRandom(0, -0.2, 0, 0.1, 0));
        straightMovingLeftFiringEmail.add(ControlFiring.createWithXY(300, EnemyType.FIRING_EMAIL, -0.35, 0));
        
        straightMovingRight = new ControlPattern(false);
        straightMovingRight.add(ControlDisplacement.createRelativeWithRandom(0, 0, 0, 0, 50));
        straightMovingRight.add(ControlRandomWait.create(0, 1000));
        straightMovingRight.add(ControlVelocity.createWithRandom(0, 0.2, 0, 0.1, 0));
        
        straightMovingUp = new ControlPattern(false);
        straightMovingUp.add(ControlVelocity.createWithRandom(0, 0, -0.2, 0, 0.1));
        
        straightMovingDown = new ControlPattern(false);
        straightMovingDown.add(ControlVelocity.createWithRandom(0, 0, 0.2, 0, 0.1));
        
        straightSlowMovingLeft = new ControlPattern(false);
        straightSlowMovingLeft.add(ControlDisplacement.createRelativeWithRandom(0, 0, 0, 0, 100));
        straightSlowMovingLeft.add(ControlVelocity.createWithRandom(0, -0.08, 0, 0.02, 0));
        
        straightSlowMovingRight = new ControlPattern(false);
        straightSlowMovingRight.add(ControlDisplacement.createRelativeWithRandom(0, 0, 0, 0, 100));
        straightSlowMovingRight.add(ControlVelocity.createWithRandom(0, 0.08, 0, 0.02, 0));
        
        straightMovingLeftUp = new ControlPattern(false);
        straightMovingLeftUp.add(ControlVelocity.createWithRandom(0, -0.14, -0.1, 0.07, 0.05));
        
        straightMovingLeftDown = new ControlPattern(false);
        straightMovingLeftDown.add(ControlVelocity.createWithRandom(0, -0.14, 0.1, 0.07, 0.05));
        
        straightMovingRightUp = new ControlPattern(false);
        straightMovingRightUp.add(ControlVelocity.createWithRandom(0, 0.14, -0.1, 0.07, 0.05));
        
        straightSwoopMovingLeft = new ControlPattern(false);
        straightSwoopMovingLeft.add(ControlVelocity.createWithRandom(0, -0.5, 0, 0.1, 0));
        straightSwoopMovingLeft.add(ControlAcceleration.createWithTerminalVelocity(500, 0.0004, 0, 0.5, 0));
        
        straightSwoopMovingLeftFire1BulletEarly = new ControlPattern(false);
        straightSwoopMovingLeftFire1BulletEarly.add(ControlVelocity.createWithRandom(0, -0.3, 0, 0.1, 0));
        straightSwoopMovingLeftFire1BulletEarly.add(ControlFiring.createWithXY(100, EnemyType.FIRING_CIRCLE, -0.4, 0));
        straightSwoopMovingLeftFire1BulletEarly.add(ControlAcceleration.createWithTerminalVelocity(500, 0.0003, 0, 0.5, Double.NaN));
        
        straightSwoopMovingLeftFire1BulletMiddle = new ControlPattern(false);
        straightSwoopMovingLeftFire1BulletMiddle.add(ControlVelocity.create(0, -0.5, 0));
        straightSwoopMovingLeftFire1BulletMiddle.add(ControlAcceleration.createWithTerminalVelocity(0, 0.0005, 0, 0.5, Double.NaN));
        straightSwoopMovingLeftFire1BulletMiddle.add(ControlFiring.createHoming(1000, EnemyType.FIRING_CIRCLE, 0.2));
        
        arcMovingUpLeft = new ControlPattern(false);
        arcMovingUpLeft.add(ControlVelocity.create(0, -0.1, -0.6));
        arcMovingUpLeft.add(ControlAcceleration.createWithTerminalVelocity(0, -0.0003, 0.0003, -0.6, 0));
        
        arcMovingLeftUp = new ControlPattern(false);
        arcMovingLeftUp.add(ControlVelocity.create(0, -0.6, -0.1));
        arcMovingLeftUp.add(ControlAcceleration.createWithTerminalVelocity(0, 0.0003, -0.0003, 0, -0.6));
        
        arcMovingDownLeft = new ControlPattern(false);
        arcMovingDownLeft.add(ControlVelocity.create(0, -0.1, 0.6));
        arcMovingDownLeft.add(ControlAcceleration.createWithTerminalVelocity(0, -0.0003, -0.0003, -0.6, 0));
        
        arcMovingLeftDown = new ControlPattern(false);
        arcMovingLeftDown.add(ControlVelocity.create(0, -0.6, 0.1));
        arcMovingLeftDown.add(ControlAcceleration.createWithTerminalVelocity(0, 0.0003, 0.0003, 0, 0.6));
        
        parabolaMovingLeftUp = new ControlPattern(false);
        parabolaMovingLeftUp.add(ControlAcceleration.create(0, 0.0005, 0));
        parabolaMovingLeftUp.add(ControlVelocity.create(0, -0.7, -0.1));
        
        parabolaMovingLeftDown = new ControlPattern(false);
        parabolaMovingLeftDown.add(ControlAcceleration.create(0, 0.0005, 0));
        parabolaMovingLeftDown.add(ControlVelocity.create(0, -0.7, 0.1));
        
        rectangle2CornersMovingLeftUp = new ControlPattern(false);
        rectangle2CornersMovingLeftUp.add(ControlVelocity.create(0, -0.4, 0));
        rectangle2CornersMovingLeftUp.add(ControlAcceleration.createWithTerminalVelocityHalted(800, 0.003, -0.003, 0, -0.4));
        rectangle2CornersMovingLeftUp.add(ControlAcceleration.createWithTerminalVelocityHalted(1000, 0.003, 0.003, 0.4, 0));
        
        rectangle2CornersMovingLeftDown = new ControlPattern(false);
        rectangle2CornersMovingLeftDown.add(ControlVelocity.create(0, -0.4, 0));
        rectangle2CornersMovingLeftDown.add(ControlAcceleration.createWithTerminalVelocityHalted(800, 0.003, 0.003, 0, 0.4));
        rectangle2CornersMovingLeftDown.add(ControlAcceleration.createWithTerminalVelocityHalted(1000, 0.003, -0.003, 0.4, 0));
        
        rectangle4CornersMovingLeftUp = new ControlPattern(false);
        rectangle4CornersMovingLeftUp.add(ControlVelocity.create(0, -0.4, 0));
        rectangle4CornersMovingLeftUp.add(ControlAcceleration.createWithTerminalVelocityHalted(800, 0.003, -0.003, 0, -0.4));
        rectangle4CornersMovingLeftUp.add(ControlAcceleration.createWithTerminalVelocityHalted(1100, 0.003, 0.003, 0.4, 0));
        rectangle4CornersMovingLeftUp.add(ControlAcceleration.createWithTerminalVelocityHalted(1400, -0.003, 0.003, 0, 0.4));
        rectangle4CornersMovingLeftUp.add(ControlAcceleration.createWithTerminalVelocityHalted(1700, -0.003, -0.003, -0.4, 0));
        
        rectangle4CornersMovingLeftDown = new ControlPattern(false);
        rectangle4CornersMovingLeftDown.add(ControlVelocity.create(0, -0.4, 0));
        rectangle4CornersMovingLeftDown.add(ControlAcceleration.createWithTerminalVelocityHalted(800, 0.003, 0.003, 0, 0.4));
        rectangle4CornersMovingLeftDown.add(ControlAcceleration.createWithTerminalVelocityHalted(1100, 0.003, -0.003, 0.4, 0));
        rectangle4CornersMovingLeftDown.add(ControlAcceleration.createWithTerminalVelocityHalted(1400, -0.003, -0.003, 0, -0.4));
        rectangle4CornersMovingLeftDown.add(ControlAcceleration.createWithTerminalVelocityHalted(1700, -0.003, 0.003, -0.4, 0));
        
        diamond1CornerMovingLeftUp = new ControlPattern(false);
        diamond1CornerMovingLeftUp.add(ControlVelocity.create(0, -0.3, -0.3));
        diamond1CornerMovingLeftUp.add(ControlAcceleration.createWithTerminalVelocity(1000, 0, 0.003, Double.NaN, 0.3));
        
        diamond1CornerMovingLeftDown = new ControlPattern(false);
        diamond1CornerMovingLeftDown.add(ControlVelocity.create(0, -0.3, 0.3));
        diamond1CornerMovingLeftDown.add(ControlAcceleration.createWithTerminalVelocity(1000, 0, -0.003, Double.NaN, -0.3));
        
        diamond3CornersMovingLeftUp = new ControlPattern(false);
        diamond3CornersMovingLeftUp.add(ControlVelocity.create(0, -0.3, -0.2));
        diamond3CornersMovingLeftUp.add(ControlAcceleration.createWithTerminalVelocity(1600, 0.003, 0, 0.3, Double.NaN));
        diamond3CornersMovingLeftUp.add(ControlAcceleration.createWithTerminalVelocity(2000, 0, 0.003, Double.NaN, 0.2));
        diamond3CornersMovingLeftUp.add(ControlAcceleration.createWithTerminalVelocity(2400, -0.003, 0, -0.3, Double.NaN));
        
        diamond3CornersMovingLeftDown = new ControlPattern(false);
        diamond3CornersMovingLeftDown.add(ControlVelocity.create(0, -0.3, 0.2));
        diamond3CornersMovingLeftDown.add(ControlAcceleration.createWithTerminalVelocity(1600, 0.003, 0, 0.3, Double.NaN));
        diamond3CornersMovingLeftDown.add(ControlAcceleration.createWithTerminalVelocity(2000, 0, -0.003, Double.NaN, -0.2));
        diamond3CornersMovingLeftDown.add(ControlAcceleration.createWithTerminalVelocity(2400, -0.003, 0, -0.3, Double.NaN));
        
        staircase1StepMovingLeftDown = new ControlPattern();
        staircase1StepMovingLeftDown.add(ControlVelocity.create(0, -0.4, 0));
        staircase1StepMovingLeftDown.add(ControlAcceleration.createWithTerminalVelocityHalted(500, 0.002, 0.002, 0, 0.4));
        staircase1StepMovingLeftDown.add(ControlAcceleration.createWithTerminalVelocity(750, -0.001, -0.001, -0.4, 0));
        
        staircase5StepsMovingLeftDown = new ControlPattern();
        staircase5StepsMovingLeftDown.add(ControlVelocity.create(0, -0.4, 0));
        staircase5StepsMovingLeftDown.add(ControlAcceleration.createWithTerminalVelocityHalted(400, 0.001, 0.001, 0, 0.4));
        staircase5StepsMovingLeftDown.add(ControlAcceleration.createWithTerminalVelocityHalted(400, -0.001, -0.001, -0.4, 0));
        staircase5StepsMovingLeftDown.add(ControlAcceleration.createWithTerminalVelocityHalted(400, 0.001, 0.001, 0, 0.4));
        staircase5StepsMovingLeftDown.add(ControlAcceleration.createWithTerminalVelocityHalted(400, -0.001, -0.001, -0.4, 0));
        staircase5StepsMovingLeftDown.add(ControlAcceleration.createWithTerminalVelocityHalted(400, 0.001, 0.001, 0, 0.4));
        staircase5StepsMovingLeftDown.add(ControlAcceleration.createWithTerminalVelocityHalted(400, -0.001, -0.001, -0.4, 0));
        staircase5StepsMovingLeftDown.add(ControlAcceleration.createWithTerminalVelocityHalted(400, 0.001, 0.001, 0, 0.4));
        staircase5StepsMovingLeftDown.add(ControlAcceleration.createWithTerminalVelocityHalted(400, -0.001, -0.001, -0.4, 0));
        staircase5StepsMovingLeftDown.add(ControlAcceleration.createWithTerminalVelocityHalted(400, 0.001, 0.001, 0, 0.4));
        staircase5StepsMovingLeftDown.add(ControlAcceleration.createWithTerminalVelocityHalted(400, -0.001, -0.001, -0.4, 0));
        
        staircase1StepMovingLeftUp = new ControlPattern();
        staircase1StepMovingLeftUp.add(ControlVelocity.create(0, -0.4, 0));
        staircase1StepMovingLeftUp.add(ControlAcceleration.createWithTerminalVelocityHalted(500, 0.002, -0.002, 0, -0.4));
        staircase1StepMovingLeftUp.add(ControlAcceleration.createWithTerminalVelocity(750, -0.002, 0.002, -0.4, 0));
        
        staircase5StepsMovingLeftUp = new ControlPattern();
        staircase5StepsMovingLeftUp.add(ControlVelocity.create(0, -0.4, 0));
        staircase5StepsMovingLeftUp.add(ControlAcceleration.createWithTerminalVelocityHalted(400, 0.001, -0.001, 0, -0.4));
        staircase5StepsMovingLeftUp.add(ControlAcceleration.createWithTerminalVelocityHalted(400, -0.001, 0.001, -0.4, 0));
        staircase5StepsMovingLeftUp.add(ControlAcceleration.createWithTerminalVelocityHalted(400, 0.001, -0.001, 0, -0.4));
        staircase5StepsMovingLeftUp.add(ControlAcceleration.createWithTerminalVelocityHalted(400, -0.001, 0.001, -0.4, 0));
        staircase5StepsMovingLeftUp.add(ControlAcceleration.createWithTerminalVelocityHalted(400, 0.001, -0.001, 0, -0.4));
        staircase5StepsMovingLeftUp.add(ControlAcceleration.createWithTerminalVelocityHalted(400, -0.001, 0.001, -0.4, 0));
        staircase5StepsMovingLeftUp.add(ControlAcceleration.createWithTerminalVelocityHalted(400, 0.001, -0.001, 0, -0.4));
        staircase5StepsMovingLeftUp.add(ControlAcceleration.createWithTerminalVelocityHalted(400, -0.001, 0.001, -0.4, 0));
        staircase5StepsMovingLeftUp.add(ControlAcceleration.createWithTerminalVelocityHalted(400, 0.001, -0.001, 0, -0.4));
        staircase5StepsMovingLeftUp.add(ControlAcceleration.createWithTerminalVelocityHalted(400, -0.001, 0.001, -0.4, 0));
        
        staircase5StepsMovingRightUp = new ControlPattern();
        staircase5StepsMovingRightUp.add(ControlVelocity.create(0, 0.4, 0));
        staircase5StepsMovingRightUp.add(ControlAcceleration.createWithTerminalVelocityHalted(0, -0.001, -0.001, 0, -0.4));
        staircase5StepsMovingRightUp.add(ControlAcceleration.createWithTerminalVelocityHalted(0, 0.001, 0.001, 0.4, 0));
        staircase5StepsMovingRightUp.add(ControlAcceleration.createWithTerminalVelocityHalted(0, -0.001, -0.001, 0, -0.4));
        staircase5StepsMovingRightUp.add(ControlAcceleration.createWithTerminalVelocityHalted(0, 0.001, 0.001, 0.4, 0));
        staircase5StepsMovingRightUp.add(ControlAcceleration.createWithTerminalVelocityHalted(0, -0.001, -0.001, 0, -0.4));
        staircase5StepsMovingRightUp.add(ControlAcceleration.createWithTerminalVelocityHalted(0, 0.001, 0.001, 0.4, 0));
        staircase5StepsMovingRightUp.add(ControlAcceleration.createWithTerminalVelocityHalted(0, -0.001, -0.001, 0, -0.4));
        staircase5StepsMovingRightUp.add(ControlAcceleration.createWithTerminalVelocityHalted(0, 0.001, 0.001, 0.4, 0));
        staircase5StepsMovingRightUp.add(ControlAcceleration.createWithTerminalVelocityHalted(0, -0.001, -0.001, 0, -0.4));
        staircase5StepsMovingRightUp.add(ControlAcceleration.createWithTerminalVelocityHalted(0, 0.001, 0.001, 0.4, 0));
        
        zigzag5CornersMovingLeftDown = new ControlPattern();
        zigzag5CornersMovingLeftDown.add(ControlVelocity.create(0, -0.1, 0.1));
        zigzag5CornersMovingLeftDown.add(ControlVelocity.create(900, -0.1, -0.1));
        zigzag5CornersMovingLeftDown.add(ControlVelocity.create(1900, -0.1, 0.1));
        zigzag5CornersMovingLeftDown.add(ControlVelocity.create(2700, -0.1, -0.1));
        zigzag5CornersMovingLeftDown.add(ControlVelocity.create(3600, -0.1, 0.1));
        zigzag5CornersMovingLeftDown.add(ControlVelocity.create(4600, -0.1, -0.1));
        
        squarewaveLargeAmplitudeMovingLeft = new ControlPattern(true);
        squarewaveLargeAmplitudeMovingLeft.add(ControlVelocity.create(0, -0.15, 0));
        squarewaveLargeAmplitudeMovingLeft.add(ControlVelocity.create(400, 0, 0.15));
        squarewaveLargeAmplitudeMovingLeft.add(ControlVelocity.create(1400, -0.15, 0));
        squarewaveLargeAmplitudeMovingLeft.add(ControlVelocity.create(1800, 0, -0.15));
        squarewaveLargeAmplitudeMovingLeft.add(ControlNull.create(2800));
        
        squarewaveLargeAmplitudeMovingRight = new ControlPattern(true);
        squarewaveLargeAmplitudeMovingRight.add(ControlVelocity.create(0, 0.15, 0));
        squarewaveLargeAmplitudeMovingRight.add(ControlVelocity.create(400, 0, -0.15));
        squarewaveLargeAmplitudeMovingRight.add(ControlVelocity.create(1400, 0.15, 0));
        squarewaveLargeAmplitudeMovingRight.add(ControlVelocity.create(1800, 0, 0.15));
        squarewaveLargeAmplitudeMovingRight.add(ControlNull.create(2800));
        
        squarewaveLargeAmplitudeMovingUp = new ControlPattern(true);
        squarewaveLargeAmplitudeMovingUp.add(ControlVelocity.create(0, 0, -0.15));
        squarewaveLargeAmplitudeMovingUp.add(ControlVelocity.create(400, 0.15, 0));
        squarewaveLargeAmplitudeMovingUp.add(ControlVelocity.create(1400, 0, -0.15));
        squarewaveLargeAmplitudeMovingUp.add(ControlVelocity.create(1800, -0.15, 0));
        squarewaveLargeAmplitudeMovingUp.add(ControlNull.create(2800));
        
        squarewaveLargeAmplitudeMovingDown = new ControlPattern(true);
        squarewaveLargeAmplitudeMovingDown.add(ControlVelocity.create(0, 0, 0.15));
        squarewaveLargeAmplitudeMovingDown.add(ControlVelocity.create(400, -0.15, 0));
        squarewaveLargeAmplitudeMovingDown.add(ControlVelocity.create(1400, 0, 0.15));
        squarewaveLargeAmplitudeMovingDown.add(ControlVelocity.create(1800, 0.15, 0));
        squarewaveLargeAmplitudeMovingDown.add(ControlNull.create(2800));
        
        squarewaveLargeAmplitudeMovingRightThenLeft = new ControlPattern(1);
        squarewaveLargeAmplitudeMovingRightThenLeft.add(ControlVelocity.createWithTerminalHalted(0, 0.15, 0, 0.75, Double.NaN));
        squarewaveLargeAmplitudeMovingRightThenLeft.add(ControlVelocity.create(0, 0, -0.15));
        squarewaveLargeAmplitudeMovingRightThenLeft.add(ControlVelocity.create(1000, -0.15, 0));
        squarewaveLargeAmplitudeMovingRightThenLeft.add(ControlVelocity.create(1400, 0, 0.15));
        squarewaveLargeAmplitudeMovingRightThenLeft.add(ControlVelocity.create(2400, -0.15, 0));
        squarewaveLargeAmplitudeMovingRightThenLeft.add(ControlNull.create(2800));
        
        sawtoothLargeAmplituteMovingLeft = new ControlPattern(true);
        sawtoothLargeAmplituteMovingLeft.add(ControlVelocity.create(0, -0.1, 0));
        sawtoothLargeAmplituteMovingLeft.add(ControlAcceleration.createWithTerminalVelocityHalted(0, 0, 0.0003, Double.NaN, 0.35));
        sawtoothLargeAmplituteMovingLeft.add(ControlVelocity.create(0, 0.1, 0));
        sawtoothLargeAmplituteMovingLeft.add(ControlVelocity.createWithTerminal(500, 0, -0.1, Double.NaN, 0.05));
        sawtoothLargeAmplituteMovingLeft.add(ControlNull.create(2700));
        
        shmMovingLeft = new ControlPattern(true);
        shmMovingLeft.add(ControlVelocity.create(0, -0.1, 0));
        shmMovingLeft.add(ControlAcceleration.create(0, 0, 0.0007));
        shmMovingLeft.add(ControlAcceleration.create(500, 0, 0));
        shmMovingLeft.add(ControlAcceleration.create(1000, 0, -0.0015));
        shmMovingLeft.add(ControlAcceleration.create(1500, 0, 0));
        shmMovingLeft.add(ControlAcceleration.create(2000, 0, 0));
        
        shmInfinitessimalAmplitudeMovingLeft = new ControlPattern(1);
        shmInfinitessimalAmplitudeMovingLeft.add(ControlVelocity.createWithRandom(0, -0.1, 0, 0.02, 0));
        shmInfinitessimalAmplitudeMovingLeft.add(ControlAcceleration.createWithTerminalVelocityHalted(0, 0, 0.0006, Double.NaN, 0.05));
        shmInfinitessimalAmplitudeMovingLeft.add(ControlAcceleration.create(0, 0, 0));
        shmInfinitessimalAmplitudeMovingLeft.add(ControlAcceleration.createWithTerminalVelocityHalted(250, 0, -0.0012, Double.NaN, -0.05));
        shmInfinitessimalAmplitudeMovingLeft.add(ControlAcceleration.create(250, 0, 0));
        shmInfinitessimalAmplitudeMovingLeft.add(ControlNull.create(500));
        
        shmTinyAmplitudeMovingLeft = new ControlPattern(1);
        shmTinyAmplitudeMovingLeft.add(ControlVelocity.createWithRandom(0, -0.09, 0, 0.02, 0));
        shmTinyAmplitudeMovingLeft.add(ControlAcceleration.createWithTerminalVelocityHalted(0, 0, 0.0006, Double.NaN, 0.15));
        shmTinyAmplitudeMovingLeft.add(ControlAcceleration.create(0, 0, 0));
        shmTinyAmplitudeMovingLeft.add(ControlAcceleration.createWithTerminalVelocityHalted(250, 0, -0.0012, Double.NaN, -0.15));
        shmTinyAmplitudeMovingLeft.add(ControlAcceleration.create(250, 0, 0));
        shmTinyAmplitudeMovingLeft.add(ControlNull.create(500));
        
        shmSmallAmplitudeMovingLeft = new ControlPattern(1);
        shmSmallAmplitudeMovingLeft.add(ControlVelocity.createWithRandom(0, -0.09, 0, 0.02, 0));
        shmSmallAmplitudeMovingLeft.add(ControlAcceleration.createWithTerminalVelocityHalted(0, 0, 0.0007, Double.NaN, 0.3));
        shmSmallAmplitudeMovingLeft.add(ControlAcceleration.create(0, 0, 0));
        shmSmallAmplitudeMovingLeft.add(ControlAcceleration.createWithTerminalVelocityHalted(250, 0, -0.0014, Double.NaN, -0.3));
        shmSmallAmplitudeMovingLeft.add(ControlAcceleration.create(250, 0, 0));
        shmSmallAmplitudeMovingLeft.add(ControlNull.create(500));
        
        shmMediumAmplitudeMovingLeft = new ControlPattern(1);
        shmMediumAmplitudeMovingLeft.add(ControlVelocity.createWithRandom(0, -0.08, 0, 0.02, 0));
        shmMediumAmplitudeMovingLeft.add(ControlAcceleration.createWithTerminalVelocityHalted(0, 0, 0.0005, Double.NaN, 0.28));
        shmMediumAmplitudeMovingLeft.add(ControlAcceleration.create(0, 0, 0));
        shmMediumAmplitudeMovingLeft.add(ControlAcceleration.createWithTerminalVelocityHalted(250, 0, -0.001, Double.NaN, -0.28));
        shmMediumAmplitudeMovingLeft.add(ControlAcceleration.create(250, 0, 0));
        shmMediumAmplitudeMovingLeft.add(ControlNull.create(500));
        
        shmLargeAmplitudeMovingLeft = new ControlPattern(1);
        shmLargeAmplitudeMovingLeft.add(ControlVelocity.createWithRandom(0, -0.08, 0, 0.02, 0));
        shmLargeAmplitudeMovingLeft.add(ControlAcceleration.createWithTerminalVelocityHalted(0, 0, 0.0005, Double.NaN, 0.4));
        shmLargeAmplitudeMovingLeft.add(ControlAcceleration.create(0, 0, 0));
        shmLargeAmplitudeMovingLeft.add(ControlAcceleration.createWithTerminalVelocityHalted(250, 0, -0.001, Double.NaN, -0.4));
        shmLargeAmplitudeMovingLeft.add(ControlAcceleration.create(250, 0, 0));
        shmLargeAmplitudeMovingLeft.add(ControlNull.create(500));
        
        shmHugeAmplitudeMovingLeft = new ControlPattern(1);
        shmHugeAmplitudeMovingLeft.add(ControlVelocity.createWithRandom(0, -0.08, 0, 0.02, 0));
        shmHugeAmplitudeMovingLeft.add(ControlAcceleration.createWithTerminalVelocityHalted(0, 0, 0.0003, Double.NaN, 0.4));
        shmHugeAmplitudeMovingLeft.add(ControlAcceleration.create(0, 0, 0));
        shmHugeAmplitudeMovingLeft.add(ControlAcceleration.createWithTerminalVelocityHalted(250, 0, -0.0006, Double.NaN, -0.4));
        shmHugeAmplitudeMovingLeft.add(ControlAcceleration.create(250, 0, 0));
        shmHugeAmplitudeMovingLeft.add(ControlNull.create(500));
        
        loopSingleUpMovingLeft = new ControlPattern();
        loopSingleUpMovingLeft.add(ControlVelocity.create(0, -0.4, 0));
        loopSingleUpMovingLeft.add(ControlAcceleration.createWithTerminalVelocityHalted(500, 0.001, -0.001, 0, -0.4));
        loopSingleUpMovingLeft.add(ControlAcceleration.createWithTerminalVelocityHalted(500, 0.001, 0.001, 0.4, 0));
        loopSingleUpMovingLeft.add(ControlAcceleration.createWithTerminalVelocityHalted(500, -0.001, 0.001, 0, 0.4));
        loopSingleUpMovingLeft.add(ControlAcceleration.createWithTerminalVelocityHalted(500, -0.001, -0.001, -0.4, 0));
        
        loopSingleDownMovingLeft = new ControlPattern();
        loopSingleDownMovingLeft.add(ControlVelocity.create(0, -0.4, 0));
        loopSingleDownMovingLeft.add(ControlAcceleration.createWithTerminalVelocityHalted(500, 0.001, 0.001, 0, 0.4));
        loopSingleDownMovingLeft.add(ControlAcceleration.createWithTerminalVelocityHalted(500, 0.001, -0.001, 0.4, 0));
        loopSingleDownMovingLeft.add(ControlAcceleration.createWithTerminalVelocityHalted(500, -0.001, -0.001, 0, -0.4));
        loopSingleDownMovingLeft.add(ControlAcceleration.createWithTerminalVelocityHalted(500, -0.001, 0.001, -0.4, 0));
        
        loopXSingleRightMovingLeftUp = new ControlPattern();
        loopXSingleRightMovingLeftUp.add(ControlVelocity.create(0, -0.4, -0.4));
        loopXSingleRightMovingLeftUp.add(ControlAcceleration.createWithTerminalVelocityHalted(700, 0, 0.001, Double.NaN, 0));
        loopXSingleRightMovingLeftUp.add(ControlAcceleration.createWithTerminalVelocityHalted(700, 0.001, 0.001, 0, 0.4));
        loopXSingleRightMovingLeftUp.add(ControlAcceleration.createWithTerminalVelocityHalted(700, 0.001, -0.001, 0.4, 0));
        loopXSingleRightMovingLeftUp.add(ControlAcceleration.createWithTerminalVelocityHalted(700, 0, -0.001, Double.NaN, -0.4));
        
        loopXSingleRightMovingLeftDown = new ControlPattern();
        loopXSingleRightMovingLeftDown.add(ControlVelocity.create(0, -0.4, 0.4));
        loopXSingleRightMovingLeftDown.add(ControlAcceleration.createWithTerminalVelocityHalted(700, 0, -0.001, Double.NaN, 0));
        loopXSingleRightMovingLeftDown.add(ControlAcceleration.createWithTerminalVelocityHalted(700, 0.001, -0.001, 0, -0.4));
        loopXSingleRightMovingLeftDown.add(ControlAcceleration.createWithTerminalVelocityHalted(700, 0.001, 0.001, 0.4, 0));
        loopXSingleRightMovingLeftDown.add(ControlAcceleration.createWithTerminalVelocityHalted(700, 0, 0.001, Double.NaN, 0.4));
        
        loopXSingleRightMovingLeftDownFiringHomingCircle = new ControlPattern();
        loopXSingleRightMovingLeftDownFiringHomingCircle.add(ControlVelocity.create(0, -0.4, 0.4));
        loopXSingleRightMovingLeftDownFiringHomingCircle.add(ControlAcceleration.createWithTerminalVelocityHalted(200, 0.001, 0, 0, Double.NaN));
        loopXSingleRightMovingLeftDownFiringHomingCircle.add(ControlAcceleration.createWithTerminalVelocityHalted(200, 0.001, -0.001, 0.4, 0));
        loopXSingleRightMovingLeftDownFiringHomingCircle.add(ControlFiring.createHomingWithRandom(200, EnemyType.FIRING_CIRCLE, 0.3, 20, 0.05));
        loopXSingleRightMovingLeftDownFiringHomingCircle.add(ControlAcceleration.createWithTerminalVelocityHalted(200, -0.001, -0.001, 0, -0.4));
        loopXSingleRightMovingLeftDownFiringHomingCircle.add(ControlAcceleration.createWithTerminalVelocityHalted(200, -0.001, 0, -0.4, Double.NaN));
        
        loopVSingleRightMovingLeftUp = new ControlPattern();
        loopVSingleRightMovingLeftUp.add(ControlVelocity.create(0, -0.4, -0.3));
        loopVSingleRightMovingLeftUp.add(ControlAcceleration.createWithTerminalVelocityHalted(600, 0.001, 0, 0, Double.NaN));
        loopVSingleRightMovingLeftUp.add(ControlAcceleration.createWithTerminalVelocityHalted(600, 0.001, 0.001, 0.4, 0));
        loopVSingleRightMovingLeftUp.add(ControlAcceleration.createWithTerminalVelocityHalted(600, -0.001, 0.001, 0, 0.4));
        loopVSingleRightMovingLeftUp.add(ControlAcceleration.createWithTerminalVelocityHalted(600, -0.001, -0.001, -0.4, 0));
        loopVSingleRightMovingLeftUp.add(ControlAcceleration.createWithTerminalVelocityHalted(600, 0.001, -0.001, 0, -0.4));
        loopVSingleRightMovingLeftUp.add(ControlAcceleration.createWithTerminalVelocityHalted(600, 0.001, 0, 0.4, Double.NaN));
        
        loopVSingleRightMovingLeftDown = new ControlPattern();
        loopVSingleRightMovingLeftDown.add(ControlVelocity.create(0, -0.4, 0.3));
        loopVSingleRightMovingLeftDown.add(ControlAcceleration.createWithTerminalVelocityHalted(600, 0.001, 0, 0, Double.NaN));
        loopVSingleRightMovingLeftDown.add(ControlAcceleration.createWithTerminalVelocityHalted(600, 0.001, -0.001, 0.4, 0));
        loopVSingleRightMovingLeftDown.add(ControlAcceleration.createWithTerminalVelocityHalted(600, -0.001, -0.001, 0, -0.4));
        loopVSingleRightMovingLeftDown.add(ControlAcceleration.createWithTerminalVelocityHalted(600, -0.001, 0.001, -0.4, 0));
        loopVSingleRightMovingLeftDown.add(ControlAcceleration.createWithTerminalVelocityHalted(600, 0.001, 0.001, 0, 0.4));
        loopVSingleRightMovingLeftDown.add(ControlAcceleration.createWithTerminalVelocityHalted(600, 0.001, 0, 0.4, Double.NaN));
        
        diamondFire4Bullets = new ControlPattern(false);
        diamondFire4Bullets.add(ControlVelocity.create(1000, -0.1, -0.1));
        diamondFire4Bullets.add(ControlFiring.createHoming(1500, EnemyType.FIRING_CIRCLE, 0.3));
        diamondFire4Bullets.add(ControlVelocity.create(2000, -0.1, 0.1));
        diamondFire4Bullets.add(ControlFiring.createHoming(2500, EnemyType.FIRING_CIRCLE, 0.3));
        diamondFire4Bullets.add(ControlVelocity.create(3000, 0.1, 0.1));
        diamondFire4Bullets.add(ControlFiring.createHoming(3500, EnemyType.FIRING_CIRCLE, 0.3));
        diamondFire4Bullets.add(ControlVelocity.create(4000, 0.1, -0.1));
        diamondFire4Bullets.add(ControlFiring.createHoming(4500, EnemyType.FIRING_CIRCLE, 0.3));
        diamondFire4Bullets.add(ControlVelocity.create(5000, -0.1, 0));
        diamondFire4Bullets.add(ControlAcceleration.createWithTerminalVelocity(5000, -0.0005, 0, -0.4, Double.NaN));
        
        stopgoDownMovingLeft = new ControlPattern(false);
        stopgoDownMovingLeft.add(ControlVelocity.create(0, -0.4, 0.1));
        stopgoDownMovingLeft.add(ControlAcceleration.createWithTerminalVelocity(0, 0.0004, 0, 0, Double.NaN));
        stopgoDownMovingLeft.add(ControlFiring.createWithXYRandom(500, EnemyType.FIRING_MISSILE, -0.3, 0, -0.05, 0));
        stopgoDownMovingLeft.add(ControlFiring.createWithXYRandom(800, EnemyType.FIRING_MISSILE, -0.3, 0, -0.05, 0));
        stopgoDownMovingLeft.add(ControlFiring.createWithXYRandom(1300, EnemyType.FIRING_MISSILE, -0.3, 0, -0.05, 0));
        stopgoDownMovingLeft.add(ControlFiring.createWithXYRandom(1600, EnemyType.FIRING_MISSILE, -0.3, 0, -0.05, 0));
        stopgoDownMovingLeft.add(ControlAcceleration.createWithTerminalVelocity(2000, -0.0008, 0, -0.4, Double.NaN));
        
        stopgoUpMovingLeft = new ControlPattern(false);
        stopgoUpMovingLeft.add(ControlVelocity.create(0, -0.4, -0.1));
        stopgoUpMovingLeft.add(ControlAcceleration.createWithTerminalVelocity(0, 0.0004, 0, 0, Double.NaN));
        stopgoUpMovingLeft.add(ControlFiring.createWithXYRandom(500, EnemyType.FIRING_MISSILE, -0.3, 0, -0.05, 0));
        stopgoUpMovingLeft.add(ControlFiring.createWithXYRandom(800, EnemyType.FIRING_MISSILE, -0.3, 0, -0.05, 0));
        stopgoUpMovingLeft.add(ControlFiring.createWithXYRandom(1300, EnemyType.FIRING_MISSILE, -0.3, 0, -0.05, 0));
        stopgoUpMovingLeft.add(ControlFiring.createWithXYRandom(1600, EnemyType.FIRING_MISSILE, -0.3, 0, -0.05, 0));
        stopgoUpMovingLeft.add(ControlAcceleration.createWithTerminalVelocity(2000, -0.0008, 0, -0.4, Double.NaN));
        
        stopgoDownUpMovingLeft = new ControlPattern(false);
        stopgoDownUpMovingLeft.add(ControlVelocity.create(0, -0.4, 0.1));
        stopgoDownUpMovingLeft.add(ControlAcceleration.createWithTerminalVelocity(0, 0.0004, 0, 0, Double.NaN));
        stopgoDownUpMovingLeft.add(ControlFiring.createWithXYRandom(500, EnemyType.FIRING_MISSILE, -0.3, 0, -0.05, 0));
        stopgoDownUpMovingLeft.add(ControlFiring.createWithXYRandom(800, EnemyType.FIRING_MISSILE, -0.3, 0, -0.05, 0));
        stopgoDownUpMovingLeft.add(ControlAcceleration.create(1000, 0, 0));
        stopgoDownUpMovingLeft.add(ControlVelocity.create(1000, 0, -0.15));
        stopgoDownUpMovingLeft.add(ControlFiring.createWithXYRandom(1300, EnemyType.FIRING_MISSILE, -0.3, 0, -0.05, 0));
        stopgoDownUpMovingLeft.add(ControlFiring.createWithXYRandom(1600, EnemyType.FIRING_MISSILE, -0.3, 0, -0.05, 0));
        stopgoDownUpMovingLeft.add(ControlAcceleration.createWithTerminalVelocity(2000, -0.0008, 0, -0.4, Double.NaN));
        
        plane3CornersMovingLeft = new ControlPattern();
        plane3CornersMovingLeft.add(ControlVelocity.create(0, -0.5, 0.25));
        plane3CornersMovingLeft.add(ControlAcceleration.createWithTerminalVelocity(0, 0.0004, -0.0002, -0.2, 0.1));
        plane3CornersMovingLeft.add(ControlVelocity.create(800, 0.15, 0.1));
        plane3CornersMovingLeft.add(ControlVelocity.create(1200, 0.15, -0.1));
        plane3CornersMovingLeft.add(ControlVelocity.create(1600, -0.2, 0));
        plane3CornersMovingLeft.add(ControlAcceleration.createWithTerminalVelocity(2000, -0.0005, 0, -0.6, Double.NaN));
        
        plane5CornersMovingLeft = new ControlPattern();
        plane5CornersMovingLeft.add(ControlVelocity.create(0, -0.5, 0.25));
        plane5CornersMovingLeft.add(ControlAcceleration.createWithTerminalVelocity(0, 0.0004, -0.0002, -0.2, 0.1));
        plane5CornersMovingLeft.add(ControlVelocity.create(800, 0.15, 0.1));
        plane5CornersMovingLeft.add(ControlVelocity.create(1200, 0, -0.2));
        plane5CornersMovingLeft.add(ControlVelocity.create(1600, -0.15, 0.1));
        plane5CornersMovingLeft.add(ControlVelocity.create(2000, 0.2, 0));
        plane5CornersMovingLeft.add(ControlVelocity.create(2400, -0.2, -0.1));
        plane5CornersMovingLeft.add(ControlAcceleration.createWithTerminalVelocity(3200, -0.0004, -0.0002, -0.5, -0.25));
        
        plane6CornersMovingLeft = new ControlPattern();
        plane6CornersMovingLeft.add(ControlVelocity.create(0, -0.5, 0.25));
        plane6CornersMovingLeft.add(ControlAcceleration.createWithTerminalVelocity(0, 0.0004, -0.0002, -0.2, 0.1));
        plane6CornersMovingLeft.add(ControlVelocity.create(800, 0.2, 0));
        plane6CornersMovingLeft.add(ControlVelocity.create(1200, -0.15, 0.1));
        plane6CornersMovingLeft.add(ControlVelocity.create(1600, 0.15, 0.1));
        plane6CornersMovingLeft.add(ControlVelocity.create(2000, 0.15, -0.1));
        plane6CornersMovingLeft.add(ControlVelocity.create(2400, 0, 0.2));
        plane6CornersMovingLeft.add(ControlVelocity.create(2800, -0.2, -0.1));
        plane6CornersMovingLeft.add(ControlAcceleration.createWithTerminalVelocity(3600, -0.0004, -0.0002, -0.5, -0.25));
        
        
        /* Boss movement patterns */
        bossSupervisorMovement = new ControlPattern(2);
        bossSupervisorMovement.add(ControlVelocity.create(3000, -0.3, 0));
        bossSupervisorMovement.add(ControlContainment.create(4000, 0.2, 0.05, 0.8, 0.6));
        bossSupervisorMovement.add(ControlVelocity.create(4000, -0.3, 0.1));
        bossSupervisorMovement.add(ControlVelocity.createWithTerminal(4500, 0.1, 0, 0.6, Double.NaN));
        bossSupervisorMovement.add(ControlVelocity.createWithTerminal(6000, -0.3, -0.1, Double.NaN, 0.1));
        bossSupervisorMovement.add(ControlVelocity.createWithTerminal(6500, 0.1, 0, 0.6, Double.NaN));
        bossSupervisorMovement.add(ControlNull.create(8000));
        
        bossTeamLeaderMovement = new ControlPattern(2);
        bossTeamLeaderMovement.add(ControlVelocity.create(3000, -0.4, 0));
        bossTeamLeaderMovement.add(ControlContainment.create(3650, 0.2, 0.1, 0.7, 0.6));
        bossTeamLeaderMovement.add(ControlAcceleration.createWithTerminalVelocityHalted(3650, 0.002, 0.002, -0.283, 0.2));
        bossTeamLeaderMovement.add(ControlAcceleration.createWithTerminalVelocityHalted(4000, -0.002, -0.002, -0.4, 0));
        bossTeamLeaderMovement.add(ControlAcceleration.createWithTerminalVelocityHalted(4000, 0.002, -0.002, 0, -0.4));
        bossTeamLeaderMovement.add(ControlAcceleration.createWithTerminalVelocityHalted(4000, 0.002, 0.002, 0.4, 0));
        bossTeamLeaderMovement.add(ControlAcceleration.createWithTerminalVelocityHalted(4000, -0.002, 0.002, 0, 0.4));
        bossTeamLeaderMovement.add(ControlAcceleration.createWithTerminalVelocityHalted(4000, -0.002, -0.002, -0.4, 0));
        bossTeamLeaderMovement.add(ControlAcceleration.createWithTerminalVelocityHalted(4000, 0.002, -0.002, 0, -0.4));
        bossTeamLeaderMovement.add(ControlAcceleration.createWithTerminalVelocityHalted(4000, 0.002, 0.002, 0.4, 0));
        bossTeamLeaderMovement.add(ControlAcceleration.createWithTerminalVelocityHalted(4000, -0.002, 0.002, 0.3, 0.2));
        bossTeamLeaderMovement.add(ControlAcceleration.createWithTerminalVelocityHalted(4500, 0.002, -0.002, 0.4, 0));
        bossTeamLeaderMovement.add(ControlAcceleration.createWithTerminalVelocityHalted(4500, -0.002, -0.002, 0, -0.4));
        bossTeamLeaderMovement.add(ControlAcceleration.createWithTerminalVelocityHalted(4500, -0.002, 0.002, -0.4, 0));
        bossTeamLeaderMovement.add(ControlAcceleration.createWithTerminalVelocityHalted(4500, 0.002, 0.002, 0, 0.4));
        bossTeamLeaderMovement.add(ControlAcceleration.createWithTerminalVelocityHalted(4500, 0.002, -0.002, 0.4, 0));
        bossTeamLeaderMovement.add(ControlAcceleration.createWithTerminalVelocityHalted(4500, -0.002, -0.002, 0, -0.4));
        bossTeamLeaderMovement.add(ControlAcceleration.createWithTerminalVelocityHalted(4500, -0.002, 0.002, -0.4, 0));
        
        bossNetworksManagerMovement = new ControlPattern(2);
        bossNetworksManagerMovement.add(ControlVelocity.createWithTerminalHalted(2500, -0.2, 0.1, 0.85, 0.05));
        bossNetworksManagerMovement.add(ControlContainment.create(2500, -0.1, 0, 1, 1));
        bossNetworksManagerMovement.add(ControlVelocity.create(2500, -0.4, 0));
        bossNetworksManagerMovement.add(ControlAcceleration.createWithTerminalVelocityHalted(3000, 0, 0.002, Double.NaN, 0.5));
        bossNetworksManagerMovement.add(ControlAcceleration.createWithTerminalVelocityHalted(3000, -0.001, -0.001, -0.5, 0));
        bossNetworksManagerMovement.add(ControlAcceleration.createWithTerminalVelocityHalted(3000, 0.002, -0.002, 0, -0.5));
        bossNetworksManagerMovement.add(ControlAcceleration.createWithTerminalVelocityHalted(3000, 0.002, 0.002, 0.5, 0.2));
        bossNetworksManagerMovement.add(ControlAcceleration.createWithTerminalVelocityHalted(4000, -0.002, 0.002, 0, 0.5));
        bossNetworksManagerMovement.add(ControlAcceleration.createWithTerminalVelocityHalted(4000, -0.002, -0.002, -0.5, 0));
        bossNetworksManagerMovement.add(ControlAcceleration.createWithTerminalVelocityHalted(4000, 0.001, -0.001, 0, -0.5));
        bossNetworksManagerMovement.add(ControlAcceleration.createWithTerminalVelocityHalted(4000, -0.001, 0.001, -0.5, 0));
        bossNetworksManagerMovement.add(ControlAcceleration.createWithTerminalVelocityHalted(4500, 0.002, 0.002, 0, 0.5));
        bossNetworksManagerMovement.add(ControlAcceleration.createWithTerminalVelocityHalted(4500, 0.002, -0.002, 0.4, -0.25));
        bossNetworksManagerMovement.add(ControlVelocity.createWithTerminalHalted(5400, 0.4, -0.25, 0.85, 0.05));
        
        bossOperationsManagerMovement = new ControlPattern(2);
        bossOperationsManagerMovement.add(ControlVelocity.create(3000, -0.3, 0));
        bossOperationsManagerMovement.add(ControlContainment.create(4000, 0.1, 0.1, 0.6, 0.5));
        bossOperationsManagerMovement.add(ControlVelocity.create(4000, -0.3, 0.3));
        bossOperationsManagerMovement.add(ControlVelocity.create(4500, 0, -0.3));
        bossOperationsManagerMovement.add(ControlVelocity.create(5000, 0.3, 0.3));
        bossOperationsManagerMovement.add(ControlVelocity.create(5500, 0, -0.3));
        bossOperationsManagerMovement.add(ControlNull.create(6000));
        
        bossITExecutiveMovement = new ControlPattern(2);
        bossITExecutiveMovement.add(ControlVelocity.create(3000, -0.2, 0));
        bossITExecutiveMovement.add(ControlContainment.create(5000, 0.3, 0, 0.8, 0.6));
        bossITExecutiveMovement.add(ControlVelocity.createWithTerminal(5000, -0.15, 0, 0.5, Double.NaN));
        bossITExecutiveMovement.add(ControlVelocity.create(5900, 0, 0.15));
        bossITExecutiveMovement.add(ControlVelocity.createWithTerminal(6700, 0.15, 0, 0.8, Double.NaN));
        bossITExecutiveMovement.add(ControlVelocity.create(7500, 0, -0.15));
        bossITExecutiveMovement.add(ControlVelocity.create(8300, -0.15, 0));
        
        bossCIOMovement = new ControlPattern(2);
        bossCIOMovement.add(ControlVelocity.create(2300, -0.4, 0));
        bossCIOMovement.add(ControlContainment.create(3000, 0.15, 0, 0.85, 0.9));
        bossCIOMovement.add(ControlVelocity.create(3000, -0.6, 0));
        bossCIOMovement.add(ControlAcceleration.createWithTerminalVelocityHalted(3000, 0.0007, 0.0005, 0, 0.2));
        bossCIOMovement.add(ControlAcceleration.createWithTerminalVelocityHalted(3000, 0.00065, 0, 0.6, Double.NaN));
        bossCIOMovement.add(ControlAcceleration.createWithTerminalVelocityHalted(3000, -0.002, -0.002, 0, -0.4));
        bossCIOMovement.add(ControlAcceleration.createWithTerminalVelocityHalted(3000, -0.002, 0.002, -0.4, 0));
        bossCIOMovement.add(ControlAcceleration.createWithTerminalVelocityHalted(3000, 0.002, 0.002, 0, 0.4));
        bossCIOMovement.add(ControlAcceleration.createWithTerminalVelocityHalted(3000, 0.002, -0.002, 0.4, 0));
        bossCIOMovement.add(ControlAcceleration.createWithTerminalVelocityHalted(3000, -0.002, -0.002, 0, -0.4));
        bossCIOMovement.add(ControlAcceleration.createWithTerminalVelocityHalted(3000, -0.002, 0.002, -0.5, -0.09));
        bossCIOMovement.add(ControlAcceleration.createWithTerminalVelocityHalted(3000, 0.0008, 0, 0.5, Double.NaN));
        bossCIOMovement.add(ControlAcceleration.createWithTerminalVelocityHalted(3000, -0.002, -0.002, 0, -0.4));
        bossCIOMovement.add(ControlAcceleration.createWithTerminalVelocityHalted(3000, -0.002, 0.002, -0.6, 0));
        
        bossCEOMovement = new ControlPattern(2);
        bossCEOMovement.add(ControlVelocity.createWithTerminalHalted(3000, -0.3, 0, 0.7, 0.2));
        bossCEOMovement.add(ControlContainment.create(3000, 0.2, 0.1, 0.7, 0.6));
        /*
        bossCEOMovement.add(ControlVelocity.createWithTerminalHalted(3000, -0.3, 0, 0.6, Double.NaN));
        bossCEOMovement.add(ControlVelocity.createWithTerminalHalted(3000, -0.2, 0.1, 0.4, 0.3));
        bossCEOMovement.add(ControlVelocity.createWithTerminalHalted(3000, 0, 0.3, Double.NaN, 0.4));
        bossCEOMovement.add(ControlVelocity.createWithTerminalHalted(3000, 0.2, 0.1, 0.6, 0.5));
        bossCEOMovement.add(ControlVelocity.createWithTerminalHalted(3000, 0.3, 0, 0.7, Double.NaN));
        bossCEOMovement.add(ControlVelocity.createWithTerminalHalted(3000, 0, -0.3, Double.NaN, 0.2));
         */
        bossCEOMovement.add(ControlVelocity.createWithTerminalHalted(3000, -0.3, 0, 0.6, Double.NaN));
        bossCEOMovement.add(ControlVelocity.createWithTerminalHalted(3000, -0.2, 0.1, 0.3, 0.3));
        bossCEOMovement.add(ControlVelocity.createWithTerminalHalted(3000, 0, 0.3, Double.NaN, 0.4));
        bossCEOMovement.add(ControlVelocity.createWithTerminalHalted(3000, 0.2, 0.1, 0.5, 0.5));
        bossCEOMovement.add(ControlVelocity.createWithTerminalHalted(3000, 0.3, 0, 0.62, Double.NaN));
        bossCEOMovement.add(ControlVelocity.createWithTerminalHalted(3000, 0, -0.3, Double.NaN, 0.2));
        
        bossDirectorMovement = new ControlPattern(3);
        bossDirectorMovement.add(ControlVelocity.createWithTerminalHalted(3000, -0.4, 0, 0.6, Double.NaN));
        bossDirectorMovement.add(ControlVelocity.create(3000, -0.4, 0));
        bossDirectorMovement.add(ControlContainment.create(3000, 0.2, 0.1, 0.9, 0.8));
        bossDirectorMovement.add(ControlAcceleration.createWithTerminalVelocityHalted(3000, 0.0004, 0.0004, 0, 0.4));
        bossDirectorMovement.add(ControlAcceleration.createWithTerminalVelocityHalted(3000, 0.0004, -0.0004, 0.4, 0));
        bossDirectorMovement.add(ControlAcceleration.createWithTerminalVelocityHalted(3000, -0.0004, -0.0004, 0, -0.4));
        bossDirectorMovement.add(ControlAcceleration.createWithTerminalVelocityHalted(3000, -0.0004, 0.0004, -0.4, 0));
        
        bossChairmanMovement = new ControlPattern(2);
        bossChairmanMovement.add(ControlVelocity.createWithTerminalHalted(3000, -0.1, 0, 0.6, Double.NaN));
        bossChairmanMovement.add(ControlContainment.create(3000, 0.5, 0.3, 0.7, 0.5));
        bossChairmanMovement.add(ControlVelocity.createWithTerminalHalted(3000, -0.1, -0.1, 0.55, 0.35));
        bossChairmanMovement.add(ControlVelocity.createWithTerminalHalted(3000, 0.1, 0, 0.6, Double.NaN));
        bossChairmanMovement.add(ControlVelocity.createWithTerminalHalted(3000, 0, 0.1, Double.NaN, 0.4));
        bossChairmanMovement.add(ControlVelocity.createWithTerminalHalted(3000, -0.1, 0.1, 0.55, 0.45));
        bossChairmanMovement.add(ControlVelocity.createWithTerminalHalted(3000, 0, -0.1, Double.NaN, 0.4));
        bossChairmanMovement.add(ControlVelocity.createWithTerminalHalted(3000, 0.1, 0, 0.6, Double.NaN));
        
        bossReceiverMovement = new ControlPattern(false);
        bossReceiverMovement.add(ControlVelocity.createWithTerminalHalted(3000, -0.05, 0, 0.45, Double.NaN));
        //bossReceiverMovement.add(ControlContainment.create(3000, 0.3, 0, 0.7, 0.5));
        bossReceiverMovement.add(ControlVelocity.createWithTerminalHalted(10000, 0, 0.02, Double.NaN, 0.1));
        bossReceiverMovement.add(ControlVelocity.createWithTerminalHalted(15000, -0.02, 0, 0.35, Double.NaN));
        bossReceiverMovement.add(ControlVelocity.createWithTerminalHalted(20000, 0, -0.02, Double.NaN, 0));
        bossReceiverMovement.add(ControlVelocity.createWithTerminalHalted(25000, 0.02, 0, 0.45, Double.NaN));
        bossReceiverMovement.add(ControlVelocity.createWithTerminalHalted(30000, 0, 0.02, Double.NaN, 0.1));
        bossReceiverMovement.add(ControlVelocity.createWithTerminalHalted(35000, -0.02, 0, 0.35, Double.NaN));
        bossReceiverMovement.add(ControlVelocity.createWithTerminalHalted(40000, 0, -0.02, Double.NaN, 0));
        bossReceiverMovement.add(ControlVelocity.createWithTerminalHalted(45000, 0.02, 0, 0.45, Double.NaN));
        bossReceiverMovement.add(ControlVelocity.createWithTerminalHalted(50000, 0, 0.02, Double.NaN, 0.1));
        bossReceiverMovement.add(ControlVelocity.createWithTerminalHalted(55000, -0.02, 0, 0.35, Double.NaN));
        bossReceiverMovement.add(ControlVelocity.createWithTerminalHalted(60000, 0, -0.02, Double.NaN, 0));
        bossReceiverMovement.add(ControlVelocity.createWithTerminalHalted(65000, 0.02, 0, 0.45, Double.NaN));
        bossReceiverMovement.add(ControlVelocity.createWithTerminalHalted(70000, 0, 0.02, Double.NaN, 0.1));
        bossReceiverMovement.add(ControlVelocity.createWithTerminalHalted(75000, -0.02, 0, 0.35, Double.NaN));
        bossReceiverMovement.add(ControlVelocity.createWithTerminalHalted(80000, 0, -0.02, Double.NaN, 0));
        bossReceiverMovement.add(ControlVelocity.create(85000, -0.05, 0));
        
        /*
         * FIRING PATTERNS
         */
        firingLeftExplodingChair = new ControlPattern(false);
        firingLeftExplodingChair.add(ControlVelocity.createWithRandom(0, -0.3, -0.3, 0.2, 0.1));
        firingLeftExplodingChair.add(ControlFiring.createWithAngleRandom(1000, EnemyType.FIRING_CIRCLE, 0, 0.35, 25, 0));
        firingLeftExplodingChair.add(ControlFiring.createWithAngleRandom(1000, EnemyType.FIRING_CIRCLE, 45, 0.35, 25, 0));
        firingLeftExplodingChair.add(ControlFiring.createWithAngleRandom(1000, EnemyType.FIRING_CIRCLE, 90, 0.35, 25, 0));
        firingLeftExplodingChair.add(ControlFiring.createWithAngleRandom(1000, EnemyType.FIRING_CIRCLE, 135, 0.35, 25, 0));
        firingLeftExplodingChair.add(ControlFiring.createWithAngleRandom(1000, EnemyType.FIRING_CIRCLE, 180, 0.35, 25, 0));
        firingLeftExplodingChair.add(ControlFiring.createWithAngleRandom(1000, EnemyType.FIRING_CIRCLE, 225, 0.35, 25, 0));
        firingLeftExplodingChair.add(ControlFiring.createWithAngleRandom(1000, EnemyType.FIRING_CIRCLE, 270, 0.35, 25, 0));
        firingLeftExplodingChair.add(ControlFiring.createWithAngleRandom(1000, EnemyType.FIRING_CIRCLE, 315, 0.35, 25, 0));
        firingLeftExplodingChair.add(ControlFinish.createWithExplosion(1000));
        
        firingUpExplodingChair = new ControlPattern(false);
        firingUpExplodingChair.add(ControlVelocity.createWithRandom(0, 0, -0.2, 0.05, 0.1));
        firingUpExplodingChair.add(ControlFiring.createWithAngleRandom(1000, EnemyType.FIRING_CIRCLE, 0, 0.35, 25, 0));
        firingUpExplodingChair.add(ControlFiring.createWithAngleRandom(1000, EnemyType.FIRING_CIRCLE, 45, 0.35, 25, 0));
        firingUpExplodingChair.add(ControlFiring.createWithAngleRandom(1000, EnemyType.FIRING_CIRCLE, 90, 0.35, 25, 0));
        firingUpExplodingChair.add(ControlFiring.createWithAngleRandom(1000, EnemyType.FIRING_CIRCLE, 135, 0.35, 25, 0));
        firingUpExplodingChair.add(ControlFiring.createWithAngleRandom(1000, EnemyType.FIRING_CIRCLE, 180, 0.35, 25, 0));
        firingUpExplodingChair.add(ControlFiring.createWithAngleRandom(1000, EnemyType.FIRING_CIRCLE, 225, 0.35, 25, 0));
        firingUpExplodingChair.add(ControlFiring.createWithAngleRandom(1000, EnemyType.FIRING_CIRCLE, 270, 0.35, 25, 0));
        firingUpExplodingChair.add(ControlFiring.createWithAngleRandom(1000, EnemyType.FIRING_CIRCLE, 315, 0.35, 25, 0));
        firingUpExplodingChair.add(ControlFinish.createWithExplosion(1000));
        
        firingLeftEarlySingleMissile = new ControlPattern(false);
        
        firingLeftSingleCircle = new ControlPattern(false);
        firingLeftSingleCircle.add(ControlRandomWait.create(0, 300));
        firingLeftSingleCircle.add(ControlFiring.createWithXY(300, EnemyType.FIRING_CIRCLE, -SPEED_MEDIUM, 0));
        
        firingLeftEarlySingleCircle = new ControlPattern(false);
        firingLeftEarlySingleCircle.add(ControlFiring.createWithXY(100, EnemyType.FIRING_CIRCLE, -SPEED_MEDIUM, 0));
        
        firingLeftLateSingleCircle = new ControlPattern(false);
        firingLeftLateSingleCircle.add(ControlRandomWait.create(0, 600));
        firingLeftLateSingleCircle.add(ControlFiring.createWithXY(600, EnemyType.FIRING_CIRCLE, -SPEED_MEDIUM, 0));
        
        firingLeftLightCircles = new ControlPattern(true);
        firingLeftLightCircles.add(ControlFiring.createWithXY(100, EnemyType.FIRING_CIRCLE, -SPEED_MEDIUM, 0));
        firingLeftLightCircles.add(ControlNull.create(1000));
        
        firingLeftCircles = new ControlPattern(true);
        firingLeftCircles.add(ControlRandomWait.create(0, 1000));
        firingLeftCircles.add(ControlFiring.createWithXYRandom(0, EnemyType.FIRING_CIRCLE, -SPEED_FAST, 0, 0.2, 0.3));
        firingLeftCircles.add(ControlNull.create(200));
        
        firingLeftSingleEmail = new ControlPattern(false);
        firingLeftSingleEmail.add(ControlFiring.createWithXY(100, EnemyType.FIRING_EMAIL, -SPEED_MEDIUM, 0));
        
        firingLeftFastLateSingleEmail = new ControlPattern(false);
        firingLeftFastLateSingleEmail.add(ControlFiring.createWithXY(1000, EnemyType.FIRING_EMAIL, -SPEED_FAST, 0));
        
        firingLeftHeavyEmail = new ControlPattern(true);
        firingLeftHeavyEmail.add(ControlRandomWait.create(0, 400));
        firingLeftHeavyEmail.add(ControlFiring.createWithXYRandom(100, EnemyType.FIRING_EMAIL, -SPEED_FAST, 0, 0.1, 0));
        
        firingLeftSpread5Circle = new ControlPattern(true);
        firingLeftSpread5Circle.add(ControlFiring.createWithAngle(0, EnemyType.FIRING_CIRCLE, 180, 0.3));
        firingLeftSpread5Circle.add(ControlFiring.createWithAngle(0, EnemyType.FIRING_CIRCLE, 165, 0.3));
        firingLeftSpread5Circle.add(ControlFiring.createWithAngle(0, EnemyType.FIRING_CIRCLE, 195, 0.3));
        firingLeftSpread5Circle.add(ControlFiring.createWithAngle(0, EnemyType.FIRING_CIRCLE, 150, 0.3));
        firingLeftSpread5Circle.add(ControlFiring.createWithAngle(0, EnemyType.FIRING_CIRCLE, 210, 0.3));
        firingLeftSpread5Circle.add(ControlNull.create(2000));
        
        firingLeftSlow4Missiles = new ControlPattern(false);
        firingLeftSlow4Missiles.add(ControlFiring.createWithXYRandom(500, EnemyType.FIRING_MISSILE, -0.3, 0, 0.05, 0));
        firingLeftSlow4Missiles.add(ControlFiring.createWithXYRandom(800, EnemyType.FIRING_MISSILE, -0.3, 0, 0.05, 0));
        firingLeftSlow4Missiles.add(ControlFiring.createWithXYRandom(1300, EnemyType.FIRING_MISSILE, -0.3, 0, 0.05, 0));
        firingLeftSlow4Missiles.add(ControlFiring.createWithXYRandom(1600, EnemyType.FIRING_MISSILE, -0.3, 0, 0.05, 0));
        
        firingLeft4Missiles = new ControlPattern(false);
        firingLeft4Missiles.add(ControlRandomWait.create(0, 200));
        firingLeft4Missiles.add(ControlFiring.createWithXYRandom(400, EnemyType.FIRING_MISSILE, -0.35, 0, 0.05, 0.1));
        firingLeft4Missiles.add(ControlRandomWait.create(400, 200));
        firingLeft4Missiles.add(ControlFiring.createWithXYRandom(600, EnemyType.FIRING_MISSILE, -0.35, 0, 0.05, 0.1));
        firingLeft4Missiles.add(ControlRandomWait.create(600, 200));
        firingLeft4Missiles.add(ControlFiring.createWithXYRandom(1000, EnemyType.FIRING_MISSILE, -0.35, 0, 0.05, 0.1));
        firingLeft4Missiles.add(ControlRandomWait.create(1000, 200));
        firingLeft4Missiles.add(ControlFiring.createWithXYRandom(1200, EnemyType.FIRING_MISSILE, -0.35, 0, 0.05, 0.1));
        
        firingLeftLightMissiles = new ControlPattern(true);
        firingLeftLightMissiles.add(ControlRandomWait.create(0, 200));
        firingLeftLightMissiles.add(ControlFiring.createWithXYRandom(400, EnemyType.FIRING_MISSILE, -0.35, 0, 0.05, 0.1));
        firingLeftLightMissiles.add(ControlRandomWait.create(400, 200));
        firingLeftLightMissiles.add(ControlFiring.createWithXYRandom(600, EnemyType.FIRING_MISSILE, -0.35, 0, 0.05, 0.1));
        firingLeftLightMissiles.add(ControlRandomWait.create(600, 200));
        firingLeftLightMissiles.add(ControlFiring.createWithXYRandom(1000, EnemyType.FIRING_MISSILE, -0.35, 0, 0.05, 0.1));
        firingLeftLightMissiles.add(ControlRandomWait.create(1000, 200));
        firingLeftLightMissiles.add(ControlFiring.createWithXYRandom(1200, EnemyType.FIRING_MISSILE, -0.35, 0, 0.05, 0.1));
        firingLeftLightMissiles.add(ControlNull.create(2000));
        
        firingRotaryCirclesLeftUpLightPhones = new ControlPattern(true);
        firingRotaryCirclesLeftUpLightPhones.add(ControlFiring.createWithAngle(0, EnemyType.FIRING_CIRCLE, 120, SPEED_MEDIUM));
        firingRotaryCirclesLeftUpLightPhones.add(ControlFiring.createWithAngle(100, EnemyType.FIRING_CIRCLE, 135, SPEED_MEDIUM));
        firingRotaryCirclesLeftUpLightPhones.add(ControlFiring.createWithAngle(200, EnemyType.FIRING_CIRCLE, 150, SPEED_MEDIUM));
        firingRotaryCirclesLeftUpLightPhones.add(ControlFiring.createWithAngle(300, EnemyType.FIRING_CIRCLE, 165, SPEED_MEDIUM));
        firingRotaryCirclesLeftUpLightPhones.add(ControlFiring.createWithAngle(400, EnemyType.FIRING_CIRCLE, 180, SPEED_MEDIUM));
        firingRotaryCirclesLeftUpLightPhones.add(ControlFiring.createWithAngle(500, EnemyType.FIRING_CIRCLE, -165, SPEED_MEDIUM));
        firingRotaryCirclesLeftUpLightPhones.add(ControlFiring.createWithAngle(600, EnemyType.FIRING_CIRCLE, -150, SPEED_MEDIUM));
        firingRotaryCirclesLeftUpLightPhones.add(ControlFiring.createWithAngle(700, EnemyType.FIRING_CIRCLE, -135, SPEED_MEDIUM));
        firingRotaryCirclesLeftUpLightPhones.add(ControlFiring.createWithAngle(800, EnemyType.FIRING_CIRCLE, -120, SPEED_MEDIUM));
        firingRotaryCirclesLeftUpLightPhones.add(ControlFiring.createWithAngleOffsetsAndRandom(1000, EnemyType.FIRING_PHONE, 115, -30, 135, SPEED_MEDIUM, 60, 0.1));
        firingRotaryCirclesLeftUpLightPhones.add(ControlNull.create(1200));
        
        firingRotaryCirclesLeftUpLightChairs = new ControlPattern(true);
        firingRotaryCirclesLeftUpLightChairs.add(ControlFiring.createWithAngle(0, EnemyType.FIRING_CIRCLE, 120, SPEED_MEDIUM));
        firingRotaryCirclesLeftUpLightChairs.add(ControlFiring.createWithAngle(100, EnemyType.FIRING_CIRCLE, 135, SPEED_MEDIUM));
        firingRotaryCirclesLeftUpLightChairs.add(ControlFiring.createWithAngle(200, EnemyType.FIRING_CIRCLE, 150, SPEED_MEDIUM));
        firingRotaryCirclesLeftUpLightChairs.add(ControlFiring.createWithAngle(300, EnemyType.FIRING_CIRCLE, 165, SPEED_MEDIUM));
        firingRotaryCirclesLeftUpLightChairs.add(ControlFiring.createWithAngle(400, EnemyType.FIRING_CIRCLE, 180, SPEED_MEDIUM));
        firingRotaryCirclesLeftUpLightChairs.add(ControlFiring.createWithAngle(500, EnemyType.FIRING_CIRCLE, -165, SPEED_MEDIUM));
        firingRotaryCirclesLeftUpLightChairs.add(ControlFiring.createWithAngle(600, EnemyType.FIRING_CIRCLE, -150, SPEED_MEDIUM));
        firingRotaryCirclesLeftUpLightChairs.add(ControlFiring.createWithAngle(700, EnemyType.FIRING_CIRCLE, -135, SPEED_MEDIUM));
        firingRotaryCirclesLeftUpLightChairs.add(ControlFiring.createWithAngle(800, EnemyType.FIRING_CIRCLE, -120, SPEED_MEDIUM));
        firingRotaryCirclesLeftUpLightChairs.add(ControlFiring.createControlPattern(1000, EnemyType.FIRING_CHAIR, firingLeftExplodingChair));
        firingRotaryCirclesLeftUpLightChairs.add(ControlNull.create(1200));
        
        firingHomingEarlySingleCircle = new ControlPattern(false);
        firingHomingEarlySingleCircle.add(ControlFiring.createHomingWithRandom(100, EnemyType.FIRING_CIRCLE, SPEED_MEDIUM, 20, 0.1));
        
        firingHomingSingleCircle = new ControlPattern(false);
        firingHomingSingleCircle.add(ControlRandomWait.create(0, 150));
        firingHomingSingleCircle.add(ControlFiring.createHomingWithRandom(300, EnemyType.FIRING_CIRCLE, SPEED_MEDIUM, 20, 0.1));
        
        firingHomingLateSingleCircle = new ControlPattern(false);
        firingHomingLateSingleCircle.add(ControlFiring.createHomingWithRandom(600, EnemyType.FIRING_CIRCLE, SPEED_MEDIUM, 20, 0.1));
        
        firingHomingFastEarlySingleCircle = new ControlPattern(false);
        firingHomingFastEarlySingleCircle.add(ControlFiring.createHomingWithRandom(100, EnemyType.FIRING_CIRCLE, SPEED_FAST, 20, 0.1));
        
        firingHomingFastSingleCircle = new ControlPattern(false);
        firingHomingFastSingleCircle.add(ControlFiring.createHomingWithRandom(300, EnemyType.FIRING_CIRCLE, SPEED_FAST, 20, 0.1));
        
        firingHomingFastLateSingleCircle = new ControlPattern(false);
        firingHomingFastLateSingleCircle.add(ControlFiring.createHomingWithRandom(600, EnemyType.FIRING_CIRCLE, SPEED_FAST, 20, 0.1));
        
        firingHoming2Circles = new ControlPattern(false);
        firingHoming2Circles.add(ControlRandomWait.create(0, 150));
        firingHoming2Circles.add(ControlFiring.createHomingWithRandom(0, EnemyType.FIRING_CIRCLE, SPEED_MEDIUM, 20, 0.1));
        firingHoming2Circles.add(ControlRandomWait.create(500, 150));
        firingHoming2Circles.add(ControlFiring.createHomingWithRandom(0, EnemyType.FIRING_CIRCLE, SPEED_MEDIUM, 20, 0.1));
        
        
        firingHomingLightCircles = new ControlPattern(true);
        firingHomingLightCircles.add(ControlFiring.createHomingWithRandom(0, EnemyType.FIRING_CIRCLE, SPEED_MEDIUM, 20, 0.1));
        firingHomingLightCircles.add(ControlRandomWait.create(700, 600));
        
        firingHomingCircles = new ControlPattern(true);
        firingHomingCircles.add(ControlRandomWait.create(0, 800));
        firingHomingCircles.add(ControlFiring.createHomingWithRandom(0, EnemyType.FIRING_CIRCLE, SPEED_MEDIUM, 30, 0.14));
        firingHomingCircles.add(ControlNull.create(400));
        
        firingHomingFastCircles = new ControlPattern(true);
        firingHomingFastCircles.add(ControlRandomWait.create(0, 800));
        firingHomingFastCircles.add(ControlFiring.createHomingWithRandom(0, EnemyType.FIRING_CIRCLE, SPEED_FAST, 30, 0.14));
        firingHomingFastCircles.add(ControlNull.create(400));
        
        firingLeftCircleHomingCircle = new ControlPattern(true);
        firingLeftCircleHomingCircle.add(ControlRandomWait.create(0, 500));
        firingLeftCircleHomingCircle.add(ControlFiring.createWithXY(200, EnemyType.FIRING_CIRCLE, -SPEED_MEDIUM, 0));
        firingLeftCircleHomingCircle.add(ControlRandomWait.create(200, 500));
        firingLeftCircleHomingCircle.add(ControlFiring.createHomingWithRandom(500, EnemyType.FIRING_CIRCLE, SPEED_MEDIUM, 40, 0.1));
        
        firingHomingLightCircleLeftLightEmail = new ControlPattern(1);
        firingHomingLightCircleLeftLightEmail.add(ControlRandomWait.create(200, 500));
        firingHomingLightCircleLeftLightEmail.add(ControlFiring.createHomingWithRandom(200, EnemyType.FIRING_CIRCLE, SPEED_MEDIUM, 20, 0.1));
        firingHomingLightCircleLeftLightEmail.add(ControlRandomWait.create(600, 500));
        firingHomingLightCircleLeftLightEmail.add(ControlFiring.createWithXY(600, EnemyType.FIRING_EMAIL, -SPEED_MEDIUM, 0));
        firingHomingLightCircleLeftLightEmail.add(ControlRandomWait.create(1500, 500));
        
        firingHomingLightCircleLeftLightPhone = new ControlPattern(1);
        firingHomingLightCircleLeftLightPhone.add(ControlRandomWait.create(200, 500));
        firingHomingLightCircleLeftLightPhone.add(ControlFiring.createHomingWithRandom(200, EnemyType.FIRING_CIRCLE, SPEED_MEDIUM, 20, 0.1));
        firingHomingLightCircleLeftLightPhone.add(ControlRandomWait.create(600, 500));
        firingHomingLightCircleLeftLightPhone.add(ControlFiring.createWithAngleRandom(600, EnemyType.FIRING_PHONE, 150, SPEED_LIGHTNING, 40, 0.1));
        firingHomingLightCircleLeftLightPhone.add(ControlRandomWait.create(1500, 500));
        
        firingHomingLightCircleLeftEarlySinglePhone = new ControlPattern(2);
        firingHomingLightCircleLeftEarlySinglePhone.add(ControlFiring.createHomingWithRandom(300, EnemyType.FIRING_CIRCLE, SPEED_MEDIUM, 20, 0.1));
        firingHomingLightCircleLeftEarlySinglePhone.add(ControlFiring.createWithAngleRandom(500, EnemyType.FIRING_PHONE, 150, SPEED_LIGHTNING, 40, 0.1));
        firingHomingLightCircleLeftEarlySinglePhone.add(ControlFiring.createHomingWithRandom(1600, EnemyType.FIRING_CIRCLE, SPEED_MEDIUM, 20, 0.1));
        firingHomingLightCircleLeftEarlySinglePhone.add(ControlRandomWait.create(2500, 500));
        
        firingHomingLightCircleLeftSinglePhone = new ControlPattern(2);
        firingHomingLightCircleLeftSinglePhone.add(ControlFiring.createHomingWithRandom(300, EnemyType.FIRING_CIRCLE, SPEED_MEDIUM, 20, 0.1));
        firingHomingLightCircleLeftSinglePhone.add(ControlFiring.createWithAngleRandom(900, EnemyType.FIRING_PHONE, 150, SPEED_LIGHTNING, 40, 0.1));
        firingHomingLightCircleLeftSinglePhone.add(ControlFiring.createHomingWithRandom(1600, EnemyType.FIRING_CIRCLE, SPEED_MEDIUM, 20, 0.1));
        firingHomingLightCircleLeftSinglePhone.add(ControlRandomWait.create(2500, 500));
        
        firingHomingLightCircleLeftDownLateSinglePhone = new ControlPattern(2);
        firingHomingLightCircleLeftDownLateSinglePhone.add(ControlFiring.createHomingWithRandom(300, EnemyType.FIRING_CIRCLE, SPEED_MEDIUM, 20, 0.1));
        firingHomingLightCircleLeftDownLateSinglePhone.add(ControlFiring.createWithAngleRandom(1500, EnemyType.FIRING_PHONE, 190, SPEED_FAST, 40, 0.1));
        firingHomingLightCircleLeftDownLateSinglePhone.add(ControlFiring.createHomingWithRandom(2000, EnemyType.FIRING_CIRCLE, SPEED_MEDIUM, 20, 0.1));
        firingHomingLightCircleLeftDownLateSinglePhone.add(ControlRandomWait.create(3000, 500));
        
        firingHomingLightCircleLeftLateSinglePhone = new ControlPattern(2);
        firingHomingLightCircleLeftLateSinglePhone.add(ControlFiring.createHomingWithRandom(300, EnemyType.FIRING_CIRCLE, SPEED_MEDIUM, 20, 0.1));
        firingHomingLightCircleLeftLateSinglePhone.add(ControlFiring.createWithAngleRandom(2000, EnemyType.FIRING_PHONE, 150, SPEED_LIGHTNING, 40, 0.1));
        firingHomingLightCircleLeftLateSinglePhone.add(ControlFiring.createHomingWithRandom(2500, EnemyType.FIRING_CIRCLE, SPEED_MEDIUM, 20, 0.1));
        firingHomingLightCircleLeftLateSinglePhone.add(ControlRandomWait.create(3500, 500));
        
        firingLeftSingleEmailHomingSingleCircle = new ControlPattern(false);
        firingLeftSingleEmailHomingSingleCircle.add(ControlRandomWait.create(200, 500));
        firingLeftSingleEmailHomingSingleCircle.add(ControlFiring.createWithXY(200, EnemyType.FIRING_EMAIL, -SPEED_MEDIUM, 0));
        firingLeftSingleEmailHomingSingleCircle.add(ControlRandomWait.create(200, 500));
        firingLeftSingleEmailHomingSingleCircle.add(ControlFiring.createHomingWithRandom(500, EnemyType.FIRING_CIRCLE, SPEED_FAST, 40, 0.1));
        
        /*
        firingStraightSpread3Missile = new ControlPattern(true);
        firingStraightSpread3Missile.add(ControlFiring.createWithAngle(0, EnemyType.FIRING_MISSILE, 180, SPEED_MEDIUM));
        firingStraightSpread3Missile.add(ControlFiring.createWithAngleRandom(100, EnemyType.FIRING_MISSILE, 185, SPEED_MEDIUM, 0, 0));
        firingStraightSpread3Missile.add(ControlFiring.createWithAngleRandom(200, EnemyType.FIRING_MISSILE, 175, SPEED_MEDIUM, 3, 0));
        firingStraightSpread3Missile.add(ControlNull.create(1400));
        */
        firingStraightSpread3Missile = new ControlPattern(true);
        firingStraightSpread3Missile.add(ControlFiring.createWithAngle(0, EnemyType.FIRING_MISSILE, 180, SPEED_LIGHTNING));
        firingStraightSpread3Missile.add(ControlFiring.createWithAngleRandom(100, EnemyType.FIRING_MISSILE, 185, SPEED_LIGHTNING, 0, 0));
        firingStraightSpread3Missile.add(ControlFiring.createWithAngleRandom(200, EnemyType.FIRING_MISSILE, 175, SPEED_LIGHTNING, 3, 0));
        firingStraightSpread3Missile.add(ControlNull.create(1400));
        
        firingHomingSpread5Circle = new ControlPattern(true);
        firingHomingSpread5Circle.add(ControlFiring.createHomingPlusAngle(0, EnemyType.FIRING_CIRCLE, 0, 0.3));
        firingHomingSpread5Circle.add(ControlFiring.createHomingPlusAngle(0, EnemyType.FIRING_CIRCLE, -15, 0.3));
        firingHomingSpread5Circle.add(ControlFiring.createHomingPlusAngle(0, EnemyType.FIRING_CIRCLE, 15, 0.3));
        firingHomingSpread5Circle.add(ControlFiring.createHomingPlusAngle(0, EnemyType.FIRING_CIRCLE, -30, 0.3));
        firingHomingSpread5Circle.add(ControlFiring.createHomingPlusAngle(0, EnemyType.FIRING_CIRCLE, 30, 0.3));
        firingHomingSpread5Circle.add(ControlNull.create(2000));
        
        firingHomingSingleCircleLeftSingleChair = new ControlPattern(true);
        firingHomingSingleCircleLeftSingleChair.add(ControlRandomWait.create(0, 500));
        firingHomingSingleCircleLeftSingleChair.add(ControlFiring.createHomingWithRandom(600, EnemyType.FIRING_CIRCLE, SPEED_MEDIUM, 20, 0.1));
        firingHomingSingleCircleLeftSingleChair.add(ControlRandomWait.create(900, 500));
        firingHomingSingleCircleLeftSingleChair.add(ControlFiring.createControlPattern(900, EnemyType.FIRING_CHAIR, firingLeftExplodingChair));
        
        firingSingleChairSingleEmailSinglePhone = new ControlPattern(false);
        firingSingleChairSingleEmailSinglePhone.add(ControlRandomWait.create(0, 300));
        firingSingleChairSingleEmailSinglePhone.add(ControlFiring.createControlPattern(400, EnemyType.FIRING_CHAIR, firingLeftExplodingChair));
        firingSingleChairSingleEmailSinglePhone.add(ControlRandomWait.create(400, 500));
        firingSingleChairSingleEmailSinglePhone.add(ControlFiring.createWithXY(1000, EnemyType.FIRING_EMAIL, -SPEED_MEDIUM, 0));
        firingSingleChairSingleEmailSinglePhone.add(ControlRandomWait.create(1000, 300));
        firingSingleChairSingleEmailSinglePhone.add(ControlFiring.createWithAngleRandom(1300, EnemyType.FIRING_PHONE, 145, SPEED_LIGHTNING, 40, 0.1));
        
        bossSupervisorFiring = new ControlPattern(true);
        bossSupervisorFiring.add(ControlFiring.createWithXYOffsetsAndRandom(0, EnemyType.FIRING_MISSILE, 109, 12, -0.4, 0, 0, 0.05));
        bossSupervisorFiring.add(ControlFiring.createWithXYOffsetsAndRandom(200, EnemyType.FIRING_MISSILE, 0, 100, -0.4, 0, 0, 0.05));
        bossSupervisorFiring.add(ControlFiring.createWithXYOffsetsAndRandom(400, EnemyType.FIRING_MISSILE, 120, 126, -0.4, 0, 0, 0.05));
        bossSupervisorFiring.add(ControlNull.create(700));
        
        bossTeamLeaderFiring = new ControlPattern(true);
        bossTeamLeaderFiring.add(ControlFiring.createHomingPlusAngleWithOffsetsRandom(0, EnemyType.FIRING_CIRCLE, 50, 170, 0, SPEED_SLOW, 20, 0.05));
        bossTeamLeaderFiring.add(ControlFiring.createHomingPlusAngleWithOffsetsRandom(50, EnemyType.FIRING_CIRCLE, 145, 180, 0, SPEED_SLOW, 20, 0.05));
        bossTeamLeaderFiring.add(ControlFiring.createHomingPlusAngleWithOffsetsRandom(100, EnemyType.FIRING_CIRCLE, 235, 170, 0, SPEED_SLOW, 20, 0.05));
        bossTeamLeaderFiring.add(ControlNull.create(800));
        
        bossNetworksManagerFiring = new ControlPattern(true);
        bossNetworksManagerFiring.add(ControlFiring.createWithXYOffsetsAndRandom(0, EnemyType.FIRING_EMAIL, 0, 46, -0.5, 0, 0.05, 0.05));
        bossNetworksManagerFiring.add(ControlFiring.createWithAngleOffsets(100, EnemyType.FIRING_CIRCLE, 0, 180, 180, 0.3));
        bossNetworksManagerFiring.add(ControlFiring.createWithAngleOffsets(100, EnemyType.FIRING_CIRCLE, 0, 180, 210, 0.3));
        bossNetworksManagerFiring.add(ControlFiring.createWithAngleOffsets(100, EnemyType.FIRING_CIRCLE, 0, 180, 150, 0.3));
        bossNetworksManagerFiring.add(ControlFiring.createWithXYOffsetsAndRandom(300, EnemyType.FIRING_EMAIL, 0, 46, -0.5, 0, 0.05, 0.05));
        bossNetworksManagerFiring.add(ControlFiring.createHomingWithOffsetsRandom(800, EnemyType.FIRING_CIRCLE, 180, 52, 0.25, 20, 0.05));
        bossNetworksManagerFiring.add(ControlNull.create(1100));
        
        bossOperationsManagerFiring = new ControlPattern(true);
        
        bossOperationsManagerFiring.add(ControlFiring.createWithAngleOffsets(0, EnemyType.FIRING_CIRCLE, 104, 94, 180, 0.35));
        bossOperationsManagerFiring.add(ControlFiring.createWithAngleOffsets(0, EnemyType.FIRING_CIRCLE, 137, 46, 190, 0.35));
        bossOperationsManagerFiring.add(ControlFiring.createWithAngleOffsets(0, EnemyType.FIRING_CIRCLE, 137, 140, 170, 0.35));
        
        bossOperationsManagerFiring.add(ControlFiring.createWithAngleOffsets(400, EnemyType.FIRING_CIRCLE, 104, 94, 180, 0.35));
        bossOperationsManagerFiring.add(ControlFiring.createWithAngleOffsets(400, EnemyType.FIRING_CIRCLE, 137, 46, 190, 0.35));
        bossOperationsManagerFiring.add(ControlFiring.createWithAngleOffsets(400, EnemyType.FIRING_CIRCLE, 137, 140, 170, 0.35));
        
        bossOperationsManagerFiring.add(ControlFiring.createWithAngleOffsets(800, EnemyType.FIRING_CIRCLE, 104, 94, 180, 0.35));
        bossOperationsManagerFiring.add(ControlFiring.createWithAngleOffsets(800, EnemyType.FIRING_CIRCLE, 137, 46, 190, 0.35));
        bossOperationsManagerFiring.add(ControlFiring.createWithAngleOffsets(800, EnemyType.FIRING_CIRCLE, 137, 140, 170, 0.35));
        
        bossOperationsManagerFiring.add(ControlFiring.createHomingWithOffsetsRandom(1200, EnemyType.FIRING_CIRCLE, 196, 46, 0.35, 15, 0.02));
        bossOperationsManagerFiring.add(ControlFiring.createHomingWithOffsetsRandom(1250, EnemyType.FIRING_CIRCLE, 228, 94, 0.35, 15, 0.02));
        bossOperationsManagerFiring.add(ControlFiring.createHomingWithOffsetsRandom(1300, EnemyType.FIRING_CIRCLE, 196, 140, 0.35, 15, 0.02));
        bossOperationsManagerFiring.add(ControlFiring.createHomingWithOffsetsRandom(1350, EnemyType.FIRING_CIRCLE, 196, 46, 0.35, 15, 0.02));
        bossOperationsManagerFiring.add(ControlFiring.createHomingWithOffsetsRandom(1400, EnemyType.FIRING_CIRCLE, 228, 94, 0.35, 15, 0.02));
        bossOperationsManagerFiring.add(ControlFiring.createHomingWithOffsetsRandom(1450, EnemyType.FIRING_CIRCLE, 196, 140, 0.35, 15, 0.02));
        bossOperationsManagerFiring.add(ControlFiring.createHomingWithOffsetsRandom(1500, EnemyType.FIRING_CIRCLE, 196, 46, 0.35, 15, 0.02));
        bossOperationsManagerFiring.add(ControlFiring.createHomingWithOffsetsRandom(1550, EnemyType.FIRING_CIRCLE, 228, 94, 0.35, 15, 0.02));
        bossOperationsManagerFiring.add(ControlFiring.createHomingWithOffsetsRandom(1600, EnemyType.FIRING_CIRCLE, 196, 140, 0.35, 15, 0.02));
        bossOperationsManagerFiring.add(ControlFiring.createHomingWithOffsetsRandom(1650, EnemyType.FIRING_CIRCLE, 196, 46, 0.35, 15, 0.02));
        bossOperationsManagerFiring.add(ControlFiring.createHomingWithOffsetsRandom(1700, EnemyType.FIRING_CIRCLE, 228, 94, 0.35, 15, 0.02));
        bossOperationsManagerFiring.add(ControlFiring.createHomingWithOffsetsRandom(1750, EnemyType.FIRING_CIRCLE, 196, 140, 0.35, 15, 0.02));
        bossOperationsManagerFiring.add(ControlNull.create(1800));
        
        bossITExecutiveFiring = new ControlPattern(true);
        bossITExecutiveFiring.add(ControlFiring.createControlPatternWithOffsets(200, EnemyType.USER_EGG, -40, 130, straightMovingLeftFiringEmail));
        bossITExecutiveFiring.add(ControlFiring.createWithAngleOffsetsAndRandom(400, EnemyType.FIRING_PHONE, 20, 60, 135, 0.2, 20, 0.1));
        bossITExecutiveFiring.add(ControlFiring.createWithAngleOffsetsAndRandom(800, EnemyType.FIRING_PHONE, 20, 60, 135, 0.4, 20, 0.1));
        bossITExecutiveFiring.add(ControlFiring.createControlPatternWithOffsets(1200, EnemyType.USER_UFO, 5, 210, loopXSingleRightMovingLeftDownFiringHomingCircle));
        bossITExecutiveFiring.add(ControlFiring.createWithXYOffsetsAndRandom(1500, EnemyType.FIRING_MISSILE, 60, 150, -0.25, 0, 0, 0));
        bossITExecutiveFiring.add(ControlFiring.createWithXYOffsetsAndRandom(1600, EnemyType.FIRING_MISSILE, 70, 115, -0.25, 0, 0, 0));
        bossITExecutiveFiring.add(ControlFiring.createWithXYOffsetsAndRandom(1600, EnemyType.FIRING_MISSILE, 70, 180, -0.25, 0, 0, 0));
        bossITExecutiveFiring.add(ControlNull.create(1800));
        
        bossCIOFiring = new ControlPattern(true);
        bossCIOFiring.add(ControlFiring.createWithXYOffsetsAndRandom(0, EnemyType.FIRING_MISSILE, 90, 20, -0.4, 0, 0, 0));
        bossCIOFiring.add(ControlFiring.createWithXYOffsetsAndRandom(0, EnemyType.FIRING_MISSILE, 90, 255, -0.4, 0, 0, 0));
        bossCIOFiring.add(ControlFiring.createWithXYOffsetsAndRandom(400, EnemyType.FIRING_MISSILE, 20, 52, -0.4, 0, 0, 0));
        bossCIOFiring.add(ControlFiring.createWithXYOffsetsAndRandom(400, EnemyType.FIRING_MISSILE, 20, 215, -0.4, 0, 0, 0));
        bossCIOFiring.add(ControlFiring.createWithXYOffsetsAndRandom(600, EnemyType.FIRING_MISSILE, 20, 52, -0.4, 0, 0, 0));
        bossCIOFiring.add(ControlFiring.createWithXYOffsetsAndRandom(600, EnemyType.FIRING_MISSILE, 20, 215, -0.4, 0, 0, 0));
        bossCIOFiring.add(ControlFiring.createWithXYOffsetsAndRandom(1200, EnemyType.FIRING_MISSILE, -5, 95, -0.4, 0, 0, 0));
        bossCIOFiring.add(ControlFiring.createWithXYOffsetsAndRandom(1200, EnemyType.FIRING_MISSILE, -5, 175, -0.4, 0, 0, 0));
        bossCIOFiring.add(ControlFiring.createWithXYOffsetsAndRandom(1400, EnemyType.FIRING_MISSILE, -5, 95, -0.4, 0, 0, 0));
        bossCIOFiring.add(ControlFiring.createWithXYOffsetsAndRandom(1400, EnemyType.FIRING_MISSILE, -5, 175, -0.4, 0, 0, 0));
        bossCIOFiring.add(ControlFiring.createWithXYOffsetsAndRandom(1600, EnemyType.FIRING_MISSILE, -5, 95, -0.4, 0, 0, 0));
        bossCIOFiring.add(ControlFiring.createWithXYOffsetsAndRandom(1600, EnemyType.FIRING_MISSILE, -5, 175, -0.4, 0, 0, 0));
        bossCIOFiring.add(ControlFiring.createHomingPlusAngle(2200, EnemyType.FIRING_CIRCLE, 0, 0.3));
        bossCIOFiring.add(ControlFiring.createHomingPlusAngle(2200, EnemyType.FIRING_CIRCLE, -15, 0.3));
        bossCIOFiring.add(ControlFiring.createHomingPlusAngle(2200, EnemyType.FIRING_CIRCLE, 15, 0.3));
        bossCIOFiring.add(ControlFiring.createHomingPlusAngle(2200, EnemyType.FIRING_CIRCLE, -30, 0.3));
        bossCIOFiring.add(ControlFiring.createHomingPlusAngle(2200, EnemyType.FIRING_CIRCLE, 30, 0.3));
        bossCIOFiring.add(ControlFiring.createHomingPlusAngle(2200, EnemyType.FIRING_CIRCLE, -45, 0.3));
        bossCIOFiring.add(ControlFiring.createHomingPlusAngle(2200, EnemyType.FIRING_CIRCLE, 45, 0.3));
        bossCIOFiring.add(ControlFiring.createHomingPlusAngle(2200, EnemyType.FIRING_CIRCLE, -60, 0.3));
        bossCIOFiring.add(ControlFiring.createHomingPlusAngle(2200, EnemyType.FIRING_CIRCLE, 60, 0.3));
        bossCIOFiring.add(ControlNull.create(2500));
        
        bossCEOFiring = new ControlPattern(true);
        bossCEOFiring.add(ControlFiring.createHomingPlusAngleWithOffsetsRandom(0, EnemyType.FIRING_CIRCLE, 330, 45, -20, 0.4, 10, 0.05));
        bossCEOFiring.add(ControlFiring.createHomingPlusAngleWithOffsetsRandom(0, EnemyType.FIRING_CIRCLE, 330, 45, 0, 0.4, 10, 0.05));
        bossCEOFiring.add(ControlFiring.createHomingPlusAngleWithOffsetsRandom(0, EnemyType.FIRING_CIRCLE, 330, 45, 20, 0.4, 10, 0.05));
        bossCEOFiring.add(ControlFiring.createControlPatternWithOffsets(400, EnemyType.FIRING_CHAIR, 230, -15, firingLeftExplodingChair));
        bossCEOFiring.add(ControlFiring.createWithXYOffsetsAndRandom(800, EnemyType.FIRING_MISSILE, -10, 130, -0.5, 0, 0.02, 0.04));
        bossCEOFiring.add(ControlFiring.createWithXYOffsetsAndRandom(1000, EnemyType.FIRING_MISSILE, -10, 130, -0.5, 0, 0.02, 0.04));
        bossCEOFiring.add(ControlFiring.createHomingPlusAngleWithOffsetsRandom(1200, EnemyType.FIRING_CIRCLE, 330, 45, -20, 0.3, 10, 0.05));
        bossCEOFiring.add(ControlFiring.createHomingPlusAngleWithOffsetsRandom(1200, EnemyType.FIRING_CIRCLE, 330, 45, 0, 0.3, 10, 0.05));
        bossCEOFiring.add(ControlFiring.createHomingPlusAngleWithOffsetsRandom(1200, EnemyType.FIRING_CIRCLE, 330, 45, 20, 0.3, 10, 0.05));
        bossCEOFiring.add(ControlFiring.createWithXYOffsetsAndRandom(1200, EnemyType.FIRING_MISSILE, -10, 130, -0.5, 0, 0.02, 0.04));
        bossCEOFiring.add(ControlFiring.createWithXYOffsetsAndRandom(1400, EnemyType.FIRING_MISSILE, -10, 130, -0.5, 0, 0.02, 0.04));
        bossCEOFiring.add(ControlFiring.createControlPatternWithOffsets(1800, EnemyType.FIRING_CHAIR, 230, -15, firingUpExplodingChair));
        bossCEOFiring.add(ControlFiring.createWithXYOffsetsAndRandom(2000, EnemyType.FIRING_MISSILE, -10, 130, -0.5, 0, 0.02, 0.04));
        bossCEOFiring.add(ControlFiring.createWithXYOffsetsAndRandom(2400, EnemyType.FIRING_MISSILE, -10, 130, -0.5, 0, 0.02, 0.04));
        bossCEOFiring.add(ControlFiring.createControlPatternWithOffsets(2800, EnemyType.FIRING_CHAIR, 230, -15, firingUpExplodingChair));
        
        bossDirectorFiring = new ControlPattern(true);
        bossDirectorFiring.add(ControlFiring.createWithXYOffsetsAndRandom(0, EnemyType.FIRING_MISSILE, -15, 65, -0.45, 0, 0.1, 0.1));
        bossDirectorFiring.add(ControlFiring.createWithXYOffsetsAndRandom(200, EnemyType.FIRING_MISSILE, -15, 65, -0.45, 0, 0.1, 0.1));
        bossDirectorFiring.add(ControlFiring.createWithXYOffsetsAndRandom(400, EnemyType.FIRING_MISSILE, -15, 65, -0.45, 0, 0.1, 0.1));
        bossDirectorFiring.add(ControlNull.create(1000));
        
        bossChairmanFiring = new ControlPattern(true);
        bossChairmanFiring.add(ControlFiring.createWithAngleOffsets(0, EnemyType.FIRING_CIRCLE, 20, 50, 0, 0.35));
        bossChairmanFiring.add(ControlFiring.createWithAngleOffsets(50, EnemyType.FIRING_CIRCLE, 20, 50, 20, 0.35));
        bossChairmanFiring.add(ControlFiring.createWithAngleOffsets(100, EnemyType.FIRING_CIRCLE, 20, 50, 40, 0.35));
        bossChairmanFiring.add(ControlFiring.createWithAngleOffsets(150, EnemyType.FIRING_CIRCLE, 20, 50, 60, 0.35));
        bossChairmanFiring.add(ControlFiring.createWithAngleOffsets(200, EnemyType.FIRING_CIRCLE, 20, 50, 80, 0.35));
        bossChairmanFiring.add(ControlFiring.createWithAngleOffsets(250, EnemyType.FIRING_CIRCLE, 20, 50, 100, 0.35));
        bossChairmanFiring.add(ControlFiring.createWithAngleOffsets(300, EnemyType.FIRING_CIRCLE, 20, 50, 120, 0.35));
        bossChairmanFiring.add(ControlFiring.createWithAngleOffsets(350, EnemyType.FIRING_CIRCLE, 20, 50, 140, 0.35));
        bossChairmanFiring.add(ControlFiring.createWithAngleOffsets(400, EnemyType.FIRING_CIRCLE, 20, 50, 160, 0.35));
        bossChairmanFiring.add(ControlFiring.createWithAngleOffsets(450, EnemyType.FIRING_CIRCLE, 20, 50, 180, 0.35));
        bossChairmanFiring.add(ControlFiring.createWithAngleOffsets(500, EnemyType.FIRING_CIRCLE, 20, 50, 200, 0.35));
        bossChairmanFiring.add(ControlFiring.createWithAngleOffsets(550, EnemyType.FIRING_CIRCLE, 20, 50, 220, 0.35));
        bossChairmanFiring.add(ControlFiring.createWithAngleOffsets(600, EnemyType.FIRING_CIRCLE, 20, 50, 240, 0.35));
        bossChairmanFiring.add(ControlFiring.createWithAngleOffsets(650, EnemyType.FIRING_CIRCLE, 20, 50, 260, 0.35));
        bossChairmanFiring.add(ControlFiring.createWithAngleOffsets(700, EnemyType.FIRING_CIRCLE, 20, 50, 280, 0.35));
        bossChairmanFiring.add(ControlFiring.createWithAngleOffsets(750, EnemyType.FIRING_CIRCLE, 20, 50, 300, 0.35));
        bossChairmanFiring.add(ControlFiring.createWithAngleOffsets(800, EnemyType.FIRING_CIRCLE, 20, 50, 320, 0.35));
        bossChairmanFiring.add(ControlFiring.createWithAngleOffsets(850, EnemyType.FIRING_CIRCLE, 20, 50, 340, 0.35));
        bossChairmanFiring.add(ControlFiring.createWithAngleOffsets(900, EnemyType.FIRING_CIRCLE, 20, 50, 10, 0.35));
        bossChairmanFiring.add(ControlFiring.createWithAngleOffsets(950, EnemyType.FIRING_CIRCLE, 20, 50, 30, 0.35));
        bossChairmanFiring.add(ControlFiring.createWithAngleOffsets(1000, EnemyType.FIRING_CIRCLE, 20, 50, 50, 0.35));
        bossChairmanFiring.add(ControlFiring.createWithAngleOffsets(1050, EnemyType.FIRING_CIRCLE, 20, 50, 70, 0.35));
        bossChairmanFiring.add(ControlFiring.createWithAngleOffsets(1100, EnemyType.FIRING_CIRCLE, 20, 50, 90, 0.35));
        bossChairmanFiring.add(ControlFiring.createWithAngleOffsets(1150, EnemyType.FIRING_CIRCLE, 20, 50, 110, 0.35));
        bossChairmanFiring.add(ControlFiring.createWithAngleOffsets(1200, EnemyType.FIRING_CIRCLE, 20, 50, 130, 0.35));
        bossChairmanFiring.add(ControlFiring.createWithAngleOffsets(1250, EnemyType.FIRING_CIRCLE, 20, 50, 150, 0.35));
        bossChairmanFiring.add(ControlFiring.createWithAngleOffsets(1300, EnemyType.FIRING_CIRCLE, 20, 50, 170, 0.35));
        bossChairmanFiring.add(ControlFiring.createWithAngleOffsets(1350, EnemyType.FIRING_CIRCLE, 20, 50, 190, 0.35));
        bossChairmanFiring.add(ControlFiring.createWithAngleOffsets(1400, EnemyType.FIRING_CIRCLE, 20, 50, 210, 0.35));
        bossChairmanFiring.add(ControlFiring.createWithAngleOffsets(1450, EnemyType.FIRING_CIRCLE, 20, 50, 230, 0.35));
        bossChairmanFiring.add(ControlFiring.createWithAngleOffsets(1500, EnemyType.FIRING_CIRCLE, 20, 50, 250, 0.35));
        bossChairmanFiring.add(ControlFiring.createWithAngleOffsets(1550, EnemyType.FIRING_CIRCLE, 20, 50, 270, 0.35));
        bossChairmanFiring.add(ControlFiring.createWithAngleOffsets(1600, EnemyType.FIRING_CIRCLE, 20, 50, 290, 0.35));
        bossChairmanFiring.add(ControlFiring.createWithAngleOffsets(1650, EnemyType.FIRING_CIRCLE, 20, 50, 310, 0.35));
        bossChairmanFiring.add(ControlFiring.createWithAngleOffsets(1700, EnemyType.FIRING_CIRCLE, 20, 50, 330, 0.35));
        bossChairmanFiring.add(ControlNull.create(1750));
        
        /*
         * ATTACK WAVE PATTERNS
         */
        userEgg5StraightFromRightTop = new AttackWavePattern();
        userEgg5StraightFromRightTop.add(new AttackWavePrimitive(0, EnemyType.USER_EGG, 1, 0.12, straightMovingLeft));
        userEgg5StraightFromRightTop.add(new AttackWavePrimitive(400, EnemyType.USER_EGG, 1, 0.2, straightMovingLeft));
        userEgg5StraightFromRightTop.add(new AttackWavePrimitive(2000, EnemyType.USER_EGG, 1, 0.38, straightMovingLeft));
        userEgg5StraightFromRightTop.add(new AttackWavePrimitive(2500, EnemyType.USER_EGG, 1, 0.28, straightMovingLeft));
        userEgg5StraightFromRightTop.add(new AttackWavePrimitive(2800, EnemyType.USER_EGG, 1, 0.40, straightMovingLeft));
        
        userEgg3StraightFromLeftBottom = new AttackWavePattern();
        userEgg3StraightFromLeftBottom.add(new AttackWavePrimitive(0, EnemyType.USER_EGG, -0.2, 0.9, straightMovingRight));
        userEgg3StraightFromLeftBottom.add(new AttackWavePrimitive(500, EnemyType.USER_EGG, -0.2, 0.85, straightMovingRight));
        userEgg3StraightFromLeftBottom.add(new AttackWavePrimitive(1100, EnemyType.USER_EGG, -0.2, 0.84, straightMovingRight));
        
        userEgg3StraightDownFromTopRight = new AttackWavePattern();
        userEgg3StraightDownFromTopRight.add(new AttackWavePrimitive(0, EnemyType.USER_EGG, 0.8, -0.1, straightMovingDown));
        userEgg3StraightDownFromTopRight.add(new AttackWavePrimitive(400, EnemyType.USER_EGG, 0.72, -0.1, straightMovingDown));
        userEgg3StraightDownFromTopRight.add(new AttackWavePrimitive(1000, EnemyType.USER_EGG, 0.6, -0.1, straightMovingDown));
        
        userEgg3StraightUpFromBottomRight = new AttackWavePattern();
        userEgg3StraightUpFromBottomRight.add(new AttackWavePrimitive(0, EnemyType.USER_EGG, 0.7, 1, straightMovingUp));
        userEgg3StraightUpFromBottomRight.add(new AttackWavePrimitive(800, EnemyType.USER_EGG, 0.65, 1, straightMovingUp));
        userEgg3StraightUpFromBottomRight.add(new AttackWavePrimitive(1200, EnemyType.USER_EGG, 0.8, 1, straightMovingUp));
        
        userEgg3StraightLeftUpFromRightBottom = new AttackWavePattern();
        userEgg3StraightLeftUpFromRightBottom.add(new AttackWavePrimitive(0, EnemyType.USER_EGG, 1, 0.9, straightMovingLeftUp));
        userEgg3StraightLeftUpFromRightBottom.add(new AttackWavePrimitive(500, EnemyType.USER_EGG, 1, 0.7, straightMovingLeftUp));
        userEgg3StraightLeftUpFromRightBottom.add(new AttackWavePrimitive(1400, EnemyType.USER_EGG, 1, 0.76, straightMovingLeftUp));
        
        userEgg3StraightLeftDownFromRightTop = new AttackWavePattern();
        userEgg3StraightLeftDownFromRightTop.add(new AttackWavePrimitive(0, EnemyType.USER_EGG, 1, 0.25, straightMovingLeftDown));
        userEgg3StraightLeftDownFromRightTop.add(new AttackWavePrimitive(800, EnemyType.USER_EGG, 1, 0.35, straightMovingLeftDown));
        userEgg3StraightLeftDownFromRightTop.add(new AttackWavePrimitive(1200, EnemyType.USER_EGG, 1, 0.05, straightMovingLeftDown));
        
        userEgg3StraightRightUpFromBottomLeft = new AttackWavePattern();
        userEgg3StraightRightUpFromBottomLeft.add(new AttackWavePrimitive(0, EnemyType.USER_EGG, 0.12, 1, straightMovingRightUp));
        userEgg3StraightRightUpFromBottomLeft.add(new AttackWavePrimitive(700, EnemyType.USER_EGG, 0, 1, straightMovingRightUp));
        userEgg3StraightRightUpFromBottomLeft.add(new AttackWavePrimitive(1500, EnemyType.USER_EGG, 0.05, 1, straightMovingRightUp));
        
        userEgg4StraightSwoopFromRightTop = new AttackWavePattern();
        userEgg4StraightSwoopFromRightTop.add(new AttackWavePrimitive(0, EnemyType.USER_EGG, 1, 0.33, straightSwoopMovingLeft));
        userEgg4StraightSwoopFromRightTop.add(new AttackWavePrimitive(400, EnemyType.USER_EGG, 1, 0.02, straightSwoopMovingLeft));
        userEgg4StraightSwoopFromRightTop.add(new AttackWavePrimitive(600, EnemyType.USER_EGG, 1, 0.3, straightSwoopMovingLeft));
        userEgg4StraightSwoopFromRightTop.add(new AttackWavePrimitive(1200, EnemyType.USER_EGG, 1, 0.16, straightSwoopMovingLeft));
        
        userEgg2StraightSwoopFromRightTop = new AttackWavePattern();
        userEgg2StraightSwoopFromRightTop.add(new AttackWavePrimitive(0, EnemyType.USER_EGG, 1, 0.05, straightSwoopMovingLeft));
        userEgg2StraightSwoopFromRightTop.add(new AttackWavePrimitive(500, EnemyType.USER_EGG, 1, 0.12, straightSwoopMovingLeft));
        
        userEgg6StraightSwoopFromRightTop = new AttackWavePattern();
        userEgg6StraightSwoopFromRightTop.add(new AttackWavePrimitive(0, EnemyType.USER_EGG, 1, 0.05, straightSwoopMovingLeft));
        userEgg6StraightSwoopFromRightTop.add(new AttackWavePrimitive(100, EnemyType.USER_EGG, 1, 0.40, straightSwoopMovingLeft));
        userEgg6StraightSwoopFromRightTop.add(new AttackWavePrimitive(1000, EnemyType.USER_EGG, 1, 0.20, straightSwoopMovingLeft));
        userEgg6StraightSwoopFromRightTop.add(new AttackWavePrimitive(3000, EnemyType.USER_EGG, 1, 0.25, straightSwoopMovingLeft));
        userEgg6StraightSwoopFromRightTop.add(new AttackWavePrimitive(3200, EnemyType.USER_EGG, 1, 0.11, straightSwoopMovingLeft));
        userEgg6StraightSwoopFromRightTop.add(new AttackWavePrimitive(4000, EnemyType.USER_EGG, 1, 0.18, straightSwoopMovingLeft));
        
        
        userUfo1StaticFromTopMiddle = new AttackWavePattern();
        userUfo1StaticFromTopMiddle.add(new AttackWavePrimitive(0, EnemyType.USER_UFO, 0.5, 0.1, staticMovingNowhere));
        
        userUfo3StraightSwoopFromRightBottomFiring3Circles = new AttackWavePattern();
        userUfo3StraightSwoopFromRightBottomFiring3Circles.add(new AttackWavePrimitive(0, EnemyType.USER_UFO, 1, 1, straightSwoopMovingLeftFire1BulletMiddle));
        userUfo3StraightSwoopFromRightBottomFiring3Circles.add(new AttackWavePrimitive(100, EnemyType.USER_UFO, 1, 0.8, straightSwoopMovingLeftFire1BulletMiddle));
        userUfo3StraightSwoopFromRightBottomFiring3Circles.add(new AttackWavePrimitive(200, EnemyType.USER_UFO, 1, 0.6, straightSwoopMovingLeftFire1BulletMiddle));
        
        userUfo4ArcUpLeftFromRightBottom = new AttackWavePattern();
        userUfo4ArcUpLeftFromRightBottom.add(new AttackWavePrimitive(0, EnemyType.USER_UFO, 1, 0.9, arcMovingUpLeft));
        userUfo4ArcUpLeftFromRightBottom.add(new AttackWavePrimitive(400, EnemyType.USER_UFO, 1, 0.9, arcMovingUpLeft));
        userUfo4ArcUpLeftFromRightBottom.add(new AttackWavePrimitive(800, EnemyType.USER_UFO, 1, 0.9, arcMovingUpLeft));
        userUfo4ArcUpLeftFromRightBottom.add(new AttackWavePrimitive(1200, EnemyType.USER_UFO, 1, 0.9, arcMovingUpLeft));
        
        userUfo4ArcLeftUpFromRightBottom = new AttackWavePattern();
        userUfo4ArcLeftUpFromRightBottom.add(new AttackWavePrimitive(0, EnemyType.USER_UFO, 1, 0.9, arcMovingLeftUp));
        userUfo4ArcLeftUpFromRightBottom.add(new AttackWavePrimitive(400, EnemyType.USER_UFO, 1, 0.9, arcMovingLeftUp));
        userUfo4ArcLeftUpFromRightBottom.add(new AttackWavePrimitive(800, EnemyType.USER_UFO, 1, 0.9, arcMovingLeftUp));
        userUfo4ArcLeftUpFromRightBottom.add(new AttackWavePrimitive(1200, EnemyType.USER_UFO, 1, 0.9, arcMovingLeftUp));
        
        userUfo4ArcDownLeftFromRightTop = new AttackWavePattern();
        userUfo4ArcDownLeftFromRightTop.add(new AttackWavePrimitive(0, EnemyType.USER_UFO, 1, 0.1, arcMovingDownLeft));
        userUfo4ArcDownLeftFromRightTop.add(new AttackWavePrimitive(400, EnemyType.USER_UFO, 1, 0.1, arcMovingDownLeft));
        userUfo4ArcDownLeftFromRightTop.add(new AttackWavePrimitive(800, EnemyType.USER_UFO, 1, 0.1, arcMovingDownLeft));
        userUfo4ArcDownLeftFromRightTop.add(new AttackWavePrimitive(1200, EnemyType.USER_UFO, 1, 0.1, arcMovingDownLeft));
        
        userUfo4ArcLeftDownFromRightTop = new AttackWavePattern();
        userUfo4ArcLeftDownFromRightTop.add(new AttackWavePrimitive(0, EnemyType.USER_UFO, 1, 0.1, arcMovingLeftDown));
        userUfo4ArcLeftDownFromRightTop.add(new AttackWavePrimitive(400, EnemyType.USER_UFO, 1, 0.1, arcMovingLeftDown));
        userUfo4ArcLeftDownFromRightTop.add(new AttackWavePrimitive(800, EnemyType.USER_UFO, 1, 0.1, arcMovingLeftDown));
        userUfo4ArcLeftDownFromRightTop.add(new AttackWavePrimitive(1200, EnemyType.USER_UFO, 1, 0.1, arcMovingLeftDown));
        
        userUfo4ArcUpLeftFromRightBottomFiringCircleSingleHoming = new AttackWavePattern();
        userUfo4ArcUpLeftFromRightBottomFiringCircleSingleHoming.add(new AttackWavePrimitive(0, EnemyType.USER_UFO, 1, 0.9, arcMovingUpLeft, firingHomingFastSingleCircle));
        userUfo4ArcUpLeftFromRightBottomFiringCircleSingleHoming.add(new AttackWavePrimitive(400, EnemyType.USER_UFO, 1, 0.9, arcMovingUpLeft, firingHomingFastSingleCircle));
        userUfo4ArcUpLeftFromRightBottomFiringCircleSingleHoming.add(new AttackWavePrimitive(800, EnemyType.USER_UFO, 1, 0.9, arcMovingUpLeft, firingHomingFastSingleCircle));
        userUfo4ArcUpLeftFromRightBottomFiringCircleSingleHoming.add(new AttackWavePrimitive(1200, EnemyType.USER_UFO, 1, 0.9, arcMovingUpLeft, firingHomingFastSingleCircle));
        
        userUfo4ArcLeftUpFromRightBottomFiringCircleSingleHoming = new AttackWavePattern();
        userUfo4ArcLeftUpFromRightBottomFiringCircleSingleHoming.add(new AttackWavePrimitive(0, EnemyType.USER_UFO, 1, 0.9, arcMovingLeftUp, firingHomingFastEarlySingleCircle));
        userUfo4ArcLeftUpFromRightBottomFiringCircleSingleHoming.add(new AttackWavePrimitive(400, EnemyType.USER_UFO, 1, 0.9, arcMovingLeftUp, firingHomingFastEarlySingleCircle));
        userUfo4ArcLeftUpFromRightBottomFiringCircleSingleHoming.add(new AttackWavePrimitive(800, EnemyType.USER_UFO, 1, 0.9, arcMovingLeftUp, firingHomingFastEarlySingleCircle));
        userUfo4ArcLeftUpFromRightBottomFiringCircleSingleHoming.add(new AttackWavePrimitive(1200, EnemyType.USER_UFO, 1, 0.9, arcMovingLeftUp, firingHomingFastEarlySingleCircle));
        
        userUfo4ArcDownLeftFromRightTopFiringCircleSingleHoming = new AttackWavePattern();
        userUfo4ArcDownLeftFromRightTopFiringCircleSingleHoming.add(new AttackWavePrimitive(0, EnemyType.USER_UFO, 1, 0.1, arcMovingDownLeft, firingHomingFastSingleCircle));
        userUfo4ArcDownLeftFromRightTopFiringCircleSingleHoming.add(new AttackWavePrimitive(400, EnemyType.USER_UFO, 1, 0.1, arcMovingDownLeft, firingHomingFastSingleCircle));
        userUfo4ArcDownLeftFromRightTopFiringCircleSingleHoming.add(new AttackWavePrimitive(800, EnemyType.USER_UFO, 1, 0.1, arcMovingDownLeft, firingHomingFastSingleCircle));
        userUfo4ArcDownLeftFromRightTopFiringCircleSingleHoming.add(new AttackWavePrimitive(1200, EnemyType.USER_UFO, 1, 0.1, arcMovingDownLeft, firingHomingFastSingleCircle));
        
        userUfo4ArcLeftDownFromRightTopFiringCircleSingleHoming = new AttackWavePattern();
        userUfo4ArcLeftDownFromRightTopFiringCircleSingleHoming.add(new AttackWavePrimitive(0, EnemyType.USER_UFO, 1, 0.1, arcMovingLeftDown, firingHomingFastEarlySingleCircle));
        userUfo4ArcLeftDownFromRightTopFiringCircleSingleHoming.add(new AttackWavePrimitive(400, EnemyType.USER_UFO, 1, 0.1, arcMovingLeftDown, firingHomingFastEarlySingleCircle));
        userUfo4ArcLeftDownFromRightTopFiringCircleSingleHoming.add(new AttackWavePrimitive(800, EnemyType.USER_UFO, 1, 0.1, arcMovingLeftDown, firingHomingFastEarlySingleCircle));
        userUfo4ArcLeftDownFromRightTopFiringCircleSingleHoming.add(new AttackWavePrimitive(1200, EnemyType.USER_UFO, 1, 0.1, arcMovingLeftDown, firingHomingFastEarlySingleCircle));
        
        userUfo8ArcAlternateVerticalFromRight = new AttackWavePattern();
        userUfo8ArcAlternateVerticalFromRight.add(new AttackWavePrimitive(0, EnemyType.USER_UFO, 1, 0.1, arcMovingDownLeft));
        userUfo8ArcAlternateVerticalFromRight.add(new AttackWavePrimitive(500, EnemyType.USER_UFO, 1, 0.9, arcMovingUpLeft));
        userUfo8ArcAlternateVerticalFromRight.add(new AttackWavePrimitive(1000, EnemyType.USER_UFO, 1, 0.1, arcMovingDownLeft));
        userUfo8ArcAlternateVerticalFromRight.add(new AttackWavePrimitive(1500, EnemyType.USER_UFO, 1, 0.9, arcMovingUpLeft));
        userUfo8ArcAlternateVerticalFromRight.add(new AttackWavePrimitive(2000, EnemyType.USER_UFO, 1, 0.1, arcMovingDownLeft));
        userUfo8ArcAlternateVerticalFromRight.add(new AttackWavePrimitive(2500, EnemyType.USER_UFO, 1, 0.9, arcMovingUpLeft));
        userUfo8ArcAlternateVerticalFromRight.add(new AttackWavePrimitive(3000, EnemyType.USER_UFO, 1, 0.1, arcMovingDownLeft));
        userUfo8ArcAlternateVerticalFromRight.add(new AttackWavePrimitive(3500, EnemyType.USER_UFO, 1, 0.9, arcMovingUpLeft));
        
        userUfo8ArcAlternateHorizontalFromRight = new AttackWavePattern();
        userUfo8ArcAlternateHorizontalFromRight.add(new AttackWavePrimitive(0, EnemyType.USER_UFO, 1, 0.1, arcMovingLeftDown));
        userUfo8ArcAlternateHorizontalFromRight.add(new AttackWavePrimitive(500, EnemyType.USER_UFO, 1, 0.9, arcMovingLeftUp));
        userUfo8ArcAlternateHorizontalFromRight.add(new AttackWavePrimitive(1000, EnemyType.USER_UFO, 1, 0.1, arcMovingLeftDown));
        userUfo8ArcAlternateHorizontalFromRight.add(new AttackWavePrimitive(1500, EnemyType.USER_UFO, 1, 0.9, arcMovingLeftUp));
        userUfo8ArcAlternateHorizontalFromRight.add(new AttackWavePrimitive(2000, EnemyType.USER_UFO, 1, 0.1, arcMovingLeftDown));
        userUfo8ArcAlternateHorizontalFromRight.add(new AttackWavePrimitive(2500, EnemyType.USER_UFO, 1, 0.9, arcMovingLeftUp));
        userUfo8ArcAlternateHorizontalFromRight.add(new AttackWavePrimitive(3000, EnemyType.USER_UFO, 1, 0.1, arcMovingLeftDown));
        userUfo8ArcAlternateHorizontalFromRight.add(new AttackWavePrimitive(3500, EnemyType.USER_UFO, 1, 0.9, arcMovingLeftUp));
        
        userUfo3ParabolaFromRightTop = new AttackWavePattern();
        userUfo3ParabolaFromRightTop.add(new AttackWavePrimitive(0, EnemyType.USER_UFO, 1, 0.1, parabolaMovingLeftDown));
        userUfo3ParabolaFromRightTop.add(new AttackWavePrimitive(200, EnemyType.USER_UFO, 1, 0.3, parabolaMovingLeftDown));
        userUfo3ParabolaFromRightTop.add(new AttackWavePrimitive(400, EnemyType.USER_UFO, 1, 0.5, parabolaMovingLeftDown));
        
        userUfo3ParabolaFromRightBottom = new AttackWavePattern();
        userUfo3ParabolaFromRightBottom.add(new AttackWavePrimitive(0, EnemyType.USER_UFO, 1, 0.9, parabolaMovingLeftUp));
        userUfo3ParabolaFromRightBottom.add(new AttackWavePrimitive(200, EnemyType.USER_UFO, 1, 0.7, parabolaMovingLeftUp));
        userUfo3ParabolaFromRightBottom.add(new AttackWavePrimitive(400, EnemyType.USER_UFO, 1, 0.5, parabolaMovingLeftUp));
        
        userUfo5Staircase1StepFromRightTop = new AttackWavePattern();
        userUfo5Staircase1StepFromRightTop.add(new AttackWavePrimitive(0, EnemyType.USER_UFO, 1, 0.3, staircase1StepMovingLeftDown));
        userUfo5Staircase1StepFromRightTop.add(new AttackWavePrimitive(200, EnemyType.USER_UFO, 1, 0.3, staircase1StepMovingLeftDown));
        userUfo5Staircase1StepFromRightTop.add(new AttackWavePrimitive(400, EnemyType.USER_UFO, 1, 0.3, staircase1StepMovingLeftDown));
        userUfo5Staircase1StepFromRightTop.add(new AttackWavePrimitive(600, EnemyType.USER_UFO, 1, 0.3, staircase1StepMovingLeftDown));
        userUfo5Staircase1StepFromRightTop.add(new AttackWavePrimitive(800, EnemyType.USER_UFO, 1, 0.3, staircase1StepMovingLeftDown));
        
        userUfo5Staircase5StepsFromRightBottom = new AttackWavePattern();
        userUfo5Staircase5StepsFromRightBottom.add(new AttackWavePrimitive(0, EnemyType.USER_UFO, 1, 0.1, staircase5StepsMovingLeftDown));
        userUfo5Staircase5StepsFromRightBottom.add(new AttackWavePrimitive(300, EnemyType.USER_UFO, 1, 0.1, staircase5StepsMovingLeftDown));
        userUfo5Staircase5StepsFromRightBottom.add(new AttackWavePrimitive(600, EnemyType.USER_UFO, 1, 0.1, staircase5StepsMovingLeftDown));
        userUfo5Staircase5StepsFromRightBottom.add(new AttackWavePrimitive(900, EnemyType.USER_UFO, 1, 0.1, staircase5StepsMovingLeftDown));
        userUfo5Staircase5StepsFromRightBottom.add(new AttackWavePrimitive(1200, EnemyType.USER_UFO, 1, 0.1, staircase5StepsMovingLeftDown));
        
        userUfo5Staircase5StepsFromRightTop = new AttackWavePattern();
        userUfo5Staircase5StepsFromRightTop.add(new AttackWavePrimitive(0, EnemyType.USER_UFO, 1, 0.9, staircase5StepsMovingLeftUp));
        userUfo5Staircase5StepsFromRightTop.add(new AttackWavePrimitive(300, EnemyType.USER_UFO, 1, 0.9, staircase5StepsMovingLeftUp));
        userUfo5Staircase5StepsFromRightTop.add(new AttackWavePrimitive(600, EnemyType.USER_UFO, 1, 0.9, staircase5StepsMovingLeftUp));
        userUfo5Staircase5StepsFromRightTop.add(new AttackWavePrimitive(900, EnemyType.USER_UFO, 1, 0.9, staircase5StepsMovingLeftUp));
        userUfo5Staircase5StepsFromRightTop.add(new AttackWavePrimitive(1200, EnemyType.USER_UFO, 1, 0.9, staircase5StepsMovingLeftUp));
        
        userUfo5LoopSingleFromRightMiddle = new AttackWavePattern();
        userUfo5LoopSingleFromRightMiddle.add(new AttackWavePrimitive(0, EnemyType.USER_UFO, 1, 0.5, loopSingleUpMovingLeft));
        userUfo5LoopSingleFromRightMiddle.add(new AttackWavePrimitive(300, EnemyType.USER_UFO, 1, 0.5, loopSingleUpMovingLeft));
        userUfo5LoopSingleFromRightMiddle.add(new AttackWavePrimitive(600, EnemyType.USER_UFO, 1, 0.5, loopSingleUpMovingLeft));
        userUfo5LoopSingleFromRightMiddle.add(new AttackWavePrimitive(900, EnemyType.USER_UFO, 1, 0.5, loopSingleUpMovingLeft));
        userUfo5LoopSingleFromRightMiddle.add(new AttackWavePrimitive(1200, EnemyType.USER_UFO, 1, 0.5, loopSingleUpMovingLeft));
        
        userUfo5LoopSingleFromRightTop = new AttackWavePattern();
        userUfo5LoopSingleFromRightTop.add(new AttackWavePrimitive(0, EnemyType.USER_UFO, 1, 0.05, loopSingleDownMovingLeft));
        userUfo5LoopSingleFromRightTop.add(new AttackWavePrimitive(300, EnemyType.USER_UFO, 1, 0.05, loopSingleDownMovingLeft));
        userUfo5LoopSingleFromRightTop.add(new AttackWavePrimitive(600, EnemyType.USER_UFO, 1, 0.05, loopSingleDownMovingLeft));
        userUfo5LoopSingleFromRightTop.add(new AttackWavePrimitive(900, EnemyType.USER_UFO, 1, 0.05, loopSingleDownMovingLeft));
        userUfo5LoopSingleFromRightTop.add(new AttackWavePrimitive(1200, EnemyType.USER_UFO, 1, 0.05, loopSingleDownMovingLeft));
        
        userUfo5LoopXSingleRightFromRightBottom = new AttackWavePattern();
        userUfo5LoopXSingleRightFromRightBottom.add(new AttackWavePrimitive(0, EnemyType.USER_UFO, 1, 0.99, loopXSingleRightMovingLeftUp));
        userUfo5LoopXSingleRightFromRightBottom.add(new AttackWavePrimitive(300, EnemyType.USER_UFO, 1, 0.99, loopXSingleRightMovingLeftUp));
        userUfo5LoopXSingleRightFromRightBottom.add(new AttackWavePrimitive(600, EnemyType.USER_UFO, 1, 0.99, loopXSingleRightMovingLeftUp));
        userUfo5LoopXSingleRightFromRightBottom.add(new AttackWavePrimitive(900, EnemyType.USER_UFO, 1, 0.99, loopXSingleRightMovingLeftUp));
        userUfo5LoopXSingleRightFromRightBottom.add(new AttackWavePrimitive(1200, EnemyType.USER_UFO, 1, 0.99, loopXSingleRightMovingLeftUp));
        
        userUfo5LoopXSingleRightFromRightTop = new AttackWavePattern();
        userUfo5LoopXSingleRightFromRightTop.add(new AttackWavePrimitive(0, EnemyType.USER_UFO, 1, 0.01, loopXSingleRightMovingLeftDown));
        userUfo5LoopXSingleRightFromRightTop.add(new AttackWavePrimitive(300, EnemyType.USER_UFO, 1, 0.01, loopXSingleRightMovingLeftDown));
        userUfo5LoopXSingleRightFromRightTop.add(new AttackWavePrimitive(600, EnemyType.USER_UFO, 1, 0.01, loopXSingleRightMovingLeftDown));
        userUfo5LoopXSingleRightFromRightTop.add(new AttackWavePrimitive(900, EnemyType.USER_UFO, 1, 0.01, loopXSingleRightMovingLeftDown));
        userUfo5LoopXSingleRightFromRightTop.add(new AttackWavePrimitive(1200, EnemyType.USER_UFO, 1, 0.01, loopXSingleRightMovingLeftDown));
        
        userUfo5LoopVSingleRightFromRightBottom = new AttackWavePattern();
        userUfo5LoopVSingleRightFromRightBottom.add(new AttackWavePrimitive(0, EnemyType.USER_UFO, 1, 0.99, loopVSingleRightMovingLeftUp));
        userUfo5LoopVSingleRightFromRightBottom.add(new AttackWavePrimitive(300, EnemyType.USER_UFO, 1, 0.99, loopVSingleRightMovingLeftUp));
        userUfo5LoopVSingleRightFromRightBottom.add(new AttackWavePrimitive(600, EnemyType.USER_UFO, 1, 0.99, loopVSingleRightMovingLeftUp));
        userUfo5LoopVSingleRightFromRightBottom.add(new AttackWavePrimitive(900, EnemyType.USER_UFO, 1, 0.99, loopVSingleRightMovingLeftUp));
        userUfo5LoopVSingleRightFromRightBottom.add(new AttackWavePrimitive(1200, EnemyType.USER_UFO, 1, 0.99, loopVSingleRightMovingLeftUp));
        
        userUfo5LoopVSingleRightFromRightTop = new AttackWavePattern();
        userUfo5LoopVSingleRightFromRightTop.add(new AttackWavePrimitive(0, EnemyType.USER_UFO, 1, 0.01, loopVSingleRightMovingLeftDown));
        userUfo5LoopVSingleRightFromRightTop.add(new AttackWavePrimitive(300, EnemyType.USER_UFO, 1, 0.01, loopVSingleRightMovingLeftDown));
        userUfo5LoopVSingleRightFromRightTop.add(new AttackWavePrimitive(600, EnemyType.USER_UFO, 1, 0.01, loopVSingleRightMovingLeftDown));
        userUfo5LoopVSingleRightFromRightTop.add(new AttackWavePrimitive(900, EnemyType.USER_UFO, 1, 0.01, loopVSingleRightMovingLeftDown));
        userUfo5LoopVSingleRightFromRightTop.add(new AttackWavePrimitive(1200, EnemyType.USER_UFO, 1, 0.01, loopVSingleRightMovingLeftDown));
        
        userUfo5DiamondFromRightTopFiring20Circles = new AttackWavePattern();
        userUfo5DiamondFromRightTopFiring20Circles.add(new AttackWavePrimitive(0, EnemyType.USER_UFO, 1, 0, diamondFire4Bullets));
        userUfo5DiamondFromRightTopFiring20Circles.add(new AttackWavePrimitive(1000, EnemyType.USER_UFO, 1, 0.2, diamondFire4Bullets));
        userUfo5DiamondFromRightTopFiring20Circles.add(new AttackWavePrimitive(2000, EnemyType.USER_UFO, 1, 0.4, diamondFire4Bullets));
        userUfo5DiamondFromRightTopFiring20Circles.add(new AttackWavePrimitive(3000, EnemyType.USER_UFO, 1, 0.6, diamondFire4Bullets));
        userUfo5DiamondFromRightTopFiring20Circles.add(new AttackWavePrimitive(4000, EnemyType.USER_UFO, 1, 0.8, diamondFire4Bullets));
        
        
        userBurger4Rectangle2CornersFromRightMiddle = new AttackWavePattern();
        userBurger4Rectangle2CornersFromRightMiddle.add(new AttackWavePrimitive(0, EnemyType.USER_BURGER, 1, 0.6, rectangle2CornersMovingLeftUp));
        userBurger4Rectangle2CornersFromRightMiddle.add(new AttackWavePrimitive(250, EnemyType.USER_BURGER, 1, 0.6, rectangle2CornersMovingLeftUp));
        userBurger4Rectangle2CornersFromRightMiddle.add(new AttackWavePrimitive(500, EnemyType.USER_BURGER, 1, 0.6, rectangle2CornersMovingLeftUp));
        userBurger4Rectangle2CornersFromRightMiddle.add(new AttackWavePrimitive(750, EnemyType.USER_BURGER, 1, 0.6, rectangle2CornersMovingLeftUp));
        
        userBurger4Rectangle2CornersFromRightTop = new AttackWavePattern();
        userBurger4Rectangle2CornersFromRightTop.add(new AttackWavePrimitive(0, EnemyType.USER_BURGER, 1, 0.2, rectangle2CornersMovingLeftDown));
        userBurger4Rectangle2CornersFromRightTop.add(new AttackWavePrimitive(250, EnemyType.USER_BURGER, 1, 0.2, rectangle2CornersMovingLeftDown));
        userBurger4Rectangle2CornersFromRightTop.add(new AttackWavePrimitive(500, EnemyType.USER_BURGER, 1, 0.2, rectangle2CornersMovingLeftDown));
        userBurger4Rectangle2CornersFromRightTop.add(new AttackWavePrimitive(750, EnemyType.USER_BURGER, 1, 0.2, rectangle2CornersMovingLeftDown));
        
        userBurger4Rectangle4CornersFromRightMiddle = new AttackWavePattern();
        userBurger4Rectangle4CornersFromRightMiddle.add(new AttackWavePrimitive(0, EnemyType.USER_BURGER, 1, 0.6, rectangle4CornersMovingLeftUp));
        userBurger4Rectangle4CornersFromRightMiddle.add(new AttackWavePrimitive(250, EnemyType.USER_BURGER, 1, 0.6, rectangle4CornersMovingLeftUp));
        userBurger4Rectangle4CornersFromRightMiddle.add(new AttackWavePrimitive(500, EnemyType.USER_BURGER, 1, 0.6, rectangle4CornersMovingLeftUp));
        userBurger4Rectangle4CornersFromRightMiddle.add(new AttackWavePrimitive(750, EnemyType.USER_BURGER, 1, 0.6, rectangle4CornersMovingLeftUp));
        
        userBurger4Rectangle4CornersFromRightTop = new AttackWavePattern();
        userBurger4Rectangle4CornersFromRightTop.add(new AttackWavePrimitive(0, EnemyType.USER_BURGER, 1, 0.2, rectangle4CornersMovingLeftDown));
        userBurger4Rectangle4CornersFromRightTop.add(new AttackWavePrimitive(250, EnemyType.USER_BURGER, 1, 0.2, rectangle4CornersMovingLeftDown));
        userBurger4Rectangle4CornersFromRightTop.add(new AttackWavePrimitive(500, EnemyType.USER_BURGER, 1, 0.2, rectangle4CornersMovingLeftDown));
        userBurger4Rectangle4CornersFromRightTop.add(new AttackWavePrimitive(750, EnemyType.USER_BURGER, 1, 0.2, rectangle4CornersMovingLeftDown));
        
        userBurger4Diamond1CornerFromRightBottom = new AttackWavePattern();
        userBurger4Diamond1CornerFromRightBottom.add(new AttackWavePrimitive(0, EnemyType.USER_BURGER, 1, 0.6, diamond1CornerMovingLeftUp));
        userBurger4Diamond1CornerFromRightBottom.add(new AttackWavePrimitive(200, EnemyType.USER_BURGER, 1, 0.6, diamond1CornerMovingLeftUp));
        userBurger4Diamond1CornerFromRightBottom.add(new AttackWavePrimitive(400, EnemyType.USER_BURGER, 1, 0.6, diamond1CornerMovingLeftUp));
        userBurger4Diamond1CornerFromRightBottom.add(new AttackWavePrimitive(600, EnemyType.USER_BURGER, 1, 0.6, diamond1CornerMovingLeftUp));
        
        userBurger4Diamond1CornerFromRightTop = new AttackWavePattern();
        userBurger4Diamond1CornerFromRightTop.add(new AttackWavePrimitive(0, EnemyType.USER_BURGER, 1, 0.2, diamond1CornerMovingLeftDown));
        userBurger4Diamond1CornerFromRightTop.add(new AttackWavePrimitive(200, EnemyType.USER_BURGER, 1, 0.2, diamond1CornerMovingLeftDown));
        userBurger4Diamond1CornerFromRightTop.add(new AttackWavePrimitive(400, EnemyType.USER_BURGER, 1, 0.2, diamond1CornerMovingLeftDown));
        userBurger4Diamond1CornerFromRightTop.add(new AttackWavePrimitive(600, EnemyType.USER_BURGER, 1, 0.2, diamond1CornerMovingLeftDown));
        
        userBurger4Diamond3CornersFromRightBottom = new AttackWavePattern();
        userBurger4Diamond3CornersFromRightBottom.add(new AttackWavePrimitive(0, EnemyType.USER_BURGER, 1, 0.7, diamond3CornersMovingLeftUp));
        userBurger4Diamond3CornersFromRightBottom.add(new AttackWavePrimitive(400, EnemyType.USER_BURGER, 1, 0.7, diamond3CornersMovingLeftUp));
        userBurger4Diamond3CornersFromRightBottom.add(new AttackWavePrimitive(800, EnemyType.USER_BURGER, 1, 0.7, diamond3CornersMovingLeftUp));
        userBurger4Diamond3CornersFromRightBottom.add(new AttackWavePrimitive(1200, EnemyType.USER_BURGER, 1, 0.7, diamond3CornersMovingLeftUp));
        
        userBurger4Diamond3CornersFromRightTop = new AttackWavePattern();
        userBurger4Diamond3CornersFromRightTop.add(new AttackWavePrimitive(0, EnemyType.USER_BURGER, 1, 0.3, diamond3CornersMovingLeftDown));
        userBurger4Diamond3CornersFromRightTop.add(new AttackWavePrimitive(400, EnemyType.USER_BURGER, 1, 0.3, diamond3CornersMovingLeftDown));
        userBurger4Diamond3CornersFromRightTop.add(new AttackWavePrimitive(800, EnemyType.USER_BURGER, 1, 0.3, diamond3CornersMovingLeftDown));
        userBurger4Diamond3CornersFromRightTop.add(new AttackWavePrimitive(1200, EnemyType.USER_BURGER, 1, 0.3, diamond3CornersMovingLeftDown));
        
        userBurger3StraightSwoopFromRightBottomFiring3Circles = new AttackWavePattern();
        userBurger3StraightSwoopFromRightBottomFiring3Circles.add(new AttackWavePrimitive(0, EnemyType.USER_BURGER, 1, 1, straightSwoopMovingLeftFire1BulletMiddle));
        userBurger3StraightSwoopFromRightBottomFiring3Circles.add(new AttackWavePrimitive(100, EnemyType.USER_BURGER, 1, 0.8, straightSwoopMovingLeftFire1BulletMiddle));
        userBurger3StraightSwoopFromRightBottomFiring3Circles.add(new AttackWavePrimitive(200, EnemyType.USER_BURGER, 1, 0.6, straightSwoopMovingLeftFire1BulletMiddle));
        
        userBurger3Staircase1StepDownFromRightTop = new AttackWavePattern();
        userBurger3Staircase1StepDownFromRightTop.add(new AttackWavePrimitive(0, EnemyType.USER_BURGER, 1, 0.1, staircase1StepMovingLeftDown));
        userBurger3Staircase1StepDownFromRightTop.add(new AttackWavePrimitive(400, EnemyType.USER_BURGER, 1, 0.2, staircase1StepMovingLeftDown));
        userBurger3Staircase1StepDownFromRightTop.add(new AttackWavePrimitive(800, EnemyType.USER_BURGER, 1, 0.3, staircase1StepMovingLeftDown));
        
        userBurger3Staircase1StepUpFromRightBottom = new AttackWavePattern();
        userBurger3Staircase1StepUpFromRightBottom.add(new AttackWavePrimitive(0, EnemyType.USER_BURGER, 1, 0.8, staircase1StepMovingLeftUp));
        userBurger3Staircase1StepUpFromRightBottom.add(new AttackWavePrimitive(400, EnemyType.USER_BURGER, 1, 0.7, staircase1StepMovingLeftUp));
        userBurger3Staircase1StepUpFromRightBottom.add(new AttackWavePrimitive(800, EnemyType.USER_BURGER, 1, 0.6, staircase1StepMovingLeftUp));
        
        userBurger4Staircase5StepDownFromRightTop = new AttackWavePattern();
        userBurger4Staircase5StepDownFromRightTop.add(new AttackWavePrimitive(0, EnemyType.USER_BURGER, 1, 0.1, staircase5StepsMovingLeftDown));
        userBurger4Staircase5StepDownFromRightTop.add(new AttackWavePrimitive(200, EnemyType.USER_BURGER, 1, 0.1, staircase5StepsMovingLeftDown));
        userBurger4Staircase5StepDownFromRightTop.add(new AttackWavePrimitive(400, EnemyType.USER_BURGER, 1, 0.1, staircase5StepsMovingLeftDown));
        userBurger4Staircase5StepDownFromRightTop.add(new AttackWavePrimitive(600, EnemyType.USER_BURGER, 1, 0.1, staircase5StepsMovingLeftDown));
        
        userBurger4Staircase5StepUpFromRightBottom = new AttackWavePattern();
        userBurger4Staircase5StepUpFromRightBottom.add(new AttackWavePrimitive(0, EnemyType.USER_BURGER, 1, 0.8, staircase5StepsMovingLeftUp));
        userBurger4Staircase5StepUpFromRightBottom.add(new AttackWavePrimitive(200, EnemyType.USER_BURGER, 1, 0.8, staircase5StepsMovingLeftUp));
        userBurger4Staircase5StepUpFromRightBottom.add(new AttackWavePrimitive(400, EnemyType.USER_BURGER, 1, 0.8, staircase5StepsMovingLeftUp));
        userBurger4Staircase5StepUpFromRightBottom.add(new AttackWavePrimitive(600, EnemyType.USER_BURGER, 1, 0.8, staircase5StepsMovingLeftUp));
        
        userBurger4Staircase5StepUpFromLeftBottom = new AttackWavePattern();
        userBurger4Staircase5StepUpFromLeftBottom.add(new AttackWavePrimitive(0, EnemyType.USER_BURGER, 0.1, 1, staircase5StepsMovingRightUp));
        userBurger4Staircase5StepUpFromLeftBottom.add(new AttackWavePrimitive(200, EnemyType.USER_BURGER, 0.1, 1, staircase5StepsMovingRightUp));
        userBurger4Staircase5StepUpFromLeftBottom.add(new AttackWavePrimitive(400, EnemyType.USER_BURGER, 0.1, 1, staircase5StepsMovingRightUp));
        userBurger4Staircase5StepUpFromLeftBottom.add(new AttackWavePrimitive(600, EnemyType.USER_BURGER, 0.1, 1, staircase5StepsMovingRightUp));
        
        procurement1ShmFromRightTop = new AttackWavePattern();
        procurement1ShmFromRightTop.add(new AttackWavePrimitive(0, EnemyType.PROCUREMENT_DAMAGE, 1, 0.1, shmMovingLeft));
        
        procurement1ShmSmallAmplitudeFromRightTop = new AttackWavePattern();
        procurement1ShmSmallAmplitudeFromRightTop.add(new AttackWavePrimitive(0, EnemyType.PROCUREMENT_DAMAGE, 1, 0.2, shmSmallAmplitudeMovingLeft));
        
        procurementElevationFromRightTop = new AttackWavePattern();
        procurementElevationFromRightTop.add(new AttackWavePrimitive(0, EnemyType.PROCUREMENT_ELEVATION, 1, 0, shmInfinitessimalAmplitudeMovingLeft));
        
        procurementSpeedFromRightTop = new AttackWavePattern();
        procurementSpeedFromRightTop.add(new AttackWavePrimitive(0, EnemyType.PROCUREMENT_SPEED, 1, 0, shmTinyAmplitudeMovingLeft));
        
        procurementBombsFromRightTop = new AttackWavePattern();
        procurementBombsFromRightTop.add(new AttackWavePrimitive(0, EnemyType.PROCUREMENT_BOMBS, 1, 0, shmSmallAmplitudeMovingLeft));
        
        procurementDamageFromRightTop = new AttackWavePattern();
        procurementDamageFromRightTop.add(new AttackWavePrimitive(0, EnemyType.PROCUREMENT_DAMAGE, 1, 0, shmMediumAmplitudeMovingLeft));
        
        procurementInvincibilityFromRightTop = new AttackWavePattern();
        procurementInvincibilityFromRightTop.add(new AttackWavePrimitive(0, EnemyType.PROCUREMENT_INVINCIBILITY, 1, 0, shmLargeAmplitudeMovingLeft));
        
        procurementLaserFromRightTop = new AttackWavePattern();
        procurementLaserFromRightTop.add(new AttackWavePrimitive(0, EnemyType.PROCUREMENT_LASER, 1, 0, shmHugeAmplitudeMovingLeft));
        
        procurementNullFromRightTop = new AttackWavePattern();
        procurementNullFromRightTop.add(new AttackWavePrimitive(0, EnemyType.PROCUREMENT_NULL, 1, 0, shmMediumAmplitudeMovingLeft));
        
        
        developerBird1StraightFromRightTop = new AttackWavePattern();
        developerBird1StraightFromRightTop.add(new AttackWavePrimitive(0, EnemyType.DEVELOPER_BIRD, 1, 0.05, straightMovingLeft));
        
        developerBird1StopGoDownFromRightTop = new AttackWavePattern();
        developerBird1StopGoDownFromRightTop.add(new AttackWavePrimitive(0, EnemyType.DEVELOPER_BIRD, 1, 0.05, stopgoDownMovingLeft));
        
        developerBird1StopGoUpFromRightMiddle = new AttackWavePattern();
        developerBird1StopGoUpFromRightMiddle.add(new AttackWavePrimitive(0, EnemyType.DEVELOPER_BIRD, 1, 0.6, stopgoUpMovingLeft));
        
        developerBird1StopGoDownUpFromRightTop = new AttackWavePattern();
        developerBird1StopGoDownUpFromRightTop.add(new AttackWavePrimitive(0, EnemyType.DEVELOPER_BIRD, 1, 0.35, stopgoDownUpMovingLeft));
        
        
        developerPlane1StraightFromRightMiddle = new AttackWavePattern();
        developerPlane1StraightFromRightMiddle.add(new AttackWavePrimitive(0, EnemyType.DEVELOPER_PLANE, 1, 0.5, straightMovingLeftSlowly));
        
        developerPlane3ParabolaFromRightMiddle = new AttackWavePattern();
        developerPlane3ParabolaFromRightMiddle.add(new AttackWavePrimitive(0, EnemyType.DEVELOPER_PLANE, 1, 0.95, parabolaMovingLeftUp));
        developerPlane3ParabolaFromRightMiddle.add(new AttackWavePrimitive(400, EnemyType.DEVELOPER_PLANE, 1, 0.05, parabolaMovingLeftDown));
        developerPlane3ParabolaFromRightMiddle.add(new AttackWavePrimitive(800, EnemyType.DEVELOPER_PLANE, 1, 0.5, straightSwoopMovingLeft));
        
        developerPlane1ZigzagFromRightMiddle = new AttackWavePattern();
        developerPlane1ZigzagFromRightMiddle.add(new AttackWavePrimitive(0, EnemyType.DEVELOPER_PLANE, 1, 0.3, zigzag5CornersMovingLeftDown));
        
        developerPlane3CornersFromRightTop = new AttackWavePattern();
        developerPlane3CornersFromRightTop.add(new AttackWavePrimitive(0, EnemyType.DEVELOPER_PLANE, 1, 0.2, plane3CornersMovingLeft));
        
        developerPlane5CornersFromRightTop = new AttackWavePattern();
        developerPlane5CornersFromRightTop.add(new AttackWavePrimitive(0, EnemyType.DEVELOPER_PLANE, 1, 0.3, plane5CornersMovingLeft));
        
        developerPlane6CornersFromRightTop = new AttackWavePattern();
        developerPlane6CornersFromRightTop.add(new AttackWavePrimitive(0, EnemyType.DEVELOPER_PLANE, 1, 0.2, plane6CornersMovingLeft));
        
        
        developerBubble1StraightFromRightTop = new AttackWavePattern();
        developerBubble1StraightFromRightTop.add(new AttackWavePrimitive(0, EnemyType.DEVELOPER_BUBBLE, 1, 0.1, straightSlowMovingLeft, firingHomingSpread5Circle));
        
        developerBubble1StraightFromLeftBottom = new AttackWavePattern();
        developerBubble1StraightFromLeftBottom.add(new AttackWavePrimitive(0, EnemyType.DEVELOPER_BUBBLE, -0.2, 0.82, straightSlowMovingRight, firingHomingSpread5Circle));
        
        developerBubble1SquarewaveFromRightMiddle = new AttackWavePattern();
        developerBubble1SquarewaveFromRightMiddle.add(new AttackWavePrimitive(0, EnemyType.DEVELOPER_BUBBLE, 1, 0.2, squarewaveLargeAmplitudeMovingLeft, firingHomingSpread5Circle));
        
        developerBubble1SquarewaveRightFromBottomLeft = new AttackWavePattern();
        developerBubble1SquarewaveRightFromBottomLeft.add(new AttackWavePrimitive(0, EnemyType.DEVELOPER_BUBBLE, -0.1, 0.8, squarewaveLargeAmplitudeMovingRight, firingHomingSpread5Circle));
        
        developerBubble1SquarewaveUpFromBottomLeft = new AttackWavePattern();
        developerBubble1SquarewaveUpFromBottomLeft.add(new AttackWavePrimitive(0, EnemyType.DEVELOPER_BUBBLE, 0.25, 1, squarewaveLargeAmplitudeMovingUp, firingHomingSpread5Circle));
        
        developerBubble1SquarewaveDownFromTopRight = new AttackWavePattern();
        developerBubble1SquarewaveDownFromTopRight.add(new AttackWavePrimitive(0, EnemyType.DEVELOPER_BUBBLE, 0.85, -0.1, squarewaveLargeAmplitudeMovingDown, firingHomingSpread5Circle));
        
        developerBubble1SquarewaveRightLeftFromBottomLeft = new AttackWavePattern();
        developerBubble1SquarewaveRightLeftFromBottomLeft.add(new AttackWavePrimitive(0, EnemyType.DEVELOPER_BUBBLE, -0.1, 0.8, squarewaveLargeAmplitudeMovingRightThenLeft, firingHomingSpread5Circle));
        
        
        architectFromRightBottom = new AttackWavePattern();
        architectFromRightBottom.add(new AttackWavePrimitive(0, EnemyType.ARCHITECT, 1, 0.2, sawtoothLargeAmplituteMovingLeft));
        
        
        bossSupervisor = new AttackWavePattern();
        bossSupervisor.add(new AttackWavePrimitive(0, EnemyType.BOSS_SUPERVISOR, 1, 0.1, bossSupervisorMovement, bossSupervisorFiring));
        
        bossTeamLeader = new AttackWavePattern();
        bossTeamLeader.add(new AttackWavePrimitive(0, EnemyType.BOSS_TEAM_LEADER, 1, 0.1, bossTeamLeaderMovement, bossTeamLeaderFiring));
        
        bossNetworksManager = new AttackWavePattern();
        bossNetworksManager.add(new AttackWavePrimitive(0, EnemyType.BOSS_NETWORKS_MANAGER, 1, 0, bossNetworksManagerMovement, bossNetworksManagerFiring));
        
        bossOperationsManager = new AttackWavePattern();
        bossOperationsManager.add(new AttackWavePrimitive(0, EnemyType.BOSS_OPERATIONS_MANAGER, 1, 0.1, bossOperationsManagerMovement, bossOperationsManagerFiring));
        
        bossITExecutive = new AttackWavePattern();
        bossITExecutive.add(new AttackWavePrimitive(0, EnemyType.BOSS_IT_EXECUTIVE, 1, 0.01, bossITExecutiveMovement, bossITExecutiveFiring));
        
        bossCIO = new AttackWavePattern();
        bossCIO.add(new AttackWavePrimitive(0, EnemyType.BOSS_CIO, 1, 0.05, bossCIOMovement, bossCIOFiring));
        
        bossCEO = new AttackWavePattern();
        bossCEO.add(new AttackWavePrimitive(0, EnemyType.BOSS_CEO, 1, 0.2, bossCEOMovement, bossCEOFiring));
        
        bossChairman = new AttackWavePattern();
        bossChairman.add(new AttackWavePrimitive(0, EnemyType.BOSS_CHAIRMAN, 1, 0.3, bossChairmanMovement, bossChairmanFiring));
        bossChairman.add(new AttackWavePrimitive(2000, EnemyType.BOSS_DIRECTOR, 1, 0.11, bossDirectorMovement, bossDirectorFiring));
        bossChairman.add(new AttackWavePrimitive(3200, EnemyType.BOSS_DIRECTOR, 1, 0.11, bossDirectorMovement, bossDirectorFiring));
        bossChairman.add(new AttackWavePrimitive(4400, EnemyType.BOSS_DIRECTOR, 1, 0.11, bossDirectorMovement, bossDirectorFiring));
        
        bossReceiver = new AttackWavePattern();
        bossReceiver.add(new AttackWavePrimitive(0, EnemyType.BOSS_RECEIVER, 1, 0, bossReceiverMovement, null));
        
        levels = new LevelAttackPattern[NUMBER_OF_LEVELS + 1];
        
        levels[0] = new LevelAttackPattern();
        
        levels[1] = new LevelAttackPattern();
        levels[1].description = "Morning briefing";
        levels[1].bossName = "Supervisor";
        
        levels[1].add(LevelAttackPrimitive.create(1000, userEgg5StraightFromRightTop));
        levels[1].add(LevelAttackPrimitive.createWithOffsets(4000, userEgg5StraightFromRightTop, 0, 0.5));
        levels[1].add(LevelAttackPrimitive.create(7000, userEgg5StraightFromRightTop));
        levels[1].add(LevelAttackPrimitive.create(10000, userEgg6StraightSwoopFromRightTop));
        levels[1].add(LevelAttackPrimitive.createWithOffsets(13000, procurementSpeedFromRightTop, 0, 0.08));
        levels[1].add(LevelAttackPrimitive.createWithOffsets(15000, userEgg5StraightFromRightTop, 0, 0.3));
        levels[1].add(LevelAttackPrimitive.create(20000, userEgg6StraightSwoopFromRightTop));
        levels[1].add(LevelAttackPrimitive.create(24000, userEgg2StraightSwoopFromRightTop));
        levels[1].add(LevelAttackPrimitive.createWithOffsets(27000, procurementElevationFromRightTop, 0, 0.1));
        levels[1].add(LevelAttackPrimitive.createWithFiringAndOffsets(28000, userEgg5StraightFromRightTop, firingLeftEarlySingleCircle, 0, 0.2));
        levels[1].add(LevelAttackPrimitive.createWithFiringAndOffsets(36000, userEgg2StraightSwoopFromRightTop, firingLeftLateSingleCircle, 0, 0.25));
        levels[1].add(LevelAttackPrimitive.createWithOffsets(39000, userEgg6StraightSwoopFromRightTop, 0, 0.15));
        levels[1].add(LevelAttackPrimitive.createWithFiring(42000, userEgg4StraightSwoopFromRightTop, firingLeftLateSingleCircle));
        levels[1].add(LevelAttackPrimitive.createWithOffsets(46000, procurementDamageFromRightTop, 0, 0.1));
        levels[1].add(LevelAttackPrimitive.createWithFiringAndOffsets(48000, userEgg5StraightFromRightTop, firingLeftEarlySingleCircle, 0, 0.05));
        levels[1].add(LevelAttackPrimitive.create(53000, developerBird1StopGoDownFromRightTop));
        levels[1].add(LevelAttackPrimitive.createWithFiring(56000, userEgg4StraightSwoopFromRightTop, firingLeftLateSingleCircle));
        levels[1].add(LevelAttackPrimitive.createWithOffsets(60000, procurementBombsFromRightTop, 0, 0.15));
        levels[1].add(LevelAttackPrimitive.createWithOffsets(61000, developerBird1StopGoDownFromRightTop, 0.1, 0.1));
        levels[1].add(LevelAttackPrimitive.createWithFiringAndOffsets(64000, userEgg5StraightFromRightTop, firingLeftEarlySingleCircle, 0, 0.35));
        levels[1].add(LevelAttackPrimitive.create(67000, userEgg2StraightSwoopFromRightTop));
        levels[1].add(LevelAttackPrimitive.createWithFiringAndOffsets(71000, userEgg6StraightSwoopFromRightTop, firingLeftLateSingleCircle, 0, 0.18));
        levels[1].add(LevelAttackPrimitive.createWithFiring(75000, userEgg5StraightFromRightTop, firingLeftSingleCircle));
        levels[1].add(LevelAttackPrimitive.createWithOffsets(77000, procurementSpeedFromRightTop, 0, 0.2));
        levels[1].add(LevelAttackPrimitive.createWithFiringAndOffsets(79000, userEgg4StraightSwoopFromRightTop, firingLeftLateSingleCircle, 0, 0.22));
        levels[1].add(LevelAttackPrimitive.createWithOffsets(82000, userEgg2StraightSwoopFromRightTop, 0, 0.14));
        levels[1].add(LevelAttackPrimitive.createWithFiringAndOffsets(85000, userEgg6StraightSwoopFromRightTop, firingLeftLateSingleCircle, 0, 0.1));
        levels[1].add(LevelAttackPrimitive.createBoss(90000, bossSupervisor));
        
        
        levels[2] = new LevelAttackPattern();
        levels[2].description = "Team meeting";
        levels[2].bossName = "Team Leader";
        
        levels[2].add(LevelAttackPrimitive.create(0, userUfo5LoopSingleFromRightTop));
        levels[2].add(LevelAttackPrimitive.create(3000, userUfo5LoopSingleFromRightMiddle));
        levels[2].add(LevelAttackPrimitive.createWithOffsets(4000, procurementSpeedFromRightTop, 0.03, 0.25));
        levels[2].add(LevelAttackPrimitive.create(6000, userUfo4ArcLeftDownFromRightTop));
        levels[2].add(LevelAttackPrimitive.create(9000, userUfo4ArcDownLeftFromRightTop));
        levels[2].add(LevelAttackPrimitive.createWithOffsets(10500, procurementDamageFromRightTop, 0, 0.19));
        levels[2].add(LevelAttackPrimitive.create(12000, userUfo4ArcUpLeftFromRightBottom));
        levels[2].add(LevelAttackPrimitive.create(15000, userUfo4ArcLeftUpFromRightBottom));
        levels[2].add(LevelAttackPrimitive.createWithFiring(18000, userUfo5LoopVSingleRightFromRightBottom, firingHomingEarlySingleCircle));
        levels[2].add(LevelAttackPrimitive.createWithFiring(21000, userUfo5LoopXSingleRightFromRightBottom, firingHomingEarlySingleCircle));
        levels[2].add(LevelAttackPrimitive.createWithOffsets(24000, procurementSpeedFromRightTop, 0, 0.25));
        levels[2].add(LevelAttackPrimitive.createWithFiring(24000, userUfo4ArcLeftDownFromRightTop, firingHomingEarlySingleCircle));
        levels[2].add(LevelAttackPrimitive.createWithFiring(26000, userUfo4ArcLeftUpFromRightBottom, firingHomingEarlySingleCircle));
        levels[2].add(LevelAttackPrimitive.createWithFiring(28000, userUfo4ArcDownLeftFromRightTop, firingHomingSingleCircle));
        levels[2].add(LevelAttackPrimitive.createWithFiring(30000, userUfo4ArcUpLeftFromRightBottom, firingHomingSingleCircle));
        levels[2].add(LevelAttackPrimitive.createWithFiring(32000, userUfo5LoopXSingleRightFromRightBottom, firingHomingFastSingleCircle));
        levels[2].add(LevelAttackPrimitive.createWithFiring(33500, userUfo5LoopXSingleRightFromRightTop, firingHomingFastSingleCircle));
        levels[2].add(LevelAttackPrimitive.createWithFiringAndOffsets(36000, userUfo5LoopVSingleRightFromRightBottom, firingHomingFastLateSingleCircle, 0, 0.2));
        levels[2].add(LevelAttackPrimitive.createWithFiringAndOffsets(38000, userUfo5LoopVSingleRightFromRightTop, firingHomingFastLateSingleCircle, 0, -0.2));
        levels[2].add(LevelAttackPrimitive.createWithOffsets(41000, procurementInvincibilityFromRightTop, 0, 0.12));
        levels[2].add(LevelAttackPrimitive.createWithFiring(44000, userUfo8ArcAlternateVerticalFromRight, firingHomingEarlySingleCircle));
        levels[2].add(LevelAttackPrimitive.createWithFiring(49000, userUfo8ArcAlternateHorizontalFromRight, firingHomingSingleCircle));
        levels[2].add(LevelAttackPrimitive.create(54000, developerBird1StopGoDownFromRightTop));
        levels[2].add(LevelAttackPrimitive.createWithFiring(57000, userUfo8ArcAlternateHorizontalFromRight, firingHomingSingleCircle));
        levels[2].add(LevelAttackPrimitive.create(62000, developerBird1StopGoDownFromRightTop));
        levels[2].add(LevelAttackPrimitive.createWithFiring(65000, userUfo5LoopSingleFromRightMiddle, firingHomingSingleCircle));
        levels[2].add(LevelAttackPrimitive.createWithOffsets(66000, procurementBombsFromRightTop, 0.03, 0.2));
        levels[2].add(LevelAttackPrimitive.createWithOffsets(67000, developerBird1StopGoDownFromRightTop, 0.12, -0.05));
        levels[2].add(LevelAttackPrimitive.create(67000, developerBird1StopGoUpFromRightMiddle));
        levels[2].add(LevelAttackPrimitive.createWithFiring(70000, userUfo5LoopSingleFromRightTop, firingHomingSingleCircle));
        levels[2].add(LevelAttackPrimitive.createWithOffsets(70000, userUfo5LoopSingleFromRightMiddle, 0, 0.1));
        levels[2].add(LevelAttackPrimitive.createWithOffsets(73000, procurementSpeedFromRightTop, 0.02, 0.11));
        levels[2].add(LevelAttackPrimitive.createWithFiring(74000, userUfo5LoopSingleFromRightTop, firingHomingSingleCircle));
        levels[2].add(LevelAttackPrimitive.createWithFiringAndOffsets(74000, userUfo5LoopSingleFromRightMiddle, firingHomingSingleCircle, 0, 0.1));
        levels[2].add(LevelAttackPrimitive.createWithFiring(79000, userUfo4ArcUpLeftFromRightBottom, firingHomingSingleCircle));
        levels[2].add(LevelAttackPrimitive.createWithFiring(79000, userUfo4ArcDownLeftFromRightTop, firingHomingSingleCircle));
        levels[2].add(LevelAttackPrimitive.createWithOffsets(82000, procurementElevationFromRightTop, 0.02, 0.27));
        levels[2].add(LevelAttackPrimitive.createWithFiringAndOffsets(84000, userUfo5LoopSingleFromRightMiddle, firingHomingSingleCircle, 0, 0.2));
        levels[2].add(LevelAttackPrimitive.createWithOffsets(88000, procurementElevationFromRightTop, 0, 0.11));
        levels[2].add(LevelAttackPrimitive.createWithOffsets(90000, developerBird1StopGoUpFromRightMiddle, 0.1, 0.15));
        levels[2].add(LevelAttackPrimitive.createWithFiring(90000, userUfo4ArcUpLeftFromRightBottom, firingHomingSingleCircle));
        levels[2].add(LevelAttackPrimitive.createBoss(93000, bossTeamLeader));
        
        
        levels[3] = new LevelAttackPattern();
        levels[3].description = "Performance review";
        levels[3].bossName = "Networks Manager";
        
        levels[3].add(LevelAttackPrimitive.createWithOffsets(0, userBurger4Rectangle2CornersFromRightTop, 0, -0.1));
        levels[3].add(LevelAttackPrimitive.createWithOffsets(0, procurementLaserFromRightTop, 0, 0.15));
        levels[3].add(LevelAttackPrimitive.create(1000, userBurger4Rectangle2CornersFromRightMiddle));
        levels[3].add(LevelAttackPrimitive.createWithFiring(4000, userBurger4Rectangle2CornersFromRightTop, firingHomingSingleCircle));
        levels[3].add(LevelAttackPrimitive.createWithFiring(8000, userBurger4Rectangle2CornersFromRightMiddle, firingHomingSingleCircle));
        levels[3].add(LevelAttackPrimitive.createWithFiring(10000, userBurger4Rectangle4CornersFromRightMiddle, firingHomingSingleCircle));
        levels[3].add(LevelAttackPrimitive.createWithFiring(12000, userBurger4Rectangle4CornersFromRightTop, firingHomingSingleCircle));
        levels[3].add(LevelAttackPrimitive.createWithOffsets(15000, procurementDamageFromRightTop, 0, 0.13));
        levels[3].add(LevelAttackPrimitive.createWithFiringAndOffsets(16000, userBurger4Rectangle2CornersFromRightMiddle, firingHomingSingleCircle, 0, 0.2));
        levels[3].add(LevelAttackPrimitive.createWithOffsets(18000, developerBird1StopGoDownFromRightTop, -0.2, -0.1));
        levels[3].add(LevelAttackPrimitive.createWithFiring(20000, userBurger3Staircase1StepDownFromRightTop, firingHomingSingleCircle));
        levels[3].add(LevelAttackPrimitive.createWithFiring(21500, userBurger3Staircase1StepDownFromRightTop, firingHomingSingleCircle));
        levels[3].add(LevelAttackPrimitive.createWithOffsets(23000, procurementElevationFromRightTop, 0, 0.2));
        levels[3].add(LevelAttackPrimitive.createWithFiringAndOffsets(26000, userBurger3Staircase1StepUpFromRightBottom, firingHomingSingleCircle, 0.2, 0));
        levels[3].add(LevelAttackPrimitive.createWithFiringAndOffsets(27500, userBurger3Staircase1StepUpFromRightBottom, firingHomingSingleCircle, 0.2, 0));
        levels[3].add(LevelAttackPrimitive.createWithFiringAndOffsets(29000, userBurger3Staircase1StepUpFromRightBottom, firingHomingSingleCircle, 0.2, 0));
        levels[3].add(LevelAttackPrimitive.createWithOffsets(30000, procurementSpeedFromRightTop, 0, 0.2));
        levels[3].add(LevelAttackPrimitive.createWithFiring(32000, userBurger4Staircase5StepDownFromRightTop, firingHomingSingleCircle));
        levels[3].add(LevelAttackPrimitive.create(34000, developerBird1StopGoDownFromRightTop));
        levels[3].add(LevelAttackPrimitive.createWithOffsets(35000, developerBird1StopGoUpFromRightMiddle, 0, 0.15));
        levels[3].add(LevelAttackPrimitive.createWithFiring(36000, userBurger4Staircase5StepUpFromRightBottom, firingHomingSingleCircle));
        levels[3].add(LevelAttackPrimitive.create(39000, developerBird1StopGoDownFromRightTop));
        levels[3].add(LevelAttackPrimitive.createWithFiringAndOffsets(41000, userBurger4Diamond1CornerFromRightBottom, firingHomingSingleCircle, 0.2, -0.1));
        levels[3].add(LevelAttackPrimitive.createWithOffsets(41000, procurementSpeedFromRightTop, 0, 0.3));
        levels[3].add(LevelAttackPrimitive.createWithFiring(43000, developerPlane1StraightFromRightMiddle, firingStraightSpread3Missile));
        levels[3].add(LevelAttackPrimitive.createWithFiring(45000, userBurger4Diamond1CornerFromRightTop, firingHomingSingleCircle));
        levels[3].add(LevelAttackPrimitive.createWithFiring(47000, userBurger4Diamond1CornerFromRightBottom, firingHomingSingleCircle));
        levels[3].add(LevelAttackPrimitive.createWithFiring(50000, userBurger4Diamond3CornersFromRightBottom, firingHoming2Circles));
        levels[3].add(LevelAttackPrimitive.createWithFiring(55000, userBurger4Diamond3CornersFromRightTop, firingHoming2Circles));
        levels[3].add(LevelAttackPrimitive.createWithOffsets(57000, procurementBombsFromRightTop, 0, 0.23));
        levels[3].add(LevelAttackPrimitive.createWithFiring(58000, developerPlane1StraightFromRightMiddle, firingStraightSpread3Missile));
        levels[3].add(LevelAttackPrimitive.createWithOffsets(61000, developerBird1StopGoDownFromRightTop, 0, 0.2));
        levels[3].add(LevelAttackPrimitive.createWithOffsets(62000, developerBird1StopGoDownFromRightTop, 0.2, -0.05));
        levels[3].add(LevelAttackPrimitive.createWithFiringAndOffsets(64000, userBurger4Rectangle2CornersFromRightMiddle, firingHoming2Circles, 0, 0.2));
        levels[3].add(LevelAttackPrimitive.createWithOffsets(64000, procurementSpeedFromRightTop, 0, 0.28));
        levels[3].add(LevelAttackPrimitive.createWithFiringAndOffsets(67000, userBurger4Rectangle2CornersFromRightTop, firingHoming2Circles, 0, -0.05));
        levels[3].add(LevelAttackPrimitive.createWithFiring(70000, userBurger4Staircase5StepDownFromRightTop, firingHoming2Circles));
        levels[3].add(LevelAttackPrimitive.createWithOffsets(72000, procurementInvincibilityFromRightTop, 0, 0.16));
        levels[3].add(LevelAttackPrimitive.create(72000, developerBird1StopGoDownFromRightTop));
        levels[3].add(LevelAttackPrimitive.createWithOffsets(74000, developerBird1StopGoDownFromRightTop, 0.08, 0.12));
        levels[3].add(LevelAttackPrimitive.createWithFiring(76000, userBurger4Staircase5StepDownFromRightTop, firingHoming2Circles));
        levels[3].add(LevelAttackPrimitive.createWithOffsets(78000, developerBird1StopGoDownFromRightTop, 0.04, 0.22));
        levels[3].add(LevelAttackPrimitive.createWithFiring(80000, userBurger4Staircase5StepUpFromRightBottom, firingHoming2Circles));
        levels[3].add(LevelAttackPrimitive.createWithOffsets(82000, procurementSpeedFromRightTop, 0, 0.11));
        levels[3].add(LevelAttackPrimitive.createWithFiringAndOffsets(82000, developerPlane1StraightFromRightMiddle, firingStraightSpread3Missile, 0.20, -0.28));
        levels[3].add(LevelAttackPrimitive.createWithFiringAndOffsets(84000, developerPlane1StraightFromRightMiddle, firingStraightSpread3Missile, 0.15, -0.15));
        levels[3].add(LevelAttackPrimitive.createWithFiringAndOffsets(86000, developerPlane1StraightFromRightMiddle, firingStraightSpread3Missile, 0.05, 0.1));
        levels[3].add(LevelAttackPrimitive.createWithFiring(88000, userBurger4Diamond3CornersFromRightBottom, firingHoming2Circles));
        levels[3].add(LevelAttackPrimitive.createBoss(90000, bossNetworksManager));

        
        levels[4] = new LevelAttackPattern();
        levels[4].description = "Change management";
        levels[4].bossName = "IT Operations Manager";
        
        levels[4].add(LevelAttackPrimitive.createWithFiringAndOffsets(0, userEgg5StraightFromRightTop, firingLeftSingleEmail, 0, 0.15));
        levels[4].add(LevelAttackPrimitive.createWithOffsets(2000, procurementSpeedFromRightTop, 0, 0.2));
        levels[4].add(LevelAttackPrimitive.createWithFiring(4000, userEgg6StraightSwoopFromRightTop, firingLeftFastLateSingleEmail));
        levels[4].add(LevelAttackPrimitive.createWithFiringAndOffsets(6000, userEgg5StraightFromRightTop, firingLeftSingleEmail, 0, 0.3));
        levels[4].add(LevelAttackPrimitive.createWithFiringAndOffsets(8000, userEgg6StraightSwoopFromRightTop, firingLeftFastLateSingleEmail, 0.2, 0.2));
        levels[4].add(LevelAttackPrimitive.createWithOffsets(10000, procurementDamageFromRightTop, 0, 0.32));
        levels[4].add(LevelAttackPrimitive.createWithOffsets(10000, developerBird1StopGoDownUpFromRightTop, 0.05, -0.05));
        levels[4].add(LevelAttackPrimitive.create(12000, developerBird1StopGoUpFromRightMiddle));
        levels[4].add(LevelAttackPrimitive.createWithFiringAndOffsets(14000, userEgg5StraightFromRightTop, firingLeftSingleEmailHomingSingleCircle, 0, 0.1));
        levels[4].add(LevelAttackPrimitive.create(16000, developerBird1StopGoDownUpFromRightTop));
        levels[4].add(LevelAttackPrimitive.createWithFiringAndOffsets(17000, userEgg6StraightSwoopFromRightTop, firingLeftSingleEmailHomingSingleCircle, 0.1, 0.1));
        levels[4].add(LevelAttackPrimitive.createWithFiringAndOffsets(20000, userEgg5StraightFromRightTop, firingLeftSingleEmailHomingSingleCircle, 0, 0.3));
        levels[4].add(LevelAttackPrimitive.createWithFiringAndOffsets(23000, userEgg5StraightFromRightTop, firingLeftSingleEmailHomingSingleCircle, 0.1, 0.2));
        levels[4].add(LevelAttackPrimitive.createWithFiring(25000, userUfo4ArcDownLeftFromRightTop, firingLeftCircleHomingCircle));
        levels[4].add(LevelAttackPrimitive.createWithFiring(29000, userUfo4ArcUpLeftFromRightBottom, firingLeftCircleHomingCircle));
        levels[4].add(LevelAttackPrimitive.createWithOffsets(31500, procurementInvincibilityFromRightTop, 0, 0.08));
        levels[4].add(LevelAttackPrimitive.createWithFiringAndOffsets(31500, userEgg5StraightFromRightTop, firingLeftSingleEmailHomingSingleCircle, 0, 0.25));
        levels[4].add(LevelAttackPrimitive.createWithFiring(33000, developerPlane1ZigzagFromRightMiddle, firingStraightSpread3Missile));
        levels[4].add(LevelAttackPrimitive.createWithFiring(37000, userUfo5LoopXSingleRightFromRightBottom, firingLeftCircleHomingCircle));
        levels[4].add(LevelAttackPrimitive.createWithFiring(39000, userUfo5LoopXSingleRightFromRightTop, firingLeftCircleHomingCircle));
        levels[4].add(LevelAttackPrimitive.createWithFiring(41000, developerBubble1StraightFromRightTop, firingLeftSpread5Circle));
        levels[4].add(LevelAttackPrimitive.createWithFiring(42000, userEgg6StraightSwoopFromRightTop, firingLeftFastLateSingleEmail));
        levels[4].add(LevelAttackPrimitive.createWithOffsets(44000, procurementDamageFromRightTop, 0, 0.15));
        levels[4].add(LevelAttackPrimitive.createWithFiring(46000, userEgg3StraightFromLeftBottom, firingHomingLightCircles));
        levels[4].add(LevelAttackPrimitive.createWithFiring(50000, userEgg5StraightFromRightTop, firingLeftSingleEmail));
        levels[4].add(LevelAttackPrimitive.createWithFiring(51000, userEgg3StraightFromLeftBottom, firingHomingLightCircles));
        levels[4].add(LevelAttackPrimitive.createWithFiring(55000, developerBubble1SquarewaveFromRightMiddle, firingLeftSpread5Circle));
        levels[4].add(LevelAttackPrimitive.createWithFiring(58000, userEgg6StraightSwoopFromRightTop, firingLeftFastLateSingleEmail));
        levels[4].add(LevelAttackPrimitive.createWithOffsets(61000, developerBird1StopGoDownUpFromRightTop, 0, 0.15));
        levels[4].add(LevelAttackPrimitive.createWithFiring(63000, userUfo5LoopVSingleRightFromRightTop, firingHomingLightCircles));
        levels[4].add(LevelAttackPrimitive.createWithOffsets(64000, developerBird1StopGoDownUpFromRightTop, 0, 0.05));
        levels[4].add(LevelAttackPrimitive.createWithOffsets(65000, procurementSpeedFromRightTop, 0, 0.15));
        levels[4].add(LevelAttackPrimitive.createWithOffsets(66000, developerBird1StopGoUpFromRightMiddle, 0, 0.15));
        levels[4].add(LevelAttackPrimitive.createWithFiring(68000, userUfo5LoopVSingleRightFromRightBottom, firingHomingLightCircles));
        levels[4].add(LevelAttackPrimitive.createWithFiring(71000, userUfo4ArcDownLeftFromRightTop, firingHomingLightCircles));
        levels[4].add(LevelAttackPrimitive.createWithFiringAndOffsets(73000, developerPlane1ZigzagFromRightMiddle, firingStraightSpread3Missile, 0, -0.15));
        levels[4].add(LevelAttackPrimitive.createWithFiring(77000, userUfo4ArcUpLeftFromRightBottom, firingHomingLightCircles));
        levels[4].add(LevelAttackPrimitive.createWithFiring(79000, userEgg3StraightFromLeftBottom, firingHomingLightCircles));
        levels[4].add(LevelAttackPrimitive.createWithOffsets(79000, procurementLaserFromRightTop, 0, 0.18));
        levels[4].add(LevelAttackPrimitive.createWithFiringAndOffsets(80000, developerPlane1StraightFromRightMiddle, firingStraightSpread3Missile, 0, 0.1));
        levels[4].add(LevelAttackPrimitive.createWithFiringAndOffsets(83000, developerBubble1StraightFromLeftBottom, firingLeftSpread5Circle, 0, -0.1));
        levels[4].add(LevelAttackPrimitive.createWithFiring(86000, userUfo5LoopXSingleRightFromRightTop, firingLeftSingleCircle));
        levels[4].add(LevelAttackPrimitive.createWithFiring(89000, userUfo5LoopXSingleRightFromRightBottom, firingLeftSingleCircle));
        levels[4].add(LevelAttackPrimitive.createWithOffsets(79000, procurementBombsFromRightTop, 0, 0.05));
        levels[4].add(LevelAttackPrimitive.createBoss(92000, bossOperationsManager));
        
        
        levels[5] = new LevelAttackPattern();
        levels[5].description = "Strategy workshop";
        levels[5].bossName = "GM IT Infrastructure";
        
        levels[5].add(LevelAttackPrimitive.createWithFiring(0, userEgg5StraightFromRightTop, firingHomingLightCircleLeftLightEmail));
        levels[5].add(LevelAttackPrimitive.createWithFiring(5000, userEgg3StraightLeftDownFromRightTop, firingHomingLightCircleLeftLightEmail));
        levels[5].add(LevelAttackPrimitive.createWithFiring(8000, userEgg3StraightLeftUpFromRightBottom, firingHomingLightCircleLeftLightEmail));
        levels[5].add(LevelAttackPrimitive.createWithFiring(11000, userEgg3StraightDownFromTopRight, firingHomingLightCircleLeftLightEmail));
        levels[5].add(LevelAttackPrimitive.createWithFiringAndOffsets(13000, userEgg5StraightFromRightTop, firingHomingLightCircleLeftLightEmail, 0, 0.15));
        levels[5].add(LevelAttackPrimitive.createWithFiring(15000, userEgg3StraightRightUpFromBottomLeft, firingHomingLightCircleLeftLightEmail));
        levels[5].add(LevelAttackPrimitive.createWithOffsets(17000, procurementSpeedFromRightTop, 0, 0.2));
        levels[5].add(LevelAttackPrimitive.createWithOffsets(19000, procurementDamageFromRightTop, 0, 0.16));
        levels[5].add(LevelAttackPrimitive.createWithOffsets(21000, procurementInvincibilityFromRightTop, 0, 0.12));
        levels[5].add(LevelAttackPrimitive.createWithOffsets(23000, procurementLaserFromRightTop, 0, 0.08));
        levels[5].add(LevelAttackPrimitive.createWithOffsets(25000, procurementElevationFromRightTop, 0, 0.1));
        levels[5].add(LevelAttackPrimitive.createWithFiringAndOffsets(28000, developerBird1StraightFromRightTop, firingLeft4Missiles, 0, 0.3));
        levels[5].add(LevelAttackPrimitive.createWithFiringAndOffsets(30000, developerBird1StopGoDownFromRightTop, firingLeft4Missiles, 0, 0.1));
        levels[5].add(LevelAttackPrimitive.createWithFiringAndOffsets(31000, developerBird1StopGoUpFromRightMiddle, firingLeft4Missiles, 0, -0.1));
        levels[5].add(LevelAttackPrimitive.createWithFiringAndOffsets(34000, userEgg3StraightDownFromTopRight, firingHomingLightCircleLeftLightEmail, 0.1, 0));
        levels[5].add(LevelAttackPrimitive.createWithFiring(36000, userEgg3StraightUpFromBottomRight, firingHomingLightCircleLeftLightEmail));
        levels[5].add(LevelAttackPrimitive.createWithOffsets(38000, procurementBombsFromRightTop, 0, 0.1));
        levels[5].add(LevelAttackPrimitive.createWithFiringAndOffsets(40000, userEgg3StraightUpFromBottomRight, firingHomingLightCircleLeftLightEmail, -0.15, 0));
        levels[5].add(LevelAttackPrimitive.createWithFiring(45000, architectFromRightBottom, firingRotaryCirclesLeftUpLightPhones));
        levels[5].add(LevelAttackPrimitive.createWithFiring(52000, userEgg6StraightSwoopFromRightTop, firingHomingLightCircleLeftLightEmail));
        levels[5].add(LevelAttackPrimitive.createWithFiringAndOffsets(56000, developerBird1StopGoDownFromRightTop, firingLeftSlow4Missiles, 0.1, -0.15));
        levels[5].add(LevelAttackPrimitive.createWithFiringAndOffsets(57000, developerBird1StopGoUpFromRightMiddle, firingLeftSlow4Missiles, 0.2, 0.2));
        levels[5].add(LevelAttackPrimitive.createWithFiringAndOffsets(58000, userEgg3StraightUpFromBottomRight, firingHomingLightCircleLeftLightEmail, 0.1, 0));
        levels[5].add(LevelAttackPrimitive.createWithFiringAndOffsets(59000, developerBird1StraightFromRightTop, firingLeftSlow4Missiles, 0, 0.3));
        levels[5].add(LevelAttackPrimitive.createWithFiring(65000, userBurger3Staircase1StepDownFromRightTop, firingHomingLightCircleLeftEarlySinglePhone));
        levels[5].add(LevelAttackPrimitive.createWithOffsets(68000, procurementDamageFromRightTop, 0, 0.15));
        levels[5].add(LevelAttackPrimitive.createWithFiring(70000, userBurger4Rectangle2CornersFromRightTop, firingHomingLightCircleLeftSinglePhone));
        levels[5].add(LevelAttackPrimitive.createWithFiring(74000, userBurger4Diamond3CornersFromRightBottom, firingHomingLightCircleLeftLateSinglePhone));
        levels[5].add(LevelAttackPrimitive.createWithFiring(77000, developerBubble1StraightFromLeftBottom, firingLeftSpread5Circle));
        levels[5].add(LevelAttackPrimitive.createWithOffsets(79000, procurementLaserFromRightTop, 0, 0.1));
        levels[5].add(LevelAttackPrimitive.createWithFiring(81000, userBurger4Staircase5StepDownFromRightTop, firingHomingLightCircleLeftLateSinglePhone));
        levels[5].add(LevelAttackPrimitive.createWithFiringAndOffsets(83000, developerBird1StopGoDownFromRightTop, firingLeftSlow4Missiles, 0.15, -0.15));
        levels[5].add(LevelAttackPrimitive.createWithFiringAndOffsets(84000, developerBird1StopGoUpFromRightMiddle, firingLeftSlow4Missiles, 0.05, 0.2));
        levels[5].add(LevelAttackPrimitive.createWithFiringAndOffsets(87000, userBurger4Rectangle4CornersFromRightMiddle, firingHomingLightCircleLeftSinglePhone, 0.02, 0.05));
        levels[5].add(LevelAttackPrimitive.createBoss(90000, bossITExecutive));
        
        
        levels[6] = new LevelAttackPattern();
        levels[6].description = "Budget round";
        levels[6].bossName = "Chief Information Officer";
        levels[6].add(LevelAttackPrimitive.createWithFiring(0, userUfo5LoopSingleFromRightMiddle, firingHomingLightCircles));
        levels[6].add(LevelAttackPrimitive.createWithFiring(3000, userUfo5LoopSingleFromRightTop, firingHomingLightCircles));
        levels[6].add(LevelAttackPrimitive.createWithOffsets(6000, procurementSpeedFromRightTop, 0, 0.1));
        levels[6].add(LevelAttackPrimitive.createWithOffsets(6500, procurementSpeedFromRightTop, 0.1, 0.1));
        levels[6].add(LevelAttackPrimitive.createWithFiring(8000, developerPlane5CornersFromRightTop, firingStraightSpread3Missile));
        levels[6].add(LevelAttackPrimitive.createWithFiring(11000, developerPlane6CornersFromRightTop, firingStraightSpread3Missile));
        levels[6].add(LevelAttackPrimitive.createWithFiringAndOffsets(13000, developerPlane5CornersFromRightTop, firingStraightSpread3Missile, 0.04, 0.05));
        levels[6].add(LevelAttackPrimitive.createWithFiring(16000, userUfo8ArcAlternateHorizontalFromRight, firingHomingLightCircles));
        levels[6].add(LevelAttackPrimitive.createWithFiring(20000, userUfo5LoopXSingleRightFromRightBottom, firingHomingLightCircles));
        levels[6].add(LevelAttackPrimitive.createWithFiring(22000, developerBubble1SquarewaveDownFromTopRight, firingLeftSpread5Circle));
        levels[6].add(LevelAttackPrimitive.createWithOffsets(25000, procurementDamageFromRightTop, 0, 0.2));
        levels[6].add(LevelAttackPrimitive.createWithOffsets(25500, procurementDamageFromRightTop, 0.1, 0.2));
        levels[6].add(LevelAttackPrimitive.createWithFiringAndOffsets(27000, developerPlane5CornersFromRightTop, firingStraightSpread3Missile, 0.05, 0.1));
        levels[6].add(LevelAttackPrimitive.createWithFiringAndOffsets(29000, developerPlane6CornersFromRightTop, firingStraightSpread3Missile, 0.02, -0.08));
        levels[6].add(LevelAttackPrimitive.createWithFiringAndOffsets(31000, developerPlane3CornersFromRightTop, firingStraightSpread3Missile, 0.02, -0.08));
        levels[6].add(LevelAttackPrimitive.createWithFiring(34000, userUfo5LoopXSingleRightFromRightBottom, firingHomingLightCircles));
        levels[6].add(LevelAttackPrimitive.createWithFiring(36000, userUfo4ArcLeftDownFromRightTop, firingHomingLightCircles));
        levels[6].add(LevelAttackPrimitive.createWithFiring(38000, userUfo4ArcUpLeftFromRightBottom, firingHomingLightCircles));
        levels[6].add(LevelAttackPrimitive.createWithFiringAndOffsets(40000, developerBird1StopGoDownFromRightTop, firingLeftSlow4Missiles, 0.05, -0.05));
        levels[6].add(LevelAttackPrimitive.createWithFiringAndOffsets(40000, developerBird1StopGoUpFromRightMiddle, firingLeftSlow4Missiles, 0.1, 0.2));
        levels[6].add(LevelAttackPrimitive.createWithFiringAndOffsets(42000, developerPlane3CornersFromRightTop, firingStraightSpread3Missile, 0.02, -0.08));
        levels[6].add(LevelAttackPrimitive.createWithFiring(45000, userUfo5LoopVSingleRightFromRightTop, firingHomingLightCircles));
        levels[6].add(LevelAttackPrimitive.createWithFiring(47000, userUfo4ArcUpLeftFromRightBottom, firingHomingLightCircles));
        levels[6].add(LevelAttackPrimitive.createWithFiring(50000, architectFromRightBottom, firingRotaryCirclesLeftUpLightPhones));
        levels[6].add(LevelAttackPrimitive.createWithFiring(53000, userUfo5LoopSingleFromRightMiddle, firingHomingLightCircles));
        levels[6].add(LevelAttackPrimitive.createWithFiring(56000, userUfo5LoopVSingleRightFromRightTop, firingHomingLightCircles));
        levels[6].add(LevelAttackPrimitive.createWithFiringAndOffsets(58000, developerPlane6CornersFromRightTop, firingStraightSpread3Missile, 0, 0.05));
        levels[6].add(LevelAttackPrimitive.createWithFiringAndOffsets(60000, developerPlane3CornersFromRightTop, firingStraightSpread3Missile, 0.02, 0.25));
        levels[6].add(LevelAttackPrimitive.createWithOffsets(61000, procurementElevationFromRightTop, 0, 0.2));
        levels[6].add(LevelAttackPrimitive.createWithOffsets(61500, procurementElevationFromRightTop, 0.1, 0.2));
        levels[6].add(LevelAttackPrimitive.createWithFiring(64000, userUfo4ArcUpLeftFromRightBottom, firingHomingLightCircles));
        levels[6].add(LevelAttackPrimitive.createWithFiringAndOffsets(65000, developerPlane5CornersFromRightTop, firingStraightSpread3Missile, 0.1, 0.1));
        levels[6].add(LevelAttackPrimitive.createWithFiring(67000, userUfo5LoopXSingleRightFromRightBottom, firingHomingLightCircles));
        levels[6].add(LevelAttackPrimitive.createWithFiringAndOffsets(69000, developerPlane5CornersFromRightTop, firingStraightSpread3Missile, 0.03, 0.4));
        levels[6].add(LevelAttackPrimitive.createWithFiringAndOffsets(71000, developerPlane6CornersFromRightTop, firingStraightSpread3Missile, 0, 0.15));
        levels[6].add(LevelAttackPrimitive.createWithFiring(72000, userUfo4ArcDownLeftFromRightTop, firingHomingLightCircles));
        levels[6].add(LevelAttackPrimitive.createWithOffsets(73000, procurementLaserFromRightTop, 0, 0.1));
        levels[6].add(LevelAttackPrimitive.createWithFiring(74000, userUfo4ArcUpLeftFromRightBottom, firingHomingLightCircles));
        levels[6].add(LevelAttackPrimitive.createWithFiring(76000, userUfo4ArcLeftDownFromRightTop, firingHomingLightCircles));
        levels[6].add(LevelAttackPrimitive.createWithFiringAndOffsets(77000, developerPlane3CornersFromRightTop, firingStraightSpread3Missile, 0, -0.1));
        levels[6].add(LevelAttackPrimitive.createWithFiring(78000, userUfo4ArcLeftUpFromRightBottom, firingHomingLightCircles));
        levels[6].add(LevelAttackPrimitive.createWithOffsets(79000, procurementInvincibilityFromRightTop, 0, 0.1));
        levels[6].add(LevelAttackPrimitive.createWithOffsets(79500, procurementInvincibilityFromRightTop, 0.1, 0.1));
        levels[6].add(LevelAttackPrimitive.createWithFiringAndOffsets(82000, developerPlane3CornersFromRightTop, firingStraightSpread3Missile, 0.1, -0.18));
        levels[6].add(LevelAttackPrimitive.createWithFiringAndOffsets(82500, developerPlane3CornersFromRightTop, firingStraightSpread3Missile, 0.05, 0.04));
        levels[6].add(LevelAttackPrimitive.createWithFiringAndOffsets(83000, developerPlane3CornersFromRightTop, firingStraightSpread3Missile, 0, 0.28));
        levels[6].add(LevelAttackPrimitive.createWithFiring(85000, userUfo5LoopVSingleRightFromRightTop, firingHomingLightCircles));
        levels[6].add(LevelAttackPrimitive.createWithFiring(86000, userUfo5LoopVSingleRightFromRightBottom, firingHomingLightCircles));
        levels[6].add(LevelAttackPrimitive.createWithFiring(88000, userUfo5LoopXSingleRightFromRightTop, firingHomingLightCircles));
        levels[6].add(LevelAttackPrimitive.createWithFiring(89000, userUfo5LoopXSingleRightFromRightBottom, firingHomingLightCircles));
        levels[6].add(LevelAttackPrimitive.createBoss(91000, bossCIO));
        
        
        levels[7] = new LevelAttackPattern();
        levels[7].description = "Shareholder meeting";
        levels[7].bossName = "Chief Executive Officer";
        
        levels[7].add(LevelAttackPrimitive.createWithOffsets(0, procurementLaserFromRightTop, 0.05, 0.08));
        levels[7].add(LevelAttackPrimitive.createWithFiringAndOffsets(3000, userEgg6StraightSwoopFromRightTop, firingHomingLightCircles, 0, 0.05));
        levels[7].add(LevelAttackPrimitive.createWithFiring(6000, developerBubble1SquarewaveFromRightMiddle, firingLeftSpread5Circle));
        levels[7].add(LevelAttackPrimitive.createWithFiringAndOffsets(9000, userEgg5StraightFromRightTop, firingHomingCircles, 0.02, 0.02));
        levels[7].add(LevelAttackPrimitive.createWithOffsets(10000, procurementElevationFromRightTop, 0, 0.3));
        levels[7].add(LevelAttackPrimitive.createWithFiringAndOffsets(14000, userEgg6StraightSwoopFromRightTop, firingHomingLightCircles, 0, 0.02));
        levels[7].add(LevelAttackPrimitive.createWithOffsets(19000, procurementLaserFromRightTop, 0.05, 0.08));
        levels[7].add(LevelAttackPrimitive.createWithFiring(22000, userEgg3StraightLeftUpFromRightBottom, firingHomingCircles));
        levels[7].add(LevelAttackPrimitive.createWithFiring(25000, userEgg3StraightRightUpFromBottomLeft, firingHomingLightCircles));
        levels[7].add(LevelAttackPrimitive.createWithFiring(27000, developerBubble1SquarewaveFromRightMiddle, firingLeftSpread5Circle));
        levels[7].add(LevelAttackPrimitive.createWithOffsets(29000, procurementDamageFromRightTop, 0.11, 0.03));
        
        levels[7].add(LevelAttackPrimitive.createWithFiring(31000, userEgg3StraightUpFromBottomRight, firingHomingCircles));
        levels[7].add(LevelAttackPrimitive.createWithFiring(34000, developerBubble1SquarewaveRightFromBottomLeft, firingHomingSpread5Circle));
        levels[7].add(LevelAttackPrimitive.createWithFiring(36000, userUfo4ArcUpLeftFromRightBottom, firingHomingSingleCircle));
        levels[7].add(LevelAttackPrimitive.createWithFiringAndOffsets(39000, userUfo5LoopSingleFromRightMiddle, firingHomingSingleCircle, 0.1, -0.1));
        levels[7].add(LevelAttackPrimitive.createWithFiringAndOffsets(42000, userUfo4ArcLeftUpFromRightBottom, firingHomingCircles, 0.1, -0.1));
        levels[7].add(LevelAttackPrimitive.createWithOffsets(42000, procurementBombsFromRightTop, 0.03, 0.12));
        levels[7].add(LevelAttackPrimitive.createWithFiringAndOffsets(45000, userUfo5LoopSingleFromRightTop, firingHomingCircles, 0.1, 0.1));
        levels[7].add(LevelAttackPrimitive.createWithFiringAndOffsets(48000, developerBubble1SquarewaveUpFromBottomLeft, firingHomingSpread5Circle, 0.5, 0));
        levels[7].add(LevelAttackPrimitive.createWithFiringAndOffsets(51000, userUfo4ArcDownLeftFromRightTop, firingHomingCircles, 0.1, -0.1));
        levels[7].add(LevelAttackPrimitive.createWithOffsets(54000, procurementSpeedFromRightTop, 0, 0.2));
        levels[7].add(LevelAttackPrimitive.createWithFiring(54000, userUfo5LoopXSingleRightFromRightBottom, firingHomingCircles));
        levels[7].add(LevelAttackPrimitive.createWithFiring(57000, userUfo5LoopXSingleRightFromRightTop, firingHomingCircles));
        levels[7].add(LevelAttackPrimitive.createWithFiringAndOffsets(60000, developerBubble1SquarewaveUpFromBottomLeft, firingHomingSpread5Circle, 0.5, 0));
        levels[7].add(LevelAttackPrimitive.createWithFiring(61000, developerBird1StopGoDownUpFromRightTop, firingLeftLightMissiles));
        levels[7].add(LevelAttackPrimitive.createWithFiring(62000, developerBird1StopGoUpFromRightMiddle, firingLeftLightMissiles));
        levels[7].add(LevelAttackPrimitive.createWithOffsets(64000, procurementDamageFromRightTop, 0.09, 0.22));
        levels[7].add(LevelAttackPrimitive.createWithFiring(68000, userBurger4Diamond1CornerFromRightTop, firingHomingCircles));
        levels[7].add(LevelAttackPrimitive.createWithFiring(71000, userBurger4Diamond1CornerFromRightTop, firingHomingLightCircleLeftDownLateSinglePhone));
        levels[7].add(LevelAttackPrimitive.createWithFiring(74000, developerBubble1SquarewaveRightLeftFromBottomLeft, firingHomingSpread5Circle));
        levels[7].add(LevelAttackPrimitive.createWithOffsets(75000, procurementBombsFromRightTop, 0.12, 0.05));
        levels[7].add(LevelAttackPrimitive.createWithFiring(78000, userBurger4Rectangle2CornersFromRightMiddle, firingHomingLightCircleLeftSinglePhone));
        levels[7].add(LevelAttackPrimitive.createWithOffsets(81000, procurementInvincibilityFromRightTop, 0.04, 0.15));
        levels[7].add(LevelAttackPrimitive.createWithFiring(83000, developerBubble1StraightFromLeftBottom, firingHomingSpread5Circle));
        levels[7].add(LevelAttackPrimitive.createWithFiring(85500, developerBubble1SquarewaveFromRightMiddle, firingHomingSpread5Circle));
        levels[7].add(LevelAttackPrimitive.createWithFiringAndOffsets(87250, developerBubble1SquarewaveUpFromBottomLeft, firingHomingSpread5Circle, 0.5, 0));
        levels[7].add(LevelAttackPrimitive.createWithFiring(88750, developerBubble1SquarewaveDownFromTopRight, firingHomingSpread5Circle));
        levels[7].add(LevelAttackPrimitive.createBoss(97000, bossCEO));
        

        levels[8] = new LevelAttackPattern();
        levels[8].description = "Press conference";
        levels[8].bossName = "Chairman of the Board";
        
        levels[8].add(LevelAttackPrimitive.createWithOffsets(0, procurementElevationFromRightTop, 0.04, 0.35));
        levels[8].add(LevelAttackPrimitive.createWithOffsets(1000, procurementElevationFromRightTop, 0.04, 0.35));
        levels[8].add(LevelAttackPrimitive.createWithFiringAndOffsets(1000, architectFromRightBottom, firingRotaryCirclesLeftUpLightPhones, 0.03, 0.03));
        levels[8].add(LevelAttackPrimitive.createWithOffsets(3000, procurementBombsFromRightTop, 0.07, 0.17));
        levels[8].add(LevelAttackPrimitive.createWithOffsets(7000, procurementDamageFromRightTop, 0, 0.2));
        levels[8].add(LevelAttackPrimitive.createWithFiringAndOffsets(9000, developerBird1StopGoDownFromRightTop, firingLeft4Missiles, 0, 0.07));
        levels[8].add(LevelAttackPrimitive.createWithFiringAndOffsets(9000, developerBird1StopGoUpFromRightMiddle, firingLeft4Missiles, 0, 0.25));
        levels[8].add(LevelAttackPrimitive.createWithOffsets(12000, procurementSpeedFromRightTop, 0, 0.4));
        levels[8].add(LevelAttackPrimitive.createWithOffsets(13000, procurementSpeedFromRightTop, 0, 0.4));
        levels[8].add(LevelAttackPrimitive.createWithFiringAndOffsets(17000, architectFromRightBottom, firingRotaryCirclesLeftUpLightPhones, 0, 0.07));
        levels[8].add(LevelAttackPrimitive.createWithOffsets(21000, procurementLaserFromRightTop, 0.07, 0.07));
        levels[8].add(LevelAttackPrimitive.createWithOffsets(22000, procurementDamageFromRightTop, 0, 0.2));
        levels[8].add(LevelAttackPrimitive.createWithFiringAndOffsets(25000, developerBird1StopGoDownFromRightTop, firingLeft4Missiles, 0, 0));
        levels[8].add(LevelAttackPrimitive.createWithFiringAndOffsets(25000, developerBird1StopGoUpFromRightMiddle, firingLeft4Missiles, 0, 0.32));
        levels[8].add(LevelAttackPrimitive.createWithOffsets(28000, procurementInvincibilityFromRightTop, 0.18, 0.14));
        levels[8].add(LevelAttackPrimitive.createWithOffsets(29000, procurementInvincibilityFromRightTop, 0, 0.14));
        levels[8].add(LevelAttackPrimitive.createWithFiring(31000, developerBird1StopGoDownUpFromRightTop, firingLeftLightMissiles));
        levels[8].add(LevelAttackPrimitive.createWithFiring(32000, developerBird1StopGoUpFromRightMiddle, firingLeftLightMissiles));
        levels[8].add(LevelAttackPrimitive.createWithFiring(35000, userBurger4Staircase5StepUpFromLeftBottom, firingHomingSingleCircleLeftSingleChair));
        levels[8].add(LevelAttackPrimitive.createWithFiringAndOffsets(38000, developerPlane3CornersFromRightTop, firingStraightSpread3Missile, 0.1, -0.18));
        levels[8].add(LevelAttackPrimitive.createWithFiringAndOffsets(38500, developerPlane3CornersFromRightTop, firingStraightSpread3Missile, 0.05, 0.04));
        levels[8].add(LevelAttackPrimitive.createWithFiringAndOffsets(39000, developerPlane3CornersFromRightTop, firingStraightSpread3Missile, 0, 0.28));
        levels[8].add(LevelAttackPrimitive.createWithFiringAndOffsets(42000, userBurger4Rectangle2CornersFromRightMiddle, firingHomingSingleCircleLeftSingleChair, 0.02, 0.05));
        levels[8].add(LevelAttackPrimitive.createWithFiringAndOffsets(45000, userEgg3StraightUpFromBottomRight, firingSingleChairSingleEmailSinglePhone, 0.05, 0));
        levels[8].add(LevelAttackPrimitive.createWithFiringAndOffsets(48000, architectFromRightBottom, firingRotaryCirclesLeftUpLightPhones, 0, 0.1));
        levels[8].add(LevelAttackPrimitive.createWithFiringAndOffsets(51000, userBurger4Rectangle2CornersFromRightTop, firingHomingSingleCircleLeftSingleChair, 0, -0.05));
        levels[8].add(LevelAttackPrimitive.createWithOffsets(54000, procurementDamageFromRightTop, 0, 0.2));
        levels[8].add(LevelAttackPrimitive.createWithOffsets(56000, procurementSpeedFromRightTop, 0, 0.4));
        levels[8].add(LevelAttackPrimitive.createWithFiring(58000, architectFromRightBottom, firingRotaryCirclesLeftUpLightChairs));
        levels[8].add(LevelAttackPrimitive.createWithFiring(60000, architectFromRightBottom, firingRotaryCirclesLeftUpLightChairs));
        levels[8].add(LevelAttackPrimitive.createWithFiring(61000, userUfo3ParabolaFromRightBottom, firingLeftHeavyEmail));
        levels[8].add(LevelAttackPrimitive.createWithFiring(64000, userUfo3ParabolaFromRightTop, firingLeftHeavyEmail));
        levels[8].add(LevelAttackPrimitive.createWithOffsets(67000, procurementInvincibilityFromRightTop, 0, 0.1));
        levels[8].add(LevelAttackPrimitive.createWithFiringAndOffsets(70000, developerBird1StopGoUpFromRightMiddle, firingLeftLightMissiles, 0, 0.1));
        levels[8].add(LevelAttackPrimitive.createWithFiringAndOffsets(71000, developerBird1StopGoDownFromRightTop, firingLeftLightMissiles, 0, -0.1));
        levels[8].add(LevelAttackPrimitive.createWithFiringAndOffsets(74000, developerBubble1SquarewaveUpFromBottomLeft, firingHomingSpread5Circle, 0.2, 0));
        levels[8].add(LevelAttackPrimitive.createWithFiring(75000, userUfo3ParabolaFromRightTop, firingLeftHeavyEmail));
        levels[8].add(LevelAttackPrimitive.createWithFiring(78000, userUfo3ParabolaFromRightBottom, firingLeftHeavyEmail));
        levels[8].add(LevelAttackPrimitive.createWithOffsets(79000, procurementLaserFromRightTop, 0, 0.1));
        levels[8].add(LevelAttackPrimitive.createWithFiring(81000, userBurger4Staircase5StepUpFromLeftBottom, firingHomingSingleCircleLeftSingleChair));
        levels[8].add(LevelAttackPrimitive.createWithFiringAndOffsets(84000, userEgg3StraightLeftDownFromRightTop, firingSingleChairSingleEmailSinglePhone, 0, 0.1));
        levels[8].add(LevelAttackPrimitive.createWithOffsets(86000, procurementSpeedFromRightTop, 0, 0.3));
        levels[8].add(LevelAttackPrimitive.createWithOffsets(87000, procurementDamageFromRightTop, 0, 0.3));
        levels[8].add(LevelAttackPrimitive.createBoss(91000, bossChairman));
        
        
        levels[9] = new LevelAttackPattern();
        levels[9].description = "Liquidation";
        levels[9].bossName = "Receiver";
        
        levels[9].add(LevelAttackPrimitive.createBoss(0, bossReceiver));
    }
    
    
    /** Creates a new instance of GameAttackPattern */
    private LevelAttackPattern() {
    }
    
    public String getDescription() {
        return description;
    }
    
    public String getBossName() {
        return bossName;
    }
}
