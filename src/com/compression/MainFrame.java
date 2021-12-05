package com.compression;

import com.pixelmed.dicom.DicomException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class MainFrame extends JFrame {
//    private Toolbar toolbar;
    private ImagePanel imagePanel;
    private MenuPanel menuPanel;
    private static String outputJpgFile = "0015compressedJPG.jpg";
    private static String outputPngFile = "0015compressedPNG.png";

    public MainFrame(String title) {
        super(title);

//        this.toolbar = new Toolbar();
        this.imagePanel = new ImagePanel();
        this.menuPanel = new MenuPanel();

        this.setLayout(new BorderLayout());
        this.setSize(new Dimension(600, 500));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.add(this.imagePanel, BorderLayout.CENTER);
//        this.add(this.toolbar, BorderLayout.SOUTH);
        this.add(this.menuPanel, BorderLayout.WEST);
//
//        this.toolbar.addCompressionListener(new CompressionListener() {
//            @Override
//            public void compress() {
//
//            }
//        });
//
//        this.toolbar.addCompressionListener(new CompressionListener() {
//            @Override
//            public void compress() {
//
//            }
//        });

        this.imagePanel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                imagePanel.resizeImage(e.getComponent().getSize());
            }
        });

        this.menuPanel.addMenuPanelListener(new MenuPanelListener() {
            @Override
            public void menuEventHandler() {
                try {
                    Compressor.compressDCM2JPEG(imagePanel.getImage(), outputJpgFile, 0.1f);
                }
                catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        this.menuPanel.addMenuPanelListener(new MenuPanelListener() {
            @Override
            public void menuEventHandler() {
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
