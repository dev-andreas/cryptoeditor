package com.andreas.main.save;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.SecretKey;

import com.andreas.main.FileUtils;
import com.andreas.main.cryptography.AES;
import com.andreas.main.cryptography.CryptoUtils;
import com.andreas.main.cryptography.RSA;
import com.andreas.main.cryptography.SHA;
import com.andreas.main.stages.StageUtils;

import org.jdom2.Element;

/**
 * This class is used to manage registers.
 * @author Andreas Gerasimow.
 * @version 1.0.
 * @see {@link com.andreas.main.save.Register}
 */
public class Save {
    private String name;
    private String prevExportDir;
    private byte[] passwordSalt, dataSaltCipher;
    private byte[] passwordHash;
    private byte[] currentFileNameIV;

    private PublicKey publicKey;
    private SecretKey secretKey;

    private SaveTreeItem root;

    private boolean open;

    public Save() {
        root = new SaveTreeItem(this);

        this.secretKey = null;
    }

    /**
     * This method creates a completely new save. All data before will be deleted.
     * @param name The name for the new save.
     * @param password The password for the new save.
     * @param keyPair The key pair for the new save.
     */
    public void create(String name, String password, String keyPath) {
        KeyPair keyPair = RSA.readKeyPair(keyPath);
        this.name = name;
        prevExportDir = "";
        publicKey = keyPair.getPublic();
        passwordSalt = CryptoUtils.getRandom16Bytes();
        byte[] dataSalt = CryptoUtils.getRandomBytes(245);
        dataSaltCipher = RSA.encrypt(dataSalt, keyPair.getPublic());
        passwordHash = SHA.sign(password, passwordSalt);

        currentFileNameIV = CryptoUtils.getRandom16Bytes();

        this.secretKey = null;

        String path = StageUtils.SAVES_PATH + name + "/registers";
        FileUtils.createDirectories(path);
    }

    /**
     * This method unlocks all SaveTreeItems in this save.
     * @param keyPath The path where the corresponding key is saved
     */
    public void open(String keyPath, String password) {
        PrivateKey privateKey = RSA.readKeyPair(keyPath).getPrivate();
        byte[] dataSalt = RSA.decryptToBytes(dataSaltCipher, privateKey);

        secretKey = AES.gen(password, dataSalt);
        open = true;

        root.setName("registers");
        root.setType(Register.DIRECTORY);

        refreshFileNames();
    }

    /**
     * This method will lock every SaveTreeItem in this save.
     */
    public void close() {
        secretKey = null;
        open = false;

        root.forEachChild(e -> {
            e.unload();
        });
    }

    /**
     * This method opens a register.
     * @param register The register to open.
     */
    public void openRegister(Register register) {
        if (!open) 
            return;

        register.open(secretKey);
    }

     /**
     * This method saves a register.
     * @param register The register to save.
     */
    public void saveRegister(Register register) {
        if (!open) 
            return;

        register.save(secretKey);
    }

    /**
     * This method encrypts data with this save key.
     * @param data The data to encrypt.
     * @param iv The initialisazion value.
     * @return The encrypted data.
     */
    public byte[] encrypt(byte[] data) {
        if (!isOpen())
            return null;

        return AES.encrypt(data, secretKey, currentFileNameIV);
    }

    /**
     * This method decrypts data with this save key.
     * @param data The data to decrypt.
     * @param iv The initialisazion value.
     * @return The decrypted data.
     */
    public byte[] decrypt(byte[] data) {
        if (!isOpen())
            return null;

        return AES.decryptToBytes(data, secretKey, currentFileNameIV);
    }

    /**
     * This method initializes the save from the corresponding directory.
     * @see {@link #write(String)}, {@link #create(String, String, String)}.
     */
    public void read(String registerPath) {
        Element root = FileUtils.readXmlFile(registerPath);

        // name
        String name = root.getChildText("name");
        this.name = name;

        //password salt
        String passwordSalt = root.getChildText("passwordSalt");
        this.passwordSalt = FileUtils.hexToBytes(passwordSalt);

        // data salt cipher
        String dataSaltCipher = root.getChildText("dataSaltCipher");
        this.dataSaltCipher = FileUtils.hexToBytes(dataSaltCipher);

        // password hash
        String passwordHash = root.getChildText("passwordHash");
        this.passwordHash = FileUtils.hexToBytes(passwordHash);

        // current file name IV
        String currentFileNameIV = root.getChildText("currentFileNameIV");
        this.currentFileNameIV = FileUtils.hexToBytes(currentFileNameIV);

        // public key
        String publicKey = root.getChildText("publicKey");
        this.publicKey = RSA.getPublicKeyFromByteArray(FileUtils.hexToBytes(publicKey));

        // previous export directory
        String prevExportDir = root.getChildText("prevExportDir");
        this.prevExportDir = prevExportDir;
    }
    
    /**
     * This method writes this save to the corresponding directory.
     * @return Returns <code>true</code> if creation was successfull.
     * @see {@link #write(String)}.
     */
    public boolean write() {
        Element root = new Element("save");
        
        // name
        Element name = new Element("name");
        name.setText(this.name);
        root.addContent(name);

        // password salt
        Element passwordSalt = new Element("passwordSalt");
        passwordSalt.setText(FileUtils.bytesToHex(this.passwordSalt));
        root.addContent(passwordSalt);

        // data salt cipher
        Element dataSaltCipher = new Element("dataSaltCipher");
        dataSaltCipher.setText(FileUtils.bytesToHex(this.dataSaltCipher));
        root.addContent(dataSaltCipher);

        // password hash
        Element passwordHash = new Element("passwordHash");
        passwordHash.setText(FileUtils.bytesToHex(this.passwordHash));
        root.addContent(passwordHash);

        // current file name IV
        Element currentFileNameIV = new Element("currentFileNameIV");
        currentFileNameIV.setText(FileUtils.bytesToHex(this.currentFileNameIV));
        root.addContent(currentFileNameIV);

        // public key
        Element publicKey = new Element("publicKey");
        publicKey.setText(FileUtils.bytesToHex(RSA.getBytesFromPublicKey(this.publicKey)));
        root.addContent(publicKey);

        // previous export directory
        Element prevExportDir = new Element("prevExportDir");
        prevExportDir.setText(this.prevExportDir);
        root.addContent(prevExportDir);

        return FileUtils.createXmlFile(StageUtils.SAVES_PATH + this.name + "/saveData.xml", root);
    }

    public void refreshSecretKey() {
        //TODO Refresh every file of this save.
    }

    /**
     * This method refreshes the file name IV and every file / dir name in this save.
     */
    public void refreshFileNames() {

        if (!isOpen())
            return;

        byte[] nextFileNameIV = CryptoUtils.getRandom16Bytes();

        FileUtils.forEachFile(StageUtils.SAVES_PATH + getName() + "/registers", path -> {
            
            String nameCipher = path.getFileName().toString();
            byte[] nameCipherBytes = FileUtils.hexToBytes(nameCipher);
            String fileName = new String(decrypt(nameCipherBytes), StandardCharsets.UTF_8);
            
            byte[] newNameCipher = AES.encrypt(fileName, secretKey, nextFileNameIV);

            FileUtils.renamePath(path.toString(), FileUtils.bytesToHex(newNameCipher));
        });

        this.currentFileNameIV = nextFileNameIV;
        write();
    }

    // GETTERS AND SETTERS

    /**
     * @return This method returns the name of the save.
     */
    public String getName() {
        return name;
    }

    /**
     * This method sets the name of the save.
     * @param name New name of the save.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return This method returns the password hash of the save.
     */
    public byte[] getPasswordHash() {
        return passwordHash;
    }

    /**
     * @return This method returns the password salt.
     */
    public byte[] getPasswordSalt() {
        return passwordSalt;
    }

    /**
     * @return This method returns the public key of the save.
     */
    public PublicKey getPublicKey() {
        return publicKey;
    }

    /**
     * @return This method will return the RegisterTreeItem root.
     */
    public SaveTreeItem getRoot() {
        return root;
    }

    /**
     * @return This method returns <code>true</code> if the save is open.
     */
    public boolean isOpen() {
        return open;
    }

    /**
     * @return This method returns the current file name iv.
     */
    public byte[] getCurrentFileNameIV() {
        return currentFileNameIV;
    }

    /**
     * @return This method returns the previous export directory.
     */
    public String getPrevExportDir() {
        return prevExportDir;
    }

    /**
     * @return This method sets the previous export directory.
     */
    public void setPrevExportDir(String prevExportDir) {
        this.prevExportDir = prevExportDir;
    }
}