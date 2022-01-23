package com.compression;

import com.pixelmed.dicom.DicomException;
import com.pixelmed.display.ConsumerFormatImageMaker;
import java.io.IOException;

/**
 * Klasa statyczna udostępniająca metodę kompresji danych obrazowych z pliku DICOM.
 */
public abstract class Compressor {
    /**
     * Funkcja opakowująca funkcję ConsumerFormatImageMaker.convertFileToEightBitImage() z biblioteki pixelmed. Odpowiada
     * z cały proces kompresji załadowanego obrazu DICOM z parametrami wybranymi przez użytkownika.
     * @param dicomFilePath ścieżka pliku DICOM, który chcemy poddać kompresji
     * @param outputFilePath ścieżka pliku wyjściowego
     * @param outputFormat format kompresji (JPEG lub PNG)
     * @param imageQuality jakość obrazu po kompresji (aplikowalna w przypadku kompresji JPEG)
     * @param windowCenter środek okna przy okienkowaniu
     * @param windowWidth szerokość okna przy okienkowaniu
     * @param annotations pozwala umieścić informacje o pliku DICOM na obrazie wyjściowym
     * @throws IOException
     * @throws DicomException
     */
    public static String[] compressImage(String dicomFilePath, String outputFilePath, String outputFormat, int imageQuality,
                                     double windowCenter, double windowWidth, String annotations) throws IOException, DicomException {

        return ConsumerFormatImageMaker.convertFileToEightBitImage(dicomFilePath, outputFilePath,outputFormat,windowCenter,
                windowWidth, 0,0,imageQuality, annotations);
    }

}