module javamediaplayer {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive javafx.media;

    opens javamediaplayer to javafx.fxml;

    exports javamediaplayer;
}
