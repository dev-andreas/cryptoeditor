package com.andreas.main.stages.themeSelectorStage;

import com.andreas.main.app.AppScene;
import com.andreas.main.app.AppStage;

import javafx.application.Application;
import javafx.stage.Modality;

public class ThemeSelectorStage extends AppStage {

    public ThemeSelectorStage(Application app) {
        super(app);
        setResizable(false);
        initModality(Modality.APPLICATION_MODAL);

        setScene(new AppScene(app, "stages/themeSelectorStage/themeSelector.fxml", "Select stylesheet"));
    }  

    public void applyTheme(String path) {
    }
}
