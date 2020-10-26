package com.andreas.main.app;

import java.nio.file.Path;

import com.andreas.main.save.Register;

import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class BinaryTab extends AppTab {

    private VBox box;
    private Text text;

    public BinaryTab(AppScene scene, Register register) {
        super(scene, register, false);

        init();
    }

    public BinaryTab(AppScene scene, Path path) {
        super(scene, path);

        init();
    }

    private void init() {
        text = new Text("This register appears to be binary and therefore not readable!");
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
