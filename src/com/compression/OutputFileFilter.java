package com.compression;

import java.io.File;
import java.io.FileFilter;
import java.util.regex.Pattern;

/**
 * Filtr nazw plików stosowany do znajdowania plików o tej samej nazwie rozszerzonej o suffiksy:
 * <p>{numer}</p>
 * <p>{numer}_frameNumber </p>
 */

public class OutputFileFilter implements FileFilter {
    private final String searchedFileName;

    public OutputFileFilter(String searchedFileName) {
        this.searchedFileName = searchedFileName;
    }

    @Override
    public boolean accept(File f) {
        String fileName = f.getName();
        String[] fileNameParts = fileName.split("\\.");
        return fileNameParts[0].matches( Pattern.quote(searchedFileName) + "(\\{\\d+})*(_[0-9]*)?$");
    }
}
