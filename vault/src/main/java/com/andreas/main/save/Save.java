package com.andreas.main.save;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;

import com.andreas.main.Utils;
import com.andreas.main.cryptography.BigCipher;
import com.andreas.main.cryptography.RSA;

import org.jdom2.Attribute;
import org.jdom2.Element;

public class Save {
    private String name;
    private int id;
    private PublicKey publicKey;
    private byte[] passwordCertificate;

    private ArrayList<Register> registers;

    private boolean open;

    public void initialize(KeyPair keyPair, String name, String password) {

        publicKey = keyPair.getPublic();

        this.name = name;
        this.passwordCertificate = RSA.sign(password, keyPair.getPrivate());

        this.registers = new ArrayList<Register>();
    }

    public void open(String keyPath) {
        PrivateKey privateKey = RSA.readKeyPair(keyPath).getPrivate();

        for (Register r : registers) {
            r.unlock(privateKey);
        }

        open = true;
    }

    public void close() {
        publicKey = null;

        for (Register r : registers) {
            r.lock();
        }

        open = false;
    }

    public void save(String filePath) {
        for (Register r : registers) {
            r.save();
        }

        writeToFile(filePath);
    }

    public void readFromFile(String path) {
        
        Element root = Utils.readXmlFile(path);

        name = root.getAttributeValue("name"); // read name
        id = Integer.parseInt(root.getAttributeValue("id")); // read id

        // read public key
        byte[] publicKeyArray = Utils.hexToBytes(root.getChildText("publicKey"));
        publicKey = RSA.getPublicKeyFromByteArray(publicKeyArray);

        passwordCertificate = Utils.hexToBytes(root.getChildText("password")); // read password
        
        Element regs = root.getChild("registers");

        registers = new ArrayList<>();
        // read registers
        for (Element r : regs.getChildren("register")) {

            byte[] nameCipher = Utils.hexToBytes(r.getChildText("name"));
            Element content = r.getChild("content");

            BigCipher contentCipher = new BigCipher();
            contentCipher.fromXmlElement(content);

            Register register = new Register(this, nameCipher, contentCipher);
            registers.add(register);
        }
    }

    public void writeToFile(String path) {


        Element root = new Element("save");

        root.setAttribute(new Attribute("name", name)); // write name
        root.setAttribute(new Attribute("id", "" + id)); // write id

        // write public key
        byte[] publicKeyBytes = RSA.getBytesFromPublicKey(publicKey);
        Element publicKey = new Element("publicKey");
        publicKey.setText(Utils.bytesToHex(publicKeyBytes));
        root.addContent(publicKey);

        // write password
        Element password = new Element("password");
        password.setText(Utils.bytesToHex(passwordCertificate));
        root.addContent(password);

        Element regs = new Element("registers");

        // write registers
        for (Register r : registers) {
            Element register = new Element("register");
            
            Element name = new Element("name");
            name.setText(Utils.bytesToHex(r.getNameCipher()));
            register.addContent(name);

            Element content = r.getContentCipher().toXmlElement("content");
            register.addContent(content);

            regs.addContent(register);
        }

        root.addContent(regs);

        Utils.createXmlFile(path, root);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte[] getPasswordCertificate() {
        return passwordCertificate;
    }

    public void setPassword(String password) {
        this.passwordCertificate = RSA.encrypt(password, publicKey);
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public ArrayList<Register> getRegisters() {
        return registers;
    }

    public boolean isOpen() {
        return open;
    }
}