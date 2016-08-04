package schutte;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 *
 * @author Tom Schutte
 */
public class Paddle {
    int xCoordinate;
    int yCoordinate;
    final int HEIGHT = 100;
    final int WIDTH = 20;
    Color color;
    
    private int speed;
    
    public Paddle(int x, int y, Color C){
        this.color = C;
        this.xCoordinate = x;
        this.yCoordinate = y;
        speed = 0;
    }
    
    public void move(int speed){
        yCoordinate += (this.speed * speed);
        
    }
    
    public void moveX(int dx){
        if (dx > 0)
            xCoordinate += WIDTH;
        else
            xCoordinate -= WIDTH;
    }
    
    public Rectangle getBounds(){        
        return new Rectangle(xCoordinate, yCoordinate, WIDTH, HEIGHT);
    }
    
    public void setSpeed(int speed) {
        this.speed = speed;
    }
    
    public void draw (Graphics e){
        e.setColor(color);
        e.fillRect(xCoordinate, yCoordinate, WIDTH, HEIGHT);       
    }
}
