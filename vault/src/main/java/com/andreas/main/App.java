package com.andreas.main;

import com.andreas.main.stages.loginStage.LoginStage;

import javafx.application.Application;
import javafx.stage.Stage;

public final class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        stage = new LoginStage(this);
        stage.show();
    }
}
