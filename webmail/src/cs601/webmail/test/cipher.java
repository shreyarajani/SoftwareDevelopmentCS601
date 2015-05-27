package cs601.webmail.test;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

public class cipher {

    private static final byte[] keyValue =
            new byte[] { '4', 'h', 'g', 's', '6', 'j', 't',
                    '1', 'w', 'h', 'r','a', 'p', 'f', 'e', 'd' };

    public static String encrypt(String Data) {

        String encryptedValue=null;
        try {
            Key key = generateKey();
            Cipher c = Cipher.getInstance("AES");

        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encVal = c.doFinal(Data.getBytes());
        encryptedValue = new BASE64Encoder().encode(encVal);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encryptedValue;
    }

    public static String decrypt(String encryptedData) {
        String decryptedValue = null;
        try {
        Key key = generateKey();
        Cipher c = Cipher.getInstance("AES");
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedData);
        byte[] decValue = c.doFinal(decordedValue);
            decryptedValue = new String(decValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decryptedValue;
    }
    private static Key generateKey() throws Exception {
        Key key = new SecretKeySpec(keyValue, "AES");
        return key;
    }

    public static void main(String[] args) {
        cipher c = new cipher();
        String s = "Shreya";
        String o = c.encrypt(s);
        System.out.println(o);
        String d = decrypt("4sN/KBQKjoy3HP/ORmZOoQ==");
        System.out.println(d);
    }

}