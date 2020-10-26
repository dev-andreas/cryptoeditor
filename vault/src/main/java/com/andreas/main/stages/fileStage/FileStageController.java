package com.andreas.main.stages.fileStage;

import java.nio.file.Path;

import com.andreas.main.FileUtils;
import com.andreas.main.app.AppController;
import com.andreas.main.app.AppTab;
import com.andreas.main.stages.themeSelectorStage.ThemeSelectorStage;

import javafx.fxml.FXML;
import javafx.scene.control.TabPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.stage.FileChooser;

public class FileStageController extends AppController {

    @FXML
    public TabPane pane;

    private Path filePath;
    private FileStage fileStage;

    @Override
    public void init() {

        // shortcuts  
        getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.O, KeyCombination.SHORTCUT_DOWN), () -> {
            openFile();
        });
         
        getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.S, KeyCombination.SHORTCUT_DOWN), () -> {
            saveFile();
        });

        getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.P, KeyCombination.SHORTCUT_DOWN), () -> {
            printFile();
        });

        fileStage = (FileStage)getScene().getStage();
        filePath = fileStage.getFile();

        AppTab tab = FileStage.getCorrectTab(getScene(), filePath);

        pane.getTabs().add(tab);
    }

    @FXML
    private void openFile() {
        FileChooser fc = new FileChooser();
        Path file = fc.showOpenDialog(null).toPath();

        AppTab tab = FileStage.getCorrectTab(getScene(), file);

        pane.getTabs().add(tab);
    }

    @FXML
    private void openInNewWindow() {
        FileChooser fc = new FileChooser();
        Path file = fc.showOpenDialog(null).toPath();


        FileStage stage = new FileStage(getScene().getStage().getApp(), file);
        stage.start();
    }

    @FXML
    private void saveFile() {
        AppTab tab = (AppTab)pane.getSelectionModel().getSelectedItem();

        tab.save(data -> {
            FileUtils.createBinaryFile(filePath.toString(), data);
        });
    }

    @FXML
    private void saveFileTo() {
        FileChooser fc = new FileChooser();
        Path file = fc.showOpenDialog(null).toPath();

        AppTab tab = (AppTab)pane.getSelectionModel().getSelectedItem();

        tab.save(data -> {
            FileUtils.createBinaryFile(file.toString(), data);
        });
    }

    @FXML
    private void printFile() {

    }

    @FXML
    private void setTheme() {
        ThemeSelectorStage stage = new ThemeSelectorStage(getScene().getStage().getApp());
        stage.start();
    }

    @FXML
    private void viewSourceCode() {

    }
}
