import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Color;
import javax.swing.*;

/**
 * Write a description of class Rect here.
 *
 * @author pigsty
 * @version 1.0
 */
public class Rect extends JComponent
{
    private Rectangle rectangle;
    private int x;
    private int y;
    private int height;
    private int width;
    private ArrayList<DirX> cannotMoveX;
    public ArrayList<DirY> cannotMoveY;

    public Rect() {
    }
    public Rect(int newX, int newY, int h, int w) {
        this.x = newX;
        this.y = newY;
        this.height = h;
        this.width = w;
        this.cannotMoveX = new ArrayList<>();
        this.cannotMoveY = new ArrayList<>();
        rectangle = new Rectangle(x, y, height, width);
    }
    public Rectangle getRect() {
        return this.rectangle;
    }
    public List getPos() {
        return Arrays.asList(rectangle.getX(), rectangle.getY());
    }
    public int round(double d) {
        return (int) Math.round(d);
    }
    public boolean checkMovement(Vector2D v, JFrame frame) {
        Rectangle checker = new Rectangle(x + (int) v.getX(), y + (int) v.getY(), 15, 15);
        frame.add(new DrawGhost(new Rectangle(x, y, 1, 1)));
        for (Collidable c : Main.getMap().getCollidables()) {
            if (checker.getBounds().intersects(c.getRect().getBounds())) {
                return false;
            }
        }
        return true;
    }
    public boolean checkMovement(Vector2D v) {
        Rectangle checker = new Rectangle(x + (int) v.getX() - 30, y + (int) v.getY(), 15, 15);
        for (Collidable c : Main.getMap().getCollidables()) {
            if (checker.getBounds().intersects(c.getRect().getBounds())) {
                if (v.getY() == 0) {
                    System.out.println("Collided at " + checker.intersection(c.getRect()).getMinX());
                }
                return false;
            }
        }
        return true;
    }
    public void move(Vector2D v) {
        rectangle.translate((int) v.getX(), (int) v.getY());
        x += v.getX();
        y += v.getY();
    }
    public void moveX(double newX) {
        rectangle.translate((int) newX, 0);
        this.x += newX;
    }
    public void moveY(double newY) {
        rectangle.translate(0, (int) newY);
        this.y += newY;
    }
    public void paintComponent(Graphics g) {
        Graphics2D graphics = (Graphics2D) g;

        graphics.draw(rectangle);
        graphics.setColor(Color.BLUE);
        graphics.fill(rectangle);
    }
}
