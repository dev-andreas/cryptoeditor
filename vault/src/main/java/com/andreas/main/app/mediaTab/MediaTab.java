package com.andreas.main.app.mediaTab;

import com.andreas.main.app.AppScene;
import com.andreas.main.app.RegisterTab;
import com.andreas.main.save.Register;
import com.andreas.main.stages.mainStage.scenes.saveScene.SaveScene;

public class MediaTab extends RegisterTab {

    public MediaTab(SaveScene saveScene, Register register) {
        super(saveScene, register, false);

        AppScene content = new AppScene(saveScene.getApp(), "app/mediaTab/mediaTab.fxml", "");
        content.setStage(saveScene.getStage());
        ((MediaTabController) content.getController()).setRegister(register);
        content.init();

        setContent(content.getRoot());
    }

    @Override
    protected byte[] getNewContent() {
        return null;
    }
    
}
