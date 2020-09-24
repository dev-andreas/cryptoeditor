package com.andreas.main.stages.saveStage;

import com.andreas.main.app.AppController;
import com.andreas.main.app.RegisterTab;
import com.andreas.main.save.Register;
import com.andreas.main.save.Save;
import com.andreas.main.save.SaveTreeItem;
import com.andreas.main.stages.createBackupStage.CreateBackupStage;
import com.andreas.main.stages.exportRegisterStage.ExportRegisterStage;
import com.andreas.main.stages.importRegisterStage.ImportRegisterStage;
import com.andreas.main.stages.loadBackupStage.LoadBackupStage;
import com.andreas.main.stages.loginStage.LoginStage;
import com.andreas.main.stages.newRegisterStage.NewRegisterStage;
import com.andreas.main.stages.renameRegisterStage.RenameRegisterStage;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SaveController extends AppController {

    @FXML
    public Label registerName;

    @FXML
    public Label fileType;

    @FXML
    public Label savedState;

    @FXML
    public Button saveButton;

    @FXML
    public VBox registersBox;

    @FXML
    public TabPane tabs;

    @FXML
    public TreeView<String> registers;

    public SaveTreeItem selectedItem, selectedDirectory;

    @Override
    public void init() {

        // shortcuts
        Platform.runLater(() -> {        
            stage.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.S, KeyCombination.SHORTCUT_DOWN), () -> {
                saveCurrentTab();
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
                createBackup();
            });
        });

        Platform.runLater(() -> {        
            stage.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.L, KeyCombination.SHORTCUT_DOWN), () -> {
                loadBackup();
            });
        });

        Platform.runLater(() -> {        
            stage.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.I, KeyCombination.SHORTCUT_DOWN), () -> {
                importRegister();
            });
        });

        Platform.runLater(() -> {        
            stage.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.E, KeyCombination.SHORTCUT_DOWN), () -> {
                exportRegister();
            });
        });

        Platform.runLater(() -> {        
            stage.getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.ESCAPE), () -> {
                lockSavePressed();
            });
        });


        // set tree item double click action
        registers.setCellFactory(tree -> {
            TreeCell<String> cell = new TreeCell<String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty) ;
                    if (empty) {
                        setText(null);
                    } else {
                        setText(item);
                    }
                }
            };
            cell.setOnMouseClicked(event -> {
                if (! cell.isEmpty()) {
                    if (event.getButton().equals(MouseButton.PRIMARY)) {
                        selectedItem = (SaveTreeItem) registers.getSelectionModel().getSelectedItem();
                        if (!selectedItem.getType().equals(Register.DIRECTORY)) {
                            if (event.getClickCount() == 2)
                                openTab();
                            selectedDirectory = (SaveTreeItem) registers.getSelectionModel().getSelectedItem().getParent();
                        } else {
                            selectedDirectory = (SaveTreeItem) registers.getSelectionModel().getSelectedItem();
                        }
                    }
                }
            });
            return cell ;
        });

        // tab changed action
        tabs.getSelectionModel().selectedItemProperty().addListener( new ChangeListener<Tab>() {

			@Override
			public void changed(ObservableValue<? extends Tab> observable, Tab oldTab, Tab newTab) {
				if (newTab == null) {
                    registerName.setText("");
                    fileType.setText("");
                    return;
                }
                savedState.setText(((RegisterTab)newTab).isSaved() ? "" : "Unsaved changes.");
                registerName.setText(((RegisterTab)newTab).getRegister().getName());
                fileType.setText(((RegisterTab)newTab).getRegister().getFileType());
			}
        });

        selectedItem = (SaveTreeItem) registers.getRoot();
        selectedDirectory = selectedItem;
    }

    @FXML
    public void lockSavePressed() {
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

    @FXML
    public void addRegister() {
        NewRegisterStage stage = new NewRegisterStage(this.stage.getApp(), (SaveStage) this.stage);
        stage.show();
    }

    @FXML
    public void deleteRegister() {
        Alert alert = new Alert(AlertType.CONFIRMATION, "Delete \"" + selectedItem.getName() + "\"?", 
            ButtonType.CANCEL, ButtonType.YES);
        alert.initModality(Modality.WINDOW_MODAL);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            ((SaveStage)stage).removeRegister();
        }

        if (alert.getResult() == ButtonType.CANCEL) {
            alert.close();
        }
    }

    @FXML
    public void renameRegister() {
        Stage stage = new RenameRegisterStage(this.stage.getApp(), (SaveStage)this.stage);
        stage.show();
    }

    @FXML
    public void importRegister() {
        Stage stage = new ImportRegisterStage(this.stage.getApp(), (SaveStage)this.stage);
        stage.show();
    }

    @FXML
    public void exportRegister() {
        Stage stage = new ExportRegisterStage(this.stage.getApp(), (SaveStage)this.stage);
        stage.show();
    }

    @FXML
    public void createBackup() {
        Stage stage = new CreateBackupStage(this.stage.getApp(), (SaveStage)this.stage);
        stage.show();
    }

    @FXML
    public void loadBackup() {
        Stage stage = new LoadBackupStage(this.stage.getApp(), (SaveStage)this.stage);
        stage.show();
    }

    public void saveCurrentTab() {
        if (tabs.getSelectionModel().getSelectedIndex() < 0 || savedState.getText().equals(""))
            return;
        Save save = ((SaveStage)stage).getSave();
        ((RegisterTab)tabs.getSelectionModel().getSelectedItem()).save(save);

        savedState.setText("");
    }

    public void openTab() {
        if (selectedItem.getType().equals(Register.DIRECTORY))
            return;

        Register register = new Register(selectedItem.calculatePath());
        register.read();
        ((SaveStage)stage).getSave().openRegister(register);
        RegisterTab tab = RegisterTab.getCorrectTab((SaveStage)stage, register);
        tabs.getTabs().add(tab);
        tabs.getSelectionModel().select(tab);
    }

    public boolean nameExists(String name) {
        boolean[] exists = new boolean[] { false };
        selectedDirectory.forEachChild(e -> {
            if ((e.getName() + e.getType()).equals(name)) {
                exists[0] = true;
                return;
            }
        });
        return exists[0];
    }
}