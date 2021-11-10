package javamediaplayer;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Duration;

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
    private java.io.File file = new java.io.File("./src/main/java/javamediaplayer/assets/video.mp4");
    // private java.io.File icon = new
    // java.io.File("./src/main/java/javamediaplayer/assets/icon.png");
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
        mediaView.setFitWidth(700);
        mediaBar = new MediaBar(mediaPlayer);

        borderPane.setCenter(mediaView);
        borderPane.setBottom(mediaBar);
        borderPane.setPadding(new Insets(10, 20, 10, 20));

        borderPane.widthProperty().addListener((ob, old, ne) -> {
            mediaBar.relocateX(ne.doubleValue());
            mediaView.setFitWidth((ne.doubleValue() / 800) * 600);
        });

        scene = new Scene(borderPane, 800, 600);
        stage.getIcons().add(new Image("file:src/main/java/javamediaplayer/assets/icon.png"));
        stage.setScene(scene);
        stage.show();

            mediaView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {
                if(me.getButton().equals(MouseButton.PRIMARY)) {
                    if (me.getClickCount() == 2) {
                        if (me.getX() >= mediaView.getFitWidth() / 2) {
                            mediaPlayer.seek(new Duration(mediaPlayer.getCurrentTime().toMillis() + 10000));
                        } else {
                            mediaPlayer.seek(new Duration(mediaPlayer.getCurrentTime().toMillis() - 10000));
                        }
                    }
                }
            }
        });
    }

    public static void main(String[] args) {
        launch();
    }
}