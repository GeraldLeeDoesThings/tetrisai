package core.tetris;

import core.tetris.game.Tetromino;

import java.awt.*;
import java.util.Arrays;

public class GridDisplay implements TetrisConstants {

    private Color[][] displayColors;
    private GridFrame holder;

    public GridDisplay() {

        displayColors = new Color[BOARD_WIDTH][BOARD_HEIGHT];
        for (Color[] column : displayColors) {
            Arrays.fill(column, Color.WHITE);
        }
        holder = new GridFrame("Tetris", displayColors);
    }

    public void calculateDisplayColors(boolean[][] board, Tetromino tetromino) {
        for (int x = 0; x < BOARD_WIDTH; x++) {
            for (int y = 0; y < BOARD_HEIGHT; y++) {
                if (!board[x][y]) {
                    displayColors[x][y] = Color.WHITE;
                }
            }
        }
        if (tetromino != null) {
            for (int[] point : tetromino.getPositions()) {
                if (point[1] < BOARD_HEIGHT) {
                    displayColors[point[0]][point[1]] = tetromino.getType().getColor();
                }
            }
        }
        update();
    }

    private void update() {
        holder.updateColors(displayColors);
    }

    public GridFrame getHolder() {
        return holder;
    }

}
