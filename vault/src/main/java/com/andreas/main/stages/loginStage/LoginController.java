package com.andreas.main.stages.loginStage;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Modality;

import com.andreas.main.app.AppController;
import com.andreas.main.cryptography.RSA;
import com.andreas.main.cryptography.SHA;
import com.andreas.main.save.Save;
import com.andreas.main.stages.StageUtils;
import com.andreas.main.stages.createKeyStage.CreateKeyStage;
import com.andreas.main.stages.newSaveStage.NewSaveStage;
import com.andreas.main.stages.removeSaveStage.RemoveSaveStage;
import com.andreas.main.stages.renameSaveStage.RenameSaveStage;
import com.andreas.main.stages.saveStage.SaveStage;

import org.jdom2.Element;

import java.io.File;
import java.security.KeyPair;
import java.util.Arrays;

import com.andreas.main.FileUtils;

public class LoginController extends AppController {

    @FXML
    public PasswordField password;

    @FXML
    public Label saveName;

    @FXML
    public ListView<String> savesList;

    @FXML
    public TextField keyPath;

    @FXML
    public Button open;

    private int index;

    @Override
    public void init() {

        // Shortcuts
        Platform.runLater(() -> {        
            stage.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.ENTER), () -> {
                openPressed(null);
            });
        });

        Platform.runLater(() -> {        
            stage.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.C, KeyCombination.SHORTCUT_DOWN), () -> {
                addSave();
            });
        });

        Platform.runLater(() -> {        
            stage.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.R, KeyCombination.SHORTCUT_DOWN), () -> {
                renameSave();
            });
        });

        Platform.runLater(() -> {        
            stage.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.D, KeyCombination.SHORTCUT_DOWN), () -> {
                deleteSave();
            });
        });

        Platform.runLater(() -> {        
            stage.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.B, KeyCombination.SHORTCUT_DOWN), () -> {
                // TODO backup
            });
        });

        Platform.runLater(() -> {        
            stage.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.L, KeyCombination.SHORTCUT_DOWN), () -> {
                // TODO load backup
            });
        });

        setEditable();

        keyPath.setText(readKeyPath());
    }

    public void openPressed(MouseEvent event) {

        Save save = new Save();
        save.read(StageUtils.SAVES_PATH + savesList.getItems().get(index) + "/saveData.xml");

        if (!keyPathReadable()) {
            StageUtils.pushNotification("Key file not found!");
            return;
        }

        if (!Arrays.equals(save.getPublicKey().getEncoded(), RSA.readKeyPair(keyPath.getText()).getPublic().getEncoded())) {
            StageUtils.pushNotification("Selected key file is not compatible!");
            return;
        }

        if (passwordMatches(save, password.getText())) {

            save.open(keyPath.getText(), password.getText());

            SaveStage saveStage = new SaveStage(this.stage.getApp(), save);

            ButtonType openAnyway = new ButtonType("Open anyway");
            ButtonType removed = new ButtonType("I removed it");
            Alert alert = new Alert(AlertType.WARNING, "Please remove your storage medium in which the key is located.", 
            removed, openAnyway);

            alert.initModality(Modality.WINDOW_MODAL);

            alert.showAndWait();

            if (alert.getResult() == openAnyway) {
                stage.close();
                saveStage.show();
            }

            if (alert.getResult() == removed) {

                if (keyPathReadable()) {
                    StageUtils.pushNotification("Storage medium not removed!");
                    return;
                }
                saveStage.show();
                stage.close();
            }
        } else {
            StageUtils.pushNotification("Password is incorrect!");
        }
    }

    public void savePressed(MouseEvent event) {
        
        index = savesList.getSelectionModel().getSelectedIndex();
        if (index < 0)
            return;

        saveName.setText(savesList.getSelectionModel().getSelectedItem());
        setEditable();
    }

    public void browsePressed(MouseEvent event) {
        FileChooser fc = new FileChooser();
        File file = fc.showOpenDialog(null);

        if (file != null) {
            String path = file.getAbsolutePath();
            keyPath.setText(path);

            if (!keyPathReadable()) {
                keyPath.setText("");
                StageUtils.pushNotification("File doesn't contain the necessary data.");
            } else 
                saveKeyPath(path);
        }
    }

    public void addKeyPressed(MouseEvent event) {
        CreateKeyStage stage = new CreateKeyStage(this.stage.getApp());
        stage.show();
    }

    public boolean passwordMatches(Save save, String password) {

        if (SHA.verify(password, save.getPasswordSalt(), save.getPasswordHash()))
            return true;
        return false;
    }

    public void addSave() {
        NewSaveStage stage = new NewSaveStage(this.stage.getApp(), (LoginStage)this.stage);
        stage.show();
    }

    public void deleteSave() {
        RemoveSaveStage stage = new RemoveSaveStage(this.stage.getApp(), (LoginStage)this.stage);
        stage.show();
    }

    public void renameSave() {
        RenameSaveStage stage = new RenameSaveStage(this.stage.getApp(), (LoginStage)this.stage);
        stage.show();
    }

    public void setEditable() {
        password.setEditable(savesList.getItems().size() > 0);
    }

    private void saveKeyPath(String keyPath) {
        Element root = FileUtils.readXmlFile(StageUtils.DATA_FILE_PATH);
        root.getChild("keyPath").setText(keyPath);
        FileUtils.createXmlFile(StageUtils.DATA_FILE_PATH, root);
    }

    private String readKeyPath() {
        Element root = FileUtils.readXmlFile(StageUtils.DATA_FILE_PATH);
        return root.getChildText("keyPath");
    }


    private boolean keyPathReadable() {
        try {
            KeyPair kp = RSA.readKeyPair(keyPath.getText());
            return !(kp.getPrivate() == null || kp.getPublic() == null);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean nameExists(String name) {
        for (String n : savesList.getItems())
            if (n.equals(name))
                return true;
        return false;
    }
}
