package javamediaplayer;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
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
    private Button btnFile, btnInfo;
    private Media media;
    private MediaPlayer mediaPlayer;
    private MediaView mediaView;
    private Popup popup;
    private BorderPane topBar;

    private java.io.File file;
    private java.io.File currFile;

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
                    scene.setCursor(Cursor.NONE);
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
                currFile = file;
                if (mediaPlayer != null)
                    mediaPlayer.stop();
                media = new Media(file.toURI().toString());
                mediaPlayer = new MediaPlayer(media);
                // mediaView.setMediaPlayer(mediaPlayer);
                mediaView = new MediaView(mediaPlayer);
                mediaView.setFitWidth((borderPane.getWidth() / 800) * 600);
                mediaPlayer.setAutoPlay(true);
                mediaBar = new MediaBar(mediaPlayer);
                mediaBar2 = new MediaBar(mediaPlayer);
                mediaBar.relocateX(borderPane.getWidth());
                mediaBar2.relocateX(borderPane.getWidth());
                if (popup != null) {

                    popup.getContent().clear();
                    popup.getContent().add(mediaBar2);
                }
                borderPane.setBottom(mediaBar);
                borderPane.setCenter(mediaView);
                topBar = new BorderPane();
                topBar.setLeft(btnFile);
                // topBar.setRight(btnInfo);
                borderPane.setTop(topBar);
                mediaStart(stage);
            }

        });

        stage.setTitle("Java Media Player");
        borderPane = new BorderPane();
        borderPane.setCenter(btnFile);
        borderPane.setPadding(new Insets(10, 20, 10, 20));
        scene = new Scene(borderPane, 800, 600);
        stage.getIcons().add(new Image("file:src/main/java/javamediaplayer/assets/icon.png"));
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }

    public void mediaStart(Stage stage) {
        borderPane.widthProperty().addListener((ob, old, ne) -> {
            if (!stage.isFullScreen()) {
                mediaBar.relocateX(ne.doubleValue());
                mediaView.setFitWidth((ne.doubleValue() / 800) * 600);
            }
        });

        btnInfo = new Button("Info");
        btnInfo.setOnAction(e -> {

            int i = currFile.getAbsolutePath().lastIndexOf('.');
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("File : " + currFile.getAbsolutePath());
            alert.setHeaderText("File Type : " + currFile.getAbsolutePath().substring(i + 1));
            alert.setContentText("Duration : "
                    + (double) Math.round(mediaPlayer.getTotalDuration().toSeconds() * 100) / 100 + " seconds");
            alert.showAndWait();
        });
        topBar.setRight(btnInfo);
        popup = new Popup();
        mediaBar2 = new MediaBar(mediaPlayer);
        popup.getContent().add(mediaBar2);
        stage.fullScreenProperty().addListener(e -> {
            if (!stage.isFullScreen()) {
                borderPane.setBottom(mediaBar);
                borderPane.setTop(topBar);
                borderPane.setPadding(new Insets(10, 20, 10, 20));
                Color c = Color.rgb(0, 0, 0);
                mediaBar.colorchange(c);
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
                Color c = Color.rgb(255, 255, 255);
                mediaBar2.colorchange(c);
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
                        if (mediaPlayer != null) {
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
            }
        });
        scene.addEventFilter(MouseEvent.ANY, e -> {
            if (stage.isFullScreen()) {
                mediaBar2.relocateX(stage.getWidth());
                if (!popup.isShowing()) {
                    popup.show(stage, 20, (stage.getHeight()));
                    scene.setCursor(Cursor.DEFAULT);
                }
                mouseStatus.setY(e.getY());
                mouseStatus.setX(e.getX());
                loop.start();
            } else {
                popup.hide();
                loop.stop();
            }
        });
        popup.addEventFilter(MouseEvent.ANY, e -> {
            if (stage.isFullScreen()) {
                loop.stop();
            }
        });
    }
}