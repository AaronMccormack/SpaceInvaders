import java.awt.Image;

public class PlayerBullet extends Sprite2D{

    private static double ySpeed = -4;

    public PlayerBullet(Image i) {
        super(i);
        myImage2 = i;
    }

    public void move() {
        y+=ySpeed;
    }  
}