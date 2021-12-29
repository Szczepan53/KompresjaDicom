package com.compression;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


/**
Wyświetla obrazy po kompresji w okienkach po prawej stronie obrazu oryginalnego.
Tu w sumie zrobiłem poglądowo tylko jak to mogłoby wyglądać żeby ta prawa strona nie była taka pusta ale nie jest to dokończone.
*/
public class ComparisonPanel extends JPanel {
    private BufferedImage upperImage;
    private BufferedImage lowerImage;
    private final JLabel upperLabel;
    private final JLabel lowerLabel;
    private final int imageWidth;
    private final int imageHeight;

    public ComparisonPanel() {
        Dimension dims = this.getSize();
        dims.width = 500;
        this.setPreferredSize(dims);
        this.upperLabel = new JLabel("upperImage", JLabel.CENTER);
        this.lowerLabel = new JLabel("lowerImage", JLabel.CENTER);
        this.upperLabel.setPreferredSize(new Dimension(500,500));
        this.lowerLabel.setPreferredSize(new Dimension(500,500));
        Border outerBorder = BorderFactory.createEmptyBorder(15, 15, 15, 15);
        Border innerBorder = BorderFactory.createBevelBorder(BevelBorder.LOWERED);
        Border compoundBorder = BorderFactory.createCompoundBorder(outerBorder, innerBorder);
        this.upperLabel.setBorder(compoundBorder);
        this.lowerLabel.setBorder(compoundBorder);
        try {
            this.upperImage = ImageIO.read(new File("dicom_files/outputs/MRBRAIN.jpg"));
            this.lowerImage = ImageIO.read(new File("dicom_files/outputs/MRBRAIN(1).jpg"));
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        this.imageWidth = 270;
        this.imageHeight = 270;
        this.upperLabel.setIcon(new ImageIcon(this.upperImage.getScaledInstance(this.imageWidth, this.imageHeight, Image.SCALE_SMOOTH)));
        this.lowerLabel.setIcon(new ImageIcon(this.lowerImage.getScaledInstance(this.imageWidth, this.imageHeight, Image.SCALE_SMOOTH)));
        this.setLayout(new BorderLayout());
        this.add(this.upperLabel, BorderLayout.NORTH);
        this.add(this.lowerLabel, BorderLayout.SOUTH);
    }

    public void setUpperImage(BufferedImage upperImage) {
        this.upperImage = upperImage;
        this.upperLabel.setIcon(new ImageIcon(this.upperImage.getScaledInstance(this.imageWidth, this.imageHeight, Image.SCALE_SMOOTH)));
    }

    //    public void resizeImages(Dimension dim) {
//        if(upperImage != null) {
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
