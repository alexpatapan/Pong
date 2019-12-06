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

    final int stageWidth = 700;
    final int stageHeight = 500;
    final int paddleWidth = 15;
    final int paddleHeight = 60;

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Display the start screen with the option to play
     * @param stage The main stage which the game runs on
     */
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
        scene = new Scene(border, stageWidth, stageHeight);

        stage.setScene(scene);
        stage.show();
        stage.setWidth(700);
        stage.setHeight(500);

    }

    /**
     * Run an instance of the game
     */
    private void run() {
        ball = new Ball(10, 200, 200);
        right = new Paddle(670, 60, paddleWidth, paddleHeight);
        left = new Paddle(15, 60, paddleWidth, paddleHeight);

        border.getChildren().addAll(ball, left, right);

        AnimationTimer ballTimer = new BallTimer();
        ballTimer.start();

        controls();
    }

    /**
     * Setup the controls for the players to move the paddles
     */
    private void controls() {
        // Enable paddleTimers on keypress
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
        // Turn off paddleTimers
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
        private int lastHitCoordinates[];

        @Override
        public void handle(long now) {
            if (flip) {
                ball.setCenterX(ball.getX() - 2);
                ball.setX(ball.getX() - 2);
            } else {
                ball.setCenterX(ball.getX() + 2);
                ball.setX(ball.getX() + 2);
            }

            if (checkPaddleCollision()) {
                // get angle
                flip = !flip;
            }
        }

    }

    /**
     * Check to see if the ball has hit the paddle
     * @return true if the ball has hit the paddle, else false
     */
    private boolean checkPaddleCollision() {
        if (ball.getX() == 2*paddleWidth) {
            if (ball.getY() > left.getY() && ball.getY() < left.getY() + paddleHeight) {
                return true;
            }
        }

        if (ball.getX() == stageWidth - 2*paddleWidth) {
            if (ball.getY() > right.getY() && ball.getY() < right.getY() + paddleHeight) {
                return true;
            }
        }

        return false;
    }
}




