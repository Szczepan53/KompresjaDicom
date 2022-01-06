package com.compression;

import java.util.EventObject;

/**
 *  Klasa służąca do przekazywania informacji zebranych w MenuPanel do MainFrame (kontrolera).
 */
public class MenuEvent extends EventObject {
    private CompressionType compressionType;
    private int compressionQuality;
    private String selectedFilePath;
    private String imprintInfo;

    public MenuEvent(Object source, CompressionType compressionType, int compressionQuality, String annotations) {
        super(source);
        this.compressionType = compressionType;
        this.compressionQuality = compressionQuality;
        this.imprintInfo = annotations;
    }

    public MenuEvent(Object source, String selectedFilePath) {
        super(source);
        this.selectedFilePath = selectedFilePath;
    }

    public MenuEvent(Object source, int compressionQuality) {
        super(source);
        this.compressionQuality = compressionQuality;
    }

    public CompressionType getCompressionType() {
        return compressionType;
    }

    public int getCompressionQuality() {
        return compressionQuality;
    }

    public String getSelectedFilePath() {
        return selectedFilePath;
    }

    public String getImprintInfo() {
        return imprintInfo;
    }
}
