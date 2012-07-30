/*
 * SNMPScopeException.java
 *
 * Created on 30 August, 05:09
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

package nz.co.abrahams.asithappens.snmputil;

/**
 * Exception indicating returned value from SNMP operation is different to the type specified.
 *
 * @author mark
 */
public class SNMPScopeException extends Exception {
    
    /**
     * Creates a new SNMPScopeException.
     *
     * @param message  description of exception
     * @param cause    root cause error/exception
     */
    public SNMPScopeException(String message, Throwable cause) {
        super(message, cause);
    }
    
    /**
     * Creates a new SNMPScopeException.
     *
     * @param message  description of exception
     */
    public SNMPScopeException(String message) {
        super(message);
    }
}
