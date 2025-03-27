package com.example.demo.entity;

public class fileInfo {
    private String name;
    private boolean directory;
    private long size;
    public fileInfo() {
        this.name = "";
        this.directory = false;
        this.size = 0;
    }

    public fileInfo(String name, boolean directory, long size) {
        this.name = name;
        this.directory = directory;
        this.size = size;
    }

    public void setName(String name) { this.name = name; }

    public void setDirectory(boolean directory) { this.directory = directory; }
    public String getName() { return name; }

    public boolean isDirectory() { return directory; }

    public long getSize() { return size; }

    public void setSize(long size) { this.size = size; }

}
