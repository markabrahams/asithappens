/*
 * DatabaseException.java
 *
 * Created on 19 November 2005, 20:02
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

/**
 * Exception thrown when there is a problem accessing the database.  This
 * should only be thrown by database access object (DAO) classes.
 * 
 * This is an unchecked exception so that callers are not expected to handle
 * it.  The rationale for this is that there is that the database is a 
 * fundamental part of the application, and callers have little recourse
 * for alternative action if it's not available.
 *
 * @author mark
 */
public class DBException extends RuntimeException {
    
    /**
     * Creates a DatabaseException instance.
     *
     * @param message Exception message
     * @param cause   Root cause exception e.g. SQLException
     */
    public DBException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates a DatabaseException instance.
     *
     * @param message Exception message
     */    
    public DBException(String message) {
        super(message);
    }    
    
}
