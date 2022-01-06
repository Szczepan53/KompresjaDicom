package com.compression;

/**
 * Typ wyliczeniowy reprezentujący wybrany tryb kompresji obrazu z DICOMa.
 */

public enum CompressionType {
    JPEG("JPEG"),
    PNG("PNG");

    private final String name;

    CompressionType(String name) {
        this.name = name;
    }


    @Override
    public String toString() {
        return name;
    }
}
