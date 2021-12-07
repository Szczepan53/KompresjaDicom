package com.compression;

import java.util.EventObject;

public class MenuEvent extends EventObject {
    private String compressionType;
    private int compressionQuality;
    private String selectedFilePath;

    public MenuEvent(Object source, String compressionType, int compressionQuality) {
        super(source);
        this.compressionType = compressionType;
        this.compressionQuality = compressionQuality;
    }

    public MenuEvent(Object source, String selectedFilePath) {
        super(source);
        this.selectedFilePath = selectedFilePath;
    }

    public String getCompressionType() {
        return compressionType;
    }

    public int getCompressionQuality() {
        return compressionQuality;
    }

    public String getSelectedFilePath() {
        return selectedFilePath;
    }
}
