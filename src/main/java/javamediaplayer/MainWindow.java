package javamediaplayer;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
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

    private java.io.File file = new java.io.File("./src/main/java/javamediaplayer/assets/video.mp4");
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
                if (me.getButton().equals(MouseButton.PRIMARY)) {
                    if (me.getClickCount() == 2) {
                        if (me.getX() >= mediaView.getFitWidth() / 2) {
                            mediaPlayer.seek(new Duration(mediaPlayer.getCurrentTime().toMillis() + 10000));
                        } else {
                            mediaPlayer.seek(new Duration(mediaPlayer.getCurrentTime().toMillis() - 10000));
                        }
                    } else {
                        MediaPlayer.Status status = mediaPlayer.getStatus();
                        if (status == MediaPlayer.Status.PLAYING) {
                            if (mediaPlayer.getCurrentTime().greaterThanOrEqualTo(mediaPlayer.getTotalDuration())) {
                                mediaPlayer.seek(mediaPlayer.getStartTime());
                                mediaPlayer.play();
                            } else {
                                mediaPlayer.pause();
                            }
                        }
                        if (status == MediaPlayer.Status.HALTED || status == MediaPlayer.Status.STOPPED
                                || status == MediaPlayer.Status.PAUSED || status == MediaPlayer.Status.READY) {
                            mediaPlayer.play();
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