package com.cm6121.countWord.app.utilityFile;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class FileReader {
    /**
     * Method that will return a List of Array of String that will contains the CSV content.
     * The method skip the first line of the CSV
     * You can implement another method, or use this one to read a CSV. This method is using OpenCSV
     *
     * @param file, the CSV file
     * @return a List of String array
     */
    public List<String[]> readCSVMethod(File file) {
        List<String[]> strings = new ArrayList<>();
        try {
            CSVReader reader = new CSVReader(
                    new InputStreamReader(new FileInputStream(file), "UTF-8"));
            {
                reader.skip(1);
                strings = reader.readAll();
            }
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
        return strings;
    }

    public void readFolder(File dir) {
        File[] files = dir.listFiles();
        HashMap<String, Integer> map = new HashMap<>();
        WriterCSV writerCSV = new WriterCSV();
        FileWriter writer;
        assert files != null;
        for (File file : files) {
            List<String[]> list = readCSVMethod(file);
            String[] header = new String[]{list.get(0)[0], list.get(0)[2]};
            String title = list.get(0)[0].replaceAll(" ", "_");
            File saveFile = new File(writerCSV.createDirectory() + title + "_allWords.csv");
            try {
                if (!saveFile.exists()) {
                    saveFile.createNewFile();
                }
                writer = new FileWriter(saveFile);
                CSVWriter write = new CSVWriter(writer);
                write.writeNext(header);
                for (String[] strings : list) {
                    instanceC(strings[1], map);
                    System.out.println(strings[1]);
                    System.out.println();
                    map = ascendingOrder(map);
                    for (Map.Entry<String, Integer> entry : map.entrySet()) {
                        String[] data = new String[]{entry.getKey(), String.valueOf(entry.getValue())};
                        write.writeNext(data);
                        System.out.println("Word: " + entry.getKey() + ", Number of times: " + entry.getValue());
                    }
                    System.out.println();
                }
                write.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            map = new HashMap<>();
        }
    }

    public HashMap<String, Integer> start(File dir) {
        File[] files = dir.listFiles();
        HashMap<String, Integer> map = new HashMap<>();
        WriterCSV writerCSV = new WriterCSV();
        FileWriter writer = null;
        CSVWriter write = null;
        System.out.println("The Number of Documents in the Folder: " + files.length);
        assert files != null;
        for (File file : files) {
            List<String[]> list = readCSVMethod(file);
            for (String[] strings : list) {
                String[] header = new String[]{"Word", "Number of Occurrence"};
                File saveFile = new File(writerCSV.createDirectory() + "CSVAllDocuments_allWords.csv");
                try {
                    if (!saveFile.exists()) {
                        saveFile.createNewFile();
                    }
                    writer = new FileWriter(saveFile);
                    write = new CSVWriter(writer);
                    write.writeNext(header);
                    System.out.println("The file name is " + file.getName() + ", " +
                            "the title is " + strings[0] + ", the Creation Date is " + strings[2]);
                    instanceC(strings[1], map);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            map = ascendingOrder(map);
            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                String[] data = new String[]{entry.getKey(), String.valueOf(entry.getValue())};
                assert write != null;
                write.writeNext(data);
            }
            try {
                assert write != null;
                write.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return map;
    }

    /**
     * This method is used in several tests to return a list of files that is contained in a folder
     * This method should not be changed.
     *
     * @param folder String that represent the path of the folder that we want the list of files
     * @return a array of files
     */
    public static File[] getResourceFolderFiles(String folder) {
        return new File(folder).listFiles();
    }

    public int instanceC(String text, HashMap<String, Integer> map) {
        int x = 0;
        String[] listOfWords = text.split(" ");
        listOfWords = unpunc(listOfWords);
        for (String i : listOfWords) {
            String[] data = i.split(" ");
            for (String j: data) {
                if (j.length() > 1) {
                    if (map.containsKey(j)) {
                        int value = map.get(j) + 1;
                        map.replace(j, value);
                    } else {
                        map.put(j, 1);
                    }
                }
            }
        }
        return x;
    }

    public static HashMap<String, Integer> ascendingOrder(HashMap<String, Integer> hm) {
        // Create a list from elements of HashMap
        List<Map.Entry<String, Integer> > list =
                new LinkedList<Map.Entry<String, Integer> >(hm.entrySet());

        // Sort the list
        Collections.sort(list, new Comparator<Map.Entry<String, Integer> >() {
            public int compare(Map.Entry<String, Integer> o1,
                               Map.Entry<String, Integer> o2)
            {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        // put data from sorted list to hashmap
        HashMap<String, Integer> temp = new LinkedHashMap<String, Integer>();
        for (Map.Entry<String, Integer> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }

    public String[] unpunc(String[] strings) {
        String[] strings1 = new String[strings.length];
        for (int i = 0; i < strings.length; i++) {
            if (strings[i] != null) {
                strings[i] = strings[i].replaceAll("\\p{Punct}", "");
                strings[i] = strings[i].replaceAll("[^a-zA-Z0-9]", " ");
                strings[i] = strings[i].toLowerCase();
                strings[i] = strings[i].replaceAll("TRUE", "true");
                strings[i] = strings[i].replaceAll("FALSE", "false");
                strings1[i] = strings[i];
            }
        }
        return strings1;
    }
}
