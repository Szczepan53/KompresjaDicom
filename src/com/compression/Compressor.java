package com.compression;

import com.pixelmed.dicom.AttributeList;
import com.pixelmed.dicom.DicomException;
import com.pixelmed.display.SourceImage;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Compressor {
    private static String dicomFile = "C:dicom_files\\D0001.DCM";
    private static String outputJpgFile = "0015compressedJPG.jpg";
    private static String outputPngFile = "0015compressedPNG.png";



    public static void main(String[] args) {
        AttributeList attrList = new AttributeList();
        try {
            compressDCM2JPEG(dicomFile, outputJpgFile, 0.05f);
            compressDCM2PNG(dicomFile, outputPngFile);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    BufferedImage image - dane obrazowe załadowane z pliku DICOM
    File compressedImage - plik docelowy do zapisu obrazu po kompresji
    ImageWriter imageWrite - obiekt definiujący format zapisu obrazu
    ImageWriteParam imageWriteParam - parametry zapisu (np. stopień kompresji obrazu)

    Zapisuje obraz pobrany z pliku dicom w formacie definiowanym przez imageWriter.
     */
    public static void write2Image(BufferedImage image, String destPath, ImageWriter imageWriter, ImageWriteParam imageWriteParam) throws IOException {
        try (ImageOutputStream output = ImageIO.createImageOutputStream(new File(destPath))) {
            imageWriter.setOutput(output);
            IIOImage outputImage = new IIOImage(image, null, null);
            imageWriter.write(null, outputImage, imageWriteParam);
        }
        imageWriter.dispose();
        System.out.println("Compressed image saved to " + destPath);
    }


    /*
    String srcDCMFilePath - ścieżka pliku źródłowego DICOM
    String destPNGFilePath - ścieżka pliku docelowy do zapisu obrazu po kompresji
    float compressionQuality - wybór stopnia kompresji obrazu w zakresie (0.00f - 1.00f] np. 1.0f = 100% jakość ; 0.2f = 20% jakość itp.

    KOMPRESJA STRATNA - JPEG (możliwość wyboru stopnia kompresji poprzez parametr compressionQuality).
    Wczytuje plik DICOM, tworzy BufferedImage na jego podstawie i zapisuje go w formacie JPEG.
     */
    public static void compressDCM2JPEG(String srcDCMFilePath, String destJPGFilePath, float compressionQuality)  throws IOException, DicomException {
        AttributeList attrList = new AttributeList();
        attrList.read(srcDCMFilePath);
        SourceImage srcImage = new SourceImage(attrList);
        BufferedImage bufferedImage = srcImage.getBufferedImage(); //for now only 1 frame to compress
        compressDCM2JPEG(bufferedImage, destJPGFilePath, compressionQuality);
    }

    public static void compressDCM2JPEG(BufferedImage bufferedImage, String destJPGFilePath, float compressionQuality) throws IOException {
        ImageWriter jpegWriter = ImageIO.getImageWritersByFormatName("jpg").next();
        ImageWriteParam jpegWriteParam = jpegWriter.getDefaultWriteParam();
        jpegWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        jpegWriteParam.setCompressionQuality(compressionQuality);

        write2Image(bufferedImage, destJPGFilePath, jpegWriter, jpegWriteParam);


    }

    /*
    String srcDCMFilePath - ścieżka pliku źródłowego DICOM
    String destPNGFilePath - ścieżka pliku docelowy do zapisu obrazu po kompresji

    KOMPRESJA BEZSTRATNA? - PNG (brak możliwości wyboru stopnia kompresji).
    Zapisuje obraz wczytany z pliku DICOM w formacie PNG.
     */
    public static void compressDCM2PNG(String srcDCMFilePath, String destPNGFilePath) throws IOException, DicomException{
        AttributeList attrList = new AttributeList();
        attrList.read(srcDCMFilePath);
        SourceImage srcImage = new SourceImage(attrList);
        BufferedImage bufferedImage = srcImage.getBufferedImage(); //for now only 1 frame to compress
        compressDCM2PNG(bufferedImage, destPNGFilePath);
    }

    public static void compressDCM2PNG(BufferedImage bufferedImage, String destPNGFilePath) throws IOException{
        ImageWriter pngWriter = ImageIO.getImageWritersByFormatName("png").next();
        ImageWriteParam pngWriteParam = pngWriter.getDefaultWriteParam();

        write2Image(bufferedImage, destPNGFilePath, pngWriter, pngWriteParam);
    }

}
