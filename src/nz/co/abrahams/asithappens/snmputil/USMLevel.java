package nz.co.abrahams.asithappens.snmputil;

public enum USMLevel {

    noAuthNoPriv(1), AuthNoPriv(2), AuthPriv(3);

    int index;

    USMLevel(int index) {
        this.index = index;
    }

    public static USMLevel getLevel(int desiredIndex) throws SNMPException {
        for ( USMLevel level : USMLevel.values() ) {
            if ( level.index == desiredIndex ) {
                return level;
            }
        }
        throw new SNMPException("User security level " + desiredIndex + " not found");
    }
}
