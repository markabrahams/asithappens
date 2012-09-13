package nz.co.abrahams.asithappens.snmputil;

/**
 *
 * @author mark
 */
public class USMUser {
    
    public static final int MINIMUM_KEY_LENGTH = 8;
    
    private String userName;
    private USMLevel userLevel;
    private USMAuthProtocol userAuthProtocol;
    private String userAuthKey;
    private USMPrivProtocol userPrivProtocol;
    private String userPrivKey;

    public USMUser(
            String userName,
            USMLevel userLevel,
            USMAuthProtocol userAuthProtocol,
            String userAuthKey,
            USMPrivProtocol userPrivProtocol,
            String userPrivKey
    ) {
        this.userName = userName;
        this.userLevel = userLevel;
        this.userAuthProtocol = userAuthProtocol;
        this.userAuthKey = userAuthKey;
        this.userPrivProtocol = userPrivProtocol;
        this.userPrivKey = userPrivKey;
    }

    public String getUserAuthKey() {
        return userAuthKey;
    }

    public USMAuthProtocol getUserAuthProtocol() {
        return userAuthProtocol;
    }

    public USMLevel getUserLevel() {
        return userLevel;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserPrivKey() {
        return userPrivKey;
    }

    public USMPrivProtocol getUserPrivProtocol() {
        return userPrivProtocol;
    }
    
    
}
