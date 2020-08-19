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

public class AES {

    public static SecretKey gen(int keysize) {
        KeyGenerator keyGen;
        try {
            keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(keysize, SecureRandom.getInstanceStrong());
            return keyGen.generateKey();
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        }

        return null;
    }

    public static byte[] encrypt(String data, SecretKey secret, byte[] iv) {
        Cipher cipher;
        try {
            cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, secret, new GCMParameterSpec(128, iv));
            byte[] encryptedText = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
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

    public static String decrypt(byte[] cText, SecretKey secret, byte[] iv) {
        Cipher cipher;
        try {
            cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.DECRYPT_MODE, secret, new GCMParameterSpec(128, iv));
            byte[] plainText = cipher.doFinal(cText);
            return new String(plainText, StandardCharsets.UTF_8);
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

    public static SecretKey getSecretKeyFromByteArray(byte[] byteArray) {
		return new SecretKeySpec(byteArray, "AES");
    }
    
    public static byte[] getByteArrayFromSecretKey(SecretKey sk) {
        return sk.getEncoded();
    }

    public static byte[] getRandomNonce(int numBytes) {
        byte[] nonce = new byte[numBytes];
        new SecureRandom().nextBytes(nonce);
        return nonce;
    }

    // 12 bytes IV
    public static byte[] getRandom16BitIV() {
        byte[] nonce = new byte[16];
        new SecureRandom().nextBytes(nonce);
        return nonce;
    }
    
    // 12 bytes IV
    public static byte[] getRandom12BitIV() {
            byte[] nonce = new byte[12];
            new SecureRandom().nextBytes(nonce);
            return nonce;
    }
}