package com.compression;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.io.File;
import java.io.IOException;

import javax.swing.JScrollPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.BorderLayout;

/**
 * Klasa reprezentująca okno służące do porównywania obrazów po kompresji.
 * <p>Umożliwia porównanie jakości wyjściowej obrazów o różnej kompresji pozwalając na ustwienie ich obok siebie oraz na
 * zoomowanie obrazów w celu wydobycia z nich detali.</p>
 */
public class ComparisonWindow extends JFrame{
    private final JLabel imageLabel1;
    private final JLabel imageLabel2;
    private double zoom = 1.0;
    private BufferedImage img1;
    private BufferedImage img2;
    private final JFileChooser fileChooser;
    private final JLabel imagePathLabel1;
    private final JLabel imagePathLabel2;
    private final JLabel zoomLabel1;
    private final JLabel zoomLabel2;

    /**
     * Inicjalizacja okna ComparisonWindow.
     * @param mainFrameRef referencja do instancji okna głównego MainFrame
     */
    ComparisonWindow(MainFrame mainFrameRef){
        this.setTitle("Compare output images");
        this.setJMenuBar(createMenuBar());
        fileChooser = new JFileChooser(new File(mainFrameRef.outputJpgFilePath).getParentFile());
        fileChooser.setFileFilter(new FileNameExtensionFilter("Compressed image files (*.jpg, *.png)",
                "jpg", "png"));

        this.setSize(700,500);
        JScrollPane scrollPane1 = new JScrollPane();
        JScrollPane scrollPane2 = new JScrollPane();
        JPanel comparedImagePanel1 = new JPanel(new BorderLayout());
        JPanel comparedImagePanel2 = new JPanel(new BorderLayout());
        comparedImagePanel1.add(scrollPane1, BorderLayout.CENTER);
        comparedImagePanel2.add(scrollPane2, BorderLayout.CENTER);

        this.imagePathLabel1 = new JLabel("Please load left image to compare");
        this.imagePathLabel1.setBorder(BorderFactory.createRaisedSoftBevelBorder());
        this.imagePathLabel1.setVerticalAlignment(JLabel.CENTER);
        comparedImagePanel1.add(this.imagePathLabel1, BorderLayout.NORTH);

        this.zoomLabel1 = new JLabel("Zoom: None");
        this.zoomLabel1.setBorder(BorderFactory.createRaisedSoftBevelBorder());
        this.zoomLabel1.setVerticalAlignment(JLabel.CENTER);
        comparedImagePanel1.add(this.zoomLabel1, BorderLayout.SOUTH);

        this.imagePathLabel2 = new JLabel("Please load right image to compare");
        this.imagePathLabel2.setBorder(BorderFactory.createRaisedSoftBevelBorder());
        this.imagePathLabel2.setVerticalAlignment(JLabel.CENTER);
        comparedImagePanel2.add(this.imagePathLabel2, BorderLayout.NORTH);

        this.zoomLabel2 = new JLabel("Zoom: None");
        this.zoomLabel2.setBorder(BorderFactory.createRaisedSoftBevelBorder());
        this.zoomLabel2.setVerticalAlignment(JLabel.CENTER);
        comparedImagePanel2.add(this.zoomLabel2, BorderLayout.SOUTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, comparedImagePanel1, comparedImagePanel2);
        splitPane.setDividerLocation(0.5);
        Dimension minSize = new Dimension(150, 150);
        scrollPane1.setMinimumSize(minSize);
        scrollPane2.setMinimumSize(minSize);
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        this.getContentPane().add(panel, BorderLayout.CENTER);
        panel.add(splitPane, BorderLayout.CENTER);
        imageLabel1 = new JLabel();
        imageLabel2 = new JLabel();

        /*
         * MouseListener reagujący na kręcenie rolką myszki, powoduje zmianę aktualnego przybliżenia obrazu lewego.
         */
        imageLabel1.addMouseWheelListener(e -> {
            if(img1 != null) {
                int notches = e.getWheelRotation();
                double temp = zoom - (notches * 0.2);
                temp = Math.max(temp, 0.1);
                if (temp != zoom) {
                    zoom = temp;
                    resizeImage(imageLabel1, img1);
                    ComparisonWindow.this.zoomLabel1.setText("Zoom: " + (int)(zoom * 100) + "%");
                }
            }
        });
        /*
         * MouseListener reagujący na kręcenie rolką myszki, powoduje zmianę aktualnego przybliżenia obrazu prawego.
         */
        imageLabel2.addMouseWheelListener(e -> {
            if(img2 != null) {
                int notches = e.getWheelRotation();
                double temp = zoom - (notches * 0.2);
                temp = Math.max(temp, 0.1);
                if (temp != zoom) {
                    zoom = temp;
                    resizeImage(imageLabel2, img2);
                    ComparisonWindow.this.zoomLabel2.setText("Zoom: " + (int)(zoom * 100) + "%");
                }
            }
        });
        scrollPane1.setViewportView(imageLabel1);
        scrollPane2.setViewportView(imageLabel2);

        this.setVisible(true);
    }

    /**
     * Funkcja służaca do skalowania załadowanego obrazu.
     * @param label referencja do JLabel zawierającej skalowany obraz
     * @param img referencja do skalowanego obrazu
     */
    private void resizeImage(JLabel label, BufferedImage img) {
        AffineTransform t = AffineTransform.getScaleInstance(zoom, zoom);
        BufferedImageOp resizeOp = new AffineTransformOp(t, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        BufferedImage resizedImage = resizeOp.filter(img, null);
        Icon imageIcon = new ImageIcon(resizedImage);
        label.setIcon(imageIcon);
    }

    /**
     * Pozwala załadować nowy obraz w miejsce lewego obrazu.
     * @param imagePath ścieżka do nowego obrazu
     * @throws IOException
     * @throws NullPointerException
     */
    private void setLeftImage(String imagePath) throws IOException, NullPointerException {
        this.img1 = ImageIO.read(new File(imagePath));
        Icon loadedImageIcon = new ImageIcon(this.img1);
        this.imageLabel1.setIcon(loadedImageIcon);
        this.imagePathLabel1.setText(new File(imagePath).getName());
        this.revalidate();
        this.repaint();
    }

    /**
     * Pozwala załadować nowy obraz w miejsce prawego obrazu.
     * @param imagePath ścieżka do nowego obrazu
     * @throws IOException
     * @throws NullPointerException
     */
    private void setRightImage(String imagePath) throws IOException, NullPointerException {
        this.img2 = ImageIO.read(new File(imagePath));
        Icon loadedImageIcon = new ImageIcon(this.img2);
        this.imageLabel2.setIcon(loadedImageIcon);
        this.imagePathLabel2.setText(new File(imagePath).getName());
        this.revalidate();
        this.repaint();
    }

    /**
     * Utworzenie i inicjalizacja MenuBara na górze okna.
     * @return instancja MenuBar
     */
    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem leftImageItem = new JMenuItem("Load left image");
        JMenuItem rightImageItem = new JMenuItem("Load right image");

        leftImageItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int response = fileChooser.showOpenDialog(null);
                if (response == JFileChooser.APPROVE_OPTION) {
                    File path = new File(fileChooser.getSelectedFile().getAbsolutePath());
                    String selectedFilePath = path.getAbsolutePath();
                        try {
                            setLeftImage(selectedFilePath);

                        } catch (IOException | NullPointerException ex) {
                            JOptionPane.showMessageDialog(ComparisonWindow.this,
                                    "Cannot load file " + selectedFilePath,
                                    "Error loading image file",
                                    JOptionPane.ERROR_MESSAGE);
                        }
                        ComparisonWindow.this.fileChooser.setCurrentDirectory(path.getParentFile());
                    }
                }

        });

        rightImageItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int response = fileChooser.showOpenDialog(null);
                if (response == JFileChooser.APPROVE_OPTION) {
                    File path = new File(fileChooser.getSelectedFile().getAbsolutePath());
                    String selectedFilePath = path.getAbsolutePath();
                        try {
                            setRightImage(selectedFilePath);

                        } catch (IOException | NullPointerException ex) {
                            JOptionPane.showMessageDialog(ComparisonWindow.this,
                                    "Cannot load file " + selectedFilePath,
                                    "Error loading image file",
                                    JOptionPane.ERROR_MESSAGE);
                            ex.printStackTrace();
                        }
                        ComparisonWindow.this.fileChooser.setCurrentDirectory(path.getParentFile());
                    }
                }

        });

        fileMenu.add(leftImageItem);
        fileMenu.addSeparator();
        fileMenu.add(rightImageItem);
        menuBar.add(fileMenu);
        return menuBar;
    }

}