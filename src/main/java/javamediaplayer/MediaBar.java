package javamediaplayer;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.HBox;
import javafx.scene.media.MediaPlayer;

public class MediaBar extends HBox {
    private Button btnPlay;

    public MediaBar(MediaPlayer mediaPlayer) {
        btnPlay = new Button("||");
        btnPlay.setPrefSize(32, 32);
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

        getChildren().add(btnPlay);
    }
}