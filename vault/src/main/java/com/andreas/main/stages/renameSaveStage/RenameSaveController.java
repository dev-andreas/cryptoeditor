package com.andreas.main.stages.renameSaveStage;

import com.andreas.main.app.AppController;
import com.andreas.main.cryptography.SHA;
import com.andreas.main.save.Save;
import com.andreas.main.stages.StageUtils;
import com.andreas.main.stages.mainStage.scenes.loginScene.LoginController;
import com.andreas.main.stages.mainStage.scenes.loginScene.LoginScene;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.MouseEvent;

public class RenameSaveController extends AppController {

    @FXML
    public TextField newName;

    @FXML
    public PasswordField password;

    @FXML
    public Label notification;

    @FXML
    public Button rename;

    private LoginScene loginScene;
    private LoginController loginController;

    @Override
    public void init() {
        
        loginScene = ((RenameSaveStage) getScene().getStage()).getLoginScene();
        loginController = (LoginController) loginScene.getController();

        // Shortcuts      
        getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.ENTER), () -> {
            renamePressed(null);
        });
    }

    public void renamePressed(MouseEvent event) {
        int index = loginController.savesList.getSelectionModel().getSelectedIndex();

        Save save = new Save();
        save.read(StageUtils.SAVES_PATH + loginController.savesList.getItems().get(index) + "/saveData.xml");

        if (!SHA.verify(password.getText(), save.getPasswordSalt(), save.getPasswordHash())) {
            notification.setText("Passwords do not match!");
            return;
        }

        if (loginController.nameExists(newName.getText())) {
            notification.setText("Name already exists!");
            return;
        }

        getScene().getStage().applyLoadingScene(action -> {
            action.setText("Renaming save...");
            loginScene.renameSave(save, index, newName.getText());
            action.endNow(endingAction -> {
                getScene().getStage().stop();
            });
        });
    }

    public void cancelPressed(MouseEvent event) {
        getScene().getStage().stop();
    }
}