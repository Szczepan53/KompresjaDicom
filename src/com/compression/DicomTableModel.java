package com.compression;

import com.pixelmed.dicom.Attribute;
import com.pixelmed.dicom.AttributeList;
import com.pixelmed.dicom.AttributeTag;
import com.pixelmed.dicom.DicomDictionary;

import javax.swing.table.AbstractTableModel;
import java.util.*;

    /**
     * Model tablicy wykorzystywany w tablicy zawartej w DicomTablePanel
     * <p>Zawiera atrybuty DICOM wyciągnięte z załadowanego pliku DICOM.</p>
     */
public class DicomTableModel extends AbstractTableModel {
    private static final DicomDictionary dicomDictionary = AttributeList.getDictionary();

    private TreeMap<String, String> attrsMap;
    private String[][] data;
    private final String[] columnNames = {"Tag", "Attribute", "Value"};
    private int instanceNumberRow;

    public DicomTableModel() {
        this.attrsMap = new TreeMap<>();
        this.data = new String[0][0];
    }

    public DicomTableModel(AttributeList dicomAttrs) {
        this.setData(dicomAttrs);
    }

    public void setData(AttributeList dicomAttrs) {
        this.attrsMap = new TreeMap<>();
        for(Map.Entry<AttributeTag, Attribute> entry : dicomAttrs.entrySet()){
            String attributeName = dicomDictionary.getNameFromTag(entry.getKey());
            if(attributeName == null) continue; //skip attributes unhandled by pixelmed library

            String valueString = entry.getValue().getDelimitedStringValuesOrNull();
            if(valueString != null){
                this.attrsMap.put(dicomDictionary.getNameFromTag(entry.getKey()), valueString);
            }
            else {
                this.attrsMap.put(dicomDictionary.getNameFromTag(entry.getKey()), ">>NON-PRINTABLE-DATA<<");
            }
        }

        this.data = new String [this.attrsMap.size()][3];
        int counter = 0;
        this.instanceNumberRow = -1;
        for (Map.Entry<String, String> entry : this.attrsMap.entrySet()) {
            String key = entry.getKey();
            if(key.equals("InstanceNumber")) {
                this.instanceNumberRow = counter;
            }
            this.data[counter][0] = dicomDictionary.getTagFromName(entry.getKey()).toString();
            this.data[counter][1] = entry.getKey();
            this.data[counter][2] = entry.getValue();
            counter++;
        }

        this.fireTableDataChanged();
    }

    public void updateFrame(int newFrame) {
        if(!attrsMap.containsKey("NumberOfFrames") || this.instanceNumberRow < 0) return;

        this.attrsMap.replace("InstanceNumber", Integer.toString(newFrame));
        this.data[this.instanceNumberRow][2] = Integer.toString(newFrame);
        this.fireTableCellUpdated(this.instanceNumberRow, 2);
    }

    @Override
    public int getRowCount() {
        return attrsMap.size();
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return this.data[rowIndex][columnIndex];
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }
}
