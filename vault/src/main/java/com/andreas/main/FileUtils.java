package com.andreas.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

/**
 * @author Andreas Gerasimow.
 * @version 1.0.
 */
public class FileUtils {
    /**
     * This method converts byte array to hexadecimal string.
     * @param byteArray The byte array to convert.
     * @return Returns the converted hexadecimal string.
     */
    public static String bytesToHex(byte[] byteArray) {
        String string = "";

        for (int i = 0; i < byteArray.length; i++) {
            string += "" + String.format("%02x", byteArray[i] + 128);
        }

        return string;
    }

    /**
     * This method converts a hexadecimal string to byte array.
     * @param string The hexadecimal string to convert.
     * @return Returns the converted byte array.
     */
    public static byte[] hexToBytes(String string) {

        String[] stringArray = new String[string.length() / 2];

        for (int i = 0; i < stringArray.length; i++) {
            stringArray[i] = string.substring(i * 2, i * 2 + 2);
        }

        byte[] byteArray = new byte[stringArray.length];

        for (int i = 0; i < stringArray.length; i++) {

            int b = Integer.decode("0x" + stringArray[i]) - 128;
            byteArray[i] = (byte) b;
        }

        return byteArray;
    }

    /**
     * This method automatically creates a xml file.
     * @param path Path where the .xml file should be located.
     * @param root Root element of .xml file.
     * @return Returns <code>true</code> if the creation was successfull.
     */
    public static boolean createXmlFile(String path, Element root) {

        root.detach();

        try {
            Document doc = new Document(root);

            BufferedWriter writer = Files.newBufferedWriter(Paths.get(path)); 

            XMLOutputter xmlOutput = new XMLOutputter();
            xmlOutput.setFormat(Format.getPrettyFormat());
            xmlOutput.output(doc, writer);

            writer.close();

            return true;

        } catch (IOException io) {
            io.printStackTrace();
        }

        return false;
    }

    /**
     * This method reads an .xml file automaticaly.
     * @param path Path where the .xml file is located.
     * @return Returns the root element.
     */
    public static Element readXmlFile(String path) {

        Path xmlPath = Paths.get(path);
        if (Files.notExists(xmlPath))
            return null;

        SAXBuilder builder = new SAXBuilder();

        try {
            Document document = (Document) builder.build(path);
            return document.getRootElement();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JDOMException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * This method creates a text based file of any kind automatically.
     * @param path Path where the file should be located.
     * @param content The content of your file in <code>String</code>.
     * @return Returns <code>true</code> if the creation was successfull.
     */
    public static boolean createFile(String path, String content) {
        try {
            BufferedWriter writer = Files.newBufferedWriter(Paths.get(path), StandardCharsets.UTF_8);
            writer.write(content);
            writer.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * This method creates a binary file of any kind automatically.
     * @param path Path where the file should be located.
     * @param bytes The content of your file in <code>byte[]</code>.
     * @return Returns <code>true</code> if the creation was successfull.
     */
    public static boolean createBinaryFile(String path, byte[] bytes) {
        try {
            OutputStream writer = Files.newOutputStream(Paths.get(path));
            writer.write(bytes);
            writer.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * This method reads a text based file of any kind automatically.
     * @param path Path where the file is located.
     * @return The content of the file as <code>String</code>
     */
    public static String readFile(String path) {

        String text = "";

        try {

            BufferedReader reader = Files.newBufferedReader(Paths.get(path), StandardCharsets.UTF_8);

            for (String line = reader.readLine(); line != null; line = reader.readLine())
                text += line + "\n";

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return text;
    }

    /**
     * This method reads a binary file of any kind automatically.
     * @param path Path where the file is located.
     * @return The content of the file as <code>byte[]</code>.
     */
    public static byte[] readBinaryFile(String path) {

        byte[] bytes = null;
        try {
            InputStream reader = Files.newInputStream(Paths.get(path));
            bytes = reader.readAllBytes();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bytes;
    }

    /**
     * This method is for creating new directories.
     * @param dirPath The directory path.
     * @return Returns <code>true</code> if creation was successfull.
     */
    public static boolean createDirectories(String dirPath) {
        Path path = Paths.get(dirPath);
        try {
            Files.createDirectories(path);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * This method is for deleting files.
     * @param path Path to file.
     * @return Returns <code>true</code> if deleted successfully.
     */
    public static boolean deleteFile(String path) {
        try {
            return Files.deleteIfExists(Paths.get(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * This method is for deleting directories.
     * @param path Path to directory.
     * @return Returns <code>true</code> if deleted successfully.
     */
    public static boolean deleteDirectory(String dirPath) {
        Path path = Paths.get(dirPath);
        try {
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    if (exc != null) {
                        throw exc;
                    }
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }
            });
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * This method renames a specific path or directory.
     * @param oldPath Path to rename.
     * @param newPath New name.
     * @return Returns <code>true</code> if renaming was successfull.
     */
    public static boolean renamePath(String path, String newName) {
        Path oldPath = Paths.get(path); 
        try {
            Files.move(oldPath, oldPath.resolveSibling(newName), StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * This method checks if a <code>byte[]</code> seems binary in UTF-8 encoding.
     * @param bytes The <code>byte[]</code> to check.
     * @return Returns <code>true</code> if the array seems to be binary. 
     */
    public static boolean seemsBinary(byte[] bytes) {

        int bin = 0;
        float binPercentage = 0.05f;

        for (byte b : bytes) {
            char c = (char)b;
            if (!Character.isWhitespace(c) && Character.isISOControl(c))
                bin++;
        }

        if (bin > bytes.length * binPercentage)
            return true;
        return false;
    }

    /**
     * This method checks if a <code>String</code> seems binary in UTF-8 encoding.
     * @param text The <code>String</code> to check.
     * @return Returns <code>true</code> if the <code>String</code> seems to be binary. 
     */
    public static boolean seemsBinary(String text) {
        return seemsBinary(text.getBytes(StandardCharsets.UTF_8));
    }


    /**
     * This method splits the full name of a file in prefix and suffix.
     * @param fullName The full name to split.
     * @return Returns a <code>String[]</code> with index 0 as prefix and index 1 as suffix.
     */
    public static String[] splitPrefixAndSuffix(String fullName) {
        String[] nameSplits = fullName.split("\\.");
        String fileName = nameSplits[0];
        for (int i = 1; i < nameSplits.length-1; i++)
            fileName += "."+nameSplits[i];
        
        String name = fileName;
        String type = "." + nameSplits[nameSplits.length - 1].toLowerCase();
  
        return new String[] { name, type };
    }

    /**
     * This method iterates over every file inside a directory.
     * @param root The root directory.
     * @param action The action to execute.
     */
    public static void forEachFile(String root, Consumer<Path> action) {
        Objects.requireNonNull(action);
        try {
            if (!Files.isDirectory(Paths.get(root))) 
                return;
            Stream<Path> list = Files.list(Paths.get(root));
            list.forEach(e -> {
                forEachFile(e.toString(), action);
                action.accept(e);
            });
            list.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    /**
     * This method creates a .zip file.
     * @param targetPath The files which should be compressed.
     * @param destinationFilePath The path where the .zip file should be created.
     * @param password The password for AES256 encryption. Leave empty if no encryption should be used.
     */
    public static void zip(String targetPath, String destinationFilePath, String password) {
        try {
            ZipParameters parameters = new ZipParameters();
            parameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
            parameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);

            if (!password.isEmpty()) {
                parameters.setEncryptFiles(true);
                parameters.setEncryptionMethod(Zip4jConstants.ENC_METHOD_AES);
                parameters.setAesKeyStrength(Zip4jConstants.AES_STRENGTH_256);
                parameters.setPassword(password);
            }

            ZipFile zipFile = new ZipFile(destinationFilePath);

            File targetFile = new File(targetPath);
            if (targetFile.isFile()) {
                zipFile.addFile(targetFile, parameters);
            } else if (targetFile.isDirectory()) {
                zipFile.addFolder(targetFile, parameters);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method unzips a .zip file.
     * @param targetZipFilePath The .zip file to unzip.
     * @param destinationFolderPath The folder where the .zip file should be unzipped.
     * @param password The password used to encrypt the .zip file. Leave empty if no password was used.
     */
    public static void unzip(String targetZipFilePath, String destinationFolderPath, String password) {
        try {
            ZipFile zipFile = new ZipFile(targetZipFilePath);
            if (zipFile.isEncrypted()) {
                zipFile.setPassword(password);
            }
            zipFile.extractAll(destinationFolderPath);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method copies a file from one directory to another.
     * @param target The file path to copy.
     * @param newDir The file path for the copied file.
     * @return Returns <code>true</code> if copy was successfull.
     */
    public static boolean copyFile(String target, String newDir) {
        Path targetPath = Paths.get(target);
        Path newDirPath = Paths.get(newDir);
        byte[] bytes = readBinaryFile(target);
        return createBinaryFile(newDirPath.toString() + "/" + targetPath.getFileName().toString(), bytes);
    }
}
