package com.compression;

import com.pixelmed.dicom.AttributeList;
import com.pixelmed.dicom.DicomDictionary;
import com.pixelmed.dicom.DicomException;
import com.pixelmed.display.SingleImagePanel;
import com.pixelmed.display.SourceImage;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseWheelEvent;
import java.io.IOException;

public class ImagePanel extends JPanel {
    private SingleImagePanelExtended singleImagePanel;
    private SourceImage currentImage;
    private JLabel title;
    private int currentFrameIndex;
    private int maxFrameIndex;
    private ImagePanelListener imagePanelListener;
    private final DicomDictionary dicomDictionary = AttributeList.getDictionary();

    /**
     * Odpowiada za wyświetlanie danych obrazowych z załadowanego pliku DICOM
     * @param dicomFilePath ścieżka pliku DICOM
     * @param dicomAttrs lista atrybutów załadowanego pliku DICOM zawarta w obiekcie AttributeList
     */
    public ImagePanel(String dicomFilePath, AttributeList dicomAttrs){
        try {
            this.currentImage = new SourceImage(dicomFilePath);
            this.maxFrameIndex = this.currentImage.getNumberOfFrames();
            this.currentFrameIndex = dicomAttrs.get(dicomDictionary.getTagFromName("InstanceNumber")).getSingleIntegerValueOrDefault(-1);
            this.singleImagePanel = new SingleImagePanelExtended(currentImage);
            this.setLayout(new BorderLayout());
            Border outerBorder = BorderFactory.createEmptyBorder(5, 15, 5, 15);
            Border innerBorder = BorderFactory.createTitledBorder("Currently loaded dicom file");
            Border innerBorder2 = BorderFactory.createBevelBorder(BevelBorder.LOWERED);
            Border compoundBorder = BorderFactory.createCompoundBorder(outerBorder, innerBorder);
            this.setBorder(BorderFactory.createCompoundBorder(compoundBorder, innerBorder2));
            this.singleImagePanel.setBackground(Color.BLACK);
            this.add(this.singleImagePanel, BorderLayout.CENTER);

            this.title = new JLabel(dicomFilePath);
            this.title.setBorder(BorderFactory.createRaisedSoftBevelBorder());
            this.title.setVerticalAlignment(JLabel.CENTER);
            this.add(title, BorderLayout.NORTH);
        }
        catch (DicomException | IOException ex) {
            ex.printStackTrace();
        }
    }

    public void setImage(String dicomFilePath, AttributeList dicomAttrs) throws IOException, DicomException{
        this.currentImage = new SourceImage(dicomFilePath);
        this.currentFrameIndex = dicomAttrs.get(dicomDictionary.getTagFromName("InstanceNumber")).getSingleIntegerValueOrDefault(-1);
        this.maxFrameIndex = this.currentImage.getNumberOfFrames();
        this.title.setText(dicomFilePath);
        this.remove(this.singleImagePanel);
        this.singleImagePanel = new SingleImagePanelExtended(this.currentImage);
        this.add(singleImagePanel, BorderLayout.CENTER);
    }

    public double getWindowWidth() {
        return this.singleImagePanel.getWindowWidth();
    }

    public double getWindowCenter() {
        return this.singleImagePanel.getWindowCenter();
    }

    public void setImagePanelListener(ImagePanelListener imagePanelListener) {
        this.imagePanelListener = imagePanelListener;
    }

    public class SingleImagePanelExtended extends SingleImagePanel {
        public SingleImagePanelExtended(SourceImage sImg) {
            super(sImg);
        }

        public double getWindowCenter() {
            return this.windowCenter;
        }

        public double getWindowWidth() {
            return this.windowWidth;
        }

        /**
         * Przeciąża metodę z klasy SingleImagePanel w celu aktualizacji danych o obecnie wyświetlanej instancji obrazu
         * (w przypadku plików DICOM składających się z wielu obrazów).
         * @param e
         */
        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            super.mouseWheelMoved(e);
            int delta = e.getWheelRotation();

            if(ImagePanel.this.currentFrameIndex < 0) {
                return;
            }

            int tmp = ImagePanel.this.currentFrameIndex + delta;
            if(tmp > ImagePanel.this.maxFrameIndex || tmp < 1) {
                return;
            }

            ImagePanel.this.currentFrameIndex = tmp;
            ImagePanel.this.imagePanelListener.imagePanelEventHandler(new ImagePanelEvent(e, ImagePanel.this.currentFrameIndex));
        }

    }
}