package com.compression;
import com.pixelmed.display.SourceImage;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;


public class NewWindow extends JFrame{

    NewWindow(String path, CompressionType compressionType, int compressionQuality){

        BufferedImage img = null;

        try
        {
            img = ImageIO.read(new File(path));
            this.setSize(700,500);
            JLabel jLabel = new JLabel(new StretchIcon(img));
            JPanel jPanel = new JPanel();
            this.add(jLabel);

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
}