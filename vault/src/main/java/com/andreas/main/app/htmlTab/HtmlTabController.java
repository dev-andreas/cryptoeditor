package com.andreas.main.app.htmlTab;

import com.andreas.main.app.AppController;

import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;

public class HtmlTabController extends AppController {

    @FXML
    public StackPane pane;

    private HtmlTab htmlTab;

    @Override
    public void init() {
    }


    @FXML
    private void setHtmlView() {
        pane.getChildren().clear();
        pane.getChildren().add(htmlTab.getHtmlEditor());
    }
    
    @FXML
    private void setPlainView() {
        pane.getChildren().clear();
        pane.getChildren().add(htmlTab.getTextArea());
    }

    // GETTERS AND SETTERS

    public HtmlTab getHtmlTab() {
        return htmlTab;
    }

    public void setHtmlTab(HtmlTab htmlTab) {
        this.htmlTab = htmlTab;
    }
}
