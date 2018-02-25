import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.*;
import java.awt.image.*;
import java.util.ArrayList;
import java.util.Iterator;

public class InvadersApplication extends JFrame implements Runnable, KeyListener {

    private static final Dimension WindowSize = new Dimension(800,600);
    private static final int NUMALIENS = 30;
    private ArrayList<Alien> AliensArray = new ArrayList<>(NUMALIENS);
    private Spaceship PlayerShip;
    private static String workingDirectory;
    private static boolean isGraphicsInitialised = false;
    private BufferStrategy strategy;
    private ArrayList<PlayerBullet> bullets = new ArrayList<>();
    private boolean isGameInProgress = true;
    private int alienCount = NUMALIENS;
    private int score = 0;
    private int hiScore = score;
    
    public InvadersApplication() {

        Dimension screensize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        int x = screensize.width/2 - WindowSize.width/2;
        int y = screensize.height/2 - WindowSize.height/2;
        setBounds(x, y, WindowSize.width, WindowSize.height);
        setVisible(true);
        this.setTitle("Space Invaders!");
       
        createBufferStrategy(2);
        strategy = getBufferStrategy();

        ImageIcon ship = new ImageIcon(workingDirectory + "\\playership.png");
        PlayerShip = new Spaceship(ship.getImage());
        PlayerShip.setPosition(300, 530);

        ImageIcon alien1 = new ImageIcon(workingDirectory + "\\sprite1.png");
        ImageIcon alien2 = new ImageIcon(workingDirectory + "\\sprite2.png");
        
        for(int i=0; i < NUMALIENS; i++) {
            Alien al = new Alien(alien1.getImage(), alien2.getImage());
            AliensArray.add(al);
            double xx = (i%5)*80 + 120;
            double yy = (i/5)*40 + 80;
            AliensArray.get(i).setPosition(xx, yy);
        }
        
        Sprite2D.setWinWidth(WindowSize.width);
        repaint();
        addKeyListener(this);
        Thread t = new Thread(this);
        t.start();
        isGraphicsInitialised = true;

    }
    
    public void run() {
        while(true) {
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {}
            boolean alienDirectionReversalNeeded = false;
            for (int i = 0; i < AliensArray.size(); i++) {
                if (AliensArray.get(i).move()) {
                    alienDirectionReversalNeeded = true;
                }
            }

            if (alienDirectionReversalNeeded) {
                Alien.reverseDirection();
                for (int i = 0; i < AliensArray.size(); i++) {
                    AliensArray.get(i).down();
                }
            }

            PlayerShip.move();
            for(PlayerBullet b: bullets){
                b.move();
            }

            //collision testing 
            for(int i = 0; i < AliensArray.size(); i++)
            {
                Alien alienCollide = AliensArray.get(i);
                double height1 = alienCollide.myImage.getHeight(null);
                double width1 = alienCollide.myImage.getWidth(null);

                double x1 = alienCollide.x;
                double y1 = alienCollide.y;

                for(int j = 0; j < bullets.size(); j++){
                    PlayerBullet bulletCollide = bullets.get(j);
                    double height2 = bulletCollide.myImage.getHeight(null);
                    double width2 = bulletCollide.myImage.getWidth(null);

                    double x2 = bulletCollide.x;
                    double y2 = bulletCollide.y;

                    if (((x1<x2 && x1+width1>x2) || 
                    	(x2<x1 && x2+width2>x1))
                    	&& 
                    	((y1<y2 && y1+height1>y2) || 
                    	(y2<y1 && y2+height2>y1))){
                    	alienCount --;
                        bullets.remove(j);
                        AliensArray.remove(i);
                        score+=100;
                    }
                }
            }
           
            //lose scenario - for now
            //will update next assignment
           for(int i = 0; i < AliensArray.size(); i++) {
            if(AliensArray.get(i).y >= 530 ) {
            	System.out.println("You Lose");
            	System.exit(0);
            }
          }
           
           if(alienCount == 0) {
        	   System.out.println("YOU WIN!!!");
        	   System.exit(0);
        	   isGameInProgress = false;
           }
            this.repaint();
        }
    }
    
    public int getScore() {
    	return score;
    }
    
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                PlayerShip.setXSpeed(-5);
                break;
            case KeyEvent.VK_RIGHT:
                PlayerShip.setXSpeed(5);
                break;
            case KeyEvent.VK_SPACE:
                shoot();
                break;
            case KeyEvent.VK_ESCAPE:
            	System.exit(0);
        }

    }

    public void keyReleased(KeyEvent e) {
    	switch (e.getKeyCode()) {
        case KeyEvent.VK_LEFT:
            PlayerShip.setXSpeed(0);
            break;
        case KeyEvent.VK_RIGHT:
            PlayerShip.setXSpeed(0);
            break;
    	}
    }

    public void keyTyped(KeyEvent e) {
    	//no usage for this event handler
    }

    public void shoot() {
        ImageIcon bullet = new ImageIcon(workingDirectory + "\\bullet.png");
        Image bulletImage = bullet.getImage();
        PlayerBullet b = new PlayerBullet(bulletImage);
        b.setPosition(PlayerShip.x+24.5, PlayerShip.y);
        bullets.add(b);
    }
    
    public void paint(Graphics g) {
    	
        if(isGraphicsInitialised) {
            g = strategy.getDrawGraphics();
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, WindowSize.width, WindowSize.height);
            for(int i = 0; i < AliensArray.size(); i++) {
                AliensArray.get(i).paint(g);
            }
            Graphics2D g2 = (Graphics2D)g;
            Font myFont = new Font("Arabic", Font.BOLD, 24);
           	g2.setColor(Color.YELLOW);
            g2.setFont(myFont);
            g2.drawString("Score: " + getScore(), 180, 50);
            //g2.drawString("HI-SCORE: " + getHiScore(), 450, 50);
            
            Iterator<PlayerBullet> loop = bullets.iterator();
            while(loop.hasNext()){
                PlayerBullet b = loop.next();
                b.paint(g);
            }
            PlayerShip.paint(g);
            g.dispose();
            strategy.show();
        }
    }
    
    public static void main(String[] args) {
    	workingDirectory = System.getProperty("user.dir");
        InvadersApplication game = new InvadersApplication();
    }
}