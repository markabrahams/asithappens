/*
 * LongTask.java
 *
 * Created on 11 September 2006, 23:51
 */

package nz.co.abrahams.asithappens.uiutil;

/**
 *
 * @author mark
 */
public interface LongTask {
    
    public final static int COMPLETED = 100;

    public void start();
    public boolean cancel(boolean mayInterruptIfRunning);
    public int getProgress();
    public boolean isDone();
    
}
