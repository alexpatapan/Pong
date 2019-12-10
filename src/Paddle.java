import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.shape.Rectangle;

import java.io.File;

public class Paddle extends Rectangle {

    int y;
    boolean direction = false; // false = up, true = down
    private paddleTimer timer;

    public Paddle(int x, int y, int width, int height) {
        super(x, y, width, height);
        this.y = y;
        timer = new paddleTimer();


    }

    public AnimationTimer getPaddleTimer() {
        return timer;
    }

    private class paddleTimer extends AnimationTimer {
        @Override
        public void handle(long now) {
            doHandle();
        }

        private void doHandle() {

           if (direction) {
               if (y < 414) {
                   y += 6;
                   Paddle.this.setY(y);
               }
           } else {
               if (y > 5) {
                   y -= 6;
                   Paddle.this.setY(y);
               }
           }
        }
    }
}