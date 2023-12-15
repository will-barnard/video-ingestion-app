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

public class Application extends javafx.application.Application {

    private Log log;
    private Data data;
    private List<File> fileList = new ArrayList<>();
    private String pathDisplay = "";
    private String indexDisplay = "";


    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Ingestion Automator");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_LEFT);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Scene scene = new Scene(grid, 500, 600);
        primaryStage.setScene(scene);

        Text scenetitle = new Text("Super Ingestionator");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));


        Label pathLabel = new Label("Destination Path:");
        TextField destinationPath = new TextField();
        Button update = new Button("Update");

        Label currentPathLabel = new Label("Current Path:");
        Text currentPath = new Text(pathDisplay);

        Label currentIndexLabel = new Label("Current Index:");
        Text currentIndex = new Text(indexDisplay);

        Label studentNameLabel = new Label("Student Name:");
        TextField studentName = new TextField();

        Label pieceTitleLabel = new Label("Title of Piece:");
        TextField pieceTitle = new TextField();

        Label composerLabel = new Label("Composer:");
        TextField composer = new TextField();

        Button execute = new Button("Execute");

        Text dropHere = new Text("Drop files here:");
        Rectangle dropzone = new Rectangle(100,100, Paint.valueOf("Lightgray"));

        ImageView imageView = new ImageView();
        imageView.setFitHeight(90);
        imageView.setFitWidth(90);

        Button clear = new Button("Clear");
        Button printFiles = new Button("Show Filenames");

        Text filenames = new Text();

        // grid layouts below
        {
            grid.add(scenetitle, 0, 0, 2, 1);

            grid.add(pathLabel, 0, 1);
            grid.add(destinationPath, 1, 1);
            grid.add(update, 3, 1);

            grid.add(currentPathLabel, 0, 3);
            grid.add(currentPath, 1, 3);

            grid.add(currentIndexLabel, 0, 4);
            grid.add(currentIndex, 1, 4);

            grid.add(studentNameLabel, 0, 6);
            grid.add(studentName, 1, 6);

            grid.add(pieceTitleLabel, 0, 7);
            grid.add(pieceTitle, 1, 7);

            grid.add(composerLabel, 0, 8);
            grid.add(composer, 1, 8);

            grid.add(execute, 0, 9, 1, 2);

            grid.add(dropHere, 0, 13);
            grid.add(dropzone, 0, 14, 2, 2);
            grid.add(imageView, 0, 14, 2, 2);

            grid.add(printFiles, 0, 16, 1, 1);
            grid.add(clear, 1, 16, 1, 1);

            grid.add(filenames, 0, 17, 1, 2);
        }
        primaryStage.show();

        // button updates the filepath and generates log and data
        update.setOnAction(actionEvent -> {
            if (!destinationPath.getText().substring(destinationPath.getText().length() - 1).equals("/")) {
                this.pathDisplay = destinationPath.getText() + "/";
            } else{
                this.pathDisplay = destinationPath.getText();
            }
            initLog(pathDisplay);
            initData(pathDisplay);
            System.out.println("current log path is " + pathDisplay);
            currentPath.setText(pathDisplay);
            currentIndex.setText(log.getIndexStr());
        });

        // button to execute file transfer and data write
        execute.setOnAction(actionEvent -> {
            // write to data file
            data.writeData(log.getIndexStr(), studentName.getText(), pieceTitle.getText(), composer.getText());
            // step index
            log.stepLog();
            indexDisplay = log.getIndexStr();
            currentIndex.setText(indexDisplay);
            // clear text fields
            studentName.clear();
            pieceTitle.clear();
            composer.clear();
            // file I/O, move files and rename
            moveFiles(fileList);
            fileList.clear();
            imageView.setImage(null);
        });

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

        clear.setOnAction(actionEvent -> {
            fileList.clear();
            filenames.setText("");
            imageView.setImage(null);
        });

        printFiles.setOnAction(actionEvent -> {
            filenames.setText(printFileNames(fileList));
        });
    }

    public static void main(String[] args) {
        launch();
    }

    private void initLog(String path) {
        Log initLog = new Log(path);
        this.log = initLog;
        this.indexDisplay = log.getIndexStr();
        this.pathDisplay = path;
    }

    private void initData(String path) {
        Data initData = new Data(path);
        this.data = initData;
    }

    private void moveFiles(List<File> files) {

        int step = 1;
        for (File file : files) {
            String stepStr = get2DigitSequence(step);
            File destFile = new File(pathDisplay + indexDisplay + "_" + stepStr + "_" + file.getName());
            step++;
            if (file.renameTo(destFile)) {
                System.out.println("File moved successfully");
            } else {
                System.out.println("Failed to move file");
            }
        }

    }

    private String get2DigitSequence(int num) {
        if (num > 9) {
            return Integer.toString(num);
        } else {
            return "0" + num;
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

}