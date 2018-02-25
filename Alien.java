import java.awt.Image;

public class Alien extends Sprite2D{
    private static double xSpeed = 0;

    public Alien(Image i, Image i2) {
        super(i);
        myImage2 = i2;
        xSpeed = 2;
    }

    public boolean move() {
        x+=xSpeed;
        if(x<=0 || x >= winWidth-myImage.getWidth(null)) 
        {
            return true;
        }
        else 
        {
            return false;
        }
    }

    public static void reverseDirection() {
        xSpeed=-xSpeed;
    }

    public static void setXSpeed(double dx) {
        xSpeed=dx;
    }

    public void down() {
        y += 20;
    }

}