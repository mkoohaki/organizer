package com.company;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main {

    private static Scanner input = new Scanner(System.in);

    public static void main(String[] args) throws IOException {

        FileAndDirectory directories = new FileAndDirectory(getResultDirectoryName(), getDirectoryName());
        createDirectory(directories.getName());
        copyPhotos(directories.getDirectory(), directories.getName());
        findingFiles(directories.getName());
    }

    private static void findingFiles(String directory) {

        File files = new File(directory);
        String[] photos = files.list();
        assert photos != null;
        Map<String, Set<File>> pictures = new HashMap<>();
        DateFormat dateFormat = new SimpleDateFormat("yyyy, MMMM dd");
        for(String photo: photos){
            File file = new File(directory + "//" + photo);
            long time = file.lastModified();
            Set<File> photoSet = new HashSet<>();
            if(pictures.containsKey(dateFormat.format(time)))
                photoSet = pictures.get(dateFormat.format(time));
            photoSet.add(file);
            pictures.put(dateFormat.format(time), photoSet);
        }
        sorting(directory, pictures);
    }

    private static void sorting(String directory, Map<String, Set<File>> pictures) {

        for(String key : pictures.keySet()){
            createDirectory(directory + "\\" + key);
        }

        for(Map.Entry<String, Set<File>> photos : pictures.entrySet()){
            String key = photos.getKey();
            Set<File> value = photos.getValue();
            for(File val : value){
                String[] v = val.toString().split("\\\\");
                val.renameTo(new File(directory + "\\" + key + "\\" + v[v.length-1]));
            }
        }
    }

    private static void copyPhotos(String directory, String name)  throws IOException {

        Path sourceDirectory = Paths.get(directory);
        Files.walkFileTree (sourceDirectory, new HashSet<>(), 2, new FileVisitor<>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
                    throws IOException {
                //System.out.println("preVisitDirectory: " + dir.toString());
                if(!dir.equals(sourceDirectory)) {
                    String[] newDir = dir.toString().split("\\\\");
                    FileAndDirectory newDirectory = new FileAndDirectory(name + "\\" + newDir[newDir.length - 1]);
                    createDirectory(newDirectory.getName());
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                    throws IOException {
                //System.out.println("visitFile: " + file);
                String[] target = file.toString().split("\\\\");
                Files.copy(file, Path.of(name + "\\" + target[target.length-1]));
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc)
                    throws IOException {
                //System.out.println("visitFileFailed: " + file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc)
                    throws IOException {
                //System.out.println("postVisitDirectory: " + dir);
                return FileVisitResult.CONTINUE;
            }
        });

    }

    private static String getDirectoryName() {
        String directory = "";
        Path checkDirectory;
        File photos;
        do {
            System.out.print("Enter the directory: ");
            directory = input.nextLine();
            checkDirectory = Path.of(directory);
            if(!Files.exists(checkDirectory))
                System.out.println("The directory not exist");

            photos = new File(directory);
            if(photos.length() == 0)
                System.out.println("The directory is empty");

        } while (directory.equals("") || !Files.exists(checkDirectory) || photos.length() == 0);

        return directory;
    }

    private static void createDirectory(String destinationDirectory) {
        File f = new File(destinationDirectory);
        if(f.mkdir())
            System.out.println("\"" + destinationDirectory + "\" has been created successfully");
        else
            System.out.println("Directory " + "\"" + destinationDirectory + "\"" + " already exists");
    }

    private static String getResultDirectoryName() {
        String name = "";
        Path directory;
        do {
            System.out.print("Enter the name for result directory: ");
            name = input.nextLine();
            directory = Path.of("C:\\HardDrive\\" + name);
            if(Files.exists(directory))
                System.out.println("The directory name already exists");
        } while (name.equals("") || Files.exists(directory));

        return name;
    }
}
