package com.compression;

import com.pixelmed.dicom.AttributeList;
import com.pixelmed.dicom.AttributeTag;
import com.pixelmed.dicom.DicomDictionary;
import com.pixelmed.dicom.DicomException;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * Panel tablicy z danymi wyciągniętymi z pliku DICOM, wyświetlany w lewym dolnym rogu aplikacji.
 */
public class DicomTablePanel extends JPanel {
    private final JTable table;
    private final DicomTableModel tableModel;
    private static final DicomDictionary dicomDictionary = AttributeList.getDictionary();

    public DicomTablePanel() {
        this.tableModel = new DicomTableModel();
        this.table = new JTable(this.tableModel) {
            @Override
            public String getToolTipText(MouseEvent event) {
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

        this.setLayout(new BorderLayout());
        Border outerBorder = BorderFactory.createEmptyBorder(5, 5, 5, 5);
        Border innerBorder = BorderFactory.createTitledBorder("DICOM Attributes");
        Border innerBorder2 = BorderFactory.createBevelBorder(BevelBorder.LOWERED);
        Border compoundBorder = BorderFactory.createCompoundBorder(outerBorder, innerBorder);
        this.setBorder(BorderFactory.createCompoundBorder(compoundBorder, innerBorder2));
        this.add(new JScrollPane(this.table), BorderLayout.CENTER);
    }

    public void setData(AttributeList dicomAttrs) {
        this.tableModel.setData(dicomAttrs);
        this.table.getColumnModel().getColumn(0).setPreferredWidth(20);
    }

    public void updateFrame(int newFrame) {
        this.tableModel.updateFrame(newFrame);
    }

}
