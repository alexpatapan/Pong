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

    private final int stageWidth = 700;
    private final int stageHeight = 500;
    private final int paddleWidth = 15;
    private final int paddleHeight = 60;
    private int lastHitCoordinates[] = new int[2];

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
        lastHitCoordinates[0] = 200;
        lastHitCoordinates[1] = 200;

    }

    /**
     * Run an instance of the game
     */
    private void run() {
        ball = new Ball(10, 200, 200);
        right = new Paddle(670, 190, paddleWidth, paddleHeight);
        left = new Paddle(15, 190, paddleWidth, paddleHeight);

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
        private int moveY = -1;
        private int moveX = 2;
        @Override
        public void handle(long now) {
            moveY = checkBoundary(moveY);

            moveBall(flip, moveX, moveY);

            if (checkPaddleCollision()) {
                //get angle
                moveY = calcDirection(flip, moveY);


                flip = !flip;
            }

            if (ball.getX() < 0 || ball.getX() > stageWidth) {
                ball.setX(200);
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

    /**
     * A method to move the ball
     * @param flip
     */
    private void moveBall(boolean flip, int x, int y) {
        if (flip) {
            ball.setCenterX(ball.getX() - x);
            ball.setX(ball.getX() - x);
            ball.setCenterY(ball.getY() - y);
            ball.setY(ball.getY() - y);
        } else {
            ball.setCenterX(ball.getX() + x);
            ball.setX(ball.getX() + x);
            ball.setCenterY(ball.getY() - y);
            ball.setY(ball.getY() - y);
        }
    }

    private int calcDirection(boolean flip, int moveY) {

        int paddleCentre;

        if (flip) {
            paddleCentre = (int) left.getY() + paddleHeight / 2;
        } else {
            paddleCentre = (int) right.getY() + paddleHeight / 2;
        }

        int distance = (int) ball.getCenterY() - paddleCentre;

        if (Math.abs(distance) < 5 ) {
            return 0;
        }
        return -1* (distance / 5);

    }

    private int checkBoundary(int moveY) {
        if (ball.getY() + 2*ball.getRadius() > 490 || ball.getY() < 10) {
            return moveY * -1;
        } else return moveY;
    }
}




