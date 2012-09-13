/*
 * DBUtil.java
 *
 * Created on 21 June 2008, 21:56
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
 *
 */

package nz.co.abrahams.asithappens.core;

import java.sql.*;
import org.apache.log4j.Logger;
//import com.sun.rowset.CachedRowSetImpl;

/**
 *
 * @author mark
 */
public class DBUtil {
    
    /** Test for database existence */
    public static final String DB_EXISTS_TEST = "SELECT sessionID FROM sessions";
    
    /** Logging provider */
    private static Logger logger = Logger.getLogger(DBUtil.class);
    
    /** Flag indicating that the driver is registered */
    private static boolean registered = false;
    
    /** Connection URL built from configuration */
    private static String connectionURL;
    
    /** Private unused constructor - all methods are static in this class */
    private DBUtil() {
    }
    
    public static void setConnectionURL(String url) {
        connectionURL = url;
    }
    
    public static String getConnectionURL(boolean includePassword) throws DBException {
        String connectionString;
        
        // MySQL connection
        if ( Configuration.getProperty("database.type").equals("mysql") ) {
            connectionString = "jdbc:" +
                    Configuration.getProperty("database.type") + "://" +
                    Configuration.getProperty("database.server") + "/" +
                    Configuration.getProperty("database.name") + "?user=" +
                    Configuration.getProperty("database.user") + "&password=";
            if ( includePassword ) {
                connectionString = connectionString + Configuration.getProperty("database.password");
            }
            return connectionString;
        }
        
        // H2 connection
        else if ( Configuration.getProperty("database.type").equals("h2") ) {
            
            connectionString = "jdbc:" +
                    Configuration.getProperty("database.type") + ":" +
                    Configuration.getProperty("database.filepath");
            return connectionString;
        }
        
        // Other DB types are not supported
        else {
            throw new DBException("Database type  \"" +
                    Configuration.getProperty("database.type") +
                    "\" not supported");
        }
    }
    
    public static Connection getConnection() throws DBException {
        return getConnection(connectionURL, null, null);
    }
    
    /** Opens a new database connection. */
    public static Connection getConnection(String url) throws DBException {
        return getConnection(url, null, null);
    }
    
    /** Opens a new database connection. */
    public static Connection getConnection(String url, String user, String password) throws DBException {
        String driverName;
        Connection newConnection;
        
        driverName = Configuration.getProperty("database.driver");
        
        try {
            
            if ( ! registered ) {
                //DriverManager.registerDriver((Driver)(Class.forName(driverName).newInstance()));
                Class.forName(driverName).newInstance();
                registered = true;
            }
            
            if ( user == null )
                newConnection = DriverManager.getConnection(url);
            else
                newConnection = DriverManager.getConnection(url, user, password);
            return newConnection;
            
        } catch (ClassNotFoundException e) {
            logger.warn("Cannot find database driver class \"" + driverName + "\"");
            throw new DBException("Cannot find database driver class \"" + driverName + "\"", e);
        } catch (InstantiationException e) {
            logger.warn("Cannot instantiate database driver class \"" + driverName + "\"");
            throw new DBException("Cannot instantiate database driver class \"" + driverName + "\"", e);
        } catch (IllegalAccessException e) {
            logger.warn("Illegal access when opening database driver \"" + driverName + "\"");
            throw new DBException("Illegal access when opening database driver \"" + driverName + "\"", e);
        } catch (SQLException e) {
            logger.warn("Cannot open database connection: " + e.toString());
            throw new DBException("Cannot open database connection", e);
        }
        
    }
    
    /**
     * Check for database existence.
     *
     * @return true if the database exists, false if not.
     */
    public static boolean databaseExists() throws DBException {
        Connection connection;
        Statement statement;
        
        try {
            connection = getConnection();
            statement = connection.createStatement();
            // TODO - Get a decent test for table existence
            statement.executeQuery(DB_EXISTS_TEST);
            return true;
        } catch (SQLException e) {
            if ( Configuration.getProperty("database.type").equals("mysql") ) {
                if ( ( e.toString().matches(".*Invalid authorization specification message.*") ||
                        e.toString().matches(".*Unknown database.*") ) )
                    return false;
                else
                    throw new DBException("Cannot determine database state", e);
                
            } else if ( Configuration.getProperty("database.type").equals("h2") ) {
                if ( e.getErrorCode() == 90020 ) {
                    System.out.println("Error code: " + ((SQLException)(e.getCause())).getErrorCode() );
                    throw new DBException("Database locked by another running application - ensure that AsItHappens is not already running", e);
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
    }
    
    /**
     * Fetches a single value from the database.  The query result must be a
     * single row, or an exception is thrown.  The value in the given column
     * of the returned row is selected.
     *
     * @param connection connection to database
     * @param query      an SQL query string
     * @param keyID      the integer primary key for the table
     * @return           an object containing the typed value
     */
    public static Object retrieveSingleAttributeWithPK(Connection connection, String query, int keyID) throws DBException {
        PreparedStatement statement;
        ResultSet results;
        Object value;
        
        try {
            statement = connection.prepareStatement(query);
            statement.setInt(1, keyID);
            results = statement.executeQuery();
            if (results.next())
                value = results.getObject(1);
            else {
                logger.debug("No result for query: " + query + " with key " + keyID);
                throw new DBException("No result for query: " + query + " with key " + keyID);
            }
            if (results.next()) {
                logger.debug("More than one entry for query: " + query + " with key " + keyID);
                throw new DBException("More than one entry for query: " + query + " with key " + keyID);
            }
            results.close();
            statement.close();
            return value;
        } catch (SQLException e) {
            logger.error("Error fetching query: " + query + " with key " + keyID);
            throw new DBException("Error fetching query: " + query + " with key " + keyID, e);
        }
        
    }
    
    public static int retrieveIntWithPK(Connection connection, String query, int keyID) throws DBException {
        return ((Integer)(DBUtil.retrieveSingleAttributeWithPK(connection, query, keyID))).intValue();
    }

    public static long retrieveLongWithPK(Connection connection, String query, int keyID) throws DBException {
        return ((Long)(DBUtil.retrieveSingleAttributeWithPK(connection, query, keyID))).longValue();
    }

    public static boolean retrieveBooleanWithPK(Connection connection, String query, int keyID) throws DBException {
        return ((Byte)(DBUtil.retrieveSingleAttributeWithPK(connection, query, keyID))).intValue() == 1;
    }
    
    public static String retrieveStringWithPK(Connection connection, String query, int keyID) throws DBException {
        return (String)(DBUtil.retrieveSingleAttributeWithPK(connection, query, keyID));
    }
    
    /**
     * Fetches a single value from the database.  The query result must be a
     * single row, or an exception is thrown.  The value in the given column
     * of the returned row is selected.
     *
     * @param connection connection to database
     * @param query      an SQL query string
     * @param keyName    a string containing the primary key for the table
     * @return           an object containing the typed value
     */
    public static Object retrieveSingleAttributeWithStringPK(Connection connection, String query, String keyName) throws DBException {
        PreparedStatement statement;
        ResultSet results;
        Object value;
        
        try {
            statement = connection.prepareStatement(query);
            statement.setString(1, keyName);
            results = statement.executeQuery();
            if (results.next())
                value = results.getObject(1);
            else {
                logger.debug("No result for query: " + query + " with key " + keyName);
                throw new DBException("No result for query: " + query + " with key " + keyName);
            }
            if (results.next()) {
                logger.debug("More than one entry for query: " + query + " with key " + keyName);
                throw new DBException("More than one entry for query: " + query + " with key " + keyName);
            }
            results.close();
            statement.close();
            return value;
        } catch (SQLException e) {
            logger.error("Error fetching query: " + query + " with key " + keyName);
            throw new DBException("Error fetching query: " + query + " with key " + keyName, e);
        }
        
    }

    /**
     * Fetches a single value from the database.  The query result must be a
     * single row, or an exception is thrown.  The value in the given column
     * of the returned row is selected.
     *
     * @param connection connection to database
     * @param query      an SQL query string
     * @param keyName    a string containing the primary key for the table
     * @return           an object containing the typed value
     */
    public static int deleteWithIntKey(Connection connection, String query, int keyID) throws DBException {
        PreparedStatement statement;
        int result;
        
        try {
            statement = connection.prepareStatement(query);
            statement.setInt(1, keyID);
            result = statement.executeUpdate();
            statement.close();
            return result;
        } catch (SQLException e) {
            logger.error("Error deleting rows: " + query + " with key " + keyID);
            throw new DBException("Error deleting rows: " + query + " with key " + keyID, e);
        }
        
    }
    
    /**
     * Fetches a single row for the given query.  The query result must be a
     * single row, or an exception is thrown.
     *
     * @param connection connection to database
     * @param statement  a query statement that returns a single row
     * @return           cached row set containing the single row
     */
    /*
    public static CachedRowSet getSingleRow(Connection connection, PreparedStatement statement) throws DatabaseException {
        ResultSet results;
        CachedRowSetImpl rowSet;
        
        try {
            results = statement.executeQuery();
            if ( ! results.next() ) {
                logger.error("No result found for query: " + statement);
                throw new DatabaseException("No result for query: " + statement);
            }
            
            if ( results.next() ) {
                logger.error("More than one entry for query: " + statement);
                throw new DatabaseException("More than one entry for query: " + statement);
            }
            
            // The results.first() wouldn't work, so to workaround, execute the query again
            //results.first();
            results.close();
            results = statement.executeQuery();
            
            rowSet = new CachedRowSetImpl();
            rowSet.populate(results);
            rowSet.first();
            results.close();
            statement.close();
            return rowSet;
        } catch (SQLException e) {
            logger.error("Error fetching results for query: " + statement);
            throw new DatabaseException("Error fetching results for query: " + statement, e);
        }
        
    }
    */
    
    /**
     * Closes database connection opened for this DAO.
     */
    public static void closeConnection(Connection connection, String daoString) throws DBException {
        try {
            connection.close();
        } catch (SQLException e) {
            logger.error("Error closing database connection for " + daoString + " DAO");
            throw new DBException("Error closing database connection for " + daoString + " DAO", e);
        }
    }
    
}
