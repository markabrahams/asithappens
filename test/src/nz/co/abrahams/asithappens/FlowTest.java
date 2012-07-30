/*
 * FlowTest.java
 * JUnit based test
 *
 * Created on 9 May 2005, 22:55
 */

package nz.co.abrahams.asithappens;

import nz.co.abrahams.asithappens.storage.Device;
import nz.co.abrahams.asithappens.flow.FlowOptions;
import nz.co.abrahams.asithappens.flow.Flow;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import org.doomdark.uuid.EthernetAddress;
import java.net.*;

/**
 *
 * @author mark
 */
public class FlowTest {
    
    //private Logger logger;
    private Flow flow1, flow2, flow3, flow4, flow5;
    private Device device1, device2, device3, device4, device5;
    private FlowOptions ipProtocolOptions;
    private FlowOptions destinationAddressOptions;
    private FlowOptions destinationPortOptions;
    private FlowOptions destinationAddressPortOptions;
    
    public FlowTest() {
    }

    @Before
    public void setUp() throws java.lang.Exception {
        device1 = new Device(InetAddress.getByName("192.168.0.1"), new EthernetAddress("00:11:D8:0C:25:11"));
        device2 = new Device(InetAddress.getByName("10.1.2.3"), new EthernetAddress("00:11:D8:0C:25:22"));
        device3 = new Device(InetAddress.getByName("192.168.0.1"), new EthernetAddress("00:11:D8:0C:25:11"));
        device4 = new Device(InetAddress.getByName("10.1.2.3"), new EthernetAddress("00:11:D8:0C:25:22"));
        device5 = new Device(InetAddress.getByName("102.168.0.2"), new EthernetAddress("00:11:D8:0C:25:22"));
        
        flow1 = new Flow(1115641185, device1, device2, 2048, 6, 1025, 80, 100);
        flow2 = new Flow(1115641188, device3, device4, 2048, 6, 1025, 80, 200);
        
        flow3 = new Flow(1115641188, device1, device5, 2048, 6, 1025, 80, 300);
        flow4 = new Flow(1115641198, device1, device5, 2048, 6, 1029, 80, 300);
        flow5 = new Flow(1115641198, device2, device5, 2048, 6, 1034, 80, 300);
        
        
        destinationAddressPortOptions = new FlowOptions(true, false, true, false, false, true);
        ipProtocolOptions = new FlowOptions(true, false, false, false, false, false);
        destinationAddressOptions = new FlowOptions(true, false, true, false, false, false);
        destinationPortOptions = new FlowOptions(true, false, false, false, false, true);
    }

    @After
    public void tearDown() throws java.lang.Exception {
    }

    /**
     * Compatibility method for JUnit 3.x test runner.
     */
    public static junit.framework.Test suite() {
        return new junit.framework.JUnit4TestAdapter(FlowTest.class);
    }

    /**
     * Test of matches method, of class nz.co.abrahams.asithappens.Flow.
     */
    @Test
    public void testMatches() {
        
        Assert.assertTrue(flow1.matches(flow2));
        Assert.assertTrue(flow2.matches(flow1));
        Assert.assertFalse(flow1.matches(flow3));
        Assert.assertFalse(flow3.matches(flow1));
        
        Assert.assertTrue(flow3.matches(flow4, destinationAddressPortOptions));
        Assert.assertTrue(flow4.matches(flow3, destinationAddressPortOptions));
        Assert.assertTrue(flow3.matches(flow5, destinationAddressPortOptions));
        Assert.assertFalse(flow1.matches(flow5, destinationAddressPortOptions));
        
        Assert.assertTrue(flow1.matches(flow5, ipProtocolOptions));
    }

    /**
     * Test of toString method, of class nz.co.abrahams.asithappens.Flow.
     */
    @Test
    public void testToString() {
    }
        
}
