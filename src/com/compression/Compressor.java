package com.compression;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

public class Compressor {
    public static String dicomFilePath = "dicom_files\\0015.DCM"; //ścieżka do pliku DICOM, na którym sb testowałem


    //Testowanko
    public static void main(String[] args) throws IOException{
        File dcmFile = new File(dicomFilePath);
        File jpgFile = new File("compressedImage.jpg");
        File pngFile = new File("compressedImage.png");
        compressImage2JPEG(dcmFile, jpgFile, 0.1f);
        compressImage2PNG(dcmFile, pngFile);
    }

    /*
    BufferedImage image - dane obrazowe załadowane z pliku DICOM
    File compressedImage - plik docelowy do zapisu obrazu po kompresji
    ImageWriter imageWrite - obiekt definiujący format zapisu obrazu
    ImageWriteParam imageWriteParam - parametry zapisu (np. stopień kompresji obrazu)

    Zapisuje obraz pobrany z pliku dicom w formacie definiowanym przez imageWriter.
     */
    public static void write2Image(BufferedImage image, File compressedImage, ImageWriter imageWriter, ImageWriteParam imageWriteParam) throws IOException {
        try (ImageOutputStream output = ImageIO.createImageOutputStream(compressedImage)) {
            imageWriter.setOutput(output);
            IIOImage outputImage = new IIOImage(image, null, null);
            imageWriter.write(null, outputImage, imageWriteParam);
        }
        imageWriter.dispose();
        System.out.println("Compressed image saved to " + compressedImage.getName());
    }


    /*
    File originalImage - plik źródłowy DICOM
    File compressedImage - plik docelowy do zapisu obrazu po kompresji
    float compressionQuality - wybór stopnia kompresji obrazu w zakresie (0.00f - 1.00f] np. 1.0f = 100% jakość ; 0.2f = 20% jakość itp.

    KOMPRESJA STRATNA - JPEG (możliwość wyboru stopnia kompresji poprzez parametr compressionQuality).
    Przygotowuje imageWriter oraz imageWriterParam dla kompresji obrazu do formatu JPEG.
     */
    public static void compressImage2JPEG(File originalImage, File compressedImage, float compressionQuality) throws IOException {
        BufferedImage image = ImageIO.read(originalImage);
        ImageWriter jpegWriter = ImageIO.getImageWritersByFormatName("jpg").next();
        ImageWriteParam jpegWriteParam = jpegWriter.getDefaultWriteParam();
        jpegWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
        jpegWriteParam.setCompressionQuality(compressionQuality);

        write2Image(image, compressedImage, jpegWriter, jpegWriteParam);

    }

    /*
    File originalImage - plik źródłowy DICOM
    File compressedImage - plik docelowy do zapisu obrazu po kompresji

    KOMPRESJA BEZSTRATNA? - PNG (brak możliwości wyboru stopnia kompresji).
    Przygotowuje imageWriter oraz imageWriterParam dla kompresji obrazu do formatu PNG.
     */
    public static void compressImage2PNG(File originalImage, File compressedImage) throws IOException {
        BufferedImage image = ImageIO.read(originalImage);
        ImageWriter pngWriter = ImageIO.getImageWritersByFormatName("png").next();
        ImageWriteParam pngWriteParam = pngWriter.getDefaultWriteParam();

        write2Image(image, compressedImage, pngWriter, pngWriteParam);
    }
}

