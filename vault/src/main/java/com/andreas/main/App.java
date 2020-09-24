package com.andreas.main;

import com.andreas.main.save.Save;
import com.andreas.main.stages.StageUtils;
import com.andreas.main.stages.loadBackupStage.LoadBackupController;
import com.andreas.main.stages.loginStage.LoginStage;
import com.andreas.main.stages.saveStage.SaveStage;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * @author Andreas Gerasimow.
 */
public final class App extends Application {

    /**
     * This method starts the program.
     */
    @Override
    public void start(Stage stage) throws Exception {

        /*
        Save save = new Save();
        save.read(StageUtils.SAVES_PATH + "TestingSave/backups/TestingSave1600795498549.bcp");
        save.open("C:/Users/andre/Desktop/myKey.xml", "Password1");

        stage = new SaveStage(this, save);
        stage.show();
        */

        stage = new LoginStage(this);
        stage.show();
    }
}
