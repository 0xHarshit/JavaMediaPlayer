package javamediaplayer;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class test extends Application {

    Scene scene;
    MouseStatus mouseStatus = new MouseStatus();

    Label infoLabel;

    @Override
    public void start(Stage primaryStage) {

        Group root = new Group();

        infoLabel = new Label();
        root.getChildren().add(infoLabel);

        scene = new Scene(root, 640, 480);

        primaryStage.setScene(scene);
        primaryStage.show();

        addInputListeners();

    }

    private void addInputListeners() {

        scene.addEventFilter(MouseEvent.ANY, e -> {

            infoLabel.setText("Moving");

            mouseStatus.setX(e.getX());
            mouseStatus.setY(e.getY());
            mouseStatus.setPrimaryButtonDown(e.isPrimaryButtonDown());
            mouseStatus.setSecondaryButtonDown(e.isSecondaryButtonDown());

        });

        AnimationTimer loop = new AnimationTimer() {

            long deltaNs = 1000_000_000;

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
                        infoLabel.setText("Stopped");
                    }

                    prevX = currX;
                    prevY = currY;
                    prevNs = currNs;
                }

            }
        };
        loop.start();

    }

    public class MouseStatus {

        double x;
        double y;
        boolean primaryButtonDown;
        boolean secondaryButtonDown;

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

        public boolean isPrimaryButtonDown() {
            return primaryButtonDown;
        }

        public void setPrimaryButtonDown(boolean primaryButtonDown) {
            this.primaryButtonDown = primaryButtonDown;
        }

        public boolean isSecondaryButtonDown() {
            return secondaryButtonDown;
        }

        public void setSecondaryButtonDown(boolean secondaryButtonDown) {
            this.secondaryButtonDown = secondaryButtonDown;
        }

    }

    public static void main(String[] args) {
        launch(args);
    }

}