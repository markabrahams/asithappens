/*
 * KeyHandlerLauncher.java
 *
 * Created on 23 December 2007, 16:49
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

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 *
 * @author mark
 */
public class KeyHandlerLauncher extends KeyAdapter {
    
    protected SDNContainerPanel containerPanel;
    
    /** Creates a new instance of KeyHandlerLauncher */
    public KeyHandlerLauncher(SDNContainerPanel containerPanel) {
        this.containerPanel = containerPanel;
    }
    
    public void keyTyped(KeyEvent evt) {
        if ( evt.getKeyChar() == 'n' || evt.getKeyChar() == 'N' )
            containerPanel.startGame();
    }
    
}
