package com.andreas.main.stages.renameRegisterStage;

import com.andreas.main.app.AppController;
import com.andreas.main.stages.saveStage.SaveController;
import com.andreas.main.stages.saveStage.SaveStage;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.MouseEvent;

public class RenameRegisterController extends AppController{

    @FXML
    public TextField newName = new TextField();

    @FXML
    public Label message = new Label();

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
        SaveStage stage = ((RenameRegisterStage)this.stage).getSaveStage();
        SaveController controller = (SaveController)stage.getController();

        if (newName.getText().isEmpty()) {
            message.setText("Please enter a name!");
            return;
        }

        if (controller.nameExists(newName.getText())) {
            message.setText("Name already exists!");
            return;
        }
        
        stage.renameRegister(controller.registers.getSelectionModel().getSelectedIndex(), message.getText());

        this.stage.close();
    }

    public void cancelPressed(MouseEvent event) {
        stage.close();
    }
}