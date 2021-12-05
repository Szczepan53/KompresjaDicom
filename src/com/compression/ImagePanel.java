package com.compression;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ImagePanel extends JPanel {
    private final JLabel imageLabel;
    private BufferedImage image;

    public ImagePanel() {

        this.imageLabel = new JLabel();
        this.setLayout(new GridBagLayout());
        this.add(imageLabel);
    }

    public BufferedImage getImage() {
        return this.image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
        this.imageLabel.setIcon(new ImageIcon(this.image));
    }

    public void resizeImage(Dimension dim) {
        if(image != null) {
            int resizedWidth = dim.width;
            int resizedHeight = dim.height;

            if(dim.height > dim.width) {
                double height2WidthRatio = ((double)this.image.getHeight()) / this.image.getWidth();
                resizedHeight = (int) (dim.width * height2WidthRatio);
            }
            else if(dim.height < dim.width) {
                double width2HeightRatio = ((double)this.image.getWidth()) / this.image.getHeight();
                resizedWidth = (int) (dim.height * width2HeightRatio);
            }

            ImageIcon currentIcon = new ImageIcon(this.image);
            Image resizedImage = currentIcon.getImage().getScaledInstance(resizedWidth,resizedHeight,Image.SCALE_SMOOTH);
            this.imageLabel.setIcon(new ImageIcon(resizedImage));
        }
    }
}
