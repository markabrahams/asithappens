/*
 * AllTests.java
 *
 * Created on 26 January 2005, 01:37
 */

package nz.co.abrahams.asithappens;

import nz.co.abrahams.asithappens.core.Configuration;
import junit.framework.Test;
import junit.framework.TestSuite;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 * @author mark
 */
public class AllTests {

    public static String TEST_CONFIGURATION = "test/config/test.properties";

    /** Creates a new instance of AllTests */
    public AllTests() {
    }
    
    protected static void setUpTestEnvironment() throws FileNotFoundException, IOException {
        Configuration.loadPropertiesFile(TEST_CONFIGURATION);
        //DatabaseCreate.create();
    }

    public static Test suite() throws FileNotFoundException, IOException {
        TestSuite suite;

        setUpTestEnvironment();
        suite = new TestSuite("Test for nz.co.abrahams.asithappens");
        //suite.addTestSuite(DatabaseCreateTest.class);
        //suite.addTestSuite(DataPointTest.class);
        //suite.addTestSuite(DataSetTest.class);
        //suite.addTestSuite(FlowTest.class);
        //suite.addTestSuite(PacketTraceIteratorTest.class);
        return suite;
    }
}
