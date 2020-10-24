package com.andreas.main.cryptography;

import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.SecretKey;

import com.andreas.main.FileUtils;

import org.jdom2.Element;

/**
 * This class enables you to encrypt a big amount of data with the RSA algorithm by combining RSA with AES.
 * @author Andreas Gerasimow.
 * @version 1.0.
 */
public class BigCipher {
    private byte[] cipher;
    private byte[] keyCipher;
    private byte[] iv;

    public BigCipher() {}

    /**
     * This constructor is used if you want to initialize a BigCipher with already encrypted data.
     * @param cipher The encrypted data.
     * @param keyCipher The AES key cipher. 
     * @param iv The AES initialisazion value.
     */
    public BigCipher(byte[] cipher, byte[] keyCipher, byte[] iv) {
        this.cipher = cipher;
        this.keyCipher = keyCipher;
        this.iv = iv;
    }

    /**
     * This method is for encrypting <code>byte[]</code> data.
     * @param bytes The <code>byte[]</code> to encrypt.
     * @param key The RSA public key to encrypt the data.
     */
    public void encrypt(byte[] bytes, PublicKey key) {
        SecretKey skey = AES.gen(256);

        iv = CryptoUtils.getRandom16Bytes();
        cipher = AES.encrypt(bytes, skey, iv);

        byte[] keyBytes = AES.getByteArrayFromSecretKey(skey);
        keyCipher = RSA.encrypt(keyBytes, key);
    }

    /**
     * This method is for encrypting <code>String</code> data.
     * @param bytes The <code>String</code> to encrypt.
     * @param key The RSA public key to encrypt the data.
     */
    public void encrypt(String data, PublicKey key) {
        encrypt(data.getBytes(StandardCharsets.UTF_8), key);
    }

    /**
     * This method is used to decrypt the BigCipher data to <code>byte[]</code> data.
     * @param key The RSA private key to decrypt the cipher.
     * @return Returns the decrypted data as <code>byte[]</code>.
     */
    public byte[] decryptToBytes(PrivateKey key) {
        byte[] keyBytes = RSA.decryptToBytes(keyCipher, key);

        SecretKey skey = AES.getSecretKeyFromByteArray(keyBytes);

        return AES.decryptToBytes(cipher, skey, iv);
    }

    /**
     * This method is used to decrypt the BigCipher data to <code>String</code> data.
     * @param key The RSA private key to decrypt the cipher.
     * @return Returns the decrypted data as <code>String</code>.
     */
    public String decrypt(PrivateKey key) {
        return new String(decryptToBytes(key), StandardCharsets.UTF_8);
    }

    /**
     * This method creates an .xml element from a BigCipher instance.
     * @param name Name of the element.
     * @return Returns the .xml element.
     */
    public Element toXmlElement(String name) {
        Element root = new Element(name);

        Element cipher = new Element("cipher");
        cipher.setText(FileUtils.bytesToHex(this.cipher));
        root.addContent(cipher);

        Element keyCipher = new Element("keyCipher");
        keyCipher.setText(FileUtils.bytesToHex(this.keyCipher));
        root.addContent(keyCipher);

        Element iv = new Element("iv");
        iv.setText(FileUtils.bytesToHex(this.iv));
        root.addContent(iv);

        return root;
    }

    /**
     * This method reads an .xml element and initializes the BigCipher instance with it.
     * @param element The .xml element.
     * @see {@link #toXmlElement(String)}.
     */
    public void fromXmlElement(Element element) {
        cipher = FileUtils.hexToBytes(element.getChildTextNormalize("cipher"));
        keyCipher = FileUtils.hexToBytes(element.getChildTextNormalize("keyCipher"));
        iv = FileUtils.hexToBytes(element.getChildTextNormalize("iv"));
    }

    // GETTERS AND SETTERS

    /**
     * @return This method returns the AES encrypted cipher.
     */
    public byte[] getCipher() {
        return cipher;
    }

    /**
     * @return This method returns the RSA encrypted key cipher.
     */
    public byte[] getKeyCipher() {
        return keyCipher;
    }

    /**
     * @return This method returns the initialisazion value (iv) for the AES algorithm.
     */
    public byte[] getIV() {
        return iv;
    }
}