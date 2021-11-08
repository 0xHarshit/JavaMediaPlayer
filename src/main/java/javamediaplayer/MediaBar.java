package javamediaplayer;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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
        ComboBox vol = new ComboBox();
        Slider volSlider = new Slider();
        volSlider.setOrientation(Orientation.VERTICAL);
        vol.getItems().add(volSlider);
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

        });
        volSlider.valueProperty().addListener((ob, old, ne) -> {

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
        getChildren().add(btnPlay);
        getChildren().add(progressSlider);
        getChildren().add(vol);
    }
}