package nz.co.abrahams.asithappens.snmputil;

public enum USMAuthProtocol {

    NoAuth(1), MD5(2), SHA(3);

    int index;

    USMAuthProtocol(int index) {
        this.index = index;
    }

    public static USMAuthProtocol getAuthProtocol(int desiredIndex) throws SNMPException {
        for ( USMAuthProtocol protocol : USMAuthProtocol.values() ) {
            if ( protocol.index == desiredIndex ) {
                return protocol;
            }
        }
        throw new SNMPException("User security authentication protocol " + desiredIndex + " not found");
    }
}
