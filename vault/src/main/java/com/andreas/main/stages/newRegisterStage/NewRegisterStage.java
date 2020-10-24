package com.andreas.main.stages.newRegisterStage;

import com.andreas.main.app.AppScene;
import com.andreas.main.app.AppStage;
import com.andreas.main.stages.mainStage.scenes.saveScene.SaveScene;

import javafx.application.Application;
import javafx.stage.Modality;

public class NewRegisterStage extends AppStage {

    private SaveScene saveScene;

    public NewRegisterStage(Application app, SaveScene saveScene) {
        super(app);
        setResizable(false);
        initModality(Modality.APPLICATION_MODAL);

        this.saveScene = saveScene;
        setScene(new AppScene(app, "stages/newRegisterStage/newRegister.fxml", "New register"));
    }
    
    public SaveScene getSaveScene() {
        return saveScene;
    } 
}