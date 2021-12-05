package com.compression;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.MenuListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.EventObject;

public class MenuPanel extends JPanel {
    private JComboBox<CompressionType> compressionTypeCombo;
    private JButton compressButton;
    private ArrayList<MenuPanelListener> menuPanelListeners;

    public MenuPanel(){
        Dimension dim = this.getPreferredSize();
        dim.width = 250;
        this.setPreferredSize(dim);
        this.compressionTypeCombo = new JComboBox<>();
        this.compressButton = new JButton("Compress image");
        this.menuPanelListeners = new ArrayList<>();

        DefaultComboBoxModel<CompressionType> compressionTypeComboModel = new DefaultComboBoxModel<>();
        compressionTypeComboModel.addElement(new CompressionType(0, "JPEG"));
        compressionTypeComboModel.addElement(new CompressionType(1, "PNG"));
        this.compressionTypeCombo.setModel(compressionTypeComboModel);

        this.compressButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                menuPanelListeners.get(compressionTypeCombo.getSelectedIndex()).menuEventHandler(); //do zmiany zeby obsługiwała wybrany obraz a nie hard-coded
            }
        });

        Border outerBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
        Border innerBorder = BorderFactory.createTitledBorder("Menu");
        this.setBorder(BorderFactory.createCompoundBorder(outerBorder, innerBorder));
        this.setLayout(new GridBagLayout());
        this.setUpLayout();
    }

    private void setUpLayout() {
        GridBagConstraints gc = new GridBagConstraints();
        //First row//////////////////////////////////
        //GridBagConstraints for CompressionTypeCombo
        gc.gridx = 0;
        gc.gridy = 0;
        gc.weightx = 1;
        gc.weighty = 1;
        gc.anchor = GridBagConstraints.FIRST_LINE_END;
        this.add(this.compressionTypeCombo, gc);
        //GridBagConstraints for CompressionTypeCompo label
//        gc.gridx = 0;
//        gc.anchor = GridBagConstraints.LINE_END;
//        gc.insets = new Insets(0,0,0,5);
//        this.add(new JLabel("Compression: "));

        //Second row//////////////////////////////////
        //GridBagConstraints for compressButton
        gc = new GridBagConstraints();
        gc.gridx = 1;
        gc.gridy = 0;
        gc.gridwidth = 2;
        gc.weightx = 1;
        gc.weighty = 1;
        gc.anchor = GridBagConstraints.NORTH;
        this.add(this.compressButton, gc);
    }

    public void addMenuPanelListener(MenuPanelListener menuPanelListener) {
        this.menuPanelListeners.add(menuPanelListener);
    }

    class CompressionType {
        private int id;
        private String text;

        public CompressionType(int id, String text) {
            this.id = id;
            this.text = text;
        }

        public int getId() {
            return id;
        }

        @Override
        public String toString() {
            return this.text;
        }
    }
}
