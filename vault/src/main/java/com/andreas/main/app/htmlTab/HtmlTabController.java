package com.andreas.main.app.htmlTab;

import com.andreas.main.app.AppController;
import com.andreas.main.save.Register;

import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;

public class HtmlTabController extends AppController {

    @FXML
    public StackPane pane;

    private Register register;
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

    public Register getRegister() {
        return register;
    }

    public void setRegister(Register register) {
        this.register = register;
    }

    public HtmlTab getHtmlTab() {
        return htmlTab;
    }

    public void setHtmlTab(HtmlTab htmlTab) {
        this.htmlTab = htmlTab;
    }
}
