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

    private String keyPath;

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
        this.keyPath = keyPath;

        String path = StageUtils.SAVES_PATH + name;
        FileUtils.createDirectories(path + "/registers");
        FileUtils.createDirectories(path + "/backups");
        refreshFileNames();
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

        this.keyPath = keyPath;
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
    public void read(String savePath) {
        Element root = FileUtils.readXmlFile(savePath);

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(byte[] passwordHash) {
        this.passwordHash = passwordHash;
    }

    public byte[] getPasswordSalt() {
        return passwordSalt;
    }

    public void setPasswordSalt(byte[] passwordSalt) {
        this.passwordSalt = passwordSalt;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
    }

    public SaveTreeItem getRoot() {
        return root;
    }

    public void setRoot(SaveTreeItem root) {
        this.root = root;
    }

    public boolean isOpen() {
        return open;
    }

    public byte[] getCurrentFileNameIV() {
        return currentFileNameIV;
    }

    public void setCurrentFileNameIV(byte[] currentFileNameIV) {
        this.currentFileNameIV = currentFileNameIV;
    }

    public String getPrevExportDir() {
        return prevExportDir;
    }

    public void setPrevExportDir(String prevExportDir) {
        this.prevExportDir = prevExportDir;
    }

    public byte[] getDataSaltCipher() {
        return dataSaltCipher;
    }
    
    public void setDataSaltCipher(byte[] dataSaltCipher) {
        this.dataSaltCipher = dataSaltCipher;
    }

    public String getKeyPath() {
        return keyPath;
    }
}
