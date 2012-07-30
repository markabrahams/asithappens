/*
 * UpdateableObject.java
 *
 * Created on 26 January 2008, 08:58
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package nz.co.abrahams.asithappens.sdn;

/**
 *
 * @author mark
 */
public interface UpdateableObject {
    
    void update();
    
    boolean isFinished();
}
