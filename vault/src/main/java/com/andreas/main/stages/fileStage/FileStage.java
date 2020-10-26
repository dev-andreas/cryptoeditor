package com.andreas.main.stages.fileStage;

import java.nio.file.Path;

import com.andreas.main.FileUtils;
import com.andreas.main.app.AppScene;
import com.andreas.main.app.AppStage;
import com.andreas.main.app.AppTab;
import com.andreas.main.app.BinaryTab;
import com.andreas.main.app.TextTab;
import com.andreas.main.app.htmlTab.HtmlTab;
import com.andreas.main.app.imageTab.ImageTab;
import com.andreas.main.app.mediaTab.MediaTab;
import com.andreas.main.save.Register;

import javafx.application.Application;

public class FileStage extends AppStage {

    public static AppTab getCorrectTab(AppScene appScene, Path filePath) {
        
        boolean seemsBinary = FileUtils.seemsBinary(FileUtils.readBinaryFile(filePath.toString()));
        String[] fullName = FileUtils.splitPrefixAndSuffix(filePath.getFileName().toString());
        String suffix = fullName[1];

        if (seemsBinary) {
            for (int i = 0; i < Register.ACCEPTED_IMAGE_FILE_TYPES.length; i++) {
                if (Register.ACCEPTED_IMAGE_FILE_TYPES[i].equals(suffix.toLowerCase()))
                    return new ImageTab(appScene, filePath);
            }
            for (int i = 0; i < Register.ACCEPTED_MEDIA_FILE_TYPES.length; i++) {
                if (Register.ACCEPTED_MEDIA_FILE_TYPES[i].equals(suffix.toLowerCase()))
                    return new MediaTab(appScene, filePath);
            }
            return new BinaryTab(appScene, filePath);
        }
        for (int i = 0; i < Register.ACCEPTED_HTML_FILE_TYPES.length; i++) {
            if (Register.ACCEPTED_HTML_FILE_TYPES[i].equals(suffix.toLowerCase()))
                return new HtmlTab(appScene, filePath);
        }
        return new TextTab(appScene, filePath);
    }

    // CLASS

    private Path filePath;

    public FileStage(Application app, Path filePath) {
        super(app);

        this.filePath = filePath;

        setResizable(true);
        setScene(new AppScene(app, "stages/fileStage/fileStage.fxml", "cryptoeditor | " + filePath.getFileName().toString()));
    }

    // GETTERS AND SETTERS

    public Path getFile() {
        return filePath;
    }
}
