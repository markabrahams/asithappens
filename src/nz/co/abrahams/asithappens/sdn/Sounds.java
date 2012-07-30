/*
 * SoundPlay.java
 *
 * Created on 24 January 2008, 01:32
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

import java.applet.Applet;
import java.applet.AudioClip;
import java.net.URL;
import java.net.MalformedURLException;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;

/**
 *
 * @author mark
 */
public class Sounds {
    
    public static final String SOUND_DIRECTORY = "sounds";
    
    public static final int REPEAT_THRESHOLD = 200;
    
    /*
    public static AudioClip GAME_START;
     
    public static AudioClip FIRE_LASER;
     
    public static AudioClip FIRE_BOMB;
     
    public static AudioClip EXPLOSION_SELF;
     
    public static AudioClip EXPLOSION_LANDSCAPE;
     
    public static AudioClip EXPLOSION_ENEMY;
     
    public static AudioClip EXPLOSION_BOMB;
     */
    
    public static Sounds TRANSITION_START;
    
    public static Sounds TRANSITION_BOSS;
    
    public static Sounds TRANSITION_LEVEL;
    
    public static Sounds TRANSITION_GAMEOVER;
    
    public static Sounds TRANSITION_VICTORY;
    
    public static Sounds FIRE_LASER;
    
    public static Sounds FIRE_BOMB;
    
    public static Sounds EXPLOSION_SELF;
    
    public static Sounds EXPLOSION_LANDSCAPE;
    
    public static Sounds EXPLOSION_ENEMY;
    
    public static Sounds EXPLOSION_SPATIAL;
    
    public static Sounds EXPLOSION_BOMB;
    
    public static Sounds COLLECT_POWERUP;
    
    public static Sounds COLLECT_CD;
    
    public static Sounds INVINCIPILL;
    
    public static Sounds BONUS_LIFE;
    
    public static boolean soundOn;
    
    
    protected Clip clip;
    
    static {
        try {
            /*
            GAME_START = Sounds.createClip("game-start.wav");
            FIRE_LASER = Sounds.createClip("fire-laser.wav");
            FIRE_BOMB = Sounds.createClip("fire-bomb.wav");
            EXPLOSION_SELF = Sounds.createClip("explosion-self.wav");
            EXPLOSION_LANDSCAPE = Sounds.createClip("explosion-landscape.wav");
            EXPLOSION_ENEMY = Sounds.createClip("explosion-enemy.wav");
            EXPLOSION_BOMB = Sounds.createClip("explosion-bomb.wav");
             */
            TRANSITION_START = new Sounds("game-start.wav");
            TRANSITION_BOSS = new Sounds("boss.wav");
            TRANSITION_LEVEL = new Sounds("end-level.wav");
            TRANSITION_GAMEOVER = new Sounds("game-over.wav");
            TRANSITION_VICTORY = new Sounds("victory.wav");
            FIRE_LASER = new Sounds("fire-laser.wav");
            FIRE_BOMB = new Sounds("fire-bomb.wav");
            EXPLOSION_SELF = new Sounds("explosion-self.wav");
            EXPLOSION_LANDSCAPE = new Sounds("explosion-landscape.wav");
            EXPLOSION_ENEMY = new Sounds("explosion-enemy.wav");
            EXPLOSION_SPATIAL = new Sounds("explosion-spatial.wav");
            EXPLOSION_BOMB = new Sounds("explosion-bomb.wav");
            COLLECT_POWERUP = new Sounds("collect-powerup.wav");
            COLLECT_CD = new Sounds("collect-cd.wav");
            INVINCIPILL = new Sounds("invincipill.wav");
            BONUS_LIFE = new Sounds("bonus-life.wav");
            soundOn = true;
        } catch ( Exception e ) {
            System.out.println(e);
        }
    }
    
    /** Creates a new instance of SoundPlay */
    public Sounds(String fileName) {
        loadClip(fileName);
    }
    
    public static AudioClip createClip(String file) throws MalformedURLException {
        return Applet.newAudioClip(new File(SOUND_DIRECTORY + "/" + file).toURI().toURL());
    }
    
    public static void setSoundOn(boolean value) {
        soundOn = value;
    }
    
    /*
    public static void play(AudioClip audioClip) {
        if ( soundOn )
            audioClip.play();
    }
    */
    
    public void play() {
        //long position;
        
        //position = clip.getMicrosecondPosition();
        if ( ! clip.isRunning() || clip.getMicrosecondPosition() > REPEAT_THRESHOLD * 1000 ) {
            clip.setFramePosition(0);
            clip.start();
        }
    }
    
    protected void loadClip(String fileName) {
        File file;
        AudioInputStream ais;
        
        file = new File(SOUND_DIRECTORY + "/" + fileName);
        // read audio file from disk
        try {
            ais = AudioSystem.getAudioInputStream(file);
            clip = (Clip)AudioSystem.getLine(new Line.Info(Clip.class));
            clip.open(ais);
            //clip.addLineListener(this);
        } catch (UnsupportedAudioFileException uafe) {
            soundOn = false;
            throw new RuntimeException("Not a valid supported audio file: \"" + fileName + "\"");
        } catch (LineUnavailableException lue) {
            soundOn = false;
            throw new RuntimeException("Line is not available to play sound \"" + fileName + "\"");
        } catch (IOException ioe) {
            soundOn = false;
            throw new RuntimeException("I/O error while reading file: \"" + fileName + "\"");
        }
        
    }
    
    protected Clip getClip() {
        return clip;
    }
}
