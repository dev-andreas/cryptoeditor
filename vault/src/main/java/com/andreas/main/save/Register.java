package com.andreas.main.save;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.crypto.SecretKey;

import com.andreas.main.FileUtils;
import com.andreas.main.cryptography.AES;
import com.andreas.main.cryptography.CryptoUtils;

import org.jdom2.Element;

public class Register {

    public static final String DEFAULT_FILE_TYPE = ".txt", DIRECTORY = ".directory";
    public static final String[] INTERN_FILE_TYPES = new String[] {
        DIRECTORY,
    };

    // CLASS

    private byte[] iv, nameCipher, fileTypeCipher, contentCipher, content;
    private String name, fileType;

    private boolean open;

    private String path;

    public Register(String path) {
        this.path = path;
        name = "";
        fileType = "";
        content = new byte[] {};

        open = false;
    }

    /**
     * This method opens the register by encrypting the cipher data.
     * @param key The corresponding AES key of the save.
     */
    public void open(SecretKey key) {
        name = AES.decrypt(nameCipher, key, iv);
        fileType = AES.decrypt(fileTypeCipher, key, iv);
        content = AES.decryptToBytes(contentCipher, key, iv);

        open = true;
    }

    /**
     * This method closes the register and resets all non cipher data.
     */
    public void close() {
        name = "";
        fileType = "";
        content = new byte[] {};

        open = false;
    }

    /**
     * This method saves the register data to a file.
     * @param key The corresponding AES key of the save.
     */
    public void save(SecretKey key) {
        iv = CryptoUtils.getRandom16Bytes();
        nameCipher = AES.encrypt(name, key, iv);
        fileTypeCipher = AES.encrypt(fileType, key, iv);
        contentCipher = AES.encrypt(content, key, iv);

        Element root = new Element("register");

        Element iv = new Element("iv");
        iv.setText(FileUtils.bytesToHex(this.iv));
        root.addContent(iv);

        Element nameCipher = new Element("nameCipher");
        nameCipher.setText(FileUtils.bytesToHex(this.nameCipher));
        root.addContent(nameCipher);

        Element fileTypeCipher = new Element("fileTypeCipher");
        fileTypeCipher.setText(FileUtils.bytesToHex(this.fileTypeCipher));
        root.addContent(fileTypeCipher);

        Element contentCipher = new Element("contentCipher");
        contentCipher.setText(FileUtils.bytesToHex(this.contentCipher));
        root.addContent(contentCipher);

        FileUtils.createXmlFile(path, root);
    }

    /**
     * This method reads the register file and initializes the cipher data.
     */
    public void read() {
        Element root = FileUtils.readXmlFile(path);

        String iv = root.getChildText("iv");
        this.iv = FileUtils.hexToBytes(iv);

        String nameCipher = root.getChildText("nameCipher");
        this.nameCipher = FileUtils.hexToBytes(nameCipher);

        String fileTypeCipher = root.getChildText("fileTypeCipher");
        this.fileTypeCipher = FileUtils.hexToBytes(fileTypeCipher);

        String contentCipher = root.getChildText("contentCipher");
        this.contentCipher = FileUtils.hexToBytes(contentCipher);
    }

    /**
     * This method initializes the plain text data by reading an external file.
     * @param filePath The file path to read.
     */
    public void readFromFile(String filePath) {
        Path path = Paths.get(filePath);

        if (Files.isDirectory(path))
            return;
        
        String[] nameAndType = FileUtils.splitFileNameAndType(path.getFileName().toString());
        setName(nameAndType[0]);
        setFileType(nameAndType[1]);
        setContent(FileUtils.readBinaryFile(path.toAbsolutePath().toString()));
    }

    // GETTERS AND SETTERS

    /**
     * @return This method returns the name of the register.
     */
    public String getName() {
        return name;
    }

    /**
     * This method sets the name of this register.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return This method returns the file type of this register.
     */
    public String getFileType() {
        return fileType;
    }

    /**
     * This method sets the file type of this register.
     */
    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    /**
     * @return This method returns the content of this register.
     */
    public byte[] getContent() {
        return content;
    }

    /**
     * This method sets the content of this register.
     */
    public void setContent(byte[] content) {
        this.content = content;
    }

    /**
     * @return  This method returns the IV of this register.
     */
    public byte[] getIV() {
        return iv;
    }

    /**
     * @return  This method returns <code>true</code> if the save is open
     */
    public boolean isOpen() {
        return open;
    }
}