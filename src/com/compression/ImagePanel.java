package com.compression;

import com.pixelmed.display.SingleImagePanel;
import com.pixelmed.display.SourceImage;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ImagePanel extends SingleImagePanel {
    private BufferedImage image;

    public ImagePanel(SourceImage srcImg) {
        super(srcImg);
        this.image = srcImg.getBufferedImage();
    }

    public BufferedImage getImage() {
        return this.image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

//    public void resizeImage(Dimension dim) {
//        if(image != null) {
//            int resizedWidth = dim.width;
//            int resizedHeight = dim.height;
//
//            if(dim.height > dim.width) {
//                double height2WidthRatio = ((double)this.image.getHeight()) / this.image.getWidth();
//                resizedHeight = (int) (dim.width * height2WidthRatio);
//            }
//            else if(dim.height < dim.width) {
//                double width2HeightRatio = ((double)this.image.getWidth()) / this.image.getHeight();
//                resizedWidth = (int) (dim.height * width2HeightRatio);
//            }
//
//            ImageIcon currentIcon = new ImageIcon(this.image);
//            Image resizedImage = currentIcon.getImage().getScaledInstance(resizedWidth,resizedHeight,Image.SCALE_SMOOTH);
//            this.imageLabel.setIcon(new ImageIcon(resizedImage));
//        }
//    }
}
