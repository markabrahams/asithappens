/*
 * DataPointTest.java
 * JUnit based test
 *
 * Created on 22 March 2006, 00:46
 */

package nz.co.abrahams.asithappens;

import nz.co.abrahams.asithappens.storage.DataPoint;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import java.util.Vector;

/**
 *
 * @author mark
 */
public class DataPointTest {
    
    protected static final long START_TIME = 1141991986200L;
    
    protected static final long INCREMENT = 2000;
    
    protected DataPoint nanPoint1;
    protected DataPoint point1;
    protected DataPoint point2;
    
    public DataPointTest() {
    }

    @Before
    public void setUp() throws java.lang.Exception {
        nanPoint1 = new DataPoint(START_TIME);
        point1 = new DataPoint(START_TIME, 1);
        point2 = new DataPoint(START_TIME + INCREMENT, 2);
    }

    @After
    public void tearDown() throws java.lang.Exception {
    }

    /**
     * Compatibility method for JUnit 3.x test runner.
     */
    public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(DataPointTest.class);
    }

    /**
     * Test of getTime method, of class nz.co.abrahams.asithappens.DataPoint.
     */
    @Test
    public void testGetTime() {
        System.out.println("testGetTime");
        
        Assert.assertEquals(START_TIME, nanPoint1.getTime());
        Assert.assertEquals(START_TIME, point1.getTime());
    }

    /**
     * Test of getValue method, of class nz.co.abrahams.asithappens.DataPoint.
     */
    @Test
    public void testGetValue() {
        System.out.println("testGetValue");
        
        Assert.assertEquals(0D, nanPoint1.getValue(), 0D);
        Assert.assertEquals(1D, point1.getValue(), 0D);
    }

    /**
     * Test of getVector method, of class nz.co.abrahams.asithappens.DataPoint.
     */
    @Test
    public void testGetVector() {
        Vector result;
        
        System.out.println("testGetVector");
        
        result = nanPoint1.getVector();
        Assert.assertEquals(DataPoint.formatTime(START_TIME), result.elementAt(0));
        Assert.assertTrue(((Double)(result.elementAt(1))).isNaN());
        
        result = point1.getVector();
        Assert.assertEquals(DataPoint.formatTime(START_TIME), result.elementAt(0));
        Assert.assertEquals(1D, ((Double)(result.elementAt(1))).doubleValue(), 0D);
    }

    /**
     * Test of isDefined method, of class nz.co.abrahams.asithappens.DataPoint.
     */
    @Test
    public void testIsDefined() {
        System.out.println("testIsDefined");
        
        Assert.assertFalse(nanPoint1.isDefined());
        Assert.assertTrue(point1.isDefined());

    }

    /**
     * Test of setValue method, of class nz.co.abrahams.asithappens.DataPoint.
     */
    @Test
    public void testSetValue() {
        DataPoint newPoint;
        
        System.out.println("testSetValue");
        
        newPoint = new DataPoint(START_TIME, 2);
        newPoint.setValue(3);
        Assert.assertEquals(3D, newPoint.getValue(), 0D);
    }

    /**
     * Test of compareTo method, of class nz.co.abrahams.asithappens.DataPoint.
     */
    @Test
    public void testCompareTo() {
        System.out.println("testCompareTo");
        
        Assert.assertEquals(0, point1.compareTo(point1));
        Assert.assertEquals(-INCREMENT, point1.compareTo(point2));
        Assert.assertEquals(INCREMENT, point2.compareTo(point1));
    }

    /**
     * Test of formatTime method, of class nz.co.abrahams.asithappens.DataPoint.
     */
    @Test
    public void testFormatTime() {
        System.out.println("testFormatTime");
        
        Assert.assertEquals("00:59:46.200", DataPoint.formatTime(START_TIME));
    }

    /**
     * Test of toString method, of class nz.co.abrahams.asithappens.DataPoint.
     */
    @Test
    public void testToString() {
        System.out.println("testToString");
        
        Assert.assertEquals("00:59:46.200 NaN", nanPoint1.toString());
        Assert.assertEquals("00:59:46.200 1.0", point1.toString());
    }
    
}
