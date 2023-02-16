package com.company;

public class FileAndDirectory {

    private String name;
    private String directory;

    public FileAndDirectory(String name) {
        this.name = name;
    }

    public FileAndDirectory(String name, String directory) {
        this.name = "C:\\HardDrive\\" + name;
        this.directory = directory;
    }

    private void setName() {
        this.name = "C:\\HardDrive\\" + name;
    }

    private void setDirectory() {
        this.directory = directory;
    }

    public String getName() {
        return name;
    }

    public String getDirectory() {
        return directory;
    }
}
