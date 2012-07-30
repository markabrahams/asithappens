/*
 * ControlImage.java
 *
 * Created on 25 April 2008, 04:18
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

/**
 *
 * @author mark
 */
public class ControlImage extends ControlPrimitive {
    
    public GameImage gameImage;
    
    /** Creates a new instance of ControlImage */
    public ControlImage(int timeOffset, GameImage gameImage) {
        super(timeOffset);
        this.gameImage = gameImage;
    }
    
    public static ControlImage create(int timeOffset, GameImage gameImage) {
        return new ControlImage(timeOffset, gameImage);
    }
}
