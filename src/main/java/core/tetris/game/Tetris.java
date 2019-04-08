package core.tetris.game;

import core.tetris.GameInstance;
import core.tetris.GridDisplay;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class Tetris extends GameInstance {

    private Thread requestHandler;
    private AtomicLong targetTime;
    private GridDisplay display;
    private Tetromino.Type heldPiece;
    private List<Tetromino.Type> pieceQueue;
    private List<TetrisAction> tetrisActionQueue;
    private AtomicBoolean isGameRunning;
    private AtomicBoolean autoSoftDrop;
    private boolean canHold;

    public Tetris() {

        pieceQueue = Collections.synchronizedList(new LinkedList<>());
        tetrisActionQueue = Collections.synchronizedList(new LinkedList<>());
        targetTime = new AtomicLong(System.currentTimeMillis() + 500L);
        pieceQueue = null;
        canHold = true;
        heldPiece = null;
        isGameRunning = new AtomicBoolean(true);
        autoSoftDrop = new AtomicBoolean(false);
        display = new GridDisplay(0, 10, 0, 20);

        requestHandler = new Thread(() -> {
            while (isGameRunning.get()) {
                if (System.currentTimeMillis() >= targetTime.get()) {
                    targetTime.set(System.currentTimeMillis() + 500L);
                    if (super.softDropTetromino()) {
                        fuseCurrentPiece();
                        canHold = true;
                    }
                }
                if (!tetrisActionQueue.isEmpty()) {
                    switch (tetrisActionQueue.get(0)) {
                        case HOLD:
                            if (canHold) {
                                Tetromino.Type currentType = currentPiece.getType();
                                currentPiece = (heldPiece != null) ? new Tetromino(heldPiece) : drawFromPieceQueue();
                                heldPiece = currentType;
                                canHold = false;
                                targetTime.set(System.currentTimeMillis() + 500L);
                            }
                            break;
                        case HARD_DROP:
                            super.hardDropTetromino();
                            fuseCurrentPiece();
                            canHold = true;
                            targetTime.set(System.currentTimeMillis() + 500L);
                            break;
                        case SOFT_DROP:
                            if (super.softDropTetromino()) {
                                fuseCurrentPiece();
                                canHold = true;
                            }
                            break;
                        case SHIFT_LEFT:
                            super.moveTetrominoLeft();
                            break;
                        case ROTATE_LEFT:
                            if (super.rotateTetrominoLeft()) {
                                targetTime.set(System.currentTimeMillis() + 500L);
                            }
                            break;
                        case SHIFT_RIGHT:
                            super.moveTetrominoRight();
                            break;
                        case ROTATE_RIGHT:
                            if (super.rotateTetrominoRight()) {
                                targetTime.set(System.currentTimeMillis() + 500L);
                            }
                            break;
                    }
                    tetrisActionQueue.remove(0);
                }
                display.calculateDisplayColors(board, currentPiece);
                clearLines();
            }
        });

    }

    @Override
    public void moveTetrominoLeft() {
        if (tetrisActionQueue.size() < 4) {
            tetrisActionQueue.add(TetrisAction.SHIFT_LEFT);
        }
    }

    @Override
    public void moveTetrominoRight() {
        if (tetrisActionQueue.size() < 4) {
            tetrisActionQueue.add(TetrisAction.SHIFT_RIGHT);
        }
    }

    @Override
    public boolean softDropTetromino() {
        if (tetrisActionQueue.size() < 4) {
            tetrisActionQueue.add(TetrisAction.SOFT_DROP);
        }
        return true;
    }

    @Override
    public void hardDropTetromino() {
        if (tetrisActionQueue.size() < 4) {
            tetrisActionQueue.add(TetrisAction.HARD_DROP);
        }
    }

    @Override
    public boolean rotateTetrominoRight() {
        if (tetrisActionQueue.size() < 4) {
            tetrisActionQueue.add(TetrisAction.ROTATE_RIGHT);
        }
        return false;
    }

    @Override
    public boolean rotateTetrominoLeft() {
        if (tetrisActionQueue.size() < 4) {
            tetrisActionQueue.add(TetrisAction.ROTATE_LEFT);
        }
        return false;
    }

    public void hold() {
        if (tetrisActionQueue.size() < 4) { // TODO Hold action
            tetrisActionQueue.add(TetrisAction.HOLD);
        }
    }

    private Tetromino drawFromPieceQueue() {
        while (pieceQueue.size() < 4) {
            pieceQueue.add(generateNextTetromino().getType());
        }
        Tetromino newTetromino = new Tetromino(pieceQueue.get(0));
        while (pieceQueue.size() < 4) {
            pieceQueue.add(generateNextTetromino().getType());
        }
        return newTetromino;
    }

    public void gravitySoftDrop() {
        autoSoftDrop.set(true);
    }

    public boolean isRunning() {
        return isGameRunning.get();
    }

}
