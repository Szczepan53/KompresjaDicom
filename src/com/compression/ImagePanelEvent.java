package com.compression;

import java.awt.event.ActionEvent;
import java.util.EventObject;

public class ImagePanelEvent extends EventObject {
    private final int frameIndex;

    public ImagePanelEvent(Object source, int frameIndex) {
        super(source);
        this.frameIndex = frameIndex;
    }

    public int getFrameIndex() {
        return frameIndex;
    }
}
