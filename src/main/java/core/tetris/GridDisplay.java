package core.tetris;

import core.tetris.game.Tetromino;

import java.awt.*;
import java.util.Arrays;

public class GridDisplay {

    private Color[][] displayColors;
    private GridFrame holder;
    private int startX, endX, startY, endY;

    public GridDisplay(int startX, int startY, int endX, int endY) {

        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;

        displayColors = new Color[endX - startX][endY - startY];
        for (Color[] column : displayColors) {
            Arrays.fill(column, Color.WHITE);
        }
        holder = new GridFrame("Tetris", displayColors);
    }

    public void calculateDisplayColors(boolean[][] board, Tetromino tetromino) {
        for (int x = startX; x < endX; x++) {
            for (int y = startY; y < endY; y++) {
                if (board[x][y]) {
                    displayColors[x][y] = Color.BLACK;
                }
            }
        }
        for (int[] point : tetromino.getPositions()) {
            displayColors[point[0]][point[1]] = tetromino.getType().getColor();
        }
        update();
    }

    private void update() {
        holder.updateColors(displayColors);
    }

}
