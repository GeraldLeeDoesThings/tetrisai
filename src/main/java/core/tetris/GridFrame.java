package core.tetris;

import javax.swing.*;
import java.awt.*;

class GridFrame extends JFrame {

    private Color[][] displayColors;

    GridFrame(String name, Color[][] displayColors) {
        super(name);
        this.displayColors = displayColors;
        super.setSize(displayColors.length * 12 + 2, displayColors[0].length * 12 + 2);
        super.setBackground(Color.BLACK);
    }

    protected void updateColors(Color[][] displayColors) {
        this.displayColors = displayColors;
        paint(getGraphics());
    }

    @Override
    public void paint(Graphics g) {
        int x = 0;
        int y;
        for (Color[] column : displayColors) {
            y = 0;
            for (Color elem : column) {
                g.setColor(elem);
                g.fillRect(x * 12 + 1, y * 12 + 1, 10, 10);
                y++;
            }
            x++;
        }
    }

}
