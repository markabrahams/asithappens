/*
 * KeyHandler.java
 *
 * Created on 22 December 2007, 19:46
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

import java.awt.event.*;
import org.apache.log4j.Logger;
/**
 *
 * @author mark
 */
public class KeyHandler extends KeyAdapter {

    protected static Logger logger = Logger.getLogger(KeyHandler.class);
    
    protected GameContext game;
    
    /** Creates a new instance of KeyHandler */
    public KeyHandler(GameContext game) {
        this.game = game;
    }
    
    public void keyPressed(KeyEvent event) {
        if ( event.getKeyCode() == KeyEvent.VK_LEFT )
            game.getSelf().startMovingLeft();
        else if ( event.getKeyCode() == KeyEvent.VK_RIGHT )
            game.getSelf().startMovingRight();
        else if ( event.getKeyCode() == KeyEvent.VK_UP )
            game.getSelf().startMovingUp();
        else if ( event.getKeyCode() == KeyEvent.VK_DOWN )
            game.getSelf().startMovingDown();
        else if ( event.getKeyCode() == KeyEvent.VK_C )
            game.getSelf().fireLaser();
        else if ( event.getKeyCode() == KeyEvent.VK_X )
            game.getSelf().fireBomb();
        else if ( event.getKeyCode() == KeyEvent.VK_Z )
            game.getSelf().takeInvincipill();
        else if ( event.getKeyCode() == KeyEvent.VK_COMMA )
            game.testMethod();
        else if ( event.getKeyCode() == KeyEvent.VK_SLASH )
            game.toggleSoundEffects();
        //else if ( event.getKeyCode() == KeyEvent.VK_ESCAPE )
        //    game.forceEnd();
    }

    public void keyReleased(KeyEvent event) {
        if ( event.getKeyCode() == KeyEvent.VK_LEFT )
            game.getSelf().stopMovingLeft();
        else if ( event.getKeyCode() == KeyEvent.VK_RIGHT )
            game.getSelf().stopMovingRight();
        else if ( event.getKeyCode() == KeyEvent.VK_UP )
            game.getSelf().stopMovingUp();
        else if ( event.getKeyCode() == KeyEvent.VK_DOWN )
            game.getSelf().stopMovingDown();
    }
    
}
