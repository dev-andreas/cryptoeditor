package com.andreas.main.stages.importRegisterStage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import com.andreas.main.FileUtils;
import com.andreas.main.app.AppController;
import com.andreas.main.save.Register;
import com.andreas.main.save.SaveTreeItem;
import com.andreas.main.stages.StageUtils;
import com.andreas.main.stages.mainStage.scenes.saveScene.SaveController;
import com.andreas.main.stages.mainStage.scenes.saveScene.SaveScene;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.MouseEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;

public class ImportRegisterController extends AppController {

    @FXML
    public Label name;

    @FXML
    public Label type;

    @FXML
    public Label toggleText;

    @FXML
    public TextField filePath;

    @FXML
    public ToggleButton deleteFile;

    private SaveScene saveScene;
    private SaveController saveController;

    @Override
    public void init() {
        // Shortcuts       
        getScene().getAccelerators().put(new KeyCodeCombination(KeyCode.ENTER), () -> {
            importPressed(null);
        });

        deleteFile.setSelected(true);

        saveScene = ((ImportRegisterStage)getScene().getStage()).getSaveScene();
        saveController = (SaveController) saveScene.getController();
    }

    public void importPressed(MouseEvent event) {
        if (filePath.getText().isEmpty())
            return;

        Path path = Paths.get(filePath.getText());

        if (!Files.exists(path))
            return;
        
        SaveTreeItem root = new SaveTreeItem(saveScene.getSave());

        if (saveController.nameExists(name.getText() + type.getText())) {
            StageUtils.pushNotification("Name \"" + name.getText() + type.getText() + "\"already exists!");
            return;
        }

        getScene().getStage().applyLoadingScene(action -> {
            action.setText("Importing register...");
            saveController.selectedDirectory.addChild(root);
            loadDirectory(path, root);
    
            if (deleteFile.isSelected()) {
                if (root.getType().equals(Register.DIRECTORY))
                    FileUtils.deleteDirectory(path.toString());
                else 
                    FileUtils.deleteFile(path.toString());
            }
            action.endNow(endingAction -> { getScene().getStage().stop(); });
        });
    }

    public void browsePressed(MouseEvent event) {
        ButtonType fileChoose = new ButtonType("Import file");
        ButtonType directoryChoose = new ButtonType("Import directory");
        Alert alert = new Alert(AlertType.CONFIRMATION, "Do you want to imoport a file or a directory?", 
        fileChoose, directoryChoose);

        alert.initModality(Modality.WINDOW_MODAL);

        alert.showAndWait();

        if (alert.getResult() == fileChoose) {
            FileChooser fc = new FileChooser();
            File file = fc.showOpenDialog(null);
            filePath.setText(file.getAbsolutePath());
            String[] fileName = FileUtils.splitPrefixAndSuffix(file.getName());
            name.setText(fileName[0]);
            type.setText(fileName[1]);
        }

        if (alert.getResult() == directoryChoose) {
            DirectoryChooser dc = new DirectoryChooser();
            File file = dc.showDialog(null);
            filePath.setText(file.getAbsolutePath());
            name.setText(file.getName());
            type.setText(Register.DIRECTORY);
        }
    }

    public void togglePressed(MouseEvent event) {
        toggleText.setText(deleteFile.isSelected() ? "Yes" : "No");
    }
    
    public void cancelPressed(MouseEvent event) {
        getScene().getStage().stop();
    }

    private void loadDirectory(Path path, SaveTreeItem root) {

        if (Files.isDirectory(path)) {
            
            root.setName(path.getFileName().toString());
            root.setType(Register.DIRECTORY);
            FileUtils.createDirectories(root.calculatePath());

            try {
                Stream<Path> list = Files.list(path);
                list.forEach(e -> {
                    SaveTreeItem child = new SaveTreeItem(saveScene.getSave());
                    root.addChild(child);
                    loadDirectory(e, child);
                });
    
                list.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            String[] nameAndType = FileUtils.splitPrefixAndSuffix(path.getFileName().toString());
            root.setName(nameAndType[0]);
            root.setType(nameAndType[1]);

            Register register = new Register(root.calculatePath());
            register.readFromFile(path.toString());
            saveScene.getSave().saveRegister(register);
        }
    }
}
