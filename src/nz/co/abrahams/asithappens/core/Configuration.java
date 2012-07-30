/*
 * Configuration.java
 *
 * Created on February 12, 2005, 12:08 AM
 *
 * AsItHappens - real-time network monitor
 * Copyright (C) 2006  Mark Abrahams
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */
package nz.co.abrahams.asithappens.core;

import java.io.*;
import java.util.*;

/**
 * Application configuration provider.  The configuration is a global singleton
 * so that the entire application receives the same configuration.
 * Configuration is provided via two
 * properties files, which can contain the same set of option keys.  The first
 * is a "defaults" file, which is envisioned to remain unchanged by the user.
 * The second contains the user-specified application settings.
 *
 * @author mark
 */
public class Configuration {

    //public static final String FILE_RESOURCE_PREFIX = "nz/co/abrahams/asithappens";

    /** Defaults properties file */
    public static final String DEFAULT_PROPERTIES_FILE = "config/default.properties";
    /** User-defined properties file */
    public static final String CUSTOM_PROPERTIES_FILE = "config/asithappens.properties";
    /** Logging properties file */
    public static final String DEFAULT_LOG4J_PROPERTIES_FILE = "config/log4j.properties";
    /** Icon for top-level Frames */
    public static final String FRAME_ICON = "images/asithappens-icon16x16.png";
    /** Global application configuration */
    protected static Configuration global = new Configuration();
    /** Application properties */
    Properties properties;

    /** Creates a new Configuration instance. */
    /*
    public Configuration() throws FileNotFoundException, IOException {
        properties = new Properties();
        if (global == null)
            global = this;
        loadProperties(DEFAULT_PROPERTIES_FILE);

        // Ignore condition where no custom properties file is found
        try {
            loadProperties(PROPERTIES_FILE);
        } catch (Exception e) {
        }
    }
    */

    private Configuration() {
        properties = new Properties();
    }

    public static void initialize() throws FileNotFoundException, IOException {
        Configuration.loadPropertiesFile(DEFAULT_PROPERTIES_FILE);

        // Ignore condition where no custom properties file is found
        try {
            Configuration.loadPropertiesFile(CUSTOM_PROPERTIES_FILE);
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }
    }

    public static void loadPropertiesFile(String propertiesFileName) throws FileNotFoundException, IOException {
        global.loadProperties(propertiesFileName);
    }

    /**
     * Loads properties from file into this configuration
     *
     * @param propertiesFileName the name of the properties file to load
     */
    protected void loadProperties(String propertiesFileName) throws FileNotFoundException, IOException {
        InputStream in;

        //in = ClassLoader.getSystemResourceAsStream(DEFAULT_PROPERTIES_FILE);
        in = new FileInputStream(propertiesFileName);
        properties.load(in);
        in.close();
    }

    /**
     * Fetches a property with a string value.
     *
     * @param propertyName the property key to fetch
     * @return             the property value
     */
    public static String getProperty(String propertyName) {
        return global.properties.getProperty(propertyName);
    }

    /**
     * Fetches a property with an integer value.
     *
     * @param propertyName the property key to fetch
     * @return             the property value
     */
    public static int getPropertyInt(String propertyName) {
        return Integer.parseInt(global.properties.getProperty(propertyName));
    }
}
