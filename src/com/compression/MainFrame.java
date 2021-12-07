package com.compression;

import com.pixelmed.dicom.DicomException;
import com.pixelmed.display.SourceImage;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class MainFrame extends JFrame {
    //    private Toolbar toolbar;
    private ImagePanel imagePanel;
    private MenuPanel menuPanel;
    private String dicomFilePath;
    private String outputJpgFilePath;
    private  String outputPngFilePath;

    public MainFrame(String title, SourceImage srcImage, String dicomFilePath) {
        super(title);
        this.dicomFilePath = dicomFilePath;
        this.outputJpgFilePath = makeCompressedFilePath(dicomFilePath, "jpg");
        this.outputPngFilePath = makeCompressedFilePath(dicomFilePath, "png");
        this.imagePanel = new ImagePanel(srcImage);
        this.menuPanel = new MenuPanel();

        this.setLayout(new BorderLayout());
        this.setSize(new Dimension(600, 500));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.add(this.imagePanel, BorderLayout.CENTER);
        this.add(this.menuPanel, BorderLayout.WEST);

         this.menuPanel.setMenuPanelListener(new MenuPanelListener() {
            @Override
            public void menuEventHandler(MenuEvent ev) {
                try {
                    String compressionType = ev.getCompressionType();
                    int compressionQuality = ev.getCompressionQuality();
                    String outputFilePath = compressionType.equals("jpg")? outputJpgFilePath : outputPngFilePath;
                    Compressor.compressImage(MainFrame.this.dicomFilePath, outputFilePath, compressionType, compressionQuality);
                }
                catch (IOException | DicomException ex) {
                    ex.printStackTrace();
                }
//                NewWindow myWindow = new NewWindow(outputJpgFilePath, imagePanel.getImage());
            }
        });

        this.menuPanel.setFileChooserListener(new MenuPanelListener() {
            @Override
            public void menuEventHandler(MenuEvent ev) {
                try {
                    String selectedFilePath = ev.getSelectedFilePath();
                    setDicomFilePath(selectedFilePath);
                    setOutputJpgFilePath(makeCompressedFilePath(selectedFilePath, "jpg"));
                    setOutputPngFilePath(makeCompressedFilePath(selectedFilePath, "png"));
                    setImagePanel(selectedFilePath);
                }
                catch (IOException | DicomException e) {
                    e.printStackTrace();
                }
            }
        });


        this.setVisible(true);

    }

//    public void setImagePanelImage(BufferedImage image) {
//        this.imagePanel.setImage(image);
//    }

    private static String makeCompressedFilePath(String filePath, String destFormat) {
        String[] partPath = filePath.split("\\\\");
        partPath[partPath.length-2] += "\\outputs";
        String[] lastPart = partPath[partPath.length-1].split("\\.");
        lastPart[1] = destFormat;
        String changedFormatLastPart = String.join(".", lastPart);
        partPath[partPath.length-1] = changedFormatLastPart;
        String newPath = String.join("\\", partPath);
        System.out.println(newPath);
        return newPath;
    }

    private void setDicomFilePath(String dicomFilePath) {
        this.dicomFilePath = dicomFilePath;
    }

    private void setImagePanel(String newDicomFilePath) throws IOException, DicomException{
        this.remove(this.imagePanel);
        this.imagePanel = new ImagePanel(new SourceImage(newDicomFilePath));
        this.add(imagePanel, BorderLayout.CENTER);
        this.revalidate();
//        this.repaint();
    }

    private void setOutputJpgFilePath(String outputJpgFilePath) {
        this.outputJpgFilePath = outputJpgFilePath;
    }

    private void setOutputPngFilePath(String outputPngFilePath) {
        this.outputPngFilePath = outputPngFilePath;
    }
}
