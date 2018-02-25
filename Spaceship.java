import java.awt.Image;

public class Spaceship extends Sprite2D{

    private double xSpeed = 0;

    public Spaceship(Image i) {
        super(i);
        myImage2 = i;
    }

    public void move() {
        x+=xSpeed;

        if(x<=0){
        		x=0;
        		xSpeed=0;
        }
        else if(x>=winWidth-myImage.getWidth(null)){
            	x=winWidth-myImage.getWidth(null);
            	xSpeed = 0;
        }
    }

    public void setXSpeed(double dx) {
        xSpeed=dx;
    }

}