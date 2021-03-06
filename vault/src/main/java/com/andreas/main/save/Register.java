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

    public static final String DEFAULT_FILE_TYPE = ".txt", DIRECTORY = ".directory", HTML = ".html", ACCOUNT = ".account";

    public static final String[] INTERN_FILE_TYPES = new String[] {
        DEFAULT_FILE_TYPE,
        HTML,
        DIRECTORY,
    };

    public static final String[] UNCHANGEABLE_FILE_TYPES = new String[] {
        DIRECTORY,
        ACCOUNT,
    };

    public static final String[] ACCEPTED_IMAGE_FILE_TYPES = new String[] {
        ".png",
        ".jpg",
        ".jpeg",
        ".bmp",
    };

    public static final String[] ACCEPTED_MEDIA_FILE_TYPES = new String[] {
        ".aif",
        ".aiff",
        ".fxm",
        ".flv",
        ".m3u8",
        ".mp3",
        ".mp4", 
        ".m4a", 
        ".m4v",
        ".wav",
    };

    public static final String[] ACCEPTED_HTML_FILE_TYPES = new String[] {
        ".html",
        ".htm",
    };

    public static final String PROPERTIES_SUFFIX = ".prp", CONTENT_SUFFIX = ".cnt";

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
     * This method opens the register without encrypting the chiper data.
     */
    public void open() {
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

        FileUtils.createXmlFile(path + PROPERTIES_SUFFIX, root);
        FileUtils.createBinaryFile(path + CONTENT_SUFFIX, contentCipher);
    }

    /**
     * This method reads the register file and initializes the cipher data.
     */
    public void read() {
        Element root = FileUtils.readXmlFile(path + PROPERTIES_SUFFIX);

        String iv = root.getChildText("iv");
        this.iv = FileUtils.hexToBytes(iv);

        String nameCipher = root.getChildText("nameCipher");
        this.nameCipher = FileUtils.hexToBytes(nameCipher);

        String fileTypeCipher = root.getChildText("fileTypeCipher");
        this.fileTypeCipher = FileUtils.hexToBytes(fileTypeCipher);

        this.contentCipher = FileUtils.readBinaryFile(path + CONTENT_SUFFIX);
    }

    /**
     * This method initializes the plain text data by reading an external file.
     * @param filePath The file path to read.
     */
    public void readFromFile(String filePath) {
        Path path = Paths.get(filePath);

        if (Files.isDirectory(path))
            return;
        
        String[] nameAndType = FileUtils.splitPrefixAndSuffix(path.getFileName().toString());
        setName(nameAndType[0]);
        setFileType(nameAndType[1]);
        setContent(FileUtils.readBinaryFile(path.toAbsolutePath().toString()));
    }

    // GETTERS AND SETTERS

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getNameCipher() {
        return nameCipher;
    }

    public void setNameCipher(byte[] nameCipher) {
        this.nameCipher = nameCipher;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public byte[] getFileTypeCipher() {
        return fileTypeCipher;
    }

    public void setFileTypeCipher(byte[] fileTypeCipher) {
        this.fileTypeCipher = fileTypeCipher;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public byte[] getContentCipher() {
        return contentCipher;
    }

    public void setContentCipher(byte[] contentCipher) {
        this.contentCipher = contentCipher;
    }

    public byte[] getIV() {
        return iv;
    }

    public void setIV(byte[] iv) {
        this.iv = iv;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isOpen() {
        return open;
    }
}