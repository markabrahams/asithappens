/*
 * CollectableObject.java
 *
 * Created on 19 April 2008, 11:29
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nz.co.abrahams.asithappens.sdn;

/**
 *
 * @author mark
 */
public interface CollectableObject {
    
    GameImage getGameImage();
    
    double getX();
    
    double getY();
    
    java.awt.Image getImage();
    
    void collected();
}
