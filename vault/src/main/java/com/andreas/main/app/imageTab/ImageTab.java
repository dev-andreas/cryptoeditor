package com.andreas.main.app.imageTab;

import java.net.MalformedURLException;
import java.nio.file.Path;

import com.andreas.main.app.AppScene;
import com.andreas.main.app.AppTab;
import com.andreas.main.save.Register;
import com.andreas.main.temp.TempHandler;
import com.andreas.main.temp.TempUtils;

import javafx.scene.image.Image;


public class ImageTab extends AppTab {

    
    public ImageTab(AppScene appScene, Register register) {
        super(appScene, register, false);

        TempHandler tempHandler = TempUtils.createTempFile(data, true);
        Image image = null;
        try {
            image = new Image(tempHandler.getTempPath().toAbsolutePath().toUri().toURL().toExternalForm());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        tempHandler.orderDelete();

        init(appScene, image);
    }

    public ImageTab(AppScene appScene, Path path) {
        super(appScene, path);

        Image image = null;
        try {
            image = new Image(path.toAbsolutePath().toUri().toURL().toExternalForm());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        init(appScene, image);
    }

    private void init(AppScene appScene, Image image) {
        AppScene content = new AppScene(appScene.getApp(), "app/imageTab/imageTab.fxml", "");
        content.setStage(appScene.getStage());
        ((ImageTabController) content.getController()).setImageFile(image);
        content.init();

        setContent(content.getRoot());
    }

    @Override
    protected byte[] getNewContent() {
        return null;
    }
}
