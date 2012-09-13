package nz.co.abrahams.asithappens.snmputil;

import org.snmp4j.security.PrivAES128;
import org.snmp4j.security.PrivAES192;
import org.snmp4j.security.PrivAES256;
import org.snmp4j.security.PrivDES;
import org.snmp4j.security.Priv3DES;
import org.snmp4j.smi.OID;

public enum USMPrivProtocol {

    NoPriv(1, null), DES(2, PrivDES.ID), TripleDES(3, Priv3DES.ID),
    AES128(4, PrivAES128.ID), AES192(5, PrivAES192.ID), AES256(6, PrivAES256.ID);

    int index;
    
    OID snmp4jID;

    USMPrivProtocol(int index, OID snmp4jID) {
        this.index = index;
        this.snmp4jID = snmp4jID;
    }

    public static USMPrivProtocol getPrivProtocol(int desiredIndex) {
        for ( USMPrivProtocol protocol : USMPrivProtocol.values() ) {
            if ( protocol.index == desiredIndex ) {
                return protocol;
            }
        }
        throw new IllegalArgumentException("User security privacy protocol " + desiredIndex + " not found");
    }

    public int getIndex() {
        return index;
    }
    
    public OID getSnmp4jID() {
        return snmp4jID;
    }
}
