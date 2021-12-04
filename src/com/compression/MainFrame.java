package com.compression;

import com.pixelmed.dicom.DicomException;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class MainFrame extends JFrame {
    private Toolbar toolbar;
    private ImagePanel imagePanel;
    private static String outputJpgFile = "0015compressedJPG.jpg";
    private static String outputPngFile = "0015compressedPNG.png";

    public MainFrame(String title) {
        super(title);

        this.toolbar = new Toolbar();
        this.imagePanel = new ImagePanel();

        this.setLayout(new BorderLayout());
        this.setSize(new Dimension(1024, 1024));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.add(this.imagePanel, BorderLayout.CENTER);
        this.add(this.toolbar, BorderLayout.SOUTH);

        this.toolbar.addCompressionListener(new CompressionListener() {
            @Override
            public void compress() {
                try {
                    Compressor.compressDCM2JPEG(imagePanel.getImage(), outputJpgFile, 0.1f);
                }
                catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        this.toolbar.addCompressionListener(new CompressionListener() {
            @Override
            public void compress() {
                try {
                    Compressor.compressDCM2PNG(imagePanel.getImage(), outputPngFile);
                }
                catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        this.setVisible(true);

    }

    public void setImagePanelImage(BufferedImage image) {
        this.imagePanel.setImage(image);
    }
}
