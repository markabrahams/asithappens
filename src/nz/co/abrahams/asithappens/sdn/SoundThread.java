/*
 * SoundThread.java
 *
 * Created on 11 February 2008, 20:31
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

import org.apache.log4j.Logger;
import javax.sound.sampled.*;

/**
 *
 * @author mark
 */
public class SoundThread extends Thread {
    
    public static final int REPEAT_THRESHOLD = 100;
    
    protected Logger logger;
    
    protected GameContext gameContext;
    
    protected Object playSoundMonitor;
    
    protected boolean threadRunning;
    
    protected Sounds nextSound;
    
    /** Creates a new instance of SoundThread */
    public SoundThread(GameContext gameContext) {
        super("SDN-sound");
        this.gameContext = gameContext;
        logger = Logger.getLogger(this.getClass().getName());
        playSoundMonitor = new Object();
    }
    
    public void run() {
        threadRunning = true;
        while ( threadRunning ) {
            try {
                synchronized(playSoundMonitor) {
                    playSoundMonitor.wait();
                    if ( nextSound != null )
                        playNextSound();
                }
            } catch (InterruptedException e) {
                logger.error("Sound thread interrupted from wait");
            }
        }
    }
    
    public void playNextSound() {
        Clip clip;
        
        clip = nextSound.getClip();
        if ( gameContext.isSoundOn() && ( ! clip.isRunning() || clip.getMicrosecondPosition() > REPEAT_THRESHOLD * 1000 ) ) {
            clip.setFramePosition(0);
            clip.start();
        }
    }
    
    public void addSound(Sounds sound) {
        nextSound = sound;
        synchronized(playSoundMonitor) {
            playSoundMonitor.notifyAll();
        }
    }
    
    public void endThread() {
        threadRunning = false;
        nextSound = null;
        synchronized(playSoundMonitor) {
            playSoundMonitor.notifyAll();
        }
    }
}
