package com.compression;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class CompareWindow extends JFrame implements ActionListener{

    private final JButton compare1Button;
    private final JButton compare2Button;
    private final JPanel menuPanel;
    private final JPanel centerPanel;
    private final JPanel image1Panel;
    private final JPanel image2Panel;
    private JLabel title1;
    private JLabel title2;
    private String image1Path;
    private String image2Path;
    private JLabel image1Label;
    private JLabel image2Label;


    CompareWindow(String title){

        super(title);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setSize(getToolkit().getScreenSize());
        this.setResizable(false);
        this.compare1Button = new JButton("Select image 1");
        this.compare1Button.setToolTipText("Select first file to compare");
        this.compare2Button = new JButton("Select image 2");
        this.compare2Button.setToolTipText("Select second file to compare");

        compare1Button.addActionListener(this);
        compare2Button.addActionListener(this);
        this.setLayout(new BorderLayout());

        this.image1Path = new String("Upload file ...");
        this.image2Path = new String("Upload file ...");
        this.image1Label = new JLabel();
        this.image2Label = new JLabel();



        this.title1 = new JLabel();
        this.title2 = new JLabel();
        this.menuPanel = new JPanel();
        this.menuPanel.setLayout(new FlowLayout(FlowLayout.LEADING,10,10));
        this.menuPanel.add(compare1Button);
        this.menuPanel.add(compare2Button);
        this.centerPanel = new JPanel();
        this.image1Panel = new JPanel();
        this.image2Panel = new JPanel();
        this.centerPanel.setLayout(new GridLayout(1,2, 10, 10));


        this.add(this.menuPanel, BorderLayout.PAGE_START);

        this.centerPanel.add(this.image1Panel, BorderLayout.WEST);
        this.centerPanel.add(this.image2Panel, BorderLayout.EAST);
        this.getContentPane().add(centerPanel, BorderLayout.CENTER);

        Border outer1Border = BorderFactory.createEmptyBorder(5, 15, 5, 15);
        Border inner1Border = BorderFactory.createTitledBorder("Image 1");
        Border inner1Border2 = BorderFactory.createBevelBorder(BevelBorder.LOWERED);
        Border compound1Border = BorderFactory.createCompoundBorder(outer1Border, inner1Border);
        this.image1Panel.setBorder(BorderFactory.createCompoundBorder(compound1Border, inner1Border2));

        Border outer2Border = BorderFactory.createEmptyBorder(5, 15, 5, 15);
        Border inner2Border = BorderFactory.createTitledBorder("Image 2");
        Border inner2Border2 = BorderFactory.createBevelBorder(BevelBorder.LOWERED);
        Border compound2Border = BorderFactory.createCompoundBorder(outer2Border, inner2Border);
        this.image2Panel.setBorder(BorderFactory.createCompoundBorder(compound2Border, inner2Border2));

        this.title1 = new JLabel(image1Path);
        this.title1.setBorder(BorderFactory.createRaisedSoftBevelBorder());
        this.title1.setVerticalAlignment(JLabel.CENTER);
        this.image1Panel.add(title1, BorderLayout.NORTH);

        this.title2 = new JLabel(image2Path);
        this.title2.setBorder(BorderFactory.createRaisedSoftBevelBorder());
        this.title2.setVerticalAlignment(JLabel.CENTER);
        this.image2Panel.add(title2, BorderLayout.NORTH);


        this.setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource()==compare1Button){
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setAcceptAllFileFilterUsed(false);
            fileChooser.addChoosableFileFilter(new FileNameExtensionFilter(".JPG", "jpg"));
            fileChooser.addChoosableFileFilter(new FileNameExtensionFilter(".PNG", "png"));
            int response = fileChooser.showOpenDialog(null);
            if(response == JFileChooser.APPROVE_OPTION){
                File file1 = new File(fileChooser.getSelectedFile().getAbsolutePath());
                this.image1Path = fileChooser.getSelectedFile().getAbsolutePath();
                this.title1.setText(this.image1Path);

                BufferedImage img = null;

                try
                {
                    img = ImageIO.read(new File(this.image1Path));

                    this.image1Panel.remove(this.image1Label);
                    ImageIcon imageIcon = new ImageIcon(new ImageIcon(img).getImage().getScaledInstance(700, 500, Image.SCALE_AREA_AVERAGING));
                    this.image1Label.setIcon(imageIcon);
                    this.repaint();

                    this.image1Panel.add(this.image1Label, BorderLayout.CENTER);

                }
                catch (IOException ex)
                {
                    ex.printStackTrace();
                }
            }
        }
        if(e.getSource()==compare2Button){
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setAcceptAllFileFilterUsed(false);
            fileChooser.addChoosableFileFilter(new FileNameExtensionFilter(".JPG", "jpg"));
            fileChooser.addChoosableFileFilter(new FileNameExtensionFilter(".PNG", "png"));
            int response = fileChooser.showOpenDialog(null);
            if(response == JFileChooser.APPROVE_OPTION){
                File file2 = new File(fileChooser.getSelectedFile().getAbsolutePath());
                this.image2Path = fileChooser.getSelectedFile().getAbsolutePath();
                this.title2.setText(this.image2Path);

                BufferedImage img = null;

                try
                {
                    img = ImageIO.read(new File(this.image2Path));

                    this.image2Panel.remove(this.image2Label);
                    ImageIcon imageIcon = new ImageIcon(new ImageIcon(img).getImage().getScaledInstance(700, 500, Image.SCALE_AREA_AVERAGING));
                    this.image2Label.setIcon(imageIcon);
                    this.repaint();

                    this.image2Panel.add(this.image2Label, FlowLayout.CENTER);

                }
                catch (IOException ex)
                {
                    ex.printStackTrace();
                }
            }
        }
    }

}
