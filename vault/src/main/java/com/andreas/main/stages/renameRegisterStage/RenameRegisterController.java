package com.andreas.main.stages.renameRegisterStage;

import com.andreas.main.app.AppController;
import com.andreas.main.save.Register;
import com.andreas.main.stages.StageUtils;
import com.andreas.main.stages.mainStage.scenes.saveScene.SaveController;
import com.andreas.main.stages.mainStage.scenes.saveScene.SaveScene;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.MouseEvent;

public class RenameRegisterController extends AppController{

    @FXML
    public TextField newName;

    @FXML
    public Label oldName;

    @FXML
    public TextField registerType;

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

        newName.setText(saveController.selectedItem.getName());
        registerType.setText(saveController.selectedItem.getType());

        for (String type : Register.UNCHANGEABLE_FILE_TYPES) {
            if (type.equals(saveController.selectedItem.getType())) {
                registerType.setDisable(true);
            }
        }
    }

    public void renamePressed(MouseEvent event) {

        // checking if text field is empty
        if (newName.getText().isEmpty()) {
            StageUtils.pushNotification("Please enter a name!");
            return;
        }

        // checking if name already exists
        if (saveController.nameExists(newName.getText() + registerType.getText())) {
            StageUtils.pushNotification("Name already exists!");
            return;
        }

        // checking if file type is unchangeable
        if (!registerType.isDisabled()) {
            for (String type : Register.UNCHANGEABLE_FILE_TYPES) {
                if (type.equals(registerType.getText())) {
                    StageUtils.pushNotification("Can't convert register to this type!");
                    return;
                }
            }
        }
        
        getScene().getStage().applyLoadingScene(action -> {
            action.setText("Renaming register...");
            if (registerType.getText().charAt(0) == '.')
                saveScene.renameRegister(newName.getText(), registerType.getText());
            else
                saveScene.renameRegister(newName.getText(), "." + registerType.getText());
            action.endNow(endingAction -> {
                getScene().getStage().stop();
            });
        });
    }

    public void cancelPressed(MouseEvent event) {
        getScene().getStage().stop();
    }
}