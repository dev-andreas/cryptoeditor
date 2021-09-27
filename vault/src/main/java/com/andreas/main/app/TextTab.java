package com.andreas.main.app;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

import com.andreas.main.save.Register;
import com.andreas.main.save.SaveTreeItem;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextArea;

public class TextTab extends AppTab {

    private TextArea textArea;

    public TextTab(Register register, SaveTreeItem saveTreeItem) {
        super(register, true, saveTreeItem);
        init();
    }

    public TextTab(Path path) {
        super(path);
        init();        
    }

    private void init() {
        textArea = new TextArea();
        textArea.setText(new String(data, StandardCharsets.UTF_8));

        setContent(textArea);

        textArea.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                unsave();
            }
        });
    }

    @Override
    protected byte[] getNewContent() {
        return textArea.getText().getBytes(StandardCharsets.UTF_8);
    }
}
