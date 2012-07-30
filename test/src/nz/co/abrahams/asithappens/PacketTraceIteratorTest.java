/*
 * PacketTraceIteratorTest.java
 * JUnit based test
 *
 * Created on 2 May 2005, 23:34
 */

package nz.co.abrahams.asithappens;

import nz.co.abrahams.asithappens.flow.Flow;
import nz.co.abrahams.asithappens.capture.PacketTraceIterator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

/**
 *
 * @author mark
 */
public class PacketTraceIteratorTest {
    
    protected static final String FILENAME = "test/data/sample.pkt";
    protected PacketTraceIterator iterator;
    
    public PacketTraceIteratorTest() {
    }

    @Before
    public void setUp() throws java.lang.Exception {
        iterator = new PacketTraceIterator(FILENAME);
    }

    @After
    public void tearDown() throws java.lang.Exception {
    }

    /**
     * Compatibility method for JUnit 3.x test runner.
     */
    public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(PacketTraceIteratorTest.class);
    }

    /**
     * Test of nextPacket method, of class nz.co.abrahams.asithappens.PacketTraceParser.
     */
    @Test
    public void testNext() {
        Flow flow;
        System.out.println("testNext");
        flow = iterator.next();
        flow = iterator.next();
        flow = iterator.next();
        Assert.assertTrue(true);
    }
    
}
