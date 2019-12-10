import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.*;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.animation.AnimationTimer;

import java.io.File;
import javafx.util.Duration;


public class Main extends Application {

    Pane border = new Pane();
    Scene scene;
    Ball ball;
    Paddle left, right;
    Label lscore, rscore;

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

        String startup = "sounds/Beep13.wav";
        Media playSound = new Media(new File(startup).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(playSound);
        mediaPlayer.play();
    }

    /**
     * Run an instance of the game
     */
    private void run() {
        ball = new Ball(10, 350, 250);
        right = new Paddle(670, 190, paddleWidth, paddleHeight);
        left = new Paddle(15, 190, paddleWidth, paddleHeight);

        lscore = new Label("0");
        lscore.setLayoutX(60);
        lscore.setLayoutY(20);
        lscore.setFont(new Font("Arial", 27));
        rscore = new Label("0");
        rscore.setLayoutX(630);
        rscore.setLayoutY(20);
        rscore.setFont(new Font("Arial", 27));

        border.getChildren().addAll(ball, left, right, lscore, rscore);

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

        String paddleHit = "sounds/paddle.wav";
        Media paddleSound = new Media(new File(paddleHit).toURI().toString());
        MediaPlayer playPaddleSound = new MediaPlayer(paddleSound);

       // private val explosion = Media(App::class.java.getResource("/bombs/explosion.mp3").toString())

        @Override
        public void handle(long now) {
            moveY = checkBoundary(moveY);
            playPaddleSound.stop();
            moveBall(flip, moveX, moveY);

            if (checkPaddleCollision()) {
                //get angle
                moveY = calcDirection(flip, moveY);
                playPaddleSound.play();

                flip = !flip;
            } else {
                playPaddleSound.stop();
            }

            if (ball.getX() < 0 || ball.getX() > stageWidth) {

                if (ball.getX() < 0) {
                    rscore.setText(Integer.toString(Integer.parseInt(rscore.getText()) + 1));
                } else {
                    lscore.setText(Integer.toString(Integer.parseInt(rscore.getText()) + 1));
                }

                // reset ball
                ball.setX(350);
                ball.setY(250);
                moveY = (int) (Math.random()*7-3);
                flip = (Math.random() < 0.5);
                System.out.println(moveY);
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
     * @param flip The current direction of the ball
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
        return -1 * (distance / 5);

    }

    private int checkBoundary(int moveY) {
        if (ball.getY() + 2*ball.getRadius() > 490 || ball.getY() < 10) {
            return moveY * -1;
        } else return moveY;
    }
}




