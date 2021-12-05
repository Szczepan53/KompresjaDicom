package com.compression;

import com.pixelmed.dicom.AttributeList;
import com.pixelmed.dicom.DicomException;
import com.pixelmed.dicom.TagFromName;
import com.pixelmed.display.ConsumerFormatImageMaker;
import com.pixelmed.display.SourceImage;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.Raster;
import java.awt.image.RescaleOp;
import java.io.File;
import java.io.IOException;

public class Compressor {
    private static String dicomFile = "C:\\Users\\Admin\\IdeaProjects\\PJAVA_KompresjaDicom\\dicom_files\\D0001.dcm";
    private static String outputJpgFile = "D0001JPG.jpg";
    private static String outputPngFile = "D0001PNG.png";
    public static final String JPG = "jpg";
    public static final String PNG = "png";

    public static void main(String[] args) {
        try {
            AttributeList attributeList = new AttributeList();
            attributeList.read(dicomFile);
            ConsumerFormatImageMaker.convertFileToEightBitImage(dicomFile,outputJpgFile,"jpg",0,0,0,0,1,ConsumerFormatImageMaker.ALL_ANNOTATIONS);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    /* robi całą robote */
    public static void compressImage(String dicomFilePath, String outputFilePath, String outputFormat, int imageQuality) throws IOException, DicomException {
        ConsumerFormatImageMaker.convertFileToEightBitImage(dicomFilePath, outputFilePath,outputFormat,0,0,0,0,imageQuality, ConsumerFormatImageMaker.NO_ANNOTATIONS);
    }

//    public static BufferedImage get8BitImage(AttributeList attrList) throws DicomException {
//        int pixelsAllocated = attrList.get(TagFromName.BitsAllocated).getSingleIntegerValueOrDefault(0);
//        BufferedImage containedImage = (new SourceImage(attrList)).getBufferedImage();
//
//        if(pixelsAllocated == 8) {
//            return containedImage;
//        }
//
//        Raster raster = containedImage.getData();
//        int imgWidth = containedImage.getWidth();
//        int imgHeight = containedImage.getHeight();
//        int minPixelValue = Integer.MAX_VALUE;
//        int maxPixelValue = Integer.MIN_VALUE;
//        for(int y=0; y < imgHeight; y++) {
//            for(int x=0; x<imgWidth; x++) {
//                int curPixelValue = raster.getSample(x, y, 0);
//                if(curPixelValue < minPixelValue) {
//                    minPixelValue = curPixelValue;
//                }
//                else if(curPixelValue > maxPixelValue) {
//                    maxPixelValue = curPixelValue;
//                }
//            }
//        }
//        System.out.println("minPixelValue = " + minPixelValue);
//        System.out.println("maxPixelValue = " + maxPixelValue);
//        int oldRange = maxPixelValue - minPixelValue;
//        int newRange = Short.MAX_VALUE - Short.MIN_VALUE;
//        float scale = ((float) newRange)/oldRange;
//        float offset = Short.MIN_VALUE - (minPixelValue * ((float)newRange) / oldRange);
//
//        System.out.println("New min: " + (minPixelValue * scale + offset));
//        System.out.println("New max: " + (maxPixelValue * scale + offset));
//        RescaleOp rescaleOp = new RescaleOp(scale, offset, null);
//        containedImage = rescaleOp.filter(containedImage, null);
////        BufferedImage newImage = new BufferedImage(imgWidth, imgHeight, BufferedImage.TYPE_BYTE_GRAY);
////        for(int y=0; y < imgHeight; y++) {
////            for(int x=0; x<imgWidth; x++) {
////                int argb = containedImage.getRGB(x, y);
////                int r = (argb >> 16) & 0xff;
////                int g = (argb >> 8) & 0xff;
////                int b = argb & 0xff;
////                System.out.printf("%d %d %d\n", r, g, b);
////                r += 100;
////                g += 100;
////                b += 100;
////                argb = (r << 16) | (g << 8) | b;
////            }
////        }
//        return containedImage;
//    }

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