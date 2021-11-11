package javamediaplayer;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;

class MouseStatus {

    double x;
    double y;

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}

public class MainWindow extends Application {
    private Stage stage;
    private Scene scene;
    private BorderPane borderPane;
    private MediaBar mediaBar;
    private MediaBar mediaBar2;
    private Button btnFile;
    private Media media;
    private MediaPlayer mediaPlayer;
    private MediaView mediaView;
    private Popup popup;

    private java.io.File file = new java.io.File("./src/main/java/javamediaplayer/assets/video.mp4");
    private String MEDIA_URL = file.toURI().toString();

    MouseStatus mouseStatus = new MouseStatus();
    AnimationTimer loop = new AnimationTimer() {

        long deltaNs = 2000_000_000;

        double currX;
        double currY;
        long currNs;

        double prevX;
        double prevY;
        long prevNs;

        @Override
        public void handle(long now) {

            currX = mouseStatus.x;
            currY = mouseStatus.y;
            currNs = now;
            if (currNs - prevNs > deltaNs) {

                if (prevX == currX && prevY == currY) {
                    popup.hide();
                }

                prevX = currX;
                prevY = currY;
                prevNs = currNs;
            }

        }
    };

    @Override
    public void start(Stage stage) throws IOException {
        btnFile = new Button("File");
        btnFile.setOnAction(e -> {

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Media...");
            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("MP4 Video", "*.mp4"),
                    new FileChooser.ExtensionFilter("MP3 Music", "*.mp3"),
                    new FileChooser.ExtensionFilter("All Files", "*.*"));

            file = fileChooser.showOpenDialog(new Stage());
            if (file != null) {
                mediaPlayer.stop();
                media = new Media(file.toURI().toString());
                mediaPlayer = new MediaPlayer(media);
                mediaView.setMediaPlayer(mediaPlayer);
                mediaPlayer.setAutoPlay(true);
                mediaBar = new MediaBar(mediaPlayer);
                mediaBar2 = new MediaBar(mediaPlayer);
                popup.getContent().add(mediaBar2);
                borderPane.setBottom(mediaBar);
            }

        });
        stage.setTitle("Java Media Player");
        borderPane = new BorderPane();
        media = new Media(MEDIA_URL);
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.setOnReady(stage::sizeToScene);
        mediaView = new MediaView(mediaPlayer);
        mediaView.setFitWidth(700);
        mediaBar = new MediaBar(mediaPlayer);

        borderPane.setTop(btnFile);
        borderPane.setCenter(mediaView);
        borderPane.setBottom(mediaBar);
        borderPane.setPadding(new Insets(10, 20, 10, 20));

        borderPane.widthProperty().addListener((ob, old, ne) -> {
            if (!stage.isFullScreen()) {
                mediaBar.relocateX(ne.doubleValue());
                mediaView.setFitWidth((ne.doubleValue() / 800) * 600);
            }
        });

        scene = new Scene(borderPane, 800, 600);
        stage.getIcons().add(new Image("file:src/main/java/javamediaplayer/assets/icon.png"));
        stage.setScene(scene);
        stage.show();

        popup = new Popup();
        mediaBar2 = new MediaBar(mediaPlayer);
        popup.getContent().add(mediaBar2);
        stage.fullScreenProperty().addListener(e -> {
            if (!stage.isFullScreen()) {
                borderPane.setBottom(mediaBar);
                borderPane.setTop(btnFile);
                borderPane.setPadding(new Insets(10, 20, 10, 20));
                BackgroundFill bg_fill = new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY);
                Background bg = new Background(bg_fill);
                borderPane.setBackground(bg);
                mediaBar.relocateX(borderPane.getWidth());
                mediaView.setFitWidth((borderPane.getWidth() / 800) * 600);
            } else {
                borderPane.setTop(null);
                borderPane.setPadding(new Insets(0, 0, 0, 0));
                borderPane.setBottom(null);
                BackgroundFill bg_fill = new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY);
                Background bg = new Background(bg_fill);
                borderPane.setBackground(bg);
            }
        });
        mediaView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {
                if (me.getButton().equals(MouseButton.PRIMARY)) {
                    if (me.getClickCount() >= 2) {
                        if (me.getX() >= (3 * mediaView.getFitWidth()) / 4) {
                            mediaPlayer.seek(new Duration(mediaPlayer.getCurrentTime().toMillis() + 10000));
                        } else if (me.getX() <= (mediaView.getFitWidth()) / 4) {
                            mediaPlayer.seek(new Duration(mediaPlayer.getCurrentTime().toMillis() - 10000));
                        } else {
                            if (stage.isFullScreen()) {
                                stage.setFullScreen(false);
                            } else {
                                stage.setFullScreen(true);
                                mediaView.setFitWidth(stage.getWidth());
                            }
                        }
                        mediaPlayer.play();
                    } else if (me.getClickCount() == 1) {
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
        scene.addEventFilter(MouseEvent.ANY, e -> {
            if (stage.isFullScreen()) {
                mediaBar2.relocateX(stage.getWidth());
                if (!popup.isShowing()) {
                    popup.show(stage, 0, (stage.getHeight()));
                }
            } else {
                popup.hide();
            }
            mouseStatus.setX(e.getX());
            mouseStatus.setY(e.getY());
            loop.start();
        });
        popup.addEventFilter(MouseEvent.ANY, e -> {
            if (stage.isFullScreen()) {
                loop.stop();
            }
        });
    }

    public static void main(String[] args) {
        launch();
    }
}