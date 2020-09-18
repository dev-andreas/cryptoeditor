package com.andreas.main.cryptography;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * A SHA algorithm utility class.
 * @author Andreas Gerasimow.
 * @version 1.0.
 */
public class SHA {
    
    /**
     * This method creates a 256 bit SHA hash from <code>byte[]</code>.
     * @param data The data to hash.
     * @return The 256 bit hash.
     */
    public static byte[] sign(byte[] data) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.digest(data);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * This method creates a 256 bit SHA hash from <code>String</code>.
     * @param data The data to hash.
     * @return The 256 bit hash.
     */
    public static byte[] sign(String data) {
        return sign(data.getBytes(StandardCharsets.UTF_8));
    }   

    /**
     * This method creates a 256 bit SHA hash from <code>byte[]</code>.
     * @param data The data to hash.
     * @param salt The salt.
     * @return The 256 bit hash.
     */
    public static byte[] sign(byte[] data, byte[] salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            return md.digest(data);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * This method creates a 256 bit SHA hash from <code>String</code>.
     * @param data The data to hash.
     * @param salt The salt.
     * @return The 256 bit hash.
     */
    public static byte[] sign(String data, byte[] salt) {
        return sign(data.getBytes(StandardCharsets.UTF_8), salt);
    }

    /**
     * This method compares a 256 bit SHA hash with a <code>byte[]</code>.
     * @param data The data to compare.
     * @param signature The signature hash.
     * @return Returns <code>true</code> if data has the same hash.
     */
    public static boolean verify(byte[] data, byte[] signature) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            return Arrays.equals(md.digest(data), signature);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return false;
    }

     /**
     * This method compares a 256 bit SHA hash with a <code>String</code>.
     * @param data The data to compare.
     * @param signature The signature hash.
     * @return Returns <code>true</code> if data has the same hash.
     */
    public static boolean verify(String data, byte[] signature) {
        return verify(data.getBytes(StandardCharsets.UTF_8), signature);
    }

     /**
     * This method compares a 256 bit SHA hash with a <code>byte[]</code>.
     * @param data The data to compare.
     * @param salt The salt.
     * @param signature The signature hash.
     * @return Returns <code>true</code> if data has the same hash.
     */
    public static boolean verify(byte[] data, byte[] salt, byte[] signature) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt);
            return Arrays.equals(md.digest(data), signature);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * This method compares a 256 bit SHA hash with a <code>String</code>.
     * @param data The data to compare.
     * @param salt The salt.
     * @param signature The signature hash.
     * @return Returns <code>true</code> if data has the same hash.
     */
    public static boolean verify(String data, byte[] salt, byte[] signature) {
        return verify(data.getBytes(StandardCharsets.UTF_8), salt, signature);
    }
}