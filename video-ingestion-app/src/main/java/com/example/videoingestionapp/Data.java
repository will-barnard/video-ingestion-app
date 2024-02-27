package com.example.videoingestionapp;

import java.io.*;

public class Data {

    private File data;
    private boolean newFile = false;

    public Data(String path) {
        // parse path
        String fileName = path + "data.csv";
        File data = new File(fileName);
        // set
        this.data = data;
        initData(data);
        if (newFile) {
            writeData("index", "name", "title", "composer");
        }
    }
    private void initData(File data) {
        try {if(!data.exists()) {
            data.createNewFile();
            this.newFile = true;
        }
        } catch(IOException e) {System.out.println("Something went wrong creating or reading the data file");}
    }

    public void writeData(String indexStr, String name, String title, String composer) {
        // concat
        String str = concatForm(indexStr, name, title, composer);
        // write
        try(PrintWriter writer = new PrintWriter(new FileOutputStream(data,true))) {
            writer.println(str);
        } catch(FileNotFoundException e) {
            System.out.println("Something went wrong writing to the data file");
        }
    }

    private String concatForm(String indexStr, String name, String title, String composer) {
        return indexStr + "," + name + "," + title + "," + composer;
    }

    public Submission getRow(String index) {
        Submission result = null;
        try (BufferedReader br = new BufferedReader(new FileReader(data))) {
            String line;
            boolean stop = false;
            while ((line = br.readLine()) != null && !stop) {
                if (line.substring(0, 3).equals(index)) {
                    String[] lineSplit = line.split(",");
                    result = new Submission(lineSplit);
                    stop = true;
                }
            }
        } catch(Exception e) {
            System.out.println("Something went wrong reading the data file");
        }
        return result;
    }


}
