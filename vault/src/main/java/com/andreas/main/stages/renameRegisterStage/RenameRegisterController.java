package com.andreas.main.stages.renameRegisterStage;

import com.andreas.main.app.AppController;
import com.andreas.main.save.Register;
import com.andreas.main.stages.mainStage.scenes.saveScene.SaveController;
import com.andreas.main.stages.mainStage.scenes.saveScene.SaveScene;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.MouseEvent;

public class RenameRegisterController extends AppController{

    @FXML
    public TextField newName;

    @FXML
    public Label message;

    @FXML
    public Label oldName;

    @FXML
    public ComboBox<String> registerType;

    private SaveController saveController;
    private SaveScene saveScene;

    @Override
    public void init() {

        saveScene = ((RenameRegisterStage) getScene().getStage()).getSaveScene();
        saveController = (SaveController) saveScene.getController();

        // Shortcuts      
        getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.ENTER), () -> {
            renamePressed(null);
        });

        oldName.setText("Rename\"" + saveController.selectedItem.getName() + "\"");
        registerType.setValue(saveController.selectedItem.getType());

        registerType.getItems().addAll(Register.INTERN_FILE_TYPES);
    }

    public void renamePressed(MouseEvent event) {

        if (newName.getText().isEmpty()) {
            message.setText("Please enter a name!");
            return;
        }

        if (saveController.nameExists(newName.getText() + registerType.getValue())) {
            message.setText("Name already exists!");
            return;
        }
        
        getScene().getStage().applyLoadingScene(action -> {
            action.setText("Renaming register...");
            saveScene.renameRegister(newName.getText(), registerType.getValue());
            action.endNow(endingAction -> {
                getScene().getStage().stop();
            });
        });
    }

    public void cancelPressed(MouseEvent event) {
        getScene().getStage().stop();
    }
}