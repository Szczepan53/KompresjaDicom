package com.compression;
import com.pixelmed.display.SourceImage;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import com.mortennobel.imagescaling.ResampleOp;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseWheelEvent;

public class NewWindow extends JFrame{

    private JLabel label =null;
    private double zoom = 1.0;
    private BufferedImage img = null;

    NewWindow(String path, CompressionType compressionType, int compressionQuality){

        try
        {
            img = ImageIO.read(new File(path));
            this.setSize(700,500);
            JScrollPane scrollPane = new JScrollPane();
            this.getContentPane().add(scrollPane, BorderLayout.CENTER);
            JPanel panel = new JPanel();
            panel.setLayout(new BorderLayout());

            Icon imageIcon = new ImageIcon(path);
            label = new JLabel(imageIcon);
            panel.add(label, BorderLayout.CENTER);
            panel.addMouseWheelListener(e -> {
                int notches = e.getWheelRotation();
                double temp = zoom - (notches * 0.2);
                temp = Math.max(temp, 1.0);
                if (temp != zoom) {
                    zoom = temp;
                    resizeImage();
                }
            });
            scrollPane.setViewportView(panel);

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        String [] nameFile = path.split("\\\\");
        String name = nameFile[nameFile.length-1];
        if(compressionType == CompressionType.PNG) {
            this.setTitle("Image: "+ name);
        }
        else{
            this.setTitle("Image: " + name + ", Image Quality: " + compressionQuality + "%");
        }

        this.setVisible(true);
    }
    public void resizeImage() {
        System.out.println(zoom);
        ResampleOp  resampleOp = new ResampleOp((int)(img.getWidth()*zoom), (int)(img.getHeight()*zoom));
        BufferedImage resizedIcon = resampleOp.filter(img, null);
        Icon imageIcon = new ImageIcon(resizedIcon);
        label.setIcon(imageIcon);
    }
}