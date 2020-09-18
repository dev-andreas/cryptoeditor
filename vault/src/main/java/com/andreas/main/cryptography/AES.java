package com.andreas.main.cryptography;

import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * An AES algorithm utility class.
 * @author Andreas Gerasimow.
 * @version 1.0.
 */
public class AES {

    /**
     * This method generates a secret AES key of any size.
     * @param keysize The keysize in bits of the secret key.
     * @return Returns a <code>javax.crypto.SecretKey</code>
     */
    public static SecretKey gen(int keysize) {
        KeyGenerator keyGen;
        try {
            keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(keysize, SecureRandom.getInstanceStrong());
            return keyGen.generateKey();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static SecretKey gen(String password, byte[] salt) {
        byte[] hash = SHA.sign(password, salt);
        return getSecretKeyFromByteArray(hash);
    }

    /**
     * This method encrypts <code>byte[]</code> data.
     * @param data The data to encrypt.
     * @param secret The secret key you want the data to encrypt with.
     * @param iv The initialisazion value. You can get a save iv in {@link #getRandom16BitIV()}.
     * @return Returns the encrypted cipher.
     */
    public static byte[] encrypt(byte[] data, SecretKey secret, byte[] iv) {
        Cipher cipher;
        try {
            cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, secret, new GCMParameterSpec(128, iv));
            byte[] encryptedText = cipher.doFinal(data);
            return encryptedText;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

     /**
     * This method encrypts <code>String</code> data.
     * @param data The data to encrypt.
     * @param secret The secret key for encrypting the data.
     * @param iv The initialisazion value. You can get a save iv in {@link #getRandom16BitIV()}.
     * @return Returns the encrypted cipher.
     */
    public static byte[] encrypt(String data, SecretKey secret, byte[] iv) {
        return encrypt(data.getBytes(StandardCharsets.UTF_8), secret, iv);
    }

    /**
     * This method decrypts a <code>byte[]</code> cipher to <code>byte[]</code>.
     * @param cText The cipher to decrypt.
     * @param secret The secret key for decrypting the cipher.
     * @param iv The initialisazion value of the key.
     * @return Returns the decrypted data as <code>byte[]</code>.
     */
    public static byte[] decryptToBytes(byte[] cText, SecretKey secret, byte[] iv) {
        Cipher cipher;
        try {
            cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, secret, new GCMParameterSpec(128, iv));
            byte[] bytes = cipher.doFinal(cText);
            return bytes;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * This method decrypts a <code>byte[]</code> cipher to <code>String</code>.
     * @param cText The cipher to decrypt.
     * @param secret The secret key for decrypting the cipher.
     * @param iv The initialisazion value of the key.
     * @return Returns the decrypted data as <code>String</code>.
     */
    public static String decrypt(byte[] cText, SecretKey secret, byte[] iv) {
        return new String(decryptToBytes(cText, secret, iv), StandardCharsets.UTF_8);
    }

    /**
     * This method converts a <code>byte[]</code> to <code>javax.crypto.SecretKey</code>.
     * @param byteArray The <code>byte[]</code> to convert.
     * @return returns the secet key <code>javax.crypto.SecretKey</code>.
     */
    public static SecretKey getSecretKeyFromByteArray(byte[] byteArray) {
		return new SecretKeySpec(byteArray, "AES");
    }
    
    /**
     * This method converts a <code>javax.crypto.SecretKey</code> to <code>byte[]</code>.
     * @param sk The secret key to convert.
     * @return returns the <code>byte[]</code> of the secret key.
     */
    public static byte[] getByteArrayFromSecretKey(SecretKey sk) {
        return sk.getEncoded();
    }
}