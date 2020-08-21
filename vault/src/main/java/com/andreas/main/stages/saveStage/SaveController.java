package com.andreas.main.stages.saveStage;

import com.andreas.main.app.AppController;
import com.andreas.main.save.Save;
import com.andreas.main.stages.loginStage.LoginStage;
import com.andreas.main.stages.newRegisterStage.NewRegisterStage;
import com.andreas.main.stages.renameRegisterStage.RenameRegisterStage;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;

public class SaveController extends AppController {

    @FXML
    public Label registerName = new Label();

    @FXML
    public TextArea registerContent = new TextArea();

    @FXML
    public ListView<String> registers = new ListView<>();

    @FXML
    public Label savedState = new Label();

    @FXML
    public Button saveButton = new Button();

    private int index;

    @Override
    public void init() {

        // shortcuts
        Platform.runLater(() -> {        
            stage.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.S, KeyCombination.SHORTCUT_DOWN), () -> {
                saveData();
            });
        });

        Platform.runLater(() -> {        
            stage.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.C, KeyCombination.SHORTCUT_DOWN), () -> {
                addRegister();
            });
        });

        Platform.runLater(() -> {        
            stage.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.R, KeyCombination.SHORTCUT_DOWN), () -> {
                renameRegister();
            });
        });

        Platform.runLater(() -> {        
            stage.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.D, KeyCombination.SHORTCUT_DOWN), () -> {
                deleteRegister();
            });
        });

        Platform.runLater(() -> {        
            stage.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.B, KeyCombination.SHORTCUT_DOWN), () -> {
                // TODO backup
            });
        });

        Platform.runLater(() -> {        
            stage.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.L, KeyCombination.SHORTCUT_DOWN), () -> {
                // TODO load backup
            });
        });

        Platform.runLater(() -> {        
            stage.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.I, KeyCombination.SHORTCUT_DOWN), () -> {
                // TODO import file
            });
        });

        Platform.runLater(() -> {        
            stage.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.E, KeyCombination.SHORTCUT_DOWN), () -> {
                // TODO export file
            });
        });

        Platform.runLater(() -> {        
            stage.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.ESCAPE), () -> {
                lockSavePressed(null);
            });
        });

        setEditable();
    }

    public void registersPressed(MouseEvent event) {

        index = registers.getSelectionModel().getSelectedIndex();
        if (index < 0)
            return;

        registerName.setText(registers.getSelectionModel().getSelectedItem());
        registerContent.setText(((SaveStage) stage).getSave().getRegisters().get(index).getContent());
        setEditable();
    }

    public void lockSavePressed(MouseEvent event) {
        Alert alert = new Alert(AlertType.CONFIRMATION, "Save gets closed. Are you sure you want to leave?\nAttention: Don't forget to save!",
                ButtonType.CANCEL, ButtonType.YES);
        alert.initModality(Modality.WINDOW_MODAL);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            LoginStage stage = new LoginStage(this.stage.getApp());
            stage.show();
            this.stage.close();
            ((SaveStage)this.stage).getSave().close();
        }

        if (alert.getResult() == ButtonType.CANCEL) {
            alert.close();
        }
    }

    public void keyTyped(KeyEvent event) {
        if (index < 0)
            return;

        savedState.setText("Unsaved changes!");
        ((SaveStage) stage).getSave().getRegisters().get(index).setContent(registerContent.getText());
    }

    public void saveData() {
        Save save = ((SaveStage) stage).getSave();

        save.save("data/saves/" + save.getId() + ".xml");
        savedState.setText("Saved");
    }

    public void addRegister() {
        NewRegisterStage stage = new NewRegisterStage(this.stage.getApp(), (SaveStage) this.stage);
        stage.show();
    }

    public void deleteRegister() {

        Alert alert = new Alert(AlertType.CONFIRMATION, "Delete \"" + registers.getSelectionModel().getSelectedItem() + "\"?", 
            ButtonType.CANCEL, ButtonType.YES);
        alert.initModality(Modality.WINDOW_MODAL);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            registers.getItems().remove(index);
            ((SaveStage)stage).removeRegister(index);
            registerName.setText("No register selected");
            registerContent.setText("");
            setEditable();
        }

        if (alert.getResult() == ButtonType.CANCEL) {
            alert.close();
        }
    }

    public void renameRegister() {
        RenameRegisterStage stage = new RenameRegisterStage(this.stage.getApp(), (SaveStage)this.stage);
        stage.show();
    }

    public void setEditable() {
        registerContent.setEditable(registers.getItems().size() > 0);
    }

    public boolean nameExists(String name) {
        for (String n : registers.getItems())
            if (n.equals(name))
                return true;
        return false;
    }
}