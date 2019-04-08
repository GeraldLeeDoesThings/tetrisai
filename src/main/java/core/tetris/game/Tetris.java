package core.tetris.game;

import core.tetris.GameInstance;
import core.tetris.GridDisplay;

import java.awt.event.KeyListener;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class Tetris extends GameInstance {

    private Thread requestHandler;
    private AtomicLong targetTime;
    private GridDisplay display;
    private Tetromino.Type heldPiece;
    private List<Tetromino.Type> pieceQueue;
    private SynchronizedTetrisAction nextAction;
    private AtomicBoolean isGameRunning;
    private AtomicLong softDropDelay;
    private Set<ActionExecutionListener> actionExecutionListeners;
    private Set<GravityDropListener> gravityDropListeners;
    private boolean canHold;

    public Tetris() {

        pieceQueue = Collections.synchronizedList(new LinkedList<>());
        nextAction = new SynchronizedTetrisAction();
        targetTime = new AtomicLong(System.currentTimeMillis() + 500L);
        pieceQueue = new LinkedList<>();
        actionExecutionListeners = new HashSet<>();
        gravityDropListeners = new HashSet<>();
        canHold = true;
        heldPiece = null;
        isGameRunning = new AtomicBoolean(true);
        softDropDelay = new AtomicLong(500L);
        display = new GridDisplay();
        currentPiece = drawFromPieceQueue();
        requestHandler = new Thread(() -> {
            while (isGameRunning.get()) {
                if (System.currentTimeMillis() >= targetTime.get()) {
                    resetLockDelay();
                    if (super.softDropTetromino()) {
                        fuseCurrentPiece();
                        canHold = true;
                    }
                }
                TetrisAction next = nextAction.getAction();
                if (next != null) {
                    switch (next) {
                        case HOLD:
                            if (canHold) {
                                Tetromino.Type currentType = currentPiece.getType();
                                currentPiece = (heldPiece != null) ? new Tetromino(heldPiece) : drawFromPieceQueue();
                                heldPiece = currentType;
                                canHold = false;
                                resetLockDelay();
                            }
                            break;
                        case HARD_DROP:
                            super.hardDropTetromino();
                            fuseCurrentPiece();
                            canHold = true;
                            resetLockDelay();
                            break;
                        case SHIFT_LEFT:
                            super.moveTetrominoLeft();
                            resetLockDelay();
                            break;
                        case ROTATE_LEFT:
                            super.rotateTetrominoLeft();
                            resetLockDelay();
                            break;
                        case SHIFT_RIGHT:
                            super.moveTetrominoRight();
                            resetLockDelay();
                            break;
                        case ROTATE_RIGHT:
                            super.rotateTetrominoRight();
                            resetLockDelay();
                            break;
                    }
                }
                display.calculateDisplayColors(board, currentPiece);
                clearLines();
            }
        });
        requestHandler.start();
    }

    @Override
    public void moveTetrominoLeft() {
        nextAction.setAction(TetrisAction.SHIFT_LEFT);
    }

    @Override
    public void moveTetrominoRight() {
        nextAction.setAction(TetrisAction.SHIFT_RIGHT);
    }

    public void startSoftDrop() {
        softDropDelay.set(25L);
    }

    public void stopSoftDrop() {
        softDropDelay.set(500L);
    }

    @Override
    public void hardDropTetromino() {
        nextAction.setAction(TetrisAction.HARD_DROP);
    }

    @Override
    public boolean rotateTetrominoRight() {
        nextAction.setAction(TetrisAction.ROTATE_RIGHT);
        return false;
    }

    @Override
    public boolean rotateTetrominoLeft() {
        nextAction.setAction(TetrisAction.ROTATE_LEFT);
        return false;
    }

    public void hold() {
        nextAction.setAction(TetrisAction.HOLD);
    }

    private Tetromino drawFromPieceQueue() {
        while (pieceQueue.size() < 4) {
            pieceQueue.add(generateNextTetromino());
        }
        Tetromino newTetromino = new Tetromino(pieceQueue.get(0));
        pieceQueue.remove(0);
        while (pieceQueue.size() < 4) {
            pieceQueue.add(generateNextTetromino());
        }
        return newTetromino;
    }

    public boolean isRunning() {
        return isGameRunning.get();
    }

    @Override
    protected void fuseCurrentPiece() {
        super.fuseCurrentPiece();
        currentPiece = drawFromPieceQueue();
    }

    private void notifyAllActionListeners(TetrisAction action) {
        for (ActionExecutionListener listener : actionExecutionListeners) {
            listener.handleActionExecution(action);
        }
    }

    private void notifyAllGravityDropListeners() {
        for (GravityDropListener listener : gravityDropListeners) {
            listener.handleGravityDrop();
        }
    }

    private void resetLockDelay() {
        targetTime.set(System.currentTimeMillis() + softDropDelay.get());
    }

    public void addKeyListener(KeyListener listener) {
        display.getHolder().addKeyListener(listener);
    }

}
