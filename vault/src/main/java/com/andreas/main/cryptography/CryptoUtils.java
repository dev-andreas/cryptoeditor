package com.andreas.main.cryptography;

import java.security.SecureRandom;

/**
 * This is a util class for cryptography.
 * @author Andreas Gerasimow.
 * @version 1.0.
 */
public class CryptoUtils {
    public static final String ALL_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789~`!@#$%^&*()-_=+[{]}\\|;:\'\",<.>/?";
    public static final String UPPER_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String LOWER_CHARACTERS = "abcdefghijklmnopqrstuvwxyz";
    public static final String NUMBER_CHARACTERS = "0123456789";
    public static final String SPECIAL_CHARACTERS = "~`!@#$%^&*()-_=+[{]}\\|;:\'\",<.>/?";

    /**
     * This method generates a secure password of any length for cryptography.
     * @param length Length of password.
     * @param charset Charset for password. All characters the password should be made of.
     * @return Returns the secure password <code>String</code>.
     * @see {@link #ALL_CHARACTERS},  {@link #UPPER_CHARACTERS},  {@link #LOWER_CHARACTERS},  {@link #NUMBER_CHARACTERS},  {@link #SPECIAL_CHARACTERS}.
     */
    public static String GenerateSecurePassword(int length, String charset) {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder("");
        for (int i = 0; i < length; i++) {
            int charAt = random.nextInt(charset.length());
            sb.append(charset.charAt(charAt));
        }

        return sb.toString();
    }

    /**
     * This method creates a random byte array of any size for an IV / Salt.
     * @param bytesAmt The IV size in bytes.
     * @return Returns the IV.
     */
    public static byte[] getRandomBytes(int bytesAmt) {
        byte[] iv = new byte[bytesAmt];
        new SecureRandom().nextBytes(iv);
        return iv;
    }

    /**
     * This method creates a random 16 byte array for an IV / Salt.
     * @return Returns the random bytes.
     */
    public static byte[] getRandom16Bytes() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return iv;
    }
    
    /**
     * This method creates a random 12 byte array for an IV / Salt.
     * @return Returns the random bytes.
     */
    public static byte[] getRandom12Bytes() {
            byte[] iv = new byte[12];
            new SecureRandom().nextBytes(iv);
            return iv;
    }
}