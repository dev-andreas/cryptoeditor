package com.andreas.main.stages.newSaveStage;

import java.security.KeyPair;

import com.andreas.main.app.AppController;
import com.andreas.main.cryptography.CryptoUtils;
import com.andreas.main.cryptography.RSA;
import com.andreas.main.save.Save;
import com.andreas.main.stages.StageUtils;
import com.andreas.main.stages.mainStage.scenes.loginScene.LoginController;
import com.andreas.main.stages.mainStage.scenes.loginScene.LoginScene;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.MouseEvent;

public class NewSaveController extends AppController {

    @FXML
    public TextField name;

    @FXML
    public PasswordField password;

    @FXML
    public PasswordField repeatPassword;

    @FXML
    public Button create;

    private String keyPath;

    private LoginScene loginScene;
    private LoginController loginController;

    @Override
    public void init() {
        
        loginScene = ((NewSaveStage)getScene().getStage()).getLoginScene();
        loginController = (LoginController) loginScene.getController();

        // Shortcuts
        getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.ENTER), () -> {
            createPressed(null);
        });

        keyPath = loginController.keyPath.getText();

        if (keyPath.isEmpty()) {
            StageUtils.pushNotification("No key path found!");
            create.setDisable(true);
        }

        create.requestFocus();
    }

    public void cancelPressed(MouseEvent mouseEvent) {
        getScene().getStage().close();
    }

    public void createPressed(MouseEvent mouseEvent) {
        if (password.getText().equals(repeatPassword.getText())) {
            SecurityNote sn = FulfillsSecurity(password.getText());

            if (loginController.nameExists(name.getText())) {
                StageUtils.pushNotification("Name already exists!");
                return;
            }

            if (sn.fulfillsSecurity) {

                getScene().getStage().applyLoadingScene(action -> {
                    action.setText("Creating new save...");
                    Save save = new Save();

                    KeyPair kp = RSA.readKeyPair(keyPath);
    
                    if (kp == null) {
                        action.endNow(endingAction -> {
                            StageUtils.pushNotification("Key path is invalid!");
                        });
                    }
    
                    save.create(name.getText(), password.getText(), keyPath);
                    
                    loginScene.addSave(save);
                    action.endNow(endingAction -> {
                        getScene().getStage().close();
                    });
                });
            } else {
                StageUtils.pushNotification(sn.message);
            }
        } else {
            StageUtils.pushNotification("Passwords do not match!");
        }
    }

    public void generateRandomPressed(MouseEvent event) {
        String password = CryptoUtils.GenerateSecurePassword(20, CryptoUtils.ALL_CHARACTERS);

        this.password.setText(password);
        this.repeatPassword.setText(password);

        StageUtils.CopyTextToClipboard(password);
        StageUtils.pushNotification("Your password: " + password + "\nPassword is copied to clipboard.");
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