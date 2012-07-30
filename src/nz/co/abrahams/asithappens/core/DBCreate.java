/*
 * DatabaseCreate.java
 *
 * Created on 3 April 2006, 23:35
 */

package nz.co.abrahams.asithappens.core;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Vector;
import java.sql.Connection;
import java.sql.Statement;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * Creates the database for storing AsItHappens collection sessions.
 *
 * @author mark
 */
public class DBCreate {
    
    public static final String SQL_FILE = "sql/db-create-h2.sql";
    
    /** Logging provider */
    private static Logger logger = Logger.getLogger(DBCreate.class);
    
    /** Creates a new instance of DatabaseCreate */
    private DBCreate() {
    }

    public static void create(String user, String password) throws DBException {
        Connection connection;

        connection = openDatabaseConnection(user, password);
        executeScript(connection);
        closeDatabaseConnection(connection);
    }

    public static void delete() throws IOException {
        // write deletion of all database.filepath + "*" files
        File directory;
        File[] dbFiles;

        directory = new File(Configuration.getProperty("database.filepath")).getParentFile();
        dbFiles = directory.listFiles();
        for ( int i = 0 ; i < dbFiles.length ; i++ ) {
            System.out.println(dbFiles[i].getAbsolutePath());
            dbFiles[i].delete();
        }
    }

    /** Opens a new database connection. */
    protected static Connection openDatabaseConnection(String user, String password) throws DBException {
        String connectionString;
        
        connectionString = getCreationURL();
        logger.debug("Opening database handle using connection string: " + connectionString);
        
        // MySQL connection
        if ( Configuration.getProperty("database.type").equals("mysql") ) {
            return DBUtil.getConnection(connectionString, user, password);
        }
        // H2 connection
        else if ( Configuration.getProperty("database.type").equals("h2") ) {
            return DBUtil.getConnection(connectionString);
        }
        // Unknown connection type
        else {
            throw new DBException("Database type  \"" +
                    Configuration.getProperty("database.type") +
                    "\" not supported");
        }
        
    }
    
    protected static String getCreationURL() throws DBException {
        
        // MySQL connection
        if ( Configuration.getProperty("database.type").equals("mysql") ) {
            return "jdbc:" +
                    Configuration.getProperty("database.type") + "://" +
                    Configuration.getProperty("database.server") + "/";
            
        }
        // H2 connection
        else {
            //if ( Configuration.getProperty("database.type").equals("h2") ) {
            //return DBUtil.getConnectionURL(true);
            return "jdbc:" +
                    Configuration.getProperty("database.type") + ":" +
                    Configuration.getProperty("database.filepath");
        }
        
    }
    
    protected static void executeScript(Connection connection) {
        Object[] statements;
        Statement statement;
        int rowsAffected;
        
        try {
            statements = getStatements();
            statement = connection.createStatement();
            
            for ( int i = 0; i < statements.length; i++ ) {
                if ( statements[i] != null && ((String)statements[i]).length() > 0 && ! ((String)statements[i]).substring(0,2).equals("--") ) {
                    logger.info(statements[i]);
                    rowsAffected = statement.executeUpdate((String)statements[i]);
                    //if (rowsAffected == 1)
                    //    logger.debug("OK");
                }
            }
        } catch(Exception e) {
            logger.error("Error executing SQL script: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    protected static Object[] getStatements() throws FileNotFoundException, IOException {
        BufferedReader reader;
        String line;
        Vector<String> statements;
        
        reader = new BufferedReader(new FileReader(SQL_FILE));
        statements = new Vector<String>();
        while ( (line = reader.readLine()) != null ) {
            if ( line != null && line.length() > 1 && ! line.substring(0,2).equals("--") ) {
                
                if ( statements.size() > 0 && statements.lastElement().charAt(statements.lastElement().length() - 1) != ';' ) {
                    statements.setElementAt(statements.lastElement().concat(line), statements.size() - 1);
                } else {
                    statements.add(line);
                }
            }
        }
        
        return statements.toArray();
    }
    
    protected static void closeDatabaseConnection(Connection connection){
        try {
            if ( connection != null )
                connection.close();
            connection = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /** Runs the database creation */
    public static void main(String args[]) {
        
        try {
            PropertyConfigurator.configure(Configuration.DEFAULT_LOG4J_PROPERTIES_FILE);
            Configuration.initialize();
            DBCreate.create("root", "");
        } catch (FileNotFoundException e) {
            System.err.println("Problem locating configuration files");
        } catch (IOException e) {
            System.err.println("Problem opening configuration files");
        } catch (DBException e) {
            System.err.println("Cannot create database");
            e.printStackTrace();
        }
    }
}
