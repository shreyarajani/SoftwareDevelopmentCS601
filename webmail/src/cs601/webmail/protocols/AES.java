package cs601.webmail.protocols;

/**
 * This code is taken from:
 * http://www.code2learn.com/2011/06/encryption-and-decryption-of-data-using.html
 */

import org.apache.log4j.Logger;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

public class AES { //AES
    static final Logger logger = Logger.getLogger(AES.class);

    public static String encrypt(String Data) {
        String encryptedValue=null;
        try {
            Key key = generateKey();
            Cipher c = Cipher.getInstance("AES");

            c.init(Cipher.ENCRYPT_MODE, key);
            byte[] encVal = c.doFinal(Data.getBytes());
            encryptedValue = new BASE64Encoder().encode(encVal);
        } catch (Exception e) {
            logger.error(e);
        }
        return encryptedValue;
    }

    public static String decrypt(String encryptedData) {
        String decryptedValue = null;
        try {
            Key key = generateKey();
            Cipher c = Cipher.getInstance("AES");
            c.init(Cipher.DECRYPT_MODE, key);
            byte[] decodeBuffer = new BASE64Decoder().decodeBuffer(encryptedData);
            byte[] decValue = c.doFinal(decodeBuffer);
            decryptedValue = new String(decValue);
        } catch (Exception e) {
            logger.error(e);
        }
        return decryptedValue;
    }
    private static Key generateKey() throws Exception {
        byte[] keyValue =
                new byte[] { '4', 'h', 'g', 's', '6', 'j', 't',
                        '1', 'w', 'h', 'r','a', 'p', 'f', 'e', 'd' };
        Key key = new SecretKeySpec(keyValue, "AES");
        return key;
    }
}