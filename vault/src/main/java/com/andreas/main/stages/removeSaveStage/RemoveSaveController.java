package com.andreas.main.stages.removeSaveStage;

import com.andreas.main.app.AppController;
import com.andreas.main.cryptography.SHA;
import com.andreas.main.save.Save;
import com.andreas.main.stages.StageUtils;
import com.andreas.main.stages.loginStage.LoginController;
import com.andreas.main.stages.loginStage.LoginStage;

import javafx.application.Platform;
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

    @Override
    public void init() {
        Platform.runLater(() -> {        
            stage.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.ENTER), () -> {
                deletePressed(null);
            });
        });
    }

    public void deletePressed(MouseEvent event) {
        LoginStage stage = ((RemoveSaveStage)this.stage).getLoginStage();
        LoginController controller = (LoginController)stage.getController();
        
        int index = controller.savesList.getSelectionModel().getSelectedIndex();

        Save save = new Save();
        save.read(StageUtils.SAVES_PATH + controller.savesList.getItems().get(index) + "/saveData.xml");

        if (!SHA.verify(password.getText(), save.getPasswordSalt(), save.getPasswordHash())) {
            StageUtils.pushNotification("Passwords do not match!");
            return;
        }

        stage.removeSave(save, controller.savesList.getSelectionModel().getSelectedIndex());
        this.stage.close();
    }

    public void cancelPressed(MouseEvent event) {
        stage.close();
    }
}