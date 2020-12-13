package com.andreas.main.scenes.loadingScene;

import java.util.function.Consumer;

import com.andreas.main.app.AppScene;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.BorderPane;

public class LoadingScene extends AppScene {

    public static Consumer<LoadingScene> noneAction = new Consumer<LoadingScene>() {

        @Override
        public void accept(LoadingScene t) { }
    };

    private Consumer<LoadingScene> action, postAction, endingAction;

    private LoadingService service;

    private LoadingController loadingController;

    public LoadingScene(Application app, Consumer<LoadingScene> action) {
        super(app, "scenes/loadingScene/loading.fxml", "");
        this.action = action;

        postAction = noneAction;
        endingAction = noneAction;

        loadingController = (LoadingController) getController();

        loadingController.fade(0.75, new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event) {
                
            }
        });
    }
    
    @Override
    public void init() {
        super.init();
        BorderPane root = (BorderPane) getRoot();
        root.setPrefSize(getStage().getWidth(), getStage().getHeight());


        service = new LoadingService(this, action);
        service.start();

        service.setOnSucceeded(state -> {endLoading();});
        service.setOnCancelled(state -> {endLoading();});
        service.setOnFailed(state -> {endLoading();});
        service.setOnScheduled(state -> {endLoading();});
    }

    public void endLoading() {
        getStage().popScene();
        postAction.accept(this);
        endingAction.accept(this);
    }

    public void endNow(Consumer<LoadingScene> endingAction) {
        this.endingAction = endingAction;
        service.cancel();
    }

    // GETTERS AND SETTERS

    public LoadingService getService() {
        return service;
    }

    public void setPostAction(Consumer<LoadingScene> postAction) {
        this.postAction = postAction;
    }
    
    public void setProgress(double value) {
        ((LoadingController)getController()).indicator.setProgress(value);
    }

    public void setText(String value) {
        ((LoadingController)getController()).text.setText(value);
    }
}
