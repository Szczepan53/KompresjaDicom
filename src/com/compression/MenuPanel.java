package com.compression;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;


/**
 * Panel sterowania w lewym górnym rogu aplikacji.
 * <p>Pozwala wybrać tryb kompresji (compressionTypeCombo), jakość obrazu po kompresji (slider dla JPEG) oraz czy obraz
 * wyjściowy ma mieć nadrukowane informacje dodatkowe z wczytanego pliku DICOM (imprintInfoGroup)</p>
 */
public class MenuPanel extends JPanel implements ChangeListener {
    private final JComboBox<CompressionType> compressionTypeCombo;
    private final JButton compressButton;
    private MenuPanelListener menuPanelListener;
    public String file;
    private final JSlider slider;
    private final JLabel label;
    private int cr;
    private final JRadioButton noneRadio;
    private final JRadioButton someRadio;
    private final JRadioButton allRadio;
    private final ButtonGroup imprintInfoGroup;
    private final JPanel imprintInfoPanel;


    public MenuPanel(){
        this.setPreferredSize(new Dimension(300, 200));
        this.compressionTypeCombo = new JComboBox<>();
        this.compressButton = new JButton("Compress image");
        this.compressButton.setToolTipText("Compress currently open image to selected format");

        slider = new JSlider(1,100,50);//Maksymalny stopień kompresji ustawiłam na 10, ale to może być do zmiany
        cr = slider.getValue();
//        slider.setPaintTicks(true);
        slider.setMinorTickSpacing(1);
        slider.setPaintTrack(true);
        slider.setMajorTickSpacing(49);
        slider.setPaintLabels(true);
        slider.setFont(new Font("SansSerif",Font.PLAIN,11));
        label = new JLabel();
        label.setText("Compressed image quality = "+ slider.getValue() + "%");
        slider.addChangeListener(this);
        slider.setToolTipText("Move slider to select image quality after compression");


        DefaultComboBoxModel<CompressionType> compressionTypeComboModel = new DefaultComboBoxModel<>();
        compressionTypeComboModel.addElement(CompressionType.JPEG);
        compressionTypeComboModel.addElement(CompressionType.PNG);
        this.compressionTypeCombo.setModel(compressionTypeComboModel);
        this.compressionTypeCombo.setToolTipText("Select compression format");

        this.noneRadio = new JRadioButton("None");
        this.someRadio = new JRadioButton("Some");
        this.allRadio = new JRadioButton("All");
        this.imprintInfoGroup = new ButtonGroup();

        //Set up imprintInfoGroup radioButtons
        this.noneRadio.setActionCommand("none");
        this.noneRadio.setVerticalTextPosition(SwingConstants.TOP);
        this.noneRadio.setHorizontalTextPosition(SwingConstants.CENTER);
        this.noneRadio.setToolTipText("Don't imprint DICOM info on output image(s)");

        this.someRadio.setActionCommand("icon");
        this.someRadio.setVerticalTextPosition(SwingConstants.TOP);
        this.someRadio.setHorizontalTextPosition(SwingConstants.CENTER);
        this.someRadio.setToolTipText("Imprint some DICOM info on output image(s)");


        this.allRadio.setActionCommand("all");
        this.allRadio.setVerticalTextPosition(SwingConstants.TOP);
        this.allRadio.setHorizontalTextPosition(SwingConstants.CENTER);
        this.allRadio.setToolTipText("Imprint a lot of DICOM info on output image(s)");

        this.noneRadio.setSelected(true);
        this.imprintInfoGroup.add(this.noneRadio);
        this.imprintInfoGroup.add(this.someRadio);
        this.imprintInfoGroup.add(this.allRadio);

        this.imprintInfoPanel = new JPanel(new BorderLayout());
        this.imprintInfoPanel.add(this.noneRadio, BorderLayout.WEST);
        this.imprintInfoPanel.add(this.someRadio, BorderLayout.CENTER);
        this.imprintInfoPanel.add(this.allRadio, BorderLayout.EAST);
        this.imprintInfoPanel.setBorder(BorderFactory.createLoweredBevelBorder());


        this.compressButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MenuEvent ev = new MenuEvent(e, (CompressionType) compressionTypeCombo.getSelectedItem(),
                        cr, imprintInfoGroup.getSelection().getActionCommand());

                menuPanelListener.menuEventHandler(ev);
            }
        });

        this.compressionTypeCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean isJPEG = (compressionTypeCombo.getSelectedIndex() == 0);
                slider.setEnabled(isJPEG);
                label.setVisible(isJPEG);
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
        int currentRow = 0;
        //CompressionTypeCombo
        gc.gridx = 1;
        gc.gridy = currentRow;
        gc.weightx = 1;
        gc.weighty = 1;
        gc.anchor = GridBagConstraints.FIRST_LINE_START;
        this.add(this.compressionTypeCombo, gc);
        //CompressionTypeCombo label
        gc.gridx = 0;
        gc.gridwidth = 1;
        gc.insets = new Insets(0,0,0,5);
        gc.anchor = GridBagConstraints.NORTH;
        this.add(new JLabel("Compression type:"), gc);

        //imprintInfoPanel label
        gc.gridx = 2;
        gc.gridy = currentRow;
        gc.weightx = 1;
        gc.weighty = 1;
        this.add(new JLabel("Imprint DICOM info:"), gc);

        //Second row//////////////////////////////////
        currentRow++;
        //Label
        //Slider
        gc.gridx = 0;
        gc.gridy = currentRow;
        gc.gridwidth = 2;
        gc.weightx = 1.2;
        gc.weighty = 1;
        gc.anchor = GridBagConstraints.CENTER;
        this.add(this.label, gc);

        //imprintInfoPanel
        gc.gridx = 2;
        gc.weightx = 0.8;
        gc.gridwidth = 1;
        gc.anchor = GridBagConstraints.NORTH;
        this.add(this.imprintInfoPanel, gc);

        //Third row///////////////////////////////////
        currentRow++;
        //Slider
        gc.gridx = 0;
        gc.gridy = currentRow;
        gc.gridwidth = 2;
        gc.weightx = 1.2;
        gc.weighty = 1;
        gc.anchor = GridBagConstraints.CENTER;
        this.add(this.slider, gc);

        //Last row//////////////////////////////////
        currentRow++;
        //compressButton
        gc.gridx = 2;
        gc.gridwidth = 1;
        gc.gridy = currentRow;
        gc.weightx = 1;
        gc.weighty = 1;
        gc.anchor = GridBagConstraints.NORTH;
        this.add(this.compressButton, gc);

    }

    public void setMenuPanelListener(MenuPanelListener menuPanelListener) {
        this.menuPanelListener = menuPanelListener;
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        label.setText("Compressed image quality = "+ this.slider.getValue() + "%");
        this.cr = this.slider.getValue();
    }
}
