package com.example.demo.entity;

public class fileInfo {
    private String name;
    private boolean directory;
    private long size;
    private long lastModified;
    public fileInfo() {
        this.name = "";
        this.directory = false;
        this.size = 0;
        this.lastModified = 0;
    }

    public fileInfo(String name, boolean directory, long size, long lastModified) {
        this.name = name;
        this.directory = directory;
        this.size = size;
        this.lastModified = lastModified;
    }

    public void setName(String name) { this.name = name; }

    public void setDirectory(boolean directory) { this.directory = directory; }
    public String getName() { return name; }

    public boolean isDirectory() { return directory; }

    public long getSize() { return size; }

    public void setSize(long size) { this.size = size; }

    public long getLastModified() { return lastModified; }

    public void setLastModified(long lastModified) { this.lastModified = lastModified; }

}
