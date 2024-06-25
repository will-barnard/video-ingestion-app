package com.example.videoingestionapp;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Application extends javafx.application.Application {

    private Log log;
    private Data data;
    private List<File> fileList = new ArrayList<>();
    private String pathDisplay = "";
    private String indexDisplay = "";
    private String renamePath = "";
    // launch
    public static void main(String[] args) {
        launch();
    }
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Super Ingestionator");

        GridPane grid = new GridPane();
        // grid settings
        {
            grid.setAlignment(Pos.TOP_LEFT);
            grid.setHgap(10);
            grid.setVgap(10);
            grid.setPadding(new Insets(25, 25, 25, 25));
        }

        // scene
        Scene scene = new Scene(grid, 600, 800);
        primaryStage.setScene(scene);

        // form elements
        // title
        Text scenetitle = new Text("Directory");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));

        // pathing
        Button loadStoredPath = new Button("Load stored path");
        Label pathLabel = new Label("Destination Path:");
        TextField destinationPath = new TextField();
        Button update = new Button("Update");
        // pathing display
        Label currentPathLabel = new Label("Current Path:");
        Text currentPath = new Text(pathDisplay);
        Label currentIndexLabel = new Label("Current Index:");
        Text currentIndex = new Text(indexDisplay);

        // CSV info form
        Text dataTitle = new Text("Submission form");
        dataTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        Label studentNameLabel = new Label("Student Name:");
        TextField studentName = new TextField();
        Label pieceTitleLabel = new Label("Title of Piece:");
        TextField pieceTitle = new TextField();
        Label composerLabel = new Label("Composer:");
        TextField composer = new TextField();
        Button execute = new Button("Log submission");

        // drag n drop
        Text dropHere = new Text("Drop files here:");
        Rectangle dropzone = new Rectangle(100,100, Paint.valueOf("Lightgray"));
        ImageView imageView = new ImageView();
        imageView.setFitHeight(90);
        imageView.setFitWidth(90);
        Button clear = new Button("Clear");
        Button printFiles = new Button("Show Filenames");
        Text filenames = new Text();

        // UTILITY
        Text utilityTitle = new Text("Utility");
        utilityTitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        // name toggle
        TextField renameDestinationPath = new TextField();
        Button printRename = new Button("Print file names");
        Button rename = new Button("Rename submissions");
        // recital title
        TextField monthTitle = new TextField();
        Button createTitleButton = new Button("Create recital title");

        // grid layouts
        {
            grid.add(scenetitle, 0, 0, 2, 1);

            grid.add(pathLabel, 0, 1);
            grid.add(destinationPath, 1, 1);
            grid.add(update, 2, 1);

            grid.add(currentPathLabel, 0, 2);
            grid.add(currentPath, 1, 2);

            grid.add(currentIndexLabel, 0, 3);
            grid.add(currentIndex, 1, 3);

            grid.add(loadStoredPath, 0, 4);

            grid.add(dataTitle, 0, 6);

            grid.add(studentNameLabel, 0, 7);
            grid.add(studentName, 1, 7);

            grid.add(pieceTitleLabel, 0, 8);
            grid.add(pieceTitle, 1, 8);

            grid.add(composerLabel, 0, 9);
            grid.add(composer, 1, 9);

            grid.add(execute, 0, 10, 1, 2);

            grid.add(dropHere, 0, 14);
            grid.add(dropzone, 0, 15, 2, 2);
            grid.add(imageView, 0, 15, 2, 2);

            grid.add(printFiles, 0, 17, 1, 1);
            grid.add(clear, 1, 17, 1, 1);

            grid.add(filenames, 0, 18, 1, 2);

            grid.add(utilityTitle, 0, 22);

            grid.add(renameDestinationPath, 0, 23, 1, 1);
            grid.add(printRename, 2, 23);
            grid.add(rename, 1, 23);


            grid.add(monthTitle, 0, 26);
            grid.add(createTitleButton, 1, 26);
        }
        primaryStage.show();


        // EVENTS BELOW

        // update the filepath, generate log and data if null
        update.setOnAction(actionEvent -> {



            // catch path name laziness
            loadDirectory(destinationPath.getText());
            currentPath.setText(pathDisplay);
            currentIndex.setText(log.getIndexStr());

        });

        // load stored directory
        loadStoredPath.setOnAction(actionEvent -> {
            loadDirectory(getStoredDirectory());
            currentPath.setText(pathDisplay);
            currentIndex.setText(log.getIndexStr());
        });

        // create the recital "monthTitle" file
        createTitleButton.setOnAction(actionEvent -> {
            createTitle(monthTitle.getText());
        });



        // execute program function, transfer files, and write to log/data
        execute.setOnAction(actionEvent -> {

            // write to data file
            data.writeData(log.getIndexStr(), studentName.getText(), pieceTitle.getText(), composer.getText());
            // clear text fields
            studentName.clear();
            pieceTitle.clear();
            composer.clear();
            // file I/O, move files and rename
            moveFiles(fileList);
            fileList.clear();
            imageView.setImage(null);
            // step index
            log.stepLog();
            indexDisplay = log.getIndexStr();
            currentIndex.setText(indexDisplay);

        });

        // dropzone file drop events
        dropzone.setOnDragOver(actionEvent -> {
            if (actionEvent.getDragboard().hasFiles()) {
                actionEvent.acceptTransferModes(TransferMode.ANY);
            }
        });
        dropzone.setOnDragDropped(actionEvent -> {
            fileList.clear();
            List<File> files = actionEvent.getDragboard().getFiles();
            try {
                Image img = new Image(new FileInputStream(files.get(0)));
                imageView.setImage(img);
            } catch(FileNotFoundException e) {
                System.out.println("File not found");
            }
            this.fileList = files;
        });

        // "Clear" and "Show File Names" buttons
        clear.setOnAction(actionEvent -> {
            fileList.clear();
            filenames.setText("");
            imageView.setImage(null);
        });

        printFiles.setOnAction(actionEvent -> {
            filenames.setText(printFileNames(fileList));
        });

        // print files for renaming
        printRename.setOnAction(actionEvent -> {
            System.out.println("files for rename: \n" + renameDestinationPath.getText());
            for (File file : new File(renameDestinationPath.getText()).listFiles()) {
                System.out.println(file.getName());
            }
        });


        // rename files
        rename.setOnAction(actionEvent -> {
            this.renamePath = renameDestinationPath.getText();
            if (!renamePath.substring(renamePath.length() - 1).equals("/")) {
                this.renamePath = renamePath + "/";
            }

            File renameDir = new File(renamePath);

            System.out.println("renaming");

            for (File file : renameDir.listFiles()) {
                String[] array = file.getName().split("\\.");
                if(!array[0].equals("")) {
                    String ext = file.getName().split("\\.")[1];
                    File newFile = new File(renameDir + "/" + data.getRow(array[0]).toString() + "." + ext);
                    if (newFile.exists()) {
                        System.out.println("File already exists");
                    } else {
                        file.renameTo(newFile);
                    }
                }
            }
        });
    }



    // App sub-methods
    private void initLog(String path) {
        this.pathDisplay = path;
        path += "data/";
        Log initLog = new Log(path);
        this.log = initLog;
        this.indexDisplay = log.getIndexStr();
    }
    private void initData(String path) {
        path += "data/";
        Data initData = new Data(path);
        this.data = initData;
    }
    private void createTitle(String title) {

        try {
            if (pathDisplay == null) {
                throw new Exception("no path");
            }
        } catch(Exception e) {
            System.out.println(e);
        }
        File file = new File(pathDisplay + "data/title.csv");
        try {
            if(!file.exists()) {
                file.createNewFile();
                try(PrintWriter writer = new PrintWriter(new FileOutputStream(file,true))) {
                    writer.println("Recital Title");
                    writer.println(title);
                    System.out.println("succesfully created title file");
                } catch(FileNotFoundException e) {
                    System.out.println("Something went wrong writing to the recital title file");
                }
            }

        } catch(IOException e) {
            System.out.println("Something went wrong creating or reading the recital title file");
        }

    }

    private void moveFiles(List<File> files) {
        int step = 1;

        if (fileList.size() > 1) {
            for (File file : files) {
                String[] fileSplit = file.getName().split("\\.");
                String stepStr = get2DigitSequence(step);
                File destFile = new File(pathDisplay + "/video/submissions/" + indexDisplay + "_" + stepStr + "." + fileSplit[fileSplit.length - 1]);
                step++;
                if (file.renameTo(destFile)) {
                    System.out.println("File moved successfully");
                } else {
                    System.out.println("Failed to move file");
                }
            }
        } else if (fileList.size() == 1) {
            String[] fileSplit = fileList.get(0).getName().split("\\.");
            File destFile = new File(pathDisplay + "/video/submissions/" + indexDisplay + "." + fileSplit[fileSplit.length - 1]);
            if (fileList.get(0).renameTo(destFile)) {
                System.out.println("File moved successfully");
            } else {
                System.out.println("Failed to move file");
            }
        }

    }
    private String printFileNames(List<File> files) {
        String result = "";
        int step = 0;
        for (File file : files) {
            result += file.getName();
            if (step != files.size() - 1) {
                result += "\n";
            }
        }
        return result;
    }
    private String get2DigitSequence(int num) {
        if (num > 9) {
            return Integer.toString(num);
        } else {
            return "0" + num;
        }
    }

    private String getStoredDirectory() {
        String dir = "";
        File file = new File("src/main/resources/directory-path.txt");

        try {
            Scanner scanner = new Scanner(file);
            dir = scanner.nextLine();
        } catch(FileNotFoundException e) {
            System.out.println("dir file not found");
        }


        return dir;
    }

    private void loadDirectory(String path) {
        // catch path name laziness
        if (!path.substring(path.length() - 1).equals("/")) {
            this.pathDisplay = path + "/";
        } else{
            this.pathDisplay = path;
        }

        // load state
        initLog(pathDisplay);
        initData(pathDisplay);
        System.out.println("current log path is " + pathDisplay);
    }

}