package com.compression;

import com.pixelmed.dicom.AttributeList;
import com.pixelmed.dicom.AttributeTag;
import com.pixelmed.dicom.DicomException;
import com.pixelmed.dicom.TagFromName;
import com.pixelmed.display.SourceImage;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Main {
    private static final String dicomFile = "dicom_files\\MammoTomoUPMC_Case10\\Case10 [Case10]\\Series 73200000 [MG - L MLO Breast Tomosynthesis Image].dcm";
    private static final BufferedImage image = null;
    private static SourceImage srcImage;

    public static void main(String[] args) {
//        try {
//            srcImage = new SourceImage(dicomFile);
//        }
//        catch (IOException | DicomException ex) {
//            System.out.println("ERROR loading DICOM file...");
//            ex.printStackTrace();
//            System.exit(-1);
//        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    MainFrame mainFrame = new MainFrame("DICOM Image Compression App", dicomFile);
                }
                catch (DicomException | IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
}