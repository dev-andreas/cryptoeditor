package com.andreas.main.stages.removeSaveStage;

import com.andreas.main.app.AppController;
import com.andreas.main.cryptography.RSA;
import com.andreas.main.save.Save;
import com.andreas.main.stages.loginStage.LoginController;
import com.andreas.main.stages.loginStage.LoginStage;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.input.MouseEvent;

public class RemoveSaveController extends AppController{

    @FXML
    public PasswordField password = new PasswordField();

    @FXML
    public Label notification = new Label();

    @FXML
    public Button delete;

    @Override
    public void init() {
        delete.requestFocus();
    }

    public void deletePressed(MouseEvent event) {
        LoginStage stage = ((RemoveSaveStage)this.stage).getLoginStage();
        LoginController controller = (LoginController)stage.getController();

        int index = controller.savesList.getSelectionModel().getSelectedIndex();
        
        Save save = new Save();
        save.readFromFile("data/saves/" + stage.getSaves().get(index).getId() + ".xml");

        if (!RSA.verify(password.getText(), save.getPasswordCertificate(), save.getPublicKey())) {
            notification.setText("Passwords do not match!");
            return;
        }

        stage.removeSave(controller.savesList.getSelectionModel().getSelectedIndex());
        this.stage.close();

    }

    public void cancelPressed(MouseEvent event) {
        stage.close();
    }
}