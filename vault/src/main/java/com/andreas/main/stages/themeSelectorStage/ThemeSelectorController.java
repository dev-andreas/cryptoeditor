package com.andreas.main.stages.themeSelectorStage;

import java.io.File;

import com.andreas.main.App;
import com.andreas.main.FileUtils;
import com.andreas.main.app.AppController;
import com.andreas.main.stages.StageUtils;

import org.jdom2.Element;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.stage.FileChooser;

public class ThemeSelectorController extends AppController {

    @FXML
    public ComboBox<String> themeSelector;

    @Override
    public void init() {
        FileUtils.forEachFile("data/themes", path -> {
            themeSelector.getItems().add(path.getFileName().toString());
        });

        Element root = FileUtils.readXmlFile("data/data.xml");
        themeSelector.getEditor().setText(root.getChildText("theme"));
    }

    @FXML
    private void importTheme() {
        FileChooser fc = new FileChooser();
        File file = fc.showOpenDialog(null);

        String suffix = FileUtils.splitPrefixAndSuffix(file.getName())[1];
        if (!suffix.toLowerCase().equals(".css")){
            StageUtils.pushNotification("Theme must be a .css file!");
            return;
        }

        FileUtils.copyFile(file.getAbsolutePath(), "data/themes");
        getScene().getStage().stop();
    }

    @FXML
    private void applyTheme() {
        setTheme(themeSelector.getSelectionModel().getSelectedItem());
        getScene().getStage().stop();
    }

    private void setTheme(String name) {
        Element root = FileUtils.readXmlFile("data/data.xml");
        Element theme = root.getChild("theme");
        if (theme == null) {
            theme = new Element("theme");
            root.getChildren().add(theme);
        }
        theme.setText(name);

        App.stages.forEach(stage -> {
            stage.applyStylesheet(name);
        });

        FileUtils.createXmlFile("data/data.xml", root);
    }

    @FXML
    private void cancel() {
        getScene().getStage().stop();
    }
}
