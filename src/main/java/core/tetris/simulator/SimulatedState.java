package core.tetris.simulator;

import core.tetris.GameInstance;
import core.tetris.game.Tetromino;

public class SimulatedState extends GameInstance {

    private boolean resolved;

    public SimulatedState(boolean[][] board, Tetromino currentPiece) {
        super(board);
        this.currentPiece = currentPiece;
        resolved = false;
    }

    @Override
    public boolean softDropTetromino() {
        if (super.softDropTetromino()) {
            fuseCurrentPiece();
            resolved = true;
        }
        return false;
    }

    @Override
    public void hardDropTetromino() {
        super.hardDropTetromino();
        fuseCurrentPiece();
        resolved = true;
    }



}
