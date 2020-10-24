package com.andreas.main.app.imageTab;

import com.andreas.main.app.AppScene;
import com.andreas.main.app.RegisterTab;
import com.andreas.main.save.Register;
import com.andreas.main.stages.mainStage.scenes.saveScene.SaveScene;


public class ImageTab extends RegisterTab {

    public ImageTab(SaveScene saveScene, Register register) {
        super(saveScene, register, false);

        AppScene content = new AppScene(saveScene.getApp(), "app/imageTab/imageTab.fxml", "");
        content.setStage(saveScene.getStage());
        ((ImageTabController) content.getController()).setRegister(register);
        content.init();

        setContent(content.getRoot());
    }

    @Override
    protected byte[] getNewContent() {
        return null;
    }
}
