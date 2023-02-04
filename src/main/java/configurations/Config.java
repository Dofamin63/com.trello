package configurations;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {

    public static String getValue(String value) {
        Properties properties = new Properties();
        try (FileInputStream fileInputStream = new FileInputStream("src/test/resources/base.properties")) {
            properties.load(fileInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties.getProperty(value);
    }
}
