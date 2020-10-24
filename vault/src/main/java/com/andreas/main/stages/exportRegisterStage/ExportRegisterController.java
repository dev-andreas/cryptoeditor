package com.andreas.main.stages.exportRegisterStage;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.andreas.main.FileUtils;
import com.andreas.main.app.AppController;
import com.andreas.main.save.Register;
import com.andreas.main.save.SaveTreeItem;
import com.andreas.main.stages.mainStage.scenes.saveScene.SaveController;
import com.andreas.main.stages.mainStage.scenes.saveScene.SaveScene;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;

public class ExportRegisterController extends AppController {

    @FXML
    public Label name;

    @FXML
    public Label type;

    @FXML
    public TextField filePath;

    SaveScene saveScene;
    SaveController saveController;

    @Override
    public void init() {
        // Shortcuts      
        getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.ENTER), () -> {
            exportPressed(null);
        });
        
        saveScene = (SaveScene) ((ExportRegisterStage) getScene().getStage()).getSaveScene();
        saveController = (SaveController) saveScene.getController();

        name.setText(saveController.selectedItem.getName());
        type.setText(saveController.selectedItem.getType());
        filePath.setText(saveScene.getSave().getPrevExportDir());
    }
    
    public void exportPressed(MouseEvent event) {

        if (filePath.getText().isEmpty())
            return;

        if (Files.notExists(Paths.get(filePath.getText())))
            return;

        getScene().getStage().applyLoadingScene(action -> {
            action.setText("Exporting register...");
            exportRegister(saveController.selectedItem, filePath.getText());
            action.endNow(endingAction -> {getScene().getStage().stop();});
        });
    }

    public void cancelPressed(MouseEvent event) {
        getScene().getStage().stop();
    }

    public void browsePressed(MouseEvent event) {
        DirectoryChooser fc = new DirectoryChooser();
        File file = fc.showDialog(null);
        filePath.setText(file.getAbsolutePath());
    }

    private void exportRegister(SaveTreeItem item, String path) {
        if (item.getType().equals(Register.DIRECTORY)) {
            if (Files.exists(Paths.get(path + "/" + name.getText())))
                FileUtils.createDirectories(path + "/" + item.getName() + "I");
            else
                FileUtils.createDirectories(path + "/" + item.getName());
            item.forEachChild(e -> {
                exportRegister(e, path + "/" + item.getName());
            });
        } else {
            Register r = new Register(item.calculatePath());
            r.read();
            saveScene.getSave().openRegister(r);
            if (Files.exists(Paths.get(path + "/" + name.getText() + type.getText())))
                FileUtils.createBinaryFile(path + "/" + item.getName() + "I" + item.getType(), r.getContent());
            else
                FileUtils.createBinaryFile(path + "/" + item.getName() + item.getType(), r.getContent());
            r.close();
        }

        saveScene.getSave().setPrevExportDir(filePath.getText());

        saveScene.getSave().write();
    }
}
