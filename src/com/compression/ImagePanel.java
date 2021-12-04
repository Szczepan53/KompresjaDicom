package com.compression;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ImagePanel extends JPanel {
    private BufferedImage image = null;

    public ImagePanel() {
        super();
    }

    public BufferedImage getImage() {
        return this.image;
    }

    public void setImage(BufferedImage image) {
        if(image != null) {
            this.image = image;
            this.setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
            this.repaint();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        super.paintComponent(g);

        if(this.image != null) {
            g2d.drawImage(this.image, null, 0, 0);
        }
    }
}
