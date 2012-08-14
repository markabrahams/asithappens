/*
 * SNMPType.java
 *
 * Created on 28 August 2006, 22:12
 */

package nz.co.abrahams.asithappens.snmputil;

import org.snmp4j.smi.SMIConstants;
import org.snmp4j.smi.Variable;

/**
 *
 * @author mark
 */
public enum SNMPType {
    
    Counter32("Counter32") {
        boolean sameType(Variable var) {
            return var.getSyntax() == SMIConstants.SYNTAX_COUNTER32;
        }
    },
    Counter64("Counter64") {
        boolean sameType(Variable var) {
            return var.getSyntax() == SMIConstants.SYNTAX_COUNTER64;
        }
    },
    Gauge32("Gauge32") {
        boolean sameType(Variable var) {
            return var.getSyntax() == SMIConstants.SYNTAX_GAUGE32;
        }
    },
    Integer32("Integer32") {
        boolean sameType(Variable var) {
            return var.getSyntax() == SMIConstants.SYNTAX_INTEGER32;
        }
    },
    Unsigned32("Unsigned32") {
        boolean sameType(Variable var) {
            return var.getSyntax() == SMIConstants.SYNTAX_UNSIGNED_INTEGER32;
        }
    },
    OctetString("OctetString") {
        boolean sameType(Variable var) {
            return var.getSyntax() == SMIConstants.SYNTAX_OCTET_STRING;
        }
    },
    OID("OID") {
        boolean sameType(Variable var) {
            return var.getSyntax() == SMIConstants.SYNTAX_OBJECT_IDENTIFIER;
        }
    };
                            
    SNMPType(String label) {
        this.label = label;
    }
    
    public String label;
    
    abstract boolean sameType(Variable var);
    
    // Removed as this can be achieved by the type.valueOf(name) call
    /*
    static SNMPType getTypeFromLabel(String desiredLabel) {
        if ( desiredLabel == null )
            return null;
        for ( SNMPType type : SNMPType.values() ) {
            if ( desiredLabel.equals(type.label) )
                return type;
        }
        return null;
    }
     */
}
