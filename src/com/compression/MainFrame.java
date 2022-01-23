package com.compression;

import com.pixelmed.dicom.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;


/**
 * Okno główne aplikacji.
 * <p>Pełni rolę kontenera oraz kontrolera spajając wszystkie elementy aplikacji i przekazując dane pomiędzy nimi.</p>
 */
public class MainFrame extends JFrame{
    private final JFileChooser fileChooser;
    private final MenuPanel menuPanel;
    private final DicomTablePanel tablePanel;
    private final ImagePanel imagePanel;
    public static String destinationFilePath;
    private String dicomFilePath;
    public String outputJpgFilePath;
    public String outputPngFilePath;
    private AttributeList dicomAttrs;
    private final ProgressDialog progressDialog;
    private Dimension lastSize;

    /**
     * Inicjalizacja okna głównego aplikacji.
     * @param title tytuł okna głównego
     * @param dicomFilePath ścieżka pliku DICOM ładowanego przy uruchamianiu aplikacji
     * @throws DicomException
     * @throws IOException
     */
    public MainFrame(String title, String dicomFilePath) throws DicomException, IOException {
        super(title);
        this.dicomFilePath = dicomFilePath;
        this.outputJpgFilePath = makeCompressedFilePath(dicomFilePath, "jpg", 50);
        this.outputPngFilePath = makeCompressedFilePath(dicomFilePath, "png", 50);
        this.dicomAttrs = new AttributeList();
        this.dicomAttrs.read(dicomFilePath);
        this.imagePanel = new ImagePanel(dicomFilePath, this.dicomAttrs);
        this.menuPanel = new MenuPanel();
        this.tablePanel = new DicomTablePanel();
        JPanel menuContainer = new JPanel();
        this.fileChooser = new JFileChooser();
        this.progressDialog = new ProgressDialog(this);
        this.lastSize = new Dimension(1280, 720);
        this.setJMenuBar(createMenuBar());


        this.setLayout(new BorderLayout());
        this.setSize(this.lastSize);

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
        fileChooser.setCurrentDirectory(new File("dicom_files"));
        this.add(this.imagePanel, BorderLayout.CENTER);

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
                    CompressionType compressionType = ev.getCompressionType();
                    int compressionQuality = ev.getCompressionQuality();
                    String imprintInfo = ev.getImprintInfo();
                    setOutputJpgFilePath(makeCompressedFilePath(MainFrame.this.dicomFilePath, "jpg", compressionQuality));
                    setOutputPngFilePath(makeCompressedFilePath(MainFrame.this.dicomFilePath, "png", compressionQuality));
                    String outputFilePath = (compressionType == CompressionType.JPEG) ? outputJpgFilePath : outputPngFilePath;
                    setDestinationFilePath(outputFilePath);

                    compressDicomImage(outputFilePath, compressionType, compressionQuality, imprintInfo);
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


        this.setLocationRelativeTo(null); //Centruje okno na ekranie
        this.setVisible(true);

    }


    /** Funkcja tworząca ścieżkę pliku wyjściowego (po kompresji) na podstawie ścieżki pliku wejściowego (DICOM)
     * oraz wybranego imageQuality.
     * <p>Jeśli utworzona ścieżka pliku wyjściowego już istnieje to modyfikuje utworzoną ścieżkę o suffiks {liczba}.</p>
     * */
    private static String makeCompressedFilePath(String filePath, String destFormat, int imageQuality) {

        String qualityPart = destFormat.equals("jpg")? ("_quality_" + imageQuality) : "";

        String[] partPath = filePath.split("\\\\");
        partPath[partPath.length - 2] += "\\outputs";
        String[] lastPart = partPath[partPath.length - 1].split("\\.");
        String changedFormatLastPart;
        try{
            lastPart[1] = destFormat;
            lastPart[0] += qualityPart;
            changedFormatLastPart = String.join(".", lastPart);
        }
        catch (IndexOutOfBoundsException ex){
            lastPart[0] += qualityPart;
            changedFormatLastPart = lastPart[0] + "." + destFormat;
        }

        partPath[partPath.length - 1] = changedFormatLastPart;
        String newPath = String.join("\\", partPath);
        String dirPath = newPath.replace(changedFormatLastPart, "");
        File outputsDir = new File(dirPath);
        if (!outputsDir.exists()) {
            outputsDir.mkdir();
        }

        //Wyciągnięcie ostatniego numeru pliku w nawiasach
        String fileName = lastPart[0];
        File[] matchedFiles = outputsDir.listFiles(new OutputFileFilter(fileName));
        String latestFile = null;

        if(matchedFiles != null && matchedFiles.length > 0) {
           latestFile = Arrays.stream(matchedFiles)
                   .map((File file) -> file.getName().split("\\.")[0])
                   .sorted().peek(System.out::println)
                   .max(Comparator.naturalOrder()).get();
        }

        int lastNumber = -1;
        if(latestFile != null) {
            lastNumber = 0;
            String[] parts = latestFile.split("\\{");
            try {
                lastNumber = Integer.parseInt(parts[parts.length-1].substring(0, 1));
            }
            catch (ArrayIndexOutOfBoundsException | NumberFormatException ignore) {}
        }


        if(lastNumber >= 0){
            lastPart[0] = lastPart[0] + "{" + (++lastNumber) + "}";
            lastPart[1] = destFormat;
            changedFormatLastPart = String.join(".", lastPart);
            newPath = dirPath + changedFormatLastPart;
        }
        System.out.println(newPath);

        return newPath;
    }

    /**
     * Funkcja opakowująca funkcję compressImage z klasy statycznej Compressor, pozwalająca operacji kompresji obrazu
     * na działanie w oddzielnym wątku.
     * @param outputFilePath ścieżka docelowa pliku po kompresji
     * @param compressionType żądany typ kompresji (JPEG lub PNG)
     * @param compressionQuality żądana jakość po kompresji (w przypadku kompresji JPEG)
     * @param imprintInfo parametr decydujący, czy nadrukować dodatkowe informacji z pliku DICOM na obraz wyjściowy
     * @throws IOException
     * @throws DicomException
     */
    private void compressDicomImage(String outputFilePath,
                                    CompressionType compressionType,
                                    int compressionQuality,
                                    String imprintInfo) throws IOException, DicomException{

        int nFrames = Attribute.getSingleIntegerValueOrDefault(this.dicomAttrs, TagFromName.NumberOfFrames, 1);

        this.progressDialog.setnFrames(nFrames);
        this.progressDialog.setCompressionFormat(compressionType.toString());
        this.progressDialog.setVisible(true);

        SwingWorker<Integer, Integer> worker = new SwingWorker<Integer, Integer>() {
            private String firstOutputFilePath;

            @Override
            protected Integer doInBackground() throws Exception {

                String[] outputFilePaths = Compressor.compressImage(MainFrame.this.dicomFilePath, outputFilePath,
                        compressionType.toString(), compressionQuality,
                        imagePanel.getWindowCenter(), imagePanel.getWindowWidth(),
                        imprintInfo);

                this.firstOutputFilePath = outputFilePaths[0];
                return null;
            }

            @Override
            protected void process(List<Integer> chunks) {
                super.process(chunks);
            }

            @Override
            protected void done() {
                super.done();
                progressDialog.setVisible(false);
            }
        };

        worker.execute();


    }

    private void setDicomFilePath(String dicomFilePath) {
        this.dicomFilePath = dicomFilePath;
    }

    private static void setDestinationFilePath(String destinationFilePath){
        MainFrame.destinationFilePath = destinationFilePath;
    }

    /**
     * Zmienia wyświetlany w ImagePanel plik obrazu z DICOMa.
     * @param newDicomFilePath ścieżka nowego pliku DICOM, którego dane obrazowe mają zostać wyświetlone w ImagePanel
     * @throws IOException
     * @throws DicomException
     */
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

    /** Inicjalizacja menuBar na górze ekranu
    * <p>> menuFile pozwala wczytać plik DICOM (przeniosłem tu do openFileItem to co mieliśmy jako ten przycisk Choose File),
    * a także pozwala zamknąć program</p>
    *
    * <p>> menuTools ma opcję otworzenia okienka do porównywania obrazów
    * */

    private JMenuBar createMenuBar(){
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        //Initialize File menu//////////////////
        fileMenu.setMnemonic(KeyEvent.VK_F);

        JMenuItem openFileItem = new JMenuItem("Open...", new ImageIcon("icons/png/ic-folder.png"));
        openFileItem.setToolTipText("Select and open DICOM file...");
        openFileItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int response = fileChooser.showOpenDialog(null);
                if (response == JFileChooser.APPROVE_OPTION) {
                    File path = new File(fileChooser.getSelectedFile().getAbsolutePath());
                    String selectedFilePath = path.getAbsolutePath();
                    if(DicomFileUtilities.isDicomOrAcrNemaFile(path)) { //Sprawdzenie czy ładowany plik zawiera DICOM dataset
                            AttributeList newAttrs = new AttributeList();
                        try {
                            newAttrs.read(selectedFilePath);
                            setImage(selectedFilePath);

                        } catch (IOException | DicomException ex) {
                            ex.printStackTrace();
                        }
                        //Jeśli plik dicom załadował się poprawnie to zaktualizuj dane
                            setDicomFilePath(selectedFilePath);

                            dicomAttrs = newAttrs;
                            tablePanel.setData(dicomAttrs);
                    }
                    else{
                        JOptionPane.showMessageDialog(MainFrame.this,
                                "Cannot load file " + selectedFilePath,
                                "Error loading DICOM",
                                JOptionPane.ERROR_MESSAGE);
                    }

                }
            }
        });

        openFileItem.setMnemonic(KeyEvent.VK_O);
        openFileItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));

        JMenuItem fullscreenFileItem = new JMenuItem("Fullscreen", new ImageIcon("icons/png/ic-monitor.png"));
        fullscreenFileItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F12, InputEvent.CTRL_MASK));

        fullscreenFileItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainFrame.this.dispose();
                boolean isFullscreen = MainFrame.this.isUndecorated();
                MainFrame.this.setUndecorated(!isFullscreen);
                fullscreenFileItem.setText(isFullscreen? "Fullscreen" : "Exit fullscreen");
                MainFrame.this.pack();
                if(isFullscreen) {
                    MainFrame.this.setSize(MainFrame.this.lastSize);
                }
                else {
                    MainFrame.this.lastSize = MainFrame.this.getSize();
                    MainFrame.this.setExtendedState(JFrame.MAXIMIZED_BOTH);
                }
                MainFrame.this.setVisible(true);
            }
        });

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
        fileMenu.add(fullscreenFileItem);
        fileMenu.addSeparator();
        fileMenu.add(exitFileItem);
        menuBar.add(fileMenu);

        //Initialize Tools menu////////////
        JMenu toolsMenu = new JMenu("Tools");
        toolsMenu.setMnemonic(KeyEvent.VK_T);

        JMenuItem compareToolItem = new JMenuItem("Compare images",
                new ImageIcon("icons/png/ic-magnifying-glass.png"));

        compareToolItem.addActionListener((a)->new ComparisonWindow(MainFrame.this));

        toolsMenu.add(compareToolItem);
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
