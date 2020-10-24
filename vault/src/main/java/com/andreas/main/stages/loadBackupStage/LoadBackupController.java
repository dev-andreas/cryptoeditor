package com.andreas.main.stages.loadBackupStage;

import java.io.File;

import com.andreas.main.FileUtils;
import com.andreas.main.app.AppController;
import com.andreas.main.cryptography.SHA;
import com.andreas.main.stages.StageUtils;
import com.andreas.main.stages.mainStage.scenes.saveScene.SaveScene;

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
                loadBackupRegisters();
                saveScene.getSave().open(saveScene.getSave().getKeyPath(), password.getText());
                saveScene.getSave().write();
        
                action.endNow(endingAction -> {
                    saveScene.loadRegisters();
                    getScene().getStage().stop();
                });
            });
        }

        if (alert.getResult() == cancel) {
            getScene().getStage().stop();
        }
    }

    public void cancelPressed() {
        getScene().getStage().stop();
    }

    public void loadBackupRegisters() {
        FileUtils.unzip(filePath.getText(), StageUtils.SAVES_PATH, "");
    }
}
