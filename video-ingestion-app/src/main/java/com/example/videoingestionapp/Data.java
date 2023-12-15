package com.example.videoingestionapp;

import java.io.*;

public class Data {

    private File data;

    public Data(String path) {
        // parse path
        String fileName = path + "data.csv";
        File data = new File(fileName);
        // set
        this.data = data;
        initData(data);
    }
    private void initData(File data) {
        try {if(!data.exists()) {data.createNewFile();}
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


}
