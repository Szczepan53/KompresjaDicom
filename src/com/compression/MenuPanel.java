package com.compression;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;


public class MenuPanel extends JPanel implements ActionListener, ChangeListener {
    private JComboBox<CompressionType> compressionTypeCombo;
    private JButton compressButton;
    private MenuPanelListener menuPanelListener;
    private MenuPanelListener fileChooserListener;
    private static ArrayList<String> compressionTypes = new ArrayList<>(Arrays.asList("jpg", "png"));
    JButton button;
    public String file;
    JSlider slider;
    JLabel label;
    private int cr;


    public MenuPanel(){
        Dimension dim = this.getPreferredSize();
        dim.width = 300;
        this.setPreferredSize(dim);
        this.compressionTypeCombo = new JComboBox<>();
        this.compressButton = new JButton("Compress image");

        button = new JButton("Select File");
        button.addActionListener(this);

        slider = new JSlider(1,100,50);//Maksymalny stopień kompresji ustawiłam na 10, ale to może być do zmiany
//        slider.setPaintTicks(true);
        slider.setMinorTickSpacing(1);
        slider.setPaintTrack(true);
        slider.setMajorTickSpacing(49);
        slider.setPaintLabels(true);
        slider.setFont(new Font("SansSerif",Font.PLAIN,11));
        label = new JLabel();
        label.setText("Stopień kompresji = "+ slider.getValue());
        slider.addChangeListener(this);



        DefaultComboBoxModel<CompressionType> compressionTypeComboModel = new DefaultComboBoxModel<>();
        compressionTypeComboModel.addElement(new CompressionType(0, "JPEG"));
        compressionTypeComboModel.addElement(new CompressionType(1, "PNG"));
        this.compressionTypeCombo.setModel(compressionTypeComboModel);

        this.compressButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MenuEvent ev = new MenuEvent(e, compressionTypes.get(compressionTypeCombo.getSelectedIndex()),cr);
                menuPanelListener.menuEventHandler(ev); //do zmiany zeby obsługiwała wybrany obraz a nie hard-coded
                NewWindow myWindow = new NewWindow(MainFrame.destinationFilePath, ev.getCompressionType(), ev.getCompressionQuality());
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
        gc.insets = new Insets(1,0,1,0);
        //GridBagConstraints for selectig file button
        gc = new GridBagConstraints();
        gc.gridx = 1;
        gc.gridy = 0;
        gc.gridwidth = 1;
        gc.gridheight = 1;
        gc.weightx = 0;
        gc.weighty = 0.6;
        gc.anchor = GridBagConstraints.SOUTH;
        this.add(this.button,gc);
        //First row//////////////////////////////////
        //GridBagConstraints for CompressionTypeCombo
        gc = new GridBagConstraints();
        gc.gridx = 0;
        gc.gridy = 1;
//        gc.gridwidth = 0;
//        gc.gridheight = 1;
        gc.weightx = 0;
        gc.weighty = 0.1;
        gc.anchor = GridBagConstraints.FIRST_LINE_END;
//        gc.anchor = GridBagConstraints.CENTER;
        this.add(this.compressionTypeCombo, gc);
        //GridBagConstraints for CompressionTypeCompo label
//        gc.gridx = 0;
//        gc.anchor = GridBagConstraints.LINE_END;
//        gc.insets = new Insets(0,0,0,5);
//        this.add(new JLabel("Compression: "));

        //Second row//////////////////////////////////
        //GridBagConstraints for compressButton
        gc.gridx = 1;
        gc.gridy = 1;
//        gc.gridwidth = 1;
//        gc.gridheight = 1;
        gc.weightx = 0;
        gc.weighty = 0.1;
//        gc.fill = GridBagConstraints.VERTICAL;
        gc.anchor = GridBagConstraints.NORTH;
        this.add(this.compressButton, gc);
        //Slider
        gc.gridx = 1;
        gc.gridy = 2;
        gc.gridwidth = 2;
        gc.gridheight = 1;
        gc.weightx = 0;
        gc.weighty = 0.05;
        this.add(this.slider, gc);
        //Label
        //Slider
        gc.gridx = 1;
        gc.gridy = 5;
        gc.gridwidth = 6;
        gc.gridheight = 1;
        gc.weightx = 0;
        gc.weighty = 0.4;
        this.add(this.label, gc);



    }

    public void setMenuPanelListener(MenuPanelListener menuPanelListener) {
        this.menuPanelListener = menuPanelListener;
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        label.setText("Stopień kompresji = "+ this.slider.getValue());
        this.cr = this.slider.getValue();
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
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==button){
            JFileChooser fileChooser = new JFileChooser();
            FileNameExtensionFilter dcmFilter = new FileNameExtensionFilter("DICOM Files", "dcm");
            fileChooser.setFileFilter(dcmFilter);
            int response = fileChooser.showOpenDialog(null);
            if(response == JFileChooser.APPROVE_OPTION){
                File path = new File(fileChooser.getSelectedFile().getAbsolutePath());
                String selectedFilePath = path.getAbsolutePath();
                MenuEvent ev = new MenuEvent(e, selectedFilePath);
                fileChooserListener.menuEventHandler(ev);
            }
        }

    }

    public void setFileChooserListener(MenuPanelListener fileChooserListener) {
        this.fileChooserListener = fileChooserListener;
    }
}
