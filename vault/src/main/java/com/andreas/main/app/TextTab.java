package com.andreas.main.app;

import java.nio.charset.StandardCharsets;

import com.andreas.main.save.Register;
import com.andreas.main.stages.mainStage.scenes.saveScene.SaveScene;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextArea;

public class TextTab extends RegisterTab {

    private TextArea textArea;

    public TextTab(SaveScene saveScene, Register register) {
        super(saveScene, register, true);

        textArea = new TextArea();
        textArea.setText(new String(register.getContent(), StandardCharsets.UTF_8));

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
