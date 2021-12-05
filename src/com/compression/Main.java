package com.compression;

import com.pixelmed.dicom.AttributeList;
import com.pixelmed.dicom.DicomException;
import com.pixelmed.display.SourceImage;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Main {
    private static String dicomFile = "C:dicom_files\\D0001.dcm";
    private static BufferedImage image = null;

    public static void main(String[] args) {
        try {
            image = (new SourceImage(dicomFile)).getBufferedImage();
        }
        catch (IOException | DicomException ex) {
            System.out.println("ERROR loading DICOM file...");
            ex.printStackTrace();
            System.exit(-1);
        }
        
        
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainFrame mainFrame = new MainFrame("DICOM Image Compression App");
                mainFrame.setImagePanelImage(image);
            }
        });
    }
}
