package com.andreas.main.stages.newRegisterStage;

import com.andreas.main.app.AppController;
import com.andreas.main.save.Register;
import com.andreas.main.stages.StageUtils;
import com.andreas.main.stages.mainStage.scenes.saveScene.SaveController;
import com.andreas.main.stages.mainStage.scenes.saveScene.SaveScene;

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

    private SaveScene saveScene;
    private SaveController saveController;

    @Override
    public void init() {

        saveScene = ((NewRegisterStage) getScene().getStage()).getSaveScene();
        saveController = (SaveController)saveScene.getController();

        // Shortcuts      
        getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.ENTER), () -> {
            createPressed(null);
        });

        registerType.getItems().addAll(Register.INTERN_FILE_TYPES);
        registerType.setValue(Register.DEFAULT_FILE_TYPE);
    }

    public void createPressed(MouseEvent event) {
        

        if (registerName.getText().isEmpty()) {
            StageUtils.pushNotification("Please enter a register name!");
            return;
        }

        if (saveController.nameExists(registerName.getText()+registerType.getValue())) {
            StageUtils.pushNotification("Name already exists!");
            return;
        }

        getScene().getStage().applyLoadingScene(action -> {
            action.setText("Creating register...");
            saveScene.addRegister(registerName.getText(), registerType.getValue());
            action.endNow(endingAction -> {
                getScene().getStage().close();
            });
        });
    }

    public void cancelPressed(MouseEvent event) {
        getScene().getStage().close();
    }
}