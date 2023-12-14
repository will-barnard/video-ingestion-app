module com.example.videoingestionapp {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;

    opens com.example.videoingestionapp to javafx.fxml;
    exports com.example.videoingestionapp;
}