package schutte;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JApplet;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import java.util.Random;

/**
 *
 * @author Tom Schutte
 */
@SuppressWarnings("serial")
public class Pong extends JApplet implements ActionListener, KeyListener {

    //Not final because it can be set in a normal game, defaults to 4 otherwise.
    int BALL_SPEED = 0;
    final int PADDLE_SPEED = 5;
    private int vollyCount = 0, playerOneScore = 0, playerTwoScore = 0;
    private Timer timer;
    private Paddle playerOne, playerTwo;
    private Ball ball;
    final int APPLET_WIDTH = 800;
    final int APPLET_HEIGHT = 600;
    private Image offScreen;
    private Graphics buffer;
    private boolean computer = false;
    private int comp, diff;
    //Random used in difficulty processing. decides whether computer gets to move.
    private Random rand = new Random();
    

    public void init() {
        setBackground(Color.BLACK);
        setFocusable(true);
        offScreen = createImage(APPLET_WIDTH, APPLET_HEIGHT);
        buffer = offScreen.getGraphics();

        addKeyListener(this);

        playerOne = new Paddle(50, APPLET_HEIGHT / 2, Color.WHITE);
        playerTwo = new Paddle(APPLET_WIDTH - 50, APPLET_HEIGHT / 2, Color.WHITE);
        ball = new Ball(APPLET_WIDTH / 2, APPLET_HEIGHT / 2, Color.WHITE);

        JOptionPane.showMessageDialog(null,
                "Instructions:\nPlayers 1 uses 'w' and 's' to move.\n"
                + "Player 2 uses the up and down arrow keys.\n"
                + "Press Space to reset the ball!\n");

        comp = JOptionPane.showConfirmDialog(null, "Play a computer?");
        if (comp == JOptionPane.YES_OPTION) {
            computer = true;
            BALL_SPEED = 4;
            try {
                String cDiff = JOptionPane.showInputDialog("Difficulty 1-10"
                        + "\t \n 1-2:   Baby"
                        + "\t \n 3-4:   Easy"
                        + "\t \n 5:      Normal"
                        + "\t \n 6:      Hard"
                        + "\t \n 7:      Now were talking"
                        + "\t \n 8:      Manly"
                        + "\t \n 9:      Super Manly"
                        + "\t \n 10:    Death Wish"
                        + "\t \n (5 is default): ");
                diff = Integer.parseInt(cDiff);
            } catch (NumberFormatException e) {
                diff = 5;
            }
        } else {
            computer = false;
            diff = 0;
            try {
                String setBallSpeed = JOptionPane.showInputDialog("Set Ball speed (4 is default): ");
                BALL_SPEED = Integer.parseInt(setBallSpeed);
            } catch (NumberFormatException e) {
                BALL_SPEED = 4;
            }
        }

        //game loop control.
        timer = new Timer(5, this);
        timer.start();

        setSize(APPLET_WIDTH, APPLET_HEIGHT);

    }

    @Override
    public void paint(Graphics g) {
        //buffer clears screen to clean up shuttering and melting images.
        buffer.clearRect(0, 0, APPLET_WIDTH, APPLET_HEIGHT);
        setBackground(Color.WHITE);
        //everything is redrawn back to buffer.
        buffer.drawString("Bounces: " + vollyCount, APPLET_WIDTH / 2, 10);
        buffer.drawString("Player one: " + playerOneScore, APPLET_WIDTH / 4, 20);
        buffer.drawString("Player two: " + playerTwoScore, ((APPLET_WIDTH / 4) * 3), 20);
        if (ball.getX() > APPLET_WIDTH + ball.diam || ball.getX() < 0 - ball.diam) {
            setBackground(Color.WHITE);
            buffer.drawString("Spacebar to reset the ball!", (APPLET_WIDTH / 2) - 40, APPLET_HEIGHT / 2);
        }

        playerOne.draw(buffer);
        playerTwo.draw(buffer);
        ball.draw(buffer);

        //offscreen used to clear screen and clean up rendering.
        g.drawImage(offScreen, 0, 0, null);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        ball.move(BALL_SPEED);
        playerOne.move(PADDLE_SPEED);

        if (computer) {
            if (rand.nextInt(10) <= diff + 1) {
                if (ball.horizontalVelocity > 0) {
                    if (playerTwo.yCoordinate > ball.getY()) {
                        playerTwo.yCoordinate += -PADDLE_SPEED;
                        
                    } else {
                        playerTwo.yCoordinate += PADDLE_SPEED;
                    }
                } else {
                    playerTwo.yCoordinate += 0;
                }
            }
        } else {
            playerTwo.move(PADDLE_SPEED);
        }

        checkCollision();

        repaint();
    }

    public void checkCollision() {
        Rectangle playerOneBounds = playerOne.getBounds();
        Rectangle playerTwoBoudns = playerTwo.getBounds();
        Rectangle ballBounds = ball.getBounds();

        if (playerOneBounds.intersects(ballBounds) && ball.horizontalVelocity < 0) {
            ball.horizontalVelocity = -ball.horizontalVelocity;
            vollyCount++;
            Sound.paddleHit.play();
            //BSPEED = (BSPEED * 2);
            if (vollyCount >= 50){
                ball.color = Color.blue;
            }
        }
        if (playerTwoBoudns.intersects(ballBounds) && ball.horizontalVelocity > 0) {
            ball.horizontalVelocity = -ball.horizontalVelocity;
            vollyCount++;
            if (vollyCount >= 50){
                ball.color = Color.blue;
            }
            //BSPEED = (BSPEED * 2);
        }
        if (ball.getY() < 1) {
            ball.verticalVelocity = -ball.verticalVelocity;
        }
        if (ball.getY() + ball.diam > APPLET_HEIGHT) {
            ball.verticalVelocity = -ball.verticalVelocity;
        }
    }

    @Override
    public void keyPressed(KeyEvent event) {
        int key = event.getKeyCode();

        switch (key) {
            case KeyEvent.VK_W:
                playerOne.setSpeed(-1);
                break;
            case KeyEvent.VK_S:
                playerOne.setSpeed(1);
                break;
            case KeyEvent.VK_D:
                playerOne.moveX(1);
                break;
            case KeyEvent.VK_A:
                playerOne.moveX(-1);
                break;

            case KeyEvent.VK_UP:
                playerTwo.setSpeed(-1);
                break;
            case KeyEvent.VK_DOWN:
                playerTwo.setSpeed(1);
                break;
            //case KeyEvent.VK_ESCAPE:
            //    break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        switch (key) {
            case KeyEvent.VK_W:
                playerOne.setSpeed(0);
                break;
            case KeyEvent.VK_S:
                playerOne.setSpeed(0);
                break;
            case KeyEvent.VK_UP:
                playerTwo.setSpeed(0);
                break;
            case KeyEvent.VK_DOWN:
                playerTwo.setSpeed(0);
                break;
            case KeyEvent.VK_SPACE:
                resetBall();
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        /*
         * unused
         */
    }

    public void resetBall() {
        if (ball.getX() > APPLET_WIDTH + ball.diam) {
            playerOneScore += vollyCount;
            ball.setX(360);
            ball.setY(240);
        }
        if (ball.getX() < 0 - ball.diam) {
            playerTwoScore += vollyCount;
            ball.setX(360);
            ball.setY(240);
        }
        vollyCount = 0;
        if (playerOneScore >= 10) {
            playerOne.color = Color.GREEN;
        }
        if (playerOneScore >= 20) {
            playerOne.color = Color.YELLOW;
        }
        if (playerOneScore >= 30) {
            playerOne.color = Color.RED;
        }
        if (playerTwoScore >= 10) {
            playerTwo.color = Color.GREEN;
        }
        if (playerTwoScore >= 20) {
            playerTwo.color = Color.YELLOW;
        }
        if (playerTwoScore >= 30) {
            playerTwo.color = Color.RED;
        }
    }
}