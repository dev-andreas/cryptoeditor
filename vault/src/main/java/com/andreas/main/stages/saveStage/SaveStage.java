package com.andreas.main.stages.saveStage;

import com.andreas.main.App;
import com.andreas.main.app.AppStage;
import com.andreas.main.save.Register;
import com.andreas.main.save.Save;

public class SaveStage extends AppStage {

    private Save save;

    public SaveStage(App app, Save save) {
        super(app, "stages/saveStage/save.fxml");

        this.save = save;

        setTitle(save.getName());

        init();

        loadRegisters();
    }

    public Save getSave() {
        return save;
    }

    public void addRegister(Register register, String saveName) {
        save.getRegisters().add(register);
        ((SaveController)getController()).registers.getItems().add(saveName);
        ((SaveController)getController()).savedState.setText("Unsaved changes!");
    }

    public void renameRegister(int index, String name) {
        save.getRegisters().get(index).setName(name);
        ((SaveController)getController()).registers.getItems().set(index, name);
        ((SaveController)getController()).savedState.setText("Unsaved changes!");
    }

    private void loadRegisters() {
        for (Register r : save.getRegisters()) {
            ((SaveController)getController()).registers.getItems().add(r.getName());
        }
    }
}