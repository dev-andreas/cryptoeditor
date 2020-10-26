package com.andreas.main.stages.mainStage.scenes.loginScene;

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
import com.andreas.main.stages.fileStage.FileStage;
import com.andreas.main.stages.mainStage.scenes.saveScene.SaveScene;
import com.andreas.main.stages.newSaveStage.NewSaveStage;
import com.andreas.main.stages.removeSaveStage.RemoveSaveStage;
import com.andreas.main.stages.renameSaveStage.RenameSaveStage;
import com.andreas.main.stages.themeSelectorStage.ThemeSelectorStage;

import org.jdom2.Element;

import java.io.File;
import java.nio.file.Path;
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
        getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.ENTER), () -> {
            openPressed(null);
        });

        getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.C, KeyCombination.SHORTCUT_DOWN), () -> {
            addSave();
        });

        getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.R, KeyCombination.SHORTCUT_DOWN), () -> {
            renameSave();
        });

        getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.D, KeyCombination.SHORTCUT_DOWN), () -> {
            deleteSave();
        });

        getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.O, KeyCombination.SHORTCUT_DOWN), () -> {
            openFile();
        });

        setEditable();

        keyPath.setText(readKeyPath());
    }

    @FXML
    private void openPressed(MouseEvent event) {

        getScene().getStage().applyLoadingScene(action -> {

            action.setText("Opening...");

            Save save = new Save();
            save.read(StageUtils.SAVES_PATH + savesList.getItems().get(index) + "/saveData.xml");

            if (!keyPathReadable()) {
                action.endNow(endingAction -> {
                    StageUtils.pushNotification("Key file not found!");
                });
                return;
            }

            if (!Arrays.equals(save.getPublicKey().getEncoded(), RSA.readKeyPair(keyPath.getText()).getPublic().getEncoded())) {
                action.endNow(endingAction -> {
                    StageUtils.pushNotification("Selected key file is not compatible!");
                });
                return;
            }

            if (passwordMatches(save, password.getText())) {

                save.open(keyPath.getText(), password.getText());

                action.endNow(endingAction -> {
                    ButtonType openAnyway = new ButtonType("Open anyway");
                    ButtonType removed = new ButtonType("I removed it");
                    Alert alert = new Alert(AlertType.WARNING, "Please remove your storage medium in which the key is located.", 
                    removed, openAnyway);

                    alert.initModality(Modality.WINDOW_MODAL);

                    alert.showAndWait();

                    if (alert.getResult() == openAnyway) {
                        getScene().getStage().setScene(new SaveScene(getScene().getStage().getApp(), save));
                    }

                    if (alert.getResult() == removed) {

                        if (keyPathReadable()) {
                            StageUtils.pushNotification("Storage medium not removed!");
                            return;
                        }
                        getScene().getStage().setScene(new SaveScene(getScene().getStage().getApp(), save));
                    }
                });
            } else {
                action.endNow(endingAction -> {
                    StageUtils.pushNotification("Password is incorrect!");
                });
            }
        });
    }

    @FXML
    private void savePressed(MouseEvent event) {
        index = savesList.getSelectionModel().getSelectedIndex();
        if (index < 0)
            return;

        saveName.setText(savesList.getSelectionModel().getSelectedItem());
        setEditable();
    }

    @FXML
    private void browsePressed(MouseEvent event) {
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

    @FXML
    private void addKeyPressed(MouseEvent event) {
        CreateKeyStage stage = new CreateKeyStage(getScene().getStage().getApp());
        stage.start();
    }

    @FXML
    private void addSave() {
        NewSaveStage stage = new NewSaveStage(getScene().getStage().getApp(), (LoginScene)getScene());
        stage.start();
    }

    @FXML
    private void deleteSave() {
        RemoveSaveStage stage = new RemoveSaveStage(getScene().getStage().getApp(), (LoginScene)getScene());
        stage.start();
    }

    @FXML
    private void renameSave() {
        RenameSaveStage stage = new RenameSaveStage(getScene().getStage().getApp(), (LoginScene)getScene());
        stage.start();
    }

    @FXML
    private void openFile() {
        FileChooser fc = new FileChooser();
        Path file = fc.showOpenDialog(null).toPath();


        FileStage stage = new FileStage(getScene().getStage().getApp(), file);
        stage.start();
    }

    @FXML
    private void selectTheme() {
        ThemeSelectorStage stage = new ThemeSelectorStage(getScene().getStage().getApp());
        stage.start();
    }

    public boolean passwordMatches(Save save, String password) {

        if (SHA.verify(password, save.getPasswordSalt(), save.getPasswordHash()))
            return true;
        return false;
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
