package com.skripsi.skripsiservice.domain;

import java.io.InputStream;

public class MaterialTypeCheck {
    private InputStream inputStream;

    private String filename;

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
