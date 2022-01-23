package com.compression;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * Okienko dialogowe wyświetlające pasek postępu w trakcie kompresji obrazu z DICOMa.
 * <p>Pasek postępu ustawiony jest jako "niezdeterminowany" - sygnalizuje proces dziejący się w tle, ale nie pokazuje
 * ile zostało do jego ukończenia.</p>
 */

public class ProgressDialog extends JDialog {

    private final JProgressBar progressBar;
    private int nFrames = 1;
    private String compressionFormat = "JPG";

    public ProgressDialog(Frame owner) {
        super(owner, "Image compression in progress...",
                ModalityType.APPLICATION_MODAL);

        this.progressBar = new JProgressBar();
        this.progressBar.setIndeterminate(true);

        Border outerBorder = BorderFactory.createEmptyBorder(5,5,5,5);
        Border innerBorder = BorderFactory.createLoweredBevelBorder();
        this.progressBar.setBorder(BorderFactory.createCompoundBorder(outerBorder, innerBorder));

        Dimension barSize = this.progressBar.getPreferredSize();
        barSize.width = 300;
        barSize.height = 35;
        this.progressBar.setPreferredSize(barSize);

        this.progressBar.setStringPainted(true);
        this.progressBar.setString("Compressing image(s)...");

        this.setLayout(new FlowLayout());
        this.add(this.progressBar);

        this.pack();
        this.setLocationRelativeTo(owner);

        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    }


    @Override
    public void setVisible(boolean visible) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                if(visible) {
                    ProgressDialog.this.getOwner().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                }
                else {
                    ProgressDialog.this.getOwner().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                }
                ProgressDialog.super.setVisible(visible);
                if(!visible) {
                    Icon checkIcon = new ImageIcon("icons/check.png");
                    String message = String.format("Successfully compressed %d DICOM %s to %s format!",
                            nFrames,
                            (nFrames > 1)? "frames" : "frame",
                            compressionFormat);
                    JOptionPane.showMessageDialog(ProgressDialog.super.getOwner(), message,
                            "Compression completed",JOptionPane.INFORMATION_MESSAGE, checkIcon);
                }
            }
        });
    }

    public int getnFrames() {
        return nFrames;
    }

    public void setnFrames(int nFrames) {
        this.nFrames = nFrames;
    }

    public String getCompressionFormat() {
        return compressionFormat;
    }

    public void setCompressionFormat(String compressionFormat) {
        this.compressionFormat = compressionFormat;
    }
}
