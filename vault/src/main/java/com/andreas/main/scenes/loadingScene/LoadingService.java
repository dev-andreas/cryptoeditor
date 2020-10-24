package com.andreas.main.scenes.loadingScene;

import java.util.function.Consumer;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class LoadingService extends Service<LoadingScene> {

    private LoadingScene scene;
    private Consumer<LoadingScene> action;

    public LoadingService(LoadingScene scene, Consumer<LoadingScene> action) {
        this.scene = scene;
        this.action = action;
    }

    @Override
    protected Task<LoadingScene> createTask() {

        return new Task<LoadingScene>() {

            @Override
            protected LoadingScene call() throws Exception {
                action.accept(scene);
                scene.endLoading();
                return scene;
            }
        };
    }
}
