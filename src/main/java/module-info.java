module javamediaplayer {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;

    opens javamediaplayer to javafx.fxml;
    exports javamediaplayer;
}
