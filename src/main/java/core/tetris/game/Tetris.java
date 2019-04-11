package core.tetris.game;

import core.tetris.GameInstance;
import core.tetris.GridDisplay;

import java.awt.*;
import java.awt.event.KeyListener;
import java.util.*;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class Tetris extends GameInstance {

    private Thread requestHandler;
    private AtomicBoolean isSoftDropping;
    private GridDisplay display;
    private Tetromino.Type heldPiece;
    private List<Tetromino.Type> pieceQueue;
    private SynchronizedTetrisAction nextAction;
    private AtomicBoolean isGameRunning;
    private Set<ActionExecutionListener> actionExecutionListeners;
    private Set<GravityDropListener> gravityDropListeners;
    private TetrisTimer timer;
    private boolean canHold;

    public Tetris() {

        pieceQueue = Collections.synchronizedList(new LinkedList<>());
        nextAction = new SynchronizedTetrisAction();
        isSoftDropping = new AtomicBoolean(false);
        pieceQueue = new LinkedList<>();
        actionExecutionListeners = new HashSet<>();
        gravityDropListeners = new HashSet<>();
        canHold = true;
        heldPiece = null;
        isGameRunning = new AtomicBoolean(true);
        display = new GridDisplay();
        currentPiece = drawFromPieceQueue();
        timer = new TetrisTimer();
        requestHandler = new Thread(() -> {
            timer.resetAll();
            while (isGameRunning.get()) {
                if (!canFuseOnSoftDrop()) {
                    timer.resetFuseTimer();
                }
                else {
                    if (timer.checkFuseTimer()) {
                        timer.resetAll();
                        canHold = true;
                        display.calculateDisplayColors(board, currentPiece);
                        fuseCurrentPiece();
                    }
                }
                if ((timer.checkGravityTimer() && !isSoftDropping.get()) || timer.checkDropTimer() && isSoftDropping.get()) {
                    softDropTetromino();
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
                            }
                            break;
                        case HARD_DROP:
                            super.hardDropTetromino();
                            display.calculateDisplayColors(board, currentPiece);
                            fuseCurrentPiece();
                            canHold = true;
                            break;
                        case SHIFT_LEFT:
                            super.moveTetrominoLeft();
                            break;
                        case ROTATE_LEFT:
                            super.rotateTetrominoLeft();
                            break;
                        case SHIFT_RIGHT:
                            super.moveTetrominoRight();
                            break;
                        case ROTATE_RIGHT:
                            super.rotateTetrominoRight();
                            break;
                    }
                    timer.resetFuseTimer();
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
        if (isSoftDropping.get()) {
            softDropTetromino();
        }
        isSoftDropping.set(true);
    }

    public void stopSoftDrop() {
        isSoftDropping.set(false);
    }

    @Override
    public void hardDropTetromino() {
        nextAction.setAction(TetrisAction.HARD_DROP);
    }

    @Override
    public void rotateTetrominoRight() {
        nextAction.setAction(TetrisAction.ROTATE_RIGHT);
    }

    @Override
    public void rotateTetrominoLeft() {
        nextAction.setAction(TetrisAction.ROTATE_LEFT);
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

    public void addKeyListener(KeyListener listener) {
        display.getHolder().addKeyListener(listener);
    }

    @Override
    protected int clearLines() {
        int lineCount = 0;
        for (int y = 0; y < BOARD_HEIGHT + BOARD_BUFFER; y++) {
            if (isLineAtY(y)) {
                shiftDownAtY(y);
                lineCount++;
                y--;
            }
        }
        if (lineCount > 0) {
            System.out.println(lineCount);
        }
        return lineCount;
    }

    @Override
    protected void shiftDownAtY(int y) {
        clearLineAtY(y);
        Color[][] colors = display.getDisplayColors();
        for (int lineMove = y; lineMove < BOARD_HEIGHT + BOARD_BUFFER - 1; lineMove++) {
            for (int x = 0; x < BOARD_WIDTH; x++) {
                board[x][lineMove] = board[x][lineMove + 1];
                board[x][lineMove + 1] = false;
                if (lineMove < BOARD_HEIGHT - 1) {
                    if (board[x][lineMove]) {
                        colors[x][lineMove] = colors[x][lineMove + 1];
                    }
                    else {
                        colors[x][lineMove] = Color.WHITE;
                    }
                } else if (lineMove < BOARD_HEIGHT) {
                    colors[x][lineMove] = Color.WHITE;
                }
            }
        }
        clearLineAtY(BOARD_HEIGHT + BOARD_BUFFER - 1);
        display.setDisplayColors(colors);
    }

}
