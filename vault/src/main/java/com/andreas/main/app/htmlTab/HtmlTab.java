package com.andreas.main.app.htmlTab;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

import com.andreas.main.app.AppScene;
import com.andreas.main.app.AppTab;
import com.andreas.main.save.Register;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import javafx.scene.web.HTMLEditor;

public class HtmlTab extends AppTab {

    private HtmlTabController controller;

    private HTMLEditor htmlEditor;
    private TextArea textArea;

    public HtmlTab(AppScene appScene, Register register) {
        super(appScene, register, true);
        init(appScene);
    }

    public HtmlTab(AppScene appScene, Path path) {
        super(appScene, path);
        init(appScene);
    }

    private void init(AppScene appScene) {
        AppScene content = new AppScene(appScene.getApp(), "app/htmlTab/htmlTab.fxml", "");
        
        content.setStage(appScene.getStage());
        controller = ((HtmlTabController) content.getController());
        controller.setHtmlTab(this);
        content.init();

        setContent(content.getRoot());

        String registerContent = new String(data, StandardCharsets.UTF_8);
        
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                htmlEditor = new HTMLEditor();
                htmlEditor.setHtmlText(registerContent);

                textArea = new TextArea();
                textArea.setText(registerContent);

                htmlEditor.setOnKeyReleased(new EventHandler<KeyEvent>() {
                    @Override
                    public void handle(KeyEvent event) {
                        if (!textArea.getText().equals(htmlEditor.getHtmlText())) {
                            textArea.setText(htmlEditor.getHtmlText());
                            unsave();
                        }
                    }
                });

                textArea.setOnKeyReleased(new EventHandler<KeyEvent>() {
                    @Override
                    public void handle(KeyEvent event) {
                        if (!htmlEditor.getHtmlText().equals(textArea.getText())) {
                            htmlEditor.setHtmlText(textArea.getText());
                            unsave();
                        }
                    }
                });
            }
        });
    }

    @Override
    protected byte[] getNewContent() {
        return textArea.getText().getBytes(StandardCharsets.UTF_8);
    }

    // GETTERS AND SETTERS

    public HTMLEditor getHtmlEditor() {
        return htmlEditor;
    }

    public TextArea getTextArea() {
        return textArea;
    }
}
