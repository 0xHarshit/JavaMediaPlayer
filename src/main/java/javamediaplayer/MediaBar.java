package javamediaplayer;

import javafx.beans.binding.Bindings;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

class BtnForward extends Button {
    public BtnForward(MediaPlayer mediaPlayer) {
        setText(">");
        setPrefSize(26, 26);
        setLayoutX(134);
        setLayoutY(30);
        setOnAction(e -> {
            mediaPlayer.seek(mediaPlayer.getCurrentTime().add(Duration.millis(10000)));
        });
    }
}

class BtnBackward extends Button {
    public BtnBackward(MediaPlayer mediaPlayer) {
        setText("<");
        setPrefSize(26, 26);
        setLayoutX(20);
        setLayoutY(30);
        setOnAction(e -> {
            mediaPlayer.seek(mediaPlayer.getCurrentTime().subtract(Duration.millis(10000)));
        });
    }
}

class BtnPlay extends Button {
    public BtnPlay(MediaPlayer mediaPlayer) {

        textProperty().bind(Bindings.createStringBinding(() -> {
            MediaPlayer.Status status = mediaPlayer.getStatus();
            if (status == MediaPlayer.Status.PLAYING) {
                return "PAUSE";
            } else {
                return "PLAY";
            }
        }, mediaPlayer.statusProperty()));

        setLayoutX(60);
        setLayoutY(30);
        setPrefSize(60, 26);
        setOnAction(e -> {
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
        });
    }
}

class ProgressSlider extends Slider {
    public ProgressSlider(MediaPlayer mediaPlayer) {
        setPrefWidth(500);
        setPrefSize(692, 14);
        setLayoutX(80);
        valueProperty().addListener((ob, old, ne) -> {
            if (isPressed())
                mediaPlayer.seek(mediaPlayer.getTotalDuration().multiply(getValue() / 100));
        });
    }
}

class VolBox extends ComboBox {
    Slider volSlider;

    public VolBox() {
        volSlider = new Slider(0, 100, 100);
        // volSlider.setBlockIncrement(1);
        // volSlider.setMajorTickUnit(1);
        // volSlider.setMinorTickCount(0);
        // volSlider.setSnapToTicks(true);
        volSlider.setShowTickMarks(true);
        volSlider.setShowTickLabels(true);

        volSlider.setOrientation(Orientation.VERTICAL);

        setLayoutX(603);
        setLayoutY(30);
        setPromptText("Volume");
        getItems().add(volSlider);
    }
}

class SpeedBox extends ComboBox {
    Slider speedSlider;

    public SpeedBox() {
        speedSlider = new Slider(0.25, 2, 1);
        speedSlider.setBlockIncrement(0.25);
        speedSlider.setMajorTickUnit(0.25);
        speedSlider.setMinorTickCount(0);
        speedSlider.setShowTickLabels(true);
        speedSlider.setShowTickMarks(true);
        speedSlider.setSnapToTicks(true);

        speedSlider.setOrientation(Orientation.VERTICAL);

        setLayoutX(701);
        setLayoutY(30);
        setPromptText("Speed");
        getItems().add(speedSlider);
    }
}

public class MediaBar extends AnchorPane {
    private BtnPlay btnPlay;
    private BtnForward btnForward;
    private BtnBackward btnBackward;
    private VolBox volBox;
    private SpeedBox speedBox;
    private ProgressSlider progressSlider;
    private Label timeLabel = new Label();

    public MediaBar(MediaPlayer mediaPlayer) {
        btnPlay = new BtnPlay(mediaPlayer);
        btnForward = new BtnForward(mediaPlayer);
        btnBackward = new BtnBackward(mediaPlayer);
        progressSlider = new ProgressSlider(mediaPlayer);
        volBox = new VolBox();
        speedBox = new SpeedBox();

        mediaPlayer.currentTimeProperty().addListener((ob, old, n) -> {
            Double duration = mediaPlayer.getTotalDuration().toSeconds();
            Double CurrTime = mediaPlayer.getCurrentTime().toSeconds();
            progressSlider.setValue((CurrTime / duration) * 100);
        });

        timeLabel.textProperty().bind(Bindings.createStringBinding(() -> {
            Duration totalTime = mediaPlayer.getTotalDuration();
            Duration currentTime = mediaPlayer.getCurrentTime();
            return String.format("%d:%02d:%02d/%d:%02d:%02d", (int) currentTime.toHours(),
                    (int) currentTime.toMinutes() % 60, (int) currentTime.toSeconds() % 60, (int) totalTime.toHours(),
                    (int) totalTime.toMinutes() % 60, (int) totalTime.toSeconds() % 60);
        }, mediaPlayer.currentTimeProperty()));
        volBox.volSlider.valueProperty().addListener(e -> {
            mediaPlayer.setVolume(volBox.volSlider.valueProperty().doubleValue() / 100);
        });
        speedBox.speedSlider.valueProperty().addListener(e -> {
            mediaPlayer.setRate(speedBox.speedSlider.valueProperty().doubleValue());
        });
        getChildren().addAll(btnBackward, btnPlay, btnForward, progressSlider, volBox, speedBox, timeLabel);
    }

    public void relocateX(double w) {
        speedBox.setLayoutX(w - 150);
        volBox.setLayoutX(w - 250);
        progressSlider.setPrefWidth((w / 800) * 692);
    }
}