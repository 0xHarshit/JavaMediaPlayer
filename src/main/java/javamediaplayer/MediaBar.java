package javamediaplayer;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.media.MediaPlayer;

public class MediaBar extends HBox {
    private Button btnPlay;
    private Slider progressSlider;
    // private ImageView myImageView;

    public MediaBar(MediaPlayer mediaPlayer) {
        // Image pause = new
        // Image(getClass().getResourceAsStream("file:assets/pause.png"));
        // ImageView pausew = new ImageView(pause);
        Slider speedSlider = new Slider(0.25, 2, 0.25);
        speedSlider.setBlockIncrement(0.25);
        speedSlider.setMajorTickUnit(0.25);
        speedSlider.setMinorTickCount(0);
        speedSlider.setShowTickLabels(true);
        speedSlider.setSnapToTicks(true);
        speedSlider.setValue(1);

        ComboBox vol = new ComboBox();
        ComboBox speed = new ComboBox();
        speed.setPrefWidth(50);
        vol.setPrefWidth(50);
        Slider volSlider = new Slider();
        volSlider.setOrientation(Orientation.VERTICAL);
        speedSlider.setOrientation(Orientation.VERTICAL);

        vol.getItems().add(volSlider);
        speed.getItems().add(speedSlider);
        volSlider.setValue(50);
        btnPlay = new Button("||");
        progressSlider = new Slider();
        // int progressMaxWidth = 500;
        progressSlider.setPrefWidth(500);
        // progressSlider.setMax(mediaPlayer.getTotalDuration().toSeconds());
        // btnPlay = new Button("", pausew);
        btnPlay.setPrefSize(32, 32);
        mediaPlayer.currentTimeProperty().addListener((ob, old, n) -> {
            // System.out.println(progressSlider.getMax());
            Double duration = mediaPlayer.getTotalDuration().toSeconds();
            Double CurrTime = mediaPlayer.getCurrentTime().toSeconds();
            progressSlider.setValue((CurrTime / duration) * 100);
            // System.out.println(mediaPlayer.getRate());

        });
        progressSlider.valueProperty().addListener((ob, old, ne) -> {

            if (progressSlider.isPressed())
                mediaPlayer.seek(mediaPlayer.getTotalDuration().multiply(progressSlider.getValue() / 100));
        });
        btnPlay.setOnAction(e -> {
            MediaPlayer.Status status = mediaPlayer.getStatus(); // To get the status of Player
            if (status == MediaPlayer.Status.PLAYING) {
                // If the status is Video playing
                if (mediaPlayer.getCurrentTime().greaterThanOrEqualTo(mediaPlayer.getTotalDuration())) {

                    // If the player is at the end of video
                    mediaPlayer.seek(mediaPlayer.getStartTime()); // Restart the video
                    mediaPlayer.play();
                } else {
                    // Pausing the player
                    mediaPlayer.pause();
                    btnPlay.setText(">");
                }
            } // If the video is stopped, halted or paused
            if (status == MediaPlayer.Status.HALTED || status == MediaPlayer.Status.STOPPED
                    || status == MediaPlayer.Status.PAUSED) {
                mediaPlayer.play(); // Start the video
                btnPlay.setText("||");
            }
        });
        mediaPlayer.volumeProperty().bind(volSlider.valueProperty().divide(100));
        mediaPlayer.rateProperty().bind(speedSlider.valueProperty());
        getChildren().add(btnPlay);
        getChildren().add(progressSlider);
        getChildren().add(vol);
        getChildren().add(speed);
    }
}