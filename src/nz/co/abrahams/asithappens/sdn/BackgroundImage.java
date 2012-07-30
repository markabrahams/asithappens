/*
 * BackgroundImage.java
 *
 * Created on 20 April 2008, 00:03
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
import java.awt.Image;

/**
 *
 * @author mark
 */
public class BackgroundImage extends SpatialObject {
    
    //public GameImage gameImage;
    
    /** Creates a new instance of BackgroundImage */
    public BackgroundImage(GameContext gameContext, TimeSeriesContext graphContext,
            GameImage gameImage, double initialX, double initialY, boolean subjectToGravity,
            ControlPattern movementPattern) {
        super(gameContext, graphContext, gameImage, initialX, initialY,
                0, 0, Double.NaN, Double.NaN,
                subjectToGravity, 1, 1, 1, 1, movementPattern);
        //this.gameImage = gameImage;
    }
    
    /*
    public Image getImage() {
        return gameImage.getImage();
    }
     */
    
}
