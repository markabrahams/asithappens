/*
 * GameImage.java
 *
 * Created on 8 January 2008, 03:13
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
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.ColorModel;
import javax.imageio.ImageIO;
import java.io.File;

/**
 *
 * @author mark
 */
public class GameImage {
    
    public static final String IMAGE_DIRECTORY = "images/sdn";
    
    public static final int ALPHA_CHANNEL_BAND = 0;
    
    public static final GameImage SELF = new GameImage("self.png", true, true);
    
    public static final GameImage SELF_MINI = new GameImage("self-mini.png", false, false);
    
    public static final GameImage USER_EGG = new GameImage("user-egg.png", false, true);
    
    public static final GameImage USER_UFO = new GameImage("user-ufo.png", false, true);
    
    public static final GameImage USER_BURGER = new GameImage("user-burger.png", false, true);
    
    public static final GameImage FIRING_CIRCLE = new GameImage("firing-circle.png", false, true);
    
    public static final GameImage FIRING_MISSILE = new GameImage("firing-missile.png", false, true);
    
    public static final GameImage FIRING_EMAIL = new GameImage("firing-email.png", false, true);
    
    public static final GameImage FIRING_PHONE = new GameImage("firing-phone.png", false, true);
    
    public static final GameImage FIRING_CHAIR = new GameImage("firing-chair.png", false, true);

    public static final GameImage PROCUREMENT = new GameImage("procurement.png", false, true);
    
    public static final GameImage BOMB = new GameImage("bomb.png", false, true);
    
    public static final GameImage BOMB_MINI = new GameImage("bomb-mini.png", false, false);
    
    public static final GameImage DEVELOPER_BIRD = new GameImage("developer-bird.png", false, true);
    
    public static final GameImage DEVELOPER_PLANE = new GameImage("developer-plane.png", false, true);
    
    public static final GameImage DEVELOPER_BUBBLE = new GameImage("developer-bubble.png", false, true);
    
    public static final GameImage ARCHITECT = new GameImage("architect.png", false, true);
    
    public static final GameImage BOSS_SUPERVISOR = new GameImage("boss-supervisor.png", false, true);
    
    public static final GameImage BOSS_TEAM_LEADER = new GameImage("boss-teamleader.png", false, true);
    
    public static final GameImage BOSS_NETWORKS_MANAGER = new GameImage("boss-networksmanager.png", false, true);
    
    public static final GameImage BOSS_OPERATIONS_MANAGER = new GameImage("boss-operationsmanager.png", false, true);
    
    public static final GameImage BOSS_IT_EXECUTIVE = new GameImage("boss-itexecutive.png", false, true);
    
    public static final GameImage BOSS_CIO = new GameImage("boss-cio.png", false, true);
    
    public static final GameImage BOSS_CEO = new GameImage("boss-ceo.png", false, true);
    
    public static final GameImage BOSS_DIRECTOR = new GameImage("boss-director.png", false, true);
    
    public static final GameImage BOSS_CHAIRMAN = new GameImage("boss-chairman.png", false, true);
    
    public static final GameImage BOSS_RECEIVER = new GameImage("boss-receiver.png", false, true);
    
    public static final GameImage POWERUP_DAMAGE = new GameImage("powerup-damage.png", false, false);
    
    public static final GameImage POWERUP_SPEED = new GameImage("powerup-speed.png", false, false);
    
    public static final GameImage POWERUP_ELEVATION = new GameImage("powerup-elevation.png", false, false);
    
    public static final GameImage POWERUP_LASER = new GameImage("powerup-laser.png", false, false);
    
    public static final GameImage POWERUP_BOMBS = new GameImage("powerup-bombs.png", false, false);
    
    public static final GameImage POWERUP_INVINCIBILITY = new GameImage("powerup-invincibility.png", false, false);
    
    public static final GameImage POWERUP_NULL = new GameImage("powerup-null.png", false, false);
                    
    public static final GameImage POWERUP_DAMAGE_MINI = new GameImage("powerup-damage-mini.png", false, false);
    
    public static final GameImage POWERUP_SPEED_MINI = new GameImage("powerup-speed-mini.png", false, false);
    
    public static final GameImage POWERUP_ELEVATION_MINI = new GameImage("powerup-elevation-mini.png", false, false);
    
    public static final GameImage POWERUP_LASER_MINI = new GameImage("powerup-laser-mini.png", false, false);
    
    public static final GameImage POWERUP_BOMBS_MINI = new GameImage("powerup-bombs-mini.png", false, false);
    
    public static final GameImage POWERUP_INVINCIBILITY_MINI = new GameImage("powerup-invincibility-mini.png", false, false);
    
    public static final GameImage POWERUP_NULL_MINI = new GameImage("powerup-null-mini.png", false, false);
                    
    public static final GameImage CD_AIX = new GameImage("cd-aix.png", false, false);
    
    public static final GameImage CD_OSX = new GameImage("cd-osx.png", false, false);
    
    public static final GameImage CD_SOLARIS = new GameImage("cd-solaris.png", false, false);
    
    public static final GameImage CD_BSD = new GameImage("cd-bsd.png", false, false);
    
    public static final GameImage CD_LINUX = new GameImage("cd-linux.png", false, false);
    
    public static final GameImage EXPLOSION_120X120 = new GameImage("explosion-120x120.png", false, false);
    
    public static final GameImage EXPLOSION_100X100 = new GameImage("explosion-100x100.png", false, false);
    
    public static final GameImage EXPLOSION_80X80 = new GameImage("explosion-80x80.png", false, false);
    
    public static final GameImage EXPLOSION_60X60 = new GameImage("explosion-60x60.png", false, false);
    
    public static final GameImage EXPLOSION_50X50 = new GameImage("explosion-50x50.png", false, false);
    
    public static final GameImage EXPLOSION_40X40 = new GameImage("explosion-40x40.png", false, false);
    
    public static final GameImage EXPLOSION_30X30 = new GameImage("explosion-30x30.png", false, false);
    
    public static final GameImage EXPLOSION_20X20 = new GameImage("explosion-20x20.png", false, false);
    
    public static final GameImage EXPLOSION_10X10 = new GameImage("explosion-10x10.png", false, false);
    
    public static final GameImage WORDS_BONUS_LIFE = new GameImage("words-bonus-life.png", false, false);
    
    public static final GameImage WORDS_ROUTER_RAINBOW = new GameImage("words-router-rainbow.png", false, false);
    
    public static final GameImage WORDS_INSTALL_YAHTZEE = new GameImage("words-install-yahtzee.png", false, false);
    
    
    protected Logger logger;
    
    protected String imageString;
    
    protected BufferedImage image;
    
    protected int width;
    
    protected int height;
    
    protected int[] leftProfile;
    
    protected int[] rightProfile;
    
    protected int[] topProfile;
    
    protected int[] bottomProfile;
    
    /** Creates a new instance of LoadImages */
    public GameImage(String imageString, boolean horizontalProfiles, boolean verticalProfiles) {
        logger = Logger.getLogger(this.getClass().getName());
        this.imageString = imageString;
        logger.debug("Reading image file: " + imageString);
        image = GameImage.readImage(IMAGE_DIRECTORY + "/" + imageString);
        width = image.getWidth();
        height = image.getHeight();
        if ( horizontalProfiles )
            createHorizontalProfiles();
        if ( verticalProfiles )
            createVerticalProfiles();
        //logger.debug("Create image: ");
        //logger.debug(toString());
    }
    
    public static BufferedImage readImage(String imageString) {
        try {
            return ImageIO.read(new File(imageString));
        } catch (Exception e) {
            System.out.println("Exception: " + e);
            return null;
        }
    }
    
    public Image getImage() {
        return image;
    }
    
    protected void createHorizontalProfiles() {
        topProfile = new int[width];
        bottomProfile = new int[width];
        //logger.debug("Image type: " + image.getType());
        for ( int x = 0 ; x < width ; x++ ) {
            boolean foundTop;
            foundTop = false;
            
            for ( int y = 0 ; y < height ; y++ ) {
                //pixelComponents = image.getData().getPixel(x, y, pixelComponents);
                //if ( pixelComponents[ALPHA_CHANNEL_BAND] > 0 ) {
                if ( image.getAlphaRaster().getSample(x, y, ALPHA_CHANNEL_BAND) > 0 ) {
                    if ( ! foundTop ) {
                        foundTop = true;
                        topProfile[x] = y;
                    }
                    bottomProfile[x] = y;
                }
            }
        }
        
    }
    
    protected void createVerticalProfiles() {
        
        leftProfile = new int[height];
        rightProfile = new int[height];
        for ( int y = 0 ; y < height ; y++ ) {
            boolean foundLeft;
            foundLeft = false;
            
            for ( int x = 0 ; x < width ; x++ ) {
                //pixelComponents = image.getData().getPixel(x, y, pixelComponents);
                //if ( pixelComponents[ALPHA_CHANNEL_BAND] > 0 ) {
                if ( image.getAlphaRaster().getSample(x, y, ALPHA_CHANNEL_BAND) > 0 ) {
                    if ( ! foundLeft ) {
                        foundLeft = true;
                        leftProfile[y] = x;
                    }
                    rightProfile[y] = x;
                }
            }
        }
    }
    
    public boolean collisionOverlap(GameImage otherImage, int thisXOffset, int thisYOffset,
            int otherXOffset, int otherYOffset, int xOverlapWidth, int yOverlapHeight) {
        int xDifference;
        int thisXMatterStart;
        int thisXMatterEnd;
        int otherXMatterStart;
        int otherXMatterEnd;
        int[] otherLeftProfile;
        int[] otherRightProfile;
        
        if ( thisXOffset > 0 )
            xDifference = thisXOffset;
        else
            xDifference = - otherXOffset;
        otherLeftProfile = otherImage.getLeftProfile();
        otherRightProfile = otherImage.getRightProfile();
        
        for ( int y = 0 ; y < yOverlapHeight ; y++ ) {
            thisXMatterStart = Math.max(leftProfile[y + thisYOffset], thisXOffset);
            thisXMatterEnd = Math.min(rightProfile[y + thisYOffset], thisXOffset + xOverlapWidth);
            otherXMatterStart = Math.max(otherLeftProfile[y + otherYOffset], otherXOffset) + xDifference;
            otherXMatterEnd = Math.min(otherRightProfile[y + otherYOffset], otherXOffset + xOverlapWidth) + xDifference;
            if ( otherXMatterEnd >= thisXMatterStart && otherXMatterStart <= thisXMatterEnd )
                return true;
        }
        return false;
    }
    
    public int getLeftOffset(int y) {
        return leftProfile[y];
    }

    public int getRightOffset(int y) {
        return rightProfile[y];
    }
    
    public int getTopOffset(int x) {
        return topProfile[x];
    }
    
    public int getBottomOffset(int x) {
        return bottomProfile[x];
    }
    
    public int[] getLeftProfile() {
        return leftProfile;
    }
    
    public int[] getRightProfile() {
        return rightProfile;
    }
    
    public int[] getTopProfile() {
        return topProfile;
    }
    
    public int[] getBottomProfile() {
        return bottomProfile;
    }
    
    public int getWidth() {
        return width;
    }
    
    public int getHeight() {
        return height;
    }
    
    public String toString() {
        StringBuffer buffer;
        
        buffer = new StringBuffer();
        
        if ( leftProfile != null ) {
            buffer.append("Left profile: ");
            for ( int y = 0 ; y < height ; y++ ) {
                buffer.append(leftProfile[y]);
                if ( y < height - 1 )
                    buffer.append(",");
            }
            buffer.append("\n");
        }
        
        if ( rightProfile != null ) {
            buffer.append("Right profile: ");
            for ( int y = 0 ; y < height ; y++ ) {
                buffer.append(rightProfile[y]);
                if ( y < height - 1 )
                    buffer.append(",");
            }
            buffer.append("\n");
        }
        
        if ( topProfile != null ) {
            buffer.append("Top profile: ");
            for ( int x = 0 ; x < width ; x++ ) {
                buffer.append(topProfile[x]);
                if ( x < width - 1 )
                    buffer.append(",");
            }
            buffer.append("\n");
        }
        
        if ( bottomProfile != null ) {
            buffer.append("Bottom profile: ");
            for ( int x = 0 ; x < width ; x++ ) {
                buffer.append(bottomProfile[x]);
                if ( x < width - 1 )
                    buffer.append(",");
            }
            buffer.append("\n");
        }
        
        return buffer.toString();
    }
}