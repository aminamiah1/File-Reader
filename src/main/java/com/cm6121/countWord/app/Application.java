package com.cm6121.countWord.app;

import com.cm6121.countWord.app.utilityFile.FileReader;
import com.cm6121.countWord.app.utilityFile.WriterCSV;

import java.io.File;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.io.PrintWriter;

public class Application {

    public static void main(String[] args) {
        String documentToRead = ClassLoader.getSystemClassLoader().getResource("FolderDocumentsToRead").getPath();
        String myDir = "";
        String[] dirWords = documentToRead.split("/");
        for (int x = 4; x < dirWords.length; x++) {
            myDir = myDir + "/" + dirWords[x];
        }
        myDir = myDir + "/";
        myDir = myDir.replaceAll("%20", " ");
        File directoryPath = new File(System.getProperty("user.home") + myDir);

        HashMap<String, Integer> map = new HashMap<>();
        com.cm6121.countWord.app.utilityFile.FileReader fileReader = new FileReader();
        WriterCSV writerCSV = new WriterCSV();
        map = fileReader.start(new File(System.getProperty("user.home") + myDir));
        Scanner scanner = new Scanner(System.in);
        int response = -1;
        while (response != 5) {
            System.out.println();
            System.out.println("1- Do you want to display the names and number of documents \n " +
                    "2- Do you want to display number of occurrences of the words for each document\n " +
                    "3- Do you want to enter a word and display the occurrences in each document \n" +
                    "4- Do you want to display the number of each word in the whole corpus \n " +
                    "5- Exit \n");
            response = scanner.nextInt();
            switch (response) {
                case 1: {
                    System.out.println();
                    fileReader.start(new File(System.getProperty("user.home") + myDir));
                    System.out.println();
                    break;
                }
                case 2: {
                    System.out.println();
                    fileReader.readFolder(new File(System.getProperty("user.home") + myDir));
                    System.out.println();
                    break;
                }
                case 3: {
                    System.out.println();
                    scanner.nextLine();
                    System.out.print("Enter a word: ");
                    String words = scanner.nextLine();
                    for (File file : Objects.requireNonNull(new File(writerCSV.createDirectory()).listFiles())) {
                        final List<String[]> strings = fileReader.readCSVMethod(file);
                        for (String[] word : strings) {
                            if (word[0].equalsIgnoreCase(words)) {
                                System.out.println("Number of times the word " + words + " appears in "
                                        + file.getName() + " is " + word[1]);
                            }
                        }
                    }
                    break;
                }
                case 4: {
                    for (Map.Entry<String, Integer> entry : map.entrySet()) {
                        System.out.println("Word: " + entry.getKey() + ", Number of times: " + entry.getValue());
                    }
                }
                case 5: {
                    break;
                }
            }
        }
        fileReader.readFolder(new File(System.getProperty("user.home") + myDir));
    }
}
