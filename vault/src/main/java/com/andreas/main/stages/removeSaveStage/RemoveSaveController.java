package com.andreas.main.stages.removeSaveStage;

import com.andreas.main.app.AppController;
import com.andreas.main.cryptography.SHA;
import com.andreas.main.save.Save;
import com.andreas.main.stages.StageUtils;
import com.andreas.main.stages.mainStage.scenes.loginScene.LoginController;
import com.andreas.main.stages.mainStage.scenes.loginScene.LoginScene;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.MouseEvent;

public class RemoveSaveController extends AppController{

    @FXML
    public PasswordField password = new PasswordField();

    @FXML
    public Button delete;

    private LoginController loginController;
    private LoginScene loginScene;

    @Override
    public void init() {
        loginScene = ((RemoveSaveStage)getScene().getStage()).getLoginScene();
        loginController = (LoginController) loginScene.getController();

        getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.ENTER), () -> {
            deletePressed(null);
        });
    }

    public void deletePressed(MouseEvent event) {
        
        int index = loginController.savesList.getSelectionModel().getSelectedIndex();

        getScene().getStage().applyLoadingScene(action -> {
            action.setText("Deleting save...");
            Save save = new Save();
            save.read(StageUtils.SAVES_PATH + loginController.savesList.getItems().get(index) + "/saveData.xml");
    
            if (!SHA.verify(password.getText(), save.getPasswordSalt(), save.getPasswordHash())) {
                StageUtils.pushNotification("Passwords do not match!");
                action.endNow(endingAction -> {StageUtils.pushNotification("Passwords do not match!");});
            }
    
            loginScene.removeSave(save, loginController.savesList.getSelectionModel().getSelectedIndex());
            action.endNow(endingAction -> {getScene().getStage().close();});
        });
    }

    public void cancelPressed(MouseEvent event) {
        getScene().getStage().close();
    }
}