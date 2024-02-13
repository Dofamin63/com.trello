package configurations;

import org.aeonbits.owner.Config;

@Config.Sources
        ({
                "classpath:base.properties"
        })

public interface BaseConfigurations extends Config {

    @Config.Key("trello.url")
    String getTrelloUrl();

    @Config.Key("base.path")
    String getBasePath();

    @Config.Key("db.url")
    String getDbUrl();

    @Config.Key("db.login")
    String getDbLogin();

    @Config.Key("db.password")
    String getDbPassword();

    @Config.Key("db.encryptionKey")
    String getDbEncryptionKey();

    @Config.Key("db.userName")
    String getDbUserName();
}
