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
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

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
        Platform.runLater(() -> {        
            saveButton.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.S, KeyCombination.SHORTCUT_DOWN), () -> {
                if (index < 0) return;
                saveData();
            });
        });

        ContextMenu contextMenu = new ContextMenu();

        MenuItem item1 = new MenuItem("Create new");
        MenuItem item2 = new MenuItem("Delete");
        MenuItem item3 = new MenuItem("Rename");

        item1.setOnAction(e -> {
            addRegister();
        });

        item2.setOnAction(e -> {
            deleteRegister();
        });

        item3.setOnAction(e -> {
            renameRegister();
        });

        contextMenu.getItems().addAll(item1, item2, item3); 
        registers.setContextMenu(contextMenu);
        setEditable();
    }

    public void addRegisterPressed(MouseEvent event) {
        addRegister();
    }

    public void registersPressed(MouseEvent event) {

        index = registers.getSelectionModel().getSelectedIndex();
        if (index < 0)
            return;

        registerName.setText(registers.getSelectionModel().getSelectedItem());
        registerContent.setText(((SaveStage) stage).getSave().getRegisters().get(index).getContent());
        setEditable();
    }

    public void saveChangesPressed(MouseEvent event) {
        saveData();
    }

    public void lockSavePressed(MouseEvent event) {
        Alert alert = new Alert(AlertType.CONFIRMATION, "Save gets closed. Are you sure you want to leave?\nAttention: Don't forget to save!", 
        ButtonType.YES, ButtonType.CANCEL);
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

    private void saveData() {
        Save save = ((SaveStage) stage).getSave();

        save.save("data/saves/" + save.getId() + ".xml");
        savedState.setText("Saved");
    }

    private void addRegister() {
        NewRegisterStage stage = new NewRegisterStage(this.stage.getApp(), (SaveStage) this.stage);
        stage.show();
    }

    private void deleteRegister() {
        Alert alert = new Alert(AlertType.CONFIRMATION, "Delete \"" + registers.getSelectionModel().getSelectedItem() + "\"?", 
            ButtonType.YES, ButtonType.CANCEL);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            registers.getItems().remove(index);
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