package com.andreas.main.stages;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.Modality;

/**
 * This is a util class for JavaFX stages.
 * @author Andreas Gerasimow.
 * @version 1.0.
 */
public class StageUtils {

    public static final String DATA_FILE_PATH = "data/data.xml";
    public static final String SAVES_PATH = "data/saves/";

    /**
     * This method pushes a simple <code>javafx.scene.control.Alert</code> warning with a string.
     * @param text The text to push.
     */
    public static void pushNotification(String text) {
        Alert alert = new Alert(AlertType.WARNING, text, ButtonType.OK);
            alert.initModality(Modality.WINDOW_MODAL);
            alert.showAndWait();
    }

    /**
     * This method will copy a string to the clipboard.
     * @param text The string to copy.
     */
    public static void CopyTextToClipboard(String text) {
        ClipboardContent content = new ClipboardContent();
        content.putString(text);

        Clipboard clipboard = Clipboard.getSystemClipboard();
        clipboard.setContent(content);

    }
}
