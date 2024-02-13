package configurations;

import org.aeonbits.owner.ConfigFactory;

public class Config {

    private static BaseConfigurations baseConfigurations;

    public static BaseConfigurations baseConfigurations() {
        if (baseConfigurations == null) {
            baseConfigurations = ConfigFactory.create(BaseConfigurations.class);
        }
        return baseConfigurations;
    }
}
