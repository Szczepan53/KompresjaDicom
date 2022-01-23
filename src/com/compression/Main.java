package com.compression;

import com.formdev.flatlaf.FlatLightLaf;
import com.pixelmed.dicom.DicomException;

import javax.swing.*;
import java.io.IOException;

/**
 * Klasa główna służąca do poprawnego uruchomienia programu.
 */
public class Main {
    //Ścieżka do pliku DICOM otwieranego przy uruchamianiu programu.
    private static final String dicomFile = "dicom_files\\MammoTomoUPMC_Case1\\Case1 [Case1]\\Series 73200000 [MG - L MLO Breast Tomosynthesis Image].dcm";

    public static void main(String[] args) {
        //Uruchomienie programu w oddzielnym wątku.
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    FlatLightLaf.install(); //Ustawienie skórki programu na bardziej nowoczesną.
                    MainFrame mainFrame = new MainFrame("DICOM image compression app", dicomFile);
                }
                catch (DicomException | IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }
}