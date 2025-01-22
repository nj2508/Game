import javax.swing.*;
import java.awt.*;

public class DrawGhost extends JComponent {
    private Rectangle ghost;
    public DrawGhost(Rectangle r) {
        this.ghost = r;
    }
    public void paintComponent(Graphics g) {
        Graphics2D graphics = (Graphics2D) g;

        graphics.draw(ghost);
    }
}
