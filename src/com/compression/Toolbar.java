package com.compression;

import com.pixelmed.dicom.DicomException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

public class Toolbar extends JPanel implements ActionListener {
    private final Button compress2JpgButton;
    private final Button compress2PngButton;
    private final ArrayList<CompressionListener> compressionListeners;

    public Toolbar() {
        this.compress2JpgButton = new Button("Compress to JPEG");
        this.compress2PngButton = new Button("Compress to PNG");
        this.compressionListeners = new ArrayList<>();

        this.compress2JpgButton.addActionListener(this);
        this.compress2PngButton.addActionListener(this);

        this.setLayout(new FlowLayout());
        this.add(compress2JpgButton, FlowLayout.LEFT);
        this.add(compress2PngButton, FlowLayout.CENTER);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        CompressionListener compressionListener = null;
        try {
            if (e.getSource() == compress2JpgButton) {
                compressionListener = compressionListeners.get(0);
            } else if (e.getSource() == compress2PngButton) {
                compressionListener = compressionListeners.get(1);
            }
            compressionListener.compress();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void addCompressionListener(CompressionListener compressionListener) {
        this.compressionListeners.add(compressionListener);
    }
}
