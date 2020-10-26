package com.andreas.main.app.imageTab;

import com.andreas.main.app.AppController;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ImageTabController extends AppController {

    @FXML
    public Label sizeIndicator;
    @FXML
    public Slider sizeSlider;
    @FXML
    public ImageView image;

    public Image imageFile;

    private double imageWidth, imageHeight;

    @Override
    public void init() {

        image.setImage(imageFile);

        imageWidth = image.getImage().getWidth();
        imageHeight = image.getImage().getHeight();

        setSizeIndicator(sizeSlider.getValue());
    }

    @FXML
    private void scaled() {
        setSizeIndicator(sizeSlider.getValue());
        image.setFitWidth((int) (imageWidth * sizeSlider.getValue()));
        image.setFitHeight((int) (imageHeight * sizeSlider.getValue()));
    }

    private void setSizeIndicator(double scale) {
        sizeIndicator.setText("Size: " + Math.round(scale * 100) + "%");
    }

    // GETTERS AND SETTERS

    public void setImageFile(Image imageFile) {
        this.imageFile = imageFile;
    }
}
