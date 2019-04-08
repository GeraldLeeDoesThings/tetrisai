package core.tetris;

import core.tetris.game.Tetromino;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public abstract class GameInstance {

    protected boolean[][] board;
    protected Tetromino currentPiece;
    protected boolean canFloorKick;
    protected List<Tetromino.Type> pieceBag;

    public GameInstance() {
        board = new boolean[10][23];
        init();
    }

    public GameInstance(boolean[][] board) {
        this.board = board;
        init();
    }

    void init() {
        pieceBag = Collections.synchronizedList(new LinkedList<>());
        canFloorKick = true;
        currentPiece = null;
    }

    protected Tetromino generateNextTetromino() {
        if (pieceBag.isEmpty()) {
            fillPieceBag();
        }
        return new Tetromino(pieceBag.get((int)(Math.random() * pieceBag.size())));
    }

    public void moveTetrominoLeft() {
        currentPiece.tryLeftShift(board);
    }

    public void moveTetrominoRight() {
        currentPiece.tryRightShift(board);
    }

    public boolean softDropTetromino() {
        return currentPiece.softDrop(board);
    }

    public void hardDropTetromino() {
        currentPiece.hardDrop(board);
    }

    protected void fuseCurrentPiece() {
        for (int[] point : currentPiece.getPositions()) {
            board[point[0]][point[1]] = true;
        }
        canFloorKick = true;
    }

    protected int clearLines() {
        int lineCount = 0;
        for (int y = 0; y < 23; y++) {
            if (isLineAtY(y)) {
                shiftDownAtY(y);
                lineCount++;
            }
        }
        return lineCount;
    }

    protected boolean isLineAtY(int y) {
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
        clearLineAtY(y);
        for (int lineMove = y; lineMove < 22; lineMove++) {
            for (int x = 0; x < 10; x++) {
                board[x][y] = board[x][y + 1];
            }
            clearLineAtY(y + 1);
        }
        clearLineAtY(22);
    }

    public boolean rotateTetrominoRight() {
        boolean didFloorKick;
        if ((didFloorKick = currentPiece.attemptRotation(board, Tetromino.Rotate.RIGHT, canFloorKick))) {
            canFloorKick = false;
        }
        return didFloorKick;
    }

    public boolean rotateTetrominoLeft() {
        boolean didFloorKick;
        if ((didFloorKick = currentPiece.attemptRotation(board, Tetromino.Rotate.LEFT, canFloorKick))) {
            canFloorKick = false;
        }
        return didFloorKick;
    }

    private void fillPieceBag() {
        pieceBag.clear();
        pieceBag.addAll(Arrays.asList(Tetromino.Type.values()));
    }

}
