package nz.co.abrahams.asithappens.snmputil;

import org.snmp4j.security.SecurityLevel;

public enum USMLevel {

    noAuthNoPriv(1, SecurityLevel.NOAUTH_NOPRIV),
    AuthNoPriv(2, SecurityLevel.AUTH_NOPRIV),
    AuthPriv(3, SecurityLevel.AUTH_PRIV);

    int index;
    
    int snmp4jID;

    USMLevel(int index, int snmp4jID) {
        this.index = index;
        this.snmp4jID = snmp4jID;
    }

    public static USMLevel getLevel(int desiredIndex) {
        for ( USMLevel level : USMLevel.values() ) {
            if ( level.index == desiredIndex ) {
                return level;
            }
        }
        throw new IllegalArgumentException("User security level " + desiredIndex + " not found");
    }

    public int getIndex() {
        return index;
    }
    
    public int getSnmp4jID() {
        return snmp4jID;
    }
    
}
