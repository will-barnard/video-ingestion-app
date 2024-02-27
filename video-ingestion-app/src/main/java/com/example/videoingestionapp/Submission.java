package com.example.videoingestionapp;

import java.io.File;

public class Submission {

    private File file = null;
    private String index;
    private String name;
    private String title;
    private String composer;


    public Submission(String index, String name, String title, String composer) {
        this.index = index;
        this.name = name;
        this.title = title;
        this.composer = composer;
    }

    public Submission(String[] array) {
        this.index = array[0];
        this.name = array[1];
        this.title = array[2];
        this.composer = array[3];
    }

    public String toString() {
        return index + " " + name + " " + title + " " + composer;
    }

}
