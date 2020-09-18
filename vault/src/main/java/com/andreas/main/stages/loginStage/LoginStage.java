package com.andreas.main.stages.loginStage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import com.andreas.main.FileUtils;
import com.andreas.main.app.AppStage;
import com.andreas.main.save.Save;
import com.andreas.main.stages.StageUtils;

import javafx.application.Application;

public class LoginStage extends AppStage {

    public LoginStage(Application app) {
        super(app, "stages/loginStage/login.fxml");
        setTitle("Login");

        init();

        loadSaves();
    }

    public void addSave(Save save) {
        LoginController loginController = (LoginController) getController();
        loginController.savesList.getItems().add(save.getName());
        save.write();
    }

    public void renameSave(Save save, int index, String name) {
        LoginController loginController = (LoginController) getController();
        loginController.savesList.getItems().set(index, name);
        loginController.saveName.setText(name);
        FileUtils.renamePath(StageUtils.SAVES_PATH + "/" + save.getName(), name);
        save.setName(name);
        save.write();
    }

    public void removeSave(Save save, int index) {
        LoginController loginController = (LoginController) getController();
        loginController.savesList.getItems().remove(index);
        loginController.savesList.getSelectionModel().select(loginController.savesList.getItems().size() - 1);
        loginController.saveName.setText(loginController.savesList.getSelectionModel().getSelectedItem());
        FileUtils.deleteDirectory(StageUtils.SAVES_PATH + save.getName());
    }

    public void loadSaves() {
        Path path = Paths.get(StageUtils.SAVES_PATH);
        if (Files.isDirectory(path)) {
            try {
                Stream<Path> list = Files.list(path);
                list.forEach(e -> {
                    if (Files.isDirectory(e))
                        ((LoginController)getController()).savesList.getItems().add(e.getFileName().toString());
                });
                list.close();
            } catch (IOException e) {
            e.printStackTrace();
            }
        }
    }
}