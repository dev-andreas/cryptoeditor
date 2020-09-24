package com.andreas.main.stages.saveStage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import com.andreas.main.FileUtils;
import com.andreas.main.app.AppStage;
import com.andreas.main.save.Register;
import com.andreas.main.save.Save;
import com.andreas.main.save.SaveTreeItem;
import com.andreas.main.stages.StageUtils;

import javafx.application.Application;

public class SaveStage extends AppStage {

    private Save save;

    public SaveStage(Application app, Save save) {
        super(app, "stages/saveStage/save.fxml");
        this.save = save;

        setTitle(save.getName());

        loadRegisters();
        init();
    }

    public Save getSave() {
        return save;
    }

    public void addRegister(String name, String type) {
        SaveTreeItem item = new SaveTreeItem(getSave());
        item.setName(name);
        item.setType(type);
        ((SaveController)getController()).selectedDirectory.addChild(item);


        if (item.getType().equals(Register.DIRECTORY)) {
            FileUtils.createDirectories(item.calculatePath());
        } else {
            Register register = new Register(item.calculatePath());
            register.setName(item.getName());
            register.setFileType(item.getType());
            getSave().saveRegister(register);
        }
    }

    public void addRegister(Register register) {
        if (!register.isOpen())
            return;
        
        SaveTreeItem item = new SaveTreeItem(getSave());
        item.setName(register.getName());
        item.setType(register.getFileType());
        ((SaveController)getController()).selectedDirectory.addChild(item);

        if (item.getType().equals(Register.DIRECTORY)) {
            FileUtils.createDirectories(item.calculatePath());
        } else {
            getSave().saveRegister(register);
        }
    }

    public void renameRegister(String name, String type) {
        SaveController saveController = (SaveController)getController();
        SaveTreeItem item = saveController.selectedItem;

        if (!item.getType().equals(Register.DIRECTORY)) {
            Register register = new Register(item.calculatePath());
            register.read();
            getSave().openRegister(register);
            register.setName(name);
            register.setFileType(type);
            getSave().saveRegister(register);
            register.close();
        }

        String nameCipher = FileUtils.bytesToHex(getSave().encrypt(name.getBytes(StandardCharsets.UTF_8)));
        FileUtils.renamePath(item.calculatePath(), nameCipher);

        item.setName(name);
        item.setType(type);
    }

    public void removeRegister() {
        SaveController saveController = (SaveController)getController();
        SaveTreeItem item = saveController.selectedItem;
        if (item.getType().equals(Register.DIRECTORY)) {
            FileUtils.deleteDirectory(item.calculatePath());
        } else {
            FileUtils.deleteFile(item.calculatePath());
        }
        item.getParent().getChildren().remove(item);
    }

    public void loadRegisters() {
        save.setRoot(new SaveTreeItem(save));
        save.getRoot().setName("registers");
        save.getRoot().setType(Register.DIRECTORY);
        ((SaveController)getController()).registers.setRoot(getSave().getRoot());

        Path path = Paths.get(StageUtils.SAVES_PATH + getSave().getName() + "/registers");
        loadDirectory(path, getSave().getRoot());
    }

    public void loadDirectory(Path path, SaveTreeItem root) {
        if (Files.isDirectory(path)) {
            try {
                Stream<Path> list = Files.list(path);
                list.forEach(e -> {
                    SaveTreeItem child = new SaveTreeItem(getSave());

                    byte[] cipher = FileUtils.hexToBytes(e.getFileName().toString());
                    String name = new String(getSave().decrypt(cipher), StandardCharsets.UTF_8);
                    String[] nameAndType = FileUtils.splitFileNameAndType(name);

                    child.setName(nameAndType[0]);
                    child.setType(nameAndType[1]);

                    root.addChild(child);

                    if (Files.isDirectory(e))
                        loadDirectory(e, child);
                });
                list.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
