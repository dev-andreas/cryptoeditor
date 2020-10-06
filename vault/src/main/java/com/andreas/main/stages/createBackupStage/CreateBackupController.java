package com.andreas.main.stages.createBackupStage;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Calendar;

import com.andreas.main.FileUtils;
import com.andreas.main.app.AppController;
import com.andreas.main.cryptography.RSA;
import com.andreas.main.save.Register;
import com.andreas.main.save.Save;
import com.andreas.main.save.SaveTreeItem;
import com.andreas.main.stages.StageUtils;
import com.andreas.main.stages.mainStage.scenes.saveScene.SaveScene;

import org.jdom2.Element;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;

public class CreateBackupController extends AppController {

    @FXML
    private Label saveName;

    @FXML
    private Label backupName;

    @FXML
    private TextField filePath;

    private Save save;
    private SaveScene saveScene;

    @Override
    public void init() {
        saveScene = ((CreateBackupStage)getScene().getStage()).getSaveScene();
        save = saveScene.getSave();

        Calendar calendar = Calendar.getInstance();

        saveName.setText(save.getName());
        backupName.setText(save.getName() + calendar.getTimeInMillis() + ".bcp");
        filePath.setText(StageUtils.SAVES_PATH + save.getName() + "/backups");

        File recordsDir = new File(filePath.getText());
        if (! recordsDir.exists()) {
            recordsDir.mkdirs();
        }
    }

    public void browsePressed() {
        DirectoryChooser fc = new DirectoryChooser();
        File file = fc.showDialog(null);
        filePath.setText(file.getAbsolutePath());
    }

    public void createPressed() {
        if (filePath.getText().isEmpty())
            return;

        if (!Files.exists(Paths.get(filePath.getText()))) {
            StageUtils.pushNotification("Path does not exist!");
            return;
        }

        
        getScene().getStage().applyLoadingScene(action -> {
            action.setText("Creating backup...");
            Element backup = backup();

            FileUtils.createXmlFile(filePath.getText() + "/" + backupName.getText(), backup);

            action.endNow(endingAction -> {
                getScene().getStage().close();
            });
        });
    }

    public void cancelPressed() {
        getScene().getStage().close();
    }

    private Element backup() {
        Element root = new Element("backup");
        
        // save name
        Element name = new Element("name");
        name.setText(save.getName());
        root.addContent(name);

        // password salt
        Element passwordSalt = new Element("passwordSalt");
        passwordSalt.setText(FileUtils.bytesToHex(save.getPasswordSalt()));
        root.addContent(passwordSalt);

        // data salt cipher
        Element dataSaltCipher = new Element("dataSaltCipher");
        dataSaltCipher.setText(FileUtils.bytesToHex(save.getDataSaltCipher()));
        root.addContent(dataSaltCipher);

        // password hash
        Element passwordHash = new Element("passwordHash");
        passwordHash.setText(FileUtils.bytesToHex(save.getPasswordHash()));
        root.addContent(passwordHash);

        // current file name iv
        Element currentFileNameIV = new Element("currentFileNameIV");
        currentFileNameIV.setText(FileUtils.bytesToHex(save.getCurrentFileNameIV()));
        root.addContent(currentFileNameIV);

        // public key
        Element publicKey = new Element("publicKey");
        byte[] keyBytes = RSA.getBytesFromPublicKey(save.getPublicKey());
        publicKey.setText(FileUtils.bytesToHex(keyBytes));
        root.addContent(publicKey);

        // previous export directory
        Element prevExportDir = new Element("prevExportDir");
        prevExportDir.setText(save.getPrevExportDir());
        root.addContent(prevExportDir);

        // registers
        Element registers = new Element("registers");
        backupRegister(save.getRoot(), registers);
        root.addContent(registers);

        return root;
    }

    public void backupRegister(SaveTreeItem item, Element registers) {
        item.forEachChild(child -> {
            Element register = new Element("register");

            String childPath = child.calculatePath();

            // path to register
            Element path = new Element("path");
            path.setText(childPath);
            register.addContent(path);

            // is directory
            Element isDir = new Element("isDir");
            register.addContent(isDir);

            if (child.getType().equals(Register.DIRECTORY)) {
                isDir.setText(Boolean.toString(true));
                backupRegister(child, registers);
            } else {
                isDir.setText(Boolean.toString(false));
                
                Register data = new Register(childPath);
                data.read();
                save.openRegister(data);

                // register iv
                Element iv = new Element("iv");
                iv.setText(FileUtils.bytesToHex(data.getIV()));
                register.addContent(iv);

                // register name cipher
                Element nameCipher = new Element("nameCipher");
                nameCipher.setText(FileUtils.bytesToHex(data.getNameCipher()));
                register.addContent(nameCipher);

                // register file type cipher
                Element fileTypeCipher = new Element("fileTypeCipher");
                fileTypeCipher.setText(FileUtils.bytesToHex(data.getFileTypeCipher()));
                register.addContent(fileTypeCipher);

                // register content cipher
                Element contentCipher = new Element("contentCipher");
                contentCipher.setText(FileUtils.bytesToHex(data.getContentCipher()));
                register.addContent(contentCipher);
            }

            registers.addContent(register);
        });
    }
}
