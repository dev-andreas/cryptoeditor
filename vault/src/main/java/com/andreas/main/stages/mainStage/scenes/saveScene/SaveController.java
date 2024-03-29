package com.andreas.main.stages.mainStage.scenes.saveScene;

import com.andreas.main.app.AppController;
import com.andreas.main.app.AppTab;
import com.andreas.main.save.Register;
import com.andreas.main.save.Save;
import com.andreas.main.save.SaveTreeItem;
import com.andreas.main.stages.createBackupStage.CreateBackupStage;
import com.andreas.main.stages.exportRegisterStage.ExportRegisterStage;
import com.andreas.main.stages.importRegisterStage.ImportRegisterStage;
import com.andreas.main.stages.loadBackupStage.LoadBackupStage;
import com.andreas.main.stages.mainStage.scenes.loginScene.LoginScene;
import com.andreas.main.stages.newRegisterStage.NewRegisterStage;
import com.andreas.main.stages.renameRegisterStage.RenameRegisterStage;
import com.andreas.main.stages.themeSelectorStage.ThemeSelectorStage;

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
    public Button saveButton;

    @FXML
    public VBox registersBox;

    @FXML
    public TabPane tabs;

    @FXML
    public TreeView<String> registers;

    public SaveTreeItem selectedItem, selectedDirectory;
    public AppTab selectedTab;

    @Override
    public void init() {

        // shortcuts       
        getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.S, KeyCombination.SHORTCUT_DOWN), () -> {
            saveCurrentTab();
        });
      
        getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.C, KeyCombination.SHORTCUT_DOWN), () -> {
            addRegister();
        });
      
        getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.R, KeyCombination.SHORTCUT_DOWN), () -> {
            renameRegister();
        });
    
        getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.D, KeyCombination.SHORTCUT_DOWN), () -> {
            deleteRegister();
        });
    
        getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.B, KeyCombination.SHORTCUT_DOWN), () -> {
            createBackup();
        });
    
        getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.L, KeyCombination.SHORTCUT_DOWN), () -> {
            loadBackup();
        });

        getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.I, KeyCombination.SHORTCUT_DOWN), () -> {
            importRegister();
        });
    
        getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.E, KeyCombination.SHORTCUT_DOWN), () -> {
            exportRegister();
        });
    
        getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.ESCAPE), () -> {
            lockSavePressed();
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
        tabs.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {

			@Override
			public void changed(ObservableValue<? extends Tab> observable, Tab oldTab, Tab newTab) {
				if (newTab == null) {
                    registerName.setText("");
                    fileType.setText("");
                    return;
                }
                registerName.setText(((AppTab) newTab).getName());
                fileType.setText(((AppTab) newTab).getFileType());
			}
        });

        tabs.getSelectionModel().selectedItemProperty().addListener((ov, oldTab, newTab) -> {
            selectedTab = (AppTab) newTab;
        });

        selectedItem = (SaveTreeItem) registers.getRoot();
        selectedDirectory = selectedItem;
    }

    @FXML
    private void lockSavePressed() {
        Alert alert = new Alert(AlertType.CONFIRMATION, "Save gets closed. Are you sure you want to leave?\nAttention: Don't forget to save!",
                ButtonType.CANCEL, ButtonType.YES);
        alert.initModality(Modality.WINDOW_MODAL);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            getScene().getStage().setScene(new LoginScene(getScene().getStage().getApp()));
            ((SaveScene) getScene()).getSave().close();
        }

        if (alert.getResult() == ButtonType.CANCEL) {
            alert.close();
        }
    }

    @FXML
    private void addRegister() {
        NewRegisterStage stage = new NewRegisterStage(getScene().getStage().getApp(), (SaveScene) getScene());
        stage.start();
    }

    @FXML
    private void deleteRegister() {
        Alert alert = new Alert(AlertType.CONFIRMATION, "Delete \"" + selectedItem.getName() + "\"?", 
            ButtonType.CANCEL, ButtonType.YES);
        alert.initModality(Modality.WINDOW_MODAL);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            ((SaveScene) getScene()).removeRegister();
        }

        if (alert.getResult() == ButtonType.CANCEL) {
            alert.close();
        }
    }

    @FXML
    private void renameRegister() {
        Stage stage = new RenameRegisterStage(getScene().getStage().getApp(), (SaveScene) getScene());
        stage.show();
    }

    @FXML
    private void importRegister() {
        Stage stage = new ImportRegisterStage(getScene().getStage().getApp(), (SaveScene) getScene());
        stage.show();
    }

    @FXML
    private void exportRegister() {
        Stage stage = new ExportRegisterStage(getScene().getStage().getApp(), (SaveScene) getScene());
        stage.show();
    }

    @FXML
    private void createBackup() {
        Stage stage = new CreateBackupStage(getScene().getStage().getApp(), (SaveScene) getScene());
        stage.show();
    }

    @FXML
    private void loadBackup() {
        Stage stage = new LoadBackupStage(getScene().getStage().getApp(), (SaveScene) getScene());
        stage.show();
    }

    @FXML
    private void selectTheme() {
        ThemeSelectorStage stage = new ThemeSelectorStage(getScene().getStage().getApp());
        stage.start();
    }

    public void saveCurrentTab() {
        if (selectedTab == null || selectedTab.isSaved())
            return;

        Save save = ((SaveScene) getScene()).getSave();


        String path = selectedTab.getSaveTreeItem().calculatePath();
        System.out.println(selectedTab.getSaveTreeItem().getName());
        Register register = new Register(path);
        register.read();
        save.openRegister(register);

        ((AppTab)tabs.getSelectionModel().getSelectedItem()).save(action -> {

            register.setContent(action);
            save.saveRegister(register);
        });
    }

    public void openTab() {
        if (selectedItem.getType().equals(Register.DIRECTORY))
            return;
            
        getScene().getStage().applyLoadingScene(action -> {
            action.setText("Opening...");
            Register register = new Register(selectedItem.calculatePath());;
            register.read();
            ((SaveScene) getScene()).getSave().openRegister(register);

            AppTab tab = SaveScene.getCorrectTab((SaveScene)getScene(), register, selectedItem);
            
            action.endNow(endingAction -> {
                tabs.getTabs().add(tab);
                tabs.getSelectionModel().select(tab);
            });
        });
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