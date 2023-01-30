package configurations;

import org.aeonbits.owner.ConfigFactory;

public class Config {

    private static ApiConfigurations apiConfigurations;
    private static DbConfigurations dBConfigurations;

    public static ApiConfigurations apiConfigurations() {
        if (apiConfigurations == null) {
            apiConfigurations = ConfigFactory.create(ApiConfigurations.class);
        }
        return apiConfigurations;
    }

    public static DbConfigurations getDbConfigurations() {
        if (dBConfigurations == null) {
            dBConfigurations = ConfigFactory.create(DbConfigurations.class);
        }
        return dBConfigurations;
    }
}
