package schutte;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Random;

/**
 *
 * @author Tom Schutte
 */
public class Ball {

    Random rand;
    private int xCoordinate;
    private int yCoordinate;
    Color color;
    final int diam = 50;
    int horizontalVelocity = 1;
    int verticalVelocity = 1;

    public Ball(int x, int y, Color c) {
        this.xCoordinate = x;
        this.color = c;
        this.yCoordinate = y;

    }

    public void move(int speed) {
        setX(getX() + (speed * horizontalVelocity));
        setY(getY() + (speed * verticalVelocity));
    }

    public void draw(Graphics e) {
        e.setColor(color);
        e.fillOval(getX(), getY(), diam, diam);
    }

    public Rectangle getBounds() {
        return new Rectangle(getX(), getY(), diam, diam);
    }

    /**
     * @return the x
     */
    public int getX() {
        return xCoordinate;
    }

    /**
     * @param x the x to set
     */
    public void setX(int x) {
        this.xCoordinate = x;
    }

    /**
     * @return the y
     */
    public int getY() {
        return yCoordinate;
    }

    /**
     * @param y the y to set
     */
    public void setY(int y) {
        this.yCoordinate = y;
    }
}
