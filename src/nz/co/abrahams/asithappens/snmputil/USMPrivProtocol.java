package nz.co.abrahams.asithappens.snmputil;

public enum USMPrivProtocol {

    NoPriv(1), DES(2), TripleDES(3), AES128(4), AES192(5), AES256(6);

    int index;

    USMPrivProtocol(int index) {
        this.index = index;
    }

    public static USMPrivProtocol getPrivProtocol(int desiredIndex) throws SNMPException {
        for ( USMPrivProtocol protocol : USMPrivProtocol.values() ) {
            if ( protocol.index == desiredIndex ) {
                return protocol;
            }
        }
        throw new SNMPException("User security privacy protocol " + desiredIndex + " not found");
    }
}
