import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.animation.AnimationTimer;


public class Main extends Application {

    Pane border = new Pane();
    Scene scene;
    Ball ball;
    Paddle left;
    Paddle right;


    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage stage) {
        stage.setTitle("Pong !");
        stage.setResizable(false);
        Button btn = new Button();
        btn.setText("Play Pong");
        btn.setLayoutX(310);
        btn.setLayoutY(210);
        btn.setOnAction((e) -> {
            border.getChildren().remove(btn);
            run();

        });

        border.getChildren().add(btn);
        scene = new Scene(border, 700, 500);

        stage.setScene(scene);
        stage.show();
        stage.setWidth(700);
        stage.setHeight(500);

    }

    private void run() {
        ball = new Ball(10, 200, 200);
        right = new Paddle(665, 60, 15, 60);
        left = new Paddle(15, 60, 15, 60);

        border.getChildren().addAll(ball, left, right);

        AnimationTimer ballTimer = new BallTimer();
        ballTimer.start();

        controls();

    }

    private void controls() {

        scene.setOnKeyPressed(e -> {
            switch (e.getCode().toString()) {
                case "DOWN":
                    right.getPaddleTimer().start();
                    right.direction = true;
                    break;
                case "W":
                    left.getPaddleTimer().start();
                    left.direction = false;
                    break;
                case "UP" :
                    right.getPaddleTimer().start();
                    right.direction = false;
                    break;
                case "S" :
                    left.getPaddleTimer().start();
                    left.direction = true;
            }
        });

        scene.setOnKeyReleased(e -> {
            if (e.getCode().toString().equals("DOWN") ||
                    e.getCode().toString().equals("UP")) {
                right.getPaddleTimer().stop();
            } else if (e.getCode().toString().equals("W") ||
                    e.getCode().toString().equals("S")) {
                left.getPaddleTimer().stop();
            }
        });
    }

    /**
     * A class to animate the balls movement across the screen
     */
    private class BallTimer extends AnimationTimer {
        private boolean flip = false;   // 0 = moving right, 1 = moving left
        @Override
        public void handle(long now) {
            doHandle();
        }

        private void doHandle() {

            if (flip) {
                ball.setCenterX(ball.getX() - 2);
                ball.setX(ball.getX() - 2);
            } else {
                ball.setCenterX(ball.getX() + 2);
                ball.setX(ball.getX() + 2);
            }

            if (ball.getX() > 700 || ball.getX() < 0) {
                flip = !flip;
            }
        }
    }

}




