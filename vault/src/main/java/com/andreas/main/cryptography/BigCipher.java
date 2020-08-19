package com.andreas.main.cryptography;

import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.SecretKey;

import com.andreas.main.Utils;

import org.jdom2.Element;

public class BigCipher {
    private byte[] cipher;
    private byte[] keyCipher;
    private byte[] iv;

    public BigCipher() {}

    public BigCipher(byte[] cipher, byte[] keyCipher, byte[] iv) {
        this.cipher = cipher;
        this.keyCipher = keyCipher;
        this.iv = iv;
    }

    public void encrypt(String message, PublicKey pkey) {
        SecretKey skey = AES.gen(256);

        iv = AES.getRandom16BitIV();
        cipher = AES.encrypt(message, skey, iv);

        byte[] keyBytes = AES.getByteArrayFromSecretKey(skey);
        keyCipher = RSA.encrypt(keyBytes, pkey);
    }

    public String decrypt(PrivateKey pkey) {
        byte[] keyBytes = RSA.decryptToBytes(keyCipher, pkey);

        System.out.println(keyBytes.length);

        SecretKey skey = AES.getSecretKeyFromByteArray(keyBytes);

        return AES.decrypt(cipher, skey, iv);
    }

    public byte[] getCipher() {
        return cipher;
    }

    public byte[] getKeyCipher() {
        return keyCipher;
    }

    public byte[] getIV() {
        return iv;
    }

    public Element toXmlElement(String name) {
        Element root = new Element(name);

        Element cipher = new Element("cipher");
        cipher.setText(Utils.bytesToHex(this.cipher));
        root.addContent(cipher);

        Element keyCipher = new Element("keyCipher");
        keyCipher.setText(Utils.bytesToHex(this.keyCipher));
        root.addContent(keyCipher);

        Element iv = new Element("iv");
        iv.setText(Utils.bytesToHex(this.iv));
        root.addContent(iv);

        return root;
    }

    public void fromXmlElement(Element element) {
        cipher = Utils.hexToBytes(element.getChildTextNormalize("cipher"));
        keyCipher = Utils.hexToBytes(element.getChildTextNormalize("keyCipher"));
        iv = Utils.hexToBytes(element.getChildTextNormalize("iv"));
    }
}