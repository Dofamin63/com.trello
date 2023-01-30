package configurations;

import org.aeonbits.owner.Config;

@Config.Sources
        ({
                "classpath:api.properties"
        })
public interface ApiConfigurations extends Config {

    @Config.Key("trello.url")
    String getTrelloUrl();

    @Config.Key("base.path")
    String getBasePath();
}
