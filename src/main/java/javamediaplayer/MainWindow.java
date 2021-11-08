package javamediaplayer;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

import java.io.IOException;

public class MainWindow extends Application {
    private Stage stage;
    private Scene scene;
    private BorderPane borderPane;
    private MediaBar mediaBar;

    private Media media;
    private MediaPlayer mediaPlayer;
    private MediaView mediaView;

    // private String MEDIA_URL =
    // "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4";
    // private String MEDIA_URL = "./song.mp3";
    // String s = getClass().getResourceAsStream("song.mp3").toString();
    private java.io.File file = new java.io.File("./src/main/java/javamediaplayer/assets/song.mp3");
    private String MEDIA_URL = file.toURI().toString();

    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("Java Media Player");

        borderPane = new BorderPane();

        media = new Media(MEDIA_URL);
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.setOnReady(stage::sizeToScene);
        mediaView = new MediaView(mediaPlayer);

        borderPane.setCenter(mediaView);

        mediaBar = new MediaBar(mediaPlayer);
        borderPane.setBottom(mediaBar);

        scene = new Scene(borderPane);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}