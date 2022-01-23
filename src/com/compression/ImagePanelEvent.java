package com.compression;

import java.util.EventObject;

/**
 * Klasa reprezentująca zdarzenie, które zaszło w instancji ImagePanel.
 * <p>Służy do przekazywania informacji o numerze obecnie wyświetlanego frame'a z instancji ImagePanel
 * do instancji DicomTableModel</p>
 */
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
