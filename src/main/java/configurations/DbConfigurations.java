package configurations;

import org.aeonbits.owner.Config;

@Config.Sources
        ({
                "classpath:db.properties"
        })
public interface DbConfigurations extends Config {

    @Config.Key("db.url")
    String getDbUrl();

    @Config.Key("db.login")
    String getDbLogin();

    @Config.Key("db.password")
    String getDbPassword();

    @Config.Key("db.encryptionKey")
    String getEncryptionKey();

    @Config.Key("db.userName")
    String getUserName();
}
