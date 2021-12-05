package com.compression;

import java.util.EventObject;

public class MenuEvent extends EventObject {
    private final String compressionType;
    private final int compressionQuality;

    public MenuEvent(Object source, String compressionType, int compressionQuality) {
        super(source);
        this.compressionType = compressionType;
        this.compressionQuality = compressionQuality;
    }

    public String getCompressionType() {
        return compressionType;
    }

    public int getCompressionQuality() {
        return compressionQuality;
    }
}
