/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package nz.co.abrahams.asithappens;

import nz.co.abrahams.asithappens.core.Configuration;
import nz.co.abrahams.asithappens.core.DBCreate;
import nz.co.abrahams.asithappens.core.DBException;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 * @author mark
 */
public class TestEnvironment {

    private TestEnvironment() {
    }

    public static void setUp() throws FileNotFoundException, IOException, DBException {
        Configuration.initialize();
        Configuration.loadPropertiesFile(ConfigurationTest.TEST_CONFIGURATION_FILE);
        DBCreate.create(null, null);
    }

    public static void tearDown() throws IOException {
        DBCreate.delete();
    }

}
