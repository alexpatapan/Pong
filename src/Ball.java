import javafx.animation.AnimationTimer;
import javafx.scene.shape.Circle;

public class Ball extends Circle {
    private int x;
    private int y;

    Ball(int radius, int x, int y) {
        super(x, y, radius);
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

}