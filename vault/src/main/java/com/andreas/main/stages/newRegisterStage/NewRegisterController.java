package com.andreas.main.stages.newRegisterStage;

import com.andreas.main.app.AppController;
import com.andreas.main.save.Register;
import com.andreas.main.stages.StageUtils;
import com.andreas.main.stages.saveStage.SaveController;
import com.andreas.main.stages.saveStage.SaveStage;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.MouseEvent;

public class NewRegisterController extends AppController {

    @FXML
    public TextField registerName;

    @FXML
    public ComboBox<String> registerType;

    @Override
    public void init() {
        // Shortcuts
        Platform.runLater(() -> {        
            stage.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.ENTER), () -> {
                createPressed(null);
            });
        });

        registerType.getItems().addAll(Register.INTERN_FILE_TYPES);
        registerType.setValue(Register.DEFAULT_FILE_TYPE);
    }

    public void createPressed(MouseEvent event) {
        SaveStage saveStage = ((NewRegisterStage)stage).getSaveStage();
        SaveController saveController = (SaveController)saveStage.getController();

        if (registerName.getText().isEmpty()) {
            StageUtils.pushNotification("Please enter a register name!");
            return;
        }

        if (saveController.nameExists(registerName.getText()+registerType.getValue())) {
            StageUtils.pushNotification("Name already exists!");
            return;
        }

        saveStage.addRegister(registerName.getText(), registerType.getValue());

        stage.close();
    }

    public void cancelPressed(MouseEvent event) {
        stage.close();
    }
}