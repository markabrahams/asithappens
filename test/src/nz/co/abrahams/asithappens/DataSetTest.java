/*
 * DataSetTest.java
 * JUnit based test
 *
 * Created on 21 March 2006, 23:26
 */

package nz.co.abrahams.asithappens;

import nz.co.abrahams.asithappens.storage.DataSet;
import nz.co.abrahams.asithappens.storage.DataPoint;
import nz.co.abrahams.asithappens.storage.SummaryStatistics;
import nz.co.abrahams.asithappens.cartgraph.TimeSeriesContext;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import java.util.*;

/**
 *
 * @author mark
 */
public class DataSetTest {
    
    protected static long START_TIME = 1141991986200L;
    protected static long FINISH_TIME = 1141991988200L;
    protected static long TIME_INCREMENT = 2000;
    
    protected static double INITIAL_VALUE = 400D;
    protected static double VALUE_INCREMENT = 100D;
    protected static double EQUAL_DELTA = 0D;
    
    protected DataSet emptySet;
    protected DataSet singleNanSet;
    protected DataSet multipleNanSet;
    protected DataSet singlePointSet;
    protected DataSet multiplePointSet;
    
    public DataSetTest() {
    }

    @Before
    public void setUp() throws java.lang.Exception {
        
        emptySet = new DataSet();
        
        singleNanSet = new DataSet();
        singleNanSet.add(new DataPoint(START_TIME));
        
        multipleNanSet = new DataSet();
        multipleNanSet.add(new DataPoint(START_TIME));
        multipleNanSet.add(new DataPoint(START_TIME + TIME_INCREMENT));
        multipleNanSet.add(new DataPoint(START_TIME + 2 * TIME_INCREMENT));
        
        singlePointSet = new DataSet();
        singlePointSet.add(new DataPoint(START_TIME, INITIAL_VALUE));

        multiplePointSet = new DataSet();
        multiplePointSet.add(new DataPoint(START_TIME, INITIAL_VALUE));
        multiplePointSet.add(new DataPoint(START_TIME + TIME_INCREMENT, INITIAL_VALUE + VALUE_INCREMENT));
        multiplePointSet.add(new DataPoint(START_TIME + 2 * TIME_INCREMENT, INITIAL_VALUE + 2 * VALUE_INCREMENT));
        
    }

    @After
    public void tearDown() throws java.lang.Exception {
    }

    /**
     * Compatibility method for JUnit 3.x test runner.
     */
    public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(DataSetTest.class);
    }

    /**
     * Test of aggregate method, of class nz.co.abrahams.asithappens.DataSet.
     */
    @Test
    public void testAggregate() {
        System.out.println("testAggregate");
        
        Assert.assertTrue(emptySet.aggregate(TimeSeriesContext.Aggregation.Average) == null);
        Assert.assertFalse(emptySet.aggregate(TimeSeriesContext.Aggregation.Average, START_TIME, FINISH_TIME).isDefined());
        Assert.assertEquals( (START_TIME + FINISH_TIME) / 2, emptySet.aggregate(TimeSeriesContext.Aggregation.Average, START_TIME, FINISH_TIME).getTime());
        
        Assert.assertFalse(singleNanSet.aggregate(TimeSeriesContext.Aggregation.Average).isDefined());
        Assert.assertEquals(START_TIME, singleNanSet.aggregate(TimeSeriesContext.Aggregation.Average).getTime());
        
        Assert.assertTrue(singlePointSet.aggregate(TimeSeriesContext.Aggregation.Average).isDefined());
        Assert.assertEquals(START_TIME, singlePointSet.aggregate(TimeSeriesContext.Aggregation.Average).getTime());
        Assert.assertEquals(INITIAL_VALUE, singlePointSet.aggregate(TimeSeriesContext.Aggregation.Average).getValue(), EQUAL_DELTA);
        
        Assert.assertFalse(multipleNanSet.aggregate(TimeSeriesContext.Aggregation.Average).isDefined());
        Assert.assertEquals(START_TIME + TIME_INCREMENT, multipleNanSet.aggregate(TimeSeriesContext.Aggregation.Average).getTime());
        
        Assert.assertTrue(multiplePointSet.aggregate(TimeSeriesContext.Aggregation.Average).isDefined());
        Assert.assertEquals(START_TIME + TIME_INCREMENT, multiplePointSet.aggregate(TimeSeriesContext.Aggregation.Average).getTime());
        Assert.assertEquals(START_TIME + TIME_INCREMENT, multiplePointSet.aggregate(TimeSeriesContext.Aggregation.Maximum).getTime());
        Assert.assertEquals(START_TIME + TIME_INCREMENT, multiplePointSet.aggregate(TimeSeriesContext.Aggregation.Summed).getTime());
        Assert.assertEquals(INITIAL_VALUE + VALUE_INCREMENT, multiplePointSet.aggregate(TimeSeriesContext.Aggregation.Average).getValue(), EQUAL_DELTA);
        Assert.assertEquals(INITIAL_VALUE + 2 * VALUE_INCREMENT, multiplePointSet.aggregate(TimeSeriesContext.Aggregation.Maximum).getValue(), EQUAL_DELTA);
        //Assert.assertEquals( (INITIAL_VALUE + VALUE_INCREMENT) * 3 / (TIME_INCREMENT * 2), multiplePointSet.aggregate(TimeSeriesContext.Aggregation.Summed).getValue());
        
        Assert.assertFalse(multiplePointSet.aggregate(TimeSeriesContext.Aggregation.Average, START_TIME + (TIME_INCREMENT / 4 ), START_TIME + (TIME_INCREMENT * 3 / 4)).isDefined());
        Assert.assertTrue(multiplePointSet.aggregate(TimeSeriesContext.Aggregation.Average, START_TIME + (TIME_INCREMENT / 2 ), START_TIME + (TIME_INCREMENT * 3 / 2)).isDefined());
        Assert.assertEquals(INITIAL_VALUE + VALUE_INCREMENT, multiplePointSet.aggregate(TimeSeriesContext.Aggregation.Average, START_TIME + (TIME_INCREMENT / 2 ), START_TIME + (TIME_INCREMENT * 3 / 2)).getValue(), EQUAL_DELTA);
        
        Assert.assertEquals(INITIAL_VALUE, multiplePointSet.aggregate(TimeSeriesContext.Aggregation.Average, START_TIME, START_TIME + TIME_INCREMENT).getValue(), EQUAL_DELTA);
    }
    
    /**
     * Test of generateSummaryStatistics method, of class nz.co.abrahams.asithappens.DataSet.
     */
    @Test
    public void testGenerateSummaryStatistics() {
        SummaryStatistics summary;
        
        System.out.println("testGenerateSummaryStatistics");
        
        summary = emptySet.generateSummaryStatistics(START_TIME, FINISH_TIME);
        Assert.assertEquals(0, summary.samples);
        
        summary = singleNanSet.generateSummaryStatistics(START_TIME - TIME_INCREMENT, START_TIME + TIME_INCREMENT);
        Assert.assertEquals(0, summary.samples);
        
        summary = singlePointSet.generateSummaryStatistics(START_TIME - TIME_INCREMENT, START_TIME + TIME_INCREMENT);
        Assert.assertEquals(1, summary.samples);
        Assert.assertEquals(START_TIME, summary.firstTime);
        Assert.assertEquals(START_TIME, summary.lastTime);
        Assert.assertEquals(INITIAL_VALUE, summary.average, EQUAL_DELTA);
        Assert.assertEquals(INITIAL_VALUE, summary.minimum, EQUAL_DELTA);
        Assert.assertEquals(INITIAL_VALUE, summary.maximum, EQUAL_DELTA);
        summary = singlePointSet.generateSummaryStatistics(START_TIME - TIME_INCREMENT, START_TIME);
        Assert.assertEquals(1, summary.samples);
        summary = singlePointSet.generateSummaryStatistics(START_TIME, START_TIME + TIME_INCREMENT);
        Assert.assertEquals(1, summary.samples);
        summary = singlePointSet.generateSummaryStatistics(START_TIME, START_TIME);
        Assert.assertEquals(1, summary.samples);
        summary = singlePointSet.generateSummaryStatistics(START_TIME + TIME_INCREMENT, START_TIME + 2 * TIME_INCREMENT);
        Assert.assertEquals(0, summary.samples);

        summary = multipleNanSet.generateSummaryStatistics(START_TIME - TIME_INCREMENT, START_TIME + TIME_INCREMENT);
        Assert.assertEquals(0, summary.samples);

        summary = multiplePointSet.generateSummaryStatistics(START_TIME, START_TIME + 2 * TIME_INCREMENT);
        Assert.assertEquals(3, summary.samples);
        Assert.assertEquals(START_TIME, summary.firstTime);
        Assert.assertEquals(START_TIME + 2 * TIME_INCREMENT, summary.lastTime);
        Assert.assertEquals(INITIAL_VALUE + VALUE_INCREMENT, summary.average, EQUAL_DELTA);
        Assert.assertEquals(INITIAL_VALUE, summary.minimum, EQUAL_DELTA);
        Assert.assertEquals(INITIAL_VALUE + 2 * VALUE_INCREMENT, summary.maximum, EQUAL_DELTA);
        summary = multiplePointSet.generateSummaryStatistics(START_TIME - TIME_INCREMENT, START_TIME + TIME_INCREMENT);
        Assert.assertEquals(2, summary.samples);
        Assert.assertEquals(START_TIME, summary.firstTime);
        Assert.assertEquals(START_TIME + TIME_INCREMENT, summary.lastTime);
        Assert.assertEquals((2 * INITIAL_VALUE + VALUE_INCREMENT) / 2, summary.average, EQUAL_DELTA);
        Assert.assertEquals(INITIAL_VALUE, summary.minimum, EQUAL_DELTA);
        Assert.assertEquals(INITIAL_VALUE + VALUE_INCREMENT, summary.maximum, EQUAL_DELTA);
         
    }    
    
    /**
     * Test of generateSummaryStatistics method, of class nz.co.abrahams.asithappens.DataSet.
     */
    @Test
    public void testGetVector() {
        Vector vector;
        
        vector = emptySet.getVector();
        Assert.assertEquals(0, vector.size());

        vector = singleNanSet.getVector();
        Assert.assertEquals(1, vector.size());

        vector = singlePointSet.getVector();
        Assert.assertEquals(1, vector.size());
        
        vector = multipleNanSet.getVector();
        Assert.assertEquals(3, vector.size());

        vector = multiplePointSet.getVector();
        Assert.assertEquals(3, vector.size());
    }
    
}
