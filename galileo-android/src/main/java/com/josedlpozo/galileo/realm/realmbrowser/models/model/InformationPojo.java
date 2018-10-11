package com.josedlpozo.galileo.realm.realmbrowser.models.model;

public class InformationPojo {
    private final long sizeInByte;
    private final String path;

    public InformationPojo(long sizeInByte, String path) {
        this.sizeInByte = sizeInByte;
        this.path = path;
    }

    public long getSizeInByte() {
        return sizeInByte;
    }

    public String getPath() {
        return path;
    }
}
