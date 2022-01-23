package com.compression;

/**
 * Interfejs służący przekazaniu z MainFrame funkcji wywoływanej przy kliknięciu przycisku Compress Image w MenuPanel.
 */
public interface MenuPanelListener {
    void menuEventHandler(MenuEvent ev);
}
