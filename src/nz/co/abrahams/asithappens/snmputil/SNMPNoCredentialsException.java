/*
 * SNMPNoCredentialsException.java
 *
 * Created on 31 August 2012, 04:24
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
 * Exception when performing an SNMP operation.
 *
 * @author mark
 */
public class SNMPNoCredentialsException extends SNMPException {
    
    /**
     * Creates a new SNMPNoCredentialsException.
     *
     * @param message  description of exception
     * @param cause    root cause error/exception
     */
    public SNMPNoCredentialsException(String message, Throwable cause) {
        super(message, cause);
    }
    
    /**
     * Creates a new SNMPNoCredentialsException.
     *
     * @param message  description of exception
     */
    public SNMPNoCredentialsException(String message) {
        super(message);
    }
}
