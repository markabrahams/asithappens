/*
 * AttackWavePattern.java
 *
 * Created on 10 January 2008, 20:16
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
public class AttackWavePattern extends Vector<AttackWavePrimitive> {
    
    /*
    public static AttackWavePattern trMmBr5Users;
    
    public static AttackWavePattern mid3Users;
    
    static {
        trMmBr5Users = new AttackWavePattern();
        trMmBr5Users.add(new AttackWavePrimitive(0, Enemy.TYPE_USER, 1, 0, MovementPattern.userDiamond));
        trMmBr5Users.add(new AttackWavePrimitive(1000, Enemy.TYPE_USER, 1, 0.2, MovementPattern.userDiamond));
        trMmBr5Users.add(new AttackWavePrimitive(2000, Enemy.TYPE_USER, 1, 0.4, MovementPattern.userDiamond));
        trMmBr5Users.add(new AttackWavePrimitive(3000, Enemy.TYPE_USER, 1, 0.6, MovementPattern.userDiamond));
        trMmBr5Users.add(new AttackWavePrimitive(4000, Enemy.TYPE_USER, 1, 0.8, MovementPattern.userDiamond));
        
        mid3Users = new AttackWavePattern();
        mid3Users.add(new AttackWavePrimitive(0, Enemy.TYPE_USER, 1, 1, MovementPattern.userRightAcceleration));
        mid3Users.add(new AttackWavePrimitive(100, Enemy.TYPE_USER, 1, 0.8, MovementPattern.userRightAcceleration));
        mid3Users.add(new AttackWavePrimitive(200, Enemy.TYPE_USER, 1, 0.6, MovementPattern.userRightAcceleration));
    }
    */
    
    /** Creates a new instance of AttackWavePattern */
    public AttackWavePattern() {
    }
    
}
