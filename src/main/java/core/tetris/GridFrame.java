package core.tetris;

import javax.swing.*;
import java.awt.*;

public class GridFrame extends JFrame implements TetrisConstants {

    private Color[][] displayColors;
    private boolean initColors;

    GridFrame(String name, Color[][] displayColors) {
        super(name);
        this.displayColors = displayColors;
        initColors = true;
        super.setSize(displayColors.length * (BOARD_SCALE + 2) + 2 * BOARD_BORDER, displayColors[0].length * (BOARD_SCALE + 2) + 2 + BOARD_SCALE);
        super.setBackground(Color.BLACK);
        super.setVisible(true);
    }

    protected void updateColors(Color[][] displayColors) {
        this.displayColors = displayColors;
        paint(getGraphics());
    }

    @Override
    public void paint(Graphics g) {
        if (g != null) {
            if (initColors) {
                g.setColor(Color.LIGHT_GRAY);
                g.fillRect(0,0, getWidth(), getHeight());
                initColors = false;
            }
            int x = 0;
            int y;
            for (Color[] column : displayColors) {
                y = 0;
                for (Color elem : column) {
                    g.setColor(elem);
                    g.fillRect(x * (BOARD_SCALE + 2) + 1, (column.length - y) * (BOARD_SCALE + 2) + 1, BOARD_SCALE, BOARD_SCALE);
                    y++;
                }
                x++;
            }
        }
    }

}
