package com.andreas.main.app;

import java.nio.file.Path;

import com.andreas.main.save.Register;
import com.andreas.main.save.SaveTreeItem;

import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public abstract class BinaryTab extends AppTab {

    private VBox box;
    private Text text;

    public BinaryTab(Register register, SaveTreeItem saveTreeItem) {
        super(register, false, saveTreeItem);

        init();
    }

    public BinaryTab(Path path) {
        super(path);

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
