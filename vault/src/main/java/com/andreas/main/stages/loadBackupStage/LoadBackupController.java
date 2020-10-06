package com.andreas.main.stages.loadBackupStage;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.andreas.main.FileUtils;
import com.andreas.main.app.AppController;
import com.andreas.main.cryptography.SHA;
import com.andreas.main.save.Register;
import com.andreas.main.stages.StageUtils;
import com.andreas.main.stages.mainStage.scenes.saveScene.SaveScene;

import org.jdom2.Element;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.FileChooser;
import javafx.stage.Modality;

public class LoadBackupController extends AppController {

    @FXML 
    public Label backupName;

    @FXML 
    public TextField filePath;

    @FXML
    public PasswordField password;

    private SaveScene saveScene;

    @Override 
    public void init() {
        saveScene = ((LoadBackupStage) getScene().getStage()).getSaveScene();
    }

    public void browsePressed() {
        File recordsDir = new File(StageUtils.SAVES_PATH + saveScene.getSave().getName() + "/backups");
        if (! recordsDir.exists()) {
            recordsDir.mkdirs();
        }

        FileChooser fc = new FileChooser();
        fc.setInitialDirectory(recordsDir);
        File file = fc.showOpenDialog(null);
        filePath.setText(file.getAbsolutePath());
        backupName.setText(file.getName());
    }

    public void loadPressed() {
        if (!SHA.verify(password.getText(), saveScene.getSave().getPasswordSalt(), saveScene.getSave().getPasswordHash())) {
            StageUtils.pushNotification("Password is incorrect!");
            return;
        }

        ButtonType loadBackup = new ButtonType("Load backup");
        ButtonType cancel = new ButtonType("Cancel");
        Alert alert = new Alert(AlertType.CONFIRMATION, "Are you sure you want to load this backup?\nAll data which was edited after this backup will be deleted!", 
        loadBackup, cancel);

        alert.initModality(Modality.WINDOW_MODAL);

        alert.showAndWait();

        if (alert.getResult() == loadBackup) {
            getScene().getStage().applyLoadingScene(action -> {
                action.setText("Loading backup...");
                saveScene.getSave().read(filePath.getText());
                loadBackupRegisters();
                saveScene.getSave().open(saveScene.getSave().getKeyPath(), password.getText());
                saveScene.getSave().write();
                saveScene.loadRegisters();
                getScene().getStage().close();
            });
        }

        if (alert.getResult() == cancel) {
            getScene().getStage().close();
        }
    }

    public void cancelPressed() {
        getScene().getStage().close();
    }

    public void loadBackupRegisters() {
        Element root = FileUtils.readXmlFile(filePath.getText());
    

        // registers
        Element registers = root.getChild("registers");

        FileUtils.forEachFile(StageUtils.SAVES_PATH + saveScene.getSave().getName() + "/registers", path -> {
            FileUtils.deleteDirectory(path.toString());
        });

        for (Element register : registers.getChildren("register")) {
            Element isDir = register.getChild("isDir");
            Path path = Paths.get(register.getChildText("path"));

            if (Boolean.parseBoolean(isDir.getText())) {    // if register is directory.
                FileUtils.createDirectories(path.toString());
            } else {                                        // if register is file.
                Path parent = path.getParent();

                if (!Files.exists(parent))
                    FileUtils.createDirectories(parent.toString());

                Register file = new Register(path.toString());

                file.setIV(FileUtils.hexToBytes(register.getChildText("iv")));
                file.setNameCipher(FileUtils.hexToBytes(register.getChildText("nameCipher")));
                file.setFileTypeCipher(FileUtils.hexToBytes(register.getChildText("fileTypeCipher")));
                file.setContentCipher(FileUtils.hexToBytes(register.getChildText("contentCipher")));

                saveScene.getSave().openRegister(file);
                saveScene.getSave().saveRegister(file);
                file.close();
            }
        }
    }
}
