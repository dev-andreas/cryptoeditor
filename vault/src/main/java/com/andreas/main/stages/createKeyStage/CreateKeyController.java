package com.andreas.main.stages.createKeyStage;

import java.awt.Button;
import java.io.File;
import java.security.KeyPair;

import com.andreas.main.app.AppController;
import com.andreas.main.cryptography.RSA;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;

public class CreateKeyController extends AppController {

    @FXML
    public TextField keyName;

    @FXML
    public TextField keyPath;

    @FXML
    public Label notifiation;

    @FXML
    public Button create;

    @Override
    public void init() {
        create.requestFocus();
    }

    public void browsePressed(MouseEvent event) {
        DirectoryChooser dc = new DirectoryChooser();
        File file = dc.showDialog(null);


        keyPath.setText(file.getAbsolutePath());
    }

    public void createPressed(MouseEvent event) {
        if (keyName.getText().isEmpty()) {
            notifiation.setText("Please type in a key name.");
            return;
        }
        if (keyPath.getText().isEmpty()) {
            notifiation.setText("Please select a directory.");
            return;
        }
    
        KeyPair kp = RSA.gen(2048);
        if (!RSA.writeKeyPair(kp, keyPath.getText() + "/" + keyName.getText() + ".xml")) {
            notifiation.setText("Key couldn't be created.");
            return;
        }

        stage.close();
    }

    public void cancelPressed(MouseEvent event) {
        stage.close();
    }
}