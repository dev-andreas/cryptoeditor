package com.andreas.main.stages.newSaveStage;

import java.security.KeyPair;

import com.andreas.main.app.AppController;
import com.andreas.main.cryptography.RSA;
import com.andreas.main.save.Save;
import com.andreas.main.stages.loginStage.LoginController;
import com.andreas.main.stages.loginStage.LoginStage;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class NewSaveController extends AppController {
    
    @FXML
    public TextField name;

    @FXML
    public PasswordField password;

    @FXML
    public PasswordField repeatPassword;

    @FXML
    public Label message;

    @FXML
    public Button create;

    private String filePath;

    @Override
    public void init() {

        LoginStage loginStage = ((NewSaveStage)stage).getLoginStage();
        LoginController loginController = (LoginController)loginStage.getController();

        filePath = loginController.keyPath.getText();

        if (filePath.isEmpty()) {
            message.setText("No key path found!");
            create.setDisable(true);
        }

        create.requestFocus();
    }

    public void cancelPressed(MouseEvent mouseEvent) {
        stage.close();
    }

    public void createPressed(MouseEvent mouseEvent) {
        if (password.getText().equals(repeatPassword.getText())) {
            SecurityNote sn = FulfillsSecurity(password.getText());

            LoginStage loginStage = ((NewSaveStage)stage).getLoginStage();
            LoginController loginController = (LoginController)loginStage.getController();

            if (loginController.nameExists(name.getText())) {
                message.setText("Name already exists!");
                return;
            }

            message.setText(sn.message);
            if (sn.fulfillsSecurity) {

                Save save = new Save();

                KeyPair kp = RSA.readKeyPair(filePath);

                if (kp == null) {
                    message.setText("Key path is invalid!");
                    return;
                }

                save.initialize(kp, name.getText(), password.getText());
                
                
                ((NewSaveStage)stage).getLoginStage().addSave(save);
                this.stage.close();
            }
        } else {
            message.setText("Passwords do not match!");
        }
    }

    public static SecurityNote FulfillsSecurity(String password)
    {
        SecurityNote sn = new SecurityNote();

        if (password.length() < 8)
        {
            sn.message = "Password is to short, it must be at least 8 characters!";
            sn.fulfillsSecurity = false;
        }
        else if (password.equals(password.toLowerCase()))
        {
            sn.message = "Password must contain at least one upper case character!";
            sn.fulfillsSecurity = false;
        }
        else if (password.equals(password.toUpperCase()))
        {
            sn.message = "Password must contain at least one lower case character!";
            sn.fulfillsSecurity = false;
        }
        else if (!password.matches(".*\\d+.*"))
        {
            sn.message = "Password must contain at least one digit!";
            sn.fulfillsSecurity = false;
        }

        return sn;
    }
}