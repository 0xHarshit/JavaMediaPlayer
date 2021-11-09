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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.media.MediaPlayer;

public class MediaBar extends AnchorPane {
    private Button btnPlay;
    private Slider progressSlider;
    private ComboBox vol;
    private ComboBox speed;
    private Slider volSlider;
    private Slider speedSlider;

    public MediaBar(MediaPlayer mediaPlayer) {

        speedSlider = new Slider(0.25, 2, 0.25);
        speedSlider.setBlockIncrement(0.25);
        speedSlider.setMajorTickUnit(0.25);
        speedSlider.setMinorTickCount(0);
        speedSlider.setShowTickLabels(true);
        speedSlider.setSnapToTicks(true);
        speedSlider.setValue(1);

        vol = new ComboBox();
        speed = new ComboBox();
        volSlider = new Slider();
        volSlider.setOrientation(Orientation.VERTICAL);
        speedSlider.setOrientation(Orientation.VERTICAL);

        vol.getItems().add(volSlider);
        speed.getItems().add(speedSlider);
        volSlider.setValue(50);
        btnPlay = new Button("PLAY");
        btnPlay.setLayoutY(30);
        btnPlay.setLayoutX(32);
        vol.setLayoutX(603);
        vol.setLayoutY(30);
        vol.setPromptText("Volume");
        speed.setLayoutX(701);
        speed.setLayoutY(30);
        speed.setPromptText("Speed");

        progressSlider = new Slider();
        progressSlider.setPrefWidth(500);
        progressSlider.setPrefSize(692, 14);
        progressSlider.setLayoutX(54);
        btnPlay.setPrefSize(60, 26);
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
            MediaPlayer.Status status = mediaPlayer.getStatus();
            System.out.println(status); // To get the status of Player
            if (status == MediaPlayer.Status.PLAYING) {
                // If the status is Video playing
                if (mediaPlayer.getCurrentTime().greaterThanOrEqualTo(mediaPlayer.getTotalDuration())) {

                    // If the player is at the end of video
                    mediaPlayer.seek(mediaPlayer.getStartTime()); // Restart the video
                    mediaPlayer.play();
                } else {
                    // Pausing the player
                    mediaPlayer.pause();
                    btnPlay.setText("PLAY");
                }
            } // If the video is stopped, halted or paused
            if (status == MediaPlayer.Status.HALTED || status == MediaPlayer.Status.STOPPED
                    || status == MediaPlayer.Status.PAUSED || status == MediaPlayer.Status.READY) {
                mediaPlayer.play(); // Start the video
                btnPlay.setText("PAUSE");
            }
        });
        mediaPlayer.volumeProperty().bind(volSlider.valueProperty().divide(100));
        mediaPlayer.rateProperty().bind(speedSlider.valueProperty());
        getChildren().add(btnPlay);
        getChildren().add(progressSlider);
        getChildren().add(vol);
        getChildren().add(speed);
    }

    public void relocateX(double w) {
        speed.setLayoutX(w - 150);
        vol.setLayoutX(w - 250);
        btnPlay.setLayoutX((w / 800) * 32);
        progressSlider.setPrefWidth((w / 800) * 692);

    }
}