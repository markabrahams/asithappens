package nz.co.abrahams.asithappens.snmputil;

import org.snmp4j.security.AuthMD5;
import org.snmp4j.security.AuthSHA;
import org.snmp4j.smi.OID;

public enum USMAuthProtocol {

    NoAuth(1, null), MD5(2, AuthMD5.ID), SHA(3, AuthSHA.ID);

    int index;
    
    OID snmp4jID;

    USMAuthProtocol(int index, OID snmp4jID) {
        this.index = index;
        this.snmp4jID = snmp4jID;
    }

    public static USMAuthProtocol getAuthProtocol(int desiredIndex) throws SNMPException {
        for ( USMAuthProtocol protocol : USMAuthProtocol.values() ) {
            if ( protocol.index == desiredIndex ) {
                return protocol;
            }
        }
        throw new SNMPException("User security authentication protocol " + desiredIndex + " not found");
    }

    public int getIndex() {
        return index;
    }
    
    public OID getSnmp4jID() {
        return snmp4jID;
    }
    
}
