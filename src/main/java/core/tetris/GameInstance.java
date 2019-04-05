package core.tetris;

import core.tetris.game.Tetromino;

import java.awt.*;
import java.util.LinkedList;

public abstract class GameInstance {

    protected boolean[][] board;
    protected Tetromino currentPiece;
    protected boolean canFloorKick;

    public GameInstance() {
        board = new boolean[10][23];
        init();
    }

    public GameInstance(boolean[][] board) {
        this.board = board;
        init();
    }

    void init() {
        canFloorKick = true;
        currentPiece = null;
    }

    protected static Tetromino generateNextTetromino() {
        return new Tetromino(Tetromino.Type.I);
    }

    public void moveTetrominoLeft() {
        currentPiece.tryLeftShift(board);
    }

    public void moveTetrominoRight() {
        currentPiece.tryRightShift(board);
    }

    public void softDropTetromino() {
        currentPiece.softDrop(board);
    }

    public void hardDropTetromino() {
        currentPiece.hardDrop(board);
    }

    protected void fuseCurrentPiece() {
        for (int[] point : currentPiece.getPositions()) {
            board[point[0]][point[1]] = true;
        }
    }

    protected int clearLines() {
        int lineCount = 0;
        for (int y = 0; y < 23; y++) {
            if (isLineAtY(y)) {

            }
        }
        return lineCount;
    }

    private boolean isLineAtY(int y) {
        boolean allFull = true;
        for (int x = 0; x < 10; x++) {
            if (!board[x][y]) {
                return false;
            }
        }
        return true;
    }

    private void clearLineAtY(int y) {
        for (int x = 0; x < 10; x++) {
            board[x][y] = false;
        }
    }

    private void shiftDownAtY(int y) {

    }

    public void rotateTetrominoRight() {
        if (currentPiece.attemptRotation(board, Tetromino.Rotate.RIGHT, canFloorKick)) {
            canFloorKick = false;
        }
    }

}
