package com.andreas.main.stages.createBackupStage;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Calendar;

import com.andreas.main.FileUtils;
import com.andreas.main.app.AppController;
import com.andreas.main.save.Save;
import com.andreas.main.stages.StageUtils;
import com.andreas.main.stages.mainStage.scenes.saveScene.SaveScene;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;

public class CreateBackupController extends AppController {

    @FXML
    private Label saveName;

    @FXML
    private Label backupName;

    @FXML
    private TextField filePath;

    private Save save;
    private SaveScene saveScene;

    @Override
    public void init() {
        saveScene = ((CreateBackupStage)getScene().getStage()).getSaveScene();
        save = saveScene.getSave();

        Calendar calendar = Calendar.getInstance();

        saveName.setText(save.getName());
        backupName.setText(save.getName() + calendar.getTimeInMillis() + ".bcp");
        filePath.setText(StageUtils.SAVES_PATH + save.getName() + "/backups");

        File recordsDir = new File(filePath.getText());
        if (! recordsDir.exists()) {
            recordsDir.mkdirs();
        }
    }

    public void browsePressed() {
        DirectoryChooser fc = new DirectoryChooser();
        File file = fc.showDialog(null);
        filePath.setText(file.getAbsolutePath());
    }

    public void createPressed() {
        if (filePath.getText().isEmpty())
            return;

        if (!Files.exists(Paths.get(filePath.getText()))) {
            StageUtils.pushNotification("Path does not exist!");
            return;
        }

        
        getScene().getStage().applyLoadingScene(action -> {
            action.setText("Creating backup...");

            if (!Files.exists(Paths.get(filePath.getText())))
                FileUtils.createDirectories(filePath.getText());

            FileUtils.zip(StageUtils.SAVES_PATH + save.getName(), filePath.getText() + "/" + backupName.getText(), "");

            action.endNow(endingAction -> {
                getScene().getStage().stop();
            });
        });
    }

    public void cancelPressed() {
        getScene().getStage().stop();
    }
}
