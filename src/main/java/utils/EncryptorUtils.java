package utils;

import configurations.Config;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

public class EncryptorUtils {

   private final static String ENCRYPTION_KYE = Config.getValue("db.encryptionKey");

    public static String encrypt(String value) {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setPassword(ENCRYPTION_KYE);
        return encryptor.decrypt(value);
    }
}
