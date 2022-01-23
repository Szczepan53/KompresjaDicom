package com.compression;

/**
 * Interfejs służacy do przekazania do instancji ImagePanel funkcji wywoływanej przy pokręceniu kółkiem myszy w obszarze
 * instancji ImagePanel.
 */
public interface ImagePanelListener {
    void imagePanelEventHandler(ImagePanelEvent ev);
}
