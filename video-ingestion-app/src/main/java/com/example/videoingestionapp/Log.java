package com.example.videoingestionapp;

import java.io.*;
import java.security.Principal;
import java.util.Scanner;

public class Log {

    private int index = 0;
    private String indexStr = "000";
    private File log;

    public Log(String path) {

        String fileName = path + "log.txt";
        File log = new File(fileName);

        this.index = readLog(log);
        this.indexStr = createIndexString(index);
        this.log = log;

    }

    public String getIndexStr() {
        return indexStr;
    }
    public int getIndex() {
        return index;
    }


    public void setIndex(int index) {
        this.index = index;
        this.indexStr = createIndexString(index);
    }


    public File getLog() {
        return log;
    }
    public void setLog(File log) {
        this.log = log;
    }

    public void stepLog() {
        setIndex(index + 1);
        try(PrintWriter writer = new PrintWriter(new FileOutputStream(log,false))) {

            writer.println(getIndexStr());
        } catch(FileNotFoundException e) {
            System.out.println("Something went wrong writing to the log file");
        }
    }

    private int readLog(File file) {

        int localIndex = 0;

        try {

            if(!file.exists()) {

                file.createNewFile();
                this.log = file;
                try(PrintWriter writer = new PrintWriter(new FileOutputStream(log,false))) {
                    writer.println("000");
                    return localIndex;
                } catch(FileNotFoundException e) {
                    System.out.println("Something went wrong writing init to the log file");
                }
            }

            Scanner readIndex = new Scanner(file);
            localIndex = Integer.parseInt(readIndex.nextLine());

        } catch(IOException e) {
            System.out.println("Something went wrong creating or reading the log file");
        }

        return localIndex;
    }

    private String createIndexString(int num) {
        if (num > 99) {
            return Integer.toString(num);
        }
        if (num > 9) {
            return "0" + num;
        }
        else {
            return "00" + num;
        }
    }

}
