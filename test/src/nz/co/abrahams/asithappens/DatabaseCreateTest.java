/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nz.co.abrahams.asithappens;

import nz.co.abrahams.asithappens.core.Configuration;
import nz.co.abrahams.asithappens.core.DBCreate;
import nz.co.abrahams.asithappens.core.DBUtil;
import nz.co.abrahams.asithappens.core.DBException;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.*;

/**
 *
 * @author mark
 */
public class DatabaseCreateTest {

    public static final String DBCREATE_CONFIGURATION_FILE = "test/config/dbcreate.properties";

    public DatabaseCreateTest() {
    }

    @Before
    public void setUp() throws FileNotFoundException, IOException {
        Configuration.initialize();
        Configuration.loadPropertiesFile(ConfigurationTest.TEST_CONFIGURATION_FILE);
        Configuration.loadPropertiesFile(DBCREATE_CONFIGURATION_FILE);
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testCreate() throws DBException, IOException, InterruptedException {
        DBCreate.create(null, null);
        DBUtil.setConnectionURL(DBUtil.getConnectionURL(true));
        assertTrue(DBUtil.databaseExists());
        //DBUtil.
        //Thread.sleep(2000);
        DBCreate.delete();
        //assertFalse(DBUtil.databaseExists());
    }

    @Ignore
    public void testDelete() throws IOException {
        DBCreate.delete();
    }
    
}