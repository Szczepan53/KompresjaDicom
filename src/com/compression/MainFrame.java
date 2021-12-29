package com.compression;

import com.pixelmed.dicom.AttributeList;
import com.pixelmed.dicom.DicomException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;


/**
 * Okno główne aplikacji.
 * <p>Pełni rolę kontrolera spajając wszystkie elementy aplikacji i przekazując dane pomiędzy nimi.</p>
 */
public class MainFrame extends JFrame {
    private final JFileChooser fileChooser;
    private final MenuPanel menuPanel;
    private final DicomTablePanel tablePanel;
    private final ImagePanel imagePanel;
    private final ComparisonPanel comparisonPanel;
    public static String destinationFilePath;
    private String dicomFilePath;
    private String outputJpgFilePath;
    private String outputPngFilePath;
    private final AttributeList dicomAttrs;

    public MainFrame(String title, String dicomFilePath) throws DicomException, IOException {
        super(title);
        this.dicomFilePath = dicomFilePath;
        this.outputJpgFilePath = makeCompressedFilePath(dicomFilePath, "jpg");
        this.outputPngFilePath = makeCompressedFilePath(dicomFilePath, "png");
        this.dicomAttrs = new AttributeList();
        this.dicomAttrs.read(dicomFilePath);
        this.imagePanel = new ImagePanel(dicomFilePath, this.dicomAttrs);
        this.comparisonPanel = new ComparisonPanel();
        this.menuPanel = new MenuPanel();
        this.tablePanel = new DicomTablePanel();
        JPanel menuContainer = new JPanel();
        this.fileChooser = new JFileChooser();
        this.setJMenuBar(createMenuBar());


        this.setLayout(new BorderLayout());
        this.setSize(new Dimension(1280, 720));
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);

        /* Ustawienie akcji wykonywanej przy próbie zamknięcia okna */
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                defaultExitAction();
            }
        });

        fileChooser.setFileFilter(new FileNameExtensionFilter("DICOM Files", "dcm"));
        this.add(this.imagePanel, BorderLayout.CENTER);
        this.add(this.comparisonPanel, BorderLayout.EAST);

        /* Inicjalizacja panelu sterowania (menuPanel) oraz tablicy atrybutów wczytanego pliku DICOM */
        menuContainer.setLayout(new BorderLayout());
        menuContainer.add(this.menuPanel, BorderLayout.NORTH);
        this.tablePanel.setData(this.dicomAttrs);

        menuContainer.add(this.tablePanel, BorderLayout.CENTER);
        this.add(menuContainer, BorderLayout.WEST);

        this.menuPanel.setMenuPanelListener(new MenuPanelListener() {
            @Override
            public void menuEventHandler(MenuEvent ev) {
                try {
                    String compressionType = ev.getCompressionType();
                    int compressionQuality = ev.getCompressionQuality();
                    String imprintInfo = ev.getImprintInfo();
                    setOutputJpgFilePath(makeCompressedFilePath(MainFrame.this.dicomFilePath, "jpg"));
                    setOutputPngFilePath(makeCompressedFilePath(MainFrame.this.dicomFilePath, "png"));
                    String outputFilePath = compressionType.equals("jpg") ? outputJpgFilePath : outputPngFilePath;

                    Compressor.compressImage(MainFrame.this.dicomFilePath, outputFilePath,
                            compressionType, compressionQuality,
                            imagePanel.getWindowCenter(), imagePanel.getWindowWidth(),
                            imprintInfo);

                    setDestinationFilePath(outputFilePath);
                    comparisonPanel.setUpperImage(ImageIO.read(new File(outputFilePath)));
                } catch (IOException | DicomException ex) {
                    ex.printStackTrace();
                }
            }
        });

        this.imagePanel.setImagePanelListener(new ImagePanelListener() {
            @Override
            public void imagePanelEventHandler(ImagePanelEvent ev) {
                MainFrame.this.tablePanel.updateFrame(ev.getFrameIndex());
            }
        });


        this.setVisible(true);

    }


    /** Funkcja tworząca ścieżkę pliku wyjściowego (po kompresji) na podstawie ścieżki pliku wejściowego (DICOM)
    *
    * <p>>imo trzeba zrobić tak żeby zapisywał stopień stopień kompresji pliku wyściowego w nazwie pliku </p>*/
    private static String makeCompressedFilePath(String filePath, String destFormat) {

        String[] partPath = filePath.split("\\\\");
        partPath[partPath.length - 2] += "\\outputs";
        String[] lastPart = partPath[partPath.length - 1].split("\\.");
        String changedFormatLastPart;
        try{
            lastPart[1] = destFormat;
            changedFormatLastPart = String.join(".", lastPart);
        }
        catch (IndexOutOfBoundsException ex){
            changedFormatLastPart = lastPart[0] + "." + destFormat;
        }
        partPath[partPath.length - 1] = changedFormatLastPart;
        String newPath = String.join("\\", partPath);
        String dirPath = newPath.replace(changedFormatLastPart, "");
        File outputsDir = new File(dirPath);
        if (!outputsDir.exists()) {
            outputsDir.mkdir();
        }
        File imagePath = new File(newPath);
        if(imagePath.exists()){
            partPath = filePath.split("\\\\");
            partPath[partPath.length - 2] += "\\outputs";
            lastPart = partPath[partPath.length - 1].split("\\.");
            lastPart[0] = lastPart[0]+"(1)";
            lastPart[1] = destFormat;
            changedFormatLastPart = String.join(".", lastPart);
            partPath[partPath.length - 1] = changedFormatLastPart;
            newPath = String.join("\\", partPath);
        }
        System.out.println(newPath);
//        destinationFilePath = newPath;
        return newPath;
    }

    private void setDicomFilePath(String dicomFilePath) {
        this.dicomFilePath = dicomFilePath;
    }
    private void setDestinationFilePath(String destinationFilePath){
        MainFrame.destinationFilePath = destinationFilePath;
    }

    private void setImage(String newDicomFilePath) throws IOException, DicomException {
        this.dicomAttrs.clear();
        this.dicomAttrs.read(newDicomFilePath);
        this.imagePanel.setImage(newDicomFilePath, this.dicomAttrs);
        this.revalidate();
        this.repaint();
    }

    private void setOutputJpgFilePath(String outputJpgFilePath) {
        this.outputJpgFilePath = outputJpgFilePath;
    }

    private void setOutputPngFilePath(String outputPngFilePath) {
        this.outputPngFilePath = outputPngFilePath;
    }

    /* Inicjalizacja menuBar na górze ekranu
    * > menuFile pozwala wczytać plik DICOM (przeniosłem tu do openFileItem to co mieliśmy jako ten przycisk Choose File),
    * a także pozwala zamknąć program
    *
    * > w menuTools chciałem zrobić to narzędzie do zoomowania obrazków (zoomToolItem) ale nie mam na razie pomysłu jak
    * */

    private JMenuBar createMenuBar(){
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        //Initialize File menu//////////////////
        fileMenu.setMnemonic(KeyEvent.VK_F);

        JMenuItem openFileItem = new JMenuItem("Open...", new ImageIcon("icons/png/ic-folder.png"));
        openFileItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int response = fileChooser.showOpenDialog(null);
                if (response == JFileChooser.APPROVE_OPTION) {
                    File path = new File(fileChooser.getSelectedFile().getAbsolutePath());
                    String selectedFilePath = path.getAbsolutePath();
                    try {
                        setDicomFilePath(selectedFilePath);
                        setOutputJpgFilePath(makeCompressedFilePath(selectedFilePath, "jpg"));
                        setOutputPngFilePath(makeCompressedFilePath(selectedFilePath, "png"));
                        setImage(dicomFilePath);
                        dicomAttrs.clear();
                        dicomAttrs.read(dicomFilePath);
                        tablePanel.setData(dicomAttrs);
                    } catch (IOException | DicomException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        openFileItem.setMnemonic(KeyEvent.VK_O);
        openFileItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));

        JMenuItem exitFileItem = new JMenuItem("Exit");
        exitFileItem.setMnemonic(KeyEvent.VK_X);

        exitFileItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                defaultExitAction();
            }
        });
        fileMenu.add(openFileItem);
        fileMenu.addSeparator();
        fileMenu.add(exitFileItem);
        menuBar.add(fileMenu);

        //Initialize Tools menu////////////
        JMenu toolsMenu = new JMenu("Tools");
        toolsMenu.setMnemonic(KeyEvent.VK_T);

        JMenuItem zoomToolItem = new JMenuItem("Zoom", new ImageIcon("icons/png/ic-magnifying-glass.png"));

        toolsMenu.add(zoomToolItem);
        menuBar.add(toolsMenu);

        return menuBar;
    }

    /** Funkcja wywoływana przy próbie zamknięcia programu albo przez X w prawym górnym rogu
    albo przez opcję Exit w FileMenu
     */
    public void defaultExitAction() {
        int action = JOptionPane.showConfirmDialog(MainFrame.this,
                "Are you sure you want to exit the program?",
                "Exit dialog",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if(action == JOptionPane.OK_OPTION) {
            System.exit(0);
        }
    }
}
