package com.compression;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private static Toolbar toolbar;

    public MainFrame(String title) {
        super(title);
        this.setLayout(new BorderLayout());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1000, 1000);
    }
}
