package com.andreas.main.stages.mainStage.scenes.saveScene;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import com.andreas.main.FileUtils;
import com.andreas.main.app.AppScene;
import com.andreas.main.app.AppTab;
import com.andreas.main.app.BinaryTab;
import com.andreas.main.app.TextTab;
import com.andreas.main.app.htmlTab.HtmlTab;
import com.andreas.main.app.imageTab.ImageTab;
import com.andreas.main.save.Register;
import com.andreas.main.save.Save;
import com.andreas.main.save.SaveTreeItem;
import com.andreas.main.stages.StageUtils;

import javafx.application.Application;

public class SaveScene extends AppScene {

    public static AppTab getCorrectTab(AppScene appScene, Register register) {
        if (!register.isOpen())
            return null;
        
        if (FileUtils.seemsBinary(register.getContent())) {
            for (int i = 0; i < Register.ACCEPTED_IMAGE_FILE_TYPES.length; i++) {
                if (Register.ACCEPTED_IMAGE_FILE_TYPES[i].equals(register.getFileType().toLowerCase()))
                    return new ImageTab(appScene, register);
            }
            /*
            for (int i = 0; i < Register.ACCEPTED_MEDIA_FILE_TYPES.length; i++) {
                if (Register.ACCEPTED_MEDIA_FILE_TYPES[i].equals(register.getFileType().toLowerCase()))
                    return new MediaTab(saveScene, register);
            }
            */
            return new BinaryTab(appScene, register);
        } else {
            for (int i = 0; i < Register.ACCEPTED_HTML_FILE_TYPES.length; i++) {
                if (Register.ACCEPTED_HTML_FILE_TYPES[i].equals(register.getFileType().toLowerCase()))
                    return new HtmlTab(appScene, register);
            }
            return new TextTab(appScene, register);
        }
    }

    // CLASS

    private Save save;

    public SaveScene(Application app, Save save) {
        super(app, "stages/mainStage/scenes/saveScene/save.fxml", "cryptoeditor | " + save.getName());
        
        this.save = save;
        loadRegisters();
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

        String filePath = item.calculatePath();
        String nameCipher = FileUtils.bytesToHex(getSave().encrypt((name + type).getBytes(StandardCharsets.UTF_8)));

        if (!item.getType().equals(Register.DIRECTORY)) {
            Register register = new Register(item.calculatePath());
            register.read();
            getSave().openRegister(register);
            register.setName(name);
            register.setFileType(type);
            getSave().saveRegister(register);
            register.close();

            FileUtils.renamePath(filePath + Register.PROPERTIES_SUFFIX, nameCipher + Register.PROPERTIES_SUFFIX);
            FileUtils.renamePath(filePath + Register.CONTENT_SUFFIX, nameCipher + Register.CONTENT_SUFFIX);
        } else {
            FileUtils.renamePath(filePath, nameCipher);
        }

        item.setName(name);
        item.setType(type);
    }

    public void removeRegister() {
        SaveController saveController = (SaveController)getController();
        SaveTreeItem item = saveController.selectedItem;
        if (item.getType().equals(Register.DIRECTORY)) {
            FileUtils.deleteDirectory(item.calculatePath());
        } else {
            String filePath = item.calculatePath();
            FileUtils.deleteFile(filePath + Register.PROPERTIES_SUFFIX);
            FileUtils.deleteFile(filePath + Register.CONTENT_SUFFIX);
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
                    String[] nameAndType = FileUtils.splitPrefixAndSuffix(e.getFileName().toString());
                    if (nameAndType[1].equals(Register.CONTENT_SUFFIX))
                        return;

                    SaveTreeItem child = new SaveTreeItem(getSave());

                    byte[] cipher = FileUtils.hexToBytes(nameAndType[0]);
                    String name = new String(getSave().decrypt(cipher), StandardCharsets.UTF_8);
                    
                    nameAndType = FileUtils.splitPrefixAndSuffix(name);

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

    public Save getSave() {
        return save;
    }
}
