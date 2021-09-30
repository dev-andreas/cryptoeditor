package com.andreas.main;

import java.util.ArrayList;
import java.util.List;
import com.andreas.main.app.AppStage;
import com.andreas.main.stages.mainStage.MainStage;
import com.andreas.main.temp.TempUtils;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * @author Andreas Gerasimow.
 */
public final class App extends Application {

    public static List<AppStage> stages = new ArrayList<>();

    /**
     * This method starts the app.
     */
    @Override
    public void start(Stage stage) throws Exception {
        TempUtils.init();

        stage = new MainStage(this);
        ((AppStage)stage).start();
    }
}
