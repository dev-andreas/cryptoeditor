package com.andreas.main.save;

import java.security.PrivateKey;

import com.andreas.main.cryptography.BigCipher;
import com.andreas.main.cryptography.RSA;

public class Register {
    private byte[] nameCipher;
    private BigCipher contentCipher;

    private String name;
    private String content;

    private Save save;

    public Register(Save save) {
        this.save = save;

        name = "";
        content = "";
        
        nameCipher = new byte[] {};
        contentCipher = new BigCipher();
    }

    public Register(Save save, String name, String content) {
        this.save = save;
        this.name = name;
        this.content = content;

        nameCipher = new byte[] {};
        contentCipher = new BigCipher();
    }

    public Register(Save save, byte[] nameCipher, BigCipher contentCipher) {
        this.save = save;
        this.nameCipher = nameCipher;

        this.contentCipher = contentCipher;
    }

    public void unlock(PrivateKey key) {
        this.name = RSA.decrypt(nameCipher, key);
        this.content = contentCipher.decrypt(key);
    }

    public void lock() {
        name = "";
        content = "";
    }

    public void save() {
        nameCipher = RSA.encrypt(name, save.getPublicKey());
        contentCipher.encrypt(content, save.getPublicKey());
    }

    public String getName() {
        return name;
    }

    public String getContent() {
        return content;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public byte[] getNameCipher() {
        return nameCipher;
    }

    public BigCipher getContentCipher() {
        return contentCipher;
    }
}