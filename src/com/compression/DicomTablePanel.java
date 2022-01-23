package com.compression;

import com.pixelmed.dicom.AttributeList;
import com.pixelmed.dicom.AttributeTag;
import com.pixelmed.dicom.DicomDictionary;
import com.pixelmed.dicom.DicomException;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Panel tablicy z danymi wyciągniętymi z pliku DICOM, wyświetlany w lewym dolnym rogu aplikacji.
 * <p>Za pomocą prawego kliknięcia myszy otwiera się menu kontekstowe pozwalające na skopiowanie zawartości zaznaczonej
 * komórki do schowka.</p>
 */
public class DicomTablePanel extends JPanel {
    private static final DicomDictionary dicomDictionary = AttributeList.getDictionary();
    private final JTable table;
    private final DicomTableModel tableModel;
    private final TablePopupMenu tablePopupMenu;

    public DicomTablePanel() {
        this.tableModel = new DicomTableModel();
        this.tablePopupMenu = new TablePopupMenu();

        this.table = new JTable(this.tableModel) {
            @Override
            public String getToolTipText(MouseEvent event) {
            //Przytrzymanie kursora myszy nad komórką z nazwą atrybutu pozwala wyświetlić jego pełną nazwę (zamiast skróconej).
                String tip = null;
                java.awt.Point p = event.getPoint();
                int rowIndex = rowAtPoint(p);
                int columnIndex = columnAtPoint(p);

                try {
                    if(rowIndex != 0){
                        if(columnIndex == 1) {
                            String tagStr = getValueAt(rowIndex, 0).toString();
                            tip = dicomDictionary.getFullNameFromTag(new AttributeTag(tagStr));
                        }
                        else {
                            tip = getValueAt(rowIndex, columnIndex).toString();
                        }
                    }
                } catch (RuntimeException | DicomException ignored) {}

                return tip;
            }
        };

        /* Dodanie menu kontekstowego wyświetlanego prawym kliknięciem myszy w obszarze tablicy atrybutów
         (umożliwia kopiowanie danych z tablicy). */
        this.table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(SwingUtilities.isRightMouseButton(e) && e.getClickCount() == 1) {
                    DicomTablePanel.this.tablePopupMenu.show(DicomTablePanel.this.table, e.getX(), e.getY());
                }
            }
        });

        this.setLayout(new BorderLayout());
        Border outerBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
        Border innerBorder = BorderFactory.createTitledBorder("DICOM Attributes");
        Border innerBorder2 = BorderFactory.createBevelBorder(BevelBorder.LOWERED);
        Border compoundBorder = BorderFactory.createCompoundBorder(outerBorder, innerBorder);
        this.setBorder(BorderFactory.createCompoundBorder(compoundBorder, innerBorder2));
        this.add(new JScrollPane(this.table), BorderLayout.CENTER);
}

    /**
     * Załadowanie nowego zestawu danych do tablicy atrybutów.
     * @param dicomAttrs lista atrybutów DICOM nowo załadowanego pliku DICOM
     */
    public void setData(AttributeList dicomAttrs) {
        this.tableModel.setData(dicomAttrs);
        this.table.getColumnModel().getColumn(0).setPreferredWidth(20);
    }

    public void updateFrame(int newFrame) {
        this.tableModel.updateFrame(newFrame);
    }

    /**
     * Menu kontekstowe.
     * <p>Umożliwia skopiowanie zawartości komórki do schowka.</p>
     */
    private class TablePopupMenu extends JPopupMenu {
        public TablePopupMenu() {
            super();
            JMenuItem copy = new JMenuItem("Copy");

            copy.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    int rowIndex = DicomTablePanel.this.table.getSelectedRow();
                    int columnIndex = DicomTablePanel.this.table.getSelectedColumn();
                    String value = DicomTablePanel.this.table.getValueAt(rowIndex, columnIndex).toString();
                    StringSelection valueText = new StringSelection(value);
                    Toolkit.getDefaultToolkit().getSystemClipboard().setContents(valueText, null);
                }
            });
            this.add(copy);
        }
    }
}
