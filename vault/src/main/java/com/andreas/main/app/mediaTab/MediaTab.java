package com.andreas.main.app.mediaTab;

import java.net.MalformedURLException;
import java.nio.file.Path;

import com.andreas.main.app.AppScene;
import com.andreas.main.app.AppTab;
import com.andreas.main.save.Register;
import com.andreas.main.save.SaveTreeItem;

import javafx.scene.media.Media;

public class MediaTab extends AppTab {


    public MediaTab(AppScene appScene, Register register, SaveTreeItem saveTreeItem) {
        super(register, false, saveTreeItem);
        init(appScene, null);
    }

    public MediaTab(AppScene appScene, Path path) {
        super(path);
        Media media = null;
        try {
            media = new Media(path.toAbsolutePath().toUri().toURL().toExternalForm());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        init(appScene, media);
    }

    private void init(AppScene appScene, Media media) {
        AppScene content = new AppScene(appScene.getApp(), "app/mediaTab/mediaTab.fxml", "");
        content.setStage(appScene.getStage());
        ((MediaTabController) content.getController()).setMedia(media);
        content.init();

        setContent(content.getRoot());
    }

    @Override
    protected byte[] getNewContent() {
        return null;
    }
    
}
