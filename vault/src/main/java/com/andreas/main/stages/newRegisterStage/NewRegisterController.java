package com.andreas.main.stages.newRegisterStage;

import com.andreas.main.app.AppController;
import com.andreas.main.save.Register;
import com.andreas.main.stages.saveStage.SaveController;
import com.andreas.main.stages.saveStage.SaveStage;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class NewRegisterController extends AppController {

    @FXML
    public TextField registerName = new TextField();

    @FXML
    public Label notification = new Label();

    @Override
    public void init() {

    }

    public void createPressed(MouseEvent event) {
        SaveStage saveStage = ((NewRegisterStage)stage).getSaveStage();
        SaveController saveController = (SaveController)saveStage.getController();

        if (registerName.getText().isEmpty()) {
            notification.setText("Please enter a register name!");
            return;
        }

        if (saveController.nameExists(registerName.getText())) {
            notification.setText("Name already exists!");
            return;
        }

        Register register = new Register(saveStage.getSave(), registerName.getText(), "");
        saveStage.addRegister(register, registerName.getText());

        stage.close();
    }

    public void cancelPressed(MouseEvent event) {
        stage.close();
    }
}