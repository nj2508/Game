import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;
import java.util.List;
import java.util.Timer;

/**
 * Main class
 *
 * @author pigsty
 * @version 1.0
 */
public class Main extends JFrame {
    final static ArrayList<Collidable> objects = new ArrayList<>();
    final static JFrame frame = new JFrame();
    final static Vector2D v = new Vector2D(0, 0);
    static Rect ghost = new Rect(); // ghost object
    static Rect player = new Rect(); // player object
    private static lis keyList = new lis(player);
    private final static boolean[] isContactingWall = {false};
    private final static boolean[] isContactingCeiling = {false};
    private static ArrayList<Collidable> collided;
    private static Map map;


    /**
     * Main body; generates frame, adds player.
     */
    public static void main(String[] args)
    {
        frame.setSize(900, 700);
        frame.setLayout(new BorderLayout());
        frame.setTitle("Game Window");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.addKeyListener(keyList);

        map = new Map(frame, objects);

        player = map.getPlayer();

//        ghost = map.getGhost();

        frame.add(map);

        updateMovement();

        frame.setVisible(true);
    }

    /**
     * Moves the player every 10th of a second based on the player's velocity, also
     * updates velocity based on acceleration and checks for collisions
     */
    public static void updateMovement() {
        final int MAX_SPEED = 10;
        final double GRAVITY = 0.05;
        final double FRICTION_RATE = 0.003;

        double friction[] = {1};

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Vector2D a = keyList.getAcceleration();

                /*
                 * Apply friction in +-x plane
                 */
                if (a.getX() == 0) {
                    if (!player.checkMovement(new Vector2D(0, 2))) {
                        if (v.getX() > 0) {
                            if (v.getX() > 0 && v.getX() < 0.5) {
                                v.setX(0);
                                friction[0] = 1;
                            } else {
                                v.setX(v.getX() / friction[0]);
                                friction[0] += FRICTION_RATE;
                            }
                        } else if (v.getX() < 0) {
                            if (v.getX() < 0 && v.getX() > -0.5) {
                                v.setX(0);
                                friction[0] = 1;
                            } else {
                                v.setX(v.getX() / friction[0]);
                                friction[0] += FRICTION_RATE;
                            }
                        }
                    } else {
                        friction[0] = 1;
                    }
                }

                if (v.getX() < MAX_SPEED) {
                    v.addX(a.getX());
                }
                if (v.getY() < MAX_SPEED) {
                    v.addY(a.getY());
                }

                boolean canMoveX = player.checkMovement(new Vector2D((int) v.getX(), 0), frame);

                boolean canMoveY = player.checkMovement(new Vector2D(0, (int) v.getY()), frame);
                boolean isOnGround = !player.checkMovement(new Vector2D(0, 1));

                if (canMoveX) {
                    player.moveX((int) v.getX());
                } else {
                    a.setX(0);
                    v.setX(0);
                }
                if (!isOnGround) {
                    if (v.getY() < 1 && v.getY() > -1) {
                        v.addY(GRAVITY * 8);
                    }
                    v.addY(GRAVITY);
                }
                if (canMoveY) {
                    player.moveY((int) v.getY());
                } else {
                    a.setY(0);
                    v.setY(0);
                }

                frame.revalidate();
                frame.repaint();
            }
        }, 10, 10);
    }
    public static Map getMap() {return map;}
    public static Vector2D getV() {return v;}
}
class lis implements KeyListener {
    Vector2D acceleration = new Vector2D(0, 0);
    Rect player;

    public lis(Rect player) {
        this.player = player;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        final double MOVEMENT_RATE = 0.2;
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            acceleration.setY(-1 * MOVEMENT_RATE);
        }
        if (e.getKeyCode() == KeyEvent.VK_A) {
            acceleration.setX(-1 * MOVEMENT_RATE);
        }
        if (e.getKeyCode() == KeyEvent.VK_D) {
            acceleration.setX(MOVEMENT_RATE);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            acceleration.setY(0);
        }
        if (e.getKeyCode() == KeyEvent.VK_A) {
            acceleration.setX(0);
        }
        if (e.getKeyCode() == KeyEvent.VK_D) {
            acceleration.setX(0);
        }
    }
    public Vector2D getAcceleration() {
        return acceleration;
    }
}
class Collidable extends JComponent {
    private Rectangle rect;
    private int x;
    private int y;
    private int width;
    private int height;
    public Collidable(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.height = height;
        this.width = width;
        this.rect = new Rectangle(x, y, width, height);

    }
    public void paintComponent(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics;

        g.draw(rect);
    }
    public Rectangle getRect() {
        return this.rect;
    }
    public int getXPos() {return x;}
    public int getYPos() {return y;}
}
class Map extends JComponent {
    Rect ghost;
    Rect player;
    Collidable floor;
    Collidable leftWall;
    Collidable rightWall;
    Collidable roof;
    public Map (JFrame frame, ArrayList<Collidable> objects) {
//        ghost = new Rect(300, 200, 15, 15, null);
        player = new Rect(300, 200, 10, 10);
        floor = new Collidable(0, frame.getHeight() - 100, frame.getWidth(), 100);
        leftWall = new Collidable(10, 0, 10, frame.getHeight());
        rightWall = new Collidable(frame.getWidth() - 30, 0, 10, frame.getHeight());
        roof = new Collidable(0, 0, frame.getWidth(), 10);

        objects.add(floor);
        objects.add(leftWall);
        objects.add(rightWall);
        objects.add(roof);
    }
    public void paintComponent(Graphics graphics) {
        Graphics2D g = (Graphics2D) graphics;
        g.draw(player.getRect());
        g.setColor(Color.CYAN);
        g.fill(player.getRect());

//        g.draw(ghost.getRect());
//        g.setColor(Color.LIGHT_GRAY);
//        g.fill(ghost.getRect());

        g.setColor(Color.BLACK);
        g.draw(floor.getRect());
        g.draw(leftWall.getRect());
        g.draw(rightWall.getRect());
        g.draw(roof.getRect());
    }
    public Rect getPlayer() {
        return player;
    }
    public Rect getGhost() {
        return ghost;
    }
    public List<Collidable> getCollidables() {
        return Arrays.asList(floor, leftWall, rightWall, roof);
    }

}
enum DirX {
    LEFT(-1),
    RIGHT(1);
    int dirX;
    private DirX(int dirX) {
        this.dirX = dirX;
    }
}
enum DirY {
    UP(-1),
    DOWN(1);
    int dirY;
    private DirY(int dirY) {
        this.dirY = dirY;
    }
}