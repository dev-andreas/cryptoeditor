package com.andreas.main.app;

import com.andreas.main.save.Register;
import com.andreas.main.stages.mainStage.scenes.saveScene.SaveScene;

import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class BinaryTab extends RegisterTab {

    private VBox box;
    private Text text;

    public BinaryTab(SaveScene saveScene, Register register) {
        super(saveScene, register, false);

        text = new Text("This register seems binary and thus can't be opened.");
        text.setFill(new Color(.60, .60, .60, 1));
        text.setStyle("-fx-font-size: 16;");

        box = new VBox();
        box.setAlignment(Pos.CENTER);
        box.getChildren().add(text);
        
        setContent(box);
    }

    @Override
    protected byte[] getNewContent() {
        return null;
    }
}
