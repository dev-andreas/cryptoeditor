package com.andreas.main.app;

import java.nio.file.Path;
import java.nio.file.Paths;

import com.andreas.main.FileUtils;
import com.andreas.main.save.Register;
import com.andreas.main.stages.StageUtils;
import com.andreas.main.stages.mainStage.scenes.saveScene.SaveScene;

import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class BinaryTab extends RegisterTab {

    private SaveScene saveScene;

    private VBox box;
    private Text text;

    public BinaryTab(SaveScene saveScene, Register register) {
        super(saveScene, register, false);

        this.saveScene = saveScene;

        text = new Text("This register appears to be binary and is therefore not readable!");
        text.setFill(new Color(.60, .60, .60, 1));
        text.setStyle("-fx-font-size: 16;");

        box = new VBox();
        box.setAlignment(Pos.CENTER);
        box.getChildren().add(text);
        
        setContent(box);
    }

    public void open() {
        saveScene.getStage().applyLoadingScene(action -> {
            FileUtils.createDirectories(StageUtils.SAVES_PATH + "temp");
            Path path = Paths.get(StageUtils.SAVES_PATH + "temp/" + System.nanoTime() + register.getFileType());
            FileUtils.createBinaryFile(path.toString(), register.getContent());
        });
    }

    @Override
    protected byte[] getNewContent() {
        return null;
    }
}
