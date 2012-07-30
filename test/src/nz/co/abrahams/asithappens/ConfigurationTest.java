/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nz.co.abrahams.asithappens;

import nz.co.abrahams.asithappens.core.Configuration;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author mark
 */
public class ConfigurationTest {

    public static final String TEST_CONFIGURATION_FILE = "test/config/test.properties";

    public static final String STRING_PROPERTY_NAME = "test.string";

    public static final String STRING_PROPERTY_VALUE = "itsallgoodg";

    public static final String INT_PROPERTY_NAME = "test.int";

    public static final int INT_PROPERTY_VALUE = 5;

    public ConfigurationTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of loadPropertiesFile method, of class Configuration.
     */
    @Test
    public void testLoadPropertiesFile() throws Exception {
        //System.out.println("loadPropertiesFile");
        //String propertiesFileName = "";
        Configuration.loadPropertiesFile(TEST_CONFIGURATION_FILE);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of loadProperties method, of class Configuration.
     */
    /*
    @Test
    public void testLoadProperties() throws Exception {
        System.out.println("loadProperties");
        String propertiesFileName = "";
        //Configuration instance = new Configuration();
        Configuration.initialize();
        Configuration.loadPropertiesFile(propertiesFileName);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
     */

    /**
     * Test of getProperty method, of class Configuration.
     */
    @Test
    public void testGetProperty() {
        String result;

        result = Configuration.getProperty(STRING_PROPERTY_NAME);
        assertEquals(STRING_PROPERTY_VALUE, result);
    }

    /**
     * Test of getPropertyInt method, of class Configuration.
     */
    @Test
    public void testGetPropertyInt() {
        int result;

        result = Configuration.getPropertyInt(INT_PROPERTY_NAME);
        assertEquals(INT_PROPERTY_VALUE, result);
    }

}