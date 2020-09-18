package com.andreas.main.stages.renameSaveStage;

import com.andreas.main.app.AppController;
import com.andreas.main.cryptography.SHA;
import com.andreas.main.save.Save;
import com.andreas.main.stages.StageUtils;
import com.andreas.main.stages.loginStage.LoginController;
import com.andreas.main.stages.loginStage.LoginStage;

import javafx.application.Platform;
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

    @Override
    public void init() {
        
        // Shortcuts
        Platform.runLater(() -> {        
            stage.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.ENTER), () -> {
                renamePressed(null);
            });
        });
    }

    public void renamePressed(MouseEvent event) {
        LoginStage stage = ((RenameSaveStage)this.stage).getLoginStage();
        LoginController controller = (LoginController)stage.getController();

        int index = controller.savesList.getSelectionModel().getSelectedIndex();

        Save save = new Save();
        save.read(StageUtils.SAVES_PATH + controller.savesList.getItems().get(index) + "/saveData.xml");

        if (!SHA.verify(password.getText(), save.getPasswordSalt(), save.getPasswordHash())) {
            notification.setText("Passwords do not match!");
            return;
        }

        if (controller.nameExists(newName.getText())) {
            notification.setText("Name already exists!");
            return;
        }

        stage.renameSave(save, index, newName.getText());
        this.stage.close();
    }

    public void cancelPressed(MouseEvent event) {
        stage.close();
    }
}