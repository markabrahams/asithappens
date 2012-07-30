package nz.co.abrahams.asithappens.snmputil;

/**
 *
 * @author mark
 */
public class USMUser {

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
}
